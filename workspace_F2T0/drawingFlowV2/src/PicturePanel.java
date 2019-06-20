
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


import javax.swing.JPanel;


/**
 * Generic class of panel that can be exported as pdf file and jpeg image
 * @author simon
 */
public class PicturePanel extends JPanel implements MouseListener, MouseMotionListener{

	private static final long serialVersionUID = 1L;
	
	protected Main main;
	
	public boolean click=false;
	
	public boolean click_right=false;
	public boolean click_left=false;

	private boolean saveFlow_pressed=false;
	private boolean saveRail_pressed=false;
	
	private boolean write=false;
	
	public int selected_point=0;
	
	public boolean point_capture=false;
	
	public boolean histogram_capture=false;

	private boolean slider_length=false;
	public boolean slider_width1=false;
	public boolean slider_width2=false;
	public boolean slider_angle=false;
	public boolean slider_dispersion=false;
	
	public int ex=0;
	public int ey=0;
	
	
	public PicturePanel(Main m){
		main=m;
		
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	
	public void paintComponent(Graphics g){
		g.setColor(Color.white);
		g.fillRect(0, 0, 1600, 800);
		
		
	    // main display
		if (main.picture!=null){
			g.drawImage(main.picture_img, 0, 0, this);
		}
		else{
			g.setColor(Color.black);
			g.drawRect(0, 0, 700, 700);
		}
		
		// vector flow
		g.setColor(Color.green);
		for (int i=0;i<700;i+=10){
			for (int j=0;j<700;j+=10){
				g.drawLine(i, j, i+(int)(main.flow[i][j][0]*20), j+(int)(main.flow[i][j][1]*20));
			}
		}
		

		// flow lines
		for (int l=0;l<10;l++){
			g.setColor(Color.gray);
			for (int i=0;i<main.line[l].sizeTarget();i++){
				if (main.selectedLine==l){
					g.setColor(Color.blue);
					g.drawLine(main.line[l].targetList.get(i).x+main.line[l].targetList.get(i).p1x, main.line[l].targetList.get(i).y+main.line[l].targetList.get(i).p1y,
							   main.line[l].targetList.get(i).x+main.line[l].targetList.get(i).p2x, main.line[l].targetList.get(i).y+main.line[l].targetList.get(i).p2y);
					g.fillOval(main.line[l].targetList.get(i).x+main.line[l].targetList.get(i).p1x-2, main.line[l].targetList.get(i).y+main.line[l].targetList.get(i).p1y-2, 5, 5);
				}
				
				if (main.selectedLine==l) g.setColor(Color.red);
				g.fillOval(main.line[l].targetList.get(i).x-2, main.line[l].targetList.get(i).y-2, 5, 5);
				
				if (selected_point==i && main.selectedLine==l){
					g.setColor(Color.magenta);
					g.drawOval(main.line[l].targetList.get(i).x-7, main.line[l].targetList.get(i).y-7, 16, 16);
				}
			}

			if (main.selectedLine==l) g.setColor(Color.blue);
			for (int i=0;i<main.line[l].sizePoint()-1;i++){
				g.drawLine((int)main.line[l].pointList.get(i  ).x, (int)main.line[l].pointList.get(i  ).y,
						   (int)main.line[l].pointList.get(i+1).x, (int)main.line[l].pointList.get(i+1).y);
			}
			
			if (main.selectedLine==l){
				g.setColor(Color.red);
				for (int i=0;i<main.line[l].sizePoint()-1;i++){
					g.drawLine((int)main.line[l].pointList.get(i).x, (int)main.line[l].pointList.get(i).y,
							   (int)(main.line[l].pointList.get(i).x+main.line[l].pointList.get(i).vx*10), (int)(main.line[l].pointList.get(i).y+main.line[l].pointList.get(i).vy*10));
				}
			}
		}
		
		// cursor
		if (ex>=0 && ex<700 && ey<700){
			g.drawOval(ex-main.width2,ey-main.width2,main.width2*2,main.width2*2);
			g.drawOval(ex-(int)(main.width2*main.width1/100),ey-(int)(main.width2*main.width1/100),
					      (int)(main.width2*main.width1/100*2),(int)(main.width2*main.width1/100*2));
		}
		
		
		// histogram
		g.setColor(Color.black);
		g.drawRect(750, 10, 400, 220);
		g.drawLine(950, 30, 950, 230);
		
		g.drawLine(950-main.width2, 230, 950-(int)(main.width2*main.width1/100), 230-(int)(2*main.length));
		g.drawLine(950+main.width2, 230, 950+(int)(main.width2*main.width1/100), 230-(int)(2*main.length));
		g.drawLine(950-(int)(main.width2*main.width1/100), 230-(int)(2*main.length), 950+(int)(main.width2*main.width1/100), 230-(int)(2*main.length));
		
		
		// sliders
		g.setColor(Color.lightGray);
		g.fillRect(1170, 10, 22, 220);
		g.fillRect(750, 250, 400, 22);
		g.fillRect(750, 280, 400, 22);
		g.fillRect(750, 310, 400, 22);
		g.fillRect(1200, 10, 22, 220);
		g.setColor(Color.blue);
		g.fillRect(1172, 230-main.length*2, 18, main.length*2);
		g.fillRect(750, 252, (int)(main.width1*4), 18);
		g.fillRect(750, 282, (int)(main.width2*2), 18);
		g.fillRect(750+(int)(2.22*(main.angle+90))-1, 312, 3, 18);
		g.fillRect(1202,10+(int)(1.22*(main.dispersion+90))-1, 18, 3);

		
		for (int i=0;i<10;i++){
			if (main.picturePanel!=null && main.picturePanel.slot!=null){
			if (main.line[i].targetList.isEmpty()) main.picturePanel.slot[i].setText((i+1)+": empty");
			else main.picturePanel.slot[i].setText((i+1)+": "+main.line[i].targetList.size()+ " pts");
			}
		}
	}


	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}



