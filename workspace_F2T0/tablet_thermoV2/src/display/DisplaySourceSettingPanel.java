package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.Main;

public class DisplaySourceSettingPanel extends JPanel  implements ActionListener, MouseListener{

	private static final long serialVersionUID = 1L;
	
	public Main main;
	private DisplayFrame frame;
	
	private JButton remLast;
	private JButton clearSources;
	private JButton saveSources;
	
	private JTextField parameters;
	
	public DisplaySourceSettingPanel(Main m, DisplayFrame f, KeyboardListener k){
		main=m;
		frame=f;
		this.setLayout(null);
		
		addMouseListener(this);
		
		remLast=new JButton("<html><center> Remove last source</center></html>");
		remLast.addActionListener(this);
		remLast.addKeyListener(k);
		this.add(remLast);
		remLast.setBounds(10, 5, 172, 35);
		
		clearSources=new JButton("<html><center> Clear all sources</center></html>");
		clearSources.addActionListener(this);
		clearSources.addKeyListener(k);
		this.add(clearSources);
		clearSources.setBounds(192, 5, 172, 35);
		
		
		saveSources=new JButton("<html><center> Save current<br>source list</center></html>");
		saveSources.addActionListener(this);
		saveSources.addKeyListener(k);
		this.add(saveSources);
		saveSources.setBounds(192, 65, 172, 50);
		
		
		parameters = new JTextField();
		this.add(parameters);
		parameters.setBounds(192, 130, 170, 30);
		
	}
	
	public void paintComponent(Graphics g){
		
		if (frame.display_mode==1){
			g.setColor(this.getBackground());
			g.fillRect(0, 0, 375, 230);
			g.setColor(Color.black);
		
			g.drawRect(0, 0, 375, 220);
		
			
			g.setColor(Color.black);
			g.drawString("Cursor position :", 30, 90);
			g.drawString("( "+frame.posx+" , "+frame.posy+" )", 40, 105);
			
			
			g.drawString("Source parameters :", 30, 150);
			
			
			
			g.drawString("Select image :", 30, 200);

			if (frame.selected_image==-1) g.setColor(Color.red);
			else g.setColor(Color.lightGray);
			g.fillRect(130, 185, 20, 20);
			g.setColor(Color.black);
			g.drawRect(130, 185, 20, 20);
			
			if (frame.selected_image==0) g.setColor(Color.red);
			else if (main.script.ageList.get(main.script.currentAge).image.view!=null) g.setColor(Color.lightGray);
			else g.setColor(Color.gray);
			g.fillRect(160, 185, 20, 20);
			g.setColor(Color.black);
			g.drawRect(160, 185, 20, 20);

			if (frame.selected_image==1) g.setColor(Color.red);
			else if (main.script.ageList.get(main.script.currentAge).image.tactile!=null) g.setColor(Color.lightGray);
			else g.setColor(Color.gray);
			g.fillRect(190, 185, 20, 20);
			g.setColor(Color.black);
			g.drawRect(190, 185, 20, 20);
			
			if (frame.selected_image==2) g.setColor(Color.red);
			else if (main.script.ageList.get(main.script.currentAge).image.area!=null) g.setColor(Color.lightGray);
			else g.setColor(Color.gray);
			g.fillRect(220, 185, 20, 20);
			g.setColor(Color.black);
			g.drawRect(220, 185, 20, 20);
		}
	}
	
	
	public String getParameters(){
		return parameters.getText();
	}
	
	
	public void actionPerformed(ActionEvent e) {
		
		
		if (e.getSource()==remLast) main.script.ageList.get(main.script.currentAge).sourceList.remove(main.script.ageList.get(main.script.currentAge).sourceList.size()-1);
		if (e.getSource()==clearSources) main.script.ageList.get(main.script.currentAge).sourceList.clear();
		
		if (e.getSource()==saveSources) main.saveSource();
	}

	public void mouseClicked(MouseEvent e) {
		
		for (int i=-1;i<3;i++){
			if (e.getX()>160+i*30 && e.getX()<180+i*30 && e.getY()>185 && e.getY()<205) frame.selected_image=i;
		}
		
		this.repaint();
	}

	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

}