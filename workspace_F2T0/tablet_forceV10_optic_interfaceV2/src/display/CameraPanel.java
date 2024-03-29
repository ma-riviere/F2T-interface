package display;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import main.Main;



/**
 * Display the probe input
 * @author simon gay
 */

/* - inherited from EnvPanel :
 *   Agent agent      : pointer to the agent
 */
public class CameraPanel extends JPanel implements MouseListener{
	
	private static final long serialVersionUID = 1L;
	
	private static int PRESET=650;
	private static int IMG=780;
	private static int TACT=910;
	private static int FLOW=1040;
	private static int RAIL=1170;
	private static int PATH=1300;
	private static int SCRIPT=1430;
	

	public Main main;

	private boolean rescan_pressed=false;
	private boolean save_pressed=false;
	private boolean mode_pressed=false;
	private boolean pause_pressed=false;
	private boolean clear_pressed=false;
	private boolean clearLast_pressed=false;
	private boolean savePath_pressed=false;
	

	//private boolean guide_mode=false;
	
	public CameraPanel(Main m){
		main=m;
		addMouseListener(this);
	}
	
	public void paintComponent(Graphics g){
		
		// draw image camera
		if (Main.CAMERA_CONNECTED){
			Graphics2D g2d = (Graphics2D) g.create();
			AffineTransform at = new AffineTransform();
	        at.setToRotation(Math.PI, main.webcam.width()/2, main.webcam.height()/2);
	        g2d.setTransform(at);
	        Image image = Main.Mat2bufferedImage(main.webcam);
	        g2d.drawImage(image, 5, 5, this);
		}
		
        // draw joystick position
 		g.setColor(Color.gray);
 		g.drawOval(325+(int)(main.x_prev*0.5+35), 245-(int)(main.y_prev*0.5-35), 50, 50);
 		g.setColor(Color.white);
 		g.drawOval(325+(int)(main.x*0.5+35), 245-(int)(main.y*0.5-35), 50, 50);
 		g.drawLine(350+(int)(main.x*0.5+35), 270-(int)(main.y*0.5-35), 350+(int)(main.x*0.5+35)+(int)main.dx*5, 270-(int)(main.y*0.5-35)-(int)main.dy*5);
 		g.setColor(Color.red);
 		g.drawOval(325+(int)(main.x_next*0.5+35), 245-(int)(main.y_next*0.5-35), 50, 50);
    
        
        g.setColor(Color.black);
        for (int i=0;i<main.listPreset.length;i++){
            g.fillRect(PRESET, 10+i*20, 10, 10);
            g.drawString(main.listPreset[i], PRESET+15, 20+20*i);
        }
		
        if (main.selected_img==-1) g.setColor(Color.red);
        else g.setColor(Color.black);
        g.fillOval(IMG, 10, 10, 10);
        g.drawString("No image", IMG+15, 20);
        for (int i=0;i<main.listImages.length;i++){
        	if (main.selected_img==i) g.setColor(Color.red);
            else g.setColor(Color.black);
            g.fillOval(IMG, 30+i*20, 10, 10);
            g.drawString(main.listImages[i], IMG+15, 40+20*i);
        }
	
        if (main.selected_tactile==-1) g.setColor(Color.red);
        else g.setColor(Color.black);
        g.fillOval(TACT, 10, 10, 10);
        g.drawString("No tactile", TACT+15, 20);
        for (int i=0;i<main.listTactile.length;i++){
        	if (main.selected_tactile==i) g.setColor(Color.red);
            else g.setColor(Color.black);
            g.fillOval(TACT, 30+i*20, 10, 10);
            g.drawString(main.listTactile[i], TACT+15, 40+20*i);
        }
        
        if (main.selected_flow==-1) g.setColor(Color.red);
        else g.setColor(Color.black);
        g.fillOval(FLOW, 10, 10, 10);
        g.drawString("No flow", FLOW+15, 20);
        for (int i=0;i<main.listFlow.length;i++){
        	if (main.selected_flow==i) g.setColor(Color.red);
            else g.setColor(Color.black);
            g.fillOval(FLOW, 30+i*20, 10, 10);
            g.drawString(main.listFlow[i], FLOW+15, 40+20*i);
        }
        
        if (main.selected_rail==-1) g.setColor(Color.red);
        else g.setColor(Color.black);
        g.fillOval(RAIL, 10, 10, 10);
        g.drawString("No rails", RAIL+15, 20);
        for (int i=0;i<main.listRail.length;i++){
        	if (main.selected_rail==i) g.setColor(Color.red);
            else g.setColor(Color.black);
            g.fillOval(RAIL, 30+i*20, 10, 10);
            g.drawString(main.listRail[i], RAIL+15, 40+20*i);
        }
        
        g.setColor(Color.black);
        for (int i=0;i<main.listPath.length;i++){
            g.fillRect(PATH, 10+i*20, 10, 10);
            g.drawString(main.listPath[i], PATH+15, 20+20*i);
        }
        
        g.setColor(Color.black);
        for (int i=0;i<main.listScript.length;i++){
            g.fillRect(SCRIPT, 10+i*20, 10, 10);
            g.drawString(main.listScript[i], SCRIPT+15, 20+20*i);
        }
        
        g.setColor(Color.black);
        g.drawRect(10, 490, 120, 140);
        
        // rescan button
        drawButton(g,20,500,"Rescan files", rescan_pressed);
        
        // save button
        drawButton(g,20,570,"save preset", save_pressed);

        
        g.setColor(Color.black);
        g.drawRect(140, 490, 495, 140);
        
        // guide mode button
        drawButton(g,150,500,"Guide mode", mode_pressed);
        
        // mode leds
        if (main.target_type==0){
        	g.setColor(Color.black);
        	g.fillOval(260, 510, 10, 10);
        	g.setColor(Color.red);
        	g.fillOval(260, 530, 10, 10);
        }
        else{
        	g.setColor(Color.red);
        	g.fillOval(260, 510, 10, 10);
        	g.setColor(Color.black);
        	g.fillOval(260, 530, 10, 10);
        }
        g.setColor(Color.black);
        g.drawString("full guide", 280, 520);
        g.drawString("attractor", 280, 540);
        
        
        // speed slider
        g.drawString("Speed : "+main.target_speed, 390,520);
        g.setColor(Color.darkGray);
        g.fillRect(380, 535, 250, 10);
        g.setColor(Color.lightGray);
        g.fillRect(375+main.target_speed/2, 530, 10, 20);
        
        
        // pause button
        drawButton(g,150,570,"", pause_pressed);
        g.setColor(Color.black);
        if (!main.target_pause){
        	g.drawLine(190,585,190,605);
        	g.drawLine(190,585,215,595);
        	g.drawLine(190,605,215,595);
        }
        else{
        	g.fillRect(195, 585, 5, 20);
        	g.fillRect(205, 585, 5, 20);
        }
        
        // clear button
        drawButton(g,270,570,"Clear points", clear_pressed);
        
        // clear last
        drawButton(g,390,570,"Clear last", clearLast_pressed);
        
        // save button
        drawButton(g,510,570,"Save path", savePath_pressed);
        
        
        g.setColor(Color.black);
        g.drawString("Age "+(main.script.getCurrentIndex()+1)+" / "+main.script.size(), 10, 700);
        
        for (int a=0;a<main.script.size();a++){
        	g.drawString(main.script.sequence.get(a).getImages(), 10+a*600, 730);
        	g.drawString(main.script.sequence.get(a).getPath(), 10+a*600, 760);
        	g.drawString(main.script.sequence.get(a).getAreas(), 10+a*600, 790);
        	g.drawString(main.script.sequence.get(a).getSources(), 10+a*600, 820);
        	g.drawString(main.script.sequence.get(a).getSources(), 10+a*600, 850);
        	g.drawString(main.script.sequence.get(a).getCondition(), 10+a*600, 880);
        }
	}

	
	private void drawButton(Graphics g, int x, int y, String msg, boolean pressed){
		if (pressed){
        	g.setColor(new Color(180,180,180));
        	g.fillRect(x, y, 100, 50);
        	g.setColor(new Color(150,150,150));
        	g.fillRect(x, y, 100, 5);
        	g.fillRect(x, y, 5, 50);
        	g.setColor(new Color(220,220,220));
        	g.fillRect(x, 45+y, 100, 5);
        	g.fillRect(95+x, y, 5, 50);
        }
        else{
        	g.setColor(new Color(200,200,200));
        	g.fillRect(x, y, 100, 50);
        	g.setColor(new Color(220,220,220));
        	g.fillRect(x, y, 100, 5);
        	g.fillRect(x, y, 5, 50);
        	g.setColor(new Color(150,150,150));
        	g.fillRect(x, 45+y, 100, 5);
        	g.fillRect(95+x, y, 5, 50);
        }
        g.setColor(Color.black);
        g.drawString(msg, 13+x, 30+y);
	}
	

