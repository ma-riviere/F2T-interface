package modules;

import main.Main;

public class Rail {

	public int railX_read=0;
	public int railY_read=0;

	private Main main;
	
	public Rail(Main m){
		main=m;
	}
	
	public void compute(){
		if (main.currentAge.image.rail!=null){
			if ( main.currentAge.targetSequence.size()==0 || main.currentAge.targetSequence.get(0).control==0 || main.target_pause ){
				
				railY_read=-(main.currentAge.image.rail_mat[Math.min(699,Math.max(0,350+(int)(main.px)))][Math.min(699,Math.max(0,350-(int)(main.py)))][0]-128)*2;
				railX_read=-(main.currentAge.image.rail_mat[Math.min(699,Math.max(0,350+(int)(main.px)))][Math.min(699,Math.max(0,350-(int)(main.py)))][1]-128)*2;

				float scalar=main.mX*railX_read + main.mY*(-railY_read);
				float norm2=railX_read*railX_read+railY_read*railY_read;
				
				if (norm2>0){
					
					float offX=  scalar * railX_read/norm2;
					float offY= -scalar * railY_read/norm2;
					float norm=(float)Math.sqrt(norm2)/10;
					if (norm>1) norm=1;

					main.mX-=offX* norm;
					main.mY-=offY* norm;
				}
			}
		}
	}
	
	public float getValue(int i){
		if (i==0) return railX_read;
		else if (i==1) return railY_read;
		else return 0;
	}

}
