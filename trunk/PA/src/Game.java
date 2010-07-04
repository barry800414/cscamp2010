
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
	
	private boolean			game_end;
	
	/** Initializer */
	public Game() {
		info = new GameInfo();
		queue = new GameQueue();
		updater = new Updater(this);
		gengine = new GraphicsEngine(this);
		
		game_end = false;
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
		
		// When ends
		printGameStatistics();
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
	
	public void notifyEndOfGame() {
		//game_end = true;
		for(Player p : info.getAllPlayers()) {
			p.notifyEndOfGame();
		}
	}
	
	public void printGameStatistics() {
		System.out.println("=== Game Statistic ===");
		for(Player p : info.getAllPlayers()) {
			System.out.println("Player ["+p+"]: alive for "+p.getTimeDied()+" ms, score = "+p.getScore());
		}
		System.out.println("======================");
	}
	
	/**
	 * The main loop of the game:
	 * get the next event in the queue and fire it at correct time.
	 */
	public void mainLoop() {
		long time_delta;
		
		while(!game_end) {
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
}
