import javax.swing.*;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.*;
import java.text.AttributedString;

import javax.imageio.ImageIO;
	
public class GraphicsEngine {
	public static final int TARGET_FPS = 60;
	public static final long UPDATE_FPS_PERIOD = 1000;
	
	public static final double PLAYER_SIZE = 50.0;
	public static final double BULLET_SIZE = 10.0;
	public static final double SHIELD_SIZE = 74;
	
	public static final Font font1 = new Font("Arial",Font.BOLD,20);
	public static final Font font2 = new Font("Arial",Font.BOLD,18);
	public static final Font font3 = new Font("Arial",Font.PLAIN,11);
	
	private Game game;
	private GameInfo info;
	
	private JFrame main_scr;
	private Canvas game_scr;
	private BufferStrategy buffer;
	private BufferedImage[] ufo = new BufferedImage[9];
	private BufferedImage bullet;
	private BufferedImage shield1;
	private BufferedImage background;
	private BufferedImage freeze;
	
	private long last_draw;
	
	private long last_fps_update;
	private long frames_drew;
	private double fps;
	private int current_target_fps = TARGET_FPS;
	
	public GraphicsEngine(Game game) {
		this.game = game;
		this.info = game.getGameInfo();
	}
	
	public void init() {
		game_scr = new Canvas();
		game_scr.setSize((int)info.getWidth(), (int)info.getHeight());
		game_scr.setFocusable(false);
		game_scr.validate();
		
		main_scr = new JFrame();
		main_scr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main_scr.add(game_scr);
		// Make JFrame resize to fit the size of the components
		main_scr.setResizable(false);
		main_scr.pack();
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
			shield1 = ImageIO.read(getClass().getResourceAsStream("ShieldA.png"));
			background = ImageIO.read(getClass().getResourceAsStream("background.png"));
			freeze = ImageIO.read(getClass().getResourceAsStream("freeze.png"));
		}
		catch(Exception e){
			System.out.println("Cannot load some images.");
			System.out.println(e);
		}
		
		draw();
	}
	
	public void draw() {
		long time_now = game.getCurrentEventTime();
		
		/* We cannot create BufferStrategy if it is not diaplayable
		 * (will result in a exception), and it is not necessary to
		 * draw screen if it is not displayable.
		 */
		if(game_scr.isDisplayable()) {
			// Create BufferStrategy on first draw.
			if(buffer == null) {
				game_scr.createBufferStrategy(2);
				buffer = game_scr.getBufferStrategy();
			}
			
			// Get the graphics buffer
			Graphics g = buffer.getDrawGraphics();
			Graphics2D g2d = (Graphics2D)g;
			
			// Clean buffer
			g2d.clearRect(0, 0, (int)info.getWidth(), (int)info.getHeight());
			g2d.drawImage(background,0,0,null);
			
			// Draw all components
			drawBullets(g2d);
			drawPlayers(g2d);
			drawStatus(g2d);
			drawEffect(g2d);
			drawAnimation(g2d);
			if(game.isDebug()) {
				drawBulletsSpeed(g2d);
			}
			
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
		if(game.getTime() - last_draw > 1000/current_target_fps) {
			current_target_fps-=2;
			if(current_target_fps < 1) current_target_fps = 1;
		} else {
			if(current_target_fps < TARGET_FPS) current_target_fps++;
		}
		enqueueNextDraw();
	}
	
	private void drawBullets(Graphics2D g) {
		for(Bullet b : info.getAllBullets()) {
			g.drawImage(bullet,(int)(b.locX-BULLET_SIZE/2),(int)(b.locY-BULLET_SIZE/2),null);
		}
	}
	
	private void drawPlayers(Graphics2D g) {
		int num = info.getAllPlayers().length;
		Player p;
		for(int i=0;i<num;i++) {
			p = info.getPlayer(i);
			if(p.isAlive()) {
				g.drawImage(ufo[p.direct],(int)(p.locX-PLAYER_SIZE/2),(int)(p.locY-PLAYER_SIZE/2),null);
				AttributedString as = new AttributedString("" + (i+1));
				as.addAttribute(TextAttribute.FONT, font2);
				g.drawString(as.getIterator(),(int)(p.locX) - 5,(int)(p.locY) + 5);
				// TODO: need to be change to apply Animation mechanism.
				if(p.hasState(Effect.EFFECT_SHIELDA) || p.hasState(Effect.EFFECT_SHIELDB) || p.hasState(Effect.EFFECT_UNVULNERABLE)) {
					g.drawImage(shield1,(int)(p.locX-SHIELD_SIZE/2) , (int)(p.locY-SHIELD_SIZE/2),null);
				}
			}
		}
	}
	
	private void drawStatus(Graphics2D g) {
		int num = info.getAllPlayers().length;
		for(int i=0;i<num;i++) {
			Player p = info.getPlayer(i);
			{
				String s = (i+1) + "  " + p.toString();
				AttributedString as = new AttributedString(s);
				as.addAttribute(TextAttribute.FONT,font1);
				as.addAttribute(TextAttribute.FOREGROUND,Color.yellow);
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.drawString(as.getIterator(), 10 + 200*i, 35);
			}
			{
				String s = "    " + p.getLife() + " / " + p.getScore();
				AttributedString as = new AttributedString(s);
				as.addAttribute(TextAttribute.FONT,font1);
				as.addAttribute(TextAttribute.FOREGROUND,Color.red);
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.drawString(as.getIterator(), 10 + 200*i, 60);
			}
		}
	}
	
	private void drawEffect(Graphics2D g){
		int num = info.getAllPlayers().length;
		for(int i=0;i<num;i++){
			if(info.getPlayer(i).hasState(Effect.EFFECT_FROZEN)){
				g.drawImage(freeze,0,0,null);
				break;
			}
		}
	}
	
	private void drawAnimation(Graphics2D g) {
		for(Animation a : info.getAllAnimations()) {
			a.draw(g, game);
		}
	}
	
	private void drawBulletsSpeed(Graphics2D g) {
		double vlen = 20;
		for(Bullet b : info.getAllBullets()) {
			// Line
			g.setColor(Color.RED);
			g.setStroke(new BasicStroke(3));
			g.drawLine((int)b.locX, (int)b.locY, (int)(b.locX+b.dirX*vlen), (int)(b.locY+b.dirY*vlen));
			
			// Hitting?
			boolean show = false;
			String s = String.format("(%.0f,%.0f)", b.locX, b.locY);
			
			// Check for all players
			for(Player p : info.getAllPlayers()) {
				if(!p.isAlive()) continue;
				double hit = Tool.WhenHit(new AIPlayer(p), new AIBullet(b));
				if(hit <= 3000) {
					s += String.format(", hitting %s in %.1f s", p.toString(), hit/1000.0);
					show = true;
				}
			}
			
			if(show) {
				AttributedString as = new AttributedString(s);
				as.addAttribute(TextAttribute.FONT,font3);
				as.addAttribute(TextAttribute.FOREGROUND,Color.CYAN);
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.drawString(as.getIterator(), (int)b.locX+10, (int)b.locY+10);
			}
		}
	}
	
	private void enqueueNextDraw() {
		final GraphicsEngine ge = this;
		final long time_next_draw = last_draw + 1000 / current_target_fps;
		
		game.getGameQueue().addEvent(new Event() {
			public long getTime() { return time_next_draw; }
			public void action() { ge.draw(); }
		});
	}

	
	
	public JFrame getMainFrame() {
		return main_scr;
	}
}
