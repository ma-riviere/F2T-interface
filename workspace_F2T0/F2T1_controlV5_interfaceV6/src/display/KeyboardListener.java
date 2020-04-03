package display;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.Main;

/**
 * simple keyboard listener
 * @author simon gay
 */

public class KeyboardListener  implements KeyListener {

	
	private int keyPressed=-1;
	private boolean pauseKey=false;
	boolean pressed=false;
	
	public boolean up=false;
	public boolean down=false;
	public boolean left=false;
	public boolean right=false;
	
	public KeyboardListener(){
		keyPressed=-1;
	}
	
	
	public boolean getPausePressed(){
		boolean ret=pauseKey;
		pauseKey=false;
		return ret;
	}
	
	public int getKeyPressed(){
		int ret=keyPressed;
		keyPressed=-1;
		return ret;
	}
	
	public void keyTyped(KeyEvent e) {}

	public void keyPressed(KeyEvent e) {
		if (!pressed){
	    	if (Main.displayKey) System.out.println("Key pressed : " + e.getKeyCode() +" (" + e.getKeyChar() + ")");
	    	keyPressed=e.getKeyCode();
	    	if (keyPressed==Main.pauseKey1 || keyPressed==Main.pauseKey2 || keyPressed==Main.pauseKey3) pauseKey=true;
	    	pressed=true;
    	}
		
		if (e.getKeyCode() == KeyEvent.VK_Z)    up=true;
		if (e.getKeyCode() == KeyEvent.VK_S)  down=true;
		if (e.getKeyCode() == KeyEvent.VK_Q)  left=true;
		if (e.getKeyCode() == KeyEvent.VK_D) right=true;
	}

	public void keyReleased(KeyEvent e) {
		keyPressed=-1;
    	pressed=false;
    	
    	if (e.getKeyCode() == KeyEvent.VK_Z)    up=false;
		if (e.getKeyCode() == KeyEvent.VK_S)  down=false;
		if (e.getKeyCode() == KeyEvent.VK_Q)  left=false;
		if (e.getKeyCode() == KeyEvent.VK_D) right=false;
	}
	
	

}
