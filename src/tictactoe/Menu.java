package tictactoe;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

// import org.apache.log4j.Logger;

/**
 * Implementation for start window. Prompts for player's 1 and 2's names.
 * Comment out log4j2 statements when creating a .jar <br>
 * 
 * @author Brian Perel
 *
 */
public class Menu extends KeyAdapter implements ActionListener {

	private JButton btnStart;
	private JFrame frame = new JFrame();
	private static final String ERROR = "ERROR";
	private JTextField nameOneTextField, nameTwoTextField;
	private JRadioButton playAgainstComputerRadioButton;
	// private static final Logger logger = Logger.getLogger(Menu.class);

	public static void main(String[] args) {		
		try {
			Menu window = new Menu();
			window.frame.setVisible(true);
			window.frame.setTitle("Tic Tac Toe App by: Brian Perel");
			window.frame.setResizable(false);
			window.frame.setLocationRelativeTo(null);
			// logger.info("Starting tic tac toe log");
		} catch (Exception e) {
			// logger.error("Error: " + e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * Create the application. Build all components
	 */
	public Menu() {
		frame.setBounds(100, 100, 399, 358);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		btnStart.setFont(new Font("Lucida Fax", Font.BOLD, 14));
		btnStart.setBounds(145, 192, 107, 35);
		frame.getContentPane().add(btnStart);
		btnStart.addActionListener(this);
		btnStart.addKeyListener(this);
		btnStart.setBackground(new Color(144, 238, 144));
		
		playAgainstComputerRadioButton = new JRadioButton("Play against computer");
		playAgainstComputerRadioButton.setBounds(207, 275, 157, 23);
		frame.getContentPane().add(playAgainstComputerRadioButton);
		playAgainstComputerRadioButton.addActionListener(this);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_ENTER && !nameOneTextField.getText().isEmpty()
				&& !nameTwoTextField.getText().isEmpty() && !nameOneTextField.getText().equalsIgnoreCase(nameTwoTextField.getText())) {

			// remove extra whitespace from name textfields before proceeding
			GameBoard.setPlayerOnesName(nameOneTextField.getText().trim());
			GameBoard.setPlayerTwosName(nameTwoTextField.getText().trim());

			// if first letter of player one's name is lowercase make upper case
			if (Character.isLowerCase(GameBoard.getPlayerOnesName().charAt(0))) {
				GameBoard.setPlayerOnesName((GameBoard.getPlayerOnesName().charAt(0) + "").toUpperCase()
						+ GameBoard.getPlayerOnesName().substring(1));
			}

			// if first letter of player two's name is lowercase make upper case
			if (Character.isLowerCase(GameBoard.getPlayerTwosName().charAt(0))) {
				GameBoard.setPlayerTwosName((GameBoard.getPlayerTwosName().charAt(0) + "").toUpperCase()
						+ GameBoard.getPlayerTwosName().substring(1));
			}

			// logger.info("Player 1: " + GameBoard.getPlayerOnesName());
			// logger.info("Player 2: " + GameBoard.getPlayerTwosName());

			frame.dispose();
			new GameBoard(true, true, false);

			// if one of the 2 textfields doesn't get a name
		} 
		else if (nameOneTextField.getText().isEmpty() || nameTwoTextField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "Please enter names for both players", ERROR,
					JOptionPane.ERROR_MESSAGE);
		} // if first name field equals the second one
		else if (nameOneTextField.getText().equalsIgnoreCase(nameTwoTextField.getText())) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "Please enter different player names", ERROR,
					JOptionPane.ERROR_MESSAGE);
			nameOneTextField.setText("");
			nameTwoTextField.setText("");
			nameOneTextField.requestFocus();
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == playAgainstComputerRadioButton) {
			frame.dispose();
			new GameBoardTwo(true, true, false);
		}
		// if start button is pushed and both name fields arn't empty
		else if (ae.getSource() == btnStart && !nameOneTextField.getText().isEmpty()
				&& !nameTwoTextField.getText().isEmpty()) {

			// remove whitespace from name textfields before proceeding
			GameBoard.setPlayerOnesName(nameOneTextField.getText().trim());
			GameBoard.setPlayerTwosName(nameTwoTextField.getText().trim());

			// if first name field equals the second one
			if (nameOneTextField.getText().equalsIgnoreCase(nameTwoTextField.getText())) {
				JOptionPane.showMessageDialog(frame.getComponent(0), "Please enter different player names");
				nameOneTextField.setText("");
				nameTwoTextField.setText("");
				// return statement prevents bottom of method statements from being executed
				// (frame.dispose() and new GameBoard())
				return;
			}

			// if first letter of player one's name is lowercase make upper case
			if (Character.isLowerCase(GameBoard.getPlayerOnesName().charAt(0))) {
				GameBoard.setPlayerOnesName((GameBoard.getPlayerOnesName().charAt(0) + "").toUpperCase()
						+ GameBoard.getPlayerOnesName().substring(1));
			}

			// if first letter of player two's name is lowercase make upper case
			if (Character.isLowerCase(GameBoard.getPlayerTwosName().charAt(0))) {
				GameBoard.setPlayerTwosName((GameBoard.getPlayerTwosName().charAt(0) + "").toUpperCase()
						+ GameBoard.getPlayerTwosName().substring(1));
			}
			
			// logger.info("Player 1: " + GameBoard.getPlayerOnesName());
			// logger.info("Player 2: " + GameBoard.getPlayerTwosName());

			frame.dispose();
			new GameBoard(true, true, false);

			// if both name text fields are empty
		} else if (nameOneTextField.getText().isEmpty() || nameTwoTextField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "Please enter names for both players", ERROR,
					JOptionPane.ERROR_MESSAGE);
		} 
	}
}
