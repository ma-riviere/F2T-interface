import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Main  extends JFrame{

	
	protected MainPanel panel;
	
	public Main(){
		
		this.setTitle("color");
    	this.setSize(255*2, 255*2);
    	this.setLocationRelativeTo(null);               
    	this.setVisible(true);
    	panel=new MainPanel();
    	this.setContentPane(panel);
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		new Main();
	}

	
	private class  MainPanel extends JPanel{
		public MainPanel(){

		}
		
		public void paintComponent(Graphics g){
			
			for (int i=0;i<255;i++){
				for (int j=0;j<255;j++){
					g.setColor(new Color(i,j,0));
					g.fillRect(i*2, (255-j)*2,2,2);
				}
			}
		}
	}
}
