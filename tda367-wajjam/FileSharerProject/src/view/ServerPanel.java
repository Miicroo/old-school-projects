package view;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

import User.User;

import model.login.*;
import util.ActionID;
import util.ActionIDCarrier;
import util.DataSupplier;
import util.Performable;
import util.UnsupportedActionException;

import java.util.*;
import java.util.List;

/**
 * A server panel is a graphical representation of a server, with a user list, a folder tree
 * and a file table. 
 * @author Albin Bramstång
 */
public class ServerPanel extends Observable implements MouseListener, Performable<Object>, DataSupplier<Object> {

	private JPanel serverPanel = new JPanel();
	private JList userList = new JList();
	private JPanel cardPanel = new JPanel();
	private FolderTree folderTree;
	private FileTable fileTable;
	private JScrollPane userListScrollPane;
	private CardLayout cardLayout;
	private List<ActionIDCarrier> canPerform = new LinkedList<ActionIDCarrier>();
	private List<ActionIDCarrier> canSupply = new LinkedList<ActionIDCarrier>();
	private Server server = null;
	private Server activeServer = null;
	private User activeUser = null;
	private JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	private JSplitPane splitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	private ChatPanel chatPanel;
	private boolean viewingChat = false;
	private static int instancesRunning = 0;
	private int numberOfInstance;
	
	public ServerPanel(Server server, Observer o) {
		this.addObserver(o);
		this.server = server;
		chatPanel = new ChatPanel(server, o,instancesRunning);
		serverPanel.setLayout(new BorderLayout());

		userList.addMouseListener(this);
		userList.setListData(server.getUserArray());
		userListScrollPane = new JScrollPane(userList);
		userListScrollPane.setPreferredSize(new Dimension(100, 0));
		folderTree = new FolderTree(server, o,instancesRunning);
		
		fileTable = new FileTable(server, o,instancesRunning);
		splitPane.setTopComponent(folderTree.getScrollPane());
		splitPane.setBottomComponent(fileTable.getScrollPane());
		
		
		cardPanel.setLayout(new CardLayout());
		cardPanel.add(splitPane, "Browse");
		cardPanel.add(chatPanel.getPanel(), "Chat");
		cardLayout = (CardLayout)cardPanel.getLayout();
		
		splitPane2.setTopComponent(userListScrollPane);
		splitPane2.setBottomComponent(cardPanel);
		
		serverPanel.add(splitPane2, BorderLayout.CENTER);
		numberOfInstance = instancesRunning;
		canSupply.add(new ActionIDCarrier(ActionID.IS_CHATVIEW));
		canSupply.add(new ActionIDCarrier(ActionID.RECEIVE_TREE));
		canPerform.add(new ActionIDCarrier(ActionID.SERVER_UPDATED));
		canPerform.add(new ActionIDCarrier(ActionID.VIEW_CHAT));
		canPerform.add(new ActionIDCarrier(ActionID.VIEW_SHARED_CONTENT));
		canPerform.add(new ActionIDCarrier(ActionID.UPDATE_SELECTED_SERVER_NAME));
		setChanged();
		notifyObservers(new ActionIDCarrier(ActionID.NEW_PERFOMER_INSTANTIATED));		

		instancesRunning++;
	}
	
	/**
	 * Returns the main graphical component of this class, containing
	 * all subcomponents. 
	 * 
	 * @return JPanel
	 */
	public JPanel getServerPanel() {
		return serverPanel;
	}
	
	public Server getServer() {
		return server;
	}
	
	public void remove(){
		chatPanel.remove();
		folderTree.remove();
		fileTable.remove();
		setChanged();
		notifyObservers(new ActionIDCarrier(ActionID.PERFORMER_DELETED));
		setChanged();
		notifyObservers(new ActionIDCarrier(ActionID.SUPPLIER_DELETED));
	}
	
	/**
	 * Makes the chat visible.
	 */
	public void viewingChat() {
		cardLayout.show(cardPanel, "Chat");
		chatPanel.getChatTextField().requestFocus();
	}

	/**
	 * Makes the shared content visible; the folder tree and the file table.
	 */
	public void viewSharedContent() {
		cardLayout.show(cardPanel, "Browse");
	}

	@Override
	public List<ActionIDCarrier> canPerform() {
		return canPerform;
	}

	@Override
	public void perform(ActionID actionToPerform, Object requiredData) {
		switch (actionToPerform) {
		
		case VIEW_CHAT: 
			if(server.equals(activeServer)){
				viewingChat=true;
				this.viewingChat();
			}
			break;
		case VIEW_SHARED_CONTENT: 
			if(server.equals(activeServer)){
				viewingChat=false;
				this.viewSharedContent();
			}
			break;
		case SERVER_UPDATED:
			if(((Server)requiredData).equals(server)){
				userList.setListData(server.getUserArray());
				userList.repaint();	
			}
			break;
		case UPDATE_SELECTED_SERVER_NAME:
			activeServer = (Server)requiredData;
			if(activeServer.equals(server)){
				setChanged();
				notifyObservers(new ActionIDCarrier(ActionID.NEW_SUPPLIER_INSTANTIATED));
				setChanged();
				notifyObservers(new ActionIDCarrier(ActionID.IS_CHATVIEW));
				setChanged();
				notifyObservers(new ActionIDCarrier(ActionID.SUPPLIER_DELETED));
			}
			break;	
		}
	}
	
	public boolean isViewingChat() {
		return viewingChat;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			activeUser = (User)userList.getSelectedValue();
			setChanged();
			notifyObservers(new ActionIDCarrier(ActionID.NEW_SUPPLIER_INSTANTIATED));
			setChanged();
			notifyObservers(new ActionIDCarrier(ActionID.RECEIVE_TREE));
			setChanged();
			notifyObservers(new ActionIDCarrier(ActionID.SUPPLIER_DELETED));
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public List<ActionIDCarrier> canSupply() {
		return canSupply;
	}

	@Override
	public Object getData(ActionID action) throws UnsupportedActionException {
		switch (action) {
		case RECEIVE_TREE:
			return activeUser;
		case IS_CHATVIEW:
			return isViewingChat();
		default:
			throw new UnsupportedActionException();
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