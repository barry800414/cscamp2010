/**
 * AIRunner
 * This class wraps a AI, and runs it in a safe environment,
 * which prevents from hanging other parts of program.
 */
public class AIRunner {
	private String classname;
	private AI ai_instance;
	private Thread ai_thread;
	private boolean finished = true;
	
	/** Init with class name */
	public AIRunner(String classname) {
		try {
			this.classname = classname;
			ai_instance = (AI)(Class.forName(classname).newInstance());
		} catch(ClassNotFoundException e) {
			System.out.println("AI Class: " + classname + " not found.");
			e.printStackTrace();
		} catch(Exception e) {
			System.out.println("AI Class: " + classname + " unable to load.");
			e.printStackTrace();
		}
	}
	
	/** Init with a AI instance */
	public AIRunner(AI ai) {
		if(ai != null) {
			this.classname = ai.getClass().toString();
			this.ai_instance = ai;
		} else {
			throw new NullPointerException();
		}
	}
	
	/** Get the instance of the AI */
	public AI getAIInstance() {
		return ai_instance;
	}
	
	/** Returns true if the AI had finished running */
	public synchronized boolean isFinished() {
		return finished;
	}
	
	/** The thread will use this method to report its state */
	public synchronized void setFinished(boolean finished) {
		this.finished = finished;
	}

	/** Stop AI thread if exist any */
	public void stop() {
		if(!isFinished()) {
			try {
				ai_thread.interrupt();
				setFinished(true);
			} catch(SecurityException e) {
				System.out.println("AI Class: " + classname + " access denied and cannot stop.");
			}
		}
	}

	/**
	 * Create a new AI thread and run.
	 * NOTE: ONLY when no existing thread running.
	 */
	public void run() {
		if(isFinished()) {
			try {
				final AIRunner that = this;
				that.setFinished(false);
				ai_thread = new Thread(new Runnable() {
					public AI ai = that.getAIInstance();
					public void run() {
						ai.run();
						that.setFinished(true);
					}
				});
				ai_thread.start();
			} catch(IllegalThreadStateException e) {
				System.out.println("AI Class: " + classname);
				e.printStackTrace();
				setFinished(true);
			}
		}
	}
}
