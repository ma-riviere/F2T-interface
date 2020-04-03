package display;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.Main;

/**
 * link edition panel
 * @author simon gay
 */

public class DisplayPathSettingPanel extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	

	public Main main;
	
	public DisplayPathSettingPanel panel=this;
	
	private JButton playPause;
	private JButton remLast;
	private JButton clearPath;
	
	private ButtonGroup targetMode;
	private JRadioButton controlMode;
	private JRadioButton attractorMode;
	
	private JSlider speed;

	public DisplayPathSettingPanel(Main m, DisplayFrame f, KeyboardListener k){
		main=m;
		this.setLayout(null);

		playPause=new JButton( new ImageIcon(Main.FILES+"ressources/play_icon.png"));
		playPause.addActionListener(this);
		playPause.addKeyListener(k);
		this.add(playPause);
		playPause.setBounds(5, 105, 80, 30);
		
		remLast=new JButton("Rem.");
		remLast.addActionListener(this);
		remLast.addKeyListener(k);
		this.add(remLast);
		remLast.setBounds(5, 5, 80, 30);
		
		clearPath=new JButton("Clear");
		clearPath.addActionListener(this);
		clearPath.addKeyListener(k);
		this.add(clearPath);
		clearPath.setBounds(90, 5, 80, 30);
		
		speed=new JSlider(JSlider.HORIZONTAL, 0, 500, 200);
		speed.setMinorTickSpacing(1);
		speed.setPaintTicks(true);
		speed.setPaintLabels(true);
		speed.setMinorTickSpacing(20);
		//speed.setMajorTickSpacing(100);
		speed.addChangeListener(new ChangeListener(){
		      public void stateChanged(ChangeEvent e){
		    	  main.target_speed=((JSlider)e.getSource()).getValue();
		    	  panel.repaint();
		      }});
		speed.addKeyListener(k);
		speed.setBounds(2, 55, 180, 50);
		this.add(speed);
		
		targetMode = new ButtonGroup();
		controlMode = new JRadioButton("Control");
		attractorMode = new JRadioButton("Attractor");
		this.add(controlMode);
		this.add(attractorMode);
		controlMode.setBounds(95, 105, 90, 18);
		controlMode.addActionListener(this);
		controlMode.addKeyListener(k);
		attractorMode.setBounds(95, 120, 90, 18);
		attractorMode.addActionListener(this);
		attractorMode.addKeyListener(k);
		
		targetMode.add(controlMode);
		targetMode.add(attractorMode);
		
	}

	
	public void paintComponent(Graphics g){

		g.setColor(this.getBackground());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
			
		g.setColor(this.getBackground());
		g.fillRect(0, 0, 185, 185);
		g.setColor(Color.black);
		
		// path bloc
		g.drawRect(0, 0, 185, 140);
		if (main.target_pause) playPause.setIcon( new ImageIcon(Main.FILES+"ressources/pause_icon.png"));
		else playPause.setIcon( new ImageIcon(Main.FILES+"ressources/play_icon.png"));
		
		g.drawString(""+main.target_speed, 8+(int)(main.target_speed*(float)150/(float)500), 55);	
		

 		if (main.target_type==1) controlMode.setSelected(true);
 		else attractorMode.setSelected(true);
 		
	}
	

	public void actionPerformed(ActionEvent e) {
		
		//////////////////////////////////////////////////////////////////////
		if (e.getSource()==playPause) main.target_pause=!main.target_pause;
		if (e.getSource()==remLast) if (main.script.ageList.get(main.script.currentAge).targetSequence.sizeTarget()>0)
			main.script.ageList.get(main.script.currentAge).targetSequence.remove(main.script.ageList.get(main.script.currentAge).targetSequence.sizeTarget()-1);
		if (e.getSource()==clearPath) main.clearPath();
		
		if (e.getSource()==controlMode && controlMode.isSelected()) main.target_type=1;
		if (e.getSource()==attractorMode && attractorMode.isSelected()) main.target_type=0;

		this.repaint();
	}

}
