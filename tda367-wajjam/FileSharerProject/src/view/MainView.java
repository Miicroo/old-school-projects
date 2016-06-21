package view;

import javax.swing.*;

import java.awt.*;
import java.util.*;

/**
 * This class represents the GUI used by the File Sharer client.
 * It contains all main components, and is the central class for
 * the whole GUI.
 * @author Albin Bramstång
 */
public class MainView {
	
	private JFrame mainFrame = new JFrame();
	private JPanel mainPanel = new JPanel();
	private MenuBar menuBar;	
	private ToolBar toolBar;
	private ControlPanel controlPanel;
	private StatusPanel statusPanel;
	
	public MainView(Observer o) {
		menuBar = new MenuBar(o);
		toolBar = new ToolBar(o);
		controlPanel = new ControlPanel(o);
		new ServerInputDialog(o);
		statusPanel = new StatusPanel(o);
		new SettingsWindow(o);
		mainFrame.setAlwaysOnTop(true);
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(toolBar.getToolBar(), BorderLayout.NORTH);
		mainPanel.add(controlPanel.getControlPanel(), BorderLayout.CENTER);
		mainPanel.add(statusPanel.getStatusPanel(), BorderLayout.SOUTH);
		
		mainFrame.setLayout(new BorderLayout());
		mainFrame.add(menuBar.getMenuBar(), BorderLayout.NORTH);
		mainFrame.add(mainPanel, BorderLayout.CENTER);
		
		mainFrame.setIconImage(new ImageIcon("dc.png").getImage());
		mainFrame.setTitle("File Sharer");
		mainFrame.setMinimumSize(new Dimension(800, 600));
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
		mainFrame.setAlwaysOnTop(false);
	}
}