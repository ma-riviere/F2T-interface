package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;


import main.Main;
import main.SoundSource;

public class DisplaySourcePanel extends JPanel implements MouseListener , MouseMotionListener{

	private static final long serialVersionUID = 1L;
	
	protected Main main;
	public DisplayFrame frame;
	
	public DisplaySourcePanel(Main m, DisplayFrame f){
		main=m;
		frame=f;
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void paintComponent(Graphics g){
		
		g.setColor(Color.white);
		g.fillRect(0, 0, 1100, 800);
		
		g.setColor(Color.black);
		g.drawRect(0, 0, 999, 699);
		
		if (frame.selected_image==0){
			if (main.currentAge.image.view!=null) g.drawImage(main.currentAge.image.view_img_miniature, 413, 263, this);
			else{
				g.setColor(Color.gray);
				g.fillRect(413, 263, 175, 175);
				g.setColor(Color.black);
				g.drawRect(413, 263, 175, 175);
				g.drawLine(414, 350, 587, 350);
				g.drawLine(500, 263, 500, 438);
			}
		}
		
		if (frame.selected_image==1){
			if (main.currentAge.image.tactile!=null) g.drawImage(main.currentAge.image.tactile_img_miniature, 413, 263, this);
			else{
				g.setColor(Color.gray);
				g.fillRect(413, 263, 175, 175);
				g.setColor(Color.black);
				g.drawRect(413, 263, 175, 175);
				g.drawLine(414, 350, 587, 350);
				g.drawLine(500, 263, 500, 438);
			}
		}
		
		if (frame.selected_image==2){
			if (main.currentAge.image.flow!=null) g.drawImage(main.currentAge.image.flow_img_miniature, 413, 263, this);
			else{
				g.setColor(Color.gray);
				g.fillRect(413, 263, 175, 175);
				g.setColor(Color.black);
				g.drawRect(413, 263, 175, 175);
				g.drawLine(414, 350, 587, 350);
				g.drawLine(500, 263, 500, 438);
			}
		}
		
		if (frame.selected_image==3){
			if (main.currentAge.image.rail!=null) g.drawImage(main.currentAge.image.rail_img_miniature, 413, 263, this);
			else{
				g.setColor(Color.gray);
				g.fillRect(413, 263, 175, 175);
				g.setColor(Color.black);
				g.drawRect(413, 263, 175, 175);
				g.drawLine(414, 350, 587, 350);
				g.drawLine(500, 263, 500, 438);
			}
		}
		
		if (frame.selected_image==4){
			if (main.currentAge.image.area!=null) g.drawImage(main.currentAge.image.area_img_miniature, 413, 263, this);
			else{
				g.setColor(Color.gray);
				g.fillRect(413, 263, 175, 175);
				g.setColor(Color.black);
				g.drawRect(413, 263, 175, 175);
				g.drawLine(414, 350, 587, 350);
				g.drawLine(500, 263, 500, 438);
			}
		}
		
		if (frame.selected_image<0){
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
 		g.drawLine(500+(int)(main.x/4), 350-(int)(main.y/4),500+(int)(main.x/4)+(int)main.dx*5, 350-(int)(main.y/4)-(int)main.dy*5);
 		
 		/////////////////////////////////////
 		// draw sound sources
 		g.setColor(Color.red);
 		for (int i=0;i<main.currentAge.sourceList.size();i++){
			g.fillOval(495+(int)(main.currentAge.sourceList.get(i).px/4), 345-(int)(main.currentAge.sourceList.get(i).py/4), 5, 5);
			g.drawString(""+i+": ("+(int)main.currentAge.sourceList.get(i).rx+", "+(int)main.currentAge.sourceList.get(i).ry+")",
					     495+(int)(main.currentAge.sourceList.get(i).px/4) ,340-(int)(main.currentAge.sourceList.get(i).py/4) );
		}
		
	}

	public void mouseClicked(MouseEvent e) {
		
		int x=(int)(e.getX()-500)*4;
		int y=(int)(-e.getY()+350)*4;

		boolean found=false;
		
		for (int i=0;i<main.currentAge.sourceList.size();i++){
			if ( (main.currentAge.sourceList.get(i).px-x)*(main.currentAge.sourceList.get(i).px-x) + (main.currentAge.sourceList.get(i).py-y)*(main.currentAge.sourceList.get(i).py-y)<1600){
				main.currentAge.sourceList.remove(i);
				i--;
				found=true;
			}
		}
		
		
		if (!found) main.currentAge.sourceList.add(new SoundSource(x, y ));
	}
	
	
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	public void mouseDragged(MouseEvent e) {}

	public void mouseMoved(MouseEvent e) {
		frame.updateSourcePanel((e.getX()-500)*4, (e.getY()-350)*4);
	}
}
