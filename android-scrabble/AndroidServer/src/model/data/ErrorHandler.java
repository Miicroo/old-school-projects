package model.data;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.Database;

/**
 * Class for error handling.
 * Connects to the database and
 * saves the error.
 *
 */
public class ErrorHandler {
	
	private final static Database db = new Database();

	/**
	 * Reports the error to the database.
	 * 
	 * @param error the error that is being logged.
	 */
	public static void report(String error){
		String date = getDateNow();
		System.err.println(error+" @ "+date);
		String q = "INSERT INTO error VALUES(NULL, '"+error+"', '"+date+"')";
		
		try{
			db.execUpdate(q);
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * @return current date
	 */
	private static String getDateNow(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}