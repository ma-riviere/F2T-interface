package display;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


import main.Main;

/**
 * Console mode panel
 * @author simon gay
 */

public class DisplayConsolePanel extends JPanel implements KeyListener{

	private static final long serialVersionUID = 1L;
	
	protected Main main;
	public DisplayFrame frame;
	
	private JTextArea textArea;
	private JScrollPane scroll;
	
	private JTextField input;
	
	public DisplayConsolePanel(Main m, DisplayFrame f){
		main=m;
		frame=f;
		
		this.setLayout(null);
		
		textArea = new JTextArea(40, 85);
        scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        textArea.setEditable(false);
        
        this.add(scroll);
        scroll.setBounds(10,10,980,620);
        
        
        input=new JTextField();
        this.add(input);
		input.setBounds(10, 640, 980, 30);
		
		input.addKeyListener(this);
        
	}
	
	
	public void setMsg(String msg){
		textArea.setText(msg);
	}


	public void keyTyped(KeyEvent e) {}


	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_ENTER){
			
			String line=input.getText();
			if (line.length()>1){
				main.script.setCommandLine(line);
			}
			input.setText("");
		}
	}

	public void keyReleased(KeyEvent e) {}

}
