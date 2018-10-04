package script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import main.Main;
import main.SoundSource;
import main.Target;

public class Script {

	private Main main;
	
	public ArrayList<Age> ageList;
	
	public int currentAge=-1;
	
	private int writing_age=0;
	private int writing_history=0;
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	// initialize a script
	public Script(Main m){
		main=m;
		
		ageList=new ArrayList<Age>();
		
		ageList.add(new Age(main, "Primary Age"));
		currentAge=0;

		//ageList.get(currentAge).resetAge();
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	public void play(int x, int y){
		if (currentAge>=0 && currentAge<ageList.size()){
			
			ageList.get(currentAge).play(x,y);
			
			//System.out.println(ageList.get(currentAge).exitIndex);
			
			if (ageList.get(currentAge).exitIndex>=0){
				int key=main.display.keyboard.getKeyPressed();
				
				if (key==96){
					System.out.println("move to age "+ageList.get(currentAge).ages.get(ageList.get(currentAge).exitIndex));
					
					boolean reset=(ageList.get(currentAge).reboot.get(ageList.get(currentAge).exitIndex)==1);
					String name=ageList.get(currentAge).ages.get(ageList.get(currentAge).exitIndex);
					
					int a=0;
					boolean found=false;
					while (!found && a<ageList.size()){
						if (ageList.get(a).name.equals(name)) found=true;
						else a++;
					}
					
					if (found){
						currentAge=a;
						if (reset) ageList.get(currentAge).resetAge();
						
						main.currentAge=ageList.get(currentAge);
						
						main.display.updateMiniatures(-1);
						main.display.updateIndex(-1);
						
					}
					
				}
			}
		}
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	// set new elements in current age
	public void setPreset(String file, int erase){
		ageList.get(currentAge).setPreset(file, erase);
	}
	public void setPath(String file){
		ageList.get(currentAge).setPath(file);
	}
	public void setSources(String file){
		ageList.get(currentAge).setSource(file);
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////
	// load script file
	public void loadScript(String file){
		
		System.out.println("Read script "+Main.FILES+Main.SCRIPT+file);
		
		for (int a=0;a<ageList.size();a++) ageList.get(a).close();		
		ageList.clear();
		currentAge=-1;
		
		writing_age=-1;
		
		String fileName = Main.FILES+Main.SCRIPT+file;
		String[] elements;
		
		try {
			InputStream ips=new FileInputStream(fileName); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line=null;
			
			line=br.readLine();
			elements=line.split(" ");

			// read first line
			while (line==null || elements[0].equals("")){
				line=br.readLine();
				elements=line.split(" ");
			}
			
			// if first line initialize or load an age, remove the default one
			if (elements.length>=2 && elements[0].equals("age")){
				ageList.add(new Age(main, elements[1]));	// initialize described age
				line=br.readLine();							// read next line
				System.out.println(line+" , new age");
			}
			else if (elements.length>=2 && elements[0].equals("loadAge")){
				boolean valid=loadAge(elements[1]);			// load described age
				if (!valid){
					ageList.clear();
					ageList.add(new Age(main, "Primary Age"));	// if load failed, initialize default age
					System.out.println(line+" , load age");
				}
				line=br.readLine();							// read next line
			}
			else{
				ageList.add(new Age(main, "Primary Age"));	// initialize default age
				System.out.println(line+" , primary");
			}

			
			writing_age=0;
			writing_history=0;
			
			while (line!=null){
				
				System.out.println(line);
				elements=line.split(" ");
				
				boolean read=true;
				
				//////////////////////////////////////////
				// case image file
				if (elements.length>=2 && elements[0].equals("image")){
					if (elements[1].equals("none")) ageList.get(writing_age).history.get(writing_history).image="none";
					else ageList.get(writing_age).history.get(writing_history).image=elements[1];
				}
				// case tactile file
				if (elements.length>=2 && elements[0].equals("tactile")){
					if (elements[1].equals("none")) ageList.get(writing_age).history.get(writing_history).tactile="none";
					else ageList.get(writing_age).history.get(writing_history).tactile=elements[1];
				}	
				// case flow file
				if (elements.length>=2 && elements[0].equals("flow")){
					if (elements[1].equals("none")) ageList.get(writing_age).history.get(writing_history).flow="none";
					else ageList.get(writing_age).history.get(writing_history).flow=elements[1];
				}
				// case rail file
				if (elements.length>=2 && elements[0].equals("rail")){
					if (elements[1].equals("none")) ageList.get(writing_age).history.get(writing_history).rail="none";
					else ageList.get(writing_age).history.get(writing_history).rail=elements[1];
				}
				// case area descriptor file
				if (elements.length>=2 && elements[0].equals("area")){
					if (elements[1].equals("none"))  ageList.get(writing_age).history.get(writing_history).area="none";
					else ageList.get(writing_age).history.get(writing_history).area=elements[1];
				}
				
				//////////////////////////////////////////
				// case target, area or source
				if (elements.length>=5 && elements[0].equals("t")) ageList.get(writing_age).history.get(writing_history).path.add(new Target(elements));
				if (elements.length>=2 && elements[0].equals("a")) ageList.get(writing_age).history.get(writing_history).addArea(elements);
				if (elements.length>=3 && elements[0].equals("s")) ageList.get(writing_age).history.get(writing_history).sources.add(new SoundSource(elements));
				
				if (elements.length>=2 && elements[0].equals("playSound")) ageList.get(writing_age).history.get(writing_history).addInitialSound(elements[1]);
				
				///////////////////////////////////////////
				// case load preset, path or source
				if (elements.length>=2 && elements[0].equals("preset")) ageList.get(writing_age).history.get(writing_history).loadPreset(elements[1], Integer.parseInt(elements[2]));
				if (elements.length>=2 && elements[0].equals("path")) ageList.get(writing_age).history.get(writing_history).loadPath(elements[1]);
				if (elements.length>=2 && elements[0].equals("source")) ageList.get(writing_age).history.get(writing_history).loadSource(elements[1]);
				
				//////////////////////////////////////////
				// case erase path or sources
				if (elements.length>=1 && elements[0].equals("clearPath")) ageList.get(writing_age).history.get(writing_history).clearPath=true;
				if (elements.length>=1 && elements[0].equals("clearSources")) ageList.get(writing_age).history.get(writing_history).clearSources=true;

				// case erase all images
				if (elements.length>=1 && elements[0].equals("clearImages")){
					ageList.get(writing_age).history.get(writing_history).image="none";
					ageList.get(writing_age).history.get(writing_history).tactile="none";
					ageList.get(writing_age).history.get(writing_history).flow="none";
					ageList.get(writing_age).history.get(writing_history).rail="none";
					ageList.get(writing_age).history.get(writing_history).area="none";
				}
				// case erase everything
				if (elements.length>=1 && elements[0].equals("clearAll")){
					ageList.get(writing_age).history.get(writing_history).image="none";
					ageList.get(writing_age).history.get(writing_history).tactile="none";
					ageList.get(writing_age).history.get(writing_history).flow="none";
					ageList.get(writing_age).history.get(writing_history).rail="none";
					ageList.get(writing_age).history.get(writing_history).area="none";
					ageList.get(writing_age).history.get(writing_history).clearPath=true;
					ageList.get(writing_age).history.get(writing_history).clearSources=true;
				}
				
				// case default play
				if (elements.length>=1 && elements[0].equals("play")) ageList.get(writing_age).history.get(writing_history).initialPause=false;
				if (elements.length>=1 && elements[0].equals("stop")) ageList.get(writing_age).history.get(writing_history).initialPause=true;
				
				
				//////////////////////////////////////////
				// case next step condition
				else if (elements.length>=1 && elements[0].equals("condition")){
					for (int c=1;c<elements.length;c++){
						if (elements[c].equals("t")) ageList.get(writing_age).history.get(writing_history).setConditionTarget();
						else if (elements[c].equals("s")) ageList.get(writing_age).history.get(writing_history).setConditionSound();
						else ageList.get(writing_age).history.get(writing_history).addConditionArea(Integer.parseInt(elements[c]));
					}
					
					writing_history++;
					ageList.get(writing_age).history.add(new History());
				}
				
				//////////////////////////////////////////
				// case age exit condition
				else if (elements.length>=4 && elements[0].equals("exit")){
					ageList.get(writing_age).history.get(writing_history).addExitDoor(elements);
				}
				
				//////////////////////////////////////////
				// case new age
				else if (elements.length>=2 && elements[0].equals("loadAge")){
					if (ageExists(elements[1])) System.out.println("age "+elements[1]+" is already defined. Age NOT loaded");
					else loadAge(elements[1]);
				}
				else if (elements.length>=2 && elements[0].equals("age")){
					if (ageExists(elements[1])){
						System.out.println("age "+elements[1]+" is already defined. Age NOT loaded");
						elements[0]="";
						while (line!=null && !elements[0].equals("age") &&  !elements[0].equals("loadAge")){	// read until new Age is declared
							line=br.readLine();
							elements=line.split(" ");
						}
						read=false;	
					}
					else{
						ageList.add(new Age(main,elements[1]));
						writing_age++;
						writing_history=0;
					}
				}
				
				if (read) line=br.readLine();
			}
			br.close();
			
			
			currentAge=0;
			writing_age=0;
			writing_history=0;
		}
		catch (Exception e) {System.out.println("no file found");}
		
		
		// detect missing exits
		for (int a=0;a<ageList.size();a++){
			for (int p=0;p<ageList.get(a).history.size();p++){
				
				for (int e=0;e<ageList.get(a).history.get(p).exitAreasId.size();e++){
					
					String exit=ageList.get(a).history.get(p).ages.get(e);
					boolean found=false;
					int a2=0;
					while (!found && a2<ageList.size()){
						if (exit.equals(ageList.get(a2).name))found=true;
						else a2++;
					}
					
					if (!found){		// missing age
						System.out.println("missing Age "+exit);
						// try to find age in age directory
						File repertoire = new File(Main.FILES+Main.AGE);
						String[] listFiles;
						if (repertoire.exists()){
							listFiles=repertoire.list();
							
							found=false;
							a2=0;
							while (!found && a2<listFiles.length){
								if (exit.equals(listFiles[a2])) found=true;
								else a2++;
							}
							
							if (found){
								loadAge(listFiles[a2]);
								System.out.println("load age "+exit+" from files");
							}
							else System.out.println("Missing Age "+exit+" ! ");
						}
						else{
							System.out.println("Missing Age "+exit+" ! ");
						}
					}
				}
			}
		}
		
		// initialize age
		currentAge=0;
		ageList.get(currentAge).resetAge();
		
		// update display
		main.display.updateIndex(-1);
	}

	
	//////////////////////////////////////////////////////////////////////////////////////////////
	// load age file
	public boolean loadAge(String file){
		System.out.println("-------- "+writing_age+" ; "+writing_history);
		System.out.println("Read script "+Main.FILES+Main.AGE+file);
		
		boolean ret=true;
		
		ageList.add(new Age(main, file));
		writing_age++;
		writing_history=0;
		
		
		String fileName = Main.FILES+Main.AGE+file;
		String[] elements;
		
		try {
			InputStream ips=new FileInputStream(fileName); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line=null;
			
			line=br.readLine();
			
			
			while (line!=null){
				
				System.out.println("++++++ "+writing_age+" ; "+writing_history);
				
				System.out.println(line);
				elements=line.split(" ");
				
				//////////////////////////////////////////
				// case image file
				if (elements.length>=2 && elements[0].equals("image")){
					if (elements[1].equals("none")) ageList.get(writing_age).history.get(writing_history).image="none";
					else ageList.get(writing_age).history.get(writing_history).image=elements[1];
				}
				// case tactile file
				if (elements.length>=2 && elements[0].equals("tactile")){
					if (elements[1].equals("none")) ageList.get(writing_age).history.get(writing_history).tactile="none";
					else ageList.get(writing_age).history.get(writing_history).tactile=elements[1];
				}	
				// case flow file
				if (elements.length>=2 && elements[0].equals("flow")){
					if (elements[1].equals("none")) ageList.get(writing_age).history.get(writing_history).flow="none";
					else ageList.get(writing_age).history.get(writing_history).flow=elements[1];
				}
				// case rail file
				if (elements.length>=2 && elements[0].equals("rail")){
					if (elements[1].equals("none")) ageList.get(writing_age).history.get(writing_history).rail="none";
					else ageList.get(writing_age).history.get(writing_history).rail=elements[1];
				}
				// case area descriptor file
				if (elements.length>=2 && elements[0].equals("area")){
					if (elements[1].equals("none"))  ageList.get(writing_age).history.get(writing_history).area="none";
					else ageList.get(writing_age).history.get(writing_history).area=elements[1];
				}
				
				//////////////////////////////////////////
				// case target, area-sound association or source
				if (elements.length>=5 && elements[0].equals("t")) ageList.get(writing_age).history.get(writing_history).path.add(new Target(elements));
				if (elements.length>=2 && elements[0].equals("a")) ageList.get(writing_age).history.get(writing_history).addArea(elements);
				if (elements.length>=3 && elements[0].equals("s")) ageList.get(writing_age).history.get(writing_history).sources.add(new SoundSource(elements));
				
				///////////////////////////////////////////
				// case load preset, path or sources
				if (elements.length>=2 && elements[0].equals("preset")) ageList.get(writing_age).history.get(writing_history).loadPreset(elements[1], Integer.parseInt(elements[2]));
				if (elements.length>=2 && elements[0].equals("path"))   ageList.get(writing_age).history.get(writing_history).loadPath(elements[1]);
				if (elements.length>=2 && elements[0].equals("source")) ageList.get(writing_age).history.get(writing_history).loadSource(elements[1]);
				
				//////////////////////////////////////////
				// case erase path or source 
				if (elements.length>=1 && elements[0].equals("clearPath")) ageList.get(writing_age).history.get(writing_history).clearPath=true;
				if (elements.length>=1 && elements[0].equals("clearSources")) ageList.get(writing_age).history.get(writing_history).clearSources=true;

				// case erase all images
				if (elements.length>=1 && elements[0].equals("clearImages")){
					ageList.get(writing_age).history.get(writing_history).image="none";
					ageList.get(writing_age).history.get(writing_history).tactile="none";
					ageList.get(writing_age).history.get(writing_history).flow="none";
					ageList.get(writing_age).history.get(writing_history).rail="none";
					ageList.get(writing_age).history.get(writing_history).area="none";
				}
				// case erase everything
				if (elements.length>=1 && elements[0].equals("clearAll")){
					ageList.get(writing_age).history.get(writing_history).image="none";
					ageList.get(writing_age).history.get(writing_history).tactile="none";
					ageList.get(writing_age).history.get(writing_history).flow="none";
					ageList.get(writing_age).history.get(writing_history).rail="none";
					ageList.get(writing_age).history.get(writing_history).area="none";
					ageList.get(writing_age).history.get(writing_history).clearPath=true;
					ageList.get(writing_age).history.get(writing_history).clearSources=true;
				}
				
				// case default play
				if (elements.length>=1 && elements[0].equals("play")) ageList.get(writing_age).history.get(writing_history).initialPause=false;
				if (elements.length>=1 && elements[0].equals("stop")) ageList.get(writing_age).history.get(writing_history).initialPause=true;
				
				
				//////////////////////////////////////////
				// case next step condition
				else if (elements.length>=1 && elements[0].equals("condition")){
					for (int c=1;c<elements.length;c++){
						if (elements[c].equals("t")) ageList.get(writing_age).history.get(writing_history).setConditionTarget();
						else if (elements[c].equals("s")) ageList.get(writing_age).history.get(writing_history).setConditionSound();
						else ageList.get(writing_age).history.get(writing_history).addConditionArea(Integer.parseInt(elements[c]));
					}
					
					writing_history++;
					ageList.get(writing_age).history.add(new History());
				}
				
				//////////////////////////////////////////
				// case age exit condition
				else if (elements.length>=4 && elements[0].equals("exit")){
					ageList.get(writing_age).history.get(writing_history).addExitDoor(elements);
				}
				
				line=br.readLine();
			}
			br.close();
		}
		catch (Exception e) {
			System.out.println("no file found, age not added");
			writing_age--;
			writing_history=-1;
			ageList.remove(writing_age);
			ret=false;
		}
		
		return ret;
	}
	
	public boolean ageExists(String name){
		boolean ret=false;
		int a=0;
		while (!ret && a<ageList.size()){
			if (ageList.get(a).name.equals(name)) ret=true;
			a++;
		}
		
		return ret;
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void close(){
		for (int i=0;i<ageList.size();i++){
			ageList.get(i).close();
		}
	}
}
