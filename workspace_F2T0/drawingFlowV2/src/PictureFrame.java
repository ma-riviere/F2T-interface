
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


public class PictureFrame extends JFrame implements ActionListener, KeyListener{

	private static final long serialVersionUID = 1L;

	Main main;
	
	protected PicturePanel panel;
	
	
	private JButton undo;
	private JButton redo;
	
	private JButton set;
	private JButton get;
	
	private JButton add;
	private JButton remove;
	private JButton clear;
	
	private ButtonGroup slotNB;
	public JRadioButton[] slot;
	
	private JButton load;
	
	private JTextField flowName;
	private JTextField railName;
	private JTextField curveName;
	private JButton saveFlow;
	private JButton saveRail;
	private JButton saveCurve;
	private JLabel saveFlowMsg;
	private JLabel saveRailMsg;
	private JLabel saveCurveMsg;
	
	public JComboBox<String> list_image;
	
	public JComboBox<String> list_curves;
	
	public PictureFrame(Main m){
		
		main=m;

		this.setTitle("Flow");
    	this.setSize(1240, 740);
    	this.setLocationRelativeTo(null);               
    	this.setVisible(true);
    	
    	
    	panel=new PicturePanel(m);
    	panel.setLayout(null);
    	this.setContentPane(panel);
    	panel.setBounds(0, 0, 1220, 740);
    	
 
		
    	slotNB 	= new ButtonGroup();
		slot 	= new JRadioButton[10];
		for (int i=0;i<10;i++){
			slot[i]=new JRadioButton((i+1)+": empty");
			this.add(slot[i]);
			slot[i].setBounds(750, 390+18*i, 100, 18);
			slot[i].setLayout(null);
			slot[i].addActionListener(this);
			slotNB.add(slot[i]);
		}
		slot[0].setSelected(true);
		
		
		set=new JButton("Set");
		set.addActionListener(this);
		this.add(set);
		set.setBounds(880, 395, 100, 30);
		
		get=new JButton("Get");
		get.addActionListener(this);
		this.add(get);
		get.setBounds(880, 430, 100, 30);
		
		add=new JButton("Add");
		add.addActionListener(this);
		this.add(add);
		add.setBounds(880, 465, 100, 30);
		
		remove=new JButton("Remove");
		remove.addActionListener(this);
		this.add(remove);
		remove.setBounds(880, 500, 100, 30);
		
		clear=new JButton("Clear");
		clear.addActionListener(this);
		this.add(clear);
		clear.setBounds(880, 535, 100, 30);
		
		
		undo=new JButton("Undo");
		undo.addActionListener(this);
		this.add(undo);
		undo.setBounds(1050, 400, 100, 50);
		
		redo=new JButton("Redo");
		redo.addActionListener(this);
		this.add(redo);
		redo.setBounds(1050, 480, 100, 50);
		
		
		list_image=new JComboBox<String>();
		list_image.addItem("none");
		for (int i=0;i<main.listImages.length;i++) list_image.addItem(main.listImages[i]);
		list_image.addActionListener(this);
		this.add(list_image);
		list_image.setBounds(750, 350, 150, 30);
		
		list_curves=new JComboBox<String>();
		for (int i=0;i<main.listCurves.length;i++) list_curves.addItem(main.listCurves[i]);
		list_curves.addActionListener(this);
		this.add(list_curves);
		list_curves.setBounds(950, 350, 150, 30);
		
		load=new JButton("Load");
		load.addActionListener(this);
		this.add(load);
		load.setBounds(1110, 350, 70, 30);
		
		curveName = new JTextField();
		this.add(curveName);
		curveName.addKeyListener(this);
		curveName.setBounds(750, 585, 140, 30);
		curveName.setText(main.curveName);
		
		flowName = new JTextField();
		this.add(flowName);
		flowName.addKeyListener(this);
		flowName.setBounds(900, 585, 140, 30);
		flowName.setText(main.flowName);
		
		railName = new JTextField();
		this.add(railName);
		railName.addKeyListener(this);
		railName.setBounds(1050, 585, 140, 30);
		railName.setText(main.railName);
		
		saveCurve=new JButton("Save Curve");
		saveCurve.addActionListener(this);
		this.add(saveCurve);
		saveCurve.setBounds(750, 620, 140, 50);
		
		saveFlow=new JButton("Save Flow");
		saveFlow.addActionListener(this);
		this.add(saveFlow);
		saveFlow.setBounds(900, 620, 140, 50);
		
		saveRail=new JButton("Save Rail");
		saveRail.addActionListener(this);
		this.add(saveRail);
		saveRail.setBounds(1050, 620, 140, 50);
		
		
		saveCurveMsg=new JLabel();
		this.add(saveCurveMsg);
		saveCurveMsg.setBounds(750, 675, 140, 15);
		
		saveFlowMsg=new JLabel();
		this.add(saveFlowMsg);
		saveFlowMsg.setBounds(900, 675, 140, 15);
		
		saveRailMsg=new JLabel();
		this.add(saveRailMsg);
		saveRailMsg.setBounds(1050, 675, 140, 15);
		
    	
    	this.addWindowListener(new WindowAdapter() {
  	      public void windowClosing(WindowEvent e) {
  	    	//main.inter.close();
  	        System.exit(0);
  	      }
  	    });
	}

