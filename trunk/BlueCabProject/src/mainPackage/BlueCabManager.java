package mainPackage;

import BlueCabRequester.CabRequester;
import BlueCabServiceProvider.CabProvider;

/**
 * 
 */

/**
 * @author Rohit
 *
 */
public class BlueCabManager {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CabRequester cr=new CabRequester();
		cr.createconnection();
		CabProvider cp=new CabProvider();
		cp.acceptconnections();
	}

}
