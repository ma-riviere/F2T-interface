package display;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
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
	
	
	private JPanel image_view;
	private ImageIcon image_icon;
	
	private JPanel tactile_view;
	private ImageIcon tactile_icon;
	
	private JPanel flow_view;
	private ImageIcon flow_icon;
	
	private JPanel rail_view;
	private ImageIcon rail_icon;
	
	private JPanel area_view;
	private ImageIcon area_icon;
	
	private JPanel magnetic_view;
	private ImageIcon magnetic_icon;

	
	public DisplayMiniaturesPanel(Main m, DisplayFrame f){
		main=m;
		frame=f;
		addMouseListener(this);
		this.setLayout(null);
		
		image_view=new JPanel();
		image_view.setBackground(Color.lightGray);
		image_view.setVisible(true);
		image_view.setBounds(0, 0, 175, 175);
		this.add(image_view);
		
		tactile_view=new JPanel();
		tactile_view.setBackground(Color.lightGray);
		tactile_view.setVisible(true);
		tactile_view.setBounds(180, 0, 175, 175);
		this.add(tactile_view);
		
		
		flow_view=new JPanel();
		flow_view.setBackground(Color.lightGray);
		flow_view.setVisible(true);
		flow_view.setBounds(0,180, 175, 175);
		this.add(flow_view);
		
		rail_view=new JPanel();
		rail_view.setBackground(Color.lightGray);
		rail_view.setVisible(true);
		rail_view.setBounds(180, 180, 175, 175);
		this.add(rail_view);
		
		area_view=new JPanel();
		area_view.setBackground(Color.lightGray);
		area_view.setVisible(true);
		area_view.setBounds(0, 360, 175, 175);
		this.add(area_view);
		
		magnetic_view=new JPanel();
		magnetic_view.setBackground(Color.lightGray);
		magnetic_view.setVisible(true);
		magnetic_view.setBounds(180, 360, 175, 175);
		this.add(magnetic_view);
		
	}
	
	
	
	
	public void paintComponent(Graphics g){
		g.setColor(this.getBackground());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

	}

	

	public void update(){

		image_view.removeAll();
		image_icon=null;
		if (main.currentAge.image.view_img_miniature!=null){
			image_icon=new ImageIcon(main.currentAge.image.view_img_miniature);
			image_view.add(new JLabel(image_icon));
			image_view.setVisible(false);
			image_view.setVisible(true);
		}
		
		tactile_view.removeAll();
		tactile_icon=null;
		if (main.currentAge.image.tactile_img_miniature!=null){
			tactile_icon=new ImageIcon(main.currentAge.image.tactile_img_miniature);
			tactile_view.add(new JLabel(tactile_icon));
			tactile_view.setVisible(false);
			tactile_view.setVisible(true);
		}
		
		flow_view.removeAll();
		flow_icon=null;
		if (main.currentAge.image.flow_img_miniature!=null){
			flow_icon=new ImageIcon(main.currentAge.image.flow_img_miniature);
			flow_view.add(new JLabel(flow_icon));
			flow_view.setVisible(false);
			flow_view.setVisible(true);
		}
		
		rail_view.removeAll();
		rail_icon=null;
		if (main.currentAge.image.rail_img_miniature!=null){
			rail_icon=new ImageIcon(main.currentAge.image.rail_img_miniature);
			rail_view.add(new JLabel(rail_icon));
			rail_view.setVisible(false);
			rail_view.setVisible(true);
		}	
		
		area_view.removeAll();
		area_icon=null;
		if (main.currentAge.image.area_img_miniature!=null){
			area_icon=new ImageIcon(main.currentAge.image.area_img_miniature);
			area_view.add(new JLabel(area_icon));
			area_view.setVisible(false);
			area_view.setVisible(true);
		}
		
		magnetic_view.removeAll();
		magnetic_icon=null;
		if (main.currentAge.image.magnetic_img_miniature!=null){
			magnetic_icon=new ImageIcon(main.currentAge.image.magnetic_img_miniature);
			magnetic_view.add(new JLabel(magnetic_icon));
			magnetic_view.setVisible(false);
			magnetic_view.setVisible(true);
		}
	}
	
	

	public void mouseClicked(MouseEvent e) {

		int x=e.getX();
		int y=e.getY();
		
		if (x>=10 && x<185  && y>=0   && y<175) frame.selected_image=0;
		if (x>=190 && x<365 && y>=0   && y<175) frame.selected_image=1;
		if (x>=10 && x<185  && y>=180 && y<355) frame.selected_image=2;
		if (x>=190 && x<365 && y>=180 && y<355) frame.selected_image=3;
		if (x>=10 && x<185  && y>=360 && y<535) frame.selected_image=4;
		if (x>=200 && x<355 && y>=380 && y<500) frame.selected_image=5;
	}
	
	
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}