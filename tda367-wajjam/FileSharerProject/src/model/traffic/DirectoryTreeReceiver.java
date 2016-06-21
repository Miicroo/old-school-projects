package model.traffic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import util.ErrorHandling;

import User.SendType;
import User.User;
import model.data.DirectoryTreeModel;
import model.data.TreeObject;
import model.state.ApplicationState;
/**
 * This class creates a socket to the user and get the users tree
 * @author Johns lap
 *
 */
public class DirectoryTreeReceiver extends Observable implements Runnable {

	private User user = null;

	public DirectoryTreeReceiver(User user,Observer o) {
		this.addObserver(o);
		this.user = user;
		new Thread(this).start();
	}

	@Override
	public void run() {
		ObjectInputStream in = null;
		ObjectOutputStream out = null;
		SSLSocket s = null;
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
			out.writeObject(SendType.TREE);
			in = new ObjectInputStream(s.getInputStream());
			setChanged();
			notifyObservers(new TreeObject(user, (DirectoryTreeModel) in.readUnshared()));

		} catch (UnknownHostException e) {
			ErrorHandling.displayErrorMessage(e);
		} catch (IOException e) {
			ErrorHandling.displayErrorMessage(e);
		} catch (ClassCastException e) {
			ErrorHandling.displayErrorMessage(e);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			ErrorHandling.displayErrorMessage(e);
		} finally {
			try {
				s.close();
				in.close();
				out.close();
			} catch (IOException e) {
			}
		}
	}

}
