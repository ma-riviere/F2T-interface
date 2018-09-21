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
	public static String AREA="area/";
	
	public static int MAX_UNDO=10;
	
	public int buffer=-1;
	
	public String[] listImages;
	public String[] listArea;
	public String areaName;
	
	
	public BufferedImage picture_img;
	public String picture;
	public int selected_img=-1;
	
	public PictureFrame picturePanel;
	public RenderFrame renderPanel;
	
	public short[][][] area;
	
	private ArrayList<short[][][]> area_buffer;
	
	public Main(){
		
		listFiles();
		
		area = new short[700][700][3];
		
		area_buffer=new ArrayList<short[][][]>();
		
		area_buffer.add(new short[700][700][3]);
		for (int i=0;i<700;i++){
			for (int j=0;j<700;j++){
				area_buffer.get(0)[i][j][0]=area[i][j][0];
				area_buffer.get(0)[i][j][1]=area[i][j][1];
				area_buffer.get(0)[i][j][1]=area[i][j][2];
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

		repertoire = new File(FILES+AREA);
		if (repertoire.exists()){
			listArea=repertoire.list();
		}
		else System.out.println("Area file directory does not exist");
		
		
		// get a name to save a flow image
		boolean found=false;
		int i=0;
		areaName="area0.png";
		while (!found){
			areaName="area"+i+".png";
			found=true;
			int n=0;
			while (found && n<listArea.length){
				if (areaName.equals(listArea[n])) found=false;
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
		
		while (area_buffer.size()>buffer+1) area_buffer.remove(area_buffer.size()-1);
		
		area_buffer.add(new short[700][700][3]);
		buffer++;
		for (int i=0;i<700;i++){
			for (int j=0;j<700;j++){
				area_buffer.get(buffer)[i][j][0]=area[i][j][0];
				area_buffer.get(buffer)[i][j][1]=area[i][j][1];
				area_buffer.get(buffer)[i][j][2]=area[i][j][2];
			}
		}
		
		if (buffer>MAX_UNDO){
			area_buffer.remove(0);
			buffer--;
		}
	}
	
	public void undo(){
		
		if (buffer>0){
			
			for (int i=0;i<700;i++){
				for (int j=0;j<700;j++){
					area[i][j][0]=area_buffer.get(buffer-1)[i][j][0];
					area[i][j][1]=area_buffer.get(buffer-1)[i][j][1];
					area[i][j][2]=area_buffer.get(buffer-1)[i][j][2];
				}
			}
			buffer--;
		}
	}
	
	public void redo(){
		
		if (buffer+1<area_buffer.size()){
			
			for (int i=0;i<700;i++){
				for (int j=0;j<700;j++){
					area[i][j][0]=area_buffer.get(buffer+1)[i][j][0];
					area[i][j][1]=area_buffer.get(buffer+1)[i][j][1];
				}
			}
			buffer++;
		}
		
	}
	
	
	public void saveArea(){
		
		System.out.println("save image : "+areaName);
		
		BufferedImage bufferedImage;
		bufferedImage = new BufferedImage( 705, 705, BufferedImage.TYPE_INT_RGB );
		Graphics g = bufferedImage.createGraphics();
		renderPanel.panel.paint( g );
		try {
		    ImageIO.write( bufferedImage, "png", new File(FILES+AREA+areaName) ); }
		   
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
