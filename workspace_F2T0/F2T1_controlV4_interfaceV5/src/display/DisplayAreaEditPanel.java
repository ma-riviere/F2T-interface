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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.Main;

public class DisplayAreaEditPanel  extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener{
	
	private static final long serialVersionUID = 1L;
	
	public static int MAX_UNDO=20;
	
	public Main main;
	public DisplayFrame frame;
	
	private int selected_image=0;
	
	
	public short[][][] area_map;
	
	private ArrayList<short[][][]> area_buffer;
	public int buffer=-1;
	
	public short color=0;
	public short value=0;
	public short red=0;
	public short green=0;
	public short blue=0;
	public short areaID=0;
	public ArrayList<Integer> currentAreaID;
	
	public boolean red_channel=true;
	public boolean green_channel=true;
	public boolean blue_channel=true;

	public boolean click_right=false;
	public boolean click_left=false;
	
	public int selected_point=0;
	public boolean point_capture=false;
	public boolean write=false;
	
	public int ex=0;
	public int ey=0;
	
	public int tx=0;
	public int ty=0;
	
	private String displayed="";
	
	public boolean change=true;
	
	
	private ButtonGroup imageMode;
	private JRadioButton image;
	private JRadioButton tactile;
	private JRadioButton flow;
	private JRadioButton rail;
	private JRadioButton area;
	private JRadioButton magnetic;
	
	private JSlider brush_slider;
	private int brush_size=50;
	
	private JButton undo;
	private JButton redo;
	
	private JButton get;
	private JButton clear;
	
	private JComboBox<String> list_area;
	private JButton areaLoad;
	
	private JTextField name;
	private JButton areaSave;
	private String saveMsg="";
	
	private JCheckBox red_box = new JCheckBox("red");
	private JCheckBox green_box = new JCheckBox("green");
	private JCheckBox blue_box = new JCheckBox("blue");
	



