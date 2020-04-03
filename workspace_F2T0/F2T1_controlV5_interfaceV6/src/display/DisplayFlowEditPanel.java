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
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import structures.BezierCurveFlow;
import structures.FlowPoint;

import main.Main;

/**
 * Flow and rail edition tool
 * @author simon gay
 */

public class DisplayFlowEditPanel  extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener{
	
	private static final long serialVersionUID = 1L;
	
	public static int MAX_UNDO=50;
	
	private static int NBSLOTS=6;
	
	public Main main;
	public DisplayFrame frame;
	
	private int selected_image=0;
	
	public int selected_slot=0;
	
	private ButtonGroup imageMode;
	private JRadioButton image;
	private JRadioButton tactile;
	private JRadioButton flow;
	private JRadioButton rail;
	private JRadioButton area;
	private JRadioButton magnetic;
	
	private ButtonGroup slotNB;
	private JRadioButton[] slot;
	
	private JButton set;
	private JButton get;
	private JButton remove;
	private JButton add;
	private JButton clear;
	
	private JButton undo;
	private JButton redo;
	
	private JButton testFlow;
	private JButton testRail;
	private JButton clearFlow;
	
	
	
	private JComboBox<String> list_curves;
	private JButton curveLoad;
	
	private JTextField curveName;
	private JButton curveSave;
	private String saveCurveMsg="";
	
	private JTextField flowName;
	private JButton flowSave;
	private String saveFlowMsg="";
	
	private JTextField railName;
	private JButton railSave;
	private String saveRailMsg="";
	
	
	
	public BezierCurveFlow[] line;
	public ArrayList<BezierCurveFlow[]> line_buffer;
	public int buffer;
	
	
	public float[][][] flowMap;
	
	
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
	
	

