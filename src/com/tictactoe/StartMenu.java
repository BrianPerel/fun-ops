package com.tictactoe;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Implementation for the app's start window which prompts for player 1 and 2's names.
 *
 * @author Brian Perel
 *
 */
public class StartMenu extends KeyAdapter implements ActionListener {

	private static final String ERROR_TITLE = "Error";
	private static final Color LIGHT_GREEN_COLOR = new Color(144, 238, 144);
	private static final Logger LOG = Logger.getLogger(StartMenu.class.getName());
	protected static JFrame window = new JFrame("Tic Tac Toe App by: Brian Perel");

	private JButton btnStart;
	private JFormattedTextField nameOneTextField;
	private JFormattedTextField nameTwoTextField;
	private JRadioButton playAgainstComputerRadioButton;
	private TicTacToe ticTacToeGame;

	/**
	 * Initializes the application, customizes the logger for the current class instance, and builds all GUI components
	 */
	public StartMenu() {
		TicTacToe.customizeLogger(LOG);
		ticTacToeGame = new TicTacToe();
		createGui();
		window.setVisible(true);
	}

	private void createGui() {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			LOG.severe("Failed to set LookAndFeel\n" + e);
			e.printStackTrace();
		}

		// declare max number of characters a player can use for their name entry
		final int MAX_CHARS_LIMIT = 12;

		// changes the program's taskbar icon
	    window.setIconImage(new ImageIcon("res/graphics/taskbar_icons/tic-tac-toe.png").getImage());

		window.setResizable(false);
		window.setSize(399, 358);
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.getContentPane().setLayout(null);

		window.setContentPane(new JLabel(new ImageIcon("res/graphics/bg-image-tac.jpg")));

		JLabel lblPlayer1 = new JLabel("Player 1 (" + TicTacToe.PLAYER_ONE_SHAPE + "):");
		lblPlayer1.setFont(new Font("MV Boli", Font.PLAIN, 20));
		lblPlayer1.setBounds(55, 56, 143, 25);
		window.getContentPane().add(lblPlayer1);

		nameOneTextField = new JFormattedTextField();
		// Using a custom GUI document filter to limit player one's name to a size of 12 and prevent all invalid data type input
		((AbstractDocument) nameOneTextField.getDocument()).setDocumentFilter(new DocumentFilter() {
			@Override
			public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
					throws BadLocationException {
				if ((fb.getDocument().getLength() + text.length() - length) <= MAX_CHARS_LIMIT) {
					super.replace(fb, offset, length, text, attrs);
				}
			}
		});
		nameOneTextField.setFont(new Font("DialogInput", Font.PLAIN, 14));
		nameOneTextField.setBounds(195, 54, 130, 35);
		window.getContentPane().add(nameOneTextField);
		nameOneTextField.setColumns(10);

		JLabel lblPlayer2 = new JLabel("Player 2 (" + TicTacToe.PLAYER_TWO_SHAPE + "):");
		lblPlayer2.setFont(new Font("MV Boli", Font.PLAIN, 20));
		lblPlayer2.setBounds(52, 114, 143, 25);
		window.getContentPane().add(lblPlayer2);

		nameTwoTextField = new JFormattedTextField();
		// Using a custom GUI document filter to limit player two's name to a size of 12 and prevent all invalid data type input
		((AbstractDocument) nameTwoTextField.getDocument()).setDocumentFilter(new DocumentFilter() {
			@Override
			public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
					throws BadLocationException {
				if ((fb.getDocument().getLength() + text.length() - length) <= MAX_CHARS_LIMIT) {
					super.replace(fb, offset, length, text, attrs);
				}
			}
		});
		nameTwoTextField.setFont(new Font("DialogInput", Font.PLAIN, 14));
		nameTwoTextField.setColumns(10);
		nameTwoTextField.setBounds(195, 112, 130, 35);
		window.getContentPane().add(nameTwoTextField);

		btnStart = new JButton("Start");
		btnStart.setFont(new Font("Lucida Fax", (Font.BOLD + Font.ITALIC), 14));
		btnStart.setBounds(145, 192, 107, 35);
		window.getContentPane().add(btnStart);
		btnStart.addActionListener(this);
		btnStart.addKeyListener(this);
		btnStart.setBackground(LIGHT_GREEN_COLOR);
		btnStart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		setBtnHoverColor(btnStart);

