package util;

import java.io.Serializable;

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