/**
 * Effect
 * A player has several effects.
 * The effect may generate game events.
 */
public class Effect {
	public static final int	EFFECT_NOTHING = 0,
							EFFECT_FROZEN = 10,
							EFFECT_SHIELDA = 1,
							EFFECT_SHIELDB = 2,
							EFFECT_ACCELERATE = 3,
							EFFECT_SLOWDOWN = 9,
							EFFECT_UNVULNERABLE = 11;
	
	protected Game game;
	protected Player player;
	
	public Effect(Game game, Player player) {
		this.game = game;
		this.player = player;
	}
	
	/** Each type of effect should have a unique id */
	public int getId() { return EFFECT_NOTHING; }
	
	/** Called when set to a player */
	public void onSet() {}
	
	/**
	 * Called each update of a player (eg. player.updateStatus()).
	 * Due to the speed of a player is recalculated every round,
	 * this method is to let you have a chance to do that.
	 */
	public void onEffect() {}
	
	/** Called on removal */
	public void onRemove() {}
	
	/**
	 * Called on player on damage,
	 * letting effect to have a chance to remove damage.
	 */
	public void onDamage(Damage d) {}
}

/**
 * A template for defining effects
 */
class EffectNothing extends Effect {
	public EffectNothing(Game game, Player player) { super(game, player); }
	@Override public int getId() { return EFFECT_NOTHING; }
	@Override
	public void onSet() {
		System.out.println("EffectNothing: onSet()");
	}
	@Override
	public void onEffect() {
	}
	@Override
	public void onRemove() {
		System.out.println("EffectNothing: onRemove()");
	}
	@Override
	public void onDamage(Damage d) {
		System.out.println("EffectNothing: onDamage()");
	}
}


/*
 * BELOW: All effects used in game.
 */
class EffectFrozen extends Effect {
	public EffectFrozen(Game game, Player player) { super(game, player); }
	@Override public int getId() { return EFFECT_FROZEN; }
	@Override
	public void onSet() {
		game.getGameQueue().enqueueRemoveEffect(player, this, game.getTime() + 3000);
	}
	@Override
	public void onEffect() {
		player.setSpeed(0.0);
	}
}

class EffectShieldA extends Effect {
	public EffectShieldA(Game game, Player player) { super(game, player); }
	@Override public int getId() { return EFFECT_SHIELDA; }
	@Override
	public void onSet() {
		game.getGameQueue().enqueueRemoveEffect(player, this, game.getTime() + 5000);
	}
	@Override
	public void onDamage(Damage d) {
		if(d.life < 0) d.life = 0;
	}
}

class EffectShieldB extends Effect {
	private int prevent_damage = 5;
	public EffectShieldB(Game game, Player player) { super(game, player); }
	@Override public int getId() { return EFFECT_SHIELDB; }
	@Override
	public void onDamage(Damage d) {
		if(-d.life > prevent_damage) {
			d.life += prevent_damage;
			prevent_damage = 0;
		} else {
			prevent_damage += d.life;
			d.life = 0;
		}
		if(prevent_damage <= 0)
			player.removeEffect(this);
	}
}

class EffectAccelerate extends Effect {
	public EffectAccelerate(Game game, Player player) { super(game, player); }
	@Override public int getId() { return EFFECT_ACCELERATE; }
	@Override
	public void onSet() {
		game.getGameQueue().enqueueRemoveEffect(player, this, game.getTime() + 10000);
	}
	@Override
	public void onEffect() {
		player.setSpeed(player.getSpeed() * 1.6);
	}
}

class EffectSlowdown extends Effect {
	public EffectSlowdown(Game game, Player player) { super(game, player); }
	@Override public int getId() { return EFFECT_SLOWDOWN; }
	@Override
	public void onSet() {
		game.getGameQueue().enqueueRemoveEffect(player, this, game.getTime() + 5000);
	}
	@Override
	public void onEffect() {
		player.setSpeed(player.getSpeed() * 0.1);
	}
}

class EffectUnvulnerable extends Effect {
	public EffectUnvulnerable(Game game, Player player) { super(game, player); }
	@Override public int getId() { return EFFECT_UNVULNERABLE; }
	@Override
	public void onSet() {
		game.getGameQueue().enqueueRemoveEffect(player, this, game.getTime() + 1000);
	}
	@Override
	public void onDamage(Damage d) {
		if(d.life < 0) d.life = 0;
	}
}