package model.data;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Class that works exactly like a DefaultMutableTreeNode with the difference that it
 * contains displayname and filename. The displayname is the name of a directory on a computer
 * in the filesystem, e.g. "Users" or "Downloads". The filename is the filepath to a file
 * e.g. "C:\\Users\\" or "D:\\files\\downloads".
 * 
 * @author Magnus
 */
public class DirectoryTreeNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = 7586472792603508627L;
	private String filename;
	
	/**
	 * Create a new DirectoryTreeNode with the specified displayname and filename.
	 * 
	 * @param displayname the name of the directory to display.
	 * @param filename the filename that is a path to a directory on a drive.
	 */
	public DirectoryTreeNode(String displayname, String filename){
		super((displayname == null ? "" : displayname));
		this.filename = filename;
	}
	
	/**
	 * Gets the filepath of the directory.
	 * 
	 * @return the filepath of the node.
	 */
	public String getFile(){
		return filename;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null) {
			return false;
		}
		
		if(o instanceof DirectoryTreeNode) {
			DirectoryTreeNode other = (DirectoryTreeNode)o;
			if(filename == null || other.filename == null){
				return false;
			}
			return filename.equals(other.filename);
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		int returnInt = super.hashCode();
		if(filename != null) {
			returnInt = 97*filename.hashCode();
		}
		return returnInt;
	}
}