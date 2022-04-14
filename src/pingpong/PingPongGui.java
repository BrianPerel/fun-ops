package pingpong;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;

public class PingPongGui extends JFrame {

	private static final long serialVersionUID = 8513294011065865486L;
	private static final Color DARK_GREEN = new Color(0, 78, 3);

	public static void main(String[] args) {
		new PingPongGui();
	}

	/**
	 * Setups and creates the GUI
	 */
	public PingPongGui() {
		super("Pong Game");
		this.add(new GameBoard());
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		JMenuItem menuOption = new JMenuItem("Restart Game");
		menu.setMnemonic('r'); // alt+m = menu keyboard shortcut/ keyboard mnemonic
		menu.setDisplayedMnemonicIndex(-1);
		menu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		menuOption.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		menu.add(menuOption);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
		menuOption.addActionListener((ActionEvent e) -> {
			if (e.getSource() == menuOption)  {  
				this.dispose();
				new PingPongGui();
			}
		});
		
		this.setResizable(false);
		this.setBackground(DARK_GREEN);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);	
	}
}
