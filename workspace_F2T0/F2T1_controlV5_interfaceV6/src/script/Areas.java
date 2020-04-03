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

/**
 * This class contains a list of associations between areas and sounds. It also manages sound player.
 * @author simon gay
 */

public class Areas {
	
	public ArrayList<Integer> areas;
	public ArrayList<Integer> types;		// 0 stop, 1 continue , 2 forced
	public ArrayList<Integer> repeat;		// 0 no, 1 yes
	public ArrayList<String> soundFiles;

	
	private int current_association=-1;
	
	private int current_playing=-1;
	private int last_playing=-1;
	
	public boolean change=false;
	
	public MusicPlayer player;
	
	
	///////////////////////////////////////////////////////////////
	
	public Areas(){
		areas=new ArrayList<Integer>();
		types=new ArrayList<Integer>();
		repeat=new ArrayList<Integer>();
		soundFiles=new ArrayList<String>();
		
		player=new MusicPlayer(this);
		
		player.start();
	}
	
	
	public void detect(int red, int green, int blue){
		
		// detect current area
		current_association=-1;
		if (red!=0 || green!=0 || blue!=0){
			int a=0;
			boolean found=false;
			
			while (!found && a<areas.size()){
				if (red>0 && red==areas.get(a)) found=true;
				else if (green>0 && green+25==areas.get(a)) found=true;
				else if (blue>0 && blue +50==areas.get(a)) found=true;
				else a++;
			}
			if (found) current_association=a;
		}
		else{		// case area 0
			int a=0;
			boolean found=false;
			
			while (!found && a<areas.size()){
				if (areas.get(a)==0) found=true;
				else a++;
			}
			if (found) current_association=a;
		}
		
		
		
		// stop condition
		if (current_playing !=-1 && current_playing<types.size()){
			
			// stop when out
			if (types.get(current_playing)==0){
				if (current_association!=current_playing){	// moved out of the area
					current_playing=-1;
					last_playing=-1;
					change=true;
				}
			}
			
			// stop when other area
			else if (types.get(current_playing)==1){
				if (current_association!=-1 && current_association!=current_playing){	// enter another area
					current_playing=-1;
					last_playing=-1;
					change=true;
				}
			}
		}
		
		// if moved out of previous area
		if (current_association!=last_playing) last_playing=-1;
		
		// start new sound
		if (current_playing==-1 && current_association!=-1){
			
			if (repeat.get(current_association)==1 || current_association!=last_playing){
				current_playing=current_association;
				last_playing=current_association;
				change=true;
			}
		}
	}
	
	public void addArea(int id, int type, int r, String file){
		areas.add(id);
		types.add(type);
		repeat.add(r);
		soundFiles.add(file);
	}
	
	
	public void stopSound(){
		current_playing=-1;
	}
	
	public boolean isSoundComplete(){
		return (current_playing==-1);
	}
	
	
	public void close(){
		current_association=-1;
		current_playing=-1;
		last_playing=-1;

		areas.clear();
		types.clear();
		repeat.clear();
		soundFiles.clear();
		
		player.close();
	}
	
	public void clear(){
		current_association=-1;
		current_playing=-1;
		last_playing=-1;
		
		change=true;

		areas.clear();
		types.clear();
		repeat.clear();
		soundFiles.clear();
	}
	
	public void stop(){
		current_association=-1;
		current_playing=-1;
		last_playing=-1;
		
		change=true;
	}
	
	
	private class MusicPlayer extends Thread{
		
		private Areas area;
		
		public boolean running=true;
		
		private File audioFile=null;
		private AudioInputStream audioStream=null;
		private Clip audioClip=null;
		
		public MusicPlayer(Areas a){
			area=a;
		}
		
		public void run(){
			
			boolean started=false;
			
			while (running){
				started=false;
				
				/////////////////////////////////////////////////////////////////////////////
				if (area.change){

					// stop player
					if (audioClip!=null){
						try {
							audioClip.stop();
							audioClip.close();
							audioStream.close();
						} catch (IOException e) {e.printStackTrace();}
					}
					
					// start new sound
					
					if (area.current_playing!=-1){
						try{
							audioFile = new File(Main.FILES+Main.SOUND+area.soundFiles.get(area.current_playing));
							audioStream = AudioSystem.getAudioInputStream(audioFile);
							AudioFormat format = audioStream.getFormat();
							DataLine.Info info = new DataLine.Info(Clip.class, format);
							audioClip = (Clip) AudioSystem.getLine(info);
							
							audioClip.stop();
							
							audioClip.open(audioStream);
							audioClip.start();
							started=true;
							
						}
						catch (Exception e){
						    e.printStackTrace();
						}
					}
					
					area.change=false;
				}
				
				/////////////////////////////////////////////////////////////
				// detect end of sound
				if (audioClip!=null && !audioClip.isActive() && area.current_playing!=-1 && !started){
					try {
						audioClip.stop();
						audioClip.close();
						audioStream.close();
					} catch (IOException e) {e.printStackTrace();}
					
					area.current_playing=-1;
				}
				
				
				try {Thread.sleep(50);
				} catch (InterruptedException e) {e.printStackTrace();}
			}
		}
		
		public void close(){
			try {
				if (audioClip!=null && audioClip.isOpen()) audioClip.close();
				if (audioStream!=null) audioStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			running=false;
		}
	}
	
}
