package util;

import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Singleton that contains all NamedConnections that are online. 
 */
public class OnlineList {

	private static OnlineList list = null;
	private List<NamedConnection> connections;
	
	private OnlineList(){
		connections = new ArrayList<NamedConnection>();
	}
	
	/**
	 * Gets the instance of this class.
	 * @return
	 */
	public static OnlineList getInstance(){
		if(list == null){
			list = new OnlineList();
		}
		return list;
	}
	
	/**
	 * Add a new connection.
	 * 
	 * @param nc
	 */
	public void add(NamedConnection nc){
		System.out.println("added "+nc.getName());
		connections.add(nc);
	}
	
	/**
	 * Remove connection from name.
	 * @param name
	 */
	public void remove(String name){
		ListIterator<NamedConnection> it = connections.listIterator();
		while(it.hasNext()){
			NamedConnection nc = it.next();
			if(nc.getName().equals(name)){
				it.remove();
				break;
			}
		}
	}
	
	/**
	 * Set name from known socket.
	 * 
	 * @param name
	 * @param s
	 */
	public void setName(String name, Socket s){
		for(NamedConnection nc : connections){
			if(nc.getSocket().equals(s)){
				nc.setName(name);
				break;
			}
		}
	}
	
	/**
	 * Get socket from name.
	 * 
	 * @param name
	 * @return
	 */
	public Socket getSocket(String name){
		for(NamedConnection nc : connections){
			if(nc.getName().equalsIgnoreCase(name)){
				return nc.getSocket();
			}
		}
		return null;
	}
	
	/**
	 * Get the names of the OnlineList as a List<>
	 * 
	 * @return
	 */
	public List<String> getList(){
		List<String> list = new LinkedList<String>();
		for(NamedConnection nc : connections){
			list.add(nc.getName());
		}
		return list;
	}
	
	/**
	 * Get the names of the OnlineList as an Array[]
	 * 
	 * @return
	 */
	public String[] getArray(){
		return getList().toArray(new String[0]);
	}
}