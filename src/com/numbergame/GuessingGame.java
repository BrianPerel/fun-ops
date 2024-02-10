package com.numbergame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
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
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import com.stopwatch.StopWatch;
import com.stopwatch.StopWatch.StopWatchPanel;

/**
 * A guessing number game where the player receives a randomly generated secret
 * number between 1 and 99 and must guess the remainder to make 100.
 *
 * Every correct guess gives the player 10 points, every incorrect guess takes away 10 points.
 * The player's score is kept for every session of the game. The game also features a timer that
 * is enabled by default, but can be disabled by checking a box.<br>
 *
 * This program uses an optional timer app for timing functionality
 *
 * @author Brian Perel
 *
 */
public class GuessingGame extends KeyAdapter implements ActionListener {

	protected static int maxCharsLimit = 2;
	protected static boolean turnOn = true;
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
	private int previousRandomNumber;
	private int previousGuess;

	public static void main(String[] args) {
		new GuessingGame();
	}

	/**
	 * Creates the application - Builds the GUI
	 */
	public GuessingGame() {
		createGui();

		if (turnOn) {  // workaround to avoid GUI launch issue
			window.setVisible(true); // this needs to stay here, before the setTimeCounterImpl() method call because otherwise
									 // the counter program GUI appears above this GUI in the computer's taskbar
			setTimeCounterImpl();
			textFieldGuessTheNumber.requestFocus();
		}
	}

	private void createGui() {
		window = new JFrame("Number Guessing Game by: Brian Perel");
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.getContentPane().setLayout(null);
		window.setBackground(Color.WHITE);
		window.setLocationRelativeTo(null);
		window.setResizable(false);
		window.setSize(526, 352);

	    // changes the program's taskbar icon
	    window.setIconImage(new ImageIcon("res/graphics/taskbar_icons/guessing-game.png").getImage());

	    // set background image for window pane
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

		previousRandomNumber = randomNumber = randomGenerator.nextInt(1, 99);
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
		textFieldGuessTheNumber.setCaretColor(Color.BLUE); // made the cursor color blue

		// Use document filter to limit user entry box component input size. Using a custom DocumentFilter to filter all invalid data input
		// ensure that the first digit entered is between 1 and 9, and subsequent digits can be in the range 0-9
		((AbstractDocument) textFieldGuessTheNumber.getDocument()).setDocumentFilter(new DocumentFilter() {
		    @Override
		    public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
		            throws BadLocationException {
		        String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
		        String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);

		        if (((newText.length() <= maxCharsLimit) && newText.matches("^[1-9]\\d*$")) || text.isEmpty()) {
		            super.replace(fb, offset, length, text, attrs);
		        }
		    }
		});

		window.getContentPane().add(textFieldGuessTheNumber);
		textFieldGuessTheNumber.setColumns(10);
		textFieldGuessTheNumber.addActionListener(this);
		textFieldGuessTheNumber.setBorder(
				BorderFactory.createCompoundBorder(new RoundedCornerLineBorder(), BorderFactory.createEmptyBorder(0, 5, 0, 0)));

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

		// prevents the default blue button that appears when a button is clicked (before the button is released) from displaying
		btnGuess.setUI(new BasicButtonUI());
		btnPlayAgain.setUI(new BasicButtonUI());

