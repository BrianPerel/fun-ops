package com.numbergame;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDateTime;

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
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import com.stopwatch.StopWatch;
import com.stopwatch.StopWatch.StopWatchPanel;

/**
 * A guessing number game in which the user receives a randomly generated secret
 * number between 1-99 and he/she must guess what the remainder is. Every
 * correct guess gives the player 10 points, every incorrect guess takes away 10
 * points. Score is kept for every session. <br>
 *
 * @author Brian Perel
 *
 */
public class GuessingGame extends KeyAdapter implements ActionListener {

	protected static int maxCharsLimit = 2;
	protected static final String FAIL_SOUND = "res/audio/fail.wav";
	protected static final Color LIGHT_GREEN_COLOR = new Color(50, 205, 50);
	protected static final SecureRandom randomGenerator = new SecureRandom(
				LocalDateTime.now().toString().getBytes(StandardCharsets.US_ASCII));

	protected JFrame window;
	protected JLabel lblGuessInstructions;
	private JCheckBox closeTimerCheckBox = new JCheckBox("Play without the timer");
	protected JFormattedTextField textFieldGuessTheNumber;
	protected int totalGuessesMade;
	protected int gameScore;
	protected int randomNumber;
	protected JTextField textFieldScore;
	protected JTextField textFieldGuesses;
	protected JTextField textFieldRandomNumber;
	protected JButton btnPlayAgain;
	protected JButton btnGuess;
	private StopWatch timeCounter;
	private int previousRandomNumber = 0;

	public static void main(String[] args) {
		new GuessingGame();
	}

	/**
	 * Creates the application - Builds the GUI
	 */
	public GuessingGame() {
		createGui();
	}

