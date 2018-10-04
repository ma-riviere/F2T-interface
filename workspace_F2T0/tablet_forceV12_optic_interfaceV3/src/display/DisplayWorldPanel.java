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
			g.drawString("Age : "+main.script.ageList.get(selectedAge).name, 30, 30);
	    	g.drawString(main.script.ageList.get(selectedAge).getImages(), 80, 60);
	    	g.drawString(main.script.ageList.get(selectedAge).getPath(), 80, 75);
	    	g.drawString(main.script.ageList.get(selectedAge).getAreas(), 80, 90);
	    	g.drawString(main.script.ageList.get(selectedAge).getSources(), 80, 105);
	    	g.drawString(main.script.ageList.get(selectedAge).getCondition(), 80, 120);
	    	g.drawString(main.script.ageList.get(selectedAge).getDoor(), 80, 135);
		}
		
    	g.drawRect(10, 175, 980, 500);

		
    	for (int a=0;a<main.script.ageList.size();a++){
    		
    		if (a==main.script.currentAge) g.setColor(Color.lightGray);
    		else g.setColor(Color.gray);
    		
    		g.fillOval(350-50+main.script.ageList.get(a).px, 350-50+main.script.ageList.get(a).py,100,100);
    		g.setColor(Color.black);
    		g.drawOval(350-50+main.script.ageList.get(a).px, 350-50+main.script.ageList.get(a).py,100,100);
    		g.drawString(main.script.ageList.get(a).name, 350-40+main.script.ageList.get(a).px, 355+main.script.ageList.get(a).py);
    	}
		
		
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}
