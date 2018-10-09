package display;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	private JComboBox<String> list_area;
	
	private JComboBox<String> list_preset;
	private JComboBox<String> list_source;
	
	private JButton preset;
	private JButton source;
	
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
		
		
		list_area=new JComboBox<String>();
		list_area.addItem("none");
		for (int i=0;i<main.listArea.length;i++) list_area.addItem(main.listArea[i]);
		list_area.addActionListener(this);
		list_area.addKeyListener(k);
		this.add(list_area);
		list_area.setBounds(70,125, 150, 30);
		
		
		list_preset=new JComboBox<String>();
		for (int i=0;i<main.listPreset.length;i++) list_preset.addItem(main.listPreset[i]);
		list_preset.addActionListener(this);
		list_preset.addKeyListener(k);
		this.add(list_preset);
		list_preset.setBounds(70,175, 170, 30);

		list_source=new JComboBox<String>();
		for (int i=0;i<main.listSource.length;i++) list_source.addItem(main.listSource[i]);
		list_source.addActionListener(this);
		list_source.addKeyListener(k);
		this.add(list_source);
		list_source.setBounds(70,235, 170, 30);
		
		preset=new JButton("Load");
		preset.addActionListener(this);
		preset.addKeyListener(k);
		this.add(preset);
		preset.setBounds(265, 175, 80, 30);
		
		source=new JButton("Load");
		source.addActionListener(this);
		source.addKeyListener(k);
		this.add(source);
		source.setBounds(265, 235, 80, 30);
		
		
		list_script=new JComboBox<String>();
		for (int i=0;i<main.listScript.length;i++) list_script.addItem(main.listScript[i]);
		list_script.addActionListener(this);
		list_script.addKeyListener(k);
		this.add(list_script);
		list_script.setBounds(70,285, 170, 30);
		
		script=new JButton("Load");
		script.addActionListener(this);
		script.addKeyListener(k);
		this.add(script);
		script.setBounds(265, 285, 80, 30);
		
		
		rescan=new JButton("Rescan");
		rescan.addActionListener(this);
		rescan.addKeyListener(k);
		this.add(rescan);
		rescan.setBounds(245, 20, 100, 40);
		
		savePreset=new JButton("<html><center> Save<br>preset</center></html>");
		savePreset.addActionListener(this);
		savePreset.addKeyListener(k);
		this.add(savePreset);
		savePreset.setBounds(245, 90, 100, 40);
		
	}
	
	
	
	
	public void paintComponent(Graphics g){

		g.setColor(Color.black);
		
		// draw list names
		g.drawRect(0, 0, 225, 160);
		g.drawString("Image", 10, 25);
		g.drawString("Areas", 10, 145);
		
		g.drawRect(0, 170, 355, 100);
		g.drawString("Preset", 10, 195);
		g.drawString("Sources", 10, 255);
		
		g.drawRect(0, 280, 355, 40);
		g.drawString("Script", 10, 305);
		
		
		// preset bloc
		g.drawRect(235, 0, 120, 160);

        
        try {Thread.sleep(20);
		} catch (InterruptedException e) {e.printStackTrace();}
	}

	

	
	
	public void rescan(){
		list_image.removeAllItems();
		list_image.addItem("none");
		for (int i=0;i<main.listImages.length;i++) list_image.addItem(main.listImages[i]);

		list_area.removeAllItems();
		list_area.addItem("none");
		for (int i=0;i<main.listArea.length;i++) list_area.addItem(main.listArea[i]);
		
		
		list_preset.removeAllItems();
		for (int i=0;i<main.listPreset.length;i++) list_preset.addItem(main.listPreset[i]);

		list_source.removeAllItems();
		for (int i=0;i<main.listSource.length;i++) list_source.addItem(main.listSource[i]);
		
		list_script.removeAllItems();
		for (int i=0;i<main.listScript.length;i++) list_script.addItem(main.listScript[i]);
	}
	


	public void actionPerformed(ActionEvent e) {
		
		///////////////////////////////////////////////////////////////////////
		if (e.getSource()==list_image){
			if (list_image.getSelectedIndex()==0) main.setPicture(null);
			else main.setPicture((String)list_image.getSelectedItem());
		}
		if (e.getSource()==list_area){
			if (list_area.getSelectedIndex()==0) main.setArea(null);
			else main.setArea((String)list_area.getSelectedItem());
		}
		
		///////////////////////////////////////////////////////////////////////
		if (e.getSource()==preset) main.script.setPreset((String)list_preset.getSelectedItem(),1);
		if (e.getSource()==source) main.script.setSources((String)list_source.getSelectedItem());
		
		///////////////////////////////////////////////////////////////////////
		if (e.getSource()==script) main.script.loadScript((String)list_script.getSelectedItem());
		
		///////////////////////////////////////////////////////////////////////
		if (e.getSource()==rescan) main.listFiles();
		if (e.getSource()==savePreset) main.savePreset();
		
		this.repaint();
	}
	
	public void updateIndex(int id){
		if (id==0 || id==-1) list_image.setSelectedIndex(main.selected_img+1);
		if (id==4 || id==-1) list_area.setSelectedIndex(main.selected_area+1);
	}

}
