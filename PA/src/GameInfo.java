import java.util.ArrayList;

public class GameInfo {
	private ArrayList<Player> players;
	private ArrayList<Bullet> bullets;
	private ArrayList<Animation> animations;
	
	/** Constructor */
	public GameInfo() {
		players = new ArrayList<Player>();
		bullets = new ArrayList<Bullet>();
		animations = new ArrayList<Animation>();
	}
	
	public int getNumPlayers() {
		return players.size();
	}

	public int getNumBullets() {
		return bullets.size();
	}

	public int getNumAnimations() {
		return animations.size();
	}
	
	public Player getPlayer(int index) {
		if(index >= 0 && index < getNumPlayers())
			return players.get(index);
		else
			return null;
	}

	public Bullet getBullet(int index) {
		if(index >= 0 && index < getNumBullets())
			return bullets.get(index);
		else
			return null;
	}

	public Animation getAnimation(int index) {
		if(index >= 0 && index < getNumAnimations())
			return animations.get(index);
		else
			return null;
	}
	
	public void addPlayer(Player p) {
		players.add(p);
	}

	public void addBullet(Bullet b) {
		bullets.add(b);
	}

	public void addAnimation(Animation ani) {
		animations.add(ani);
	}
	
	public void removePlayer(Player p) {
		players.remove(p);
	}
	
	public void removeBullet(Bullet b) {
		bullets.remove(b);
	}
	
	public void removeAnimation(Animation ani) {
		animations.remove(ani);
	}
}
