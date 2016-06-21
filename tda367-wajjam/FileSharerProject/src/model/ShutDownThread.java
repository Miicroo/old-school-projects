package model;

import model.login.ServerInOutLogic;
import model.state.ApplicationState;
import model.state.SettingsIO;

/**
 * This thread is to be added as a ShutDownHook and is responsible for the
 * closure and logout of all streams and server connections. Also, it saves
 * the application state until next use.
 * 
 * @author John Johansson
 */
public class ShutDownThread extends Thread{
	
	public ShutDownThread(){
	}

	public void run(){
		ServerInOutLogic.getInstance().logOutAll();
		SettingsIO.save(ApplicationState.getInstance());
	}
}
