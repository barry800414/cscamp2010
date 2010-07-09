import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;


/**
 * This class does the initialization of AIs and other game parts.
 */
public class Launcher {
	public static void printHelp() {
		System.out.println("Launcher [-f skill_quota_file] AI1 [AI2] [AI3] [AI4]");
	}
	
	public static void main(String[] args) {
		if(args.length == 0) {
			printHelp();
			return;
		}
		
		int i = 0;
		int pid = 0;
		// Create game instance
		Game g = new Game();
		GameInfo info = g.getGameInfo();
		
		// Debug?
		if(args[i].equals("--debug")) {
			g.setDebug(true);
			i++;
		}
		
		// Load skill quota if needed
		Map<String, int[]> skills = new Hashtable<String, int[]>();;
		if(args[i].equals("-f")) {
			skills = loadSkills(args[i+1]);
			i += 2;
		}
		
		
		// Load players
		for(; i < args.length; i++) {
			pid++;
			String ai_class = args[i];
			
			Player p = new Player(g);
			p.setLoc(g.random.nextDouble() * info.getWidth(), g.random.nextDouble() * info.getHeight());
			p.setId(pid);
			p.setName("Player " + (pid) + ": " + ai_class);
			
			if(ai_class.equals("AIHuman")) {
				for(int j = 1; j <= 10; j++) {
					p.setSkillQuota(Skill.skillFromId(j), 5);
				}
				
				p.setAI(new AIHuman(g));
			} else {
				int[] skill_quota = skills.get(ai_class);
				if(skill_quota != null) {
					for(int id = 0; id < skill_quota.length; id++) {
						p.setSkillQuota(Skill.skillFromId(Skill.SKILL_ID_MIN + id), skill_quota[id]);
					}
				}
				
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
		
		if(g.getGameInfo().getNumPlayers() == 0) {
			printHelp();
			return;
		}
		
		// Starts the game
		g.startGame();
	}
	
	public static Map<String, int[]> loadSkills(String fn) {
		final int skill_count = 10;
		
		Map<String, int[]> skills = new Hashtable<String, int[]>();
		
		try {
			Scanner input = new Scanner(new FileInputStream(new File(fn)));
			
			while(input.hasNext()) {
				String cls = input.next();
				int[] count = new int[skill_count];
				for(int i = 0; i < skill_count; i++)
					count[i] = input.nextInt();
				skills.put(cls, count);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Cannot load skill config file: "+fn);
			e.printStackTrace();
		}
		
		return skills;
	}
}
