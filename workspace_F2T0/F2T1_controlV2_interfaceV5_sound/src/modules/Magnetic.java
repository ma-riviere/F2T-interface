package modules;

import main.Image;
import main.Main;

public class Magnetic {

	public int xmin=-1;
	public int ymin=-1;

	private Main main;
	
	public Magnetic(Main m){
		main=m;
	}

	public void compute(){
		if (main.currentAge.image.magnetic!=null){

			int xmin2=-1;
			int ymin2=-1;
			float min=1000000;
			
			float d2=0;
			
			if (main.currentAge.image.magnetic_mat[Math.min(699,Math.max(0,(int)(main.px+350)))][Math.min(699,Math.max(0,(int)(main.py+350)))]==0){
				for (int i=0;i<Image.SIZE;i++){
					for (int j=0;j<Image.SIZE;j++){
						
						if (main.currentAge.image.magnetic_mat[i][j]>0){
							d2= ((main.px+350)-(i))*((main.px+350)-(i)) + ((main.py+350)-(Image.SIZE-j))*((main.py+350)-(Image.SIZE-j));
							
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
					float force=200*500/(d2*10);
					float d=(float) Math.sqrt(d2);
					//System.out.println(force+" ; "+d+" ; "+main.py+" , "+xmin+" ; "+main.py+" , "+ymin);
					main.mX -= force * (main.px-(xmin))/d;
					main.mY -= force * (main.py-(ymin))/d;
				}
				
			}
		}
	}
	
}
