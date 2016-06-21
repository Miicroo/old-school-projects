package main;
import java.io.IOException;

import javax.swing.UIManager;

import model.state.ApplicationState;
import model.state.SettingsIO;

import util.lockHandling.LockHandler;
import controller.Controller;

public final class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			if(!LockHandler.checkLocked()) {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			    } catch (Exception e) {}
				
				if(args.length == 2) {
					if(args[0] != null && args[1] != null) {
						//TODO implement easy-to-switch customized models and views.	
					}
				} else {
					if(SettingsIO.open() != null) {
						ApplicationState.setState(SettingsIO.open());
					}					
					new Controller(null, null);
				}
			}
		} catch(IOException e) {}
	}
}
