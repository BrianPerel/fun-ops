package tic.tac.toe;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
 *         names.
 */
public class Start implements ActionListener {

	private JFrame frame;
	JButton startBtn = new JButton("Start");
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Start window = new Start();
					window.frame.setVisible(true);
					window.frame.setTitle("Tic Tac Toe App by: Brian Perel");
					window.frame.setResizable(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application. Build all components
	 */
	public Start() {
		frame = new JFrame();
		frame.setBounds(100, 100, 399, 358);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Player 1:");
		lblNewLabel.setBounds(107, 59, 83, 25);
		frame.getContentPane().add(lblNewLabel);

		startBtn.setBounds(145, 192, 107, 35);
		frame.getContentPane().add(startBtn);
		startBtn.addActionListener(this);

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
	public void actionPerformed(ActionEvent ae) {

		// remove whitespace from name textfields before proceeding 
		TicTacToeBoard.playerOne = textField.getText().trim();
		TicTacToeBoard.playerTwo = textField_1.getText().trim();

		// if start button is pushed and both name fields arn't empty 
		if (ae.getSource() == startBtn && !textField.getText().isEmpty() && !textField_1.getText().isEmpty()) {

			// if first name field equals the second one
			if (textField.getText().equals(textField_1.getText())) {
				JOptionPane.showMessageDialog(frame.getComponent(0), "Please enter different player names");
				textField.setText("");
				textField_1.setText("");
				return; 
			}

			// if first letter of player one's name is lowercase make upper case 
			if (Character.isLowerCase(TicTacToeBoard.playerOne.charAt(0))) {
				TicTacToeBoard.playerOne = (TicTacToeBoard.playerOne.charAt(0) + "").toUpperCase()
						+ TicTacToeBoard.playerOne.substring(1);
			}

			// if first letter of player two's name is lowercase make upper case 
			if (Character.isLowerCase(TicTacToeBoard.playerTwo.charAt(0))) {
				TicTacToeBoard.playerTwo = (TicTacToeBoard.playerTwo.charAt(0) + "").toUpperCase()
						+ TicTacToeBoard.playerTwo.substring(1);
			}

			frame.dispose();
			new TicTacToeBoard();

		// if both name textfields are empty
		} else if (textField.getText().isEmpty() || textField_1.getText().isEmpty()) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "Please enter names for both players");
		}
	}
}