	public void mousePressed(MouseEvent e) {
		ex=e.getX();
		ey=e.getY();

		// detect clicked button
		if (!click_left && !click_right){
			if (e.getButton()==MouseEvent.BUTTON3){
				click_right=true;
			}
			else if (e.getButton()==MouseEvent.BUTTON1){
				click_left=true;
			}
		}
		
		
		if (click_left){
			
			// add new target point
			if (ex>=0 && ex<700 && ey>=0 && ey<700){
				main.line[main.selectedLine].targetList.add(new Target(ex,ey, (int)(main.width2*main.width1/100), main.width2, main.length));
				selected_point=main.line[main.selectedLine].targetList.size()-1;
				write=true;
			}
			
			// sliders
			if (ex>=1170 && ex<=1192 && ey>=10 && ey<=230){
				slider_length=true;
			}
			if (ex>=750 && ex<=1150 && ey>=250 && ey<=272){
				slider_width1=true;
			}
			if (ex>=750 && ex<=1150 && ey>=280 && ey<=302){
				slider_width2=true;
			}
			if (ex>=750 && ex<=1150 && ey>=310 && ey<=332){
				slider_angle=true;
			}
			if (ex>=1200 && ex<=1222 && ey>=10 && ey<=230){
				slider_dispersion=true;
			}

			// histogram
			if (ex>=750 && ex<1150 && ey>=10 && ey<220){
				histogram_capture=true;
			}
		}
		
		
		
		if (click_right){
			
			// selection of a target point
			if (ex>0 && ex<700 && ey>0 && ey<700){
				double min=10;
				int imin=-1;
				double d=0;
				
				for (int i=0;i<main.line[main.selectedLine].sizeTarget();i++){
					d=Math.sqrt( (ex-(main.line[main.selectedLine].targetList.get(i).x))*(ex-(main.line[main.selectedLine].targetList.get(i).x)) 
							   + (ey-(main.line[main.selectedLine].targetList.get(i).y))*(ey-(main.line[main.selectedLine].targetList.get(i).y)));
					if (d<min){
						min=d;
						imin=i;
					}
				}
				
				if (imin>=0){
					selected_point=imin;

					if (e.isControlDown()){
						main.line[main.selectedLine].targetList.get(selected_point).resetBezier();
						write=true;
					}
					else{
						point_capture=true;
					}

					this.repaint();
				}
			}
			
			// histogram
			if (ex>=750 && ex<1150 && ey>=10 && ey<220){
				histogram_capture=true;
			}
			
			// reset sliders values
			if (ex>=750 && ex<=1150 && ey>=310 && ey<=332){
				main.angle=0;
				if (selected_point>=0 && selected_point<main.line[main.selectedLine].sizeTarget()){
					main.line[main.selectedLine].targetList.get(selected_point).angle=0;
					write=true;
				}	
			}
			if (ex>=1200 && ex<=1222 && ey>=10 && ey<=230){
				main.dispersion=0;
				if (selected_point>=0 && selected_point<main.line[main.selectedLine].sizeTarget()){
					main.line[main.selectedLine].targetList.get(selected_point).dispersion=0;
					write=true;
				}	
			}
		}
		
		this.repaint();
	}



