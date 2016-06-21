import java.io.IOException;
import java.net.ServerSocket;

import util.ServerUtils;

import network.ServerInputThread;

public class Main {
	
	/**
	 * Starts the server and prints the ip and port the server is running on
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ServerSocket s = null;
		try {
			s = new ServerSocket(7896);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ServerInputThread st = new ServerInputThread(s);
		st.start();
		
		System.out.println("Servern springer! Ip "+ServerUtils.getIp()+" och port "+s.getLocalPort());
	}
}