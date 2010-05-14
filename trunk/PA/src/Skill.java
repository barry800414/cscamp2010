import java.util.Random;

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
	ShieldA() {
		public boolean canSetTarget() { return true; }
		public void use(Game game, Player src, Player target) {
			if(target == null) target = src;
			target.addEffect(new EffectShieldA(game, target));
		}
	},
	ShieldB() {
		public boolean canSetTarget() { return true; }
		public void use(Game game, Player src, Player target) {
			if(target == null) target = src;
			target.addEffect(new EffectShieldB(game, target));
		}
	},
	Accelerate() {
		public boolean canSetTarget() { return true; }
		public void use(Game game, Player src, Player target) {
			if(target == null) target = src;
			target.addEffect(new EffectAccelerate(game, target));
		}
	},
	SelfDestroy() {
		public boolean canSetTarget() { return false; }
		public void use(Game game, Player src, Player target) {
			GameInfo info = game.getGameInfo();
			
			// Do damage to near objects (players, bullets)
			GameObject[] near_obj = info.getNearObjects(src, 50.0);
			for(GameObject obj : near_obj) {
				obj.applyDamage(Damage.newWithLife(-1));
			}
			
			// Do damage to the caster
			src.applyDamage(Damage.newWithLife(-1));
		}
	},
	ControlBullets() {
		public boolean canSetTarget() { return true; }
		public void use(Game game, Player src, Player target) {
			if(target == null) target = src;
			
			GameInfo info = game.getGameInfo();
			
			// Set the direction of near bullets to target
			GameObject[] near_obj = info.getNearObjects(src, 50.0);
			for(GameObject obj : near_obj) {
				if(obj instanceof Bullet) {
					Bullet bullet = (Bullet)obj;
					bullet.setOwner(src);
					bullet.setDirection(target);
				}
			}
		}
	},
	Teleport() {
		public boolean canSetTarget() { return true; }
		public void use(Game game, Player src, Player target) {
			if(target == null) target = src;
			
			GameInfo info = game.getGameInfo();
			Random rand = new Random();
			
			// Teleport!
			target.setLoc(
					rand.nextDouble() * info.getWidth(),
					rand.nextDouble() * info.getHeight()
			);
		}
	},
	CrossKill() { // so hard to translate...
		public boolean canSetTarget() { return true; }
		public void use(Game game, Player src, Player target) {
			if(target == null) target = src;
			
			// TODO: implementation
		}
	},
	Steal() {
		public boolean canSetTarget() { return true; }
		public void use(Game game, Player src, Player target) {
			if(target == null) target = src;
			
			// TODO: implementation
		}
	},
	Slowdown() {
		public boolean canSetTarget() { return true; }
		public void use(Game game, Player src, Player target) {
			if(target == null) target = src;
			target.addEffect(new EffectSlowdown(game, target));
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
