import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class Main {

	private static String OS = System.getProperty("os.name").toLowerCase();
	public static String FILES="../../F2T_imagesV2/";	// path to images
	public static String IMG="img/";				// sub paths to image types
	public static String FLOW="flow/";
	public static String RAIL="rail/";
	public static String CURVES="curves/";
	
	public float[][][] flow;
	public int selectedLine=0;
	public Line[] line;

	public static int MAX_UNDO=40;
	
	private ArrayList<float[][][]> flow_buffer;
	public int bufferFlowId=-1;
	
	private ArrayList<Line[]> line_buffer;
	public int bufferLineId=-1;
	
	public String[] listImages;
	public String[] listFlow;
	public String[] listRail;
	public String[] listCurves;
	public String flowName;
	public String railName;
	public String curveName;
	
	
	public BufferedImage picture_img;
	public String picture;
	public int selected_img=-1;
	
	public PictureFrame picturePanel;
	public RenderFrame renderPanel;
	

	
	public int length=100;
	public float width1=50;
	public int width2=50;
	public float angle=0;
	public float dispersion=0;
	
	
	public Main(){
		
		listFiles();
		
		flow = new float[700][700][3];
		
		flow_buffer=new ArrayList<float[][][]>();
		flow_buffer.add(new float[700][700][3]);
		for (int i=0;i<700;i++){
			for (int j=0;j<700;j++){
				flow_buffer.get(0)[i][j][0]=flow[i][j][0];
				flow_buffer.get(0)[i][j][1]=flow[i][j][1];
			}
		}
		bufferFlowId=0;
		
		line=new Line[10];
		for (int l=0;l<10;l++) line[l]=new Line();
		
		line_buffer=new ArrayList<Line[]>();
		line_buffer.add(new Line[10]);
		for (int l=0;l<10;l++){
			line_buffer.get(0)[l]=line[l].duplicate();
		}
		bufferLineId=0;
		
		renderPanel=new RenderFrame(this);
		picturePanel=new PictureFrame(this);
		
	}
	
	
	public void setParameters(int speed, float w1, int w2){
		length=speed;
		width1=w1;
		width2=w2;
	}
	
	public void update(){
		
		for (int l=0;l<10;l++){
			line[l].update();
		}
		
		for (int i=0;i<700;i++){
			for (int j=0;j<700;j++){
				flow[i][j][0]=0;
				flow[i][j][1]=0;
				flow[i][j][2]=0;
			}
		}
		
		float d=0;
		float d2=0;
		float dist2=0;
		float dist=0;
		
		float x=0;
		float y=0;
		float speed=0;
		float angle=0;

		
		float vx=0;
		float vy=0;
		float vx2=0;
		float vy2=0;
		
		float vxt=0;
		float vyt=0;
		float scal=0;
		
		for (int l=0;l<10;l++){
			for (int i=0;i<line[l].sizePoint();i++){
				
				x=line[l].pointList.get(i).x;
				y=line[l].pointList.get(i).y;
				speed=line[l].pointList.get(i).speed;
				
				d=line[l].pointList.get(i).range2;
				d2=d*d;

				for (int i1=(int) -d;i1<d;i1++){
					for (int j1=(int) -d;j1<d;j1++){
						
						if (x+i1>=0 && x+i1<700 && y+j1>=0 && y+j1<700){
							
							dist2=i1*i1+j1*j1;
							
							if (dist2<d2){
								dist=(float)Math.sqrt((float)dist2);
								
								vx=line[l].pointList.get(i).vx;
								vy=line[l].pointList.get(i).vy;
								
								vxt=-vy;
								vyt=vx;
								
								scal=(float)i1*vxt +(float)j1*vyt ;
								
								
								angle=(float) Math.toRadians( line[l].pointList.get(i).dispersion*scal/d + line[l].pointList.get(i).angle );
								
								vx2=(float)(Math.cos(angle)*vx - Math.sin(angle)*vy);
								vy2=(float)(Math.sin(angle)*vx + Math.cos(angle)*vy);
								
								
								if (dist<line[l].pointList.get(i).range2*((float)line[l].pointList.get(i).range1/100)) speed=line[l].pointList.get(i).speed;
								else{
									speed= ((float)line[l].pointList.get(i).speed/((line[l].pointList.get(i).range2*(float)line[l].pointList.get(i).range1/100)-(float)line[l].pointList.get(i).range2)) * (dist-(float)line[l].pointList.get(i).range2);
								}
								
								if (dist<=1){
									flow[(int)(x+i1)][(int)(y+j1)][0]+= vx2*speed;
									flow[(int)(x+i1)][(int)(y+j1)][1]+= vy2*speed;
									flow[(int)(x+i1)][(int)(y+j1)][2]+= 1;
								}
								else{
									flow[(int)(x+i1)][(int)(y+j1)][0]+= vx2*speed * ((-1/(float)line[l].pointList.get(i).range2)*dist+1);
									flow[(int)(x+i1)][(int)(y+j1)][1]+= vy2*speed * ((-1/(float)line[l].pointList.get(i).range2)*dist+1);
									flow[(int)(x+i1)][(int)(y+j1)][2]+= ((-1/(float)line[l].pointList.get(i).range2)*dist+1);
								}
							}
						}
					}
				}
			}
		}
		
		for (int i=0;i<700;i++){
			for (int j=0;j<700;j++){
				if (flow[i][j][2]>0){
					flow[i][j][0]=flow[i][j][0]/flow[i][j][2]/255;
					flow[i][j][1]=flow[i][j][1]/flow[i][j][2]/255;
				}
			}
		}
		picturePanel.repaint();
		renderPanel.repaint();
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
		
		repertoire = new File(FILES+CURVES);
		if (repertoire.exists()){
			listCurves=repertoire.list();
		}
		else System.out.println("Curve file directory does not exist");
		
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
		
		// get a name to save a curve file
		found=false;
		i=0;
		curveName="curve0.png";
		while (!found){
			curveName="curve"+i+".txt";
			found=true;
			int n=0;
			while (found && n<listCurves.length){
				if (curveName.equals(listCurves[n])) found=false;
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
	
	
	/////////////////////////////////////////////////////////////////////////////////
	// buffer system
	/////////////////////////////////////////////////////////////////////////////////
	public void pushBufferFlow(){
		
		while (flow_buffer.size()>bufferFlowId+1) flow_buffer.remove(flow_buffer.size()-1);
		
		flow_buffer.add(new float[700][700][2]);
		bufferFlowId++;
		for (int i=0;i<700;i++){
			for (int j=0;j<700;j++){
				flow_buffer.get(bufferFlowId)[i][j][0]=flow[i][j][0];
				flow_buffer.get(bufferFlowId)[i][j][1]=flow[i][j][1];
			}
		}
		
		if (bufferFlowId>MAX_UNDO){
			flow_buffer.remove(0);
			bufferFlowId--;
		}
	}
	
	public void pushBufferLine(){
		while (line_buffer.size()>bufferLineId+1) line_buffer.remove(line_buffer.size()-1);
		
		line_buffer.add(new Line[10]);
		bufferLineId++;
		for (int l=0;l<10;l++){
			line_buffer.get(bufferLineId)[l]=line[l].duplicate();
		}
		
		if (bufferLineId>MAX_UNDO){
			line_buffer.remove(0);
			bufferLineId--;
		}
	}
	
	public void undoFlow(){
		
		if (bufferFlowId>0){
			
			for (int i=0;i<700;i++){
				for (int j=0;j<700;j++){
					flow[i][j][0]=flow_buffer.get(bufferFlowId-1)[i][j][0];
					flow[i][j][1]=flow_buffer.get(bufferFlowId-1)[i][j][1];
				}
			}
			bufferFlowId--;
		}
	}
	
	public void undoLine(){
		System.out.println(" test undo : "+bufferLineId+" ; "+line_buffer.size());
		if (bufferLineId>0){
			
			for (int l=0;l<10;l++){
				line[l]=line_buffer.get(bufferLineId-1)[l].duplicate();
			}
			bufferLineId--;

			update();
		}
	}
	
	
	public void redoFlow(){
		
		if (bufferFlowId+1<flow_buffer.size()){
			
			for (int i=0;i<700;i++){
				for (int j=0;j<700;j++){
					flow[i][j][0]=flow_buffer.get(bufferFlowId+1)[i][j][0];
					flow[i][j][1]=flow_buffer.get(bufferFlowId+1)[i][j][1];
				}
			}
			bufferFlowId++;
		}
	}
	
	public void redoLine(){
		
		if (bufferLineId+1<line_buffer.size()){
			
			for (int l=0;l<10;l++){
				line[l]=line_buffer.get(bufferLineId+1)[l].duplicate();
			}
			bufferLineId++;
			
			update();
		}
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////
	// image saving
	/////////////////////////////////////////////////////////////////////////////////
	public void saveFlow(String name){
		
		System.out.println("save image : "+name);
		
		BufferedImage bufferedImage;
		bufferedImage = new BufferedImage( 705, 705, BufferedImage.TYPE_INT_RGB );
		Graphics g = bufferedImage.createGraphics();
		renderPanel.panel.paintComponent( g );
		try {
		    ImageIO.write( bufferedImage, "png", new File(FILES+FLOW+name) ); }
		   
		    catch (Exception e) {
		     System.out.println("erreur enregistrement image..." );
		     e.printStackTrace();
		}
		
		listFiles();
	}
	
	public void saveRail(String name){
		
		System.out.println("save image : "+name);
		
		BufferedImage bufferedImage;
		bufferedImage = new BufferedImage( 705, 705, BufferedImage.TYPE_INT_RGB );
		Graphics g = bufferedImage.createGraphics();
		renderPanel.panel.paintComponent( g );
		try {
		    ImageIO.write( bufferedImage, "png", new File(FILES+RAIL+name) ); }
		   
		    catch (Exception e) {
		     System.out.println("erreur enregistrement image..." );
		     e.printStackTrace();
		}
		
		listFiles();
	}
	
	public void saveCurve(String name){
		
		System.out.println("save curve : "+name);

		try {
			PrintWriter file  = new PrintWriter(new FileWriter(FILES+CURVES+name));
			for (int l=0;l<10;l++){
				for (int t=0;t<line[l].targetList.size();t++){
					file.println(l+" "+line[l].targetList.get(t).x+" "+line[l].targetList.get(t).y+" "+
									   line[l].targetList.get(t).range1+" "+line[l].targetList.get(t).range2+" "+line[l].targetList.get(t).speed+" "+
									   line[l].targetList.get(t).p1x+" "+line[l].targetList.get(t).p1y+" "+
									   line[l].targetList.get(t).angle+" "+line[l].targetList.get(t).dispersion);
				}
			}
			file.close();
			System.out.println("path saved");
		}
		catch (Exception e) {e.printStackTrace();}
		
		listFiles();
		
		picturePanel.list_curves.removeAllItems();
		for (int i=0;i<listCurves.length;i++) picturePanel.list_curves.addItem(listCurves[i]);
	}
	
	
	public void loadCurve(String file){
		System.out.println("Load path file "+file);
		
		String fileName = FILES+CURVES+file;
		String[] elements;
		
		try {
			InputStream ips=new FileInputStream(fileName); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String l;
			l=br.readLine();
			
			while (l!=null){
				
				System.out.println("   "+l);
				
				elements=l.split(" ");
				if (elements.length>=8){
						line[Integer.parseInt(elements[0])].targetList.add(
								new Target(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]),
										   Integer.parseInt(elements[3]), Integer.parseInt(elements[4]),Integer.parseInt(elements[5]),
										   Integer.parseInt(elements[6]), Integer.parseInt(elements[7]) ) );
				}
				else if (elements.length>=10){
					line[Integer.parseInt(elements[0])].targetList.add(
							new Target(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]),
									   Integer.parseInt(elements[3]), Integer.parseInt(elements[4]),Integer.parseInt(elements[5]),
									   Integer.parseInt(elements[6]), Integer.parseInt(elements[7]),
									   Float.parseFloat(elements[8]), Float.parseFloat(elements[9]) ) );
				}
				l=br.readLine();
			}
			br.close();
		}
		catch (Exception e) {System.out.println("   /!\\ Path file not found or containing errors");}
		pushBufferLine();
		update();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args){
		
		// detect OS
		if (OS.indexOf("win") >= 0){
			FILES=".\\";							// path to images
			IMG="img\\";							// sub paths to image types
			FLOW="flow\\";
			RAIL="rail\\";
			CURVES="curves\\";
		}
		
		new Main();
	}
	
}
