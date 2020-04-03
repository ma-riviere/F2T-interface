package developmentDisplay;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import main.Image;
import main.Main;
import modules.Edges;



/**
 * Display the probe input
 * @author simon gay
 */

/* - inherited from EnvPanel :
 *   Agent agent      : pointer to the agent
 */
public class TouchPanel extends JPanel implements MouseListener, MouseMotionListener{
	
	private static final long serialVersionUID = 1L;

	public Main main;
	
	public int p1x=0;
	public int p2x=0;
	public int p1y=0;
	public int p2y=0;
	
	public TouchPanel(Main m){
		main=m;
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void paintComponent(Graphics g){
		
		
		g.setColor(Color.black);
		g.fillRect(0, 0, 1300, 700);
		
		/*for (int i=0;i<Edges.SIZE2;i++){
			for (int j=0;j<Edges.SIZE2;j++){
				
				float val=(main.edges.sphere[i][j]);
				if (val<0) val=0;
				if (val>1) val=1;
				
				g.setColor(new Color(val, 0,1-val));
				g.drawRect(20+i*10, 20+j*10, 9, 9);
				
				val=main.edges.heighMap[i][j];
				g.setColor(new Color(0, val,0));
				g.fillRect(21+i*10, 21+j*10, 8, 8);
				
				val=main.edges.contactMap[i][j]/2;
				if (val<0) val=0;
				if (val>1) val=1;
				g.setColor(new Color(val, 1-val,0));
				g.fillRect(620+i*10, 20+j*10, 10, 10);
				
			}
		}
		
		g.setColor(Color.blue);
		for (int i=0;i<main.edges.contactsX.size();i++){
			g.fillOval(620+(main.edges.contactsX.get(i)+Edges.SIZE1)*10, 20+(main.edges.contactsY.get(i)+Edges.SIZE1)*10, 10, 10);
		}
	
		g.setColor(new Color(0, 1, 0));
		g.fillOval(620+(int)((main.edges.contactX*12)+Edges.SIZE1)*10, 20+(int)((main.edges.contactY*12)+Edges.SIZE1)*10, 10, 10);
		
		g.setColor(Color.gray);
		g.fillRect(550, 20, 10, 500);
		g.setColor(Color.green);
		g.fillRect(540, 15+500-(int)(main.edges.contactHeight*500), 30, 5);*/
		
		int val1=0;
		int val2=0;
		for (int i=0;i<Image.SIZE;i++){
			for (int j=0;j<Image.SIZE;j++){
				//val=(int)(main.edges.map[i][j]*255);
				val1=(int)(main.currentAge.image.gradient[i][j][0]*127)+127;
				val2=(int)(main.currentAge.image.gradient[i][j][1]*127)+127;
				if (val1>255) val1=255;
				if (val1<0) val1=0;
				if (val2>255) val2=255;
				if (val2<0) val2=0;
				g.setColor(new Color(val1,val2,0));
				
				//if (main.edges.grad[i][j]>=0.15) g.setColor(Color.red);
				g.drawLine(i, 699-j, i+1, 699-j);
			}
		}
		
		//g.setColor(Color.red);
		//g.drawLine(p1x, p1y, p2x, p2y);
		
	}

	public void mouseClicked(MouseEvent e) {

	}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseMoved(MouseEvent e) {
		int ex=e.getX();
		int ey=e.getY();
		
		if (ex>0 && ex<699 && ey>0 && ey<699){
			
			
			
			float dist=(float)Math.sqrt(main.currentAge.image.gradient[ex][699-ey][0]*main.currentAge.image.gradient[ex][699-ey][0]
								     +main.currentAge.image.gradient[ex][699-ey][1]*main.currentAge.image.gradient[ex][699-ey][1]);
			
			System.out.println("### "+((int)(100*main.currentAge.image.gradient[ex][699-ey][0]))+" ; "
			                         +((int)(100*main.currentAge.image.gradient[ex][699-ey][1]))+" ; "
			                         +(dist*100) );
			
			p1x=ex;
			p1y=ey;
			
			p2x=ex+(int)(main.currentAge.image.gradient[ex][ey][0]*100);
			p2y=ey+(int)(main.currentAge.image.gradient[ex][ey][1]*100);
		}
		
		//this.repaint();
		
		
		
	}
}