		closeTimerCheckBox.setBackground(Color.WHITE);
		closeTimerCheckBox.setBounds(296, 260, 158, 23);
		window.getContentPane().add(closeTimerCheckBox);
		closeTimerCheckBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		closeTimerCheckBox.addActionListener(this);
		closeTimerCheckBox.addKeyListener(this);
		closeTimerCheckBox.setOpaque(false);
	}

	/**
	 * Sets a custom hover color for a specific button and then reverts to the default color when the mouse exits
	 *
	 * @param argBtn The button for which you want a custom hover color to appear
	 * @param argBtnHoverColor the color for when hovering
	 * @param argBtnNoHoverColor the color for when not hovering
	 */
	private void setBtnHoverColor(JButton argBtn, Color argBtnHoverColor, Color argBtnNoHoverColor) {
		argBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				argBtn.setBackground(argBtnHoverColor);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				argBtn.setBackground(argBtnNoHoverColor);
			}
		});
	}

	/**
	 * Sets up the stop watch feature implementation for the guessing game.
	 * The stopwatch is launched with the specified width and height, and an option
	 * to enable a custom red font to appear after 10 seconds.
	 *
	 * The method also configures the behavior of the stopwatch window frame to prevent
	 * closure from closing the guessing game window. Additionally, it ensures that if the main
	 * window frame is minimized, the stopwatch window frame is also minimized and vice versa.
	 */
	protected void setTimeCounterImpl() {
		timeCounter = new StopWatch(300, 110, true); // launch the stop watch, pass in parameters: width, height, and if you want to enable custom red font to appear in stop watch post 10 seconds

		// prevents closure of the stopwatch window frame from closing the guessing game
		timeCounter.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		timeCounter.setLocation(window.getX() + window.getWidth(), window.getY());

		// if we minimize the main window frame then minimize the counter window frame too
		window.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowIconified(WindowEvent e) {
		    	timeCounter.setExtendedState(Frame.ICONIFIED);
		    }

		    @Override
		    public void windowDeiconified(WindowEvent e) {
		    	timeCounter.setExtendedState(Frame.NORMAL);
		    	timeCounter.setLocation(window.getX() + window.getWidth(), window.getY());
				textFieldGuessTheNumber.requestFocus();
		    }
		});

		// if we minimize the time counter frame then minimize the main window frame too
		timeCounter.addWindowListener(new WindowAdapter() {
			@Override
		    public void windowIconified(WindowEvent e) {
				window.setExtendedState(Frame.ICONIFIED);
		    }

		    @Override
		    public void windowDeiconified(WindowEvent e) {
		    	window.setExtendedState(Frame.NORMAL);
		    }
		});

		// hide the stop watch buttons, as we won't be using them here
		StopWatchPanel.BTN_START.setVisible(false);
		StopWatchPanel.BTN_STOP.setVisible(false);
		StopWatchPanel.BTN_RESET.setVisible(false);

		// timer auto starts as soon as the game board appears
		StopWatchPanel.BTN_START.doClick();
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		// guard against tab key press, prevents a game bug where counter was resetting when user would press the tab button
		if (ke.getKeyChar() != KeyEvent.VK_TAB) {
			eventHandler(ke.getSource(), ke.getKeyChar());
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		eventHandler(ae.getSource(), (char) KeyEvent.VK_ENTER);
	}

	/**
	 * An event handler for both the action and key event types
	 *
	 * @param source the object on which the event initially occurred
	 * @param keyChar the character associated with the key in this event
	 */
	private void eventHandler(Object source, char keyChar) {

		// if enter key was accidentally pressed and source doesn't match any of the existing buttons in the GUI frame then exit method
		if (keyChar == KeyEvent.VK_ENTER && !(source.equals(btnGuess) || source.equals(btnPlayAgain)
				|| source.equals(closeTimerCheckBox))) {
			return;
		}

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

		turnOffTimer(source, keyChar);

		// prevent space key from being used as it was fading out a button and making 2 windows appear
		if (keyChar != KeyEvent.VK_SPACE) {
			performGuiButtonAction(source, isTimeout);
		}

		resetTimer();

		// only execute this code if the timer GUI is minimized as if it's in an extended state (value of 1) then there's no need to bring it up again
		if (closeTimerCheckBox.isSelected() || !closeTimerCheckBox.isSelected()) {
			timeCounter.setVisible(true);

			// make the second GUI (timer) always appear to the right of the first GUI
			timeCounter.setExtendedState(Frame.NORMAL);
		}

		// start the timer from 0
		if (!closeTimerCheckBox.isSelected()) {
			StopWatchPanel.BTN_START.doClick();
		}

		textFieldGuessTheNumber.setText("");
		textFieldGuessTheNumber.setBorder(
				BorderFactory.createCompoundBorder(new RoundedCornerLineBorder(), BorderFactory.createEmptyBorder(0, 5, 0, 0)));

		// forces focus to jump from button pressed to guessing text field again
		textFieldGuessTheNumber.requestFocus();

		// fixes the problem where after the counter is disabled the focus is still on the counter
		window.toFront();
	}

	/**
	 * Turns off the timer if the specified key character is pressed and the event source is the closeTimerCheckBox.
	 *
	 * @param source The object that triggered the event
	 * @param keyChar The character of the key pressed
	 */
	private void turnOffTimer(Object source, char keyChar) {
		if (keyChar == KeyEvent.VK_ENTER && source.equals(closeTimerCheckBox)) {
			StopWatchPanel.BTN_STOP.doClick();

			StopWatchPanel.WATCH.setEnabled(StopWatchPanel.WATCH.isEnabled());
			closeTimerCheckBox.setSelected(StopWatchPanel.WATCH.isEnabled());

			if (StopWatchPanel.WATCH.isEnabled()) {
				StopWatchPanel.BTN_START.doClick();
			}
		}
	}

	private void resetTimer() {
	    StopWatchPanel.BTN_RESET.doClick();
	    StopWatchPanel.BTN_START.setEnabled(!closeTimerCheckBox.isSelected());
	    StopWatchPanel.WATCH.setEnabled(!closeTimerCheckBox.isSelected());

	    timeCounter.setLocation(window.getX() + window.getWidth(), window.getY());
	}

	/**
	 * Performs associated action of the GUI button that is clicked
	 *
	 * @param source the action event triggered
	 * @param isTimeout boolean keeping track of whether the timer has hit 10
	 *                    seconds
	 */
	protected void performGuiButtonAction(Object source, boolean isTimeout) {
		// if guess btn is pushed and input is numeric data
		if (source.equals(btnGuess)) {
			if (textFieldGuessTheNumber.getText().matches("-?\\d+")) {
				evaluateGuess(isTimeout, 100);

				// player's score should not exceed 100000, if it does then displaying of score will cause offset in GUI layout
				if(gameScore >= 100000 || totalGuessesMade >= 100000) {
					JOptionPane.showMessageDialog(window, "You've beat the game! You've won 100,000 times or more", "Game Completed",
							JOptionPane.INFORMATION_MESSAGE);

					System.exit(0);
				}
			}
			// if guess btn is pushed and input is empty
			else if (textFieldGuessTheNumber.getText().isEmpty() || !textFieldGuessTheNumber.getText().matches("-?[1-9]\\d*|0")) {
				playSound(FAIL_SOUND);
				textFieldGuessTheNumber.setBorder(
						BorderFactory.createCompoundBorder(new RoundedCornerLineBorder(Color.RED), BorderFactory.createEmptyBorder(0, 5, 0, 0)));
				JOptionPane.showMessageDialog(window.getComponent(0), "Please enter a number");
				textFieldGuessTheNumber.setBorder(
						BorderFactory.createCompoundBorder(new RoundedCornerLineBorder(), BorderFactory.createEmptyBorder(0, 5, 0, 0)));
			}
		}
		// if play again btn is pushed
		else if (source.equals(btnPlayAgain)) {
			playAgain();
		}
	}

	/**
	 * Resets the game by playing a sound, displaying a reset message, generating a new random number,
	 * updating the text fields, and resetting the game scores
	 */
	private void playAgain() {
		playSound("res/audio/chimes.wav");
		JOptionPane.showMessageDialog(window.getComponent(0), "Game reset");
		textFieldGuesses.setText("0");
		randomNumber = randomGenerator.nextInt(1, 99);

		// adds an extra layer of security - prevents the new chosen random number from being the previous number used
		if (randomNumber == previousRandomNumber) {
			randomNumber = randomGenerator.nextInt(1, 99);
		}

		textFieldRandomNumber.setText(Integer.toString(randomNumber));
		textFieldScore.setText("0");
		gameScore = totalGuessesMade = 0;
	}

	/**
	 * Evaluates the user's input guess value
	 *
	 * @param isTimeout is the user out of time
	 * @param TOTAL_SUM game mode's correct guess plus random number's sum (which is either 100 or 1000 depending on game mode)
	 */
	protected void evaluateGuess(boolean isTimeout, final int TOTAL_SUM) {
		int textFieldGuessTheNumberInt = 0;

		try {
			textFieldGuessTheNumberInt = Integer.parseInt(textFieldGuessTheNumber.getText());
		} catch(NumberFormatException nfe) {
			System.out.println("Error: NumberFormatException caught. Number entered is too large");
			nfe.printStackTrace();
		}

		// if input remainder entered is outside of the range 1-99 or 100-999
		if (textFieldGuessTheNumberInt >= TOTAL_SUM || textFieldGuessTheNumberInt <= 0) {
			playSound(FAIL_SOUND);
			textFieldGuessTheNumber.setBorder(
					BorderFactory.createCompoundBorder(new RoundedCornerLineBorder(Color.RED), BorderFactory.createEmptyBorder(0, 5, 0, 0)));
			JOptionPane.showMessageDialog(window.getComponent(0),
					"Please enter a valid number " + ((TOTAL_SUM == 100) ? "(1-99)" : "(100-999)"));
		}
		else if (textFieldGuessTheNumberInt + randomNumber == TOTAL_SUM) {
			gameWonCompleteSession(TOTAL_SUM, isTimeout);
		}
		else if (textFieldGuessTheNumberInt + randomNumber != TOTAL_SUM) {
			playSound(FAIL_SOUND);
			textFieldGuessTheNumber.setBorder(
					BorderFactory.createCompoundBorder(new RoundedCornerLineBorder(Color.RED), BorderFactory.createEmptyBorder(0, 5, 0, 0)));

			JOptionPane.showMessageDialog(window.getComponent(0),
					(previousGuess == textFieldGuessTheNumberInt) ? "Same incorrect guess made as previous guess, try again"
						: "Incorrect. That doesn't sum to " + ((TOTAL_SUM == 100) ? "100" : "1000"));

			if (gameScore != 0) {
				gameScore -= 10;
			}

			previousGuess = textFieldGuessTheNumberInt;
		}

		textFieldGuessTheNumber.setBorder(
				BorderFactory.createCompoundBorder(new RoundedCornerLineBorder(), BorderFactory.createEmptyBorder(0, 5, 0, 0)));

		// sets the score after action is completed
		textFieldScore.setText(Integer.toString(gameScore));
		textFieldGuesses.setText(Integer.toString(++totalGuessesMade));
	}

	/**
	 * Handles the completion of a successful game session, plays a winning sound, updates UI components,
	 * displays a success message, generates a new random number, and adjusts game scores.
	 *
	 * @param MAX_LIMIT The maximum limit for the game (either 100 or 1000 depending on game mode)
	 * @param isTimeout Indicates if the game ended due to timeout
	 */
	private void gameWonCompleteSession(final int MAX_LIMIT, boolean isTimeout) {
		playSound("res/audio/win.wav");
		textFieldGuessTheNumber.setBorder(
				BorderFactory.createCompoundBorder(new RoundedCornerLineBorder(LIGHT_GREEN_COLOR), BorderFactory.createEmptyBorder(0, 5, 0, 0)));
		JOptionPane.showMessageDialog(window.getComponent(0),
				"Correct. You made " + ((MAX_LIMIT == 100) ? "100" : "1000"));
		randomNumber = randomGenerator.nextInt(1, 99);

		// adds an extra layer of security - prevents random number chosen from being the previous number used
		if (randomNumber == previousRandomNumber) {
			randomNumber = randomGenerator.nextInt(1, 99);
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

	/**
	 * Creates a custom decorative rounded-corner border by drawing a rounded rectangle around the component it is applied to.
	 * Default border color applied is black.
	 */
	private class RoundedCornerLineBorder extends AbstractBorder {
	    private static final long serialVersionUID = 1L;
	    private Color borderColor = Color.BLACK;

	    public RoundedCornerLineBorder() {}

	    public RoundedCornerLineBorder(Color argBorderColor) {
	    	borderColor = argBorderColor;
	    }

	    @Override
	    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
	        Graphics2D g2d = (Graphics2D) g.create();
	        int arc = 7; // border corner arc degree
	        g2d.setColor(borderColor);
            g2d.drawRoundRect(x, y, width-1, height-1, arc, arc);
	        g2d.dispose();
	    }
	}
}
