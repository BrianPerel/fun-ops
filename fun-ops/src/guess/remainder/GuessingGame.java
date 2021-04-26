package guess.remainder;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class GuessingGame implements ActionListener {

	private JFrame frame;
	private JTextField textFieldScore;
	private JTextField guessesTextField;
	private JTextField textFieldGuessTheNumber;
	private JTextField textFieldRandomNumber;
	JButton btnPlayAgain = new JButton("Play again?");
	JButton btnGuess = new JButton("Guess");
	static Random ran = new Random();
	static int randomNumber = ran.nextInt(100);
	static int guesses = 0;
	static int score = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					GuessingGame window = new GuessingGame();
					window.frame.setTitle("Guess the number App by: Brian Perel");
					window.frame.setResizable(false);
					window.frame.setLocationRelativeTo(null);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GuessingGame() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBackground(Color.WHITE);
		frame.setBounds(100, 100, 526, 352);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblScore = new JLabel("Score");
		lblScore.setBounds(21, 24, 34, 17);
		frame.getContentPane().add(lblScore);
		
		textFieldScore = new JTextField(Integer.toString(score));
		textFieldScore.setEditable(false);
		textFieldScore.setBounds(64, 22, 52, 20);
		frame.getContentPane().add(textFieldScore);
		textFieldScore.setColumns(10);
		
		JLabel lblGuesses = new JLabel("Guesses");
		lblGuesses.setBounds(145, 25, 65, 17);
		frame.getContentPane().add(lblGuesses);
		
		guessesTextField = new JTextField(Integer.toString(guesses));
		guessesTextField.setEditable(false);
		guessesTextField.setColumns(10);
		guessesTextField.setBounds(204, 23, 52, 20);
		frame.getContentPane().add(guessesTextField);	
		
		ImageIcon image = new ImageIcon(getClass().getResource("question.jpg"));
		JLabel lblImage = new JLabel(image);
		lblImage.setBounds(10, 66, 220, 238);
		frame.getContentPane().add(lblImage);
		
		JLabel lblNumberIs = new JLabel("The number is");
		lblNumberIs.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNumberIs.setBounds(302, 80, 102, 37);
		frame.getContentPane().add(lblNumberIs);
		
		textFieldRandomNumber = new JTextField(Integer.toString(randomNumber));
		textFieldRandomNumber.setEditable(false);
		textFieldRandomNumber.setColumns(10);
		textFieldRandomNumber.setBounds(400, 90, 34, 20);
		frame.getContentPane().add(textFieldRandomNumber);
		
		JLabel lblGuess = new JLabel("Enter a number b/w 1-99 to make 100");
		lblGuess.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblGuess.setBounds(240, 140, 275, 37);
		frame.getContentPane().add(lblGuess);
		
		btnGuess.setBounds(255, 230, 105, 23);
		btnGuess.addActionListener(this); 		
		frame.getContentPane().add(btnGuess);
		
		textFieldGuessTheNumber = new JTextField();
		textFieldGuessTheNumber.setBounds(345, 189, 52, 20);
		frame.getContentPane().add(textFieldGuessTheNumber);
		textFieldGuessTheNumber.setColumns(10);
		textFieldGuessTheNumber.addActionListener(this); 
		textFieldGuessTheNumber.setFocusable(true);

		JLabel lblScoringInfo = new JLabel("Successful guess = 10 points");
		lblScoringInfo.setBounds(283, 26, 172, 17);
		frame.getContentPane().add(lblScoringInfo);
		
		btnPlayAgain.setBounds(382, 230, 105, 23);
		frame.getContentPane().add(btnPlayAgain);
		btnPlayAgain.addActionListener(this);
	} 
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if(ae.getSource() == btnGuess && textFieldGuessTheNumber.getText().matches("^[0-9]*$")) {
			guesses++;
			
			if(textFieldGuessTheNumber.getText().isEmpty()) {
				JOptionPane.showMessageDialog(frame.getComponent(0), "Please enter a number");
				guessesTextField.setText(Integer.toString(guesses));
			} else if(Integer.valueOf(textFieldGuessTheNumber.getText()) >= 100) {
				JOptionPane.showMessageDialog(frame.getComponent(0), "Please enter a valid number");
				guessesTextField.setText(Integer.toString(guesses));
			} else if(Integer.valueOf(textFieldGuessTheNumber.getText()) + randomNumber == 100) {
				JOptionPane.showMessageDialog(frame.getComponent(0), "Correct! You made 100");
				guessesTextField.setText(Integer.toString(guesses));
				randomNumber = ran.nextInt(100);
				textFieldRandomNumber.setText(Integer.toString(randomNumber));
				score += 10;
			} else if(Integer.valueOf(textFieldGuessTheNumber.getText()) + randomNumber != 100) {
				JOptionPane.showMessageDialog(frame.getComponent(0), "Incorrect! That doesn't sum to 100");
				guessesTextField.setText(Integer.toString(guesses));
				if(score != 0) {
					score -= 10;
				}
			}
			
			// set score after action is completed
			textFieldScore.setText(Integer.toString(score));
			
		} else if(ae.getSource() == btnGuess && !textFieldGuessTheNumber.getText().matches("^[0-9]*$")){
			JOptionPane.showMessageDialog(frame.getComponent(0), "Please enter a numeric value");
		} else if(ae.getSource() == btnPlayAgain) {			
			JOptionPane.showMessageDialog(frame.getComponent(0), "Game reset");
			guessesTextField.setText("0");
			randomNumber = ran.nextInt(100);
			textFieldRandomNumber.setText(Integer.toString(randomNumber));
			textFieldScore.setText("0");
			guesses = 0;
		}
		
		// set guess text field to blank
		textFieldGuessTheNumber.setText("");
	}
}
