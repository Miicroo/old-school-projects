package model.traffic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.net.ssl.SSLSocket;

import model.data.ChatObject;
import model.login.Server;
import model.state.ApplicationState;

import util.ActionID;
import util.ActionIDCarrier;
import util.DataSupplier;
import util.UnsupportedActionException;
import util.ErrorHandling;
/**
 * Receive a chatobject from the server and tell the observer
 * @author Johns lap
 *
 */
public class ChatReceiver extends Observable implements Runnable, DataSupplier<ChatObject> {
	private String chat = "";
	private SSLSocket sk= null;
	private ObjectInputStream in = null;
	private Thread thread = null;
	private ChatObject ob = null;
	private ArrayList<ActionIDCarrier> canSupply = new ArrayList<ActionIDCarrier>();
	
	public ChatReceiver(SSLSocket sk, ObjectInputStream in, Observer o){
		this.addObserver(o);
		this.sk=sk;
		this.in=in;
		
		canSupply.add(new ActionIDCarrier(ActionID.CHAT_RECEIVED));
		thread = new Thread(this);
		thread.start();
		
	}
	@Override
	public void run() {
		try {
			chat = (String)in.readUnshared();
			String host = (String)in.readUnshared();
			if(host.equals(ApplicationState.getInstance().getUser().getLocalIp())){
				host = "127.0.0.1";
			}
			int port = (Integer)in.readUnshared();
			ob = new ChatObject(new Server(host,port),chat);
		} catch (IOException e) {
			ErrorHandling.displayErrorMessage(e);
		} catch (ClassNotFoundException e) {
			ErrorHandling.displayErrorMessage(e);
		}
		setChanged();
		notifyObservers(new ActionIDCarrier(ActionID.NEW_SUPPLIER_INSTANTIATED));
		setChanged();
		notifyObservers(new ActionIDCarrier(ActionID.CHAT_RECEIVED));
		ob=null;
		setChanged();
		notifyObservers(new ActionIDCarrier(ActionID.SUPPLIER_DELETED));
		try {
			sk.close();
		} catch (IOException e) {
			ErrorHandling.displayErrorMessage(e);
		}
	}
	@Override
	public List<ActionIDCarrier> canSupply() {		
		return canSupply;
	}
	@Override
	public ChatObject getData(ActionID action) throws UnsupportedActionException {
		return ob;
	}
	
	@Override
	public int getSupplyIDNumber(ActionIDCarrier action) {
		return canSupply().get(canSupply().indexOf(action)).getIDNumber();
	}
}
