package script;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

import main.Main;



public class Areas {


	private Main main;
	
	private ArrayList<Integer> areas;
	private ArrayList<Integer> types;		// 0 stop, 1 continue , 2 forced
	private ArrayList<Integer> repeat;		// 0 no, 1 yes
	private ArrayList<String> soundFiles;
	
	private int previous_area=-1;
	private int current_area=-1;
	
	private int current_playing=-1;
	private int last_playing=-1;
	
	private boolean soundComplete=true;
	
	///////////////////////////////////////////////////////////////
	
	private File audioFile=null;
	private AudioInputStream audioStream=null;
	private Clip audioClip=null;
	
	
	public Areas(Main m){
		
		main=m;
		
		areas=new ArrayList<Integer>();
		types=new ArrayList<Integer>();
		repeat=new ArrayList<Integer>();
		soundFiles=new ArrayList<String>();
	}
	
	
	public void detect(int i, int j){
		
		if (audioClip!=null && !audioClip.isActive()){
			current_playing=-1;
			soundComplete=true;
		}
		
		previous_area=current_area;
		
		current_area=-1;
		
		if (main.image.area_mat[i][j][0]!=0 || main.image.area_mat[i][j][1]!=0 || main.image.area_mat[i][j][2]!=0){
			
			int red=main.image.area_mat[i][j][0];
			int green=main.image.area_mat[i][j][1];
			int blue=main.image.area_mat[i][j][2];

			int a=0;
			boolean found=false;
			
			while (!found && a<areas.size()){
				if (red>0 && red==areas.get(a)) found=true;
				else if (green>0 && green+25==areas.get(a)) found=true;
				else if (blue>0 && blue +50==areas.get(a)) found=true;
				else a++;
			}
			if (found) current_area=a;
		}
		
		
		
		if (current_area!=last_playing) last_playing=-1;
		
		//System.out.println(current_area);
		
		
		
		// case sound playing
		if (current_playing>=0){

			// sub case forced sound : no change
			if (types.get(current_playing)==2){

			}
			// sub case continue sound : stop if in another area
			else{
				if (types.get(current_playing)==1){
					if (current_area!=current_playing && current_area!=-1){
						stopSound();
						current_playing=-1;
						soundComplete=true;
					}
				}
				// sub case stop sound : stop when out of area
				else{	
					if (types.get(current_playing)==0){
						if (current_area!=current_playing){
							stopSound();
							current_playing=-1;
							soundComplete=true;
						}
					}
					else { // if invalid types
						stopSound();
						current_playing=-1;
						soundComplete=true;
					}
				}
			}
		}
		
		if (current_playing<0 && current_area!=-1){
			if (last_playing!=current_area || repeat.get(current_area)==1){
				if (last_playing!=current_area) soundComplete=false;
				current_playing=current_area;
				last_playing=current_area;
				playSound(soundFiles.get(current_area));
				
			}
		}
		
		
		//System.out.println(current_playing+" ; "+soundComplete);
	}
	
	public void addArea(int id, int type, int r, String file){
		areas.add(id);
		types.add(type);
		repeat.add(r);
		soundFiles.add(file);
	}
	
	public int getCurrentArea(){
		if (current_area==-1) return -1;
		else return areas.get(current_area);
	}
	
	private void playSound(String sound){
		
		try{
			//System.out.println(Main.FILES+Main.SOUND+sound);
			
			audioFile = new File(Main.FILES+Main.SOUND+sound);
			audioStream = AudioSystem.getAudioInputStream(audioFile);
			AudioFormat format = audioStream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			audioClip = (Clip) AudioSystem.getLine(info);
			
			audioClip.stop();
			
			audioClip.open(audioStream);
			audioClip.start();
			
		}
		catch (Exception e){
		    e.printStackTrace();
		}
		
	}
	
	private void stopSound(){
		
		try {
			audioClip.stop();
			audioClip.close();
			audioStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isSoundComplete(){
		return (soundComplete);
	}
	
	public boolean isEntering(){
		return previous_area!=current_area;
	}
	
	public void close(){
		try {
			if (audioClip!=null && audioClip.isOpen()) audioClip.close();
			if (audioStream!=null) audioStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
