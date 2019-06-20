package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import main.Main;

public class DisplaySettingsPanel extends JPanel implements ActionListener  {

	private static final long serialVersionUID = 1L;
	
	public Main main;
	public DisplayFrame frame;
	
	private JCheckBox value = new JCheckBox("Display values");
	private JCheckBox trace = new JCheckBox("Display trace");
	private JCheckBox joystick = new JCheckBox("Display joystick");
	
	public DisplaySettingsPanel(Main m, DisplayFrame f, KeyboardListener k){
		main=m;
		frame=f;
		this.setLayout(null);
		
		this.add(value);
		value.setBounds(20, 10, 160, 20);
		value.setSelected(true);
		value.addActionListener(this);
		value.addKeyListener(k);
		
		this.add(trace);
		trace.setBounds(20, 30, 160, 20);
		trace.setSelected(true);
		trace.addActionListener(this);
		trace.addKeyListener(k);
		
		this.add(joystick);
		joystick.setBounds(20, 50, 160, 20);
		joystick.setSelected(true);
		joystick.addActionListener(this);
		joystick.addKeyListener(k);
	}
	
	public void paintComponent(Graphics g){
		g.setColor(Color.black);
		g.drawRect(0, 0, 375, 75);
	}
	

	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==value) frame.setDisplayValues(value.isSelected());
		if (e.getSource()==trace) frame.setDisplayTrace(trace.isSelected());
		if (e.getSource()==joystick) frame.setDisplayJoystick(joystick.isSelected());
	}
	
}
