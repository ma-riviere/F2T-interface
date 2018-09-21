
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;


public class PictureFrame extends JFrame{

	private static final long serialVersionUID = 1L;

	Main main;
	
	protected PicturePanel panel;
	
	public PictureFrame(Main m){
		
		main=m;
		
		this.setTitle("Flow");
    	this.setSize(1400, 700);
    	this.setLocationRelativeTo(null);               
    	this.setVisible(true);
    	panel=new PicturePanel(m);
    	this.setContentPane(panel);
    	
    	this.addWindowListener(new WindowAdapter() {
  	      public void windowClosing(WindowEvent e) {
  	    	//main.inter.close();
  	        System.exit(0);
  	      }
  	    });
	}
	
}
