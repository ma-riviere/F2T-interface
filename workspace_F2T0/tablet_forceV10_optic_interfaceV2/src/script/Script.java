package script;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import main.Main;

public class Script {

	private Main main;
	
	public ArrayList<Age> sequence;
	
	public int sequence_index=-1;
	
	/////////////////////////////////////////////////////////////////////////////
	// initialize a script
	public Script(Main m){
		main=m;
		sequence=new ArrayList<Age>();
		
		sequence.add(new Age(m));
		sequence_index=0;
		main.currentAge=getAge();
	}
	
	//////////////////////////////////////////////////////////////////////////////
	// read a new script
	public void setScript(String file){
		
		sequence.clear();
		sequence.add(new Age(main));
		sequence_index=0;
		main.currentAge=getAge();
		
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
			
			while (line!=null && !error){
				
				System.out.println(line);
				elements=line.split(" ");
				
				
				//////////////////////////////////////////
				// case image file
				if (elements.length>=2 && elements[0].equals("image")){
					if (elements[1].equals("none")) image=null;
					else image=elements[1];
					sequence.get(age_index).setImage(image);
				}
				
				// case tactile file
				if (elements.length>=2 && elements[0].equals("tactile")){
					if (elements[1].equals("none")) tactile=null;
					else tactile=elements[1];
					sequence.get(age_index).setTactile(tactile);
				}	
		
				// case flow file
				if (elements.length>=2 && elements[0].equals("flow")){
					if (elements[1].equals("none")) flow=null;
					else flow=elements[1];
					sequence.get(age_index).setFlow(flow);
				}
				
				// case rail file
				if (elements.length>=2 && elements[0].equals("rail")){
					if (elements[1].equals("none")) rail=null;
					else rail=elements[1];
					sequence.get(age_index).setRail(rail);
				}

				// case area descriptor file
				if (elements.length>=2 && elements[0].equals("area")){
					descript=elements[1];
					sequence.get(age_index).setDescriptor(descript);
				}
				

				//////////////////////////////////////////
				// case target
				if (elements.length>=5 && elements[0].equals("t")){
					sequence.get(age_index).addTarget(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]), Integer.parseInt(elements[3]), Integer.parseInt(elements[4]));
				}
	
				// case area-sound association
				if (elements.length>=5 && elements[0].equals("a")){
					sequence.get(age_index).addArea(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]), Integer.parseInt(elements[3]), elements[4]);
				}
				
				// case sound source
				if (elements.length>=3 && elements[0].equals("s")){
					sequence.get(age_index).addSource(elements);
				}
				
				
				///////////////////////////////////////////
				// case load preset
				if (elements.length>=2 && elements[0].equals("preset")){
					main.setPreset(elements[1], Integer.parseInt(elements[2]));
				}
				
				// case add path
				if (elements.length>=2 && elements[0].equals("path")){
					main.setPath(elements[1]);
				}
				
				// case add path
				if (elements.length>=2 && elements[0].equals("source")){
					main.setSource(elements[1]);
				}

				
				//////////////////////////////////////////
				// case erase path
				if (elements.length>=1 && elements[0].equals("clearPath")){
					sequence.get(age_index).targetSequence.clear();
				}
				
				// case erase sources
				if (elements.length>=1 && elements[0].equals("clearSources")){
					sequence.get(age_index).sourceList.clear();
				}
				
				
				//////////////////////////////////////////
				// case age exit condition
				else if (elements[0].equals("newage")){
					for (int c=1;c<elements.length;c++){
						if (elements[c].equals("t")) sequence.get(age_index).setConditionTarget();
						else if (elements[c].equals("s")) sequence.get(age_index).setConditionSound();
						else sequence.get(age_index).addAreaCondition(Integer.parseInt(elements[c]));
					}
					
					age_index++;
					sequence.add(new Age(main));
					
					//sequence.get(age_index).setDescriptor(descript);
					//sequence.get(age_index).setImage(image);
					//sequence.get(age_index).setTactile(tactile);
					//sequence.get(age_index).setFlow(flow);
					//sequence.get(age_index).setRail(rail);
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
		
		if (sequence.size()>0 && sequence_index>=0 && sequence_index<sequence.size()){
			
			sequence.get(sequence_index).detect(i, j);
			
			if (sequence.get(sequence_index).isComplete()){
				System.out.println("change Age to "+(sequence_index+1));
				sequence.get(sequence_index).close();
				sequence_index++;
				main.currentAge=getAge();
			}
		}
	}
	
	
	public void computeSources(float x, float y){
		sequence.get(sequence_index).computeSources(x, y);
	}
	
	public Age getAge(){
		return sequence.get(sequence_index);
	}
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void close(){
		for (int i=0;i<sequence.size();i++){
			sequence.get(i).close();
		}
	}
}