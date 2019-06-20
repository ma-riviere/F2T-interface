package main;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


import script.Age;
import script.Script;
import display.*;

import org.urish.openal.OpenAL;

public class Main {

	
	private static float ALPHA=5;
	private static float GAMMA=100;
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static String OS = System.getProperty("os.name").toLowerCase();
	public static String PORT="/dev/ttyUSB";					// name of the port, without number

	public static String FILES="./";							// path to images
	public static String IMG="img/";							// sub paths to image types
	public static String TACTILE="tactile/";
	public static String FLOW="flow/";
	public static String RAIL="rail/";
	public static String PATH="path/";
	public static String PRESET="preset/";
	public static String AREA="area/";
	public static String MAGNETIC="magnetic/";
	public static String SCRIPT="script/";
	public static String SOUND="sound/";
	public static String SOURCE="source/";
	public static String AGE="age/";
	public static String ASSOCIATION="association/";
	public static String ATTRACTOR="attractor/";
	public static String SYMBOL="symbol/";
	
	public static int button1=96;								// action button key code
	public static int button2=96;
	public static int button3=96;
	public static boolean displayKey=false;
	
	public static int LENGTH=50;								// length of the trace
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// console messages
	public String console="";
	
	
	// serial interface
	public Interface inter;
	
	
	// display frames
	public TouchFrame touchFrame;
	public DisplayFrame display;
	// update display counter
	public int displayCounter=0;
	
	
	// pointer to current age
	public Age currentAge;
	
	// script defining ages
	public Script script;
	
	public Virtual virtual;
	
	public ArrayList<Symbol> symbolList;
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	

	// position of real cursor
	public float x=0;
	public float y=0;
	
	// position of finger
	public float px=0;
	public float py=0;
	
	
	// joystick input values
	public float jx=0;
	public float jy=0;
	
	public float jx_prev=0;
	public float jy_prev=0;

	// motor speed
	public float vmX=0;
	public float vmY=0;
	
	// difference between virtual and real position
	public float deltaX=0;
	public float deltaY=0;
	
	public float deltaX_prev1=0;
	public float deltaY_prev1=0;
	public float deltaX_prev2=0;
	public float deltaY_prev2=0;
	public float deltaX_prev3=0;
	public float deltaY_prev3=0;
	public float deltaX_prev4=0;
	public float deltaY_prev4=0;
	
	public float DdeltaX=0;
	public float DdeltaY=0;

	// trace buffer
	public float[][] trace;
	public int time;
	

	// target properties
	public boolean target_pause=true;
	public int target_speed=200;
	public int target_type=1;
	
	public int attractor_strength=200;
	public int attractor_distance=100;
	public int attractor_type=0;
	
	
	///////////////////////////////////////////////////////////////
	
	// name for save files
	public String presetName="preset";
	public String pathName="path";
	public String sourceName="source";
	public String associationName="association";
	public String attractorName="attractor";
	public String symbolName="symbol";
	
	public String[] listImages;
	public String[] listTactile;
	public String[] listFlow;
	public String[] listRail;
	public String[] listPath;
	public String[] listPreset;
	public String[] listArea;
	public String[] listMagnetic;
	public String[] listScript;
	public String[] listSource;
	public String[] listAssociation;
	public String[] listAttractors;
	private String[] listSymbols;
	
	public String[] listSound;
	public String[] listAge;
	
	public int selected_img=-1;
	public int selected_tactile=-1;
	public int selected_flow=-1;
	public int selected_rail=-1;
	public int selected_area=-1;
	public int selected_magnetic=-1;
	public int selected_symbol=-1;
	
	private int counter=0;
	
	//private long time3=0;
	
	private Comparator comp = new Comparator() {
        public int compare(Object o1, Object o2) {
            return ((String)o1).compareToIgnoreCase((String)o2);
        }
    };
	
	
	public boolean processing=false;
	
	
	public static OpenAL openal;  
	
