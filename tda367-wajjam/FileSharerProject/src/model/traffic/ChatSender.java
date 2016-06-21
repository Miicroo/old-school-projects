package model.traffic;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import util.ErrorHandling;

import model.state.ApplicationState;

import User.ServerUser;

import model.login.Server;
/**
 * Sends the messege that you chat to the specific server
 * @author Johns lap
 *
 */
public class ChatSender implements Runnable{

	private String chat = "";
	private Thread thread = null;
	private Server s = null;
	public ChatSender(String chat, Server s) {
		this.chat=chat;
		this.s=s;
		thread= new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		try {
			Socket sk = new Socket(s.getHost(), s.getPort());
			ObjectOutputStream out = new ObjectOutputStream(sk.getOutputStream());
			out.writeUnshared(ServerUser.CHAT);
			out.writeUnshared(ApplicationState.getInstance().getUser());
			out.writeUnshared(s.getHost());
			out.writeUnshared(chat);
			sk.close();
		} catch (UnknownHostException e) {
			ErrorHandling.displayErrorMessage(e);
		} catch (IOException e) {
			ErrorHandling.displayErrorMessage(e);
		}
	}
}
