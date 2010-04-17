/**
 * 
 */
package BlueCabRequester;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;



/**
 * @author GAMEBOY
 *
 */
public class CabRequester implements DiscoveryListener {
	
	public CabRequester(){
		
		try {
			LocalDevice localDevice = LocalDevice.getLocalDevice();
			DiscoveryAgent disAgent = localDevice.getDiscoveryAgent();
			disAgent.startInquiry(DiscoveryAgent.GIAC, this);
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
}