	public DisplayAreaEditPanel(Main m, DisplayFrame f){
		main=m;
		frame=f;
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		currentAreaID=new ArrayList<Integer>();

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
		
		undo=new JButton("Undo");
		undo.addActionListener(this);
		undo.addKeyListener(f.keyboard);
		this.add(undo);
		undo.setBounds(720, 145, 75, 40);
		
		redo=new JButton("Redo");
		redo.addActionListener(this);
		redo.addKeyListener(f.keyboard);
		this.add(redo);
		redo.setBounds(800, 145, 75, 40);
		
		clear=new JButton("clear");
		clear.addActionListener(this);
		clear.addKeyListener(f.keyboard);
		this.add(clear);
		clear.setBounds(900, 145, 80, 40);
		
		get=new JButton("Get");
		get.addActionListener(this);
		get.addKeyListener(f.keyboard);
		this.add(get);
		get.setBounds(990, 145, 80, 40);
		
		/////////////////////////////////
		
		list_area=new JComboBox<String>();
		for (int i=0;i<main.listArea.length;i++) list_area.addItem(main.listArea[i]);
		list_area.addActionListener(this);
		list_area.addKeyListener(f.keyboard);
		this.add(list_area);
		list_area.setBounds(800,470, 170, 30);
		
		areaLoad=new JButton("Load");
		areaLoad.addActionListener(this);
		areaLoad.addKeyListener(f.keyboard);
		this.add(areaLoad);
		areaLoad.setBounds(980, 470, 80, 30);
		
		/////////////////////////////////
		
		name = new JTextField();
		this.add(name);
		name.setBounds(800, 550, 170, 30);
		name.addKeyListener(this);
		
		areaSave=new JButton("Save");
		areaSave.addActionListener(this);
		areaSave.addKeyListener(f.keyboard);
		this.add(areaSave);
		areaSave.setBounds(980, 550, 80, 30);
		
		/////////////////////////////////
		
		this.add(red_box);
		red_box.setBounds(900, 30, 160, 20);
		red_box.setSelected(true);
		red_box.addActionListener(this);
		red_box.addKeyListener(f.keyboard);
		
		this.add(green_box);
		green_box.setBounds(900, 50, 160, 20);
		green_box.setSelected(true);
		green_box.addActionListener(this);
		green_box.addKeyListener(f.keyboard);
		
		this.add(blue_box);
		blue_box.setBounds(900, 70, 160, 20);
		blue_box.setSelected(true);
		blue_box.addActionListener(this);
		blue_box.addKeyListener(f.keyboard);
		
		///////////////////////////////
		
		brush_slider=new JSlider(JSlider.HORIZONTAL, 5, 85, 10);
		brush_slider.setPaintTicks(true);
		brush_slider.setPaintLabels(true);
		brush_slider.setMinorTickSpacing(5);
		brush_slider.setMajorTickSpacing(15);
		brush_slider.addChangeListener(new ChangeListener(){
		      public void stateChanged(ChangeEvent e){
		    	  brush_size=brush_slider.getValue();
		    	  //main.display.repaint();
		      }});
		brush_slider.addKeyListener(f.keyboard);
		brush_slider.setBounds(910, 390, 160, 50);
		brush_slider.setValue(50);
		this.add(brush_slider);
		
		
		///////////////////////////////
		area_map = new short[700][700][3];
		
		area_buffer=new ArrayList<short[][][]>();
		
		area_buffer.add(new short[700][700][3]);
		for (int i=0;i<700;i++){
			for (int j=0;j<700;j++){
				area_buffer.get(0)[i][j][0]=area_map[i][j][0];
				area_buffer.get(0)[i][j][1]=area_map[i][j][1];
				area_buffer.get(0)[i][j][1]=area_map[i][j][2];
			}
		}
		buffer=0;
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void paintComponent(Graphics g){
		
		
		boolean image=true;
		
		/////////////////////////////////////////////////////////////////////////////
		// draw background image
		if (selected_image==0){
			if (main.script.ageList.get(main.script.currentAge).image.view!=null){
				if (!main.script.ageList.get(main.script.currentAge).image.view.equals(displayed) || change){
					g.drawImage(main.script.ageList.get(main.script.currentAge).image.view_img, 0, 0, this);
					change=true;
					displayed=main.script.ageList.get(main.script.currentAge).image.view;
				}
			}
			else{
				image=false;
				if (!displayed.equals("") || change){
					change=true;
					displayed="";
				}
			}
		}
		else if (selected_image==1){
			if (main.script.ageList.get(main.script.currentAge).image.tactile!=null){
				if (!main.script.ageList.get(main.script.currentAge).image.tactile.equals(displayed) || change){
					g.drawImage(main.script.ageList.get(main.script.currentAge).image.tactile_img, 0, 0, this);
					change=true;
					displayed=main.script.ageList.get(main.script.currentAge).image.tactile;
				}
			}
			else{
				image=false;
				if (!displayed.equals("") || change){
					change=true;
					displayed="";
				}
			}
		}
		else if (selected_image==2){
			if (main.script.ageList.get(main.script.currentAge).image.flow!=null){
				if (!main.script.ageList.get(main.script.currentAge).image.flow.equals(displayed) || change){
					g.drawImage(main.script.ageList.get(main.script.currentAge).image.flow_img, 0, 0, this);
					change=true;
					displayed=main.script.ageList.get(main.script.currentAge).image.flow;
				}
			}
			else{
				image=false;
				if (!displayed.equals("") || change){
					change=true;
					displayed="";
				}
			}
		}
		else if (selected_image==3){
			if (main.script.ageList.get(main.script.currentAge).image.rail!=null){
				if (!main.script.ageList.get(main.script.currentAge).image.rail.equals(displayed) || change){
					g.drawImage(main.script.ageList.get(main.script.currentAge).image.rail_img, 0, 0, this);
					change=true;
					displayed=main.script.ageList.get(main.script.currentAge).image.rail;
				}
			}
			else{
				image=false;
				if (!displayed.equals("") || change){
					change=true;
					displayed="";
				}
			}
		}
		else if (selected_image==4){
			if (main.script.ageList.get(main.script.currentAge).image.area!=null){
				if (!main.script.ageList.get(main.script.currentAge).image.area.equals(displayed) || change){
					g.drawImage(main.script.ageList.get(main.script.currentAge).image.area_img, 0, 0, this);
					change=true;
					displayed=main.script.ageList.get(main.script.currentAge).image.area;
				}
			}
			else{
				image=false;
				if (!displayed.equals("") || change){
					change=true;
					displayed="";
				}
			}
		}
		else if (selected_image==5){
			if (main.script.ageList.get(main.script.currentAge).image.magnetic!=null){
				if (!main.script.ageList.get(main.script.currentAge).image.magnetic.equals(displayed) || change){
					g.drawImage(main.script.ageList.get(main.script.currentAge).image.magnetic_img, 0, 0, this);
					change=true;
					displayed=main.script.ageList.get(main.script.currentAge).image.magnetic;
				}
			}
			else{
				image=false;
				if (!displayed.equals("") || change){
					change=true;
					displayed="";
				}
			}
		}
		/////////////////////////////////////////////////////////////////////////////////	
		// draw area map
		if (change){ 
			
			int red,green,blue;
			
			if (image){
				for (int i=0;i<700;i+=2){
					for (int j=0;j<700;j+=2){
						red=area_map[i][j][0];
						if (!red_channel) red=0;
						green=area_map[i][j][1];
						if (!green_channel) green=0;
						blue=area_map[i][j][2];
						if (!blue_channel) blue=0;
						g.setColor(new Color(red,green,blue));
						g.drawLine(i, j, i, j);
						g.drawLine(i+1, j+1, i+1, j+1);
					}
				}
			}
			else{
				g.setColor(getBackground());
				g.fillRect(0,0, 1000,800);
				
				for (int i=0;i<700;i+=2){
					for (int j=0;j<700;j+=2){
						red=area_map[i][j][0];
						if (!red_channel) red=0;
						green=area_map[i][j][1];
						if (!green_channel) green=0;
						blue=area_map[i][j][2];
						if (!blue_channel) blue=0;
						g.setColor(new Color(red,green,blue));
						g.fillRect(i, j, 2, 2);
					}
				}
			}
			change=false;
		}
		
		///////////////////////////////////////////////////////////////
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
		
		////////////////////////////////////////////////////////////////
		// erase old values
		g.setColor(this.getBackground());
		g.fillRect(800, 590, 180, 30);
		g.fillRect(710, 140, 370, 50);
		g.fillRect(900, 200, 180, 240);
		
		/////////////////////////////////////////////////////////////////
		// display color selector
		g.setColor(Color.red);
		g.fillRect(730, 210, 20, 20);
		g.setColor(Color.green);
		g.fillRect(760, 210, 20, 20);
		g.setColor(Color.blue);
		g.fillRect(790, 210, 20, 20);
		
		g.setColor(Color.black);
		if (color==0) g.drawRect(727, 207, 25, 25);
		if (color==1) g.drawRect(757, 207, 25, 25);
		if (color==2) g.drawRect(787, 207, 25, 25);
		
		g.setColor(Color.black);
		g.fillRect(730, 240, 140, 20);
		if (value==0){
			g.setColor(Color.black);
			g.drawRect(727, 237, 145, 25);
		}
		
		for (int c=0;c<25;c++){
			if (color==0) g.setColor(new Color((c+1)*10,0,0));
			if (color==1) g.setColor(new Color(0,(c+1)*10,0));
			if (color==2) g.setColor(new Color(0,0,(c+1)*10));
			g.fillRect(730+30*(c%5), 270+30*(int)(c/5), 20, 20);

			if (c+1==value){
				g.setColor(Color.black);
				g.drawRect(727+30*(c%5), 267+30*(int)(c/5), 25, 25);
			}
		}
		
		g.setColor(Color.black);
		g.drawString("Selected area : "+areaID, 730, 430);
		
		
		//////////////////////////////////////////////////////////////////////
		// draw brush shape
		if (value==0) g.setColor(Color.black);
		else{
			if (color==0) g.setColor(new Color(red,0,0));
			if (color==1) g.setColor(new Color(0,green,0));
			if (color==2) g.setColor(new Color(0,0,blue));
		}
		g.fillOval(990-brush_size,300-brush_size,brush_size*2,brush_size*2);
		
		
		//////////////////////////////////////////////////////////////////////
		// display tool cursor
		g.setColor(Color.red);
		if (ex>=0 && ex<700 && ey>=0 && ey<700){
			g.drawOval(ex-brush_size,ey-brush_size,brush_size*2,brush_size*2);
		}

		
		g.setColor(Color.black);
		
		g.drawRect(710, 0, 370, 130);
		g.drawRect(710, 140, 370, 50);
		g.drawRect(710, 200, 180, 240);
		g.drawRect(900, 200, 180, 240);
		g.drawRect(710, 450, 370, 160);
		
		g.drawString("Load path", 720, 488);
		g.drawString("Save as", 720, 568);
		g.drawString(saveMsg, 801, 600);
		g.drawString("Color channels", 900, 25);
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==image) selected_image=0;
		if (e.getSource()==tactile) selected_image=1;
		if (e.getSource()==flow) selected_image=2;
		if (e.getSource()==rail) selected_image=3;
		if (e.getSource()==area) selected_image=4;
		if (e.getSource()==magnetic) selected_image=5;

		else if (e.getSource()==get){
			if (main.script.ageList.get(main.script.currentAge).image.area!=null){
				
				for (int i=0;i<700;i++){
					for (int j=0;j<700;j++){
						if (main.script.ageList.get(main.script.currentAge).image.area_mat[i][j][0]!=0)
							area_map[i][j][0]=(short)(main.script.ageList.get(main.script.currentAge).image.area_mat[i][j][0]*10);
						if (main.script.ageList.get(main.script.currentAge).image.area_mat[i][j][1]!=0)
							area_map[i][j][1]=(short)(main.script.ageList.get(main.script.currentAge).image.area_mat[i][j][1]*10);
						if (main.script.ageList.get(main.script.currentAge).image.area_mat[i][j][2]!=0)
							area_map[i][j][2]=(short)(main.script.ageList.get(main.script.currentAge).image.area_mat[i][j][2]*10);
					}
				}
				
			}
			pushBuffer();
		}
		else if (e.getSource()==clear){
			for (int i=0;i<700;i++){
				for (int j=0;j<700;j++){
					area_map[i][j][0]=0;
					area_map[i][j][1]=0;
					area_map[i][j][2]=0;
				}
			}
			pushBuffer();
		}
		
		else if (e.getSource()==undo) undo();
		else if (e.getSource()==redo) redo();
		
		else if (e.getSource()==red_box) red_channel=red_box.isSelected();
		else if (e.getSource()==green_box) green_channel=green_box.isSelected();
		else if (e.getSource()==blue_box) blue_channel=blue_box.isSelected();
		
		else if (e.getSource()==areaLoad) loadPath((String)list_area.getSelectedItem());
		else if (e.getSource()==areaSave){
			if (name.getText().length()>0) saveArea(name.getText());
		}
		
		this.repaint();
		
		change=true;
	}
	
	
	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	
	
