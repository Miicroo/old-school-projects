package android.scrabble;


import java.util.Observable;
import java.util.Observer;

import util.ResponseObject;

import model.GameModel;
import model.GameSettingsModel;

import util.WordObject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.app.Activity;
import android.graphics.Color;

public class GameBoardActivity extends Activity implements OnClickListener, Observer{
	
	private GameModel model = null;
	private TextView playerOne, playerTwo, playerOnePoints, playerTwoPoints; 
	private Button swapLetters, playWord, resignGame, pass, shuffle;
	private Button[] playerLetters;
    private Button[][] gameBoard;
    private char[][] yourPlacedLetters,
    				 oldGameBoard;
    
    private int letterId = -1;
	
	   @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        model = new GameModel();
	        model.addObserver(this);
	        
	        playerOne = new TextView(this);
	        playerTwo = new TextView(this);
	        playerOnePoints = new TextView(this);
	        playerTwoPoints = new TextView(this);
	        
	        swapLetters = new Button(this);
	        playWord = new Button(this);
	        resignGame = new Button(this);
	        pass = new Button(this);
	        shuffle = new Button(this);
	       
	        playerLetters = new Button[7];
	        gameBoard = new Button[15][15];
	        oldGameBoard = new char[15][15];
	        yourPlacedLetters = new char[15][15];
	        
	        for(int i = 0; i<15; i++){
	        	for(int j = 0; j<15; j++){
	        		yourPlacedLetters[i][j] = ' ';
	        		oldGameBoard[i][j] = ' ';
	        	}
	        }
	        
	        ScrollView mainScrollLayout = new ScrollView(this);
	        
	        HorizontalScrollView horScrollLayout = new HorizontalScrollView(this);
	        
	        TableLayout la = new TableLayout(this);
	        
	        TableLayout masterLayout = new TableLayout(this);
	                
	        //Adding the board to the main layout
	        la.addView(createBoardGrid());
	        la.addView(createCharGrid());
	        la.addView(createPlayerGrid());
	        la.addView(createButtonGrid());
	        
	        horScrollLayout.addView(la);
	        mainScrollLayout.addView(horScrollLayout);
	        
	        masterLayout.addView(mainScrollLayout);
	        
