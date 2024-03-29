package main;


import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import script.Age;
import script.Script;
import display.*;

public class Main {

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static boolean CAMERA_CONNECTED=true;
	
	
	public static String PORT="/dev/ttyUSB";						// name of the port, without number
	
	//public static String FILES="/home/simon/Bureau/F2T_images/";	// path to images
	public static String FILES="../../F2T_images/";	// path to images
	//public static String FILES="./";
	public static String IMG="img/";								// sub paths to image types
	public static String TACTILE="tactile/";
	public static String FLOW="flow/";
	public static String RAIL="rail/";
	public static String PATH="path/";
	public static String PRESET="preset/";
	public static String AREA="area/";
	public static String SCRIPT="script/";
	public static String SOUND="sound/";
	public static String SOURCE="source/";
	
	public static int LENGTH=500;									// length of the trace
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// serial interface
	public Interface inter;
	
	
	// main display frames
	//public TouchFrame touchFrame;
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

	public float x_next=0;
	public float y_next=0;
	
	
	// joystick input values
	public float jx=0;
	public float jy=0;
	
	public float dx=0;
	public float dy=0;
	
	// tactile property values
	public float friction_fluid_read=0;
	public float friction_solid_read=0;
	
	private float friction_fluid=0;
	private int previous_fluid=0;
	
	private float friction_solid=0;
	private int previous_solid=0;
	
	public float gx=0;
	public float gy=0;
	public int previous_gx=0;
	public int previous_gy=0;
	
	public int flowX_read=0;
	public int flowY_read=0;
	
	public int railX_read=0;
	public int railY_read=0;
	
	private int flowX=0;
	private int flowY=0;
	private int previous_flowX=0;
	private int previous_flowY=0;
	
	public int area_red_read=0;
	public int area_green_read=0;
	public int area_blue_read=0;
	
	public boolean control_mode=false;	// direct or indirect control loop
	
	// trace buffer
	public float[][] trace;
	public int time;
	public int count;
	
	public float[] gauss;
	
	public float contactHeight=0;
	public float contactX=0;
	public float contactY=0;
	public float[][] sphere;
	public float[][] heighMap;
	public float[][] contactMap;

	
	// target properties
	public boolean target_pause=true;
	public int target_speed=200;
	public int target_type=1;
	
	
	
	//////////////////////////////////////////////////////////////
	
	public VideoCapture camera;
	
	public Mat webcam1=new Mat();
	public Mat webcam =new Mat();
	public Mat webcam_display =new Mat();
	public Mat webcam_display1=new Mat();
	public Mat webcam_miniature=new Mat();
	public Mat map=new Mat();
	
	private Size sz = new Size(640,480);
	private Size sz_miniature = new Size(160,120);
	
	public double[] histogram_X;
	public double[] histogram_Y;
	
	///////////////////////////////////////////////////////////////
	
	// name for save files
	public String presetName="preset";
	public String pathName="path";
	public String sourceName="source";
	
	public String[] listImages;
	public String[] listTactile;
	public String[] listFlow;
	public String[] listRail;
	public String[] listPath;
	public String[] listPreset;
	public String[] listArea;
	public String[] listSPath;
	public String[] listScript;
	public String[] listSource;
	
