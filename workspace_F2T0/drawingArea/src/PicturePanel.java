
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;


/**
 * Generic class of panel that can be exported as pdf file and jpeg image
 * @author simon
 */
public class PicturePanel extends JPanel implements MouseListener, MouseMotionListener{

	private static final long serialVersionUID = 1L;
	
	protected Main main;
	
	public boolean click=false;
	
	private boolean undo_pressed=false;
	private boolean redo_pressed=false;
	private boolean save_pressed=false;
	
	private boolean write=false;
	
	public short color=0;
	public short value=0;
	public short red=0;
	public short green=0;
	public short blue=0;
	public short areaID=0;
	public ArrayList<Integer> currentAreaID;
	
	public boolean slider_capture=false;
	public int width1=50;
	
	public int ex=0;
	public int ey=0;
	
	
	public PicturePanel(Main m){
		main=m;
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		currentAreaID=new ArrayList<Integer>();
	}

	
	public void paintComponent(Graphics g){
		g.setColor(Color.white);
		g.fillRect(0, 0, 1600, 800);
		
		// display list of image file
		if (main.selected_img==-1) g.setColor(Color.red);
	    else g.setColor(Color.black);
	    g.fillOval(10, 10, 10, 10);
	    g.drawString("No image", 25, 20);
	    for (int i=0;i<main.listImages.length;i++){
	      	if (main.selected_img==i) g.setColor(Color.red);
	        else g.setColor(Color.black);
	        g.fillOval(10, 30+i*20, 10, 10);
	        g.drawString(main.listImages[i], 25, 40+20*i);
	    }
		
	    
	    // draw image
		if (main.picture!=null){
			g.drawImage(main.picture_img, 200, 0, this);
		}
		else{
			g.setColor(Color.black);
			g.drawRect(200, 0, 700, 700);
		}
		
		
		// display areas
		for (int i=0;i<700;i+=2){
			for (int j=0;j<700;j+=2){
				g.setColor(new Color(main.area[i][j][0],main.area[i][j][1],main.area[i][j][2]));
				g.drawLine(200+i, j, 200+i, j);
			}
		}
		
		// display color selector
		
		g.setColor(Color.red);
		g.fillRect(1000, 260, 20, 20);
		g.setColor(Color.green);
		g.fillRect(1030, 260, 20, 20);
		g.setColor(Color.blue);
		g.fillRect(1060, 260, 20, 20);
		
		g.setColor(Color.black);
		if (color==0) g.drawRect(997, 257, 25, 25);
		if (color==1) g.drawRect(1027, 257, 25, 25);
		if (color==2) g.drawRect(1057, 257, 25, 25);
		
		g.setColor(Color.black);
		g.fillRect(1000, 300, 140, 20);
		if (value==0){
			g.setColor(Color.black);
			g.drawRect(997, 297, 145, 25);
		}
		
		for (int c=0;c<25;c++){
			if (color==0) g.setColor(new Color((c+1)*10,0,0));
			if (color==1) g.setColor(new Color(0,(c+1)*10,0));
			if (color==2) g.setColor(new Color(0,0,(c+1)*10));
			g.fillRect(1000+30*(c%5), 330+30*(int)(c/5), 20, 20);
			
			
			if (c+1==value){
				g.setColor(Color.black);
				g.drawRect(997+30*(c%5), 327+30*(int)(c/5), 25, 25);
			}
		}
		
		g.setColor(Color.black);
		g.drawString("Selected area : "+areaID, 1000, 490);
		
		// display tool cursor
		g.setColor(Color.red);
		if (ex>-50 && ex<750 && ey<700){
			g.drawOval(ex-width1+200,ey-width1,width1*2,width1*2);
		}
		
		g.setColor(Color.black);
		g.drawRect(920, 10, 400, 220);
		
		g.setColor(Color.black);
		if (currentAreaID.size()>0){
			String msg="Current areas : ";
			for (int i=0;i<currentAreaID.size();i++) msg+=currentAreaID.get(i)+" , ";
			g.drawString(msg, 930, 20);
		}
		
		if (value==0) g.setColor(Color.black);
		else{
			if (color==0) g.setColor(new Color(red,0,0));
			if (color==1) g.setColor(new Color(0,green,0));
			if (color==2) g.setColor(new Color(0,0,blue));
		}
		
		g.fillOval(1120-width1,120-width1,width1*2,width1*2);
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(950, 300, 20, 201);
		
		g.setColor(Color.blue);
		g.fillRect(952, 500-(width1*2), 16, (width1*2));
		
		g.setColor(Color.black);
		g.drawString(""+width1, 950, 515);
		
		drawButton(g, 1170, 300, 100, 30, "Undo", undo_pressed);
		drawButton(g, 1170, 370, 100, 30, "Redo", redo_pressed);
		drawButton(g, 970, 600, 120, 15, "Render Areas", save_pressed);
	}

	
	private void drawButton(Graphics g, int x, int y, int width, int offset, String msg, boolean pressed){
		if (pressed){
        	g.setColor(new Color(180,180,180));
        	g.fillRect(x, y, width, 50);
        	g.setColor(new Color(150,150,150));
        	g.fillRect(x, y, width, 5);
        	g.fillRect(x, y, 5, 50);
        	g.setColor(new Color(220,220,220));
        	g.fillRect(x, 45+y, width, 5);
        	g.fillRect(width-5+x, y, 5, 50);
        }
        else{
        	g.setColor(new Color(200,200,200));
        	g.fillRect(x, y, width, 50);
        	g.setColor(new Color(220,220,220));
        	g.fillRect(x, y, width, 5);
        	g.fillRect(x, y, 5, 50);
        	g.setColor(new Color(150,150,150));
        	g.fillRect(x, 45+y, width, 5);
        	g.fillRect(width-5+x, y, 5, 50);
        }
        g.setColor(Color.black);
        g.drawString(msg, offset+x, 30+y);
	}


	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}



	public void mousePressed(MouseEvent e) {
		int x=e.getX();
		int y=e.getY();
		
		// picture
		if (e.getX()>5 && e.getX()<25 && e.getY()>5 && e.getY()<25){
			main.setPicture(null);
		}
		else{
			int selected=-1;
			for (int i=0;i<main.listImages.length;i++){
				if (e.getX()>5 && e.getX()<25 && e.getY()>25+i*20 && e.getY()<45+i*20) selected=i;
			}
			if (selected>-1) main.setPicture(main.listImages[selected]);
		}
		
		// color selector
		if (x>1000 && x<1020 && y>260 && y<280) color=0;
		if (x>1030 && x<1050 && y>260 && y<280) color=1;
		if (x>1060 && x<1080 && y>260 && y<280) color=2;
		
		if (x>1000 && x<1140 && y>300 && y<320) value=0;
		for (short c=0;c<25;c++){
			if (x>1000+30*(c%5) && x<1020+30*(c%5) && y>330+30*(int)(c/5) && y<350+30*(int)(c/5)) value=(short) (c+1);
		}
		
		if (value==0){
			red=0;
			green=0;
			blue=0;
			areaID=0;
		}
		else{
			if (color==0){ // red
				red=(short) (value*10);
				green=-12;
				blue=-1;
				areaID=value;
			}
			if (color==1){ // green
				green=(short) (value*10);
				red=-12;
				blue=-1;
				areaID=(short) (value+25);
			}
			if (color==2){ // blue
				blue=(short) (value*10);
				green=-12;
				red=-1;
				areaID=(short) (value+50);
			}
		}
		
		// sliders and buttons
		if (x>=950 && x<=970 && y>=300 && y<=500){
			width1=(500-y)/2;
			slider_capture=true;
		}
		
		// undo button
		if (e.getX()>1170 && e.getX()<1270 && e.getY()>300 && e.getY()<350){
			if (!undo_pressed && !redo_pressed){
				undo_pressed=true;
				main.undo();
			}
		}
		
		// redo button
		if (e.getX()>1170 && e.getX()<1270 && e.getY()>370 && e.getY()<420){
			if (!undo_pressed && !redo_pressed){
				redo_pressed=true;
				main.redo();
			}
		}
		
		// save button
		if (e.getX()>970 && e.getX()<1090 && e.getY()>600 && e.getY()<650){
			if (!save_pressed && !save_pressed){
				save_pressed=true;
				main.saveArea();
			}
		}
		this.repaint();
	}



	public void mouseReleased(MouseEvent arg0) {

		undo_pressed=false;
		redo_pressed=false;
		save_pressed=false;
		
		slider_capture=false;
		
		this.repaint();
		main.renderPanel.repaint();
		
		if (write) main.pushBuffer();
		write=false;
	}



	public void mouseDragged(MouseEvent e) {
		ex=e.getX()-200;
		ey=e.getY();
		
		if (!slider_capture){
			for (int i=-width1;i<=width1;i++){
				for (int j=-width1;j<=width1;j++){
					if (i*i+j*j<width1*width1 && ex+i>=0 && ex+i<700 && ey+j>=0 && ey+j<700){
					
						
						if (red  >=0) main.area[ex+i][ey+j][0]=red;
						if (green>=0) main.area[ex+i][ey+j][1]=green;
						if (blue >=0) main.area[ex+i][ey+j][2]=blue;
						
						write=true;
					}
				}
			}
		}
		
		if (slider_capture){
			width1=(500-ey)/2;
			
			if (width1>100) width1=100;
			if (width1<1) width1=1;
		}
		
		this.repaint();
	}



	public void mouseMoved(MouseEvent e) {

		ex=e.getX()-200;
		ey=e.getY();
		
		currentAreaID.clear();

		if (ex>0 && ex<700 && ey>0 && ey<700){
			int r=main.area[ex][ey][0]/10;
			int g=main.area[ex][ey][1]/10;
			int b=main.area[ex][ey][2]/10;
			
			if (r==0 && g==0 && b==0) currentAreaID.add(0);
			else{
				if (r>0) currentAreaID.add(r);
				if (g>0) currentAreaID.add(g+25);
				if (b>0) currentAreaID.add(b+50);
			}
		}
		
		this.repaint();
	}
}