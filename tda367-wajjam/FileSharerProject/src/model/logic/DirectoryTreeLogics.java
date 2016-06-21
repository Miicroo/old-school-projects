package model.logic;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.PathChecker;

import java.io.File;

import javax.swing.filechooser.FileSystemView;

import model.data.DirectoryTreeNode;
import model.filter.CheckedDirectoryFilter;

/**
 * Logic-class that saves the users filesystem.
 * (You might say that it is the logics of the DirectoryTreeModel).
 * 
 * @see DirectoryTreeModel
 * 
 * @author Magnus
 */
public class DirectoryTreeLogics {
	
	private DirectoryTreeNode abstractRoot = null;	//	The topnode, which is abstract and just used to collect the other nodes.
	private DirectoryTreeNode[] realRoot = null;	//	The topnodes that you want to use, e.g. "C:\\" and "D:\\"
	private PathChecker pc = null;	//	The PathChecker that keeps track of your folders.
	
	/**
	 * The constructor may be slow, depending on the roots and how many/how big the subdirectories are.
	 *
	 * @param pc the PathChecker that keeps track on what files to keep/remove.
	 */
	public DirectoryTreeLogics(PathChecker pc){
		if(pc != null){
			final int nrOfRoots = pc.keepLength();
			
			this.pc = pc;
			
			File[] roots = new File[nrOfRoots];
			for(int i = 0; i<nrOfRoots; i++){
				roots[i] = pc.getKeep(i);
			}
			
			realRoot = new DirectoryTreeNode[nrOfRoots];
			
			for(int i = 0; i<nrOfRoots; i++){
				realRoot[i] = getFileSystem(null, roots[i]);
			}
			
			abstractRoot = new DirectoryTreeNode(null, null);
			
			for(DirectoryTreeNode root : realRoot){
				abstractRoot.add(root);
			}
		}
	}
	
	/*
	 * Saves the users filesystem as a DirectoryTreeNode.
	 * This method is recursive and starts from bottom and then works recursively
	 * to the top.

	 * ## NOTE ## This method is effective, but if you want to share "C:\\" or 
	 * something like that, it will take time. Thats's a fact. Not a bug. End of line.
	 */
	private DirectoryTreeNode getFileSystem(DirectoryTreeNode top, File dir){
		if(dir != null) {
			String filename = FileSystemView.getFileSystemView().getSystemDisplayName(dir);
			DirectoryTreeNode currDir = new DirectoryTreeNode(filename, dir.getPath());
		    
		    if (top != null) {
		      top.add(currDir);	//	Add the directory to it's parent :)
		    }
		    
		    File[] tmp = dir.listFiles(new CheckedDirectoryFilter(pc));
		    
		    if(tmp != null){
			    for(int i = 0; i<tmp.length; i++){
			      String path = tmp[i].getAbsolutePath();
		
			      File f = new File(path);
			      getFileSystem(currDir, f);	//	Add next node, recursively
			    }
		    }
		    
		    return currDir;
		}
		return null;
	}

	public DirectoryTreeNode getAbstractRoot() {
		return abstractRoot;
	}

	public DirectoryTreeNode[] getRealRoot() {
		return realRoot;
	}
}
