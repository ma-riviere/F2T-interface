
import java.awt.Color;
import java.awt.Graphics;


import javax.swing.JPanel;


/**
 * Generic class of panel that can be exported as pdf file and jpeg image
 * @author simon
 */
public class RenderPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
	protected Main main;
	
	public boolean click=false;
	
	
	
	public RenderPanel(Main m){
		main=m;
	}

	
	public void paintComponent(Graphics g){
		g.setColor(new Color(128,128,0));
		g.fillRect(0, 0, 1000, 1000);

		for (int i=0;i<700;i++){
			for (int j=0;j<700;j++){
				g.setColor(new Color(Math.max(0, Math.min(1, 0.5f+main.flow[i][j][0]/2)),Math.max(0, Math.min(1, 0.5f-main.flow[i][j][1]/2)),0 ));
				g.drawLine(i, j, i, j);
			}	
		}
		

	}

}