	public void mousePressed(MouseEvent e) {
		int x=e.getX();
		int y=e.getY();
		
		// color selector
		if (x>730 && x<750 && y>210 && y<230) color=0;
		if (x>760 && x<780 && y>210 && y<230) color=1;
		if (x>790 && x<810 && y>210 && y<230) color=2;
		
		
		if (x>730 && x<870 && y>240 && y<260) value=0;
		for (short c=0;c<25;c++){
			if (x>730+30*(c%5) && x<750+30*(c%5) && y>270+30*(int)(c/5) && y<290+30*(int)(c/5)) value=(short) (c+1);
		}
		
		if (value==0){
			red=0;
			green=0;
			blue=0;
			areaID=0;
		}
		else{
			if (color==0){ // red
				red=(short) (value*10);
				green=-12;
				blue=-1;
				areaID=value;
			}
			if (color==1){ // green
				green=(short) (value*10);
				red=-12;
				blue=-1;
				areaID=(short) (value+25);
			}
			if (color==2){ // blue
				blue=(short) (value*10);
				green=-12;
				red=-1;
				areaID=(short) (value+50);
			}
		}
		change=true;
	}



	public void mouseReleased(MouseEvent e) {
		if (write) pushBuffer();
		write=false;
	}



	public void mouseDragged(MouseEvent e) {
		ex=e.getX();
		ey=e.getY();
		
		// write area id where brush is applyied
		for (int i=-brush_size;i<=brush_size;i++){
			for (int j=-brush_size;j<=brush_size;j++){
				if (i*i+j*j<brush_size*brush_size && ex+i>=0 && ex+i<700 && ey+j>=0 && ey+j<700){			
					if (red  >=0) area_map[ex+i][ey+j][0]=red;
					if (green>=0) area_map[ex+i][ey+j][1]=green;
					if (blue >=0) area_map[ex+i][ey+j][2]=blue;
					write=true;
				}
			}
		}
		change=true;
	}



