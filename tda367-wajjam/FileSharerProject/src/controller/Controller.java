package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import model.ModelInitiater;

import util.ActionIDCarrier;
import util.DataSupplier;
import util.Performable;
import util.UnsupportedActionException;
import util.ErrorHandling;
import view.LoadFrame;
import view.MainView;

/**
 * The controller class of the application. Connects the view with the model in
 * one, loosely attached class. The controller saves two lists with
 * <code>Performable</code> objects and <code>DataSupplier</code> objects
 * respectively.
 * 
 * <p>
 * For further information on how the controller delegates between view and
 * model, see the methods in the class.
 * 
 * <p>
 * This modified MVC system works as follows. There are three main kinds of
 * classes. The first are the classes that notifies the controller that
 * something has happened that requires one or more other class/classes to
 * perform an action. These extends Observable. The second kind are the classes
 * that are tasked with performing these actions. They all implement
 * <code>Performable</code> and the controller saves <code>Performable</code>
 * references to them. The third kind are the classes which holds data necessary
 * to perform the actions. These all implement <code>DataSupplier</code> and the
 * controller saves references to them as such.
 * 
 * <p>
 * Any class can be any of the three kinds, or any combination of them. For
 * example, there is nothing wrong with one class implementing both interfaces
 * and also calling the observer to notify it on an action to initiate(however,
 * if all three corresponds to the SAME <code>ActionID</code>, you might as well
 * not bother going through the controller).
 * 
 * @author Wånge
 * @see DataSupplier
 * @see Performable
 * @see #registerNewPerformer(Performable)
 * @see #registerNewSupplier(DataSupplier)
 * @see #update(Observable, Object)
 */
public class Controller implements Observer{

	private List<Performable<?>> allPerformers = new ArrayList<Performable<?>>();
	private List<DataSupplier<?>> allSuppliers = new ArrayList<DataSupplier<?>>();
	private List<ActionIDCarrier> actionsToPerform = new ArrayList<ActionIDCarrier>();
	private List<ActionIDCarrier> actionsToSupply = new ArrayList<ActionIDCarrier>();
	private LoadFrame loadFrame;

	/**
	 * Instantiates a new controller, with the specified model and view to be
	 * used. If the arguments are null, default models and/or views are used.
	 * 
	 * @param model the model to be used.
	 * @param view the view to be used.
	 */
	public Controller(String model, String view) {

		loadFrame = new LoadFrame();
		
		if (model == null) {
			new ModelInitiater(this);
		} else {

		}
		if (view == null) {
			final Observer o = this;	
			new MainView(o);
			loadFrame.setVisible(false);
		} else {

		}
	}

	/**
	 * Registers a new <code>Performable</code> to the controller. Called by
	 * <code>update(Observable arg0, Object arg1)</code> when it gets a
	 * NEW_PERFORMER_INSTANTIATED call. Can also be called without the Observer
	 * pattern, if other inheritance than Observable is wanted for the class
	 * implementing <code>Performable</code>.
	 * 
	 * @param p the <code>Performable</code> to be registered.
	 */
	public void registerNewPerformer(Performable<?> p) {
		allPerformers.add(p);
		actionsToPerform.addAll(p.canPerform());
	}
	
	public void deletePerformer(Performable<?> p){
		if(allPerformers.remove(p)){
			actionsToPerform.removeAll(p.canPerform());
		}
		
	}

	/**
	 * Registers a new <code>DataSupplier</code> to the controller. Called by
	 * <code>update(Observable arg0, Object arg1)</code> when it gets a
	 * NEW_SUPPLIER_INSTANTIATED call. Can also be called without the Observer
	 * pattern, if other inheritance than Observable is wanted for the class
	 * implementing <code>DataSupplier</code>.
	 * 
	 * @param ds the <code>DataSupplier</code> to be registered.
	 */
	public void registerNewSupplier(DataSupplier<?> ds) {
		allSuppliers.add(ds);
		actionsToSupply.addAll(ds.canSupply());
	}
	
