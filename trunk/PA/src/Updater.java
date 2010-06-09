import java.util.Enumeration;
import java.util.Hashtable;
import java.lang.Math;
import java.util.Random;

/**
 * Updater
 * Game Logic: move players, bullets, and detect collisions.
 * Generates game events.
 * Controls AI: threads, etc.
 */
public class Updater {
	/** The time in millisecond that the world should update. */
	public static final long UPDATE_PERIOD = 1000 / 60;
	/** The time in millisecond that AI should make decision. */
	public static final long UPDATE_AI_PERIOD = 100;
	/** This constant is not in use now, due to we have set the speed to pixel/sec. */
	public static final double SPEED_CORRECTION = 1.0; // Always set to 1.0
	
	private Game game;
	private GameInfo info;
	
	private long last_update;
	private long since_last_update;
	
	private Hashtable<Player, Skill> skill_used_last_ai_update;
	
	public Updater(Game game) {
		this.game = game;
		this.info = game.getGameInfo();
		
		this.skill_used_last_ai_update = new Hashtable<Player, Skill>();
	}
	
	/** Generate first world update event and AI event. */
	public void init() {
		last_update = game.getTime();
		
		// Generate initial events
		update();
		decideAI();
	}
	
	/** Update the world and detect collision. */
	public void update() {
		/* We need to record time now, due to moving objects and
		 * detecting collisions may take some time. It is to avoid
		 * time measuring errors.
		 */
		long time_now = game.getTime();
		since_last_update = time_now - last_update;
		
		movePlayers();
		moveBullets();
		detectCollisions();
		setBullet();
		
		last_update = time_now;
		enqueueNextUpdate();
	}
	
	private void movePlayers() {
		for(Player p : info.getAllPlayers()) {
			if(p.isAlive()) {
				p.updateStatus();
				moveGameObject(p);
				
				// check if out of bounds
				double x = p.getLocX(), y = p.getLocY();
				if(x < 0) x = 0;
				if(x > info.getWidth()) x = info.getWidth();
				if(y < 0) y = 0;
				if(y > info.getHeight()) y = info.getHeight();
				p.setLoc(x, y);
			}
		}
	}
	
	private void moveBullets() {
		for(Bullet b : info.getAllBullets()) {
			moveGameObject(b);
		}
	}
	
	private void setBullet() {
		// target a Bullet to a GameObject, use: bullet.setDirection(GameObject, miss)
		Bullet bullet[] = info.getAllBullets();
		Player player[] = info.getAllPlayers();
		final double BULLET_SPACING = -30.0;
		double w = info.getWidth(), h = info.getHeight();
		Random random = new Random();
		
		for( int i = 0 ; i < bullet.length ; i++ )
		{
			if( bullet[i].getLocX() < BULLET_SPACING || bullet[i].getLocX() > w ||
					bullet[i].getLocY() < BULLET_SPACING || bullet[i].getLocY() > h  )
			{
				bullet[i].setDirection( player[random.nextInt(player.length)] , random.nextInt(100) );
			}
		}
	}
	
	private void detectCollisions() {
		Bullet bullet[] = info.getAllBullets();
		Player player[] = info.getAllPlayers();
		//bullet hit player
		for( int i = 0 ; i < bullet.length ; i++ )
		{
			for( int j = 0 ; j < player.length ; j++ )
			{
				if(!player[j].isAlive()) continue;
				
				double pradius = GraphicsEngine.PLAYER_SIZE/2;
				double bradius = GraphicsEngine.BULLET_SIZE/2;
				double distance = Math.sqrt(Math.pow(bullet[i].locX - player[j].locX,2)+ 
						                    Math.pow(bullet[i].locY - player[j].locY,2));
				if( distance <  pradius+bradius )
				{
					bullet[i].applyDamage( Damage.newWithLife(-1) );
					player[j].applyDamage( Damage.newWithLife(-1) );
					System.out.println( "player("+j+")was hitted, hp:"+player[j].getLife() );
					//ufo's dead is not complete
				}
			}
		}
		//players collide each other
		for( int i = 0 ; i < player.length ; i++ )
			for( int j = i+1 ; j < player.length ; j++ )
			{
				double pradius = GraphicsEngine.PLAYER_SIZE;
				double distance = Math.sqrt(Math.pow(player[i].locX - player[j].locX,2)+ 
											Math.pow(player[i].locY - player[j].locY,2));
				if( distance < pradius )
				{
					player[i].applyDamage( Damage.newWithLife(-1) );
					player[j].applyDamage( Damage.newWithLife(-1) );
					System.out.println( "player("+i+") and player("+j+") collide each other, hp:"+player[i].getLife()+","+player[j].getLife() );
				}
			}
	}
	
	private void moveGameObject(GameObject obj) {
		double length = SPEED_CORRECTION * obj.getSpeed() * since_last_update / 1000;
		obj.setLoc(obj.getLocX() + obj.getDirX() * length, obj.getLocY() + obj.getDirY() * length);
	}
	
	private void enqueueNextUpdate() {
		final long next_update = game.getTime() + UPDATE_PERIOD;
		final Updater updater = this;
		
		game.getGameQueue().addEvent(new Event() {
			public long getTime() { return next_update; }
			public void action() { updater.update(); }
		});
	}

	/**
	 * Update AI's game information, and let AI to start to decide.
	 * Also enqueue updateAI() event.
	 */
	public void decideAI() {
		final long time_to_update = game.getCurrentEventTime() + UPDATE_AI_PERIOD;
		
		// Update all AI state
		for(Player p : info.getAllPlayers()) {
			if(p.isAlive()) {
				AI ai = p.getAI();
				AIRunner runner = p.getAIRunner();
				// Only run AI if it is finished
				if(ai != null && runner.isFinished()) {
					ai.resetInternalState();
					ai.updateGameInfo(game, p);
					
					// Notify AI if a player used a skill
					for(Enumeration<Player> e = skill_used_last_ai_update.keys(); e.hasMoreElements(); ) {
						Player player = e.nextElement();
						Skill skill = skill_used_last_ai_update.get(player);
						if(skill != null) {
							ai.onPlayerSkillUsage(player, skill);
						}
					}
					
					// Run AI
					runner.run();
				} // if has ai and finished
			} // if isAlive()
		}
		
		// Clear the skill usage table on each decide turn
		skill_used_last_ai_update.clear();
		
		// Enqueue updateAI() event
		final Updater updater = this;
		game.getGameQueue().addEvent(new Event() {
			public long getTime() { return time_to_update; }
			public void action() { updater.updateAI(); }
		});
	}
	
	/**
	 * Check all AIs' state and do what they want if they're finished.
	 * Also calls decideAI().
	 */
	public void updateAI() {
		// Update all AI state
		for(Player p : info.getAllPlayers()) {
			if(p.isAlive()) {
				AI ai = p.getAI();
				AIRunner runner = p.getAIRunner();
				// Only do AI action if it is finished
				if(ai != null && runner.isFinished()) {
					// Update direction
					p.setDir(ai.getMove());
					
					// Check if using a skill
					if(ai.isUsingSkill()) {
						Skill skill = ai.getSkill();
						if(p.getSkillQuota(skill) > 0) {
							Player target = info.getPlayerFromId(ai.getTarget());
							if(!skill.canSetTarget() || target == null) {
								target = p;
							}
							
							// Record it and use it
							skill_used_last_ai_update.put(p, skill);
							p.useSkill(skill, target);
						}
					}
				} // if has ai and finished
			} // if isAlive()
		}
		
		// Make AI into decide mode
		decideAI();
	}
}
