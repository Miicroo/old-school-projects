package util;

import java.util.LinkedList;
import java.util.List;

/**
 *  A list that you can listen to.
 *  
 *  @param <E>
 */
public class ListenableList<E> extends LinkedList<E> {
	
	private static final long serialVersionUID = 4829748232963920754L;
	private List<ListListener> listeners = new LinkedList<ListListener>();
	
	/**
	 * Adds an ite and notifys all listeners that the item has been added.
	 */
	public boolean add(E item){
		boolean value = super.add(item);

		for(ListListener ll : listeners){
			ll.listChanged();
		}
		return value;
	}
	
	/**
	 * Add ll as listener to the list.
	 * 
	 * @param ll
	 */
	public void addListListener(ListListener ll){
		listeners.add(ll);
	}
	
	/**
	 * Remove ll from the list of listeners
	 *  
	 * @param ll
	 */
	public void removeListListener(ListListener ll){
		listeners.remove(ll);
	}
}