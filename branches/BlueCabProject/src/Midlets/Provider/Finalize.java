package com.Blue.Provider;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.io.StreamConnection;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.midlet.MIDlet;

public class Finalize extends Form implements CommandListener{
	
	private MIDlet midlet;
	private Display display;
	
	private DataInputStream input;
	private String response;
	private Command cmd_Tryagain, cmd_Finish;
	
	private boolean recd;
	//Receive passcode and time of meeting  from the user and finish
	public Finalize(MIDlet midlet, Display display,	StreamConnection Connection, DataInputStream input, DataOutputStream output) {
				
		super("");
		this.midlet = midlet;
		this.display = display;

		this.input=input;
				
		recd=false;
		response=new String();

		cmd_Tryagain = new Command("Try Again", Command.BACK, 0);
		cmd_Finish = new Command("Finish", Command.EXIT, 0);
						
		System.out.println("In prov finalize");
		
		while (recd == false) {	
			try{
				response = input.readUTF();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (response!=null) {
				recd = true;
				System.out.println(":"+response);
					
		} 
		}
		
		this.addCommand(cmd_Tryagain);
		this.addCommand(cmd_Finish);
		this.append("Passcode % Meeting time:"+response);
		
		setCommandListener(this);
		display.setCurrent(this);
	}

	public void commandAction(Command c, Displayable d) {

		if (c == cmd_Finish) {
			midlet.notifyDestroyed();
		} else {
			new Setup(midlet,display);
		}

	}

}

