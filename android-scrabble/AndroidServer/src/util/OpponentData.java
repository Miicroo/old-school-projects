package util;

import java.io.Serializable;

/**
 * Class that represents OpponentData, with username and boolean if the user is online.
 */
public class OpponentData implements Serializable{

	private static final long serialVersionUID = 2167462336411984335L;
	
	private String username;
	private boolean isOnline;
	
	public OpponentData(String username, boolean isOnline){
		this.username = username;
		this.isOnline = isOnline;
	}

	public String getUsername() {
		return username;
	}

	public boolean isOnline() {
		return isOnline;
	}
}