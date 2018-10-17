package main;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import script.Age;
import script.Script;
import display.*;

public class Main {

	/////////////////////////////////////////////////////////////////////////////////////////////////////

	public static String FILES="../../thermoformed_imagesV2/";	// path to images
	
	public static String IMG="img/";							// sub paths to image types
	public static String PRESET="preset/";
	public static String AREA="area/";
	public static String SCRIPT="script/";
	public static String SOUND="sound/";
	public static String SOURCE="source/";
	public static String AGE="age/";
	
	public static int button=96;								// action button key code
	public static boolean displayKey=false;
	
	public static int LENGTH=500;								// length of the trace
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////

	// main display frames
	public DisplayFrame display;
	
	
	// pointer to current age
	public Age currentAge;
	
	// script defining ages
	public Script script;
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	

	// position of cursor, previous and predicted
	public float x=0;
	public float y=0;
	
	public float x_prev=0;
	public float y_prev=0;
	
	public float x_prev2=0;
	public float y_prev2=0;
	
	
	// joystick input values
	public float dx=0;
	public float dy=0;
	
	// tactile property values
	public int area_red_read=0;
	public int area_green_read=0;
	public int area_blue_read=0;

	// trace buffer
	public float[][] trace;
	public int time;
	public int count;

	public boolean clicked=false;
	public int frame_counter=0;
	
	///////////////////////////////////////////////////////////////
	
	// name for save files
	public String presetName="preset";
	public String pathName="path";
	public String sourceName="source";
	
	public String[] listImages;
	public String[] listPreset;
	public String[] listArea;
	public String[] listScript;
	public String[] listSource;
	
	public int selected_img=-1;
	public int selected_area=-1;
	
	
	///////////////////////////////////////////////////////////////
	public Main(){
		
		
		time=0;
		count=0;
		trace=new float[LENGTH][3];
		
		///////////////////////////////////////////////////////////////////////////////////////////////

		// sequence of Ages
		script=new Script(this);
		currentAge=script.ageList.get(script.currentAge);
		
		// detection of available files
		listFiles();

		
		//////////////////////////////////////////////////////////////////////////////////////////
		// initialization of main display panels
		//touchFrame=new TouchFrame(this);
		display=new DisplayFrame(this);
		
		
		//////////////////////////////////////////////////////////////////////////////////////////
		while (true){
			
			// pointer to current age
			if (script.currentAge>=0 && script.currentAge<script.ageList.size()) currentAge=script.ageList.get(script.currentAge);
			
			////////////////////////////////////////////////////////////////////////
			// get position : get x, y, x_prev, y_prev and predict x_next and y_next
			getTouchedPosition();

			///////////////////////////////////////////////////////////
			// get user speed dx and dy
			getUserMovement();

			
			
			/////////////////////////////////////////////////////////////////////////////
			// detect areas
			area_red_read=currentAge.image.area_mat[Math.min(699,Math.max(0,350+(int)(x+dx)))][Math.min(699,Math.max(0,350-(int)(y+dy)))][0];
			area_green_read=currentAge.image.area_mat[Math.min(699,Math.max(0,350+(int)(x+dx)))][Math.min(699,Math.max(0,350-(int)(y+dy)))][1];
			area_blue_read=currentAge.image.area_mat[Math.min(699,Math.max(0,350+(int)(x+dx)))][Math.min(699,Math.max(0,350-(int)(y+dy)))][2];
			
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// update display panels
			
			//touchFrame.repaint();
			display.repaint();
			
			//////////////////////////////
			// update user trace
			updateTrace();
			
			
			//////////////////////////////
			// update script			
			script.play(Math.min(699,Math.max(0,350+(int)(x+dx))),Math.min(699,Math.max(0,350-(int)(y+dy))));
			
			
			try {Thread.sleep(10);
			} catch (InterruptedException e) {e.printStackTrace();}
			
		}
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// file lister
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	public void listFiles(){
		
		for (int a=0;a<script.ageList.size();a++) script.ageList.get(a).close();
		script.ageList.clear();
		script.ageList.add(new Age(this, "Primary Age"));

		File repertoire = new File(FILES+IMG);
		if (repertoire.exists()){
			listImages=repertoire.list();
		}
		else System.out.println("Image file directory does not exist");

		repertoire = new File(FILES+AREA);
		if (repertoire.exists()){
			listArea=repertoire.list();
		}
		else System.out.println("Area file directory does not exist");
		
		
		repertoire = new File(FILES+PRESET);
		if (repertoire.exists()){
			listPreset=repertoire.list();
		}
		else System.out.println("Preset file directory does not exist");

		repertoire = new File(FILES+SOURCE);
		if (repertoire.exists()){
			listSource=repertoire.list();
		}
		else System.out.println("Source file directory does not exist");
		
		
		repertoire = new File(FILES+SCRIPT);
		if (repertoire.exists()){
			listScript=repertoire.list();
		}
		else System.out.println("Script file directory does not exist");


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
		
		if (display!=null) display.rescan();
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// save elements of current Age : preset, path and source list
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	public void savePreset(){
		
		if (currentAge.image.view==null
		 && currentAge.image.area==null){
			System.out.println("No element selected, pre-set not saved");
		}
		else{
			String fileName = FILES+PRESET+presetName;
			try {
				PrintWriter file  = new PrintWriter(new FileWriter(fileName));
				if (currentAge.image.view!=null)    file.println("image "+currentAge.image.view);
				if (currentAge.image.area!=null)    file.println("area "+currentAge.image.area);
				file.close();
				System.out.println("preset saved : "+presetName);
			}
			catch (Exception e) {e.printStackTrace();}
			listFiles();
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////
	public void saveSource(){
		if (currentAge.sourceList.size()==0) System.out.println("No sound source to save");
		else{
			String fileName = FILES+SOURCE+sourceName;
			try {
				PrintWriter file  = new PrintWriter(new FileWriter(fileName));
				for (int i=0;i<currentAge.sourceList.size();i++){
					file.println("s "+currentAge.sourceList.get(i).px+" "+currentAge.sourceList.get(i).py);
				}
				file.close();
				System.out.println("source configuration saved : "+sourceName);
			}
			catch (Exception e) {e.printStackTrace();}
		}
	}

	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Load image files
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	// picture
	public void setPicture(String img_file){
		script.ageList.get(script.currentAge).setPicture(img_file);
	}
	
	// area descriptor file
	public void setArea(String area_file){
		script.ageList.get(script.currentAge).setArea(area_file);
	}

	
	///////////////////////////////////////////////////////////
	
	public void getTouchedPosition(){
		
		x_prev2=x_prev;
		y_prev2=y_prev;
		
		x_prev=x;
		y_prev=y;	
		
		x=display.getTouchX();
		y=display.getTouchY();
		
		if (clicked && frame_counter<3) frame_counter++;
	}
	
	
	public void getUserMovement(){
		if (frame_counter>=3){
			dx=x-x_prev2;
			dy=y-y_prev2;
		}
		else{
			dx=0;
			dy=0;
		}
	}
	

	public void updateTrace(){
		trace[time][0]=x;
		trace[time][1]=y;
		
		if (frame_counter>0) trace[time][2]=1;
		else trace[time][2]=0;
		
		time++;
		if (time>=LENGTH) time=0;
	}
	
	
	
	
	public void close(){
		script.close();
	}
	
	
	public static void main(String[] args){
		new Main();
	}
	
}
