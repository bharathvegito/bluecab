/**
 * 
 */
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
 * @author GAMEBOY
 *
 */
public class CabRequester implements DiscoveryListener {
	private RemoteDevice rd;
	private ServiceRecord service;
	
	public CabRequester(){
		
		try {
			LocalDevice localDevice = LocalDevice.getLocalDevice();
			DiscoveryAgent disAgent = localDevice.getDiscoveryAgent();
			disAgent.startInquiry(DiscoveryAgent.GIAC, this);
			UUID uuid = new UUID(0x0003);
			disAgent.searchServices(new int[]{0x0100},new UUID[]{uuid},rd,this);
			if(localDevice==null)
				System.out.println("No local Device Found");
			else{
				System.out.println("Local Device found"+localDevice.getFriendlyName());
				}
		} catch (BluetoothStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void deviceDiscovered(RemoteDevice arg0, DeviceClass arg1) {
		// TODO Auto-generated method stub
		rd=arg0;
	}

	public void inquiryCompleted(int arg0) {
		// TODO Auto-generated method stub
		System.out.println("All nearby devices has been detected");
	}

	public void serviceSearchCompleted(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
		System.out.println("All services has been detected");
	}

	public void servicesDiscovered(int arg0, ServiceRecord[] arg1) {
		// TODO Auto-generated method stub
		service = arg1[0];
	}
	public void createconnection()
	{
		try 
		{
			String url = service.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
			StreamConnection connection = (StreamConnection) Connector.open(url);
			DataOutputStream output = connection.openDataOutputStream();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
