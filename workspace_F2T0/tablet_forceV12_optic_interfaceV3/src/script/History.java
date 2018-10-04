package script;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import main.Main;
import main.SoundSource;
import main.Target;

public class History {

	// elements to change or add
	public String image=null;
	public String tactile=null;
	public String flow=null;
	public String rail=null;
	public String area=null;
	
	public ArrayList<Target> path;
	public ArrayList<SoundSource> sources;
	public ArrayList<Areas> areas;
	
	public String initialSound=null;
	
	// initial setup
	public boolean initialPause=false;
	public boolean clearPath=false;
	public boolean clearSources=false;
	
	// area-sound associations
	public ArrayList<Integer> areasId;
	public ArrayList<Integer> areasTypes;		// 0 stop, 1 continue , 2 forced
	public ArrayList<Integer> areasRepeat;		// 0 no, 1 yes
	public ArrayList<String> soundFiles;
	
	// next history condition
	public ArrayList<Integer> condition_areas;
	public boolean condition_sound=false;
	public boolean condition_target=false;
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
		exitAreasId.add(Integer.parseInt(args[1]));
		ages.add(args[2]);
		reboot.add(Integer.parseInt(args[3]));
	}

	public void setConditionSound(){
		condition_sound=true;
		condition=true;
	}
	
	public void setConditionTarget(){
		condition_target=true;
		condition=true;
	}
	
	public void addConditionArea(int id){
		condition_areas.add(id);
		condition=true;
	}
	
	
	public void loadPreset(String file, int erase){
		
		if (erase==1){
			image="none";
			tactile="none";
			flow="none";
			rail="none";
			area="none";
		}
		
		String fileName = Main.FILES+Main.PRESET+file;
		String[] elements;
		
		try {
			InputStream ips=new FileInputStream(fileName); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line;
			line=br.readLine();
			
			while (line!=null){
				elements=line.split(" ");
				if (elements.length>0){
					if (     elements.length>=2 && elements[0].equals("image")) image=elements[1];
					else if (elements.length>=2 && elements[0].equals("tactile")) tactile=elements[1];
					else if (elements.length>=2 && elements[0].equals("flow")) flow=elements[1];
					else if (elements.length>=2 && elements[0].equals("rail")) rail=elements[1];
					else if (elements.length>=2 && elements[0].equals("area")) area=elements[1];
					else if (elements.length>=2 && elements[0].equals("path")) loadPath(elements[1]);
					else if (elements.length>=2 && elements[0].equals("source")) loadSource(elements[1]);
					else if (elements.length>=1 && elements[0].equals("play")) initialPause=false;
					else if (elements.length>=1 && elements[0].equals("stop")) initialPause=true;
					else if (elements.length>=1 && elements[0].equals("clearPath")) clearPath=true;
					else if (elements.length>=1 && elements[0].equals("clearSources")) clearSources=true;
					else if (elements.length>=1) System.out.println("ERROR : wrong keyword "+elements[0]);
				}
				line=br.readLine();
			}
			br.close();
		}
		catch (Exception e) {System.out.println("no file found");}		
	}
	
	public void loadPath(String file){
		String fileName = Main.FILES+Main.PATH+file;
		String[] elements;
		
		try {
			InputStream ips=new FileInputStream(fileName); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line;
			line=br.readLine();
			
			while (line!=null){
				elements=line.split(" ");
				if (elements.length==5 && elements[0].equals("t")){
					path.add(new Target(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]),Integer.parseInt(elements[3]), Integer.parseInt(elements[4]) ));
				}
				line=br.readLine();
			}
			br.close();
		}
		catch (Exception e) {System.out.println("no file found");}
	}
	
	
	public void loadSource(String file){
		String fileName = Main.FILES+Main.SOURCE+file;
		String[] elements;
		
		try {
			InputStream ips=new FileInputStream(fileName); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line;
			line=br.readLine();
			
			while (line!=null){
				elements=line.split(" ");
				if (elements.length>=3 && elements[0].equals("s")){
					sources.add(new SoundSource(elements));
				}
				line=br.readLine();
			}
			br.close();
		}
		catch (Exception e) {System.out.println("no file found");}
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
	
	public String getCondition(){
		String msg="Exit conditions : ";
		for (int a=0;a<condition_areas.size();a++){
			msg+=condition_areas.get(a)+", ";
		}
		if (condition_sound) msg+="s ";
		if (condition_target)msg+="t";
		
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
