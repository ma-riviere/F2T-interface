package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;


import main.Main;
import main.SoundSource;


public class SourcePanel extends JPanel implements MouseListener, MouseMotionListener{

	private static final long serialVersionUID = 1L;
	
	protected Main main;
	
	private int px=-10000;
	private int py=-10000;
	

	public SourcePanel(Main m){
		main=m;
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	
	public void paintComponent(Graphics g){

		g.setColor(Color.white);
		g.fillRect(0, 0, 1000, 1000);
		
		
		/// draw image camera
		g.setColor(Color.gray);
		g.fillRect(405-100, 405-100, 200, 200);
		g.setColor(Color.black);
		g.drawRect(405-100, 405-100, 200, 200);
		g.drawLine(405, 405-100, 405, 405+100);
		g.drawLine(405-100, 405, 405+100, 405);
		
		// draw joystick position
 		g.setColor(Color.white);
 		g.drawOval(405+(int)(main.x*0.25)-6, 405-(int)(main.y*0.25)-6, 12, 12);
 		g.drawLine(405+(int)(main.x*0.25), 405-(int)(main.y*0.25), 405+(int)(main.x*0.25)+(int)main.dx, 405-(int)(main.y*0.25)-(int)main.dy);
 		
 		
 		// draw sound sources
 		g.setColor(Color.red);
 		for (int i=0;i<main.soundSources.size();i++){
			g.fillOval(402+(int)(main.soundSources.get(i).px*0.25), 402-(int)(main.soundSources.get(i).py*0.25), 5, 5);
			g.drawString(""+i+": ("+(int)main.soundSources.get(i).rx+", "+(int)main.soundSources.get(i).ry+")",
					     405+(int)(main.soundSources.get(i).px*0.25) ,405-(int)(main.soundSources.get(i).py*0.25) );
		}
 		
 		if (px!=-10000){
 			g.setColor(Color.black);
 			g.drawString(px+" , "+py,5,14);
 		}
 		
	}

	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {
		px=-10000;
		py=-10000;
	}
	
	public void mousePressed(MouseEvent e) {
		
		int x=(int)(e.getX()-405)*4;
		int y=(int)(405-e.getY())*4;

		boolean found=false;
		
		for (int i=0;i<main.soundSources.size();i++){
			if ( (main.soundSources.get(i).px-x)*(main.soundSources.get(i).px-x) + (main.soundSources.get(i).py-y)*(main.soundSources.get(i).py-y)<1600){
				main.soundSources.remove(i);
				i--;
				found=true;
			}
		}
		
		
		if (!found) main.soundSources.add(new SoundSource(x, y ));
	}

	public void mouseReleased(MouseEvent arg0) {}


	public void mouseDragged(MouseEvent e) {}


	public void mouseMoved(MouseEvent e) {
		px=(int)(e.getX()-405)*4;
		py=(int)(405-e.getY())*4;
	}
}