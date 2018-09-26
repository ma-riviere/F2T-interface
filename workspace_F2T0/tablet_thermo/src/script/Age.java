package script;

import java.util.ArrayList;

import main.Main;


public class Age {

	private Areas areas;
	
	private ArrayList<Integer> condition_areas;
	
	private boolean condition_sound=false;
	
	private boolean forever=true;
	
	private String descriptor=null;
	private String image=null;
	
	
	public Age(Main m){
		areas=new Areas(m);
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
	
	public void setConditionSound(){
		condition_sound=true;
		forever=false;
	}
	
	public void setConditionTarget(){
		forever=false;
	}
	
	public String getDescriptor(){
		return descriptor;
	}
	
	public String getImage(){
		return image;
	}
	
	public boolean isComplete(){
		return (!forever && condition_areas.size()==0 && (!condition_sound || areas.isSoundComplete()));
	}
	
	
	public void close(){
		areas.close();
	}
}
