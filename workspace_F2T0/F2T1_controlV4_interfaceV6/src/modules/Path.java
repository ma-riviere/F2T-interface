package modules;

import main.Main;
import main.Virtual;

public class Path extends Module{

	
	public Path(Main m, Virtual v){
		super(m,v);
		name="path";
	}
	
	public void compute(){
		if (main.currentAge.targetSequence.sizePoint()>0 && !main.target_pause){
			main.currentAge.targetSequence.compute(virtual.px, virtual.py);
			
			if (main.currentAge.targetSequence.sizePoint()>0 && !main.target_pause){
				if (main.currentAge.targetSequence.getControl(0)==1){
					virtual.mx_temp=main.currentAge.targetSequence.offsetX/2;
					virtual.my_temp=main.currentAge.targetSequence.offsetY/2;
					
					
				}
				else{
					virtual.mx_temp+=main.currentAge.targetSequence.offsetX/2;
					virtual.my_temp+=main.currentAge.targetSequence.offsetY/2;
					
					offsetX=main.currentAge.targetSequence.offsetX/2;
					offsetY=main.currentAge.targetSequence.offsetY/2;
				}
			}
		}
	}
	
	/*public float getValue(int i){
		return main.currentAge.targetSequence.size();
	}*/

}
