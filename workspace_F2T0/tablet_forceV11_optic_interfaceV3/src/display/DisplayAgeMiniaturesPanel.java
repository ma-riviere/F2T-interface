package display;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import main.Main;



/**
 * Display the probe input
 * @author simon gay
 */

/* - inherited from EnvPanel :
 *   Agent agent      : pointer to the agent
 */
public class DisplayAgeMiniaturesPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	

	public Main main;
	public DisplayFrame frame;
	
	public DisplayAgeMiniaturesPanel(Main m, DisplayFrame f){
		main=m;
		frame=f;
		this.setLayout(null);
	}
	
	
	public void paintComponent(Graphics g){
		
		if (Main.CAMERA_CONNECTED){
			Image image = Main.Mat2bufferedImage(main.webcam_miniature);
		    g.drawImage(image, 5, 10, this);
		    
		}
		
		if (main.currentAge.image.view!=null) g.drawImage(main.currentAge.image.view_img_miniature, 0, 0, this);
		else{
			g.setColor(Color.gray);
			g.fillRect(0, 0, 175, 175);
			g.setColor(Color.black);
			g.drawRect(0, 0, 175, 175);
			g.drawLine(87, 0, 87, 175);
			g.drawLine(0, 87, 175, 87);
		}
		
		if (main.currentAge.image.area!=null) g.drawImage(main.currentAge.image.area_img_miniature, 180, 0, this);
		else{
			g.setColor(Color.gray);
			g.fillRect(180, 0, 175, 175);
			g.setColor(Color.black);
			g.drawRect(180, 0, 175, 175);
			g.drawLine(267, 0, 267, 175);
			g.drawLine(180, 87, 355, 87);
		}
		
		// draw joystick position
		g.setColor(Color.magenta);
		g.drawOval(82+(int)(main.x/4), 82-(int)(main.y/4), 10, 10);
		
		g.drawOval(262+(int)(main.x/4), 82-(int)(main.y/4), 10, 10);
	}


}
