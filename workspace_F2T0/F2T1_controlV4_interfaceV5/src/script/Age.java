package script;


import java.util.ArrayList;

import main.Image;
import main.Main;
import main.SoundSource;
import main.Attractor;


public class Age {

	private Main main;
	
	public String name;
	
	// position for display purposes
	public float px=-1;
	public float py=-1;
	public ArrayList<Integer> connections;
	
	// sequence of periods
	public ArrayList<History> history;
	public int current_period=-1;
	
	// current properties
	public Image image;
	public InitialSound initialSound;
	public Areas areas;
	public BezierCurve targetSequence;
	public ArrayList<SoundSource> sourceList;
	public ArrayList<Attractor> attractorList;
	
	// areas
	private int previous_area_red=-1;
	public int current_area_red=-1;
	private int previous_area_green=-1;
	public int current_area_green=-1;
	private int previous_area_blue=-1;
	public int current_area_blue=-1;
	
	// next period condition
	private ArrayList<Integer> condition_areas;
	private boolean condition_sound=false;
	private boolean condition_target=false;
	private boolean condition_button=false;
	private boolean condition=false;
	
	
	// exit door
	public ArrayList<Integer> exitAreasId;
	public ArrayList<String> ages;
	public ArrayList<Integer> reboot;
	
	public int exitIndex=-1;
	
