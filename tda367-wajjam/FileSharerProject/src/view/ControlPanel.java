package view;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import model.data.ChatObject;
import model.login.Server;
import model.state.ApplicationState;
import util.ActionID;
import util.ActionIDCarrier;
import util.DataSupplier;
import util.Performable;
import util.UnsupportedActionException;


/**
 * This panel contains a tabbed pane where all the connected servers will be displayed,
 * and a table with general information about all the current file transfers. 
 * @author Albin Bramstång
 */
public class ControlPanel extends Observable implements ActionListener, MouseListener, Performable<Object>, DataSupplier<Server> {
	
	private JPanel controlPanel = new JPanel();
	private JSplitPane splitPane = new JSplitPane();
	private JScrollPane scrollPane;
	private JTabbedPane tabbedPane = new JTabbedPane();
	private OverviewTable overviewTable;
	private LinkedList<ServerPanel> tabbedPaneContent = new LinkedList<ServerPanel>();
	private List<ActionIDCarrier> canPerform = new LinkedList<ActionIDCarrier>();
	private List<ActionIDCarrier> canSupply = new LinkedList<ActionIDCarrier>();
	private Server activeServer = null;
	private Observer o;
	public ControlPanel(Observer o) {
		this.addObserver(o);
		this.o = o;
		
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setDividerLocation(300);
		
		tabbedPane.addMouseListener(this);
		splitPane.setTopComponent(tabbedPane);
		

		
		overviewTable = new OverviewTable(o);

		scrollPane = new JScrollPane(overviewTable.getTable());
		scrollPane.setPreferredSize(new Dimension(0, 0));
		scrollPane.setBackground(Color.white);
		scrollPane.getViewport().setBackground(Color.white);
		splitPane.setBottomComponent(scrollPane);
		controlPanel.setLayout(new BorderLayout());
		controlPanel.add(splitPane, BorderLayout.CENTER);
		canPerform.add(new ActionIDCarrier(ActionID.CHAT_RECEIVED));
		//canPerform.add(new ActionIDCarrier(ActionID.SERVER_UPDATED));
		canPerform.add(new ActionIDCarrier(ActionID.ADD_SERVER));
		canSupply.add(new ActionIDCarrier(ActionID.UPDATE_SELECTED_SERVER_NAME));
		canSupply.add(new ActionIDCarrier(ActionID.CONNECT));
		canSupply.add(new ActionIDCarrier(ActionID.DISCONNECT));
		canSupply.add(new ActionIDCarrier(ActionID.REMOVE));
		canSupply.add(new ActionIDCarrier(ActionID.VIEW_CHAT));
		canSupply.add(new ActionIDCarrier(ActionID.VIEW_SHARED_CONTENT));
		
		this.setChanged();
		this.notifyObservers(new ActionIDCarrier(ActionID.NEW_PERFOMER_INSTANTIATED));
		this.setChanged();
		this.notifyObservers(new ActionIDCarrier(ActionID.NEW_SUPPLIER_INSTANTIATED));
		for(int i=0;i<ApplicationState.getInstance().getServerList().size();i++){
			addTab(ApplicationState.getInstance().getServerList().get(i));
		}
	}
	
