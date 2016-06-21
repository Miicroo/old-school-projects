package model.traffic;

import java.io.IOException;

import java.util.Observer;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import util.ErrorHandling;


/**
 * This class contains a serversocket that listen for connections and send them
 * to sendertypethread
 * 
 * @author Johns lap
 * 
 */
public class ClientSocketListener implements Runnable {

	private SSLServerSocket serverSocket;
	private Observer o;

	/**
	 * @param s
	 */
	public ClientSocketListener(int port, Observer o) {
		this.o = o;
		try {
			SSLServerSocketFactory sl = (SSLServerSocketFactory) SSLServerSocketFactory
					.getDefault();
			serverSocket = (SSLServerSocket) sl.createServerSocket(port);
			final String[] enabledCipherSuites = { "SSL_DH_anon_WITH_RC4_128_MD5" };
			serverSocket.setEnabledCipherSuites(enabledCipherSuites);
		} catch (IOException e) {
			ErrorHandling.displayErrorMessage(e);
		}
	}

	public SSLServerSocket getServerSocket() {
		return serverSocket;
	}

	/**
         * 
         */
	public void run() {
		while (true) {
			try {
				new SenderTypeThread((SSLSocket) serverSocket.accept(), o);
			} catch (IOException e) {
			}
		}
	}

}
