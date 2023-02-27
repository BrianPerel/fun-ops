package com.tictactoe;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Implementation for app's start window. Prompts for player's 1 and 2's names.
 *
 * @author Brian Perel
 *
 */
public class StartMenu extends KeyAdapter implements ActionListener {

	private static final String ERROR_TITLE = "Error";
	protected static final String PLAYER = "Player";
	protected static final String COMPUTER = "Computer";
	private static final Color LIGHT_GREEN_COLOR = new Color(144, 238, 144);
	private static final Logger logger_ = Logger.getLogger(StartMenu.class.getName());
	protected static JFrame window;

	private JButton btnStart;
	private JFormattedTextField nameOneTextField;
	private JFormattedTextField nameTwoTextField;
	private JRadioButton playAgainstComputerRadioButton;

	public static void main(String[] args) {
		window = new JFrame("Tic Tac Toe App by: Brian Perel");
		new StartMenu();
	}

	/**
	 * Create the application. Build all components
	 */
	public StartMenu() {
		createGui();
	}

	private void createGui() {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			logger_.info("Starting tic-tac-toe log");
		} catch (Exception e) {
			logger_.severe("Failed to set LookAndFeel\n" + e);
			e.printStackTrace();
		}

		window.setResizable(false);
		window.setSize(399, 358);
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.getContentPane().setLayout(null);

		window.setContentPane(new JLabel(new ImageIcon("res/graphics/bg-image-tac.jpg")));

		JLabel lblPlayer1 = new JLabel("Player 1 (X):");
		lblPlayer1.setFont(new Font("MV Boli", Font.PLAIN, 20));
		lblPlayer1.setBounds(55, 56, 143, 25);
		window.getContentPane().add(lblPlayer1);

		nameOneTextField = new JFormattedTextField();
		// Use document filter to limit player 1's name to size of 12
		((AbstractDocument) nameOneTextField.getDocument()).setDocumentFilter(new DocumentFilter() {
			@Override
			public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
					throws BadLocationException {
				if ((fb.getDocument().getLength() + text.length() - length) <= 12) {
					super.replace(fb, offset, length, text, attrs);
				}
			}
		});
		nameOneTextField.setFont(new Font("DialogInput", Font.PLAIN, 14));
		nameOneTextField.setBounds(195, 54, 130, 35);
		window.getContentPane().add(nameOneTextField);
		nameOneTextField.setColumns(10);

		JLabel lblPlayer2 = new JLabel("Player 2 (O):");
		lblPlayer2.setFont(new Font("MV Boli", Font.PLAIN, 20));
		lblPlayer2.setBounds(52, 114, 143, 25);
		window.getContentPane().add(lblPlayer2);

		nameTwoTextField = new JFormattedTextField();
		// Use document filter to limit player 1's name to size of 12
		((AbstractDocument) nameTwoTextField.getDocument()).setDocumentFilter(new DocumentFilter() {
			@Override
			public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
					throws BadLocationException {
				if ((fb.getDocument().getLength() + text.length() - length) <= 12) {
					super.replace(fb, offset, length, text, attrs);
				}
			}
		});
		nameTwoTextField.setFont(new Font("DialogInput", Font.PLAIN, 14));
		nameTwoTextField.setColumns(10);
		nameTwoTextField.setBounds(195, 112, 130, 35);
		window.getContentPane().add(nameTwoTextField);

		btnStart = new JButton("Start");
		btnStart.setFont(new Font("Lucida Fax", Font.BOLD + Font.ITALIC, 14));
		btnStart.setBounds(145, 192, 107, 35);
		window.getContentPane().add(btnStart);
		btnStart.addActionListener(this);
		btnStart.addKeyListener(this);
		btnStart.setBackground(LIGHT_GREEN_COLOR);
		btnStart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		setHoverColor(btnStart);

