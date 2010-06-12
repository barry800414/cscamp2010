
/**
 * This class does the initialization of AIs and other game parts.
 */
public class Launcher {
	public static void main(String[] args) {
		if(args.length == 0) {
			System.out.println("Launcher AI1 [AI2] [AI3] [AI4]");
			return;
		}
		
		// Create game instance
		Game g = new Game();
		GameInfo info = g.getGameInfo();
		
		// Load players
		for(int i = 0; i < args.length; i++) {
			String ai_class = args[i];
			
			Player p = new Player(g);
			p.setLoc(g.random.nextDouble() * info.getWidth(), g.random.nextDouble() * info.getHeight());
			p.setId(i + 1);
			p.setName("Player " + (i + 1) + ": " + ai_class);
			
			if(ai_class.equals("AIHuman")) {
				for(int j = 1; j <= 10; j++) {
					p.setSkillQuota(Skill.skillFromId(j), 5);
				}
				
				p.setAI(new AIHuman(g));
			} else {
				p.setAI(new AIRunner(ai_class));
			}
			
			g.getGameInfo().addPlayer(p);
		}
		/*
		// Init bullets
		int BULLETS = 50;
		Bullet[] b = new Bullet[BULLETS];
		for(int i = 0; i < BULLETS; i++) {
			b[i] = new Bullet(g);
			b[i].setLoc(g.getGameInfo().getWidth() * g.random.nextDouble(), g.getGameInfo().getHeight() * g.random.nextDouble());
			b[i].setDir(g.random.nextDouble() - 0.5, g.random.nextDouble() - 0.5);
			//b[i].setSpeed(1.0);
			g.getGameInfo().addBullet(b[i]);
		}
		*/
		// Starts the game
		g.startGame();
	}
}
