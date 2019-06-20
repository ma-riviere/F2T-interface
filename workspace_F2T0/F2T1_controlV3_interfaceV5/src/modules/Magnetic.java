package modules;

import main.Image;
import main.Main;
import main.Virtual;

public class Magnetic extends Module{

	public int xmin=-1;
	public int ymin=-1;
	public float force;
	
	public Magnetic(Main m, Virtual v){
		super(m,v);
		name="magnetic path";
	}

	public void compute(){
		if (main.currentAge.image.magnetic!=null){

			int xmin2=-1;
			int ymin2=-1;
			float min=1000000;
			
			float d2=0;
			
			if (main.currentAge.image.magnetic_mat[Math.min(699,Math.max(0,(int)(virtual.px+350)))][Math.min(699,Math.max(0,(int)(virtual.py+350)))]==0){
				for (int i=0;i<Image.SIZE;i++){
					for (int j=0;j<Image.SIZE;j++){
						
						if (main.currentAge.image.magnetic_mat[i][j]>0){
							d2= ((virtual.px+350)-(i))*((virtual.px+350)-(i)) + ((virtual.py+350)-(Image.SIZE-j))*((virtual.py+350)-(Image.SIZE-j));
							
							if (d2<min){
								min=d2;
								xmin2=i-350;
								ymin2=350-j;
							}	
						}
					}
				}
				
				xmin=xmin2;
				ymin=ymin2;
				
				d2=min/2;
				
				if (d2>10){
					force=200*500/(d2*10);
					float d=(float) Math.sqrt(d2);
					//System.out.println(force+" ; "+d+" ; "+main.py+" , "+xmin+" ; "+main.py+" , "+ymin);
					virtual.mx_temp -= force * (virtual.px-(xmin))/d;
					virtual.my_temp -= force * (virtual.py-(ymin))/d;
				}
				
			}
		}
	}
	
	public float getValue(int i){
		if (i==0) return xmin;
		else if (i==1) return ymin;
		else if (i==2) return force;
		else return 0;
	}
	
}
