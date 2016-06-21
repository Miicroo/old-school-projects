package model.login;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

import util.ErrorHandling;

import model.state.ApplicationState;

import User.ServerUser;
/**
 * 
 * @author Johns lap
 *A thread that tells the server that you have change you username
 */
public class UserChangeName extends Observable implements Runnable {
	private Server server = null;

	public UserChangeName(Server server, Observer o) {
		this.server = server;
		this.addObserver(o);
		new Thread(this).start();
	}

	@Override
	public void run() {
		ObjectOutputStream out = null;
		Socket s = null;
		try {
			s = new Socket(server.getHost(), server.getPort());
			out = new ObjectOutputStream(s.getOutputStream());
			out.writeObject(ServerUser.CHANGE_USERNAME);
			out.writeObject(ApplicationState.getInstance().getUser());
		} catch (UnknownHostException e) {
			ErrorHandling.displayErrorMessage(e);
		} catch (IOException e) {
			ErrorHandling.displayErrorMessage(e);
		} finally {
			try {
				s.close();
			} catch (IOException e1) {
			}
		}
	}
}
