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
 */
public class CabProvider implements DiscoveryListener {
	private UUID uuid = new UUID(0x1101);
	// Use RFCOM protocol
	StreamConnectionNotifier connection;
	LocalDevice localDevice = null;
	String baddr;

	public CabProvider() {
		try {
			localDevice = LocalDevice.getLocalDevice();
			// Local device object for localhost

			System.out.println("Local Device found" + localDevice.toString()
					+ localDevice.getFriendlyName());

			localDevice.setDiscoverable(DiscoveryAgent.GIAC);
			// Set device to be visible to all}
			baddr = localDevice.getBluetoothAddress();
			System.out.println("Host device addr: " + baddr);
			System.out.println("Setting Discoverable Flag...Done");
		} catch (BluetoothStateException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void deviceDiscovered(RemoteDevice arg0, DeviceClass arg1) {
	}

	public void inquiryCompleted(int arg0) {
	}

	public void serviceSearchCompleted(int arg0, int arg1) {
	}

	public void servicesDiscovered(int arg0, ServiceRecord[] arg1) {
	}

	public void acceptconnections() {
		try {
			String url = "btspp://baddr:" + uuid
					+ ";name=CabProvider;authorize=false";
			connection = (StreamConnectionNotifier) Connector
					.open("btspp://baddr:" + uuid
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
