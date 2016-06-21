package util;

import java.io.Serializable;

public class ResponseObject implements Serializable {

	private static final long serialVersionUID = 1448073141773535347L;
	private SendableAction action;
	private Object object = null;
	
	public ResponseObject(SendableAction a, Object o){
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