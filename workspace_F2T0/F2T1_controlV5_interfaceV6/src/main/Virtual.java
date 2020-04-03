package main;

import java.util.ArrayList;

import modules.*;

/**
 * Virtual position of the joystick for faster position estimation
 * @author simon gay
 */

public class Virtual  extends Thread {

	private Main main;
	
	public float px=350;
	public float py=-350;
	
	public float px0=350;
	public float py0=-350;
	
	public float mx=0;
	public float my=0;
	
	public float mx_temp=0;
	public float my_temp=0;
	
	public float[] mx_trace;
	public float[] my_trace;
	public int traceIndex=0;
	
	public float speedX=0;
	public float speedY=0;
	
	public long time1=0;
	public long time2=0;
	
	public boolean stop=false;
	public boolean started=false;
	
	public ArrayList<Module> moduleList;
	
	//public Frictions friction;
	//public Edges edges;
	//public Flow flow;
	//public Rail rail;
	//public Path path;
	//public Magnetic magnetic;
	//public Attraction attraction;
	
	public Virtual(Main m){
		
		main=m;
		
		moduleList=new ArrayList<Module>();
		
		mx_trace=new float[10];
		my_trace=new float[10];
		
		moduleList.add(new Frictions(main,this));
		moduleList.add(new Edges(main,this));
		moduleList.add(new Flow(main, this));
		moduleList.add(new Rail(main, this));
		moduleList.add(new Magnetic(main, this));
		moduleList.add(new Attraction(main, this));
		moduleList.add(new Path(main, this));
		
		//friction=new Frictions(main,this);
		//edges=new Edges(main,this);
		//flow=new Flow(main, this);
		//rail=new Rail(main, this);
		//path=new Path(main, this);
		//magnetic=new Magnetic(main, this);
		//attraction=new Attraction(main, this);
	}

	public void run(){
		
		
		time1=System.nanoTime();
		
		px=349;
		py=-350;
		px0=349;
		py0=-350;
		
		started=true;
		
		while (!stop){

			try {Thread.sleep(1);
			} catch (InterruptedException e) {e.printStackTrace();}
			
			mx_temp=main.jx*80;
			my_temp=main.jy*80;

			for (int i=0;i<moduleList.size();i++) moduleList.get(i).compute();
			
			
			//if (mx_temp<5 && mx_temp>-5) mx_temp=0;
			//if (my_temp<5 && my_temp>-5) my_temp=0;
			
			
			float speed2=mx_temp*mx_temp+my_temp*my_temp;
			
			if (speed2>64516){ // 254²
				float speed=(float) Math.sqrt(speed2);
				mx_temp=mx_temp*254/speed;
				my_temp=my_temp*254/speed;
			}
			
			if (mx_temp>254) mx_temp=254;
			if (mx_temp<-254) mx_temp=-254;
			if (my_temp>254) my_temp=254;
			if (my_temp<-254) my_temp=-254;
			
			//if (mx_temp<10 && mx_temp>-10) mx_temp=0;;
			//if (my_temp<10 && my_temp>-10) my_temp=0;
			
			//System.out.println(mx_temp);
			
			time2=time1;
			time1=System.nanoTime();

			px0+=mx*1000/(float)(time1-time2);
			py0+=my*1000/(float)(time1-time2);
			
			if (px0>349) px0=349;
			if (px0<-350) px0=-350;
			if (py0>350) py0=350;
			if (py0<-350) py0=-350;
			
			// border protections
			if (mx_temp>0 && px0>320) mx_temp=mx_temp/(Math.abs(320-px0+1)/2);
			if (my_temp<0 && py0<-320) my_temp=my_temp/(Math.abs(320+py0+1)/2);
			
			
			mx=mx_temp*1.8f;
			my=my_temp*1.8f;
			
			//System.out.println(main.jx+" , "+(mx/80/1.8f));
			
			px0=px0*0.995f+main.px*0.005f;
			py0=py0*0.995f+main.py*0.005f;
			
			px=px0;
			py=py0;
			
			mx_trace[traceIndex]=mx;
			my_trace[traceIndex]=my;
			traceIndex++;
			if (traceIndex>=10) traceIndex=0;

			//System.out.println((dx*10000000/time));
			
		}
		
		System.out.println("virtual system stopped");
	}
	
}
