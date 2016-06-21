package controller;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import network.RequestHandler;
import model.Model;
import model.data.ErrorHandler;

import util.ListListener;
import util.ListenableList;
import util.NamedConnection;
import util.OnlineList;
import util.ResponseObject;
import util.SendObject;

/**
 * The thread that "works" on an incoming object.
 * The thread sends the object to the proper method in the proper model.
 */
public class WorkThread extends Thread implements ListListener{

	private ObjectOutputStream oos = null;
	private ListenableList<SendObject> tasks;
	private Model model = null;
	private Socket socket;
	private boolean isOnlineThread = false;	// if this workthread is the "main thread", where the user always can be reached.
	private String onlinename = "";
	
	public WorkThread(Socket socket) throws IOException{
		oos = new ObjectOutputStream(socket.getOutputStream());
		
		tasks = new ListenableList<SendObject>();	//	 the lists with objects that the thread "works" on
		tasks.addListListener(this);	//	listen to the list so the thread knows when to stop waiting.
		
		model = new Model();
		
		this.socket = socket;
		
		new RequestHandler(socket, tasks).start();	//	start the input thread that reads objects from the inputstream
	}
	
	public void run(){
		while(true){
			try {
				while(tasks.size() == 0){
					synchronized(this){
						wait();
					}
				}
				
				if(tasks.get(0) == null){	//	if the data is corrupt, quit!
					break;
				}
				else{
					work(tasks.get(0));	//	work on this object
					tasks.remove(0);
				}
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		if(isOnlineThread){
			System.out.println("closing main thread");
			OnlineList.getInstance().remove(onlinename);	//	remove the name from the onlinelist
		}
		//	Thread is dead.
	}
	
	/**
	 * Take cares of the incoming object and passes it on to the
	 * right model+method
	 */
	public void work(SendObject object){
		
		Socket oppSocket = null;
		ResponseObject data = null;
		
		switch(object.getAction()){
		case LOGIN:
			String s = (String)object.getObject();
			data = model.login(s);
			break;
		case LOGOUT:
			String s2 = (String)object.getObject();
			data = model.logout(s2);
			break;
		case PLAYERS_ONLINE:
			data = model.getPlayersOnline();
			break;
		case GET_DICTIONARIES:
			data = model.getDictionaries();
			break;
		case SEARCH_PLAYER:
			break;
		case SIGN_UP:
			String s3 = (String)object.getObject();
			data = model.signUp(s3);
			break;
		case PLACE_WORD:
			//model.placeWord(i);
			break;
		case INVITE_GAME:
			String[] users = (String[])object.getObject();
			oppSocket = OnlineList.getInstance().getSocket(users[1]);
			model.invitePlayer(users[0]);
			break;
		case START_GAME:
			//data = model.startGame(users[0], users[1]);
			break;
		case QUIT_GAME:
			//model.quitGame(i);
			break;
		case PASS:
			//model.pass(i);
			break;
		case SWAP:
			//model.swap(i);
			break;
		case OPPONENT_DATA:
			String s7 = (String)object.getObject();
			data = model.getOpponentData(s7);
			break;
		case MAIN_THREAD:
			//	add username to onlinelist
			isOnlineThread = true;
			String s8 = (String)object.getObject();
			onlinename = s8;
			OnlineList.getInstance().add(new NamedConnection(s8, socket));
			break;
		default:
			break;
		}
		
		if(data != null && oppSocket != null){	//	send data to opponent
			System.out.println("Skickar "+data.getObject()+" to "+oppSocket);
			sendToOpponent(oppSocket, data);
		}
		else if(data != null){	//	send data to user
			send(data);
		}
	}
	
	/**
	 * Send the processed data to the user.
	 */
	public void send(Object object){
		try {
			oos.writeUnshared(object);
			oos.flush();
		}
		catch(IOException e){
			ErrorHandler.report("Error sending from WorkThread :"+e.getMessage());
		}
	}
	
	/**
	 * Send data to an opponent.
	 */
	public void sendToOpponent(Socket other, Object object){
		try {
			ObjectOutputStream o = new ObjectOutputStream(other.getOutputStream());
			o.writeUnshared(object);
			o.flush();
		}
		catch(IOException e){
			ErrorHandler.report("Error sending from WorkThread :"+e.getMessage());
		}
	}

	@Override
	public void listChanged() {
		synchronized(this){
			notifyAll();
		}
	}
}