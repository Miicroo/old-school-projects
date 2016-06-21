package org.wajjam.project.server;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.wajjam.project.server.gui.FileServer;


public class Main {
	

	public static void main(String args[]){
		boolean check=true;
		int port=0;
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } catch (Exception e) {}
	    
	    //Checks so the given port is a real port
		while(check){
			String portNumber = JOptionPane.showInputDialog("Enter the port for the server");
			try{
				port = Integer.parseInt(portNumber);
				if(port<=65535&&port>=0){
					check=false;
				}
			}catch(Exception e){
				if(portNumber==null){
					System.exit(0);
				}
			}
		}
		//Starts the server with typed port
		new FileServer(port);
	}
}