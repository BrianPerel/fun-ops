package com.pingpong;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serial;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
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
			System.out.println("Failed to set '" + e.getMessage() + "' UI LookAndFeel");
			e.printStackTrace();
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
		getContentPane().setLayout(new FlowLayout());

		// Set translucent background
		setContentPane(new JPanel() {
			@Serial
			private static final long serialVersionUID = -1599948337626679218L;

			@Override
		    protected void paintComponent(Graphics g) {
		        super.paintComponent(g);
		        Graphics2D g2d = (Graphics2D) g;
		        g2d.setPaint(new GradientPaint(0, 0, new Color(0, 0, 0, 200), 0, getHeight(), new Color(0, 0, 0, 100)));
		        g2d.fillRect(0, 0, getWidth(), getHeight());
		    }
		});

		getContentPane().add(new GameBoard());

		// changes the program's taskbar icon
	    setIconImage(new ImageIcon("res/graphics/taskbar_icons/pingpong.png").getImage());

		JMenuBar menuBar = new JMenuBar();
		menuBar.setMargin(new Insets(2, 2, 2, 2));
		JMenu menu = new JMenu("Menu ▼");
		menu.setMnemonic('r'); // alt+r = menu keyboard shortcut/ keyboard mnemonic
		menu.setDisplayedMnemonicIndex(-1);
		menu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		menu.setBackground(new Color(0, 126, 210));
		menu.setForeground(Color.WHITE);
		menu.setOpaque(true);
		menu.addMouseListener(new MouseAdapter() {
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
		menuOption.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_R, InputEvent.ALT_DOWN_MASK));

		menu.add(menuOption);
		menuBar.add(menu);
		setJMenuBar(menuBar);
		menuOption.addActionListener(actionEvent -> {
			if (actionEvent.getSource() == menuOption)  {
				dispose();
				new PingPongGui();
			}
		});

		setBackground(DARK_GREEN);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(1200, 700);
		setMinimumSize(new Dimension(getWidth(), getHeight()));
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
