/**
 * Skill
 */
public enum Skill {
	Nothing() {
		public boolean canSetTarget() { return true; }
		public void use(Game game, Player src, Player target) {
			System.out.println("Used a null skill on " + target + ".");
		}
	},
	Froze() {
		public boolean canSetTarget() { return false; }
		public void use(Game game, Player src, Player target) {
			int time_effect_start = game.getTime() + 3000;
			
			// Schedule a event to apply frozen effect to all players except itself
			GameQueue queue = game.getGameQueue();
			GameInfo info = game.getGameInfo();
			int sz = info.getNumPlayers();
			
			for(int i = 0; i < sz; i++) {
				// Add the corresponding effect
				Player p = info.getPlayer(i);
				if(p != src)
					queue.enqueueAddEffect(p, new EffectFrozen(game, p), time_effect_start);
			}
		}
	};
	
	public abstract boolean canSetTarget();
	public abstract void use(Game game, Player src, Player target);
}
