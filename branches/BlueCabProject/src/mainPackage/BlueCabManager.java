package mainPackage;

import BlueCabRequester.CabRequester;
import BlueCabServiceProvider.CabProvider;

/**
 * @author Rohit
 * @description
 */
public class BlueCabManager {

	public static void main(String[] args) {

		CabRequester cr = new CabRequester();
		cr.createconnection();

		CabProvider cp = new CabProvider();
		cp.acceptconnections();

	}
}