		playAgainstComputerRadioButton = new JRadioButton("Play against computer");
		playAgainstComputerRadioButton.setBounds(207, 268, 157, 23);
		playAgainstComputerRadioButton.setOpaque(false);
		window.getContentPane().add(playAgainstComputerRadioButton);
		playAgainstComputerRadioButton.addActionListener(this);
		playAgainstComputerRadioButton.addKeyListener(this);
		playAgainstComputerRadioButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		window.setVisible(true);
		window.setLocationRelativeTo(null);
	}

	/**
	 * Sets a custom color to appear when you hover over a specific button
	 *
	 * @param argBtn button on which you want a different color to appear when hovering
	 */
	protected static void setHoverColor(JButton argBtn) {
		argBtn.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				argBtn.setBackground(new Color(38, 195, 54));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				argBtn.setBackground(LIGHT_GREEN_COLOR);
			}
		});
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		if(ke.getKeyChar() != KeyEvent.VK_TAB) {
			eventHandler(ke.getSource(), ke.getKeyChar());
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		eventHandler(ae.getSource(), (char) KeyEvent.VK_ENTER);
	}

	/**
	 * A common method used by both types of listeners (for keys and action buttons)
	 * that handles an event
	 *
	 * @param source  the object on which the event initially occurred
	 * @param keyChar the character associated with the key in this event
	 */
	private void eventHandler(Object source, char keyChar) {
		if (keyChar == KeyEvent.VK_ENTER && source.equals(playAgainstComputerRadioButton)) {
			// play against computer game flow
			window.dispose();
			new CvPGameBoard(true, true, false);
			return; // prevents below code from running
		}

		String nameOne = nameOneTextField.getText().trim();
		String nameTwo = nameTwoTextField.getText().trim();

		// if start button is pushed and both name fields aren't empty and both names are
		// different
		if (validationRulesCheck(keyChar, source, nameOne, nameTwo)) {
			// first letter of name should be capitalized, and the rest of the name should
			// be in lowercase
			PvPGameBoard.setPlayerOnesName(nameOne.substring(0, 1).toUpperCase() + nameOne.substring(1).toLowerCase());
			PvPGameBoard.setPlayerTwosName(nameTwo.substring(0, 1).toUpperCase() + nameTwo.substring(1).toLowerCase());

			logger_.info(MessageFormat.format("Player 1: {0}, Player 2: {1}", PvPGameBoard.getPlayerOnesName(), PvPGameBoard.getPlayerTwosName()));

			window.dispose();
			new PvPGameBoard(true, true, false);

		} else {
			checkInput(nameOne, nameTwo, source);
		}
	}

	private boolean validationRulesCheck(char keyChar, Object source, String nameOne, String nameTwo) {
		return keyChar == KeyEvent.VK_ENTER && source.equals(btnStart)
				&& !(nameOne.isEmpty() || nameTwo.isEmpty() || nameOne.equalsIgnoreCase(nameTwo))
				&& !(PLAYER.equalsIgnoreCase(nameOne) || COMPUTER.equalsIgnoreCase(nameOne)
				|| PLAYER.equalsIgnoreCase(nameTwo) || COMPUTER.equalsIgnoreCase(nameTwo));
	}

	private void checkInput(String nameOne, String nameTwo, Object source) {
		// if one or both name textfields are empty
		if ((nameOne.isEmpty() || nameTwo.isEmpty()) && source.equals(btnStart)) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(window.getComponent(0), "Please enter names for both players", ERROR_TITLE,
					JOptionPane.ERROR_MESSAGE);
		}
		// if entered player 1 or 2's name equals 'PLAYER' or 'COMPUTER'
		else if (PLAYER.equalsIgnoreCase(nameOne) || COMPUTER.equalsIgnoreCase(nameOne)
				|| PLAYER.equalsIgnoreCase(nameTwo) || COMPUTER.equalsIgnoreCase(nameTwo)) {
			JOptionPane.showMessageDialog(window.getComponent(0),
					String.format("Please don't use '%s' or '%s' as a name", PLAYER, COMPUTER), ERROR_TITLE,
					JOptionPane.ERROR_MESSAGE);
			nameOneTextField.setText("");
			nameTwoTextField.setText("");
			nameOneTextField.requestFocus();
		}
		// if first player's name field equals the second one
		else if (nameOne.equalsIgnoreCase(nameTwo) && source.equals(btnStart)) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(window.getComponent(0), "Please enter different player names", ERROR_TITLE,
					JOptionPane.ERROR_MESSAGE);
			nameOneTextField.setText("");
			nameTwoTextField.setText("");
			nameOneTextField.requestFocus();
		}
	}
}
