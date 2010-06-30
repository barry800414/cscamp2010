import javax.swing.JFrame;
import java.awt.event.KeyEvent;

public class AIHuman2 extends AI {
	private Game game;
	private Keyboard kb;
	private boolean ready;
	
	public AIHuman2(Game game) {
		this.game = game;
		init();
	}

	/** Do a late init, due to the main frame may not have been created on instance creation. */
	private boolean init() {
		if(!ready && game != null) {
			GraphicsEngine ge = this.game.getGraphicsEngine();
			if(ge != null) {
				JFrame main_scr = ge.getMainFrame();
				if(main_scr != null) {
					this.kb = new Keyboard();
					main_scr.addKeyListener(kb);
					main_scr.addFocusListener(kb);
					
					ready = true;
				}
			}
		}
		return ready;
	}
	
	public void run() {
		/*
		//System.out.println("AIHuman: run()");
		
		if(!ready && !init()) return;
		
		boolean kW = kb.isKeyDown(KeyEvent.VK_I), kA = kb.isKeyDown(KeyEvent.VK_J),
				kS = kb.isKeyDown(KeyEvent.VK_K), kD = kb.isKeyDown(KeyEvent.VK_L);
		/*
		 * Calculates the direction the player would like to move,
		 * See GameObject.setDir(int) for defined directions.
		 */
		/*
		int dir = 0, n = 0;
		if(kW) { dir += 3; n++; }
		if(kA) { dir += 5; n++; }
		if(kS) { dir += 7; n++; }
		if(kD) { dir += 1; n++; }
		if(kW && kS) { dir -= 10; n -= 2; }
		if(kA && kD) { dir -= 6;  n -= 2; }
		if(n == 2 && kS && kD)
			move(8);
		else if(n > 0)
			move(dir / n);
		else
			move(0);
		
		/*
		 * Detect if the player wants to cast a skill.
		 */
		/*
		int skill = -1;
		for(int i = 1; i <= 10; i++) {
			if(kb.isKeyDownOnce(KeyEvent.VK_0 + (i % 10))) {
				skill = i;
				break;
			}
		}
		if(skill > -1) {
			if(skill == 0) skill = 10;
			useSkill(skill, myId);
		}
		*/
	}
}
