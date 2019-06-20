package display;

import main.Target;

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

import script.BezierCurve;

import main.Main;

public class DisplayPathEditPanel  extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener{
	
	private static final long serialVersionUID = 1L;
	
	public static int MAX_UNDO=50;
	
	private static int NBSLOTS=5;
	
	public Main main;
	public DisplayFrame frame;
	
	private int selected_image=0;
	
	private int selected_slot=0;
	
	private ButtonGroup imageMode;
	private JRadioButton image;
	private JRadioButton tactile;
	private JRadioButton flow;
	private JRadioButton rail;
	private JRadioButton area;
	private JRadioButton magnetic;
	
	private ButtonGroup slotNB;
	private JRadioButton[] slot;
	
	private JButton remove;
	private JButton add;
	private JButton set;
	private JButton clear;
	
	private JButton undo;
	private JButton redo;
	
	private JButton test;
	private JButton get;
	private JButton setSymb;
	
	private JComboBox<String> list_path;
	private JButton pathLoad;
	
	private JComboBox<String> list_symbol;
	private JButton symbolLoad;
	
	private JTextField pathName;
	private JButton pathSave;
	private String savePathMsg="";
	
	private JTextField symbolName;
	private JButton symbolSave;
	private String saveSymbolMsg="";
	
	public BezierCurve[] path;
	public ArrayList<BezierCurve>[] path_buffer;
	public int buffer[];
	
	public boolean click_right=false;
	public boolean click_left=false;
	public boolean click_middle=false;
	
	public int selected_point=0;
	public boolean point_capture=false;
	public boolean write=false;
	
	public int ex=0;
	public int ey=0;
	
	public int tx=0;
	public int ty=0;
	
	

