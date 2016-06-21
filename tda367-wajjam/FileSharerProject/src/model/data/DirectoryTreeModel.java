package model.data;

import java.io.Serializable;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.*;

/**
 * TreeModel that lists a filesystem and then saves it in TreeNodes.
 * Usage:
 * <code>
 * String[] roots = {"C:\\", "D:\\"};
 * DirectoryTreeLogics dtl = new DirectoryTreeLogics(roots);
 * 
 * DirectoryTreeModel model = new DirectoryTreeModel(dtl.getAbstractRoot(), dtl.getRealRoot());
 * 
 * JTree tree = new JTree();
 * tree.setModel(model);
 * </code>
 * 
 * This code will produce a JTree that contains the rootnodes "C:\\" and "D:\\", with
 * each rootnode's subdirectories added as a treepath.
 * 
 * It is also possible to send this model on a network between computers, since it's serialized and
 * the subdirectories are already saved and not dynamically created.
 * 
 * @author Magnus
 */
public class DirectoryTreeModel implements TreeModel, Serializable{

	private static final long serialVersionUID = 692634468352045428L;
	private DirectoryTreeNode abstractRoot = null;	//	The topnode, which is abstract and just used to collect the other nodes.
	private DirectoryTreeNode[] realRoot = null;	//	The topnodes that you want to use, e.g. "C:\\" and "D:\\"
	
	/**
	 * Create a new DirectoryTreeModel with the specified roots.
	 * 
	 * @param abstractRoot the topnode, which is abstract and just used to collect the other nodes.
	 * @param realRoot the topnodes that you want to use, e.g. "C:\\" and "D:\\".
	 */
	public DirectoryTreeModel(DirectoryTreeNode abstractRoot, DirectoryTreeNode[] realRoot){
		this.abstractRoot = abstractRoot;
		
		final int realRootLen = realRoot.length;
		
		this.realRoot = new DirectoryTreeNode[realRootLen];
		
		for(int i = 0; i<realRootLen; i++){
			this.realRoot[i] = realRoot[i];
		}
	}
	
	/**
	 * Gets the root. In this case, the root is the abstract root (non-visible)
	 * that contains the topnodes.
	 * 
	 * @return the root that contains the topnodes.
	 */
	public Object getRoot() {
		return abstractRoot;
	}

	/**
	 * Calculates if the node is the leaf. This means practically if the node doesn't
	 * contain any children.
	 * 
	 * @see DefaultMutableTreeNode#getChildCount()
	 * 
	 * @param node the node to calculate if it is a leaf.
	 * 
	 * @return true if the node is a leaf, otherwise false.
	 */
	public boolean isLeaf(Object node){
		return ((DirectoryTreeNode)node).isLeaf();
	}

	/**
	 * Calculates the number of children that the node contains.
	 * 
	 * @param node the node where to calculate the number of children from.
	 * 
	 * @return the number of children that the node contains.
	 */
	public int getChildCount(Object node) {
		return ((DirectoryTreeNode)node).getChildCount();
	}

	/**
	 * Gets the node at the specific index of the certain node.
	 * 
	 * @param node the node where to find the index.
	 * @param index the index of the children to find.
	 * 
	 * @return the TreeNode at the specific index of the specific node
	 */
	public Object getChild(Object node, int index) {
		return ((DirectoryTreeNode)node).getChildAt(index);
	}

	/**
	 * Gets the index of the child at the node.
	 * 
	 * @param node the node where to find the child.
	 * @param child the child to find
	 * 
	 * @return the index of the child at the node.
	 */
	public int getIndexOfChild(Object node, Object child) {
		return ((DirectoryTreeNode)node).getIndex(((TreeNode)(child)));
	}

	/**
	 * N/A
	 */
	public void valueForPathChanged(TreePath path, Object newvalue) {}
	/**
	 * N/A
	 */
	public void addTreeModelListener(TreeModelListener l) {}
	/**
	 * N/A
	 */
	public void removeTreeModelListener(TreeModelListener l) {}
	
	@Override
	public boolean equals(Object o) {
		if(o == null) {
			return false;
		}
		
		if(o instanceof DirectoryTreeModel) {
			DirectoryTreeModel other = (DirectoryTreeModel)o;
			if(abstractRoot.equals(other.abstractRoot)) {
				return false;
			}
			
			for(int i = 0; i < realRoot.length; i++) {
				if(!realRoot[i].equals(other.realRoot[i])) {
					return false;
				}
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		int returnInt = 0;
		returnInt = abstractRoot.hashCode()*43;
		for(DirectoryTreeNode dtn : realRoot) {
			returnInt += dtn.hashCode()*3;
		}
		return returnInt;
	}
}