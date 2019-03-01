package main;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;



public class MainPanel extends JPanel implements MouseListener{

	private static final long serialVersionUID = 1L;
	
	protected Main main;
	
	
	public MainPanel(Main m){
		main=m;
		addMouseListener(this);
	}

	
	public void paintComponent(Graphics g){

		g.setColor(Color.white);
		g.fillRect(0, 0, 1500, 1500);
		
		g.setColor(Color.blue);
		for (int i=0;i<main.jx.size();i++){
			g.fillOval(main.jx.get(i)*4-1, 800-main.jy.get(i)*4-1, 3, 3);
		}
 		
	}

	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	
	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent arg0) {}
}