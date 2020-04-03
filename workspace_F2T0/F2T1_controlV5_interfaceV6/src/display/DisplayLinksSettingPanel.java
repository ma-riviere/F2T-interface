package display;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import main.Main;

/**
 * link edition panel
 * @author simon gay
 */

public class DisplayLinksSettingPanel extends JPanel implements MouseListener, ActionListener, KeyListener{
	
	private static final long serialVersionUID = 1L;
	

	public Main main;
	public DisplayFrame frame;
	
	
	private JComboBox<String> list_sound;
	private JComboBox<String> list_end;
	private JComboBox<String> list_repeat;
	
	private DefaultListModel<String> associationList;
	private JList<String> associations;
	private JScrollPane associationScroll;
	
	private JButton association_add;
	private JButton association_remove;
	private JButton association_up;
	private JButton association_down;
	
	private JTextField name;
	private JButton association_save;
	private String saveMsg="";
	
	
	private JComboBox<String> list_ages;
	
	private DefaultListModel<String> ageList;
	private JList<String> ages;
	private JScrollPane ageScroll;

	private JCheckBox reboot = new JCheckBox("Reboot");
	
	private JButton exit_add;
	private JButton exit_remove;
	
	public DisplayLinksSettingPanel(Main m, DisplayFrame f, KeyboardListener k){
		main=m;
		frame=f;
		
		addMouseListener(this);
		addKeyListener(this);
		this.setLayout(null);
		
		list_sound=new JComboBox<String>();
		for (int i=0;i<main.listSound.length;i++) list_sound.addItem(main.listSound[i]);
		list_sound.addActionListener(this);
		list_sound.addKeyListener(k);
		this.add(list_sound);
		list_sound.setBounds(10,80, 150, 30);
		
		list_end=new JComboBox<String>();
		list_end.addItem("quit area");
		list_end.addItem("other area");
		list_end.addItem("sound ends");
		list_end.addActionListener(this);
		list_end.addKeyListener(k);
		this.add(list_end);
		list_end.setBounds(170,80, 120, 30);
		
		list_repeat=new JComboBox<String>();
		list_repeat.addItem("no");
		list_repeat.addItem("yes");
		list_repeat.addActionListener(this);
		list_repeat.addKeyListener(k);
		this.add(list_repeat);
		list_repeat.setBounds(300,80, 60, 30);
		
		
		associationList = new DefaultListModel<String>();
		associations=new JList<String>(associationList);
		associations.addKeyListener(k);

		associationScroll = new JScrollPane(associations, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(associationScroll);
		associationScroll.setBounds(10,130, 250, 160);
		
		
		association_add=new JButton("Add");
		association_add.addActionListener(this);
		association_add.addKeyListener(k);
		this.add(association_add);
		association_add.setBounds(270, 135, 90, 30);
		
		association_remove=new JButton("Remove");
		association_remove.addActionListener(this);
		association_remove.addKeyListener(k);
		this.add(association_remove);
		association_remove.setBounds(270, 175, 90, 30);
		

		
		association_up=new JButton("Up");
		association_up.addActionListener(this);
		association_up.addKeyListener(k);
		this.add(association_up);
		association_up.setBounds(270, 215, 90, 30);
		
		association_down=new JButton("Down");
		association_down.addActionListener(this);
		association_down.addKeyListener(k);
		this.add(association_down);
		association_down.setBounds(270, 255, 90, 30);
		
		
		association_save=new JButton("Save");
		association_save.addActionListener(this);
		association_save.addKeyListener(k);
		this.add(association_save);
		association_save.setBounds(270, 315, 90, 30);
		
		name = new JTextField();
		this.add(name);
		name.setBounds(80, 315, 180, 30);
		name.addKeyListener(this);
		name.setText(main.associationName);
		
		
		list_ages=new JComboBox<String>();
		for (int i=0;i<main.listAge.length;i++) list_ages.addItem(main.listAge[i]);
		list_ages.addActionListener(this);
		list_ages.addKeyListener(k);
		this.add(list_ages);
		list_ages.setBounds(10,390, 200, 30);
		
		this.add(reboot);
		reboot.setBounds(250, 390, 80, 35);
		reboot.setSelected(false);
		reboot.addActionListener(this);
		reboot.addKeyListener(k);
		
		exit_add=new JButton("Add link");
		exit_add.addActionListener(this);
		exit_add.addKeyListener(k);
		this.add(exit_add);
		exit_add.setBounds(30, 440, 140, 40);
		
		exit_remove=new JButton("Remove link");
		exit_remove.addActionListener(this);
		exit_remove.addKeyListener(k);
		this.add(exit_remove);
		exit_remove.setBounds(200, 440, 140, 40);
		
		ageList = new DefaultListModel<String>();
		ages=new JList<String>(ageList);
		ages.addKeyListener(k);

		ageScroll = new JScrollPane(ages, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(ageScroll);
		ageScroll.setBounds(10,500, 340, 180);
	}
	
	
	public void update(){
		associationList.clear();
		for (int i=0;i<main.currentAge.areas.areas.size();i++){
			associationList.addElement(main.currentAge.areas.areas.get(i)+" "
		                              +main.currentAge.areas.types.get(i)+" "
					                  +main.currentAge.areas.repeat.get(i)+" "
		                              +main.currentAge.areas.soundFiles.get(i));
		}
		
		ageList.clear();
		for (int i=0;i<main.currentAge.ages.size();i++){
			ageList.addElement(main.currentAge.exitAreasId.get(i)+" "
		                              +main.currentAge.ages.get(i)+" "
					                  +main.currentAge.reboot.get(i));
		}
	}
	
	
	public void paintComponent(Graphics g){
		
		g.setColor(this.getBackground());
		g.fillRect(0,0, this.getWidth(), this.getHeight());
		
		// area selection
		
		g.setColor(Color.black);
		g.drawRect(0,0, 369, 40);
		
		g.setColor(Color.lightGray);
		if (frame.selectedArea<=25) g.fillRect(110, 5, 60, 30);
		else if (frame.selectedArea<=50) g.fillRect(200, 5, 60, 30);
		else g.fillRect(290, 5, 60, 30);
		g.setColor(Color.black);
		if (frame.selectedArea<=25) g.drawRect(110, 5, 60, 30);
		else if (frame.selectedArea<=50) g.drawRect(200, 5, 60, 30);
		else g.drawRect(290, 5, 60, 30);
		
		
		
		g.drawString("selected area:",10,25);
		
		if (frame.areaRed>=0){
			g.setColor(new Color(frame.areaRed*10,0,0));
			g.fillRect(120,10,20,20);
			g.setColor(Color.black);
			g.drawString(""+frame.areaRed, 145, 25);
		}
		else{
			g.setColor(Color.black);
			g.drawRect(120, 10, 20, 20);
		}
		
		if (frame.areaGreen>=0){
			g.setColor(new Color(0,frame.areaGreen*10,0));
			g.fillRect(210,10,20,20);
			g.setColor(Color.black);
			g.drawString(""+(frame.areaGreen+25), 235, 25);
		}
		else{
			g.setColor(Color.black);
			g.drawRect(210, 10, 20, 20);
		}
		
		if (frame.areaBlue>=0){
			g.setColor(new Color(0,0,frame.areaBlue*10));
			g.fillRect(300,10,20,20);
			g.setColor(Color.black);
			g.drawString(""+(frame.areaBlue+50), 325, 25);
		}
		else{
			g.setColor(Color.black);
			g.drawRect(300, 10, 20, 20);
		}
		

		// area associations
		g.setColor(Color.black);
		g.drawRect(0,50, 369, 300);
		
		g.drawString("sound:", 60, 70);
		g.drawString("ends when:", 190, 70);
		g.drawString("repeat:", 305, 70);
		
		// exits list
		g.drawRect(0,360, 369, 330);
		
		g.drawString("Available Ages:", 60, 380);
		
		g.drawString("Save as", 20, 335);
		g.drawString(saveMsg, 80, 310);
	}


	public void mouseClicked(MouseEvent e) {
		
		int x=e.getX();
		int y=e.getY();
		
		if (frame.areaRed>=0 && x>=120 && x<180 && y>=5 && y<30) frame.selectedArea=frame.areaRed;
		else if (frame.areaGreen>0 && x>=210 && x<270 && y>=5 && y<30) frame.selectedArea=frame.areaGreen+25;
		else if (frame.areaBlue>0 && x>=300 && x<360 && y>=5 && y<30) frame.selectedArea=frame.areaBlue+50;
		
		this.repaint();
	}
	
	
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}




	public void actionPerformed(ActionEvent e) {

		if (e.getSource()==association_add){
			main.currentAge.areas.stopSound();
			String line1="a "+frame.selectedArea+" "+(list_end.getSelectedIndex())+" "+(list_repeat.getSelectedIndex()+" "+list_sound.getSelectedItem());
			main.script.setCommandLine(line1);
			update();
		}
		else if (e.getSource()==association_remove){
			int[] selected=associations.getSelectedIndices();

			main.currentAge.areas.stopSound();
			for (int i=selected.length-1;i>=0;i--){
				main.currentAge.areas.areas.remove(selected[i]);
				main.currentAge.areas.types.remove(selected[i]);
				main.currentAge.areas.repeat.remove(selected[i]);
				main.currentAge.areas.soundFiles.remove(selected[i]);
			}
			update();
		}
		else if (e.getSource()==association_save){
			if (name.getText().length()>0) main.saveAssociation(name.getText());
		}
		else if (e.getSource()==association_up){
			int[] selected=associations.getSelectedIndices();
			
			if (selected.length>0){
				ArrayList<Integer> areas=new ArrayList<Integer>();
				ArrayList<Integer> types=new ArrayList<Integer>();
				ArrayList<Integer> repeat=new ArrayList<Integer>();
				ArrayList<String> sounds=new ArrayList<String>();
				
				main.currentAge.areas.stopSound();
				
				for (int i=selected.length-1;i>=0;i--){
					areas.add(main.currentAge.areas.areas.get(selected[i]));
					types.add(main.currentAge.areas.types.get(selected[i]));
					repeat.add(main.currentAge.areas.repeat.get(selected[i]));
					sounds.add(main.currentAge.areas.soundFiles.get(selected[i]));
					
					main.currentAge.areas.areas.remove(selected[i]);
					main.currentAge.areas.types.remove(selected[i]);
					main.currentAge.areas.repeat.remove(selected[i]);
					main.currentAge.areas.soundFiles.remove(selected[i]);
				}
				
				int id=selected[0]-1;
				if (id<0) id=0;
				
				for (int i=0;i<selected.length;i++){
					main.currentAge.areas.areas.add(id, areas.get(i));
					main.currentAge.areas.types.add(id, types.get(i));
					main.currentAge.areas.repeat.add(id, repeat.get(i));
					main.currentAge.areas.soundFiles.add(id, sounds.get(i));
				}
				
				update();
				
				for (int i=0;i<selected.length;i++){
					selected[i]=id+i;
				}
				associations.setSelectedIndices(selected);
			}
		}
		
		else if (e.getSource()==association_down){
			int[] selected=associations.getSelectedIndices();
			
			if (selected.length>0){
				ArrayList<Integer> areas=new ArrayList<Integer>();
				ArrayList<Integer> types=new ArrayList<Integer>();
				ArrayList<Integer> repeat=new ArrayList<Integer>();
				ArrayList<String> sounds=new ArrayList<String>();
				
				main.currentAge.areas.stopSound();
				
				for (int i=selected.length-1;i>=0;i--){
					areas.add(main.currentAge.areas.areas.get(selected[i]));
					types.add(main.currentAge.areas.types.get(selected[i]));
					repeat.add(main.currentAge.areas.repeat.get(selected[i]));
					sounds.add(main.currentAge.areas.soundFiles.get(selected[i]));
					
					main.currentAge.areas.areas.remove(selected[i]);
					main.currentAge.areas.types.remove(selected[i]);
					main.currentAge.areas.repeat.remove(selected[i]);
					main.currentAge.areas.soundFiles.remove(selected[i]);
				}
				
				int id=selected[selected.length-1]-selected.length+2;
				if (id>main.currentAge.areas.areas.size()) id=main.currentAge.areas.areas.size();

				for (int i=0;i<selected.length;i++){
					main.currentAge.areas.areas.add(id, areas.get(i));
					main.currentAge.areas.types.add(id, types.get(i));
					main.currentAge.areas.repeat.add(id, repeat.get(i));
					main.currentAge.areas.soundFiles.add(id, sounds.get(i));
				}
				
				update();
				
				for (int i=0;i<selected.length;i++){
					selected[i]=id+i;
				}
				associations.setSelectedIndices(selected);
			}
		}
		
		else if (e.getSource()==exit_add){
			
			// detect existing exit for this area
			int i=0;
			boolean found=false;
			
			while (!found && i<main.currentAge.ages.size()){
				if ( frame.selectedArea== main.currentAge.exitAreasId.get(i)) found=true;
				else i++;
			}
			
			if (!found){
				String line1="exit "+frame.selectedArea+" "+(String)list_ages.getSelectedItem()+" ";
				if (reboot.isSelected()) line1+="1";
				else line1+="0";
				
				main.script.setCommandLine(line1);
				
				update();
			}
			else{
				main.println("An exit already exists for this area");
			}
		}
		
	}


	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {}


	public void keyReleased(KeyEvent e) {
		if (e.getSource()==name){
			boolean found=false;
			int i=0;
			
			while (!found && i<main.listAssociation.length){
				System.out.print(main.listAssociation[i]+" , "+name.getText()+" -> ");
				if (main.listAssociation[i].equals(name.getText())) found=true;
				i++;
			}
			
			if (found) saveMsg="/!\\ name already exists !";
			else saveMsg="";
			
			if (name.getText().length()==0) association_save.setEnabled(false);
			else association_save.setEnabled(true);
			
			this.repaint();
		}
	}
}