	public void mouseReleased(MouseEvent e) {
		
		if (e.getButton()==MouseEvent.BUTTON3){
			click_right=false;
		}
		if (e.getButton()==MouseEvent.BUTTON1){
			click_left=false;
		}
		
		point_capture=false;
		histogram_capture=false;
		
		saveFlow_pressed=false;
		saveRail_pressed=false;
		
		slider_length=false;
		slider_width1=false;
		slider_width2=false;
		slider_angle=false;
		slider_dispersion=false;
		
		if (write) main.pushBufferLine();
		write=false;
		
		main.update();
	}



	public void mouseDragged(MouseEvent e) {
		ex=e.getX();
		ey=e.getY();
		
		if (click_right && point_capture && !e.isShiftDown()){
			// move target point
			if (ex>=0 && ex<700 && ey>=0 && ey<700){
				if (main.line[main.selectedLine].targetList.get(selected_point).x!=ex || main.line[main.selectedLine].targetList.get(selected_point).y!=ey) write=true;
				main.line[main.selectedLine].targetList.get(selected_point).x=ex;
				main.line[main.selectedLine].targetList.get(selected_point).y=ey;
				main.line[main.selectedLine].update();
			}
		}
		
		// speed and range2 histogram drag
		else if (click_left && histogram_capture){
			main.length=Math.min(100, Math.max(0,(220-e.getY()+10)/2));
			main.width2=Math.min(200, Math.max(0,Math.abs((e.getX()-950))));
			
			if (selected_point>=0 && selected_point<main.line[main.selectedLine].sizeTarget()){
				main.line[main.selectedLine].targetList.get(selected_point).speed=main.length;
				main.line[main.selectedLine].targetList.get(selected_point).range2=main.width2;
				main.line[main.selectedLine].targetList.get(selected_point).range1=(int)main.width1;

				write=true;
			}
			this.repaint();
		}
		
		// speed and range1 histogram drag
		else if (click_right && histogram_capture){
			main.length=Math.min(100, Math.max(0,(220-e.getY()+10)/2));
			main.width1=Math.min(100, Math.max(0,(float)Math.abs((e.getX()-950)/2)));
			
			if (selected_point>=0 && selected_point<main.line[main.selectedLine].sizeTarget()){
				main.line[main.selectedLine].targetList.get(selected_point).speed=main.length;
				main.line[main.selectedLine].targetList.get(selected_point).range2=main.width2;
				main.line[main.selectedLine].targetList.get(selected_point).range1=(int)main.width1;

				write=true;
			}
			
			this.repaint();
		}
		
		// speed slider
		if (slider_length){
			main.length=Math.min(100, Math.max(0,(230-e.getY())/2));
			if (selected_point>=0 && selected_point<main.line[main.selectedLine].sizeTarget()){
				main.line[main.selectedLine].targetList.get(selected_point).speed=main.length;
				write=true;
			}	
		}
		// range1 slider
		if (slider_width1){
			main.width1=Math.min(100, Math.max(0,(e.getX()-750)/4));
			if (selected_point>=0 && selected_point<main.line[main.selectedLine].sizeTarget()){
				main.line[main.selectedLine].targetList.get(selected_point).range1=(int) main.width1;
				write=true;
			}	
		}
		// range2 slider
		if (slider_width2){
			main.width2=Math.min(200, Math.max(0,(e.getX()-750)/2));
			if (selected_point>=0 && selected_point<main.line[main.selectedLine].sizeTarget()){
				main.line[main.selectedLine].targetList.get(selected_point).range2=(int) main.width2;
				write=true;
			}	
		}
		// angle slider
		if (slider_angle){
			main.angle=(float)Math.min(90, Math.max(-90,(e.getX()-950)*0.45));
			if (selected_point>=0 && selected_point<main.line[main.selectedLine].sizeTarget()){
				main.line[main.selectedLine].targetList.get(selected_point).angle=(int) main.angle;
				write=true;
			}	
		}
		// dispersion slider
		if (slider_dispersion){
			main.dispersion=(float)Math.min(90, Math.max(-90,(e.getY()-120)/1.22));
			if (selected_point>=0 && selected_point<main.line[main.selectedLine].sizeTarget()){
				main.line[main.selectedLine].targetList.get(selected_point).dispersion=main.dispersion;
				write=true;
			}	
		}
		
		// move target bezier controller
		else if (click_right && selected_point>=0 && selected_point<main.line[main.selectedLine].sizeTarget() && e.isShiftDown()){
			main.line[main.selectedLine].changeBezier(selected_point, e.getX(), e.getY());
			write=true;
		}
		
		this.repaint();
	}



	public void mouseMoved(MouseEvent e) {

		ex=e.getX();
		ey=e.getY();

		this.repaint();
	}
}