package model.traffic;
/**
 * This class reads the first object in the inputstream and descide what thread which 
 * shall continue the work
 * @author Johns lap
 *
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Observer;

import javax.net.ssl.SSLSocket;

import util.ErrorHandling;

import User.SendType;

/**
 * 
 * @author Johns lap
 * 
 */
public class SenderTypeThread implements Runnable {
	private SSLSocket sk = null;
	private Observer o = null;

	public SenderTypeThread(SSLSocket sk, Observer o) {
		this.o=o;
		this.sk = sk;
		new Thread(this).start();
	}

	@Override
	public void run() {
		try {
			sk.startHandshake();
			ObjectInputStream in = new ObjectInputStream(sk.getInputStream());
			SendType temp = (SendType) in.readObject();
			if (temp == SendType.FILE) {
				new CryptFileSender(sk, in, o);
			} else if (temp == SendType.TREE) {
				new DirectoryTreeSender(sk, in);
			}else if ( temp == SendType.ACQ_FILE){
				new FileSender(sk,in);
			}else if(temp == SendType.CHAT){
				new ChatReceiver(sk, in, o);
			}
		} catch (IOException e) {
			ErrorHandling.displayErrorMessage(e);
		} catch (ClassNotFoundException e) {
			ErrorHandling.displayErrorMessage(e);
		}
	}
}