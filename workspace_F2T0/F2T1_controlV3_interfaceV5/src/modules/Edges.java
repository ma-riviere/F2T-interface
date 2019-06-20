package modules;


import java.awt.Graphics;

import main.Main;
import main.Virtual;

public class Edges extends Module{

	public static float SCALE=1500;
	
	public float contactHeight=0;
	public float gradientX=0;
	public float gradientY=0;

	public Edges(Main m, Virtual v){
		super(m,v);
		name="friction";
		panelWidth=100;
	}

	public void compute(){
		
		float gradX=0;
		float gradY=0;
		float d2=0;
		float sum=0;
		int count=0;
		
		//contactHeight=(float)main.currentAge.image.tactile_mat[Math.min(699,Math.max(0,350+(int)(virtual.px)))][Math.min(699,Math.max(0,350-(int)(virtual.py)))][1]/255;
		
		
		for (int i2=-15;i2<=15;i2++){
			for (int j2=-15;j2<=15;j2++){
				if (i2!=0 || j2!=0){
					
					//if ((int)(virtual.px+main.dx*3+i2)>-350 && (int)(virtual.px+main.dx*3+i2)<350 && (int)(virtual.py+main.dy*3+j2)>-350 && (int)(virtual.py+main.dy*3+j2)<350){
					//	if (/*height2>height1 
					//			&&*/ (   grad[350+(int)(virtual.px+main.dx*3+i2)][350+(int)(virtual.py+main.dy*3+j2)][0]!=0 
					//			      || grad[350+(int)(virtual.px+main.dx*3+i2)][350+(int)(virtual.py+main.dy*3+j2)][1]!=0)){
					//		
					//		d2=1/((float)i2*(float)i2+(float)j2*(float)j2);
					//		gradX+=grad[350+(int)(virtual.px+main.dx*3+i2)][350+(int)(virtual.py+main.dy*3+j2)][0]*d2;
					//		gradY+=grad[350+(int)(virtual.px+main.dx*3+i2)][350+(int)(virtual.py+main.dy*3+j2)][1]*d2;
					//		sum+=d2;
					//		count++;
					//	}
					//}
					
					
					if ((int)(virtual.px+i2)>-350 && (int)(virtual.px+i2)<350 && (int)(virtual.py+j2)>-350 && (int)(virtual.py+j2)<350){
						if (/*height2>height1 
								&&*/ (   main.currentAge.image.gradient[350+(int)(virtual.px+i2)][350+(int)(virtual.py+j2)][0]!=0 
								      || main.currentAge.image.gradient[350+(int)(virtual.px+i2)][350+(int)(virtual.py+j2)][1]!=0)){
							
							//d2=1/((float)i2*(float)i2+(float)j2*(float)j2);
							
							if (i2*main.currentAge.image.gradient[350+(int)(virtual.px+i2)][350+(int)(virtual.py+j2)][0]>=0)
								gradX+=main.currentAge.image.gradient[350+(int)(virtual.px+i2)][350+(int)(virtual.py+j2)][0];//*d2;
							
							if (j2*main.currentAge.image.gradient[350+(int)(virtual.px+i2)][350+(int)(virtual.py+j2)][1]>=0)
								gradY+=main.currentAge.image.gradient[350+(int)(virtual.px+i2)][350+(int)(virtual.py+j2)][1];//*d2;
							sum+=1;//d2;*/
							count++;
						}
					}
				}
			}
		}
		
		if (count>4){
			gradientX=gradX/sum;
			gradientY=gradY/sum;

			virtual.mx_temp-= gradientX*SCALE;
			virtual.my_temp-= gradientY*SCALE;
			
			System.out.println((int)(gradientX*SCALE));
		}
		else{
			gradientX=0;
			gradientY=0;
		}
	}
	
	
	public float getValue(int i){
		if (i==0) return gradientX;
		else if (i==1) return gradientY;
		else if (i==2) return contactHeight;
		else return 0;
	}
	
	
	public void paint(Graphics g, int x){
		
		String msg="";
		
		int val=(int)(gradientX*100);
		if (val==100) msg="100";
		else if (val>=10) msg=" "+val;
		else msg="  "+val;
		g.drawString("grad X: "+msg+" %", 7+x, 15);
		
		val=(int)(gradientY*100);
		if (val==100) msg="100";
		else if (val>=10) msg=" "+val;
		else msg="  "+val;
		g.drawString("grad Y: "+msg+" %", 7+x, 30);
		
		val=(int)(contactHeight*100);
		if (val==100) msg="100";
		else if (val>=10) msg=" "+val;
		else msg="  "+val;
		g.drawString("height: "+msg+" %", 7+x, 45);
		
		/*g.setColor(Color.gray);
		g.fillOval(100+x, 0, 50, 50);
		g.setColor(Color.black);
		g.drawOval(100+x, 0, 50, 50);
		
		g.setColor(Color.red);
		g.drawLine(125+x, 25,125+x+(int)(gradientX*50), 25-(int)(gradientY*50));*/
	}
}
