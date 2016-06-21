package org.wajjam.project.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import User.SendType;
import User.User;
/**
 * 
 * @author Johns lap
 *	Read the chat and send it out to all connected users and say which server it's
 */
public class ChatThread implements Runnable{

	private Thread thread = null;
	private Socket sk = null;
	private ObjectInputStream in = null;
	public ChatThread(Socket sk,ObjectInputStream in) {
		this.sk=sk;
		this.in=in;
		thread= new Thread(this);
		thread.start();
		
	}

	@Override
	public void run() {
		String chat = "";
		String ip = "";
		User user = null;
		try {
			user = (User)in.readUnshared();
			ip  = (String)in.readUnshared();
			chat  = (String)in.readUnshared();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			sk.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		for(User u: UserList.getInstance().getUserList()){
			try {
				SSLSocket s = null;	
				SSLSocketFactory sl = (SSLSocketFactory) SSLSocketFactory.getDefault();
				String host = u.getIp();
				if(host.equals(Server.getIp())){
					host=u.getLocalIp();
				}			
				s = (SSLSocket) sl.createSocket(host, u.getPort());
				final String[] enabledCipherSuites = { "SSL_DH_anon_WITH_RC4_128_MD5" };
				s.setEnabledCipherSuites(enabledCipherSuites);
				s.startHandshake();
				ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
				out.writeUnshared(SendType.CHAT);
				String h=Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+"";
				String m = Calendar.getInstance().get(Calendar.MINUTE)+"";
				String sec= Calendar.getInstance().get(Calendar.SECOND)+"";
				if(h.length()==1){
					h="0"+h;
				}
				if(sec.length()==1){
					sec="0"+sec;
				}
				if(m.length()==1){
					m="0"+m;
				}
				String time = "["+h+":"+m+":"+sec+"]";
		
				out.writeUnshared(time+ " "+user.getUsername()+": "+chat);
				host=Server.getIp();
				
				if(u.getIp().equals(Server.getIp())){
					host=Server.getLocalIp();
				}
				if(user.equals(u)){
					host=ip;
				}
				out.writeUnshared(host);
				out.writeUnshared(sk.getLocalPort());
				s.close();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
