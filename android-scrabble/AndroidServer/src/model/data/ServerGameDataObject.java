package model.data;

import util.Player;

/**
 * An object that represents game data (players, board, id and turn)
 */
public class ServerGameDataObject {
	
	private Player p1 = null;
	private Player p2 = null;
	private String turn = null;
	private Board board = null;
	private int gameID = -1;
	
	/**
	 * @param p1 - player1
	 * @param p2 - Player2
	 * @param s - whose turn it is
	 * @param b - the board
	 * @param i - game ID
	 */
	public ServerGameDataObject(Player p1, Player p2, String s, Board b, int i){
		this.p1 = p1;
		this.p2 = p2;
		turn = s;
		board = b;
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
	 * @return username of the player whose turn it is
	 */
	public String getTurn() {
		return turn;
	}

	/**
	 * @return the board
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * @return ID of the game
	 */
	public int getGameID() {
		return gameID;
	}
}
