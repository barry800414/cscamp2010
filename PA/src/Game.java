
public class Game {
	private GameInfo		info;
	private GameQueue		queue;
	private Updater			updater;
	private GraphicsEngine	gengine;
	
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
	
	/** Get the time in millisecond after game starts */
	public int getTime() {
	}
	
	/**
	 * The main loop of the game:
	 * get the next event in the queue and fire it at correct time.
	 */
	public void mainLoop() {
	}
	
	public static void main(String[] args) {
		new Game().mainLoop();
	}
}
