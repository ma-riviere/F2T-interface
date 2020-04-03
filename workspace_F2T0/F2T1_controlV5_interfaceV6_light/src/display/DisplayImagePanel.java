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
	
	public static float SCALE=0.4f;
	
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
				if (main.script.ageList.get(main.script.currentAge).image.view!=null) g.drawImage(main.script.ageList.get(main.script.currentAge).image.view_img_miniature, 0, 0, this);
				else{
					g.setColor(Color.gray);
					g.fillRect(0, 0, (int)(699*SCALE), (int)(699*SCALE));
					g.setColor(Color.black);
					g.drawRect(0, 0, (int)(699*SCALE), (int)(699*SCALE));
					g.drawLine((int)(350*SCALE),0,(int)(350*SCALE),(int)(700*SCALE));
					g.drawLine(0,(int)(350*SCALE),(int)(700*SCALE),(int)(350*SCALE));
				}
			}
			else if (frame.selected_image==1){
				if (main.script.ageList.get(main.script.currentAge).image.tactile!=null) g.drawImage(main.script.ageList.get(main.script.currentAge).image.tactile_img_miniature, 0, 0, this);
				else{
					g.setColor(Color.gray);
					g.fillRect(0, 0, (int)(699*SCALE), (int)(699*SCALE));
					g.setColor(Color.black);
					g.drawRect(0, 0, (int)(699*SCALE), (int)(699*SCALE));
					g.drawLine((int)(350*SCALE),0,(int)(350*SCALE),(int)(700*SCALE));
					g.drawLine(0,(int)(350*SCALE),(int)(700*SCALE),(int)(350*SCALE));
				}
			}
			else if (frame.selected_image==2){
				if (main.script.ageList.get(main.script.currentAge).image.flow!=null) g.drawImage(main.script.ageList.get(main.script.currentAge).image.flow_img_miniature, 0, 0, this);
				else{
					g.setColor(Color.gray);
					g.fillRect(0, 0, (int)(699*SCALE), (int)(699*SCALE));
					g.setColor(Color.black);
					g.drawRect(0, 0, (int)(699*SCALE), (int)(699*SCALE));
					g.drawLine((int)(350*SCALE),0,(int)(350*SCALE),(int)(700*SCALE));
					g.drawLine(0,(int)(350*SCALE),(int)(700*SCALE),(int)(350*SCALE));
				}
			}
			else if (frame.selected_image==3){
				if (main.script.ageList.get(main.script.currentAge).image.rail!=null) g.drawImage(main.script.ageList.get(main.script.currentAge).image.rail_img_miniature, 0, 0, this);
				else{
					g.setColor(Color.gray);
					g.fillRect(0, 0, (int)(699*SCALE), (int)(699*SCALE));
					g.setColor(Color.black);
					g.drawRect(0, 0, (int)(699*SCALE), (int)(699*SCALE));
					g.drawLine((int)(350*SCALE),0,(int)(350*SCALE),(int)(700*SCALE));
					g.drawLine(0,(int)(350*SCALE),(int)(700*SCALE),(int)(350*SCALE));
				}
			}
			else if (frame.selected_image==4){
				if (main.script.ageList.get(main.script.currentAge).image.area!=null) g.drawImage(main.script.ageList.get(main.script.currentAge).image.area_img_miniature, 0, 0, this);
				else{
					g.setColor(Color.gray);
					g.fillRect(0, 0, (int)(699*SCALE), (int)(699*SCALE));
					g.setColor(Color.black);
					g.drawRect(0, 0, (int)(699*SCALE), (int)(699*SCALE));
					g.drawLine((int)(350*SCALE),0,(int)(350*SCALE),(int)(700*SCALE));
					g.drawLine(0,(int)(350*SCALE),(int)(700*SCALE),(int)(350*SCALE));
				}
			}
			else if (frame.selected_image==5){
				if (main.script.ageList.get(main.script.currentAge).image.magnetic!=null){
					g.drawImage(main.script.ageList.get(main.script.currentAge).image.magnetic_img_miniature, 0, 0, this);
				}
				else{
					g.setColor(Color.gray);
					g.fillRect(0, 0, (int)(699*SCALE), (int)(699*SCALE));
					g.setColor(Color.black);
					g.drawRect(0, 0, (int)(699*SCALE), (int)(699*SCALE));
					g.drawLine((int)(350*SCALE),0,(int)(350*SCALE),(int)(700*SCALE));
					g.drawLine(0,(int)(350*SCALE),(int)(700*SCALE),(int)(350*SCALE));
				}
			}
			

	        // draw joystick position

			if (frame.displayTrace){
				g.setColor(Color.yellow);
				for (int t=0;t<Main.LENGTH-1;t++){
					if (t+1!=main.time){
					g.drawLine((int)((350+main.trace[t  ][0])*SCALE), (int)((350-main.trace[t][1])*SCALE),
							   (int)((350+main.trace[t+1][0])*SCALE), (int)((350-main.trace[t+1][1])*SCALE));
					}
				}
				g.setColor(Color.cyan);
				for (int t=0;t<Main.LENGTH-1;t++){
					if (t+1!=main.time){
					g.drawLine((int)((350+main.trace[t  ][2])*SCALE), (int)((350-main.trace[t][3])*SCALE),
							   (int)((350+main.trace[t+1][2])*SCALE), (int)((350-main.trace[t+1][3])*SCALE));
					}
				}
			}
			
			if (frame.displayJoystick){
				g.setColor(Color.yellow);
				g.drawOval((int)((325+main.x)*SCALE), (int)((325-main.y)*SCALE), (int)(50*SCALE), (int)(50*SCALE));
				g.drawLine((int)((350+main.x)*SCALE), (int)((350-main.y)*SCALE),(int)((350+main.x)*SCALE)+(int)main.jx*3, (int)((350-main.y)*SCALE)-(int)main.jy*3);
				
				// draw virtual position
				g.setColor(Color.cyan);
				g.drawOval((int)((325+main.x)*SCALE), (int)((325-main.y)*SCALE), (int)(50*SCALE), (int)(50*SCALE));
			}
			
			
			// draw path
			if (main.script.ageList.get(main.script.currentAge)!=null && main.script.ageList.get(main.script.currentAge).targetSequence.sizeTarget()>0){
				if (main.script.ageList.get(main.script.currentAge).targetSequence.targetList.get(0).control==0) g.setColor(Color.yellow);
				else g.setColor(Color.magenta);
				g.fillOval((int)((348+main.script.ageList.get(main.script.currentAge).targetSequence.targetList.get(0).x)*SCALE),
						   (int)((348-main.script.ageList.get(main.script.currentAge).targetSequence.targetList.get(0).y)*SCALE), (int)(5*SCALE), (int)(5*SCALE));
				for (int i=1;i<main.script.ageList.get(main.script.currentAge).targetSequence.sizeTarget();i++){
					if (main.script.ageList.get(main.script.currentAge).targetSequence.targetList.get(i).control==0) g.setColor(Color.yellow);
					else g.setColor(Color.magenta);
					g.fillOval((int)((348+main.script.ageList.get(main.script.currentAge).targetSequence.targetList.get(i).x)*SCALE),
							   (int)((348-main.script.ageList.get(main.script.currentAge).targetSequence.targetList.get(i).y)*SCALE), (int)(5*SCALE), (int)(5*SCALE));
				}
			}
			if (main.script.ageList.get(main.script.currentAge)!=null && main.script.ageList.get(main.script.currentAge).targetSequence.sizePoint()>0){
				for (int i=1;i<main.script.ageList.get(main.script.currentAge).targetSequence.sizePoint();i++){
					if (main.script.ageList.get(main.script.currentAge).targetSequence.pointList.get(i).control==0) g.setColor(Color.yellow);
					else g.setColor(Color.red);
					g.drawLine((int)((350+main.script.ageList.get(main.script.currentAge).targetSequence.pointList.get(i-1).x)*SCALE),
							   (int)((350-main.script.ageList.get(main.script.currentAge).targetSequence.pointList.get(i-1).y)*SCALE),
							   (int)((350+main.script.ageList.get(main.script.currentAge).targetSequence.pointList.get(i  ).x)*SCALE),
							   (int)((350-main.script.ageList.get(main.script.currentAge).targetSequence.pointList.get(i  ).y)*SCALE));
				}
			}
	}

	

	public void mouseClicked(MouseEvent e) {

		int x=e.getX();
		int y=e.getY();
		
		if (frame.selected_image>=0){
			if (x>0 && x<700*SCALE && y>0 && y<700*SCALE){
				main.script.ageList.get(main.script.currentAge).targetSequence.add(new TargetPoint((int)((-350*SCALE+e.getX())/SCALE),
						                                                                           (int)(( 350*SCALE-e.getY())/SCALE),main.target_speed, main.target_type));
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
	}

}
