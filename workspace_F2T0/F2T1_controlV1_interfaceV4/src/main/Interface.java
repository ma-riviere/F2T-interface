package main;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class Interface  implements SerialPortEventListener {

	private boolean F2T_connected=true;
	
	private SerialPort serialPort;
	
	private int message_type=0;  // 0 waiting, 1 : x, 2 : y , 3 : mx , 4 : my
	
	
	public boolean ready=false;
	public float joystickX=0;
	public float joystickY=0;
	
	public int posx=0;
	public int posy=0;
	
	public boolean ready2=false;
	public int movementX=0;
	public int movementY=0;
	
	public int counterX=0;
	public int counterY=0;
	
	private long time1=0;
	private long time2=0;
	
	private Main main;
	
	public Interface(Main m){

		main=m;
		
		int port=0;
		F2T_connected=false;
		
		
		while (!F2T_connected && port<6){
		try {
			serialPort = new SerialPort(Main.PORT+port);
			serialPort.openPort();
			serialPort.setParams(115200, 8, 1, 0);
			
			//serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | 
            //         					  SerialPort.FLOWCONTROL_RTSCTS_OUT);

			serialPort.addEventListener(this, SerialPort.MASK_RXCHAR);
			
			System.out.println("port opened");
			F2T_connected=true;
			
		} catch (SerialPortException e) {
				System.out.println("cannot connect port USB"+port);
				port++;
			}
		}
	}
	
	public void close(){
		try {
			if (serialPort.isOpened()){
				serialPort.writeString("f000");
				serialPort.writeString("s000");
				serialPort.writeString("g500");
				serialPort.writeString("h500");
				
				serialPort.writeString("o500");
				serialPort.writeString("p500");
				
				serialPort.writeString("m0");
				
				System.out.println("Port closed: " + serialPort.closePort());
			}
		} catch (SerialPortException e) {e.printStackTrace();}
	}
	
	public void sendMsg(byte[] msg){

		if (F2T_connected){
			try {
				
				//System.out.println("sent : "+(msg[0]& 0xff) +" , "+(msg[1]& 0xff)+" , "+(msg[2]& 0xff)+" , "+(msg[3]& 0xff));
				serialPort.writeBytes(msg);
			} catch (SerialPortException e) {e.printStackTrace();}
		}
		else{
			//System.out.println("message : "+msg);
		}
	}

	
	
	public void serialEvent(SerialPortEvent event) {

        if(event.isRXCHAR() && event.getEventValue() > 0) {
            try {
            	
                //String received = serialPort.readString(event.getEventValue());
                byte[] received=serialPort.readBytes();

                for(int i=0;i<received.length; i++){
                	
                	//System.out.println("R: " + (received[i]));
                	
                	
                	/*if ((received[i] & 0xff)==253){
                		//time1=System.nanoTime();
                		message_type=0;
                		System.out.println();
                	}
                	else if ((received[i] & 0xff)==254){
                		
                		message_type=10;
                	}
                	if (message_type==10){
                		System.out.print((char)received[i]);
                	}*/
                	
                	
                	// new message
                	if ((received[i] & 0xff)==255){
                		//time1=System.nanoTime();
                		message_type=1;
                	}
                	else{
                		if (message_type==1){
                			movementX=(received[i] & 0xff);
                    		message_type=2;
                		}

                		else if (message_type==2){
                			movementY=(received[i] & 0xff);
                    		message_type=3;                    		
                		}
                		else if (message_type==3){
                			counterX=(received[i] & 0xff)*254;
                			message_type=4; 
                		}
                		else if (message_type==4){
                			counterX+=(received[i] & 0xff);
                			message_type=5; 
                		}
                		else if (message_type==5){
                			counterY=(received[i] & 0xff)*254;
                			message_type=6; 
                		}
                		else if (message_type==6){
                			counterY+=(received[i] & 0xff);
                			message_type=0;
                			
                			joystickX=((float)movementX-100)/20;
                			joystickY=((float)movementY-100)/20;
                			
                			posx=(counterX-30000+535);
                			posy=(counterY-30000-535);
                			
                			ready=false;
                					
                			main.dx=joystickX;
            				main.dy=joystickY;
            				
            				if (main.dx<0.2 && main.dx>-0.2) main.dx=0;
            				if (main.dy<0.2 && main.dy>-0.2) main.dy=0;
            				
            				
            				main.x=(float)posx/1.55f;
            				main.y=(float)posy/1.55f;
                			
            				ready=true;
            				
            				//System.out.println("+++ received -> " + joystickX+" , "+joystickY+" ; "+main.dx+" , "+main.dy);
                			//System.out.println("+++ received "+(System.nanoTime()-time1)+" -> " + joystickX+" , "+joystickY+" ; "+main.x+" , "+posy);
                			//time1=System.nanoTime();
                			
                			
                			if (!main.processing) main.controlLoop();
                			else System.out.println("test");
                			
                			//System.out.println((time1-System.nanoTime()));
                		}
                		//else System.out.println("R: " + (received[i]));
                	}
                }
                
            }
            catch (SerialPortException ex) {System.out.println("Error in receiving string from COM-port: " + ex);}
        }
    }

}




