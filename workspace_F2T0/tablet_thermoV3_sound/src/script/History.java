package script;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import main.Main;
import main.SoundSource;

public class History {

	// elements to change or add
	public String image=null;
	public String tactile=null;
	public String area=null;
	
	public ArrayList<SoundSource> sources;
	public ArrayList<Areas> areas;
	
	public String initialSound=null;
	
	// initial setup
	public boolean clearSources=false;
	
	// area-sound associations
	public ArrayList<Integer> areasId;
	public ArrayList<Integer> areasTypes;		// 0 stop, 1 continue , 2 forced
	public ArrayList<Integer> areasRepeat;		// 0 no, 1 yes
	public ArrayList<String> soundFiles;
	
	// next history condition
	public ArrayList<Integer> condition_areas;
	public boolean condition_sound=false;
	public boolean condition_button=false;
	public boolean condition=false;
	
	// exit door
	public ArrayList<Integer> exitAreasId;
	public ArrayList<String> ages;
	public ArrayList<Integer> reboot;
	
	public History(){
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
	public void setConditionButton(){
		condition_button=true;
		condition_sound=false;
		condition_areas.clear();
		condition=true;
		
		exitAreasId.clear();
		ages.clear();
		reboot.clear();
	}
	
	
	public void loadPreset(String file, int erase){
		
		if (erase==1){
			image="none";
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
					else if (elements.length>=2 && elements[0].equals("area")) area=elements[1];
					else if (elements.length>=2 && elements[0].equals("source")) loadSource(elements[1]);
					else if (elements.length>=1 && elements[0].equals("clearSources")) clearSources=true;
					else if (elements.length>=1) System.out.println("ERROR : wrong keyword "+elements[0]);
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
		if (area!=null) msg+=", area: "+area;
		else  msg+=", area: none";
		
		if (msg.length()>110) msg=msg.substring(0, 107)+" ...";
		
		return msg;
	}
	
	public String getAreas(){
		String msg="Sound areas: ";
		if (initialSound!=null) msg+="(init,"+initialSound+"), ";
		for (int a=0;a<areasId.size();a++){
			msg+="("+areasId.get(a)+", "+soundFiles.get(a)+"), ";
		}
		
		if (msg.length()>110) msg=msg.substring(0, 107)+" ...";
		
		return msg;
	}
	
	public String getSources(){
		String msg="Sound sources: ("+sources.size()+" sources), ";
		for (int s=0;s<sources.size();s++){
			msg+="("+(int)sources.get(s).px+", "+(int)sources.get(s).py+"), ";
		}
		if (msg.length()>110) msg=msg.substring(0, 107)+" ...";
		
		return msg;
	}
	
	public String getCondition(){
		String msg="Exit conditions : ";
		for (int a=0;a<condition_areas.size();a++){
			msg+=condition_areas.get(a)+", ";
		}
		if (condition_sound) msg+="s ";
		if (condition_button) msg+="b";
		if (!condition) msg+=("forever");
		
		if (msg.length()>110) msg=msg.substring(0, 107)+" ...";
		
		return msg;
	}

	
	public String getDoor(){
		String msg="Exit doors : ";
		for (int a=0;a<exitAreasId.size();a++){
			msg+="("+exitAreasId.get(a)+", "+ages.get(a)+", "+reboot.get(a)+"), ";
		}
		
		if (msg.length()>110) msg=msg.substring(0, 107)+" ...";
		
		return msg;
	}
	
}
