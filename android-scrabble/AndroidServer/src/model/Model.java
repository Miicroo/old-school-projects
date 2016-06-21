package model;

import java.util.HashMap;
import java.util.List;

import model.data.ServerGameDataObject;

import util.OnlineList;
import util.OpponentData;
import util.ResponseObject;
import util.SendableAction;
import util.WordObject;


/**
 * A class that manages all input a redirects it the the right class (GameModel, GameLogic or UserManager).
 * Creates a responsobject to be sent back to the client when a class has been handling the input data
 */
public class Model {

	private UserManager usermanager = null;
	private GameLogic gameLogic = null;
	private HashMap<Integer, GameModel> modelList;
	
	/**
	 * Creates a UserManager, GameLogic and a HashMap containing all GameModels
	 */
	public Model(){
		usermanager = new UserManager();
		gameLogic = new GameLogic();
		modelList = new HashMap<Integer, GameModel>();
	}
	
	/**
	 * @param gameID - the ID of a game
	 * @return a GameModel
	 */
	public GameModel getModel(int gameID){
		return modelList.get(gameID);
	}
	
	/**
	 * @return next GameID to be used
	 */
	private int nextID(){
		return modelList.size()+1;
	}

	/**
	 * Triggers the login function
	 * 
	 * @param s - username
	 * @return a responsobject with the processed data
	 */
	public ResponseObject login(String s) {
		return usermanager.login(s);
	}

	/**
	 * Triggers the logout function
	 * 
	 * @param s2 - username
	 * @return a responsobject with the processed data
	 */
	public ResponseObject logout(String s2) {
		return usermanager.logout(s2);
	}

	/**
	 * Triggers the signup function
	 * 
	 * @param s2 - username
	 * @return a responsobject with the processed data
	 */
	public ResponseObject signUp(String s3) {
		return usermanager.signUp(s3);
	}
	
	/**
	 * An update function that returns an object with all game data
	 * 
	 * @param gameID - the ID of the game
	 * @return responsobject with game data
	 */
	public ResponseObject update(int gameID){
		GameModel gm = modelList.get(gameID);
		ServerGameDataObject obj = new ServerGameDataObject(gm.getPlayer1(), gm.getPlayer2(), getTurn(gm), gm.getBoard(), gameID);
		return new ResponseObject(SendableAction.UPDATE, obj);
	}

	/**
	 * Triggers the pass function
	 * 
	 * @param gameID - the ID of the game
	 * @return a responsobject with the processed data
	 */
	public ResponseObject pass(int gameID) {
		GameModel gm = modelList.get(gameID);
		if(gm.pass()){
			ServerGameDataObject obj = new ServerGameDataObject(null, null, getTurn(gm), null, gameID);
			return new ResponseObject(SendableAction.PASS, obj);
		}
		else{
			return quitGame(gameID);
		}
	}

	/**
	 * 
	 * @param gameID - the ID of the game
	 * @return a responsobject with all gama data
	 */
	public ResponseObject quitGame(int gameID) {
		GameModel gm = modelList.get(gameID);
		ServerGameDataObject obj = new ServerGameDataObject(gm.getPlayer1(), gm.getPlayer2(), getTurn(gm), gm.getBoard(), gameID);
		return new ResponseObject(SendableAction.QUIT_GAME, obj);
	}

	/**
	 * Triggers the placeWord function
	 * 
	 * @param gameID - the ID of the game
	 * @return a responsobject with the processed data, second field of the responsobject is null if the word was invalid
	 */
	public ResponseObject placeWord(int gameID, WordObject wo) {
		GameModel gm = modelList.get(gameID);
		if(gameLogic.checkWord(wo)){
			if(gm.placeWord(wo)){
				ServerGameDataObject obj = new ServerGameDataObject(gm.getPlayer1(), gm.getPlayer2(), getTurn(gm), gm.getBoard(), gameID);
				return new ResponseObject(SendableAction.PLACE_WORD, obj);
			}
			else{
				return quitGame(gameID);
			}
		}
		else{
			return new ResponseObject(SendableAction.PLACE_WORD, null);
		}
	}

	/**
	 * Triggers the swap function
	 * 
	 * @param gameID - the ID of the game
	 * @return a responsobject with the processed data, second field of the responsobject is null if the swap was invalid
	 */
	public ResponseObject swap(int gameID, int i) {
		GameModel gm = modelList.get(gameID);
		if(gm.getLettersLeft() == 0){
			return new ResponseObject(SendableAction.SWAP, null);
		}
		else{
			gm.generateLetters(i);
			ServerGameDataObject obj = new ServerGameDataObject(gm.getPlayer1(), gm.getPlayer2(), getTurn(gm), gm.getBoard(), gameID);
			return new ResponseObject(SendableAction.SWAP, obj);
		}
	}

	
	/**
	 * Triggers the start game funtion
	 * 
	 * @param name1 - host name
	 * @param name2 - opponent name
	 * @return a responseobject with processed data
	 */
	public ResponseObject startGame(String name1, String name2) {
		int ID = nextID();
		modelList.put(ID, new GameModel(name1, name2));
		GameModel gm = modelList.get(ID);
		ServerGameDataObject obj = new ServerGameDataObject(gm.getPlayer1(), gm.getPlayer2(), getTurn(gm), gm.getBoard(), ID);
		return new ResponseObject(SendableAction.START_GAME, obj);
	}

	/**
	 * @return players online
	 */
	public ResponseObject getPlayersOnline() {
		return usermanager.getUsersOnline();
	}
	
	/**
	 * @param gm - a GameModel
	 * @return the name of the player whose turn it is
	 */
	private String getTurn(GameModel gm){
		if(gm.getPlayer1().isTurn()){
			return gm.getPlayer1().getUsername();
		}
		else{
			 return gm.getPlayer2().getUsername();
		}
	}
	
	/**
	 * @return a responsobject with all dictionaries available 
	 */
	public ResponseObject getDictionaries(){
		return new ResponseObject(SendableAction.GET_DICTIONARIES, new String[]{"English"});
	}

	
	/**
	 * Triggers the getOpponentData function
	 * 
	 * @param username - the user that is playing
	 * @return a responseobject with processed data
	 */
	public ResponseObject getOpponentData(String username) {
		List<String> opponents = gameLogic.getOpponentData(username);
		OpponentData[] opData = new OpponentData[opponents.size()];
		
		List<String> allOnline = OnlineList.getInstance().getList();
		for(int i = 0; i<opponents.size(); i++){
			if(allOnline.contains(opponents.get(i))){
				opData[i] = new OpponentData(opponents.get(i), true);
			}
			else{
				opData[i] = new OpponentData(opponents.get(i), false);
			}
		}
		
		return new ResponseObject(SendableAction.OPPONENT_DATA, opData);
	}

	/**
	 * Invites a player to a new game
	 * 
	 * @param host - username of the player sending the invite
	 * @return a responsobject with processed data
	 */
	public ResponseObject invitePlayer(String host) {
		return new ResponseObject(SendableAction.INVITE_GAME, new String[]{host, host+" vill spela ett spel med dig."});
	}
}