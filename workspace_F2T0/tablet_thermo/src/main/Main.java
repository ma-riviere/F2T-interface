package main;
import java.util.ArrayList;

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
import org.opencv.imgcodecs.Imgcodecs;

import script.Script;



import display.*;

public class Main {

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static String FILES="/home/simon/Bureau/thermoformed_images/";	// path to images
	//public static String FILES="./";
	public static String IMG="img/";								// sub paths to image types
	public static String PRESET="preset/";
	public static String AREA="area/";
	public static String SCRIPT="script/";
	public static String SOUND="sound/";
	public static String SOURCE="source/";
	
	public static int LENGTH=500;									// length of the trace
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	// main display frames
	public CameraFrame cameraFrame;
	public SourceFrame sourceFrame;
	
	// four display frames

	private ViewPortFrame areaDisplay;
	
	// tactile image with all properties
	public Image image;
	
	// script with all command lines
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
	
	// trace buffer
	public float[][] trace;
	public int time;
	public int count;

	public boolean clicked=false;
	public int frame_counter=0;
	
		
	// list of sound sources
	public ArrayList<SoundSource> soundSources;
	
	
	//////////////////////////////////////////////////////////////

	public Mat map=new Mat();
	
	///////////////////////////////////////////////////////////////
	
	public String presetName;
	
	public String[] listImages;
	public String[] listPreset;
	public String[] listArea;
	public String[] listScript;
	public String[] listSource;
	
	public int selected_img=-1;
	
	
	///////////////////////////////////////////////////////////////
	public Main(){
		
		time=0;
		count=0;
		trace=new float[LENGTH][3];
		
		///////////////////////////////////////////////////////////////////////////////////////////////

		image=new Image();
		
		script=new Script(this);
		
		listFiles();
		
		///////////////////////////////////////////////////////////////////////////////////////////////
		soundSources=new ArrayList<SoundSource>();
		
		cameraFrame=new CameraFrame(this);
		sourceFrame=new SourceFrame(this);
		
		//////////////////////////////////////////////////////////////////////////////////////////
		while (true){

			
			////////////////////////////////////////////////////////////////////////
			// get position : get x, y, x_prev, y_prev and predict x_next and y_next
			getTouchedPosition();
			

			///////////////////////////////////////////////////////////
			// get user speed dx and dy
			getUserMovement();
			

			
			//////////////////////////////
			// update display panels
			if (areaDisplay   !=null) areaDisplay.repaint();
			
			
			cameraFrame.repaint();
			sourceFrame.repaint();

			
			//////////////////////////////
			// update user trace
			updateTrace();
			
			
			//////////////////////////////
			// story script
			script.detect(350+(int)(x+dx),270-(int)(y+dy));
			
			

			//////////////////////////////
			// compute sound sources positions
			for (int i=0;i<soundSources.size();i++){
				soundSources.get(i).compute(x, y);
			}
			
			try {Thread.sleep(20);
			} catch (InterruptedException e) {e.printStackTrace();}
			
		}
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////
	
	public void listFiles(){
		
		setPicture(null);
		
		File repertoire = new File(FILES+IMG);
		if (repertoire.exists()){
			listImages=repertoire.list();
		}
		else System.out.println("Image file directory does not exist");

		
		repertoire = new File(FILES+PRESET);
		if (repertoire.exists()){
			listPreset=repertoire.list();
		}
		else System.out.println("Preset file directory does not exist");

		
		repertoire = new File(FILES+SCRIPT);
		if (repertoire.exists()){
			listScript=repertoire.list();
		}
		else System.out.println("Script file directory does not exist");
		
		repertoire = new File(FILES+SOURCE);
		if (repertoire.exists()){
			listSource=repertoire.list();
		}
		else System.out.println("Source file directory does not exist");
		
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
	}
	
	
	public void savePreset(){
		
		if (image.view==null){
			System.out.println("No element selected, pre-set not saved");
		}
		else{
			
			System.out.println("save preset "+presetName);
			
			
			String fileName = FILES+PRESET+presetName;
			
			try {
				PrintWriter file  = new PrintWriter(new FileWriter(fileName));
				
				if (image.view!=null)    file.println("image "+image.view);
				
				file.close();
				System.out.println("preset saved");
			}
			catch (Exception e) {e.printStackTrace();}
			
			
			listFiles();
		}
	}
	
	
	public void setPreset(int id){
		System.out.println("Preset "+id+" : "+listPreset[id]);
		
		String fileName = Main.FILES+PRESET+listPreset[id];
		String[] elements;
		
		String img_file=null;
		String source_file=null;
		
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
					else if (elements[0].equals("source")) source_file=elements[1];
					else System.out.println("ERROR : wrong keyword");
				}
				line=br.readLine();
			}
			br.close();
			
			setPicture(img_file);
			setSource(source_file);
		}
		catch (Exception e) {
			System.out.println("no file found");
		}
	}
	
	
	public void setScript(int id){
		System.out.println("Script "+id+" : "+listScript[id]);
		
		script.setScript(listScript[id]);
		
		setPicture(null);
	}
	
	public void setSource(int id){
		System.out.println("Source "+id+" : "+listSource[id]);
		
		setSource(listSource[id]);
		
		setPicture(null);
	}
	
	public void setPicture(String img_file){
		image.setPicture(img_file);
		
		selected_img=-1;
		if (img_file!=null){
			for (int i=0;i<listImages.length;i++){
				if (img_file.equals(listImages[i])) selected_img=i;
			}
		}
	}
	
	
	public void setArea(String area_file){
		image.setArea(area_file);
		
		if (image.area   !=null && areaDisplay==null) areaDisplay =new ViewPortFrame(this, image.area_img);
		else if (image.area   ==null && areaDisplay!=null){
			areaDisplay.dispose();
			areaDisplay=null;
		}
	}
	
	public void setSource(String name){
		
		if (name.equals("clear")) soundSources.clear();
		else{
			
			String fileName = Main.FILES+SOURCE+name;
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
						soundSources.add(new SoundSource(elements ));
					}
					line=br.readLine();
				}
				
				br.close();
			}
			catch (Exception e) {
				System.out.println("no file found");
			}
		}
	}
	
	
	///////////////////////////////////////////////////////////
	
	public void getTouchedPosition(){
		
		x_prev2=x_prev;
		y_prev2=y_prev;
		
		x_prev=x;
		y_prev=y;	
		
		x=cameraFrame.getTouchX();
		y=cameraFrame.getTouchY();
		
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
		script.close();
	}
	
	
	public static void main(String[] args){
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		new Main();
	}
	
}
