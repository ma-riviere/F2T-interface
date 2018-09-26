package display;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import main.Main;



/**
 * Display the probe input
 * @author simon gay
 */

/* - inherited from EnvPanel :
 *   Agent agent      : pointer to the agent
 */
public class CameraPanel extends JPanel implements MouseListener, MouseMotionListener{
	
	private static final long serialVersionUID = 1L;
	
	private static int PRESET=850;
	private static int IMG=980;
	private static int SCRIPT=1430;
	
	public int x=0;
	public int y=0;
	

	public Main main;
	
	Graphics2D g2d;
	AffineTransform at;
	
	//private boolean guide_mode=false;
	
	public CameraPanel(Main m){
		main=m;
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void paintComponent(Graphics g){
		
		// draw image camera
		
		if (main.image.view_img!=null){
			
			float ix=main.image.view_img.getWidth();
			float iy=main.image.view_img.getHeight();
			
			if (ix!=800 && iy!=800){
				float scale=800/Math.max(ix, iy);
				g2d = (Graphics2D) g.create();
				at = new AffineTransform();
				at.setToScale(scale, scale);
				g2d.setTransform(at);
				
				g2d.drawImage(main.image.view_img, 5, 5, this);
			}
			else g.drawImage(main.image.view_img, 5, 5, this);
		}
		else{
			g.setColor(Color.gray);
			g.fillRect(5, 5, 800, 800);
			g.setColor(new Color(110,110,110));
			for (int i=5;i<805;i+=20){
				g.drawLine(i, 5, i, 805);
				g.drawLine(5, i, 805, i);
			}
			g.setColor(Color.black);
			g.drawLine(405, 5, 405, 805);
			g.drawLine(5, 405, 805, 405);
		}
		g.setColor(Color.black);
		g.drawRect(5, 5, 800, 800);
		
        // draw joystick position
 		g.setColor(Color.darkGray);
 		g.drawOval((int)(main.x_prev+405-25), (int)(405-main.y_prev-25), 50, 50);
 		g.setColor(Color.white);
 		g.drawOval((int)(main.x+405-25), (int)(405-main.y-25), 50, 50);
 		g.drawLine((int)(main.x+405   ), (int)(405-main.y   ), (int)(main.x+405)+(int)main.dx, (int)(405-main.y)-(int)main.dy);
    
 		// draw trace
		g.setColor(Color.cyan);
		for (int t=0;t<Main.LENGTH-1;t++){
			if (t+1!=main.time){
			g.drawLine((int)main.trace[t][0]+405, 405-(int)main.trace[t][1],
					   (int)main.trace[t+1][0]+405, 405-(int)main.trace[t+1][1]);
			}
		}
 		
        
        g.setColor(Color.black);
        for (int i=0;i<main.listPreset.length;i++){
            g.fillRect(PRESET, 10+i*20, 10, 10);
            g.drawString(main.listPreset[i], PRESET+15, 20+20*i);
        }
		
        if (main.selected_img==-1) g.setColor(Color.red);
        else g.setColor(Color.black);
        g.fillOval(IMG, 10, 10, 10);
        g.drawString("No image", IMG+15, 20);
        for (int i=0;i<main.listImages.length;i++){
        	if (main.selected_img==i) g.setColor(Color.red);
            else g.setColor(Color.black);
            g.fillOval(IMG, 30+i*20, 10, 10);
            g.drawString(main.listImages[i], IMG+15, 40+20*i);
        }
	
 
        
        g.setColor(Color.black);
        for (int i=0;i<main.listScript.length;i++){
            g.fillRect(SCRIPT, 10+i*20, 10, 10);
            g.drawString(main.listScript[i], SCRIPT+15, 20+20*i);
        }
        

	}

	
	private void drawButton(Graphics g, int x, int y, String msg, boolean pressed){
		if (pressed){
        	g.setColor(new Color(180,180,180));
        	g.fillRect(x, y, 100, 50);
        	g.setColor(new Color(150,150,150));
        	g.fillRect(x, y, 100, 5);
        	g.fillRect(x, y, 5, 50);
        	g.setColor(new Color(220,220,220));
        	g.fillRect(x, 45+y, 100, 5);
        	g.fillRect(95+x, y, 5, 50);
        }
        else{
        	g.setColor(new Color(200,200,200));
        	g.fillRect(x, y, 100, 50);
        	g.setColor(new Color(220,220,220));
        	g.fillRect(x, y, 100, 5);
        	g.fillRect(x, y, 5, 50);
        	g.setColor(new Color(150,150,150));
        	g.fillRect(x, 45+y, 100, 5);
        	g.fillRect(95+x, y, 5, 50);
        }
        g.setColor(Color.black);
        g.drawString(msg, 13+x, 30+y);
	}
	

	public void mouseClicked(MouseEvent e) {

		// preset
		int selected=-1;
		for (int i=0;i<main.listPreset.length;i++){
			if (e.getX()>PRESET-5 && e.getX()<PRESET+15 && e.getY()>5+i*20 && e.getY()<25+i*20) selected=i;
		}
		if (selected>-1) main.setPreset(selected);
		
		// script
		selected=-1;
		for (int i=0;i<main.listScript.length;i++){
			if (e.getX()>SCRIPT-5 && e.getX()<SCRIPT+15 && e.getY()>5+i*20 && e.getY()<25+i*20) selected=i;
		}
		if (selected>-1) main.setScript(selected);

		// picture
		if (e.getX()>IMG-5 && e.getX()<IMG+15 && e.getY()>5 && e.getY()<25){
			main.setPicture(null);
		}
		else{
			selected=-1;
			for (int i=0;i<main.listImages.length;i++){
				if (e.getX()>IMG-5 && e.getX()<IMG+15 && e.getY()>25+i*20 && e.getY()<45+i*20) selected=i;
			}
			if (selected>-1) main.setPicture(main.listImages[selected]);
		}
	}
	
	
	public void mousePressed(MouseEvent e) {

	}
	public void mouseReleased(MouseEvent e) {

	}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	public void mouseDragged(MouseEvent e) {
		
		if (e.getX()>10 && e.getX()<795 && e.getY()>10 && e.getY()<795){
			x=e.getX()-405;
			y=405-e.getY();
		}
	}

	public void mouseMoved(MouseEvent e) {}
}