package com.tictactoe;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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

	private static final String ERROR_TITLE = "ERROR";
	protected static final String PLAYER = "Player";
	protected static final String COMPUTER = "Computer";
	private static final Color LIGHT_GREEN = new Color(144, 238, 144);
	private static final Logger logger_ = Logger.getLogger(StartMenu.class.getName());
	protected static JFrame frame;

	private JButton btnStart;
	private JFormattedTextField nameOneTextField;
	private JFormattedTextField nameTwoTextField;
	private JRadioButton playAgainstComputerRadioButton;

	public static void main(String[] args) {
		new StartMenu();
	}

	/**
	 * Create the application. Build all components
	 */
	public StartMenu() {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			logger_.info("Starting tic-tac-toe log");
		} catch (Exception e) {
			logger_.severe("Failed to set LookAndFeel\n" + e.toString());
			e.printStackTrace();
		}

		frame = new JFrame("Tic Tac Toe App by: Brian Perel");
		frame.setResizable(false);
		frame.setSize(399, 358);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		frame.setContentPane(new JLabel(new ImageIcon("res/graphics/bg-image-tac.jpg")));

		JLabel lblPlayer1 = new JLabel("Player 1:");
		lblPlayer1.setFont(new Font("MV Boli", Font.PLAIN, 20));
		lblPlayer1.setBounds(87, 56, 83, 25);
		frame.getContentPane().add(lblPlayer1);

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
		nameOneTextField.setBounds(190, 54, 130, 35);
		frame.getContentPane().add(nameOneTextField);
		nameOneTextField.setColumns(10);

		JLabel lblPlayer2 = new JLabel("Player 2:");
		lblPlayer2.setFont(new Font("MV Boli", Font.PLAIN, 20));
		lblPlayer2.setBounds(87, 114, 98, 25);
		frame.getContentPane().add(lblPlayer2);

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
		nameTwoTextField.setBounds(190, 112, 130, 35);
		frame.getContentPane().add(nameTwoTextField);

		btnStart = new JButton("Start");
		btnStart.setFont(new Font("Lucida Fax", Font.BOLD + Font.ITALIC, 14));
		btnStart.setBounds(145, 192, 107, 35);
		frame.getContentPane().add(btnStart);
		btnStart.addActionListener(this);
		btnStart.addKeyListener(this);
		btnStart.setBackground(LIGHT_GREEN);
		btnStart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		playAgainstComputerRadioButton = new JRadioButton("Play against computer");
		playAgainstComputerRadioButton.setBounds(207, 268, 157, 23);
		playAgainstComputerRadioButton.setOpaque(false);
		frame.getContentPane().add(playAgainstComputerRadioButton);
		playAgainstComputerRadioButton.addActionListener(this);
		playAgainstComputerRadioButton.addKeyListener(this);
		playAgainstComputerRadioButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		eventHandler(ke.getSource(), ke.getKeyChar());
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
		if (keyChar == KeyEvent.VK_ENTER && source == playAgainstComputerRadioButton) {
			// play against computer game flow
			frame.dispose();
			new CvPGameBoard(true, true, false);
			return; // prevents below code from running
		}

		String nameOne = nameOneTextField.getText().trim();
		String nameTwo = nameTwoTextField.getText().trim();

		// if start button is pushed and both name fields arn't empty and both names are
		// different
		if (keyChar == KeyEvent.VK_ENTER && source == btnStart
				&& !(nameOne.isEmpty() && nameTwo.isEmpty() || nameOne.equalsIgnoreCase(nameTwo))) {
			// first letter of name should be capitalized, and the rest of the name should
			// be in lowercase
			PvPGameBoard.setPlayerOnesName(nameOne.substring(0, 1).toUpperCase() + nameOne.substring(1).toLowerCase());
			PvPGameBoard.setPlayerTwosName(nameTwo.substring(0, 1).toUpperCase() + nameTwo.substring(1).toLowerCase());

			logger_.info("Player 1: " + PvPGameBoard.getPlayerOnesName() + ", Player 2: "
					+ PvPGameBoard.getPlayerTwosName());

			frame.dispose();
			new PvPGameBoard(true, true, false);

		} else {
			checkInput(nameOne, nameTwo, source);
		}
	}

	private void checkInput(String nameOne, String nameTwo, Object source) {
		// if one or both name textfields are empty
		if ((nameOne.isEmpty() || nameTwo.isEmpty()) && source == btnStart) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(frame.getComponent(0), "Please enter names for both players", ERROR_TITLE,
					JOptionPane.ERROR_MESSAGE);
		}
		// if entered player 1 or 2's name equals 'PLAYER' or 'COMPUTER'
		else if (PLAYER.equalsIgnoreCase(nameOne) || COMPUTER.equalsIgnoreCase(nameOne)
				|| PLAYER.equalsIgnoreCase(nameTwo) || COMPUTER.equalsIgnoreCase(nameTwo)) {
			JOptionPane.showMessageDialog(frame.getComponent(0),
					"Please don't use \'" + PLAYER + "\' or \'" + COMPUTER + "\' as a name", ERROR_TITLE,
					JOptionPane.ERROR_MESSAGE);
			nameOneTextField.setText("");
			nameTwoTextField.setText("");
			nameOneTextField.requestFocus();
		}
		// if first player's name field equals the second one
		else if (nameOne.equalsIgnoreCase(nameTwo) && source == btnStart) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(frame.getComponent(0), "Please enter different player names", ERROR_TITLE,
					JOptionPane.ERROR_MESSAGE);
			nameOneTextField.setText("");
			nameTwoTextField.setText("");
			nameOneTextField.requestFocus();
		}
	}
}
