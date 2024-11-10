package com.hangman;

import static java.awt.Color.WHITE;
import static java.awt.Font.BOLD;
import static java.awt.Font.PLAIN;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Robot;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Hangman game app with a 4-letter car brand theme. <br>
 *
 * @author Brian Perel
 */
public class Hangman extends KeyAdapter implements FocusListener {

	private static final SecureRandom randomGenerator = new SecureRandom(
			LocalDateTime.now().toString().getBytes(StandardCharsets.US_ASCII));
	private static final Logger LOG = Logger.getLogger(Hangman.class.getName());
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	// store contents of random words in an arraylist to give ability to extract txt at a specific line
	private static final List<String> secretWordList = new ArrayList<>();
	// store hangman drawing in an unmodifiable collection list, each part is to be displayed in a separate
	// space of the array
	private static final List<String> HANGMAN_DRAWING = List.of(
			   					"  ____________",
			   LINE_SEPARATOR + " |  /       |",
			   LINE_SEPARATOR + " | /        O",
			   LINE_SEPARATOR + " |/         | ",
			   LINE_SEPARATOR + " |         /|\\",
			   LINE_SEPARATOR + " |        / | \\",
			   // due to the bold font type set for the textarea for where this drawing is
			   // being referenced, I had to shift the remaining pieces below by an offset of 1 space to the right
			   LINE_SEPARATOR + " |           |",
			   LINE_SEPARATOR + " |          / \\",
			   LINE_SEPARATOR + " |         /   \\",
			   LINE_SEPARATOR + " |____________" );

	private JFormattedTextField[] letterTextFields;
	private JTextArea hangmanTextArea;
	private JTextField textFieldGuessesLeft;
	private JTextField textFieldGameScore;
	private JTextField textFieldHangmanWord;
	private String maskingAsterisk;
	private String secretWord;
	private int guessesLeft;
	private int gameScore;
	private JFrame window;
	private Robot robot;

	// counter that tells which part of the hangman part to display in the game from the hangman drawing
	private int wrongWordCount;

	// prevents player from loosing health when same button is continuously pressed
	private char previousGuess;

	// textFieldHasFocus: flags to indicate which of the 4 user guessing text fields have the insertion
	// pointer. letter[0-3]: flags to indicate this particular letter has been discovered by user and
	// printed
	private final boolean[] letters = new boolean[4];
	private final boolean[] textFieldHasFocus = new boolean[letters.length];

	// custom font for GUI components
	private Font customFont;

	public static void main(String[] args) throws AWTException {
		UIManager.put("ToolTip.background", Color.ORANGE); // sets the tooltip's background color to the given custom color

		Hangman hangman = new Hangman();

		if(hangman.window != null) {
			hangman.window.setVisible(true);
		}
	}

	/**
	 * Creates the application. Places all the buttons on the app's board (initializes the contents of the frame), building the GUI.
	 *
	 * @throws AWTException
	 */
	public Hangman() throws AWTException {
		customizeLogger();

		robot = new Robot();

		boolean isloadSuccessful = obtainSecretWords();

		if (isloadSuccessful) {
			createGui();
			makeSecretWord();
		}
	}

