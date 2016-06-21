package model.data;

import model.login.Server;
/**
 * 
 * @author Johns lap
 *	Object that contains a string and a server
 */
public class ChatObject {
	
	private Server server;
	private String message;
	
	public ChatObject(Server s, String m) {
		server = s;
		message = m;
	}
	
	public String getMessage() {
		return message;
	}
	
	public Server getServer() {
		return server;
	}
}
