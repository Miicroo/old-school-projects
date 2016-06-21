package model.logic;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.FileContainer;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeFile;

import java.io.File;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import model.state.ApplicationState;


/**
 * This is the model-class that creates a dynamic CheckboxTree displaying the filesystem.
 * Using the cbt-lib.
 * 
 * @see it.cnr.imaa.essi.lablib.gui.checkboxtree.CheckboxTree
 * 
 * @author Magnus
 */
public class FileSystemModel implements TreeModel, Serializable {

	private static final long serialVersionUID = -8296110378512669386L;
	private FileContainer root;
	private List<String> childPath = new LinkedList<String>();	//	Contains the children of a parent as a String

	/**
	 * Creates a new abstract from the given roots.
	 * 
	 * @param roots the rootfolders represented as File
	 */
	public FileSystemModel(File[] roots) {
		root = new FileContainer(roots);
	}

	public Object getRoot(){
		return root;
	}

	/**
	 * Returns the child of the parent root at the specific index.
	 * When the parent root is the abstract root there is no parent!
	 */
	public Object getChild(Object parent, int index){
		if(parent instanceof File){
			File directory = (File) parent;
			String[] children = directory.list();
	
			return new TreeFile(directory, children[index]);
		}
		else{
			FileContainer directory = (FileContainer)parent;
			return new TreeFile(null, directory.getFileAt(index).getAbsolutePath());
		}
	}
	
	/**
	 * Gets all the children from the parent node.
	 * This method also sets the childPath's content, which is just
	 * as the list that this method returns but it contains
	 * String's representing the getAbsolutePath().
	 * This is done because of the indexing to {@link #getNewReferences()}.
	 * 
	 * @param parent the object that contains the children.
	 * 
	 * @return all the children of the parent
	 */
	private List<Object> getAllChildren(Object parent){
		List<Object> o = new LinkedList<Object>();
		
		int c = getChildCount(parent);
		childPath.clear();
		
		for(int i = 0; i<c; i++){
			o.add(getChild(parent, i));
			childPath.add(((TreeFile)getChild(parent, i)).getAbsolutePath());
		}
		
		return o;
	}

	/**
	 * Returns the number of children the parent root contains.
	 * In the CheckboxTree both files and folders count as objects.
	 */
	public int getChildCount(Object parent) {
		if(parent instanceof File){
			File file = (File) parent;
			if (file.isDirectory()) {
				String[] fileList = file.list();
				if (fileList != null)
					return file.list().length;
			}
			return 0;
		}
		else{
			FileContainer file = (FileContainer) parent;
			return file.getCount();
		}
	}

	/**
	 * Checks if a node is the leaf.
	 * In the CheckboxTree only displaying files/folders this is equal to
	 * if the file/folder contains other files/folders. You can therefore
	 * say that this method checks if the node is a file or not.
	 */
	public boolean isLeaf(Object node) {
		if(node instanceof File){
			File file = (File) node;
			return file.isFile();
		}
		else{
			return false;
		}
	}

	/**
	 * Finds the index of a child in it's parent.
	 * This method is only used for the the files and not for the abstract root.
	 * If you implement this method and get a ClassCastException, just check if the object
	 * sent as parameters really are File/TreeFile.
	 */
	public int getIndexOfChild(Object parent, Object child) {
		File directory = (File) parent;
		File file = (File) child;
		String[] children = directory.list();
		for (int i = 0; i < children.length; i++) {
			if (file.getName().equals(children[i])) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Sets that the value for the path has changed.
	 * This method updates the files/folders that the path has been updated to.
	 * Inherited from TreeModel. 
	 */
	public void valueForPathChanged(TreePath path, Object value) {
		File oldFile = (File) path.getLastPathComponent();
		String fileParentPath = oldFile.getParent();
		String newFileName = (String) value;
		File targetFile = new File(fileParentPath, newFileName);
		oldFile.renameTo(targetFile);
		File parent = new File(fileParentPath);
		int[] changedChildrenIndices = { getIndexOfChild(parent, targetFile) };
		Object[] changedChildren = { targetFile };
		fireTreeNodesChanged(path.getParentPath(), changedChildrenIndices,
				changedChildren);

	}

	/**
	 * Recomputes the saved TreePath[] to the new references in this tree.
	 * 
	 * @return the new references in this tree.
	 */
	public TreePath[] getNewReferences(){
		TreePath[] oldPaths = ApplicationState.getInstance().getTreePaths();
		
		if(oldPaths == null){	//	error reading the TreePath[], or TreePath[] isn't set.
			return null;
		}
		
		List<TreePath> newPaths = new LinkedList<TreePath>();
		
		Object root = getRoot();
		
		for(TreePath path : oldPaths){	//	Loop through the TreePath's
			Object[] pathAsObject = path.getPath();	//	Retrieve the paths one by one
			Object[] newPath = new Object[pathAsObject.length];
			
			newPath[0] = root;	//	The root is always the root of this tree.
			
			Object parent = root;	//	Save a parent. This is used in the for-loop so it has to be "global".
			
			boolean dirStillExists = true;
			
			for(int i = 1; i<newPath.length; i++){
				Object oldNode = pathAsObject[i];
				List<Object> possibles = getAllChildren(parent);
				
				//	find the TreeFile in the list
				final int possIndex = childPath.indexOf(((TreeFile)oldNode).getAbsolutePath());
				
				if(possIndex != -1){
					
					//	if the file (aka a directory) exists, save it to the path.
					Object newNode = possibles.get(possIndex);
					
					parent = newNode;
					newPath[i] = newNode;
				}
				else{
					//	the dir is not found in the tree.
					//	might be because the dir is deleted, or has restricted access.
					dirStillExists = false;
					break;
				}
			}
			
			if(dirStillExists){
				newPaths.add(new TreePath(newPath));
			}
		}
		return ((TreePath[])(newPaths.toArray(new TreePath[0])));
	}	
	
	
	/*
	 * Method that fires an event every time something updates.
	 * It is this method that makes it more dynamically when
	 * expanding/repainting and so. Important: don't remove!!
	 */
	private void fireTreeNodesChanged(TreePath parentPath, int[] indices,
			Object[] children) {
		/*TreeModelEvent event = new TreeModelEvent(this, parentPath, indices,
				children);
		Iterator<TreeModelListener> iterator = listeners.iterator();
		TreeModelListener listener = null;
		while (iterator.hasNext()) {
			listener = (TreeModelListener) iterator.next();
			listener.treeNodesChanged(event);
		}*/
	}

	/**
	 * 
	 * @see javax.swing.tree.TreeModel#addTreeModelListener(javax.swing.event.TreeModelListener)
	 */
	public void addTreeModelListener(TreeModelListener listener) {
		//listeners.add(listener);
	}

	/**
	 * @see javax.swing.tree.TreeModel#removeTreeModelListener(javax.swing.event.TreeModelListener)
	 */
	public void removeTreeModelListener(TreeModelListener listener) {
		//listeners.remove(listener);
	}
}