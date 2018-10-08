package display;


import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import main.Main;



/**
 * Display the probe input
 * @author simon gay
 */

/* - inherited from EnvPanel :
 *   Agent agent      : pointer to the agent
 */
public class DisplayImagePanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	public Main main;
	public DisplayFrame frame;
	
	
	//private boolean guide_mode=false;
	
	public DisplayImagePanel(Main m, DisplayFrame f){
		main=m;
		frame=f;
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


	        // draw joystick position
			if (frame.selected_image>=0){
				
					g.setColor(Color.cyan);
					for (int t=0;t<Main.LENGTH-1;t++){
						if (t+1!=main.time){
						g.drawLine(350+(int)main.trace[t  ][0], 350-(int)main.trace[t][1],
								   350+(int)main.trace[t+1][0], 350-(int)main.trace[t+1][1]);
						}
					}

				
					g.setColor(Color.gray);
					g.drawOval(325+(int)(main.x_prev), 325-(int)(main.y_prev), 50, 50);

		 		g.setColor(Color.white);
		 		g.drawOval(325+(int)(main.x), 325-(int)(main.y), 50, 50);
		 		g.drawLine(350+(int)(main.x), 350-(int)(main.y),350+(int)(main.x)+(int)main.dx*5, 350-(int)(main.y)-(int)main.dy*5);

			}
			else{

					g.setColor(Color.gray);
		 			g.drawOval(425+(int)(main.x_prev/3)-25, 420-(int)(main.y_prev/2.56)-25, 50, 50);

		 		g.setColor(Color.white);
		 		g.drawOval(425+(int)(main.x/3)-25, 420-(int)(main.y/2.56)-25, 50, 50);
		 		g.drawLine(425+(int)(main.x/3), 420-(int)(main.y/2.56),425+(int)(main.x/3)+(int)main.dx*5, 420-(int)(main.y/2.56)-(int)main.dy*5);

			}
	}

}
