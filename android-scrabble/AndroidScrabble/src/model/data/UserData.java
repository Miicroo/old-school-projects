package model.data;

import android.content.Context;

import model.InvitationModel;

/**
 * Singleton that keep tracks of username and invitationthread.
 */
public class UserData {

	private static UserData userData = null;
	private InvitationModel invModel = null;
	private boolean isInit = false;

	private String username = "";
	
	private UserData(){
	}
	
	public static UserData getInstance(){
		if(userData == null){
			userData = new UserData();
		}
		return userData;
	}
	
	/**
	 * Initiate the socket so the server knows that the user is online
	 */
	public void init(Context c, String serverIp){
		isInit = true;
		
		//	create model
		invModel = new InvitationModel(c, serverIp);
		invModel.start();	// start waiting for game requests
	}

	/*
	 * Close the socket so the server knows that the user is offline
	 */
	public void killSocket(){
		if(invModel != null){
			invModel.killSocket();
			invModel = null;
		}
	}

	public void setUsername(String uName) {
		username = uName;
	}

	public String getUsername() {
		return username;
	}

	public boolean isIntantiated() {
		return isInit;
	}
}