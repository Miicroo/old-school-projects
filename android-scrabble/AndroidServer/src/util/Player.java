package util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that represent a player
 */
public class Player implements Serializable{
	
	private static final long serialVersionUID = -3089302783006861156L;
	private int nrletters = -1;
	private int points = 0;
	private String username = null;
	private List<Character> letters = new ArrayList<Character>();
	private boolean turn = false;
	
	/**
	 * @param name - the username of the player
	 */
	public Player(String name){
		username = name;
	}
	
	/**
	 * @return true if it your turn
	 */
	public boolean isTurn() {
		return turn;
	}

	/**
	 * @param turn - true if set to your turn
	 */
	public void setTurn(boolean turn) {
		this.turn = turn;
	}

	/**
	 * @return the letters that the player has
	 */
	public List<Character> getLetters() {
		return letters;
	}

	/**
	 * Sets the players letters
	 * 
	 * @param letters - a list of characters
	 */
	public void setLetters(List<Character> letters) {
		this.letters = letters;
	}
	
	/**
	 * @return how many letters the player has
	 */
	public int getNrLetters() {
		return nrletters;
	}

	/**
	 * Sets how many letters the player has
	 * 
	 * @param lettersLeft - amount of letters
	 */
	public void setNrLetters(int lettersLeft) {
		this.nrletters = lettersLeft;
	}

	/**
	 * @return players username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * @return players points
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * Adds parameter points to the points the player already has
	 * 
	 * @param points - amount of points
	 */
	public void addPoints(int points) {
		this.points += points;
	}
}
