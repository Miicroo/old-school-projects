package util;

import java.io.Serializable;

/**
 * An object that is sent from the client to the server.
 * The SendObject contains a SendableAction which tells the server what to do,
 * and an Object which contains the data that is required to process the action.
 */
public class SendObject implements Serializable {

	private static final long serialVersionUID = 8158559495390915513L;
	private SendableAction action;
	private Object object = null;
	
	public SendObject(SendableAction a, Object o){
		action = a;
		object = o;
	}
	
	public SendableAction getAction() {
		return action;
	}
	
	public Object getObject() {
		return object;
	}
}