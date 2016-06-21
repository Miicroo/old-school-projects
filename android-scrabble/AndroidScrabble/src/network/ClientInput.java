package network;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;

import android.util.Log;

import util.ResponseObject;
import controller.NetworkController;

/**
 * Class that listens for input from the server.
 */
public class ClientInput extends Thread implements Runnable{

	private NetworkController c = null;
	private ObjectInputStream in = null;
	
	public ClientInput(NetworkController c) {
		this.c = c;
		try {
			in = new ObjectInputStream(c.getSocket().getInputStream());
		} catch (StreamCorruptedException e) {
			Log.d("alpha", "Stream corrupted in input", e);
		} catch (IOException e) {
			Log.d("alpha", "IOE", e);
		}
	}
	
	public void run(){
		Object o = null;
		while(true){
	        try{
	            while((o = in.readUnshared()) != null){
					if(o.getClass().equals(ResponseObject.class)){
						c.redirect(((ResponseObject)o));
					}
	            }
	        }
	        catch(ClassNotFoundException e){
	        	e.printStackTrace();
	        }
	        catch (IOException e){
	        	//	We arrive here when the socket is closed.
	            break;
	        }
	    }
	}
}