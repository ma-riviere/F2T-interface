package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

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
	
	private Main main;
	
	private Simulation simulation=null;
	
	public Interface(Main m){

		main=m;
		
		int port=0;
		F2T_connected=false;
		
		
		while (!F2T_connected && port<6){
		try {
			serialPort = new SerialPort(Main.PORT+port);
			serialPort.openPort();
			serialPort.setParams(28800, 8, 1, 0);

			serialPort.addEventListener(this, SerialPort.MASK_RXCHAR);
			
			System.out.println("port opened");
			F2T_connected=true;
			
		} catch (SerialPortException e) {
				System.out.println("cannot connect port USB"+port);
				port++;
			}
		}
		
		if (!F2T_connected){
			simulation=new Simulation(this);
			simulation.start();
		}
	}
	
	public void close(){
		try {
			
			if (simulation!=null) simulation.running=false;
			
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
				serialPort.writeBytes(msg);
			} catch (SerialPortException e) {e.printStackTrace();}
		}
		else{
			
		}
	}

	
	
	public void serialEvent(SerialPortEvent event) {

        if(event.isRXCHAR() && event.getEventValue() > 0) {
            try {
                byte[] received=serialPort.readBytes();

                for(int i=0;i<received.length; i++){
                	
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
                			
                			posx=(counterX-30000+750);
                			posy=(counterY-30000-750);
                			
                			ready=false;
                					
                			main.jx=joystickX;
            				main.jy=joystickY;
            				
            				if (main.jx<0.15 && main.jx>-0.15) main.jx=0;
            				if (main.jy<0.15 && main.jy>-0.15) main.jy=0;
            				
            				if (main.jx>0) main.jx-=0.15;
            				if (main.jx<0) main.jx+=0.15;
            				if (main.jy>0) main.jy-=0.15;
            				if (main.jy<0) main.jy+=0.15;
            				
            				main.x=(float)posx/2.14f;
            				main.y=(float)posy/2.14f;
                			
            				ready=true;

                			if (!main.processing) main.controlLoop();
                		}
                	}
                }
            }
            catch (SerialPortException ex) {System.out.println("Error in receiving string from COM-port: " + ex);}
        }
    }
	
	protected class Simulation extends Thread{
		
		private Interface inter;
		protected boolean running=true;

		public Simulation(Interface inter){
			this.inter=inter;
		}
		
		public void run(){
			
			try {Thread.sleep(10);
			} catch (InterruptedException e) {e.printStackTrace();}
			
			while (running){

				ready=false;
				
				if (inter.main.display.keyboard.up || inter.main.display.keyboard.down){
					if (inter.main.display.keyboard.up)   inter.main.jy=3;
					if (inter.main.display.keyboard.down) inter.main.jy=-3;
				} else inter.main.jy=0;
				if (inter.main.display.keyboard.left || inter.main.display.keyboard.right){
					if (inter.main.display.keyboard.left)   inter.main.jx=-3;
					if (inter.main.display.keyboard.right) inter.main.jx=3;
				} else inter.main.jx=0;

				ready=true;
				
				if (main.virtual!=null && main.virtual.started) main.controlLoop();
				
				main.x=main.virtual.px;
				main.y=main.virtual.py;
				
				try {Thread.sleep(1);
				} catch (InterruptedException e) {e.printStackTrace();}
			}
		}
	}

}




