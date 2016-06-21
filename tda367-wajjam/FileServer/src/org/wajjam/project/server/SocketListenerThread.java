package org.wajjam.project.server;

import java.io.IOException;
import java.net.*;
/**
 * 
 * @author Johns lap
 *	The socketlistener that accept all incomming copnnections
 */
public class SocketListenerThread extends Thread {
	
	private ServerSocket serverSocket;
	
	public SocketListenerThread(ServerSocket s) {
		serverSocket = s;
	}
	
	public void run() {
		while(true) {
			try {
				new ChooseThread(serverSocket.accept());
			} catch (IOException e) {}			
		}
	}

}
