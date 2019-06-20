package structures;

import java.util.ArrayList;

import main.Main;


public class Symbol {

	public int NBSCALE=11;
	
	public String name;
	
	public ArrayList<TargetPoint> sequence;
	public ArrayList<TargetPoint> areas;

	public int maxX=20;
	public int maxY=20;
	public int minX=-20;
	public int minY=-20;
	
	private float[] scales;
	
	private Main main;
	
	public Symbol(Main m, String n){
		sequence=new ArrayList<TargetPoint>();
		areas=new ArrayList<TargetPoint>();
		
		main = m;
		name=n;
		
		scales=new float[NBSCALE];
		scales[ 0]=0.5f;
		scales[ 1]=0.6f;
		scales[ 2]=0.7f;
		scales[ 3]=0.8f;
		scales[ 4]=0.9f;
		scales[ 5]=1.0f;
		scales[ 6]=1.1f;
		scales[ 7]=1.2f;
		scales[ 8]=1.3f;
		scales[ 9]=1.4f;
		scales[10]=1.5f;
	}
	
	
	public void set(int px, int py){
		
		if (px+maxX<344 && px+minX>-345 && py+maxY<349 && py+minY>-345){
		
			int index=0;
			
			for (int i=0;i<sequence.size();i++){
				main.currentAge.targetSequence.add(index, new TargetPoint(sequence.get(i).x+px, sequence.get(i).y+py, sequence.get(i).speed, sequence.get(i).control));
				index++;
			}
		}
		else main.println("symbol "+name+" cannot be added at ("+px+", "+py+")");
	}

	public void setTarget(TargetPoint t){
		
		if (t.control==1){
			sequence.add(t);
		
			for (int i=0;i<sequence.size();i++){
				if (sequence.get(i).x+20>maxX) maxX=(int)sequence.get(i).x+20;
				if (sequence.get(i).x-20<minX) minX=(int)sequence.get(i).x-20;
				if (sequence.get(i).y+20>maxY) maxY=(int)sequence.get(i).y+20;
				if (sequence.get(i).y-20<minY) minY=(int)sequence.get(i).y-20;
			}
		}
		else{
			areas.add(t);
		}
	}
	
	public int recognize(){
		int res=0;
		
		float val=0;
		float refx=0;
		float refy=0;
		
		int max_x=0;
		int max_y=0;
		int min_x=0;
		int min_y=0;
		
		int index=0;
		int time=0;
		int time2=0;
		
		boolean out=false;
		
		int s=0;
		int area=0;
		
		float dx,dy,d2;
		
		while (s<NBSCALE && res>=0){
			if (index>=0){
				out=false;
				
				time=main.time-1;
				time2=0;
				index=sequence.size()-1;
				
				// define symbol reference according to last element
				refx=(int)(sequence.get(index).x*scales[s]);
				refy=(int)(sequence.get(index).y*scales[s]);
	
				// define if the trace move on each element of the symbol
				while (!out && index>=0 && time2<Main.LENGTH){
					if (time<0) time+=Main.LENGTH;
					
					// define if trace move out of the symbol
					if ( (main.trace[time][0]-main.px)>((float)maxX*scales[s]-refx) || (main.trace[time][0]-main.px)<((float)minX*scales[s]-refx)
					   ||(main.trace[time][1]-main.py)>((float)maxY*scales[s]-refy) || (main.trace[time][1]-main.py)<((float)minY*scales[s]-refy)){
					
						   out=true;
					}
					else{
						// distance between trace point and symbol element in symbol reference
						dx=(main.trace[time][0]-main.px)-(sequence.get(index).x*scales[s]-refx);
						dy=(main.trace[time][1]-main.py)-(sequence.get(index).y*scales[s]-refy);
						
						d2=dx*dx+dy*dy;
		
						if (d2<20*scales[s] * 20*scales[s]){
							index--;
						}
						else{
							area=0;
							while (!out && area<areas.size()){
								dx=(main.trace[time][0]-main.px)-(areas.get(index).x*scales[s]-refx);
								dy=(main.trace[time][1]-main.py)-(areas.get(index).y*scales[s]-refy);
								
								d2=dx*dx+dy*dy;
		
								if (d2<20*scales[s] * 20*scales[s]){
									out=true;
								}
								area++;
							}
						}
						time-=2;
						if (time<0) time+=Main.LENGTH;
						time2+=2;
					}
				}
			}
			
			res=index;

			s++;
		}
		return res;
	}
	
}
