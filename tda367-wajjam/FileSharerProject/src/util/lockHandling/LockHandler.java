package util.lockHandling;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * Utility-class to handle the locking of the application, allowing only the creation of
 * a single instance at a time.
 * 
 * @author Wånge
 */
public class LockHandler {
	
	/**
	 * This method attempts to lock the application. If successful, it adds an UnlockThread
	 * as a shutdownhook, that will release the lock upon termination of the application, 
	 * and then returns true to the caller. If unsuccessful, the application will throw an
	 * exception or exit with false, depending on what fails.
	 * 
	 * @return true or false depending on whether the lock could be secured.
	 * @throws IOException if any of the involved operations failed, an IOException is thrown,
	 * indicating to the caller that the lock is in use and that the launch should be canceled.
	 */
	public static boolean checkLocked() throws IOException {
		File f;
		FileLock fl;
		FileChannel fc;
		boolean locked = false;
		
		f = new File("alreadyrunning.lock");
		
		if(f.exists()) {
			f.delete();
		}
		
		fc = new RandomAccessFile(f, "rw").getChannel();

		fl = fc.tryLock();
		
		if(fl == null) {
			fc.close();
			locked = true;
		}
		
		Runtime.getRuntime().addShutdownHook(new UnlockThread(f, fl, fc));
		return locked;
	}
}