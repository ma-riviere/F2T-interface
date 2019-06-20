package modules;

import main.Main;
import main.Virtual;

public class Path extends Module{

	
	public Path(Main m, Virtual v){
		super(m,v);
		name="path";
	}
	
	public void compute(){
		
		if (main.currentAge.targetSequence.size()>0 && !main.target_pause){

			main.currentAge.targetSequence.get(0).compute(virtual.px, virtual.py);
			if (main.currentAge.targetSequence.get(0).reached){
				main.currentAge.targetSequence.remove(0);
			}
			else{
				if (main.currentAge.targetSequence.get(0).control==1){
					virtual.mx_temp=main.currentAge.targetSequence.get(0).offsetX/2;
					virtual.my_temp=main.currentAge.targetSequence.get(0).offsetY/2;
					
					System.out.println(virtual.mx_temp+" , "+virtual.my_temp);
				}
				else{
					virtual.mx_temp+=main.currentAge.targetSequence.get(0).offsetX/2;
					virtual.my_temp+=main.currentAge.targetSequence.get(0).offsetY/2;
				}
			}
		}
	}
	
	public float getValue(int i){
		return main.currentAge.targetSequence.size();
	}

}
