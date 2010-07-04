import java.util.ArrayList;
import java.util.List;


public enum Skill {
	Nothing(0, true) {
		public void use(Game game, Player src, Player target) {
			System.out.println("Used a null skill on " + target + ".");
		}
	},
	ShieldA(1, true) {
		public void use(Game game, Player src, Player target) {
			if(target == null) target = src;
			target.addEffect(new EffectShieldA(game, target));
		}
	},
	ShieldB(2, true) {
		public void use(Game game, Player src, Player target) {
			if(target == null) target = src;
			target.addEffect(new EffectShieldB(game, target));
		}
	},
	Accelerate(3, true) {
		public void use(Game game, Player src, Player target) {
			if(target == null) target = src;
			target.addEffect(new EffectAccelerate(game, target));
		}
	},
	SelfDestroy(4, false) {
		public void use(Game game, Player src, Player target) {
			final GameInfo info = game.getGameInfo();
			final Player self = src;
			final long time_start = game.getTime() + 500;
			
			Event ev = new Event() {
				public long getTime() { return time_start; }
				public void action() {
					// Do damage to near objects (players, bullets)
					GameObject[] near_obj = info.getNearObjects(self, 50.0);
					for(GameObject obj : near_obj) {
						obj.applyDamage(Damage.newWithLife(-1));
					}
					
					// Do damage to the caster
					self.applyDamage(Damage.newWithLife(-1));
				}
			};
			game.getGameQueue().addEvent(ev);
		}
	},
	ControlBullets(5, true) {
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
	Teleport(6, true) {
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
	CrossKill(7, true) { // too hard to translate...
		public void use(Game game, Player src, Player target) {
			if(target == null) target = src;
			
			final double BULLET_SPACING = 30.0;
			final int NUM_BULLETS = 5;
			final double BULLET_SPEED = 100.0;
			
			GameInfo info = game.getGameInfo();
			double x = target.getLocX(), y = target.getLocY();
			double w = info.getWidth(), h = info.getHeight();
			
			// Generates bullets from the far side
			if(x > w / 2) {
				// bullets from left
				for(int i = 0; i < NUM_BULLETS; i++) {
					info.addBullet(new Bullet(game, (-i * BULLET_SPACING), y, 1.0, 0.0, BULLET_SPEED, src));
				}
			} else {
				// bullets from right
				for(int i = 0; i < NUM_BULLETS; i++) {
					info.addBullet(new Bullet(game, (w + i * BULLET_SPACING), y, -1.0, 0.0, BULLET_SPEED, src));
				}
			}
			
			if(y > h / 2) {
				// bullets from top
				for(int i = 0; i < NUM_BULLETS; i++) {
					info.addBullet(new Bullet(game, x, (-i * BULLET_SPACING), 0.0, 1.0, BULLET_SPEED, src));
				}
			} else {
				// bullets from bottom
				for(int i = 0; i < NUM_BULLETS; i++) {
					info.addBullet(new Bullet(game, x, (h + i * BULLET_SPACING), 0.0, -1.0, BULLET_SPEED, src));
				}
			}
		}
	},
	Steal(8, true) {
		public void use(Game game, Player src, Player target) {
			if(target == null) target = src;
			
			Skill[] skills = target.getAllSkills();
			Skill skill;
			if(skills.length == 0) {
				return;
			} else if(skills.length == 1 && skills[0] == Skill.Steal) {
				return;
			} else {
				int index;
				do {
					index = game.random.nextInt(skills.length);
					skill = skills[index];
				} while(skill == Skill.Steal);
				
				// Determine the skill target
				Player skill_target = src;
				if(skill.canSetTarget()) {
					// Randomly set the target
					Player[] players = game.getGameInfo().getAllPlayers();
					skill_target = players[game.random.nextInt(players.length)];
				}
				
				// Update the skill quota before using it
				target.setSkillQuota(skill, target.getSkillQuota(skill) - 1);
				skill.use(game, src, skill_target);
			}
		}
	},
	Slowdown(9, true) {
		public void use(Game game, Player src, Player target) {
			if(target == null) target = src;
			target.addEffect(new EffectSlowdown(game, target));
		}
	},
	Froze(10, false) {
		private List<AnimationCountdown> all_ac = new ArrayList<AnimationCountdown>();
		public void use(Game game, Player src, Player target) {
			// Check player is alive and not in frozen state
			if(!src.isAlive() || src.hasState(Effect.EFFECT_FROZEN)) {
				System.out.println("Player ["+src+"]: unable to use skill Frozen in the current state.");
				return;
			}
			
			final long time_effect_start = game.getTime() + 3000;
			final AnimationCountdown my_ac = new AnimationCountdown(game.getCurrentEventTime(), 3000, "Frozen by "+src);
			
			all_ac.add(my_ac);
			
			// Schedule events to apply frozen effect to all players except itself
			final Game g = game;
			final Player source = src;
			
			game.getGameQueue().addEvent(new Event() {
				@Override
				public void action() {
					if(!source.isAlive() || source.hasState(Effect.EFFECT_FROZEN)) {
						System.out.println("Player ["+source+"]: skill Frozen was interrupted.");
						return;
					}
					all_ac.remove(my_ac);
					for(AnimationCountdown ac : all_ac.toArray(new AnimationCountdown[0]))
						ac.cancelMsg(1000, "Interrupted", g);
					
					for(Player p : g.getGameInfo().getAllPlayers()) {
						if(p != source) p.addEffect(new EffectFrozen(g, p));
					}
				}
				@Override
				public long getTime() {
					return time_effect_start;
				}
			});
			
			g.getGameInfo().addAnimation(my_ac);
		}
	};
	
	private int id;
	private boolean targetable;
	Skill(int id, boolean targetable) {
		this.id = id;
		this.targetable = targetable;
	}
	public int getId() { return id; }
	public boolean canSetTarget() { return targetable; }
	public abstract void use(Game game, Player src, Player target);
	
	public static final int SKILL_ID_MIN = 1, SKILL_ID_MAX = 10;
	public static final Skill[] SKILL_LIST = {
		Nothing,
		ShieldA, ShieldB, Accelerate, SelfDestroy, ControlBullets,
		Teleport, CrossKill, Steal, Slowdown, Froze
	};
	public static Skill skillFromId(int id) {
		return (id >= 1 && id <= 10) ? SKILL_LIST[id] : null;
	}
}
