package main;
import java.util.ArrayList;


public class Shapes {

	public static void Rectangle(ArrayList<Target> list){
		int speed=200;
		list.add(new Target(-100,-100,speed, 1));
		list.add(new Target( 100,-100,speed, 1));
		list.add(new Target( 100, 100,speed, 1));
		list.add(new Target(-100, 100,speed, 1));
		list.add(new Target(-100,-100,speed, 1));
	}
	
	public static void Circle(ArrayList<Target> list){
		int speed=200;
		for (int i=0;i<=360;i+=20){
			list.add(new Target((int)(100*Math.cos(Math.toRadians(i))),(int)(100*Math.sin(Math.toRadians(i))),speed, 1));
		}
	}
	
	public static void Star(ArrayList<Target> list){
		int speed=100;
		list.add(new Target(0,20,speed, 1));
		list.add(new Target(5,5,speed, 1));
		list.add(new Target(20,5,speed, 1));
		list.add(new Target(5,-5,speed, 1));
		list.add(new Target(15,-20,speed, 1));
		list.add(new Target(0,-10,speed, 1));
		list.add(new Target(-15,-20,speed, 1));
		list.add(new Target(-5,-5,speed, 1));
		list.add(new Target(-20,5,speed, 1));
		list.add(new Target(-5,5,speed, 1));
		list.add(new Target(0,20,speed, 1));
	}
}
