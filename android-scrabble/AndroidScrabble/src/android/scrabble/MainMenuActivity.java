package android.scrabble;

import java.util.ArrayList;
import java.util.Collections;

import network.ClientInput;

import controller.NetworkController;

import model.GameListModel;
import model.data.UserData;

import util.OpponentData;
import util.OpponentDataAdapter;
import util.ResponseObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenuActivity extends Activity implements OnClickListener, OnMenuItemClickListener{
	
	private NetworkController controller = null;
	private boolean gamesListLoaded = false;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        controller = NetworkController.getInstance(getString(R.string.serverip));
        new ClientInput(controller).start();

        Button settings = (Button)(findViewById(R.id.settingsButton));
        settings.setOnClickListener(this);
        
        Button help = (Button)(findViewById(R.id.helpButton));
        help.setOnClickListener(this);
        
        Button about = (Button)(findViewById(R.id.aboutButton));
        about.setOnClickListener(this);
        
        Button newGame = (Button)(findViewById(R.id.newGameButton));
        newGame.setOnClickListener(this);
        
        Button updateGamesList = (Button)(findViewById(R.id.update_gameslist));
        updateGamesList.setOnClickListener(this);
    }

    /*
     * Create a menu and set a listener to the "logout"-button
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        menu.getItem(0).setOnMenuItemClickListener(this);
        return true;
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
        
        //Check if the user is logged in every time the main menu resumes
        if(UserData.getInstance().getUsername().equals("")){
        	startLoginScreen();
        	UserData.getInstance().killSocket();
        }
        else{
        	if(!gamesListLoaded){
        		loadGamesList();
        		gamesListLoaded = true;
        	}
        	if(!UserData.getInstance().isIntantiated()){
        		UserData.getInstance().init(getBaseContext(), getBaseContext().getString(android.scrabble.R.string.serverip));
        	}
        }
        
        updateLocale();	//	update text depending on language
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
    protected void onDestroy(){
        super.onDestroy();
        // The activity is about to be destroyed.
    }
    
    //	Update the text on the "logout"-button when the menu is opened
    public boolean onMenuOpened(int featureId, Menu menu){
		menu.getItem(0).setTitle(getString(R.string.logout));
    	return true;
    }

	@Override
	public void onClick(View view){
		
		switch(view.getId()){
			case R.id.settingsButton:
				startActivity(new Intent(MainMenuActivity.this, SettingsViewActivity.class));
				break;
			case R.id.helpButton:
				startActivity(new Intent(MainMenuActivity.this, HelpViewActivity.class));
				break;
			case R.id.aboutButton:
				startActivity(new Intent(MainMenuActivity.this, AboutViewActivity.class));
				break;
			case R.id.newGameButton:
				startActivity(new Intent(MainMenuActivity.this, GameSettingsActivity.class));
				break;
			case R.id.update_gameslist:
				//loadGamesList();
				break;
		}
	}
	
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if(keyCode == KeyEvent.KEYCODE_BACK){ 
        	// DONT KILL THE APP HERE
        	return true;
        }
        else{
        	return super.onKeyDown(keyCode, event);
        }
    } 

	@Override
	public boolean onMenuItemClick(MenuItem item){
		switch(item.getItemId()){
			case R.id.logout:
				Intent intent = new Intent(MainMenuActivity.this, LogoutActivity.class);
				intent.putExtra("USERNAME", UserData.getInstance().getUsername());
				
				startActivity(intent);
				break;
		}
		return true;
	}
	
	/*
	 * Update all button-texts and so on, with the proper language
	 */
	public void updateLocale(){
		TextView header = ((TextView)(findViewById(R.id.appName)));
		header.setText(getString(R.string.app_name));
		
		Button newGame = ((Button)(findViewById(R.id.newGameButton)));
		newGame.setText(getString(R.string.new_game));

		Button settings = ((Button)(findViewById(R.id.settingsButton)));
		settings.setText(getString(R.string.settings));

		Button help = ((Button)(findViewById(R.id.helpButton)));
		help.setText(getString(R.string.help));

		Button about = ((Button)(findViewById(R.id.aboutButton)));
		about.setText(getString(R.string.about));
		
		Button updateGamesList = ((Button)(findViewById(R.id.update_gameslist)));
		updateGamesList.setText(getString(R.string.update_gameslist));
	}
	
	/*
     * Get the users running games.
     */
	public void loadGamesList(){
		
        ListView listView = ((ListView)findViewById(R.id.gamesView));
        ArrayList<OpponentData> data = new ArrayList<OpponentData>();
        OpponentDataAdapter adapter = new OpponentDataAdapter(this, R.layout.row, data);
        
        /*
         * Get the information from the server 
         */
        GameListModel glModel = new GameListModel(getApplicationContext());
        ResponseObject o = glModel.getOpponentData(UserData.getInstance().getUsername());
        OpponentData[] od = ((OpponentData[])o.getObject());
        
        /*
         * Add server-data to the List
         */
        Collections.addAll(data, od);

        
        listView.setAdapter(adapter);
	}
	
	/*
	 * Start the login-screen every time the user isn't logged in
	 */
	public void startLoginScreen(){
		startActivity(new Intent(MainMenuActivity.this, LoginActivity.class));
	}
	
	public void debug(String s){
	    Context context = getBaseContext();
	    CharSequence text = s;
	    int duration = Toast.LENGTH_SHORT;

	    Toast toast = Toast.makeText(context, text, duration);
    	toast.show();
	 }
}