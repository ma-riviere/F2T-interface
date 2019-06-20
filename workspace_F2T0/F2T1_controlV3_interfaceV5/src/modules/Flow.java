package modules;

import java.awt.Color;
import java.awt.Graphics;

import main.Main;
import main.Virtual;

public class Flow extends Module{

	public int flowX_read=0;
	public int flowY_read=0;

	public Flow(Main m, Virtual v){
		super(m,v);
		name="flow";
		panelWidth=160;
	}

	public void compute(){
		if (main.currentAge.image.flow!=null){
			
			float flowX=0;
			float flowY=0;
			
			for (int i=-2;i<=2;i++){
				for (int j=-2;j<=2;j++){
					flowX+=main.currentAge.image.flow_mat[Math.min(699,Math.max(0,350+(int)(virtual.px)+i))][Math.min(699,Math.max(0,350-(int)(virtual.py)+j))][0]-128;
					flowY+=main.currentAge.image.flow_mat[Math.min(699,Math.max(0,350+(int)(virtual.px)+i))][Math.min(699,Math.max(0,350-(int)(virtual.py)+j))][1]-128;
				}
			}
			
			flowX_read=(int)(flowX/25);
			flowY_read=(int)(flowY/25);

			virtual.mx_temp+=flowX_read*10;
			virtual.my_temp+=flowY_read*10;
		}
	}
	
	public float getValue(int i){
		if (i==0) return flowX_read;
		else if (i==1) return flowY_read;
		else return 0;
	}
	
	public void paint(Graphics g, int x){
		
		String msg;
		
		g.setColor(Color.black);
		g.drawString("flow vector : ", x, 16);
		float val1=((float)(flowX_read)/127)*100;
		float val2=((float)(flowY_read)/127)*100;
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
		g.drawString(msg, x, 37);
		
		g.setColor(Color.gray);
		g.fillOval(100+x, 0, 50, 50);
		g.setColor(Color.black);
		g.drawOval(100+x, 0, 50, 50);
		
		g.setColor(Color.red);
		g.drawLine(125+x, 25, 125+x+(int)(val1/4), 25-(int)(val2/4));
		
	}
}
