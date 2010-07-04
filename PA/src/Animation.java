import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

/**
 * Animation
 * This class stores a animation.
 * The graphics engine will use the information provided (eg. getType)
 * to draw animation on screen.
 */
public abstract class Animation {
	/** Draw the animation */
	public abstract void draw(Graphics2D g, Game game);
}

class AnimationCountdown extends Animation {
	private static final Font font_msg = new Font("Arial",Font.BOLD,30);
	private long start, dur;
	private String msg;
	
	public AnimationCountdown(long startTime, long duration, String message) {
		start = startTime;
		dur = duration;
		msg = message;
	}
	
	@Override
	public void draw(Graphics2D g, Game game) {
		double left = (double)( (dur-(game.getTime()-start))/100 ) / 10.0;
		if(left <= 0) {
			game.getGameInfo().removeAnimation(this);
			return;
		}
		String s = msg + " in " + String.format("%.1f", left) + "s";
		FontMetrics fm = g.getFontMetrics(font_msg);
		AttributedString as = new AttributedString(s);
		as.addAttribute(TextAttribute.FONT, font_msg);
		as.addAttribute(TextAttribute.FOREGROUND, Color.red);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawString(as.getIterator(), ((int)game.getGameInfo().FIELD_WIDTH - fm.stringWidth(s))/2, 300);
	}
}
