package model.state;

import java.io.*;

import util.ErrorHandling;

import model.traffic.DownloadStatus;

/**
 * Utility class that saves/opens settings.
 * 
 * @author Magnus
 */
public class SettingsIO {

	private static File file = new File("settings.config");
	/**
	 * Saves the settings to the file. If the file doesn't exist it's created.
	 * 
	 * @param file the file to save the settings to.
	 * @return true if the program could be saved (no error occurred), else false.
	 */
	public static boolean save(ApplicationState sett){

		for(int i=0;i<sett.getTrafficList().size();i++){
			if(sett.getTrafficList().get(i).getStatus()!=DownloadStatus.DONE){
				sett.getTrafficList().get(i).setStatus(DownloadStatus.STOPPED);
			}
		}
		
		for(int i=0;i<sett.getServerList().size();i++){
			sett.getServerList().get(i).setConnected(false);
		}
		
		ObjectOutputStream oos = null;
		
		try {
			file.createNewFile();
		} catch (IOException e) {
			ErrorHandling.displayErrorMessage(e);
			return false;
		}
		try{
			oos = new ObjectOutputStream(new FileOutputStream("settings.config"));
			oos.writeObject(sett);
		}
		catch(FileNotFoundException fnfe){
			ErrorHandling.displayErrorMessage(fnfe);
			return false;
		}
		catch(IOException ioe){
			ErrorHandling.displayErrorMessage(ioe);
			return false;
		}
		finally{
			try {
				oos.close();	//	Always close the stream!!!
			}
			catch (IOException e) {
				ErrorHandling.displayErrorMessage(e);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Opens and returns the saved runtime state.
	 *  
	 * @return the saved settings, or null if unable to read or there is no settings.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static ApplicationState open(){
		File f = new File("settings.config");
		ApplicationState sett = null;
		
		if(f.exists()) {
			ObjectInputStream ois = null;
			try{
				ois = new ObjectInputStream(new FileInputStream("settings.config"));
				sett = (ApplicationState)ois.readObject();	//	Convert the Object to settings, or keep it as null if the Object isn't a setting
			}
			catch(IOException e) {
				sett = null;
			}
			catch(ClassNotFoundException e){
				sett = null;
			}
			finally{
	
				try {
					ois.close();
				}
				catch (IOException e) {
				}
				catch(NullPointerException npe){
					
				}
			}
		}
		return sett;
	}
}
