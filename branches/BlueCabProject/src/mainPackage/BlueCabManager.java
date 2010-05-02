package mainPackage;

import BlueCabRequester.CabRequester;
import BlueCabServiceProvider.CabProvider;

/**
 * @author Rohit
 */
public class BlueCabManager {

	public static void main(String[] args) {

		CabRequester cr = new CabRequester();
		System.out.println("CabRequestor objecte created");
		cr.createconnection();

		// CabProvider cp = new CabProvider();
		// cp.acceptconnections();

	}
}
