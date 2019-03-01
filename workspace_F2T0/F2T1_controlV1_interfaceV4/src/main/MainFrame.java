package main;


import javax.swing.JFrame;

public class MainFrame extends JFrame{

	private static final long serialVersionUID = 1L;

	protected MainPanel panel;
	
	public MainFrame(Main m){
		
		this.setTitle("sources");
    	this.setSize(800, 800);
    	this.setLocationRelativeTo(null);               
    	this.setVisible(true);
    	panel=new MainPanel(m);
    	this.setContentPane(panel);
    	this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
}
