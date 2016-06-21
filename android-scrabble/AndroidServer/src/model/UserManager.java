package model;

import java.sql.ResultSet;
import java.sql.SQLException;

import model.data.ErrorHandler;

import util.OnlineList;
import util.ResponseObject;
import util.SendableAction;


/**
 * Logic-class that contains all user-based logics.
 * 
 */
public class UserManager extends Logic{

	private Database db = null;
	
	/**
	 * Creates new UserLogic from existing Logic.
	 */
	public UserManager(){
		super();
		db = getDatabase();	//	Get the inherited database.
	}
	
	/**
	 * Logs a user in. The user is successfully logged in if it isn't already logged in
	 * on another device yet, and exists in the database. A lot of SQLExceptions might occur
	 * and all of the are reported to the ErrorHandler. The processed data is sent back to the Controller 
	 * when finished.
	 * 
	 * @param username the name of the user that wants to be logged in.
	 * @return 
	 */
	public ResponseObject login(String username){
		
		String result = "";	//	The result sent back to the Controller.
		
		boolean isOnline = false;
		try{
			isOnline = isOnline(username);	//	Checks if the user is online.
		}
		catch(SQLException e){	// Report errors!!
			result = "The following SQL-error(s) occured while trying to log in UserLogic#login(): "+e.getMessage();
			ErrorHandler.report(result);
		}
		
		if(!isOnline){
			String query = "SELECT * FROM player WHERE name = '"+username+"'";
			
			try{
				ResultSet set = db.execQuery(query);
				set.last();
				if(set.getRow() > 0){	//	if the query isn't empty, the user exists
					int logIn = setLogin(username, true);	// set player to be online
					
					if(logIn != Database.QUERY_OK){	//	if something went wrong with the query, report error.
						result = "Could not set your login-status, query is QUERY_NOT_OK. Check if username exists?";
						ErrorHandler.report(result);
					}
					else{	// Everything is checked and fine.
						result = "You are now logged in as "+username;
					}
				}
				else{	// the query is empty, i.e. the user doesn't exist.
					result = "The requested username "+username+" does not exist. Please sign up!";
				}
			}
			catch(SQLException e){	//	report all sql errors that might have occurred
				result = "The following SQL-error(s) occured while trying to log in UserLogic#login(): "+e.getMessage();
				ErrorHandler.report(result);
			}
		}
		else{	//	If the user is already logged in, do the following...
			result = username+" is already logged in on another device";
		}
		
		ResponseObject obj = new ResponseObject(SendableAction.LOGIN, result);
		return obj;
	}
	
	/**
	 * Logs a user out. The user is successfully logged out if no SQL errors occur.
	 * 
	 * @param username the name of the user that is being logged out.
	 * @return 
	 */
	public ResponseObject logout(String username){
		
		String result = "";
		int logIn = setLogin(username, false);	// set player to be offline
		
		if(logIn != Database.QUERY_OK){	//	if something went wrong with the query, report error.
			result = "Could not set your log out status, query is QUERY_NOT_OK. Check if username exists?";
			ErrorHandler.report(result);
		}
		else{	// Everything is checked and fine.
			result = "You are now logged out";
		}
		
		ResponseObject obj = new ResponseObject(SendableAction.LOGOUT, result);
		return obj;
	}

	/**
	 * Signs the user up. If the username is already taken, the method will end in return
	 * an error. The result ('you are signed up' OR 'username taken') will be returned to the
	 * Controller.
	 * 
	 * @param username the username that the user wants to register.
	 * @return 
	 */
	public ResponseObject signUp(String username){
		String result = "";
		String query = "SELECT * FROM player WHERE name = '"+username+"'";
		
		try{
			ResultSet set = db.execQuery(query);
			set.last();
			if(set.getRow() > 0){	//	username is taken
				result = "Sorry, the username "+username+" is already taken. Please choose another one.";
			}
			else{	//	username is ok.
				String msq = "INSERT INTO player VALUES(NULL, '"+username+"', 1)";
				int res = db.execUpdate(msq);
				if(res == Database.QUERY_NOT_OK){	//	Db returned error from the query
					result = "Could not perform query \""+msq+"\", returned status QUERY_NOT_OK in UserLogic#signUp()";
					ErrorHandler.report(result);
				}
				else{	//	sign up complete
					result = "You are now signed up, welcome "+username;
				}
			}
		}
		catch(SQLException e){	//	report all errors!
			ErrorHandler.report("The following SQL-error(s) occured in UserLogic#getUsersOnline(): "+e.getMessage());
		}

		ResponseObject obj = new ResponseObject(SendableAction.SIGN_UP, result);
		return obj;
	}

	/**
	 * Gets all users online.
	 * Returns the list of users to the Controller.
	 * @return 
	 * 
	 */
	public ResponseObject getUsersOnline(){
		
		ResponseObject object = new ResponseObject(SendableAction.PLAYERS_ONLINE, OnlineList.getInstance().getArray());
		return object;
	}
	
	/**
	 * Sets a certain users online mode to either true or false.
	 * 
	 * @param username	the name of the user which is updated
	 * @param b	true if the user should be logged in, false if it should be logged out.
	 * 
	 * @see Database#QUERY_OK
	 * @see Database#QUERY_NOT_OK
	 * 
	 * @return QUERY_OK if the requested operation was successful, otherwise QUERY_NOT_OK (strange query, SQL errors)
	 */
	private int setLogin(String username, boolean b){
		int online = (b ? 1 : 0);	//	turn boolean to 0 or 1
		String query = "UPDATE player SET isOnline = "+online+" WHERE name = '"+username+"'";
		
		int result = 0;
		
		try{
			result = db.execUpdate(query);	//	send update, int returned is if the query is QUERY_OK or QUERY_NOT_OK
		}
		catch(SQLException e){	//	report errors!
			ErrorHandler.report(e.getMessage());
			return Database.QUERY_NOT_OK;
		}
		return result;
	}
	
	/**
	 * Finds out if the user is online.
	 * 
	 * @param username
	 * @return true if the user is online, otherwise false.
	 * @throws SQLException SELECT-errors
	 */
	private boolean isOnline(String username) throws SQLException{
		String query = "SELECT * FROM player WHERE name = '"+username+"' AND isOnline = 1";
		
		ResultSet set = db.execQuery(query);	//	search if the user is online with query
		set.last();
		if(set.getRow() > 0){	//	if there is a row, the user is online
			return true;
		}
		else{	//	the user isn't online
			return false;
		}
	}
}