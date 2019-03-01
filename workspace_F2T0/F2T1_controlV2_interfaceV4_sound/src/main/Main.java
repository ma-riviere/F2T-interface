package main;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;


import script.Age;
import script.Script;
import display.*;
import org.urish.openal.OpenAL;

public class Main {

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static String OS = System.getProperty("os.name").toLowerCase();
	public static String PORT="/dev/ttyUSB";					// name of the port, without number

	public static String FILES="./";							// path to images
	public static String IMG="img/";							// sub paths to image types
	public static String TACTILE="tactile/";
	public static String FLOW="flow/";
	public static String RAIL="rail/";
	public static String PATH="path/";
	public static String PRESET="preset/";
	public static String AREA="area/";
	public static String SCRIPT="script/";
	public static String SOUND="sound/";
	public static String SOURCE="source/";
	public static String AGE="age/";
	public static String ASSOCIATION="association/";
	
	public static int button1=96;								// action button key code
	public static int button2=96;
	public static int button3=96;
	public static boolean displayKey=false;
	
	public static int LENGTH=500;								// length of the trace
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// console messages
	public String console="";
	
	
	// serial interface
	public Interface inter;
	
	
	// main display frames
	//public TouchFrame touchFrame;
	public DisplayFrame display;
	
	
	// pointer to current age
	public Age currentAge;
	
	// script defining ages
	public Script script;
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	

	// position of cursor, previous and predicted
	public float posx=0;
	public float posy=0;
	
	public float x=0;
	public float y=0;
	
	public float x_prev=0;
	public float y_prev=0;

	public float x_prev1=0;
	public float y_prev1=0;
	
	public float x_prev2=0;
	public float y_prev2=0;
	
	public float x_next=0;
	public float y_next=0;
	
	
	// joystick input values
	
	public float dx=0;
	public float dy=0;
	
	// tactile property values
	public float friction_fluid_read=0;
	public float friction_solid_read=0;
	
	
	public float gx=0;
	public float gy=0;
	public int previous_gx=0;
	public int previous_gy=0;
	
	public int flowX_read=0;
	public int flowY_read=0;
	
	public int railX_read=0;
	public int railY_read=0;
	
	private int flowX=0;
	private int flowY=0;
	
	public int area_red_read=0;
	public int area_green_read=0;
	public int area_blue_read=0;
	
	public boolean control_mode=false;	// direct or indirect control loop
	
	// trace buffer
	public float[][] trace;
	public int time;
	public int count;
	
	public float[] gauss;
	
	public float contactHeight=0;
	public float contactX=0;
	public float contactY=0;
	public float[][] sphere;
	public float[][] heighMap;
	public float[][] contactMap;
	
	public ArrayList<Integer> contactsX;
	public ArrayList<Integer> contactsY;
	public ArrayList<Float> contactsH;

	
	// target properties
	public boolean target_pause=true;
	public int target_speed=200;
	public int target_type=1;
	
	
	
	///////////////////////////////////////////////////////////////
	
	// name for save files
	public String presetName="preset";
	public String pathName="path";
	public String sourceName="source";
	public String associationName="association";
	
	public String[] listImages;
	public String[] listTactile;
	public String[] listFlow;
	public String[] listRail;
	public String[] listPath;
	public String[] listPreset;
	public String[] listArea;
	public String[] listSPath;
	public String[] listScript;
	public String[] listSource;
	public String[] listAssociation;
	
	public String[] listSound;
	public String[] listAge;
	
	public int selected_img=-1;
	public int selected_tactile=-1;
	public int selected_flow=-1;
	public int selected_rail=-1;
	public int selected_area=-1;
	
	
	
	private int counter=0;
	private long time1=0;
	private long time2=0;
	
	private long time3=0;
	private long time4=0;
	
	private int[] sequence;
	private int index;
	
	public boolean processing=false;
	
	public ArrayList<Integer> jx;
	public ArrayList<Integer> jy;
	public MainFrame mainFrame;
	
	public static OpenAL openal;  
	
