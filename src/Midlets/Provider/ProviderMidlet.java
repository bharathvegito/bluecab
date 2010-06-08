package com.Blue.Provider;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
//Midlet to start cabbie's app
public class ProviderMidlet extends MIDlet {
	
	private Display display;
	
	public ProviderMidlet() {
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
		//Call to accept connections only with phones running user's app
		new Setup(this,display);
	}

}