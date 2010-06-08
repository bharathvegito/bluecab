package com.Blue.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Form;
import javax.microedition.midlet.MIDlet;
//This module does the work of discovering nearby devices,filtering cabbie's devices from them and establishing connections with them
public class Setup implements Runnable, DiscoveryListener {

	private Form displayform;// Form for UI

	private LocalDevice local_device;//To obtain and store host bluetooth device
	private MIDlet midlet;//To store the midlet that called this class
	private Display display;//To store the display to use for UI
	private UUID uuid;//Unique identifier that is used to specify the service required
	private DiscoveryAgent disc_agent;//(JSR-82) DiscoveryAgent to perform device and service search
	private Vector init_devices, final_devices, final_connections,in,out;//Respecive Vectors to store first list of Remotedevices discovered, 
	//Remote device running cabbie's app,connections to the cabbie's device,DataInputStreams and DataOutputStreams for the same 
	private ServiceRecord service;//Object that stores the deatils of the the service required(attr-value list)
	private RemoteDevice selected_Device;
	private String url;//URL used to create a connection
	private StreamConnection connection;//Connection to connect to the remotedevice
	private DataInputStream input;//To receive data from connection
	private DataOutputStream output;//To send data to connection
	private int dec;//For encryption/decryption

	private boolean disc_complete, completed, recd;

	Thread t;

	public Setup(MIDlet midlet, Display display) {

		this.midlet = midlet;
		this.display = display;//Copy midlet and display

		displayform = new Form("Blue-Cab User");//Initialize form

		completed = false;//Initialize boolean variables
		disc_complete = false;
		recd = false;
		
		dec = 0;

		init_devices = new Vector();//Initialize vectors
		final_devices= new Vector();
		final_connections= new Vector();
		in=new Vector();
		out=new Vector();	

		t = new Thread(this);
		t.start();//Thread to establish connections 

		displayform
				.append("Setup is running ...\n\n\nWait for a few minutes...");
		display.setCurrent(displayform);
		while (completed == false) {
			//Loop to hold the next operation until all the devices have been discovered and filtered and connections are created
		}
		//Next module that performs the next operation
		new EntryForm(midlet, display, final_devices, final_connections,in,out);
	}
	
	public void run() {
		try {

			local_device = LocalDevice.getLocalDevice();//Obtaining host device
			disc_agent = local_device.getDiscoveryAgent();//Obtaining a discovery agent for the host device
			local_device.setDiscoverable(DiscoveryAgent.GIAC);//Initializing host device to be discoverable always
			disc_agent.startInquiry(DiscoveryAgent.GIAC, this);//Starting the inquiry for remote devices
			uuid = new UUID(0x0003);//Search for message transfer service(Serial Port)
			while (disc_complete == false) {
				//Hold the next operation to be performed until all devices have been discovered
			}
			for (int i = 0; i < init_devices.size(); i++) {

				selected_Device = (RemoteDevice) init_devices.elementAt(i);//Obtain each discovered device from the vector
				disc_agent.searchServices(new int[] { 0x0100 },
						new UUID[] { uuid }, selected_Device, this);//Search for the required service on the selected remote device
			}
		} catch (BluetoothStateException e) {
			e.printStackTrace();
		} catch (Exception f) {
			f.printStackTrace();
		}
		
	}

	public void sortdevices() {
		try {
			url = service.getConnectionURL(
					ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);//Create URL to obtain a connection, normally present in the servicerecord object 
			connection = (StreamConnection) Connector.open(url);//Create a connection
			output = connection.openDataOutputStream();//Create output stream to send data
			int enc = 647;// Send a key to be modified
			output.writeInt(enc);//Send it
			output.flush();
			
			int c=4;
			do {//Wait till modified key is received
				try {
					input = connection.openDataInputStream();//Create stream to receive data
					dec = input.readInt();//Receive data
					if (dec > 0) {//Check
						if (dec == 1) {
							//If correct (dec Mod 2) add it to a final seperate vector of cabbie's devices,connections and streams
								final_devices.addElement(selected_Device);
								final_connections.addElement(connection);
								in.addElement(input);
								out.addElement(output);
							recd = true;//To exit the loop
							
							dec = 0;
						} else {
							recd=true;
						}
					}
					Thread.sleep(2000);//To wait for some time along with integer c
					c=c-1;
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} while (recd == false&&c>0);
		} catch (Exception e) {
			display.setCurrent(new Alert(
					"Creating a connection was not possible"));
		}
	}

	public void deviceDiscovered(RemoteDevice arg0, DeviceClass arg1) {//Called every time a device is discovered
		// TODO Auto-generated method stub
		init_devices.addElement(arg0);//Add discovered remote device to a vector for sorting later
	}

	public void inquiryCompleted(int arg0) {//Called when all devices have been discovered
		// TODO Auto-generated method stub
		switch (arg0) {
		case INQUIRY_COMPLETED:
			disc_complete = true;//Allowing for further operations and actions in setup constructor 
			break;
		case INQUIRY_TERMINATED:
			display.setCurrent(	new Alert("The search for device was cancelled!"),displayform) ;
			new Setup(midlet, display);//Calling setup to run again
			break;
		case INQUIRY_ERROR:
			display.setCurrent(	new Alert("There was an error on device search!"),displayform);
					new Setup(midlet, display);
			break;
		}
	}

	public void serviceSearchCompleted(int arg0, int arg1) {//Called when all services have been discovered
		// TODO Auto-generated method stub
		switch (arg1) {
		case SERVICE_SEARCH_TERMINATED:
			display.setCurrent(new Alert(
					"The search for services was cancelled!"),displayform); 
					new Setup(	midlet, display);
			break;
		case SERVICE_SEARCH_ERROR:
			display.setCurrent(new Alert("There was an error on service search!"),displayform);
			new Setup(midlet, display);
			break;
		case SERVICE_SEARCH_NO_RECORDS:
			display.setCurrent(new Alert("The remote device isn't running the application!"),displayform);
					new Setup(midlet, display);
			break;
		case SERVICE_SEARCH_DEVICE_NOT_REACHABLE:
			display.setCurrent(new Alert("The remote device isn't reachable!"),displayform);
					new Setup(midlet, display);
			break;
		case SERVICE_SEARCH_COMPLETED:
			sortdevices();//To create a seperate list of cabbie's devices,connections and streams are obtained
			completed = true;//To indicate the cabbie's devices,connections and streams are obtained
					break;
		}
	}

	public void servicesDiscovered(int arg0, ServiceRecord[] arg1) {//Called every time a service is discovered
		// TODO Auto-generated method stub
		service = arg1[0];//Storing the first record that we require in the variable we created
		
	}

}