	///////////////////////////////////////////////////////////////
	public Main(){
		
		try{ 
            openal = new OpenAL();
        }catch (Exception e){System.out.println(e);}
		
		time=0;
		count=0;
		trace=new float[LENGTH][2];
		
		jx=new ArrayList<Integer>();
		jy=new ArrayList<Integer>();
		mainFrame=new MainFrame(this);
		
		
		///////////////////////////////////////////////////////////////////////////////////////////////

		// F2T interface
		inter=new Interface(this);

		// sequence of Ages
		script=new Script(this);
		currentAge=script.ageList.get(script.currentAge);
		
		// detection of available files
		listFiles();

		///////////////////////////////////////////////////////////////////////////////////////////////
		
		gauss=new float[9];
		gauss[0]=0.018f;//-4
		gauss[1]=0.1f;	//-3
		gauss[2]=0.37f;	//-2
		gauss[3]=0.78f;	//-1
		gauss[4]=1; 	// 0
		gauss[5]=0.78f;	// 1
		gauss[6]=0.37f;	// 2
		gauss[7]=0.1f;	// 3
		gauss[8]=0.018f;// 4
		
		
		sphere=new float[51][51];
		for (int i=-25;i<=25;i++){
			for (int j=-25;j<=25;j++){
				sphere[i+25][j+25]=(51-(float) Math.sqrt(51*51 - i*i - j*j))/6;
			}
		}
		heighMap=new float[51][51];
		contactMap=new float[51][51];
		
		contactsX=new ArrayList<Integer>();
		contactsY=new ArrayList<Integer>();
		contactsH=new ArrayList<Float>();
		
		//////////////////////////////////////////////////////////////////////////////////////////
		// initialization of main display panels
		//touchFrame=new TouchFrame(this);
		display=new DisplayFrame(this);
		
		
		sequence=new int[500];
		index=0;
		
		//////////////////////////////////////////////////////////////////////////////////////////
		/*while (true){
			
			//time3=System.nanoTime();
			
			// pointer to current age
			if (script.currentAge>=0 && script.currentAge<script.ageList.size()) currentAge=script.ageList.get(script.currentAge);
			

			///////////////////////////////////////////////////////////
			// get user speed dx and dy
			//boolean received =getUserMovement();
			
			//if (inter.ready){
				


		
			float deltaX=dx;
			float deltaY=dy;
			
			sequence[index]=(int)x;
			index++;
			if (index>=500){
				for (int i=0;i<500;i++){
					System.out.print(sequence[i]+" , ");
				}
				System.out.println();
				index=0;
			}
			System.out.println("---- "+x);

			///////////////////////////////////////////////////////////
			// get friction values (fluid and solid) and send value
			friction_fluid_read=(float)currentAge.image.tactile_mat[Math.min(699,Math.max(0,350+(int)(x+dx)))][Math.min(699,Math.max(0,350-(int)(y+dy)))][0]/255;
			friction_solid_read=(float)currentAge.image.tactile_mat[Math.min(699,Math.max(0,350+(int)(x+dx)))][Math.min(699,Math.max(0,350-(int)(y+dy)))][2]/255;
			
			
			
			deltaX= deltaX * (1-friction_fluid_read*0.8f);
			deltaY= deltaY * (1-friction_fluid_read*0.8f);
			
			//System.out.println(friction_fluid_read+" , "+deltaX+" , "+deltaY);
			
			///////////////////////////////////////////////////////////
			// define edges
			contactX=0;
			contactY=0;
			boolean wall=false;

			// Height of touched point
			contactHeight=(float)currentAge.image.tactile_mat[350+(int)(x)][350-(int)(y)][1]/255;

			for (int i=-12;i<=12;i++){
				for (int j=-12;j<=12;j++){
					if (350+(int)(x+i)>=0 && 350+(int)(x+i)<700 && 350-(int)(y-j)>=0 && 350-(int)(y-j)<700 && i*i+j*j<=144){
						heighMap[i+12][j+12]= (float)currentAge.image.tactile_mat[350+(int)(x+i)][350-(int)(y-j)][1]/255;
						contactMap[i+12][j+12]= heighMap[i+12][j+12]-sphere[i+12][j+12] -contactHeight;
						if (contactMap[i+12][j+12]>0) wall=true;
					}
				}
			}
			/*float jx2=(jx/1.6f);
			float jy2=(jy/1.6f);
			
			// detect maximums to add repulsion effects
			float rep_X=0;
			float rep_Y=0;
			for (int i=-11;i<=11;i++){
				for (int j=-11;j<=11;j++){
					float val=contactMap[i+12][j+12];
					
					if ((i!=0 || j!=0) 
					    && val>contactMap[i+12][j+11] && val>contactMap[i+12][j+13] 
					    && val>contactMap[i+11][j+12] && val>contactMap[i+13][j+12]
					    && val>=contactMap[i+11][j+11] && val>contactMap[i+13][j+13]
					    && val>=contactMap[i+11][j+13] && val>contactMap[i+13][j+11]){

						float g_norm2=i*i+j*j;
						float g_norm =(float) Math.sqrt(g_norm2);
					
						if (g_norm<10){
							rep_X+=i / g_norm * val;
							rep_Y+=j / g_norm * val;
						}
					}
				}
			}

			boolean jump=false;
			
			// detect maximums to remove component
			for (int i=-11;i<=11;i++){
				for (int j=-11;j<=11;j++){
					float val=contactMap[i+12][j+12];
					
					if ((i!=0 || j!=0) 
					    && val>contactMap[i+12][j+11] && val>contactMap[i+12][j+13] 
					    && val>contactMap[i+11][j+12] && val>contactMap[i+13][j+12]
					    && val>contactMap[i+11][j+11] && val>contactMap[i+13][j+13]
					    && val>contactMap[i+11][j+13] && val>contactMap[i+13][j+11]){
						
						float g_norm2=i*i+j*j;
						
						// remove vector component
						float scalar=jx2*i + jy2*(-j);
						
						if (scalar>0){
							
							float valX=scalar *  i  / g_norm2;
							float valY=scalar *(-j) / g_norm2;
							
							float j_norm2= valX*valX + valY*valY;
							float j_norm=(float) Math.sqrt(j_norm2);

							float delta=Math.max(0, j_norm-310*heighMap[i+12][j+12]);

							if (delta>0){
								jump=true;
								jx2-=valX/2;
								jy2-=valY/2;
							}
							else{
								jx2-=valX;
								jy2-=valY;
							}
						}
					}
				}
			}
			
			if (!jump){
				jx2-=rep_X*100;
				jy2+=rep_Y*100;
			}
			
			
			if (wall){
				if (!control_mode){
					inter.sendMsg("m1");
					control_mode=true;
				}
				
				String msg="c";
				if ((int)jx2+500<10) msg+="00";
				else if ((int)jx2+500<100) msg+="0";
				msg+=(int)jx2+500;
				
				if ((int)jy2+500<10) msg+="00";
				else if ((int)jy2+500<100) msg+="0";
				msg+=(int)jy2+500;
				inter.sendMsg(msg);
			}
			else{
				if (control_mode){
					inter.sendMsg("m0");
					control_mode=false;
				}
			}
			previous_gx=(int) gx;
			previous_gy=(int) gy;


			
			if ((int)gx!=previous_gx || (int)gy!=previous_gy){
				String msg="g";
				if (gx<10) msg+="00";
				else if (gx<100) msg+="0";
				msg+=(int)gx;
				
				if (gy<10) msg+="00";
				else if (gy<100) msg+="0";
				msg+=(int)gy;
				
				inter.sendMsg(msg);
				previous_gx=(int)gx;
			}
			
			
			///////////////////////////////////////////////////////////
			// define flow
			flowX=500;
			flowY=500;
			if ( currentAge.targetSequence.size()==0 || currentAge.targetSequence.get(0).control==0 || target_pause ){
				flowX_read=currentAge.image.flow_mat[Math.min(699,Math.max(0,350+(int)(x+dx)))][Math.min(699,Math.max(0,350-(int)(y+dy)))][0];
				flowY_read=currentAge.image.flow_mat[Math.min(699,Math.max(0,350+(int)(x+dx)))][Math.min(699,Math.max(0,350-(int)(y+dy)))][1];

				flowX=flowX_read*4-12;
				flowY=flowY_read*4-12;
				
				if (flowX<0) flowX=0;
				if (flowX>999) flowX=999;
				if (flowY<0) flowY=0;
				if (flowY>999) flowY=999;
			}
			
			
			///////////////////////////////////////////////////////////
			// define rail
			if ( currentAge.targetSequence.size()==0 || currentAge.targetSequence.get(0).control==0 || target_pause ){
				
				railX_read=currentAge.image.rail_mat[Math.min(699,Math.max(0,350+(int)(x+dx)))][Math.min(699,Math.max(0,350-(int)(y+dy)))][0];
				railY_read=currentAge.image.rail_mat[Math.min(699,Math.max(0,350+(int)(x+dx)))][Math.min(699,Math.max(0,350-(int)(y+dy)))][1];
				float railX2=-(railY_read*4-12-500);
				float railY2=-(railX_read*4-12-500);
				float scalar=jx2*railX2 + jy2*(-railY2);
				float norm2=railX2*railX2+railY2*railY2;
				
				if (norm2>0){
					
					float offX=  scalar * railX2/norm2;
					float offY= -scalar * railY2/norm2;
					float norm=(float)Math.sqrt(norm2)/500;
					if (norm>1) norm=1;
					flowX-=offX* norm *1.6f;
					flowY-=offY* norm *1.6f;
					
					if (flowX<0) flowX=0;
					if (flowX>999) flowX=999;
					if (flowY<0) flowY=0;
					if (flowY>999) flowY=999;
				}
			}
			
			
			///////////////////////////////////////////////////////////
			// if there is a target
			if (currentAge.targetSequence.size()>0 && !target_pause){
				if (currentAge.targetSequence.get(0).control==1) inter.sendMsg("t0");
				else                          inter.sendMsg("t1");
				currentAge.targetSequence.get(0).compute(x, y);
				if (currentAge.targetSequence.get(0).reached){
					currentAge.targetSequence.remove(0);
					if (currentAge.targetSequence.size()==0) inter.sendMsg("t1");
				}
				else{
					flowX+=currentAge.targetSequence.get(0).offsetX;
					flowY+=currentAge.targetSequence.get(0).offsetY;
				}
			}
			
			///////////////////////////////////////////////////////////
			// send flow value
			if (flowX!=previous_flowX){
				String msg="o";
				if (flowX<10) msg+="00";
				else if (flowX<100) msg+="0";
				msg+=flowX;
				inter.sendMsg(msg);
				previous_flowX=flowX;
			}
			if (flowY!=previous_flowY){
				String msg="p";
				if (flowY<10) msg+="00";
				else if (flowY<100) msg+="0";
				msg+=flowY;
				inter.sendMsg(msg);
				previous_flowY=flowY;
			}

			
			
			/////////////////////////////////////////////////////////////////////////////
			// detect areas
			area_red_read=currentAge.image.area_mat[Math.min(699,Math.max(0,350+(int)(x+dx)))][Math.min(699,Math.max(0,350-(int)(y+dy)))][0];
			area_green_read=currentAge.image.area_mat[Math.min(699,Math.max(0,350+(int)(x+dx)))][Math.min(699,Math.max(0,350-(int)(y+dy)))][1];
			area_blue_read=currentAge.image.area_mat[Math.min(699,Math.max(0,350+(int)(x+dx)))][Math.min(699,Math.max(0,350-(int)(y+dy)))][2];
			
			*/
			
			
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// send motor commands
			/*int speedX=(int) Math.abs(deltaX*60);
			if (speedX>255) speedX=254;
			else if (speedX<5) speedX=0;
			else speedX=speedX-1;
			
			int speedY=(int) Math.abs(deltaY*60);
			if (speedY>255) speedY=254;
			else if (speedY<5) speedY=0;
			else speedY=speedY-1;
			
			int directions=0;
			if (dx>=0) directions+=1;
			if (dy>=0) directions+=2;
			
			//directions=3;
			
			byte[] msg=new byte[4];
			msg[0]=(byte) 255;
			msg[1]=(byte)directions;
			msg[2]=(byte)speedX;
			msg[3]=(byte)speedY;
			
			
			inter.sendMsg(msg);
			
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// update display panels
			//touchFrame.repaint();
			display.repaint();
			
			//////////////////////////////
			// update user trace
			updateTrace();
			
			
			//////////////////////////////
			// update script			
			script.play(Math.min(699,Math.max(0,350+(int)(x+dx))),Math.min(699,Math.max(0,350-(int)(y+dy))));
			

			
			
			//try {Thread.sleep(0,5000);
			//} catch (InterruptedException e) {e.printStackTrace();}
			
			try {sleepNanos(500000);
			} catch (InterruptedException e) {e.printStackTrace();}
			
			//if (received) System.out.println("+++ "+(System.nanoTime()-time3));
			//else System.out.println("--- "+(System.nanoTime()-time3));
			
		}*/
	}
	
	
	public void controlLoop(){
		
		processing=true;
		
		counter++;
		if (counter>=1000){
		System.out.println("+++ "+((System.nanoTime()-time3))/1000);
		time3=System.nanoTime();
		counter=0;
		}
		
		//System.out.println((x-x_prev)+" , "+(y-y_prev));
		
		// pointer to current age
		if (script.currentAge>=0 && script.currentAge<script.ageList.size()) currentAge=script.ageList.get(script.currentAge);
		

		x_next=x + 25*(x-x_prev2);
		y_next=y + 25*(y-y_prev2);
		
		float x2=x+dx;
		float y2=y+dy;

		if (x2>349) x2=349;
		if (x2<-349) x2=-349;
		if (y2>349) y2=349;
		if (y2<-349) y2=-349;

	
		float deltaX=dx;
		float deltaY=dy;
		
		//System.out.println(deltaX+" , "+deltaY );
		if (deltaX<1 && deltaX>-1) deltaX=0;
		if (deltaY<1 && deltaY>-1) deltaY=0;
		
		deltaX=deltaX/1.5f;
		if (deltaX>0) deltaX+=1;
		else if (deltaX<0) deltaX-=1;
		else deltaX=0;
		
		
		deltaY=deltaY/1.5f;
		if (deltaY>0) deltaY+=1;
		else if (deltaY<0) deltaY-=1;
		else deltaY=0;
		
		
		float sX=deltaX*55;
		float sY=deltaY*55;
		
		

		///////////////////////////////////////////////////////////
		// get friction values (fluid and solid) and send value
		
		friction_fluid_read=(float)currentAge.image.tactile_mat[Math.min(699,Math.max(0,350+(int)(x2)))][Math.min(699,Math.max(0,350-(int)(y2)))][0]/255;
		friction_solid_read=(float)currentAge.image.tactile_mat[Math.min(699,Math.max(0,350+(int)(x2)))][Math.min(699,Math.max(0,350-(int)(y2)))][2]/255;
		
		
		if (friction_fluid_read>0){
			sX= sX * (1-friction_fluid_read*0.7f);
			sY= sY * (1-friction_fluid_read*0.7f);
		}
		
		if (friction_solid_read>0){
			float force_norm= (float)Math.sqrt(sX*sX+sY*sY)/255f;

			if (force_norm>0){
				if (force_norm<friction_solid_read*0.7f){
					sX=0;
					sY=0;
				}
				else{
					sX= sX -friction_solid_read*0.7f * sX / force_norm;
					sY= sY -friction_solid_read*0.7f * sY / force_norm;
				}
			}
		}
		
		///////////////////////////////////////////////////////////
		// define edges

		// Height of touched point
		//contactHeight=(float)currentAge.image.tactile_mat[350+(int)(x2)][350-(int)(y2)][1]/255;

		float imax=0;
		
		for (int i=-25;i<=25;i++){
			for (int j=-25;j<=25;j++){
				if (350+(int)(x2+i)>=0 && 350+(int)(x2+i)<700 && 350-(int)(y2-j)>=0 && 350-(int)(y2-j)<700 && i*i+j*j<=625){
					heighMap[i+25][j+25]= (float)currentAge.image.tactile_mat[350+(int)(x2+i)][350-(int)(y2-j)][1]/255;
					contactMap[i+25][j+25]= heighMap[i+25][j+25]-sphere[i+25][j+25];// -contactHeight;
					
					if (contactMap[i+25][j+25]>imax){
						imax=contactMap[i+25][j+25];
					}
				}
			}
		}
		contactHeight=imax;
		
		
		// detect contact points
		contactsX.clear();
		contactsY.clear();
		contactsH.clear();
		
		
		
		for (int i=-24;i<=24;i++){
			for (int j=-24;j<=24;j++){
				float val=contactMap[i+25][j+25];
				if ((i!=0 || j!=0) 
					    && val>contactMap[i+25][j+24] && val>contactMap[i+25][j+26] 
					    && val>contactMap[i+24][j+25] && val>contactMap[i+26][j+25]
					    && val>=contactMap[i+24][j+24] && val>contactMap[i+26][j+26]
					    && val>=contactMap[i+24][j+26] && val>contactMap[i+26][j+24]){
					
					contactsX.add(i);
					contactsY.add(j);
					contactsH.add(val);
				}
			}
		}

		// define force
		if (contactsX.size()>0){
			float forceX=0;
			float forceY=0;
			
			
			for (int i=0;i<contactsX.size();i++){
				float contact_norm=(float)Math.sqrt(contactsX.get(i)*contactsX.get(i)+contactsY.get(i)*contactsY.get(i));
				float force_norm= (float)Math.sqrt(sX*sX+sY*sY)/255f;
				
				//float scalar= sX*((float)contactsX.get(i)/25) + sY*(-(float)contactsY.get(i));
				
				//if (scalar>0){
					forceX+=120* contactsH.get(i) *(float)contactsX.get(i)/contact_norm;
					forceY-=120* contactsH.get(i) *(float)contactsY.get(i)/contact_norm;
				//}
				
				//forceX+=(float)contactsX.get(i)/contact_norm*255;
				//forceY-=(float)contactsY.get(i)/contact_norm*255;
				
				//System.out.println(contact_norm2+" , "+force_norm);
				
				//float scalar= sX*((float)contactsX.get(i)/25) + sY*((float)contactsY.get(i)/25);
				
				//if (scalar>0){
				
				//forceX+= scalar * (float)contactsX.get(i)/25/contact_norm2 *20;
				//forceY+= scalar * (float)contactsY.get(i)/25/contact_norm2 *20;
				//}
			}
			//System.out.println(forceX+" , "+forceY+" ; "+sX+" , "+sY);
			
			sX-=forceX/contactsX.size();
			sY-=forceY/contactsX.size();
		}
		
		///////////////////////////////////////////////////////////
		// define flow
		if (currentAge.image.flow!=null){
			if ( currentAge.targetSequence.size()==0 || currentAge.targetSequence.get(0).control==0 || target_pause ){
				flowX_read=currentAge.image.flow_mat[Math.min(699,Math.max(0,350+(int)(x2)))][Math.min(699,Math.max(0,350-(int)(y2)))][0]-128;
				flowY_read=currentAge.image.flow_mat[Math.min(699,Math.max(0,350+(int)(x2)))][Math.min(699,Math.max(0,350-(int)(y2)))][1]-128;
	
				sX+=flowX_read*4;
				sY+=flowY_read*4;
			}
		}
		
		///////////////////////////////////////////////////////////
		// define rail
		if (currentAge.image.rail!=null){
			if ( currentAge.targetSequence.size()==0 || currentAge.targetSequence.get(0).control==0 || target_pause ){
				
				railY_read=-(currentAge.image.rail_mat[Math.min(699,Math.max(0,350+(int)(x2)))][Math.min(699,Math.max(0,350-(int)(y2)))][0]-128)*2;
				railX_read=-(currentAge.image.rail_mat[Math.min(699,Math.max(0,350+(int)(x2)))][Math.min(699,Math.max(0,350-(int)(y2)))][1]-128)*2;

				float scalar=sX*railX_read + sY*(-railY_read);
				float norm2=railX_read*railX_read+railY_read*railY_read;
				
				if (norm2>0){
					
					float offX=  scalar * railX_read/norm2;
					float offY= -scalar * railY_read/norm2;
					float norm=(float)Math.sqrt(norm2)/10;
					if (norm>1) norm=1;
					
					System.out.println(offX+" , "+offY+" ; "+norm);
					
					sX-=offX* norm;
					sY-=offY* norm;
				}
			}
		}
		
		
		///////////////////////////////////////////////////////////
		// if there is a target
		if (currentAge.targetSequence.size()>0 && !target_pause){

			currentAge.targetSequence.get(0).compute(x_next, y_next);
			if (currentAge.targetSequence.get(0).reached){
				currentAge.targetSequence.remove(0);
				
				if (currentAge.targetSequence.size()==0){
					// brake
					sX=-x_prev2*2;
					sY=-y_prev2*2;
				}
			}
			else{
				flowX=currentAge.targetSequence.get(0).offsetX;
				flowY=currentAge.targetSequence.get(0).offsetY;
				
				//System.out.println(x+" , "+y+" ; "+flowX+" , "+flowY);
				
				sX=flowX;
				sY=flowY;
			}
		}
		
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// send motor commands
		int speedX=(int) Math.abs(sX);
		if (speedX>255) speedX=254;
		else if (speedX<5) speedX=0;
		else speedX=speedX-1;
		
		int speedY=(int) Math.abs(sY);
		if (speedY>255) speedY=254;
		else if (speedY<5) speedY=0;
		else speedY=speedY-1;
		
		//System.out.println(speedX+","+(dx>=0)+" ; "+speedY+","+(dy>=0));
		
		int directions=0;
		if (sX>=0) directions+=1;
		if (sY>=0) directions+=2;
		
		//directions=3;
		
		//speedX=0;
		//speedY=0;
		
		byte[] msg=new byte[4];
		msg[0]=(byte) 255;
		msg[1]=(byte)directions;
		msg[2]=(byte)speedX;
		msg[3]=(byte)speedY;
		
		
		inter.sendMsg(msg);
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// update display panels
		//touchFrame.repaint();
		display.repaint();
		
		//////////////////////////////
		// update user trace
		updateTrace();
		
		
		//////////////////////////////
		// update script			
		script.play(Math.min(699,Math.max(0,350+(int)(x2))),Math.min(699,Math.max(0,350-(int)(y2))));
		

		
		
		//try {Thread.sleep(0,5000);
		//} catch (InterruptedException e) {e.printStackTrace();}
		
		//try {sleepNanos(500000);
		//} catch (InterruptedException e) {e.printStackTrace();}
		
		//if (received) System.out.println("+++ "+(System.nanoTime()-time3));
		//else System.out.println("--- "+(System.nanoTime()-time3));
		
		processing=false;
		
		x_prev2=x_prev1;
		y_prev2=y_prev1;
		
		x_prev1=x_prev;
		y_prev1=y_prev;
		
		x_prev=x;
		y_prev=y;
		
		mainFrame.repaint();
	}
	
	
	public static void sleepNanos (long nanoDuration) throws InterruptedException {
		long time1=System.nanoTime();
		long time2=System.nanoTime();
		while (time2-time1<nanoDuration){
			time2=System.nanoTime();
		}
    }
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// file lister
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	public void listFiles(){
		
		for (int a=0;a<script.ageList.size();a++) script.ageList.get(a).close();
		script.ageList.clear();
		script.ageList.add(new Age(this, "Primary Age"));

		File repertoire = new File(FILES+IMG);
		if (repertoire.exists()){
			listImages=repertoire.list();
		}
		else System.out.println("Image file directory does not exist");

		repertoire = new File(FILES+TACTILE);
		if (repertoire.exists()){
			listTactile=repertoire.list();
		}
		else System.out.println("Tactile file directory does not exist");

		repertoire = new File(FILES+FLOW);
		if (repertoire.exists()){
			listFlow=repertoire.list();
		}
		else System.out.println("Flow file directory does not exist");

		repertoire = new File(FILES+RAIL);
		if (repertoire.exists()){
			listRail=repertoire.list();
		}
		else System.out.println("Rail file directory does not exist");

		repertoire = new File(FILES+AREA);
		if (repertoire.exists()){
			listArea=repertoire.list();
		}
		else System.out.println("Area file directory does not exist");
		
		
		repertoire = new File(FILES+PRESET);
		if (repertoire.exists()){
			listPreset=repertoire.list();
		}
		else System.out.println("Preset file directory does not exist");

		repertoire = new File(FILES+PATH);
		if (repertoire.exists()){
			listPath=repertoire.list();
		}
		else System.out.println("Path file directory does not exist");

		repertoire = new File(FILES+SOURCE);
		if (repertoire.exists()){
			listSource=repertoire.list();
		}
		else System.out.println("Source file directory does not exist");
		
		repertoire = new File(FILES+ASSOCIATION);
		if (repertoire.exists()){
			listAssociation=repertoire.list();
		}
		else System.out.println("Association file directory does not exist");
		
		repertoire = new File(FILES+SCRIPT);
		if (repertoire.exists()){
			listScript=repertoire.list();
		}
		else System.out.println("Script file directory does not exist");

		
		
		repertoire = new File(FILES+SOUND);
		if (repertoire.exists()){
			listSound=repertoire.list();
		}
		else System.out.println("Sound file directory does not exist");
		
		repertoire = new File(FILES+AGE);
		if (repertoire.exists()){
			listAge=repertoire.list();
		}
		else System.out.println("Age file directory does not exist");
		

		// get a name to save a preset
		boolean found=false;
		int i=0;
		presetName="preset0";
		while (!found){
			presetName="preset"+i;
			found=true;
			int n=0;
			while (found && n<listPreset.length){
				if (presetName.equals(listPreset[n])) found=false;
				n++;
			}
			i++;
		}

		// get a name to save a path
		found=false;
		i=0;
		pathName="path0";
		while (!found){
			pathName="path"+i;
			found=true;
			int n=0;
			while (found && n<listPath.length){
				if (pathName.equals(listPath[n])) found=false;
				n++;
			}
			i++;
		}

		// get a name to save a sound source list
		found=false;
		i=0;
		sourceName="source0";
		while (!found){
			sourceName="source"+i;
			found=true;
			int n=0;
			while (found && n<listSource.length){
				if (sourceName.equals(listSource[n])) found=false;
				n++;
			}
			i++;
		}

		// get a name to save an association list
		found=false;
		i=0;
		associationName="association0";
		while (!found){
			associationName="association"+i;
			found=true;
			int n=0;
			while (found && n<listAssociation.length){
				if (associationName.equals(listAssociation[n])) found=false;
				n++;
			}
			i++;
		}
		
		if (display!=null) display.rescan();
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// save elements of current Age : preset, path and source list
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	public void savePreset(){
		
		if (currentAge.image.view==null
		 && currentAge.image.tactile==null 
		 && currentAge.image.flow==null 
		 && currentAge.image.rail==null
		 && currentAge.image.area==null){
			System.out.println("No element selected, pre-set not saved");
		}
		else{
			String fileName = FILES+PRESET+presetName;
			try {
				PrintWriter file  = new PrintWriter(new FileWriter(fileName));
				if (currentAge.image.view!=null)    file.println("image "+currentAge.image.view);
				if (currentAge.image.tactile!=null) file.println("tactile "+currentAge.image.tactile);
				if (currentAge.image.flow!=null)    file.println("flow "+currentAge.image.flow);
				if (currentAge.image.rail!=null)    file.println("rail "+currentAge.image.rail);
				if (currentAge.image.area!=null)    file.println("area "+currentAge.image.area);
				file.close();
				System.out.println("preset saved : "+presetName);
			}
			catch (Exception e) {e.printStackTrace();}
			listFiles();
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////
	public void savePath(){
		if (currentAge.targetSequence.size()==0) System.out.println("No path to save");
		else{
			String fileName = FILES+PATH+pathName;
			try {
				PrintWriter file  = new PrintWriter(new FileWriter(fileName));
				for (int i=0;i<currentAge.targetSequence.size();i++){
					file.println("t "+currentAge.targetSequence.get(i).x+" "+currentAge.targetSequence.get(i).y+" "+currentAge.targetSequence.get(i).speed+" "+currentAge.targetSequence.get(i).control);
				}
				file.close();
				System.out.println("path saved : "+pathName);
			}
			catch (Exception e) {e.printStackTrace();}
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////
	public void saveSource(){
		if (currentAge.sourceList.size()==0) System.out.println("No sound source to save");
		else{
			String fileName = FILES+SOURCE+sourceName;
			try {
				PrintWriter file  = new PrintWriter(new FileWriter(fileName));
				for (int i=0;i<currentAge.sourceList.size();i++){
					file.println("s "+currentAge.sourceList.get(i).px+" "+currentAge.sourceList.get(i).py);
				}
				file.close();
				System.out.println("source configuration saved : "+sourceName);
			}
			catch (Exception e) {e.printStackTrace();}
		}
	}

	//////////////////////////////////////////////////////////////////////////////
	public void saveAssociation(){
		if (currentAge.areas.areas.size()==0) System.out.println("No sound association to save");
		else{
			String fileName = FILES+ASSOCIATION+associationName;
			try {
				PrintWriter file  = new PrintWriter(new FileWriter(fileName));
				for (int i=0;i<currentAge.areas.areas.size();i++){
					file.println("a "+currentAge.areas.areas.get(i)+" "+currentAge.areas.types.get(i)+" "+currentAge.areas.repeat.get(i)+" "+currentAge.areas.soundFiles.get(i));
				}
				file.close();
				System.out.println("associationList saved : "+associationName);
			}
			catch (Exception e) {e.printStackTrace();}
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Set image files
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	// picture
	public void setPicture(String img_file){
		script.ageList.get(script.currentAge).setPicture(img_file);
	}
	
	// tactile image
	public void setTactile(String tactile_file){
		script.ageList.get(script.currentAge).setTactile(tactile_file);
	}
	
	// flow file
	public void setFlow(String flow_file){
		script.ageList.get(script.currentAge).setFlow(flow_file);
	}
	
	// rail file
	public void setRail(String rail_file){
		script.ageList.get(script.currentAge).setRail(rail_file);
	}
	
	// area descriptor file
	public void setArea(String area_file){
		script.ageList.get(script.currentAge).setArea(area_file);
	}
	
	
	
	public void clearPath(){
		script.ageList.get(script.currentAge).targetSequence.clear();
		//inter.sendMsg("t1");
	}

	
	///////////////////////////////////////////////////////////


	
	
	public void updateTrace(){
		trace[time][0]=x;
		trace[time][1]=y;
		time++;
		//System.out.println(time);
		if (time>=LENGTH) time=0;
	}
	
	
	
	public void println(String line){
		console+=line+"\n";
		display.updateConsole(console);
		System.out.println(line);
	}
	
	
	public void close(){
		openal.close();
		inter.close();
		script.close();
	}
	
	
	////////////////////////////////////////
	public static void main(String[] args){

		// detect OS
		if (OS.indexOf("win") >= 0){
			PORT="COM";					// name of the port, without number

			FILES=".\\";							// path to images
			IMG="img\\";							// sub paths to image types
			TACTILE="tactile\\";
			FLOW="flow\\";
			RAIL="rail\\";
			PATH="path\\";
			PRESET="preset\\";
			AREA="area\\";
			SCRIPT="script\\";
			SOUND="sound\\";
			SOURCE="source\\";
			AGE="age\\";
			ASSOCIATION="association\\";
		}
		
		
		// load parameters from init file
		String fileName = Main.FILES+"param.ini";
		String[] elements;

		try {
			InputStream ips=new FileInputStream(fileName); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line=null;
			line=br.readLine();
			
			while (line!=null){
				elements=line.split(" ");
				if (elements.length>=2 && elements[0].equals("actionkeys")){
					button1=Integer.parseInt(elements[1]);
					if (elements.length>=3) button2=Integer.parseInt(elements[2]);
					if (elements.length>=4) button3=Integer.parseInt(elements[3]);
				}
				else if (elements.length>=2 && elements[0].equals("displaykey")) displayKey=(!elements[1].equals("0"));
				else if (elements.length>=2 && elements[0].equals("trace")) LENGTH=Integer.parseInt(elements[1]);
				else if (elements.length>=2 && elements[0].equals("path")) FILES=elements[1];
				line=br.readLine();
			}
			br.close();
			System.out.println(LENGTH);
		}
		catch (Exception e) {System.out.println("init file not found");}

		/////////////////
		new Main();
		/////////////////
	}
	
}
