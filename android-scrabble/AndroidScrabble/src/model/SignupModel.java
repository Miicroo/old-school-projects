package model;

import java.util.Observable;
import java.util.Observer;

import android.util.Log;

import controller.NetworkController;

import util.ResponseObject;
import util.SendObject;
import util.SendableAction;

public class SignupModel extends Observable implements IModel, Observer {

	public static final int SIGNUP_NOT_OK = 0;
	public static final int SIGNUP_OK = 1;

	private NetworkController cc;
	public String uname = "";

	public SignupModel() {
		cc = NetworkController.getInstance("");
		cc.addObserver(this);
	}

	/**
	 * Sends a request to the server that "username" wants to signup
	 */
	public void sendLoginRequest(String username) {
		uname = username;
		SendObject object = new SendObject(SendableAction.SIGN_UP, username);
		cc.send(object);
	}

	/**
	 * Evaluates the answer from the server and returns an int that is
	 * SignupModel#SIGNUP_NOT_OK, SignupModel#SIGNUP_OK or an error.
	 */
	private int evaluate(ResponseObject obj, String username) {
		String[] possibleOutcome = {
				"Sorry, the username " + username
						+ " is already taken. Please choose another one.",
				"You are now signed up, welcome " + username };

		for (int i = 0; i < possibleOutcome.length; i++) {
			if (obj.getObject().toString().equalsIgnoreCase(possibleOutcome[i])) {
				return i;
			}
		}

		return (SIGNUP_OK + 1); // error
	}

	@Override
	public void update(Observable observable, Object data) {
		ResponseObject r = ((ResponseObject) data);
		Log.d("username", r.getObject().toString());
		
		Integer i = evaluate(r, uname);
		if (r.getAction() == SendableAction.SIGN_UP) {
			setChanged();
			notifyObservers(i);
		}
	}
}