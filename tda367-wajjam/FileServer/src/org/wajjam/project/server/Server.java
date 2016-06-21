package org.wajjam.project.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * 
 * @author Johns lap
 *	Gets the ip both local and global
 */
public class Server {
	
	public static String getIp(){
		URL myip;
		String temp;
		try {
			myip = new URL("http://www.mittip.nu/");
			BufferedReader in = new BufferedReader(
					new InputStreamReader(
					myip.openStream()));
			while((temp = in.readLine())!=null){
				if(temp.endsWith("</h1>")){
					break;
				}
			}		
			temp=temp.substring(7, temp.length()-5);
		} catch (Exception e) {
			try {
				temp = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e1) {
				return null;
			}
		}
		return temp;
	}
	
	
	public static String getLocalIp() {
		String temp = "";
		try {
			InetAddress adr = getFirstNonLoopbackAddress(true, false);
			temp = adr.getHostAddress();
		} catch (SocketException e) {
		}

		return temp;
	}

	private static InetAddress getFirstNonLoopbackAddress(boolean preferIpv4,
			boolean preferIPv6) throws SocketException {
		Enumeration en = NetworkInterface.getNetworkInterfaces();
		while (en.hasMoreElements()) {
			NetworkInterface i = (NetworkInterface) en.nextElement();
			for (Enumeration en2 = i.getInetAddresses(); en2.hasMoreElements();) {
				InetAddress addr = (InetAddress) en2.nextElement();
				if (!addr.isLoopbackAddress()) {
					if (addr instanceof Inet4Address) {
						if (preferIPv6) {
							continue;
						}
						return addr;
					}
					if (addr instanceof Inet6Address) {
						if (preferIpv4) {
							continue;
						}

						return addr;
					}
				}
			}
		}
		return null;
	}
}
