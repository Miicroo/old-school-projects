package org.wajjam.project.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import User.User;
/**
 * 
 * @author Johns lap
 *	Changes the name on the server who request it
 */
public class UserNameThread implements Runnable{
	
	private Thread thread = null;
	private Socket sk = null;
	private ObjectInputStream in = null;
	public UserNameThread(Socket sk,ObjectInputStream in) {
		this.sk=sk;
		this.in=in;
		thread= new Thread(this);
		thread.start();
		
	}

	@Override
	public void run() {

		User user = null;
		try {
			user = (User)in.readUnshared();
			for(int i=0;i<UserList.getInstance().getUserList().size();i++){
				if(UserList.getInstance().getUserList().get(i).equals(user)){
					UserList.getInstance().getUserList().remove(i);
					UserList.getInstance().getUserList().add(i, user);
					UserList.getInstance().nameChanged();
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			sk.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}

}
