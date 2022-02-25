package numbergame;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import stopwatch.StopWatch;
import stopwatch.StopWatch.StopWatchPanel;

/**
 * A guessing number game in which the user receives a randomly generated number
 * between 1-99 and he/she must guess what the remainder is. Every correct guess
 * equates to 10 points, every incorrect guess equates to -10 points. Score is
 * kept for every session. <br>
 * 
 * @author Brian Perel
 *
 */
public class GuessingGame extends KeyAdapter implements ActionListener {

	protected JFrame frame;
	protected JLabel lblGuess, lblScoringInfo;
	protected JFormattedTextField textFieldGuessTheNumber;
	protected SecureRandom randomGenerator = new SecureRandom();
	protected int totalGuessesMade, totalGameScore, randomNumber;
	protected JTextField textFieldScore, guessesTextField, textFieldRandomNumber;
	protected JButton btnPlayAgain = new JButton("Play again?"), btnGuess = new JButton("Guess");
	protected static final String FAIL_SOUND = "res/audio/fail.wav";
	private JCheckBox closeTimerCheckBox;

	public static void main(String[] args) {
		new GuessingGame();
	}

	/**
	 * Create the application - Build the GUI
	 */
	public GuessingGame() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBackground(Color.WHITE);
		frame.setBounds(100, 100, 526, 352);
		frame.setTitle("Number Guessing Game by: Brian Perel");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		frame.setContentPane(new JLabel(new ImageIcon("res/graphics/bg-image-guess.jpg")));

		JLabel lblScore = new JLabel("Score");
		lblScore.setBounds(21, 24, 34, 17);
		frame.getContentPane().add(lblScore);

		textFieldScore = new JTextField(Integer.toString(totalGameScore));
		textFieldScore.setEditable(false);
		textFieldScore.setBounds(64, 22, 52, 20);
		textFieldScore.setColumns(10);
		textFieldScore.setFocusable(false);
		frame.getContentPane().add(textFieldScore);

		JLabel lblGuesses = new JLabel("Total Guesses");
		lblGuesses.setBounds(144, 24, 84, 17);
		frame.getContentPane().add(lblGuesses);

		guessesTextField = new JTextField("0");
		guessesTextField.setEditable(false);
		guessesTextField.setColumns(10);
		guessesTextField.setBounds(238, 22, 52, 20);
		guessesTextField.setFocusable(false);
		frame.getContentPane().add(guessesTextField);

		JLabel lblImage = new JLabel(new ImageIcon("res/graphics/bg-image-guessing-figure.jpg"));
		lblImage.setBounds(10, 66, 220, 238);
		frame.getContentPane().add(lblImage);

		JLabel lblNumberIs = new JLabel("The number is");
		lblNumberIs.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNumberIs.setBounds(302, 80, 102, 37);
		frame.getContentPane().add(lblNumberIs);

		randomNumber = randomGenerator.nextInt(89) + 10;
		textFieldRandomNumber = new JTextField(Integer.toString(randomNumber));
		textFieldRandomNumber.setEditable(false);
		textFieldRandomNumber.setColumns(10);
		textFieldRandomNumber.setBounds(400, 90, 34, 20);
		textFieldRandomNumber.setFocusable(false);
		frame.getContentPane().add(textFieldRandomNumber);

		lblGuess = new JLabel("Enter a number b/w 1-99 to make 100");
		lblGuess.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblGuess.setBounds(240, 140, 275, 37);
		frame.getContentPane().add(lblGuess);

		btnGuess.setBounds(255, 230, 105, 23);
		btnGuess.addActionListener(this);
		btnGuess.setBackground(Color.green);
		frame.getContentPane().add(btnGuess);
		btnGuess.addKeyListener(this);

