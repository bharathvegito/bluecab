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
		CabRequester cr = new CabRequester();
		cr.createconnection();
				
	}
}
