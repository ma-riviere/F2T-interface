package main;

import java.util.ArrayList;

import modules.*;

public class Virtual  extends Thread {

	private Main main;
	
	public float px=0;
	public float py=0;
	
	public float px0=0;
	public float py0=0;
	
	public float mx=0;
	public float my=0;
	
	public float mx_temp=0;
	public float my_temp=0;
	
	public float speedX=0;
	public float speedY=0;
	
	public long time1=0;
	public long time2=0;
	
	public boolean stop=false;
	
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
		
		while (!stop){

			try {Thread.sleep(1);
			} catch (InterruptedException e) {e.printStackTrace();}
			
			mx_temp=0;
			my_temp=0;
			
			mx_temp+=main.jx*80;
			my_temp+=main.jy*80;
			
			
			for (int i=0;i<moduleList.size();i++) moduleList.get(i).compute();
			
			//friction.compute();
			//edges.compute();
			//flow.compute();
			//rail.compute();
			//magnetic.compute();
			//attraction.compute();
			//path.compute();
			
			if (mx_temp<10 && mx_temp>-10) mx_temp=0;
			if (my_temp<10 && my_temp>-10) my_temp=0;
			
			if (mx_temp>254) mx_temp=254;
			if (mx_temp<-254) mx_temp=-254;
			if (my_temp>254) my_temp=254;
			if (my_temp<-254) my_temp=-254;
			
			mx=mx_temp;
			my=my_temp;
			
			//System.out.println(mx_temp);
			
			time2=time1;
			time1=System.nanoTime();

			px0+=mx*1000/(float)(time1-time2);
			py0+=my*1000/(float)(time1-time2);
			
			if (px0>349) px0=349;
			if (px0<-350) px0=-350;
			if (py0>350) py0=350;
			if (py0<-350) py0=-350;
			
			px=px0;
			py=py0;

			//System.out.println((dx*10000000/time));
			
		}
		
		System.out.println("virtual system stopped");
	}
	
}
