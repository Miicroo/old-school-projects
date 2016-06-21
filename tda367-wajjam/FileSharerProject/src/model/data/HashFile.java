package model.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class HashFile extends File {

	private static final long serialVersionUID = -9015012149773304772L;

	public HashFile(String pathname) {
		super(pathname);
	}
	
	/**
	 * Hash the files with the files lenght and the last 1000 bits
	 */
	@Override
	public int hashCode() {
		long length = this.length();
		int len = (int) (length / (1024 ));
		FileInputStream r = null;
		byte[] asd = null;
		try {
			asd = new byte[1000];
			if (length < 1000) {
				asd = new byte[(int) length];
			}
			r = new FileInputStream(this);
			if (length >= 1000) {
				r.skip(length - 1000);
				r.read(asd, 0, 1000);
			} else {
				r.read(asd, 0, (int) length);
			}
		} catch (IOException e) {
		} finally {
			try {
				r.close();
			} catch (IOException e) {
			}
		}
		for (int i = 0; i < asd.length; i++) {
			len = len - Character.getNumericValue(asd[i]);
		}
		return len;

	}

}