		textFieldGuessTheNumber = new JFormattedTextField();
		textFieldGuessTheNumber.setBounds(352, 188, 41, 20);
		frame.getContentPane().add(textFieldGuessTheNumber);
		textFieldGuessTheNumber.setColumns(10);
		textFieldGuessTheNumber.addActionListener(this);
		textFieldGuessTheNumber.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		lblScoringInfo = new JLabel("Successful guess = 10 points");
		lblScoringInfo.setBounds(315, 24, 172, 17);
		frame.getContentPane().add(lblScoringInfo);

		btnPlayAgain.setBounds(382, 230, 105, 23);
		btnPlayAgain.addActionListener(this);
		btnPlayAgain.setBackground(Color.ORANGE);
		frame.getContentPane().add(btnPlayAgain);
		btnPlayAgain.addKeyListener(this);

		closeTimerCheckBox = new JCheckBox("Play without the timer");
		closeTimerCheckBox.setBackground(Color.WHITE);
		closeTimerCheckBox.setBounds(296, 260, 158, 23);
		frame.getContentPane().add(closeTimerCheckBox);
		closeTimerCheckBox.addActionListener(this);
		closeTimerCheckBox.addKeyListener(this);
		
		StopWatch stopWatch = new StopWatch(300, 110); // launch the stopwatch
		// prevents closure of the stopwatch window frame from closing the guessing game
		stopWatch.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); 
		stopWatch.setLocation(390, 340);
		StopWatchPanel.btnStart.setVisible(false);
		StopWatchPanel.btnStop.setVisible(false);
		StopWatchPanel.btnReset.setVisible(false);
		StopWatchPanel.btnStart.doClick();
		
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		Object source = ae.getSource();
		
		boolean outOfTimeFlag = false;
		StopWatchPanel.btnStop.doClick();
		
		// if the timer is greater than 10 seconds when the user guesses 
		if (StopWatchPanel.watch.getText().substring(6, 8).compareTo("10") >= 0 && !source.equals(btnPlayAgain)) {
			outOfTimeFlag = true;
			
			playSound(FAIL_SOUND);

			JOptionPane.showMessageDialog(frame.getComponent(0), "You ran out of time!");

			if (totalGameScore != 0) {
				totalGameScore -= 10;
			}
		}

		performGuiButtonAction(source, outOfTimeFlag);

		// reset the timer
		StopWatchPanel.btnReset.doClick();

		if (closeTimerCheckBox.isSelected()) {
			StopWatchPanel.btnStart.setEnabled(false);
			closeTimerCheckBox.setEnabled(false);
			StopWatchPanel.watch.setEnabled(false);
		}

		// start the timer from 0
		StopWatchPanel.btnStart.doClick();

		// set guess text field to blank
		textFieldGuessTheNumber.setText("");

		// forces focus to jump from button pressed to guessing text field again
		textFieldGuessTheNumber.requestFocus();
	}
	
	/**
	 * Performs appropriate actions when a key is pressed 
	 */
	@Override
	public void keyPressed(KeyEvent e) {		
		Object source = e.getSource();

		boolean outOfTimeFlag = false;
		StopWatchPanel.btnStop.doClick();
		
		// if the timer is greater than 10 seconds when the user guesses 
		if (e.getKeyChar() == KeyEvent.VK_ENTER && StopWatchPanel.watch.getText().substring(6, 8).compareTo("10") >= 0
				&& !source.equals(btnPlayAgain)) {
			
			outOfTimeFlag = true;
			
			playSound(FAIL_SOUND);

			JOptionPane.showMessageDialog(frame.getComponent(0), "You ran out of time!");

			if (totalGameScore != 0) {
				totalGameScore -= 10;
			}
		}

		performGuiButtonAction(source, outOfTimeFlag);
		
		// reset the timer
		StopWatchPanel.btnReset.doClick();
		
		if (e.getKeyChar() == KeyEvent.VK_ENTER && source.equals(closeTimerCheckBox)) {
			StopWatchPanel.btnStart.setEnabled(false);
			closeTimerCheckBox.setEnabled(false);
			StopWatchPanel.watch.setEnabled(false);
		} else if(closeTimerCheckBox.isEnabled()) {
			// start the timer from 0
			StopWatchPanel.btnStart.doClick();
		}
		
		// set guess text field to blank
		textFieldGuessTheNumber.setText("");

		// forces focus to jump from button pressed to guessing text field again
		textFieldGuessTheNumber.requestFocus();
	}
	
	/**
	 * Performs associated action of the GUI button that is clicked 
	 * @param ae the action event triggered
	 * @param outOfTimeFlag boolean keeping track of whether or not timer has hit 10 seconds
	 */
	public void performGuiButtonAction(Object source, boolean outOfTimeFlag) {
		
		// if guess btn is pushed and input is numeric data
		if (source == btnGuess && textFieldGuessTheNumber.getText().matches("-?[1-9]\\d*|0")) {
			evaluateGuess(outOfTimeFlag);
		}
		
		// if play again btn is pushed
		else if (source == btnPlayAgain) {
			
			playSound("res/audio/chimes.wav");

			JOptionPane.showMessageDialog(frame.getComponent(0), "Game reset");
			guessesTextField.setText("0");
			randomNumber = randomGenerator.nextInt(100);
			textFieldRandomNumber.setText(Integer.toString(randomNumber));
			textFieldScore.setText("0");
			totalGameScore = totalGuessesMade = 0;
		}
		
		// if guess btn is pushed and input is empty
		else if (source == btnGuess && textFieldGuessTheNumber.getText().isEmpty()) {
			
			playSound(FAIL_SOUND);

			JOptionPane.showMessageDialog(frame.getComponent(0), "Please enter a number");
		}
	}
	
	/**
	 * Evaluates the user's guess value
	 */
	public void evaluateGuess(boolean outOfTimeFlag) {
		
		totalGuessesMade++;
		
		int textFieldGuessTheNumberInt = Integer.parseInt(textFieldGuessTheNumber.getText());

		// if input remainder entered is outside of range 1-99
		if (textFieldGuessTheNumberInt >= 100 || textFieldGuessTheNumberInt <= 0) {
			
			playSound(FAIL_SOUND);

			textFieldGuessTheNumber.setBorder(BorderFactory.createLineBorder(Color.red, 2));
			JOptionPane.showMessageDialog(frame.getComponent(0), "Please enter a valid number (1-99)");

		} else if (textFieldGuessTheNumberInt + randomNumber == 100) {
			
			playSound("res/audio/win.wav");

			textFieldGuessTheNumber.setBorder(BorderFactory.createLineBorder(new Color(50, 205, 50), 2));
			JOptionPane.showMessageDialog(frame.getComponent(0), "Correct! You made 100");
			randomNumber = randomGenerator.nextInt(100);
			textFieldRandomNumber.setText(Integer.toString(randomNumber));
			
			if (!outOfTimeFlag) {
				totalGameScore += 10;
			}

		} else if (textFieldGuessTheNumberInt + randomNumber != 100) {
			
			playSound(FAIL_SOUND);

			textFieldGuessTheNumber.setBorder(BorderFactory.createLineBorder(Color.red, 2));
			JOptionPane.showMessageDialog(frame.getComponent(0), "Incorrect! That doesn't sum to 100");
			
			if (totalGameScore != 0) {
				totalGameScore -= 10;
			}
		}
		
		textFieldGuessTheNumber.setBorder(BorderFactory.createLineBorder(Color.black, 1));

		// set score after action is completed
		textFieldScore.setText(Integer.toString(totalGameScore));
		guessesTextField.setText(Integer.toString(totalGuessesMade));
	}
	
	public void playSound(String fileToPlay) {
		try {
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(fileToPlay));
			Clip clip = AudioSystem.getClip();
			clip.open(audioStream);
			clip.start();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
			e1.printStackTrace();
		}
	}
}
