
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;



/**
 * Generic class of panel that can be exported as pdf file and jpeg image
 * @author simon
 */
public class PicturePanel extends JPanel implements MouseListener, MouseMotionListener{

	private static final long serialVersionUID = 1L;
	
	protected Main main;
	
	public static int VALX_MIN=-350;
	public static int VALX_MAX= 350;
	public static int VALY_MIN= 270;
	public static int VALY_MAX=-430;
	
	public static int FRAMEX_MIN=-230;
	public static int FRAMEX_MAX= 220;
	public static int FRAMEY_MIN= 200;
	public static int FRAMEY_MAX=-280;
	
	public boolean click_right=false;
	public boolean click_left=false;
	
	private boolean mode_pressed=false;
	private boolean remove_pressed=false;
	private boolean add_pressed=false;
	private boolean set_pressed=false;
	
	private boolean clear_pressed=false;
	private boolean undo_pressed=false;
	private boolean redo_pressed=false;
	
	private boolean save_pressed=false;

	public int selected_point=0;
	
	public boolean write=false;
	
	public boolean point_capture=false;
	public boolean slider_capture=false;
	
	public int speed=200;
	
	public int target_type=1;
	
	public int ex=0;
	public int ey=0;
	
	public int tx=0;
	public int ty=0;
	
	public int frameXmin;
	public int frameYmin;
	public int frameXmax;
	public int frameYmax;
	
	
	public PicturePanel(Main m){
		main=m;
		
		frameXmin=(int)(( (float)(FRAMEX_MIN-VALX_MIN) / (float)(VALX_MAX-VALX_MIN) ) *700);
		frameYmin=(int)(( (float)(FRAMEY_MIN-VALY_MIN) / (float)(VALY_MAX-VALY_MIN) ) *700);
		frameXmax=(int)(( (float)(FRAMEX_MAX-VALX_MIN) / (float)(VALX_MAX-VALX_MIN) ) *700);
		frameYmax=(int)(( (float)(FRAMEY_MAX-VALY_MIN) / (float)(VALY_MAX-VALY_MIN) ) *700);
		
		addMouseListener(this);
		addMouseMotionListener(this);
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
			g.fillRect(200, 0, 700, 700);
		}
		

		
		g.setColor(Color.red);
		g.drawRect(frameXmin+200, frameYmin, frameXmax-frameXmin, frameYmax-frameYmin);
		
		
		// draw speed slider
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(950, 70, 20, 201);
		
		g.setColor(Color.blue);
		g.fillRect(952, 270-(int)(speed/2.5), 16, (int)(speed/2.5));
		
		g.setColor(Color.black);
		g.drawString(""+speed, 945, 290);
		
		
		/////////////////////////////////////////////////////////////////////////////
		// draw target sequence
		
		if (main.path.size()>0){
			if (main.path.get(0).control==0) g.setColor(Color.yellow);
			else g.setColor(Color.magenta);
			
			int vx1=(int)(( (float)(main.path.get(0).x-VALX_MIN) / (float)(VALX_MAX-VALX_MIN) ) *700);
			int vy1=(int)(( (float)(main.path.get(0).y-VALY_MIN) / (float)(VALY_MAX-VALY_MIN) ) *700);
			
			g.fillOval(vx1+200-4, vy1-4, 10, 10);
			
			if (selected_point==0){
				g.setColor(Color.magenta);
				g.drawOval(vx1+200-7, vy1-7, 16, 16);
			}
			
			for (int i=1;i<main.path.size();i++){
				
				vx1=(int)(( (float)(main.path.get(i-1).x-VALX_MIN) / (float)(VALX_MAX-VALX_MIN) ) *700);
				vy1=(int)(( (float)(main.path.get(i-1).y-VALY_MIN) / (float)(VALY_MAX-VALY_MIN) ) *700);
				int vx2=(int)(( (float)(main.path.get(i  ).x-VALX_MIN) / (float)(VALX_MAX-VALX_MIN) ) *700);
				int vy2=(int)(( (float)(main.path.get(i  ).y-VALY_MIN) / (float)(VALY_MAX-VALY_MIN) ) *700);
				
				if (main.path.get(i).control==0) g.setColor(Color.yellow);
				else g.setColor(Color.magenta);
				g.drawLine(vx1+200, vy1, vx2+200, vy2);
				g.fillOval(vx2+200-4, vy2-4, 10, 10);
				
				if (selected_point==i){
					g.setColor(Color.magenta);
					g.drawOval(vx2+200-7, vy2-7, 16, 16);
				}
			}	
		}
		///////////////////////////////////////////////////////////////////////////////////
		
