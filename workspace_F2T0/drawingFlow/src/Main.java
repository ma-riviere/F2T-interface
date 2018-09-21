import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class Main {

	public static String FILES="/home/simon/Bureau/F2T_images/";	// path to images
	//public static String FILES="./";
	public static String IMG="img/";								// sub paths to image types
	public static String FLOW="flow/";
	public static String RAIL="rail/";
	
	public static int MAX_UNDO=10;
	
	public int buffer=-1;
	
	public String[] listImages;
	public String[] listFlow;
	public String[] listRail;
	public String flowName;
	public String railName;
	
	
	public BufferedImage picture_img;
	public String picture;
	public int selected_img=-1;
	
	public PictureFrame picturePanel;
	public RenderFrame renderPanel;
	
	public float[][][] flow;
	
	private ArrayList<float[][][]> flow_buffer;
	
	public Main(){
		
		listFiles();
		
		flow = new float[700][700][2];
		
		flow_buffer=new ArrayList<float[][][]>();
		
		flow_buffer.add(new float[700][700][2]);
		for (int i=0;i<700;i++){
			for (int j=0;j<700;j++){
				flow_buffer.get(0)[i][j][0]=flow[i][j][0];
				flow_buffer.get(0)[i][j][1]=flow[i][j][1];
			}
		}
		buffer=0;
		
		
		picturePanel=new PictureFrame(this);
		renderPanel=new RenderFrame(this);
		
	}
	
	
	
	public void listFiles(){
		
		
		File repertoire = new File(FILES+IMG);
		if (repertoire.exists()){
			listImages=repertoire.list();
		}
		else System.out.println("Image file directory does not exist");

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
		
		// get a name to save a flow image
		boolean found=false;
		int i=0;
		flowName="flow0.png";
		while (!found){
			flowName="flow"+i+".png";
			found=true;
			int n=0;
			while (found && n<listFlow.length){
				if (flowName.equals(listFlow[n])) found=false;
				n++;
			}
			i++;
		}
		
		// get a name to save a rail image
		found=false;
		i=0;
		railName="rail0.png";
		while (!found){
			railName="rail"+i+".png";
			found=true;
			int n=0;
			while (found && n<listRail.length){
				if (railName.equals(listRail[n])) found=false;
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
		
		while (flow_buffer.size()>buffer+1) flow_buffer.remove(flow_buffer.size()-1);
		
		flow_buffer.add(new float[700][700][2]);
		buffer++;
		for (int i=0;i<700;i++){
			for (int j=0;j<700;j++){
				flow_buffer.get(buffer)[i][j][0]=flow[i][j][0];
				flow_buffer.get(buffer)[i][j][1]=flow[i][j][1];
			}
		}
		
		if (buffer>MAX_UNDO){
			flow_buffer.remove(0);
			buffer--;
		}
	}
	
	public void undo(){
		
		if (buffer>0){
			
			for (int i=0;i<700;i++){
				for (int j=0;j<700;j++){
					flow[i][j][0]=flow_buffer.get(buffer-1)[i][j][0];
					flow[i][j][1]=flow_buffer.get(buffer-1)[i][j][1];
				}
			}
			buffer--;
		}
	}
	
	public void redo(){
		
		if (buffer+1<flow_buffer.size()){
			
			for (int i=0;i<700;i++){
				for (int j=0;j<700;j++){
					flow[i][j][0]=flow_buffer.get(buffer+1)[i][j][0];
					flow[i][j][1]=flow_buffer.get(buffer+1)[i][j][1];
				}
			}
			buffer++;
		}
		
	}
	
	
	public void saveFlow(){
		
		System.out.println("save image : "+flowName);
		
		BufferedImage bufferedImage;
		bufferedImage = new BufferedImage( 705, 705, BufferedImage.TYPE_INT_RGB );
		Graphics g = bufferedImage.createGraphics();
		renderPanel.panel.paint( g );
		try {
		    ImageIO.write( bufferedImage, "png", new File(FILES+FLOW+flowName) ); }
		   
		    catch (Exception e) {
		     System.out.println("erreur enregistrement image..." );
		     e.printStackTrace();
		}
		
		listFiles();
	}
	
	public void saveRail(){
		
		System.out.println("save image : "+railName);
		
		BufferedImage bufferedImage;
		bufferedImage = new BufferedImage( 705, 705, BufferedImage.TYPE_INT_RGB );
		Graphics g = bufferedImage.createGraphics();
		renderPanel.panel.paint( g );
		try {
		    ImageIO.write( bufferedImage, "png", new File(FILES+RAIL+railName) ); }
		   
		    catch (Exception e) {
		     System.out.println("erreur enregistrement image..." );
		     e.printStackTrace();
		}
		
		listFiles();
	}
	
	public static void main(String[] args){
		new Main();
	}
	
}
