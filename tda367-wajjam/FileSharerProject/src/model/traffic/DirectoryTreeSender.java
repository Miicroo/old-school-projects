package model.traffic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;

import model.state.ApplicationState;
/**
 * This class is created by sendertypethread and sends the tree to the user
 * @author Johns lap
 *
 */

public class DirectoryTreeSender implements Runnable {
	private SSLSocket sk = null;
	private ObjectInputStream in = null;

	public DirectoryTreeSender(SSLSocket sk, ObjectInputStream in) {
		this.sk = sk;
		this.in = in;
		new Thread(this).start();
	}

	@Override
	public void run() {

		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(sk.getOutputStream());
			out.writeUnshared(ApplicationState.getInstance().getSharedTree());

		} catch (UnknownHostException e) {
		} catch (IOException e) {
		} catch (ClassCastException e) {
		} finally {
			try {
				sk.close();
				in.close();
				out.close();
			} catch (IOException e) {
			}
		}
	}
}