		// current point position
		g.setColor(Color.black);
		g.drawRect(950, 5, 200, 25);
		if (ex>0 && ex<700 && ey>0 && ey<700){
			g.drawString(""+((int)tx), 960, 23);
			g.drawString(""+((int)ty), 1010, 23);
			g.drawString(""+speed, 1050, 23);
			if (target_type==0) g.drawString("attractor", 1085, 23);
			if (target_type==1) g.drawString("target", 1095, 23);
		}
		
		// point position
		g.setColor(Color.black);
		g.drawRect(950, 35, 200, 25);
		if (selected_point>=0 && selected_point<main.path.size()){
			g.drawString(""+main.path.get(selected_point).x, 960, 53);
			g.drawString(""+main.path.get(selected_point).y, 1010, 53);
			g.drawString(""+main.path.get(selected_point).speed, 1050, 53);
			if (main.path.get(selected_point).control==0) g.drawString("attractor", 1085, 53);
			if (main.path.get(selected_point).control==1) g.drawString("target", 1095, 53);
		}
		
		
		// guide mode, remove and add buttons
        drawButton(g,1050,70, 100, 10, "Guide mode", mode_pressed);
        drawButton(g,1050,130, 100, 20, "Remove", remove_pressed);
        drawButton(g,1050,190, 100, 30, "Add", add_pressed);
        drawButton(g,1050,250, 100, 30, "Set", set_pressed);
        
        // mode leds
        if (target_type==0){
        	g.setColor(Color.black);
        	g.fillOval(1160, 80, 10, 10);
        	g.setColor(Color.red);
        	g.fillOval(1160, 100, 10, 10);
        }
        else{
        	g.setColor(Color.red);
        	g.fillOval(1160, 80, 10, 10);
        	g.setColor(Color.black);
        	g.fillOval(1160, 100, 10, 10);
        }
        g.setColor(Color.black);
        g.drawString("full guide", 1175, 90);
        g.drawString("attractor", 1175, 110);
		
		
		drawButton(g, 1050, 350, 100, 30, "Clear", clear_pressed);
		drawButton(g, 1050, 420, 100, 30, "Undo", undo_pressed);
		drawButton(g, 1050, 490, 100, 30, "Redo", redo_pressed);
		
		drawButton(g, 970, 600, 120, 25, "Write path", save_pressed);
		
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
		ex=e.getX()-200;
		ey=e.getY();
		
		tx= ex * (VALX_MAX-VALX_MIN) / 700 +VALX_MIN;
		ty= ey * (VALY_MAX-VALY_MIN) / 700 +VALY_MIN;
		
