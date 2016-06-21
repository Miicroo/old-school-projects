package android.scrabble;

import java.util.Locale;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class SettingsViewActivity extends Activity  implements OnClickListener{
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_menu);

        initLangSpinner();
        
        Button save = ((Button)(findViewById(R.id.save)));
        save.setOnClickListener(this);
    }
	
    /**
     * Set possible languages
     */
	private void initLangSpinner() {
		Spinner languages = (Spinner)(findViewById(R.id.lang));
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.langs, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        int pos = getSelectedPosition(adapter);
        
        languages.setAdapter(adapter);
        languages.setSelection((pos < 0 ? 0 : pos));
	}

	private int getSelectedPosition(ArrayAdapter<CharSequence> adapter) {

		Configuration config = getBaseContext().getResources().getConfiguration();
		Locale l = config.locale;
		
		String country = getString(l);

        for(int i = 0; i<adapter.getCount(); i++){
        	if(adapter.getItem(i).equals(country)){
        		return i;
        	}
        }
        
        return -1;
	}

	private String getString(Locale l) {

		if(l.equals(Locale.ENGLISH)){
			return "English";
		}
		else if(l.equals(Locale.US)){
			return "English";
		}
		else if(l.equals(Locale.GERMAN)){
			return "Deutsch";
		}
		else if(l.equals(Locale.SIMPLIFIED_CHINESE)){
			return "Svenska";
		}
		else if(l.equals(Locale.TAIWAN)){
			return "Suomi";
		}
		else if(l.equals(Locale.ITALIAN)){
			return "Italiano";
		}
		else if(l.equals(Locale.FRENCH)){
			return "Française";
		}
		
		return null;
	}
	
	public Locale getLocale(String item){
		Locale locale = null;
		if(item.equalsIgnoreCase("English")){
			locale = Locale.ENGLISH;
		}
		else if(item.equalsIgnoreCase("Deutsch")){
			locale = Locale.GERMAN;
		}
		else if(item.equalsIgnoreCase("Svenska")){
			locale = Locale.SIMPLIFIED_CHINESE;
		}
		else if(item.equalsIgnoreCase("Suomi")){
			locale = Locale.TAIWAN;
		}
		else if(item.equalsIgnoreCase("Italiano")){
			locale = Locale.ITALIAN;
		}
		else if(item.equalsIgnoreCase("Française")){
			locale = Locale.FRENCH;
		}
		return locale;
	}
	
	public void setLocale(Locale locale){
		if(locale != null){
			Locale.setDefault(locale);
			Configuration config = getBaseContext().getResources().getConfiguration();
			if(!config.locale.equals(locale)){
				config.locale = locale;
				getBaseContext().getResources().updateConfiguration(config, null);
			}
		}
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
		
		Spinner l = ((Spinner)(findViewById(R.id.lang)));
		String item = (String)l.getSelectedItem();
		
		Locale locale = getLocale(item);
		setLocale(locale);
		
		finish();
	}
}