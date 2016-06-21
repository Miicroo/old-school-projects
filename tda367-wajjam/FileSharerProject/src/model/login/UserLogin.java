package model.login;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import util.ErrorHandling;

import User.ServerUser;
import User.User;
import model.login.ServerUpdate.Option;
import model.state.ApplicationState;
/**
 * 
 * @author Johns lap
 *	Log in the user on the specific server
 */
public final class UserLogin extends Observable implements Runnable {
	ObjectOutputStream out = null;
	Socket s = null;
	ObjectInputStream in = null;
	private User user = null;
	private Server server = null;

	public UserLogin(Server server, Observer o) {
		this.server = server;
		this.addObserver(o);
		new Thread(this).start();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		user = ApplicationState.getInstance().getUser();
		try {
			s = new Socket(server.getHost(), server.getPort());
			out = new ObjectOutputStream(s.getOutputStream());
			out.writeUnshared(ServerUser.ADD);

			in = new ObjectInputStream(s.getInputStream());

			out.writeUnshared(user);
			setChanged();
			notifyObservers(new ServerUpdate(server, Option.CONNECT));

			while (ApplicationState.getInstance().getServerList().contains(server)&& server.isConnected()) {
				LinkedList<User> u = new LinkedList<User>((LinkedList<User>) in.readUnshared());
				server.setUlist(u);
				setChanged();
				notifyObservers(new ServerUpdate(server, Option.NEW_LIST));
			}
			setChanged();
			notifyObservers(new ServerUpdate(server, Option.DISCONNECT));
		} catch (ConnectException e) {
			setChanged();
			notifyObservers(new ServerUpdate(server, Option.BAD_HOST));
		}catch (UnknownHostException e) {
			ErrorHandling.displayErrorMessage(e);
			setChanged();
			notifyObservers(new ServerUpdate(server, Option.BAD_HOST));
		} catch(EOFException e){
			setChanged();
			notifyObservers(new ServerUpdate(server, Option.IOPROBLEM));
		}catch (IOException e) {
			ErrorHandling.displayErrorMessage(e);
			setChanged();
			notifyObservers(new ServerUpdate(server, Option.IOPROBLEM));
		} catch (ClassNotFoundException e) {
			ErrorHandling.displayErrorMessage(e);
		} catch (ClassCastException e) {
			ErrorHandling.displayErrorMessage(e);
			setChanged();
			notifyObservers(new ServerUpdate(server, Option.UNAME_BUSY));
		} catch (NullPointerException e) {
			ErrorHandling.displayErrorMessage(e);
		} finally {
			try {
				s.close();
			} catch (IOException e1) {
			} catch (NullPointerException e) {
			}
		}
	}
}