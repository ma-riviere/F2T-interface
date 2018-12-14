package main;

public class SoundSource {

	public float px=0;
	public float py=0;
	
	public float rx=0;
	public float ry=0;
	
	public boolean active=false;
	
	public SoundSource(int x, int y){
		
		px=x;
		py=y;
		
	}
	
	
	public void compute(float x, float y){
		rx=px-x;
		ry=py-y;
	}
	
	public SoundSource(String args){
		String[] elements=args.split(" ");
		setParams(elements);
	}
	
	public SoundSource(String[] args){
		setParams(args);
	}
	
	
	public void setParams(String[] args){
		px=Float.parseFloat(args[1]);
		py=Float.parseFloat(args[2]);
		
		for (int i=3;i<args.length;i++) System.out.println(" param : "+args[i]);
		
	}
	
	public void activate(boolean activity){
		active=activity;
	}
	
	
	public void close(){
		
		// TODO
		System.out.println("GOODBYE");
	}
	
}
