package tic.tac.toe;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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

	private JFrame frame;
	JButton btnStart;
	static boolean startButtonSelected;
	private JTextField textField;
	private JTextField textField_1;
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
		frame = new JFrame();
		frame.setBounds(100, 100, 399, 358);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Player 1:");
		lblNewLabel.setBounds(107, 59, 83, 25);
		frame.getContentPane().add(lblNewLabel);

		btnStart = new JButton("Start");
		btnStart.setBounds(145, 192, 107, 35);
		frame.getContentPane().add(btnStart);
		btnStart.addActionListener(this);
		btnStart.addKeyListener(this);
		// using RGB color selector in setting of background
		btnStart.setBackground(new Color(144, 238, 144));

		textField = new JTextField();
		textField.setBounds(190, 54, 130, 35);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		JLabel lblPlayer = new JLabel("Player 2:");
		lblPlayer.setBounds(107, 122, 64, 14);
		frame.getContentPane().add(lblPlayer);

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(190, 112, 130, 35);
		frame.getContentPane().add(textField_1);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_ENTER && !textField.getText().isEmpty() &&
				 !textField_1.getText().isEmpty())  {
			
				// remove whitespace from name textfields before proceeding
				GameBoard.playerOne = textField.getText().trim();
				GameBoard.playerTwo = textField_1.getText().trim();
				
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
		} else if(textField.getText().isEmpty() || textField_1.getText().isEmpty()) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "Please enter names for both players");
		} // if first name field equals the second one
		else if (textField.getText().equals(textField_1.getText())) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "Please enter different player names");
			textField.setText("");
			textField_1.setText("");
		} 
	} 

	@Override
	public void actionPerformed(ActionEvent ae) {

		// if start button is pushed and both name fields arn't empty
		if (ae.getSource() == btnStart && !textField.getText().isEmpty() && !textField_1.getText().isEmpty()) {

			// remove whitespace from name textfields before proceeding
			GameBoard.playerOne = textField.getText().trim();
			GameBoard.playerTwo = textField_1.getText().trim();

			// if first name field equals the second one
			if (textField.getText().equals(textField_1.getText())) {
				JOptionPane.showMessageDialog(frame.getComponent(0), "Please enter different player names");
				textField.setText("");
				textField_1.setText("");
				// return statement prevents bottom of method statements from being executed (frame.dispose() and new GameBoard()) 
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
		} else if (textField.getText().isEmpty() || textField_1.getText().isEmpty()) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "Please enter names for both players");
		}
	}
}
