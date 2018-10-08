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
public class SurfacePanel extends JPanel implements MouseListener, MouseMotionListener{
	
	private static final long serialVersionUID = 1L;
	
	public Main main;
	private DisplayFrame frame;
	Graphics2D g2d;
	AffineTransform at;
	
	public SurfacePanel(Main m, DisplayFrame f){
		main=m;
		frame=f;
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void paintComponent(Graphics g){
		
		if (frame.selected_image==0){
			if (main.script.ageList.get(main.script.currentAge).image.view!=null) g.drawImage(main.script.ageList.get(main.script.currentAge).image.view_img, 0, 0, this);
			else{
				g.setColor(Color.gray);
				g.fillRect(0, 0, 800, 800);
				g.setColor(new Color(110,110,110));
				for (int i=0;i<800;i+=20){
					g.drawLine(i, 0, i, 800);
					g.drawLine(0, i, 800, i);
				}
				g.setColor(Color.black);
				g.drawLine(400, 5, 400, 805);
				g.drawLine(5, 400, 800, 405);
			}
		}
		else if (frame.selected_image==1){
			if (main.script.ageList.get(main.script.currentAge).image.area!=null) g.drawImage(main.script.ageList.get(main.script.currentAge).image.area_img, 0, 0, this);
			else{
				g.setColor(Color.gray);
				g.fillRect(0, 0, 800, 800);
				g.setColor(new Color(110,110,110));
				for (int i=0;i<800;i+=20){
					g.drawLine(i, 0, i, 800);
					g.drawLine(0, i, 800, i);
				}
				g.setColor(Color.black);
				g.drawLine(400, 0, 400, 800);
				g.drawLine(0, 400, 800, 400);
			}
		}
		else{
			g.setColor(Color.gray);
			g.fillRect(0, 0, 800, 800);
			g.setColor(new Color(110,110,110));
			for (int i=0;i<800;i+=20){
				g.drawLine(i, 0, i, 800);
				g.drawLine(0, i, 800, i);
			}
			g.setColor(Color.black);
			g.drawLine(400, 0, 400, 800);
			g.drawLine(0, 400, 800, 400);
		}
		g.setColor(Color.black);
		g.drawRect(0, 0, 800, 800);
		
        // draw joystick position
 		g.setColor(Color.darkGray);
 		g.drawOval((int)(main.x_prev+400-25), (int)(400-main.y_prev-25), 50, 50);
 		g.setColor(Color.white);
 		g.drawOval((int)(main.x+400-25), (int)(400-main.y-25), 50, 50);
 		g.drawLine((int)(main.x+400   ), (int)(400-main.y   ), (int)(main.x+400)+(int)main.dx, (int)(400-main.y)-(int)main.dy);
    
 		// draw trace
		for (int t=0;t<Main.LENGTH-1;t++){
			if (t+1!=main.time){
				if (main.trace[t][2]>0) g.setColor(Color.cyan);
				else g.setColor(Color.lightGray);
					
				g.drawLine((int)main.trace[t][0]+400, 400-(int)main.trace[t][1],
						   (int)main.trace[t+1][0]+400, 400-(int)main.trace[t+1][1]);
			}
		}
	}


	public void mouseClicked(MouseEvent e) {}
	
	
	public void mousePressed(MouseEvent e) {
		if (e.getX()>10 && e.getX()<795 && e.getY()>10 && e.getY()<795){
			
			main.clicked=true;
			main.frame_counter=0;
			
			frame.x=e.getX()-400;
			frame.y=400-e.getY();
		}
		else{
			main.clicked=false;
			main.frame_counter=0;
		}
	}
	public void mouseReleased(MouseEvent e) {
		main.clicked=false;
		main.frame_counter=0;	
	}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {
		main.clicked=false;
		main.frame_counter=0;	
	}

	public void mouseDragged(MouseEvent e) {
		
		if (e.getX()>5 && e.getX()<795 && e.getY()>10 && e.getY()<795){
			frame.x=e.getX()-400;
			frame.y=400-e.getY();
		}
	}

	public void mouseMoved(MouseEvent e) {}
}
