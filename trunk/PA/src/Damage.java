
public class Damage {
	/** The life damaged, usually below 0. */
	public int life;
	
	/* Utility static methods to create various types of damage, syntactic sugar. */
	public static Damage newWithLife(int life) {
		Damage damage = new Damage();
		damage.life = life;
		return damage;
	}
}
