package model;

import java.util.Observable;
import java.util.Observer;

import controller.NetworkController;

import util.ResponseObject;
import util.SendObject;
import util.SendableAction;

public class LoginModel extends Observable implements IModel, Observer {

	public static final int LOGIN_SIGN_UP = -1;
	public static final int LOGIN_NOT_OK = 0;
	public static final int LOGIN_OK = 1;

	private NetworkController cc;

	private String uname = "";

	public LoginModel() {
		cc = NetworkController.getInstance("");
		cc.addObserver(this);
	}

	/**
	 * Sends a request to the server that "username" wants to login
	 */
	public void sendLoginRequest(String username) {
		uname = username;
		SendObject object = new SendObject(SendableAction.LOGIN, username);
		cc.send(object);
	}

	/**
	 * Evaluates the answer from the server and returns an int that is
	 * LoginModel#LOGIN_SIGN_UP, LoginModel#LOGIN_NOT_OK, LoginModel#LOGIN_OK or
	 * an error.
	 */
	private int evaluate(ResponseObject obj, String username) {

		String[] possibleOutcome = {
				"The requested username " + username
						+ " does not exist. Please sign up!",
				username + " is already logged in on another device",
				"You are now logged in as " + username };

		for (int i = 0; i < possibleOutcome.length; i++) {
			if (obj.getObject().toString().equalsIgnoreCase(possibleOutcome[i])) {
				return (i - 1);
			}
		}
		return (LOGIN_OK + 1); // error
	}

	@Override
	public void update(Observable observable, Object data) {
		ResponseObject r = ((ResponseObject) data);
		Integer i = evaluate(r, uname);
		if (r.getAction() == SendableAction.LOGIN) {
			setChanged();
			notifyObservers(i);
		}
	}
}