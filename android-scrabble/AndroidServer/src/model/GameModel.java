package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.data.Board;
import model.data.ErrorHandler;

import util.Player;
import util.WordObject;
import util.WordObject.Direction;

/**
 * A model class that describes all states and game specific details for one game.
 */
public class GameModel extends Logic implements IGame{
	
	private Database db = null;
	private int lettersLeft = 0;
	private int pass = 0;
	private Board board = null;
	private Player p1 = null;
	private Player p2 = null;
	
	
	/**
	 * Creates the board, player objects and sets game variables
	 * 
	 * @param name1 - host name
	 * @param name2 - opponent name
	 */
	public GameModel(String name1, String name2){
		super();
		db = getDatabase();
		board = new Board();
		p1 = new Player(name1);
		p2 = new Player(name2);
		setStartingLettersLeft(300);
	}
	
	@Override
	public Board getBoard(){
		return board;
	}
	
	@Override
	public boolean startGame() {
		if(db.startGame(p1.getUsername(), p2.getUsername())){
			p1.setTurn(true);
			return true;
		}
			return false;
	}
	
	@Override
	public boolean generateLetters(int i) {
		ResultSet set = null;
		
		if(getLettersLeft() >= i){
			set = db.generateLetters(i);
			if(p1.isTurn()){
				p1.setNrLetters(7);
			}
			else {
				p2.setNrLetters(7);
			}
			setLettersLeft(i);
		}
		else if(getLettersLeft() != 0){
			set = db.generateLetters(getLettersLeft());
			if(p1.isTurn()){
				p1.setNrLetters(p1.getNrLetters()-i+getLettersLeft());
			}
			else {
				p2.setNrLetters(p2.getNrLetters()-i+getLettersLeft());
			}
			setLettersLeft(0);
		}
		else{
			if(p1.getNrLetters() == 0 || p2.getNrLetters() == 0 || p1.getNrLetters()-i == 0 || p2.getNrLetters()-1 == 0){
				p1.setNrLetters(0);
				p2.setNrLetters(0);
				p1.setLetters(null);
				p2.setLetters(null);
				return false;
			}
			else{
				if(p1.isTurn()){
					p1.setNrLetters(p1.getNrLetters()-i);
				}
				else {
					p2.setNrLetters(p2.getNrLetters()-i);
				}
			}
		}
		addLettersToPlayer(set);
		return true;
	}
	
	@Override
	public void addLettersToPlayer(ResultSet set){
		List<Character> letters = new ArrayList<Character>();
		try {
			if(set != null){
				while(set.next()){
					letters.add(set.getString("char").charAt(0));
					if(p1.isTurn()){
						p1.setLetters(letters);
					}
					else {
						p2.setLetters(letters);
					}
				}
			}
		} catch (SQLException e) {
			ErrorHandler.report("The following SQL-error(s) occured while trying to log in GameLogic#generateLetters(): "+ e.getMessage());
		}
	}
	
	@Override
	public void receivePoints(String s) {
		pass = 0;
		
		if(p1.isTurn()){
			p1.addPoints(db.countPoints(s));
		}
		else{
			p2.addPoints(db.countPoints(s));
		}
	}
	
	@Override
	public void changeTurn(){
		if(p1.isTurn()){
			p1.setTurn(false);
			p2.setTurn(true);
		}
		else{
			p1.setTurn(true);
			p2.setTurn(false);
		}
	}
	
	@Override
	public boolean pass(){
		setPass(getPass()+1);
		if(getPass() == 4){
			return false;
		}
		else{
			changeTurn();
			return true;
		}
	}
	
	@Override
	public boolean placeWord(WordObject word) {
		String w = word.getWord();
		int size = w.length();
		int iterator = 0;
		int x = word.getX();
		int y = word.getY();
		
		if(word.getDirection() == Direction.VERTICAL){
			while(iterator < size){
				board.addLetter(w.charAt(iterator), x, y);
				y++;
				iterator++;
			}
		}
		else if(word.getDirection() == Direction.HORIZONTAL){
			while(iterator <= size){
				board.addLetter(w.charAt(iterator), x, y);
				x++;
				iterator++;
			}
		}
		receivePoints(w);
		boolean b = generateLetters(size);
		if(b){
			changeTurn();
		}
		return b;
	}
	
	@Override
	public Player getPlayer1(){
		return p1;
	}
	
	@Override
	public Player getPlayer2(){
		return p2;
	}

	@Override
	public void setLettersLeft(int i){
		lettersLeft -= i;
	}
	
	@Override
	public void setStartingLettersLeft(int i) {
		lettersLeft = i;
	}

	@Override
	public int getLettersLeft() {
		return lettersLeft;
	}

	@Override
	public int getPass() {
		return pass;
	}

	@Override
	public void setPass(int i) {
		pass = i;
	}
}