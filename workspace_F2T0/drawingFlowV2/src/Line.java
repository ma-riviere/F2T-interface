import java.util.ArrayList;


public class Line {

	public ArrayList<Target> targetList;
	public ArrayList<BPoint> pointList;
	
	public Line(){
		targetList=new ArrayList<Target>();
		pointList=new ArrayList<BPoint>();
	}


	public Line duplicate(){
		Line ret=new Line();
		for (int i=0;i<targetList.size();i++){
			ret.targetList.add(targetList.get(i).duplicate());
		}
		return ret;
	}
	
	
	public int sizeTarget(){
		return targetList.size();
	}
	public int sizePoint(){
		return pointList.size();
	}
	
	
	// add a target point to the list
	/*public void add(Target target){
		targetList.add(target);
		if (targetList.size()==1) pointList.add(new BPoint(target.x, target.y, target.range1, target.range2, target.speed));
		else setBezier(targetList.get(targetList.size()-2), target);
	}
	
	// insert target point
	public void add(int id, Target target){
		targetList.add(id, target);
		pointList.clear();
		pointList.add(new BPoint(targetList.get(0).x, targetList.get(0).y, targetList.get(0).range1, targetList.get(0).range2, targetList.get(0).speed));
		for (int i=1;i<targetList.size();i++){
			setBezier(targetList.get(i-1), targetList.get(i));
		}
	}
	
	public void remove(int id){
		targetList.remove(id);
		pointList.clear();
		for (int i=1;i<targetList.size();i++){
			setBezier(targetList.get(i-1), targetList.get(i));
		}
	}*/
	
	// generate the sequence of bezier points
	public void update(){
		pointList.clear();
		for (int i=1;i<targetList.size();i++){
			setBezier(targetList.get(i-1), targetList.get(i));
		}
	}
	
	// set the bezier parameters of a ytarget point
	public void changeBezier(int id, int x, int y){
		targetList.get(id).setBezier(x, y);
		pointList.clear();
		for (int i=1;i<targetList.size();i++){
			setBezier(targetList.get(i-1), targetList.get(i));
		}
	}
	
	//////////////////////////////////////////////////
	// compute bezier points between two target points
	private void setBezier(Target t1, Target t2){
		float bx=0;
		float by=0;
		int speed=0;
		int range1=0;
		int range2=0;
		float angle=0;
		float dispersion=0;
		
		float x1=t1.x;
		float y1=t1.y;
		float x2=x1+t1.p1x;
		float y2=y1+t1.p1y;
		
		float x4=t2.x;
		float y4=t2.y;
		float x3=x4+t2.p2x;
		float y3=y4+t2.p2y;
		
		int nb=(int)Math.max(Math.abs(x1-x4), Math.abs(y1-y4))/2;
		float delta=1/(float)nb;
		
		for (float t=0;t<=1;t+=delta){
			
			bx = (1-t)*(1-t)*(1-t)*x1 + 3*t*(1-t)*(1-t)*x2  + 3*t*t*(1-t)*x3 + t*t*t*x4;
			by = (1-t)*(1-t)*(1-t)*y1 + 3*t*(1-t)*(1-t)*y2  + 3*t*t*(1-t)*y3 + t*t*t*y4;
			
			speed=(int)(t1.speed*(1-t) + t2.speed*t);
			range1=(int)(t1.range1*(1-t) + t2.range1*t);
			range2=(int)(t1.range2*(1-t) + t2.range2*t);
			
			angle=(t1.angle*(1-t) + t2.angle*t);
			dispersion=(t1.dispersion*(1-t) + t2.dispersion*t);
			
			pointList.add(new BPoint(bx,by,range1, range2, speed, angle, dispersion));
		}
		
		
		// set direction
		if (pointList.size()>0){
			// first point
			float d=(float) Math.sqrt( targetList.get(0).p1x*targetList.get(0).p1x + targetList.get(0).p1y*targetList.get(0).p1y);
			if (d==0){
				pointList.get(0).vx=0;
				pointList.get(0).vy=0;
			}
			else{
				pointList.get(0).vx=targetList.get(0).p1x/d;
				pointList.get(0).vy=targetList.get(0).p1y/d;
			}
			
			// points in the list
			float dx=0;
			float dy=0;
			for (int i=1;i<pointList.size()-1;i++){
				dx=pointList.get(i+1).x-pointList.get(i-1).x;
				dy=pointList.get(i+1).y-pointList.get(i-1).y;
				d=(float)Math.sqrt(dx*dx + dy*dy);
				
				if (d==0){
					pointList.get(i).vx=0;
					pointList.get(i).vy=0;
				}
				else{
					pointList.get(i).vx=dx/d;
					pointList.get(i).vy=dy/d;
				}
			}
			
			// last point
			d=(float) Math.sqrt( targetList.get(targetList.size()-1).p1x*targetList.get(targetList.size()-1).p1x 
							   + targetList.get(targetList.size()-1).p1y*targetList.get(targetList.size()-1).p1y);
			if (d==0){
				pointList.get(pointList.size()-1).vx=0;
				pointList.get(pointList.size()-1).vy=0;
			}
			else{
				pointList.get(pointList.size()-1).vx=targetList.get(targetList.size()-1).p1x/d;
				pointList.get(pointList.size()-1).vy=targetList.get(targetList.size()-1).p1y/d;
			}
		}
	}
	
}
