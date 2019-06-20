package display;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Comparator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import main.Main;



/**
 * Display the probe input
 * @author simon gay
 */

/* - inherited from EnvPanel :
 *   Agent agent      : pointer to the agent
 */
public class DisplayFilePanel extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = 1L;


	private Main main;
	
	private JComboBox<String> list_image;
	private JComboBox<String> list_tactile;
	private JComboBox<String> list_flow;
	private JComboBox<String> list_rail;
	private JComboBox<String> list_area;
	private JComboBox<String> list_magnetic;
	
	private JComboBox<String> list_preset;
	private JComboBox<String> list_path;
	private JComboBox<String> list_source;
	private JComboBox<String> list_association;
	
	private JButton preset;
	private JButton path;
	private JButton source;
	private JButton association;
	
	private JComboBox<String> list_script;
	private JButton script;
	
	private JButton rescan;
	private JButton savePreset;
     
	
	//private boolean guide_mode=false;
	
	public DisplayFilePanel(Main m, KeyboardListener k){
		main=m;
		
		this.setLayout(null);
		
		
		list_image=new JComboBox<String>();
		list_image.addItem("none");
		for (int i=0;i<main.listImages.length;i++) list_image.addItem(main.listImages[i]);
		list_image.addActionListener(this);
		list_image.addKeyListener(k);
		this.add(list_image);
		list_image.setBounds(70, 5, 150, 30);
		
		list_tactile=new JComboBox<String>();
		list_tactile.addItem("none");
		for (int i=0;i<main.listTactile.length;i++) list_tactile.addItem(main.listTactile[i]);
		list_tactile.addActionListener(this);
		list_tactile.addKeyListener(k);
		this.add(list_tactile);
		list_tactile.setBounds(70, 35, 150, 30);
		
		list_flow=new JComboBox<String>();
		list_flow.addItem("none");
		for (int i=0;i<main.listFlow.length;i++) list_flow.addItem(main.listFlow[i]);
		list_flow.addActionListener(this);
		list_flow.addKeyListener(k);
		this.add(list_flow);
		list_flow.setBounds(70,65, 150, 30);
		
		list_rail=new JComboBox<String>();
		list_rail.addItem("none");
		for (int i=0;i<main.listRail.length;i++) list_rail.addItem(main.listRail[i]);
		list_rail.addActionListener(this);
		list_rail.addKeyListener(k);
		this.add(list_rail);
		list_rail.setBounds(70,95, 150, 30);
		
		list_area=new JComboBox<String>();
		list_area.addItem("none");
		for (int i=0;i<main.listArea.length;i++) list_area.addItem(main.listArea[i]);
		list_area.addActionListener(this);
		list_area.addKeyListener(k);
		this.add(list_area);
		list_area.setBounds(70,125, 150, 30);
		
		list_magnetic=new JComboBox<String>();
		list_magnetic.addItem("none");
		for (int i=0;i<main.listMagnetic.length;i++) list_magnetic.addItem(main.listMagnetic[i]);
		list_magnetic.addActionListener(this);
		list_magnetic.addKeyListener(k);
		this.add(list_magnetic);
		list_magnetic.setBounds(70,155, 150, 30);
		
		list_preset=new JComboBox<String>();
		for (int i=0;i<main.listPreset.length;i++) list_preset.addItem(main.listPreset[i]);
		list_preset.addActionListener(this);
		list_preset.addKeyListener(k);
		this.add(list_preset);
		list_preset.setBounds(70,205, 170, 30);
		
		list_path=new JComboBox<String>();
		for (int i=0;i<main.listPath.length;i++) list_path.addItem(main.listPath[i]);
		list_path.addActionListener(this);
		list_path.addKeyListener(k);
		this.add(list_path);
		list_path.setBounds(70,235, 170, 30);
		
		list_source=new JComboBox<String>();
		for (int i=0;i<main.listSource.length;i++) list_source.addItem(main.listSource[i]);
		list_source.addActionListener(this);
		list_source.addKeyListener(k);
		this.add(list_source);
		list_source.setBounds(70,265, 170, 30);
		
		list_association=new JComboBox<String>();
		for (int i=0;i<main.listAssociation.length;i++) list_association.addItem(main.listAssociation[i]);
		list_association.addActionListener(this);
		list_association.addKeyListener(k);
		this.add(list_association);
		list_association.setBounds(70,295, 170, 30);
		
		
		preset=new JButton("Load");
		preset.addActionListener(this);
		preset.addKeyListener(k);
		this.add(preset);
		preset.setBounds(265, 205, 80, 30);
		
		path=new JButton("Load");
		path.addActionListener(this);
		path.addKeyListener(k);
		this.add(path);
		path.setBounds(265, 235, 80, 30);
		
		source=new JButton("Load");
		source.addActionListener(this);
		source.addKeyListener(k);
		this.add(source);
		source.setBounds(265, 265, 80, 30);
		
		association=new JButton("Load");
		association.addActionListener(this);
		association.addKeyListener(k);
		this.add(association);
		association.setBounds(265, 295, 80, 30);
		
		
		list_script=new JComboBox<String>();
		for (int i=0;i<main.listScript.length;i++) list_script.addItem(main.listScript[i]);
		list_script.addActionListener(this);
		list_script.addKeyListener(k);
		this.add(list_script);
		list_script.setBounds(70,345, 170, 30);
		
		script=new JButton("Load");
		script.addActionListener(this);
		script.addKeyListener(k);
		this.add(script);
		script.setBounds(265, 345, 80, 30);
		
		
		rescan=new JButton("Rescan");
		rescan.addActionListener(this);
		rescan.addKeyListener(k);
		this.add(rescan);
		rescan.setBounds(245, 20, 120, 50);
		
		savePreset=new JButton("<html><center> Save<br>preset</center></html>");
		savePreset.addActionListener(this);
		savePreset.addKeyListener(k);
		this.add(savePreset);
		savePreset.setBounds(245, 90, 120, 50);
		
	}
	
	
	
	
	public void paintComponent(Graphics g){

		g.setColor(Color.black);
		
		// draw list names
		g.drawRect(0, 0, 225, 190);
		g.drawString("Image", 10, 25);
		g.drawString("Tactile", 10, 55);
		g.drawString("Flow", 10, 85);
		g.drawString("Rails", 10, 115);
		g.drawString("Areas", 10, 145);
		g.drawString("Magnet", 10, 175);
		
		g.drawRect(0, 200, 375, 130);
		g.drawString("Preset", 10, 225);
		g.drawString("Path", 10, 255);
		g.drawString("Sources", 10, 285);
		g.drawString("Assoc.", 10, 315);
		
		g.drawRect(0, 340, 375, 40);
		g.drawString("Script", 10, 365);
		
		
		// preset bloc
		g.drawRect(235, 0, 140, 190);

        
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
		
		list_magnetic.removeAllItems();
		list_magnetic.addItem("none");
		for (int i=0;i<main.listMagnetic.length;i++) list_magnetic.addItem(main.listMagnetic[i]);
		
		
		list_preset.removeAllItems();
		for (int i=0;i<main.listPreset.length;i++) list_preset.addItem(main.listPreset[i]);
		
		list_path.removeAllItems();
		for (int i=0;i<main.listPath.length;i++) list_path.addItem(main.listPath[i]);
		
		list_source.removeAllItems();
		for (int i=0;i<main.listSource.length;i++) list_source.addItem(main.listSource[i]);
		
		list_association.removeAllItems();
		for (int i=0;i<main.listAssociation.length;i++) list_association.addItem(main.listAssociation[i]);
		
		list_script.removeAllItems();
		for (int i=0;i<main.listScript.length;i++) list_script.addItem(main.listScript[i]);
	}
	


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
		if (e.getSource()==list_magnetic){
			if (list_magnetic.getSelectedIndex()==0) main.setMagnetic(null);
			else main.setMagnetic((String)list_magnetic.getSelectedItem());
		}
		
		///////////////////////////////////////////////////////////////////////
		if (e.getSource()==preset) main.script.setPreset((String)list_preset.getSelectedItem(),1);
		if (e.getSource()==path  ) main.script.setPath((String)list_path.getSelectedItem());
		if (e.getSource()==source) main.script.setSource((String)list_source.getSelectedItem());
		if (e.getSource()==association) main.script.setAssociation((String)list_association.getSelectedItem());
		
		///////////////////////////////////////////////////////////////////////
		if (e.getSource()==script) main.script.loadScript((String)list_script.getSelectedItem());
		
		///////////////////////////////////////////////////////////////////////
		if (e.getSource()==rescan) main.listFiles();
		if (e.getSource()==savePreset) main.savePreset();
		
		this.repaint();
		
		main.display.updateAreaEdit();
	}
	
	public void updateIndex(int id){
		if (id==0 || id==-1) list_image.setSelectedIndex(main.selected_img+1);
		if (id==1 || id==-1) list_tactile.setSelectedIndex(main.selected_tactile+1);
		if (id==2 || id==-1) list_flow.setSelectedIndex(main.selected_flow+1);
		if (id==3 || id==-1) list_rail.setSelectedIndex(main.selected_rail+1);
		if (id==4 || id==-1) list_area.setSelectedIndex(main.selected_area+1);
		if (id==5 || id==-1) list_magnetic.setSelectedIndex(main.selected_magnetic+1);
	}

}
