package util;

import java.io.Serializable;
import java.util.List;

/**
 * An object representing a word, it's start position on the board and it's direction on the board
 */
public class WordObject implements Serializable{

	private static final long serialVersionUID = 6609466368232479780L;
	List<String> word = null;
	int x = -1;
	int y = -1;
	Direction direction = null;
	
	public WordObject(List<String> word, int x, int y, Direction direction) {
		super();
		this.word = word;
		this.x = x;
		this.y = y;
		this.direction = direction;
	}

	
	/**
	 * @return the words
	 */
	public List<String> getWord() {
		return word;
	}


	/**
	 * Sets the words
	 * 
	 * @param word - words
	 */
	public void setWord(List<String> word) {
		this.word = word;
	}


	/**
	 * @return starting x position
	 */
	public int getX() {
		return x;
	}


	/**
	 * @param x - starting x position
	 */
	public void setX(int x) {
		this.x = x;
	}


	/**
	 * @return starting y position
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y - starting y position
	 */
	public void setY(int y) {
		this.y = y;
	}


	/**
	 * @return direction of the word
	 */
	public Direction getDirection() {
		return direction;
	}


	/**
	 * @param direction - direction of the word
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}


	public enum Direction{
		VERTICAL,
		HORIZONTAL
	}
}
