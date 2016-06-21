package model.traffic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.net.ssl.SSLSocket;

import util.ActionID;
import util.ActionIDCarrier;
import User.User;
import model.data.HashFile;
import model.state.ApplicationState;
/**
 * This class is createt by sendertypethread and sends the specific file to the user
 * @author Johns lap
 *
 */
public class CryptFileSender extends Observable implements Runnable, TrafficObject, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2672158789295397040L;
	private long maxsize = 0;
	private long sendsize = 0;
	private byte[] buffer = null;
	private transient SSLSocket sk = null;
	private transient OutputStream output = null;
	private transient ObjectOutputStream out = null;
	private transient FileInputStream file = null;
	private transient ObjectInputStream in = null;
	private transient Thread sendThread = new Thread(this);
	private boolean running = true;
	private String fileName = "";
	private User user = null;
	private DataDirection direction = DataDirection.UP;
	private DownloadStatus status = DownloadStatus.PREPARING_DOWNLOAD;
	private long temptime = System.currentTimeMillis();
	private long temprecieved = 0;
	private double speed=0;
	private String path= "";
	private HashFile sendFile = null;
	public CryptFileSender(SSLSocket sk, ObjectInputStream in, Observer o) {
		this.addObserver(o);
		this.in = in;
		this.sk = sk;
		sendThread.start();


	}

	@Override
	public void run() {

		try {
			output = sk.getOutputStream();
			user = (User) in.readUnshared();
			path = (String) in.readUnshared();
			fileName = (String) in.readUnshared();
			out = new ObjectOutputStream(sk.getOutputStream());
			sendFile = new HashFile(path);
			file = new FileInputStream(sendFile);
			maxsize = sendFile.length();
			out.writeObject(maxsize);	
			if(((String)in.readUnshared()).equals("redo")){
				out.writeObject(Integer.valueOf((sendFile.hashCode())));
			}
			boolean check = false;
			ArrayList<TrafficObject> ll = ApplicationState.getInstance().getTrafficList();
			for(int i=0;i<ll.size();i++){
				if(ll.get(i).getDirection()==DataDirection.UP){
					if(ll.get(i).getMaxSize()==getMaxSize()&&ll.get(i).getUser().equals(getUser())&&ll.get(i).getFile().equals(sendFile)){
						ll.remove(i);
						ll.add(i, this);
						check=true;
						break;
					}
				}
			}
			if(!check){
				ApplicationState.getInstance().getTrafficList().add(this);
			}
			
			buffer = new byte[65536];
			int bytesRead = 0;
			long tempsize = 0;
			tempsize = (Long) in.readUnshared();
			file.skip(tempsize);
			sendsize = tempsize;
			status=DownloadStatus.RUNNING;
			setChanged();
			notifyObservers(new ActionIDCarrier(ActionID.STATUS_UPDATED));
			while ((bytesRead = file.read(buffer)) > 0 && running) {
				output.write(buffer, 0, bytesRead);
				sendsize = sendsize + bytesRead;
			}
			status=DownloadStatus.DONE;
			setChanged();
			notifyObservers(new ActionIDCarrier(ActionID.STATUS_UPDATED));
		} catch (IOException ex) {
			status=DownloadStatus.STOPPED;
			setChanged();
			notifyObservers(new ActionIDCarrier(ActionID.STATUS_UPDATED));
		} catch (ClassNotFoundException e) {
		} finally {
			try {
				file.close();
			} catch (IOException e) {}
			try{
				sk.close();
			}catch(IOException e){}
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
			if(file!=null){
				file.close();
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
	
	public User getUser() {
		return user;
	}
	
	public DownloadStatus getStatus() {
		return status;
	}
	public DataDirection getDirection() {
		return direction;
	}

	@Override
	public long getMaxSize() {
		return maxsize;
	}

	@Override
	public long getRecivedSize() {
		return sendsize;
	}

	@Override
	public double getSpeed() {
		long time=System.currentTimeMillis()-temptime;
		if(time>500){
			double timem = time/1000.0;
			long get = sendsize-temprecieved;			
			speed = get/(timem);
			temprecieved=sendsize;
			temptime = System.currentTimeMillis();
		}
		return speed;
	}

	@Override
	public File getFile() {
		return sendFile;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public String getDirectory() {
		return path;
	}
	/*
	public boolean equals(Object o){
		if(!(o instanceof TrafficObject)){
			return false;
		}
		TrafficObject t = (TrafficObject)o;
		if(!t.getDirectory().equals(getDirectory())){
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
		if(sendThread==null){
			running=true;
			sendThread= new Thread(this);
			sendThread.start();
		}else if(!sendThread.isAlive()){
			running=true;
			sendThread= new Thread(this);
			sendThread.start();
		}
		
	}
	
	public void startTransfer(Observer o) {
		this.addObserver(o);
		if(sendThread == null){
			running=true;
			sendThread = new Thread(this);
			sendThread.start();
		}else if(!sendThread.isAlive()){
			sendThread = new Thread(this);
			running=true;
			sendThread.start();
		}
		
	}
	
	public void init(SSLSocket sk, ObjectInputStream in){
		this.sk=sk;
		this.in=in;
	}
	
	@Override
	public void setStatus(DownloadStatus s) {
		this.status=s;		
	}
	
	
}
