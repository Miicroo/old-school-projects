package view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

/**
 * The splash screen to be displayed during startup.
 * @author Magnus Larsson
 */
public class LoadFrame extends JWindow {

	private static final long serialVersionUID = -1024146773317675649L;
	
	public LoadFrame(){
		
		JPanel panel = new JPanel();
		Icon loadIcon = new ImageIcon("res/load.gif");
		Icon logo = new ImageIcon("dc.png");
		JLabel loadLabel = new JLabel(loadIcon);
		JLabel label = new JLabel("Loading previous settings..");
		label.setFont(new Font("Tahoma", Font.BOLD, 16));
		JLabel label2 = new JLabel("FileSharer", logo, SwingConstants.RIGHT);
		label2.setFont(new Font("Tahoma", Font.BOLD, 16));		
		panel.setBackground(Color.white);
		panel.add(label2);
		panel.add(label);
		panel.add(loadLabel);
		setAlwaysOnTop(true);
		add(panel);

		setSize(logo.getIconWidth()+100, logo.getIconHeight()+100);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
