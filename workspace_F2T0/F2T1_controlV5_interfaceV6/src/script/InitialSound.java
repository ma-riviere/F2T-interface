package script;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

import main.Main;

/**
 * A sound manager that can play a sound when starting a period
 * @author simon gay
 */

public class InitialSound {
	
	public String soundFile;

	
	private boolean current_playing=false;
	private boolean soundComplete=true;
	
	
	///////////////////////////////////////////////////////////////
	
	private File audioFile=null;
	private AudioInputStream audioStream=null;
	private Clip audioClip=null;
	
	
	public InitialSound(){
		
	}
	
	
	public void playInitial(){
		
		if (!soundComplete){
			if (current_playing && audioClip!=null && !audioClip.isActive()){
				soundComplete=true;
			}
			if (!current_playing){
				current_playing=true;
				playSound(soundFile);
			}
		}
	}
	
	public void addInitialSound(String file){
		soundFile=file;
		soundComplete=false;
		current_playing=false;
	}
	
	public void removeSound(){
		soundFile=null;
		soundComplete=true;
		current_playing=false;
	}
	
	private void playSound(String sound){
		
		try{
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
	
	
	public boolean isSoundComplete(){
		return (soundComplete);
	}
	
	
	public void close(){
		try {
			if (audioClip!=null && audioClip.isOpen()) audioClip.close();
			if (audioStream!=null) audioStream.close();
			current_playing=false;
			soundComplete=true;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
