package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import User.User;

import model.login.ServerInOutLogic;
import model.state.ApplicationState;
import model.traffic.TrafficLogic;

import util.ActionID;
import util.ActionIDCarrier;
import util.DataSupplier;
import util.Performable;
import util.ErrorHandling;

/**
 * This class contains a thread that is initiated at the launch of the
 * application, and whose mission it is to initiate all model classes that is
 * needed from start. These models are
 * 
 * @author Wånge
 */
public class ModelInitiater extends Observable {

	private List<Performable<?>> performers = new ArrayList<Performable<?>>();
	private List<DataSupplier<?>> suppliers = new ArrayList<DataSupplier<?>>();

	public ModelInitiater(Observer o) {

		addObserver(o);
		Runtime.getRuntime().addShutdownHook(new ShutDownThread());
		performers.add((ServerInOutLogic.getInstance()));
		performers.add(TrafficLogic.getInstance(o));
		suppliers.add(ServerInOutLogic.getInstance());
		suppliers.add(TrafficLogic.getInstance(o));
		ServerInOutLogic.getInstance().addObserver(o);
		if (ApplicationState.getInstance().getDownloadDirectory().equals("")
				|| ApplicationState.getInstance().getDownloadDirectory() == null) {
			ApplicationState.getInstance().setDownloadDirectory(
					"C:\\FileSharer\\Download");
		}
		if (ApplicationState.getInstance().getUser() == null) {
			ApplicationState.getInstance().setUser(
					(new User(getIp(), getLocalIp(), getName())));
		} else if (!ApplicationState.getInstance().getUser().getIp().equals(
				getIp())
				|| !ApplicationState.getInstance().getUser().getLocalIp()
						.equals(getLocalIp())) {
			ApplicationState.getInstance().setUser(
					(new User(getIp(), getLocalIp(), ApplicationState
							.getInstance().getUser().getUsername())));
		}
		setChanged();
		notifyObservers(new ActionIDCarrier(ActionID.MODEL_INITIATOR_FINISHED));
	}

	public List<Performable<?>> getPerformers() {
		return performers;
	}

	public List<DataSupplier<?>> getSuppliers() {
		return suppliers;
	}

	private static String getName() {
		if (System.getenv("USERNAME") == null) {
			return "User";
		} else if (System.getenv("USERNAME").equals("")) {
			return "User";
		}
		return System.getenv("USERNAME");
	}

	private static String getIp() {
		URL myip;
		String temp;
		InputStreamReader stream = null;
		BufferedReader in = null;
		try {
			myip = new URL("http://www.mittip.nu/");
			stream = new InputStreamReader(myip.openStream());
			in = new BufferedReader(stream);
			while ((temp = in.readLine()) != null) {
				if (temp.endsWith("</h1>")) {
					break;
				}
			}
			temp = (temp != null ? temp.substring(7, temp.length() - 5)
					: InetAddress.getLocalHost().getHostAddress());
		} catch (IOException e) {
			try {
				temp = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e1) {
				return null;
			}
		} finally {
			try {
				stream.close();
				in.close();
			} catch (IOException e) {
				ErrorHandling.displayErrorMessage(e);
			}
		}
		return temp;
	}

	private static String getLocalIp() {
		String temp = "";
		try {
			InetAddress adr = getFirstNonLoopbackAddress(true, false);
			temp = adr.getHostAddress();
		} catch (SocketException e) {
		}

		return temp;
	}

	public static InetAddress getFirstNonLoopbackAddress(boolean preferIpv4,
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
