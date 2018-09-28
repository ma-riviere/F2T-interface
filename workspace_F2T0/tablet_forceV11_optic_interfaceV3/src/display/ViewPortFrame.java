package display;

import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import main.Main;


public class ViewPortFrame extends JFrame{

	private static final long serialVersionUID = 1L;

	protected ViewPortPanel panel;
	
	public ViewPortFrame(Main m, BufferedImage img){
		
		this.setTitle("Image");
    	this.setSize(700, 700);
    	this.setLocationRelativeTo(null);               
    	this.setVisible(true);
    	panel=new ViewPortPanel(m, img);
    	this.setContentPane(panel);
    	this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	public void setImage(BufferedImage img){
		panel.setImage(img);
	}
	
}
