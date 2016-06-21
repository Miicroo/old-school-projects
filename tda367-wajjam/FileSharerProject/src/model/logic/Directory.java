package model.logic;

import java.io.File;
import java.io.Serializable;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JFileChooser;

import model.data.*;
import model.filter.AcceptingFileFilter;


/**
 * Class representing a filesystem-directory (or a folder).
 * The Directory is created from a given filename, which is a File that exists.
 * The Directory then handles and lists the FileInformation that has been created.
 * The class is also sendable, since it's serialized, so it can be used over a network.
 * 
 * @author Magnus
 */
public class Directory implements Serializable{

	private static final long serialVersionUID = -2578065468237570711L;
	private String dir = null;
	private List<FileInformation> fileinfo = null;
	private JFileChooser fc = new JFileChooser();
	
	/**
	 * Creates a new Directory from the specific dir.
	 * dir is a File that exists on the computer.
	 * 
	 * @param dir the path to a existing directory on the computer.
	 */
	public Directory(String dir){
		this.dir = dir;
		fileinfo = new LinkedList<FileInformation>();
		
		updateFiles();
	}

	/**
	 * Gets the Directory as a path to an existing File.
	 * 
	 * @return the path to an existing folder/directory.
	 */
	public String getDir() {
		return dir;
	}

	/**
	 * Gets the FileInformation from the files stored in this directory.
	 * 
	 * @return the list of FileInformation contained in this directory.
	 */
	public List<FileInformation> getFileInformation() {
		return fileinfo;
	}
	
	/**
	 * Updates the list of FileInformation, and reloads the data.
	 */
	public void updateFiles(){
		
		if(dir == null){
			return;
		}
		
		fileinfo.clear();
		File f = new File(dir);
		File[] allFiles = f.listFiles(new AcceptingFileFilter());
		
		if(allFiles != null){
			for(File temp : allFiles){
				FileInformation i = new FileInformation(temp.getAbsolutePath());
				i.setMimeType(fc.getTypeDescription(temp));
				fileinfo.add(i);
			}
		}
	}
}