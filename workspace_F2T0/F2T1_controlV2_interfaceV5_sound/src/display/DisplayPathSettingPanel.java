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
 * Display the probe input
 * @author simon gay
 */

/* - inherited from EnvPanel :
 *   Agent agent      : pointer to the agent
 */
public class DisplayPathSettingPanel extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	

	public Main main;
	private DisplayFrame frame;
	
	public DisplayPathSettingPanel panel=this;
	
	private JButton playPause;
	private JButton remLast;
	private JButton clearPath;
	private JButton savePath;
	
	private ButtonGroup targetMode;
	private JRadioButton controlMode;
	private JRadioButton attractorMode;
	
	private JSlider speed;
	

	
	
	//private boolean guide_mode=false;
	
	public DisplayPathSettingPanel(Main m, DisplayFrame f, KeyboardListener k){
		main=m;
		frame=f;
		this.setLayout(null);

	
		
		playPause=new JButton( new ImageIcon(Main.FILES+"ressources/play_icon.png"));
		playPause.addActionListener(this);
		playPause.addKeyListener(k);
		this.add(playPause);
		playPause.setBounds(5, 5, 115, 35);
		
		remLast=new JButton("<html><center> Remove<br>last point</center></html>");
		remLast.addActionListener(this);
		remLast.addKeyListener(k);
		this.add(remLast);
		remLast.setBounds(130, 5, 115, 35);
		
		clearPath=new JButton("Clear path");
		clearPath.addActionListener(this);
		clearPath.addKeyListener(k);
		this.add(clearPath);
		clearPath.setBounds(255, 5, 115, 35);
		
		speed=new JSlider(JSlider.HORIZONTAL, 0, 500, 200);
		speed.setMinorTickSpacing(1);
		speed.setPaintTicks(true);
		speed.setPaintLabels(true);
		speed.setMinorTickSpacing(10);
		speed.setMajorTickSpacing(100);
		speed.addChangeListener(new ChangeListener(){
		      public void stateChanged(ChangeEvent e){
		    	  main.target_speed=((JSlider)e.getSource()).getValue();
		    	  panel.repaint();
		      }});
		speed.addKeyListener(k);
		speed.setBounds(5, 70, 365, 50);
		this.add(speed);
		
		
		savePath=new JButton("Save path");
		savePath.addActionListener(this);
		savePath.addKeyListener(k);
		this.add(savePath);
		savePath.setBounds(200, 125, 150, 50);
		
		targetMode = new ButtonGroup();
		controlMode = new JRadioButton("Control");
		attractorMode = new JRadioButton("Attractor");
		this.add(controlMode);
		this.add(attractorMode);
		controlMode.setBounds(25, 130, 100, 20);
		controlMode.addActionListener(this);
		controlMode.addKeyListener(k);
		attractorMode.setBounds(25, 150, 100, 20);
		attractorMode.addActionListener(this);
		attractorMode.addKeyListener(k);
		
		targetMode.add(controlMode);
		targetMode.add(attractorMode);
		
	}

	
	public void paintComponent(Graphics g){

		if (frame.display_mode==0 || frame.display_mode==6){
			
			g.setColor(this.getBackground());
			g.fillRect(0, 0, 375, 230);
			g.setColor(Color.black);
			
			// path bloc
			g.drawRect(0, 0, 375, 220);
			if (main.target_pause) playPause.setIcon( new ImageIcon(Main.FILES+"ressources/pause_icon.png"));
			else playPause.setIcon( new ImageIcon(Main.FILES+"ressources/play_icon.png"));
			
			g.drawString(""+main.target_speed, 8+(int)(main.target_speed*(float)335/(float)500), 60);	
			
			if (frame.selected_image>=0){
				g.setColor(Color.black);
				g.drawString("Cursor position : ( "+frame.posx+" , "+frame.posy+" )", 30, 200);
			}
		}

 		if (main.target_type==1) controlMode.setSelected(true);
 		else attractorMode.setSelected(true);
 		
	}
	

	public void actionPerformed(ActionEvent e) {
		
		//////////////////////////////////////////////////////////////////////
		if (e.getSource()==playPause) main.target_pause=!main.target_pause;
		if (e.getSource()==remLast) if (main.script.ageList.get(main.script.currentAge).targetSequence.size()>0)
			main.script.ageList.get(main.script.currentAge).targetSequence.remove(main.script.ageList.get(main.script.currentAge).targetSequence.size()-1);
		if (e.getSource()==clearPath) main.clearPath();
		
		if (e.getSource()==controlMode && controlMode.isSelected()) main.target_type=1;
		if (e.getSource()==attractorMode && attractorMode.isSelected()) main.target_type=0;
		
		if (e.getSource()==savePath) main.savePath();
		
		this.repaint();
	}

}
