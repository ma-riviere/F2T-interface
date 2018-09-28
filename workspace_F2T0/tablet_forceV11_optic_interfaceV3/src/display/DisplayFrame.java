package display;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import main.Main;


public class DisplayFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	
	Main main;
	
	DisplayPanel panel;
	
	private WindowAdapter listener;
	
	public DisplayFrame(Main m){
		
		main=m;
		
		this.setTitle("View port");
    	this.setSize(1600, 840);
    	this.setLocationRelativeTo(null);               
    	this.setVisible(true);
    	panel=new DisplayPanel(m);
    	this.setContentPane(panel);
    	
    	listener=new WindowAdapter() {
      		public void windowClosing(WindowEvent e) {
      			main.close();
      			System.exit(0);
      		}
  	   	};
  	   	
  	   	this.addWindowListener(listener);
	}
	
	public void rescan(){
		panel.rescan();
	}
}
