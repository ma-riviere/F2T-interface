import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class Main {

	
	private MainFrame frame;
	
	public ArrayList<Integer> x;
	public ArrayList<Integer> y;
	
	public Main(){
		
		x=new ArrayList<Integer>();
		y=new ArrayList<Integer>();
		
		loadFile();
		
		frame=new MainFrame(this);
		
		
		
		
		frame.repaint();
		
		
	}
	
	
	public void loadFile(){
		
		String fileName = "/home/simon/Bureau/etalonnage.txt";
		String[] elements;
		
		try {
			InputStream ips=new FileInputStream(fileName); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line=null;
			line=br.readLine();
			
			while (line!=null){
				elements=line.split(" ");
				if (elements.length==3){
					x.add(Integer.parseInt(elements[0]));
					y.add(Integer.parseInt(elements[2]));
				}
				line=br.readLine();
			}
			br.close();
		}
		catch (Exception e) {System.out.println("init file not found");}

		//for (int i=0;i<x.size();i++){
		//	System.out.println(x.get(i)+" , "+y.get(i));
		//}
	}
	
	
	
	public static void main(String[] args) {
		
		new Main();
		
	}

}
