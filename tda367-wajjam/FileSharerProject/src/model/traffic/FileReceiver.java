package model.traffic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import util.ActionID;
import util.ActionIDCarrier;
import util.DataSupplier;
import util.UnsupportedActionException;
import util.ErrorHandling;

import model.data.FileInformation;
import model.data.TreeObject;
import model.state.ApplicationState;

import User.SendType;
import User.User;

public class FileReceiver extends Observable implements Runnable,DataSupplier<TreeObject> {

	private User user = null;
	private String path=null;
	private List<FileInformation> dm = null;
	private ArrayList<ActionIDCarrier> canSupply = new ArrayList<ActionIDCarrier>();
	private Thread thread = null;
	private SSLSocket s = null;
	
	public FileReceiver(User user,String path,Observer o) {
		this.addObserver(o);
		this.path=path;
		this.user = user;
		canSupply.add(new ActionIDCarrier(ActionID.FILE_ACQUIRED));
		
		thread = new Thread(this);
		thread.start();

	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		ObjectInputStream in = null;
		ObjectOutputStream out = null;
		try {

			String host= user.getIp();
			if(host.equals(ApplicationState.getInstance().getUser().getIp())){
				host=user.getLocalIp();
			}
			SSLSocketFactory sl = (SSLSocketFactory) SSLSocketFactory
					.getDefault();
			
			s = (SSLSocket) sl.createSocket(host, user.getPort());
			final String[] enabledCipherSuites = { "SSL_DH_anon_WITH_RC4_128_MD5" };
			s.setEnabledCipherSuites(enabledCipherSuites);
			s.startHandshake();
			out = new ObjectOutputStream(s.getOutputStream());
			out.writeObject(SendType.ACQ_FILE);
			out.writeObject(path);
			in = new ObjectInputStream(s.getInputStream());
			dm = (List<FileInformation>)in.readUnshared();
			if(countObservers()!=0){
				setChanged();
				notifyObservers(new ActionIDCarrier(ActionID.NEW_SUPPLIER_INSTANTIATED));
				setChanged();
				notifyObservers(new ActionIDCarrier(ActionID.FILE_ACQUIRED));
				setChanged();
				notifyObservers(new ActionIDCarrier(ActionID.SUPPLIER_DELETED));
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
			ErrorHandling.displayErrorMessage(e);
		} catch (IOException e) {
			e.printStackTrace();
			ErrorHandling.displayErrorMessage(e);
		} catch (ClassCastException e) {
			e.printStackTrace();
			ErrorHandling.displayErrorMessage(e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			ErrorHandling.displayErrorMessage(e);
		} catch(Exception e){
			e.printStackTrace();
		}
		finally {
			try {
				s.close();
			} catch (IOException e) {
			}
		}
	}
	
	public Thread getThread() {
		return thread;
	}
	
	public void closeSocket() {
		try {
			s.close();
		} catch (IOException e) {}
	}
	

	@Override
	public List<ActionIDCarrier> canSupply() {
		return canSupply;
	}

	@Override
	public TreeObject getData(ActionID action)
			throws UnsupportedActionException {
		return new TreeObject(user, dm);
	}
	
	@Override
	public int getSupplyIDNumber(ActionIDCarrier action) {
		return canSupply().get(canSupply().indexOf(action)).getIDNumber();
	}
}
