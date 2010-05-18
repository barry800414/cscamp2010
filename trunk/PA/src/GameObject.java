
public class GameObject {
	public static final double[] DIRECTION_DEGREE = {0, 45, 90, 135, 180, 225, 270, 315};

	protected Game game;
	protected double locX, locY, dirX, dirY, speed;
	protected int direct;
	
	public GameObject(Game game) {
		this.game = game;
	}
	
	public double getLocX() {
		return locX;
	}
	
	public double getLocY() {
		return locY;
	}
	
	public double getDirX() {
		return dirX;
	}
	
	public double getDirY() {
		return dirY;
	}
	
	public int getDirect() {
		return direct;
	}
	
	/** Returns the correct radius from 0~2pi */
	public double getDirectRad() { 
		if(dirX == 0.0) {
			// don't care if (speed == 0)
			return Math.PI / 2 * ((dirY > 0.0) ? 3 : 1);
		} else {
			return Math.atan2(-dirY, dirX) + Math.PI;
		}
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public void setLoc(double x, double y) {
		locX = x;
		locY = y;
	}
	
	/** Set the direction of a game object. Will convert to unit vector. */
	public void setDir(double x, double y) {
		double length = Math.sqrt(x*x + y*y);
		if(length > 0.0) {
			dirX = x / length;
			dirY = y / length;
			
			// Set this.direct to appropriate value
			direct = (int)Math.round((getDirectRad() * 180 / Math.PI) / (360 / 8));
		}
	}
	
	/** set direction 0~7 */
	public void setDir(int direct) {
		if(direct >= 0 && direct <= 7) {
			double rad = Math.PI * DIRECTION_DEGREE[direct] / 180;
			dirX = Math.cos(rad);
			dirY = -Math.sin(rad); // y-coordinate on screen is different
			this.direct = direct;
		}
	}
	
	public void setSpeed(double v) {
		speed = v;
	}
	
	public void applyDamage(Damage d) {
	}
	
	/** Test if it is near another object, range is a square. */
	public boolean near(GameObject other, double range) {
		double ox = other.getLocX(), oy = other.getLocY();
		if(ox >= locX - range && ox <= locX + range && oy >= locY - range && oy <= locY + range) {
			return true;
		} else {
			return false;
		}
	}
}
