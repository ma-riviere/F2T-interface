package display;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.Main;
import main.Target;



/**
 * Display the probe input
 * @author simon gay
 */

/* - inherited from EnvPanel :
 *   Agent agent      : pointer to the agent
 */
public class DisplayPanel extends JPanel implements MouseListener, ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	
	
	private static int IMAGE_X=5;
	private static int IMAGE_Y=50;
	
	private static int LISTS_X=1100;
	private static int LISTS_Y=50;
	
	private static int PRESET_X=1335;
	private static int PRESET_Y=50;
	
	private static int PATH_X=1100;
	private static int PATH_Y=380;
	

	public Main main;

	
	private JComboBox<String> list_image;
	private JComboBox<String> list_tactile;
	private JComboBox<String> list_flow;
	private JComboBox<String> list_rail;
	private JComboBox<String> list_area;
	
	private JComboBox<String> list_preset;
	private JComboBox<String> list_path;
	private JComboBox<String> list_source;
	
	private JButton preset;
	private JButton path;
	private JButton source;
	
	private JComboBox<String> list_script;
	private JButton script;
	
	private JButton rescan;
	private JButton savePreset;
	
	private JButton playPause;
	private JButton remLast;
	private JButton clearPath;
	private JButton savePath;
	
	private ButtonGroup targetMode;
	private JRadioButton controlMode;
	private JRadioButton attractorMode;
	
	private JSlider speed;
	
	
	public int display_mode=0;
	public int selected_image=-1;
	
	
	//private boolean guide_mode=false;
	
	public DisplayPanel(Main m){
		main=m;
		addMouseListener(this);
		
		this.setLayout(null);
		
		
		
		list_image=new JComboBox<String>();
		list_image.addItem("none");
		for (int i=0;i<main.listImages.length;i++) list_image.addItem(main.listImages[i]);
		list_image.addActionListener(this);
		this.add(list_image);
		list_image.setBounds(LISTS_X+70, LISTS_Y+5, 150, 30);
		
		list_tactile=new JComboBox<String>();
		list_tactile.addItem("none");
		for (int i=0;i<main.listTactile.length;i++) list_tactile.addItem(main.listTactile[i]);
		list_tactile.addActionListener(this);
		this.add(list_tactile);
		list_tactile.setBounds(LISTS_X+70, LISTS_Y+35, 150, 30);
		
		list_flow=new JComboBox<String>();
		list_flow.addItem("none");
		for (int i=0;i<main.listFlow.length;i++) list_flow.addItem(main.listFlow[i]);
		list_flow.addActionListener(this);
		this.add(list_flow);
		list_flow.setBounds(LISTS_X+70,LISTS_Y+65, 150, 30);
		
		list_rail=new JComboBox<String>();
		list_rail.addItem("none");
		for (int i=0;i<main.listRail.length;i++) list_rail.addItem(main.listRail[i]);
		list_rail.addActionListener(this);
		this.add(list_rail);
		list_rail.setBounds(LISTS_X+70,LISTS_Y+95, 150, 30);
		
		list_area=new JComboBox<String>();
		list_area.addItem("none");
		for (int i=0;i<main.listArea.length;i++) list_area.addItem(main.listArea[i]);
		list_area.addActionListener(this);
		this.add(list_area);
		list_area.setBounds(LISTS_X+70,LISTS_Y+125, 150, 30);
		
		
		list_preset=new JComboBox<String>();
		for (int i=0;i<main.listPreset.length;i++) list_preset.addItem(main.listPreset[i]);
		list_preset.addActionListener(this);
		this.add(list_preset);
		list_preset.setBounds(LISTS_X+70,LISTS_Y+175, 170, 30);
		
		list_path=new JComboBox<String>();
		for (int i=0;i<main.listPath.length;i++) list_path.addItem(main.listPath[i]);
		list_path.addActionListener(this);
		this.add(list_path);
		list_path.setBounds(LISTS_X+70,LISTS_Y+205, 170, 30);
		
		list_source=new JComboBox<String>();
		for (int i=0;i<main.listSource.length;i++) list_source.addItem(main.listSource[i]);
		list_source.addActionListener(this);
		this.add(list_source);
		list_source.setBounds(LISTS_X+70,LISTS_Y+235, 170, 30);
		
		preset=new JButton("Load");
		preset.addActionListener(this);
		this.add(preset);
		preset.setBounds(LISTS_X+265, LISTS_Y+175, 80, 30);
		
		path=new JButton("Load");
		path.addActionListener(this);
		this.add(path);
		path.setBounds(LISTS_X+265, LISTS_Y+205, 80, 30);
		
		source=new JButton("Load");
		source.addActionListener(this);
		this.add(source);
		source.setBounds(LISTS_X+265, LISTS_Y+235, 80, 30);
		
		
		list_script=new JComboBox<String>();
		for (int i=0;i<main.listScript.length;i++) list_script.addItem(main.listScript[i]);
		list_script.addActionListener(this);
		this.add(list_script);
		list_script.setBounds(LISTS_X+70,LISTS_Y+285, 170, 30);
		
		script=new JButton("Load");
		script.addActionListener(this);
		this.add(script);
		script.setBounds(LISTS_X+265, LISTS_Y+285, 80, 30);
		
		
		rescan=new JButton("Rescan");
		rescan.addActionListener(this);
		this.add(rescan);
		rescan.setBounds(PRESET_X+10, PRESET_Y+20, 120, 50);
		
		savePreset=new JButton("<html><center> Save<br>preset</center></html>");
		savePreset.addActionListener(this);
		this.add(savePreset);
		savePreset.setBounds(PRESET_X+10, PRESET_Y+90, 120, 50);
		
		
		playPause=new JButton( new ImageIcon(Main.FILES+"ressources/play_icon.png"));
		playPause.addActionListener(this);
		this.add(playPause);
		playPause.setBounds(PATH_X+5, PATH_Y+5, 115, 35);
		
		remLast=new JButton("<html><center> Remove<br>last point</center></html>");
		remLast.addActionListener(this);
		this.add(remLast);
		remLast.setBounds(PATH_X+130, PATH_Y+5, 115, 35);
		
		clearPath=new JButton("Clear path");
		clearPath.addActionListener(this);
		this.add(clearPath);
		clearPath.setBounds(PATH_X+255, PATH_Y+5, 115, 35);
		
		speed=new JSlider(JSlider.HORIZONTAL, 0, 500, 200);
		speed.setMinorTickSpacing(1);
		speed.setPaintTicks(true);
		speed.setPaintLabels(true);
		speed.setMinorTickSpacing(10);
		speed.setMajorTickSpacing(100);
		speed.addChangeListener(new ChangeListener(){
		      public void stateChanged(ChangeEvent e){
		    	  main.target_speed=((JSlider)e.getSource()).getValue();
		      }});
		speed.setBounds(PATH_X+5, PATH_Y+70, 365, 50);
		this.add(speed);
		
		
		savePath=new JButton("Save path");
		savePath.addActionListener(this);
		this.add(savePath);
		savePath.setBounds(PATH_X+200, PATH_Y+125, 150, 50);
		
		targetMode = new ButtonGroup();
		controlMode = new JRadioButton("Control");
		attractorMode = new JRadioButton("Attractor");
		this.add(controlMode);
		this.add(attractorMode);
		controlMode.setBounds(PATH_X+25, PATH_Y+130, 200, 20);
		controlMode.addActionListener(this);
		attractorMode.setBounds(PATH_X+25, PATH_Y+150, 220, 20);
		attractorMode.addActionListener(this);
		
		targetMode.add(controlMode);
		targetMode.add(attractorMode);
		
	}
	
	
	
	
	public void paintComponent(Graphics g){
		
		if (display_mode==0){
			/*list_image.setVisible(true);
			list_tactile.setVisible(true);
			list_flow.setVisible(true);
			list_rail.setVisible(true);
			list_area.setVisible(true);*/
			
			// draw image
			g.setColor(Color.black);
			if (selected_image==0){
				if (main.currentAge.image.view!=null) g.drawImage(main.currentAge.image.view_img, IMAGE_X, IMAGE_Y, this);
				else g.drawRect(IMAGE_X, IMAGE_Y, 700, 700);
			}
			else if (selected_image==1){
				if (main.currentAge.image.tactile!=null) g.drawImage(main.currentAge.image.tactile_img, IMAGE_X, IMAGE_Y, this);
				else g.drawRect(IMAGE_X, IMAGE_Y, 700, 700);
			}
			else if (selected_image==2){
				if (main.currentAge.image.flow!=null) g.drawImage(main.currentAge.image.flow_img, IMAGE_X, IMAGE_Y, this);
				else g.drawRect(IMAGE_X, IMAGE_Y, 700, 700);
			}
			else if (selected_image==3){
				if (main.currentAge.image.rail!=null) g.drawImage(main.currentAge.image.rail_img, IMAGE_X, IMAGE_Y, this);
				else g.drawRect(IMAGE_X, IMAGE_Y, 700, 700);
			}
			else if (selected_image==4){
				if (main.currentAge.image.area!=null) g.drawImage(main.currentAge.image.area_img, IMAGE_X, IMAGE_Y, this);
				else g.drawRect(IMAGE_X, IMAGE_Y, 700, 700);
			}
			else{
				if (Main.CAMERA_CONNECTED){
					Image image = Main.Mat2bufferedImage(main.webcam);
				    g.drawImage(image, IMAGE_X, IMAGE_Y, IMAGE_X+640, IMAGE_Y+480, 640, 480, 0, 0, this);
				}
				else g.drawRect(IMAGE_X, IMAGE_Y, 640, 480);
			}
			
			
			// draw miniatures
			if (main.currentAge.image.view!=null) g.drawImage(main.currentAge.image.view_img, IMAGE_X+710, IMAGE_Y, 175, 175,  this);
			else g.drawRect(IMAGE_X+710, IMAGE_Y, 175, 175);
			
			
			if (main.currentAge.image.tactile!=null) g.drawImage(main.currentAge.image.tactile_img, IMAGE_X+890, IMAGE_Y, 175, 175,  this);
			else g.drawRect(IMAGE_X+890, IMAGE_Y, 175, 175);
			
			if (main.currentAge.image.flow!=null) g.drawImage(main.currentAge.image.flow_img, IMAGE_X+710, IMAGE_Y+180, 175, 175,  this);
			else g.drawRect(IMAGE_X+710, IMAGE_Y+180, 175, 175);
			
			if (main.currentAge.image.rail!=null) g.drawImage(main.currentAge.image.rail_img, IMAGE_X+890, IMAGE_Y+180, 175, 175,  this);
			else g.drawRect(IMAGE_X+890, IMAGE_Y+180, 175, 175);
			
			if (main.currentAge.image.area!=null) g.drawImage(main.currentAge.image.area_img, IMAGE_X+710, IMAGE_Y+360, 175, 175,  this);
			else g.drawRect(IMAGE_X+710, IMAGE_Y+360, 175, 175);
			
			if (Main.CAMERA_CONNECTED){
		        Image image = Main.Mat2bufferedImage(main.webcam);
		        g.drawImage(image, IMAGE_X+900, IMAGE_Y+380, IMAGE_X+1060, IMAGE_Y+500, 640, 480, 0, 0, this);
			}
			else g.drawRect(IMAGE_X+900, IMAGE_Y+380, 160, 120);
			
			
			// draw list names
			g.drawRect(LISTS_X, LISTS_Y, 225, 160);
			g.drawString("Image", LISTS_X+10, LISTS_Y+25);
			g.drawString("Tactile", LISTS_X+10, LISTS_Y+55);
			g.drawString("Flow", LISTS_X+10, LISTS_Y+85);
			g.drawString("Rails", LISTS_X+10, LISTS_Y+115);
			g.drawString("Areas", LISTS_X+10, LISTS_Y+145);
			
			g.drawRect(LISTS_X, LISTS_Y+170, 375, 100);
			g.drawString("Preset", LISTS_X+10, LISTS_Y+195);
			g.drawString("Path", LISTS_X+10, LISTS_Y+225);
			g.drawString("Sources", LISTS_X+10, LISTS_Y+255);
			
			g.drawRect(LISTS_X, LISTS_Y+280, 375, 40);
			g.drawString("Script", LISTS_X+10, LISTS_Y+305);
			
			
			// preset bloc
			g.drawRect(PRESET_X, PRESET_Y, 140, 160);
			
			
			// path bloc
			g.drawRect(PATH_X, PATH_Y, 375, 190);
			if (main.target_pause) playPause.setIcon( new ImageIcon(Main.FILES+"ressources/pause_icon.png"));
			else playPause.setIcon( new ImageIcon(Main.FILES+"ressources/play_icon.png"));
			
			g.drawString(""+main.target_speed, PATH_X+8+(int)(main.target_speed*(float)335/(float)500), PATH_Y+60);
			
			
	        // draw joystick position
			if (selected_image>=0){
				
				g.setColor(Color.cyan);
				for (int t=0;t<Main.LENGTH-1;t++){
					if (t+1!=main.time){
					g.drawLine(IMAGE_X+348+(int)main.trace[t][0], IMAGE_Y+268-(int)main.trace[t][1],
							  IMAGE_X+348+(int)main.trace[t+1][0], IMAGE_Y+268-(int)main.trace[t+1][1]);
					}
				}
				
				g.setColor(Color.gray);
		 		g.drawOval(IMAGE_X+323+(int)(main.x_prev), IMAGE_Y+243-(int)(main.y_prev), 50, 50);
		 		g.setColor(Color.white);
		 		g.drawOval(IMAGE_X+323+(int)(main.x), IMAGE_Y+245-(int)(main.y), 50, 50);
		 		g.drawLine(IMAGE_X+348+(int)(main.x), IMAGE_Y+268-(int)(main.y),IMAGE_X+348+(int)(main.x)+(int)main.dx*5, IMAGE_Y+268-(int)(main.y)-(int)main.dy*5);
		 		g.setColor(Color.red);
		 		g.drawOval(IMAGE_X+323+(int)(main.x_next), IMAGE_Y+243-(int)(main.y_next), 50, 50);
			}
			else{
		 		g.setColor(Color.gray);
		 		g.drawOval(IMAGE_X+325+(int)(main.x_prev*0.5+35), IMAGE_Y+245-(int)(main.y_prev*0.5-35), 50, 50);
		 		g.setColor(Color.white);
		 		g.drawOval(IMAGE_X+325+(int)(main.x*0.5+35), IMAGE_Y+245-(int)(main.y*0.5-35), 50, 50);
		 		g.drawLine(IMAGE_X+350+(int)(main.x*0.5+35), IMAGE_Y+270-(int)(main.y*0.5-35),IMAGE_X+350+(int)(main.x*0.5+35)+(int)main.dx*5, IMAGE_Y+270-(int)(main.y*0.5-35)-(int)main.dy*5);
		 		g.setColor(Color.red);
		 		g.drawOval(IMAGE_X+325+(int)(main.x_next*0.5+35), IMAGE_Y+245-(int)(main.y_next*0.5-35), 50, 50);
			}
			
			
			// draw path
			if (selected_image>=0){
				if (main.currentAge.targetSequence.size()>0){
					if (main.currentAge.targetSequence.get(0).control==0) g.setColor(Color.yellow);
					else g.setColor(Color.magenta);
					g.fillOval(IMAGE_X+348+(int)main.currentAge.targetSequence.get(0).x, IMAGE_Y+268-(int)main.currentAge.targetSequence.get(0).y, 5, 5);
					for (int i=1;i<main.currentAge.targetSequence.size();i++){
						if (main.currentAge.targetSequence.get(i).control==0) g.setColor(Color.yellow);
						else g.setColor(Color.magenta);
						g.drawLine(IMAGE_X+350+(int)main.currentAge.targetSequence.get(i-1).x, IMAGE_Y+270-(int)main.currentAge.targetSequence.get(i-1).y,
								   IMAGE_X+350+(int)main.currentAge.targetSequence.get(i  ).x, IMAGE_Y+270-(int)main.currentAge.targetSequence.get(i  ).y);
						g.fillOval(IMAGE_X+349+(int)main.currentAge.targetSequence.get(i).x, IMAGE_Y+269-(int)main.currentAge.targetSequence.get(i).y, 3, 3);
					}
				}
			}
			else{
				if (main.currentAge.targetSequence.size()>0){
					if (main.currentAge.targetSequence.get(0).control==0) g.setColor(Color.yellow);
					else g.setColor(Color.magenta);
					g.fillOval(IMAGE_X+348+(int)(main.currentAge.targetSequence.get(0).x*0.5), IMAGE_Y+268-(int)(main.currentAge.targetSequence.get(0).y*0.5), 5, 5);
					for (int i=1;i<main.currentAge.targetSequence.size();i++){
						if (main.currentAge.targetSequence.get(i).control==0) g.setColor(Color.yellow);
						else g.setColor(Color.magenta);
						g.drawLine(IMAGE_X+350+(int)(main.currentAge.targetSequence.get(i-1).x*0.5), IMAGE_Y+270-(int)(main.currentAge.targetSequence.get(i-1).y*0.5),
								   IMAGE_X+350+(int)(main.currentAge.targetSequence.get(i  ).x*0.5), IMAGE_Y+270-(int)(main.currentAge.targetSequence.get(i  ).y*0.5));
						g.fillOval(IMAGE_X+349+(int)(main.currentAge.targetSequence.get(i).x*0.5), IMAGE_Y+269-(int)(main.currentAge.targetSequence.get(i).y*0.5), 3, 3);
					}
				}
			}
			
		}
		
		if (display_mode==1){
			list_image.setVisible(false);
			list_tactile.setVisible(false);
			list_flow.setVisible(false);
			list_rail.setVisible(false);
			list_area.setVisible(false);
		
			
		}
		
		
		
		

    
        
 		list_image.setSelectedIndex(main.selected_img+1);
 		list_tactile.setSelectedIndex(main.selected_tactile+1);
 		list_flow.setSelectedIndex(main.selected_flow+1);
 		list_rail.setSelectedIndex(main.selected_rail+1);
 		list_area.setSelectedIndex(main.selected_area+1);
 		
 		if (main.target_type==1) controlMode.setSelected(true);
 		else attractorMode.setSelected(true);
 		
        


        

        
        try {Thread.sleep(20);
		} catch (InterruptedException e) {e.printStackTrace();}
	}

	

	
	
	public void rescan(){
		list_image.removeAllItems();
		list_image.addItem("none");
		for (int i=0;i<main.listImages.length;i++) list_image.addItem(main.listImages[i]);
		
		list_tactile.removeAllItems();
		list_tactile.addItem("none");
		for (int i=0;i<main.listTactile.length;i++) list_tactile.addItem(main.listTactile[i]);
		
		list_flow.removeAllItems();
		list_flow.addItem("none");
		for (int i=0;i<main.listFlow.length;i++) list_flow.addItem(main.listFlow[i]);
		
		list_rail.removeAllItems();
		list_rail.addItem("none");
		for (int i=0;i<main.listRail.length;i++) list_rail.addItem(main.listRail[i]);
		
		list_area.removeAllItems();
		list_area.addItem("none");
		for (int i=0;i<main.listArea.length;i++) list_area.addItem(main.listArea[i]);
		
		
		list_preset.removeAllItems();
		for (int i=0;i<main.listPreset.length;i++) list_preset.addItem(main.listPreset[i]);
		
		list_path.removeAllItems();
		for (int i=0;i<main.listPath.length;i++) list_path.addItem(main.listPath[i]);
		
		list_source.removeAllItems();
		for (int i=0;i<main.listSource.length;i++) list_source.addItem(main.listSource[i]);
		
		list_script.removeAllItems();
		for (int i=0;i<main.listScript.length;i++) list_script.addItem(main.listScript[i]);
	}
	

	public void mouseClicked(MouseEvent e) {

		int x=e.getX();
		int y=e.getY();
		
		if (x>=IMAGE_X+710 && x<IMAGE_X+885  && y>=IMAGE_Y     && y<IMAGE_Y+175) selected_image=0;
		if (x>=IMAGE_X+890 && x<IMAGE_X+1065 && y>=IMAGE_Y     && y<IMAGE_Y+175) selected_image=1;
		if (x>=IMAGE_X+710 && x<IMAGE_X+885  && y>=IMAGE_Y+180 && y<IMAGE_Y+355) selected_image=2;
		if (x>=IMAGE_X+890 && x<IMAGE_X+1065 && y>=IMAGE_Y+180 && y<IMAGE_Y+355) selected_image=3;
		if (x>=IMAGE_X+710 && x<IMAGE_X+885  && y>=IMAGE_Y+360 && y<IMAGE_Y+535) selected_image=4;
		if (x>=IMAGE_X+900 && x<IMAGE_X+1055 && y>=IMAGE_Y+380 && y<IMAGE_Y+500) selected_image=-1;
		
		
		if (selected_image>=0){
			if (x>IMAGE_X && x<IMAGE_X+700 && y>IMAGE_Y && y<IMAGE_Y+700){
				main.currentAge.targetSequence.add(new Target(-350+(int)e.getX()-IMAGE_X, 270-(int)e.getY()+IMAGE_Y,main.target_speed, main.target_type));
			}
		}
	}
	
	
	public void mousePressed(MouseEvent e) {
	
	}
	
	public void mouseReleased(MouseEvent e) {

	}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	public void actionPerformed(ActionEvent e) {
		
		
		///////////////////////////////////////////////////////////////////////
		if (e.getSource()==list_image){
			if (list_image.getSelectedIndex()==0) main.setPicture(null);
			else main.setPicture((String)list_image.getSelectedItem());
		}
		if (e.getSource()==list_tactile){
			if (list_tactile.getSelectedIndex()==0) main.setTactile(null);
			else main.setTactile((String)list_tactile.getSelectedItem());
		}
		if (e.getSource()==list_flow){
			if (list_flow.getSelectedIndex()==0) main.setFlow(null);
			else main.setFlow((String)list_flow.getSelectedItem());
		}
		if (e.getSource()==list_rail){
			if (list_rail.getSelectedIndex()==0) main.setRail(null);
			else main.setRail((String)list_rail.getSelectedItem());
		}
		if (e.getSource()==list_area){
			if (list_area.getSelectedIndex()==0) main.setArea(null);
			else main.setArea((String)list_area.getSelectedItem());
		}
		
		///////////////////////////////////////////////////////////////////////
		if (e.getSource()==preset) main.setPreset((String)list_preset.getSelectedItem(),1);
		if (e.getSource()==path  ) main.setPath((String)list_path.getSelectedItem());
		if (e.getSource()==source) main.setSource((String)list_source.getSelectedItem());
		
		///////////////////////////////////////////////////////////////////////
		if (e.getSource()==script) main.setScript((String)list_script.getSelectedItem());
		
		///////////////////////////////////////////////////////////////////////
		if (e.getSource()==rescan) main.listFiles();
		if (e.getSource()==savePreset) main.savePreset();
		
		//////////////////////////////////////////////////////////////////////
		if (e.getSource()==playPause) main.target_pause=!main.target_pause;
		if (e.getSource()==remLast) main.currentAge.targetSequence.remove(main.currentAge.targetSequence.size()-1);
		if (e.getSource()==clearPath) main.currentAge.targetSequence.clear();
		
		if (e.getSource()==controlMode && controlMode.isSelected()) main.target_type=1;
		if (e.getSource()==attractorMode && attractorMode.isSelected()) main.target_type=0;
		
		if (e.getSource()==savePath) main.savePath();
		
		this.repaint();
	}

}
