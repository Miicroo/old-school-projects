package model;

import java.util.Observable;
import java.util.Observer;

import controller.NetworkController;

import util.ResponseObject;
import util.SendObject;
import util.SendableAction;

public class LogoutModel extends Observable implements IModel, Observer {

	private NetworkController cc;

	public LogoutModel() {
		cc = NetworkController.getInstance("");
		cc.addObserver(this);
	}

	/**
	 * Sends a request to the server that "username" wants to logout
	 */
	public void sendLogoutRequest(String username) {
		SendObject object = new SendObject(SendableAction.LOGOUT, username);
		cc.send(object);
	}

	@Override
	public void update(Observable observable, Object data) {
		// cc.deleteObserver(this);
		String s = ((ResponseObject) data).getObject().toString();
		if (((ResponseObject) data).getAction() == SendableAction.LOGOUT) {
			setChanged();
			notifyObservers(s);
		}
	}
}