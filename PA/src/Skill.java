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
		public void use(final Game game, final Player src, Player target) {
			// Create the corresponding effect
			final int time_dismiss = game.getTime() + 3000;
			final Effect effect = new Effect(game.getGameQueue()) {
				public void onSet(final Player p) {
					final Effect eff = this;
					queue.addEvent(new Event() {
						public int getTime() { return time_dismiss; }
						public void action() { p.removeEffect(eff); }
					});
				}
				public void onEffect(Player p) {
					p.setSpeed(0.0);
				}
			};
			
			// Generate a event to delay the effect invocation
			final int time_effect_start = game.getTime() + 3000;
			game.getGameQueue().addEvent(new Event() {
				public int getTime() { return time_effect_start; }
				public void action() {
					// Apply to all players except itself
					GameInfo info = game.getGameInfo();
					int sz = info.getNumPlayers();
					for(int i = 0; i < sz; i++) {
						Player p = info.getPlayer(i);
						if(p != src) p.addEffect(effect);
					}
				}
			});
		} // Froze.use(...)
	};
	
	public abstract boolean canSetTarget();
	public abstract void use(Game game, Player src, Player target);
}
