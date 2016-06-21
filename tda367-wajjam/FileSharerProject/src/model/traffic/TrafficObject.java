package model.traffic;

import java.io.File;
import java.util.Observer;

import User.User;
/**
 * An interface for cryptfilegetter and cryptfilesender so they have the right information
 * for the table
 * @author Johns lap
 *
 */
public interface TrafficObject {
	
	
	public String getFileName();
	
	public DataDirection getDirection();
	
	public long getMaxSize();
	
	public long getRecivedSize();
	
	public void setStatus(DownloadStatus s);
	
	public DownloadStatus getStatus();
	
	public User getUser();
	
	public void startTransfer();
	
	public void startTransfer(Observer o);
	
	public void stopTransfer();
	
	public double getSpeed();
	
	public String getPath();
	
	public File getFile();
	
	public String getDirectory();
	
	public boolean equals(Object o);
	
}
