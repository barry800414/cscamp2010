/**
 * Event
 * All game event is put in the game queue, and
 * the queue should guarantee firing action()
 * method on time.
 */
public interface Event {
	/** The time in millisecond after game starts that this event should happen */
	int getTime();
	
	/** What should the event do */
	void action();
}
