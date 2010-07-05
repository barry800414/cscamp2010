
public final class AIPlayer {
	private Player player;
	private boolean[] player_in_state;
	
	public double locX, locY, speed, speedX, speedY;
	public int life, player_skill;
	
	public final boolean hasState(int state) {
		return (state >= 1 && state <= Effect.EFFECT_MAX_ID) ? player_in_state[state] : false;
	}
	
	/**
	 * For security reason, onPlayerSkillUsage message will be distributed to us,
	 * if it represents our player then we set the player_skill field.
	 */
	public final void onPlayerSkillUsage(Player p, Skill skill) {
		if(p == player) player_skill = skill.getId();
	}
	
	public AIPlayer(Player player) {
		this.player = player;
		locX = player.getLocX();
		locY = player.getLocY();
		life = player.getLife();
		speed = player.getSpeed();
		speedX = player.getDirX()*player.getSpeed();
		speedY = player.getDirX()*player.getSpeed();
		
		player_in_state = new boolean[Effect.EFFECT_MAX_ID + 1];
		for(Effect eff : player.getAllEffects()) {
			int id = eff.getId();
			if(id >= 1 && id <= Effect.EFFECT_MAX_ID) player_in_state[id] = true;
		}
	}
}
