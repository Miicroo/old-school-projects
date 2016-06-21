package model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



/**
 * Class that manages all database storage.
 * The database can connect to a MySQL-database and execute queries.
 *
 */
public class Database {

	/**
	 * Class that keeps track of all the constants
	 * used to manage the database.
	 * 
	 */
	public static final class DbConstants{
		public static final String serverName = "localhost";
		public static final String database = "android-scrabble";
		public static final String username = "root"; 
		public static final String password = "";
	}
	
	/**
	 * enum that holds variables to see if
	 * the database is connected or not.
	 * 
	 */
	enum Status{
		CONNECTED,
		NOT_CONNECTED;
	}
	
	private Connection connection = null;	//	Database connection
	private Status status = null;
	
	/**
	 * static query variables, if query is executed ok or not
	 */
	public static final int QUERY_OK = 1;
	public static final int QUERY_NOT_OK = 0;
	
	/**
	 * Create new database and connect it to MySQL.
	 */
	public Database(){
		connect();
	}
	
	/**
	 * Connects to MySQL.
	 */
	public void connect(){ 
		try { 
			// Load the JDBC driver 
			String driverName = "org.gjt.mm.mysql.Driver"; // MySQL MM JDBC driver 
			Class.forName(driverName); 
	
			// Create a connection to the database
			String serverName = DbConstants.serverName;
			String database = DbConstants.database; 
			String url = "jdbc:mysql://" + serverName + "/" + database; // a JDBC url 
			String username = DbConstants.username; 
			String password = DbConstants.password; 
			connection = DriverManager.getConnection(url, username, password);
			status = Status.CONNECTED;
		}
		catch(ClassNotFoundException e){
			// Could not find the database driver 
			System.out.println("Could not find the database driver: "+e.getMessage());
			status = Status.NOT_CONNECTED;
		}
		catch(SQLException e){ 
			// Could not connect to the database
			System.out.println("Could not connect to the database: "+e.getMessage());
			status = Status.NOT_CONNECTED;
		}
	}
	
	/**
	 * The Status whether the database is connected or not
	 * @return
	 */
	public Status getStatus(){
		return status;
	}
	
	/**
	 * Executes a SELECT query.
	 * @param query the query to be executed
	 * @return the ResultSet that is returned from the query
	 * @throws SQLException
	 */
	public ResultSet execQuery(String query) throws SQLException{
		
		if(status != Status.CONNECTED){
			System.out.println("NOT CONNECTED");
			return null;
		}
		
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		
		return rs;
	}
	
	/**
	 * Executes an update query (UPDATE, DELETE, INSERT INTO...)
	 * 
	 * @param query the query to be executed
	 * @return int, QUERY_OK or QUERY_NOT_OK
	 * @throws SQLException
	 */
	public int execUpdate(String query) throws SQLException{
		
		if(status != Status.CONNECTED){
			System.out.println("NOT CONNECTED");
			return -1;
		}
		
		Statement stmt = connection.createStatement();
		int val = stmt.executeUpdate(query);
		
		return val;
	}
	
	/**
	 * Inserts player names in a specific game into the database
	 * 
	 * @param s - name of the hosting player
	 * @param s2 - name of the opponent
	 * @return true if the insert has no errors
	 */
	public boolean startGame(String s, String s2){
		String insGame = "INSERT INTO game VALUES(NULL, '"+ s +"', '"+ s2 +"')";
		
		try{
			execUpdate(insGame);
			return true;
		}
		catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * Randomly selects the amount of letters put as parameter i
	 * 
	 * @param i - how many letters has been played
	 * @return A ResultSet containing the selected letters, returns null if not successful 
	 */
	public ResultSet generateLetters(int i){
		String query = "SELECT 'char' FROM english ORDER BY RAND() LIMIT " + i;
		try {
			return execQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	/**
	 * Count the amount of points that the word put as parameter s gives
	 * 
	 * @param s - the word that has been played
	 * @return the points for the word played
	 */
	public int countPoints(String s){
		int points = 0;
		s = s.toLowerCase();
		
		for(int i = 0; i<s.length(); i++){
			String query = "SELECT points FROM english WHERE 'char' = '"+s.charAt(i)+"' LIMIT 1";
			
			try {
				ResultSet set = execQuery(query);
				if(set.next()){
					points += Integer.parseInt(set.getString("points"));
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return points;
	}
}