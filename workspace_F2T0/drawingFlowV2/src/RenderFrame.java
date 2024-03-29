
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;


public class RenderFrame extends JFrame{

	private static final long serialVersionUID = 1L;

	Main main;
	
	protected RenderPanel panel;
	
	public RenderFrame(Main m){
		
		main=m;
		
		this.setTitle("Render");
    	this.setSize(700, 700);
    	this.setLocationRelativeTo(null);               
    	this.setVisible(true);
    	panel=new RenderPanel(m);
    	this.setContentPane(panel);
    	
    	this.addWindowListener(new WindowAdapter() {
  	      public void windowClosing(WindowEvent e) {
  	    	//main.inter.close();
  	        System.exit(0);
  	      }
  	    });
	}
	
}