	private void createGui() {
		window = new JFrame("Number Guessing Game by: Brian Perel");
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.getContentPane().setLayout(null);
		window.setBackground(Color.WHITE);
		window.setResizable(false);
		window.setSize(526, 352);

		window.setContentPane(new JLabel(new ImageIcon("res/graphics/bg-image-guess.jpg")));

		JLabel lblScore = new JLabel("Score");
		lblScore.setBounds(21, 24, 34, 17);
		window.getContentPane().add(lblScore);

		textFieldScore = new JTextField(Integer.toString(gameScore));
		textFieldScore.setEditable(false);
		textFieldScore.setBounds(64, 22, 52, 20);
		textFieldScore.setColumns(10);
		textFieldScore.setFocusable(false);
		window.getContentPane().add(textFieldScore);

		JLabel lblGuesses = new JLabel("Total Guesses");
		lblGuesses.setBounds(144, 24, 84, 17);
		window.getContentPane().add(lblGuesses);

		textFieldGuesses = new JTextField("0");
		textFieldGuesses.setEditable(false);
		textFieldGuesses.setColumns(10);
		textFieldGuesses.setBounds(238, 22, 52, 20);
		textFieldGuesses.setFocusable(false);
		window.getContentPane().add(textFieldGuesses);

		JLabel lblScoringInfo = new JLabel("Successful guess = 10 points");
		lblScoringInfo.setBounds(315, 24, 172, 17);
		window.getContentPane().add(lblScoringInfo);

		JLabel lblImage = new JLabel(new ImageIcon("res/graphics/bg-image-guessing-figure.jpg"));
		lblImage.setBounds(10, 66, 220, 238);
		window.getContentPane().add(lblImage);

		JLabel lblNumberIs = new JLabel("The number is");
		lblNumberIs.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNumberIs.setBounds(302, 80, 102, 37);
		window.getContentPane().add(lblNumberIs);

		previousRandomNumber = randomNumber = randomGenerator.nextInt(99) + 1;
		textFieldRandomNumber = new JTextField(Integer.toString(randomNumber));
		textFieldRandomNumber.setEditable(false);
		textFieldRandomNumber.setColumns(10);
		textFieldRandomNumber.setBounds(400, 90, 34, 20);
		textFieldRandomNumber.setFocusable(false);
		window.getContentPane().add(textFieldRandomNumber);

		lblGuessInstructions = new JLabel("Enter a number b/w 1-99 to make 100");
		lblGuessInstructions.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblGuessInstructions.setBounds(240, 140, 275, 37);
		window.getContentPane().add(lblGuessInstructions);

		textFieldGuessTheNumber = new JFormattedTextField();
		textFieldGuessTheNumber.setBounds(352, 188, 41, 20);
		// Use document filter to limit user entry box component input size
		((AbstractDocument) textFieldGuessTheNumber.getDocument()).setDocumentFilter(new DocumentFilter() {
			@Override
			public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
					throws BadLocationException {
				if ((fb.getDocument().getLength() + text.length() - length) <= maxCharsLimit && text.matches("\\d+") || text.isEmpty()) {
					super.replace(fb, offset, length, text, attrs);
				}
			}
		});
		window.getContentPane().add(textFieldGuessTheNumber);
		textFieldGuessTheNumber.setColumns(10);
		textFieldGuessTheNumber.addActionListener(this);
		textFieldGuessTheNumber.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		btnGuess = new JButton("Guess");
		btnGuess.setBounds(255, 230, 105, 23);
		btnGuess.addActionListener(this);
		btnGuess.addKeyListener(this);
		btnGuess.setBackground(Color.GREEN);
		btnGuess.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.setBtnHoverColor(btnGuess, new Color(46, 209, 63), Color.GREEN);
		window.getContentPane().add(btnGuess);

		btnPlayAgain = new JButton("Play again?");
		btnPlayAgain.setBounds(382, 230, 105, 23);
		btnPlayAgain.addActionListener(this);
		btnPlayAgain.addKeyListener(this);
		btnPlayAgain.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnPlayAgain.setBackground(Color.ORANGE);
		this.setBtnHoverColor(btnPlayAgain, new Color(201, 180, 0), Color.ORANGE);
		window.getContentPane().add(btnPlayAgain);

		closeTimerCheckBox.setBackground(Color.WHITE);
		closeTimerCheckBox.setBounds(296, 260, 158, 23);
		window.getContentPane().add(closeTimerCheckBox);
		closeTimerCheckBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		closeTimerCheckBox.addActionListener(this);
		closeTimerCheckBox.addKeyListener(this);
		closeTimerCheckBox.setOpaque(false);

		window.setLocationRelativeTo(null);

		StopWatchPanel.isRedFontEnabled = true;

		setTimeCounterImpl();

		// if we minimize the game's winner window frame then minimize the other main game frame too
		window.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowIconified(WindowEvent e) {
		    	timeCounter.setExtendedState(Frame.ICONIFIED);
		    }

		    @Override
		    public void windowDeiconified(WindowEvent e) {
		    	timeCounter.setExtendedState(Frame.NORMAL);
		    }
		});

		textFieldGuessTheNumber.requestFocus();

		window.setVisible(true);
	}

	/**
	 * Sets the colors for when hovering over a button
	 *
	 * @param argBtn the button on which to apply the hover effect
	 * @param btnHoverColor the color for when hovering
	 * @param btnNoHoverColor the color for when not hovering
	 */
	private void setBtnHoverColor(JButton argBtn, Color btnHoverColor, Color btnNoHoverColor) {
		argBtn.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				argBtn.setBackground(btnHoverColor);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				argBtn.setBackground(btnNoHoverColor);
			}
		});
	}

	/**
	 * Set up the stop watch implementation for the guessing game
	 */
	private void setTimeCounterImpl() {
		timeCounter = new StopWatch(300, 110); // launch the stop watch
		// prevents closure of the stopwatch window frame from closing the guessing game
		timeCounter.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		timeCounter.setLocation(window.getX() + window.getWidth(), window.getY());

		// hide the stop watch buttons, as we won't be using them here
		StopWatchPanel.BTN_START.setVisible(false);
		StopWatchPanel.BTN_STOP.setVisible(false);
		StopWatchPanel.BTN_RESET.setVisible(false);

		// timer auto starts as soon as game board appears
		StopWatchPanel.BTN_START.doClick();
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		// guard against tab key press, prevents a game bug where counter was resetting when user would press the tab button
		if(ke.getKeyChar() != KeyEvent.VK_TAB) {
			eventHandler(ke.getSource(), ke.getKeyChar());
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		eventHandler(ae.getSource(), (char) KeyEvent.VK_ENTER);
	}

	/**
	 * An event handler for both action and key event types
	 *
	 * @param source the object on which the Event initially occurred
	 * @param keyChar the character associated with the key in this event
	 */
	private void eventHandler(Object source, char keyChar) {
		boolean isTimeout = false;
		StopWatchPanel.BTN_STOP.doClick();

		// if the timer is greater than 10 seconds when the user guesses
		if (keyChar == KeyEvent.VK_ENTER && StopWatchPanel.WATCH.getText().substring(6).compareTo("10") >= 0
				&& !(source.equals(btnPlayAgain) || closeTimerCheckBox.isSelected())) {

			StopWatchPanel.BTN_STOP.doClick();
			isTimeout = true;
			playSound(FAIL_SOUND);
			JOptionPane.showMessageDialog(window.getComponent(0), "Your out of time.");

			if (gameScore != 0) {
				gameScore -= 10;
			}
		}

		// if you want to turn off the timer
		if(keyChar == KeyEvent.VK_ENTER && source.equals(closeTimerCheckBox)) {
			StopWatchPanel.BTN_STOP.doClick();

			StopWatchPanel.WATCH.setEnabled(StopWatchPanel.WATCH.isEnabled());
			closeTimerCheckBox.setSelected(StopWatchPanel.WATCH.isEnabled());

			if(StopWatchPanel.WATCH.isEnabled()) {
				StopWatchPanel.BTN_START.doClick(); // start the timer from 0
			}
		}

		performGuiButtonAction(source, isTimeout);

		// reset the timer
		StopWatchPanel.BTN_RESET.doClick();
		StopWatchPanel.BTN_START.setEnabled(!closeTimerCheckBox.isSelected());
		StopWatchPanel.WATCH.setEnabled(!closeTimerCheckBox.isSelected());

		if(closeTimerCheckBox.isSelected() || !closeTimerCheckBox.isSelected()) {
			timeCounter.setVisible(true);

			// make the second GUI (timer) always appear to the right of the first GUI
			timeCounter.setLocation(window.getX() + window.getWidth(), window.getY());
		}

		// start the timer from 0
		if(!closeTimerCheckBox.isSelected()) {
			StopWatchPanel.BTN_START.doClick();
		}

		textFieldGuessTheNumber.setText("");
		textFieldGuessTheNumber.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		// forces focus to jump from button pressed to guessing text field again
		textFieldGuessTheNumber.requestFocus();

		// fixes problem when after counter is disabled the focus is still on the counter
		window.toFront();
	}

	/**
	 * Performs associated action of the GUI button that is clicked
	 *
	 * @param source the action event triggered
	 * @param isTimeout boolean keeping track of whether or not timer has hit 10
	 *                    seconds
	 */
	protected void performGuiButtonAction(Object source, boolean isTimeout) {
		StopWatchPanel.BTN_RESET.doClick();

		// if guess btn is pushed and input is numeric data
		if(source.equals(btnGuess)) {
			if (textFieldGuessTheNumber.getText().matches("-?[1-9]\\d*|0")) {
				evaluateGuess(isTimeout, 100);
			}
			// if guess btn is pushed and input is empty
			else if (textFieldGuessTheNumber.getText().isEmpty() || !textFieldGuessTheNumber.getText().matches("-?[1-9]\\d*|0")) {
				playSound(FAIL_SOUND);
				textFieldGuessTheNumber.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
				JOptionPane.showMessageDialog(window.getComponent(0), "Please enter a number");
				textFieldGuessTheNumber.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			}
		}
		// if play again btn is pushed
		else if (source.equals(btnPlayAgain)) {
			playAgain();
		}
	}

	private void playAgain() {
		playSound("res/audio/chimes.wav");
		JOptionPane.showMessageDialog(window.getComponent(0), "Game reset");
		textFieldGuesses.setText("0");
		randomNumber = randomGenerator.nextInt(99) + 1;

		// adds an extra layer of security - prevents random number chosen from being the previous number used
		if(randomNumber == previousRandomNumber) {
			randomNumber = randomGenerator.nextInt(99) + 1;
		}

		textFieldRandomNumber.setText(Integer.toString(randomNumber));
		textFieldScore.setText("0");
		gameScore = totalGuessesMade = 0;
	}

	/**
	 * Evaluates the user's guess value
	 *
	 * @param isTimeout is the user out of time
	 * @param TOTAL_SUM game mode's correct guess + random number's sum (which is either 100 or 1000)
	 */
	protected void evaluateGuess(boolean isTimeout, final int TOTAL_SUM) {
		int textFieldGuessTheNumberInt = 0;

		try {
			textFieldGuessTheNumberInt = Integer.parseInt(textFieldGuessTheNumber.getText());
		} catch(NumberFormatException nfe) {
			System.out.println("Error: NumberFormatException caught. Number entered is too large");
			nfe.printStackTrace();
		}

		// if input remainder entered is outside of range 1-99 or 100-999
		if (textFieldGuessTheNumberInt >= TOTAL_SUM || textFieldGuessTheNumberInt <= 0) {
			playSound(FAIL_SOUND);
			textFieldGuessTheNumber.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
			JOptionPane.showMessageDialog(window.getComponent(0),
					"Please enter a valid number " + ((TOTAL_SUM == 100) ? "(1-99)" : "(100-999)"));
		} else if (textFieldGuessTheNumberInt + randomNumber == TOTAL_SUM) {
			gameWonCompleteSession(TOTAL_SUM, isTimeout);
		} else if (textFieldGuessTheNumberInt + randomNumber != TOTAL_SUM) {
			playSound(FAIL_SOUND);
			textFieldGuessTheNumber.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
			JOptionPane.showMessageDialog(window.getComponent(0),
					"Incorrect. That doesn't sum to " + ((TOTAL_SUM == 100) ? "100" : "1000"));

			if (gameScore != 0) {
				gameScore -= 10;
			}
		}

		textFieldGuessTheNumber.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		// set score after action is completed
		textFieldScore.setText(Integer.toString(gameScore));
		textFieldGuesses.setText(Integer.toString(++totalGuessesMade));
	}

	private void gameWonCompleteSession(final int MAX_LIMIT, boolean isTimeout) {
		playSound("res/audio/win.wav");
		textFieldGuessTheNumber.setBorder(BorderFactory.createLineBorder(LIGHT_GREEN_COLOR, 2));
		JOptionPane.showMessageDialog(window.getComponent(0),
				"Correct. You made " + ((MAX_LIMIT == 100) ? "100" : "1000"));
		randomNumber = randomGenerator.nextInt(99) + 1;

		// adds an extra layer of security - prevents random number chosen from being the previous number used
		if(randomNumber == previousRandomNumber) {
			randomNumber = randomGenerator.nextInt(99) + 1;
		}

		textFieldRandomNumber.setText(Integer.toString(randomNumber));

		if (!isTimeout) {
			gameScore += 10;
		}
	}

	/**
	 * Performs actions to play specific audio that is called upon
	 *
	 * @param fileToPlay the audio requested to play
	 */
	private void playSound(String fileToPlay) {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File(fileToPlay)));
			clip.start();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
			e1.printStackTrace();
		}
	}
}
