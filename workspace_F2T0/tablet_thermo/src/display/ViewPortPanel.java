package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import main.Main;


public class ViewPortPanel extends JPanel implements MouseListener{

	private static final long serialVersionUID = 1L;
	
	protected Main main;
	
	private BufferedImage image;
	
	public ViewPortPanel(Main m, BufferedImage img){
		main=m;
		image=img;
		addMouseListener(this);
	}
	
	public void setImage(BufferedImage img){
		image=img;
	}
	
	public void paintComponent(Graphics g){

		// draw main image
		g.drawImage(image, 0, 0, this);
		
		// draw trace
		g.setColor(Color.cyan);
		for (int t=0;t<Main.LENGTH-1;t++){
			if (t+1!=main.time){
			g.drawLine(348+(int)main.trace[t][0], 268-(int)main.trace[t][1],
					   348+(int)main.trace[t+1][0], 268-(int)main.trace[t+1][1]);
			}
		}

		
		// draw joystick position
		g.setColor(Color.white);
		g.drawOval(325+(int)main.x, 245-(int)main.y, 50, 50);
	}

	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	
	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent arg0) {}
}