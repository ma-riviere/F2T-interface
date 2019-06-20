package display;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import main.Main;
import modules.Edges;

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
		g.fillRect(0, 0, 615, 50);
		
		g.setColor(Color.black);
		g.drawRect(0, 0, 615, 50);
		
		String msg="";
		
		int val=(int)(main.friction.getValue(0)*100);
		if (val==100) msg="100";
		else if (val>=10) msg=" "+val;
		else msg="  "+val;
		g.drawString("fluid : "+msg+" %", 7, 15);
		
		val=(int)(main.friction.getValue(1)*100);
		if (val==100) msg="100";
		else if (val>=10) msg=" "+val;
		else msg="  "+val;
		g.drawString("solid: "+msg+" %", 7, 30);
		
		
		val=(int)(main.edges.contactHeight*100);
		if (val==100) msg="100";
		else if (val>=10) msg=" "+val;
		else msg="  "+val;
		g.drawString("height: "+msg+" %", 7, 45);
		
		for (int i=0;i<Edges.SIZE1;i++){
			for (int j=0;j<Edges.SIZE1;j++){
				
				float pixel=main.edges.heighMap[i*2][j*2];
				g.setColor(new Color(0, pixel,0));
				g.fillRect(100+i*2, j*2, 2, 2);
				
			}
		}
		g.setColor(Color.LIGHT_GRAY);
		g.drawOval(100,0,50,50);
		
		g.setColor(Color.black);
		g.drawString("flow vector : ", 160, 16);
		float val1=((float)(main.flow.getValue(0))/127)*100;
		float val2=((float)(main.flow.getValue(1))/127)*100;
		msg="(";
		if (val1>=0){
			if (val1==100) msg+=" 100";
			else if (val1>=10) msg+="   "+(int)val1;
			else msg+="    "+(int)val1;
		}
		else{
			if (val1==-100) msg+="-100";
			else if (val1<=-10) msg+=" "+(int)val1;
			else msg+="  "+(int)val1;
		}
		msg+=" %,";
		if (val2>=0){
			if (val2==100) msg+=" 100";
			else if (val2>=10) msg+="  "+(int)val2;
			else msg+="   "+(int)val2;
		}
		else{
			if (val2==-100) msg+="-100";
			else if (val2<=-10) msg+=" "+(int)val2;
			else msg+="  "+(int)val2;
		}
		msg+=" %)";
		g.drawString(msg, 160, 37);
		
		g.setColor(Color.gray);
		g.fillOval(260, 0, 50, 50);
		g.setColor(Color.black);
		g.drawOval(260, 0, 50, 50);
		
		g.setColor(Color.red);
		g.drawLine(285, 25, 285+(int)(val1/4), 25-(int)(val2/4));
		
		
		g.setColor(Color.black);
		g.drawString("rail vector : ", 325, 16);
		val1=((float)(main.rail.getValue(1))/127)*50;
		val2=((float)(main.rail.getValue(0))/127)*50;

		msg="(";
		if (val1>=0){
			if (val1==100) msg+=" 100";
			else if (val1>=10) msg+="   "+(int)val1;
			else msg+="    "+(int)val1;
		}
		else{
			if (val1==100) msg+="-100";
			else if (val1>=10) msg+=" "+(int)val1;
			else msg+="  "+(int)val1;
		}
		msg+=" %,";
		if (val2>=0){
			if (val2==100) msg+=" 100";
			else if (val2>=10) msg+="  "+(int)val2;
			else msg+="   "+(int)val2;
		}
		else{
			if (val2==100) msg+="-100";
			else if (val2>=10) msg+=" "+(int)val2;
			else msg+="  "+(int)val2;
		}
		msg+=" %)";
		g.drawString(msg, 325, 37);
		
		g.setColor(Color.gray);
		g.fillOval(440, 0, 50, 50);
		g.setColor(Color.black);
		g.drawOval(440, 0, 50, 50);
		
		g.setColor(Color.red);
		g.drawLine(465-(int)(val1/4), 25+(int)(val2/4), 465+(int)(val1/4), 25-(int)(val2/4));
		
		
		g.setColor(Color.black);
		g.drawString("current areas : ", 500, 14);
		
		if (main.area_red_read==0 && main.area_green_read==0 && main.area_blue_read==0){
			g.drawString("0:", 500, 30);
			g.setColor(Color.black);
			g.fillRect(500, 33, 20, 15);
		}
		else{
			int i=0;
			if (main.area_red_read>0){
				g.setColor(Color.black);
				g.drawString(main.area_red_read+":", 500, 30);
				g.setColor(new Color(10*main.area_red_read,0,0));
				g.fillRect(500, 33, 20, 15);
				i++;
			}
			
			if (main.area_green_read>0){
				g.setColor(Color.black);
				g.drawString((main.area_green_read+25)+" :", 500+30*i, 30);
				g.setColor(new Color(0,10*main.area_green_read,0));
				g.fillRect(500+30*i, 33, 20, 15);
				i++;
			}
			
			if (main.area_blue_read>0){
				g.setColor(Color.black);
				g.drawString((main.area_blue_read+50)+" :", 500+30*i, 30);
				g.setColor(new Color(0,0,10*main.area_blue_read));
				g.fillRect(500+30*i, 33, 20, 15);
				i++;
			}
		}
		
	}
	

}