	public int selected_img=-1;
	public int selected_tactile=-1;
	public int selected_flow=-1;
	public int selected_rail=-1;
	public int selected_area=-1;
	
	
	///////////////////////////////////////////////////////////////
	public Main(){
		
		
		if (CAMERA_CONNECTED){
			camera=new VideoCapture(1);
			
			// get first frame
			camera.read(webcam1);
			Imgproc.resize( webcam1, webcam, sz );
			Imgproc.resize( webcam1, webcam_display, sz );
			Imgproc.resize( webcam1, map, sz );
		}
		
		time=0;
		count=0;
		trace=new float[LENGTH][2];
		
		///////////////////////////////////////////////////////////////////////////////////////////////

		// F2T interface
		inter=new Interface();

		// sequence of Ages
		script=new Script(this);

		// detection of available files
		listFiles();

		///////////////////////////////////////////////////////////////////////////////////////////////
		
		gauss=new float[9];
		gauss[0]=0.018f;//-4
		gauss[1]=0.1f;	//-3
		gauss[2]=0.37f;	//-2
		gauss[3]=0.78f;	//-1
		gauss[4]=1; 	// 0
		gauss[5]=0.78f;	// 1
		gauss[6]=0.37f;	// 2
		gauss[7]=0.1f;	// 3
		gauss[8]=0.018f;// 4
		
		
		sphere=new float[25][25];
		for (int i=-12;i<=12;i++){
			for (int j=-12;j<=12;j++){
				sphere[i+12][j+12]=(25-(float) Math.sqrt(25*25 - i*i - j*j))/6;
			}
		}
		heighMap=new float[25][25];
		contactMap=new float[25][25];
		
		histogram_X=new double[640];
		histogram_Y=new double[480];
		
		//////////////////////////////////////////////////////////////////////////////////////////
		// initialization of main display panels
		//touchFrame=new TouchFrame(this);
		display=new DisplayFrame(this);
		
		
		//////////////////////////////////////////////////////////////////////////////////////////
		while (true){
			
			////////////////////////////////////////////////////////////////////////
			// get position : get x, y, x_prev, y_prev and predict x_next and y_next
			getTouchedPosition();

			///////////////////////////////////////////////////////////
			// get user speed dx and dy
			getUserMovement();

			///////////////////////////////////////////////////////////
			// get friction values (fluid and solid) and send value
			friction_fluid_read=(float)currentAge.image.tactile_mat[Math.min(699,Math.max(0,350+(int)(x+dx)))][Math.min(699,Math.max(0,350-(int)(y+dy)))][0]/255;
			friction_solid_read=(float)currentAge.image.tactile_mat[Math.min(699,Math.max(0,350+(int)(x+dx)))][Math.min(699,Math.max(0,350-(int)(y+dy)))][2]/255;
			sendFriction();
			
			
			///////////////////////////////////////////////////////////
			// define edges
			contactX=0;
			contactY=0;
			boolean wall=false;

			// Height of touched point
			contactHeight=(float)currentAge.image.tactile_mat[350+(int)(x)][350-(int)(y)][1]/255;

			for (int i=-12;i<=12;i++){
				for (int j=-12;j<=12;j++){
					if (350+(int)(x+i)>=0 && 350+(int)(x+i)<700 && 350-(int)(y-j)>=0 && 350-(int)(y-j)<700 && i*i+j*j<=144){
						heighMap[i+12][j+12]= (float)currentAge.image.tactile_mat[350+(int)(x+i)][350-(int)(y-j)][1]/255;
						contactMap[i+12][j+12]= heighMap[i+12][j+12]-sphere[i+12][j+12] -contactHeight;
						if (contactMap[i+12][j+12]>0) wall=true;
					}
				}
			}
			float jx2=(jx/1.6f);
			float jy2=(jy/1.6f);
			
			// detect maximums to add repulsion effects
			float rep_X=0;
			float rep_Y=0;
			for (int i=-11;i<=11;i++){
				for (int j=-11;j<=11;j++){
					float val=contactMap[i+12][j+12];
					
					if ((i!=0 || j!=0) 
					    && val>contactMap[i+12][j+11] && val>contactMap[i+12][j+13] 
					    && val>contactMap[i+11][j+12] && val>contactMap[i+13][j+12]
					    && val>=contactMap[i+11][j+11] && val>contactMap[i+13][j+13]
					    && val>=contactMap[i+11][j+13] && val>contactMap[i+13][j+11]){

						float g_norm2=i*i+j*j;
						float g_norm =(float) Math.sqrt(g_norm2);
					
						if (g_norm<10){
							rep_X+=i / g_norm * val;
							rep_Y+=j / g_norm * val;
						}
					}
				}
			}

			boolean jump=false;
			
			// detect maximums to remove component
			for (int i=-11;i<=11;i++){
				for (int j=-11;j<=11;j++){
					float val=contactMap[i+12][j+12];
					
					if ((i!=0 || j!=0) 
					    && val>contactMap[i+12][j+11] && val>contactMap[i+12][j+13] 
					    && val>contactMap[i+11][j+12] && val>contactMap[i+13][j+12]
					    && val>contactMap[i+11][j+11] && val>contactMap[i+13][j+13]
					    && val>contactMap[i+11][j+13] && val>contactMap[i+13][j+11]){
						
						float g_norm2=i*i+j*j;
						
						// remove vector component
						float scalar=jx2*i + jy2*(-j);
						
						if (scalar>0){
							
							float valX=scalar *  i  / g_norm2;
							float valY=scalar *(-j) / g_norm2;
							
							float j_norm2= valX*valX + valY*valY;
							float j_norm=(float) Math.sqrt(j_norm2);

							float delta=Math.max(0, j_norm-310*heighMap[i+12][j+12]);

							if (delta>0){
								jump=true;
								jx2-=valX/2;
								jy2-=valY/2;
							}
							else{
								jx2-=valX;
								jy2-=valY;
							}
						}
					}
				}
			}
			
			if (!jump){
				jx2-=rep_X*100;
				jy2+=rep_Y*100;
			}
			
			
			if (wall){
				if (!control_mode){
					inter.sendMsg("m1");
					control_mode=true;
				}
				
				String msg="c";
				if ((int)jx2+500<10) msg+="00";
				else if ((int)jx2+500<100) msg+="0";
				msg+=(int)jx2+500;
				
				if ((int)jy2+500<10) msg+="00";
				else if ((int)jy2+500<100) msg+="0";
				msg+=(int)jy2+500;
				inter.sendMsg(msg);
			}
			else{
				if (control_mode){
					inter.sendMsg("m0");
					control_mode=false;
				}
			}
			previous_gx=(int) gx;
			previous_gy=(int) gy;


			
			if ((int)gx!=previous_gx || (int)gy!=previous_gy){
				String msg="g";
				if (gx<10) msg+="00";
				else if (gx<100) msg+="0";
				msg+=(int)gx;
				
				if (gy<10) msg+="00";
				else if (gy<100) msg+="0";
				msg+=(int)gy;
				
				inter.sendMsg(msg);
				previous_gx=(int)gx;
			}
			
			
			///////////////////////////////////////////////////////////
			// define flow
			flowX=500;
			flowY=500;
			if ( currentAge.targetSequence.size()==0 || currentAge.targetSequence.get(0).control==0 || target_pause ){
				flowX_read=currentAge.image.flow_mat[Math.min(699,Math.max(0,350+(int)(x+dx)))][Math.min(699,Math.max(0,350-(int)(y+dy)))][0];
				flowY_read=currentAge.image.flow_mat[Math.min(699,Math.max(0,350+(int)(x+dx)))][Math.min(699,Math.max(0,350-(int)(y+dy)))][1];

				flowX=flowX_read*4-12;
				flowY=flowY_read*4-12;
				
				if (flowX<0) flowX=0;
				if (flowX>999) flowX=999;
				if (flowY<0) flowY=0;
				if (flowY>999) flowY=999;
			}
			
			
			///////////////////////////////////////////////////////////
			// define rail
			if ( currentAge.targetSequence.size()==0 || currentAge.targetSequence.get(0).control==0 || target_pause ){
				
				railX_read=currentAge.image.rail_mat[Math.min(699,Math.max(0,350+(int)(x+dx)))][Math.min(699,Math.max(0,350-(int)(y+dy)))][0];
				railY_read=currentAge.image.rail_mat[Math.min(699,Math.max(0,350+(int)(x+dx)))][Math.min(699,Math.max(0,350-(int)(y+dy)))][1];
				float railX2=-(railY_read*4-12-500);
				float railY2=-(railX_read*4-12-500);
				float scalar=jx2*railX2 + jy2*(-railY2);
				float norm2=railX2*railX2+railY2*railY2;
				
				if (norm2>0){
					
					float offX=  scalar * railX2/norm2;
					float offY= -scalar * railY2/norm2;
					float norm=(float)Math.sqrt(norm2)/500;
					if (norm>1) norm=1;
					flowX-=offX* norm *1.6f;
					flowY-=offY* norm *1.6f;
					
					if (flowX<0) flowX=0;
					if (flowX>999) flowX=999;
					if (flowY<0) flowY=0;
					if (flowY>999) flowY=999;
				}
			}
			
			
			///////////////////////////////////////////////////////////
			// if there is a target
			if (currentAge.targetSequence.size()>0 && !target_pause){
				if (currentAge.targetSequence.get(0).control==1) inter.sendMsg("t0");
				else                          inter.sendMsg("t1");
				currentAge.targetSequence.get(0).compute(x, y);
				if (currentAge.targetSequence.get(0).reached){
					currentAge.targetSequence.remove(0);
					if (currentAge.targetSequence.size()==0) inter.sendMsg("t1");
				}
				else{
					flowX+=currentAge.targetSequence.get(0).offsetX;
					flowY+=currentAge.targetSequence.get(0).offsetY;
				}
			}
			
			///////////////////////////////////////////////////////////
			// send flow value
			if (flowX!=previous_flowX){
				String msg="o";
				if (flowX<10) msg+="00";
				else if (flowX<100) msg+="0";
				msg+=flowX;
				inter.sendMsg(msg);
				previous_flowX=flowX;
			}
			if (flowY!=previous_flowY){
				String msg="p";
				if (flowY<10) msg+="00";
				else if (flowY<100) msg+="0";
				msg+=flowY;
				inter.sendMsg(msg);
				previous_flowY=flowY;
			}

			
			
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
			script.detect(Math.min(699,Math.max(0,350+(int)(x+dx))),Math.min(699,Math.max(0,350-(int)(y+dy))));
			
			
			//////////////////////////////
			// compute sound sources positions
			script.computeSources(x,y);
			
			try {Thread.sleep(10);
			} catch (InterruptedException e) {e.printStackTrace();}
			
		}
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// file lister
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	public void listFiles(){

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
		
		if (display!=null) display.rescan();
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// save elements of current Age : preset, path and source list
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	public void savePreset(){
		
		if (currentAge.image.view==null 
		 && currentAge.image.tactile==null 
		 && currentAge.image.flow==null 
		 && currentAge.image.rail==null){
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
	// Load a script from interface
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	public void setScript(int id){
		System.out.println("Script "+id+" : "+listScript[id]);
		
		setPicture(null);
		setTactile(null);
		setFlow(null);
		setRail(null);
		setArea(null);
		
		setScript(listScript[id]);
	}

	public void setScript(String file){
		
		script.reinitialize();

		int age_index=0;
		System.out.println("Read script "+Main.FILES+Main.SCRIPT+file);
		
		String fileName = FILES+SCRIPT+file;
		String[] elements;
		
		try {
			InputStream ips=new FileInputStream(fileName); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line;
			
			// read first line
			line=br.readLine();
			
			while (line!=null){
				
				System.out.println(line);
				elements=line.split(" ");
				
				
				//////////////////////////////////////////
				// case image file
				if (elements.length>=2 && elements[0].equals("image")){
					if (elements[1].equals("none")) script.setPicture(age_index,null);
					else script.setPicture(age_index,elements[1]);
				}
				
				// case tactile file
				if (elements.length>=2 && elements[0].equals("tactile")){
					if (elements[1].equals("none")) script.setTactile(age_index,null);
					else script.setTactile(age_index,elements[1]);
				}	
		
				// case flow file
				if (elements.length>=2 && elements[0].equals("flow")){
					if (elements[1].equals("none")) script.setFlow(age_index,null);
					else script.setFlow(age_index,elements[1]);
				}
				
				// case rail file
				if (elements.length>=2 && elements[0].equals("rail")){
					if (elements[1].equals("none")) script.setRail(age_index,null);
					else script.setRail(age_index,elements[1]);
				}

				// case area descriptor file
				if (elements.length>=2 && elements[0].equals("area")){
					if (elements[1].equals("none")) script.setArea(age_index,null);
					else script.setArea(age_index,elements[1]);
				}
				

				//////////////////////////////////////////
				// case target
				if (elements.length>=5 && elements[0].equals("t")){
					script.addTarget(age_index,elements);
				}
	
				// case area-sound association
				if (elements.length>=5 && elements[0].equals("a")){
					script.addArea(age_index,elements);
				}
				
				// case sound source
				if (elements.length>=3 && elements[0].equals("s")){
					script.addSource(age_index,elements);
				}
				
				
				///////////////////////////////////////////
				// case load preset
				if (elements.length>=2 && elements[0].equals("preset")){
					script.setPreset(age_index,elements[1], Integer.parseInt(elements[2]));
				}
				
				// case add path
				if (elements.length>=2 && elements[0].equals("path")){
					script.setPath(age_index,elements[1]);
				}
				
				// case add path
				if (elements.length>=2 && elements[0].equals("source")){
					script.setSource(age_index,elements[1]);
				}

				
				//////////////////////////////////////////
				// case erase path
				if (elements.length>=1 && elements[0].equals("clearPath")){
					script.clearPath(age_index);
				}
				
				// case erase sources
				if (elements.length>=1 && elements[0].equals("clearSources")){
					script.clearSource(age_index);
				}
				
				// case default play
				if (elements.length>=1 && elements[0].equals("play")){
					script.setDefaultPlay(age_index, true);
				}
				if (elements.length>=1 && elements[0].equals("stop")){
					script.setDefaultPlay(age_index, false);
				}
				
				//////////////////////////////////////////
				// case age exit condition
				else if (elements[0].equals("newage")){
					for (int c=1;c<elements.length;c++){
						if (elements[c].equals("t")) script.setConditionTarget(age_index);
						else if (elements[c].equals("s")) script.setConditionSound(age_index);
						else script.addConditionArea(age_index,Integer.parseInt(elements[c]));
					}
					
					age_index++;
					script.addNewAge();
				}
				
				line=br.readLine();
			}
			br.close();
		}
		catch (Exception e) {System.out.println("no file found");}

		updateAge(script.getCurrentAge());
		target_pause=false;
		
		display.updateMiniatures(-1);
		
		System.out.println("++++ "+currentAge.image.area);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Load elements in current Age : preset, path and source list
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void setPreset(int id){
		System.out.println("Preset "+id+" : "+listPreset[id]);
		setPreset(listPreset[id], 1);
	}
	
	public void setPreset(String file, int erase){	
		String fileName = FILES+PRESET+file;
		String[] elements;

		String img_file=null;
		String tactile_file=null;
		String flow_file=null;
		String rail_file=null;
		String area_file=null;
		
		if (erase==1){
			currentAge.targetSequence.clear();
			currentAge.sourceList.clear();
		}
		
		try {
			InputStream ips=new FileInputStream(fileName); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line;
			line=br.readLine();
			
			while (line!=null){
				elements=line.split(" ");
				if (elements.length>0){
					if (elements[0].equals("image")) img_file=elements[1];
					else if (elements[0].equals("tactile")) tactile_file=elements[1];
					else if (elements[0].equals("flow")) flow_file=elements[1];
					else if (elements[0].equals("rail")) rail_file=elements[1];
					else if (elements[0].equals("area")) area_file=elements[1];
					else if (elements[0].equals("path")) setPath(elements[1]);
					else if (elements[0].equals("source")) setSource(elements[1]);
					else if (elements[0].equals("play")) target_pause=false;
					else if (elements[0].equals("stop")) target_pause=true;
					else System.out.println("ERROR : wrong keyword");
				}
				line=br.readLine();
			}
			br.close();
			
			if (img_file!=null     || erase==1) setPicture(img_file);
			if (tactile_file!=null || erase==1) setTactile(tactile_file);
			if (flow_file!=null    || erase==1) setFlow(flow_file);
			if (rail_file!=null    || erase==1) setRail(rail_file);
			if (area_file!=null    || erase==1) setArea(area_file);
		}
		catch (Exception e) {System.out.println("no file found");}
		
		//display.updateMiniatures();
	}
	
	//////////////////////////////////////////////////////////////////////////////
	public void setPath(int id){
		System.out.println("Path "+id+" : "+listPath[id]);
		setPath(listPath[id]);
	}
	
	public void setPath(String file){
		String fileName = FILES+PATH+file;
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
					currentAge.targetSequence.add(new Target(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]),Integer.parseInt(elements[3]), Integer.parseInt(elements[4]) ));
				}
				line=br.readLine();
			}
			br.close();
		}
		catch (Exception e) {System.out.println("no file found");}
	}
	
