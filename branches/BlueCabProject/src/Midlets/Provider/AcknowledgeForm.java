package com.Blue.Provider;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.io.StreamConnection;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.midlet.MIDlet;
//Send confirmation to the selected cabbie
public class AcknowledgeForm  extends Form implements CommandListener{

		private MIDlet midlet;
		private Display display;
		
		private StreamConnection connection;
		private DataInputStream input;
		private DataOutputStream output;
		private ChoiceGroup confirmation;
		private Command cmd_Next, cmd_Restart, cmd_Exit;
		
		public void sendconfirmation() {
			if(confirmation.getSelectedIndex()==0)
			{
				try {
					output.writeInt(1);
					output.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			else
			{
				try {
					output.writeInt(0);
					output.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}

		public AcknowledgeForm(MIDlet midlet, Display display, StreamConnection connection,DataInputStream input,DataOutputStream output) {
			super("");

			this.midlet = midlet;
			this.display = display;

			this.connection = connection;
			this.input=input;
			this.output=output;
			
			confirmation = new ChoiceGroup("Confirm Request : ",ChoiceGroup.EXCLUSIVE);
			confirmation.append("Yes", null);
			confirmation.append("No", null);
			
			this.append(confirmation);
		
			cmd_Next = new Command("Next", Command.OK, 0);
			cmd_Restart = new Command("Restart", Command.BACK, 0);
			cmd_Exit = new Command("Exit", Command.EXIT, 0);

			this.addCommand(cmd_Next);
			this.addCommand(cmd_Restart);
			this.addCommand(cmd_Exit);

			setCommandListener(this);
			display.setCurrent(this);
					
		}

		public void commandAction(Command c, Displayable d) {

			if (c == cmd_Next) {
				sendconfirmation();
				new Finalize(midlet,display,connection,input,output);
			} else if (c == cmd_Exit) {
				midlet.notifyDestroyed();
			} else {
				new Setup(midlet,display);
			}

		}


}

