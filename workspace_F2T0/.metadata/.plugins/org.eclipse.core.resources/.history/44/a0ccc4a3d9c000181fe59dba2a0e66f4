package display;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import main.Main;



/**
 * Display the probe input
 * @author simon gay
 */

/* - inherited from EnvPanel :
 *   Agent agent      : pointer to the agent
 */
public class TouchPanel extends JPanel implements MouseListener{
	
	private static final long serialVersionUID = 1L;

	public Main main;
	
	public TouchPanel(Main m){
		main=m;
		addMouseListener(this);
	}
	
	public void paintComponent(Graphics g){
		
		
		g.setColor(Color.black);
		g.fillRect(0, 0, 1300, 700);
		
		for (int i=0;i<25;i++){
			for (int j=0;j<25;j++){
				
				float val=(main.sphere[i][j]);
				if (val<0) val=0;
				if (val>1) val=1;
				
				g.setColor(new Color(val, 0,1-val));
				g.drawRect(20+i*20, 20+j*20, 19, 19);
				
				val=main.heighMap[i][j];
				g.setColor(new Color(0, val,0));
				g.fillRect(21+i*20, 21+j*20, 18, 18);
				
				val=main.contactMap[i][j];
				if (val<0) val=0;
				g.setColor(new Color(val, 0,0));
				g.fillRect(620+i*20, 20+j*20, 20, 20);
				
			}
		}
	
		float val=Math.max(0, main.contactPression);
		g.setColor(new Color(val, 1, 0));
		g.fillOval(620+(int)((main.contactX*12)+12)*20, 20+(int)((main.contactY*12)+12)*20, 20, 20);
		
		g.setColor(Color.gray);
		g.fillRect(550, 20, 10, 500);
		g.setColor(Color.green);
		g.fillRect(540, 15+500-(int)(main.contactHeight*500), 30, 5);
		
	}

	public void mouseClicked(MouseEvent e) {

	}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}
