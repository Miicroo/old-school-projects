package android.scrabble.test;

import android.scrabble.AboutViewActivity;
import android.scrabble.HelpViewActivity;
import android.scrabble.LoginActivity;
import android.scrabble.MainMenuActivity;
import android.scrabble.SignupActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.jayway.android.robotium.solo.Solo;

public class TestMain extends ActivityInstrumentationTestCase2<MainMenuActivity> {
	
	private Solo solo;

	public TestMain() {
		super(MainMenuActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}

	protected void tearDown() throws Exception{
		try {
			solo.finalize();
		} catch (Throwable e) {
			Log.e("Throwable error", e.getMessage());
		}
	}
	
	public void testLogIn() throws InterruptedException {
		solo.enterText(0, "kohina");
		solo.clickOnButton("Log in!");
		if(solo.searchButton("Try to log me out!")){
			solo.clickOnButton("Try to log me out!");
			solo.clickOnButton("OK");
			solo.clickOnButton("Log in!");
		}
		solo.assertCurrentActivity("MainMenuActivity", MainMenuActivity.class);
	}
	
	public void testInvalidLogin() throws InterruptedException{
		//System.out.println("Test invalid login is running");
		solo.enterText(0, "kooooooooooooooohina");
		solo.clickOnButton("Log in!");
		if(solo.searchButton("Try to log me out!")){
			solo.clickOnButton("Try to log me out!");
			solo.clickOnButton("OK");
			solo.clickOnButton("Log in!");
		}
		solo.assertCurrentActivity("Sign up activity", SignupActivity.class);
	}
	
	public void testLogOut() throws InterruptedException{
		solo.enterText(0, "kohina");
		solo.clickOnButton("Log in!");
		if(solo.searchButton("Try to log me out!")){
			solo.clickOnButton("Try to log me out!");
			solo.clickOnButton("OK");
			solo.clickOnButton("Log in!");
		}
		solo.assertCurrentActivity("MainMenuActivity", MainMenuActivity.class);
		solo.clickOnMenuItem("Log out");
		solo.assertCurrentActivity("Log in Activity", LoginActivity.class);
	}
	
	public void testEditSettings() throws InterruptedException{
		solo.enterText(0, "kohina");
		solo.clickOnButton("Log in!");
		if(solo.searchButton("Try to log me out!")){
			solo.clickOnButton("Try to log me out!");
			solo.clickOnButton("OK");
			solo.clickOnButton("Log in!");
		}
		solo.assertCurrentActivity("MainMenuActivity", MainMenuActivity.class);
		solo.clickOnButton("Settings");
		solo.pressSpinnerItem(0, 1);
		solo.clickOnButton("Save");
		solo.assertCurrentActivity("MainMenuActivity", MainMenuActivity.class);
		solo.clickOnButton("Inställningar");
		solo.pressSpinnerItem(0, 0);
		solo.clickOnButton("Spara");
		solo.goBack();
		assertTrue(solo.searchButton("Settings"));
	}
	
	public void testAbout() throws InterruptedException{
		solo.enterText(0, "kohina");
		solo.clickOnButton("Log in!");
		if(solo.searchButton("Try to log me out!")){
			solo.clickOnButton("Try to log me out!");
			solo.clickOnButton("OK");
			solo.clickOnButton("Log in!");
		}
		solo.assertCurrentActivity("MainMenuActivity", MainMenuActivity.class);
		solo.clickOnButton("About");
		solo.assertCurrentActivity("AboutViewActivity", AboutViewActivity.class);
	}
	
	public void testHelp() throws InterruptedException{
		solo.enterText(0, "kohina");
		solo.clickOnButton("Log in!");
		if(solo.searchButton("Try to log me out!")){
			solo.clickOnButton("Try to log me out!");
			solo.clickOnButton("OK");
			solo.clickOnButton("Log in!");
		}
		solo.assertCurrentActivity("MainMenuActivity", MainMenuActivity.class);
		solo.clickOnButton("Help");
		solo.assertCurrentActivity("HelpViewActivity", HelpViewActivity.class);
	}
}