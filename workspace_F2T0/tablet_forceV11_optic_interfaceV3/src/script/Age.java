package script;

import java.util.ArrayList;

import main.Image;
import main.Main;
import main.SoundSource;
import main.Target;



public class Age {

	private Areas areas;
	
	public Image image;
	
	public ArrayList<Target> targetSequence;
	
	public ArrayList<SoundSource> sourceList;
	
	public ArrayList<Integer> condition_areas;
	
	public boolean default_play=true;
	
	private boolean condition_sound=false;
	private boolean condition_target=false;
	
	private boolean forever=true;
	
	
	public Age(Main m){
		image=new Image();
		areas=new Areas(this);
		targetSequence=new ArrayList<Target>();
		sourceList=new ArrayList<SoundSource>();
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
	
	public void addSource(String[] args){
		sourceList.add(new SoundSource(args));
	}
	
	public void addConditionArea(int id){
		condition_areas.add(id);
		forever=false;
	}
	
	public void setDescriptor(String file){
		image.setArea(file);
	}
	
	public void setImage(String file){
		image.setPicture(file);
	}
	
	public void setTactile(String file){
		image.setTactile(file);
	}
	
	public void setFlow(String file){
		image.setFlow(file);
	}
	
	public void setRail(String file){
		image.setRail(file);
	}
	
	public void setConditionSound(){
		condition_sound=true;
		forever=false;
	}
	
	public void setConditionTarget(){
		condition_target=true;
		forever=false;
	}
	
	public void setDefaultPlay(boolean play){
		default_play=play;
	}
	
	public boolean isComplete(){
		return (!forever && condition_areas.size()==0 && (!condition_sound || areas.isSoundComplete()) && (!condition_target || targetSequence.size()==0));
	}
	
	public void computeSources(float x, float y){
		for (int i=0;i<sourceList.size();i++){
			sourceList.get(i).compute(x, y);
		}
	}
	
	public void close(){
		areas.close();
	}
	
	
	/////////////////////////////////////
	// display functions
	public String getImages(){
		String msg="";
		if (image.view!=null) msg+="img : "+image.view;
		else  msg+="image : none";
		if (image.tactile!=null) msg+=", tact : "+image.tactile;
		else  msg+=", tactile : none";
		if (image.flow!=null) msg+=", flow : "+image.flow;
		else  msg+=", flow : none";
		if (image.rail!=null) msg+=", rail : "+image.rail;
		else  msg+=", rail : none";
		if (image.area!=null) msg+=", area : "+image.area;
		else  msg+=", area map : none";
		
		return msg;
	}
	
	public String getAreas(){
		String msg="Active areas : ";
		for (int a=0;a<areas.areas.size();a++){
			msg+="("+areas.areas.get(a)+", "+areas.soundFiles.get(a)+"), ";
		}
		
		return msg;
	}
	
	public String getPath(){
		String msg="Path sequence : ";
		for (int t=0;t<targetSequence.size();t++){
			msg+="("+targetSequence.get(t).x+", "+targetSequence.get(t).y+")-";
		}
		
		return msg;
	}
	
	public String getSources(){
		String msg="Sound sources : ";
		for (int s=0;s<sourceList.size();s++){
			msg+="("+sourceList.get(s).px+", "+sourceList.get(s).py+"), ";
		}
		
		return msg;
	}
	
	public String getCondition(){
		String msg="Exit conditions : ";
		for (int a=0;a<condition_areas.size();a++){
			msg+=condition_areas.get(a)+", ";
		}
		if (condition_sound) msg+="s ";
		if (condition_target)msg+="t";
		
		return msg;
	}
	
	
}
