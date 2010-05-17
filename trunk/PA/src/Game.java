import java.util.Random;
import java.util.Calendar;

public class Game {
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
		while(true) {
			Event ev = queue.peekNext();
			if(ev != null) {
				if(getTime() >= ev.getTime()) {
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
		new Game().startGame();
	}
}
