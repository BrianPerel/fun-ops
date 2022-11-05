package com.pingpong;

import java.awt.Color;
import java.awt.Cursor;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

public class PingPongGui extends JFrame {

	private static final long serialVersionUID = 8513294011065865486L;
	private static final Color DARK_GREEN = new Color(0, 78, 3);

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			System.out.println("Failed to set LookAndFeel\n" + e.getMessage());
		}

		new PingPongGui();
	}

	/**
	 * Setups and creates the GUI
	 */
	@SuppressWarnings("deprecation")
	public PingPongGui() {
		super("Pong Game");
		this.add(new GameBoard());

		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		menu.setMnemonic('r'); // alt+r = menu keyboard shortcut/ keyboard mnemonic
		menu.setDisplayedMnemonicIndex(-1);
		menu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		JMenuItem menuOption = new JMenuItem("Restart Game");
		menuOption.setIcon(new ImageIcon("res/graphics/restart-icon.png"));
		menuOption.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		menuOption.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.ALT_MASK));

		menu.add(menuOption);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
		menuOption.addActionListener(actionEvent -> {
			if (actionEvent.getSource() == menuOption)  {
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
