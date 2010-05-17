
public final class AIPlayer {
	private Player player;
	private boolean[] player_in_state;
	
	public int locX, locY, life, direct, speed, player_skill;
	
	public boolean hasState(int state) {
		return (state >= 1 && state <= 10) ? player_in_state[state] : false;
	}
	
	/**
	 * For security reason, onPlayerSkillUsage message will be distributed to us,
	 * if it represents our player then we set the player_skill field.
	 */
	public void onPlayerSkillUsage(Player p, Skill skill) {
		if(p == player) player_skill = skill.getId();
	}
	
	public AIPlayer(Player player) {
		this.player = player;
		locX = (int)Math.round(player.getLocX());
		locY = (int)Math.round(player.getLocY());
		life = player.getLife();
		direct = player.getDirect();
		speed = (int)Math.round(player.getSpeed());
		
		player_in_state = new boolean[11];
		for(Effect eff : player.getAllEffects()) {
			int id = eff.getId();
			if(id >= 1 && id <= 10) player_in_state[id] = true;
		}
	}
}