		playAgainstComputerRadioButton = new JRadioButton("Play against computer");
		playAgainstComputerRadioButton.setBounds(207, 268, 157, 23);
		playAgainstComputerRadioButton.setOpaque(false);
		window.getContentPane().add(playAgainstComputerRadioButton);
		playAgainstComputerRadioButton.addActionListener(this);
		playAgainstComputerRadioButton.addKeyListener(this);
		playAgainstComputerRadioButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		window.setLocationRelativeTo(null);
	}

	/**
	 * Sets a custom hover color for a specific button and then reverts to the default color when the mouse exits
	 *
	 * @param argBtn The button for which you want a custom hover color to appear
	 */
	protected static void setBtnHoverColor(JButton argBtn) {
		argBtn.addMouseListener(new MouseAdapter() {
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
		if (ke.getKeyChar() != KeyEvent.VK_TAB) {
			eventHandler(ke.getSource(), ke.getKeyChar());
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		eventHandler(ae.getSource(), (char) KeyEvent.VK_ENTER);
	}

	/**
	 * A common handler used by both types of event listeners (action and key listener) for handling events
	 *
	 * @param source the object on which the event initially occurred
	 * @param keyChar the character associated with the key in this event
	 */
	private void eventHandler(Object source, char keyChar) {
		if (keyChar == KeyEvent.VK_ENTER && source.equals(playAgainstComputerRadioButton)) {
			// play against computer game flow
			window.dispose();
			new CvPGameBoard(true, true, ticTacToeGame);
			return; // prevents below code from running to complete the switching of game modes
		}

		String nameOne = nameOneTextField.getText().trim();
		String nameTwo = nameTwoTextField.getText().trim();

		// check if the start button is pressed and both name fields are filled with distinct names
		if (validateNames(nameOne, nameTwo) && checkStartConditions(keyChar, source)) {
			// ensures that the first letter of the name is capitalized and all other letters are in lowercase
			String capitalizedNameOne = nameOne.substring(0, 1).toUpperCase() + nameOne.substring(1).toLowerCase();
			String capitalizedNameTwo = nameTwo.substring(0, 1).toUpperCase() + nameTwo.substring(1).toLowerCase();

			ticTacToeGame.setPlayerNames(capitalizedNameOne, capitalizedNameTwo);

			LOG.info(MessageFormat.format("Player 1: {0}, Player 2: {1}", ticTacToeGame.getPlayerOnesName(), ticTacToeGame.getPlayerTwosName()));

			window.dispose();
			new PvPGameBoard(true, true, ticTacToeGame);
		}
	}

	/**
	 * Validates input names and checks conditions for starting a game, providing feedback and handling errors.
	 *
	 * @param nameOne The name of player one.
	 * @param nameTwo The name of player two.
	 * @return {@code true} if conditions for starting a game are met, {@code false} otherwise.
	 *         Conditions include both names being non-empty, distinct, and not equal to predefined values.
	 */
	private boolean validateNames(String nameOne, String nameTwo) {
	    // If one or both name textfields are empty
	    if (nameOne.isEmpty() || nameTwo.isEmpty()) {
	        Toolkit.getDefaultToolkit().beep();
	        nameOneTextField.setBorder(nameOne.isEmpty() ? BorderFactory.createLineBorder(Color.RED, 1)
	                : BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
	        nameTwoTextField.setBorder(nameTwo.isEmpty() ? BorderFactory.createLineBorder(Color.RED, 1)
	                : BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));

	        JOptionPane.showMessageDialog(window.getComponent(0), "Please enter names for both players", ERROR_TITLE,
	                JOptionPane.ERROR_MESSAGE);

	        return false;
	    }

	    // If the entered names for player 1 or 2 are set as 'PLAYER' or 'COMPUTER'
	    if (TicTacToe.PLAYER.equalsIgnoreCase(nameOne) || TicTacToe.COMPUTER.equalsIgnoreCase(nameOne)
	            || TicTacToe.PLAYER.equalsIgnoreCase(nameTwo) || TicTacToe.COMPUTER.equalsIgnoreCase(nameTwo)) {
	        Toolkit.getDefaultToolkit().beep();
	        JOptionPane.showMessageDialog(window.getComponent(0),
	                String.format("Please don't use '%s' or '%s' as a name", TicTacToe.PLAYER, TicTacToe.COMPUTER), ERROR_TITLE,
	                JOptionPane.ERROR_MESSAGE);
	        return false;
	    }

	    // If player one's and two's textfields are equal
	    else if (nameOne.equalsIgnoreCase(nameTwo)) {
	        Toolkit.getDefaultToolkit().beep();
	        JOptionPane.showMessageDialog(window.getComponent(0), "Please enter different player names", ERROR_TITLE,
	                JOptionPane.ERROR_MESSAGE);

	        nameOneTextField.requestFocus();
	        return false;
	    }

	    return true;
	}

	/**
	 * Checks conditions for starting a game based on key character and source object.
	 *
	 * @param keyChar The key character associated with the event.
	 * @param source The source object triggering the input check.
	 * @return true if conditions for starting a game are met, false otherwise.
	 *         Conditions include pressing Enter or Space key and the source being the start button (btnStart).
	 */
	private boolean checkStartConditions(char keyChar, Object source) {
	    return (keyChar == KeyEvent.VK_ENTER || keyChar == KeyEvent.VK_SPACE) && source.equals(btnStart);
	}
}
