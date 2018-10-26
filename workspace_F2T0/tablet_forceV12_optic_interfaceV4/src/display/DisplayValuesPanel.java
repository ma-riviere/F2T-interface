package display;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import main.Main;

public class DisplayValuesPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	public Main main;
	public DisplayFrame frame;
	
	public DisplayValuesPanel(Main m, DisplayFrame f){
		main=m;
		frame=f;
		this.setLayout(null);
	}
	
	
	public void paintComponent(Graphics g){
		
		g.setColor(this.getBackground());
		g.fillRect(0, 0, 900, 50);
		
		g.setColor(Color.black);
		g.drawRect(0, 0, 899, 50);
		
		String msg="";
		
		int val=(int)(main.friction_fluid_read*100);
		if (val==100) msg="100";
		else if (val>=10) msg="  "+val;
		else msg="    "+val;
		g.drawString("fluid  friction : "+msg+" %", 10, 15);
		
		val=(int)(main.friction_solid_read*100);
		if (val==100) msg="100";
		else if (val>=10) msg="  "+val;
		else msg="    "+val;
		g.drawString("solid friction : "+msg+" %", 10, 30);
		
		
		val=(int)(main.contactHeight*100);
		if (val==100) msg="100";
		else if (val>=10) msg="  "+val;
		else msg="    "+val;
		g.drawString("elevation      : "+msg+" %", 10, 45);
		
		for (int i=0;i<25;i++){
			for (int j=0;j<25;j++){
				
				float pixel=main.heighMap[i][j];
				g.setColor(new Color(0, pixel,0));
				g.fillRect(160+i*2, j*2, 2, 2);
				
			}
		}
		g.setColor(Color.LIGHT_GRAY);
		g.drawOval(160,0,50,50);
		
		g.setColor(Color.black);
		g.drawString("flow vector : ", 250, 16);
		float val1=((float)(main.flowX_read-125)/127)*100;
		float val2=((float)(main.flowY_read-125)/127)*100;
		msg="(";
		if (val1>=0){
			if (val1==100) msg+=" 100";
			else if (val1>=10) msg+="    "+(int)val1;
			else msg+="      "+(int)val1;
		}
		else{
			if (val1==100) msg+="-100";
			else if (val1>=10) msg+="  "+(int)val1;
			else msg+="    "+(int)val1;
		}
		msg+=" %,";
		if (val2>=0){
			if (val2==100) msg+=" 100";
			else if (val2>=10) msg+="   "+(int)val2;
			else msg+="     "+(int)val2;
		}
		else{
			if (val2==100) msg+="-100";
			else if (val2>=10) msg+="  "+(int)val2;
			else msg+="    "+(int)val2;
		}
		msg+=" %)";
		g.drawString(msg, 250, 37);
		
		g.setColor(Color.gray);
		g.fillOval(370, 0, 50, 50);
		g.setColor(Color.black);
		g.drawOval(370, 0, 50, 50);
		
		g.setColor(Color.red);
		g.drawLine(395, 25, 395+(int)(val1/4), 25-(int)(val2/4));
		
		
		g.setColor(Color.black);
		g.drawString("rail vector : ", 455, 16);
		val1=((float)(main.railX_read-125)/127)*100;
		val2=((float)(main.railY_read-125)/127)*100;

		msg="(";
		if (val1>=0){
			if (val1==100) msg+=" 100";
			else if (val1>=10) msg+="    "+(int)val1;
			else msg+="      "+(int)val1;
		}
		else{
			if (val1==100) msg+="-100";
			else if (val1>=10) msg+="  "+(int)val1;
			else msg+="    "+(int)val1;
		}
		msg+=" %,";
		if (val2>=0){
			if (val2==100) msg+=" 100";
			else if (val2>=10) msg+="   "+(int)val2;
			else msg+="     "+(int)val2;
		}
		else{
			if (val2==100) msg+="-100";
			else if (val2>=10) msg+="  "+(int)val2;
			else msg+="    "+(int)val2;
		}
		msg+=" %)";
		g.drawString(msg, 455, 37);
		
		g.setColor(Color.gray);
		g.fillOval(570, 0, 50, 50);
		g.setColor(Color.black);
		g.drawOval(570, 0, 50, 50);
		
		g.setColor(Color.red);
		g.drawLine(595-(int)(val1/4), 25+(int)(val2/4), 595+(int)(val1/4), 25-(int)(val2/4));
		
		
		g.setColor(Color.black);
		g.drawString("current areas : ", 670, 14);
		
		if (main.area_red_read==0 && main.area_green_read==0 && main.area_blue_read==0){
			g.drawString("0 :", 670, 35);
			g.setColor(Color.black);
			g.fillRect(700, 20, 20, 20);
		}
		else{
			int i=0;
			if (main.area_red_read>0){
				g.setColor(Color.black);
				g.drawString(main.area_red_read+" :", 670, 35);
				g.setColor(new Color(10*main.area_red_read,0,0));
				g.fillRect(700, 20, 20, 20);
				i++;
			}
			
			if (main.area_green_read>0){
				g.setColor(Color.black);
				g.drawString((main.area_green_read+25)+" :", 670+70*i, 35);
				g.setColor(new Color(0,10*main.area_green_read,0));
				g.fillRect(700+70*i, 20, 20, 20);
				i++;
			}
			
			if (main.area_blue_read>0){
				g.setColor(Color.black);
				g.drawString((main.area_blue_read+50)+" :", 670+70*i, 35);
				g.setColor(new Color(0,0,10*main.area_blue_read));
				g.fillRect(700+70*i, 20, 20, 20);
				i++;
			}
		}
		
	}
	

}
