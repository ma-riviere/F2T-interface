package display;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import main.Main;


/**
 * File selection panel
 * @author simon gay
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
	
	private JComboBox<String> list_path;
	private JButton path;
	
	private JComboBox<String> list_script;
	private JButton script;
    
	
	public DisplayFilePanel(Main m, KeyboardListener k){
		main=m;
		
		this.setLayout(null);
		
		
		list_image=new JComboBox<String>();
		list_image.addItem("none");
		for (int i=0;i<main.listImages.length;i++) list_image.addItem(main.listImages[i]);
		list_image.addActionListener(this);
		this.add(list_image);
		list_image.setBounds(50, 5, 130, 30);
		KeyListener[] lis = list_image.getKeyListeners();
		for (int i = 0; i < lis.length; i++) {
			list_image.removeKeyListener(lis[i]);
		}
		list_image.addKeyListener(k);

		list_tactile=new JComboBox<String>();
		list_tactile.addItem("none");
		for (int i=0;i<main.listTactile.length;i++) list_tactile.addItem(main.listTactile[i]);
		list_tactile.addActionListener(this);
		list_tactile.setVisible(false);
		this.add(list_tactile);
		list_tactile.setBounds(50, 5, 130, 30);
		lis = list_tactile.getKeyListeners();
		for (int i = 0; i < lis.length; i++) {
			list_tactile.removeKeyListener(lis[i]);
		}
		list_tactile.addKeyListener(k);
		
		list_flow=new JComboBox<String>();
		list_flow.addItem("none");
		for (int i=0;i<main.listFlow.length;i++) list_flow.addItem(main.listFlow[i]);
		list_flow.addActionListener(this);
		list_flow.setVisible(false);
		this.add(list_flow);
		list_flow.setBounds(50,5, 130, 30);
		lis = list_flow.getKeyListeners();
		for (int i = 0; i < lis.length; i++) {
			list_flow.removeKeyListener(lis[i]);
		}
		list_flow.addKeyListener(k);
		
		list_rail=new JComboBox<String>();
		list_rail.addItem("none");
		for (int i=0;i<main.listRail.length;i++) list_rail.addItem(main.listRail[i]);
		list_rail.addActionListener(this);
		list_rail.setVisible(false);
		this.add(list_rail);
		list_rail.setBounds(50,5, 130, 30);
		lis = list_rail.getKeyListeners();
		for (int i = 0; i < lis.length; i++) {
			list_rail.removeKeyListener(lis[i]);
		}
		list_rail.addKeyListener(k);
		
		
		list_area=new JComboBox<String>();
		list_area.addItem("none");
		for (int i=0;i<main.listArea.length;i++) list_area.addItem(main.listArea[i]);
		list_area.addActionListener(this);
		list_area.setVisible(false);
		this.add(list_area);
		list_area.setBounds(50,5, 130, 30);
		lis = list_area.getKeyListeners();
		for (int i = 0; i < lis.length; i++) {
			list_area.removeKeyListener(lis[i]);
		}
		list_area.addKeyListener(k);
		
		
		list_magnetic=new JComboBox<String>();
		list_magnetic.addItem("none");
		for (int i=0;i<main.listMagnetic.length;i++) list_magnetic.addItem(main.listMagnetic[i]);
		list_magnetic.addActionListener(this);
		list_magnetic.setVisible(false);
		this.add(list_magnetic);
		list_magnetic.setBounds(50,5, 130, 30);
		lis = list_magnetic.getKeyListeners();
		for (int i = 0; i < lis.length; i++) {
			list_magnetic.removeKeyListener(lis[i]);
		}
		list_magnetic.addKeyListener(k);
		
		
		list_path=new JComboBox<String>();
		for (int i=0;i<main.listPath.length;i++) list_path.addItem(main.listPath[i]);
		list_path.addActionListener(this);
		this.add(list_path);
		list_path.setBounds(74,45, 106, 30);
		lis = list_path.getKeyListeners();
		for (int i = 0; i < lis.length; i++) {
			list_path.removeKeyListener(lis[i]);
		}
		list_path.addKeyListener(k);

		
		path=new JButton("Path");
		path.setFont(new Font("Arial", Font.PLAIN, 12));
		path.addActionListener(this);
		path.addKeyListener(k);
		this.add(path);
		path.setBounds(2, 45, 70, 30);
		

		list_script=new JComboBox<String>();
		for (int i=0;i<main.listScript.length;i++) list_script.addItem(main.listScript[i]);
		list_script.addActionListener(this);
		this.add(list_script);
		list_script.setBounds(74,75, 106, 30);
		lis = list_script.getKeyListeners();
		for (int i = 0; i < lis.length; i++) {
			list_script.removeKeyListener(lis[i]);
		}
		list_script.addKeyListener(k);
		
		
		script=new JButton("Script");
		script.setFont(new Font("Arial", Font.PLAIN, 12));
		script.addActionListener(this);
		script.addKeyListener(k);
		this.add(script);
		script.setBounds(2, 75, 70, 30);
		
	}
	
	
	
	
	public void paintComponent(Graphics g){

		g.setColor(this.getBackground());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		g.setColor(Color.black);
		
		// draw list names
		g.drawRect(0, 0, 185, 110);
		if (main.display!=null){
			if (main.display.selected_image==0) g.drawString("Image", 3, 25);
			if (main.display.selected_image==1) g.drawString("Tactile", 3, 25);
			if (main.display.selected_image==2) g.drawString("Flow", 3, 25);
			if (main.display.selected_image==3) g.drawString("Rails", 3, 25);
			if (main.display.selected_image==4) g.drawString("Areas", 3, 25);
			if (main.display.selected_image==5) g.drawString("Magnet", 3, 25);
		}
        
        try {Thread.sleep(20);
		} catch (InterruptedException e) {e.printStackTrace();}
	}

	

	public void updateSelectedImage(int selected){
		list_image.setVisible(false);
		list_tactile.setVisible(false);
		list_flow.setVisible(false);
		list_rail.setVisible(false);
		list_area.setVisible(false);
		list_magnetic.setVisible(false);
		
		if (selected==0)list_image.setVisible(true);
		else if (selected==1)list_tactile.setVisible(true);
		else if (selected==2)list_flow.setVisible(true);
		else if (selected==3)list_rail.setVisible(true);
		else if (selected==4)list_area.setVisible(true);
		else if (selected==5)list_magnetic.setVisible(true);
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

		list_path.removeAllItems();
		for (int i=0;i<main.listPath.length;i++) list_path.addItem(main.listPath[i]);
		
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
		if (e.getSource()==path  ) main.script.setPath((String)list_path.getSelectedItem());
		if (e.getSource()==script) main.script.loadScript((String)list_script.getSelectedItem());
		

		this.repaint();
		
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
