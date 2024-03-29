package display;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import main.Main;


public class CameraFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	
	Main main;
	
	CameraPanel panel;
	
	private WindowAdapter listener;
	
	public CameraFrame(Main m){
		
		main=m;
		
		this.setTitle("Image");
    	this.setSize(1400, 850);
    	this.setLocationRelativeTo(null);               
    	this.setVisible(true);
    	panel=new CameraPanel(m);
    	this.setContentPane(panel);
    	
    	listener=new WindowAdapter() {
      		public void windowClosing(WindowEvent e) {
      			main.close();
      			System.exit(0);
      		}
  	   	};
  	   	
  	   	this.addWindowListener(listener);
	}
	
	public int getTouchX(){
		return panel.x;
	}
	public int getTouchY(){
		return panel.y;
	}
	
}
