package BlueCabRequester;

import java.io.DataOutputStream;
import java.io.IOException;

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

/**
 * @author Rohit
 * @description
 */
public class CabRequester implements DiscoveryListener {
	private RemoteDevice remoteDevice;
	private ServiceRecord service;
	private boolean raceControlFlag = false;

	public void deviceDiscovered(RemoteDevice rdDiscovered, DeviceClass arg1) {
		// try {
		System.out.println("Device discovered called");
		remoteDevice = rdDiscovered;
		System.out
				.println("Remote device discovered" + rdDiscovered.toString());
		// + rdDiscovered.getFriendlyName(true));
		/*
		 * } catch (IOException e) { e.printStackTrace(); }
		 */
		remoteDevice = rdDiscovered;
	}

	public void inquiryCompleted(int inqueryStatus) {
		switch (inqueryStatus) {
		case INQUIRY_COMPLETED:
			System.out.println("All nearby devices has been detected");
			raceControlFlag = true;
			break;
		case INQUIRY_ERROR:
			System.out.println("Remote Device not found");
			System.exit(INQUIRY_ERROR);
			break;
		case INQUIRY_TERMINATED:
			System.out.println("Enquiry cancelled by user");
			System.exit(INQUIRY_TERMINATED);
			break;
		default:
			if (remoteDevice == null)
				System.out.println("Unknown Error");
			break;
		}
	}

	public void servicesDiscovered(int arg0, ServiceRecord[] arg1) {
		System.out.println("Services found");
		service = arg1[0];
		System.out.println("went to servicesDiscovered");
	}

	public void serviceSearchCompleted(int transID, int responseCode) {
		switch (responseCode) {
		case SERVICE_SEARCH_COMPLETED:
			System.out
					.println("All nearby servies offered by the remote device has been detected");
			raceControlFlag = true;
			break;
		case SERVICE_SEARCH_ERROR:
			System.out.println("Could not get services offered");
			System.exit(SERVICE_SEARCH_ERROR);
			break;
		case SERVICE_SEARCH_TERMINATED:
			System.out.println("Service enquiry cancelled by user");
			System.exit(SERVICE_SEARCH_TERMINATED);
			break;
		}
	}

	public CabRequester() {

		LocalDevice localDevice = null;
		try {
			if (LocalDevice.isPowerOn() == false) {
				System.out.println("No local Device Found");
				System.exit(1);
			}
			localDevice = LocalDevice.getLocalDevice();
			// Local device object for localhost
			System.out.println("Local Device found" + localDevice.toString()
					+ localDevice.getFriendlyName());
			localDevice.setDiscoverable(DiscoveryAgent.GIAC);
			DiscoveryAgent disAgent = localDevice.getDiscoveryAgent();
			System.out.println("DiscoveryAgent obtained :"
					+ disAgent.toString());
			// object that does services search after getting a device
			// For each device found get all its services providable by it
			disAgent.startInquiry(DiscoveryAgent.GIAC, this);
			System.out.println("Device Inquiry started");
			// start enquiry for device(s)
			UUID uuid = new UUID(0x0003);
			// uuid is array for set of protocols, we need only RFCOM for
			// passing msges
			System.out.println("UUID Initialized: " + uuid.toString());

			while (raceControlFlag == false)
				Thread.sleep(5000);

			disAgent.searchServices(new int[] { 0x0100 }, new UUID[] { uuid },
					remoteDevice, this);
			// Search for the service offered by the remote device
			System.out.println("Service search started");
			raceControlFlag = false;

			while (raceControlFlag == false)
				Thread.sleep(5000);

		} catch (BluetoothStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void createconnection() {
		try {
			String url = service.getConnectionURL(
					ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
			// get the url from service retrieved
			// address, port of the remote device in the url
			StreamConnection connection = (StreamConnection) Connector
					.open(url);
			// Open a connection using the url
			DataOutputStream output = connection.openDataOutputStream();
			output.writeUTF("Hai .....I am sending a message");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void customerContractSetup() {
		// TODO: Message passing to write
		// to check it out : Do we have to call setDiscoverable() for cab
		// requestor
	}

}
