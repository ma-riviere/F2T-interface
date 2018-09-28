package script;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import main.Main;
import main.SoundSource;
import main.Target;

public class Script {

	private Main main;
	
	public ArrayList<Age> sequence;
	
	private int sequence_index=-1;
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	// initialize a script
	public Script(Main m){
		main=m;
		sequence=new ArrayList<Age>();
		
		sequence.add(new Age(m));
		sequence_index=0;
		
		sequence.get(sequence_index).default_play=false;
		
		//main.currentAge=sequence.get(sequence_index);
		main.updateAge(sequence.get(sequence_index));
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public void reinitialize(){
		for (int a=0;a<sequence.size();a++) sequence.get(a).close();
		sequence.clear();
		sequence.add(new Age(main));
		sequence_index=0;
		main.currentAge=sequence.get(sequence_index);
	}
	
	public void addNewAge(){
		sequence.add(new Age(main));
	}
	
	public Age getCurrentAge(){
		return sequence.get(sequence_index);
	}
	
	public int getCurrentIndex(){
		return sequence_index;
	}
	
	public int size(){
		return sequence.size();
	}
	
	
	public void setPicture(int i, String img_file){
		sequence.get(i).image.setPicture(img_file);
	}
	public void setTactile(int i, String img_file){
		sequence.get(i).image.setTactile(img_file);
	}
	public void setFlow(int i, String img_file){
		sequence.get(i).image.setFlow(img_file);
	}
	public void setRail(int i, String img_file){
		sequence.get(i).image.setRail(img_file);
	}
	public void setArea(int i, String img_file){
		sequence.get(i).image.setArea(img_file);
	}
	
	public void addTarget(int i, String[] elements){
		sequence.get(i).addTarget(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]), Integer.parseInt(elements[3]), Integer.parseInt(elements[4]));
	}
	public void addArea(int i, String[] elements){
		sequence.get(i).addArea(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]), Integer.parseInt(elements[3]), elements[4]);
	}
	public void addSource(int i, String[] elements){
		sequence.get(i).addSource(elements);
	}
	
	public void setPreset(int i, String file, int erase){	
		String fileName = Main.FILES+Main.PRESET+file;
		String[] elements;

		String img_file=null;
		String tactile_file=null;
		String flow_file=null;
		String rail_file=null;
		String area_file=null;
		
		if (erase==1){
			sequence.get(i).targetSequence.clear();
			sequence.get(i).sourceList.clear();
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
					else if (elements[0].equals("path")) setPath(i, elements[1]);
					else if (elements[0].equals("source")) setSource(i, elements[1]);
					else System.out.println("ERROR : wrong keyword");
				}
				line=br.readLine();
			}
			br.close();
			
			if (img_file!=null     || erase==1) sequence.get(i).image.setPicture(img_file);
			if (tactile_file!=null || erase==1) sequence.get(i).image.setTactile(tactile_file);
			if (flow_file!=null    || erase==1) sequence.get(i).image.setFlow(flow_file);
			if (rail_file!=null    || erase==1) sequence.get(i).image.setRail(rail_file);
			if (area_file!=null    || erase==1) sequence.get(i).image.setArea(area_file);
		}
		catch (Exception e) {System.out.println("no file found");}
	}
	
	public void setPath(int i,String file){
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
					sequence.get(i).targetSequence.add(new Target(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]),Integer.parseInt(elements[3]), Integer.parseInt(elements[4]) ));
				}
				line=br.readLine();
			}
			br.close();
		}
		catch (Exception e) {System.out.println("no file found");}
	}
	
	public void setSource(int i, String file){
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
					sequence.get(i).sourceList.add(new SoundSource(elements));
				}
				line=br.readLine();
			}
			br.close();
		}
		catch (Exception e) {System.out.println("no file found");}
	}
	
	public void clearPath(int i){
		sequence.get(i).targetSequence.clear();
	}
	public void clearSource(int i){
		sequence.get(i).sourceList.clear();
	}
	
	public void setConditionSound(int i){
		sequence.get(i).setConditionSound();
	}
	public void setConditionTarget(int i){
		sequence.get(i).setConditionTarget();
	}
	public void addConditionArea(int i, int area){
		sequence.get(i).addConditionArea(area);
	}
	
	public void setDefaultPlay(int i, boolean play){
		sequence.get(i).setDefaultPlay(play);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	public void detect(int i, int j){
		
		if (sequence.size()>0 && sequence_index>=0 && sequence_index<sequence.size()){
			
			sequence.get(sequence_index).detect(i, j);
			
			if (sequence.get(sequence_index).isComplete()){
				System.out.println("change Age to "+(sequence_index+1));
				sequence.get(sequence_index).close();
				sequence_index++;
				//main.currentAge=sequence.get(sequence_index);
				main.updateAge(sequence.get(sequence_index));
			}
		}
	}
	
	
	public void computeSources(float x, float y){
		if (sequence.size()>sequence_index) sequence.get(sequence_index).computeSources(x, y);
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void close(){
		for (int i=0;i<sequence.size();i++){
			sequence.get(i).close();
		}
	}
}
