package display;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import main.Main;



/**
 * Display the probe input
 * @author simon gay
 */

/* - inherited from EnvPanel :
 *   Agent agent      : pointer to the agent
 */
public class DisplayAgeMiniaturesPanel extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	

	public Main main;
	
	private JButton playPause;
	
	public DisplayAgeMiniaturesPanel(Main m, KeyboardListener k){
		main=m;
		this.setLayout(null);
		
		playPause=new JButton( new ImageIcon(Main.FILES+"ressources/play_icon.png"));
		playPause.addActionListener(this);
		playPause.addKeyListener(k);
		this.add(playPause);
		playPause.setBounds(120, 185, 115, 35);
	}
	
	
	public void paintComponent(Graphics g){
		
		
		if (main.script.ageList.get(main.script.currentAge).image.view!=null) g.drawImage(main.script.ageList.get(main.script.currentAge).image.view_img_miniature, 0, 0, this);
		else{
			g.setColor(Color.gray);
			g.fillRect(0, 0, 175, 175);
			g.setColor(Color.black);
			g.drawRect(0, 0, 175, 175);
			g.drawLine(87, 0, 87, 175);
			g.drawLine(0, 87, 175, 87);
		}
		
		if (main.script.ageList.get(main.script.currentAge).image.area!=null) g.drawImage(main.script.ageList.get(main.script.currentAge).image.area_img_miniature, 180, 0, this);
		else{
			g.setColor(Color.gray);
			g.fillRect(180, 0, 175, 175);
			g.setColor(Color.black);
			g.drawRect(180, 0, 175, 175);
			g.drawLine(267, 0, 267, 175);
			g.drawLine(180, 87, 355, 87);
		}
		
		// draw joystick position
		g.setColor(Color.magenta);
		g.drawOval(82+(int)(main.x/4), 82-(int)(main.y/4), 10, 10);
		
		g.drawOval(262+(int)(main.x/4), 82-(int)(main.y/4), 10, 10);
		
		
		// button
		if (main.target_pause) playPause.setIcon( new ImageIcon(Main.FILES+"ressources/pause_icon.png"));
		else playPause.setIcon( new ImageIcon(Main.FILES+"ressources/play_icon.png"));
	}


	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==playPause) main.target_pause=!main.target_pause;
	}


}
