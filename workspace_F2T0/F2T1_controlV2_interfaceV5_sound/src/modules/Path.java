package modules;

import main.Main;

public class Path {

	private Main main;
	
	public Path(Main m){
		main=m;
	}
	
	public void compute(){
		
		if (main.currentAge.targetSequence.size()>0 && !main.target_pause){

			main.currentAge.targetSequence.get(0).compute(main.px, main.py);
			if (main.currentAge.targetSequence.get(0).reached){
				main.currentAge.targetSequence.remove(0);
			}
			else{
				if (main.currentAge.targetSequence.get(0).control==1){
					main.mX=main.currentAge.targetSequence.get(0).offsetX;
					main.mY=main.currentAge.targetSequence.get(0).offsetY;
				}
				else{
					main.mX+=main.currentAge.targetSequence.get(0).offsetX;
					main.mY+=main.currentAge.targetSequence.get(0).offsetY;
				}
			}
		}
	}

}