	public boolean initialized=false;
	
	
	public Age(Main m, String n){
		
		main=m;
		
		name=n;
		
		image=new Image(main);
		initialSound=new InitialSound();
		areas=new Areas();
		targetSequence=new BezierCurve();
		sourceList=new ArrayList<SoundSource>();
		condition_areas=new ArrayList<Integer>();
		attractorList=new ArrayList<Attractor>();
		
		exitAreasId=new ArrayList<Integer>();
		ages=new ArrayList<String>();
		reboot=new ArrayList<Integer>();
		
		history=new ArrayList<History>();
		history.add(new History());
		current_period=-1;
		
		connections=new ArrayList<Integer>();
	}
	
	
	public void resetAge(){
		
		initialized=true;
		
		current_period=0;
		
		// clear age
		setPicture(null);
		setTactile(null);
		setFlow(null);
		setRail(null);
		setArea(null);
		
		if (history.get(0).image   !=null && !history.get(0).image.equals("none")  ) setPicture(history.get(0).image);
		if (history.get(0).tactile !=null && !history.get(0).tactile.equals("none")) setTactile(history.get(0).tactile);
		if (history.get(0).flow    !=null && !history.get(0).flow.equals("none")   ) setFlow(history.get(0).flow);
		if (history.get(0).rail    !=null && !history.get(0).rail.equals("none")   ) setRail(history.get(0).rail);
		if (history.get(0).area    !=null && !history.get(0).area.equals("none")   ) setArea(history.get(0).area);
		if (history.get(0).magnetic!=null && !history.get(0).magnetic.equals("none")   ) setMagnetic(history.get(0).magnetic);
		
		areas.close();
		for (int i=0;i<history.get(0).areasId.size();i++){
			areas.addArea(history.get(0).areasId.get(i), history.get(0).areasTypes.get(i), history.get(0).areasRepeat.get(i), history.get(0).soundFiles.get(i));
		}
		
		targetSequence.clear();
		sourceList.clear();
		attractorList.clear();
		for (int i=0;i<history.get(0).path.size()      ;i++) targetSequence.add(history.get(0).path.get(i));
		for (int i=0;i<history.get(0).sources.size()   ;i++) sourceList.add(history.get(0).sources.get(i));
		for (int i=0;i<history.get(0).attractors.size();i++) attractorList.add(history.get(0).attractors.get(i));
		
		condition_areas.clear();
		for (int i=0;i<history.get(0).condition_areas.size();i++) condition_areas.add(history.get(0).condition_areas.get(i));
		condition_sound=history.get(0).condition_sound;
		condition_target=history.get(0).condition_target;
		condition_button=history.get(0).condition_button;
		condition=condition_sound || condition_target || condition_button || (condition_areas.size()>0);

		initialSound.removeSound();
		if (history.get(0).initialSound!=null) initialSound.addInitialSound(history.get(0).initialSound);
		
		exitAreasId.clear();
		ages.clear();
		reboot.clear();
		for (int i=0;i<history.get(0).exitAreasId.size();i++) exitAreasId.add(history.get(0).exitAreasId.get(i));
		for (int i=0;i<history.get(0).ages.size();i++) ages.add(history.get(0).ages.get(i));
		for (int i=0;i<history.get(0).reboot.size();i++) reboot.add(history.get(0).reboot.get(i));
		
		main.target_pause=history.get(0).initialPause;
		main.display.updateLinks();
	}
	
	
	public void setNextPeriod(){
		
		areas.close();
		initialSound.close();
		
		current_period++;
	
		// update images
		if (history.get(current_period).image  !=null){
			if (history.get(current_period).image.equals("none")) setPicture(null);
			else setPicture(history.get(current_period).image);
		}
		if (history.get(current_period).tactile!=null){
			if (history.get(current_period).tactile.equals("none")) setTactile(null);
			else setTactile(history.get(current_period).tactile);
		}
		if (history.get(current_period).flow!=null){
			if (history.get(current_period).flow.equals("none")) setFlow(null);
			else setFlow(history.get(current_period).flow);
		}
		if (history.get(current_period).rail!=null){
			if (history.get(current_period).rail.equals("none")) setRail(null);
			else setRail(history.get(current_period).rail);
		}
		if (history.get(current_period).area!=null){
			if (history.get(current_period).area.equals("none")) setArea(null);
			else setArea(history.get(current_period).area);
		}
		if (history.get(current_period).magnetic!=null){
			if (history.get(current_period).magnetic.equals("none")) setMagnetic(null);
			else setMagnetic(history.get(current_period).magnetic);
		}
		
		// clear areas and load news
		areas.close();
		for (int i=0;i<history.get(current_period).areasId.size();i++){
			areas.addArea(history.get(current_period).areasId.get(i),
						  history.get(current_period).areasTypes.get(i),
						  history.get(current_period).areasRepeat.get(i),
						  history.get(current_period).soundFiles.get(i));
		}
		
		if (history.get(current_period).clearPath) targetSequence.clear();
		if (history.get(current_period).clearSources) sourceList.clear();
		if (history.get(current_period).clearAttractors) attractorList.clear();
		for (int i=0;i<history.get(current_period).path.size()      ;i++) targetSequence.add(history.get(current_period).path.get(i));
		for (int i=0;i<history.get(current_period).sources.size()   ;i++) sourceList.add(history.get(current_period).sources.get(i));
		for (int i=0;i<history.get(current_period).attractors.size();i++) attractorList.add(history.get(current_period).attractors.get(i));
		
		condition_areas.clear();
		for (int i=0;i<history.get(current_period).condition_areas.size();i++) condition_areas.add(history.get(current_period).condition_areas.get(i));
		condition_sound=history.get(current_period).condition_sound;
		condition_target=history.get(current_period).condition_target;
		condition_button=history.get(current_period).condition_button;
		condition=condition_sound || condition_target || condition_button || (condition_areas.size()>0);

		initialSound.removeSound();
		if (history.get(current_period).initialSound!=null) initialSound.addInitialSound(history.get(current_period).initialSound);

		exitAreasId.clear();
		ages.clear();
		reboot.clear();
		for (int i=0;i<history.get(current_period).exitAreasId.size();i++) exitAreasId.add(history.get(current_period).exitAreasId.get(i));
		for (int i=0;i<history.get(current_period).ages.size();i++) ages.add(history.get(current_period).ages.get(i));
		for (int i=0;i<history.get(current_period).reboot.size();i++) reboot.add(history.get(current_period).reboot.get(i));
		

		main.target_pause=history.get(current_period).initialPause;
		
		main.display.updateLinks();
	}
	
	
	
	
	public void play(int i, int j){
		
		if (!initialized) this.resetAge();
		
		// compute sound sources
		for (int s=0;s<sourceList.size();s++){
			sourceList.get(s).compute(i-350, j-350);
		}
		
		
		// detect when entering an area
		previous_area_red=current_area_red;
		current_area_red=image.area_mat[i][j][0];
		
		previous_area_green=current_area_green;
		current_area_green=image.area_mat[i][j][1];
		
		previous_area_blue=current_area_blue;
		current_area_blue=image.area_mat[i][j][2];
		
		
		// remove conditions
		if (!initialSound.isSoundComplete()){
			initialSound.playInitial();
		}

		if (current_area_red==0 && current_area_green==0 && current_area_blue==0){	// case area 0
			if (previous_area_red!=0 || previous_area_green!=0 || previous_area_blue!=0){
				removeConditionArea(0);
			}
		}
		else{	// case other areas
			if (current_area_red!=0 && current_area_red!=previous_area_red){
				removeConditionArea(current_area_red);
			}
			if (current_area_green!=0 && current_area_green!=previous_area_green){
				removeConditionArea(current_area_green+25);
			}
			if (current_area_blue!=0 && current_area_blue!=previous_area_blue){
				removeConditionArea(current_area_blue+50);
			}
		}
		
		// play sounds if needed
		areas.detect(current_area_red, current_area_green, current_area_blue);

		
		if (isComplete()){		// next period
			exitIndex=-1;
			setNextPeriod();
		}
		else{
			// define exit door
			int a=0;
			boolean found=false;
			
			
			while (!found && a<ages.size()){
				if (current_area_red>0 && current_area_red==exitAreasId.get(a)) found=true;
				else if (current_area_green>0 && current_area_green+25==exitAreasId.get(a)) found=true;
				else if (current_area_blue>0 && current_area_blue +50==exitAreasId.get(a)) found=true;
				else a++;
			}
			
			if (!found && current_area_red==0 && current_area_green==0 && current_area_blue==0){	// case area 0
				a=0;
				while (!found && a<ages.size()){
					if (exitAreasId.get(a)==0) found=true;
					else a++;
				}
			}
			
			if (found) exitIndex=a;
			else exitIndex=-1;
		}
	}
	
	
	
