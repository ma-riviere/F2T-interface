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
public class DisplayLinksPanel extends JPanel implements MouseListener{
	
	private static final long serialVersionUID = 1L;
	
	public Main main;
	public DisplayFrame frame;
	
	private int x=350;
	private int y=350;
	
	public DisplayLinksPanel(Main m, DisplayFrame f){
		main=m;
		frame=f;
		addMouseListener(this);
		this.setLayout(null);
	}
	
	public void paintComponent(Graphics g){

		if (main.script.ageList.get(main.script.currentAge).image.area!=null) g.drawImage(main.script.ageList.get(main.script.currentAge).image.area_img, 0, 0, this);
		else{
			g.setColor(Color.gray);
			g.fillRect(0, 0, 699, 699);
			g.setColor(Color.black);
			g.drawRect(0, 0, 699, 699);
			g.drawLine(350,0,350,700);
			g.drawLine(0,350,700,350);
		}
			

        // draw joystick position

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
		
 		g.setColor(Color.white);
 		g.drawLine(x-20, y-20, x+20, y+20);
 		g.drawLine(x+20, y-20, x-20, y+20);
	}

	

	public void mouseClicked(MouseEvent e) {

		x=e.getX();
		y=e.getY();
		
		if (x>0 && x<700 && y>0 && y<700){
			
			if (main.currentAge.image.area==null){
				frame.areaRed=0;
				frame.areaGreen=-1;
				frame.areaBlue=-1;
				frame.selectedArea=0;
			}
			else{
				if (main.currentAge.image.area_mat[x][y][0]==0 && main.currentAge.image.area_mat[x][y][1]==0 && main.currentAge.image.area_mat[x][y][2]==0){
					frame.areaRed=0;
					frame.areaGreen=-1;
					frame.areaBlue=-1;
					frame.selectedArea=0;
				}
				else{
					if (main.currentAge.image.area_mat[x][y][0]>0) frame.areaRed=main.currentAge.image.area_mat[x][y][0];
					else frame.areaRed=-1;
					
					if (main.currentAge.image.area_mat[x][y][1]>0) frame.areaGreen=main.currentAge.image.area_mat[x][y][1];
					else frame.areaGreen=-1;
					
					if (main.currentAge.image.area_mat[x][y][2]>0) frame.areaBlue=main.currentAge.image.area_mat[x][y][2];
					else frame.areaBlue=-1;
					
					
					if (frame.areaRed>0) frame.selectedArea=frame.areaRed;
					else if (frame.areaGreen>0) frame.selectedArea=frame.areaGreen+25;
					else frame.selectedArea=frame.areaBlue+50;		
				}
			}
		}

		frame.repaint();
	}
	
	
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}


}
