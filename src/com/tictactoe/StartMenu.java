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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

/**
 * Implementation for start window. Prompts for player's 1 and 2's names.
 * Comment out log4j2 statements when creating a .jar <br>
 * 
 * @author Brian Perel
 *
 */
public class StartMenu extends KeyAdapter implements ActionListener {

	private JFrame frame;
	private JButton btnStart;
	private JTextField nameOneTextField;
	private JTextField nameTwoTextField;
	private static final String ERROR = "ERROR";
	protected static final String PLAYER = "Player";
	protected static final String COMPUTER = "Computer";
	protected JRadioButton playAgainstComputerRadioButton;
	private static final Color LIGHT_GREEN = new Color(144, 238, 144);
	private final Logger logger_ = Logger.getLogger(this.getClass().getName());

	public static void main(String[] args) {			
		new StartMenu();
	}

	/**
	 * Create the application. Build all components
	 */
	public StartMenu() {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");			
			logger_.info("Starting tic tac toe log");
		} catch (Exception e) {
			logger_.severe("Error: " + e.toString());
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

		nameOneTextField = new JTextField();
		nameOneTextField.setFont(new Font("DialogInput", Font.PLAIN, 14));
		nameOneTextField.setBounds(190, 54, 130, 35);
		frame.getContentPane().add(nameOneTextField);
		nameOneTextField.setColumns(10);

		JLabel lblPlayer2 = new JLabel("Player 2:");
		lblPlayer2.setFont(new Font("MV Boli", Font.PLAIN, 20));
		lblPlayer2.setBounds(87, 114, 98, 25);
		frame.getContentPane().add(lblPlayer2);

		nameTwoTextField = new JTextField();
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
	public void keyPressed(KeyEvent e) {
		eventHandler(e.getSource(), e.getKeyChar());
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		eventHandler(ae.getSource(), (char) KeyEvent.VK_ENTER);
	}

	public void eventHandler(Object source, char keyChar) {
		if (keyChar == KeyEvent.VK_ENTER && source == playAgainstComputerRadioButton) {
			frame.dispose();
			new CvPGameBoard(true, true, false);
			return;
		}
		
		String nameOne = nameOneTextField.getText().trim();
		String nameTwo = nameTwoTextField.getText().trim();
		
		if (nameOne.equalsIgnoreCase(PLAYER) || nameOne.equalsIgnoreCase(COMPUTER)
				|| nameTwo.equalsIgnoreCase(PLAYER) || nameTwo.equalsIgnoreCase(COMPUTER)) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "Please don't use 'player' or 'computer' as a name", ERROR,
					JOptionPane.ERROR_MESSAGE);
			nameOneTextField.setText("");
			nameTwoTextField.setText("");
			nameOneTextField.requestFocus();
		}		
		// if start button is pushed and both name fields arn't empty and both names are different
		else if (keyChar == KeyEvent.VK_ENTER && source == btnStart && !nameOne.isEmpty()
				&& !(nameTwo.isEmpty() || nameOne.equalsIgnoreCase(nameTwo))) {
			// first letter of name should be capitalized, and the rest of the name should be in lowercase
			PvPGameBoard.setPlayerOnesName(nameOne.substring(0, 1).toUpperCase() + nameOne.substring(1).toLowerCase());
			PvPGameBoard.setPlayerTwosName(nameTwo.substring(0, 1).toUpperCase() + nameTwo.substring(1).toLowerCase());

			logger_.info("Player 1: " + PvPGameBoard.getPlayerOnesName());
			logger_.info("Player 2: " + PvPGameBoard.getPlayerTwosName());

			frame.dispose();
			new PvPGameBoard(true, true, false);
		} 
		// if one or both name textfields are empty
		else if ((nameOne.isEmpty() || nameTwo.isEmpty()) && source == btnStart) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(frame.getComponent(0), "Please enter names for both players", ERROR,
					JOptionPane.ERROR_MESSAGE);
		} 
		// if first name field equals the second one
		else if (nameOne.equalsIgnoreCase(nameTwo) && source == btnStart) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(frame.getComponent(0), "Please enter different player names", ERROR,
					JOptionPane.ERROR_MESSAGE);
			nameOneTextField.setText("");
			nameTwoTextField.setText("");
			nameOneTextField.requestFocus();
		}
	}
}
