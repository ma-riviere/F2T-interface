package display;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;

import main.Main;

/**
 * Main frame of the interface
 * @author simon gay
 */

public class DisplayFrame extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	private static int IMAGE_X=5;
	private static int IMAGE_Y=60;
	
	private static int LISTS_X=1100;
	private static int LISTS_Y=60;
	
	private static int PATH_X=1100;
	private static int PATH_Y=450;
	
	
	private Main main;
	
	private DisplayValuesPanel valuesPanel;
	
	private DisplaySettingsPanel settingsPanel;
	
	private DisplayImagePanel imagePanel;
	private DisplayMiniaturesPanel miniaturePanel;
	private DisplayFilePanel filePanel;
	private DisplayPathSettingPanel pathPanel;
	private DisplayAttractorSettingPanel attractorPanel;
	public DisplayFlowSettingPanel flowPanel;
	
	private DisplaySourcePanel sourcePanel;
	private DisplaySourceSettingPanel sourceSettingPanel;
	
	private DisplayLinksPanel linkPanel;
	private DisplayLinksSettingPanel linkSettingPanel;
	
	private DisplayAgesPanel agesPanel;
	private DisplayAgeMiniaturesPanel ageMiniaturesPanel;
	
	private DisplayWorldPanel worldPanel;
	
	private DisplayConsolePanel consolePanel;
	
	private DisplayPathEditPanel pathEditPanel;
	
	private DisplayAreaEditPanel areaEditPanel;
	
	private DisplayAttractorEditPanel attractorEditPanel;
	
	public DisplayFlowEditPanel flowEditPanel;
	
	private WindowAdapter listener;
	
	public int display_mode=0;
	public int selected_image=0;
	
	public boolean displayValues=true;
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
	
	private JButton mode1;
	private JButton mode2;
	private JButton mode3;
	private JButton mode4;
	private JButton mode5;
	private JButton mode6;
	private JButton mode7;
	private JButton mode8;
	private JButton mode9;
	private JButton mode10;
	
	/////////////////////////////////////////////
	
	public DisplayFrame(Main m){
		
		main=m;
		
		this.setTitle("View port");
    	this.setSize(1490, 800);
    	this.setLocationRelativeTo(null);               
    	this.setVisible(true);
    	
    	this.setLayout(null);
		
    	this.setFocusable(true);
    	
    	keyboard=new KeyboardListener();
    	
    	addKeyListener(keyboard);

    	int offset=5;
    	
    	int width=85;
    	
    	mode1=new JButton("setup");
    	mode1.setFont(new Font("Dialog", Font.PLAIN, 12));
		mode1.addActionListener(this);
		mode1.addKeyListener(keyboard);
		this.add(mode1);
		mode1.setBounds(offset, 5, width, 50);
		
		offset+=width;
		
		mode10=new JButton("flow");
		mode10.setFont(new Font("Dialog", Font.PLAIN, 12));
		mode10.addActionListener(this);
		mode10.addKeyListener(keyboard);
		this.add(mode10);
		mode10.setBounds(offset, 5, width, 50);
		
		offset+=width;
		
		mode7=new JButton("path");
		mode7.setFont(new Font("Dialog", Font.PLAIN, 12));
		mode7.addActionListener(this);
		mode7.addKeyListener(keyboard);
		this.add(mode7);
		mode7.setBounds(offset, 5, width, 50);
		
		offset+=width;
		
		mode9=new JButton("Attract");
		mode9.setFont(new Font("Dialog", Font.PLAIN, 12));
		mode9.addActionListener(this);
		mode9.addKeyListener(keyboard);
		this.add(mode9);
		mode9.setBounds(offset, 5, width, 50);
		
		offset+=width;
		
		mode2=new JButton("sources");
		mode2.setFont(new Font("Dialog", Font.PLAIN, 12));
		mode2.addActionListener(this);
		mode2.addKeyListener(keyboard);
		this.add(mode2);
		mode2.setBounds(offset, 5, width, 50);
		
		offset+=width;
		
		mode8=new JButton("areas");
		mode8.setFont(new Font("Dialog", Font.PLAIN, 12));
		mode8.addActionListener(this);
		mode8.addKeyListener(keyboard);
		this.add(mode8);
		mode8.setBounds(offset, 5, width, 50);
		
		offset+=width;
		
		mode3=new JButton("links");
		mode3.setFont(new Font("Dialog", Font.PLAIN, 12));
		mode3.addActionListener(this);
		mode3.addKeyListener(keyboard);
		this.add(mode3);
		mode3.setBounds(offset, 5, width, 50);
		
		offset+=width;
		
		mode4=new JButton("story");
		mode4.setFont(new Font("Dialog", Font.PLAIN, 12));
		mode4.addActionListener(this);
		mode4.addKeyListener(keyboard);
		this.add(mode4);
		mode4.setBounds(offset, 5, width, 50);
		
		offset+=width;
		
		mode5=new JButton("Ages");
		mode5.setFont(new Font("Dialog", Font.PLAIN, 12));
		mode5.addActionListener(this);
		mode5.addKeyListener(keyboard);
		this.add(mode5);
		mode5.setBounds(offset, 5, width, 50);
		
		offset+=width;
		
		mode6=new JButton("console");
		mode6.setFont(new Font("Dialog", Font.PLAIN, 12));
		mode6.addActionListener(this);
		mode6.addKeyListener(keyboard);
		this.add(mode6);
		mode6.setBounds(offset, 5, width, 50);

		offset+=width+5;
    	
		valuesPanel=new DisplayValuesPanel(main, this);
		this.add(valuesPanel);
		valuesPanel.setBounds(offset, 5, 1000, 51);
		
		settingsPanel=new DisplaySettingsPanel(main, this, keyboard);
		this.add(settingsPanel);
		settingsPanel.setBounds(LISTS_X, 680, 380, 101);
		
		imagePanel=new DisplayImagePanel(main, this);
		this.add(imagePanel);
		imagePanel.setBounds(IMAGE_X, IMAGE_Y, 700, 700);
		
		miniaturePanel=new DisplayMiniaturesPanel(main, this);
		this.add(miniaturePanel);
		miniaturePanel.setBounds(710+IMAGE_X, IMAGE_Y, 370, 700);
		
		filePanel=new DisplayFilePanel(main, keyboard);
		this.add(filePanel);
		filePanel.setBounds(LISTS_X, LISTS_Y, 380, 385);

		pathPanel=new DisplayPathSettingPanel(main, this, keyboard);
		this.add(pathPanel);
		pathPanel.setBounds(PATH_X, PATH_Y, 380, 230);
		
		attractorPanel=new DisplayAttractorSettingPanel(main, this, keyboard);
		attractorPanel.setVisible(false);
		this.add(attractorPanel);
		attractorPanel.setBounds(PATH_X, PATH_Y, 380, 230);
		
		flowPanel=new DisplayFlowSettingPanel(main, this, keyboard);
		flowPanel.setVisible(false);
		this.add(flowPanel);
		flowPanel.setBounds(PATH_X, PATH_Y, 380, 230);
		
		sourcePanel=new DisplaySourcePanel(main, this);
		sourcePanel.setVisible(false);
		this.add(sourcePanel);
		sourcePanel.setBounds(IMAGE_X, IMAGE_Y, 1000, 700);
		
		sourceSettingPanel=new DisplaySourceSettingPanel(main, this, keyboard);
		sourceSettingPanel.setVisible(false);
		this.add(sourceSettingPanel);
		sourceSettingPanel.setBounds(PATH_X, PATH_Y, 380, 230);
		
		linkPanel=new DisplayLinksPanel(main, this);
		linkPanel.setVisible(false);
		this.add(linkPanel);
		linkPanel.setBounds(IMAGE_X, IMAGE_Y, 700, 700);
		
		linkSettingPanel=new DisplayLinksSettingPanel(main, this, keyboard);
		linkSettingPanel.setVisible(false);
		this.add(linkSettingPanel);
		linkSettingPanel.setBounds(710+IMAGE_X, IMAGE_Y, 370, 700);
		
		
		agesPanel=new DisplayAgesPanel(main);
		agesPanel.setVisible(false);
		this.add(agesPanel);
		agesPanel.setBounds(IMAGE_X, IMAGE_Y, 1000, 700);
		
		ageMiniaturesPanel=new DisplayAgeMiniaturesPanel(main,keyboard);
		ageMiniaturesPanel.setVisible(false);
		this.add(ageMiniaturesPanel);
		ageMiniaturesPanel.setBounds(PATH_X, PATH_Y, 360, 230);
    	
		worldPanel=new DisplayWorldPanel(main);
		worldPanel.setVisible(false);
		this.add(worldPanel);
		worldPanel.setBounds(IMAGE_X, IMAGE_Y, 1000, 700);
		
		
		consolePanel=new DisplayConsolePanel(main, this);
		consolePanel.setVisible(false);
		this.add(consolePanel);
		consolePanel.setBounds(IMAGE_X, IMAGE_Y, 1000,700);
		
		pathEditPanel=new DisplayPathEditPanel(main, this);
		pathEditPanel.setVisible(false);
		this.add(pathEditPanel);
		pathEditPanel.setBounds(IMAGE_X, IMAGE_Y, 1090,700);
		
		areaEditPanel=new DisplayAreaEditPanel(main, this);
		areaEditPanel.setVisible(false);
		this.add(areaEditPanel);
		areaEditPanel.setBounds(IMAGE_X, IMAGE_Y, 1090,700);
		
		attractorEditPanel=new DisplayAttractorEditPanel(main, this);
		attractorEditPanel.setVisible(false);
		this.add(attractorEditPanel);
		attractorEditPanel.setBounds(IMAGE_X, IMAGE_Y, 1090,700);
		
		flowEditPanel=new DisplayFlowEditPanel(main, this);
		flowEditPanel.setVisible(false);
		this.add(flowEditPanel);
		flowEditPanel.setBounds(IMAGE_X, IMAGE_Y, 1090,700);
		
    	listener=new WindowAdapter() {
      		public void windowClosing(WindowEvent e) {
      			main.close();
      			System.exit(0);
      		}
  	   	};
  	   	this.addWindowListener(listener);
	}
	
	
	public void repaint(){
		
		if (displayValues) valuesPanel.repaint();
		
		if (display_mode==0){
			imagePanel.repaint();
			miniaturePanel.repaint();
			pathEditPanel.repaint();
		}
		else if (display_mode==1){
			sourcePanel.repaint();
		}
		else if (display_mode==2){
			linkPanel.repaint();
			linkSettingPanel.repaint();
			ageMiniaturesPanel.repaint();
		}
		else if (display_mode==3){
			agesPanel.repaint();
			ageMiniaturesPanel.repaint();
		}
		else if (display_mode==4){
			worldPanel.repaint();
			ageMiniaturesPanel.repaint();
		}
		else if (display_mode==5){
			ageMiniaturesPanel.repaint();
		}
		else if (display_mode==6){
			pathEditPanel.repaint();
		}
		else if (display_mode==7){
			ageMiniaturesPanel.repaint();
			areaEditPanel.repaint();
		}
		else if (display_mode==8){
			attractorEditPanel.repaint();
		}
		else if (display_mode==9){
			flowEditPanel.repaint();
		}
	}
	
	public void updateMiniatures(){
		miniaturePanel.update();
	}
	
	public void updateIndex(int id){
		filePanel.updateIndex(id);
	}
	
	public void updatePathPanel(int x, int y){
		posx=x;
		posy=y;
		if (pathPanel!=null) pathPanel.repaint();
	}
	
	public void updateFlowPanel(int x, int y){
		posx=x;
		posy=y;
		if (flowPanel!=null) flowPanel.repaint();
	}
	
	public void updateSourcePanel(int x, int y){
		posx=x;
		posy=y;	
		if (sourceSettingPanel!=null) sourceSettingPanel.repaint();
	}
	
	public void updateLinks(){
		linkSettingPanel.update();
	}
	
	public void updateConsole(String msg){
		consolePanel.setMsg(msg);
		consolePanel.repaint();
	}
	
	
	public void updateAreaEdit(){
		areaEditPanel.change=true;
		areaEditPanel.repaint();
	}
	
	public String getSourceParameters(){
		return sourceSettingPanel.getParameters();
	}
	
	public void rescan(){
		filePanel.rescan();
	}
	
	public void setMode(int mode){
		
		display_mode=mode;
		
		imagePanel.setVisible(false);
		miniaturePanel.setVisible(false);
		pathPanel.setVisible(false);
		attractorPanel.setVisible(false);
		flowPanel.setVisible(false);
		
		sourcePanel.setVisible(false);
		sourceSettingPanel.setVisible(false);
		
		linkPanel.setVisible(false);
		linkSettingPanel.setVisible(false);
		
		agesPanel.setVisible(false);
		ageMiniaturesPanel.setVisible(false);
		worldPanel.setVisible(false);
		
		consolePanel.setVisible(false);
		
		pathEditPanel.setVisible(false);
		areaEditPanel.setVisible(false);
		attractorEditPanel.setVisible(false);
		flowEditPanel.setVisible(false);
		
		if (mode==0){
			imagePanel.setVisible(true);
			miniaturePanel.setVisible(true);
			pathPanel.setVisible(true);
		}
		else if (mode==1){
			sourcePanel.setVisible(true);
			sourceSettingPanel.setVisible(true);
		}
		else if (mode==2){
			linkPanel.setVisible(true);
			linkSettingPanel.setVisible(true);

			ageMiniaturesPanel.setVisible(true);
		}
		else if (mode==3){
			agesPanel.setVisible(true);
			ageMiniaturesPanel.setVisible(true);
		}
		else if (mode==4){
			ageMiniaturesPanel.setVisible(true);
			worldPanel.setVisible(true);
		}
		else if (mode==5){
			ageMiniaturesPanel.setVisible(true);
			consolePanel.setVisible(true);
		}
		else if (mode==6){
			pathPanel.setVisible(true);
			pathEditPanel.setVisible(true);
		}
		else if (mode==7){
			ageMiniaturesPanel.setVisible(true);

			areaEditPanel.setVisible(true);
			areaEditPanel.change=true;
		}
		else if (mode==8){
			attractorPanel.setVisible(true);
			attractorEditPanel.setVisible(true);
		}
		else if (mode==9){
			flowPanel.setVisible(true);
			flowEditPanel.setVisible(true);
		}
	}
	
	
	
	public void setDisplayTrace(boolean set){
		displayTrace=set;
	}
	public void setDisplayJoystick(boolean set){
		displayJoystick=set;
	}
	public void setDisplayValues(boolean set){
		displayValues=set;
		valuesPanel.setVisible(set);
	}
	
	


	public void actionPerformed(ActionEvent e) {
		
		///////////////////////////////////////////////////////////////////////
		if (e.getSource()==mode1) setMode(0);
		if (e.getSource()==mode2) setMode(1);
		if (e.getSource()==mode3) setMode(2);
		if (e.getSource()==mode4) setMode(3);
		if (e.getSource()==mode5) setMode(4);
		if (e.getSource()==mode6) setMode(5);
		if (e.getSource()==mode7) setMode(6);
		if (e.getSource()==mode8) setMode(7);
		if (e.getSource()==mode9) setMode(8);
		if (e.getSource()==mode10)setMode(9);
		///////////////////////////////////////////////////////////////////////

	}
}
