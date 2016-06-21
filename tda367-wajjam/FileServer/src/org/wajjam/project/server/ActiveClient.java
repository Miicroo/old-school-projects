package org.wajjam.project.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.*;

import User.User;

/**
 * 
 * @author Johns lap
 * ActiveClient has all comunication with the conneted user
 */
public class ActiveClient implements Runnable,Observer{

	private Socket socket;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;
	private Thread thread=null;
	private UserList userList = null;
	public ActiveClient(Socket s,ObjectInputStream instream)  {
		socket = s;	
		in = instream;
		UserList.getInstance().addObserver(this);
		thread=new Thread(this);
		thread.start();
	}

	@SuppressWarnings("static-access")
	@Override
	public void run() {
		boolean loop=true;
		
		User user = null;
		try{
			out = new ObjectOutputStream(socket.getOutputStream());	
			userList = UserList.getInstance();
			user = (User) in.readUnshared();
			if(userList.getUserList().contains(user)){
				userList.updateList();
				try {
					thread.sleep(2000);
				} catch (InterruptedException e) {}
				for(User u : userList.getUserList()){
					if(u.equalsUserName(user)){
						out.writeUnshared(new String("busy"));
						loop=false;
						break;
					}
				}
			}
			if(loop){
				userList.addUser(user);	
			}
			while(userList.getUserList().contains(user)&&loop) {
					try{
						thread.sleep(Long.MAX_VALUE);
					} catch (InterruptedException e) {}
					out.writeUnshared(UserList.getInstance().getUserList());
			}
		} catch (IOException e) {
			userList.removeUser(user);
		}
		catch (ClassNotFoundException e) {
		}
		finally{
			
			try {
				socket.close();
			} catch (IOException e1) {}
			thread=null;
		}
	}
	
	public boolean isActive(){
		try {
			out.writeUnshared(userList.getUserList());
		} catch (IOException e) {return false;}
		return true;
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		if(thread!=null){
			thread.interrupt();
		}else{
			UserList.getInstance().deleteObserver(this);
		}
	}
}