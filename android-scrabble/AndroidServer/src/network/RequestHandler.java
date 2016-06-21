package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;
import util.SendObject;

/**
 * Thread that waits for input from a certain user.
 */
public class RequestHandler extends Thread implements Runnable{
	
	private ObjectInputStream ois;
	private List<SendObject> tasks;	//	list of tasks that has been sent from the client
	
	public RequestHandler(Socket s, List<SendObject> tasks){
		this.tasks = tasks;
		try{
			ois = new ObjectInputStream(s.getInputStream());
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	@Override
	public void run(){
		Object o = null;
		while(true){	//	listen forever (at least until the socket dies).
	        try{
	            while((o = ois.readUnshared()) != null){
					if(o.getClass().equals(SendObject.class)){
						SendObject so = ((SendObject)(o));
						tasks.add(so);	//	add the incoming object to the list
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
		tasks.add(null);	//	send bad data to kill the listener thread
	}
}