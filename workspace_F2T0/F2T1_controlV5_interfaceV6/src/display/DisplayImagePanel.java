package display;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import structures.TargetPoint;

import main.Main;


/**
 * Main image display panel
 * @author simon gay
 */

public class DisplayImagePanel extends JPanel implements MouseListener, MouseMotionListener{
	
	private static final long serialVersionUID = 1L;
	
	public Main main;
	public DisplayFrame frame;
	

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

			if (frame.displayTrace){
				/*g.setColor(Color.yellow);
				for (int t=0;t<Main.LENGTH-1;t++){
					if (t+1!=main.time){
					g.drawLine(350+(int)main.trace[t  ][0], 350-(int)main.trace[t][1],
							   350+(int)main.trace[t+1][0], 350-(int)main.trace[t+1][1]);
					}
				}*/
				g.setColor(Color.cyan);
				for (int t=0;t<Main.LENGTH-1;t++){
					if (t+1!=main.time){
					g.drawLine(350+(int)main.trace[t  ][2], 350-(int)main.trace[t][3],
							   350+(int)main.trace[t+1][2], 350-(int)main.trace[t+1][3]);
					}
				}
			}
			
			if (frame.displayJoystick){
				//g.setColor(Color.yellow);
				//g.drawOval(325+(int)(main.x), 325-(int)(main.y), 50, 50);
				//g.drawLine(350+(int)(main.x), 350-(int)(main.y),350+(int)(main.x)+(int)main.jx*5, 350-(int)(main.y)-(int)main.jy*5);
				
				// draw virtual position
				g.setColor(Color.cyan);
				g.drawOval(325+(int)(main.virtual.px), 325-(int)(main.virtual.py), 50, 50);
			}
			
			
			// draw path
			if (main.script.ageList.get(main.script.currentAge)!=null && main.script.ageList.get(main.script.currentAge).targetSequence.sizeTarget()>0){
				if (main.script.ageList.get(main.script.currentAge).targetSequence.targetList.get(0).control==0) g.setColor(Color.yellow);
				else g.setColor(Color.magenta);
				g.fillOval(348+(int)main.script.ageList.get(main.script.currentAge).targetSequence.targetList.get(0).x, 348-(int)main.script.ageList.get(main.script.currentAge).targetSequence.targetList.get(0).y, 5, 5);
				for (int i=1;i<main.script.ageList.get(main.script.currentAge).targetSequence.sizeTarget();i++){
					if (main.script.ageList.get(main.script.currentAge).targetSequence.targetList.get(i).control==0) g.setColor(Color.yellow);
					else g.setColor(Color.magenta);
					g.fillOval(348+(int)main.script.ageList.get(main.script.currentAge).targetSequence.targetList.get(i).x, 348-(int)main.script.ageList.get(main.script.currentAge).targetSequence.targetList.get(i).y, 5, 5);
				}
			}
			if (main.script.ageList.get(main.script.currentAge)!=null && main.script.ageList.get(main.script.currentAge).targetSequence.sizePoint()>0){
				for (int i=1;i<main.script.ageList.get(main.script.currentAge).targetSequence.sizePoint();i++){
					if (main.script.ageList.get(main.script.currentAge).targetSequence.pointList.get(i).control==0) g.setColor(Color.yellow);
					else g.setColor(Color.red);
					g.drawLine(350+(int)main.script.ageList.get(main.script.currentAge).targetSequence.pointList.get(i-1).x, 350-(int)main.script.ageList.get(main.script.currentAge).targetSequence.pointList.get(i-1).y,
							   350+(int)main.script.ageList.get(main.script.currentAge).targetSequence.pointList.get(i  ).x, 350-(int)main.script.ageList.get(main.script.currentAge).targetSequence.pointList.get(i  ).y);
				}
			}
	}

	

	public void mouseClicked(MouseEvent e) {

		int x=e.getX();
		int y=e.getY();
		
		if (frame.selected_image>=0){
			if (x>0 && x<700 && y>0 && y<700){
				main.script.ageList.get(main.script.currentAge).targetSequence.add(new TargetPoint(-350+(int)e.getX(), 350-(int)e.getY(),main.target_speed, main.target_type));
			}
		}
	}
	
	
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	public void mouseDragged(MouseEvent e) {}

	public void mouseMoved(MouseEvent e) {
		frame.updatePathPanel(e.getX()-350, -e.getY()+350);
		frame.updateFlowPanel(e.getX()-350, -e.getY()+350);
	}

}