	/**
	 * @wbp.parser.entryPoint annotation that marks the entry point for WindowBuilder's parser.
	 * WindowBuilder will start parsing the GUI components from this method. I added this annotation here as otherwise the design view wouldn't open
	 */
	private void createGui() {
		window = new JFrame("Hangman App by: Brian Perel");
		window.setResizable(false);
		window.setSize(529, 326);
		window.getContentPane().setLayout(null); // centers the window
		window.getContentPane().setBackground(WHITE);
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setContentPane(new JLabel(new ImageIcon("res/graphics/bg-image-hangman.jpg")));

	    // changes the program's taskbar icon
	    window.setIconImage(new ImageIcon("res/graphics/taskbar_icons/hangman.png").getImage());

		hangmanTextArea = new JTextArea();
		hangmanTextArea.setBackground(Color.LIGHT_GRAY);
		hangmanTextArea.setFont(new Font("Tahoma", BOLD, 18));
		hangmanTextArea.setBounds(59, 21, 158, 239);
		window.getContentPane().add(hangmanTextArea);
		hangmanTextArea.setEditable(false);
		hangmanTextArea.setFocusable(false);
		hangmanTextArea.setToolTipText("Your health");
		hangmanTextArea.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
		hangmanTextArea.setForeground(Color.BLUE);

		JLabel lblHangmanTheme = new JLabel(letters.length + "-LETTER CAR BRANDS");
		lblHangmanTheme.setFont(new Font("Segoe UI Semibold", PLAIN, 16));
		lblHangmanTheme.setForeground(WHITE);
		lblHangmanTheme.setBounds(270, 44, 183, 29);
		window.getContentPane().add(lblHangmanTheme);

		JLabel lblGuessesLeft = new JLabel("Guesses left:");
		lblGuessesLeft.setFont(new Font("Century Schoolbook", PLAIN, 16));
		lblGuessesLeft.setForeground(WHITE);
		lblGuessesLeft.setBounds(280, 44, 183, 100);
		window.getContentPane().add(lblGuessesLeft);

		customFont = new Font("MV Boli", BOLD, 15);

		textFieldGuessesLeft = new JTextField();
		lblGuessesLeft.setLabelFor(textFieldGuessesLeft);
		textFieldGuessesLeft.setEditable(false);
		textFieldGuessesLeft.setFont(customFont);
		textFieldGuessesLeft.setBounds(375, 85, 40, 20);
		window.getContentPane().add(textFieldGuessesLeft);
		textFieldGuessesLeft.setFocusable(false);

		JLabel lblWordText = new JLabel("WORD:");
		lblWordText.setFont(new Font("Century Schoolbook", PLAIN, 14));
		lblWordText.setForeground(WHITE);
		lblWordText.setBounds(310, 102, 126, 72);
		window.getContentPane().add(lblWordText);

		textFieldHangmanWord = new JTextField("****");
		lblWordText.setLabelFor(textFieldHangmanWord);
		textFieldHangmanWord.setEditable(false);
		textFieldHangmanWord.setFont(customFont);
		textFieldHangmanWord.setBounds(368, 125, 50, 27);
		window.getContentPane().add(textFieldHangmanWord);
		textFieldHangmanWord.setFocusable(false);
		textFieldHangmanWord.setToolTipText("Secret Word");

		JSeparator separatorLine = new JSeparator();
		separatorLine.setBounds(272, 168, 171, 7);
		window.getContentPane().add(separatorLine);

		// text fields (holders) for letter guesses
		letterTextFields = new JFormattedTextField[letters.length];

		for (int x = 0; x < letterTextFields.length; x++) {
			letterTextFields[x] = new JFormattedTextField();
			// below code will make the user input that appears in the letter text fields be of size 1 and not numbers. Using a custom DocumentFilter to filter all invalid data input
			((AbstractDocument) letterTextFields[x].getDocument()).setDocumentFilter(new DocumentFilter() {
				@Override
				public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
						throws BadLocationException {
					if ((fb.getDocument().getLength() + text.length() - length) <= 1
							&& !" ".equals(text) && text.matches("^[a-zA-Z\\s]*$")) {
						super.replace(fb, offset, length, text, attrs);
					}
				}
			});
			letterTextFields[x].setFont(new Font("Papyrus", Font.BOLD + Font.ITALIC, 12));
			window.getContentPane().add(letterTextFields[x]);
			letterTextFields[x].setSize(17, 20);
			letterTextFields[x].addKeyListener(this);
			letterTextFields[x].addFocusListener(this);
		}

		letterTextFields[0].setLocation(306, 186);
		letterTextFields[1].setLocation(333, 186);
		letterTextFields[2].setLocation(360, 186);
		letterTextFields[3].setLocation(387, 186);

		JLabel lblGameScore = new JLabel("Total Score:");
		lblGameScore.setFont(new Font("Segoe UI Semibold", PLAIN, 16));
		lblGameScore.setForeground(WHITE);
		lblGameScore.setBounds(370, 44, 183, 430);
		window.getContentPane().add(lblGameScore);

		textFieldGameScore = new JTextField("0");
		lblGameScore.setLabelFor(textFieldGameScore);
		textFieldGameScore.setEditable(false);
		textFieldGameScore.setFont(customFont);
		textFieldGameScore.setBounds(465, 250, 40, 20);
		window.getContentPane().add(textFieldGameScore);
		textFieldGameScore.setFocusable(false);
		textFieldGameScore.setBackground(Color.GRAY);

		JSeparator separator = new JSeparator();
		window.getContentPane().add(separator, BorderLayout.SOUTH);
		window.setLocationRelativeTo(null);
	}

