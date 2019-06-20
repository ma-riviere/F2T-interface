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
		
		g.drawRect(10, 10, 980, 155);
		
		g.drawString("Age : "+main.script.ageList.get(main.script.currentAge).name, 30, 30);
		
		g.setColor(Color.lightGray);
		g.fillRoundRect(50, 40, 900, 115, 25, 25);
		
		g.setColor(Color.black);
    	g.drawString(main.currentAge.getImages(), 80, 60);
    	g.drawString(main.currentAge.getPath(), 80, 75);
    	g.drawString(main.currentAge.getAttractors(), 80, 90);
    	g.drawString(main.currentAge.getAreas(), 80, 105);
    	g.drawString(main.currentAge.getSources(), 80, 120);
    	g.drawString(main.currentAge.getCondition(), 80, 135);
    	g.drawString(main.currentAge.getDoor(), 80, 150);
		
    	g.drawRect(10, 175, 980, 500);
    	
    	int offset1=0;
    	int nb=main.script.ageList.get(main.script.currentAge).history.size();
    	int index=main.script.ageList.get(main.script.currentAge).current_period;
    	
    	if (nb>4){
    		if (index>1) offset1=index-1;
    		if (offset1>nb-4) offset1=nb-4;
    	}
    	
		for (int p=offset1;p<Math.min(offset1+4, nb);p++){
			
			if (index==p) g.setColor(Color.lightGray);
			else g.setColor(Color.gray);
			g.fillRoundRect(50, 185+120*(p-offset1), 900, 115, 25, 25);
			
			g.setColor(Color.black);
			g.drawString((p+1)+"/"+nb, 15, 245+120*(p-offset1));
        	g.drawString(main.script.ageList.get(main.script.currentAge).history.get(p).getImages(), 80, 200+120*(p-offset1));
        	g.drawString(main.script.ageList.get(main.script.currentAge).history.get(p).getPath(), 80, 215+120*(p-offset1));
        	g.drawString(main.script.ageList.get(main.script.currentAge).history.get(p).getAttractors(), 80, 230+120*(p-offset1));
        	g.drawString(main.script.ageList.get(main.script.currentAge).history.get(p).getAreas(), 80, 245+120*(p-offset1));
        	g.drawString(main.script.ageList.get(main.script.currentAge).history.get(p).getSources(), 80, 260+120*(p-offset1));
        	g.drawString(main.script.ageList.get(main.script.currentAge).history.get(p).getCondition(), 80, 275+120*(p-offset1));
        	g.drawString(main.script.ageList.get(main.script.currentAge).history.get(p).getDoor(), 80, 290+120*(p-offset1));
        }
		
		
		
	}
}
