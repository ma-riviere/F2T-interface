package display;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import main.Main;

/**
 * lateral miniature display panel of main mode
 * @author simon gay
 */

public class DisplayMiniaturesPanel extends JPanel implements MouseListener{
	
	private static final long serialVersionUID = 1L;
	

	public Main main;
	public DisplayFrame frame;
	

	
	public DisplayMiniaturesPanel(Main m, DisplayFrame f){
		main=m;
		frame=f;
		addMouseListener(this);
		this.setLayout(null);
		this.setBounds(0, 0, 175, 175);

	}
	
	public void paintComponent(Graphics g){
		g.setColor(this.getBackground());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		if (main.currentAge.image.view_img_miniature!=null) g.setColor(Color.red);
		else g.setColor(Color.black);
		g.fillRect(5, 5, 20, 20);
		
		if (main.currentAge.image.tactile_img_miniature!=null) g.setColor(Color.red);
		else g.setColor(Color.black);
		g.fillRect(30, 5, 20, 20);
		
		if (main.currentAge.image.flow_img_miniature!=null) g.setColor(Color.red);
		else g.setColor(Color.black);
		g.fillRect(55, 5, 20, 20);
		
		if (main.currentAge.image.rail_img_miniature!=null) g.setColor(Color.red);
		else g.setColor(Color.black);
		g.fillRect(80, 5, 20, 20);
		
		if (main.currentAge.image.area_img_miniature!=null) g.setColor(Color.red);
		else g.setColor(Color.black);
		g.fillRect(105, 5, 20, 20);
		
		if (main.currentAge.image.magnetic_img_miniature!=null) g.setColor(Color.red);
		else g.setColor(Color.black);
		g.fillRect(130, 5, 20, 20);
		
		g.setColor(Color.blue);
		g.drawRect(3+25*frame.selected_image, 3, 23, 23);
		g.drawRect(2+25*frame.selected_image, 2, 25, 25);
		
		g.setColor(Color.gray);
		g.fillOval(160, 5, 20, 20);
		g.setColor(Color.darkGray);
		g.drawOval(160, 5, 20, 20);
		g.drawOval(161, 6, 18, 18);
	}


	

	public void mouseClicked(MouseEvent e) {

		int x=e.getX();
		int y=e.getY();

		if (x>=5 && x<25  && y>=5 && y<25) frame.updateSelectedImage(0);
		if (x>=30 && x<50 && y>=5 && y<25) frame.updateSelectedImage(1);
		if (x>=55 && x<75 && y>=5 && y<25) frame.updateSelectedImage(2);
		if (x>= 80 && x<105 && y>=5 && y<25) frame.updateSelectedImage(3);
		if (x>=105 && x<125 && y>=5 && y<25) frame.updateSelectedImage(4);
		if (x>=130 && x<150 && y>=5 && y<25) frame.updateSelectedImage(5);
		
		if (x>=160 && x<180 && y>=5 && y<25) frame.keyboard.setKeyPressed(Main.button1);

		frame.repaint();
	}
	
	
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}
