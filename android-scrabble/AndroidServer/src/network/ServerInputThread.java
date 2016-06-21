package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import model.data.ErrorHandler;

import controller.WorkThread;

/**
 * Server-thread that listens to the ServerSocket that the server provides.
 * When a phone sends a request to the server it takes care of it and redirects
 * to the correct model.
 * 
 */
public class ServerInputThread extends Thread implements Runnable{

	private ServerSocket serverSocket = null;
	
	/**
	 * Creates a new ServerController from a Server and a ServerSocket.
	 * 
	 * @param server
	 * @param sSocket
	 */
	public ServerInputThread(ServerSocket s){
		this.serverSocket = s;
	}

	/**
	 * Executes the thread
	 */
	public void run(){		
		
		boolean wait = false;	//	wait-variable that makes sure that two requests aren't handled at the same time!
		
		while(true){
			try{
				if(!wait){
					wait = true;
					Socket socket = serverSocket.accept();
										
					new WorkThread(socket).start(); //	Start new client-thread
					
					wait = false;
				}
			}
			catch(IOException e){
				ErrorHandler.report("Error in server-thread (I/O): "+e.getMessage());
				e.printStackTrace();
				wait = false;
			}
		}
	}
}