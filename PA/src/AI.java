import java.util.Enumeration;
import java.util.Hashtable;


public abstract class AI {
	/** Contract: If we have 4 players, the id must be 1 to 4. */
	private Hashtable<Integer, AIPlayer> players = new Hashtable<Integer, AIPlayer>();
	private AIBullet[] bullets;
	private int[] skill_quota;
	private long time;
	
	private int direct;
	private boolean use_skill;
	private Skill skill;
	private int target;
	
	public final void resetInternalState() {
		players.clear();
		bullets = null;
		skill_quota = null;
		
		use_skill = false;
		skill = null;
		target = 0;
	}
	
	public final void updateGameInfo(Game game, Player self_player) {
		GameInfo info = game.getGameInfo();
		time = game.getTime();
		
		// Set self data
		skill_quota = new int[Skill.SKILL_ID_MAX + 1];
		for(int i = Skill.SKILL_ID_MIN; i <= Skill.SKILL_ID_MAX; i++) {
			skill_quota[i] = self_player.getSkillQuota(Skill.skillFromId(i));
		}
		
		// Set players data
		for(Player p : info.getAllPlayers()) {
			players.put(p.getId(), new AIPlayer(p));
		}
		
		// Set bullets data
		int num_bullets = info.getNumBullets();
		bullets = new AIBullet[num_bullets];
		for(int i = 0; i < num_bullets; i++) {
			bullets[i] = new AIBullet(info.getBullet(i));
		}
	}
	
	public final void onPlayerSkillUsage(Player p, Skill skill) {
		for(Enumeration<Integer> e = players.keys(); e.hasMoreElements(); ) {
			AIPlayer ai_player = players.get(e.nextElement());
			if(ai_player != null) ai_player.onPlayerSkillUsage(p, skill);
		}
	}
	
	public final int getMove() {
		return direct;
	}
	
	public final boolean isUsingSkill() {
		return use_skill;
	}
	
	public final Skill getSkill() {
		return skill;
	}
	
	public final int getTarget() {
		return target;
	}
	
	/* API for AI */
	
	public final int getNumPlayers() {
		return players.size();
	}
	
	public final AIPlayer getPlayer(int id) {
		if(id >= 1 && id <= players.size())
			return players.get(id);
		else
			return null;
	}
	
	public final int getNumBullets() {
		return bullets.length;
	}
	
	public final AIBullet getBullet(int index) {
		if(index >= 0 && index < bullets.length)
			return bullets[index];
		else
			return null;
	}
	
	public final int getTime() {
		return (int)time;
	}
	
	public final void move(int direct) {
		this.direct = direct;
	}
	
	public final void useSkill(int skillId, int targetPlayer) {
		skill = Skill.skillFromId(skillId);
		if(skill != null) {
			use_skill = true;
			if(skill.canSetTarget()) {
				target = targetPlayer;
			}
		}
	}
	
	public final int getSkillQuota(int skillId) {
		if(skillId >= Skill.SKILL_ID_MIN && skillId <= Skill.SKILL_ID_MAX)
			return skill_quota[skillId];
		else
			return 0;
	}
	
	public abstract void run();
}