	private void removeConditionArea(int id){
		int a=0;
		boolean found=false;
		while (!found && a<condition_areas.size()){
			if (condition_areas.get(a)==id){
				condition_areas.remove(a);
				found=true;
			}
			else a++;
		}
	}
	
	
	
	public boolean isComplete(){
		
		int key=0;
		if (condition_button) key=main.display.keyboard.getKeyPressed();

		return ( condition
			&& (!condition_button || (key==Main.button1 || key==Main.button2 || key==Main.button3) )
			&&   condition_areas.size()==0 
		    && (!condition_sound || (areas.isSoundComplete() && initialSound.isSoundComplete()))
		    && (!condition_target || targetSequence.sizeTarget()==0));
	}
	
	
	public void close(){
		areas.stop();
		initialSound.close();
	}
	
	
	
	////////////////////////////////////////////////////////////////////////
	
	
	// picture
	public void setPicture(String img_file){
		image.setPicture(img_file);
		
		if (main.display!=null) main.display.updateMiniatures();
		
		main.selected_img=-1;
		if (img_file!=null){
			for (int i=0;i<main.listImages.length;i++){
				if (img_file.equals(main.listImages[i])) main.selected_img=i;
			}
		}
	}
	
	// tactile image
	public void setTactile(String tactile_file){
		image.setTactile(tactile_file);
		if (main.display!=null) main.display.updateMiniatures();
		main.selected_tactile=-1;
		if (tactile_file!=null){
			for (int i=0;i<main.listTactile.length;i++){
				if (tactile_file.equals(main.listTactile[i])) main.selected_tactile=i;
			}
		}
	}
	
	// flow file
	public void setFlow(String flow_file){
		image.setFlow(flow_file);

		if (main.display!=null) main.display.updateMiniatures();

		main.selected_flow=-1;
		if (flow_file!=null){
			for (int i=0;i<main.listFlow.length;i++){
				if (flow_file.equals(main.listFlow[i])) main.selected_flow=i;
			}
		}
	}
	
