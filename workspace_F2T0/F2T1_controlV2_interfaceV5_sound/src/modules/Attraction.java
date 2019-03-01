package modules;

import main.Main;

public class Attraction {

	private Main main;
	
	private float x=0;
	private float y=0;
	
	public Attraction(Main m){
		main=m;
	}
	
	public void compute(){
		
		x=0;
		y=0;
		
		float d2=0;
		float d=0;
		
		float px=main.px;
		float py=main.py;
		
		float ax;
		float ay;
		
		float aDistance;
		float aStrength;
		
		float force=0;
		
		for (int i=0;i<main.currentAge.attractorList.size();i++){
			
			ax=main.currentAge.attractorList.get(i).x;
			ay=main.currentAge.attractorList.get(i).y;
			
			aDistance=main.currentAge.attractorList.get(i).distance;
			aStrength=main.currentAge.attractorList.get(i).strength;
			
			d2= (px-ax)*(px-ax) + (py-ay)*(py-ay);
			
			if (d2>10 && d2<aDistance*aDistance){
				
				d=(float)Math.sqrt(d2);
				if (d>aDistance) d=aDistance;
				
				if (main.currentAge.attractorList.get(i).type==0){ // linear

					force=aStrength*(aDistance-d)/aDistance;

					x-= force * (px-ax)/d;
					y-= force * (py-ay)/d;	
				}
				else if (main.currentAge.attractorList.get(i).type==1){ // constant
					
					force = aStrength;
					
					x-= force * (px-ax)/d;
					y-= force * (py-ay)/d;	
				}
				else if (main.currentAge.attractorList.get(i).type==2){ // inverse

					force=aStrength/(d/aDistance);
					
					x-= force * (px-ax)/d;
					y-= force * (py-ay)/d;	
				}
				
				else if (main.currentAge.attractorList.get(i).type==3){ // round

					float dist=d/aDistance;
					
					force= aStrength * (float)Math.sqrt( 1 - dist*dist);

					x-= force * (px-ax)/d;
					y-= force * (py-ay)/d;	
				}
				
			}
			
		}
		
		main.mX+=x;
		main.mY+=y;
	}
}
