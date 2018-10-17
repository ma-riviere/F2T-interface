package display;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.Main;

public class KeyboardListener  implements KeyListener {

	
	private int keyPressed=-1;
	boolean pressed=false;
	
	public KeyboardListener(){
		keyPressed=-1;
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
	    	pressed=true;
    	}
	}

	public void keyReleased(KeyEvent e) {
		keyPressed=-1;
    	pressed=false;
	}
	
	

}
