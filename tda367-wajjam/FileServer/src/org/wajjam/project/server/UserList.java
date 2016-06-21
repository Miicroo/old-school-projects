package org.wajjam.project.server;

import java.util.*;
import User.User;
/**
 * 
 * @author Johns lap
 *	An observable list that notify when a user connect or disconnect
 */
public class UserList extends Observable{
	
	private List<User> userList = null ;
	private static UserList ul = null;

	private UserList(){	
		userList = new LinkedList<User>();
	}
	
	public void nameChanged(){
		setChanged();
		notifyObservers();
	}


	public void addUser(User user){
		userList.add(user);	
		setChanged();
		notifyObservers();
	}
	
	public List<User> getUserList() {
		return userList;
	}
	
	public void removeUser(User user){
		userList.remove(user);
		setChanged();
		notifyObservers();
	}
	
	public void updateList(){
		setChanged();
		notifyObservers();
	}
	
	public static UserList getInstance() {
	  		if(ul == null) {
				ul = new UserList();
			}
			return ul;
	}
	
}
