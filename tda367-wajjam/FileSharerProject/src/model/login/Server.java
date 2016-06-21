package model.login;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import User.User;

/**
 * 
 * @author Johns lap
 * A class that contains all information for a server
 */
public class Server implements Serializable {

	private static final long serialVersionUID = -5691684899176047390L;
	private String host = "";
	private int port = 0;
	private boolean connected = false;
	private List<User> ulist = new LinkedList<User>();

	/**
	 * 
	 * @param host
	 * @param port
	 * 
	 */
	public Server(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
		if (!connected) {
			ulist = new LinkedList<User>();
		}
	}

	public boolean isConnected() {
		return connected;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public void setUlist(List<User> ulist) {
		this.ulist = ulist;
	}

	public List<User> getUlist() {
		return ulist;
	}

	public User[] getUserArray() {
		
		return (User[])(ulist.toArray(new User[0]));
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Server)) {
			return false;
		}
		Server s = (Server) obj;
		if (!s.getHost().equals(this.getHost())) {
			return false;
		} else if (s.getPort() != this.getPort()) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		return host.hashCode() + port;
	}

	@Override
	public String toString() {
		return "IP: " + getHost() + " Port: " + getPort();
	}
}