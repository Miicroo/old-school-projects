package model.traffic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;

import model.logic.Directory;
/**
 * This class is createt by sendertypethread and sends information of files in a map to the user
 * @author Johns lap
 *
 */
public class FileSender implements Runnable {
	private SSLSocket sk = null;
	private ObjectInputStream in = null;

	public FileSender(SSLSocket sk, ObjectInputStream in) {
		this.sk = sk;
		this.in = in;
		new Thread(this).start();
	}

	@Override
	public void run() {
		ObjectOutputStream out = null;
		try {
			String path = (String)in.readUnshared();
			out = new ObjectOutputStream(sk.getOutputStream());
			Directory d = new Directory(path);
			out.writeUnshared(d.getFileInformation());

		} catch (UnknownHostException e) {
		} catch (IOException e) {
		} catch (ClassCastException e) {
		} catch (ClassNotFoundException e) {} 
		finally {
			try {
				sk.close();
			} catch (IOException e) {
			}
		}
	}
}