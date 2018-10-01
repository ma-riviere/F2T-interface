package display;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import main.Main;

public class DisplayAgesPanel  extends JPanel{

	private static final long serialVersionUID = 1L;

	private Main main;
	
	public DisplayAgesPanel(Main m){
		
		main=m;
		
		
	}
	
	public void paintComponent(Graphics g){
		
		g.setColor(Color.white);
		g.fillRect(0, 0, 1000, 700);
		g.setColor(Color.black);
		g.drawRect(0, 0, 999, 699);
		
		for (int a=0;a<main.script.size();a++){
			
			if (main.script.getCurrentIndex()==a) g.setColor(Color.lightGray);
			else g.setColor(Color.gray);
			g.fillRoundRect(50, 10+120*a, 900, 115, 25, 25);
			
			g.setColor(Color.black);
        	g.drawString(main.script.sequence.get(a).getImages(), 80, 30+120*a);
        	g.drawString(main.script.sequence.get(a).getPath(), 80, 50+120*a);
        	g.drawString(main.script.sequence.get(a).getAreas(), 80, 70+120*a);
        	g.drawString(main.script.sequence.get(a).getSources(), 80, 90+120*a);
        	g.drawString(main.script.sequence.get(a).getCondition(), 80, 110+120*a);
        }
		
		
		
	}
}
