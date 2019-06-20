package display;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
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
public class DisplayAttractorSettingPanel extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	

	public Main main;
	private DisplayFrame frame;
	
	public DisplayAttractorSettingPanel panel=this;
	
	private JButton playPause;
	private JButton remLast;
	private JButton clearPath;
	
	//private ButtonGroup mode;
	//private JRadioButton linearMode;
	//private JRadioButton inverseMode;
	
	private JSlider strength;
	private JSlider distance;

	
	
	//private boolean guide_mode=false;
	
	public DisplayAttractorSettingPanel(Main m, DisplayFrame f, KeyboardListener k){
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
		
		clearPath=new JButton("<html><center> Clear all<br>attractors</center></html>");
		clearPath.addActionListener(this);
		clearPath.addKeyListener(k);
		this.add(clearPath);
		clearPath.setBounds(255, 5, 115, 35);
		
		strength=new JSlider(JSlider.HORIZONTAL, -250, 250, 200);
		strength.setMinorTickSpacing(1);
		strength.setPaintTicks(true);
		strength.setPaintLabels(true);
		strength.setMinorTickSpacing(10);
		strength.setMajorTickSpacing(100);
		strength.addChangeListener(new ChangeListener(){
		      public void stateChanged(ChangeEvent e){
		    	  main.attractor_strength=((JSlider)e.getSource()).getValue();
		    	  panel.repaint();
		      }});
		strength.addKeyListener(k);
		strength.setBounds(5, 70, 365, 50);
		this.add(strength);
		
		distance=new JSlider(JSlider.HORIZONTAL, 0, 500, 100);
		distance.setMinorTickSpacing(1);
		distance.setPaintTicks(true);
		distance.setPaintLabels(true);
		distance.setMinorTickSpacing(10);
		distance.setMajorTickSpacing(100);
		distance.addChangeListener(new ChangeListener(){
		      public void stateChanged(ChangeEvent e){
		    	  main.attractor_distance=((JSlider)e.getSource()).getValue();
		    	  panel.repaint();
		      }});
		distance.addKeyListener(k);
		distance.setBounds(5, 150, 365, 50);
		this.add(distance);
		
		
		/*mode = new ButtonGroup();
		linearMode = new JRadioButton("linear");
		inverseMode = new JRadioButton("inverse");
		this.add(linearMode);
		this.add(inverseMode);
		linearMode.setBounds(25, 130, 100, 20);
		linearMode.addActionListener(this);
		linearMode.addKeyListener(k);
		inverseMode.setBounds(25, 150, 100, 20);
		inverseMode.addActionListener(this);
		inverseMode.addKeyListener(k);
		
		mode.add(linearMode);
		mode.add(inverseMode);*/
		
	}

	
	public void paintComponent(Graphics g){

		if (frame.display_mode==8){
			
			g.setColor(this.getBackground());
			g.fillRect(0, 0, 375, 230);
			g.setColor(Color.black);
			
			// path bloc
			g.drawRect(0, 0, 375, 220);
			if (main.target_pause) playPause.setIcon( new ImageIcon(Main.FILES+"ressources/pause_icon.png"));
			else playPause.setIcon( new ImageIcon(Main.FILES+"ressources/play_icon.png"));
			
			g.drawString(""+main.attractor_strength, 8+(int)((main.attractor_strength+250)*(float)335/(float)500), 60);	
			g.drawString(""+main.attractor_distance, 8+(int)((main.attractor_distance)*(float)335/(float)500), 140);	
			
			/*if (frame.selected_image>=0){
				g.setColor(Color.black);
				g.drawString("Cursor position : ( "+frame.posx+" , "+frame.posy+" )", 30, 200);
			}*/
		}

 		//if (main.attractor_type==1) linearMode.setSelected(true);
 		//else inverseMode.setSelected(true);
 		
	}
	

	public void actionPerformed(ActionEvent e) {
		
		//////////////////////////////////////////////////////////////////////
		if (e.getSource()==playPause) main.target_pause=!main.target_pause;
		if (e.getSource()==remLast) 
			if (main.script.ageList.get(main.script.currentAge).targetSequence.sizeTarget()>0)
				main.script.ageList.get(main.script.currentAge).targetSequence.remove(main.script.ageList.get(main.script.currentAge).targetSequence.sizeTarget()-1);
		if (e.getSource()==clearPath) main.clearPath();
		
		//if (e.getSource()==controlMode && controlMode.isSelected()) main.target_type=1;
		//if (e.getSource()==attractorMode && attractorMode.isSelected()) main.target_type=0;

		this.repaint();
	}

}
