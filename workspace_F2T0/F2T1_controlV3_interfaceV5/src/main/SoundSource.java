package main;

import java.io.File;
import org.urish.openal.Source;

public class SoundSource {

	public float px=0;
	public float py=0;
	
	public float rx=0;
	public float ry=0;
	public float dist=0; 
	public boolean active=false;
	
	public float mute_distance = 400; 
    public float win_distance = 40; 
	
    boolean win_played = false; 
    
    public float scaled_rx, scaled_ry, scaled_dist; 
     
    public String filename, filename2; 
	public Source source ; 
    public Source win_source ; 
    
	public SoundSource(int x, int y){
		
		px=x;
		py=y;
		
	}
	
	
	public void compute(float x, float y){
		
        //main update function for SoundSource objects
        // zakres dotykowy to +/- 400 po x i y 
        //dzwieki mozemy umieszczac poza zakresem 
	rx=px-x;
	ry=-y-py; //inverted jostick coordinate
            dist = (float)Math.sqrt((rx*rx)+(ry*ry)); 
            
            if (Math.abs(rx)>mute_distance) scaled_rx=1; 
            else scaled_rx= rx/mute_distance; 
            
            if (Math.abs(ry)>mute_distance) scaled_ry=1; 
            else scaled_ry= ry/mute_distance; 
            
            if (dist>mute_distance) scaled_dist=1; 
            else scaled_dist= dist/mute_distance; 
            
            if (dist>win_distance) win_played = false;

            
            
            try
            {
                source.setPosition(scaled_rx, 0, scaled_ry);
                //OpenAL has an OpenGL-like coordinate system: left/right, up/down, back/front) 
                
                source.setGain((1-scaled_dist));
                //if we want to add sound source occlusion we can "dim" the sound using the gain if there is no direct path 
                //any more advanced sound rendering would require some form of raytracing or a different library such as EAX 
                
                
//                System.out.println("joystick " + x + " " + y + " " );
//                System.out.println("source   " + px + " " + py + " " );
//                System.out.println("relative " + rx + " " + ry + " " + dist);
//                System.out.println("scaled   " +scaled_rx + " " + scaled_ry + " " + scaled_dist);
               
            } catch (Exception e) { System.out.println("error in Compute : "+e); }
            
            
            if (!win_played && dist<win_distance )
                try
                { 
                    win_source.play();
                    win_played = true; 
                } catch (Exception e) { System.out.println(e); }
            
            //TODO:  
            
            // start with a simple "find the source experiment" 
            // 1) just one source 
            // "BRAVO" jak dojdzie do źródła 
            // 2) one source and an obstacle (That blocks movement, but not sound) 
            // 3) two sources, you have to reach first and second source
            // 4) two soruces and walking around obstacle   
            // 5) wall and a door - find the door and reach the source  
            
            // if 5) then we need to get a pointer to the image file, green channel is the height of an obstacle  
            
            // deadline na artykuł to 16 stycznia 
            // przebadać minimum 10 osób na tyle 
	}
	
	public SoundSource(String args){
		String[] elements=args.split(" ");
		setParams(elements);
		//px=Float.parseFloat(elements[1]);
		//py=Float.parseFloat(elements[2]);
		
		//for (int i=3;i<elements.length;i++) System.out.println(" param : "+elements[i]);
	}
	
	public SoundSource(String[] args){
		setParams(args);
	}
	
	
	public void setParams(String[] args){
       	//zero argument should be "s" 
        
        //source = main.openAL.createSource(new File("example.wav"));
        //source.play(); 

        for (int i=0;i<args.length;i++) 
            System.out.println(" param : "+args[i]);
           
        px=Float.parseFloat(args[1]);
        py=Float.parseFloat(args[2]);
        
        
        if (args.length > 3) filename = args[3]+".wav"; 
        else                 filename = "synth_15.wav"; 
     
        if (args.length > 4) filename2 = args[4]+".wav"; 
        else                 filename2 = "bell_27.wav"; 
        
        System.out.println(filename);
        System.out.println(filename2);
        
        
                
        try 
        { 
           //OpenAL openal = new OpenAL();
          //source = main.Main.openal.createSource(new File("c://synth_15.wav"));
          source = main.Main.openal.createSource(new File(main.Main.FILES+main.Main.SOUND+filename));
          win_source = main.Main.openal.createSource(new File(main.Main.FILES+main.Main.SOUND+filename2));
          source.play();
          source.setLooping(true); 	
        } 
        catch (Exception e)
        {
          System.out.println(e); 
        }    
	}
	
	public void activate(boolean activity){
		active=activity;
	}
	
	
	public void close(){
        source.close();    
        win_source.close();
	}
	
}
