package util;

import java.util.List;

/**
 * This interface is to be implemented by both model and view classes and works
 * to bind the models with the views, in a dynamic way. They shall have a list
 * of ActionID's that shows which actions this class can supply data for.
 * 
 * <p>
 * The controller will ask this class for required data when an action is to be
 * carried out, by calling <code>getData(ActionID)</code>.
 * 
 * <p>
 * Generic types are used to allow for differing types of returned and required
 * data amongst the implementing classes, without the need for static type
 * casting.
 * 
 * <p>
 * When writing classes implementing this interface, the programmer needs to
 * make sure that the <code>Performable</code> which gets the returned data, is
 * parameterized the same way as the <code>DataSupplier</code>-implementer, or as
 * a subclass. Formally, <code>suppliedData instanceof requiredData</code>
 * should return true. Failure to do so will result in the controller throwing a
 * <code>ClassCastException</code>, and the action will not be carried out.
 * 
 * @author Wånge
 * @param <E> the type of data the implementing class returns.
 * @see Performable
 */
public interface DataSupplier<I> {

	/**
	 * Returns a list of <code>ActionIDs</code> that denotes which actions this
	 * class can supply data for.
	 * 
	 * @return
	 */
	public List<ActionIDCarrier> canSupply();
	
	/**
	 * Asks this class for the data needed to perform the specified action. To
	 * minimize risk of erroneous behavior, implementations should check that
	 * the given <code>ActionID</code> exists in their list.
	 * <p>
	 * For instance: <code>canSupply().contains(action)</code>.
	 * 
	 * @param action the action for which data is asked.
	 * @return the data asked for to perform the specified action.
	 * @throws UnsupportedActionException if <code>action</code> is not
	 *             supported.
	 */
	public I getData(ActionID action) throws UnsupportedActionException;
	
	/**
	 * Gets the IDNumber associated with the ActionID given by
	 * <code>action.getID</code>, for this instance.
	 * 
	 * @param action
	 * @return
	 */
	public int getSupplyIDNumber(ActionIDCarrier action);
}