	public DisplayPathEditPanel(Main m, DisplayFrame f){
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
		
		slotNB 	= new ButtonGroup();
		slot 	= new JRadioButton[NBSLOTS];
		for (int i=0;i<NBSLOTS;i++){
			slot[i]=new JRadioButton("Slot "+(i+1));
			this.add(slot[i]);
			slot[i].setBounds(900, 5+20*i, 80, 20);
			slot[i].addActionListener(this);
			slot[i].addKeyListener(f.keyboard);
			slotNB.add(slot[i]);
		}
		slot[0].setSelected(true);
		
		/////////////////////////////////
		remove=new JButton("Remove");
		remove.addActionListener(this);
		remove.addKeyListener(f.keyboard);
		this.add(remove);
		remove.setBounds(725, 210, 100, 30);
		
		add=new JButton("Add");
		add.addActionListener(this);
		add.addKeyListener(f.keyboard);
		this.add(add);
		add.setBounds(725, 245, 100, 30);
		
		set=new JButton("Set");
		set.addActionListener(this);
		set.addKeyListener(f.keyboard);
		this.add(set);
		set.setBounds(725, 280, 100, 30);
		
		clear=new JButton("Clear");
		clear.addActionListener(this);
		clear.addKeyListener(f.keyboard);
		this.add(clear);
		clear.setBounds(725, 315, 100, 30);
		
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
		
		
		test=new JButton("Test !");
		test.addActionListener(this);
		test.addKeyListener(f.keyboard);
		this.add(test);
		test.setBounds(725, 370, 110, 50);
		
		get=new JButton("Get path");
		get.addActionListener(this);
		get.addKeyListener(f.keyboard);
		this.add(get);
		get.setBounds(840, 370, 110, 50);
		
		setSymb=new JButton("Set symbol");
		setSymb.addActionListener(this);
		setSymb.addKeyListener(f.keyboard);
		this.add(setSymb);
		setSymb.setBounds(955, 370, 115, 50);
		
		/////////////////////////////////
		
		list_path=new JComboBox<String>();
		for (int i=0;i<main.listPath.length;i++) list_path.addItem(main.listPath[i]);
		list_path.addActionListener(this);
		list_path.addKeyListener(f.keyboard);
		this.add(list_path);
		list_path.setBounds(800,470, 170, 30);
		
		pathLoad=new JButton("Load");
		pathLoad.addActionListener(this);
		pathLoad.addKeyListener(f.keyboard);
		this.add(pathLoad);
		pathLoad.setBounds(980, 470, 80, 30);
		
		
		list_symbol=new JComboBox<String>();
		for (int i=0;i<main.symbolList.size();i++) list_symbol.addItem(main.symbolList.get(i).name);
		list_symbol.addActionListener(this);
		list_symbol.addKeyListener(f.keyboard);
		this.add(list_symbol);
		list_symbol.setBounds(800,510, 170, 30);
		
		symbolLoad=new JButton("Load");
		symbolLoad.addActionListener(this);
		symbolLoad.addKeyListener(f.keyboard);
		this.add(symbolLoad);
		symbolLoad.setBounds(980, 510, 80, 30);
		
		/////////////////////////////////
		
		pathName = new JTextField();
		this.add(pathName);
		pathName.setBounds(800, 550, 170, 30);
		pathName.addKeyListener(this);
		pathName.setText(main.pathName);
		
		pathSave=new JButton("Save");
		pathSave.addActionListener(this);
		pathSave.addKeyListener(f.keyboard);
		this.add(pathSave);
		pathSave.setBounds(980, 550, 80, 30);
		
		
		symbolName = new JTextField();
		this.add(symbolName);
		symbolName.setBounds(800, 610, 170, 30);
		symbolName.addKeyListener(this);
		symbolName.setText(main.symbolName);
		
		symbolSave=new JButton("Save");
		symbolSave.addActionListener(this);
		symbolSave.addKeyListener(f.keyboard);
		this.add(symbolSave);
		symbolSave.setBounds(980, 610, 80, 30);
		
		/////////////////////////////////
		path=new BezierCurve[NBSLOTS];
		path_buffer=new ArrayList[NBSLOTS];
		buffer=new int[NBSLOTS];
		
		for (int i=0;i<NBSLOTS;i++){
			path[i]=new BezierCurve();
			path_buffer[i]=new ArrayList<BezierCurve>();
			path_buffer[i].add(new BezierCurve());
			buffer[i]=0;
		}
	}
	
	
	public void paintComponent(Graphics g){
		
		g.setColor(this.getBackground());
		g.fillRect(695, 0, 30, 700);
		g.fillRect(975, 5, 80, 20*NBSLOTS);
		g.fillRect(800, 580, 180, 100);
		g.fillRect(710, 140, 370, 50);
		
		
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
		// draw target sequence
		if (path[selected_slot].sizeTarget()>0){
			
			
			for (int i=0;i<path[selected_slot].sizeTarget();i++){

				int vx1=(int)path[selected_slot].targetList.get(i  ).x;
				int vy1=(int)path[selected_slot].targetList.get(i  ).y;
				
				int speed=path[selected_slot].targetList.get(i).speed/2;
				if (speed>255) speed=255;
				if (speed<0) speed=0;
				if (path[selected_slot].targetList.get(i).control==0) g.setColor(new Color(speed,255-speed,255));
				else g.setColor(new Color(speed,255-speed/2,0));
				g.fillOval(vx1+350-4, 350-vy1-4, 10, 10);
				
				int x2=vx1+(int)(path[selected_slot].targetList.get(i).p1x);
				int y2=vy1+(int)(path[selected_slot].targetList.get(i).p1y);
				int x3=vx1-(int)(path[selected_slot].targetList.get(i).p1x);
				int y3=vy1-(int)(path[selected_slot].targetList.get(i).p1y);

				g.setColor(Color.red);
				g.drawLine(x2+350,350-y2,x3+350,350-y3);
				g.fillOval(x2+350-2, 350-y2-2, 5,5);
				
				
				if (selected_point==i){
					g.setColor(Color.magenta);
					g.drawOval(vx1+350-7, 350-vy1-7, 16, 16);
				}
			}	
		}
		
		// draw target sequence
		if (path[selected_slot].sizePoint()>0){
			
			
			for (int i=1;i<path[selected_slot].sizePoint();i++){
				
				int vx1=(int)path[selected_slot].pointList.get(i-1).x;
				int vy1=(int)path[selected_slot].pointList.get(i-1).y;
				int vx2=(int)path[selected_slot].pointList.get(i  ).x;
				int vy2=(int)path[selected_slot].pointList.get(i  ).y;
				
				int speed=path[selected_slot].pointList.get(i).speed/2;
				if (speed>255) speed=255;
				if (speed<0) speed=0;
				if (path[selected_slot].pointList.get(i).control==0) g.setColor(new Color(speed,255-speed,255));
				else g.setColor(new Color(speed,255-speed/2,0));
				g.drawLine(vx1+350, 350-vy1, vx2+350, 350-vy2);
			}	
		}
		

		
		g.setColor(Color.black);
		
		g.drawRect(710, 0, 370, 130);
		g.drawRect(710, 140, 370, 50);
		g.drawRect(710, 200, 370, 240);
		g.drawRect(710, 450, 370, 245);
		
		g.drawString("Load path", 720, 488);
		g.drawString("Save path", 720, 568);
		g.drawString(savePathMsg, 801, 592);
		
		g.drawString("Load symbol", 720, 528);
		g.drawString("Save symbol", 720, 628);
		g.drawString(saveSymbolMsg, 801, 652);
		
		g.drawString("Selected point :", 725, 160);
		if (selected_point>=0 && selected_point<path[selected_slot].sizeTarget()){
			g.drawString("position : ("+path[selected_slot].targetList.get(selected_point).x+", "+path[selected_slot].targetList.get(selected_point).y+")", 830, 160);
			
			if (path[selected_slot].targetList.get(selected_point).control==1) 
				g.drawString("speed : "+path[selected_slot].targetList.get(selected_point).speed+", type : control", 830, 180);
			else
				g.drawString("force : "+path[selected_slot].targetList.get(selected_point).speed+", type : attractor", 830, 180);
		}
		
		for (int i=0;i<NBSLOTS;i++){
			if (buffer[i]==0 && path[i].sizeTarget()==0) g.drawString("empty", 980, 20+20*i);
			else g.drawString(path[i].sizeTarget()+" points", 980, 20+20*i);
		}	
		

	}
	
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==image) selected_image=0;
		if (e.getSource()==tactile) selected_image=1;
		if (e.getSource()==flow) selected_image=2;
		if (e.getSource()==rail) selected_image=3;
		if (e.getSource()==area) selected_image=4;
		if (e.getSource()==magnetic) selected_image=5;
		
		for (int i=0;i<NBSLOTS;i++){
			if (e.getSource()==slot[i]) selected_slot=i;
		}
		
		if (e.getSource()==remove){
			if (selected_point>=0 && selected_point<path[selected_slot].sizeTarget()){
				path[selected_slot].remove(selected_point);
				if (selected_point>=path[selected_slot].sizeTarget()) selected_point--;
				pushBuffer();
			}
		}
		else if (e.getSource()==add){
			if (selected_point>0 && selected_point<path[selected_slot].sizeTarget()){
				int x1=(int)((path[selected_slot].targetList.get(selected_point).x + path[selected_slot].targetList.get(selected_point-1).x )/2);
				int y1=(int)((path[selected_slot].targetList.get(selected_point).y + path[selected_slot].targetList.get(selected_point-1).y )/2);
				
				path[selected_slot].add(selected_point, new Target(x1, y1,main.target_speed, main.target_type));
				pushBuffer();
			}
		}
		else if (e.getSource()==set){
			if (selected_point>=0 && selected_point<path[selected_slot].sizeTarget()){
				path[selected_slot].targetList.get(selected_point).speed=main.target_speed;
				path[selected_slot].targetList.get(selected_point).control=main.target_type;
				path[selected_slot].set();
			}
			pushBuffer();
		}
		else if (e.getSource()==setSymb){
			if (list_symbol.getSelectedIndex()>=0 && list_symbol.getSelectedIndex()<list_symbol.getItemCount())
				main.symbolList.get(list_symbol.getSelectedIndex()).set((int)main.virtual.px, (int)main.virtual.py);
		}
		else if (e.getSource()==clear){
			path[selected_slot].clear();
			pushBuffer();
		}
		else if (e.getSource()==undo){
			if (buffer[selected_slot]>0){
				path[selected_slot].clear();
				for (int i=0;i<path_buffer[selected_slot].get(buffer[selected_slot]-1).sizeTarget();i++){
					path[selected_slot].add(path_buffer[selected_slot].get(buffer[selected_slot]-1).targetList.get(i).duplicate());
				}
				buffer[selected_slot]--;
			}
		}
		else if (e.getSource()==redo){
			if (buffer[selected_slot]+1<path_buffer[selected_slot].size()){
				path[selected_slot].clear();
				for (int i=0;i<path_buffer[selected_slot].get(buffer[selected_slot]+1).sizeTarget();i++){
					path[selected_slot].add(path_buffer[selected_slot].get(buffer[selected_slot]+1).targetList.get(i).duplicate());
				}
				buffer[selected_slot]++;
			}
		}
		else if (e.getSource()==test){
			for (int i=0;i<path[selected_slot].sizeTarget();i++){
				main.script.ageList.get(main.script.currentAge).targetSequence.add(path[selected_slot].targetList.get(i).duplicate());
			}
		}
		else if (e.getSource()==get){
			for (int i=0;i<main.script.ageList.get(main.script.currentAge).targetSequence.sizeTarget();i++){
				path[selected_slot].add(main.script.ageList.get(main.script.currentAge).targetSequence.targetList.get(i).duplicate());
			}
		}
		else if (e.getSource()==pathLoad) loadPath((String)list_path.getSelectedItem());
		else if (e.getSource()==pathSave){
			if (pathName.getText().length()>0) savePath(pathName.getText());
		}
		else if (e.getSource()==symbolLoad) loadSymbol((String)list_symbol.getSelectedItem());
		else if (e.getSource()==symbolSave){
			if (symbolName.getText().length()>0) saveSymbol(symbolName.getText());
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
		
		if (!click_left && !click_right && !click_middle){
			if (e.getButton()==MouseEvent.BUTTON3){
				click_right=true;
			}
			else if (e.getButton()==MouseEvent.BUTTON1){
				click_left=true;
			}
			else if (e.getButton()==MouseEvent.BUTTON2){
				click_middle=true;
			}
		}
		
		
		if (click_left){
			if (!e.isShiftDown()){
				// add target
				if (tx>-345 && tx<345 && ty>-345 && ty<345){
					path[selected_slot].add(new Target(tx, ty,main.target_speed, main.target_type));
					pushBuffer();
				}
			}
			else{
				if (selected_point>=0 && selected_point<path[selected_slot].sizeTarget()){
					path[selected_slot].resetBezier(selected_point);
				}
			}
		}
		
		if (click_right){
			
			if (tx>-345 && tx<345 && ty>-345 && ty<345){
				double min=10;
				int imin=-1;
				double d=0;
				
				for (int i=0;i<path[selected_slot].sizeTarget();i++){
					d=Math.sqrt( (tx-(path[selected_slot].targetList.get(i).x))*(tx-(path[selected_slot].targetList.get(i).x)) 
							   + (ty-(path[selected_slot].targetList.get(i).y))*(ty-(path[selected_slot].targetList.get(i).y)));
					if (d<min){
						min=d;
						imin=i;
					}
				}
				
				if (imin>=0){
					selected_point=imin;
					
					if (e.isControlDown()){
						path[selected_slot].targetList.get(selected_point).resetBezier();
						write=true;
						path[selected_slot].set();
					}
					else{
						point_capture=true;
					}
				}
			}
		}
		
		if (click_middle){
			if (!e.isShiftDown()){
				if (list_symbol.getSelectedIndex()>=0 && list_symbol.getSelectedIndex()<list_symbol.getItemCount())
					main.symbolList.get(list_symbol.getSelectedIndex()).set(tx, ty);
			}
		}

		this.repaint();
	}



	public void mouseReleased(MouseEvent e) {
		if (e.getButton()==MouseEvent.BUTTON3) click_right=false;
		if (e.getButton()==MouseEvent.BUTTON1) click_left=false;
		if (e.getButton()==MouseEvent.BUTTON2) click_middle=false;
		point_capture=false;
		if (write) pushBuffer();
		write=false;
		
		this.repaint();
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
		
		if (click_right && point_capture && !e.isShiftDown()){
			if (path[selected_slot].targetList.get(selected_point).x!=tx || path[selected_slot].targetList.get(selected_point).y!=ty) write=true;
			path[selected_slot].targetList.get(selected_point).x=tx;
			path[selected_slot].targetList.get(selected_point).y=ty;
			path[selected_slot].set();
		}
		
		
		
		if (click_right && selected_point>=0 && selected_point<path[selected_slot].sizeTarget() && e.isShiftDown()){
			path[selected_slot].setBezier(selected_point,tx, ty);
			write=true;
		}
		
		
		this.repaint();
	}



	public void mouseMoved(MouseEvent e) {

		ex=e.getX();
		ey=e.getY();
		
		tx= ex-350;
		ty=-ey+350;
		
		frame.updatePathPanel(tx, ty);
		
		this.repaint();
	}
	
	
	public void pushBuffer(){	
		while (path_buffer[selected_slot].size()>buffer[selected_slot]+1) path_buffer[selected_slot].remove(path_buffer[selected_slot].size()-1);
		
		path_buffer[selected_slot].add(new BezierCurve());
		buffer[selected_slot]++;
		for (int i=0;i<path[selected_slot].sizeTarget();i++){
			path_buffer[selected_slot].get(buffer[selected_slot]).add(path[selected_slot].targetList.get(i).duplicate());
		}
		if (buffer[selected_slot]>MAX_UNDO){
			path_buffer[selected_slot].remove(0);
			buffer[selected_slot]--;
		}
	}
	
	public void loadPath(String file){
		main.println("Load path file "+file+" in path editor :");
		
		String fileName = Main.FILES+Main.PATH+file;
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
				if (elements.length>=5 && elements[0].equals("t")){
					if (elements.length<7)
						path[selected_slot].add(new Target(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]),
														   Integer.parseInt(elements[3]), Integer.parseInt(elements[4]) ));
					else
						path[selected_slot].add(new Target(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]),
														   Integer.parseInt(elements[3]), Integer.parseInt(elements[4]), 
														   Float.parseFloat(elements[5]), Float.parseFloat(elements[6]) ));
				}
				line=br.readLine();
			}
			br.close();
		}
		catch (Exception e) {main.println("   /!\\ Path file not found or containing errors");}
		pushBuffer();
	}
	
	public void loadSymbol(String file){
		main.println("Load symbol file "+file+" in path editor :");
		
		String fileName = Main.FILES+Main.SYMBOL+file;
		String[] elements;
		
		try {
			InputStream ips=new FileInputStream(fileName); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line;
			line=br.readLine();
			
			int i=0;
			
			while (line!=null){
				
				main.println("   "+line);
				
				elements=line.split(" ");
				if (elements.length>=5 && elements[0].equals("t")){
					if (elements.length<7)
						path[selected_slot].add(i,new Target(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]),
													     	 Integer.parseInt(elements[3]), Integer.parseInt(elements[4]) ));
					else
						path[selected_slot].add(new Target(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]),
														   Integer.parseInt(elements[3]), Integer.parseInt(elements[4]), 
														   Float.parseFloat(elements[5]), Float.parseFloat(elements[6]) ));
					i++;
				}
				line=br.readLine();
			}
			br.close();
		}
		catch (Exception e) {main.println("   /!\\ Symbol file not found or containing errors");}
		pushBuffer();
	}
	
	public void savePath(String name){
		
		main.println("save path : "+name);
		
		String fileName = Main.FILES+Main.PATH+name;
		
		try {
			PrintWriter file  = new PrintWriter(new FileWriter(fileName));
			for (int t=0;t<path[selected_slot].sizeTarget();t++){
				file.println("t "+path[selected_slot].getString(t));
			}
			
			file.close();
			main.println("path saved");
		}
		catch (Exception e) {e.printStackTrace();}
		
		main.listFiles();
		
		list_path.removeAll();
		for (int i=0;i<main.listPath.length;i++) list_path.addItem(main.listPath[i]);
	}
	
	public void saveSymbol(String name){
		
		main.println("save symbol : "+name);
		
		String fileName = Main.FILES+Main.SYMBOL+name;
		
		try {
			PrintWriter file  = new PrintWriter(new FileWriter(fileName));
			for (int t=0;t<path[selected_slot].sizeTarget();t++){
				file.println("t "+path[selected_slot].getString(t));
			}
			
			file.close();
			main.println("symbol saved");
		}
		catch (Exception e) {e.printStackTrace();}
		
		main.listFiles();
		
		list_symbol.removeAll();
		for (int i=0;i<main.symbolList.size();i++) list_symbol.addItem(main.symbolList.get(i).name);
	}


	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {}
	
	public void keyReleased(KeyEvent e) {
		
		if (e.getSource()==pathName){
			boolean found=false;
			int i=0;
			
			while (!found && i<main.listPath.length){
				if (main.listPath[i].equals(pathName.getText())) found=true;
				i++;
			}
			
			if (found) savePathMsg="/!\\ file already exists !";
			else savePathMsg="";
			
			if (pathName.getText().length()==0) pathSave.setEnabled(false);
			else pathSave.setEnabled(true);
		}
		
		if (e.getSource()==symbolName){
			boolean found=false;
			int i=0;
			
			while (!found && i<main.symbolList.size()){
				if (main.symbolList.get(i).name.equals(symbolName.getText())) found=true;
				i++;
			}
			
			if (found) saveSymbolMsg="/!\\ file already exists !";
			else saveSymbolMsg="";
			
			if (symbolName.getText().length()==0) symbolSave.setEnabled(false);
			else symbolSave.setEnabled(true);
			
			
		}
	}
	
}
