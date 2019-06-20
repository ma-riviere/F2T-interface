
public class BPoint {

	public float x=0;
	public float y=0;
	
	public int range1;
	public int range2;
	public int speed;
	
	public float angle;
	public float dispersion;
	
	public float vx;
	public float vy;
	
	public BPoint(float px, float py, int range1, int range2, int speed, float a, float d){
		
		x=px;
		y=py;
		
		this.range1=range1;
		this.range2=range2;
		this.speed=speed;
		
		angle=a;
		dispersion=d;
		
		vx=0;
		vy=0;
	}
}
