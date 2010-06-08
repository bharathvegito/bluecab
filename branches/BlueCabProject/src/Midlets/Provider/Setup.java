package com.Blue.Provider;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.midlet.MIDlet;
//To accept incoming connections and to reply to user's app and store the connections received 
public class Setup implements Runnable, CommandListener {

	private Form displayform;
	
	private LocalDevice local_device;
	private MIDlet midlet;
	private Display display;
	private UUID uuid;
	private Vector init_connections, final_connections, inputs, final_in,
			final_out;
	private StreamConnectionNotifier connection;
	private StreamConnection connections;
	private DataInputStream input, in;
	private DataOutputStream output;
	private int dec, enc;
	private int num;
	private ChoiceGroup num_clients;
	private Command cmd_ok, cmd_exit;

	private boolean completed, flagged, ok;

	Thread t;

	public void sortconnections(Vector init_connections, Vector inputs) {

		int i;
		for ( i = 0; i < init_connections.size(); i+=1) {
			
			try {
				in = (DataInputStream) inputs.elementAt(i);
				StreamConnection Connection = (StreamConnection) init_connections
						.elementAt(i);
				output = Connection.openDataOutputStream();
				if ((dec = in.readInt()) == 647) {
					final_connections.addElement(Connection);
					final_in.addElement(in);
					final_out.addElement(output);
					enc = dec % 2;//Modify and send the key back
					output.writeInt(enc);
				} else {
					//return;
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}			
			if (i == init_connections.size()){//Check if all connections have been replied to which send key
				completed = true;
			}
	}

	public void run() {
		num = num_clients.getSelectedIndex() + 1;
		try {
			local_device = LocalDevice.getLocalDevice();
			local_device.setDiscoverable(DiscoveryAgent.GIAC);
			connection = (StreamConnectionNotifier) Connector
					.open("btspp://localhost:" + uuid
							+ ";name=Bluetooth Server;authorize=false");
			for (int i = 0; i < num; i++) {

				connections = connection.acceptAndOpen();//Listen for incoming connections
				init_connections.addElement(connections);
				input = connections.openDataInputStream();
				inputs.addElement(input);
			}
			sortconnections(init_connections, inputs);//Sort the connections received from that of cabbie's app
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Setup(MIDlet midlet, Display display) {

		this.midlet = midlet;
		this.display = display;

		uuid = new UUID(0x0009);

		displayform = new Form("Blue-Cab Provider");

		completed = false;
		flagged = false;
		ok=false;
		dec = 0;

		num_clients = new ChoiceGroup("Select no of Connections : ",//Ask to user to select maximum number of request(connections in turn he would like to receive
				ChoiceGroup.EXCLUSIVE);
		for (int i = 1; i < 6; i++)
			num_clients.append("" + i, null);
		displayform.append(num_clients);

		cmd_ok = new Command("ok", Command.OK, 1);
		cmd_exit = new Command("exit", Command.EXIT, 1);

		displayform.addCommand(cmd_ok);
		displayform.addCommand(cmd_exit);
		displayform.setCommandListener(this);

		display.setCurrent(displayform);

		final_connections = new Vector();
		final_in = new Vector();
		final_out = new Vector();
		init_connections = new Vector();
		inputs = new Vector();
		
		while(ok==false)//Wait until user has selected one(command action sets ok to true
		{
			
		}
		t = new Thread(this);
		t.start();//Thread to receive connections
		while (completed == false) {

		}
		new DisplayList(midlet, display, final_connections, final_in, final_out);
	}

	public void commandAction(Command c, Displayable arg1) {
		// TODO Auto-generated method stub
		if (c == cmd_ok && flagged == false) {
			displayform.deleteAll();
			displayform
					.append("Setup is running ...\n\n\nWait for a few minutes...");
			display.setCurrent(displayform);
			ok=true;
		} else if (c == cmd_exit) {
			midlet.notifyDestroyed();
		} else {

		}
	}

}
