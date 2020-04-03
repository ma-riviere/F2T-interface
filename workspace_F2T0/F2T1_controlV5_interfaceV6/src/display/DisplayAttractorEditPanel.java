package display;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import structures.Attractor;

import main.Main;

/**
 * Attractor points edition tool
 * @author simon gay
 */

public class DisplayAttractorEditPanel  extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener{
	
	private static final long serialVersionUID = 1L;
	
	public static int MAX_UNDO=50;
	
	public Main main;
	public DisplayFrame frame;
	
	private int selected_image=0;
	
	private ButtonGroup imageMode;
	private JRadioButton image;
	private JRadioButton tactile;
	private JRadioButton flow;
	private JRadioButton rail;
	private JRadioButton area;
	private JRadioButton magnetic;
	
	
	private JButton remove;
	private JButton set;
	private JButton clear;
	
	private JButton undo;
	private JButton redo;
	
	private ButtonGroup type;
	private JRadioButton linear;
	private JRadioButton constant;
	private JRadioButton inverse;
	private JRadioButton round;

	
	private JComboBox<String> list_attractors;
	private JButton attractorLoad;
	
	private JTextField name;
	private JButton attractorSave;
	private String saveMsg="";
	
	//public ArrayList<Attractor> attractors;
	public ArrayList<ArrayList<Attractor>> attractor_buffer;
	public int buffer;
	
	public boolean click_right=false;
	public boolean click_left=false;
	
	public int selected_point=0;
	public boolean point_capture=false;
	public boolean write=false;
	
	public int ex=0;
	public int ey=0;
	
	public int tx=0;
	public int ty=0;
	
	

