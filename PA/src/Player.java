
public class Player extends GameObject {
	public int getLife() {
	}
	
	public boolean isSilence() {
	}
	
	public void setSilence(boolean silence) {
	}
	
	public void useSkill(Skill skill, Player target) {
	}
	
	public void setSkillQuota(Skill skill, int quota) {
	}
	
	public int getSkillQuota(Skill skill) {
	}
	
	public void addEffect(Effect effect) {	
	}
	
	public void removeEffect(Effect effect){
	}
	
	/**
	 * Update the status of the player:
	 * Reset speed, silence state, etc.
	 * And iterate all effects and invoke onEffect().
	 */
	public void updateStatus() {
	}
}
