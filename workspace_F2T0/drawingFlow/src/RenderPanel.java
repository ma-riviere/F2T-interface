
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
		g.setColor(Color.white);
		g.fillRect(0, 0, 1400, 800);
		
		
		//g.drawImage(main.picture, 0, 0, this);
		
		for (int i=0;i<700;i++){
			for (int j=0;j<700;j++){
				g.setColor(new Color(0.5f+main.flow[i][j][0]/2,0.5f-main.flow[i][j][1]/2,0 ));
				g.drawLine(i, j, i, j);
			}	
		}
		

	}

}