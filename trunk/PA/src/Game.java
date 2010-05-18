import java.util.Random;
import java.util.Calendar;

public class Game {
	/** For QoS purpose. */ 
	public static final long EVENT_MAX_DELAY = 20;
	
	public final Random random = new Random();
	
	private GameInfo		info;
	private GameQueue		queue;
	private Updater			updater;
	private GraphicsEngine	gengine;
	
	private long			time_game_start;
	
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
					queue.getNext().action();
				} else {
					try {
						Thread.sleep(ev.getTime() - getTime());
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
		p.setDir(0);
		p.setId(1);

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
