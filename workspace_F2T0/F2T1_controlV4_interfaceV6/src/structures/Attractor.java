package structures;


public class Attractor {

	public int x;
	public int y;
	
	public int strength;
	public int distance;
	public int type;

	public boolean reached=false;
	
	public Attractor(int x, int y, int strength, int distance, int type){
		this.x=x;
		this.y=y;
		this.strength=strength;
		this.distance=distance;
		this.type=type;
	}
	
	public Attractor(String[] args){
		this.x=Integer.parseInt(args[1]);
		this.y=Integer.parseInt(args[2]);
		this.strength=Integer.parseInt(args[3]);
		this.distance=Integer.parseInt(args[4]);
		this.type=Integer.parseInt(args[5]);
	}
	
	public Attractor duplicate(){
		return new Attractor(x, y, strength, distance, type);
	}
}
