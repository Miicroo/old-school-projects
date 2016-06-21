package util;

import java.io.Serializable;

/**
 * An object representing a response on input sent to the sever
 */
public class ResponseObject implements Serializable {

	private static final long serialVersionUID = 1448073141773535347L;
	private SendableAction action;
	private Object object = null;
	
	/**
	 * @param a - action that the responobject handles
	 * @param o - object with processed data that is to be returned to the client
	 */
	public ResponseObject(SendableAction a, Object o){
		action = a;
		object = o;
	}
	
	/**
	 * @return the action of the responsobject
	 */
	public SendableAction getAction() {
		return action;
	}
	
	/**
	 * @return the object of processed data
	 */
	public Object getObject() {
		return object;
	}
}