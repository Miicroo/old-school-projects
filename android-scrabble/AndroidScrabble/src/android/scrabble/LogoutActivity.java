package android.scrabble;

import java.util.Observable;
import java.util.Observer;

import model.LogoutModel;
import model.data.UserData;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

public class LogoutActivity extends Activity implements Observer {

	private LogoutModel model;
	//private boolean finishActivity = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logout);

		model = new LogoutModel();
		model.addObserver(this);

		String username = getIntent().getStringExtra("USERNAME");

		if (username == null) {
			finish();
		} else {
			model.sendLogoutRequest(username);
		}
	}

	public void showLoggedOutDialog(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				LogoutActivity.this);
		builder.setMessage(message);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				//finishActivity = true;
				arg0.dismiss();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				finish();
				
			}
		});
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
		// Another activity is taking focus (this activity is about to be
		// "paused").
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
	public void update(Observable observable, final Object data) {
		UserData.getInstance().setUsername("");
		LogoutActivity.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				showLoggedOutDialog(data.toString());	
			}
		});
		
	}

	public void debug(String s) {
		Context context = getBaseContext();
		CharSequence text = s;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
}