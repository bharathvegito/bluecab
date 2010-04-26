package BlueCabServiceProvider;

import java.io.DataInputStream;
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
import javax.microedition.io.StreamConnectionNotifier;

/**
 * @author Sharath
 * @description
 */
public class CabProvider implements DiscoveryListener {
	UUID uuid = new UUID(0x0003);
	// Use RFCOM protocol
	StreamConnectionNotifier connection;

	public CabProvider() throws BluetoothStateException {
		LocalDevice localDevice = LocalDevice.getLocalDevice();
		// Local device object for localhost
		if (localDevice == null) {
			System.out.println("No local Device Found");
			System.exit(1);
		}
		System.out
				.println("Local Device found" + localDevice.getFriendlyName());

		localDevice.setDiscoverable(DiscoveryAgent.GIAC);
		// Set device to be visible to all
	}

	public void deviceDiscovered(RemoteDevice arg0, DeviceClass arg1) {
		// TODO Auto-generated method stub

	}

	public void inquiryCompleted(int arg0) {
		// TODO Auto-generated method stub

	}

	public void serviceSearchCompleted(int arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	public void servicesDiscovered(int arg0, ServiceRecord[] arg1) {
		// TODO Auto-generated method stub

	}

	public void acceptconnections() {
		try {
			connection = (StreamConnectionNotifier) Connector
					.open("btspp://localhost:" + uuid
							+ ";name=CabProvider;authorize=false");
			System.out.println("URL resolved");
			StreamConnection connToRequestor = connection.acceptAndOpen();
			System.out.println("Connected to " + connToRequestor.toString());
			DataInputStream recvMsg = connToRequestor.openDataInputStream();
			System.out.println("Message recieved : " + recvMsg.readUTF());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
