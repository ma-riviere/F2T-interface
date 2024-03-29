package structures;

import java.util.ArrayList;

/**
 * Bezier curve for path edition
 * @author simon gay
 */

public class BezierCurvePath {

	public ArrayList<TargetPoint> targetList;
	public ArrayList<TargetBP> pointList;
	
	public int offsetX=0;
	public int offsetY=0;
	
	
	public BezierCurvePath(){
		targetList=new ArrayList<TargetPoint>();
		pointList=new  ArrayList<TargetBP>();
	}
	
	
	public void compute(float px, float py){
		
		if (pointList.size()>0 ){
			float dist1=(float) Math.sqrt( (pointList.get(0).x-px)*(pointList.get(0).x-px) + (pointList.get(0).y-py)*(pointList.get(0).y-py) );

			if (dist1<=1){
				pointList.remove(0);
				
				if (pointList.size()==0){
					offsetX=0;
					offsetY=0;
				}
			}
			else{
				offsetX=(int)(pointList.get(0).speed*(pointList.get(0).x-px)/dist1);
				offsetY=(int)(pointList.get(0).speed*(pointList.get(0).y-py)/dist1);
			}
			
			
		}
		if (targetList.size()>0 ){
			float dist2=(float) Math.sqrt( (targetList.get(0).x-px)*(targetList.get(0).x-px) + (targetList.get(0).y-py)*(targetList.get(0).y-py) );
			if (dist2<=5) targetList.remove(0);
		}
	}
	
	
	
	public void add(TargetPoint target){
		targetList.add(target);
		if (targetList.size()==1) pointList.add(new TargetBP(target.x, target.y, target.control, target.speed));
		else setBezier(targetList.get(targetList.size()-2), target);	
	}
	
	
	public void add(int id, TargetPoint target){
		targetList.add(id, target);
		pointList.clear();
		pointList.add(new TargetBP(targetList.get(0).x, targetList.get(0).y, targetList.get(0).control, targetList.get(0).speed));
		for (int i=1;i<targetList.size();i++){
			setBezier(targetList.get(i-1), targetList.get(i));
		}
	}
	
	public void remove(int id){
		targetList.remove(id);
		pointList.clear();
		if (targetList.size()>0){
			pointList.add(new TargetBP(targetList.get(0).x, targetList.get(0).y, targetList.get(0).control, targetList.get(0).speed));
			for (int i=1;i<targetList.size();i++){
				setBezier(targetList.get(i-1), targetList.get(i));
			}
		}
	}
	
	public void set(){
		pointList.clear();
		if (targetList.size()>0){
			pointList.add(new TargetBP(targetList.get(0).x, targetList.get(0).y, targetList.get(0).control, targetList.get(0).speed));
			for (int i=1;i<targetList.size();i++){
				setBezier(targetList.get(i-1), targetList.get(i));
			}
		}
	}
	
	public void setBezier(int id, int bx, int by){
		targetList.get(id).p1x=bx-targetList.get(id).x;
		targetList.get(id).p1y=by-targetList.get(id).y;
		targetList.get(id).p2x=-targetList.get(id).p1x;
		targetList.get(id).p2y=-targetList.get(id).p1y;
		set();
	}
	
	public void resetBezier(int id){
		targetList.get(id).p1x=0;
		targetList.get(id).p1y=0;
		targetList.get(id).p2x=0;
		targetList.get(id).p2y=0;
		set();
	}
	
	private void setBezier(TargetPoint t1, TargetPoint t2){
		if (t2.control==0){
			pointList.add(new TargetBP(t2.x, t2.y, t2.control, t2.speed));
		}
		else{
			float bx=0;
			float by=0;
			
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
				
				pointList.add(new TargetBP(bx,by,t2.control, t2.speed));
			}
		}
	}
	
	
	public int sizeTarget(){ return targetList.size();}
	public int sizePoint(){ return pointList.size();}
	
	public String getString(int i){
		if (targetList.get(i).p1x==0 && targetList.get(i).p1y==0)
			return (int)targetList.get(i).x+" "+(int)targetList.get(i).y+" "+targetList.get(i).speed+" "+targetList.get(i).control;
		else
			return (int)targetList.get(i).x+" "+(int)targetList.get(i).y+" "+targetList.get(i).speed+" "+targetList.get(i).control+" "+targetList.get(i).p1x+" "+targetList.get(i).p1y;
	}
	
	public int getControl(int i){
		return pointList.get(i).control;
	}
	
	public void clear(){
		targetList.clear();
		pointList.clear();
	}
}
