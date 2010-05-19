import java.awt.event.*;

public class Keyboard implements KeyListener, FocusListener {
	private static final int MAX = 256;
	private boolean[] keyState = new boolean[MAX];
	private boolean[] keyPress = new boolean[MAX];

	public Keyboard() {
	}

	/* Interface KeyListener */
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() < MAX) {
			keyPress[e.getKeyCode()] = true;
			keyState[e.getKeyCode()] = true;
		}
	}

	/* Interface KeyListener */
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() < MAX) {
			keyState[e.getKeyCode()] = false;
		}
	}

	/* Interface KeyListener */
	public void keyTyped(KeyEvent e) {
	}

	public void onUnfocus() {
		for(int i = 0; i < MAX; i++) {
			keyState[i] = false;
			keyPress[i] = false;
		}
	}

	public boolean isKeyDown(int code) {
		if(code < MAX)
			return keyState[code];
		else
			return false;
	}

	public boolean isKeyDown(int code, boolean ctrl) {
		return (ctrl == isKeyDown(KeyEvent.VK_CONTROL)) && isKeyDown(code);
	}
	
	public boolean isKeyDownOnce(int code) {
		if(keyPress[code]) {
			keyPress[code] = false;
			return true;
		} else {
			return false;
		}
	}

	/* Interface FocusListener */
	public void focusGained(FocusEvent e) {
	}

	/* Interface FocusListener */
	public void focusLost(FocusEvent e) {
		onUnfocus();
	}
}

