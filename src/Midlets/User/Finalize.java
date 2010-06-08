package com.Blue.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.io.StreamConnection;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
//Send passcode(4digits, used for verification at the time of meet) and time for meeting
public class Finalize extends Form implements CommandListener{
	
	private MIDlet midlet;
	private Display display;
	
	private StreamConnection connection;
	private DataInputStream in;
	private DataOutputStream out;
	
	private TextField passcode,time;
	private Command cmd_Next, cmd_Tryagain, cmd_Finish;
	private String msg = new String();
	
	public void finish()
	{
		try {
			out.writeUTF(msg);
			out.flush();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
	public Finalize(MIDlet midlet, Display display,	StreamConnection Connection,DataInputStream in, DataOutputStream out) {
		super("");
		
		this.midlet = midlet;
		this.display = display;

		this.connection = Connection;
		this.in=in;
		this.out=out;
		
		passcode= new TextField("Enter Passcode:","",4,TextField.NUMERIC);
		time= new TextField("Enter meeting time in minutes:","",2,TextField.NUMERIC);
			
		this.append(passcode);
		this.append(time);
				
		cmd_Next = new Command("Next", Command.OK, 0);
		cmd_Tryagain = new Command("Try Again", Command.BACK, 0);
		cmd_Finish = new Command("Finish", Command.EXIT, 0);

		this.addCommand(cmd_Next);
		this.addCommand(cmd_Tryagain);
		this.addCommand(cmd_Finish);

		this.setCommandListener(this);
		display.setCurrent(this);
			
	}

	public void commandAction(Command c, Displayable d) {

		if (c == cmd_Next) {
			if(passcode.size()>0&&time.size()>0)
			{
				msg = passcode.getString()+"%"+time.getString();
				System.out.println(msg);
				finish();
				System.out.println("Done");
			}
			else
			{
				display.setCurrent(new Alert(
				"Enter Passcode and time!"), this);

			}
			new Finalize(midlet,display,connection,in,out);
		} else if (c == cmd_Finish) {
			this.append("Taxi booked. . . \n Thank You");
			display.setCurrent(this);
			midlet.notifyDestroyed();
		} else {
			new Setup(midlet,display);
		}

	}

}