	        super.setContentView(masterLayout);
	    }
	   
	    public TableLayout createBoardGrid() {
	        TableLayout boardGrid = new TableLayout(this);

	        boardGrid.setLayoutParams(new TableLayout.LayoutParams());
	        
	        boardGrid.setPadding(1, 1, 1, 1);
	        
	        int x = 100;
   		 	int y = 100;
	        
   		 	for (int i = 0; i < 15; i++) {
	        	TableRow tr = new TableRow(this);
	        	for (int n = 0; n <15; n++) {
	        		 Button b = new Button(this);
	        		 
	        		 gameBoard[i][n] = b;
	        		 
	                 
	        		 //Setting the id for each button in the form of 1xx1yy
	        		 int xx = x + n;
	        		 int yy = y + i; 
	        		 gameBoard[i][n].setId(xx*1000+yy);
	        		 gameBoard[i][n].setOnClickListener(this);
	        		 
	        		 b.setText(" ");
	                 b.setTextSize(10.0f);
	                 b.setTextColor(Color.rgb(0, 0, 0));
	                 b.setOnClickListener(this);
	                 tr.addView(b, 30, 30);
	        	}
	        	boardGrid.addView(tr);
	        }
	    	return boardGrid;
	    }
	    
	    public TableLayout createCharGrid() {
	    	TableLayout charGrid = new TableLayout(this);
	        charGrid.setLayoutParams(new TableLayout.LayoutParams(30,30));
		        
	        charGrid.setPadding(1,1,1,1);

	        for (int i = 0; i < 1; i++) {
	        	TableRow tr = new TableRow(this);
	        	for(int n = 0; n < 7; n++) {
	        		Button b = new Button (this);
	        		
	        		playerLetters[n] = b;
	        		
	        		b.setId(n);        		
	        		b.setText(""+i+n); //Need to set the character here
	        		b.setTextSize(10.0f);
	        		b.setTextColor(Color.rgb(0, 0, 0));
	        		b.setBackgroundColor(Color.rgb(100, 149, 237));
	        		b.setOnClickListener(this);
	        		tr.addView(b, 30,30);
	        	}
	        charGrid.addView(tr);
	        }
	        return charGrid;
	    }
	    
	    public TableLayout createPlayerGrid() {
	    	TableLayout playerGrid = new TableLayout(this);
	    	playerGrid.setLayoutParams(new TableLayout.LayoutParams(30, 30));
	    	
	    	playerGrid.setPadding(1,1,1,1);

        	TableRow tr1 = new TableRow(this);
        	TableRow tr2 = new TableRow(this);
        	
        	playerOne.setText("Player1");	//Need to get the playerName from somewhere
    		playerOne.setTextSize(10.0f);
    		playerOne.setTextColor(Color.rgb(255, 255, 255));
    		tr1.addView(playerOne);
    		
    		playerOnePoints.setText(" 0");	//Need to get the playerOnePoints from somewhere
    		playerOnePoints.setTextSize(10.0f);
    		playerOnePoints.setTextColor(Color.rgb(255, 255, 255));
    		tr1.addView(playerOnePoints);
    		
    		playerTwo.setText("Player2");	//Need to get the playerName from somewhere
    		playerTwo.setTextSize(10.0f);
    		playerTwo.setTextColor(Color.rgb(255, 255, 255));
    		tr2.addView(playerTwo);
    		
        	playerTwoPoints.setText(" 0");	//Need to get the playerTwoPoints from somewhere
    		playerTwoPoints.setTextSize(10.0f);
    		playerTwoPoints.setTextColor(Color.rgb(255, 255, 255));
    		tr2.addView(playerTwoPoints);
    		
    		playerGrid.addView(tr1);
    		playerGrid.addView(tr2);
	    	
    		return playerGrid;
	    }
	    
	    public TableLayout createButtonGrid() {
	    	TableLayout buttonGrid = new TableLayout(this);
	        buttonGrid.setLayoutParams(new TableLayout.LayoutParams(30,30));
		        
	        buttonGrid.setPadding(1,1,1,1);
	        
	        TableRow tr = new TableRow(this);
	        
	        playWord.setText("Play word");
    		playWord.setTextSize(10.0f);
    		playWord.setTextColor(Color.rgb(0, 0, 0));
    		playWord.setOnClickListener(this);
    		playWord.setId(100);
    		playWord.setOnClickListener(this);
    		tr.addView(playWord);
    		
    		pass.setText("Pass");
    		pass.setTextSize(10.0f);
    		pass.setTextColor(Color.rgb(0, 0, 0));
    		pass.setOnClickListener(this);
    		tr.addView(pass);
    		
    		shuffle.setText("Shuffle");
    		shuffle.setTextSize(10.0f);
    		shuffle.setTextColor(Color.rgb(0, 0, 0));
    		shuffle.setOnClickListener(this);
    		tr.addView(shuffle);
    		
    		swapLetters.setText("Swap letters");
    		swapLetters.setTextSize(10.0f);
    		swapLetters.setTextColor(Color.rgb(0, 0, 0));
    		swapLetters.setOnClickListener(this);
    		tr.addView(swapLetters);
    		
    		resignGame.setText("Resign");
    		resignGame.setTextSize(10.0f);
    		resignGame.setTextColor(Color.rgb(0, 0, 0));
    		resignGame.setOnClickListener(this);
    		tr.addView(resignGame);
	        
    		buttonGrid.addView(tr);
    		
    		return buttonGrid;
	    }
	    
	    
	    /**This method sorts the array*/
	    /* We ended up not using it
	    public Button[] sortPlayerLetters(Button[] b) {
	    	Button[] temp = new Button[b.length];
	    	int n = 0;
	    	for (int i = 0; i < b.length; i++) {
	    		if (b[i].getText() != null) {
	    			temp[n].setText(b[i].getText());
	    			n++;
	    		}
	    	}
	    	playerLetters = temp;
	    	return playerLetters;
	    }
	    */
	    
	    /**This method adds new letters to the players*/
	    public WordObject sendPlacedWord() {
	    	return null;
	    }
	    
	    /**This method is used to set new characters when some have been played*/
	    public void setLetter(char c, int x) {
	    	playerLetters[x].setText(c);
	    }
	    
	    /**This method is used to set new characters on the board*/
	    public void setLetterOnBoard(char c, int x, int y) {
	    	gameBoard[x][y].setText(c);
	    }
	    
	    /** This method will check if the word is correctly placed */
		public boolean checkPlacement(char[][] c) {
			
			boolean empty = true;
			for(int i = 0; i<15; i++){
				for(int j = 0; j<15; j++){
					if(oldGameBoard[i][j] != ' '){
						empty = false;
						break;
					}
				}
			}
			
			if(empty){
				return true;
			}
			
			boolean result = true;
			int x1 = -1;
			int y1 = -1;
			boolean changeX = false;
			boolean changeY = false;

			for (int y = 0; y < 15; y++) {
				for (int x = 0; x < 15; x++) {
					if (c[y][x] == ' ') {
						// DO NOTHING LOL
					} else {
						// if x1 and y1 is set to a index in the c array
						if (x1 != -1 && y1 != -1) {
							// if x1 has a new value since the last loop and y1
							// haven't changed more than once
							if (x1 != x && !changeY) {
								changeX = true;
							}
							// if x1 has a new value since the last loop and y1 has
							// been changed more than once return false since not
							// allowed placement
							else if (x1 != x && changeY) {
								return false;
							}
							// if y1 has a new value since the last loop and x1
							// haven't changed more than once
							else if (y1 != y && !changeX) {
								changeY = true;
							}
							// if y1 has a new value since the last loop and x1 has
							// been changed more than once return false since not
							// allowed placement
							else if (y1 != y && changeX) {
								return false;
							}
						} else {
							x1 = x;
							y1 = y;
						}
						if (y == 0 && x == 0) {
							if (gameBoard[y + 1][x].getText().charAt(0) == ' '
									|| gameBoard[y][x + 1].getText().charAt(0) == ' ') {
								result = false;
							}
						} else if (y == 14 && x == 14) {
							if (gameBoard[y - 1][x].getText().charAt(0) == ' '
									|| gameBoard[y][x - 1].getText().charAt(0) == ' ') {
								result = false;
							}
						} else if (y == 0) {
							if (gameBoard[y + 1][x].getText().charAt(0) == ' '
									|| gameBoard[y][x + 1].getText().charAt(0) == ' '
									|| gameBoard[y][x - 1].getText().charAt(0) == ' ') {
								result = false;
							}
						} else if (y == 14) {
							if (gameBoard[y - 1][x].getText().charAt(0) == ' '
									|| gameBoard[y][x + 1].getText().charAt(0) == ' '
									|| gameBoard[y][x - 1].getText().charAt(0) == ' ') {
								result = false;
							}
						} else if (x == 0) {
							if (gameBoard[y + 1][x].getText().charAt(0) == ' '
									|| gameBoard[y - 1][x].getText().charAt(0) == ' '
									|| gameBoard[y][x + 1].getText().charAt(0) == ' ') {
								result = false;
							}
						} else if (x == 14) {
							if (gameBoard[y + 1][x].getText().charAt(0) == ' '
									|| gameBoard[y - 1][x].getText().charAt(0) == ' '
									|| gameBoard[y][x - 1].getText().charAt(0) == ' ') {
								result = false;
							}
						} else {
							if (gameBoard[y + 1][x].getText().charAt(0) == ' '
									|| gameBoard[y - 1][x].getText().charAt(0) == ' '
									|| gameBoard[y][x + 1].getText().charAt(0) == ' '
									|| gameBoard[y][x - 1].getText().charAt(0) == ' ') {
								result = false;
							}
						}
					}
				}
			}
			return result;
		}
	    
	    @Override
	    protected void onStart() {
	        super.onStart();
	        // The activity is about to become visible.
	    }
	    @Override
	    protected void onResume() {
	        super.onResume();
	        // The activity has become visible (it is now "resumed").
	    }
	    @Override
	    protected void onPause() {
	        super.onPause();
	        // Another activity is taking focus (this activity is about to be "paused").
	    }
	    @Override
	    protected void onStop() {
	        super.onStop();
	        // The activity is no longer visible (it is now "stopped")
	    }
	    @Override
	    protected void onDestroy() {
	        super.onDestroy();
	        // The activity is about to be destroyed.
	    }
		@Override
		public void onClick(View v) {
			if(v.getId() > 100000){	//	its a board button
				if(letterId >= 0){
					//	find x and y
					int y = (v.getId()/1000);
					int x = (v.getId()-(y*1000));
					x -= 100;
					y -= 100;
					
					// 	swap text
					Button b = ((Button)(findViewById(letterId)));
					gameBoard[x][y].setText(b.getText());
					yourPlacedLetters[x][y] = b.getText().charAt(0);
					
					b.setText("");
					gameBoard[x][y].invalidate();
					
					letterId = -1;
				}
			}
			else if(v.getId() < 7){
				letterId = v.getId();
			}
			else if(v.getId() == 100){	//	place word
				boolean ok = checkPlacement(yourPlacedLetters);
				Log.d("class", ok+"");
			}
		}

		@Override
		public void update(Observable observable, Object data) {
			final ResponseObject r = ((ResponseObject)data);
			
			
	        switch(r.getAction()){
	        	case PASS:
	        		GameBoardActivity.this.runOnUiThread(new Runnable() {
		    			public void run(){
		    				
		    			}
		    		});
	        		break;
	        	case PLACE_WORD:
	        		GameBoardActivity.this.runOnUiThread(new Runnable() {
		    			public void run(){
		    				
		    			}
		    		});
	        		break;
	        	case QUIT_GAME:
	        		GameBoardActivity.this.runOnUiThread(new Runnable() {
		    			public void run(){
		    				
		    			}
		    		});
	        		break;
	        	case SWAP:
	        		GameBoardActivity.this.runOnUiThread(new Runnable() {
		    			public void run(){
		    				
		    			}
		    		});
	        		break;
	        }
		}
	}