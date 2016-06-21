package android.scrabble;

import java.util.Observable;
import java.util.Observer;

import model.SignupModel;
import model.data.UserData;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignupActivity extends Activity implements OnClickListener, Observer{
	
	private SignupModel model = null;
	private boolean finishActivity = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        
        String username = getIntent().getStringExtra("USERNAME");
        setTakenName(username, "not");
        
        model = new SignupModel();
        model.addObserver(this);
    	
    	Button submit = (Button)(findViewById(R.id.submit));
    	submit.setOnClickListener(this);
    }

	@Override
	public void onClick(View v){
        EditText input = (EditText)(findViewById(R.id.username));
        
        if(input.getText().toString().trim().equals("") || input.getText().toString().trim().equals(" ")){
        	setTakenName(input.getText().toString(), "already");
        }
        else{
        	model.sendLoginRequest(input.getText().toString());
        }
    }
	
	public void setTakenName(String username, String exist){
        TextView nameTaken = (TextView)(findViewById(R.id.nameTaken));
    	nameTaken.setText("The username "+username+" does "+exist+" exist!");
	}

	public void evaluate(int resCode){
		
        if(resCode == SignupModel.SIGNUP_NOT_OK){
            EditText input = (EditText)(findViewById(R.id.username));
            setTakenName(input.getText().toString(), "already");
        }
        else if(resCode == SignupModel.SIGNUP_OK){
            EditText input = (EditText)(findViewById(R.id.username));
            String uName = input.getText().toString();
            
        	UserData.getInstance().setUsername(uName);
    		
        	SignupActivity.this.runOnUiThread(new Runnable() {
    			public void run(){
    				showSignedUpDialog("You are now signed up! Welcome!");
    			}
    		});
        	while(!finishActivity){
        	}
    		finishActivity = false;
        	finish();
        }
        else{
        	//TODO: Take care of error
        }
	}
	
	public void showSignedUpDialog(String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
		builder.setMessage(message);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface arg0, int arg1){
				finishActivity = true;
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
	public void update(Observable observable, Object data){
    	evaluate((Integer)(data));
	}
}