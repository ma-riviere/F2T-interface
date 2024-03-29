package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import main.Main;
import main.SoundSource;


public class SourcePanel extends JPanel implements MouseListener{

	private static final long serialVersionUID = 1L;
	
	protected Main main;
	
	private Mat webcam;
	
	public SourcePanel(Main m){
		main=m;
		webcam=new Mat();
		addMouseListener(this);
	}

	
	public void paintComponent(Graphics g){

		g.setColor(Color.white);
		g.fillRect(0, 0, 1500, 1500);
		
		
		/// draw image camera
		if (Main.CAMERA_CONNECTED){
			Graphics2D g2d = (Graphics2D) g.create();
			AffineTransform at = new AffineTransform();
	        
			Imgproc.resize( main.webcam, webcam, new Size(320,240) );
			
			at.setToRotation(Math.PI, webcam.width()/2, webcam.height()/2);
	        
	        g2d.setTransform(at);
	        Image image = Main.Mat2bufferedImage(webcam);
	        g2d.drawImage(image, -350, -250, this);
		}
		
		// draw joystick position
		g.setColor(Color.gray);
 		g.drawOval(530+(int)(main.x_prev*0.25), 390-(int)(main.y_prev*0.25), 25, 25);
 		g.setColor(Color.white);
 		g.drawOval(530+(int)(main.x*0.25), 390-(int)(main.y*0.25), 25, 25);
 		g.drawLine(542+(int)(main.x*0.25), 402-(int)(main.y*0.25), 542+(int)(main.x*0.25)+(int)main.dx*2, 402-(int)(main.y*0.25)-(int)main.dy*2);
 		g.setColor(Color.red);
 		g.drawOval(530+(int)(main.x_next*0.25), 390-(int)(main.y_next*0.25), 25, 25);
 		
 		
 		// draw sound sources
 		g.setColor(Color.red);
 		for (int i=0;i<main.currentAge.sourceList.size();i++){
			g.fillOval(542+(int)(main.currentAge.sourceList.get(i).px*0.25), 402-(int)(main.currentAge.sourceList.get(i).py*0.25), 5, 5);
			g.drawString(""+i+": ("+(int)main.currentAge.sourceList.get(i).rx+", "+(int)main.currentAge.sourceList.get(i).ry+")",
					     542+(int)(main.currentAge.sourceList.get(i).px*0.25) ,402-(int)(main.currentAge.sourceList.get(i).py*0.25) );
		}
 		
	}

	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	
	public void mousePressed(MouseEvent e) {
		
		int x=(int)(e.getX()-542)*4;
		int y=(int)(-e.getY()+402)*4;

		boolean found=false;
		
		for (int i=0;i<main.currentAge.sourceList.size();i++){
			if ( (main.currentAge.sourceList.get(i).px-x)*(main.currentAge.sourceList.get(i).px-x) + (main.currentAge.sourceList.get(i).py-y)*(main.currentAge.sourceList.get(i).py-y)<1600){
				main.currentAge.sourceList.remove(i);
				i--;
				found=true;
			}
		}
		
		
		if (!found) main.currentAge.sourceList.add(new SoundSource(x, y ));
	}

	public void mouseReleased(MouseEvent arg0) {}
}