package modules;

import main.Main;

public class Flow {

	public int flowX_read=0;
	public int flowY_read=0;
	
	private Main main;
	
	public Flow(Main m){
		main=m;
	}

	public void compute(){
		if (main.currentAge.image.flow!=null){
			flowX_read=main.currentAge.image.flow_mat[Math.min(699,Math.max(0,350+(int)(main.px)))][Math.min(699,Math.max(0,350-(int)(main.py)))][0]-128;
			flowY_read=main.currentAge.image.flow_mat[Math.min(699,Math.max(0,350+(int)(main.px)))][Math.min(699,Math.max(0,350-(int)(main.py)))][1]-128;

			main.mX+=flowX_read*4;
			main.mY+=flowY_read*4;
		}
	}
	
	public float getValue(int i){
		if (i==0) return flowX_read;
		else if (i==1) return flowY_read;
		else return 0;
	}
}
