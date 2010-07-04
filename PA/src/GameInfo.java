import java.util.ArrayList;
import java.util.Hashtable;

public class GameInfo {
	public static final double FIELD_WIDTH = 800.0, FIELD_HEIGHT = 600.0;
	
	private ArrayList<Player> players;
	private Hashtable<Integer, Player> hash_id_player;
	private ArrayList<Bullet> bullets;
	private ArrayList<Animation> animations;
	
	/** Constructor */
	public GameInfo() {
		players = new ArrayList<Player>();
		hash_id_player = new Hashtable<Integer, Player>();
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
	
	/** Get player according to its id, null if not found. */
	public Player getPlayerFromId(int id) {
		return hash_id_player.get(id);
	}
	
	public Player[] getAllPlayers() {
		return players.toArray(new Player[0]);
	}

	public Bullet getBullet(int index) {
		if(index >= 0 && index < getNumBullets())
			return bullets.get(index);
		else
			return null;
	}

	public Bullet[] getAllBullets() {
		return bullets.toArray(new Bullet[0]);
	}
	
	public Animation getAnimation(int index) {
		if(index >= 0 && index < getNumAnimations())
			return animations.get(index);
		else
			return null;
	}

	public Animation[] getAllAnimations() {
		return animations.toArray(new Animation[0]);
	}
	
	public void addPlayer(Player p) {
		players.add(p);
		hash_id_player.put(p.getId(), p);
	}

	public void addBullet(Bullet b) {
		bullets.add(b);
	}

	public void addAnimation(Animation ani) {
		animations.add(ani);
	}
	
	public void removePlayer(Player p) {
		players.remove(p);
		hash_id_player.remove(p.getId());
	}
	
	public void removeBullet(Bullet b) {
		bullets.remove(b);
	}
	
	public void removeAnimation(Animation ani) {
		animations.remove(ani);
	}
	
	/** Get all GameObject near the given origin, excluding itself. */
	public GameObject[] getNearObjects(GameObject origin, double range) {
		ArrayList<GameObject> near_obj = new ArrayList<GameObject>();
		
		// Check for players
		for(GameObject obj : players.toArray(new GameObject[0])) {
			if(obj != origin && origin.near(obj, range))
				near_obj.add(obj);
		}

		// Check for bullets
		for(GameObject obj : bullets.toArray(new GameObject[0])) {
			if(obj != origin && origin.near(obj, range))
				near_obj.add(obj);
		}
		
		return near_obj.toArray(new GameObject[0]);
	}
	
	public double getWidth() { return FIELD_WIDTH; }
	public double getHeight() { return FIELD_HEIGHT; }
}
