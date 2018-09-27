package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import main.Main;
import main.Target;


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

		// draw target sequence
		if (main.currentAge.targetSequence.size()>0){
			if (main.currentAge.targetSequence.get(0).control==0) g.setColor(Color.yellow);
			else g.setColor(Color.magenta);
			g.fillOval(348+(int)main.currentAge.targetSequence.get(0).x, 268-(int)main.currentAge.targetSequence.get(0).y, 5, 5);
			for (int i=1;i<main.currentAge.targetSequence.size();i++){
				if (main.currentAge.targetSequence.get(i).control==0) g.setColor(Color.yellow);
				else g.setColor(Color.magenta);
				g.drawLine(350+(int)main.currentAge.targetSequence.get(i-1).x, 270-(int)main.currentAge.targetSequence.get(i-1).y,
						   350+(int)main.currentAge.targetSequence.get(i  ).x, 270-(int)main.currentAge.targetSequence.get(i  ).y);
				g.fillOval(349+(int)main.currentAge.targetSequence.get(i).x, 269-(int)main.currentAge.targetSequence.get(i).y, 3, 3);
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
		main.currentAge.targetSequence.add(new Target(-350+(int)e.getX(), 270-(int)e.getY(),main.target_speed, main.target_type));
	}

	public void mouseReleased(MouseEvent arg0) {}
}