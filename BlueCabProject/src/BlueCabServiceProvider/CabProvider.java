/**
 * 
 */
package BlueCabServiceProvider;

import java.io.DataInputStream;

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
 * @author GAMEBOY
 *
 */
public class CabProvider implements DiscoveryListener {
	
	UUID uuid = new UUID(0x0009);
	StreamConnectionNotifier connection; 

	public CabProvider() {

		try {
			LocalDevice localDevice = LocalDevice.getLocalDevice();
			localDevice.setDiscoverable(DiscoveryAgent.GIAC);
		} catch (BluetoothStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
	public void acceptconnections()
	{
		try{
			connection = (StreamConnectionNotifier)Connector.open("btspp://localhost:"+uuid+";name=CabProvider;authorize=false");
			StreamConnection conn = connection.acceptAndOpen();
			DataInputStream in = conn.openDataInputStream();
		}
		catch(Exception e)
		{
				e.printStackTrace();
		}
	}
	
}
