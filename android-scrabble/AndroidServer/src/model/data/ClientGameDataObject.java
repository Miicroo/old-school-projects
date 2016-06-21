package model.data;

import java.io.Serializable;

import util.Player;
import util.WordObject;

/**
 * An object that represents game data (players, board, id and turn)
 */
public class ClientGameDataObject implements Serializable{

	private static final long serialVersionUID = 5410058267505412927L;
	private Player p1 = null;
	private Player p2 = null;
	private WordObject wo = null;
	private int swap = -1;
	private int gameID = -1;
	
	/**
	 * @param p1 - player1
	 * @param p2 - Player2
	 * @param wo - a wordObject
	 * @param swap - how many letters to swap
	 * @param i - game ID
	 */
	public ClientGameDataObject(Player p1, Player p2, WordObject wo, int swap, int i){
		this.p1 = p1;
		this.p2 = p2;
		this.wo = wo;
		this.swap = swap;
		gameID = i;
	}
	
	/**
	 * @return player1
	 */
	public Player getP1() {
		return p1;
	}

	/**
	 * @return player2
	 */
	public Player getP2() {
		return p2;
	}

	/**
	 * @return WordObject that has been played
	 */
	public WordObject getWo() {
		return wo;
	}

	/**
	 * @return how many letters to be swaped
	 */
	public int getSwap(){
		return swap;
	}
	
	/**
	 * @return ID of the game
	 */
	public int getGameID() {
		return gameID;
	}
}
