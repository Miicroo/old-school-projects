package util;

/**
 * Wrapper class for ActionID. Contains support for multiple suppliers.
 * 
 * NOTE! This class is at the moment for future implementation only.
 * The control-system now works as it did before revision 513. 
 * 
 * @author Wånge
 */
public class ActionIDCarrier {
	
	private ActionID id;
	
	public ActionIDCarrier(ActionID i) {
		id = i;
	}
	
	public ActionIDCarrier(ActionID i, int number) {
		this(i);
		iDNumber = number;
	}
	
	public ActionID getId() {
		return id;
	}

	public void setId(ActionID id) {
		this.id = id;
	}
	
	public int getIDNumber() {
		return iDNumber;
	}
	
	public void setIDNumber(int newID) {
		if(newID == -1) {
			iDNumber = ALL;
		} else {
			iDNumber = newID;
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null) {
			return false;
		}
		
		if(o instanceof ActionIDCarrier) {
			return id == ((ActionIDCarrier)o).getId();
		} else {
			return false;
		}	
	}
	
	public int hashCode() {
		return id.hashCode();
	}
	
	public static final int ALL = 1111111311;
	private int iDNumber = ALL;
}
