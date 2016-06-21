package model.login;
/**
 * 
 * @author Johns lap
 *	A class that the different serveroperation use tell the logic what happend
 */
public class ServerUpdate {

	public enum Option {
		CONNECT, UNAME_BUSY, BAD_HOST, IOPROBLEM, DISCONNECT, NEW_LIST
	}

	private Server server = null;
	private Option option = null;

	public ServerUpdate(Server s, Option o) {
		this.server = s;
		this.option=o;
	}
	
	public Option getOption() {
		return option;
	}
	public Server getServer() {
		return server;
	}
}
