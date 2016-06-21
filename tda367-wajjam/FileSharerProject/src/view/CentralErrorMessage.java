package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import javax.swing.*;

/**
 * This class is used to display messages.
 * 
 * @author Albin Bramstång
 */
public class CentralErrorMessage extends Observable implements ActionListener {

	private JFrame mainFrame = new JFrame();
	private JPanel mainPanel = new JPanel();
	private JTextArea messageArea = new JTextArea();
	private JScrollPane messagePane = new JScrollPane(messageArea);
	private JButton confirmButton = new JButton("OK");
	private JPanel buttonPanel = new JPanel();
	
	public CentralErrorMessage() {
		
		getMessageArea().setEditable(false);
		getMessageArea().setBackground(new Color(240, 240, 240));
		getMessageArea().setFont(new Font("Tahoma", Font.PLAIN, 11));
		getMessageArea().setLineWrap(true);
		getMessageArea().setWrapStyleWord(true);
		
		confirmButton.addActionListener(this);
		buttonPanel.add(confirmButton);
		
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(messagePane);
		mainPanel.add(buttonPanel);
		
		mainFrame.add(mainPanel);
		mainFrame.setIconImage(new ImageIcon("dc.png").getImage());
		mainFrame.setTitle("Error");
		mainFrame.setPreferredSize(new Dimension(300, 200));
		mainFrame.setResizable(false);
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == confirmButton) {
			mainFrame.dispose();
		}
	}

	public void setMessageArea(JTextArea messageArea) {
		this.messageArea = messageArea;
	}

	public JTextArea getMessageArea() {
		return messageArea;
	}

	public void setVisible(boolean b) {
		mainFrame.setVisible(b);
	}
}
