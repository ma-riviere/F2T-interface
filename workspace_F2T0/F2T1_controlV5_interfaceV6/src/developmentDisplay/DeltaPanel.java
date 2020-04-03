package developmentDisplay;


import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import main.Main;



/**
 * Display the delta between real and virtual position
 * @author simon gay
 */

public class DeltaPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;

	public Main main;
	
	public int p1x=0;
	public int p2x=0;
	public int p1y=0;
	public int p2y=0;
	
	public DeltaPanel(Main m){
		main=m;
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

}
