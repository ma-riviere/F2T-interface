import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class Main {

	public static String FILES="../../F2T_images/";	// path to images
	//public static String FILES="./";
	public static String IMG="img/";								// sub paths to image types
	public static String PATH="path/";
	
	public static int MAX_UNDO=50;
	
	public String[] listImages;
	public String[] listPath;
	public String pathName;
	
	
	public BufferedImage picture_img;
	public String picture;
	public int selected_img=-1;
	
	public PictureFrame picturePanel;
	
	public ArrayList<Target> path;
	
	public ArrayList<ArrayList<Target>> path_buffer;
	public int buffer=-1;
	
	
	public Main(){
		
		listFiles();
		
		path=new ArrayList<Target>();
		
		path_buffer=new ArrayList<ArrayList<Target>>();
		path_buffer.add(new ArrayList<Target>());
		buffer=0;
		
		picturePanel=new PictureFrame(this);
	}
	
	
	
	public void listFiles(){
		
		
		File repertoire = new File(FILES+IMG);
		if (repertoire.exists()){
			listImages=repertoire.list();
		}
		else System.out.println("Image file directory does not exist");

		repertoire = new File(FILES+PATH);
		if (repertoire.exists()){
			listPath=repertoire.list();
		}
		else System.out.println("Area file directory does not exist");
		
		
		// get a name to save a flow image
		boolean found=false;
		int i=0;
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
	}
	
	
	public void setPicture(String img_file){
		
		picture=img_file;
		
		selected_img=-1;
		if (img_file!=null){
			for (int i=0;i<listImages.length;i++){
				if (img_file.equals(listImages[i])) selected_img=i;
			}
		}
		
		if (picture!=null){
			try {
				picture_img=ImageIO.read(new File(Main.FILES+Main.IMG+picture));
			} catch (IOException e) {e.printStackTrace();}
		}
		else picture_img=null;
	}
	
	
	public void pushBuffer(){
		
		while (path_buffer.size()>buffer+1) path_buffer.remove(path_buffer.size()-1);
		
		path_buffer.add(new ArrayList<Target>());
		buffer++;
		for (int i=0;i<path.size();i++){
			path_buffer.get(buffer).add(path.get(i).duplicate());
		}
		
		if (buffer>MAX_UNDO){
			path_buffer.remove(0);
			buffer--;
		}
	}
	
	public void undo(){
		
		if (buffer>0){
			
			path.clear();
			for (int i=0;i<path_buffer.get(buffer-1).size();i++){
				path.add(path_buffer.get(buffer-1).get(i).duplicate());
			}
			buffer--;
		}
	}
	
	public void redo(){
		
		if (buffer+1<path_buffer.size()){
			
			path.clear();
			for (int i=0;i<path_buffer.get(buffer+1).size();i++){
				path.add(path_buffer.get(buffer+1).get(i).duplicate());
			}
			buffer++;
		}
		
	}
	
	public void saveArea(){
		
		System.out.println("save path : "+pathName);
		
		String fileName = FILES+PATH+pathName;
		
		try {
			PrintWriter file  = new PrintWriter(new FileWriter(fileName));
			
			for (int t=0;t<path.size();t++){
				file.println("t "+path.get(t).x+" "+path.get(t).y+" "+path.get(t).speed+" "+path.get(t).control);
			}
			
			file.close();
			System.out.println("preset saved");
		}
		catch (Exception e) {e.printStackTrace();}
		
		listFiles();
	}
	
	
	public static void main(String[] args){
		new Main();
	}
	
}
