package model;

/**
 * Class that is extended by all logic-classes on the server.
 * The Logic-class handles stuff like the database.
 */
public abstract class Logic{

	private Database db;	//	the database that all logic-classes work against
	
	protected Logic(){
		db = new Database();	//	Open db.
	}

	public Database getDatabase(){
		return db;
	}
}