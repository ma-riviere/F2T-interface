

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
	
	public void compute(float px, float py){
		
		float dist=(float) Math.sqrt( (x-px)*(x-px) + (y-py)*(y-py) );
		
		if (dist<=10){
			reached=true;
			offsetX=0;
			offsetY=0;
		}
		else{
			offsetX=(int)(speed*(x-px)/dist);
			offsetY=(int)(speed*(y-py)/dist);
		}
	}
	
	
	public Target duplicate(){
		return new Target(x, y, speed, control);
	}
	
}
