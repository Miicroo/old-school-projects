package view;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.CheckboxTree;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.PathChecker;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.PathCheckingEvent;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.PathCheckingListener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import javax.swing.tree.TreePath;

import model.data.DirectoryTreeModel;
import model.logic.DirectoryTreeLogics;
import model.logic.FileSystemModel;
import model.state.ApplicationState;

import util.*;

/**
 * This class displays the settings of the program.
 * @author Albin Bramstång
 */
public class SettingsWindow extends Observable implements ActionListener, PathCheckingListener, Performable<Object> {

	private static final long serialVersionUID = -7044702512097125674L;
	
	private JFrame mainFrame = new JFrame();
	private JPanel mainPanel = new JPanel();
	private JTabbedPane tabbedPane = new JTabbedPane();
	private JPanel sharePanel = new JPanel();
	private List<ActionIDCarrier> canPerform = new LinkedList<ActionIDCarrier>();
	private CheckboxTree checkboxTree = null;
	private JButton confirmButton = new JButton("OK");
	private JButton cancelButton = new JButton("Cancel");
	private JPanel buttonPanel = new JPanel();
	private JScrollPane pane = null;
	private JTextArea messageTextArea = new JTextArea("Share folders by selecting them. Be aware that large folders may take some time to load.");
	private JPanel generalPanel = new JPanel();
	private JLabel downloadDirLabel = new JLabel("Download directory: ");
	private JTextField downloadDirTextField = new JTextField(20);
	private JButton changeDownloadDirButton = new JButton("...");
	private JPanel downloadDirPanel = new JPanel();
	private JLabel userNameLabel = new JLabel("              User name: ");
	private JTextField userNameTextField = new JTextField(10);
	private JPanel userNamePanel = new JPanel();
	private JFileChooser downloadDirChooser = new JFileChooser();
	private ApplicationState applicationsState = ApplicationState.getInstance();
	
	public SettingsWindow(Observer o) {
		this.addObserver(o);

		downloadDirPanel.setLayout(new BoxLayout(downloadDirPanel, BoxLayout.X_AXIS));
		downloadDirPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		downloadDirPanel.add(downloadDirLabel);
		downloadDirTextField.setText(applicationsState.getDownloadDirectory());
		downloadDirTextField.setMaximumSize(new Dimension(150, 23));
		downloadDirTextField.addActionListener(this);
		downloadDirPanel.add(downloadDirTextField);
		downloadDirPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		changeDownloadDirButton.addActionListener(this);
		downloadDirPanel.add(changeDownloadDirButton);
		downloadDirPanel.setMaximumSize(new Dimension(400, 35));
		
		userNamePanel.setLayout(new BoxLayout(userNamePanel, BoxLayout.X_AXIS));
		userNamePanel.add(Box.createRigidArea(new Dimension(5, 0)));
		userNamePanel.add(userNameLabel);
		userNameTextField.setText(applicationsState.getUser().getUsername());
		userNameTextField.setMaximumSize(new Dimension(150, 23));
		userNameTextField.addActionListener(this);
		userNamePanel.add(userNameTextField);
		userNamePanel.setMaximumSize(new Dimension(400, 35));
		
		generalPanel.setLayout(new BoxLayout(generalPanel, BoxLayout.Y_AXIS));
		generalPanel.add(downloadDirPanel);
		generalPanel.add(userNamePanel);
		
		tabbedPane.addTab("General", generalPanel);
		
		
		File[] roots = File.listRoots();
		FileSystemModel model = new FileSystemModel(roots);		
		
		checkboxTree = new CheckboxTree(model);		
		checkboxTree.addPathCheckingListener(this);
			
		TreePath[] newChecked = model.getNewReferences();	//	Get the saved paths, but with updated references.
		
		if(newChecked != null){
			checkboxTree.addCheckingPaths(newChecked);
		}
		
		pane = new JScrollPane(checkboxTree);
		
		messageTextArea.setFont(new Font("Tahoma", Font.PLAIN, 11));
		messageTextArea.setEditable(false);
		messageTextArea.setWrapStyleWord(true);
		messageTextArea.setLineWrap(true);
		messageTextArea.setBackground(new Color(240, 240, 240));
		
		sharePanel.setLayout(new BorderLayout());
		sharePanel.add(messageTextArea, BorderLayout.NORTH);
		sharePanel.add(pane);
		
		tabbedPane.addTab("Sharing", sharePanel);
		
		confirmButton.addActionListener(this);
		cancelButton.addActionListener(this);
		buttonPanel.add(confirmButton);
		buttonPanel.add(cancelButton);
		
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(tabbedPane);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		mainFrame.add(mainPanel);
		mainFrame.setResizable(false);
		mainFrame.setTitle("Settings");
		mainFrame.setIconImage(new ImageIcon("dc.png").getImage());
		mainFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		mainFrame.setPreferredSize(new Dimension(400, 500));
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		
		canPerform.add(new ActionIDCarrier(ActionID.SHOW_SETTINGS_WINDOW));
		canPerform.add(new ActionIDCarrier(ActionID.SHARE));
		
		downloadDirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		this.setChanged();
		this.notifyObservers(new ActionIDCarrier(ActionID.NEW_PERFOMER_INSTANTIATED));
	}

	@Override
	public List<ActionIDCarrier> canPerform() {
		return canPerform;
	}

	@Override
	public void perform(ActionID actionToPerform, Object requiredData) {
		switch (actionToPerform) {
		case SHOW_SETTINGS_WINDOW:
			mainFrame.setVisible(true);	
			break;
		case SHARE:
			break;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == confirmButton || e.getSource() == downloadDirTextField || e.getSource() == userNameTextField) {
			applicationsState.setTreePaths(checkboxTree.getCheckingRoots());
			applicationsState.setDownloadDirectory(downloadDirTextField.getText());
			if(!userNameTextField.getText().equals(applicationsState.getUser().getUsername())){
				applicationsState.getUser().setUsername(userNameTextField.getText());
				setChanged();
				notifyObservers(new ActionIDCarrier(ActionID.CHANGE_USERNAME));
			}
			mainFrame.setVisible(false);
		} else if (e.getSource() == cancelButton) {
			mainFrame.setVisible(false);
		} else if (e.getSource() == changeDownloadDirButton) {
			int chooser = downloadDirChooser.showOpenDialog(generalPanel);
			if (chooser == JFileChooser.APPROVE_OPTION) {
				downloadDirTextField.setText(downloadDirChooser.getSelectedFile().getAbsolutePath());
			}
		}
	}

	@Override
	public void pathCheckerUpdated(PathCheckingEvent e) {
		if(e.getSource().getClass() == PathChecker.class){
			ApplicationState.getInstance().setChecker(checkboxTree.getChecker());
			PathChecker c = checkboxTree.getChecker();
			DirectoryTreeLogics log = new DirectoryTreeLogics(c);
			DirectoryTreeModel model = new DirectoryTreeModel(log.getAbstractRoot(), log.getRealRoot());
			ApplicationState.getInstance().setSharedTree(model);
		}
	}
	
	@Override
	public int getPerformIDNumber(ActionIDCarrier action) {
		return canPerform.get(canPerform().indexOf(action)).getIDNumber();
	}
}
