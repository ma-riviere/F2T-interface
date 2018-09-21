package script;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import main.Main;

public class Script {

	private Main main;
	
	private ArrayList<Age> sequence;
	
	private int sequence_index=-1;
	
	
	public Script(Main m){
		main=m;
		sequence=new ArrayList<Age>();
	}
	
	
	public void setScript(String file){
		
		sequence.clear();
		sequence_index=-1;
		
		System.out.println("Read script "+Main.FILES+Main.SCRIPT+file);
		
		int age_index=-1;
		
		String fileName = Main.FILES+Main.SCRIPT+file;
		String[] elements;
		
		boolean error=false;
		
		String descript=null;
		String image=null;
		String tactile=null;
		String flow=null;
		String rail=null;
		
		try {
			InputStream ips=new FileInputStream(fileName); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line;
			
			// read first line
			line=br.readLine();
			
			System.out.println(line);
			elements=line.split(" ");
			if (elements[0].equals("desc") && elements.length==2){
				
				// initialize first Age
				descript=elements[1];
				if (line!=null){
					age_index=0;
					sequence.add(new Age(main));
					sequence.get(age_index).setDescriptor(descript);
				}
				
				line=br.readLine();
			}
			else{
				System.out.println("Script does not start with an area descriptor image");
				error=true;
			}
			
			
			while (line!=null && !error){
				
				System.out.println(line);
				elements=line.split(" ");
				
				//////////////////////////////////////////
				// case descriptor file
				if (elements[0].equals("desc")){
					if (elements.length==2){
						descript=elements[1];
						sequence.get(age_index).setDescriptor(descript);
					}
					else{
						System.out.println("error in script : descriptor");
						error=true;
					}
				}
				
				//////////////////////////////////////////
				// case image file
				if (elements[0].equals("img")){
					if (elements.length==2){
						if (elements[1].equals("none")) image=null;
						else image=elements[1];
						sequence.get(age_index).setImage(image);
					}
					else{
						System.out.println("error in script : image");
						error=true;
					}
				}
				
				//////////////////////////////////////////
				// case tactile file
				if (elements[0].equals("tactile")){
					if (elements.length==2){
						if (elements[1].equals("none")) tactile=null;
						else tactile=elements[1];
						sequence.get(age_index).setTactile(tactile);
					}
					else{
						System.out.println("error in script : tactile");
						error=true;
					}
				}	
		
				//////////////////////////////////////////
				// case flow file
				if (elements[0].equals("flow")){
					if (elements.length==2){
						if (elements[1].equals("none")) flow=null;
						else flow=elements[1];
						sequence.get(age_index).setFlow(flow);
					}
					else{
						System.out.println("error in script : flow");
						error=true;
					}
				}
				
				//////////////////////////////////////////
				// case rail file
				if (elements[0].equals("rail")){
					if (elements.length==2){
						if (elements[1].equals("none")) rail=null;
						else rail=elements[1];
						sequence.get(age_index).setRail(rail);
					}
					else{
						System.out.println("error in script : rail");
						error=true;
					}
				}	
				
				//////////////////////////////////////////
				// case area
				else if (elements[0].equals("a")){
					if (elements.length==5){
						sequence.get(age_index).addArea(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]), Integer.parseInt(elements[3]), elements[4]);
					}
					else{
						System.out.println("error in script : area");
						error=true;
					}
				}
				
				//////////////////////////////////////////
				// case target
				else if (elements[0].equals("t")){
					if (elements.length==5){
						sequence.get(age_index).addTarget(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]), Integer.parseInt(elements[3]), Integer.parseInt(elements[4]));
					}
					else{
						System.out.println("error in script : target");
						error=true;
					}
				}
				
				//////////////////////////////////////////
				// case condition
				else if (elements[0].equals("newage")){
					for (int c=1;c<elements.length;c++){
						if (elements[c].equals("t")) sequence.get(age_index).setConditionTarget();
						else if (elements[c].equals("s")) sequence.get(age_index).setConditionSound();
						else sequence.get(age_index).addAreaCondition(Integer.parseInt(elements[c]));
					}
					
					age_index++;
					sequence.add(new Age(main));
					sequence.get(age_index).setDescriptor(descript);
					sequence.get(age_index).setImage(image);
					sequence.get(age_index).setTactile(tactile);
					sequence.get(age_index).setFlow(flow);
					sequence.get(age_index).setRail(rail);
				}
				
				
				line=br.readLine();
			}
			br.close();
		}
		catch (Exception e) {
			System.out.println("no file found");
		}
		
		if (error){
			sequence.clear();
			System.out.println("Script not loaded");
		}
		
	}
	
	
	public void detect(int i, int j){
		
		// initialize Age sequence
		if (sequence_index==-1 && sequence.size()>0){
			sequence_index=0;
			main.setArea(sequence.get(sequence_index).getDescriptor());
			main.setPicture(sequence.get(sequence_index).getImage());
			main.setTactile(sequence.get(sequence_index).getTactile());
			main.setFlow(sequence.get(sequence_index).getFlow());
			main.setRail(sequence.get(sequence_index).getRail());
			
			main.target.clear();
			main.target=sequence.get(sequence_index).getTargetList();
			main.target_pause=false;
		}
		
		if (sequence.size()>0 && sequence_index>=0 && sequence_index<sequence.size()){
			
			sequence.get(sequence_index).detect(i, j);
			
			if (sequence.get(sequence_index).isComplete(main.target.size())){
				System.out.println("change Age to "+(sequence_index+1));
				sequence.get(sequence_index).close();
				sequence_index++;
				main.setArea(sequence.get(sequence_index).getDescriptor());
				main.setPicture(sequence.get(sequence_index).getImage());
				main.setTactile(sequence.get(sequence_index).getTactile());
				main.setFlow(sequence.get(sequence_index).getFlow());
				main.setRail(sequence.get(sequence_index).getRail());
				main.target=sequence.get(sequence_index).getTargetList();
			}
		}
	}
	
	
	
	public void close(){
		for (int i=0;i<sequence.size();i++){
			sequence.get(i).close();
		}
	}
}