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
import User.User;
/**
 * 
 * @author Johns lap
 * Log out the user from the specific server
 */
public class UserLogout extends Observable implements Runnable {
	private User user = null;
	private Server server = null;

	public UserLogout(Server server, Observer o) {
		this.server = server;
		this.addObserver(o);
		new Thread(this).start();
	}

	@Override
	public void run() {
		user = ApplicationState.getInstance().getUser();
		ObjectOutputStream out = null;
		Socket s = null;
		try {
			s = new Socket(server.getHost(), server.getPort());
			out = new ObjectOutputStream(s.getOutputStream());
			out.writeObject(ServerUser.REMOVE);
			out.writeObject(user);
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