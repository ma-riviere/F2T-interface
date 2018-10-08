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
	

	
	
	private Main main;
	
	private DisplayValuesPanel valuesPanel;
	
	//private DisplayImagePanel imagePanel;
	private SurfacePanel imagePanel;
	private DisplayMiniaturesPanel miniaturePanel;
	private DisplayFilePanel filePanel;
	
	private DisplaySourcePanel sourcePanel;
	private DisplaySourceSettingPanel sourceSettingPanel;
	
	private DisplayAgesPanel agesPanel;
	
	private DisplayWorldPanel worldPanel;
	
	private WindowAdapter listener;
	
	public int display_mode=0;
	public int selected_image=-1;
	
	
	public int posx=0;
	public int posy=0;
	
	public int x=0;
	public int y=0;
	
	public KeyboardListener keyboard;
	
	/////////////////////////////////////////////
	
	private JButton mode1;
	private JButton mode2;
	private JButton mode3;
	private JButton mode4;
	
	/////////////////////////////////////////////
	
	public DisplayFrame(Main m){
		
		main=m;
		
		this.setTitle("View port");
    	this.setSize(1180, 835);
    	this.setLocationRelativeTo(null);               
    	this.setVisible(true);
    	
    	this.setLayout(null);
		
    	this.setFocusable(true);
    	
    	keyboard=new KeyboardListener();
    	
    	addKeyListener(keyboard);

    	
    	mode1=new JButton("setup");
		mode1.addActionListener(this);
		mode1.addKeyListener(keyboard);
		this.add(mode1);
		mode1.setBounds(815, 5, 80, 50);
		
		mode2=new JButton("sound");
		mode2.addActionListener(this);
		mode2.addKeyListener(keyboard);
		this.add(mode2);
		mode2.setBounds(900, 5, 80, 50);
		
		mode3=new JButton("story");
		mode3.addActionListener(this);
		mode3.addKeyListener(keyboard);
		this.add(mode3);
		mode3.setBounds(985, 5, 80, 50);
		
		mode4=new JButton("Ages");
		mode4.addActionListener(this);
		mode4.addKeyListener(keyboard);
		this.add(mode4);
		mode4.setBounds(1070, 5, 80, 50);
    	
		valuesPanel=new DisplayValuesPanel(main, this);
		this.add(valuesPanel);
		valuesPanel.setBounds(810, 70, 355, 51);

		//imagePanel=new DisplayImagePanel(main, this);
		imagePanel=new SurfacePanel(main,this);
		this.add(imagePanel);
		imagePanel.setBounds(0, 0, 800, 800);
		
		miniaturePanel=new DisplayMiniaturesPanel(main, this);
		this.add(miniaturePanel);
		miniaturePanel.setBounds(810, 500, 355, 175);
		
		filePanel=new DisplayFilePanel(main, keyboard);
		this.add(filePanel);
		filePanel.setBounds(810, 130, 380, 325);

		
		sourcePanel=new DisplaySourcePanel(main, this);
		sourcePanel.setVisible(false);
		this.add(sourcePanel);
		sourcePanel.setBounds(0, 0, 800, 800);
		
		sourceSettingPanel=new DisplaySourceSettingPanel(main, this, keyboard);
		sourceSettingPanel.setVisible(false);
		this.add(sourceSettingPanel);
		sourceSettingPanel.setBounds(810, 500, 380, 230);
		
		
		agesPanel=new DisplayAgesPanel(main);
		agesPanel.setVisible(false);
		this.add(agesPanel);
		agesPanel.setBounds(0, 0, 1000, 700);
    	
		worldPanel=new DisplayWorldPanel(main);
		worldPanel.setVisible(false);
		this.add(worldPanel);
		worldPanel.setBounds(0, 0, 1000, 700);
		
    	listener=new WindowAdapter() {
      		public void windowClosing(WindowEvent e) {
      			main.close();
      			System.exit(0);
      		}
  	   	};
  	   	this.addWindowListener(listener);
	}
	
	
	public void repaint(){
		
		valuesPanel.repaint();
		
		if (display_mode==0){
			imagePanel.repaint();
			miniaturePanel.repaint();
		}
		else if (display_mode==1){
			sourcePanel.repaint();
		}
		else if (display_mode==2){
			agesPanel.repaint();
		}
		else if (display_mode==3){
			worldPanel.repaint();
		}
	}
	
	public void updateMiniatures(int id){
		miniaturePanel.update();
	}
	
	public void updateIndex(int id){
		filePanel.updateIndex(id);
	}
	
	public void updateSourcePanel(int x, int y){
		posx=x;
		posy=y;	
		if (sourceSettingPanel!=null) sourceSettingPanel.repaint();
	}
	
	public String getSourceParameters(){
		return sourceSettingPanel.getParameters();
	}
	
	public void rescan(){
		filePanel.rescan();
	}
	
	public float getTouchX(){
		return x;
	}
	public float getTouchY(){
		return y;
	}
	
	public void setMode(int mode){
		
		display_mode=mode;
		
		if (mode==0){
			imagePanel.setVisible(true);
			miniaturePanel.setVisible(true);
			filePanel.setVisible(true);
			
			sourcePanel.setVisible(false);
			sourceSettingPanel.setVisible(false);
			
			agesPanel.setVisible(false);
			worldPanel.setVisible(false);
		}
		else if (mode==1){
			imagePanel.setVisible(false);
			miniaturePanel.setVisible(false);
			filePanel.setVisible(true);
			
			sourcePanel.setVisible(true);
			sourceSettingPanel.setVisible(true);
			
			agesPanel.setVisible(false);
			worldPanel.setVisible(false);
		}
		else if (mode==2){
			imagePanel.setVisible(false);
			miniaturePanel.setVisible(false);
			filePanel.setVisible(true);
			
			sourcePanel.setVisible(false);
			sourceSettingPanel.setVisible(false);
			
			agesPanel.setVisible(true);
			worldPanel.setVisible(false);
		}
		else if (mode==3){
			imagePanel.setVisible(false);
			miniaturePanel.setVisible(false);
			filePanel.setVisible(true);
			
			sourcePanel.setVisible(false);
			sourceSettingPanel.setVisible(false);
			
			agesPanel.setVisible(false);
			worldPanel.setVisible(true);
		}
	}
	
	


	public void actionPerformed(ActionEvent e) {
		
		///////////////////////////////////////////////////////////////////////
		if (e.getSource()==mode1) setMode(0);
		if (e.getSource()==mode2) setMode(1);
		if (e.getSource()==mode3) setMode(2);
		if (e.getSource()==mode4) setMode(3);
		///////////////////////////////////////////////////////////////////////

	}
}
