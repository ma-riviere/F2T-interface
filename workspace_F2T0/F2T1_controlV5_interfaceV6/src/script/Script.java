package script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import structures.Attractor;
import structures.TargetPoint;

import main.Main;
import main.SoundSource;

/**
 * Script interpreter. A script allows to generate a dynamic environment
 * @author simon gay
 */

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
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	public void play(int x, int y){
		if (currentAge>=0 && currentAge<ageList.size()){
			
			ageList.get(currentAge).play(x,y);

			if (ageList.get(currentAge).exitIndex>=0){
				int key=main.display.keyboard.getKeyPressed();
				
				if (key==Main.button1 || key==Main.button2 || key==Main.button3){
					main.println("move to age "+ageList.get(currentAge).ages.get(ageList.get(currentAge).exitIndex));
					
					boolean reset=(ageList.get(currentAge).reboot.get(ageList.get(currentAge).exitIndex)==1);
					String name=ageList.get(currentAge).ages.get(ageList.get(currentAge).exitIndex);
					
					int a=0;
					boolean found=false;
					while (!found && a<ageList.size()){
						if (ageList.get(a).name.equals(name)) found=true;
						else a++;
					}
					
					if (found){
						ageList.get(currentAge).stop();
						currentAge=a;
						if (reset) ageList.get(currentAge).resetAge();
						
						main.currentAge=ageList.get(currentAge);
						main.display.updateMiniatures();
					}
					
				}
			}
		}
		
		// display function
		if (main.display.display_mode==4){

			for (int a1=0;a1<ageList.size();a1++){
			
				if (ageList.get(a1).px==-1 || ageList.get(a1).py==-1){
					ageList.get(a1).px=100+120*(a1%7)+(a1%50);//50+(float) (Math.random()*870+50);
					ageList.get(a1).py=100+120*(a1%3)+(a1%50);//(float) (Math.random()*400+50);
				}
				
			}
			
			
			// distance between ages
			for (int a1=0;a1<ageList.size();a1++){
				for (int a2=0;a2<ageList.size();a2++){
					
					if (a1!=a2){
						
						float dist2=(ageList.get(a1).px-ageList.get(a2).px)*(ageList.get(a1).px-ageList.get(a2).px) + (ageList.get(a1).py-ageList.get(a2).py)*(ageList.get(a1).py-ageList.get(a2).py);
						
						if (dist2<22500 && dist2>0){ // 200²
							ageList.get(a2).px-=  50f*(ageList.get(a1).px-ageList.get(a2).px)/dist2;
							ageList.get(a2).py-=  50f*(ageList.get(a1).py-ageList.get(a2).py)/dist2;
						}
					}
				}
			}
			
			// don't let ages escapes
			for (int a2=0;a2<ageList.size();a2++){
				if (ageList.get(a2).px<50) ageList.get(a2).px=50;
				if (ageList.get(a2).py<50) ageList.get(a2).py=50;
				
				if (ageList.get(a2).px>930) ageList.get(a2).px=930;
				if (ageList.get(a2).py>450) ageList.get(a2).py=450;
			}
			
			// border repulsion
			for (int a1=0;a1<ageList.size();a1++){
				
				// left
				float dist2=(ageList.get(a1).px-0)*(ageList.get(a1).px-0);
				if (dist2<40000 && dist2>0){ // 200²
					ageList.get(a1).px+=  50f*(ageList.get(a1).px-0)/dist2;
				}
				
				// right
				dist2=(ageList.get(a1).px-980)*(ageList.get(a1).px-980);
				if (dist2<40000 && dist2>0){ // 200²
					ageList.get(a1).px+=  50f*(ageList.get(a1).px-980)/dist2;
				}
				
				// top
				dist2=(ageList.get(a1).py-0)*(ageList.get(a1).py-0);
				if (dist2<40000 && dist2>0){ // 200²
					ageList.get(a1).py+=  50f*(ageList.get(a1).py-0)/dist2;
				}
				
				// bottom
				dist2=(ageList.get(a1).py-500)*(ageList.get(a1).py-500);
				if (dist2<40000 && dist2>0){ // 200²
					ageList.get(a1).py+=  50f*(ageList.get(a1).py-500)/dist2;
				}
			}
		}
	}
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////
	// load script file
	public void loadScript(String file){
		
		main.println("");
		main.println("");
		main.println("---------------------------------------------");
		main.println("Read script "+file);
		main.println("---------------------------------------------");
		main.println("");
		
		// clear list of ages
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
			
			main.println(line);
			
			// if first line initialize or load an age, remove the default one
			if (elements.length>=2 && elements[0].equals("age")){
				ageList.add(new Age(main, elements[1]));	// initialize described age
				main.println("--- new initial age: "+elements[1]);
				line=br.readLine();							// read next line
			}
			else if (elements.length>=2 && elements[0].equals("loadAge")){
				main.println("--- load initial age: "+elements[1]);
				boolean valid=loadAge(elements[1]);			// load described age
				if (!valid){
					ageList.clear();
					ageList.add(new Age(main, "Primary Age"));	// if load failed, initialize default age
					main.println("--- initial age is Primary Age");
				}
				line=br.readLine();							// read next line
			}
			else{
				ageList.add(new Age(main, "Primary Age"));	// initialize default age
				main.println("--- initial age is Primary Age");
			}
			
			writing_age=0;
			writing_history=0;
			
			while (line!=null){
				
				main.println(line);
				elements=line.split(" ");
				
				boolean read=true;
				
				//////////////////////////////////////////
				// case image file
				if (elements.length>=2 && elements[0].equals("image")){
					if (elements[1].equals("none")) ageList.get(writing_age).history.get(writing_history).image="none";
					else ageList.get(writing_age).history.get(writing_history).image=elements[1];
				}
				// case tactile file
				else if (elements.length>=2 && elements[0].equals("tactile")){
					if (elements[1].equals("none")) ageList.get(writing_age).history.get(writing_history).tactile="none";
					else ageList.get(writing_age).history.get(writing_history).tactile=elements[1];
				}	
				// case flow file
				else if (elements.length>=2 && elements[0].equals("flow")){
					if (elements[1].equals("none")) ageList.get(writing_age).history.get(writing_history).flow="none";
					else ageList.get(writing_age).history.get(writing_history).flow=elements[1];
				}
				// case rail file
				else if (elements.length>=2 && elements[0].equals("rail")){
					if (elements[1].equals("none")) ageList.get(writing_age).history.get(writing_history).rail="none";
					else ageList.get(writing_age).history.get(writing_history).rail=elements[1];
				}
				// case area descriptor file
				else if (elements.length>=2 && elements[0].equals("area")){
					if (elements[1].equals("none"))  ageList.get(writing_age).history.get(writing_history).area="none";
					else ageList.get(writing_age).history.get(writing_history).area=elements[1];
				}
				// case magnetic descriptor file
				else if (elements.length>=2 && elements[0].equals("magnet")){
					if (elements[1].equals("none"))  ageList.get(writing_age).history.get(writing_history).magnetic="none";
					else ageList.get(writing_age).history.get(writing_history).magnetic=elements[1];
				}
				
				//////////////////////////////////////////
				// case target, area or source
				else if (elements.length>=5 && elements[0].equals("t")) ageList.get(writing_age).history.get(writing_history).path.add(new TargetPoint(elements));
				else if (elements.length>=2 && elements[0].equals("a")) ageList.get(writing_age).history.get(writing_history).addArea(elements);
				else if (elements.length>=3 && elements[0].equals("s")) ageList.get(writing_age).history.get(writing_history).sources.add(new SoundSource(elements));
				else if (elements.length>=6 && elements[0].equals("g")) ageList.get(writing_age).history.get(writing_history).attractors.add(new Attractor(elements));
				
				else if (elements.length>=2 && elements[0].equals("playSound")) ageList.get(writing_age).history.get(writing_history).addInitialSound(elements[1]);
				
				///////////////////////////////////////////
				// case load preset, path or source
				else if (elements.length>=2 && elements[0].equals("preset")) loadPreset(ageList.get(writing_age).history.get(writing_history), elements[1], Integer.parseInt(elements[2]));
				else if (elements.length>=2 && elements[0].equals("path")) 	loadPath(  ageList.get(writing_age).history.get(writing_history), elements[1]);
				else if (elements.length>=2 && elements[0].equals("source")) loadSource(ageList.get(writing_age).history.get(writing_history), elements[1]);
				else if (elements.length>=2 && elements[0].equals("attractor")) loadAttractor(ageList.get(writing_age).history.get(writing_history), elements[1]);
				else if (elements.length>=2 && elements[0].equals("association")) loadAssociation(ageList.get(writing_age).history.get(writing_history), elements[1]);
				
				//////////////////////////////////////////
				// case erase path or sources
				else if (elements.length>=1 && elements[0].equals("clearPath")) ageList.get(writing_age).history.get(writing_history).clearPath=true;
				else if (elements.length>=1 && elements[0].equals("clearSources")) ageList.get(writing_age).history.get(writing_history).clearSources=true;
				else if (elements.length>=1 && elements[0].equals("clearAttractors")) ageList.get(writing_age).history.get(writing_history).clearAttractors=true;

				// case erase all images
				else if (elements.length>=1 && elements[0].equals("clearImages")){
					ageList.get(writing_age).history.get(writing_history).image="none";
					ageList.get(writing_age).history.get(writing_history).tactile="none";
					ageList.get(writing_age).history.get(writing_history).flow="none";
					ageList.get(writing_age).history.get(writing_history).rail="none";
					ageList.get(writing_age).history.get(writing_history).area="none";
					ageList.get(writing_age).history.get(writing_history).magnetic="none";
				}
				// case erase everything
				else if (elements.length>=1 && elements[0].equals("clearAll")){
					ageList.get(writing_age).history.get(writing_history).image="none";
					ageList.get(writing_age).history.get(writing_history).tactile="none";
					ageList.get(writing_age).history.get(writing_history).flow="none";
					ageList.get(writing_age).history.get(writing_history).rail="none";
					ageList.get(writing_age).history.get(writing_history).area="none";
					ageList.get(writing_age).history.get(writing_history).magnetic="none";
					ageList.get(writing_age).history.get(writing_history).clearPath=true;
					ageList.get(writing_age).history.get(writing_history).clearSources=true;
					ageList.get(writing_age).history.get(writing_history).clearAttractors=true;
				}
				
				// case default play
				else if (elements.length>=1 && elements[0].equals("play")) ageList.get(writing_age).history.get(writing_history).initialPause=false;
				else if (elements.length>=1 && elements[0].equals("stop")) ageList.get(writing_age).history.get(writing_history).initialPause=true;
				
				
				//////////////////////////////////////////
				// case next step condition
				else if (elements.length>=1 && elements[0].equals("condition")){
					for (int c=1;c<elements.length;c++){
						if (elements[c].equals("t")) ageList.get(writing_age).history.get(writing_history).setConditionTarget();
						else if (elements[c].equals("s")) ageList.get(writing_age).history.get(writing_history).setConditionSound();
						else if (elements[c].equals("b")) ageList.get(writing_age).history.get(writing_history).setConditionButton();
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
					if (ageExists(elements[1])) main.println("--- age "+elements[1]+" is already defined. Age NOT loaded");
					else loadAge(elements[1]);
				}
				else if (elements.length>=2 && elements[0].equals("age")){
					if (ageExists(elements[1])){
						main.println("--- age "+elements[1]+" is already defined. Age NOT loaded");
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

			writing_age=0;
			writing_history=0;
		}
		catch (Exception e) {main.println("/!\\ Script does not exist or contains errors");}
		
		main.println("");
		main.println("--- Script loaded");
		main.println("");
		
		findMissingAges();
		
		
		// initialize age
		currentAge=0;
		
		ageList.get(0).resetAge();
		
		main.currentAge=ageList.get(0);
		
		// update display
		main.display.updateIndex(-1);
		main.display.updateMiniatures();
		main.display.updateLinks();
	}
	
	private void findMissingAges(){
		// detect missing exits
		for (int a=0;a<ageList.size();a++){
			
			//ageList.get(a).connections.clear();
			
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
						main.println("--- missing Age "+exit);
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
								ageList.get(a).connections.add(ageList.size());
								loadAge(listFiles[a2]);									// load age file
								main.println("--- load age "+exit+" from age files");
								
							}
							else{
								main.println("--- Missing Age "+exit+" ! Exit removed");
								ageList.get(a).history.get(p).exitAreasId.remove(e);
								ageList.get(a).history.get(p).ages.remove(e);
								ageList.get(a).history.get(p).reboot.remove(e);
								e--;
							}
						}
						else{
							main.println("--- Missing Age "+exit+" ! ");
							ageList.get(a).history.get(p).exitAreasId.remove(e);
							ageList.get(a).history.get(p).ages.remove(e);
							ageList.get(a).history.get(p).reboot.remove(e);
							e--;
						}
					}
					else ageList.get(a).connections.add(a2);	// indicate connection for the age display
				}
			}
		}
	}

	
	//////////////////////////////////////////////////////////////////////////////////////////////
	// load age file
	private boolean loadAge(String file){

		
		String fileName = Main.FILES+Main.AGE+file;
		String[] elements;
		
		main.println("Load Age "+file);
		
		boolean ret=true;
		
		ageList.add(new Age(main, file));
		writing_age++;
		writing_history=0;
		
		try {
			InputStream ips=new FileInputStream(fileName); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line=null;
			
			line=br.readLine();
			
			
			while (line!=null){

				main.println(line);
				elements=line.split(" ");
				
				//////////////////////////////////////////
				// case image file
				if (elements.length>=2 && elements[0].equals("image")){
					if (elements[1].equals("none")) ageList.get(writing_age).history.get(writing_history).image="none";
					else ageList.get(writing_age).history.get(writing_history).image=elements[1];
				}
				// case tactile file
				else if (elements.length>=2 && elements[0].equals("tactile")){
					if (elements[1].equals("none")) ageList.get(writing_age).history.get(writing_history).tactile="none";
					else ageList.get(writing_age).history.get(writing_history).tactile=elements[1];
				}	
				// case flow file
				else if (elements.length>=2 && elements[0].equals("flow")){
					if (elements[1].equals("none")) ageList.get(writing_age).history.get(writing_history).flow="none";
					else ageList.get(writing_age).history.get(writing_history).flow=elements[1];
				}
				// case rail file
				else if (elements.length>=2 && elements[0].equals("rail")){
					if (elements[1].equals("none")) ageList.get(writing_age).history.get(writing_history).rail="none";
					else ageList.get(writing_age).history.get(writing_history).rail=elements[1];
				}
				// case area descriptor file
				else if (elements.length>=2 && elements[0].equals("area")){
					if (elements[1].equals("none"))  ageList.get(writing_age).history.get(writing_history).area="none";
					else ageList.get(writing_age).history.get(writing_history).area=elements[1];
				}
				// case magnetic descriptor file
				else if (elements.length>=2 && elements[0].equals("magnet")){
					if (elements[1].equals("none"))  ageList.get(writing_age).history.get(writing_history).magnetic="none";
					else ageList.get(writing_age).history.get(writing_history).magnetic=elements[1];
				}
				
				//////////////////////////////////////////
				// case target, area-sound association or source
				else if (elements.length>=5 && elements[0].equals("t")) ageList.get(writing_age).history.get(writing_history).path.add(new TargetPoint(elements));
				else if (elements.length>=5 && elements[0].equals("a")) ageList.get(writing_age).history.get(writing_history).addArea(elements);
				else if (elements.length>=3 && elements[0].equals("s")) ageList.get(writing_age).history.get(writing_history).sources.add(new SoundSource(elements));
				else if (elements.length>=6 && elements[0].equals("g")) ageList.get(writing_age).history.get(writing_history).attractors.add(new Attractor(elements));
				
				else if (elements.length>=2 && elements[0].equals("playSound")) ageList.get(writing_age).history.get(writing_history).addInitialSound(elements[1]);
				
				///////////////////////////////////////////
				// case load preset, path or sources
				else if (elements.length>=2 && elements[0].equals("preset"))     loadPreset(ageList.get(writing_age).history.get(writing_history), elements[1], Integer.parseInt(elements[2]));
				else if (elements.length>=2 && elements[0].equals("path"))       loadPath(  ageList.get(writing_age).history.get(writing_history), elements[1]);
				else if (elements.length>=2 && elements[0].equals("source"))     loadSource(ageList.get(writing_age).history.get(writing_history), elements[1]);
				else if (elements.length>=2 && elements[0].equals("attractor"))  loadAttractor(ageList.get(writing_age).history.get(writing_history), elements[1]);
				else if (elements.length>=2 && elements[0].equals("association"))loadAssociation(ageList.get(writing_age).history.get(writing_history), elements[1]);
				
				//////////////////////////////////////////
				// case erase path or source 
				else if (elements.length>=1 && elements[0].equals("clearPath")) ageList.get(writing_age).history.get(writing_history).clearPath=true;
				else if (elements.length>=1 && elements[0].equals("clearSources")) ageList.get(writing_age).history.get(writing_history).clearSources=true;
				else if (elements.length>=1 && elements[0].equals("clearAttractors")) ageList.get(writing_age).history.get(writing_history).clearAttractors=true;

				// case erase all images
				else if (elements.length>=1 && elements[0].equals("clearImages")){
					ageList.get(writing_age).history.get(writing_history).image="none";
					ageList.get(writing_age).history.get(writing_history).tactile="none";
					ageList.get(writing_age).history.get(writing_history).flow="none";
					ageList.get(writing_age).history.get(writing_history).rail="none";
					ageList.get(writing_age).history.get(writing_history).area="none";
					ageList.get(writing_age).history.get(writing_history).magnetic="none";
				}
				// case erase everything
				else if (elements.length>=1 && elements[0].equals("clearAll")){
					ageList.get(writing_age).history.get(writing_history).image="none";
					ageList.get(writing_age).history.get(writing_history).tactile="none";
					ageList.get(writing_age).history.get(writing_history).flow="none";
					ageList.get(writing_age).history.get(writing_history).rail="none";
					ageList.get(writing_age).history.get(writing_history).area="none";
					ageList.get(writing_age).history.get(writing_history).magnetic="none";
					ageList.get(writing_age).history.get(writing_history).clearPath=true;
					ageList.get(writing_age).history.get(writing_history).clearSources=true;
					ageList.get(writing_age).history.get(writing_history).clearAttractors=true;
				}
				
				// case default play
				else if (elements.length>=1 && elements[0].equals("play")) ageList.get(writing_age).history.get(writing_history).initialPause=false;
				else if (elements.length>=1 && elements[0].equals("stop")) ageList.get(writing_age).history.get(writing_history).initialPause=true;
				
				
				//////////////////////////////////////////
				// case next step condition
				else if (elements.length>=1 && elements[0].equals("condition")){
					for (int c=1;c<elements.length;c++){
						if (elements[c].equals("t")) ageList.get(writing_age).history.get(writing_history).setConditionTarget();
						else if (elements[c].equals("s")) ageList.get(writing_age).history.get(writing_history).setConditionSound();
						else if (elements[c].equals("b")) ageList.get(writing_age).history.get(writing_history).setConditionButton();
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
			main.println("/!\\ Age file not found or contains errors, age not added");
			writing_age--;
			writing_history=-1;
			ageList.remove(writing_age);
			ret=false;
		}
		
		return ret;
	}
	
	private boolean ageExists(String name){
		boolean ret=false;
		int a=0;
		while (!ret && a<ageList.size()){
			if (ageList.get(a).name.equals(name)) ret=true;
			a++;
		}
		
		return ret;
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// load functions
	private void loadPreset(History writingPeriod, String file, int erase){
		
		main.println("Load preset: "+file);
				
		if (erase>=1){
			writingPeriod.image="none";
			writingPeriod.tactile="none";
			writingPeriod.flow="none";
			writingPeriod.rail="none";
			writingPeriod.area="none";
			writingPeriod.magnetic="none";
		}
		if (erase>=2){
			writingPeriod.clearPath=true;
			writingPeriod.clearSources=true;
			writingPeriod.clearAttractors=true;
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
				
				main.println("   "+line);
				
				elements=line.split(" ");
				if (elements.length>0){
					if (     elements.length>=2 && elements[0].equals("image")) 	writingPeriod.image=elements[1];
					else if (elements.length>=2 && elements[0].equals("tactile")) 	writingPeriod.tactile=elements[1];
					else if (elements.length>=2 && elements[0].equals("flow")) 		writingPeriod.flow=elements[1];
					else if (elements.length>=2 && elements[0].equals("rail")) 		writingPeriod.rail=elements[1];
					else if (elements.length>=2 && elements[0].equals("area")) 		writingPeriod.area=elements[1];
					else if (elements.length>=2 && elements[0].equals("magnet")) 	writingPeriod.magnetic=elements[1];
					
					if (elements.length>=5 && elements[0].equals("t")) writingPeriod.path.add(new TargetPoint(elements));
					if (elements.length>=2 && elements[0].equals("a")) writingPeriod.addArea(elements);
					if (elements.length>=3 && elements[0].equals("s")) writingPeriod.sources.add(new SoundSource(elements));
					if (elements.length>=6 && elements[0].equals("g")) writingPeriod.attractors.add(new Attractor(elements));
					
					else if (elements.length>=2 && elements[0].equals("path")) 		  loadPath(  writingPeriod,elements[1]);
					else if (elements.length>=2 && elements[0].equals("source")) 	  loadSource(writingPeriod,elements[1]);
					else if (elements.length>=2 && elements[0].equals("attractor"))   loadAttractor(writingPeriod,elements[1]);
					else if (elements.length>=2 && elements[0].equals("association")) loadAssociation(writingPeriod,elements[1]);
					
					
					
					else if (elements.length>=1 && elements[0].equals("clearPath")) 	 writingPeriod.clearPath=true;
					else if (elements.length>=1 && elements[0].equals("clearSources")) 	 writingPeriod.clearSources=true;
					else if (elements.length>=1 && elements[0].equals("clearAttractors"))writingPeriod.clearAttractors=true;
					
					if (elements.length>=1 && elements[0].equals("clearImages")){
						writingPeriod.image="none";
						writingPeriod.tactile="none";
						writingPeriod.flow="none";
						writingPeriod.rail="none";
						writingPeriod.area="none";
						writingPeriod.magnetic="none";
					}
					if (elements.length>=1 && elements[0].equals("clearAll")){
						writingPeriod.image="none";
						writingPeriod.tactile="none";
						writingPeriod.flow="none";
						writingPeriod.rail="none";
						writingPeriod.area="none";
						writingPeriod.magnetic="none";
						writingPeriod.clearPath=true;
						writingPeriod.clearSources=true;
						writingPeriod.clearAttractors=true;
					}
					
					else if (elements.length>=1 && elements[0].equals("play")) 		writingPeriod.initialPause=false;
					else if (elements.length>=1 && elements[0].equals("stop"))		writingPeriod.initialPause=true;
					
					else if (elements.length>=1) main.println("   /!\\ ERROR : wrong keyword "+elements[0]);
				}
				line=br.readLine();
			}
			br.close();
		}
		catch (Exception e) {main.println("   /!\\ preset file not found or containing errors");}
	}
	
	
	private void loadPath(History writingPeriod, String file){
		
		main.println("Load path file: "+file);
		
		String fileName = Main.FILES+Main.PATH+file;
		String[] elements;
		
		try {
			InputStream ips=new FileInputStream(fileName); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line;
			line=br.readLine();
			
			while (line!=null){
				
				main.println("   "+line);
				
				elements=line.split(" ");
				if (elements.length>=5 && elements[0].equals("t")){
					if (elements.length<7)
						writingPeriod.path.add(new TargetPoint(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]),
													      Integer.parseInt(elements[3]), Integer.parseInt(elements[4]) ));
					else
						writingPeriod.path.add(new TargetPoint(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]),
														  Integer.parseInt(elements[3]), Integer.parseInt(elements[4]), 
														  Float.parseFloat(elements[5]), Float.parseFloat(elements[6]) ));
				}
				line=br.readLine();
			}
			br.close();
		}
		catch (Exception e) {main.println("   /!\\ Path file not found or containing errors");}
	}
	
	
	private void loadSource(History writingPeriod, String file){
		
		main.println("Load source file: "+file);
		
		String fileName = Main.FILES+Main.SOURCE+file;
		String[] elements;
		
		try {
			InputStream ips=new FileInputStream(fileName); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line;
			line=br.readLine();
			
			while (line!=null){
				
				main.println("   "+line);
				
				elements=line.split(" ");
				if (elements.length>=3 && elements[0].equals("s")){
					writingPeriod.sources.add(new SoundSource(elements));
				}
				line=br.readLine();
			}
			br.close();
		}
		catch (Exception e) {main.println("   /!\\ Source file not found or containing errors");}
	}
	
	private void loadAttractor(History writingPeriod, String file){
		
		main.println("Load attractor file: "+file);
		
		String fileName = Main.FILES+Main.ATTRACTOR+file;
		String[] elements;
		
		try {
			InputStream ips=new FileInputStream(fileName); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line;
			line=br.readLine();
			
			while (line!=null){
				
				main.println("   "+line);
				
				elements=line.split(" ");
				if (elements.length==6 && elements[0].equals("g")){
					writingPeriod.attractors.add(new Attractor(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]),
													           Integer.parseInt(elements[3]), Integer.parseInt(elements[4]), Integer.parseInt(elements[5]) ));
				}
				line=br.readLine();
			}
			br.close();
		}
		catch (Exception e) {main.println("   /!\\ Path file not found or containing errors");}
	}
	
	private void loadAssociation(History writingPeriod, String file){
		
		main.println("Load association file: "+file);
		
		String fileName = Main.FILES+Main.ASSOCIATION+file;
		String[] elements;
		
		try {
			InputStream ips=new FileInputStream(fileName); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line;
			line=br.readLine();
			
			while (line!=null){
				
				main.println("   "+line);
				
				elements=line.split(" ");
				if (elements.length==6 && elements[0].equals("a")){
					writingPeriod.addArea(elements);
				}
				line=br.readLine();
			}
			br.close();
		}
		catch (Exception e) {main.println("   /!\\ Path file not found or containing errors");}
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// set functions
	public void setPreset(String file, int erase){
		
		main.println("Set preset: "+file);
		
		if (erase>=1){
			ageList.get(currentAge).setPicture(null);
			ageList.get(currentAge).setTactile(null);
			ageList.get(currentAge).setFlow(null);
			ageList.get(currentAge).setRail(null);
			ageList.get(currentAge).setArea(null);
			ageList.get(currentAge).setMagnetic(null);
		}
		if (erase>=2){
			ageList.get(currentAge).targetSequence.clear();
			ageList.get(currentAge).sourceList.clear();
			ageList.get(currentAge).areas.clear();
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
				
				main.println(file);
				
				elements=line.split(" ");
				
				if (elements.length>0){
					if (     elements.length>=2 && elements[0].equals("image")){
						if (elements[0].equals("none")) ageList.get(currentAge).setPicture(null);
						else ageList.get(currentAge).setPicture(elements[1]);
					}
					else if (elements.length>=2 && elements[0].equals("tactile")){
						if (elements[0].equals("none")) ageList.get(currentAge).setTactile(null);
						else							ageList.get(currentAge).setTactile(elements[1]);
					}
					else if (elements.length>=2 && elements[0].equals("flow")){
						if (elements[0].equals("none")) ageList.get(currentAge).setFlow(null);
						else							ageList.get(currentAge).setFlow(elements[1]);
					}
					else if (elements.length>=2 && elements[0].equals("rail")){
						if (elements[0].equals("none")) ageList.get(currentAge).setRail(null);
						else							ageList.get(currentAge).setRail(elements[1]);
					}
					else if (elements.length>=2 && elements[0].equals("area")){
						if (elements[0].equals("none")) ageList.get(currentAge).setArea(null);
						else							ageList.get(currentAge).setArea(elements[1]);
					}
					else if (elements.length>=2 && elements[0].equals("magnet")){
						if (elements[0].equals("none")) ageList.get(currentAge).setMagnetic(null);
						else							ageList.get(currentAge).setMagnetic(elements[1]);
					}
					
					else if (elements.length>=5 && elements[0].equals("t")) ageList.get(currentAge).targetSequence.add(new TargetPoint(elements));
					else if (elements.length>=5 && elements[0].equals("a")) ageList.get(currentAge).areas.addArea(Integer.parseInt(elements[1]),Integer.parseInt(elements[2]),Integer.parseInt(elements[3]),elements[4]);
					else if (elements.length>=3 && elements[0].equals("s")) ageList.get(currentAge).sourceList.add(new SoundSource(elements));
					else if (elements.length>=6 && elements[0].equals("g")) ageList.get(currentAge).attractorList.add(new Attractor(elements));
					
					else if (elements.length>=2 && elements[0].equals("path")) setPath(elements[1]);
					else if (elements.length>=2 && elements[0].equals("source")) setSource(elements[1]);
					else if (elements.length>=2 && elements[0].equals("attractor")) setAttractor(elements[1]);
					else if (elements.length>=2 && elements[0].equals("association")) setAssociation(elements[1]);
					
					else if (elements.length>=1 && elements[0].equals("clearPath")) ageList.get(currentAge).targetSequence.clear();
					else if (elements.length>=1 && elements[0].equals("clearSources")) ageList.get(currentAge).sourceList.clear();
					else if (elements.length>=1 && elements[0].equals("clearAttractor")) ageList.get(currentAge).attractorList.clear();
					
					else if (elements.length>=1 && elements[0].equals("clearImages")){
						ageList.get(currentAge).setPicture(null);
						ageList.get(currentAge).setTactile(null);
						ageList.get(currentAge).setFlow(null);
						ageList.get(currentAge).setRail(null);
						ageList.get(currentAge).setArea(null);
						ageList.get(currentAge).setMagnetic(null);
					}
					// case erase everything
					else if (elements.length>=1 && elements[0].equals("clearAll")){
						ageList.get(currentAge).setPicture(null);
						ageList.get(currentAge).setTactile(null);
						ageList.get(currentAge).setFlow(null);
						ageList.get(currentAge).setRail(null);
						ageList.get(currentAge).setArea(null);
						ageList.get(currentAge).setMagnetic(null);
						ageList.get(currentAge).targetSequence.clear();
						ageList.get(currentAge).sourceList.clear();
						ageList.get(currentAge).areas.clear();
					}
					
					else if (elements.length>=1 && elements[0].equals("play")) main.target_pause=false;
					else if (elements.length>=1 && elements[0].equals("stop")) main.target_pause=true;
					
					
					else if (elements.length>=1) main.println("ERROR : wrong keyword "+elements[0]);
				}
				line=br.readLine();
			}
			br.close();
		}
		catch (Exception e) {main.println("no file found");}
		
		main.display.updateLinks();
	}
	
	public void setPath(String file){
		
		main.println("Set path: "+file);
		
		String fileName = Main.FILES+Main.PATH+file;
		String[] elements;
		
		try {
			InputStream ips=new FileInputStream(fileName); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line;
			line=br.readLine();
			
			while (line!=null){
				
				main.println("   "+line);
				
				elements=line.split(" ");
				if (elements.length>=5 && elements[0].equals("t")){
					if (elements.length<7)
						ageList.get(currentAge).targetSequence.add(new TargetPoint(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]),
																			  Integer.parseInt(elements[3]), Integer.parseInt(elements[4]) ));
					else
						ageList.get(currentAge).targetSequence.add(new TargetPoint(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]),
								  											  Integer.parseInt(elements[3]), Integer.parseInt(elements[4]),
								  											  Float.parseFloat(elements[5]), Float.parseFloat(elements[6])));
				}
				line=br.readLine();
			}
			br.close();
		}
		catch (Exception e) {main.println("no file found");}
	}
	
	
	public void setSource(String file){
		
		main.println("Set sources: "+file);
		
		String fileName = Main.FILES+Main.SOURCE+file;
		String[] elements;
		
		try {
			InputStream ips=new FileInputStream(fileName); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line;
			line=br.readLine();
			
			while (line!=null){
				
				main.println("   "+line);
				
				elements=line.split(" ");
				if (elements.length>=3 && elements[0].equals("s")){
					ageList.get(currentAge).sourceList.add(new SoundSource(elements));
				}
				line=br.readLine();
			}
			br.close();
		}
		catch (Exception e) {main.println("no file found");}
	}
	
	public void setAssociation(String file){
		
		main.println("Set association: "+file);
		
		String fileName = Main.FILES+Main.ASSOCIATION+file;
		String[] elements;
		
		try {
			InputStream ips=new FileInputStream(fileName); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line;
			line=br.readLine();
			
			while (line!=null){
				
				main.println("   "+line);
				
				elements=line.split(" ");
				if (elements.length>=3 && elements[0].equals("a")){
					ageList.get(currentAge).areas.addArea(Integer.parseInt(elements[1]),Integer.parseInt(elements[2]),Integer.parseInt(elements[3]),elements[4]);
				}
				line=br.readLine();
			}
			br.close();
		}
		catch (Exception e) {main.println("no file found");}
		
		main.display.updateLinks();
	}
	
	public void setAttractor(String file){
		
		main.println("Set attractor: "+file);
		
		String fileName = Main.FILES+Main.ATTRACTOR+file;
		String[] elements;
		
		try {
			InputStream ips=new FileInputStream(fileName); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line;
			line=br.readLine();
			
			while (line!=null){
				
				main.println("   "+line);
				
				elements=line.split(" ");
				if (elements.length==6 && elements[0].equals("g")){
					ageList.get(currentAge).attractorList.add(new Attractor(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]),
																			Integer.parseInt(elements[3]), Integer.parseInt(elements[4]), Integer.parseInt(elements[5]) ));
				}
				line=br.readLine();
			}
			br.close();
		}
		catch (Exception e) {main.println("no file found");}
	}
	
	
	///////////////////////////////////////////////////
	public void setCommandLine(String line){
		
		main.println("");
		main.println(">> "+line);
		main.println("");
		
		try {
			String[] elements=line.split(" ");
				
			//////////////////////////////////////////
			// case image file
			
			if (     elements.length>=2 && elements[0].equals("image")){
				if (elements[0].equals("none")) ageList.get(currentAge).setPicture(null);
				else ageList.get(currentAge).setPicture(elements[1]);
			}
			else if (elements.length>=2 && elements[0].equals("tactile")){
				if (elements[0].equals("none")) ageList.get(currentAge).setTactile(null);
				else							ageList.get(currentAge).setTactile(elements[1]);
			}
			else if (elements.length>=2 && elements[0].equals("flow")){
				if (elements[0].equals("none")) ageList.get(currentAge).setFlow(null);
				else							ageList.get(currentAge).setFlow(elements[1]);
			}
			else if (elements.length>=2 && elements[0].equals("rail")){
				if (elements[0].equals("none")) ageList.get(currentAge).setRail(null);
				else							ageList.get(currentAge).setRail(elements[1]);
			}
			else if (elements.length>=2 && elements[0].equals("area")){
				if (elements[0].equals("none")) ageList.get(currentAge).setArea(null);
				else							ageList.get(currentAge).setArea(elements[1]);
			}
			else if (elements.length>=2 && elements[0].equals("magnet")){
				if (elements[0].equals("none")) ageList.get(currentAge).setMagnetic(null);
				else							ageList.get(currentAge).setMagnetic(elements[1]);
			}
			
			
			//////////////////////////////////////////
			// case target, area or source
			else if (elements.length>=5 && elements[0].equals("t")) ageList.get(currentAge).targetSequence.add(new TargetPoint(elements));
			else if (elements.length>=2 && elements[0].equals("a")) ageList.get(currentAge).areas.addArea(Integer.parseInt(elements[1]),Integer.parseInt(elements[2]),Integer.parseInt(elements[3]),elements[4]);
			else if (elements.length>=3 && elements[0].equals("s")) ageList.get(currentAge).sourceList.add(new SoundSource(elements));
			else if (elements.length>=6 && elements[0].equals("g")) ageList.get(currentAge).attractorList.add(new Attractor(elements));
				
			else if (elements.length>=2 && elements[0].equals("playSound")){
				ageList.get(currentAge).initialSound.close();
				ageList.get(currentAge).initialSound.removeSound();
				if (ageList.get(currentAge).initialSound!=null) ageList.get(currentAge).initialSound.addInitialSound(elements[1]);
			}
			
			
			///////////////////////////////////////////
			// case load preset, path or source	
			else if (elements.length>=3 && elements[0].equals("preset")) setPreset(elements[1], Integer.parseInt(elements[2]));
			else if (elements.length>=2 && elements[0].equals("path")) setPath(elements[1]);
			else if (elements.length>=2 && elements[0].equals("source")) setSource(elements[1]);
			else if (elements.length>=2 && elements[0].equals("association")) setAssociation(elements[1]);
			else if (elements.length>=2 && elements[0].equals("attractor")) setAttractor(elements[1]);
		
			//////////////////////////////////////////
			// case erase path or sources
			else if (elements.length>=1 && elements[0].equals("clearPath")) ageList.get(currentAge).targetSequence.clear();
			else if (elements.length>=1 && elements[0].equals("clearSources")) ageList.get(currentAge).sourceList.clear();
			else if (elements.length>=1 && elements[0].equals("clearAttractor")) ageList.get(currentAge).attractorList.clear();
			
			else if (elements.length>=1 && elements[0].equals("clearImages")){
				ageList.get(currentAge).setPicture(null);
				ageList.get(currentAge).setTactile(null);
				ageList.get(currentAge).setFlow(null);
				ageList.get(currentAge).setRail(null);
				ageList.get(currentAge).setArea(null);
				ageList.get(currentAge).setMagnetic(null);
			}
			// case erase everything
			else if (elements.length>=1 && elements[0].equals("clearAll")){
				ageList.get(currentAge).setPicture(null);
				ageList.get(currentAge).setTactile(null);
				ageList.get(currentAge).setFlow(null);
				ageList.get(currentAge).setRail(null);
				ageList.get(currentAge).setArea(null);
				ageList.get(currentAge).setMagnetic(null);
				ageList.get(currentAge).targetSequence.clear();
				ageList.get(currentAge).sourceList.clear();
				ageList.get(currentAge).areas.clear();
				ageList.get(currentAge).attractorList.clear();
			}
			
			// case default play
			else if (elements.length>=1 && elements[0].equals("play")) main.target_pause=false;
			else if (elements.length>=1 && elements[0].equals("stop")) main.target_pause=true;
			
				
			//////////////////////////////////////////
			// case age exit condition
			else if (elements.length>=4 && elements[0].equals("exit")){
				
				// detect missing exits		
				String exit=elements[2];
				boolean found=false;
				int a2=0;
				while (!found && a2<ageList.size()){
					if (exit.equals(ageList.get(a2).name))found=true;
					else a2++;
				}
						
				if (!found){		// missing age
					main.println("--- missing Age "+exit);
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
							ageList.get(currentAge).exitAreasId.add(Integer.parseInt(elements[1]));
							ageList.get(currentAge).ages.add(elements[2]);
							ageList.get(currentAge).reboot.add(Integer.parseInt(elements[3]));
							
							int id=ageList.size();
							loadAge(listFiles[a2]);									// load age file
							main.println("--- loaded age "+exit+" from age files");
							
							findMissingAges();
							
							ageList.get(currentAge).connections.add(id);
						}
						else main.println("--- Missing Age "+exit+" ! ");
					}
					else main.println("--- Missing Age "+exit+" ! ");
				}
				else{
					ageList.get(currentAge).exitAreasId.add(Integer.parseInt(elements[1]));
					ageList.get(currentAge).ages.add(elements[2]);
					ageList.get(currentAge).reboot.add(Integer.parseInt(elements[3]));
					
					findMissingAges();
					
					ageList.get(currentAge).connections.add(a2);	// indicate connection for the age display
				}	
			}
			
			///////////////////////////////////////////
			// case reset
			else if (elements.length>=1 && elements[0].equals("reset")){
				main.println("");
				main.println("");
				main.println("---------------------------------------------");
				main.println("Reset environment");
				main.println("---------------------------------------------");
				main.println("");
				
				// clear list of ages
				for (int a=0;a<ageList.size();a++) ageList.get(a).close();		
				ageList.clear();
				
				// create Primary Age
				ageList.add(new Age(main, "Primary Age"));
			}
			
			else if (elements.length>0) main.println("Command does not exist");
		}
		catch (Exception e) {main.println("/!\\ Error in Command line");}
		

		main.display.updateIndex(-1);
		main.display.updateMiniatures();
		main.display.updateLinks();
		main.display.repaint();
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void close(){
		for (int i=0;i<ageList.size();i++){
			ageList.get(i).close();
		}
	}
}
