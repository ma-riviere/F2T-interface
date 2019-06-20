package modules;

import java.awt.Graphics;

import main.Main;
import main.Virtual;

public class Frictions extends Module{

	// tactile property values
	public float friction_fluid_read=0;
	public float friction_solid_read=0;
	
	public Frictions(Main m, Virtual v){
		super(m,v);
		name="friction";
		panelWidth=90;
	}

	public void compute(){
		
		friction_fluid_read=(float)main.currentAge.image.tactile_mat[Math.min(699,Math.max(0,350+(int)(virtual.px)))][Math.min(699,Math.max(0,350-(int)(virtual.py)))][0]/255;
		friction_solid_read=(float)main.currentAge.image.tactile_mat[Math.min(699,Math.max(0,350+(int)(virtual.px)))][Math.min(699,Math.max(0,350-(int)(virtual.py)))][2]/255;
		
		
		if (friction_fluid_read>0){
			virtual.mx_temp= virtual.mx_temp * (1-friction_fluid_read*0.9f);
			virtual.my_temp= virtual.my_temp * (1-friction_fluid_read*0.9f);
		}
		
		if (friction_solid_read>0){
			float force_norm= (float)Math.sqrt(virtual.mx_temp*virtual.mx_temp+virtual.my_temp*virtual.my_temp);

			if (force_norm>0){
				if (force_norm<friction_solid_read*255){
					virtual.mx_temp=0;
					virtual.my_temp=0;
				}
				else{
					virtual.mx_temp-= 255*friction_solid_read * virtual.mx_temp/force_norm;
					virtual.my_temp-= 255*friction_solid_read * virtual.my_temp/force_norm;
				}
			}
		}
	}
	
	public void compute2(){
		
		friction_fluid_read=(float)main.currentAge.image.tactile_mat[Math.min(699,Math.max(0,350+(int)(main.x)))][Math.min(699,Math.max(0,350-(int)(main.y)))][0]/255;
		friction_solid_read=(float)main.currentAge.image.tactile_mat[Math.min(699,Math.max(0,350+(int)(main.x)))][Math.min(699,Math.max(0,350-(int)(main.y)))][2]/255;
		
		
		if (friction_fluid_read>0){
			main.vmX= main.vmX * (1-friction_fluid_read*0.9f);
			main.vmY= main.vmY * (1-friction_fluid_read*0.9f);
		}
		
		if (friction_solid_read>0){
			float force_norm= (float)Math.sqrt(main.vmX*main.vmX+main.vmY*main.vmY);

			if (force_norm>0){

				if (force_norm<friction_solid_read*255*0.9f){
					main.vmX=0;
					main.vmY=0;
				}
				else{
					main.vmX-= 255*friction_solid_read * main.vmX/force_norm;
					main.vmY-= 255*friction_solid_read * main.vmY/force_norm;
				}
			}
		}
	}
	
	
	
	public float getValue(int i){
		if (i==0) return friction_fluid_read;
		else if (i==1) return friction_solid_read;
		else return 0;
	}
	
	
	public void paint(Graphics g, int x){
		
		String msg="";
		
		int val=(int)(friction_fluid_read*100);
		if (val==100) msg="100";
		else if (val>=10) msg=" "+val;
		else msg="  "+val;
		g.drawString("fluid : "+msg+" %", 7+x, 15);
		
		val=(int)(friction_solid_read*100);
		if (val==100) msg="100";
		else if (val>=10) msg=" "+val;
		else msg="  "+val;
		g.drawString("solid: "+msg+" %", 7+x, 30);
	}
	
}
