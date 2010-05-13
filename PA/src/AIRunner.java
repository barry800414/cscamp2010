/**
 * AIRunner
 * This class wraps a AI, and runs it in a safe environment,
 * which prevents from hanging other parts of program.
 */
public class AIRunner {
	/** Init with class name */
	public AIRunner(String classname) {
	}
	
	/** Get the instance of the AI */
	public AI getAIInstance() {
	}
	
	/** Returns true if the AI had finished running */
	public boolean isFinished() {
	}
	
	/** Suspend AI thread if exist any */
	public void suspend() {
	}

	/** Stop AI thread if exist any */
	public void stop() {
	}

	/**
	 * Create a new AI thread and run.
	 * NOTE: ONLY when no existing thread running.
	 */
	public void run() {
	}
}
