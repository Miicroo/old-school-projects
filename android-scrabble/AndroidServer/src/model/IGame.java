package model;

import java.sql.ResultSet;

import util.Player;
import util.WordObject;
import model.data.Board;

/**
 * An interface describing a game and it's states.
 * 
 * @author Marika
 *
 */
public interface IGame {
	
	/**
	 * @return returns the game board
	 */
	public Board getBoard();
	
	/**
	 * start a game and sets information about the players
	 * 
	 * @return true if the game is started without errors
	 */
	public boolean startGame();
	
	/**
	 * Gives point for the word put as parameter s to the player how have played the word
	 * 
	 * @param s - the word played
	 */
	public void receivePoints(String s);
	
	/**
	 * Generate new letters to the player after a word has been played
	 * 
	 * @param i - the amount of letters played
	 * @return false if the game is in end game state
	 */
	public boolean generateLetters(int i);
	
	/**
	 * Adds new letters to the player how has played a word
	 * 
	 * @param set - ResultSet containing generated letters
	 */
	public void addLettersToPlayer(ResultSet set);
	
	/**
	 * Changes the player turn
	 */
	public void changeTurn();
	
	/**
	 * @return true if the pass was allowed
	 */
	public boolean pass();
	
	/**
	 * Places a word to the board
	 * 
	 * @param word - the word played
	 * @return false if the game is in end game state
	 */
	public boolean placeWord(WordObject word);
	
	/**
	 * @return player object representing player1
	 */
	public Player getPlayer1();
	
	/**
	 * @return player object representing player2
	 */
	public Player getPlayer2();
	
	/**
	 * sets how many letters there's left in the bank
	 * 
	 * @param i - a number of letters
	 */
	public void setLettersLeft(int i);
	
	/**
	 * @return how many letters there's left in the bank
	 */
	public int getLettersLeft();
	
	/**
	 * @return how many times in a row pass has been played
	 */
	public int getPass();
	
	/**
	 * Sets how many times in a row pass has been played
	 * 
	 * @param i - a number of passed turns
	 */
	public void setPass(int i);
	
	/**
	 * @param i - the starting number of letters in the bank
	 */
	void setStartingLettersLeft(int i);
}