	public void mouseClicked(MouseEvent e) {

		// preset
		int selected=-1;
		for (int i=0;i<main.listPreset.length;i++){
			if (e.getX()>PRESET-5 && e.getX()<PRESET+15 && e.getY()>5+i*20 && e.getY()<25+i*20) selected=i;
		}
		if (selected>-1) main.setPreset(selected);
		
		// script
		selected=-1;
		for (int i=0;i<main.listScript.length;i++){
			if (e.getX()>SCRIPT-5 && e.getX()<SCRIPT+15 && e.getY()>5+i*20 && e.getY()<25+i*20) selected=i;
		}
		if (selected>-1) main.setScript(selected);
		
		// path
		selected=-1;
		for (int i=0;i<main.listPath.length;i++){
			if (e.getX()>PATH-5 && e.getX()<PATH+15 && e.getY()>5+i*20 && e.getY()<25+i*20) selected=i;
		}
		if (selected>-1) main.setPath(selected);
		
		// picture
		if (e.getX()>IMG-5 && e.getX()<IMG+15 && e.getY()>5 && e.getY()<25){
			main.setPicture(null);
		}
		else{
			selected=-1;
			for (int i=0;i<main.listImages.length;i++){
				if (e.getX()>IMG-5 && e.getX()<IMG+15 && e.getY()>25+i*20 && e.getY()<45+i*20) selected=i;
			}
			if (selected>-1) main.setPicture(main.listImages[selected]);
		}
		
		// tactile
		if (e.getX()>TACT-5 && e.getX()<TACT+15 && e.getY()>5 && e.getY()<25){
			main.setTactile(null);
		}
		else{
			selected=-1;
			for (int i=0;i<main.listTactile.length;i++){
				if (e.getX()>TACT-5 && e.getX()<TACT+15 && e.getY()>25+i*20 && e.getY()<45+i*20) selected=i;
			}
			if (selected>-1) main.setTactile(main.listTactile[selected]);
		}
		
		// flow
		if (e.getX()>FLOW-5 && e.getX()<FLOW+15 && e.getY()>5 && e.getY()<25){
			main.setFlow(null);
		}
		else{
			selected=-1;
			for (int i=0;i<main.listFlow.length;i++){
				if (e.getX()>FLOW-5 && e.getX()<FLOW+15 && e.getY()>25+i*20 && e.getY()<45+i*20) selected=i;
			}
			if (selected>-1) main.setFlow(main.listFlow[selected]);
		}
		
		// rail
		if (e.getX()>RAIL-5 && e.getX()<RAIL+15 && e.getY()>5 && e.getY()<25){
			main.setRail(null);
		}
		else{
			selected=-1;
			for (int i=0;i<main.listRail.length;i++){
				if (e.getX()>RAIL-5 && e.getX()<RAIL+15 && e.getY()>25+i*20 && e.getY()<45+i*20) selected=i;
			}
			if (selected>-1) main.setRail(main.listRail[selected]);
		}
	}
	
	
	public void mousePressed(MouseEvent e) {
		// rescan button
		if (e.getX()>20 && e.getX()<120 && e.getY()>500 && e.getY()<550){
			if (!rescan_pressed && !save_pressed && !mode_pressed && !pause_pressed && !clear_pressed && !clearLast_pressed){
				rescan_pressed=true;
				main.listFiles();
			}
		}
		
		// save button
		if (e.getX()>20 && e.getX()<120 && e.getY()>570 && e.getY()<620){
			if (!rescan_pressed && !save_pressed && !mode_pressed && !pause_pressed && !clear_pressed && !clearLast_pressed){
				save_pressed=true;
				main.savePreset();
			}
		}
		
		// mode button
		if (e.getX()>150 && e.getX()<250 && e.getY()>500 && e.getY()<550){
			if (!rescan_pressed && !save_pressed && !mode_pressed && !pause_pressed && !clear_pressed && !clearLast_pressed){
				mode_pressed=true;
				main.target_type=(main.target_type+1)%2;
			}
		}
		
		// pause button
		if (e.getX()>150 && e.getX()<250 && e.getY()>570 && e.getY()<620){
			if (!rescan_pressed && !save_pressed && !mode_pressed && !pause_pressed && !clear_pressed && !clearLast_pressed){
				pause_pressed=true;
				main.target_pause=!main.target_pause;
			}
		}
		
		// clear button
		if (e.getX()>270 && e.getX()<370 && e.getY()>570 && e.getY()<620){
			if (!rescan_pressed && !save_pressed && !mode_pressed && !pause_pressed && !clear_pressed && !clearLast_pressed && !savePath_pressed){
				clear_pressed=true;
				System.out.println("clear points");
				main.currentAge.targetSequence.clear();
			}
		}
		
		// clear last button
		if (e.getX()>390 && e.getX()<490 && e.getY()>570 && e.getY()<620){
			if (!rescan_pressed && !save_pressed && !mode_pressed && !pause_pressed && !clear_pressed && !clearLast_pressed && !savePath_pressed){
				clearLast_pressed=true;
				System.out.println("clear last point");
				main.currentAge.targetSequence.remove(main.currentAge.targetSequence.size()-1);
			}
		}
		
		// save path button
		if (e.getX()>510 && e.getX()<610 && e.getY()>570 && e.getY()<620){
			if (!rescan_pressed && !save_pressed && !mode_pressed && !pause_pressed && !clear_pressed && !clearLast_pressed && !savePath_pressed){
				savePath_pressed=true;
				main.savePath();
			}
		}
		
		if (e.getX()>380 && e.getX()<630 && e.getY()>520 && e.getY()<560){
			main.target_speed=(e.getX()-380)*2;
		}
		//System.out.println(e.getX()+" ; "+e.getY());
	}
	
	public void mouseReleased(MouseEvent e) {
		rescan_pressed=false;
		save_pressed=false;
		mode_pressed=false;
		clear_pressed=false;
		clearLast_pressed=false;
		pause_pressed=false;
		savePath_pressed=false;
	}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}
