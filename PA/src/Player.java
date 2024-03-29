import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Set;

public class Player extends GameObject {
	public static final double	BASE_SPEED = 200.0;
	public static final int		BASE_LIFE = 3;
	
	private int							id;
	private String						name;
	private int							life;
	private boolean						silence;
	private boolean						alive;
	private Hashtable<Skill, Integer>	skill_quota;
	private ArrayList<Effect>			effects;
	private int[]						in_state;
	private AIRunner					ai_runner;
	private int							time_score;
	private int							extra_score;
	private long						time_die;
	
	public Player(Game game) {
		super(game);
		
		id = 0;
		life = BASE_LIFE;
		silence = false;
		alive = true;
		skill_quota = new Hashtable<Skill, Integer>();
		effects = new ArrayList<Effect>();
		in_state = new int[Effect.EFFECT_MAX_ID + 1];
		ai_runner = null;
		time_score = 0;
		extra_score = 0;
		time_die = 0;
		
		updateStatus();
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int newid) {
		id = newid;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public int getLife() {
		return life;
	}
	
	public boolean isSilence() {
		return silence;
	}
	
	public void setSilence(boolean silence) {
		this.silence = silence;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public synchronized void gainScore(int s) {
		extra_score += s;
	}
	
	public void updateTimeScore() {
		if(isAlive())
			time_score = (int)( (double)game.getCurrentEventTime() * Updater.SCORE_ALIVE_TIME_RATIO );
	}
	
	public int getScore() {
		return extra_score + time_score;
	}
	
	public synchronized void resetScore() {
		extra_score = 0;
		time_score = 0;
	}
	
	public long getTimeDied() {
		return time_die;
	}
	
	public void notifyEndOfGame() {
		if(isAlive()) {
			time_die = game.getCurrentEventTime();
		}
	}
	
	public void useSkill(Skill skill, Player target) {
		if(!isSilence()) {
			int quota = getSkillQuota(skill);
			if(quota > 0) {
				if(!skill.canSetTarget()) target = this;
				// Do quota change before using skill
				setSkillQuota(skill, quota - 1);
				skill.use(game, this, target);
				
				System.out.println("Player [" + this + "] used skill " + skill + " on player [" + target + "].");
			} else {
				System.out.println("Player [" + this + "]: don't have skill " + skill + ".");
			}
		}
	}
	
	public void setSkillQuota(Skill skill, int quota) {
		/* The key in the hashtable must be removed if (quota == 0). */
		if(quota > 0) {
			skill_quota.put(skill, quota);
		} else {
			skill_quota.remove(skill);
		}
	}
	
	public int getSkillQuota(Skill skill) {
		Integer quota = skill_quota.get(skill);
		if(quota == null)
			return 0;
		else
			return quota.intValue();
	}
	
	/** Get all skills that the player owns in an array */
	public Skill[] getAllSkills() {
		Set<Skill> skills = skill_quota.keySet();
		return skills.toArray(new Skill[0]);
	}
	
	public synchronized void addEffect(Effect effect) {
		System.out.println("Player [" + this + "]: effect [" + effect + "] onSet");
		effects.add(effect);
		effect.onSet();
		in_state[effect.getId()]++;
	}
	
	public synchronized void removeEffect(Effect effect) {
		System.out.println("Player [" + this + "]: effect [" + effect + "] onRemove");
		in_state[effect.getId()]--;
		effect.onRemove();
		effects.remove(effect);
	}
	
	public Effect[] getAllEffects() {
		return effects.toArray(new Effect[0]);
	}
	
	public boolean hasState(int id) {
		if(id >= 0 && id <= Effect.EFFECT_MAX_ID)
			return (in_state[id] > 0);
		else
			return false;
	}
	
	@Override
	public Damage applyDamage(Damage damage) {
		if(alive) {
			for(Effect eff : getAllEffects()) {
				eff.onDamage(damage);
			}
			
			life += damage.life;
			
			if(damage.life < 0) {
				System.out.println("Player ["+this+"]: damaged, life left: "+life);
			}
			
			if(life <= 0) {
				alive = false;
				time_die = game.getCurrentEventTime();
			} else {
				if(damage.life < 0) {
					// Make us unvulnerable for a small time
					addEffect(new EffectUnvulnerable(game, this));
				}
			}
			
			return damage;
		} else {
			return Damage.newWithLife(0);
		}
	}
	
	/**
	 * Update the status of the player:
	 * Reset speed, silence state, etc.
	 * And iterate all effects and invoke onEffect().
	 */
	public void updateStatus() {
		// Reset information
		setSpeed(BASE_SPEED);
		
		for(Effect eff : getAllEffects()) {
			eff.onEffect();
		}
	}
	
	/** Returns assigned AI, null if not exist. */
	public AI getAI() {
		return (ai_runner != null) ? ai_runner.getAIInstance() : null;
	}
	
	/** Returns the AIRunner, null if not exist. */
	public AIRunner getAIRunner() {
		return ai_runner;
	}
	
	/** Assigns a AI to player, null to remove. */
	public void setAI(AI ai) {
		killAI();
		if(ai != null) {
			this.ai_runner = new AIRunner(ai);
		}
	}
	
	public void setAI(AIRunner runner) {
		killAI();
		this.ai_runner = runner;
	}
	
	/** Kill the AI runner */
	public void killAI() {
		// Stop and kill the AIRunner if exist
		if(ai_runner != null) {
			ai_runner.stop();
			ai_runner = null;
		}
	}
	
	@Override
	public String toString() {
		if(ai_runner.getAIInstance() != null)
			return ai_runner.getAIInstance().toString();
		return this.getClass().getName();
	}
}
