package display;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;

import main.Main;



/**
 * Display the probe input
 * @author simon gay
 */

/* - inherited from EnvPanel :
 *   Agent agent      : pointer to the agent
 */
public class DisplayFlowSettingPanel extends JPanel implements MouseListener, MouseMotionListener{
	
	private static final long serialVersionUID = 1L;
	

	public Main main;
	private DisplayFrame frame;
	
	public DisplayFlowSettingPanel panel=this;
	
	public boolean click_right=false;
	public boolean click_left=false;
	
	public boolean histogram_capture=false;

	private boolean slider_length=false;
	public boolean slider_width1=false;
	public boolean slider_width2=false;
	public boolean slider_angle=false;
	public boolean slider_dispersion=false;
	
	public int ex=0;
	public int ey=0;
	
	public DisplayFlowSettingPanel(Main m, DisplayFrame f, KeyboardListener k){
		main=m;
		frame=f;
		this.setLayout(null);
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
	}

	
	public void paintComponent(Graphics g){

		if (frame.display_mode==9){
			
			g.setColor(this.getBackground());
			g.fillRect(0, 0, 375, 230);
			g.setColor(Color.black);
			
			// path bloc
			g.drawRect(0, 0, 375, 220);
			
			// histogram
			g.setColor(Color.black);
			g.drawRect(10, 10, 200, 100);
			g.drawLine(110, 10, 110, 110);
			
			g.setColor(Color.red);
			g.drawLine(110-main.flow_range2/2, 110, 110-(int)(main.flow_range2/2*main.flow_range1/100), 110-(int)(main.flow_speed));
			g.drawLine(110+main.flow_range2/2, 110, 110+(int)(main.flow_range2/2*main.flow_range1/100), 110-(int)(main.flow_speed));
			g.drawLine(110-(int)(main.flow_range2/2*main.flow_range1/100), 110-(int)(main.flow_speed),
					   110+(int)(main.flow_range2/2*main.flow_range1/100), 110-(int)(main.flow_speed));
			
			// sliders
			g.setColor(Color.lightGray);
			g.fillRect(250, 10, 22, 100);	// speed
			g.fillRect(320, 10, 22, 100);	// dispersion
			g.fillRect(10, 130, 200, 22);	// range1
			g.fillRect(10, 160, 200, 22);	// range2
			g.fillRect(10, 190, 200, 22);	// angle
			
			g.setColor(Color.blue);
			g.fillRect(252, 110-main.flow_speed, 18, main.flow_speed);
			g.fillRect(322, 110-(int)(0.556*(main.flow_dispersion+90))-1, 18, 3);
			g.fillRect( 10, 132, (int)(main.flow_range1*2), 18);
			g.fillRect( 10, 162, (int)(main.flow_range2), 18);
			g.fillRect( 10+(int)(1.11*(main.flow_angle+90))-1, 192, 3, 18);
			
			// params
			g.setColor(Color.black);
			g.drawString("D1="+(int)main.flow_range1+"%", 220, 145);
			g.drawString("D2="+(int)main.flow_range2+"px", 220, 175);
			g.drawString("angle="+(int)main.flow_angle+"°", 220, 205);
			g.drawString("p="+(int)main.flow_speed+"%", 235, 125);
			g.drawString("disp="+(int)main.flow_dispersion+"°", 305, 125);
			
		}
 		
	}
	


