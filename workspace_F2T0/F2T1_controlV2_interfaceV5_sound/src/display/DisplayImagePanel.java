package display;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import main.Main;
import main.Target;



/**
 * Display the probe input
 * @author simon gay
 */

/* - inherited from EnvPanel :
 *   Agent agent      : pointer to the agent
 */
public class DisplayImagePanel extends JPanel implements MouseListener, MouseMotionListener{
	
	private static final long serialVersionUID = 1L;
	
	public Main main;
	public DisplayFrame frame;
	
	
	//private boolean guide_mode=false;
	
	public DisplayImagePanel(Main m, DisplayFrame f){
		main=m;
		frame=f;
		addMouseListener(this);
		addMouseMotionListener(this);
		this.setLayout(null);
	}
	
	
	
	
	public void paintComponent(Graphics g){

			// draw image
			
			if (frame.selected_image==0){
				if (main.script.ageList.get(main.script.currentAge).image.view!=null) g.drawImage(main.script.ageList.get(main.script.currentAge).image.view_img, 0, 0, this);
				else{
					g.setColor(Color.gray);
					g.fillRect(0, 0, 699, 699);
					g.setColor(Color.black);
					g.drawRect(0, 0, 699, 699);
					g.drawLine(350,0,350,700);
					g.drawLine(0,350,700,350);
				}
			}
			else if (frame.selected_image==1){
				if (main.script.ageList.get(main.script.currentAge).image.tactile!=null) g.drawImage(main.script.ageList.get(main.script.currentAge).image.tactile_img, 0, 0, this);
				else{
					g.setColor(Color.gray);
					g.fillRect(0, 0, 699, 699);
					g.setColor(Color.black);
					g.drawRect(0, 0, 699, 699);
					g.drawLine(350,0,350,700);
					g.drawLine(0,350,700,350);
				}
			}
			else if (frame.selected_image==2){
				if (main.script.ageList.get(main.script.currentAge).image.flow!=null) g.drawImage(main.script.ageList.get(main.script.currentAge).image.flow_img, 0, 0, this);
				else{
					g.setColor(Color.gray);
					g.fillRect(0, 0, 699, 699);
					g.setColor(Color.black);
					g.drawRect(0, 0, 699, 699);
					g.drawLine(350,0,350,700);
					g.drawLine(0,350,700,350);
				}
			}
			else if (frame.selected_image==3){
				if (main.script.ageList.get(main.script.currentAge).image.rail!=null) g.drawImage(main.script.ageList.get(main.script.currentAge).image.rail_img, 0, 0, this);
				else{
					g.setColor(Color.gray);
					g.fillRect(0, 0, 699, 699);
					g.setColor(Color.black);
					g.drawRect(0, 0, 699, 699);
					g.drawLine(350,0,350,700);
					g.drawLine(0,350,700,350);
				}
			}
			else if (frame.selected_image==4){
				if (main.script.ageList.get(main.script.currentAge).image.area!=null) g.drawImage(main.script.ageList.get(main.script.currentAge).image.area_img, 0, 0, this);
				else{
					g.setColor(Color.gray);
					g.fillRect(0, 0, 699, 699);
					g.setColor(Color.black);
					g.drawRect(0, 0, 699, 699);
					g.drawLine(350,0,350,700);
					g.drawLine(0,350,700,350);
				}
			}
			else if (frame.selected_image==5){
				if (main.script.ageList.get(main.script.currentAge).image.magnetic!=null){
					g.drawImage(main.script.ageList.get(main.script.currentAge).image.magnetic_img, 0, 0, this);
					g.setColor(Color.yellow);
					g.fillOval(350+(int)main.magnetic.xmin-3, 350-(int)main.magnetic.ymin-3, 7, 7);
				}
				else{
					g.setColor(Color.gray);
					g.fillRect(0, 0, 699, 699);
					g.setColor(Color.black);
					g.drawRect(0, 0, 699, 699);
					g.drawLine(350,0,350,700);
					g.drawLine(0,350,700,350);
				}
			}
			

	        // draw joystick position
			if (frame.selected_image>=0){
				
				if (frame.displayTrace){
					g.setColor(Color.cyan);
					for (int t=0;t<Main.LENGTH-1;t++){
						if (t+1!=main.time){
						g.drawLine(350+(int)main.trace[t  ][0], 350-(int)main.trace[t][1],
								   350+(int)main.trace[t+1][0], 350-(int)main.trace[t+1][1]);
						}
					}
				}
				
				if (frame.displayJoystick){
					g.setColor(Color.gray);
					g.drawOval(325+(int)(main.x_prev), 325-(int)(main.y_prev), 50, 50);
				}
		 		g.setColor(Color.white);
		 		g.drawOval(325+(int)(main.x), 325-(int)(main.y), 50, 50);
		 		g.drawLine(350+(int)(main.x), 350-(int)(main.y),350+(int)(main.x)+(int)main.dx*5, 350-(int)(main.y)-(int)main.dy*5);
		 		if (frame.displayJoystick){
		 			g.setColor(Color.red);
		 			g.drawOval(325+(int)(main.x_next), 325-(int)(main.y_next), 50, 50);
		 		}
			}
			else{
				if (frame.displayJoystick){
					g.setColor(Color.gray);
		 			g.drawOval(425+(int)(main.x_prev/3)-25, 420-(int)(main.y_prev/2.56)-25, 50, 50);
				}
		 		g.setColor(Color.white);
		 		g.drawOval(425+(int)(main.x/3)-25, 420-(int)(main.y/2.56)-25, 50, 50);
		 		g.drawLine(425+(int)(main.x/3), 420-(int)(main.y/2.56),425+(int)(main.x/3)+(int)main.dx*5, 420-(int)(main.y/2.56)-(int)main.dy*5);
		 		if (frame.displayJoystick){
		 			g.setColor(Color.red);
		 			g.drawOval(425+(int)(main.x_next/3)-25, 420-(int)(main.y_next/2.56)-25, 50, 50);
		 		}
			}
			
			
			// draw path
			if (frame.selected_image>=0){
				if (main.script.ageList.get(main.script.currentAge)!=null && main.script.ageList.get(main.script.currentAge).targetSequence.size()>0){
					if (main.script.ageList.get(main.script.currentAge).targetSequence.get(0).control==0) g.setColor(Color.yellow);
					else g.setColor(Color.magenta);
					g.fillOval(348+(int)main.script.ageList.get(main.script.currentAge).targetSequence.get(0).x, 348-(int)main.script.ageList.get(main.script.currentAge).targetSequence.get(0).y, 5, 5);
					for (int i=1;i<main.script.ageList.get(main.script.currentAge).targetSequence.size();i++){
						if (main.script.ageList.get(main.script.currentAge).targetSequence.get(i).control==0) g.setColor(Color.yellow);
						else g.setColor(Color.magenta);
						g.drawLine(350+(int)main.script.ageList.get(main.script.currentAge).targetSequence.get(i-1).x, 350-(int)main.script.ageList.get(main.script.currentAge).targetSequence.get(i-1).y,
								   350+(int)main.script.ageList.get(main.script.currentAge).targetSequence.get(i  ).x, 350-(int)main.script.ageList.get(main.script.currentAge).targetSequence.get(i  ).y);
						g.fillOval(348+(int)main.script.ageList.get(main.script.currentAge).targetSequence.get(i).x, 348-(int)main.script.ageList.get(main.script.currentAge).targetSequence.get(i).y, 5, 5);
					}
				}
			}
			else{
				if (main.script.ageList.get(main.script.currentAge)!=null && main.script.ageList.get(main.script.currentAge).targetSequence.size()>0){
					if (main.script.ageList.get(main.script.currentAge).targetSequence.get(0).control==0) g.setColor(Color.yellow);
					else g.setColor(Color.magenta);
					g.fillOval(423+(int)(main.script.ageList.get(main.script.currentAge).targetSequence.get(0).x/3), 418-(int)(main.script.ageList.get(main.script.currentAge).targetSequence.get(0).y/2.56), 5, 5);
					for (int i=1;i<main.script.ageList.get(main.script.currentAge).targetSequence.size();i++){
						if (main.script.ageList.get(main.script.currentAge).targetSequence.get(i).control==0) g.setColor(Color.yellow);
						else g.setColor(Color.magenta);
						g.drawLine(425+(int)(main.script.ageList.get(main.script.currentAge).targetSequence.get(i-1).x/3), 420-(int)(main.script.ageList.get(main.script.currentAge).targetSequence.get(i-1).y/2.56),
								   425+(int)(main.script.ageList.get(main.script.currentAge).targetSequence.get(i  ).x/3), 420-(int)(main.script.ageList.get(main.script.currentAge).targetSequence.get(i  ).y/2.56));
						g.fillOval(424+(int)(main.script.ageList.get(main.script.currentAge).targetSequence.get(i).x/3), 419-(int)(main.script.ageList.get(main.script.currentAge).targetSequence.get(i).y/2.56), 3, 3);
					}
				}
			}
			
			// draw virtual position
			g.setColor(Color.cyan);
			g.drawOval(325+(int)(main.edges.virtualX), 325-(int)(main.edges.virtualY), 50, 50);
	}

	

	public void mouseClicked(MouseEvent e) {

		int x=e.getX();
		int y=e.getY();
		
		if (frame.selected_image>=0){
			if (x>0 && x<700 && y>0 && y<700){
				main.script.ageList.get(main.script.currentAge).targetSequence.add(new Target(-350+(int)e.getX(), 350-(int)e.getY(),main.target_speed, main.target_type));
			}
		}
	}
	
	
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	public void mouseDragged(MouseEvent e) {}

	public void mouseMoved(MouseEvent e) {
		frame.updatePathPanel(e.getX()-350, e.getY()-350);
	}

}
