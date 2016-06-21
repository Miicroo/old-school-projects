package model.logic;

import java.awt.Component;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeCellRenderer;

import model.data.*;
import model.filter.DirectoryFilter;

/**
 * The renderer of a DirectoryTree.
 * The renderer is used to set icons etc to the JTree.
 * 
 * @author Magnus
 */
public class DirectoryTreeRenderer extends DefaultTreeCellRenderer {
	
	private Icon icon = getFileIcon();
	private static final long serialVersionUID = -168698531638177895L;
	
	/**
	 * Overriding the default-method, and sets icons on the tree.
	 * 
	 * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 *
	 * @return The component that has been styled with icons etc.
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus){
		
		if(value.getClass() == DirectoryTreeNode.class){	//	If it is the DirectoryTreeNode and not the abstract nodes
			DirectoryTreeNode dtn = (DirectoryTreeNode)value;	//	The node that we want to decorate
				
			JLabel result = (JLabel) super.getTreeCellRendererComponent(tree, dtn.getUserObject(), sel, expanded, leaf, row, hasFocus);
				
			result.setIcon(icon);
			return result;
		}
		else{
			return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		}
	}
	
	/*
	 * Retrieves the icon of a directory on the computer.
	 * 
	 * @TODO if you find a drive that doesn't contain a directory, what happens???
	 * 
	 * @return the icon that the system uses for directories.
	 */
	private Icon getFileIcon(){
		File f = null;
		File[] rootList = File.listRoots();
		
		for(File root : rootList){
			if(root.isDirectory()){	//	find a REAL root (aka non-virtual)
				f = root;
				break;
			}
		}
		
		File f2 = (f == null ? null : f.listFiles(new DirectoryFilter())[0]);	//	get any directory on the drive
		return FileSystemView.getFileSystemView().getSystemIcon(f2);
	}
}
