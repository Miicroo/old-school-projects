package model.data;

import java.io.Serializable;

/**
 * Representation of a board
 * 
 *
 */
public class Board implements Serializable{
	
	private static final long serialVersionUID = 8829664380725884359L;
	private char[][] board = null;

	/**
	 * Initialize an empty board.
	 */
	public Board(){
		board = new char[15][15];
	}
	
	/**
	 * adds a character to the board
	 * 
	 * @param c - the character to be added
	 * @param x - the x position on the board
	 * @param y - the y position on the board
	 */
	public void addLetter(char c, int x, int y){
		board[x][y] = c;
	}
	
	/**
	 * @return board
	 */
	public char[][] getBoard() {
		return board;
	}
	
	public char getCharAt(int x, int y){
		return board[x][y];
	}
}
