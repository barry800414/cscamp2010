/**
 * Effect
 * A player has several effects.
 * The effect may generate game events.
 */
public class Effect {
	protected GameQueue queue;
	
	public Effect(GameQueue queue) {
		this.queue = queue;
	}
	
	/** Called when set to a player */
	public void onSet(Player p) {}
	
	/**
	 * Called each update of a player (eg. player.updateStatus()).
	 * Due to the speed of a player is recalculated every round,
	 * this method is to let you have a chance to do that.
	 */
	public void onEffect(Player p) {}
	
	/** Called on removal */
	public void onRemove(Player p) {}
	
	/**
	 * Called on player on damage,
	 * letting effect to have a chance to remove damage.
	 */
	public void onDamage(Damage d) {}
}
