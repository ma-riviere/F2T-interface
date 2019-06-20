package modules;

import java.awt.Graphics;

import main.Main;
import main.Virtual;

public class Module {
	
	protected Main main;
	protected Virtual virtual;
	
	public String name;
	public int panelWidth=0;
	
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
