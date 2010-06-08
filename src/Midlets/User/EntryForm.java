package com.Blue.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
//To obtain params and send to all cabbies receive the responses(yes/no) and create a new list 
public class EntryForm extends Form implements CommandListener {

	private MIDlet midlet;
	private Display display;
	private Vector Connections, Devices, New_connections, New_devices, in, out,
			New_in, New_out;
	
	private TextField Destination, Pickup_point, Phone_num;
	private Command cmd_Next, cmd_Restart, cmd_Exit;
	private String destination, pickup_point, phone_num, datalist;
	private int confirm_status=0;
	private boolean finished, flagged;
	private boolean[] checked;
	private DataInputStream receive_response;
	private DataOutputStream send_response;

	public void receive() {//Receive confirmation status from the cabbie and create a list of cabbie's who have confirmed 1 - yes, 0 - no

		for (int i = 0; i < 4 && finished == true; i++) {
			finished = false;

			for (int j = 0; j < in.size() && checked[j] == false; j++) {

				receive_response = (DataInputStream) in.elementAt(j);

				try {
					confirm_status = receive_response.readInt();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}finally{
				if (confirm_status == 1) {
					checked[j] = true;
					New_devices.addElement(Devices.elementAt(j));
					System.out.println("New Dev :" + New_devices.size());
					New_connections.addElement(Connections.elementAt(j));
					System.out.println("New Conn :"+New_connections.size());
					New_in.addElement(receive_response);
					System.out.println("New In :"+New_in.size());
					New_out.addElement(out.elementAt(j));
					System.out.println("New Out :"+New_out.size());  
				}

				}
			}
			}
			
			for (int k = 0; k < checked.length; k++) {//Loop to check if all the cabbies have responded either with a yes or no, 
				//checked array is true if the cabbies has responded with a yes or no, false otherwise
				//finished is used to check whether all the cabbie's have replied (with a yes/no),its false and will become true if a cabbie has not responded
				if (checked[k] == false) {
					finished = true;
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	

	public void EstablishConnections() {//To send the params(dest,pick up  point and mobile no) to all the cabbies listed

		for (int i = 0; i < in.size(); i++) {

			checked[i] = false;
			send_response = (DataOutputStream) out.elementAt(i);

			try {
				send_response.writeUTF(datalist);
				send_response.flush();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		this.append("Details Sent\n\nWaiting for confirmation List........");
		display.setCurrent(this);

		receive();//Function Wait for confirmations yes/no from all the cabbies listed

	}

	public EntryForm(MIDlet midlet, Display display, Vector Devices,
			Vector Connections, Vector in, Vector out) {
		super("");

		this.midlet = midlet;
		this.display = display;

		this.Connections = Connections;
		this.Devices = Devices;
		this.in = in;
		this.out = out;

		New_connections = new Vector();
		New_devices = new Vector();
		New_in = new Vector();
		New_out = new Vector();

		finished = true;
		flagged = false;
		checked = new boolean[in.size()];

		destination = new String();
		pickup_point = new String();
		phone_num = new String();
		Destination = new TextField("Enter Destination:", "", 30, TextField.ANY);
		Pickup_point = new TextField("Enter Pick Up Point:", "", 30,
				TextField.ANY);
		Phone_num = new TextField("Enter Phone Number:", "", 10,
				TextField.PHONENUMBER);

		cmd_Next = new Command("Next", Command.OK, 0);
		cmd_Restart = new Command("Restart", Command.BACK, 0);
		cmd_Exit = new Command("Exit", Command.EXIT, 0);

		this.append(Destination);
		this.append(Pickup_point);
		this.append(Phone_num);

		this.addCommand(cmd_Next);
		this.addCommand(cmd_Restart);
		this.addCommand(cmd_Exit);

		setCommandListener(this);
		display.setCurrent(this);

	}

	public void commandAction(Command c, Displayable d) {

		if (c == cmd_Next && flagged == false) {

			flagged = true;
			destination = Destination.getString();
			pickup_point = Pickup_point.getString();
			phone_num = Phone_num.getString();

			if (destination.length() > 0 && pickup_point.length() > 0
					&& phone_num.length() > 0) {
				datalist = destination + "%" + pickup_point + "%" + phone_num;

				EstablishConnections();

				new SelectForm(midlet, display, New_devices, New_connections,
						New_in, New_out);

			} else {
				display.setCurrent(new Alert(
						"There was an error on device search!"), new EntryForm(
						midlet, display, Devices, Connections, in, out));

			}
		} else if (c == cmd_Exit) {
			midlet.notifyDestroyed();
		} else {
			new Setup(midlet, display);
		}

	}

}
