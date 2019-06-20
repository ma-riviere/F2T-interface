package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import main.Main;

public class DisplayWorldPanel  extends JPanel implements MouseListener{

	private static final long serialVersionUID = 1L;

	private Main main;
	
	private int selectedAge=0;
	
	public DisplayWorldPanel(Main m){
		
		main=m;
		
		addMouseListener(this);
		
	}
	
	public void paintComponent(Graphics g){
		
		g.setColor(Color.white);
		g.fillRect(0, 0, 1000, 700);
		g.setColor(Color.black);
		g.drawRect(0, 0, 999, 699);
		
		g.drawRect(10, 10, 980, 155);
		
		
		
		g.setColor(Color.lightGray);
		g.fillRoundRect(50, 40, 900, 115, 25, 25);
		
		if (selectedAge<main.script.ageList.size()){
			g.setColor(Color.black);
			g.drawString("Age : "+main.script.ageList.get(selectedAge).name+" , period "
						+(main.script.ageList.get(selectedAge).current_period+1)+"/"+main.script.ageList.get(selectedAge).history.size(), 30, 30);
	    	g.drawString(main.script.ageList.get(selectedAge).getImages(), 80, 60);
	    	g.drawString(main.script.ageList.get(selectedAge).getPath(), 80, 75);
	    	g.drawString(main.script.ageList.get(selectedAge).getAttractors(), 80, 90);
	    	g.drawString(main.script.ageList.get(selectedAge).getAreas(), 80,105);
	    	g.drawString(main.script.ageList.get(selectedAge).getSources(), 80, 120);
	    	g.drawString(main.script.ageList.get(selectedAge).getCondition(), 80, 135);
	    	g.drawString(main.script.ageList.get(selectedAge).getDoor(), 80, 150);
		}
		
    	g.drawRect(10, 175, 980, 500);

    	
    	for (int a=0;a<main.script.ageList.size();a++){
    		
    		for (int c=0;c<main.script.ageList.get(a).connections.size();c++){
    			
    			int index=main.script.ageList.get(a).connections.get(c);
    			
    			g.drawLine((int)main.script.ageList.get(a).px+10,
    					   (int)main.script.ageList.get(a).py+175,
    					   (int)main.script.ageList.get(index).px+10,
    					   (int)main.script.ageList.get(index).py+175);
    			
    		}
    		
    	}
    	
    	
    	String name;
    	int width;
		
    	for (int a=0;a<main.script.ageList.size();a++){
    		
    		if (a==selectedAge) g.setColor(Color.lightGray);
    		else g.setColor(Color.gray);
    		
    		g.fillOval(10-50+(int)main.script.ageList.get(a).px, 175-50+(int)main.script.ageList.get(a).py,100,100);
    		g.setColor(Color.black);
    		g.drawOval(10-50+(int)main.script.ageList.get(a).px, 175-50+(int)main.script.ageList.get(a).py,100,100);
    		
    		name=main.script.ageList.get(a).shortName();
    		width= g.getFontMetrics().stringWidth(name);
    		
    		g.drawString(name, 10-width/2+(int)main.script.ageList.get(a).px, 175+5+(int)main.script.ageList.get(a).py);
    	}
		
		
	}

	public void mouseClicked(MouseEvent e) {

		
	}

	public void mousePressed(MouseEvent e) {
		float x=e.getX()-10;
		float y=e.getY()-175;
		
		float dmin=2500;
		int imin=-1;
		
		float d=0;
		
		for (int a=0;a<main.script.ageList.size();a++){
			d=(main.script.ageList.get(a).px-x)*(main.script.ageList.get(a).px-x) + (main.script.ageList.get(a).py-y)*(main.script.ageList.get(a).py-y);
			if (d<dmin){
				imin=a;
				dmin=d;
			}
		}
		
		if (imin>=0) selectedAge=imin;
		
	}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}
