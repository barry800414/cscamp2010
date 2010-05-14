import java.util.Hashtable;
import java.util.ArrayList;

public class Player extends GameObject {
	public static final double	BASE_SPEED = 70.0;
	public static final int		BASE_LIFE = 3;
	
	private int							life;
	private boolean						silence;
	private Hashtable<Skill, Integer>	skill_quota;
	private ArrayList<Effect>			effects;
	
	public Player(Game game) {
		super(game);
		
		life = BASE_LIFE;
		silence = false;
		skill_quota = new Hashtable<Skill, Integer>();
		effects = new ArrayList<Effect>();
		
		updateStatus();
	}
	
	public int getLife() {
		return life;
	}
	
	public boolean isSilence() {
		return silence;
	}
	
	public void setSilence(boolean silence) {
		this.silence = silence;
	}
	
	public void useSkill(Skill skill, Player target) {
		if(!isSilence()) {
			int quota = getSkillQuota(skill);
			if(quota > 0) {
				if(!skill.canSetTarget()) target = this;
				skill.use(game, this, target);
				setSkillQuota(skill, quota - 1);
			} else {
				System.out.println("Player: " + this + " don't have skill " + skill + ".");
			}
		}
	}
	
	public void setSkillQuota(Skill skill, int quota) {
		skill_quota.put(skill, quota);
	}
	
	public int getSkillQuota(Skill skill) {
		Integer quota = skill_quota.get(skill);
		if(quota == null)
			return 0;
		else
			return quota.intValue();
	}
	
	public void addEffect(Effect effect) {	
		effects.add(effect);
		effect.onSet();
	}
	
	public void removeEffect(Effect effect){
		effect.onRemove();
		effects.remove(effect);
	}
	
	public void applyDamage(Damage damage) {
		for(Effect eff : effects) {
			eff.onDamage(damage);
		}
		
		life += damage.life;
	}
	
	/**
	 * Update the status of the player:
	 * Reset speed, silence state, etc.
	 * And iterate all effects and invoke onEffect().
	 */
	public void updateStatus() {
		// Reset information
		setSpeed(BASE_SPEED);
		
		for(Effect eff : effects) {
			eff.onEffect();
		}
	}
}
