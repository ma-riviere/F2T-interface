package structures;

/**
 * Bezier point
 * @author simon gay
 */

public class TargetPoint {

	public float x;
	public float y;
	
	public float p1x=0;
	public float p1y=0;
	public float p2x=0;
	public float p2y=0;
	
	public int speed;
	public int control;

	
	public TargetPoint(float x, float y, int speed, int control){
		this.x=x;
		this.y=y;
		this.speed=speed;
		this.control=control;
	}
	
	public TargetPoint(float x, float y, int speed, int control, float p1x, float p1y){
		this.x=x;
		this.y=y;
		this.speed=speed;
		this.control=control;
		this.p1x=p1x;
		this.p1y=p1y;
		this.p2x=-p1x;
		this.p2y=-p1y;
	}
	
	public TargetPoint(String[] args){
		this.x=Integer.parseInt(args[1]);
		this.y=Integer.parseInt(args[2]);
		this.speed=Integer.parseInt(args[3]);
		this.control=Integer.parseInt(args[4]);
		
		if (args.length>=7){
			this.p1x=Float.parseFloat(args[5]);
			this.p1y=Float.parseFloat(args[5]);
			this.p2x=-p1x;
			this.p2y=-p1y;
		}
	}
	

	public void resetBezier(){
		p1x=0;
		p1y=0;
		p2x=0;
		p2y=0;
	}
	
	public TargetPoint duplicate(){
		return new TargetPoint(x, y, speed, control, p1x, p1y);
	}
}
