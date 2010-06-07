
public class Bullet extends GameObject {
	public static double BASE_SPEED = 70.0;
	
	private Player owner;
	
	public Bullet(Game game) {
		super(game);
		setSpeed(BASE_SPEED);
	}
	
	public Bullet(Game game, double locX, double locY, double dirX, double dirY, double speed, Player owner) {
		this(game);
		setLoc(locX, locY);
		setDir(dirX, dirY);
		setSpeed(speed);
		setOwner(owner);
	}
	
	public Player getOwner() {
		return owner;
	}
	
	public void setOwner(Player p) {
		owner = p;
	}
	
	@Override
	public void applyDamage(Damage d) {
		if(d.life < 0) {
			game.getGameInfo().removeBullet(this);
		}
	}
	
	/** Set the bullet to target a game object with little miss (change direction once). */
	public void setDirection(GameObject target, double miss) {
		double x = target.getLocX(), y = target.getLocY();
		setDir(
				x - getLocX() + 2*(game.random.nextDouble() - 0.5)*miss,
				y - getLocY() + 2*(game.random.nextDouble() - 0.5)*miss
		);
	}
	
	@Override
	public Bullet clone() {
		Bullet copy = new Bullet(game, locX, locY, dirX, dirY, speed, owner);
		return copy;
	}
}
