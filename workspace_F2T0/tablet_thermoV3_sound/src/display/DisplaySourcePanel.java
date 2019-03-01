package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;


import main.Main;
import main.SoundSource;

public class DisplaySourcePanel extends JPanel implements MouseListener , MouseMotionListener{

	private static final long serialVersionUID = 1L;
	
	protected Main main;
	public DisplayFrame frame;
	
	public DisplaySourcePanel(Main m, DisplayFrame f){
		main=m;
		frame=f;
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void paintComponent(Graphics g){
		
		g.setColor(Color.white);
		g.fillRect(0, 0, 800, 800);
		
		g.setColor(Color.black);
		g.drawRect(0, 0, 799, 799);
		
		
		if (frame.selected_image==0){
			if (main.script.ageList.get(main.script.currentAge).image.view!=null) g.drawImage(main.script.ageList.get(main.script.currentAge).image.view_img_miniature, 300, 300, this);
			else{
				g.setColor(Color.gray);
				g.fillRect(300, 300, 200, 200);
				g.setColor(Color.black);
				g.drawRect(300, 300, 200, 200);
				g.drawLine(300, 400, 500, 400);
				g.drawLine(400, 300, 400, 500);
			}
		}
		
		else if (frame.selected_image==1){
			if (main.script.ageList.get(main.script.currentAge).image.tactile!=null) g.drawImage(main.script.ageList.get(main.script.currentAge).image.tactile_img_miniature, 300, 300, this);
			else{
				g.setColor(Color.gray);
				g.fillRect(300, 300, 200, 200);
				g.setColor(Color.black);
				g.drawRect(300, 300, 200, 200);
				g.drawLine(300, 400, 500, 400);
				g.drawLine(400, 300, 400, 500);
			}
		}
		
		else if (frame.selected_image==2){
			if (main.script.ageList.get(main.script.currentAge).image.area!=null) g.drawImage(main.script.ageList.get(main.script.currentAge).image.area_img_miniature, 300, 300, this);
			else{
				g.setColor(Color.gray);
				g.fillRect(300, 300, 200, 200);
				g.setColor(Color.black);
				g.drawRect(300, 300, 200, 200);
				g.drawLine(300, 400, 500, 400);
				g.drawLine(400, 300, 400, 500);
			}
		}
		else if (frame.selected_image<0){
			g.setColor(Color.gray);
			g.fillRect(300, 300, 200, 200);
			g.setColor(Color.black);
			g.drawRect(300, 300, 200, 200);
			g.drawLine(300, 400, 500, 400);
			g.drawLine(400, 300, 400, 500);
		}
		
		/////////////////////////////////////
		// draw joystick position
 		g.setColor(Color.blue);
 		g.drawOval(395+(int)(main.x/4), 395-(int)(main.y/4), 10, 10);
 		g.drawLine(400+(int)(main.x/4), 400-(int)(main.y/4),400+(int)(main.x/4)+(int)main.dx*5, 400-(int)(main.y/4)-(int)main.dy*5);
 		
 		/////////////////////////////////////
 		// draw sound sources
 		g.setColor(Color.red);
 		for (int i=0;i<main.script.ageList.get(main.script.currentAge).sourceList.size();i++){
			g.fillOval(395+(int)(main.script.ageList.get(main.script.currentAge).sourceList.get(i).px/4), 395-(int)(main.script.ageList.get(main.script.currentAge).sourceList.get(i).py/4), 5, 5);
			g.drawString(""+i+": ("+(int)main.script.ageList.get(main.script.currentAge).sourceList.get(i).rx+", "+(int)main.script.ageList.get(main.script.currentAge).sourceList.get(i).ry+")",
					     395+(int)(main.script.ageList.get(main.script.currentAge).sourceList.get(i).px/4) ,395-(int)(main.script.ageList.get(main.script.currentAge).sourceList.get(i).py/4) );
		}
		
	}


	public void mouseClicked(MouseEvent e) {
		
		int x=(int)(e.getX()-400)*4;
		int y=(int)(-e.getY()+400)*4;

		boolean found=false;
		
		for (int i=0;i<main.script.ageList.get(main.script.currentAge).sourceList.size();i++){
			if ( (main.script.ageList.get(main.script.currentAge).sourceList.get(i).px-x)*(main.script.ageList.get(main.script.currentAge).sourceList.get(i).px-x) + (main.script.ageList.get(main.script.currentAge).sourceList.get(i).py-y)*(main.script.ageList.get(main.script.currentAge).sourceList.get(i).py-y)<1600){
				main.script.ageList.get(main.script.currentAge).sourceList.get(i).close();
				main.script.ageList.get(main.script.currentAge).sourceList.remove(i);
				i--;
				found=true;
			}
		}
		
		if (!found) main.script.ageList.get(main.script.currentAge).sourceList.add(new SoundSource("s "+x+" "+y+" "+frame.getSourceParameters() ));
	}
	
	
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	public void mouseDragged(MouseEvent e) {
		if (e.getX()>10 && e.getX()<795 && e.getY()>10 && e.getY()<795){
			frame.x=e.getX()-405;
			frame.y=405-e.getY();
		}
	}

	public void mouseMoved(MouseEvent e) {
		frame.updateSourcePanel((e.getX()-400)*4, (e.getY()-400)*4);
	}
}