	/**
	 * Customizes the used logger by adding a custom color and having the output display the thread name and line number
	 */
	private void customizeLogger() {
		// set the console handler to use the custom formatter
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new Formatter() {
        	@Override
        	 public String format(LogRecord logRecord) {
                 String currentThreadName = "Thread: [" + Thread.currentThread().getName() + "], ";
 				 // ANSI color code for white (\u001B[37m)
                 return "\u001B[37m" + currentThreadName + getLineNumber(logRecord);
             }
        });
        LOG.addHandler(consoleHandler);
	}

	/**
	 * Adds a custom logging component to output the line numbers of log statements
	 *
	 * @param logRecord object holding metadata about the logging event
	 * @return the line number where log statement is declared
	 */
	private static String getLineNumber(LogRecord logRecord) {
		try {
			StackTraceElement[] stackTrace = new Throwable().getStackTrace();

			for (StackTraceElement element : stackTrace) {
				if (element.getClassName().equals(logRecord.getSourceClassName())
						&& element.getMethodName().equals(logRecord.getSourceMethodName())) {

					return "Line: [" + element.getLineNumber() + "] - ";
				}
			}
		}
		catch (Exception e) {
			LOG.severe("An exception occurred while getting the stack trace line number: " + e.getMessage());
		}

		return "Line: [Line unknown] - ";
	}

	private void makeSecretWord() {
		if (secretWordList.isEmpty()) {
			LOG.severe("Error: File of secret hangman words to load is empty or no valid formatted words exist in file");
			JOptionPane.showMessageDialog(window, "File of secret hangman words to load is empty or no valid formatted words exist in file", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		else {
			secretWord = getSecretWord();

			// log the hangman word
			LOG.log(Level.INFO, "The secret word is \"{0}\"", secretWord);
		}
	}

	/**
	 * Loads the secret words file and obtains a list of the hangman words
	 */
	private boolean obtainSecretWords() {

		final File hangmanFile = new File("hangman_words.txt");

		// ensure that the file is not a directory and that we have at least read access
		if (hangmanFile.exists() && hangmanFile.isFile() && hangmanFile.canRead()) {

			try (BufferedReader reader = new BufferedReader(new FileReader(hangmanFile))) {
				String line = "";

				// read file of random hangman words
				while ((line = reader.readLine()) != null) {

					// store every line (secret word) in an arraylist
					// to attach a line number to each element
					String validWord = validateWord(line.trim());

					if (!validWord.isBlank()) {
						secretWordList.add(validWord);
					}
				}

				Collections.shuffle(secretWordList); // shuffle the hangman words list, adding an extra layer of randomness to when selecting a secret word
				return true;

			} catch (FileNotFoundException e1) {
				JOptionPane.showMessageDialog(window, "File of hangman words '" + hangmanFile + "' not found", "Error", JOptionPane.ERROR_MESSAGE);
				LOG.severe("Error: File not found" + e1);
				e1.printStackTrace();
			} catch (IOException e2) {
				JOptionPane.showMessageDialog(window, "Error reading '" + hangmanFile + "' hangman words file", "Error", JOptionPane.ERROR_MESSAGE);
		        LOG.severe("Error reading file" + e2);
				e2.printStackTrace();
			}
		}

		JOptionPane.showMessageDialog(window, "File '" + hangmanFile + "' not found or error reading hangman words file", "Error", JOptionPane.ERROR_MESSAGE);
        LOG.severe("Error reading hangman words file");

		return false;
	}

	/**
	 * Validates every word being loaded from our file of hangman words
	 *
	 * @param word a secret hangman word
	 * @return empty string indicating to not use a secret work or the validated secret word w/o formatting
	 */
	private String validateWord(String word) {
		// if duplicate word is found in data file, if word accessed is not length of 4, if word is not all chars return empty string to avoid adding that String
		// also allow txt file to contain comments by ignoring anything that starts with '#'
		if (word.startsWith("#") || !word.matches("^[a-zA-Z]{4}$") || secretWordList.contains(word.toUpperCase())) {
			return "";
		}

		// in case player loads a file with words that include lower case letters
		for(char c : word.toCharArray()) {
			// if a letter in the word is found to be lower case, make the whole word upper case
			if (Character.isLowerCase(c)) {
				LOG.warning("Word loaded from the file had lowercase letters. Loading in uppercase format");
				return word.toUpperCase();
			}
		}

		return word;
	}

	/**
	 * Reads and randomly selects a word from the loaded file with a list of hangman words
	 */
	private String getSecretWord() {
		guessesLeft = HANGMAN_DRAWING.size(); // initializes the tracker for the number of guesses you have at start
		textFieldGuessesLeft.setText(String.valueOf(guessesLeft));

		maskingAsterisk = new String(new char[letterTextFields.length]).replace("\0", "*");

		// choose random word from txt file, update the .nextInt() parameter value (line.size()) as you add words to hangman_words.txt in the future
		return secretWordList.get(randomGenerator.nextInt(secretWordList.size()));
	}

	/**
	 * Resets the game, occurs when the user wins or looses
	 */
	private void playAgain() {
		hangmanTextArea.setText("");
		textFieldHangmanWord.setText("****");

		for(JFormattedTextField letter : letterTextFields) {
			if(letter.isEnabled()) {
				continue;
			}

			letter.setEnabled(true);
			letter.setBackground(Color.WHITE);
		}

		for (JFormattedTextField textField : letterTextFields) {
			textField.setText("");
		}

		letterTextFields[0].requestFocus();

		Arrays.fill(letters, false);

		avoidDuplicateWord();

		textFieldGuessesLeft.setBorder(new LineBorder(Color.BLACK, 0, true));

		// log the hangman word
		LOG.log(Level.INFO, "The secret word is \"{0}\"", secretWord);

		wrongWordCount = 0;
	}

	private void avoidDuplicateWord() {
		// enforces the program from choosing the same word as previously chosen from the hangman word list file
		final String PREVIOUS_SECRET_WORD = secretWord;

		// use counter var to tell if loop is being used again, if it is then that means we had to choose another word
		int counter = 0;

		do {
			if (++counter > 1) {
				LOG.info("Chosen word was same as previously chosen word. Choosing a different word");
			}

			secretWord = getSecretWord();

		} while((secretWordList.size() > 1) && PREVIOUS_SECRET_WORD.equals(secretWord));
	}

	/**
	 * Performs actions to display the correct placements of '*' in the hangman words box, while user is guessing
	 *
	 * @return returns letters guessed to reveal along with the masking asterisks
	 */
	private String maskHangmanWord(char argUserGuess) {
		StringBuilder maskedWord = new StringBuilder(secretWord.length());

		// iterate through every letter of the secret word, append to maskedWord as needed depending on the conditions that we're met
		for (char currentChar : secretWord.toCharArray()) {

			// disables the letter text field once the correct letter has been guessed in that position, and makes the disabled button visible
			if(currentChar == argUserGuess) {
				for(int x = 0; x < textFieldHasFocus.length; x++) {
					if(textFieldHasFocus[x]) {
						letterTextFields[x].setEnabled(false);
						letterTextFields[x].setBackground(Color.DARK_GRAY);
						break;
					}
				}
			}

		    maskedWord.append(currentChar == argUserGuess || (maskingAsterisk.charAt(maskedWord.length()) != '*') ? currentChar : '*');
		}

		maskingAsterisk = maskedWord.toString();
		return maskingAsterisk;
	}

	@Override
	public void keyPressed(KeyEvent ke) {
	    final int LOW_GUESSES_LEFT_THRESHOLD = 4;

	    if (Integer.parseInt(textFieldGuessesLeft.getText()) == LOW_GUESSES_LEFT_THRESHOLD) {
	        textFieldGuessesLeft.setBorder(new LineBorder(Color.RED, 2, true));
	    }

	    // accept only letters from user
	    if (KeyEvent.getKeyText(ke.getKeyCode()).matches("[a-zA-Z]")) {
	        char upperCaseChar = Character.toUpperCase(ke.getKeyChar());
	        analyzeGuess(upperCaseChar);

	        // set the text of the JFormattedTextField to the uppercase version of the letter
	        JFormattedTextField textField = (JFormattedTextField) ke.getComponent();
	        textField.setText(Character.toString(upperCaseChar));

	        // if all letters are found - word is guessed
	        if (letters[0] && letters[1] && letters[2] && letters[3]) {
	            handleWinCondition();
	        }
	    }
	}

	private void handleWinCondition() {
		// applies strikethrough text decoration to secret word as it's displayed when user wins
		textFieldHangmanWord.setFont(customFont.deriveFont(Map.of(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON)));

		JOptionPane.showMessageDialog(window.getComponent(0), "Correct, you win. The secret word is: " + secretWord, "Winner", JOptionPane.PLAIN_MESSAGE);

		// turns off strikethrough when next game begins
		textFieldHangmanWord.setFont(customFont);

		// stop counting the score at 100
		if (!"100".equals(textFieldGameScore.getText())) {
		    textFieldGameScore.setText(Integer.toString(++gameScore));
		}

		// player's score should not exceed 999, if it does then displaying of score will cause offset in GUI layout
		if(gameScore >= 999) {
			JOptionPane.showMessageDialog(window, "You win the game!", "Game Over",
					JOptionPane.INFORMATION_MESSAGE);

			System.exit(0);
		}

		playAgain();
	}

	/**
	 * Examines the letter the user placed in a text field
	 *
	 * @param letterGuess users guess
	 */
	private void analyzeGuess(char letterGuess) {
		// text field 1 chosen + letter entered matches first char of
		// hangman word + the letter that hasn't been guessed yet
		for (int secretWordLetterPosition = 0; secretWordLetterPosition <= 3; secretWordLetterPosition++) {
			if (isGuessCorrect(letterGuess, secretWordLetterPosition)) {
				return;
			}
		}

		if (!secretWord.contains(String.valueOf(letterGuess))) {
			handleWrongGuess(letterGuess);
		}
	}

	/**
	 * Checks if the guessed letter at a specific position is correct.
	 * Updates the displayed hangman word and marks the correct letter if the guess is accurate.
	 *
	 * @param guess The letter guessed by the player
	 * @param guessingLetterPosition The position/index where the guessed letter is being checked
	 * @return True if the guess is correct, false otherwise
	 */
	private boolean isGuessCorrect(char guess, int guessingLetterPosition) {
		if (textFieldHasFocus[guessingLetterPosition] && !letters[guessingLetterPosition] && (guess == secretWord.charAt(guessingLetterPosition))) {
			// after a correct guess unmask single letter from masked word making: x***, *x**, **x*, or ***x
			textFieldHangmanWord.setText(maskHangmanWord(guess));

			letters[guessingLetterPosition] = true;
			return true;
		}

		return false;
	}

	/**
	 * Handles the consequences of a wrong guess in the Hangman game by preventing decrementing of health
	 * for repeated wrong letters, updates hangman drawing, and checks for game over conditions.
	 *
	 * @param guess The incorrect letter guessed by the player
	 */
	private void handleWrongGuess(char guess) {
		// prevents player from loosing health if the same wrong letter was pressed more than once
		if (guess != previousGuess) {
			hangmanTextArea.append(HANGMAN_DRAWING.get(wrongWordCount++));
			textFieldGuessesLeft.setText(String.valueOf(--guessesLeft));

			// handles game over checking and response
			if (wrongWordCount == HANGMAN_DRAWING.size()) {
				JOptionPane.showMessageDialog(window.getComponent(0), "Session over. The secret word is: " + secretWord);

				if (!("0".equals(textFieldGameScore.getText()))) {
					textFieldGameScore.setText(Integer.toString(--gameScore));
				}

				playAgain();

				// this code was added to fix a bug where when user lost the game, a new game would start with a letter already appearing in the text field
				robot.keyPress(KeyEvent.VK_BACK_SPACE); // Simulate a backspace key press
			    robot.keyRelease(KeyEvent.VK_BACK_SPACE); // Simulate a backspace key release
			}
		}

		previousGuess = guess;
	}

	/**
	 * Highlights the text field that is currently holding the insertion pointer via a border to provide a visual cue
	 *
	 * @param e The FocusEvent indicating the gained focus
	 */
	@Override
	public void focusGained(FocusEvent e) {
		Arrays.stream(letterTextFields).forEach(textField -> textField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)));

		for (int i = 0; i < letterTextFields.length; i++) {
			if (letterTextFields[i].hasFocus()) {
				letterTextFields[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
				textFieldHasFocus[i] = true;
				break;
			}
		}
	}

	/**
	 * Resets visual indicators when a text field loses focus during the game.
	 * Clears the focus flag for all text fields, indicating no insertion pointer.
	 *
	 * @param e The FocusEvent indicating the lost focus
	 */
	@Override
	public void focusLost(FocusEvent e) {
		for (JTextField textField : letterTextFields) {
		    if (!textField.hasFocus()) {
		        Arrays.fill(textFieldHasFocus, false);
		        break;
		    }
		}
	}
}