	public void deleteSupplier(DataSupplier<?> ds){
		if(allSuppliers.remove(ds)){
			actionsToSupply.removeAll(ds.canSupply());
		}
	}

	/**
	 * Whenever an incident that requires the application to take action occurs,
	 * this method is called. The performer or performers of the specified
	 * action is found, along with a possible supplier of data required to carry
	 * out the action. The method then calls
	 * <code>perform(ActionID actionToPerform, E requiredData)</code> for all
	 * performers, with the data extracted from the suppliers as
	 * <code>requiredData</code>.
	 * 
	 * <p>
	 * To register new performers or suppliers, the objects that wants to
	 * register can call <code>notifyObservers()</code> with ActionIDs
	 * NEW_PERFOMER_INSTANTIATED or NEW_SUPPLIER_INSTANTIATED as argument. The
	 * <code>ModelInitiator</code> thread will call this with
	 * MODEL_INITIATOR_FINISHED before it exits.
	 */
	@Override
	public final void update(Observable arg0, Object arg1) {
		if (arg1 instanceof ActionIDCarrier) {
			ActionIDCarrier action = (ActionIDCarrier) arg1;

			switch (action.getId()) {
			
			case MODEL_INITIATOR_FINISHED:
				if (arg0 instanceof ModelInitiater) {
					List<Performable<?>> performers = ((ModelInitiater) arg0).getPerformers();
					List<DataSupplier<?>> suppliers = ((ModelInitiater) arg0).getSuppliers();

					allPerformers.addAll(performers);
					allSuppliers.addAll(suppliers);

					for (Performable<?> p : performers) {
						actionsToPerform.addAll(p.canPerform());
					}

					for (DataSupplier<?> ds : suppliers) {
						actionsToSupply.addAll(ds.canSupply());
					}
				}
				break;

			case NEW_PERFOMER_INSTANTIATED:
				if (arg0 instanceof Performable<?>) {
					registerNewPerformer((Performable<?>) arg0);
				}
				break;

			case NEW_SUPPLIER_INSTANTIATED:
				if (arg0 instanceof DataSupplier<?>) {
					registerNewSupplier((DataSupplier<?>) arg0);
				}
				break;
			case PERFORMER_DELETED:
				if (arg0 instanceof Performable<?>) {
					deletePerformer((Performable<?>) arg0);
				}
				break;

			case SUPPLIER_DELETED:
				if (arg0 instanceof DataSupplier<?>) {
					deleteSupplier((DataSupplier<?>) arg0);
				}
				break;

			default:
				performAction(action);
			}
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void performAction(ActionIDCarrier action) {
		List<Performable<?>> performers = new ArrayList<Performable<?>>();
		List<DataSupplier<?>> suppliers = new ArrayList<DataSupplier<?>>();

		for (DataSupplier<?> p : allSuppliers) {
			if (p.canSupply().contains(action)) {
				suppliers.add(p);
			}
		}
		for (Performable<?> p : allPerformers) {
			if (p.canPerform().contains(action)) {
				performers.add(p);
			}
		}
		
		try {
			for (Performable p : performers) {	//All performers should get a call
				if(!suppliers.isEmpty()) { //If there is no supplier, run perform() withouth data
					for(DataSupplier<?> ds : suppliers) { //Loop all suppliers 
						if(p.getPerformIDNumber(action) == ActionIDCarrier.ALL) {
							if(action.getIDNumber() == ds.getSupplyIDNumber(action)) {
								p.perform(action.getId(), ds.getData(action.getId()));
							}
						} else {
							if(p.getPerformIDNumber(action) == ds.getSupplyIDNumber(action)) {
								p.perform(action.getId(), ds.getData(action.getId()));
							}
						}
					}
				} else {
					p.perform(action.getId(), null);
				}
			}
		} catch (ClassCastException e) {
			ErrorHandling.displayErrorMessage(e);
			System.out.println("Error in controller. Type of supply does not match demand.");
		} catch (UnsupportedActionException e) {
			ErrorHandling.displayErrorMessage(e);
			System.out.println("Error in controller. Supplier does not support specified action");
		}
	}
}