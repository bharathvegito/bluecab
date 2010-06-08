package com.Blue.User;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class UserMidlet extends MIDlet {
	//MIDlet to start User app
	private Display display;
	
	public UserMidlet() {
		// TODO Auto-generated constructor stub
		//Obtaining the current display
		display=Display.getDisplay(this);
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		// TODO Auto-generated method stub
		
	}

	protected void pauseApp() {
		// TODO Auto-generated method stub

	}

	protected void startApp() throws MIDletStateChangeException {
		// TODO Auto-generated method stub
		//Call to discover devices and create connections only with phones running cabbie's app
		new Setup(this,display);
	}

}
