package guess.remainder;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

public class GuessingGame implements ActionListener {

	private JFrame frame;
	private JTextField textFieldScore;
	private JTextField guessesTextField;
	private JFormattedTextField textFieldGuessTheNumber;
	private JTextField textFieldRandomNumber;
	JButton btnPlayAgain = new JButton("Play again?");
	JButton btnGuess = new JButton("Guess");
	Random ran = new Random();
	int randomNumber = ran.nextInt(100);
	int guesses = 0;
	int score = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					GuessingGame window = new GuessingGame();
					window.frame.setTitle("Number Guessing Game by: Brian Perel");
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
		textFieldScore.setColumns(10);
		textFieldScore.setFocusable(false);
		frame.getContentPane().add(textFieldScore);

		JLabel lblGuesses = new JLabel("Total Guesses");
		lblGuesses.setBounds(144, 24, 84, 17);
		frame.getContentPane().add(lblGuesses);

		guessesTextField = new JTextField(Integer.toString(guesses));
		guessesTextField.setEditable(false);
		guessesTextField.setColumns(10);
		guessesTextField.setBounds(238, 22, 52, 20);
		guessesTextField.setFocusable(false);
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
		textFieldRandomNumber.setFocusable(false);
		frame.getContentPane().add(textFieldRandomNumber);

		JLabel lblGuess = new JLabel("Enter a number b/w 1-99 to make 100");
		lblGuess.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblGuess.setBounds(240, 140, 275, 37);
		frame.getContentPane().add(lblGuess);

		btnGuess.setBounds(255, 230, 105, 23);
		btnGuess.addActionListener(this);
		frame.getContentPane().add(btnGuess);

		textFieldGuessTheNumber = new JFormattedTextField(createFormatter("##"));
		textFieldGuessTheNumber.setBounds(352, 188, 41, 20);
		frame.getContentPane().add(textFieldGuessTheNumber);
		textFieldGuessTheNumber.setColumns(10);
		textFieldGuessTheNumber.addActionListener(this);

		JLabel lblScoringInfo = new JLabel("Successful guess = 10 points");
		lblScoringInfo.setBounds(315, 24, 172, 17);
		frame.getContentPane().add(lblScoringInfo);

		btnPlayAgain.setBounds(382, 230, 105, 23);
		frame.getContentPane().add(btnPlayAgain);
		btnPlayAgain.addActionListener(this);
	}
	
	/**
	 * MaskFormatter is used to specify/create the format of a text field
	 * @param s is the text field's user input area (the box)
	 * @return the formatted text field 
	 */
	protected MaskFormatter createFormatter(String s) {
	    MaskFormatter formatter = null;
	    try {
	        formatter = new MaskFormatter(s);
	    } catch (java.text.ParseException exc) {
	        System.err.println("formatter is bad: " + exc.getMessage());
	        System.exit(-1);
	    }
	    return formatter;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		
		// if guess btn is pushed and input is numeric data
		if (ae.getSource() == btnGuess && textFieldGuessTheNumber.getText().matches("-?[1-9]\\d*|0")) {
			guesses++;

			// if input remainder entered is outside of range 1-99
			if (Integer.valueOf(textFieldGuessTheNumber.getText()) >= 100 || Integer.valueOf(textFieldGuessTheNumber.getText()) <= 0) {
				JOptionPane.showMessageDialog(frame.getComponent(0), "Please enter a valid number");
				guessesTextField.setText(Integer.toString(guesses));
			} else if (Integer.valueOf(textFieldGuessTheNumber.getText()) + randomNumber == 100) {
				JOptionPane.showMessageDialog(frame.getComponent(0), "Correct! You made 100");
				guessesTextField.setText(Integer.toString(guesses));
				randomNumber = ran.nextInt(100);
				textFieldRandomNumber.setText(Integer.toString(randomNumber));
				score += 10;
			} else if (Integer.valueOf(textFieldGuessTheNumber.getText()) + randomNumber != 100) {
				JOptionPane.showMessageDialog(frame.getComponent(0), "Incorrect! That doesn't sum to 100");
				guessesTextField.setText(Integer.toString(guesses));
				if (score != 0) {
					score -= 10;
				}
			}

			// set score after action is completed
			textFieldScore.setText(Integer.toString(score));

		}

		// if play again btn is pushed
		else if (ae.getSource() == btnPlayAgain) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "Game reset");
			guessesTextField.setText("0");
			randomNumber = ran.nextInt(100);
			textFieldRandomNumber.setText(Integer.toString(randomNumber));
			textFieldScore.setText("0");
			guesses = 0;
		}
		
		// if guess btn is pushed and input is empty
		else if(ae.getSource() == btnGuess && textFieldGuessTheNumber.getText().isEmpty()) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "Please enter a number");
		}

		// set guess text field to blank
		textFieldGuessTheNumber.setText("");
		
		textFieldGuessTheNumber.requestFocus();

	}
}
