package script;

import java.util.ArrayList;

import main.Main;
import main.Target;



public class Age {

	private Areas areas;
	
	private ArrayList<Target> targetSequence;
	
	private ArrayList<Integer> condition_areas;
	
	private boolean condition_sound=false;
	private boolean condition_target=false;
	
	private boolean forever=true;
	
	private String descriptor=null;
	private String image=null;
	private String tactile=null;
	private String flow=null;
	private String rail=null;
	
	
	public Age(Main m){
		areas=new Areas(m);
		targetSequence=new ArrayList<Target>();
		condition_areas=new ArrayList<Integer>();
	}
	
	public void detect(int i, int j){
		areas.detect(i, j);
		
		if (areas.isEntering()){
			int a=0;
			boolean found=false;
			while (!found && a<condition_areas.size()){
				if (condition_areas.get(a)==areas.getCurrentArea()){
					condition_areas.remove(a);
					found=true;
				}
				else a++;
			}
		}
	}
	
	public void addArea(int id, int type, int r, String file){
		areas.addArea(id, type, r, file);
	}
	
	public void addTarget(int x, int y, int speed, int type){
		targetSequence.add(new Target(x,y,speed,type));
	}
	
	public void addAreaCondition(int id){
		condition_areas.add(id);
		forever=false;
	}
	
	public void setDescriptor(String file){
		descriptor=file;
	}
	
	public void setImage(String file){
		image=file;
	}
	
	public void setTactile(String file){
		tactile=file;
	}
	
	public void setFlow(String file){
		flow=file;
	}
	
	public void setRail(String file){
		rail=file;
	}
	
	public void setConditionSound(){
		condition_sound=true;
		forever=false;
	}
	
	public void setConditionTarget(){
		condition_target=true;
		forever=false;
	}
	
	public String getDescriptor(){
		return descriptor;
	}
	
	public String getImage(){
		return image;
	}
	
	public String getTactile(){
		return tactile;
	}
	
	public String getFlow(){
		return flow;
	}
	
	public String getRail(){
		return rail;
	}
	
	public ArrayList<Target> getTargetList(){
		return targetSequence;
	}
	
	public boolean isComplete(int targetListSize){
		return (!forever && condition_areas.size()==0 && (!condition_sound || areas.isSoundComplete()) && (!condition_target || targetListSize==0));
	}
	
	
	public void close(){
		areas.close();
	}
}
