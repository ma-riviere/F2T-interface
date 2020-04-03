package developmentDisplay;
import javax.swing.JFrame;

import main.Main;



/**
 * Display the probe input
 * @author simon gay
 */

/* - inherited from EnvFrame :
 *   Agent agent      : pointer to the agent
 *   EnvPanel panel   : panel of this frame
 *   
 * - inherited from PrintableFrame :
 *   boolean printable   : define if the frame can be printed
 *   int indexImage      : counter for image name
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
