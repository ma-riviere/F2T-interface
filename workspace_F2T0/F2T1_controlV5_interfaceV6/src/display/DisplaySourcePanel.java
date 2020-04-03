package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;


import main.Main;
import main.SoundSource;

/**
 * sound source environment edition tool
 * @author simon gay
 */

public class DisplaySourcePanel extends JPanel implements MouseListener , MouseMotionListener{

	private static final long serialVersionUID = 1L;
	
	protected Main main;
	public DisplayFrame frame;
	
	public int selected_source=-1;
	public boolean point_capture=false;
	public boolean click_right=false;
	
	public DisplaySourcePanel(Main m, DisplayFrame f){
		main=m;
		frame=f;
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void paintComponent(Graphics g){
		
		g.setColor(Color.white);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		g.setColor(Color.black);
		g.drawRect(0, 0, 999, 699);
		
		if (frame.selected_image==0){
			if (main.script.ageList.get(main.script.currentAge).image.view!=null) g.drawImage(main.script.ageList.get(main.script.currentAge).image.view_img_miniature, 413, 263, this);
			else{
				g.setColor(Color.gray);
				g.fillRect(413, 263, 175, 175);
				g.setColor(Color.black);
				g.drawRect(413, 263, 175, 175);
				g.drawLine(414, 350, 587, 350);
				g.drawLine(500, 263, 500, 438);
			}
		}
		
		else if (frame.selected_image==1){
			if (main.script.ageList.get(main.script.currentAge).image.tactile!=null) g.drawImage(main.script.ageList.get(main.script.currentAge).image.tactile_img_miniature, 413, 263, this);
			else{
				g.setColor(Color.gray);
				g.fillRect(413, 263, 175, 175);
				g.setColor(Color.black);
				g.drawRect(413, 263, 175, 175);
				g.drawLine(414, 350, 587, 350);
				g.drawLine(500, 263, 500, 438);
			}
		}
		
		else if (frame.selected_image==2){
			if (main.script.ageList.get(main.script.currentAge).image.flow!=null) g.drawImage(main.script.ageList.get(main.script.currentAge).image.flow_img_miniature, 413, 263, this);
			else{
				g.setColor(Color.gray);
				g.fillRect(413, 263, 175, 175);
				g.setColor(Color.black);
				g.drawRect(413, 263, 175, 175);
				g.drawLine(414, 350, 587, 350);
				g.drawLine(500, 263, 500, 438);
			}
		}
		
		else if (frame.selected_image==3){
			if (main.script.ageList.get(main.script.currentAge).image.rail!=null) g.drawImage(main.script.ageList.get(main.script.currentAge).image.rail_img_miniature, 413, 263, this);
			else{
				g.setColor(Color.gray);
				g.fillRect(413, 263, 175, 175);
				g.setColor(Color.black);
				g.drawRect(413, 263, 175, 175);
				g.drawLine(414, 350, 587, 350);
				g.drawLine(500, 263, 500, 438);
			}
		}
		
		else if (frame.selected_image==4){
			if (main.script.ageList.get(main.script.currentAge).image.area!=null) g.drawImage(main.script.ageList.get(main.script.currentAge).image.area_img_miniature, 413, 263, this);
			else{
				g.setColor(Color.gray);
				g.fillRect(413, 263, 175, 175);
				g.setColor(Color.black);
				g.drawRect(413, 263, 175, 175);
				g.drawLine(414, 350, 587, 350);
				g.drawLine(500, 263, 500, 438);
			}
		}
		else if (frame.selected_image<0){
			g.setColor(Color.gray);
			g.fillRect(413, 263, 175, 175);
			g.setColor(Color.black);
			g.drawRect(413, 263, 175, 175);
			g.drawLine(414, 350, 587, 350);
			g.drawLine(500, 263, 500, 438);
		}
		
		/////////////////////////////////////
		// draw joystick position
 		g.setColor(Color.blue);
 		g.drawOval(495+(int)(main.x/4), 345-(int)(main.y/4), 10, 10);
 		g.drawLine(500+(int)(main.x/4), 350-(int)(main.y/4),500+(int)(main.x/4)+(int)main.jx*5, 350-(int)(main.y/4)-(int)main.jy*5);
 		
 		/////////////////////////////////////
 		// draw sound sources
 		for (int i=0;i<main.script.ageList.get(main.script.currentAge).sourceList.size();i++){
 			g.setColor(Color.red);
			g.fillOval(495+(int)(main.script.ageList.get(main.script.currentAge).sourceList.get(i).px/4), 345-(int)(main.script.ageList.get(main.script.currentAge).sourceList.get(i).py/4), 5, 5);
			g.drawString(""+i+": ("+(int)main.script.ageList.get(main.script.currentAge).sourceList.get(i).rx+", "+(int)main.script.ageList.get(main.script.currentAge).sourceList.get(i).ry+")",
					     495+(int)(main.script.ageList.get(main.script.currentAge).sourceList.get(i).px/4) ,340-(int)(main.script.ageList.get(main.script.currentAge).sourceList.get(i).py/4) );
 		}
	}


	public void mouseClicked(MouseEvent e) {

		if (e.getButton()==MouseEvent.BUTTON1){
			int x=(int)(e.getX()-500)*4;
			int y=(int)(-e.getY()+350)*4;
			boolean found=false;
			for (int i=0;i<main.script.ageList.get(main.script.currentAge).sourceList.size();i++){
				if ( (main.script.ageList.get(main.script.currentAge).sourceList.get(i).px-x)*(main.script.ageList.get(main.script.currentAge).sourceList.get(i).px-x) + (main.script.ageList.get(main.script.currentAge).sourceList.get(i).py-y)*(main.script.ageList.get(main.script.currentAge).sourceList.get(i).py-y)<1600){
					main.script.ageList.get(main.script.currentAge).sourceList.get(i).close();
					main.script.ageList.get(main.script.currentAge).sourceList.remove(i);
					i--;
					found=true;
				}
			}
			if (!found) main.script.ageList.get(main.script.currentAge).sourceList.add(new SoundSource("t "+x+" "+y+" "+frame.getSourceParameters() ));
		}
		this.repaint();
	}
	
	
	public void mousePressed(MouseEvent e) {
		
		
		
		if (e.getButton()==MouseEvent.BUTTON3){
			
			int x=(int)(e.getX()-500)*4;
			int y=(int)(-e.getY()+350)*4;
			
			click_right=true;
			for (int i=0;i<main.script.ageList.get(main.script.currentAge).sourceList.size();i++){
				if ( (main.script.ageList.get(main.script.currentAge).sourceList.get(i).px-x)*(main.script.ageList.get(main.script.currentAge).sourceList.get(i).px-x) + (main.script.ageList.get(main.script.currentAge).sourceList.get(i).py-y)*(main.script.ageList.get(main.script.currentAge).sourceList.get(i).py-y)<1600){
					selected_source=i;
					point_capture=true;
				}
			}
		}
	}
	
	
	public void mouseReleased(MouseEvent e) {
		click_right=false;
		point_capture=false;
	}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	public void mouseDragged(MouseEvent e) {
		
		if (click_right && point_capture){
			int x=(int)(e.getX()-500)*4;
			int y=(int)(-e.getY()+350)*4;
			
			main.script.ageList.get(main.script.currentAge).sourceList.get(selected_source).px=x;
			main.script.ageList.get(main.script.currentAge).sourceList.get(selected_source).py=y;
		}
		
	}

	public void mouseMoved(MouseEvent e) {
		frame.updateSourcePanel((e.getX()-500)*4, (e.getY()-350)*4);
	}
}
