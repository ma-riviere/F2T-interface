package modules;

import main.Main;

public class Frictions{

	// tactile property values
	public float friction_fluid_read=0;
	public float friction_solid_read=0;
	
	private Main main;
	
	public Frictions(Main m){
		main=m;
	}

	public void compute(){
		
		friction_fluid_read=(float)main.currentAge.image.tactile_mat[Math.min(699,Math.max(0,350+(int)(main.px)))][Math.min(699,Math.max(0,350-(int)(main.py)))][0]/255;
		friction_solid_read=(float)main.currentAge.image.tactile_mat[Math.min(699,Math.max(0,350+(int)(main.px)))][Math.min(699,Math.max(0,350-(int)(main.py)))][2]/255;
		
		
		if (friction_fluid_read>0){
			main.mX= main.mX * (1-friction_fluid_read*0.9f);
			main.mY= main.mY * (1-friction_fluid_read*0.9f);
		}
		
		if (friction_solid_read>0){
			float force_norm= (float)Math.sqrt(main.mX*main.mX+main.mY*main.mY);

			System.out.println(main.mX+" , "+main.mY+" ; "+force_norm);
			
			if (force_norm>0){
				if (force_norm<friction_solid_read*255){
					main.mX=0;
					main.mY=0;
				}
				else{
					main.mX-= 255*friction_solid_read * main.mX/force_norm;
					main.mY-= 255*friction_solid_read * main.mY/force_norm;
				}
			}
		}
	}
	
	
	public float getValue(int i){
		if (i==0) return friction_fluid_read;
		else if (i==1) return friction_solid_read;
		else return 0;
	}
	
}
