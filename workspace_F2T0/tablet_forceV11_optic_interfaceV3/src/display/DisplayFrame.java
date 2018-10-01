package display;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;

import main.Main;


public class DisplayFrame extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	private static int IMAGE_X=5;
	private static int IMAGE_Y=50;
	
	private static int LISTS_X=1100;
	private static int LISTS_Y=50;
	
	private static int PATH_X=1100;
	private static int PATH_Y=380;
	
	
	private Main main;
	
	//private DisplayPanel panel;
	
	private DisplayImagePanel imagePanel;
	private DisplayMiniaturesPanel miniaturePanel;
	private DisplayFilePanel filePanel;
	private DisplayPathSettingPanel pathPanel;
	
	private DisplaySourcePanel sourcePanel;
	private DisplaySourceSettingPanel sourceSettingPanel;
	
	private DisplayAgesPanel agesPanel;
	private DisplayAgeMiniaturesPanel ageMiniaturesPanel;
	
	private WindowAdapter listener;
	
	public int display_mode=0;
	public int selected_image=-1;
	
	public int posx=0;
	public int posy=0;
	
	/////////////////////////////////////////////
	
	private JButton mode1;
	private JButton mode2;
	private JButton mode3;

	
	/////////////////////////////////////////////
	
	public DisplayFrame(Main m){
		
		main=m;
		
		this.setTitle("View port");
    	this.setSize(1490, 800);
    	this.setLocationRelativeTo(null);               
    	this.setVisible(true);
    	
    	this.setLayout(null);
		
    	
    	mode1=new JButton("setup");
		mode1.addActionListener(this);
		this.add(mode1);
		mode1.setBounds(5, 5, 100, 40);
		
		mode2=new JButton("sources");
		mode2.addActionListener(this);
		this.add(mode2);
		mode2.setBounds(110, 5, 100, 40);
		
		mode3=new JButton("story");
		mode3.addActionListener(this);
		this.add(mode3);
		mode3.setBounds(215, 5, 100, 40);
    	
    	
		
		imagePanel=new DisplayImagePanel(main, this);
		this.add(imagePanel);
		imagePanel.setBounds(IMAGE_X, IMAGE_Y, 700, 700);
		
		miniaturePanel=new DisplayMiniaturesPanel(main, this);
		this.add(miniaturePanel);
		miniaturePanel.setBounds(710+IMAGE_X, IMAGE_Y, 370, 700);
		
		filePanel=new DisplayFilePanel(main);
		this.add(filePanel);
		filePanel.setBounds(LISTS_X, LISTS_Y, 380, 325);

		pathPanel=new DisplayPathSettingPanel(main, this);
		this.add(pathPanel);
		pathPanel.setBounds(PATH_X, PATH_Y, 380, 230);
		
		
		sourcePanel=new DisplaySourcePanel(main, this);
		sourcePanel.setVisible(false);
		this.add(sourcePanel);
		sourcePanel.setBounds(IMAGE_X, IMAGE_Y, 1000, 700);
		
		sourceSettingPanel=new DisplaySourceSettingPanel(main, this);
		sourceSettingPanel.setVisible(false);
		this.add(sourceSettingPanel);
		sourceSettingPanel.setBounds(PATH_X, PATH_Y, 380, 230);
		
		
		agesPanel=new DisplayAgesPanel(main);
		agesPanel.setVisible(false);
		this.add(agesPanel);
		agesPanel.setBounds(IMAGE_X, IMAGE_Y, 1000, 700);
		
		ageMiniaturesPanel=new DisplayAgeMiniaturesPanel(main,this);
		ageMiniaturesPanel.setVisible(false);
		this.add(ageMiniaturesPanel);
		ageMiniaturesPanel.setBounds(PATH_X, PATH_Y, 1000, 700);
    	
    	listener=new WindowAdapter() {
      		public void windowClosing(WindowEvent e) {
      			main.close();
      			System.exit(0);
      		}
  	   	};
  	   	
  	   	this.addWindowListener(listener);
	}
	
	
	public void repaint(){
		
		if (display_mode==0){
			imagePanel.repaint();
			miniaturePanel.repaint();
		}
		else if (display_mode==1){
			sourcePanel.repaint();
		}
		else if (display_mode==2){
			agesPanel.repaint();
			ageMiniaturesPanel.repaint();
		}
		
	}
	
	public void updateMiniatures(int id){
		miniaturePanel.update();

		//miniaturePanel.setVisible(false);
		//miniaturePanel.setVisible(true);
		
		//filePanel.updateIndex(id);
	}
	
	public void updatePathPanel(int x, int y){
		
		posx=x;
		posy=y;
		
		if (pathPanel!=null) pathPanel.repaint();
	}
	
	public void updateSourcePanel(int x, int y){
		
		posx=x;
		posy=y;
		
		if (sourceSettingPanel!=null) sourceSettingPanel.repaint();
	}
	
	
	public void rescan(){
		//panel.rescan();
		
		filePanel.rescan();
	}
	
	public void setMode(int mode){
		
		display_mode=mode;
		
		if (mode==0){
			imagePanel.setVisible(true);
			miniaturePanel.setVisible(true);
			filePanel.setVisible(true);
			pathPanel.setVisible(true);
			
			sourcePanel.setVisible(false);
			sourceSettingPanel.setVisible(false);
			
			agesPanel.setVisible(false);
			ageMiniaturesPanel.setVisible(false);
		}
		else if (mode==1){
			imagePanel.setVisible(false);
			miniaturePanel.setVisible(false);
			filePanel.setVisible(true);
			pathPanel.setVisible(false);
			
			sourcePanel.setVisible(true);
			sourceSettingPanel.setVisible(true);
			
			agesPanel.setVisible(false);
			ageMiniaturesPanel.setVisible(false);
		}
		else if (mode==2){
			imagePanel.setVisible(false);
			miniaturePanel.setVisible(false);
			filePanel.setVisible(true);
			pathPanel.setVisible(false);
			
			sourcePanel.setVisible(false);
			sourceSettingPanel.setVisible(false);
			
			agesPanel.setVisible(true);
			ageMiniaturesPanel.setVisible(true);
		}
	}

	public void actionPerformed(ActionEvent e) {
		
		///////////////////////////////////////////////////////////////////////
		if (e.getSource()==mode1) setMode(0);
		if (e.getSource()==mode2) setMode(1);
		if (e.getSource()==mode3) setMode(2);
		
		///////////////////////////////////////////////////////////////////////
	}
	
}