	public void mouseMoved(MouseEvent e) {

		ex=e.getX();
		ey=e.getY();
		
		currentAreaID.clear();

		// detect areas
		if (ex>0 && ex<700 && ey>0 && ey<700){
			int r=area_map[ex][ey][0]/10;
			int g=area_map[ex][ey][1]/10;
			int b=area_map[ex][ey][2]/10;
			
			if (r==0 && g==0 && b==0) currentAreaID.add(0);
			else{
				if (r>0) currentAreaID.add(r);
				if (g>0) currentAreaID.add(g+25);
				if (b>0) currentAreaID.add(b+50);
			}
			change=true;
		}
	}
	
	
	
	public void loadPath(String file){
		main.println("Load path file "+file+" in path editor :");
		
		try {
			BufferedImage area_img = ImageIO.read(new File(Main.FILES+Main.AREA+file));
			
			for (int i=0;i<700;i++){
				for (int j=0;j<700;j++){
					area_map[i][j][0]=(short) ((area_img.getRGB(i, j)>> 16) & 0x000000FF);
					area_map[i][j][1]=(short) ((area_img.getRGB(i, j)>>  8) & 0x000000FF);
					area_map[i][j][2]=(short) ((area_img.getRGB(i, j)     ) & 0x000000FF);
				}
			}
		}
		catch (Exception e) {main.println("   /!\\ Path file not found or containing errors");}

		pushBuffer();
	}
	
