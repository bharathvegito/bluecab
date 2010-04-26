package mainPackage;

import javax.bluetooth.BluetoothStateException;

import BlueCabRequester.CabRequester;
import BlueCabServiceProvider.CabProvider;

/**
 * @author Rohit
 * @description
 */
public class BlueCabManager {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CabRequester cr = null;
		try {
			cr = new CabRequester();
		} catch (BluetoothStateException e) {
			e.printStackTrace();
		}
		cr.createconnection();
		CabProvider cp = null;
		try {
			cp = new CabProvider();
		} catch (BluetoothStateException e) {
			e.printStackTrace();
		}
		cp.acceptconnections();
	}

}
