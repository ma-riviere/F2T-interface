package modules;

import java.util.ArrayList;

import main.Main;

public class Edges{

	public static int SIZE1=25;
	public static int SIZE2=51;
	
	public float contactHeight=0;
	public float contactX=0;
	public float contactY=0;
	public float[][] sphere;
	public float[][] heighMap;
	public float[][] contactMap;
	
	public ArrayList<Integer> contactsX;
	public ArrayList<Integer> contactsY;
	public ArrayList<Float> contactsH;
	
	private Main main;

	public Edges(Main m){
		main=m;
		
		sphere=new float[SIZE2][SIZE2];
		for (int i=-SIZE1;i<=SIZE1;i++){
			for (int j=-SIZE1;j<=SIZE1;j++){
				sphere[i+SIZE1][j+SIZE1]=(SIZE2-(float) Math.sqrt(SIZE2*SIZE2 - i*i - j*j))/6;
			}
		}
		heighMap=new float[SIZE2][SIZE2];
		contactMap=new float[SIZE2][SIZE2];
		
		contactsX=new ArrayList<Integer>();
		contactsY=new ArrayList<Integer>();
		contactsH=new ArrayList<Float>();
	}
	
	public void compute(){
		

		// Height of touched point

		float imax=0;
		
		for (int i=-SIZE1;i<=SIZE1;i++){
			for (int j=-SIZE1;j<=SIZE1;j++){
				if (350+(int)(main.px+i)>=0 && 350+(int)(main.px+i)<700 && 350-(int)(main.py-j)>=0 && 350-(int)(main.py-j)<700 && i*i+j*j<=625){
					heighMap[i+SIZE1][j+SIZE1]= (float)main.currentAge.image.tactile_mat[350+(int)(main.px+i)][350-(int)(main.py-j)][1]/255;
					contactMap[i+SIZE1][j+SIZE1]= heighMap[i+SIZE1][j+SIZE1]-sphere[i+SIZE1][j+SIZE1] -contactHeight;
					
					if (contactMap[i+SIZE1][j+SIZE1]>imax){
						imax=contactMap[i+SIZE1][j+SIZE1];
					}
				}
			}
		}
		contactHeight=heighMap[SIZE1/2][SIZE1/2];
		
		
		// detect contact points
		contactsX.clear();
		contactsY.clear();
		contactsH.clear();
		
		for (int i=-SIZE1+5;i<=SIZE1-5;i++){
			for (int j=-SIZE1+5;j<=SIZE1-5;j++){
				float val=contactMap[i+SIZE1][j+SIZE1];
				if (i!=0 || j!=0){
					
					boolean found=false;
					for (int i2=-5;i2<=5;i2++){
						for (int j2=-5;j2<=5;j2++){
							if (i2!=0 || j2!=0){
								if ( val<=contactMap[i+SIZE1+i2][j+SIZE1+j2]) found=true;
							}
						}
					}
					if (!found){
						contactsX.add(i);
						contactsY.add(j);
						contactsH.add(val);
					}	
				}
			}
		}
		
		

		
		

		// define force
		if (contactsX.size()>0){
			

			for (int i=0;i<contactsX.size();i++){
				float dist=(float)Math.sqrt(contactsX.get(i)*contactsX.get(i)+contactsY.get(i)*contactsY.get(i));

				float scalarJ= main.mX*((float)contactsX.get(i)) + main.mY*(-(float)contactsY.get(i));
				
				if (scalarJ>0){
					
					float mx2=main.mX;
					float my2=main.mY;
					
					main.mX-=mx2*Math.abs(contactsX.get(i)/dist);
					main.mY-=my2*Math.abs(contactsY.get(i)/dist);
				}
			}
			
			float forceX=0;
			float forceY=0;
			
			for (int i=0;i<contactsX.size();i++){
				float dist=(float)Math.sqrt(contactsX.get(i)*contactsX.get(i)+contactsY.get(i)*contactsY.get(i));

				float scalarJ= main.mX*((float)contactsX.get(i)) + main.mY*(-(float)contactsY.get(i));
				
				if (scalarJ>0){
					
					float mx2=main.mX;
					float my2=main.mY;

					main.mX-=mx2*Math.abs(contactsX.get(i)/dist);
					main.mY-=my2*Math.abs(contactsY.get(i)/dist);
				}
				
				// speed bounce
				/*float scalarV= (main.x-main.x_prev2)*((float)contactsX.get(i)) + (main.y-main.y_prev2)*(-(float)contactsY.get(i));
				if (scalarV>0){
					forceX-= 500 * (main.x-main.x_prev2) * contactsH.get(i);
					forceY-= 500 * (main.y-main.y_prev2) * contactsH.get(i);
				}*/
				
				// force reaction
				/*float scalarJ= main.mX*((float)contactsX.get(i)) + main.mY*(-(float)contactsY.get(i));
				if (scalarJ>0){
					forceX-=400 *(float)contactsX.get(i)/dist * contactsH.get(i);
					forceY+=400 *(float)contactsY.get(i)/dist * contactsH.get(i);
				}*/
				
				// round tip force
				float force= 500 * (1-dist/SIZE1);
				forceX-= force * (float)contactsX.get(i)/dist * contactsH.get(i);
				forceY+= force * (float)contactsY.get(i)/dist * contactsH.get(i);
			}
			
			main.mX+=(forceX)/contactsX.size();
			main.mY+=(forceY)/contactsY.size();
			
		}

	}

}
