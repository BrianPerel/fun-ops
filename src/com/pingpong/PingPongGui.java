package com.pingpong;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.io.Serial;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

public class PingPongGui extends JFrame {

	@Serial
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
	public PingPongGui() {
		super("Pong Game");
		createGui();
	}

	private void createGui() {
		this.add(new GameBoard());

		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu \u25BC"); // unicode for drop down arrow (black triangle) = \u25BC
		menu.setMnemonic('r'); // alt+r = menu keyboard shortcut/ keyboard mnemonic
		menu.setDisplayedMnemonicIndex(-1);
		menu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		menu.setBackground(new Color(0, 126, 210));
		menu.setForeground(Color.WHITE);
		menu.setOpaque(true);
		menu.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				menu.setBackground(new Color(31, 83, 162));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				menu.setBackground(new Color(0, 126, 210));
			}
		});

		JMenuItem menuOption = new JMenuItem("Restart Game");
		menuOption.setIcon(new ImageIcon("res/graphics/restart-sign.png"));
		menuOption.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		menuOption.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.ALT_DOWN_MASK));

		menu.add(menuOption);
		menuBar.add(menu);
		setJMenuBar(menuBar);
		menuOption.addActionListener(actionEvent -> {
			if (actionEvent.getSource() == menuOption)  {
				dispose();
				new PingPongGui();
			}
		});

		setResizable(false);
		setBackground(DARK_GREEN);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		setLocationRelativeTo(null);
	}


}
