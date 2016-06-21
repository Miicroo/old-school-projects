package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import model.data.UserData;

import util.ResponseObject;
import util.SendObject;
import util.SendableAction;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

/**
 * Thread that listens to game invitations.
 */
public class InvitationModel extends Thread implements IModel{
	
	private Socket socket = null;
	private ObjectInputStream is = null;
	private ObjectOutputStream out = null;
	
	private Context context = null;
	
	public InvitationModel(Context context, String serverIp){
		this.context = context;

		initSocket(serverIp);
	}
	
	/*
	 * Initiates the socket and it's streams
	 */
	public void initSocket(String serverIp){
		try {
			//	create socket
			if(socket == null){
				socket = new Socket(serverIp, 7896);				
			}
			is = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
		}
		catch(UnknownHostException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/*
	 * Retrieves the answer from the server
	 */
	public void run() {
		
		//	tell the server that this is the main listener-thread
		try{
			identifyToServer();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		Object o = null;
		while(true){
	        try{
	        	Log.e("class", "running run in input");
	            while((o = is.readUnshared()) != null){
	            	Log.d("class", o.getClass().getName());
					if(o.getClass().equals(ResponseObject.class)){
						ResponseObject so = ((ResponseObject)(o));
						String[] req = ((String[])so.getObject());
						
						showInvitationDialog(req[0], req[1]);
					}
	            }
	            sleep(10000);
	        }
	        catch(InterruptedException e){	
	        	Log.e("class", e.getMessage());
	        }
	        catch(ClassNotFoundException e){
	        	Log.e("class", e.getMessage());
	        }
	        catch (IOException e){
	        	//	We arrive here when the socket is closed.
	            Log.e("class", e.getMessage());
	        	break;
	        }
	    }
	}
	
	public void showInvitationDialog(String message, final String opp){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message);
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface di, int arg1) {
				sendStartGameRequest(opp);
				di.dismiss();
			}
		});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface di, int arg1) {
				di.dismiss();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	/**
	 * Sends a request to the server that "username" wants to start a game
	 */
	public void sendStartGameRequest(String username){
		try{
			SendObject object = new SendObject(SendableAction.START_GAME, username);
			
			out.writeUnshared(object);
			out.flush();
		}
		catch(IOException io){
			Log.e("error", io.getMessage());
		}
	}
	
	/**
	 * Tell the server that this is the main thread where the user always can be reached.
	 */
	public void identifyToServer() throws IOException {
		SendObject so = new SendObject(SendableAction.MAIN_THREAD, UserData.getInstance().getUsername());
		out.writeUnshared(so);
		out.flush();
	}
	
	/**
	 * Close the socket so the server knows that the user is offline
	 */
	public void killSocket() {
		if(socket != null){
			if(!socket.isClosed()){
				try {
					socket.close();
				}
				catch(IOException e){
					e.printStackTrace();
				}
				socket = null;
			}
		}
	}
}