	public void mouseDragged(MouseEvent e) {
		ex=e.getX();
		ey=e.getY();
		
		
		// speed slider
		if (slider_length){
			main.flow_speed=Math.min(100, Math.max(0,(110-e.getY())));
			if (frame.flowEditPanel.selected_point>=0 && frame.flowEditPanel.selected_point<frame.flowEditPanel.line[frame.flowEditPanel.selected_slot].sizeTarget()){
				frame.flowEditPanel.line[frame.flowEditPanel.selected_slot].targetList.get(frame.flowEditPanel.selected_point).speed=main.flow_speed;
				frame.flowEditPanel.write=true;
			}	
		}
		// range1 slider
		if (slider_width1){
			main.flow_range1=Math.min(100, Math.max(0,(e.getX()-10)/2));
			if (frame.flowEditPanel.selected_point>=0 && frame.flowEditPanel.selected_point<frame.flowEditPanel.line[frame.flowEditPanel.selected_slot].sizeTarget()){
				frame.flowEditPanel.line[frame.flowEditPanel.selected_slot].targetList.get(frame.flowEditPanel.selected_point).range1=(int) main.flow_range1;
				frame.flowEditPanel.write=true;
			}	
		}
		// range2 slider
		if (slider_width2){
			main.flow_range2=Math.min(200, Math.max(0,(e.getX()-10)));
			if (frame.flowEditPanel.selected_point>=0 && frame.flowEditPanel.selected_point<frame.flowEditPanel.line[frame.flowEditPanel.selected_slot].sizeTarget()){
				frame.flowEditPanel.line[frame.flowEditPanel.selected_slot].targetList.get(frame.flowEditPanel.selected_point).range2=(int) main.flow_range2;
				frame.flowEditPanel.write=true;
			}	
		}
		// angle slider
		if (slider_angle){
			main.flow_angle=(float)Math.min(90, Math.max(-90,(e.getX()-110)*0.9));
			if (frame.flowEditPanel.selected_point>=0 && frame.flowEditPanel.selected_point<frame.flowEditPanel.line[frame.flowEditPanel.selected_slot].sizeTarget()){
				frame.flowEditPanel.line[frame.flowEditPanel.selected_slot].targetList.get(frame.flowEditPanel.selected_point).angle=(int) main.flow_angle;
				frame.flowEditPanel.write=true;
			}	
		}
		// dispersion slider
		if (slider_dispersion){
			main.flow_dispersion=Math.min(90, Math.max(-90,(60-e.getY())*1.8f));
			if (frame.flowEditPanel.selected_point>=0 && frame.flowEditPanel.selected_point<frame.flowEditPanel.line[frame.flowEditPanel.selected_slot].sizeTarget()){
				frame.flowEditPanel.line[frame.flowEditPanel.selected_slot].targetList.get(frame.flowEditPanel.selected_point).dispersion=main.flow_dispersion;
				frame.flowEditPanel.write=true;
			}	
		}
		this.repaint();
	}


	public void mouseMoved(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}


	public void mousePressed(MouseEvent e) {
		ex=e.getX();
		ey=e.getY();

		// detect clicked button
		if (!click_left && !click_right){
			if (e.getButton()==MouseEvent.BUTTON3){
				click_right=true;
			}
			else if (e.getButton()==MouseEvent.BUTTON1){
				click_left=true;
			}
		}
		
		if (click_left){

			// sliders
			if (ex>=250 && ex<=272 && ey>=10 && ey<=110){
				slider_length=true;
			}
			if (ex>=10 && ex<=210 && ey>=130 && ey<=152){
				slider_width1=true;
			}
			if (ex>=10 && ex<=210 && ey>=160 && ey<=182){
				slider_width2=true;
			}
			if (ex>=10 && ex<=210 && ey>=190 && ey<=212){
				slider_angle=true;
			}
			if (ex>=320 && ex<=342 && ey>=10 && ey<=110){
				slider_dispersion=true;
			}

		}
		

		if (click_right){
			
			// reset sliders values
			if (ex>=10 && ex<=210 && ey>=190 && ey<=212){
				main.flow_angle=0;
				if (frame.flowEditPanel.selected_point>=0 && frame.flowEditPanel.selected_point<frame.flowEditPanel.line[frame.flowEditPanel.selected_slot].sizeTarget()){
					frame.flowEditPanel.line[frame.flowEditPanel.selected_slot].targetList.get(frame.flowEditPanel.selected_point).angle=0;
					frame.flowEditPanel.write=true;
				}	
			}
			if (ex>=290 && ex<=312 && ey>=10 && ey<=110){
				main.flow_dispersion=0;
				if (frame.flowEditPanel.selected_point>=0 && frame.flowEditPanel.selected_point<frame.flowEditPanel.line[frame.flowEditPanel.selected_slot].sizeTarget()){
					frame.flowEditPanel.line[frame.flowEditPanel.selected_slot].targetList.get(frame.flowEditPanel.selected_point).dispersion=0;
					frame.flowEditPanel.write=true;
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
		
		histogram_capture=false;
		
		slider_length=false;
		slider_width1=false;
		slider_width2=false;
		slider_angle=false;
		slider_dispersion=false;
		
		if (frame.flowEditPanel.write) frame.flowEditPanel.pushBuffer();
		frame.flowEditPanel.write=false;
		
		frame.flowEditPanel.update();
	}


	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

}