	public DisplayFlowEditPanel(Main m, DisplayFrame f){
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
		get=new JButton("Get params");
		get.addActionListener(this);
		get.addKeyListener(f.keyboard);
		this.add(get);
		get.setBounds(725, 210, 120, 30);
		
		set=new JButton("Set params");
		set.addActionListener(this);
		set.addKeyListener(f.keyboard);
		this.add(set);
		set.setBounds(725, 245, 120, 30);
		
		remove=new JButton("Remove");
		remove.addActionListener(this);
		remove.addKeyListener(f.keyboard);
		this.add(remove);
		remove.setBounds(725, 280, 120, 30);
		
		add=new JButton("Add");
		add.addActionListener(this);
		add.addKeyListener(f.keyboard);
		this.add(add);
		add.setBounds(725, 315, 120, 30);
		
		clear=new JButton("Clear");
		clear.addActionListener(this);
		clear.addKeyListener(f.keyboard);
		this.add(clear);
		clear.setBounds(725, 350, 120, 30);
		
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
		
		
		testFlow=new JButton("Test flow");
		testFlow.addActionListener(this);
		testFlow.addKeyListener(f.keyboard);
		this.add(testFlow);
		testFlow.setBounds(725, 400, 110, 50);
	
		testRail=new JButton("Test rail");
		testRail.addActionListener(this);
		testRail.addKeyListener(f.keyboard);
		this.add(testRail);
		testRail.setBounds(840, 400, 110, 50);
		
		clearFlow=new JButton("Clear map");
		clearFlow.addActionListener(this);
		clearFlow.addKeyListener(f.keyboard);
		this.add(clearFlow);
		clearFlow.setBounds(955, 400, 110, 50);
		
		/////////////////////////////////
		
		list_curves=new JComboBox<String>();
		for (int i=0;i<main.listCurves.length;i++) list_curves.addItem(main.listCurves[i]);
		list_curves.addActionListener(this);
		list_curves.addKeyListener(f.keyboard);
		this.add(list_curves);
		list_curves.setBounds(800,490, 170, 30);
		
		curveLoad=new JButton("Load");
		curveLoad.addActionListener(this);
		curveLoad.addKeyListener(f.keyboard);
		this.add(curveLoad);
		curveLoad.setBounds(980, 490, 80, 30);

		
		/////////////////////////////////
		
		curveName = new JTextField();
		this.add(curveName);
		curveName.setBounds(800, 550, 170, 30);
		curveName.addKeyListener(this);
		curveName.setText(main.curveName);
		
		curveSave=new JButton("Save");
		curveSave.addActionListener(this);
		curveSave.addKeyListener(f.keyboard);
		this.add(curveSave);
		curveSave.setBounds(980, 550, 80, 30);
		
		flowName = new JTextField();
		this.add(flowName);
		flowName.setBounds(800, 600, 170, 30);
		flowName.addKeyListener(this);
		flowName.setText(main.flowName);
		
		flowSave=new JButton("Save");
		flowSave.addActionListener(this);
		flowSave.addKeyListener(f.keyboard);
		this.add(flowSave);
		flowSave.setBounds(980, 600, 80, 30);
		
		railName = new JTextField();
		this.add(railName);
		railName.setBounds(800, 650, 170, 30);
		railName.addKeyListener(this);
		railName.setText(main.railName);
		
		railSave=new JButton("Save");
		railSave.addActionListener(this);
		railSave.addKeyListener(f.keyboard);
		this.add(railSave);
		railSave.setBounds(980, 650, 80, 30);
		
		/////////////////////////////////
		line=new BezierCurveFlow[NBSLOTS];
		for (int l=0;l<NBSLOTS;l++) line[l]=new BezierCurveFlow();
		
		line_buffer=new ArrayList<BezierCurveFlow[]>();
		line_buffer.add(new BezierCurveFlow[NBSLOTS]);
		for (int l=0;l<NBSLOTS;l++){
			line_buffer.get(0)[l]=line[l].duplicate();
		}
		
		buffer=0;
		
		flowMap = new float[700][700][3];
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
		
		// vector flow
		g.setColor(Color.cyan);
		for (int i=0;i<700;i+=10){
			for (int j=0;j<700;j+=10){
				g.drawLine(i, j, i+(int)(flowMap[i][j][0]*20), j+(int)(flowMap[i][j][1]*20));
			}
		}
		
		
		// flow lines
		for (int l=0;l<NBSLOTS;l++){
			g.setColor(Color.darkGray);
			for (int i=0;i<line[l].sizeTarget();i++){
				if (selected_slot==l){
					g.setColor(Color.blue);
					g.drawLine(line[l].targetList.get(i).x+line[l].targetList.get(i).p1x,  line[l].targetList.get(i).y+line[l].targetList.get(i).p1y,
							   line[l].targetList.get(i).x+line[l].targetList.get(i).p2x,  line[l].targetList.get(i).y+line[l].targetList.get(i).p2y);
					g.fillOval(line[l].targetList.get(i).x+line[l].targetList.get(i).p1x-2,line[l].targetList.get(i).y+line[l].targetList.get(i).p1y-2, 5, 5);
				}
				
				if (selected_slot==l) g.setColor(Color.red);
				g.fillOval(line[l].targetList.get(i).x-2, line[l].targetList.get(i).y-2, 5, 5);
				
				if (selected_point==i && selected_slot==l){
					g.setColor(Color.magenta);
					g.drawOval(line[l].targetList.get(i).x-7, line[l].targetList.get(i).y-7, 16, 16);
				}
			}

			if (selected_slot==l) g.setColor(Color.blue);
			for (int i=0;i<line[l].sizePoint()-1;i++){
				g.drawLine((int)line[l].pointList.get(i  ).x, (int)line[l].pointList.get(i  ).y,
						   (int)line[l].pointList.get(i+1).x, (int)line[l].pointList.get(i+1).y);
			}
			
			if (selected_slot==l){
				g.setColor(Color.red);
				for (int i=0;i<line[l].sizePoint()-1;i++){
					g.drawLine((int)line[l].pointList.get(i).x, (int)line[l].pointList.get(i).y,
							   (int)(line[l].pointList.get(i).x+line[l].pointList.get(i).vx*10), (int)(line[l].pointList.get(i).y+line[l].pointList.get(i).vy*10));
				}
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
			
	 		g.setColor(Color.black);
	 		g.drawOval(325+(int)(main.x), 325-(int)(main.y), 50, 50);
	 		g.setColor(Color.white);
	 		g.drawOval(325+(int)(main.virtual.px), 325-(int)(main.virtual.py), 50, 50);
		}
		

		
		g.setColor(this.getBackground());
		g.fillRect(700, 0, 400, 700);

		g.setColor(Color.black);
		
		g.drawRect(710, 0, 370, 130);
		g.drawRect(710, 140, 370, 50);
		g.drawRect(710, 200, 370, 260);
		g.drawRect(710, 470, 370, 225);
		
		g.drawString("Load curve", 720, 510);
		
		g.drawString("Save curve", 720, 570);
		g.drawString(saveCurveMsg, 800, 592);

		g.drawString("Save flow", 720, 620);
		g.drawString(saveFlowMsg, 800, 642);
		
		g.drawString("Save rail", 720, 670);
		g.drawString(saveRailMsg, 800, 692);
		
		g.drawString("Selected point :", 725, 160);
		if (selected_point>=0 && selected_point<line[selected_slot].sizeTarget()){
			g.drawString("position: ("+line[selected_slot].targetList.get(selected_point).x+", "+line[selected_slot].targetList.get(selected_point).y+"), norm: "+line[selected_slot].targetList.get(selected_point).speed, 830, 160);
			g.drawString("range: "+line[selected_slot].targetList.get(selected_point).range2+" ("+line[selected_slot].targetList.get(selected_point).range1+") "+
						", Bezier: ("+line[selected_slot].targetList.get(selected_point).p1x+", "+line[selected_slot].targetList.get(selected_point).p1y+"), offset: ("+
						(int)line[selected_slot].targetList.get(selected_point).angle+"°, "+(int)(line[selected_slot].targetList.get(selected_point).dispersion*100/90)+"%)", 730, 180);
		}
		
		for (int i=0;i<NBSLOTS;i++){
			if (line[i].sizeTarget()==0) g.drawString("empty", 980, 20+20*i);
			else g.drawString(line[i].sizeTarget()+" points", 980, 20+20*i);
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
			if (selected_point>=0 && selected_point<line[selected_slot].sizeTarget()){
				line[selected_slot].remove(selected_point);
				if (selected_point>=line[selected_slot].sizeTarget()) selected_point--;
				update();
				pushBuffer();
			}
		}
		else if (e.getSource()==add){
			if (selected_point>0 && selected_point<line[selected_slot].sizeTarget()){
				int x1=(int)((line[selected_slot].targetList.get(selected_point).x + line[selected_slot].targetList.get(selected_point-1).x )/2);
				int y1=(int)((line[selected_slot].targetList.get(selected_point).y + line[selected_slot].targetList.get(selected_point-1).y )/2);
				int speed=(int)((line[selected_slot].targetList.get(selected_point).speed + line[selected_slot].targetList.get(selected_point-1).speed )/2);
				int range1=(int)((line[selected_slot].targetList.get(selected_point).range1 + line[selected_slot].targetList.get(selected_point-1).range1 )/2);
				int range2=(int)((line[selected_slot].targetList.get(selected_point).range2 + line[selected_slot].targetList.get(selected_point-1).range2 )/2);
				int angle=(int)((line[selected_slot].targetList.get(selected_point).angle + line[selected_slot].targetList.get(selected_point-1).angle )/2);
				int disp=(int)((line[selected_slot].targetList.get(selected_point).dispersion + line[selected_slot].targetList.get(selected_point-1).dispersion )/2);
				
				line[selected_slot].add(selected_point, new FlowPoint(x1, y1,range1, range2, speed,0,0, angle, disp));
				update();
				pushBuffer();
			}
		}
		else if (e.getSource()==clear){
			line[selected_slot].clear();
			update();
			pushBuffer();
		}
		
		else if (e.getSource()==undo){
			if (buffer>0){
				for (int l=0;l<NBSLOTS;l++){
					line[l]=line_buffer.get(buffer-1)[l].duplicate();
				}
				buffer--;
				update();
			}
		}
		else if (e.getSource()==redo){
			if (buffer+1<line_buffer.size()){
				for (int l=0;l<NBSLOTS;l++){
					line[l]=line_buffer.get(buffer+1)[l].duplicate();
				}
				buffer++;
				update();
			}
		}
		
		else if (e.getSource()==testFlow){
			main.currentAge.image.setFlow(flowMap);
		}
		else if (e.getSource()==testRail){
			main.currentAge.image.setRail(flowMap);
		}
		else if (e.getSource()==clearFlow){
			main.currentAge.image.clearFlow();
		}
		else if (e.getSource()==curveLoad) loadCurve((String)list_curves.getSelectedItem());
		else if (e.getSource()==curveSave) saveCurve((String)curveName.getText());
		else if (e.getSource()==flowSave) saveFlow((String)flowName.getText());
		else if (e.getSource()==railSave) saveRail((String)railName.getText());
		else if (e.getSource()==get){
			if (selected_point>=0 && selected_point<line[selected_slot].sizeTarget()){
				main.flow_speed=line[selected_slot].targetList.get(selected_point).speed;
				main.flow_range1=line[selected_slot].targetList.get(selected_point).range1;
				main.flow_range2=line[selected_slot].targetList.get(selected_point).range2;
				main.flow_angle=line[selected_slot].targetList.get(selected_point).angle;
				main.flow_dispersion=line[selected_slot].targetList.get(selected_point).dispersion;
				frame.flowPanel.repaint();
				update();
			}
		}
		else if (e.getSource()==set){
			if (selected_point>=0 && selected_point<line[selected_slot].sizeTarget()){
				line[selected_slot].targetList.get(selected_point).speed=main.flow_speed;
				line[selected_slot].targetList.get(selected_point).range1=main.flow_range1;
				line[selected_slot].targetList.get(selected_point).range2=main.flow_range2;
				line[selected_slot].targetList.get(selected_point).angle=main.flow_angle;
				line[selected_slot].targetList.get(selected_point).dispersion=main.flow_dispersion;
				frame.flowPanel.repaint();
				update();
			}
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
			
			// add new target point
			if (ex>=0 && ex<700 && ey>=0 && ey<700){
				line[selected_slot].targetList.add(new FlowPoint(ex,ey, (int)(main.flow_range2*main.flow_range1/100), main.flow_range2, main.flow_speed));
				selected_point=line[selected_slot].targetList.size()-1;
				write=true;
			}
		}
		
		if (click_right){
			
			// selection of a target point
			if (ex>0 && ex<700 && ey>0 && ey<700){
				double min=10;
				int imin=-1;
				double d=0;
				
				for (int i=0;i<line[selected_slot].sizeTarget();i++){
					d=Math.sqrt( (ex-(line[selected_slot].targetList.get(i).x))*(ex-(line[selected_slot].targetList.get(i).x)) 
							   + (ey-(line[selected_slot].targetList.get(i).y))*(ey-(line[selected_slot].targetList.get(i).y)));
					if (d<min){
						min=d;
						imin=i;
					}
				}
				
				if (imin>=0){
					selected_point=imin;

					if (e.isControlDown()){
						line[selected_slot].targetList.get(selected_point).resetBezier();
						write=true;
					}
					else{
						point_capture=true;
					}

					this.repaint();
				}
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
		
		update();
		
		this.repaint();
	}



	public void mouseDragged(MouseEvent e) {
		ex=e.getX();
		ey=e.getY();
		
		tx=ex;
		ty=ey;
		
		if (tx>700) tx=700;
		if (tx<0) tx=0;
		if (ty>700) ty=700;
		if (ty<0) ty=0;
		
		if (click_right && point_capture && !e.isShiftDown()){
			if (line[selected_slot].targetList.get(selected_point).x!=tx || line[selected_slot].targetList.get(selected_point).y!=ty) write=true;
			line[selected_slot].targetList.get(selected_point).x=tx;
			line[selected_slot].targetList.get(selected_point).y=ty;
			line[selected_slot].set();
		}
		
		if (click_right && selected_point>=0 && selected_point<line[selected_slot].sizeTarget() && e.isShiftDown()){
			line[selected_slot].setBezier(selected_point,ex, ey);
			write=true;
		}
		
		
		this.repaint();
	}



	public void mouseMoved(MouseEvent e) {

		ex=e.getX();
		ey=e.getY();
		
		tx= ex-350;
		ty=-ey+350;
		
		frame.updateFlowPanel(tx, ty);
		
		this.repaint();
	}
	
	
	public void pushBuffer(){
		while (line_buffer.size()>buffer+1) line_buffer.remove(line_buffer.size()-1);
		
		line_buffer.add(new BezierCurveFlow[NBSLOTS]);
		buffer++;
		for (int l=0;l<NBSLOTS;l++){
			line_buffer.get(buffer)[l]=line[l].duplicate();
		}
		
		if (buffer>MAX_UNDO){
			line_buffer.remove(0);
			buffer--;
		}
	}
	
	public void loadCurve(String file){
		main.println("Load path file "+file);
		
		String fileName = Main.FILES+Main.CURVES+file;
		String[] elements;
		
		try {
			InputStream ips=new FileInputStream(fileName); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String l;
			l=br.readLine();
			
			while (l!=null){
				
				main.println("   "+l);
				
				elements=l.split(" ");
				if (elements.length>=8){
						line[Integer.parseInt(elements[0])].targetList.add(
								new FlowPoint(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]),
										   Integer.parseInt(elements[3]), Integer.parseInt(elements[4]),Integer.parseInt(elements[5]),
										   Integer.parseInt(elements[6]), Integer.parseInt(elements[7]) ) );
				}
				else if (elements.length>=10){
					line[Integer.parseInt(elements[0])].targetList.add(
							new FlowPoint(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]),
									   Integer.parseInt(elements[3]), Integer.parseInt(elements[4]),Integer.parseInt(elements[5]),
									   Integer.parseInt(elements[6]), Integer.parseInt(elements[7]),
									   Float.parseFloat(elements[8]), Float.parseFloat(elements[9]) ) );
				}
				l=br.readLine();
			}
			br.close();
		}
		catch (Exception e) {main.println("   /!\\ Path file not found or containing errors");}
		pushBuffer();
		update();
	}
	
	public void saveCurve(String name){
		
		main.println("save curve : "+name);

		try {
			PrintWriter file  = new PrintWriter(new FileWriter(Main.FILES+Main.CURVES+name));
			for (int l=0;l<NBSLOTS;l++){
				for (int t=0;t<line[l].targetList.size();t++){
					file.println(l+" "+line[l].targetList.get(t).x+" "+line[l].targetList.get(t).y+" "+
									   line[l].targetList.get(t).range1+" "+line[l].targetList.get(t).range2+" "+line[l].targetList.get(t).speed+" "+
									   line[l].targetList.get(t).p1x+" "+line[l].targetList.get(t).p1y+" "+
									   line[l].targetList.get(t).angle+" "+line[l].targetList.get(t).dispersion);
				}
			}
			file.close();
			main.println("path saved");
		}
		catch (Exception e) {e.printStackTrace();}
		
		main.listFiles();
		
		list_curves.removeAllItems();
		for (int i=0;i<main.listCurves.length;i++) list_curves.addItem(main.listCurves[i]);
	}
	

	
	public void saveFlow(String name){
		
		main.println("save flow : "+name);
		
		BufferedImage bufferedImage;
		bufferedImage = new BufferedImage( 705, 705, BufferedImage.TYPE_INT_RGB );
		int rgb=0;
		for (int i=0;i<700;i++){
			for (int j=0;j<700;j++){
				rgb=256*256*(int)(Math.max(0, Math.min(255, flowMap[i][j][0]*128+128)))+256*(int)(Math.max(0, Math.min(255,-flowMap[i][j][1]*128+128)));
				bufferedImage.setRGB(i, j, rgb);
			}
		}
		
		try {
		    ImageIO.write( bufferedImage, "png", new File(Main.FILES+Main.FLOW+name+".png") );
		}catch (Exception e) {
		     main.println("error while saving image..." );
		     e.printStackTrace();
		}
		main.listFiles();
	}
	
	public void saveRail(String name){
		
		main.println("save rail : "+name);
		
		BufferedImage bufferedImage;
		bufferedImage = new BufferedImage( 705, 705, BufferedImage.TYPE_INT_RGB );

		int rgb=0;
		for (int i=0;i<700;i++){
			for (int j=0;j<700;j++){
				rgb=256*256*(int)(Math.max(0, Math.min(255, flowMap[i][j][0]*128+128)))+256*(int)(Math.max(0, Math.min(255,-flowMap[i][j][1]*128+128)));
				bufferedImage.setRGB(i, j, rgb);
			}
		}
		
		try {
		    ImageIO.write( bufferedImage, "png", new File(Main.FILES+Main.RAIL+name+".png") );
		}catch (Exception e) {
		     main.println("error while saving image..." );
		     e.printStackTrace();
		}
		main.listFiles();
	}

	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {}
	
	public void keyReleased(KeyEvent e) {
		
		if (e.getSource()==curveName){
			boolean found=false;
			int i=0;
			
			while (!found && i<main.listCurves.length){
				if (main.listCurves[i].equals(curveName.getText())) found=true;
				i++;
			}
			
			if (found) saveCurveMsg="/!\\ file already exists !";
			else saveCurveMsg="";
			
			if (curveName.getText().length()==0) curveSave.setEnabled(false);
			else curveSave.setEnabled(true);
		}
		
		if (e.getSource()==flowName){
			boolean found=false;
			int i=0;
			
			while (!found && i<main.listFlow.length){
				if ((main.listFlow[i]).equals(flowName.getText()+".png")) found=true;
				i++;
			}
			
			if (found){
				saveFlowMsg="/!\\ file already exists !";
			}
			else saveFlowMsg="";
			
			if (flowName.getText().length()==0) flowSave.setEnabled(false);
			else flowSave.setEnabled(true);
		}
		
		if (e.getSource()==railName){
			boolean found=false;
			int i=0;
			
			while (!found && i<main.listRail.length){
				if ((main.listRail[i]).equals(railName.getText()+".png")) found=true;
				i++;
			}
			
			if (found) saveRailMsg="/!\\ file already exists !";
			else saveRailMsg="";
			
			if (railName.getText().length()==0) railSave.setEnabled(false);
			else railSave.setEnabled(true);
		}
	}
	
	
	public void update(){
		
		for (int l=0;l<NBSLOTS;l++){
			line[l].update();
		}
		
		for (int i=0;i<700;i++){
			for (int j=0;j<700;j++){
				flowMap[i][j][0]=0;
				flowMap[i][j][1]=0;
				flowMap[i][j][2]=0;
			}
		}
		
		float d=0;
		float d2=0;
		float dist2=0;
		float dist=0;
		
		float x=0;
		float y=0;
		float speed=0;
		float angle=0;

		
		float vx=0;
		float vy=0;
		float vx2=0;
		float vy2=0;
		
		float vxt=0;
		float vyt=0;
		float scal=0;
		
		for (int l=0;l<NBSLOTS;l++){
			for (int i=0;i<line[l].sizePoint();i++){
				
				x=line[l].pointList.get(i).x;
				y=line[l].pointList.get(i).y;
				speed=line[l].pointList.get(i).speed;
				
				d=line[l].pointList.get(i).range2;
				d2=d*d;

				for (int i1=(int) -d;i1<d;i1++){
					for (int j1=(int) -d;j1<d;j1++){
						
						if (x+i1>=0 && x+i1<700 && y+j1>=0 && y+j1<700){
							
							dist2=i1*i1+j1*j1;
							
							if (dist2<d2){
								dist=(float)Math.sqrt((float)dist2);
								
								vx=line[l].pointList.get(i).vx;
								vy=line[l].pointList.get(i).vy;
								
								vxt=-vy;
								vyt=vx;
								
								scal=(float)i1*vxt +(float)j1*vyt ;
								
								
								angle=(float) Math.toRadians( line[l].pointList.get(i).dispersion*scal/d + line[l].pointList.get(i).angle );
								
								vx2=(float)(Math.cos(angle)*vx - Math.sin(angle)*vy);
								vy2=(float)(Math.sin(angle)*vx + Math.cos(angle)*vy);
								
								
								if (dist<line[l].pointList.get(i).range2*((float)line[l].pointList.get(i).range1/100)) speed=line[l].pointList.get(i).speed;
								else{
									speed= ((float)line[l].pointList.get(i).speed/((line[l].pointList.get(i).range2*(float)line[l].pointList.get(i).range1/100)-(float)line[l].pointList.get(i).range2)) * (dist-(float)line[l].pointList.get(i).range2);
								}
								
								if (dist<=1){
									flowMap[(int)(x+i1)][(int)(y+j1)][0]+= vx2*speed;
									flowMap[(int)(x+i1)][(int)(y+j1)][1]+= vy2*speed;
									flowMap[(int)(x+i1)][(int)(y+j1)][2]+= 1;
								}
								else{
									flowMap[(int)(x+i1)][(int)(y+j1)][0]+= vx2*speed * ((-1/(float)line[l].pointList.get(i).range2)*dist+1);
									flowMap[(int)(x+i1)][(int)(y+j1)][1]+= vy2*speed * ((-1/(float)line[l].pointList.get(i).range2)*dist+1);
									flowMap[(int)(x+i1)][(int)(y+j1)][2]+= ((-1/(float)line[l].pointList.get(i).range2)*dist+1);
								}
							}
						}
					}
				}
			}
		}
		
		for (int i=0;i<700;i++){
			for (int j=0;j<700;j++){
				if (flowMap[i][j][2]>0){
					flowMap[i][j][0]=flowMap[i][j][0]/flowMap[i][j][2]/100;
					flowMap[i][j][1]=flowMap[i][j][1]/flowMap[i][j][2]/100;
				}
			}
		}
	}
}
