package main;
import java.util.ArrayList;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class Interface  implements SerialPortEventListener {

	SerialPort serialPort;
	
	Main main;
	
	int previous=-1;
	
	ArrayList<Character> message;
	
	int message_type=0;  // 0 waiting, 1 : x, 2 : y , 3 : mx , 4 : my
	
	String posx="";
	String posy="";
	
	String movx="";
	String movy="";
	
	public boolean ready=false;
	public int joystickX=0;
	public int joystickY=0;
	
	public boolean ready2=false;
	public int movementX=0;
	public int movementY=0;
	
	public Interface(Main m){
		
		main=m;
		
		message=new ArrayList<Character>();
		
		int port=0;
		boolean connected=false;
		
		
		while (!connected && port<6){
		try {
			serialPort = new SerialPort(Main.PORT+port);
			serialPort.openPort();
			serialPort.setParams(19200, 8, 1, 0);
			
			//serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | 
            //         					  SerialPort.FLOWCONTROL_RTSCTS_OUT);

			serialPort.addEventListener(this, SerialPort.MASK_RXCHAR);
			
			System.out.println("port opened");
			connected=true;
			
		} catch (SerialPortException e) {
				//e.printStackTrace();
				System.out.println("cannot connect port USB"+port);
				port++;
			}
		}
	}
	
	public void close(){
		try {
			
			serialPort.writeString("f000");
			serialPort.writeString("s000");
			serialPort.writeString("g500");
			serialPort.writeString("h500");
			
			serialPort.writeString("o500");
			serialPort.writeString("p500");
			
			serialPort.writeString("m0");
			
			System.out.println("Port closed: " + serialPort.closePort());
		} catch (SerialPortException e) {e.printStackTrace();}
	}
	
	public void sendMsg(String msg){

		//System.out.println("sent : "+msg);
		try {
			serialPort.writeString(msg);
		} catch (SerialPortException e) {e.printStackTrace();}
	}

	
	
	public void serialEvent(SerialPortEvent event) {

        if(event.isRXCHAR() && event.getEventValue() > 0) {
            try {
            	
                String received = serialPort.readString(event.getEventValue());
                //System.out.println("Received response: " + received);
                
                for(int i=0;i<received.length(); i++){
                	
                	// new message
                	if ((char)Character.codePointAt(received,i)=='a'){
                		message_type=1;
                		message.clear();
                	}
                	// pos x sended
                	else if ((char)Character.codePointAt(received,i)=='b'){
                		// correct
                		if (message_type==1){
                			posx="";
                    		for (int j=0;j<message.size();j++){
                    			posx+=message.get(j);
                    		}
                    		message.clear();
                    		message_type=2;
                		}
                		// error in transmission
                		else{
                			message_type=0;
                			message.clear();
                			System.out.println("ERROR");
                		}
                	}
                	// pos y sended
                	else if ((char)Character.codePointAt(received,i)=='c'){
                		// correct
                		if (message_type==2){
                			posy="";
                    		for (int j=0;j<message.size();j++){
                    			posy+=message.get(j);
                    		}
                    		message.clear();
                    		message_type=3;
                    		
                    		if (!ready){
                    			joystickX=Integer.parseInt(posx)-500;
                    			joystickY=Integer.parseInt(posy)-500;
                    			ready=true;
                    		}
                		}
                		// error in transmission
                		else{
                			message_type=0;
                			message.clear();
                			System.out.println("ERROR");
                		}
                	}
                	// mov x sended
                	else if ((char)Character.codePointAt(received,i)=='d'){
                		// correct
                		if (message_type==3){
                			movx="";
                    		for (int j=0;j<message.size();j++){
                    			movx+=message.get(j);
                    		}
                    		message.clear();
                    		message_type=4;
                		}
                		// error in transmission
                		else{
                			message_type=0;
                			message.clear();
                			System.out.println("ERROR");
                		}
                	}
                	// mov y sended
                	else if ((char)Character.codePointAt(received,i)=='e'){
                		// correct
                		if (message_type==4){
                			movy="";
                    		for (int j=0;j<message.size();j++){
                    			movy+=message.get(j);
                    		}
                    		message.clear();
                    		message_type=0;
                    		
                    		if (!ready){
                    			movementX=Integer.parseInt(movx);
                    			movementY=Integer.parseInt(movy);
                    			ready2=true;
                    		}
                		}
                		// error in transmission
                		else{
                			message_type=0;
                			message.clear();
                			System.out.println("ERROR");
                		}
                	}
                	// sending data
                	else{
                		message.add((char)Character.codePointAt(received,i));
                	}
                }
                
            }
            catch (SerialPortException ex) {System.out.println("Error in receiving string from COM-port: " + ex);}
        }
    }

}