	/**
	 * Adds a new tab to the tabbed pane in this class.
	 */
	public void addTab(Server s) {
		ServerPanel serverPanel = new ServerPanel(s,o);
		tabbedPane.addTab(s.getHost(), serverPanel.getServerPanel());
		tabbedPaneContent.add(serverPanel);
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		JLabel serverNameLabel = new JLabel(s.getHost() + " ");
		JButton closeButton = new JButton(new ImageIcon("icons/close.png"));
		closeButton.setMargin(new Insets(2, 2, 2, 2));
		closeButton.addActionListener(this);
		
		panel.setLayout(new BorderLayout());
		panel.add(serverNameLabel, BorderLayout.WEST);
		panel.add(closeButton, BorderLayout.CENTER);
		tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, panel);
		tabbedPane.setSelectedIndex(tabbedPane.getTabCount()-1);
		activeServer = s;
		this.setChanged();
		this.notifyObservers(new ActionIDCarrier(ActionID.UPDATE_SELECTED_SERVER_NAME));
		

	}
	
	/**
	 * Removes a selected tab from the tabbed pane in this class.
	 */
	public void removeTab(int index) {
		activeServer = tabbedPaneContent.get(index).getServer();
		tabbedPane.remove(index);
		tabbedPaneContent.get(index).remove();
		tabbedPaneContent.remove(index);
		this.setChanged();
		this.notifyObservers(new ActionIDCarrier(ActionID.REMOVE));
		if(tabbedPaneContent.size()==0){
			activeServer=null;
		}else{
			activeServer=tabbedPaneContent.get(tabbedPane.getSelectedIndex()).getServer();
		}
		this.setChanged();
		this.notifyObservers(new ActionIDCarrier(ActionID.UPDATE_SELECTED_SERVER_NAME));
	}
	
	/**
	 * Returns the main graphical component of this class, containing
	 * all subcomponents. 
	 * 
	 * @return JPanel
	 */
	public JPanel getControlPanel() {
		return controlPanel;
	}
	
	/**
	 * Returns a LinkedList with all components currently in the tabbedPane.
	 * 
	 * @return LinkedList
	 */
	public LinkedList<ServerPanel> getTabbedPaneContent() {
		return tabbedPaneContent;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton) {
			int i = tabbedPane.indexOfTabComponent(((JButton)e.getSource()).getParent());
			removeTab(i);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton()==2){
			removeTab(tabbedPane.getSelectedIndex());			
		}else{
			if (tabbedPane.getTabCount() > 0) {
				activeServer = tabbedPaneContent.get(tabbedPane.getSelectedIndex()).getServer();
				JLabel label =((JLabel)((JPanel)tabbedPane.getTabComponentAt(tabbedPane.getSelectedIndex())).getComponent(0));
				label.setForeground(Color.black);
				label.setFont(new Font("Tahoma", Font.PLAIN, 11));
				this.setChanged();
				this.notifyObservers(new ActionIDCarrier(ActionID.UPDATE_SELECTED_SERVER_NAME));
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {	}

	@Override
	public void mouseExited(MouseEvent e) {	}
	
	/**
	 * Returns the title of the currently selected tab.
	 * 
	 * @return String
	 */
	public String getSelectedTabTitle() {
		return tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
	}
	
	/**
	 * Returns the content in the selected tab.
	 * 
	 * @return ServerPanel
	 */
	public ServerPanel getSelectedTabContent() {
		if (tabbedPane.getTabCount() > 0) {
			return tabbedPaneContent.get(tabbedPane.getSelectedIndex());
		} else {
			return null;
		}
	}

	@Override
	public List<ActionIDCarrier> canPerform() {
		return canPerform;
	}

	@Override
	public void perform(ActionID actionToPerform, Object requiredData) {
		switch (actionToPerform) {
		case ADD_SERVER:
			int index=0;
			boolean exist = true;
			for(;index<tabbedPaneContent.size();index++){
				if(tabbedPaneContent.get(index).getServer().equals(requiredData)){
					exist=false;
					break;
				}
			}
			if (exist) {
				this.addTab((Server)requiredData);
				setChanged();
				notifyObservers(new ActionIDCarrier(ActionID.CONNECT));
			}else{
				tabbedPane.setSelectedIndex(index);
				activeServer = tabbedPaneContent.get(index).getServer();
				setChanged();
				notifyObservers(new ActionIDCarrier(ActionID.UPDATE_SELECTED_SERVER_NAME));
			}
			break;
		case CHAT_RECEIVED:		
			ChatObject o = (ChatObject)requiredData;
			if(!o.getServer().equals(activeServer)){
				for(int i=0;i<tabbedPaneContent.size();i++){
					if(tabbedPaneContent.get(i).getServer().equals(o.getServer())){
						JLabel label =((JLabel)((JPanel)tabbedPane.getTabComponentAt(i)).getComponent(0));
						label.setForeground(Color.blue);
						label.setFont(new Font("Tahoma", Font.BOLD, 11));
						break;
					}
				}
			}
			break;
		}
		
	}

	@Override
	public List<ActionIDCarrier> canSupply() {
		return canSupply;
	}

	@Override
	public Server getData(ActionID action) throws UnsupportedActionException {
		switch (action) {
		case UPDATE_SELECTED_SERVER_NAME:
			return activeServer;
		case CONNECT:
			return activeServer;
		case DISCONNECT:
			return activeServer;
		case REMOVE:
			return activeServer;
		case VIEW_CHAT:
			return activeServer;
		case VIEW_SHARED_CONTENT:
			return activeServer;
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
