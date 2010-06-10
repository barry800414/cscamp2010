import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.io.File;

import javax.imageio.ImageIO;
	
public class GraphicsEngine {
	public static final int TARGET_FPS = 60;
	public static final long UPDATE_FPS_PERIOD = 1000;
	
	public static final double PLAYER_SIZE = 50.0;
	public static final double BULLET_SIZE = 10.0;
	public static final double SHIELD_SIZE = 74;
	
	private Game game;
	private GameInfo info;
	
	private JFrame main_scr;
	private BufferStrategy buffer;
	private BufferedImage[] ufo = new BufferedImage[9];
	private BufferedImage bullet;
	private BufferedImage shield1;
	
	private long last_draw;
	
	private long last_fps_update;
	private long frames_drew;
	private double fps;
	
	public GraphicsEngine(Game game) {
		this.game = game;
		this.info = game.getGameInfo();
	}
	
	public void init() {
		main_scr = new JFrame();
		main_scr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main_scr.setSize((int)info.getWidth(), (int)info.getHeight());

		main_scr.setVisible(true);
		
		try{
			ufo[0] = ImageIO.read(getClass().getResourceAsStream("ufo.png"));
			ufo[1] = ImageIO.read(getClass().getResourceAsStream("ufo_right.png"));
			ufo[2] = ImageIO.read(getClass().getResourceAsStream("ufo_upright.png"));
			ufo[3] = ImageIO.read(getClass().getResourceAsStream("ufo_up.png"));
			ufo[4] = ImageIO.read(getClass().getResourceAsStream("ufo_upleft.png"));
			ufo[5] = ImageIO.read(getClass().getResourceAsStream("ufo_left.png"));
			ufo[6] = ImageIO.read(getClass().getResourceAsStream("ufo_downleft.png"));
			ufo[7] = ImageIO.read(getClass().getResourceAsStream("ufo_down.png"));
			ufo[8] = ImageIO.read(getClass().getResourceAsStream("ufo_downright.png"));
			
			bullet = ImageIO.read(getClass().getResourceAsStream("bullet.png"));
			shield1 = ImageIO.read(getClass().getResourceAsStream("shieldA.png"));
		}
		catch(Exception e){
			System.out.println(e);
			System.out.println("test!!!");
		}
		
		draw();
	}
	
	public void draw() {
		long time_now = game.getCurrentEventTime();
		
		/* We cannot create BufferStrategy if it is not diaplayable
		 * (will result in a exception), and it is not necessary to
		 * draw screen if it is not displayable.
		 */
		if(main_scr.isDisplayable()) {
			// Create BufferStrategy on first draw.
			if(buffer == null) {
				main_scr.createBufferStrategy(2);
				buffer = main_scr.getBufferStrategy();
			}
			
			// Get the graphics buffer
			Graphics g = buffer.getDrawGraphics();
			Graphics2D g2d = (Graphics2D)g;
			
			// Clean buffer
			g2d.clearRect(0, 0, (int)info.getWidth(), (int)info.getHeight());
			
			// Draw all components
			drawBullets(g2d);
			drawPlayers(g2d);
			
			// Cleanup
			g.dispose();
			buffer.show();
			
			// Calculate fps
			long since_last_update = time_now - last_fps_update;
			frames_drew++;
			if(since_last_update >= UPDATE_FPS_PERIOD) {
				fps = frames_drew * 1000.0 / since_last_update;
				main_scr.setTitle(String.format("FPS: %.2f", fps));
				frames_drew = 0;
				last_fps_update = time_now;
			}
			
			// Force to update
			Toolkit.getDefaultToolkit().sync();
		}
		
		// Generate next draw event
		last_draw = time_now;
		enqueueNextDraw();
	}
	
	private void drawBullets(Graphics2D g) {
		for(Bullet b : info.getAllBullets()) {
			g.drawImage(bullet,(int)(b.locX-BULLET_SIZE/2),(int)(b.locY-BULLET_SIZE/2),null);
		}
	}
	
	private void drawPlayers(Graphics2D g) {
		for(Player p : info.getAllPlayers()) {
			if(p.isAlive()) {
				g.drawImage(ufo[p.direct],(int)(p.locX-PLAYER_SIZE/2),(int)(p.locY-PLAYER_SIZE/2),null);
				g.drawImage(shield1,(int)(p.locX-SHIELD_SIZE/2) , (int)(p.locY-SHIELD_SIZE/2),null);
			}
		}
	}
	
	private void enqueueNextDraw() {
		final GraphicsEngine ge = this;
		final long time_next_draw = last_draw + 1000 / TARGET_FPS;
		
		game.getGameQueue().addEvent(new Event() {
			public long getTime() { return time_next_draw; }
			public void action() { ge.draw(); }
		});
	}
	
	public JFrame getMainFrame() {
		return main_scr;
	}
}
