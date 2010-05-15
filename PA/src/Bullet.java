
public class Bullet extends GameObject {
	public static double BASE_SPEED = 50.0;
	
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
	
	/** Set the bullet to target a player with little miss (change direction once). */
	public void setDirection(Player target, double miss) {
		double x = target.getDirX(), y = target.getDirY();
		setDir(
				x - getDirX() + 2*(game.random.nextDouble() - 0.5)*miss,
				y - getDirY() + 2*(game.random.nextDouble() - 0.5)*miss
		);
	}
	
	@Override
	public Bullet clone() {
		Bullet copy = new Bullet(game, locX, locY, dirX, dirY, speed, owner);
		return copy;
	}
}
