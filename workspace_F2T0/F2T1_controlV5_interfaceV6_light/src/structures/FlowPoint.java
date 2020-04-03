package structures;

/**
 * Bezier point for flows and rails
 * @author simon gay
 */

public class FlowPoint {


	public int x=0;
	public int y=0;
	
	public int p1x=0;
	public int p1y=0;
	
	public int p2x=0;
	public int p2y=0;
	
	public int range1;
	public int range2;
	public int speed;
	
	public float angle;
	public float dispersion;
	
	
	public FlowPoint(int px, int py, int range1, int range2, int speed){
		x=px;
		y=py;
		p1x=0;
		p1y=0;
		p2x=0;
		p2y=0;
		this.range1=range1;
		this.range2=range2;
		this.speed=speed;
		angle=0;
		dispersion=0;
	}
	
	public FlowPoint(int px, int py, int range1, int range2, int speed, int px1, int py1){
		x=px;
		y=py;
		p1x=px1;
		p1y=py1;
		p2x=-px1;
		p2y=-py1;
		this.range1=range1;
		this.range2=range2;
		this.speed=speed;
		angle=0;
		dispersion=0;
	}
	
	public FlowPoint(int px, int py, int range1, int range2, int speed, int px1, int py1, float a, float d){
		x=px;
		y=py;
		p1x=px1;
		p1y=py1;
		p2x=-px1;
		p2y=-py1;
		this.range1=range1;
		this.range2=range2;
		this.speed=speed;
		angle=a;
		dispersion=d;
	}
	
	
	public FlowPoint duplicate(){
		return new FlowPoint(x, y, range1, range2, speed, p1x, p1y, angle, dispersion);
	}
	
	public void setBezier(int bx, int by){
		p1x=bx-x;
		p1y=by-y;
		p2x=-p1x;
		p2y=-p1y;
	}
	
	public void resetBezier(){
		p1x=0;
		p1y=0;
		p2x=0;
		p2y=0;
	}
}
