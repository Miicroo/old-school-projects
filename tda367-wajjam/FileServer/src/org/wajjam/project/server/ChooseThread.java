package org.wajjam.project.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import User.ServerUser;
import User.User;

/**
 * 
 * @author Johns lap
 *	Check what thread the server shall start depending on what object comes in first
 */
public class ChooseThread implements Runnable{
	private Socket sk = null;
	public ChooseThread(Socket sk){
		this.sk=sk;
		new Thread(this).start();
	}

	@Override
	public void run() {
		try {
			ObjectInputStream in = new ObjectInputStream(sk.getInputStream());
			ServerUser temp = (ServerUser)in.readUnshared();
			if(temp == ServerUser.ADD){			
				new ActiveClient(sk,in);
			}else if(temp == ServerUser.REMOVE){
				User user = (User)in.readUnshared();
				UserList.getInstance().removeUser(user);
			}else if(temp == ServerUser.CHAT){
				new ChatThread(sk,in);
			}else if(temp == ServerUser.CHANGE_USERNAME){
				new UserNameThread(sk,in);
			}
		} catch (IOException e) {e.printStackTrace();} 
		catch (ClassNotFoundException e) {e.printStackTrace();}		
	}
}
