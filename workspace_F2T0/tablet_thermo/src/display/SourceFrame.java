package display;

import javax.swing.JFrame;

import main.Main;


public class SourceFrame extends JFrame{

	private static final long serialVersionUID = 1L;

	protected SourcePanel panel;
	
	public SourceFrame(Main m){
		
		this.setTitle("sources");
    	this.setSize(850, 850);
    	this.setLocationRelativeTo(null);               
    	this.setVisible(true);
    	panel=new SourcePanel(m);
    	this.setContentPane(panel);
    	this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
}