	public DisplayAttractorEditPanel(Main m, DisplayFrame f){
		main=m;
		frame=f;
		
		addMouseListener(this);
		addMouseMotionListener(this);

		this.setLayout(null);
		
		imageMode 	= new ButtonGroup();
		image 		= new JRadioButton("Image");
		tactile 	= new JRadioButton("Tactile");
		flow 		= new JRadioButton("Flow");
		rail 		= new JRadioButton("Rail");
		area		= new JRadioButton("Area");
		magnetic 	= new JRadioButton("Magnetic");
		
		this.add(image);
		this.add(tactile);
		this.add(flow);
		this.add(rail);
		this.add(area);
		this.add(magnetic);
		image.setBounds(725, 5, 100, 20);
		image.addActionListener(this);
		image.addKeyListener(f.keyboard);
		tactile.setBounds(725, 25, 100, 20);
		tactile.addActionListener(this);
		tactile.addKeyListener(f.keyboard);
		flow.setBounds(725, 45, 100, 20);
		flow.addActionListener(this);
		flow.addKeyListener(f.keyboard);
		rail.setBounds(725, 65, 100, 20);
		rail.addActionListener(this);
		rail.addKeyListener(f.keyboard);
		area.setBounds(725, 85, 100, 20);
		area.addActionListener(this);
		area.addKeyListener(f.keyboard);
		magnetic.setBounds(725, 105, 100, 20);
		magnetic.addActionListener(this);
		magnetic.addKeyListener(f.keyboard);
		
		imageMode.add(image);
		imageMode.add(tactile);
		imageMode.add(flow);
		imageMode.add(rail);
		imageMode.add(area);
		imageMode.add(magnetic);
		
		image.setSelected(true);
		
		
		/////////////////////////////////
		remove=new JButton("Remove");
		remove.addActionListener(this);
		remove.addKeyListener(f.keyboard);
		this.add(remove);
		remove.setBounds(725, 220, 100, 30);
		
		set=new JButton("Set");
		set.addActionListener(this);
		set.addKeyListener(f.keyboard);
		this.add(set);
		set.setBounds(725, 265, 100, 30);
		
		clear=new JButton("Clear");
		clear.addActionListener(this);
		clear.addKeyListener(f.keyboard);
		this.add(clear);
		clear.setBounds(725, 310, 100, 30);
		
		/////////////////////////////////
		
		undo=new JButton("Undo");
		undo.addActionListener(this);
		undo.addKeyListener(f.keyboard);
		this.add(undo);
		undo.setBounds(890, 220, 100, 50);
		
		redo=new JButton("Redo");
		redo.addActionListener(this);
		redo.addKeyListener(f.keyboard);
		this.add(redo);
		redo.setBounds(890, 290, 100, 50);
		
		
		/////////////////////////////////
		
		type 	= new ButtonGroup();
		linear 		= new JRadioButton("Linear");
		constant 	= new JRadioButton("Constant");
		inverse		= new JRadioButton("inverse");
		round 		= new JRadioButton("Round");

		this.add(linear);
		this.add(constant);
		this.add(inverse);
		this.add(round);
		linear.setBounds(725,360, 100, 20);
		linear.addActionListener(this);
		linear.addKeyListener(f.keyboard);
		constant.setBounds(725, 380, 100, 20);
		constant.addActionListener(this);
		constant.addKeyListener(f.keyboard);
		inverse.setBounds(725, 400, 100, 20);
		inverse.addActionListener(this);
		inverse.addKeyListener(f.keyboard);
		round.setBounds(725, 420, 100, 20);
		round.addActionListener(this);
		round.addKeyListener(f.keyboard);
		
		type.add(linear);
		type.add(constant);
		type.add(inverse);
		type.add(round);
		
		linear.setSelected(true);
		
		
		/////////////////////////////////
		
		list_attractors=new JComboBox<String>();
		for (int i=0;i<main.listAttractors.length;i++) list_attractors.addItem(main.listAttractors[i]);
		list_attractors.addActionListener(this);
		list_attractors.addKeyListener(f.keyboard);
		this.add(list_attractors);
		list_attractors.setBounds(800,470, 170, 30);
		
		attractorLoad=new JButton("Load");
		attractorLoad.addActionListener(this);
		attractorLoad.addKeyListener(f.keyboard);
		this.add(attractorLoad);
		attractorLoad.setBounds(980, 470, 80, 30);
		
		/////////////////////////////////
		
		name = new JTextField();
		this.add(name);
		name.setBounds(800, 550, 170, 30);
		name.addKeyListener(this);
		name.setText(main.attractorName);
		
		attractorSave=new JButton("Save");
		attractorSave.addActionListener(this);
		attractorSave.addKeyListener(f.keyboard);
		this.add(attractorSave);
		attractorSave.setBounds(980, 550, 80, 30);
		
		/////////////////////////////////
		//attractors=new ArrayList<Attractor>();
		attractor_buffer=new ArrayList<ArrayList<Attractor>>();
		buffer=-1;

	}
	
	
	public void paintComponent(Graphics g){
		
		
		g.setColor(this.getBackground());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		
		// draw image

		if (selected_image==0){
			if (main.script.ageList.get(main.script.currentAge).image.view!=null) g.drawImage(main.script.ageList.get(main.script.currentAge).image.view_img, 0, 0, this);
			else{
				g.setColor(Color.gray);
				g.fillRect(0, 0, 699, 699);
				g.setColor(Color.black);
				g.drawRect(0, 0, 699, 699);
				g.drawLine(350,0,350,700);
				g.drawLine(0,350,700,350);
			}
		}
		else if (selected_image==1){
			if (main.script.ageList.get(main.script.currentAge).image.tactile!=null) g.drawImage(main.script.ageList.get(main.script.currentAge).image.tactile_img, 0, 0, this);
			else{
				g.setColor(Color.gray);
				g.fillRect(0, 0, 699, 699);
				g.setColor(Color.black);
				g.drawRect(0, 0, 699, 699);
				g.drawLine(350,0,350,700);
				g.drawLine(0,350,700,350);
			}
		}
		else if (selected_image==2){
			if (main.script.ageList.get(main.script.currentAge).image.flow!=null) g.drawImage(main.script.ageList.get(main.script.currentAge).image.flow_img, 0, 0, this);
			else{
				g.setColor(Color.gray);
				g.fillRect(0, 0, 699, 699);
				g.setColor(Color.black);
				g.drawRect(0, 0, 699, 699);
				g.drawLine(350,0,350,700);
				g.drawLine(0,350,700,350);
			}
		}
		else if (selected_image==3){
			if (main.script.ageList.get(main.script.currentAge).image.rail!=null) g.drawImage(main.script.ageList.get(main.script.currentAge).image.rail_img, 0, 0, this);
			else{
				g.setColor(Color.gray);
				g.fillRect(0, 0, 699, 699);
				g.setColor(Color.black);
				g.drawRect(0, 0, 699, 699);
				g.drawLine(350,0,350,700);
				g.drawLine(0,350,700,350);
			}
		}
		else if (selected_image==4){
			if (main.script.ageList.get(main.script.currentAge).image.area!=null) g.drawImage(main.script.ageList.get(main.script.currentAge).image.area_img, 0, 0, this);
			else{
				g.setColor(Color.gray);
				g.fillRect(0, 0, 699, 699);
				g.setColor(Color.black);
				g.drawRect(0, 0, 699, 699);
				g.drawLine(350,0,350,700);
				g.drawLine(0,350,700,350);
			}
		}
		else if (selected_image==5){
			if (main.script.ageList.get(main.script.currentAge).image.magnetic!=null) g.drawImage(main.script.ageList.get(main.script.currentAge).image.magnetic_img, 0, 0, this);
			else{
				g.setColor(Color.gray);
				g.fillRect(0, 0, 699, 699);
				g.setColor(Color.black);
				g.drawRect(0, 0, 699, 699);
				g.drawLine(350,0,350,700);
				g.drawLine(0,350,700,350);
			}
		}
		
		// draw trace and joystick
		if (frame.selected_image>=0){
			g.setColor(Color.cyan);
			for (int t=0;t<Main.LENGTH-1;t++){
				if (t+1!=main.time){
				g.drawLine(350+(int)main.trace[t  ][0], 350-(int)main.trace[t][1],
						   350+(int)main.trace[t+1][0], 350-(int)main.trace[t+1][1]);
				}
			}
			
	 		g.setColor(Color.white);
	 		g.drawOval(325+(int)(main.x), 325-(int)(main.y), 50, 50);
		}
		
		
		/////////////////////////////////////////////////////////////////////////////
		// draw attractors
		if (main.script.ageList.get(main.script.currentAge).attractorList.size()>0){
			
			for (int i=0;i<main.script.ageList.get(main.script.currentAge).attractorList.size();i++){
				
				int vx1=main.script.ageList.get(main.script.currentAge).attractorList.get(i  ).x;
				int vy1=main.script.ageList.get(main.script.currentAge).attractorList.get(i  ).y;
				int d=main.script.ageList.get(main.script.currentAge).attractorList.get(i).distance;
				int t=main.script.ageList.get(main.script.currentAge).attractorList.get(i).type;
				
				int strength=(main.script.ageList.get(main.script.currentAge).attractorList.get(i).strength+250)/2;
				if (strength>255) strength=255;
				if (strength<0) strength=0;
				if (main.script.ageList.get(main.script.currentAge).attractorList.get(i).strength>=0) g.setColor(new Color(strength,255-strength,255));
				else g.setColor(new Color(strength,255-strength/2,0));
				g.fillOval(vx1+350-4, 350-vy1-4, 10, 10);
				
				
				//g.drawOval(vx1+350-d, 350-vy1-d, d*2, d*2);
				
				if (t==0){ // linear
					for (int k=1;k<=5;k++){
						g.drawOval(vx1+350-(d*(k)/5), 350-vy1-(d*(k)/5), (d*(k)/5)*2, (d*(k)/5)*2);
					}
				}
				else if (t==1){ // constant
					g.drawOval(vx1+350-d, 350-vy1-d, d*2, d*2);
				}
				else if (t==2){ // inverse
					g.drawOval(vx1+350-d, 350-vy1-d, d*2, d*2);
					for (int k=1;k<=5;k++){
						float y=10*(float)k/5;
						g.drawOval(vx1+350-(int)(d*(1/y)), 350-vy1-(int)(d*(1/y)), (int)(d*(1/y)*2), (int)(d*(1/y)*2));
					}
				}
				else if (t==3){ // round
					g.drawOval(vx1+350-d, 350-vy1-d, d*2, d*2);
					
					for (int k=1;k<=5;k++){
						float y=(float)(Math.sqrt(1-((float)k/5)*((float)k/5)));
						g.drawOval(vx1+350-(int)(d*y), 350-vy1-(int)(d*y), (int)(d*y)*2, (int)(d*y)*2);
					}
				}
				
				
				if (selected_point==i){
					g.setColor(Color.magenta);
					g.drawOval(vx1+350-7, 350-vy1-7, 16, 16);
				}
			}
		}
		

		g.setColor(this.getBackground());
		g.fillRect(700, 0, 390, 700);

		
		g.setColor(Color.black);
		
		g.drawRect(710, 0, 370, 130);
		g.drawRect(710, 140, 370, 50);
		g.drawRect(710, 200, 370, 240);
		g.drawRect(710, 450, 370, 160);
		
		g.drawString("Load file", 720, 488);
		g.drawString("Save as", 720, 568);
		g.drawString(saveMsg, 801, 600);
		
		g.drawString("Selected point :", 725, 160);
		if (selected_point>=0 && selected_point<main.script.ageList.get(main.script.currentAge).attractorList.size()){
			g.drawString("position : ("+main.script.ageList.get(main.script.currentAge).attractorList.get(selected_point).x+", "
		                               +main.script.ageList.get(main.script.currentAge).attractorList.get(selected_point).y+")", 830, 160);
			
			if (main.script.ageList.get(main.script.currentAge).attractorList.get(selected_point).type==1) 
				g.drawString("speed : "+main.script.ageList.get(main.script.currentAge).attractorList.get(selected_point).strength+", type : linear", 830, 180);
			else
				g.drawString("force : "+main.script.ageList.get(main.script.currentAge).attractorList.get(selected_point).strength+", type : inverse", 830, 180);
		}
		
		

	}
	
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==image) selected_image=0;
		if (e.getSource()==tactile) selected_image=1;
		if (e.getSource()==flow) selected_image=2;
		if (e.getSource()==rail) selected_image=3;
		if (e.getSource()==area) selected_image=4;
		if (e.getSource()==magnetic) selected_image=5;
		
		if (e.getSource()==linear) main.attractor_type=0;
		if (e.getSource()==constant) main.attractor_type=1;
		if (e.getSource()==inverse) main.attractor_type=2;
		if (e.getSource()==round) main.attractor_type=3;
		
		
		if (e.getSource()==remove){
			if (selected_point>=0 && selected_point<main.script.ageList.get(main.script.currentAge).attractorList.size()){
				main.script.ageList.get(main.script.currentAge).attractorList.remove(selected_point);
				if (selected_point>=main.script.ageList.get(main.script.currentAge).attractorList.size()) selected_point--;
				pushBuffer();
			}
		}
		else if (e.getSource()==set){
			if (selected_point>=0 && selected_point<main.script.ageList.get(main.script.currentAge).attractorList.size()){
				main.script.ageList.get(main.script.currentAge).attractorList.get(selected_point).strength=main.attractor_strength;
				main.script.ageList.get(main.script.currentAge).attractorList.get(selected_point).distance=main.attractor_distance;
				main.script.ageList.get(main.script.currentAge).attractorList.get(selected_point).type=main.attractor_type;
				
			}
			pushBuffer();
		}
		else if (e.getSource()==clear){
			main.script.ageList.get(main.script.currentAge).attractorList.clear();
			pushBuffer();
		}
		
		
		else if (e.getSource()==undo){
			if (buffer>0){
				main.script.ageList.get(main.script.currentAge).attractorList.clear();
				for (int i=0;i<attractor_buffer.get(buffer-1).size();i++){
					main.script.ageList.get(main.script.currentAge).attractorList.add(attractor_buffer.get(buffer-1).get(i).duplicate());
				}
				buffer--;
			}
		}
		else if (e.getSource()==redo){
			if (buffer+1<attractor_buffer.size()){
				main.script.ageList.get(main.script.currentAge).attractorList.clear();
				for (int i=0;i<attractor_buffer.get(buffer+1).size();i++){
					main.script.ageList.get(main.script.currentAge).attractorList.add(attractor_buffer.get(buffer+1).get(i).duplicate());
				}
				buffer++;
			}
		}
		
		else if (e.getSource()==attractorLoad) loadAttractor((String)list_attractors.getSelectedItem());
		else if (e.getSource()==attractorSave){
			if (name.getText().length()>0) saveAttractor(name.getText());
		}
		
	}
	
	
	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	
	
	public void mousePressed(MouseEvent e) {
		ex=e.getX();
		ey=e.getY();
		
		tx= ex-350;
		ty=-ey+350;
		
		if (!click_left && !click_right){
			if (e.getButton()==MouseEvent.BUTTON3){
				click_right=true;
			}
			else if (e.getButton()==MouseEvent.BUTTON1){
				click_left=true;
			}
		}
		
		
		if (click_left){
			
			// add target
			if (tx>-345 && tx<345 && ty>-345 && ty<345){
				main.script.ageList.get(main.script.currentAge).attractorList.add(new Attractor(tx, ty,main.attractor_strength, main.attractor_distance,main.attractor_type));
				pushBuffer();
			}
		}
		
		if (click_right){
			
			if (tx>-345 && tx<345 && ty>-345 && ty<345){
				double min=10;
				int imin=-1;
				double d=0;
				
				for (int i=0;i<main.script.ageList.get(main.script.currentAge).attractorList.size();i++){
					d=Math.sqrt( (tx-(main.script.ageList.get(main.script.currentAge).attractorList.get(i).x))*(tx-(main.script.ageList.get(main.script.currentAge).attractorList.get(i).x)) 
							   + (ty-(main.script.ageList.get(main.script.currentAge).attractorList.get(i).y))*(ty-(main.script.ageList.get(main.script.currentAge).attractorList.get(i).y)));
					if (d<min){
						min=d;
						imin=i;
					}
				}
				
				if (imin>=0){
					selected_point=imin;
					point_capture=true;
				}
				
			}
		}
		
		this.repaint();
	}



	public void mouseReleased(MouseEvent e) {
		if (e.getButton()==MouseEvent.BUTTON3) click_right=false;
		if (e.getButton()==MouseEvent.BUTTON1) click_left=false;
		point_capture=false;
		if (write) pushBuffer();
		write=false;
	}



	public void mouseDragged(MouseEvent e) {
		ex=e.getX();
		ey=e.getY();
		
		tx= ex-350;
		ty=-ey+350;
		
		if (tx>345) tx=345;
		if (tx<-345) tx=-345;
		if (ty>345) ty=345;
		if (ty<-345) ty=-345;
		
		if (click_right && point_capture){
			if (main.script.ageList.get(main.script.currentAge).attractorList.get(selected_point).x!=tx || main.script.ageList.get(main.script.currentAge).attractorList.get(selected_point).y!=ty) write=true;
			main.script.ageList.get(main.script.currentAge).attractorList.get(selected_point).x=tx;
			main.script.ageList.get(main.script.currentAge).attractorList.get(selected_point).y=ty;
		}
		this.repaint();
	}



	public void mouseMoved(MouseEvent e) {

		ex=e.getX();
		ey=e.getY();
		
		tx= ex-350;
		ty=-ey+350;
		
		this.repaint();
	}
	
	
	public void pushBuffer(){	
		while (attractor_buffer.size()>buffer+1) attractor_buffer.remove(attractor_buffer.size()-1);
		
		attractor_buffer.add(new ArrayList<Attractor>());
		buffer++;
		for (int i=0;i<main.script.ageList.get(main.script.currentAge).attractorList.size();i++){
			attractor_buffer.get(buffer).add(main.script.ageList.get(main.script.currentAge).attractorList.get(i).duplicate());
		}
		if (buffer>MAX_UNDO){
			attractor_buffer.remove(0);
			buffer--;
		}
	}
	
	public void loadAttractor(String file){
		main.println("Load attractor file "+file+" in attractor editor :");
		
		String fileName = Main.FILES+Main.ATTRACTOR+file;
		String[] elements;
		
		try {
			InputStream ips=new FileInputStream(fileName); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line;
			line=br.readLine();
			
			while (line!=null){
				
				main.println("   "+line);
				
				elements=line.split(" ");
				if (elements.length==6 && elements[0].equals("g")){
					main.script.ageList.get(main.script.currentAge).attractorList.add(new Attractor(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]),Integer.parseInt(elements[3]),
													   Integer.parseInt(elements[4]), Integer.parseInt(elements[5]) ));
				}
				line=br.readLine();
			}
			br.close();
		}
		catch (Exception e) {main.println("   /!\\ Attractor file not found or containing errors");}
		pushBuffer();
	}
	
	public void saveAttractor(String name){
		
		main.println("save attractor list : "+name);
		
		String fileName = Main.FILES+Main.ATTRACTOR+name;
		
		try {
			PrintWriter file  = new PrintWriter(new FileWriter(fileName));
			for (int t=0;t<main.script.ageList.get(main.script.currentAge).attractorList.size();t++){
				file.println("g "+main.script.ageList.get(main.script.currentAge).attractorList.get(t).x+" "
			                     +main.script.ageList.get(main.script.currentAge).attractorList.get(t).y+" "
						         +main.script.ageList.get(main.script.currentAge).attractorList.get(t).strength+" "
			                     +main.script.ageList.get(main.script.currentAge).attractorList.get(t).distance+" "
						         +main.script.ageList.get(main.script.currentAge).attractorList.get(t).type);
			}
			
			file.close();
			main.println("Attractor list saved");
		}
		catch (Exception e) {e.printStackTrace();}
		
		main.listFiles();

		list_attractors.removeAll();

		for (int i=0;i<main.listAttractors.length;i++) list_attractors.addItem(main.listAttractors[i]);
	}


	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {}
	
	public void keyReleased(KeyEvent e) {
		
		if (e.getSource()==name){
			boolean found=false;
			int i=0;
			
			while (!found && i<main.listAttractors.length){
				if (main.listAttractors[i].equals(name.getText())) found=true;
				i++;
			}
			
			if (found) saveMsg="/!\\ file already exists !";
			else saveMsg="";
			
			if (name.getText().length()==0) attractorSave.setEnabled(false);
			else attractorSave.setEnabled(true);
		}
	}
	
}
