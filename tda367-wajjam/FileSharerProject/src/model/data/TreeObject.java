package model.data;

import java.util.List;

import User.User;
/**
 * 
 * @author Johns lap
 *	A object thats contains information that needs for download a users files and folders
 */
public class TreeObject {
	
	private User user = null;
	private DirectoryTreeModel model = null;
	private String node = null;
	private DirectoryTreeNode treeNode = null;
	private List<FileInformation> list = null;
	
	public TreeObject(User user, DirectoryTreeModel model) {
		this.user=user;
		this.model=model;
	}
	
	public TreeObject(User user, String node) {
		this.user=user;
		this.node=node;
	}
	
	public TreeObject(User user, DirectoryTreeNode node) {
		this.user=user;
		this.treeNode=node;
	}
	
	public TreeObject(User user, List<FileInformation> list) {
		this.user=user;
		this.list=list;
	}
	
	public List<FileInformation> getList() {
		return list;
	}

	public TreeObject(User user, DirectoryTreeModel model, String node) {
		this.user=user;
		this.node=node;
		this.model=model;
	}
	
	public String getNode() {
		return node;
	}
	
	public DirectoryTreeNode getTreeNode() {
		return treeNode;
	}

	public DirectoryTreeModel getModel() {
		return model;
	}
	
	public User getUser() {
		return user;
	}
}
