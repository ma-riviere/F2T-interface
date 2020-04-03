package display;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import main.Main;

/**
 * Display current tactile properties values
 * @author simon gay
 */

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
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		g.setColor(Color.black);
		g.drawRect(0, 0, 615, 50);
		
		int offset=0;
		for (int i=0;i<main.virtual.moduleList.size();i++){
			if (offset+main.virtual.moduleList.get(i).panelWidth<615){
				main.virtual.moduleList.get(i).paint(g, offset);
				offset+=main.virtual.moduleList.get(i).panelWidth;
			}
		}
		
		g.setColor(Color.black);
		g.drawString("current areas : ", offset, 14);
		
		if (main.currentAge.current_area_red==0 && main.currentAge.current_area_green==0 && main.currentAge.current_area_blue==0){
			g.drawString("0:", offset, 30);
			g.setColor(Color.black);
			g.fillRect(offset, 33, 20, 15);
		}
		else{
			int i=0;
			if (main.currentAge.current_area_red>0){
				g.setColor(Color.black);
				g.drawString(main.currentAge.current_area_red+":", offset, 30);
				g.setColor(new Color(10*main.currentAge.current_area_red,0,0));
				g.fillRect(offset, 33, 20, 15);
				i++;
			}
			
			if (main.currentAge.current_area_green>0){
				g.setColor(Color.black);
				g.drawString((main.currentAge.current_area_green+25)+" :", offset+30*i, 30);
				g.setColor(new Color(0,10*main.currentAge.current_area_green,0));
				g.fillRect(offset+30*i, 33, 20, 15);
				i++;
			}
			
			if (main.currentAge.current_area_blue>0){
				g.setColor(Color.black);
				g.drawString((main.currentAge.current_area_blue+50)+" :", offset+30*i, 30);
				g.setColor(new Color(0,0,10*main.currentAge.current_area_blue));
				g.fillRect(offset+30*i, 33, 20, 15);
				i++;
			}
		}
	}

}
