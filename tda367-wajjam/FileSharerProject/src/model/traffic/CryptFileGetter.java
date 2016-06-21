package model.traffic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import model.data.HashFile;
import model.state.ApplicationState;

import util.ActionID;
import util.ActionIDCarrier;
import util.ErrorHandling;
import User.SendType;
import User.User;
/**
 * This class creates a socket to the user and get the selected file from him
 * @author Johns lap
 *
 */
public class CryptFileGetter extends Observable implements Runnable, TrafficObject,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4446089166279380000L;
	private String path = "", fileName = "", dir = "";
	private User user = null;
	private transient Thread thread = new Thread(this);
	private long maxSize = 0;
	private long receivedSize = 0;
	private transient SSLSocket sk = null;
	private boolean running = true;
	private transient FileOutputStream wr = null;
	private DownloadStatus status = DownloadStatus.PREPARING_DOWNLOAD;
	private DataDirection direction = DataDirection.DOWN;
	private long temptime = System.currentTimeMillis();
	private long temprecieved = 0;
	private double speed=0;
	private File file = null;
	private transient Observer o ;
	public CryptFileGetter(User user, String path, String fileName,
			String downloadDir, Observer o) {
		this.o=o;
		this.addObserver(o);
		this.dir = downloadDir;
		this.user = user;
		this.fileName = fileName;
		this.path = path;
		thread.start();
	}

	@Override
	public void run() {
		file = null;
		try {
			String host= user.getIp();
			if(host.equals(ApplicationState.getInstance().getUser().getIp())){
				host=user.getLocalIp();
			}
			SSLSocketFactory sl = (SSLSocketFactory) SSLSocketFactory
					.getDefault();
			
			sk = (SSLSocket) sl.createSocket(host, user.getPort());
			final String[] enabledCipherSuites = { "SSL_DH_anon_WITH_RC4_128_MD5" };
			sk.setEnabledCipherSuites(enabledCipherSuites);
			sk.startHandshake();
			ObjectOutputStream out = new ObjectOutputStream(sk.getOutputStream());
			out.writeUnshared(SendType.FILE);
			InputStream input = sk.getInputStream();
			out.writeUnshared(ApplicationState.getInstance().getUser());
			out.writeUnshared(path);
			out.writeUnshared(fileName);
			ObjectInputStream in = new ObjectInputStream(input);

			maxSize = (Long) in.readObject();

			boolean check = false;
			ArrayList<TrafficObject> ll = ApplicationState.getInstance().getTrafficList();
			for(int i=0;i<ll.size();i++){
				if(ll.get(i).getDirection()==DataDirection.DOWN){
					if(ll.get(i).getMaxSize()==getMaxSize()&&ll.get(i).getFileName().equals(getFileName())){
						if(!ll.get(i).equals(this)){	
							ll.get(i).startTransfer(o);
							running=false;
						}
							check=true;
							break;	
					}
				}
			}
			if(!check){
				ApplicationState.getInstance().getTrafficList().add(this);
			}
			if(running){

				out.writeUnshared("redo");
				int tempname = (Integer) in.readObject();
				file = new File(dir);
				file.mkdirs();
				file = new File(dir + tempname);
				if (file.exists()) {
					receivedSize = file.length();
				}
				if (new File(dir + fileName).exists()) {
					if(new File(dir + fileName).length()==maxSize){
						HashFile f = new HashFile(dir + fileName);
						if(f.hashCode() == tempname){
							receivedSize=maxSize;
							file= new File(dir + fileName);
						}
					}
				}
				wr = new FileOutputStream(file, true);
	
				byte[] buffer = new byte[65536];
				int bytesReceived = 0;
				out.writeUnshared(receivedSize);
				status=DownloadStatus.RUNNING;
				setChanged();
				notifyObservers(new ActionIDCarrier(ActionID.STATUS_UPDATED));
				while ((bytesReceived = input.read(buffer)) > 0 && running) {
					wr.write(buffer, 0, bytesReceived);
					receivedSize = receivedSize + bytesReceived;
				}
				
				wr.close();
				if (receivedSize == maxSize) {
					status=DownloadStatus.DONE;
					setChanged();
					notifyObservers(new ActionIDCarrier(ActionID.STATUS_UPDATED));
					File tmp = new File(dir + fileName);
					file.renameTo(tmp);
				}else{
					status=DownloadStatus.STOPPED;
				}
			}
		} catch (IOException e) {
			status=DownloadStatus.STOPPED;
			setChanged();
			notifyObservers(new ActionIDCarrier(ActionID.STATUS_UPDATED));
			ErrorHandling.displayErrorMessage(e);
		} catch (ClassNotFoundException e) {
		}
		finally {
			setChanged();
			notifyObservers(new ActionIDCarrier(ActionID.STATUS_UPDATED));
			try {
				wr.close();
			} catch (Exception e) {}
			try {
				sk.close();
			} catch (IOException e1) {}
		}
	}

	public void stopTransfer() {
		running = false;
		if(status!=DownloadStatus.DONE){
			status=DownloadStatus.STOPPED;
		}
		setChanged();
		notifyObservers(new ActionIDCarrier(ActionID.STATUS_UPDATED));
		try {
			if(wr!=null){
				wr.close();
			}
		}catch (IOException e) {}
		try {
			if(sk!=null){
				sk.close();
			}
		} catch (IOException e) {}
	}

	public String getFileName() {
		return fileName;
	}
	
	public long getMaxSize() {
		return maxSize;
	}
	

	@Override
	public DataDirection getDirection() {
		return direction;
	}

	@Override
	public long getRecivedSize() {
		return receivedSize;
	}

	@Override
	public DownloadStatus getStatus() {
		return status;
	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public double getSpeed() {
		long time=System.currentTimeMillis()-temptime;
		if(time>500){
			double timem = time/1000.0;
			long get = receivedSize-temprecieved;			
			speed = get/(timem);
			temprecieved=receivedSize;
			temptime = System.currentTimeMillis();
		}
		return speed;
	}

	@Override
	public File getFile() {
		return file;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public String getDirectory() {
		return dir;
	}
	
	
	/*
	public boolean equals(Object o){
		if(!(o instanceof TrafficObject)){
			return false;
		}
		TrafficObject t = (TrafficObject)o;
		if(t.getDirection()!=getDirection()){
			return false;
		}else if(!t.getDirectory().equals(getDirectory())){
			return false;
		}else if(!t.getFile().equals(getFile())){
			return false;
		}else if(!t.getFileName().equals(getFileName())){
			return false;
		}else if(t.getMaxSize()!=getMaxSize()){
			return false;
		}else if(!t.getPath().equals(getPath())){
			return false;
		}
		
		return true;
	}*/

	@Override
	public void startTransfer() {
		if(thread == null){
			running=true;
			thread = new Thread(this);
			thread.start();
		}else if(!thread.isAlive()){
			thread = new Thread(this);
			running=true;
			thread.start();
		}
		
	}
	

	public void startTransfer(Observer o) {
		this.addObserver(o);
		if(thread == null){
			running=true;
			thread = new Thread(this);
			thread.start();
		}else if(!thread.isAlive()){
			thread = new Thread(this);
			running=true;
			thread.start();
		}
		
	}

	@Override
	public void setStatus(DownloadStatus s) {
		this.status=s;		
	}
	

}
