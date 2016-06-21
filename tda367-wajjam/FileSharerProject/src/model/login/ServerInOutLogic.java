package model.login;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import util.ActionID;
import util.ActionIDCarrier;
import util.DataSupplier;
import util.Performable;
import util.UnsupportedActionException;
import model.login.ServerUpdate.Option;
import model.state.ApplicationState;

/**
 * 
 * @author Johns lap
 * A singelton that is logic for all server operations
 * login, logout changename etc.
 */
public class ServerInOutLogic extends Observable implements Observer, Performable<Server>, DataSupplier<Server>{

	private Server s = null;
	private volatile static ServerInOutLogic instance = new ServerInOutLogic();
	private ArrayList<ActionIDCarrier> canPerform = new ArrayList<ActionIDCarrier>();
	ArrayList<ActionIDCarrier> canSupply = new ArrayList<ActionIDCarrier>();

	private ServerInOutLogic() {
		
		canSupply.add(new ActionIDCarrier(ActionID.SERVER_UPDATED));
		canPerform.add(new ActionIDCarrier(ActionID.CONNECT));
		canPerform.add(new ActionIDCarrier(ActionID.DISCONNECT));
		canPerform.add(new ActionIDCarrier(ActionID.REMOVE));
		canPerform.add(new ActionIDCarrier(ActionID.CHANGE_USERNAME));
	}

	/**
	 * 
	 * @param s
	 *            where s is the server(see) you try to login to
	 * @see org.wajjam.project.client.model.loging.Server
	 */
	public synchronized void logIn(Server s) {
		ApplicationState.getInstance().setLastIPandPort(new String[] {s.getHost(), Integer.toString(s.getPort())});
		if (!ApplicationState.getInstance().getServerList().contains(s)) {
			ApplicationState.getInstance().getServerList().add(s);
		}
		if (!s.isConnected()) {	
			new UserLogin(s, this);
		}
	}

	/**
	 * 
	 * @param s
	 *            where s is the server(see) you try to logout from
	 * @see org.wajjam.project.client.model.loging.Server
	 */
	public synchronized void logOut(Server s, boolean remove) {
		if (ApplicationState.getInstance().getServerList().contains(s)) {
			if (remove) {
				ApplicationState.getInstance().getServerList().remove(s);
			}
			if(s.isConnected()){
				new UserLogout(s, this);
			}
		}
	}

	public synchronized void logOutAll() {
		for (Server s : ApplicationState.getInstance().getServerList()) {
			if (ApplicationState.getInstance().getServerList().contains(s)) {
				if (s.isConnected()) {
					new UserLogout(s, this);
				}
			}
		}
	}

	/**
	 * 
	 * @return a singelton of ServerInOutLogic
	 */
	public static ServerInOutLogic getInstance() {
		if(instance == null) {
			instance = new ServerInOutLogic();
		}
		return instance;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof ServerUpdate) {
			
			ServerUpdate upt = (ServerUpdate) arg;	
			s = upt.getServer();
			if (upt.getOption() == Option.BAD_HOST) {
						s.setConnected(false);
			} else if (upt.getOption() == Option.CONNECT) {
						s.setConnected(true);
			} else if (upt.getOption() == Option.DISCONNECT) {
				if (ApplicationState.getInstance().getServerList().contains(upt.getServer())) {
					s.setConnected(false);
				}
			} else if (upt.getOption() == Option.IOPROBLEM) {
				s.setConnected(false);
			} else if (upt.getOption() == Option.UNAME_BUSY) {
				s.setConnected(false);
			} else if (upt.getOption() == Option.NEW_LIST) {
			}
			setChanged();
			notifyObservers(new ActionIDCarrier(ActionID.SERVER_UPDATED));
		}
	}

	@Override
	public List<ActionIDCarrier> canPerform() {
		
		return canPerform;
	}


	@Override
	public void perform(ActionID actionToPerform, Server requiredData) {
		if(requiredData!=null){
			if (actionToPerform.equals(ActionID.CONNECT)) {
				this.logIn(requiredData);
			} else if (actionToPerform.equals(ActionID.DISCONNECT)) {
				this.logOut(requiredData, false);
			} else if (actionToPerform.equals(ActionID.REMOVE)) {
				this.logOut(requiredData, true);
			} 
		}else{
			if (actionToPerform.equals(ActionID.CHANGE_USERNAME)) {
				this.changeName();
			}
		}
	}
	
	public void changeName(){
		
		for(Server s: ApplicationState.getInstance().getServerList()){
			if(s.isConnected()){
				new UserChangeName(s, this);
			}
		}
		
	}

	@Override
	public List<ActionIDCarrier> canSupply() {
		return canSupply;
	}

	@Override
	public Server getData(ActionID action) throws UnsupportedActionException {
		return s;
	}
	
	@Override
	public int getPerformIDNumber(ActionIDCarrier action) {
		return canPerform.get(canPerform().indexOf(action)).getIDNumber();
	}
	
	@Override
	public int getSupplyIDNumber(ActionIDCarrier action) {
		return canSupply().get(canSupply().indexOf(action)).getIDNumber();
	}
}
