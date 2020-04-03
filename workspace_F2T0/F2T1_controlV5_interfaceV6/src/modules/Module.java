package modules;

import java.awt.Graphics;

import main.Main;
import main.Virtual;

/**
 * Generic class of control module. The control module is a set of functions that generates offset in joystick position control to simulate tactile effects.
 * @author simon gay
 */

public class Module {
	
	protected Main main;
	protected Virtual virtual;
	
	public String name;
	public int panelWidth=0;
	
	public float offsetX=0;
	public float offsetY=0;
	
	public Module(Main m, Virtual v){
		main=m;
		virtual=v;
		name="generic module";
	}

	public void compute(){
		
	}
	
	public void compute2(){
		
	}
	
	public float getValue(int i){
		return 0;
	}
	
	public void paint(Graphics g, int x){
		
	}
}
