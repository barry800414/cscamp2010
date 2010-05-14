
public class Bullet extends GameObject {
	private Player owner;
	
	public Bullet(Game game) {
		super(game);
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
	public void setDirection(Player target) {
		// TODO: implementation
	}
}
