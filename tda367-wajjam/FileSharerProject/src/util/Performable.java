package util;

import java.util.List;

/**
 * This interface is to be implemented by both model and view classes and works
 * to bind the models with the views, in a dynamic way. They shall have a list
 * of ActionID's that shows which actions can be performed by this class.
 * 
 * <p>
 * To carry out these actions, the class may or may not need additional data
 * from a <code>DataSupplier</code>, which will be provided via the controller,
 * calling <code>perform(ActionID actionToPerform, E requiredData)</code> on
 * this object, where requiredData is extracted from the
 * <code>DataSupplier</code> responsible by a call to its <code>getData()</code>.
 * 
 * <p>
 * Generic types are used to allow for differing types of returned and required
 * data amongst the implementing classes, without the need for static type
 * casting.
 * 
 * <p>
 * When writing classes implementing this interface, the programmer needs to
 * make sure that the <code>DataSupplier</code> where requiredData is gotten, is
 * parameterized the same way as the <code>Performable</code>-implementer, or as
 * a subclass. Formally, <code>suppliedData instanceof requiredData</code>
 * should return true. Failure to do so will result in the controller throwing a
 * <code>ClassCastException</code>, and the action will not be carried out.
 * 
 * @author Wånge
 * @param <E> the type of data the implementing class wants to perform its
 *            action.
 * @see DataSupplier
 */
public interface Performable<T> {

	/**
	 * Returns a list of ActionID's depicting what actions this class can
	 * perform.
	 * 
	 * @return the list of actions available to this class.
	 */
	public List<ActionIDCarrier> canPerform();

	/**
	 * Method invoked by the controller with data extracted from the
	 * corresponding <code>DataSupplier</code> object.
	 * 
	 * @param actionToPerform this ActionID tells which action is to be
	 *            performed.
	 * @param requiredData the data from a <code>DataSupplier</code> that is
	 *            needed to perform the requested update.
	 */
	public void perform(ActionID actionToPerform, T requiredData);
	
	/**
	 * Gets the IDNumber associated with the ActionID given by
	 * <code>action.getID</code>, for this instance.
	 * 
	 * @param action
	 * @return
	 */
	public int getPerformIDNumber(ActionIDCarrier action);
}
