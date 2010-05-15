
public enum Skill {
	Nothing(true) {
		public void use(Game game, Player src, Player target) {
			System.out.println("Used a null skill on " + target + ".");
		}
	},
	ShieldA(true) {
		public void use(Game game, Player src, Player target) {
			if(target == null) target = src;
			target.addEffect(new EffectShieldA(game, target));
		}
	},
	ShieldB(true) {
		public void use(Game game, Player src, Player target) {
			if(target == null) target = src;
			target.addEffect(new EffectShieldB(game, target));
		}
	},
	Accelerate(true) {
		public void use(Game game, Player src, Player target) {
			if(target == null) target = src;
			target.addEffect(new EffectAccelerate(game, target));
		}
	},
	SelfDestroy(false) {
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
	ControlBullets(true) {
		public void use(Game game, Player src, Player target) {
			if(target == null) target = src;
			
			final double MAX_MISS = 20.0;
			
			// Set the direction of near bullets to target
			GameInfo info = game.getGameInfo();
			GameObject[] near_objs = info.getNearObjects(src, 50.0);
			for(GameObject obj : near_objs) {
				if(obj instanceof Bullet) {
					Bullet bullet = (Bullet)obj;
					bullet.setOwner(src);
					bullet.setDirection(target, MAX_MISS);
				}
			}
		}
	},
	Teleport(true) {
		public void use(Game game, Player src, Player target) {
			if(target == null) target = src;
			
			GameInfo info = game.getGameInfo();
			
			// Teleport!
			target.setLoc(
					game.random.nextDouble() * info.getWidth(),
					game.random.nextDouble() * info.getHeight()
			);
		}
	},
	CrossKill(true) { // too hard to translate...
		public void use(Game game, Player src, Player target) {
			if(target == null) target = src;
			
			final double BULLET_SPACING = 10.0;
			final int NUM_BULLETS = 5;
			
			GameInfo info = game.getGameInfo();
			double x = src.getDirX(), y = src.getDirY();
			double w = info.getWidth(), h = info.getHeight();
			
			// Generates bullets from the far side
			if(x > w / 2) {
				// bullets from left
				for(int i = 0; i < NUM_BULLETS; i++) {
					info.addBullet(new Bullet(game, (-i * BULLET_SPACING), y, 1.0, 0.0, Bullet.BASE_SPEED, src));
				}
			} else {
				// bullets from right
				for(int i = 0; i < NUM_BULLETS; i++) {
					info.addBullet(new Bullet(game, (w + i * BULLET_SPACING), y, -1.0, 0.0, Bullet.BASE_SPEED, src));
				}
			}
			
			if(y > h / 2) {
				// bullets from top
				for(int i = 0; i < NUM_BULLETS; i++) {
					info.addBullet(new Bullet(game, x, (-i * BULLET_SPACING), 0.0, -1.0, Bullet.BASE_SPEED, src));
				}
			} else {
				// bullets from bottom
				for(int i = 0; i < NUM_BULLETS; i++) {
					info.addBullet(new Bullet(game, x, (h + i * BULLET_SPACING), 0.0, 1.0, Bullet.BASE_SPEED, src));
				}
			}
		}
	},
	Steal(true) {
		public void use(Game game, Player src, Player target) {
			if(target == null) target = src;
			
			// TODO: implementation
		}
	},
	Slowdown(true) {
		public void use(Game game, Player src, Player target) {
			if(target == null) target = src;
			target.addEffect(new EffectSlowdown(game, target));
		}
	},
	Froze(false) {
		public void use(Game game, Player src, Player target) {
			int time_effect_start = game.getTime() + 3000;
			
			// Schedule events to apply frozen effect to all players except itself
			GameQueue queue = game.getGameQueue();
			GameInfo info = game.getGameInfo();
			int sz = info.getNumPlayers();
			
			for(int i = 0; i < sz; i++) {
				// Enqueue the corresponding effect event
				Player p = info.getPlayer(i);
				if(p != src)
					queue.enqueueAddEffect(p, new EffectFrozen(game, p), time_effect_start);
			}
		}
	};
	
	private boolean targetable;
	Skill(boolean targetable) { this.targetable = targetable; }
	public boolean canSetTarget() { return targetable; }
	public abstract void use(Game game, Player src, Player target);
	
	public static final Skill[] SKILL_LIST = {
		Nothing,
		ShieldA, ShieldB, Accelerate, SelfDestroy, ControlBullets,
		Teleport, CrossKill, Steal, Slowdown, Froze
	};
	public static Skill skillFromId(int id) {
		return (id >= 1 && id <= 10) ? SKILL_LIST[id] : null;
	}
}
