package model.state;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.PathChecker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.tree.TreePath;

import User.User;
import model.data.DirectoryTreeModel;
import model.login.*;
import model.traffic.TrafficObject;

/**
 * This class holds all the global, non-transient values of the application;
 * data that is called from and set all over the program. In short, the state,
 * or settings, of the application. Things like who this user is, what is being
 * downloaded, and how this users shared content looks like, are all in this file.
 * As a result, this class, and it's subsequent single instance (ApplicationState is
 * Singleton) can be rather large. No splitting is desired in this case however, due
 * to the ease of saving achieved by grouping.
 * 
 * <p>
 * The single instance of this class is statically accessed from anywhere by call to
 * <code>getInstance</code>. It can be set by a call to <code>setState</code>, but 
 * due to the constructor being private, and the method taking an ApplicationState as
 * parameter, this can only be done if the caller already has a reference to an instance,
 * as in the case of loading a saved state. The method also checks for and ignores null 
 * values.
 * 
 * @author Wånge
 */
public class ApplicationState implements Serializable {

	private static final long serialVersionUID = -7960508374989864344L;
	private static ApplicationState settings = new ApplicationState();
	private User user = null;
	private List<Server> serverList = new LinkedList<Server>();
	private ArrayList<TrafficObject> trafficList = new ArrayList<TrafficObject>();
	private DirectoryTreeModel sharedTree = null;
	private TreePath[] treePaths = null;
	private PathChecker checker = null;
	private String[] lastIPandPort = {"", ""};
	private String downloadDirectory = "";
	
	private ApplicationState() {}
	
	public static ApplicationState getInstance() {
		return settings;
	}
	
	public static void setState(ApplicationState as) {
		if(as != null) {
			settings = as;
		} 
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setServerList(List<Server> serverList) {
		this.serverList = serverList;
	}

	public List<Server> getServerList() {
		return serverList;
	}

	public void setTrafficList(ArrayList<TrafficObject> trafficList) {
		this.trafficList = trafficList;
	}

	public ArrayList<TrafficObject> getTrafficList() {
		return trafficList;
	}

	public void setTreePaths(TreePath[] treePaths) {
		this.treePaths = treePaths;
	}

	public TreePath[] getTreePaths() {
		return treePaths;
	}

	public void setChecker(PathChecker checker) {
		this.checker = checker;
	}

	public PathChecker getChecker() {
		return checker;
	}

	public void setSharedTree(DirectoryTreeModel sharedTree) {
		this.sharedTree = sharedTree;
	}

	public DirectoryTreeModel getSharedTree() {
		return sharedTree;
	}

	public void setLastIPandPort(String[] lastIPandPort) {
		if(lastIPandPort != null && lastIPandPort.length == 2) {
			this.lastIPandPort = lastIPandPort;
		}
	}

	public String[] getLastIPandPort() {
		return lastIPandPort;
	}
	
	
	public String getDownloadDirectory() {
		return downloadDirectory;
	}
	
	public void setDownloadDirectory(String downloadDirectory) {
		this.downloadDirectory = downloadDirectory;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null) {
			return false;
		}
		
		if(o instanceof ApplicationState) {
			ApplicationState other = (ApplicationState)o;
						
			if(!user.equals(other.user)) {
				return false;
			} else if(!serverList.equals(other.serverList)) {
				return false;
			} else if(!trafficList.equals(other.trafficList)) {
				return false;
			} else if(!sharedTree.equals(other.sharedTree)) {
				return false;
			} else if(!downloadDirectory.equals(other.downloadDirectory)) {
				
			} else if(!checker.equals(other.checker)) {
				return false;
			}
			
			if(lastIPandPort.length == other.lastIPandPort.length) {
				for(int i = 0; i < lastIPandPort.length; i++) {
					if(!lastIPandPort[i].equals(other.lastIPandPort[i])) {
						return false;
					}
				}
			} else {
				return false;
			}
			
			if(treePaths == null){
				return false;
			}
			
			if(treePaths.length == other.treePaths.length) {
				for(int i = 0; i < treePaths.length; i++) {
					if(!treePaths[i].equals(other.treePaths[i])) {
						return false;
					}
				}
			} else {
				return false;
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		int returnInt = 0;
		
		returnInt += user.hashCode()*31;
		returnInt += serverList.hashCode()*53;
		returnInt += trafficList.hashCode()*59;
		returnInt += sharedTree.hashCode()*7;
		returnInt += checker.hashCode()*83;
		returnInt += downloadDirectory.hashCode()*13;
		
		for(TreePath tp : treePaths) {
			returnInt += tp.hashCode()*2;
		}
		
		for(String s : lastIPandPort) {
			returnInt += s.hashCode()*19;
		}
		return returnInt;
	}
}
