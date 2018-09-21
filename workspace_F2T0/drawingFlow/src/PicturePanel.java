
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
	private boolean saveFlow_pressed=false;
	private boolean saveRail_pressed=false;
	
	private boolean write=false;
	
	public ArrayList<Integer> px;
	public ArrayList<Integer> py;
	
	public boolean slider_length=false;
	public boolean slider_width1=false;
	public boolean slider_width2=false;
	
	
	public float length=1;
	public int width1=5;
	public int width2=50;
	
	public int ex=0;
	public int ey=0;
	
	
	public PicturePanel(Main m){
		main=m;
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		px=new ArrayList<Integer>();
		py=new ArrayList<Integer>();
		
	}

	
	public void paintComponent(Graphics g){
		g.setColor(Color.white);
		g.fillRect(0, 0, 1600, 800);
		
		
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
		
		if (main.picture!=null){
			g.drawImage(main.picture_img, 200, 0, this);
		}
		else{
			g.setColor(Color.black);
			g.drawRect(200, 0, 700, 700);
		}
		
		g.setColor(Color.green);
		for (int i=0;i<700;i+=10){
			for (int j=0;j<700;j+=10){
				g.drawLine(i+200, j, i+200+(int)(main.flow[i][j][0]*10), j+(int)(main.flow[i][j][1]*10));
			}
		}
		
		
		g.setColor(Color.red);
		if (px.size()>10){
			for (int i=0;i<px.size()-10;i++){
				g.drawLine(px.get(i)+200, py.get(i), px.get(i+10)+200, py.get(i+10));
			}
		}
		
		if (ex>-50 && ex<750 && ey<700){
			g.drawOval(ex-width2+200,ey-width2,width2*2,width2*2);
			g.drawOval(ex-width1+200,ey-width1,width1*2,width1*2);
		}
		
		g.setColor(Color.black);
		g.drawRect(920, 10, 400, 220);
		g.drawLine(1120, 30, 1120, 230);
		
		g.drawLine(1120-width2*2, 230, 1120-width1*2, 230-(int)(200*length));
		g.drawLine(1120+width2*2, 230, 1120+width1*2, 230-(int)(200*length));
		g.drawLine(1120-width1*2, 230-(int)(200*length), 1120+width1*2, 230-(int)(200*length));
		
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(950, 300, 20, 201);
		g.fillRect(1000, 300, 20, 201);
		g.fillRect(1050, 300, 20, 201);
		
		g.setColor(Color.blue);
		g.fillRect(952, 500-(int)(200*length), 16, (int)(200*length));
		g.fillRect(1002, 500-(width2*2), 16, (width2*2));
		g.fillRect(1052, 500-(width1*2), 16, (width1*2));
		
		g.setColor(Color.black);
		g.drawString(""+(int)(length*100)+"%", 945, 515);
		g.drawString(""+width2, 1000, 515);
		g.drawString(""+width1, 1050, 515);
		
		
		drawButton(g, 1150, 300, 100, 30, "Undo", undo_pressed);
		drawButton(g, 1150, 370, 100, 30, "Redo", redo_pressed);
		drawButton(g, 970, 600, 120, 15, "Render as flow", saveFlow_pressed);
		drawButton(g, 1150, 600, 120, 15, "Render as rail", saveRail_pressed);
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
		
		
		// sliders and buttons
		if (x>=950 && x<=970 && y>=300 && y<=500){
			length=(500-(float)y)/200;
			slider_length=true;
		}
		if (x>=1000 && x<=1020 && y>=300 && y<=500){
			width2=(500-y)/2;
			if (width2<width1) width1=width2;
			slider_width2=true;
		}
		
		if (x>=1050 && x<=1070 && y>=300 && y<=500){
			width1=(500-y)/2;
			if (width2<width1) width2=width1;
			slider_width1=true;
		}
		
		// undo button
		if (e.getX()>1150 && e.getX()<1250 && e.getY()>300 && e.getY()<350){
			if (!undo_pressed && !redo_pressed){
				undo_pressed=true;
				main.undo();
			}
		}
		
		// redo button
		if (e.getX()>1150 && e.getX()<1250 && e.getY()>370 && e.getY()<420){
			if (!undo_pressed && !redo_pressed){
				redo_pressed=true;
				main.redo();
			}
		}
		
		// save button
		if (e.getX()>970 && e.getX()<1090 && e.getY()>600 && e.getY()<650){
			if (!saveFlow_pressed && !saveRail_pressed){
				saveFlow_pressed=true;
				main.saveFlow();
			}
		}
		if (e.getX()>1150 && e.getX()<1270 && e.getY()>600 && e.getY()<650){
			if (!saveFlow_pressed && !saveRail_pressed){
				saveRail_pressed=true;
				main.saveRail();
			}
		}
		
		this.repaint();
	}



	public void mouseReleased(MouseEvent arg0) {
		px.clear();
		py.clear();
		
		undo_pressed=false;
		redo_pressed=false;
		saveFlow_pressed=false;
		saveRail_pressed=false;
		
		slider_length=false;
		slider_width1=false;
		slider_width2=false;
		
		this.repaint();
		main.renderPanel.repaint();
		
		if (write) main.pushBuffer();
		write=false;
	}



	public void mouseDragged(MouseEvent e) {
		ex=e.getX()-200;
		ey=e.getY();
		

		
		if (!slider_length && !slider_width1 && !slider_width2){
			
			px.add(ex);
			py.add(ey);
			
			if (px.size()>10){
				
				int pos_x=px.get(px.size()-11);
				int pos_y=py.get(py.size()-11);
				
				float deltaX=ex-pos_x;
				float deltaY=ey-pos_y;
				
				float norm=(float) Math.sqrt(deltaX*deltaX + deltaY*deltaY);
				
				for (int i=-width2;i<=width2;i++){
					for (int j=-width2;j<=width2;j++){
						if (i*i+j*j<width2*width2 && pos_x+i>=0 && pos_x+i<700 && pos_y+j>=0 && pos_y+j<700){
							
							float coeff=1;
							if (i*i+j*j>width1*width1 && width1<width2){
								coeff= (width2-(float)Math.sqrt(i*i+j*j) ) / (width2-width1);
							}
						
							
							main.flow[pos_x+i][pos_y+j][0]= ( (main.flow[pos_x+i][pos_y+j][0]  + deltaX/norm * length * coeff) ) / (1+coeff);
							main.flow[pos_x+i][pos_y+j][1]= ( (main.flow[pos_x+i][pos_y+j][1]  + deltaY/norm * length * coeff) ) / (1+coeff);
							
							if (main.flow[pos_x+i][pos_y+j][0]>1) main.flow[pos_x+i][pos_y+j][0]=1;
							if (main.flow[pos_x+i][pos_y+j][1]>1) main.flow[pos_x+i][pos_y+j][1]=1;
							
							write=true;
						}
					}
				}
			}
		}
		
		if (slider_length){
			length=(500-(float)ey)/200;
			if (length>1) length=1;
			if (length<0) length=0;
		}
		if (slider_width2){
			width2=(500-ey)/2;
			if (width2<width1) width1=width2;
		}
		if (slider_width1){
			width1=(500-ey)/2;
			if (width2<width1) width2=width1;
		}
		if (width1>100) width1=100;
		if (width1<  0) width1=0;
		if (width2>100) width2=100;
		if (width2<  0) width2=0;
		
		this.repaint();
	}



	public void mouseMoved(MouseEvent e) {

		ex=e.getX()-200;
		ey=e.getY();
		
		this.repaint();
	}
}