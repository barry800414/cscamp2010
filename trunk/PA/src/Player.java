import java.util.Hashtable;
import java.util.ArrayList;

public class Player extends GameObject {
	public static final double	BASE_SPEED = 70.0;
	public static final int		BASE_LIFE = 3;
	
	private int							id;
	private int							life;
	private boolean						silence;
	private Hashtable<Skill, Integer>	skill_quota;
	private ArrayList<Effect>			effects;
	private AIRunner					ai_runner;
	
	public Player(Game game) {
		super(game);
		
		id = 0;
		life = BASE_LIFE;
		silence = false;
		skill_quota = new Hashtable<Skill, Integer>();
		effects = new ArrayList<Effect>();
		ai_runner = null;
		
		updateStatus();
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int newid) {
		id = newid;
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
		if(quota > 0) {
			skill_quota.put(skill, quota);
		} else {
			skill_quota.remove(skill);
		}
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
	
	public Effect[] getAllEffects() {
		return effects.toArray(new Effect[0]);
	}
	
	@Override
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
	
	/** Returns assigned AI, null if not exist. */
	public AI getAI() {
		return (ai_runner != null) ? ai_runner.getAIInstance() : null;
	}
	
	/** Returns the AIRunner, null if not exist. */
	public AIRunner getAIRunner() {
		return ai_runner;
	}
	
	/** Assigns a AI to player, null to remove. */
	public void setAI(AI ai) {
		killAI();
		if(ai != null) {
			this.ai_runner = new AIRunner(ai);
		}
	}
	
	public void setAI(AIRunner runner) {
		killAI();
		this.ai_runner = runner;
	}
	
	/** Kill the AI runner */
	public void killAI() {
		// Stop and kill the AIRunner if exist
		if(ai_runner != null) {
			ai_runner.stop();
			ai_runner = null;
		}
	}
}
