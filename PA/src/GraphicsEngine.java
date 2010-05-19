import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
	
public class GraphicsEngine {
	public static final int TARGET_FPS = 60;
	public static final long UPDATE_FPS_PERIOD = 1000;
	
	private Game game;
	private GameInfo info;
	
	private JFrame main_scr;
	private BufferStrategy buffer;
	
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
		
		main_scr.createBufferStrategy(2);
		buffer = main_scr.getBufferStrategy();
		
		draw();
	}
	
	public void draw() {
		long time_now = game.getCurrentEventTime();
		
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
		
		// Generate next draw event
		last_draw = time_now;
		enqueueNextDraw();
	}
	
	private void drawBullets(Graphics2D g) {
		for(Bullet b : info.getAllBullets()) {
			g.draw(new Ellipse2D.Double(b.getLocX(), b.getLocY(), 5, 5));
		}
	}
	
	private void drawPlayers(Graphics2D g) {
		for(Player p : info.getAllPlayers()) {
			g.draw(new Ellipse2D.Double(p.getLocX(), p.getLocY(), 20, 20));
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
}