	// rail file
	public void setRail(String rail_file){
		image.setRail(rail_file);

		if (main.display!=null) main.display.updateMiniatures();
		
		main.selected_rail=-1;
		if (rail_file!=null){
			for (int i=0;i<main.listRail.length;i++){
				if (rail_file.equals(main.listRail[i])) main.selected_rail=i;
			}
		}
	}
	
	// area descriptor file
	public void setArea(String area_file){
		image.setArea(area_file);
		
		if (main.display!=null) main.display.updateMiniatures();
		
		main.selected_area=-1;
		if (area_file!=null){
			for (int i=0;i<main.listArea.length;i++){
				if (area_file.equals(main.listArea[i])) main.selected_area=i;
			}
		}
	}
	
	// magnetic descriptor file
	public void setMagnetic(String magnetic_file){
		image.setMagnetic(magnetic_file);
		
		if (main.display!=null) main.display.updateMiniatures();
		
		main.selected_magnetic=-1;
		if (magnetic_file!=null){
			for (int i=0;i<main.listMagnetic.length;i++){
				if (magnetic_file.equals(main.listMagnetic[i])) main.selected_magnetic=i;
			}
		}
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	// display functions
	public String getImages(){
		String msg="";
		if (image.view!=null) msg+="img: "+image.view;
		else  msg+="img: none";
		if (image.tactile!=null) msg+=", tact: "+image.tactile;
		else  msg+=", tactile: none";
		if (image.flow!=null) msg+=", flow: "+image.flow;
		else  msg+=", flow: none";
		if (image.rail!=null) msg+=", rail: "+image.rail;
		else  msg+=", rail: none";
		if (image.area!=null) msg+=", area: "+image.area;
		else  msg+=", area: none";
		if (image.magnetic!=null) msg+=", magnet: "+image.magnetic;
		else  msg+=", magnet: none";
		
		if (msg.length()>140) msg=msg.substring(0, 137)+" ...";
		
		return msg;
	}
	
	public String getAreas(){
		String msg="Active areas : ";
		for (int a=0;a<areas.areas.size();a++){
			msg+="("+areas.areas.get(a)+", "+areas.soundFiles.get(a)+"), ";
		}
		
		if (msg.length()>140) msg=msg.substring(0, 137)+" ...";
		
		return msg;
	}
	
	public String getPath(){
		String msg="Path : ("+targetSequence.sizeTarget()+" points), ";
		for (int t=0;t<targetSequence.sizeTarget();t++){
			msg+="("+targetSequence.targetList.get(t).x+", "+targetSequence.targetList.get(t).y+")-";
		}
		if (msg.length()>140) msg=msg.substring(0, 137)+" ...";
		
		return msg;
	}
	
	public String getSources(){
		String msg="Sound : ("+sourceList.size()+" sources), ";
		for (int s=0;s<sourceList.size();s++){
			msg+="("+(int)sourceList.get(s).px+", "+(int)sourceList.get(s).py+"), ";
		}
		if (msg.length()>140) msg=msg.substring(0, 137)+" ...";
		
		return msg;
	}
	
	public String getAttractors(){
		String msg="Attractors : ("+attractorList.size()+" points), ";
		for (int t=0;t<attractorList.size();t++){
			msg+="("+attractorList.get(t).x+", "+attractorList.get(t).y+")-";
		}
		if (msg.length()>140) msg=msg.substring(0, 137)+" ...";
		
		return msg;
	}
	
	public String getCondition(){
		String msg="Next conditions : ";
		for (int a=0;a<condition_areas.size();a++){
			msg+=condition_areas.get(a)+", ";
		}
		if (condition_sound) msg+="s ";
		if (condition_target)msg+="t ";
		if (condition_button)msg+="b";
		
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
	
	
	public String shortName(){
		String msg="";
		
		if (name.length()<=12) msg+=name;
		else msg=name.substring(0, 9)+"...";
		
		return msg;
	}
	
}