	public void saveArea(String name){
		
		main.println("save area : "+name+".png");

		BufferedImage bufferedImage = new BufferedImage( 705, 705, BufferedImage.TYPE_INT_RGB );

		for (int i=0;i<700;i++){
			for (int j=0;j<700;j++){
				bufferedImage.setRGB(i, j, (new Color(area_map[i][j][0], area_map[i][j][1], area_map[i][j][2])).getRGB());
			}	
		}
		
		try {
		    ImageIO.write( bufferedImage, "png", new File(Main.FILES+Main.AREA+name+".png") ); }
		   
		    catch (Exception e) {
		     System.out.println("erreur enregistrement image..." );
		     e.printStackTrace();
		}
		

		main.listFiles();
		
		
		list_area.removeAll();
		for (int i=0;i<main.listArea.length;i++) list_area.addItem(main.listArea[i]);
		
	}
	
	
	public void pushBuffer(){
		while (area_buffer.size()>buffer+1) area_buffer.remove(area_buffer.size()-1);
		area_buffer.add(new short[700][700][3]);
		buffer++;
		for (int i=0;i<700;i++){
			for (int j=0;j<700;j++){
				area_buffer.get(buffer)[i][j][0]=area_map[i][j][0];
				area_buffer.get(buffer)[i][j][1]=area_map[i][j][1];
				area_buffer.get(buffer)[i][j][2]=area_map[i][j][2];
			}
		}
		if (buffer>MAX_UNDO){
			area_buffer.remove(0);
			buffer--;
		}
	}
	
	public void undo(){
		if (buffer>0){
			for (int i=0;i<700;i++){
				for (int j=0;j<700;j++){
					area_map[i][j][0]=area_buffer.get(buffer-1)[i][j][0];
					area_map[i][j][1]=area_buffer.get(buffer-1)[i][j][1];
					area_map[i][j][2]=area_buffer.get(buffer-1)[i][j][2];
				}
			}
			buffer--;
		}
	}
	
	public void redo(){
		if (buffer+1<area_buffer.size()){
			for (int i=0;i<700;i++){
				for (int j=0;j<700;j++){
					area_map[i][j][0]=area_buffer.get(buffer+1)[i][j][0];
					area_map[i][j][1]=area_buffer.get(buffer+1)[i][j][1];
					area_map[i][j][2]=area_buffer.get(buffer+1)[i][j][2];
				}
			}
			buffer++;
		}
	}


	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {}
	
	public void keyReleased(KeyEvent e) {
		
		if (e.getSource()==name){
			boolean found=false;
			int i=0;
			
			while (!found && i<main.listArea.length){
				if (main.listArea[i].equals(name.getText()+".png")) found=true;
				i++;
			}
			
			if (found) saveMsg="/!\\ file already exists !";
			else saveMsg="";
			
			if (name.getText().length()==0) areaSave.setEnabled(false);
			else areaSave.setEnabled(true);
		}
	}
	
}
