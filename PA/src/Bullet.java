
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
}
