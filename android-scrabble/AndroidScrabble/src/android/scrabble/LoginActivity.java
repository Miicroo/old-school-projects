package android.scrabble;

import java.util.Observable;
import java.util.Observer;

import model.LoginModel;
import model.data.UserData;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener, Observer{
    
	private LoginModel model;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        model = new LoginModel();
        model.addObserver(this);
        
        Button submit = (Button)(findViewById(R.id.submit));
        submit.setOnClickListener(this);
    }
    
    @Override
	public void onClick(View v){
        EditText input = (EditText)(findViewById(R.id.username));
        model.sendLoginRequest(input.getText().toString());
	}
		
	public void evaluate(int responseCode){
		
        EditText input = (EditText)(findViewById(R.id.username));
        String uName = input.getText().toString();

    	if(responseCode == LoginModel.LOGIN_SIGN_UP){
    		Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
    		intent.putExtra("USERNAME", uName);
    		
    		startActivity(intent);
    		//model.dispose();
    		finish();
    	}
    	else if(responseCode == LoginModel.LOGIN_NOT_OK){
    		LoginActivity.this.runOnUiThread(new Runnable() {
    			public void run(){
    				showLogOutDialog("There was an error logging in. If you are logged in on another device, please logout there first.");
    			}
    		});
    	}
    	else if(responseCode == LoginModel.LOGIN_OK){
    		UserData.getInstance().setUsername(uName);
    		LoginActivity.this.runOnUiThread(new Runnable() {
    			public void run(){
    				showMessage("You are now logged in!");
    			}
    		});
    		finish();
    	}
    	else{
    		// TODO: take care of errors
    	}
	}
	
	public void showMessage(String s){
    	Context context = getBaseContext();
    	CharSequence text = s;
    	int duration = Toast.LENGTH_LONG;

    	Toast toast = Toast.makeText(context, text, duration);
    	toast.show();
    }
	
	public void showLogOutDialog(String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
		builder.setMessage(message);
		builder.setPositiveButton("Try to log me out!", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface di, int arg0) {
				
		        EditText input = (EditText)(findViewById(R.id.username));
		        String uName = input.getText().toString();
		        
				Intent intent = new Intent(LoginActivity.this, LogoutActivity.class);
	    		intent.putExtra("USERNAME", uName);
	    		
	    		startActivity(intent);
			}
		});
		builder.setNegativeButton("No thanks, ill solve it!", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface di, int arg0){
				di.dismiss();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
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
	public void update(Observable observable, Object data) {
        evaluate(((Integer)data));
	}
}