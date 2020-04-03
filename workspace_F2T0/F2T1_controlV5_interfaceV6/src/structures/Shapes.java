package structures;
import java.util.ArrayList;

/**
 * Several functions to add shape paths
 * @author simon gay
 */

public class Shapes {

	public static void Rectangle(ArrayList<TargetPoint> list, int speed){
		list.add(new TargetPoint(-100,-100,speed, 1));
		list.add(new TargetPoint( 100,-100,speed, 1));
		list.add(new TargetPoint( 100, 100,speed, 1));
		list.add(new TargetPoint(-100, 100,speed, 1));
		list.add(new TargetPoint(-100,-100,speed, 1));
	}
	
	public static void Circle(ArrayList<TargetPoint> list, int speed){
		for (int i=0;i<=360;i+=20){
			list.add(new TargetPoint((int)(100*Math.cos(Math.toRadians(i))),(int)(100*Math.sin(Math.toRadians(i))),speed, 1));
		}
	}
	
	public static void Star(ArrayList<TargetPoint> list, int speed){
		list.add(new TargetPoint(0,20,speed, 1));
		list.add(new TargetPoint(5,5,speed, 1));
		list.add(new TargetPoint(20,5,speed, 1));
		list.add(new TargetPoint(5,-5,speed, 1));
		list.add(new TargetPoint(15,-20,speed, 1));
		list.add(new TargetPoint(0,-10,speed, 1));
		list.add(new TargetPoint(-15,-20,speed, 1));
		list.add(new TargetPoint(-5,-5,speed, 1));
		list.add(new TargetPoint(-20,5,speed, 1));
		list.add(new TargetPoint(-5,5,speed, 1));
		list.add(new TargetPoint(0,20,speed, 1));
	}
}
