
import java.util.Random;
import java.util.Calendar;

/**
 * The Game class is a full built class that can new a instance to run the game
 * @author mini_ming,rock1246,robertabcd
 *
 */
public class Game {
	/** For QoS purpose. */ 
	public static final long EVENT_MAX_DELAY = 20;
	
	public final Random random = new Random();
	
	private GameInfo		info;
	private GameQueue		queue;
	private Updater			updater;
	private GraphicsEngine	gengine;
	
	private long			time_game_start;
	
	private Event			curr_event;
	
	/** Initializer */
	public Game() {
		info = new GameInfo();
		queue = new GameQueue();
		updater = new Updater(this);
		gengine = new GraphicsEngine(this);
	}
	
	public GameInfo getGameInfo() {
		return info;
	}
	
	public GameQueue getGameQueue() {
		return queue;
	}
	
	public GraphicsEngine getGraphicsEngine() {
		return gengine;
	}
	
	/** This method does the init of all components, and run the main loop. */
	public void startGame() {
		time_game_start = Calendar.getInstance().getTimeInMillis();
		
		updater.init();
		gengine.init();
		
		// Drop into the main loop
		mainLoop();
	}
	
	/** Get the time in millisecond after game starts */
	public long getTime() {
		return Calendar.getInstance().getTimeInMillis() - time_game_start;
	}
	
	/** Get the current event, null if first event. */
	public Event getCurrentEvent() {
		return curr_event;
	}
	
	/**
	 * Get the current event time, returns getTime() if not exist a current event.
	 * This method is designed for time-sensitive repeating events. 
	 */
	public long getCurrentEventTime() {
		if(curr_event != null)
			return curr_event.getTime();
		else
			return getTime();
	}
	
	/**
	 * The main loop of the game:
	 * get the next event in the queue and fire it at correct time.
	 */
	public void mainLoop() {
		long time_delta;
		
		while(true) {
			Event ev = queue.peekNext();
			if(ev != null) {
				time_delta = ev.getTime() - getTime();
				if(time_delta <= 0) {
					if(time_delta < -EVENT_MAX_DELAY) {
						System.out.println("Game: QoS, event late: " + time_delta);
					}
					// Dequeue the event and fire it
					curr_event = queue.getNext();
					curr_event.action();
					curr_event = null;
				} else {
					try {
						Thread.sleep(time_delta);
					} catch(InterruptedException e) {
						// End of the world
						return;
					}
				}
			} else {
				// No events left, the end of the world
				return;
			}
		}
	}
	
	public static void main(String[] args) {
		final int BULLETS = 100;
		
		Game g = new Game();
		
		final Player p = new Player(g);
		p.setLoc(0.0, 300.0);
		p.setDir(1);
		p.setId(1);
		p.setName("Human player");
		for(int i = 1; i <= 10; i++) {
			p.setSkillQuota(Skill.skillFromId(i), 1);
		}
		p.setAI(new AIHuman(g));

		final Effect eff = new EffectFrozen(g, p);
		final long time_eff = 5000;
		Event ev = new Event() {
			public long getTime() { return time_eff; }
			public void action() { System.out.println("Event: player frozen"); p.addEffect(eff); }
		};
		g.getGameQueue().addEvent(ev);
		
		g.getGameInfo().addPlayer(p);
		
		Bullet[] b = new Bullet[BULLETS];
		for(int i = 0; i < BULLETS; i++) {
			b[i] = new Bullet(g);
			b[i].setLoc(g.getGameInfo().getWidth() * g.random.nextDouble(), g.getGameInfo().getHeight() * g.random.nextDouble());
			b[i].setDir(g.random.nextDouble() - 0.5, g.random.nextDouble() - 0.5);
			//b[i].setSpeed(1.0);
			g.getGameInfo().addBullet(b[i]);
		}
		
		g.startGame();
	}
}
