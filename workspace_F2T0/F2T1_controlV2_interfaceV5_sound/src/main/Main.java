package main;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;


import script.Age;
import script.Script;
import display.*;
import modules.*;

import org.urish.openal.OpenAL;

public class Main {

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
	
	public static int button1=96;								// action button key code
	public static int button2=96;
	public static int button3=96;
	public static boolean displayKey=false;
	
	public static int LENGTH=500;								// length of the trace
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// console messages
	public String console="";
	
	
	// serial interface
	public Interface inter;
	
	
	// main display frames
	public TouchFrame touchFrame;
	public DisplayFrame display;
	
	
	// pointer to current age
	public Age currentAge;
	
	// script defining ages
	public Script script;
	
	// control modules
	public Frictions friction;
	public Edges edges;
	public Flow flow;
	public Rail rail;
	public Path path;
	public Magnetic magnetic;
	public Attraction attraction;
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	

	// position of cursor, previous and predicted
	public float x=0;
	public float y=0;
	
	public float x_prev=0;
	public float y_prev=0;

	public float x_prev1=0;
	public float y_prev1=0;
	
	public float x_prev2=0;
	public float y_prev2=0;
	
	public float x_next=0;
	public float y_next=0;
	
	// position of finger
	public float px=0;
	public float py=0;
	
	
	// joystick input values
	public float dx=0;
	public float dy=0;
	
	public float dx_prev=0;
	public float dy_prev=0;
	
	public float Idx=0;	// integration
	public float Idy=0;
	
	public float Ddx=0;	// derivation
	public float Ddy=0;
	
	
	// motor speed
	public float mX=0;
	public float mY=0;
	
	
	
	public int area_red_read=0;
	public int area_green_read=0;
	public int area_blue_read=0;
	

	// trace buffer
	public float[][] trace;
	public int time;
	public int count;
	
	// display counter
	public int displayCounter=0;

	
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
	
	public String[] listSound;
	public String[] listAge;
	
	public int selected_img=-1;
	public int selected_tactile=-1;
	public int selected_flow=-1;
	public int selected_rail=-1;
	public int selected_area=-1;
	public int selected_magnetic=-1;
	
	private int counter=0;
	
	private long time3=0;
	
	
	public boolean processing=false;
	
	public ArrayList<Integer> jx;
	public ArrayList<Integer> jy;
	
	//public MainFrame mainFrame;
	
	public static OpenAL openal;  
	
