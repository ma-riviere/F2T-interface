package developmentDisplay;


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
public class DeltaPanel extends JPanel implements MouseListener, MouseMotionListener{
	
	private static final long serialVersionUID = 1L;

	public Main main;
	
	public int p1x=0;
	public int p2x=0;
	public int p1y=0;
	public int p2y=0;
	
	public DeltaPanel(Main m){
		main=m;
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void paintComponent(Graphics g){
		
		
		g.setColor(Color.white);
		g.fillRect(0, 0, 1300, 700);
		
		g.setColor(Color.black);
		g.drawLine(0, 650,1000,650);
		for (int i=0;i<999;i++){
			g.drawLine(i, 650-Math.abs(main.deltaX_buff[i]/10), i+1, 650-Math.abs(main.deltaX_buff[i+1]/10));
		}
		
	}

	public void mouseClicked(MouseEvent e) {

	}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseMoved(MouseEvent e) {
		int ex=e.getX();
		int ey=e.getY();
		
		if (ex>0 && ex<699 && ey>0 && ey<699){
			
			
			
			float dist=(float)Math.sqrt(main.currentAge.image.gradient[ex][699-ey][0]*main.currentAge.image.gradient[ex][699-ey][0]
								     +main.currentAge.image.gradient[ex][699-ey][1]*main.currentAge.image.gradient[ex][699-ey][1]);
			
			System.out.println("### "+((int)(100*main.currentAge.image.gradient[ex][699-ey][0]))+" ; "
			                         +((int)(100*main.currentAge.image.gradient[ex][699-ey][1]))+" ; "
			                         +(dist*100) );
			
			p1x=ex;
			p1y=ey;
			
			p2x=ex+(int)(main.currentAge.image.gradient[ex][ey][0]*100);
			p2y=ey+(int)(main.currentAge.image.gradient[ex][ey][1]*100);
		}
		
		//this.repaint();
		
		
		
	}
}
