package tic.tac.toe;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * 
 * @author Brian Perel
 *
 *         Implementation for start window. Prompts for player's 1 and 2's
 *         names. Comment out log4j2 statements when creating a .jar
 */
public class Menu extends KeyAdapter implements ActionListener {

	JButton btnStart;
	private JFrame frame = new JFrame();
	static boolean startButtonSelected;
	private JTextField NameOneTextField, NameTwoTextField;
	// private static final Logger logger_ = Logger.getLogger(StartMenu.class);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Menu window = new Menu();
					window.frame.setVisible(true);
					window.frame.setTitle("Tic Tac Toe App by: Brian Perel");
					window.frame.setResizable(false);
					window.frame.setLocationRelativeTo(null);
					// BasicConfigurator.configure();
					// logger_.info("Starting tic tac toe log");
				} catch (Exception e) {
					// logger_.error("Error: " + e.toString());
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application. Build all components
	 */
	public Menu() {
		frame.setBounds(100, 100, 399, 358);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		ImageIcon image = new ImageIcon(getClass().getResource("bg.jpg"));
		JLabel backgroundLabel = new JLabel(image);
		frame.setContentPane(backgroundLabel);

		JLabel lblPlayer1 = new JLabel("Player 1:");
		lblPlayer1.setBounds(107, 59, 83, 25);
		frame.getContentPane().add(lblPlayer1);

		btnStart = new JButton("Start");
		btnStart.setBounds(145, 192, 107, 35);
		frame.getContentPane().add(btnStart);
		btnStart.addActionListener(this);
		btnStart.addKeyListener(this);
		btnStart.setBackground(new Color(144, 238, 144));

		NameOneTextField = new JTextField();
		NameOneTextField.setBounds(190, 54, 130, 35);
		frame.getContentPane().add(NameOneTextField);
		NameOneTextField.setColumns(10);

		JLabel lblPlayer2 = new JLabel("Player 2:");
		lblPlayer2.setBounds(107, 122, 64, 14);
		frame.getContentPane().add(lblPlayer2);

		NameTwoTextField = new JTextField();
		NameTwoTextField.setColumns(10);
		NameTwoTextField.setBounds(190, 112, 130, 35);
		frame.getContentPane().add(NameTwoTextField);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_ENTER && !NameOneTextField.getText().isEmpty() && !NameTwoTextField.getText().isEmpty()) {

			// remove extra whitespace from name textfields before proceeding
			GameBoard.playerOne = NameOneTextField.getText().trim();
			GameBoard.playerTwo = NameTwoTextField.getText().trim();

			// if first letter of player one's name is lowercase make upper case
			if (Character.isLowerCase(GameBoard.playerOne.charAt(0))) {
				GameBoard.playerOne = (GameBoard.playerOne.charAt(0) + "").toUpperCase()
						+ GameBoard.playerOne.substring(1);
			}

			// if first letter of player two's name is lowercase make upper case
			if (Character.isLowerCase(GameBoard.playerTwo.charAt(0))) {
				GameBoard.playerTwo = (GameBoard.playerTwo.charAt(0) + "").toUpperCase()
						+ GameBoard.playerTwo.substring(1);
			}

			frame.dispose();
			new GameBoard(true, true, false);

			// if one of the 2 textfields doesn't get a name
		} else if (NameOneTextField.getText().isEmpty() || NameTwoTextField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "Please enter names for both players", "Error",
					JOptionPane.ERROR_MESSAGE);
		} // if first name field equals the second one
		else if (NameOneTextField.getText().equals(NameTwoTextField.getText())) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "Please enter different player names", "Error",
					JOptionPane.ERROR_MESSAGE);
			NameOneTextField.setText("");
			NameTwoTextField.setText("");
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		// if start button is pushed and both name fields arn't empty
		if (ae.getSource() == btnStart && !NameOneTextField.getText().isEmpty() && !NameTwoTextField.getText().isEmpty()) {

			// remove whitespace from name textfields before proceeding
			GameBoard.playerOne = NameOneTextField.getText().trim();
			GameBoard.playerTwo = NameTwoTextField.getText().trim();

			// if first name field equals the second one
			if (NameOneTextField.getText().equals(NameTwoTextField.getText())) {
				JOptionPane.showMessageDialog(frame.getComponent(0), "Please enter different player names");
				NameOneTextField.setText("");
				NameTwoTextField.setText("");
				// return statement prevents bottom of method statements from being executed
				// (frame.dispose() and new GameBoard())
				return;
			}

			// if first letter of player one's name is lowercase make upper case
			if (Character.isLowerCase(GameBoard.playerOne.charAt(0))) {
				GameBoard.playerOne = (GameBoard.playerOne.charAt(0) + "").toUpperCase()
						+ GameBoard.playerOne.substring(1);
			}

			// if first letter of player two's name is lowercase make upper case
			if (Character.isLowerCase(GameBoard.playerTwo.charAt(0))) {
				GameBoard.playerTwo = (GameBoard.playerTwo.charAt(0) + "").toUpperCase()
						+ GameBoard.playerTwo.substring(1);
			}

			frame.dispose();
			new GameBoard(true, true, false);

			// if both name text fields are empty
		} else if (NameOneTextField.getText().isEmpty() || NameTwoTextField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "Please enter names for both players", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
