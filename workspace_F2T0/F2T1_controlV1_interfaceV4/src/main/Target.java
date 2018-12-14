package main;

public class Target {

	public int x;
	public int y;
	
	public int offsetX;
	public int offsetY;
	
	public int speed;
	
	public int control;
	
	public boolean reached=false;
	
	public Target(int x, int y, int speed, int control){
		this.x=x;
		this.y=y;
		this.speed=speed;
		this.control=control;
	}
	
	public Target(String[] args){
		this.x=Integer.parseInt(args[1]);
		this.y=Integer.parseInt(args[2]);
		this.speed=Integer.parseInt(args[3]);
		this.control=Integer.parseInt(args[4]);
	}
	
	public void compute(float px, float py){
		
		float dist=(float) Math.sqrt( (x-px)*(x-px) + (y-py)*(y-py) );
		
		if (dist<=20){
			reached=true;
			offsetX=0;
			offsetY=0;
		}
		else{
			offsetX=(int)(speed*(x-px)/dist);
			offsetY=(int)(speed*(y-py)/dist);
		}
		
	}
	
}
