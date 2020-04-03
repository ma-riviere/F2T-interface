package modules;

import java.awt.Color;
import java.awt.Graphics;

import main.Main;
import main.Virtual;

/**
 * Railr module
 * @author simon gay
 */

public class Rail extends Module{

	public int railX_read=0;
	public int railY_read=0;
	
	public Rail(Main m, Virtual v){
		super(m,v);
		name="rail";
		panelWidth=160;
	}
	
	public void compute(){
		if (main.currentAge.image.rail!=null){

			railY_read=-(main.currentAge.image.rail_mat[Math.min(699,Math.max(0,350+(int)(virtual.px)))][Math.min(699,Math.max(0,350-(int)(virtual.py)))][0]-128)*2;
			railX_read=-(main.currentAge.image.rail_mat[Math.min(699,Math.max(0,350+(int)(virtual.px)))][Math.min(699,Math.max(0,350-(int)(virtual.py)))][1]-128)*2;

			float scalar=virtual.mx_temp*railX_read + virtual.my_temp*(-railY_read);
			float norm2=railX_read*railX_read+railY_read*railY_read;
			
			if (norm2>0){
				
				float offX=  scalar * railX_read/norm2;
				float offY= -scalar * railY_read/norm2;
				float norm=(float)Math.sqrt(norm2)/100;
				if (norm>0.9) norm=0.9f;

				virtual.mx_temp-=offX* norm;
				virtual.my_temp-=offY* norm;
				
				offsetX=-offX* norm;
				offsetY=-offY* norm;
			}
		}
	}
	
	public float getValue(int i){
		if (i==0) return railX_read;
		else if (i==1) return railY_read;
		else return 0;
	}

	
	public void paint(Graphics g, int x){
		
		String msg;
		
		g.setColor(Color.black);
		g.drawString("rail vector : ", x, 16);
		float val1=((float)(railY_read)/127)*50;
		float val2=((float)(railX_read)/127)*50;

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
		g.drawString(msg, x, 37);
		
		g.setColor(Color.gray);
		g.fillOval(100+x, 0, 50, 50);
		g.setColor(Color.black);
		g.drawOval(100+x, 0, 50, 50);
		
		g.setColor(Color.red);
		g.drawLine(125+x-(int)(val1/4), 25+(int)(val2/4), 125+x+(int)(val1/4), 25-(int)(val2/4));
	}
	
}
