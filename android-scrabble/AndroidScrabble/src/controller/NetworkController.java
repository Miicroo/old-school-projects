package controller;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;

import android.util.Log;

import util.ResponseObject;
import util.SendObject;

import network.ClientOutput;

/**
 * Class that represents a "Server".
 * 
 * The controller takes care of in and outgoing objects and redirects them to the right place.
 * 
 */
public class NetworkController extends Observable{

	private static NetworkController controller = null;
	private ClientOutput co = null;
	private Socket s = null;
	private int port = 7896;
	
	private NetworkController(String serverip){
		try {
			s = new Socket(serverip, port);
			Log.e("serverip=", serverip == null ? "null" : serverip);
		}
		catch(UnknownHostException e){
			Log.e("socket e", e.getMessage());
		}
		catch(IOException e){
			Log.e("socket e", e.getMessage());
		}
		co = new ClientOutput(s);
	}

	public static NetworkController getInstance(String ip){
		if(controller == null){
			controller = new NetworkController(ip);
		}
		return controller;
	}
	
	public void redirect(ResponseObject so){
		setChanged();
		notifyObservers(so);
	}
	
	public Socket getSocket(){
		return s;
	}
	
	public void send(SendObject o){
		Log.d("send", "try");
		co.send(o);
	}
}