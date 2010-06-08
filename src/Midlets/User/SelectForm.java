package com.Blue.User;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.util.Vector;

import javax.bluetooth.RemoteDevice;
import javax.microedition.io.StreamConnection;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.midlet.MIDlet;
//Display and Select one from the cabbie list(who have cinfirmed with a yes or no) and finalize the operations
public class SelectForm  extends Form implements CommandListener{

		private MIDlet midlet;
		private Display display;
		private Vector Connections, Devices, input, output;
		private StreamConnection connection;	
		private DataInputStream in;
		private DataOutputStream out;
		private ChoiceGroup Result_list;
		private Command cmd_Next, cmd_Restart, cmd_Exit;
		
		public void GenerateList() {
			for (int i = 0; i < Devices.size(); i++) {
				
				try {
					RemoteDevice rd = (RemoteDevice)Devices.elementAt(i);
					String device_name = rd.getFriendlyName(true);
					Result_list.append(i+1+"."+device_name, null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		public SelectForm(MIDlet midlet, Display display, Vector Devices,Vector Connections, Vector input, Vector output) {
			
			super("");

			
			this.midlet = midlet;
			this.display = display;

			
			this.Devices = Devices;
			this.Connections = Connections;
			this.input=input;
			this.output=output;

			
			cmd_Next = new Command("Next", Command.OK, 0);
			cmd_Restart = new Command("Restart", Command.BACK, 0);
			cmd_Exit = new Command("Exit", Command.EXIT, 0);

			
			Result_list= new ChoiceGroup("Cab drivers Acknowledgement List",ChoiceGroup.EXCLUSIVE);
									
			GenerateList();
						
			this.append(Result_list);
			
			this.addCommand(cmd_Next);
			this.addCommand(cmd_Restart);
			this.addCommand(cmd_Exit);

			setCommandListener(this);
			display.setCurrent(this);
		
		}

		public void commandAction(Command c, Displayable d) {

			if (c == cmd_Next) {
				connection=(StreamConnection)Connections.elementAt(Result_list.getSelectedIndex());
				in=(DataInputStream)input.elementAt(Result_list.getSelectedIndex());
				out=(DataOutputStream)output.elementAt(Result_list.getSelectedIndex());
				new Finalize(midlet,display,connection,in,out);
			} else if (c == cmd_Exit) {
				midlet.notifyDestroyed();
			} else {
				new Setup(midlet,display);
			}

		}
}
