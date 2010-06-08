package com.Blue.Provider;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.microedition.io.StreamConnection;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.midlet.MIDlet;
//Displaylist of request/orders received from users , selects one and
public class DisplayList extends Form implements CommandListener{

	private MIDlet midlet;
	private Display display;
	private Vector Connections,in,out;

	// private Form DisplayList;
	private ChoiceGroup customer_list;
	private Command cmd_Next, cmd_Restart, cmd_Exit;
	private String data = new String();
	/*private String destination, pickup_point, phone_num, parser*/
	private boolean recd,finished;
	private int index;
	private StreamConnection connection;
	private DataInputStream input;
	private DataOutputStream output;
	
	public void receive() {
		
		try {
			DataInputStream receive_response = (DataInputStream) in.elementAt(index);
			data = receive_response.readUTF();
			//data.trim();
			while (recd = false) {				
				if (data.length()>12) {
						recd = true;
				} else {
											
					}
				}
			}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void generateList() {
		for (int i = 0; i < in.size(); i++) 
		 {
			index=i;
			recd=false;
			receive();
		}
		if (index == in.size()) {
			finished = true;
		}
		for(int j = 0; j < in.size(); j++){
			customer_list.append("Customer "+(j+1)+" : "+data,null);			
		}
	}

	public DisplayList(MIDlet midlet, Display display, Vector Connections, Vector in, Vector out) {
		super("");

		this.midlet = midlet;
		this.display = display;

		this.Connections = Connections;
		this.in=in;
		this.out=out;
		
		customer_list = new ChoiceGroup("Customer List:", ChoiceGroup.EXCLUSIVE);

		recd = false;
				
		/*destination = new String();
		pickup_point = new String();
		phone_num = new String();*/

		cmd_Next = new Command("Next", Command.OK, 0);
		cmd_Restart = new Command("Restart", Command.BACK, 0);
		cmd_Exit = new Command("Exit", Command.EXIT, 0);

		this.addCommand(cmd_Next);
		this.addCommand(cmd_Restart);
		this.addCommand(cmd_Exit);

		setCommandListener(this);
		display.setCurrent(this);
		
		generateList();
		
		this.append(customer_list);
		display.setCurrent(this);
		
		while(finished==false&&customer_list.getSelectedIndex()+1>0)
		{
			//Loop until all the connections have delivered the params and one is selected
		}
		
		
	}
	public void negativeAck()//Send negative acknowledgment to others i.e no msg
	{
		for(int i=0;i<in.size();i++)
		{
			if(i==customer_list.getSelectedIndex())
			{
				
			}
			else
			{
				DataOutputStream negative_out = (DataOutputStream) out.elementAt(i);
				try {
					negative_out.writeInt(0);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void commandAction(Command c, Displayable d) {

		if (c == cmd_Next) {
			connection=(StreamConnection) Connections.elementAt(customer_list.getSelectedIndex());
			input=(DataInputStream) in.elementAt(customer_list.getSelectedIndex());
			output=(DataOutputStream) out.elementAt(customer_list.getSelectedIndex());
			negativeAck();
			new AcknowledgeForm(midlet, display, connection,input,output);
		} else if (c == cmd_Exit) {
			midlet.notifyDestroyed();
		} else {
			new Setup(midlet, display);
		}

	}

}