	//////////////////////////////////////////////////////////////////////////////
	public void setSource(int id){
		System.out.println("Sound source list "+id+" : "+listSource[id]);
		setSource(listSource[id]);
	}

	public void setSource(String file){
		String fileName = FILES+SOURCE+file;
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
					currentAge.sourceList.add(new SoundSource(elements));
				}
				line=br.readLine();
			}
			br.close();
		}
		catch (Exception e) {System.out.println("no file found");}
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Load image files
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	// picture
	public void setPicture(String img_file){
		currentAge.image.setPicture(img_file);
		
		display.updateMiniatures(0);

		selected_img=-1;
		if (img_file!=null){
			for (int i=0;i<listImages.length;i++){
				if (img_file.equals(listImages[i])) selected_img=i;
			}
		}
	}
	
	// tactile image
	public void setTactile(String tactile_file){
		currentAge.image.setTactile(tactile_file);

		display.updateMiniatures(1);
		
		selected_tactile=-1;
		if (tactile_file!=null){
			for (int i=0;i<listTactile.length;i++){
				if (tactile_file.equals(listTactile[i])) selected_tactile=i;
			}
		}
	}
	
	// flow file
	public void setFlow(String flow_file){
		currentAge.image.setFlow(flow_file);

		display.updateMiniatures(2);

		selected_flow=-1;
		if (flow_file!=null){
			for (int i=0;i<listFlow.length;i++){
				if (flow_file.equals(listFlow[i])) selected_flow=i;
			}
		}
	}
	
	// rail file
	public void setRail(String rail_file){
		currentAge.image.setRail(rail_file);

		display.updateMiniatures(3);
		
		selected_rail=-1;
		if (rail_file!=null){
			for (int i=0;i<listRail.length;i++){
				if (rail_file.equals(listRail[i])) selected_rail=i;
			}
		}
	}
	
	// area descriptor file
	public void setArea(String area_file){
		currentAge.image.setArea(area_file);
		
		display.updateMiniatures(4);
		
		selected_area=-1;
		if (area_file!=null){
			for (int i=0;i<listArea.length;i++){
				if (area_file.equals(listArea[i])) selected_area=i;
			}
		}
	}
	
	
	public void updateAge(Age age){
		currentAge=age;
		
		target_pause=!currentAge.default_play;
		
		selected_img=-1;
		if (currentAge.image.view!=null){
			for (int i=0;i<listImages.length;i++){
				if (currentAge.image.view.equals(listImages[i])) selected_img=i;
			}
		}
		
		selected_tactile=-1;
		if (currentAge.image.tactile!=null){
			for (int i=0;i<listTactile.length;i++){
				if (currentAge.image.tactile.equals(listTactile[i])) selected_tactile=i;
			}
		}

		selected_flow=-1;
		if (currentAge.image.flow!=null){
			for (int i=0;i<listFlow.length;i++){
				if (currentAge.image.flow.equals(listFlow[i])) selected_flow=i;
			}
		}

		selected_rail=-1;
		if (currentAge.image.rail!=null){
			for (int i=0;i<listRail.length;i++){
				if (currentAge.image.rail.equals(listRail[i])) selected_rail=i;
			}
		}

		selected_area=-1;
		if (currentAge.image.area!=null){
			for (int i=0;i<listArea.length;i++){
				if (currentAge.image.area.equals(listArea[i])) selected_area=i;
			}
		}
	}
	
	///////////////////////////////////////////////////////////
	
	public void getTouchedPosition(){
		
		if (CAMERA_CONNECTED){
			camera.read(webcam);

			//Imgproc.resize( webcam1, webcam, sz);
			Core.flip( webcam, webcam_display1, 0);
			Core.flip( webcam_display1, webcam_display, 1);
			
			Imgproc.resize( webcam_display, webcam_miniature, sz_miniature );

			
			// vertical
			for (int i=610;i<624;i+=2){
				for (int j=20;j<320;j++){
					double[] color=webcam.get(j, i);
					if (color[0]>100){
						double val=color[0]-Math.max(color[2],color[1]);
						if (val>20){
							color[0]=val;
							color[1]=val;
							color[2]=val;
							map.put(j, i, color);
							map.put(j, i+1, color);
							for (int g=-4;g<4;g++){
								histogram_Y[j+g]+=val*gauss[g+4];
							}
						}
					}
					else{
						color[0]=0;
						color[1]=0;
						color[2]=250;
						map.put(j, i, color);
						map.put(j, i+1, color);
					}
				}
			}
			
			// horizontal
			for (int i=120;i<520;i++){
				for (int j=440;j<460;j+=2){
					double[] color=webcam.get(j, i);
					if (color[0]>100){
						double val=color[0]-Math.max(color[2],color[1]);
						if (val>20){
							color[0]=val;
							color[1]=val;
							color[2]=val;
							map.put(j, i, color);
							map.put(j+1, i, color);
							for (int g=-4;g<4;g++){
								histogram_X[i+g]+=val*gauss[g+4];
							}
						}
					}
					else{
						color[0]=0;
						color[1]=0;
						color[2]=250;
						map.put(j, i, color);
						map.put(j+1, i, color);;
					}
				}
			}
		
		}
		else{
			try {Thread.sleep(100) ;
			}  catch (InterruptedException e) {}
		}
		
		double max_X=20;
		double max_Y=20;
		int imax=-1;
		int jmax=-1;
		
		for (int i=20;i<620;i++){
			if (histogram_X[i]>max_X){
				max_X=histogram_X[i];
				imax=i;
			}
			histogram_X[i]=0;
		}
		for (int j=20;j<460;j++){
			if (histogram_Y[j]>max_Y){
				max_Y=histogram_Y[j];
				jmax=j;
			}
			histogram_Y[j]=0;
		}
		if (imax==-1 || jmax==-1){
			x=160;
			y=120;
		}
		else{
			x_prev=x;
			y_prev=y;
			
			//System.out.println(imax+" , "+jmax);
			
			x=-(imax-270)*3;
			y=(jmax-157)*2.56f;
			
			if (x<-349) x=-349;
			if (x> 349) x= 349;
			if (y<-349) y=-349;
			if (y> 349) y= 349;
			
			x_next=x + (x-x_prev);
			y_next=y + (y-y_prev);
		}
		
		//System.out.println(x+" , "+y);
	}
	
	
	public void getUserMovement(){
		if (inter.ready){
			jx=inter.joystickX;
			jy=inter.joystickY;
			
			dx=jx/200;
			dy=jy/200;

			inter.ready=false;
		}
		
		while (350+(int)(x+dx)>700) x-=2; 
		while (350+(int)(x+dx)<  0) x+=2; 
		while (350-(int)(y+dy)>700) y+=2;
		while (350-(int)(y+dy)<  0) y-=2;
	}
	
	
	public void sendFriction(){
		friction_fluid=(int)(friction_fluid_read*490);
		if (friction_fluid!=previous_fluid){
			String msg="f";
			if (friction_fluid<10) msg+="00";
			else if (friction_fluid<100) msg+="0";
			msg+=(int)friction_fluid;
			inter.sendMsg(msg);
			
			previous_fluid=(int) friction_fluid;
		}
		
		friction_solid=(int)(friction_solid_read*490);;
		if (friction_solid!=previous_solid){
			String msg="s";
			if (friction_solid<10) msg+="00";
			else if (friction_solid<100) msg+="0";
			msg+=(int)friction_solid;
			inter.sendMsg(msg);
			
			previous_solid=(int) friction_solid;
		}/**/
	}
	
	
	public void updateTrace(){
		trace[time][0]=x;
		trace[time][1]=y;
		time++;
		if (time>=LENGTH) time=0;
	}
	
	
	///////////////////////////////////////////////////////////
	public static BufferedImage Mat2bufferedImage(Mat image) {
		MatOfByte bytemat = new MatOfByte();
		Imgcodecs.imencode(".jpg", image, bytemat);
		byte[] bytes = bytemat.toArray();
		InputStream in = new ByteArrayInputStream(bytes);
		BufferedImage img = null;
		try {
			img = ImageIO.read(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}
	
	
	
	
	
	public void close(){
		inter.close();
		script.close();
	}
	
	
	public static void main(String[] args){
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		new Main();
	}
	
}
