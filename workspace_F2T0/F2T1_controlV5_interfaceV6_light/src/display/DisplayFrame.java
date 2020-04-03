package display;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import main.Main;

/**
 * Main frame of the interface
 * @author simon gay
 */

public class DisplayFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	
	public static float SCALE=0.4f;
	
	private static int IMAGE_X=2;
	private static int IMAGE_Y=2;
	
	
	private Main main;
	
	
	
	private DisplayImagePanel imagePanel;
	private DisplayMiniaturesPanel miniaturePanel;
	private DisplayFilePanel filePanel;
	private DisplayPathSettingPanel pathPanel;

	private WindowAdapter listener;
	
	public int selected_image=0;
	
	public boolean displayTrace=true;
	public boolean displayJoystick=true;
	
	public int posx=0;
	public int posy=0;
	
	public int areaRed=0;
	public int areaGreen=-1;
	public int areaBlue=-1;
	public int selectedArea=0;
	
	public KeyboardListener keyboard;
	
	public int counter=0;
	
	/////////////////////////////////////////////
	
	public DisplayFrame(Main m){
		
		main=m;
		
		this.setTitle("View port");
    	this.setSize(480, 320);
    	this.setLocationRelativeTo(null);               
    	this.setVisible(true);
    	
    	this.setLayout(null);
		
    	this.setFocusable(true);
    	
    	keyboard=new KeyboardListener();
    	
    	addKeyListener(keyboard);
    	
		imagePanel=new DisplayImagePanel(main, this);
		this.add(imagePanel);
		imagePanel.setBounds(IMAGE_X, IMAGE_Y, (int)(700*SCALE), (int)(700*SCALE));
		
		miniaturePanel=new DisplayMiniaturesPanel(main, this);
		this.add(miniaturePanel);
		miniaturePanel.setBounds((int)(710*SCALE)+IMAGE_X, IMAGE_Y, 186, 30);
		
		filePanel=new DisplayFilePanel(main, keyboard);
		this.add(filePanel);
		filePanel.setBounds((int)(710*SCALE)+IMAGE_X, IMAGE_Y+30, 186, 111);

		pathPanel=new DisplayPathSettingPanel(main, this, keyboard);
		this.add(pathPanel);
		pathPanel.setBounds((int)(710*SCALE)+IMAGE_X, IMAGE_Y+140, 380, 230);
		
    	listener=new WindowAdapter() {
      		public void windowClosing(WindowEvent e) {
      			main.close();
      			System.exit(0);
      		}
  	   	};
  	   	this.addWindowListener(listener);
	}
	
	
	public void repaint(){
		imagePanel.repaint();
		miniaturePanel.repaint();
		filePanel.repaint();
	}
	
	
	public void updateIndex(int id){
		filePanel.updateIndex(id);
	}
	
	public void updatePathPanel(int x, int y){
		posx=x;
		posy=y;
		if (pathPanel!=null) pathPanel.repaint();
	}
	
	public void updateSelectedImage(int id){
		selected_image=id;
		filePanel.updateSelectedImage(id);
	}
	
	
	public void rescan(){
		filePanel.rescan();
	}
	
	
	
	public void setDisplayTrace(boolean set){
		displayTrace=set;
	}
	public void setDisplayJoystick(boolean set){
		displayJoystick=set;
	}
	
	
}
