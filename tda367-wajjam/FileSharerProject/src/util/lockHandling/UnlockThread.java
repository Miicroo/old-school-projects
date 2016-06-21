package util.lockHandling;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import util.ErrorHandling;

/**
 * A thread responsible for releasing the application-lock on termination, allowing
 * further sessions to be run. Should be added as a ShutDownHook at instantiating.
 * 
 * @author Wånge
 */
public class UnlockThread extends Thread {

	private File f;
	private FileLock fl;
	private FileChannel fc;	
	
	public UnlockThread(File f, FileLock fl, FileChannel fc) {
		this.f = f;
		this.fl = fl;
		this.fc = fc;
	}
	
	@Override
	public void run() {
		try {
			f.delete();
			if(fl != null) {
				fl.release();
			}
			fc.close();
		} catch(IOException e) {
			ErrorHandling.displayErrorMessage(e);
		}
	}

}