	///////////////////////////////////////////////////////////////
	public Main(){
		
		try{ 
            openal = new OpenAL();
        }catch (Exception e){System.out.println(e);}

		time=0;
		trace=new float[LENGTH][4];

		symbolList=new ArrayList<Symbol>();
		
		
		///////////////////////////////////////////////////////////////////////////////////////////////

		// sequence of Ages
		script=new Script(this);
		currentAge=script.ageList.get(script.currentAge);
		
		// detection of available files
		listFiles();
		
		virtual=new Virtual(this);
		
		//////////////////////////////////////////////////////////////////////////////////////////
		// initialization of main display panels
		touchFrame=new TouchFrame(this);
		display=new DisplayFrame(this);
		
		// F2T interface
		inter=new Interface(this);

		virtual.start();
	}
	
	
	public void controlLoop(){
		
		processing=true;
		
		counter++;
		if (counter>=1000){
		//System.out.println("+++ "+((System.nanoTime()-time3))/1000);
		//time3=System.nanoTime();
		counter=0;
		}
		
		// pointer to current age
		if (script.currentAge>=0 && script.currentAge<script.ageList.size()) currentAge=script.ageList.get(script.currentAge);
		
		
		vmX=0;
		vmY=0;


				
		// P
		vmX +=jx*20 + 500*(jx-jx_prev);
		vmY +=jy*20 + 500*(jy-jy_prev);
		

		if (vmX>-5 && vmX<5) vmX=0;
		if (vmY>-5 && vmY<5) vmY=0;
		
		if (vmX>255) vmX=255;
		if (vmX<-255) vmX=-255;
		if (vmY>255) vmY=255;
		if (vmY<-255) vmY=-255;

		px=x+3*jx;
		py=y+3*jy;

		//px=x;
		//py=y;
		
		if (px>349) px=349;
		if (px<-349) px=-349;
		if (py>349) py=349;
		if (py<-349) py=-349;
		
		float speed2=vmX*vmX+vmY*vmY;
		
		if (speed2>64516){ // 254²
			float speed=(float) Math.sqrt(speed2);
			vmX=vmX*254/speed;
			vmY=vmY*254/speed;
		}
		
		jx_prev=jx;
		jy_prev=jy;

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////

		//vmX=0;//virtual.mx/10;
		//vmY=0;//virtual.my/10;
		
		deltaX=px-virtual.px;
		deltaY=py-virtual.py;
		
		
		DdeltaX=deltaX - deltaX_prev1;
		DdeltaY=deltaY - deltaY_prev1;
		
		//System.out.println(DdeltaX+" , "+DdeltaY);
		
		
		deltaX_prev2=deltaX_prev1;
		deltaY_prev2=deltaY_prev1;
		deltaX_prev1=deltaX;
		deltaY_prev1=deltaY;

		
		/*if (deltaX>30) vmX+=254;
		else if (deltaX<-30) vmX-=254;
		else*/ vmX-=ALPHA*deltaX + GAMMA*DdeltaX;
		
		/*if (deltaY>30) vmY+=254;
		else if (deltaY<-30) vmY-=254;
		else*/ vmY-=ALPHA*deltaY + GAMMA*DdeltaY;
		
		
		//for (int i=0;i<virtual.moduleList.size();i++) virtual.moduleList.get(i).compute2();
		
		if (vmX*vmX+vmY*vmY<100){
			vmX=0;
			vmY=0;
		}
		
		if (vmX<5 && vmX>-5) vmX=0;
		if (vmY<5 && vmY>-5) vmY=0;
		
		/*float speed2=vmX*vmX+vmY*vmY;
		if (speed2>64516){ // 254²
			float speed=(float) Math.sqrt(speed2);
			vmX=vmX*254/speed;
			vmY=vmY*254/speed;
		}*/
		
		//vmX=0;
		//vmY=0;
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// send motor commands
		int speedX=(int) Math.abs(vmX);
		if (speedX>255) speedX=254;
		else if (speedX<2) speedX=0;
		else speedX=speedX-1;
		
		int speedY=(int) Math.abs(vmY);
		if (speedY>255) speedY=254;
		else if (speedY<2) speedY=0;
		else speedY=speedY-1;
		
		int directions=0;
		if (vmX<=0) directions+=2;
		if (vmY<=0) directions+=1;
		
		//System.out.println(speedX+" , "+speedY+" -> "+(int)(Math.sqrt(speedX*speedX+speedY*speedY)));
		
		//speedX=0;
		//speedY=0;
		
		//System.out.println(vmX+" , "+vmY);
		//System.out.println(virtual.mx+" , "+virtual.my);
		//System.out.println(speedX+" , "+speedY);
		
		byte[] msg=new byte[4];
		msg[0]=(byte) 255;
		msg[1]=(byte)directions;
		msg[2]=(byte)speedX;
		msg[3]=(byte)speedY;
		inter.sendMsg(msg);
		
		////////////////////////////////////////////////////////
		
		for (int i=0;i<symbolList.size();i++){
			
			int rec=symbolList.get(i).recognize();
			
			if (rec==-1) System.out.println(symbolList.get(i).name);
			
			/*System.out.print(symbolList.get(i).name+" , "+symbolList.get(i).sequence.size()+" -> ");
			for (int j=0;j<rec.length;j++){
				System.out.print(rec[j]+ ", ");
			}
			System.out.println();*/
			
		}
		
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// update display panels
		if (displayCounter==0){
			//touchFrame.repaint();
			display.repaint();
		}
		displayCounter++;
		if (displayCounter>10) displayCounter=0;
		
		//////////////////////////////
		// update user trace
		updateTrace();
		
		
		//////////////////////////////
		// update script			
		script.play(Math.min(699,Math.max(0,350+(int)(px))),Math.min(699,Math.max(0,350-(int)(py))));
		


		//try {Thread.sleep(0,5000);
		//} catch (InterruptedException e) {e.printStackTrace();}
		
		//try {sleepNanos(500000);
		//} catch (InterruptedException e) {e.printStackTrace();}
		
		//if (received) System.out.println("+++ "+(System.nanoTime()-time3));
		//else System.out.println("--- "+(System.nanoTime()-time3));
		
		processing=false;
	}
	
	
	public static void sleepNanos (long nanoDuration) throws InterruptedException {
		long time1=System.nanoTime();
		long time2=System.nanoTime();
		while (time2-time1<nanoDuration){
			time2=System.nanoTime();
		}
    }
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// file lister
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	public void listFiles(){

		File repertoire = new File(FILES+IMG);
		if (repertoire.exists()){
			listImages=repertoire.list();
			Arrays.sort(listImages, comp);
		}
		else System.out.println("Image file directory does not exist");

		repertoire = new File(FILES+TACTILE);
		if (repertoire.exists()){
			listTactile=repertoire.list();
			Arrays.sort(listTactile, comp);
		}
		else System.out.println("Tactile file directory does not exist");

		repertoire = new File(FILES+FLOW);
		if (repertoire.exists()){
			listFlow=repertoire.list();
			Arrays.sort(listFlow, comp);
		}
		else System.out.println("Flow file directory does not exist");

		repertoire = new File(FILES+RAIL);
		if (repertoire.exists()){
			listRail=repertoire.list();
			Arrays.sort(listRail, comp);
		}
		else System.out.println("Rail file directory does not exist");

		repertoire = new File(FILES+AREA);
		if (repertoire.exists()){
			listArea=repertoire.list();
			Arrays.sort(listArea, comp);
		}
		else System.out.println("Area file directory does not exist");
		
		repertoire = new File(FILES+MAGNETIC);
		if (repertoire.exists()){
			listMagnetic=repertoire.list();
			Arrays.sort(listMagnetic, comp);
		}
		else System.out.println("Magnetic file directory does not exist");
		
		
		repertoire = new File(FILES+PRESET);
		if (repertoire.exists()){
			listPreset=repertoire.list();
			Arrays.sort(listPreset, comp);
		}
		else System.out.println("Preset file directory does not exist");

		repertoire = new File(FILES+PATH);
		if (repertoire.exists()){
			listPath=repertoire.list();
			Arrays.sort(listPath, comp);
		}
		else System.out.println("Path file directory does not exist");

		repertoire = new File(FILES+SOURCE);
		if (repertoire.exists()){
			listSource=repertoire.list();
			Arrays.sort(listSource, comp);
		}
		else System.out.println("Source file directory does not exist");
		
		repertoire = new File(FILES+ASSOCIATION);
		if (repertoire.exists()){
			listAssociation=repertoire.list();
			Arrays.sort(listAssociation, comp);
		}
		else System.out.println("Association file directory does not exist");
		
		System.out.println("+++3 "+script.ageList.get(script.currentAge).attractorList.size());
		
		repertoire = new File(FILES+ATTRACTOR);
		if (repertoire.exists()){
			listAttractors=repertoire.list();
			Arrays.sort(listAttractors, comp);
		}
		else System.out.println("Attractor file directory does not exist");
		
		repertoire = new File(FILES+SYMBOL);
		if (repertoire.exists()){
			listSymbols=repertoire.list();
			Arrays.sort(listSymbols, comp);
		}
		else System.out.println("Symbol file directory does not exist");
		symbolList.clear();
		for (int i=0;i<listSymbols.length;i++) loadSymbol(listSymbols[i]);
		
		repertoire = new File(FILES+SCRIPT);
		if (repertoire.exists()){
			listScript=repertoire.list();
			Arrays.sort(listScript, comp);
		}
		else System.out.println("Script file directory does not exist");

		repertoire = new File(FILES+SOUND);
		if (repertoire.exists()){
			listSound=repertoire.list();
			Arrays.sort(listSound, comp);
		}
		else System.out.println("Sound file directory does not exist");
		
		repertoire = new File(FILES+AGE);
		if (repertoire.exists()){
			listAge=repertoire.list();
			Arrays.sort(listAge, comp);
		}
		else System.out.println("Age file directory does not exist");

		// get a name to save a preset
		boolean found=false;
		int i=0;
		presetName="preset0";
		while (!found){
			presetName="preset"+i;
			found=true;
			int n=0;
			while (found && n<listPreset.length){
				if (presetName.equals(listPreset[n])) found=false;
				n++;
			}
			i++;
		}

		// get a name to save a path
		found=false;
		i=0;
		pathName="path0";
		while (!found){
			pathName="path"+i;
			found=true;
			int n=0;
			while (found && n<listPath.length){
				if (pathName.equals(listPath[n])) found=false;
				n++;
			}
			i++;
		}

		// get a name to save a sound source list
		found=false;
		i=0;
		sourceName="source0";
		while (!found){
			sourceName="source"+i;
			found=true;
			int n=0;
			while (found && n<listSource.length){
				if (sourceName.equals(listSource[n])) found=false;
				n++;
			}
			i++;
		}

		// get a name to save an association list
		found=false;
		i=0;
		associationName="association0";
		while (!found){
			associationName="association"+i;
			found=true;
			int n=0;
			while (found && n<listAssociation.length){
				if (associationName.equals(listAssociation[n])) found=false;
				n++;
			}
			i++;
		}

		// get a name to save an attractor list
		found=false;
		i=0;
		attractorName="attractor0";
		while (!found){
			attractorName="attractor"+i;
			found=true;
			int n=0;
			while (found && n<listAttractors.length){
				if (attractorName.equals(listAttractors[n])) found=false;
				n++;
			}
			i++;
		}
		
		// get a name to save an attractor list
		found=false;
		i=0;
		symbolName="symbol0";
		while (!found){
			symbolName="symbol"+i;
			found=true;
			int n=0;
			while (found && n<listSymbols.length){
				if (symbolName.equals(listSymbols[n])) found=false;
				n++;
			}
			i++;
		}
		
		if (display!=null) display.rescan();
		
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// save elements of current Age : preset, path and source list
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	public void savePreset(){
		
		if (currentAge.image.view==null
		 && currentAge.image.tactile==null 
		 && currentAge.image.flow==null 
		 && currentAge.image.rail==null
		 && currentAge.image.area==null
		 && currentAge.image.magnetic==null){
			System.out.println("No element selected, pre-set not saved");
		}
		else{
			String fileName = FILES+PRESET+presetName;
			try {
				PrintWriter file  = new PrintWriter(new FileWriter(fileName));
				if (currentAge.image.view!=null)    file.println("image "+currentAge.image.view);
				if (currentAge.image.tactile!=null) file.println("tactile "+currentAge.image.tactile);
				if (currentAge.image.flow!=null)    file.println("flow "+currentAge.image.flow);
				if (currentAge.image.rail!=null)    file.println("rail "+currentAge.image.rail);
				if (currentAge.image.area!=null)    file.println("area "+currentAge.image.area);
				if (currentAge.image.magnetic!=null)file.println("magnetic "+currentAge.image.magnetic);
				file.close();
				System.out.println("preset saved : "+presetName);
			}
			catch (Exception e) {e.printStackTrace();}
			listFiles();
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////
	public void savePath(){
		if (currentAge.targetSequence.sizeTarget()==0) System.out.println("No path to save");
		else{
			String fileName = FILES+PATH+pathName;
			try {
				PrintWriter file  = new PrintWriter(new FileWriter(fileName));
				for (int i=0;i<currentAge.targetSequence.sizeTarget();i++){
					file.println("t "+currentAge.targetSequence.getString(i));
				}
				file.close();
				System.out.println("path saved : "+pathName);
			}
			catch (Exception e) {e.printStackTrace();}
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////
	public void saveSource(String name){
		if (currentAge.sourceList.size()==0) System.out.println("No sound source to save");
		else{
			String fileName = FILES+SOURCE+name;
			try {
				PrintWriter file  = new PrintWriter(new FileWriter(fileName));
				for (int i=0;i<currentAge.sourceList.size();i++){
					file.println("s "+currentAge.sourceList.get(i).px+" "+currentAge.sourceList.get(i).py);
				}
				file.close();
				System.out.println("source configuration saved : "+name);
			}
			catch (Exception e) {e.printStackTrace();}
		}
	}

	//////////////////////////////////////////////////////////////////////////////
	public void saveAssociation(String name){
		if (currentAge.areas.areas.size()==0) System.out.println("No sound association to save");
		else{
			String fileName = FILES+ASSOCIATION+name;
			try {
				PrintWriter file  = new PrintWriter(new FileWriter(fileName));
				for (int i=0;i<currentAge.areas.areas.size();i++){
					file.println("a "+currentAge.areas.areas.get(i)+" "+currentAge.areas.types.get(i)+" "+currentAge.areas.repeat.get(i)+" "+currentAge.areas.soundFiles.get(i));
				}
				file.close();
				System.out.println("associationList saved : "+name);
			}
			catch (Exception e) {e.printStackTrace();}
		}
	}
	
	
	private void loadSymbol(String file){
		
		System.out.println("Load symbol file: "+file);
		
		String fileName = Main.FILES+Main.SYMBOL+file;
		String[] elements;
		
		symbolList.add(new Symbol(this, file));
		int symb=symbolList.size()-1;
		
		try {
			InputStream ips=new FileInputStream(fileName); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line;
			line=br.readLine();
			
			while (line!=null){

				elements=line.split(" ");
				if (elements.length>=5 && elements[0].equals("t")){
					if (elements.length<7)
						symbolList.get(symb).setTarget(new Target(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]),
													          	  Integer.parseInt(elements[3]), Integer.parseInt(elements[4]) ));
					else
						symbolList.get(symb).setTarget(new Target(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]),
														  		  Integer.parseInt(elements[3]), Integer.parseInt(elements[4]), 
														  		  Float.parseFloat(elements[5]), Float.parseFloat(elements[6]) ));
				}
				line=br.readLine();
			}
			br.close();
		}
		catch (Exception e) {System.out.println("   /!\\ Path file not found or containing errors");}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Set image files
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	// picture
	public void setPicture(String img_file){
		script.ageList.get(script.currentAge).setPicture(img_file);
	}
	
	// tactile image
	public void setTactile(String tactile_file){
		script.ageList.get(script.currentAge).setTactile(tactile_file);
	}
	
	// flow file
	public void setFlow(String flow_file){
		script.ageList.get(script.currentAge).setFlow(flow_file);
	}
	
	// rail file
	public void setRail(String rail_file){
		script.ageList.get(script.currentAge).setRail(rail_file);
	}
	
	// area descriptor file
	public void setArea(String area_file){
		script.ageList.get(script.currentAge).setArea(area_file);
	}
	
	// magnetic descriptor file
	public void setMagnetic(String magnetic_file){
		script.ageList.get(script.currentAge).setMagnetic(magnetic_file);
	}
	
	
	public void clearPath(){
		script.ageList.get(script.currentAge).targetSequence.clear();
	}

	
	///////////////////////////////////////////////////////////


	
	
	public void updateTrace(){
		trace[time][0]=x-3*jx;
		trace[time][1]=y-3*jy;
		trace[time][2]=virtual.px;
		trace[time][3]=virtual.py;
		time++;
		if (time>=LENGTH) time=0;
	}
	
	
	
	public void println(String line){
		console+=line+"\n";
		display.updateConsole(console);
		System.out.println(line);
	}
	
	
	public void close(){
		
		byte[] msg=new byte[4];
		msg[0]=(byte) 255;
		msg[1]=(byte)0;
		msg[2]=(byte)0;
		msg[3]=(byte)0;
		inter.sendMsg(msg);
		
		openal.close();
		inter.close();
		script.close();
		
		virtual.stop=true;
		try {virtual.join();} 
		catch (InterruptedException e) {e.printStackTrace();}
	}
	
	
	////////////////////////////////////////
	public static void main(String[] args){

		// detect OS
		if (OS.indexOf("win") >= 0){
			PORT="COM";					// name of the port, without number

			FILES=".\\";							// path to images
			IMG="img\\";							// sub paths to image types
			TACTILE="tactile\\";
			FLOW="flow\\";
			RAIL="rail\\";
			PATH="path\\";
			PRESET="preset\\";
			AREA="area\\";
			MAGNETIC="magnetic\\";
			SCRIPT="script\\";
			SOUND="sound\\";
			SOURCE="source\\";
			AGE="age\\";
			ASSOCIATION="association\\";
			ATTRACTOR="attractor\\";
			SYMBOL="symbol\\";
		}
		
		
		// load parameters from init file
		String fileName = Main.FILES+"param.ini";
		String[] elements;

		try {
			InputStream ips=new FileInputStream(fileName); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line=null;
			line=br.readLine();
			
			while (line!=null){
				elements=line.split(" ");
				if (elements.length>=2 && elements[0].equals("actionkeys")){
					button1=Integer.parseInt(elements[1]);
					if (elements.length>=3) button2=Integer.parseInt(elements[2]);
					if (elements.length>=4) button3=Integer.parseInt(elements[3]);
				}
				else if (elements.length>=2 && elements[0].equals("displaykey")) displayKey=(!elements[1].equals("0"));
				else if (elements.length>=2 && elements[0].equals("trace")) LENGTH=Integer.parseInt(elements[1]);
				else if (elements.length>=2 && elements[0].equals("path")) FILES=elements[1];
				line=br.readLine();
			}
			br.close();
			System.out.println(LENGTH);
		}
		catch (Exception e) {System.out.println("init file not found");}

		/////////////////
		new Main();
		/////////////////
	}
	
}
