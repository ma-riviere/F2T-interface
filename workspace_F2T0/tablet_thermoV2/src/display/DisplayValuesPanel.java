package display;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import main.Main;

public class DisplayValuesPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	public Main main;
	public DisplayFrame frame;
	
	public DisplayValuesPanel(Main m, DisplayFrame f){
		main=m;
		frame=f;
		this.setLayout(null);
	}
	
	
	public void paintComponent(Graphics g){
		
		g.setColor(this.getBackground());
		g.fillRect(0, 0, 355, 50);
		
		g.setColor(Color.black);
		g.drawRect(0, 0, 354, 50);
		

		g.setColor(Color.black);
		g.drawString("current areas : ", 10, 14);
		
		if (main.area_red_read==0 && main.area_green_read==0 && main.area_blue_read==0){
			g.drawString("0 :", 10, 35);
			g.setColor(Color.black);
			g.fillRect(40, 20, 20, 20);
		}
		else{
			int i=0;
			if (main.area_red_read>0){
				g.setColor(Color.black);
				g.drawString(main.area_red_read+" :", 10, 35);
				g.setColor(new Color(10*main.area_red_read,0,0));
				g.fillRect(40, 20, 20, 20);
				i++;
			}
			
			if (main.area_green_read>0){
				g.setColor(Color.black);
				g.drawString((main.area_green_read+25)+" :", 10+70*i, 35);
				g.setColor(new Color(0,10*main.area_green_read,0));
				g.fillRect(40+70*i, 20, 20, 20);
				i++;
			}
			
			if (main.area_blue_read>0){
				g.setColor(Color.black);
				g.drawString((main.area_blue_read+50)+" :", 10+70*i, 35);
				g.setColor(new Color(0,0,10*main.area_blue_read));
				g.fillRect(40+70*i, 20, 20, 20);
				i++;
			}
		}
		
	}
	

}
