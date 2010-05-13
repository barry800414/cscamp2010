/**
 * Animation
 * This class stores a animation.
 * The graphics engine will use the information provided (eg. getType)
 * to draw animation on screen.
 */
public abstract class Animation {
	/** The type id of the animation */
	public abstract int getType();
	
	/** The x-coordinate to draw animation on screen */
	public abstract double getTargetX();
	
	/** The y-coordinate to draw animation on screen */
	public abstract double getTargetY();
	
	/** The time passed after animation start */
	public abstract int getTime();
}
