package BlueCabRequester;

import java.io.DataOutputStream;
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
 */
public class CabRequester implements DiscoveryListener {
	private RemoteDevice remoteDevice = null;
	private ServiceRecord service = null;
	private LocalDevice localDevice = null;
	private boolean raceControlFlag = false;

	public void deviceDiscovered(RemoteDevice rdDiscovered, DeviceClass arg1) {
		remoteDevice = rdDiscovered;
		System.out
				.println("Remote device discovered" + rdDiscovered.toString());
		// + rdDiscovered.getFriendlyName(true));
	}

	public void inquiryCompleted(int inqueryStatus) {
		switch (inqueryStatus) {
		case INQUIRY_COMPLETED:
			System.out.println("Device Inquery Completed");
			if (remoteDevice != null)
				raceControlFlag = true;
			else
				System.out.println("No device was found in the inquery");
			break;
		case INQUIRY_ERROR:
			System.out.println(" Device Inquery Error");
			System.exit(INQUIRY_ERROR);
			break;
		case INQUIRY_TERMINATED:
			System.out.println("Device Enquiry cancelled by user");
			System.exit(INQUIRY_TERMINATED);
			break;
		default:
			if (remoteDevice == null)
				System.out.println("Unknown Error");
			break;
		}
	}

	public void servicesDiscovered(int arg0, ServiceRecord[] servicesArray) {
		service = servicesArray[0];
		System.out.println("Services found" + service.toString());
	}

	public void serviceSearchCompleted(int transID, int responseCode) {
		switch (responseCode) {
		case SERVICE_SEARCH_COMPLETED:
			System.out.println("Servie Search Completed");
			if (service != null)
				raceControlFlag = true;
			else
				System.out.println("No service was found in the search");
			break;
		case SERVICE_SEARCH_ERROR:
			System.out.println("Could not do service search");
			System.exit(SERVICE_SEARCH_ERROR);
			break;
		case SERVICE_SEARCH_TERMINATED:
			System.out.println("Service enquiry cancelled by user");
			System.exit(SERVICE_SEARCH_TERMINATED);
			break;
		}
	}

	public CabRequester() {

		try {
			if (LocalDevice.isPowerOn() == false) {
				System.out.println("No local Bluetooth Device Found");
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
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void createconnection() {
		try {
			String url = service.getConnectionURL(
					ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
			// get the url from service retrieved
			// address, port of the remote device in the url
			System.out.println("URL broadcasted" + url.toString());
			StreamConnection connection = (StreamConnection) Connector
					.open(url);
			// Open a connection using the url
			System.out.println("connection to url eshtablished"
					+ connection.toString());
			DataOutputStream output = connection.openDataOutputStream();
			System.out.println("Stream object created" + output.toString());
			output.writeUTF("Hai .....I am sending a message");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void customerContractSetup() {
		// TODO: Message passing to write
		// to check it out : Do we have to call setDiscoverable() for cab
		// Requester
	}

}
