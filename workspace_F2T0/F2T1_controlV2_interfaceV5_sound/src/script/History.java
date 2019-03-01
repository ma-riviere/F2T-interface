package script;

import java.util.ArrayList;

import main.Attractor;
import main.SoundSource;
import main.Target;

public class History {

	// elements to change or add
	public String image=null;
	public String tactile=null;
	public String flow=null;
	public String rail=null;
	public String area=null;
	public String magnetic=null;
	
	public ArrayList<Target> path;
	public ArrayList<SoundSource> sources;
	public ArrayList<Areas> areas;
	public ArrayList<Attractor> attractors;
	
	public String initialSound=null;
	
	// initial setup
	public boolean initialPause=false;
	public boolean clearPath=false;
	public boolean clearSources=false;
	public boolean clearAttractors=false;
	
	// area-sound associations
	public ArrayList<Integer> areasId;
	public ArrayList<Integer> areasTypes;		// 0 stop, 1 continue , 2 forced
	public ArrayList<Integer> areasRepeat;		// 0 no, 1 yes
	public ArrayList<String> soundFiles;
	
	// next history condition
	public ArrayList<Integer> condition_areas;
	public boolean condition_sound=false;
	public boolean condition_target=false;
	public boolean condition_button=false;
	public boolean condition=false;
	
	// exit door
	public ArrayList<Integer> exitAreasId;
	public ArrayList<String> ages;
	public ArrayList<Integer> reboot;
	
	public History(){
		path=new ArrayList<Target>();
		sources=new ArrayList<SoundSource>();
		areas=new  ArrayList<Areas>();
		condition_areas=new ArrayList<Integer>();
		attractors=new ArrayList<Attractor>();
		
		areasId=new ArrayList<Integer>();
		areasTypes=new ArrayList<Integer>();
		areasRepeat=new ArrayList<Integer>();
		soundFiles=new ArrayList<String>();
		
		condition_areas=new ArrayList<Integer>();
		
		exitAreasId=new ArrayList<Integer>();
		ages=new ArrayList<String>();
		reboot=new ArrayList<Integer>();
	}
	
	
	public void addArea(String[] args){
		areasId.add(Integer.parseInt(args[1]));
		areasTypes.add(Integer.parseInt(args[2]));
		areasRepeat.add(Integer.parseInt(args[3]));
		soundFiles.add(args[4]);
	}
	
	public void addInitialSound(String sound){
		initialSound=sound;
	}
	
	
	public void addExitDoor(String[] args){
		if (!condition_button){
			exitAreasId.add(Integer.parseInt(args[1]));
			ages.add(args[2]);
			reboot.add(Integer.parseInt(args[3]));
		}
	}

	public void addConditionArea(int id){
		if (!condition_button){
			condition_areas.add(id);
			condition=true;
		}
	}

	public void setConditionSound(){
		if (!condition_button){
			condition_sound=true;
			condition=true;
		}
	}
	public void setConditionTarget(){
		if (!condition_button){
			condition_target=true;
			condition=true;
		}
	}
	public void setConditionButton(){
		condition_button=true;
		condition_target=false;
		condition_sound=false;
		condition_areas.clear();
		condition=true;
		
		exitAreasId.clear();
		ages.clear();
		reboot.clear();
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	// display functions
	public String getImages(){
		String msg="";
		if (image!=null) msg+="img: "+image;
		else  msg+="img: none";
		if (tactile!=null) msg+=", tact: "+tactile;
		else  msg+=", tactile: none";
		if (flow!=null) msg+=", flow: "+flow;
		else  msg+=", flow: none";
		if (rail!=null) msg+=", rail: "+rail;
		else  msg+=", rail: none";
		if (area!=null) msg+=", area: "+area;
		else  msg+=", area: none";
		if (magnetic!=null) msg+=", magnet: "+magnetic;
		else  msg+=", magnetic: none";
		
		if (msg.length()>140) msg=msg.substring(0, 137)+" ...";
		
		return msg;
	}
	
	public String getAreas(){
		String msg="Sound areas: ";
		if (initialSound!=null) msg+="(init,"+initialSound+"), ";
		for (int a=0;a<areasId.size();a++){
			msg+="("+areasId.get(a)+", "+soundFiles.get(a)+"), ";
		}
		
		if (msg.length()>140) msg=msg.substring(0, 137)+" ...";
		
		return msg;
	}
	
	public String getPath(){
		String msg="Path: ("+path.size()+" points, play="+!initialPause+"), ";
		for (int t=0;t<path.size();t++){
			msg+="("+path.get(t).x+", "+path.get(t).y+")-";
		}
		if (msg.length()>140) msg=msg.substring(0, 137)+" ...";
		
		return msg;
	}
	
	public String getSources(){
		String msg="Sound sources: ("+sources.size()+" sources), ";
		for (int s=0;s<sources.size();s++){
			msg+="("+(int)sources.get(s).px+", "+(int)sources.get(s).py+"), ";
		}
		if (msg.length()>140) msg=msg.substring(0, 137)+" ...";
		
		return msg;
	}
	
	public String getAttractors(){
		String msg="Attractors: ("+attractors.size()+" points), ";
		for (int t=0;t<attractors.size();t++){
			msg+="("+attractors.get(t).x+", "+attractors.get(t).y+")-";
		}
		if (msg.length()>140) msg=msg.substring(0, 137)+" ...";
		
		return msg;
	}
	
	public String getCondition(){
		String msg="Exit conditions : ";
		for (int a=0;a<condition_areas.size();a++){
			msg+=condition_areas.get(a)+", ";
		}
		if (condition_sound) msg+="s ";
		if (condition_target)msg+="t ";
		if (condition_button) msg+="b";
		if (!condition) msg+=("forever");
		
		if (msg.length()>140) msg=msg.substring(0, 137)+" ...";
		
		return msg;
	}

	
	public String getDoor(){
		String msg="Exit doors : ";
		for (int a=0;a<exitAreasId.size();a++){
			msg+="("+exitAreasId.get(a)+", "+ages.get(a)+", "+reboot.get(a)+"), ";
		}
		
		if (msg.length()>140) msg=msg.substring(0, 137)+" ...";
		
		return msg;
	}
	
}
