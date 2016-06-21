package model.traffic;

import User.User;

/**
 * A dataholder for the information cryptfilegetter need to start a download
 * 
 * @author Johns lap
 * 
 */
public class FileGetObject {

	private String path = "";
	private String filename = "";
	private String dir = "";
	private User user = null;

	public FileGetObject(User user, String path, String filename, String downloadDir) {
		this.dir = downloadDir;
		this.path = path;
		this.filename = filename;
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public String getDir() {
		return dir;
	}

	public String getFileName() {
		return filename;
	}

	public String getPath() {
		return path;
	}
}
