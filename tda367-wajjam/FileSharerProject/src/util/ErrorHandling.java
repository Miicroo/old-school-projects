package util;

import view.CentralErrorMessage;

/**
 * NOTE! This class is not used as of now. 
 * 
 * @author Albin Bramstång
 */
public class ErrorHandling {
	
	public static void displayErrorMessage(Exception e) {
		CentralErrorMessage instance = new CentralErrorMessage();
		instance.getMessageArea().setText(e.toString() + "\n");
		e.printStackTrace();
		for(StackTraceElement ste : e.getStackTrace()) {
			instance.getMessageArea().append(ste.toString() + "\n");
		}
		//instance.setVisible(true);
	}

	public static void displayErrorMessage(String e) {
		CentralErrorMessage instance = new CentralErrorMessage();
		instance.getMessageArea().setText(e + "\n");
		//instance.setVisible(true);
	}
}
