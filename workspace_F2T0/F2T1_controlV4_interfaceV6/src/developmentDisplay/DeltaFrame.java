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
public class DeltaFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	
	DeltaPanel panel;

	public DeltaFrame(Main m){
		this.setTitle("Delta");
    	this.setSize(1200, 690);
    	this.setLocationRelativeTo(null);               
    	this.setVisible(true);
    	panel=new DeltaPanel(m);
    	this.setContentPane(panel);
    	this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
}