		if (!click_left && !click_right){
			if (e.getButton()==MouseEvent.BUTTON3){
				click_right=true;
			}
			else if (e.getButton()==MouseEvent.BUTTON1){
				click_left=true;
			}
		}
		
		
		if (click_left){
			
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
			
			// add target
			if (ex>frameXmin && ex<frameXmax && ey>frameYmin && ey<frameYmax){
				main.path.add(new Target(tx, ty,speed, target_type));
				main.pushBuffer();
			}
			
			if (e.getX()>=950 && e.getX()<=970 && e.getY()>=70 && e.getY()<=270){
				speed=(int) ((270-ey)*2.5);
				slider_capture=true;
			}
			
			// mode button
			if (e.getX()>1050 && e.getX()<1150 && e.getY()>70 && e.getY()<120){
				if (!mode_pressed && !remove_pressed && !add_pressed && !set_pressed){
					mode_pressed=true;
					target_type=(target_type+1)%2;
				}
			}
			
			// remove button
			if (e.getX()>1050 && e.getX()<1150 && e.getY()>130 && e.getY()<180){
				if (!mode_pressed && !remove_pressed && !add_pressed && !set_pressed){
					remove_pressed=true;
					if (selected_point>=0 && selected_point<main.path.size()){
						main.path.remove(selected_point);
						if (selected_point>=main.path.size()) selected_point--;
						main.pushBuffer();
					}
				}
			}
			
			// add button
			if (e.getX()>1050 && e.getX()<1150 && e.getY()>190 && e.getY()<240){
				if (!mode_pressed && !remove_pressed && !add_pressed && !set_pressed){
					add_pressed=true;
					if (selected_point>0 && selected_point<main.path.size()){
						int x1=(main.path.get(selected_point).x + main.path.get(selected_point-1).x )/2;
						int y1=(main.path.get(selected_point).y + main.path.get(selected_point-1).y )/2;
						
						main.path.add(selected_point, new Target(x1, y1,speed, target_type));
						main.pushBuffer();
					}
				}
			}
			
			// set button
			if (e.getX()>1050 && e.getX()<1150 && e.getY()>250 && e.getY()<300){
				if (!mode_pressed && !remove_pressed && !add_pressed && !set_pressed){
					set_pressed=true;
					if (selected_point>0 && selected_point<main.path.size()){
						main.path.get(selected_point).speed=speed;
						main.path.get(selected_point).control=target_type;
					}
					main.pushBuffer();
				}
			}
			
			
			
			// clear button
			if (e.getX()>1050 && e.getX()<1150 && e.getY()>350 && e.getY()<400){
				if (!clear_pressed && !undo_pressed && !redo_pressed){
					clear_pressed=true;
					main.path.clear();
					main.pushBuffer();
				}
			}
			
			// undo button
			if (e.getX()>1050 && e.getX()<1150 && e.getY()>420 && e.getY()<470){
				if (!clear_pressed && !undo_pressed && !redo_pressed){
					undo_pressed=true;
					main.undo();
				}
			}
			
			// redo button
			if (e.getX()>1050 && e.getX()<1150 && e.getY()>490 && e.getY()<540){
				if (!clear_pressed && !undo_pressed && !redo_pressed){
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
		}
		
		if (click_right){
			
			if (ex>0 && ex<700 && ey>0 && ey<700){
				double min=10;
				int imin=-1;
				double d=0;
				
				for (int i=0;i<main.path.size();i++){
					d=Math.sqrt( (tx-(main.path.get(i).x))*(tx-(main.path.get(i).x)) + (ty-(main.path.get(i).y))*(ty-(main.path.get(i).y)));
					if (d<min){
						min=d;
						imin=i;
					}
				}
				
				if (imin>=0){
					selected_point=imin;
					point_capture=true;
				}
				
			}
		}
		
		this.repaint();
	}



	public void mouseReleased(MouseEvent e) {
		
		if (e.getButton()==MouseEvent.BUTTON3){
			click_right=false;
		}
		if (e.getButton()==MouseEvent.BUTTON1){
			click_left=false;
		}
		
		point_capture=false;
		slider_capture=false;
		
		mode_pressed=false;
		remove_pressed=false;
		add_pressed=false;
		set_pressed=false;
		
		clear_pressed=false;
		undo_pressed=false;
		redo_pressed=false;
		
		save_pressed=false;
		
		if (write) main.pushBuffer();
		write=false;
	}



	public void mouseDragged(MouseEvent e) {
		ex=e.getX()-200;
		ey=e.getY();
		
		tx= ex * (VALX_MAX-VALX_MIN) / 700 +VALX_MIN;
		ty= ey * (VALY_MAX-VALY_MIN) / 700 +VALY_MIN;
		
		if (click_right && point_capture){
			
			if (main.path.get(selected_point).x!=tx || main.path.get(selected_point).y!=ty) write=true;
			
			main.path.get(selected_point).x=tx;
			main.path.get(selected_point).y=ty;
		}
		
		if (click_left && slider_capture){
			speed=(int) ((270-ey)*2.5);
			
			if (speed>500) speed=500;
			if (speed<0) speed=0;
		}
		
		
		this.repaint();
	}



	public void mouseMoved(MouseEvent e) {

		ex=e.getX()-200;
		ey=e.getY();
		
		tx= ex * (VALX_MAX-VALX_MIN) / 700 +VALX_MIN;
		ty= ey * (VALY_MAX-VALY_MIN) / 700 +VALY_MIN;
		
		this.repaint();
	}
}