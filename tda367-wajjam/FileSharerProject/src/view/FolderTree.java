package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;

import model.data.DirectoryTreeNode;
import model.data.TreeObject;
import model.logic.DirectoryTreeRenderer;
import model.login.Server;

import util.ActionID;
import util.ActionIDCarrier;
import util.DataSupplier;
import util.Performable;
import util.UnsupportedActionException;

/**
 * Displays the shared folders of a user.
 * @author Albin Bramstång
 */
public class FolderTree extends Observable implements MouseListener,TreeSelectionListener, Performable<Object>, DataSupplier<Object> {

	private JTree tree = new JTree();
	private List<ActionIDCarrier> canPerform = new LinkedList<ActionIDCarrier>();
	private List<ActionIDCarrier> canSupply = new LinkedList<ActionIDCarrier>();
	private String folder;
	private Server server;
	private JPopupMenu m;
	private TreeObject treeObject;
	private Server activeServer = null;
	private JScrollPane scrollPane;
	
	public FolderTree(Server s, Observer o,int id) {
		
		canSupply.add(new ActionIDCarrier(ActionID.DOWNLOAD_NODE));		
		canSupply.add(new ActionIDCarrier(ActionID.ACQUIRE_FILE));
		canPerform.add(new ActionIDCarrier(ActionID.UPDATE_SELECTED_SERVER_NAME));
		canPerform.add(new ActionIDCarrier(ActionID.TREE_RECEIVED));
		canPerform.add(new ActionIDCarrier(ActionID.RECEIVE_TREE));
		canPerform.add(new ActionIDCarrier(ActionID.SERVER_UPDATED));
		canPerform.add(new ActionIDCarrier(ActionID.DOWNLOAD_FOLDER));
		
		server = s;
		
		tree.setCellRenderer(new DirectoryTreeRenderer());	//	Set renderer that adds icon.
		tree.setRootVisible(false);
		tree.addTreeSelectionListener(this);
		
		tree.addMouseListener(this);
		
		m = new DownloadPopup(o).getPopupMenu();
		scrollPane = new JScrollPane();
		scrollPane.setMinimumSize(new Dimension(150,0));
		scrollPane.getViewport().setBackground(Color.white);
		addObserver(o);
		setChanged();
		notifyObservers(new ActionIDCarrier(ActionID.NEW_PERFOMER_INSTANTIATED));

	}

	@Override
	public List<ActionIDCarrier> canPerform() {
		return canPerform;
	}
	@Override
	public void perform(ActionID actionToPerform, Object requiredData) {
		switch (actionToPerform) {
		case TREE_RECEIVED:
			treeObject = ((TreeObject)requiredData); 
			if (activeServer.equals(server)) {
				scrollPane.setViewportView(tree);
				if(treeObject!=null){
					tree.setModel(treeObject.getModel());
				}
			}
			break;
		case SERVER_UPDATED:
			if (((Server)requiredData).equals(this.server)) {
				if (!server.isConnected()) {
					this.tree.setModel(new DefaultTreeModel(null));
				}
			}
			break;
		case DOWNLOAD_FOLDER:
			setChanged();
			notifyObservers(new ActionIDCarrier(ActionID.NEW_SUPPLIER_INSTANTIATED));
			setChanged();
			notifyObservers(new ActionIDCarrier(ActionID.DOWNLOAD_NODE));
			setChanged();
			notifyObservers(new ActionIDCarrier(ActionID.SUPPLIER_DELETED));
			break;
		case UPDATE_SELECTED_SERVER_NAME:
			activeServer = (Server)requiredData;
			break;
		case RECEIVE_TREE:
			if(activeServer.equals(server)){
				scrollPane.setViewportView(new JLabel(new ImageIcon("res/load.gif")));
			}
			break;
		}
		tree.repaint();
	}
	

	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	public void remove(){
		setChanged();
		notifyObservers(new ActionIDCarrier(ActionID.PERFORMER_DELETED));
		setChanged();
		notifyObservers(new ActionIDCarrier(ActionID.SUPPLIER_DELETED));
	}

	@Override
	public List<ActionIDCarrier> canSupply() {
		return canSupply;
	}

	@Override
	public Object getData(ActionID action) throws UnsupportedActionException {
		switch (action) {
		case ACQUIRE_FILE:
			return new TreeObject(treeObject.getUser(), folder);
		case DOWNLOAD_NODE:	
			DirectoryTreeNode a = ((DirectoryTreeNode)((tree).getLastSelectedPathComponent()));
			System.out.println(a);
			return new TreeObject(treeObject.getUser(),a);
		default:
			return null;
		}
	}
	
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		if(e.getSource() == tree){
			DirectoryTreeNode node = ((DirectoryTreeNode)(((JTree)e.getSource()).getLastSelectedPathComponent()));
			if (node != null) {
				folder = node.getFile();
				setChanged();
				notifyObservers(new ActionIDCarrier(ActionID.NEW_SUPPLIER_INSTANTIATED));
				setChanged();
				notifyObservers(new ActionIDCarrier(ActionID.ACQUIRE_FILE));
				setChanged();
				notifyObservers(new ActionIDCarrier(ActionID.SUPPLIER_DELETED));
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton()==3){ 
		   int row = tree.getClosestRowForLocation(e.getX(),e.getY());  
		   tree.setSelectionRow(row);  
		   m.show(tree, e.getX(),e.getY());
		}  	
	}
	
	@Override
	public int getPerformIDNumber(ActionIDCarrier action) {
		return canPerform.get(canPerform().indexOf(action)).getIDNumber();
	}
	
	@Override
	public int getSupplyIDNumber(ActionIDCarrier action) {
		return canSupply().get(canSupply().indexOf(action)).getIDNumber();
	}
}