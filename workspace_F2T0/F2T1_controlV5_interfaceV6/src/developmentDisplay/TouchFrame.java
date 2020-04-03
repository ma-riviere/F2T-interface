package developmentDisplay;
import javax.swing.JFrame;

import main.Main;



/**
 * Display height map
 * @author simon gay
 */

public class TouchFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	
	TouchPanel panel;

	public TouchFrame(Main m){
		this.setTitle("Touch");
    	this.setSize(1200, 600);
    	this.setLocationRelativeTo(null);               
    	this.setVisible(true);
    	panel=new TouchPanel(m);
    	this.setContentPane(panel);
    	this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
}
