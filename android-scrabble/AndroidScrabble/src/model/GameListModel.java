package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.UnknownHostException;

import util.ResponseObject;
import util.SendObject;
import util.SendableAction;
import android.content.Context;
import android.scrabble.R;
import android.util.Log;

/**
 * Model class that gets all games and lists them.
 */
public class GameListModel implements IModel{

	private Context context = null;
	private Socket socket = null;
	private ObjectInputStream is = null;
	private ObjectOutputStream out = null;
	
	public GameListModel(Context c){
		context = c;

		initSocket();
	}
	
	/**
	 * Initiates the socket and it's streams
	 */
	public void initSocket(){
		try {
			socket = new Socket(context.getString(R.string.serverip), 7896);
			
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
	
	/**
	 * Sends a request to the server to get all the opponent data for "username"
	 */
	public ResponseObject getOpponentData(String username){
		ResponseObject retrieved = null;
		try{
			SendObject object = new SendObject(SendableAction.OPPONENT_DATA, username);
			
			out.writeUnshared(object);
			out.flush();
			
			if(socket.getSoTimeout() == 0){
				socket.setSoTimeout(4000);	//	wait max 4 seconds to get response
			}
			
			retrieved = getServerAnswer();
		}
		catch(IOException io){
			Log.e("error", io.getMessage());
		}
		return retrieved;
	}
	
	/**
	 * Retrieves the answer from the server
	 */
	private ResponseObject getServerAnswer() {
		ResponseObject data = null;
		try {
			do{	//	wait until the data is read. must take less than socket.getSoTimeout() secs.
				data = (ResponseObject)is.readUnshared();
			}while(data == null);
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		catch(StreamCorruptedException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		return data;
	}
}