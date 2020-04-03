package developmentDisplay;


import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import main.Image;
import main.Main;



/**
 * Display height map
 * @author simon gay
 */

public class TouchPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;

	public Main main;
	
	public int p1x=0;
	public int p2x=0;
	public int p1y=0;
	public int p2y=0;
	
	public TouchPanel(Main m){
		main=m;
	}
	
	public void paintComponent(Graphics g){
		
		
		g.setColor(Color.black);
		g.fillRect(0, 0, 1300, 700);

		int val1=0;
		int val2=0;
		for (int i=0;i<Image.SIZE;i++){
			for (int j=0;j<Image.SIZE;j++){
				val1=(int)(main.currentAge.image.gradient[i][j][0]*127)+127;
				val2=(int)(main.currentAge.image.gradient[i][j][1]*127)+127;
				if (val1>255) val1=255;
				if (val1<0) val1=0;
				if (val2>255) val2=255;
				if (val2<0) val2=0;
				g.setColor(new Color(val1,val2,0));
				
				g.drawLine(i, 699-j, i+1, 699-j);
			}
		}
	}

}