	public void actionPerformed(ActionEvent e) {
		
		for (int i=0;i<10;i++){
			if (e.getSource()==slot[i]) main.selectedLine=i;
		}
		
		if (e.getSource()==list_image){
			if (list_image.getSelectedIndex()==0) main.setPicture(null);
			else main.setPicture((String)list_image.getSelectedItem());
		}
			
			
		if (e.getSource()==undo) main.undoLine();
		else if (e.getSource()==redo) main.redoLine();
		else if (e.getSource()==get){
			if (panel.selected_point>=0 && panel.selected_point<main.line[main.selectedLine].sizeTarget()){
				main.length=main.line[main.selectedLine].targetList.get(panel.selected_point).speed;
				main.width1=main.line[main.selectedLine].targetList.get(panel.selected_point).range1;
				main.width2=main.line[main.selectedLine].targetList.get(panel.selected_point).range2;
				main.angle=main.line[main.selectedLine].targetList.get(panel.selected_point).angle;
				main.dispersion=main.line[main.selectedLine].targetList.get(panel.selected_point).dispersion;
				main.update();
			}
		}
		else if (e.getSource()==set){
			if (panel.selected_point>=0 && panel.selected_point<main.line[main.selectedLine].sizeTarget()){
				main.line[main.selectedLine].targetList.get(panel.selected_point).speed=main.length;
				main.line[main.selectedLine].targetList.get(panel.selected_point).range1=(int)main.width1;
				main.line[main.selectedLine].targetList.get(panel.selected_point).range2=main.width2;
				main.line[main.selectedLine].targetList.get(panel.selected_point).angle=main.angle;
				main.line[main.selectedLine].targetList.get(panel.selected_point).dispersion=main.dispersion;
				main.update();
			}
		}
		else if (e.getSource()==add){
			if (panel.selected_point>0 && panel.selected_point<main.line[main.selectedLine].sizeTarget()){
				int id=panel.selected_point;
				int line=main.selectedLine;
				main.line[line].targetList.add(id,
									new Target( (main.line[line].targetList.get(id).x+main.line[line].targetList.get(id-1).x)/2 ,
											    (main.line[line].targetList.get(id).y+main.line[line].targetList.get(id-1).y)/2 ,
											    (main.line[line].targetList.get(id).range1+main.line[line].targetList.get(id-1).range1)/2 ,
											    (main.line[line].targetList.get(id).range2+main.line[line].targetList.get(id-1).range2)/2 ,
											    (main.line[line].targetList.get(id).speed +main.line[line].targetList.get(id-1).speed )/2 ,
											    0,0,
											    (main.line[line].targetList.get(id).angle +main.line[line].targetList.get(id-1).angle )/2,
											    (main.line[line].targetList.get(id).dispersion +main.line[line].targetList.get(id-1).dispersion )/2));
				main.update();
				main.pushBufferLine();
			}
		}
		else if (e.getSource()==remove){
			if (panel.selected_point>=0 && panel.selected_point<main.line[main.selectedLine].sizeTarget()){
				main.line[main.selectedLine].targetList.remove(panel.selected_point);
				main.update();
				main.pushBufferLine();
			}
		}
		else if (e.getSource()==clear){
			main.line[main.selectedLine].targetList.clear();
			main.update();
			main.pushBufferLine();
		}
		
		else if (e.getSource()==saveCurve) main.saveCurve(curveName.getText());
		else if (e.getSource()==saveFlow) main.saveFlow(flowName.getText());
		else if (e.getSource()==saveRail) main.saveRail(railName.getText());
		else if (e.getSource()==load) main.loadCurve((String)list_curves.getSelectedItem());
		
		this.repaint();
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
			
			if (found) saveCurveMsg.setText("/!\\ file exists !");
			else saveCurveMsg.setText("");
		}
		if (e.getSource()==flowName){
			boolean found=false;
			int i=0;
			
			while (!found && i<main.listFlow.length){
				if (main.listFlow[i].equals(flowName.getText())) found=true;
				i++;
			}
			
			if (found) saveFlowMsg.setText("/!\\ file exists !");
			else saveFlowMsg.setText("");
		}
		if (e.getSource()==railName){
			boolean found=false;
			int i=0;
			
			while (!found && i<main.listRail.length){
				if (main.listRail[i].equals(railName.getText())) found=true;
				i++;
			}
			
			if (found) saveRailMsg.setText("/!\\ file exists !");
			else saveRailMsg.setText("");
		}
	}
	
}
