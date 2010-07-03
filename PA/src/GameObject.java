
public class GameObject {
	public static final double[] DIRECTION_DEGREE = {0, 0, 45, 90, 135, 180, 225, 270, 315};

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
		double rad = 0.0;
		if(dirX == 0.0) {
			// don't care if (speed == 0)
			rad = Math.PI / 2 * ((dirY > 0.0) ? 3 : 1);
		} else {
			rad = Math.atan2(-dirY, dirX) + Math.PI;
		}
		if(rad >= Math.PI*2) rad -= Math.PI*2;
		return rad;
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
			direct = 1 + (int)Math.round((getDirectRad() / Math.PI / 2) * 8) % 8;
		} else {
			dirX = 0.0;
			dirY = 0.0;
			direct = 0;
		}
	}
	
	/** set direction 0~8: 0(stop), 1-8(0-2pi). */
	public void setDir(int direct) {
		if(direct == 0) {
			this.direct = 0;
			setDir(0.0, 0.0);
		}
		else if(direct >= 1 && direct <= 8) {
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