	///////////////////////////////////////////////////////////////
	public Main(){
		
		try{ 
            openal = new OpenAL();
        }catch (Exception e){System.out.println(e);}
		
		
		friction=new Frictions(this);
		edges=new Edges(this);
		flow=new Flow(this);
		rail=new Rail(this);
		path=new Path(this);
		magnetic=new Magnetic(this);
		attraction=new Attraction(this);
		
		time=0;
		count=0;
		trace=new float[LENGTH][2];
		
		jx=new ArrayList<Integer>();
		jy=new ArrayList<Integer>();
		//mainFrame=new MainFrame(this);
		
		
		///////////////////////////////////////////////////////////////////////////////////////////////

		// F2T interface
		inter=new Interface(this);

		// sequence of Ages
		script=new Script(this);
		currentAge=script.ageList.get(script.currentAge);
		
		// detection of available files
		listFiles();
		
		
		//////////////////////////////////////////////////////////////////////////////////////////
		// initialization of main display panels
		touchFrame=new TouchFrame(this);
		display=new DisplayFrame(this);
	}
	
	
	public void controlLoop(){
		
		processing=true;
		
		counter++;
		if (counter>=1000){
		System.out.println("+++ "+((System.nanoTime()-time3))/1000);
		time3=System.nanoTime();
		counter=0;
		}
		
		// pointer to current age
		if (script.currentAge>=0 && script.currentAge<script.ageList.size()) currentAge=script.ageList.get(script.currentAge);
		
		
		mX=0;
		mY=0;
		
		Idx+=dx;
		Idy+=dy;
		
		Ddx=dx-dx_prev;
		Ddy=dy-dy_prev;
		
		dx_prev=dx;
		dy_prev=dy;
				
		// P
		mX +=dx*65;
		mY +=dy*65;
		
		// I
		//mX+=0.001*Idx;
		//mY+=0.001*Idy;

		// D
		//mX+=100*Ddx;
		//mY+=100*Ddy;
		
		if (mX>-50 && mX<50) mX=0;
		if (mY>-50 && mY<50) mY=0;
		
		if (mX>255) mX=255;
		if (mX<-255) mX=-255;
		if (mY>255) mY=255;
		if (mY<-255) mY=-255;

		x_next=x + 5*(x-x_prev2);
		y_next=y + 5*(y-y_prev2);
		
		px=x+dx;
		py=y+dy;

		if (px>349) px=349;
		if (px<-349) px=-349;
		if (py>349) py=349;
		if (py<-349) py=-349;
		
		

		///////////////////////////////////////////////////////////
		// get friction values (fluid and solid) and send value
		friction.compute();

		// define edges
		edges.compute();

		// define flow
		flow.compute();
		
		// define rail
		rail.compute();
		
		// magnetic rail
		magnetic.compute();
		
		// attractive points
		attraction.compute();
		

		// if there is a target
		path.compute();
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// send motor commands
		int speedX=(int) Math.abs(mX);
		if (speedX>255) speedX=254;
		else if (speedX<5) speedX=0;
		else speedX=speedX-1;
		
		int speedY=(int) Math.abs(mY);
		if (speedY>255) speedY=254;
		else if (speedY<5) speedY=0;
		else speedY=speedY-1;
		
		int directions=0;
		if (mX>=0) directions+=1;
		if (mY>=0) directions+=2;
		
		
		byte[] msg=new byte[4];
		msg[0]=(byte) 255;
		msg[1]=(byte)directions;
		msg[2]=(byte)speedX;
		msg[3]=(byte)speedY;
		inter.sendMsg(msg);
		
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// update display panels
		if (displayCounter==0){
			touchFrame.repaint();
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
		

		area_red_read=currentAge.current_area_red;
		area_green_read=currentAge.current_area_green;
		area_blue_read=currentAge.current_area_blue;
		
		
		//try {Thread.sleep(0,5000);
		//} catch (InterruptedException e) {e.printStackTrace();}
		
		//try {sleepNanos(500000);
		//} catch (InterruptedException e) {e.printStackTrace();}
		
		//if (received) System.out.println("+++ "+(System.nanoTime()-time3));
		//else System.out.println("--- "+(System.nanoTime()-time3));
		
		processing=false;
		
		x_prev2=x_prev1;
		y_prev2=y_prev1;
		
		x_prev1=x_prev;
		y_prev1=y_prev;
		
		x_prev=x;
		y_prev=y;
		
		//mainFrame.repaint();
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
		
		//for (int a=0;a<script.ageList.size();a++) script.ageList.get(a).close();
		//script.ageList.clear();
		//script.ageList.add(new Age(this, "Primary Age"));

		File repertoire = new File(FILES+IMG);
		if (repertoire.exists()){
			listImages=repertoire.list();
		}
		else System.out.println("Image file directory does not exist");

		repertoire = new File(FILES+TACTILE);
		if (repertoire.exists()){
			listTactile=repertoire.list();
		}
		else System.out.println("Tactile file directory does not exist");

		repertoire = new File(FILES+FLOW);
		if (repertoire.exists()){
			listFlow=repertoire.list();
		}
		else System.out.println("Flow file directory does not exist");

		repertoire = new File(FILES+RAIL);
		if (repertoire.exists()){
			listRail=repertoire.list();
		}
		else System.out.println("Rail file directory does not exist");

		repertoire = new File(FILES+AREA);
		if (repertoire.exists()){
			listArea=repertoire.list();
		}
		else System.out.println("Area file directory does not exist");
		
		repertoire = new File(FILES+MAGNETIC);
		if (repertoire.exists()){
			listMagnetic=repertoire.list();
		}
		else System.out.println("Magnetic file directory does not exist");
		
		
		repertoire = new File(FILES+PRESET);
		if (repertoire.exists()){
			listPreset=repertoire.list();
		}
		else System.out.println("Preset file directory does not exist");

		repertoire = new File(FILES+PATH);
		if (repertoire.exists()){
			listPath=repertoire.list();
		}
		else System.out.println("Path file directory does not exist");

		repertoire = new File(FILES+SOURCE);
		if (repertoire.exists()){
			listSource=repertoire.list();
		}
		else System.out.println("Source file directory does not exist");
		
		repertoire = new File(FILES+ASSOCIATION);
		if (repertoire.exists()){
			listAssociation=repertoire.list();
		}
		else System.out.println("Association file directory does not exist");
		
		System.out.println("+++3 "+script.ageList.get(script.currentAge).attractorList.size());
		
		repertoire = new File(FILES+ATTRACTOR);
		if (repertoire.exists()){
			listAttractors=repertoire.list();
		}
		else System.out.println("Attractor file directory does not exist");
		
		repertoire = new File(FILES+SCRIPT);
		if (repertoire.exists()){
			listScript=repertoire.list();
		}
		else System.out.println("Script file directory does not exist");

		repertoire = new File(FILES+SOUND);
		if (repertoire.exists()){
			listSound=repertoire.list();
		}
		else System.out.println("Sound file directory does not exist");
		
		repertoire = new File(FILES+AGE);
		if (repertoire.exists()){
			listAge=repertoire.list();
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
		if (currentAge.targetSequence.size()==0) System.out.println("No path to save");
		else{
			String fileName = FILES+PATH+pathName;
			try {
				PrintWriter file  = new PrintWriter(new FileWriter(fileName));
				for (int i=0;i<currentAge.targetSequence.size();i++){
					file.println("t "+currentAge.targetSequence.get(i).x+" "+currentAge.targetSequence.get(i).y+" "+currentAge.targetSequence.get(i).speed+" "+currentAge.targetSequence.get(i).control);
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
		//inter.sendMsg("t1");
	}

	
	///////////////////////////////////////////////////////////


	
	
	public void updateTrace(){
		trace[time][0]=x;
		trace[time][1]=y;
		time++;
		//System.out.println(time);
		if (time>=LENGTH) time=0;
	}
	
	
	
	public void println(String line){
		console+=line+"\n";
		display.updateConsole(console);
		System.out.println(line);
	}
	
	
	public void close(){
		openal.close();
		inter.close();
		script.close();
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
