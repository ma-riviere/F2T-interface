package modules;

import java.util.ArrayList;

import main.Image;
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
	
	public float[][][] grad;
	
	public ArrayList<Integer> contactsX;
	public ArrayList<Integer> contactsY;
	public ArrayList<Float> contactsH;
	
	public int nbContact=0;
	
	public float virtualX;
	public float virtualY;
	
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
		
		grad=new float[Image.SIZE][Image.SIZE][2];
		
		virtualX=main.x;
		virtualY=main.y;
	}
	
	
	/*public void setMap(){
		
		float height=0;
		
		for (int i2=0;i2<Image.SIZE;i2++){
			for (int j2=0;j2<Image.SIZE;j2++){
				
				float gradientX=0;
				float gradientY=0;
				
				float sum=0;
				
				// Define contact map
				height=(float)main.currentAge.image.tactile_mat[i2][699-(j2)][1]/255;
				
				for (int i=-2;i<=2;i++){
					for (int j=-2;j<=2;j++){
						if ((i2+i)>=0 && (i2+i)<700 && 699-(j2+j)>=0 && 699-(j2+j)<700 && i*i+j*j<=625){
							
							float h=(float)main.currentAge.image.tactile_mat[i2+i][699-(j2+j)][1]/255;
							
							if (height<h){
								
								float d=(float)Math.sqrt(i*i+j*j);
								
								gradientX+=(h-height) * i/d;
								gradientY+=(h-height) * j/d;
								sum++;
								
							}
						}
					}
				}
				
				if (sum>0){
					grad[i2][j2][0]=gradientX/sum;
					grad[i2][j2][1]=gradientY/sum;
				}
				else{
					grad[i2][j2][0]=0;
					grad[i2][j2][1]=0;
				}
			}
		}
		
	}
	
	public void clearMap(){
		for (int i=0;i<Image.SIZE;i++){
			for (int j=0;j<Image.SIZE;j++){
				grad[i][j][0]=0;
				grad[i][j][1]=0;
			}
		}
		
	}*/
	
	
	/*public void compute(){
		

		// Define contact map
		float imax=0;
		for (int i=-SIZE1;i<=SIZE1;i++){
			for (int j=-SIZE1;j<=SIZE1;j++){
				if (350+(int)(virtualX+i)>=0 && 350+(int)(virtualX+i)<700 && 350-(int)(virtualY-j)>=0 && 350-(int)(virtualY-j)<700 && i*i+j*j<=625){
					heighMap[i+SIZE1][j+SIZE1]= (float)main.currentAge.image.tactile_mat[350+(int)(virtualX+i)][350-(int)(virtualY-j)][1]/255;
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
		
		float dx=main.x - virtualX;
		float dy=main.y - virtualY;
		float distR=(float)Math.sqrt(dx*dx + dy*dy);
		
		if (contactsH.size()==0 && distR<5){
			virtualX=main.x;
			virtualY=main.y;
		}
		else{

			for (int i=0;i<contactsH.size();i++){
				float distP=(float)Math.sqrt(contactsX.get(i)*contactsX.get(i)+contactsY.get(i)*contactsY.get(i));
				
				float scalar= dx*((float)contactsX.get(i)) + dy*(-(float)contactsY.get(i));
				
				if (scalar>0){
					
					dx-= dx*Math.abs((float)contactsX.get(i)/distP);
					dy-= dy*Math.abs((float)contactsY.get(i)/distP);
					
					
				}
				
			}
			
			for (int i=0;i<contactsH.size();i++){
				
				if (contactsX.get(i)>0 && contactsX.get(i)<20) dx-=(25-(float)contactsX.get(i))/10;
				if (contactsX.get(i)<0 && contactsX.get(i)>-20) dx-=(-25+(float)contactsX.get(i))/10;
				
				if (contactsY.get(i)>0 && contactsY.get(i)<20) dy-=(25-(float)contactsY.get(i))/10;
				if (contactsY.get(i)<0 && contactsY.get(i)>-20) dy-=(-25+(float)contactsY.get(i))/10;
				//dx-=25-(float)contactsX.get(i);
				//dy-=25-(float)contactsY.get(i);
				//System.out.println(contactsX.get(i)+" ; "+contactsY.get(i));
				
			}
			
			virtualX+=dx;
			virtualY+=dy;
			
		}
	}*/
	
	
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
