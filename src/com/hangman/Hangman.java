package com.hangman;

import static java.awt.Color.WHITE;
import static java.awt.Font.BOLD;
import static java.awt.Font.PLAIN;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import java.util.logging.Level;
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
import javax.swing.text.MaskFormatter;

/**
 * Hangman game app with a 4-letter car theme. <br>
 * @author Brian Perel
 */
public class Hangman extends KeyAdapter implements FocusListener {

	private static final Color LIGHT_GREEN = new Color(34, 139, 34);
	private static final SecureRandom randomGenerator = new SecureRandom(
			LocalDateTime.now().toString().getBytes(StandardCharsets.US_ASCII));
	private static final Logger logger_ = Logger.getLogger(Hangman.class.getName());

	private JFormattedTextField[] letterTextFields;
	private JTextArea hangmanTextArea;
	private JTextField textFieldGuessesLeft;
	private JTextField textFieldGameScore;
	private JTextField textFieldHangmanWord;
	private String maskingAsterisk;
	private String secretWord;
	private int guessesLeft;
	private int gameScore;
	private JFrame frame;

	// store contents of random words in arraylist to give ability to extract txt at
	// specific line. Using an arraylist because this will allow us to modify our hangman
	// word file list on the fly. On the contrary a regular array requires us to specify the size when we declare it,
	// requiring us to change array size if we modify the txt file
	private final List<String> wordList = new ArrayList<>();

	// counter that tells which part of the hangman part to display in the game from the hangman drawing
	private int wongWordCount;

	// prevents player from loosing health when same button is continuously pressed
	private char previousGuess;

	// textFieldHasFocus: flags to indicate which of the 4 user guessing text fields have the insertion
	// pointer. letter[0-3]: flags to indicate this particular letter has been discovered by user and
	// printed
	private boolean[] letter = new boolean[4];
	private boolean[] textFieldHasFocus = new boolean[4];

	// store hangman drawing in an unmodifiable collection list, each part is to be displayed is in a separate
	// space of the array
	private final List<String> HANGMAN_DRAWING = Collections.unmodifiableList(List.of(
			   "  ____________",
			   "\n |  /      |",
			   "\n | /       O",
			   "\n |/        | ",
			   "\n |        /|\\",
			   "\n |       / | \\",
			   "\n |         |",
			   "\n |        / \\",
			   "\n |       /   \\",
			   "\n |____________" ));

	public static void main(String[] args) {
		UIManager.put("ToolTip.background", Color.ORANGE); // sets the tooltip's background color to given custom color
		new Hangman();
	}

	/**
	 * Creates the application. Places all the buttons on the app's board (initializes the contents of the frame), building the GUI.
	 */
	public Hangman() {
		frame = new JFrame("Hangman App by: Brian Perel");
		frame.setResizable(false);
		frame.setSize(529, 326);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().setBackground(WHITE);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setContentPane(new JLabel(new ImageIcon("res/graphics/bg-image-hangman.jpg")));

		hangmanTextArea = new JTextArea();
		hangmanTextArea.setBackground(Color.LIGHT_GRAY);
		hangmanTextArea.setFont(new Font("Tahoma", PLAIN, 18));
		hangmanTextArea.setBounds(59, 21, 150, 239);
		frame.getContentPane().add(hangmanTextArea);
		hangmanTextArea.setEditable(false);
		hangmanTextArea.setFocusable(false);
		hangmanTextArea.setToolTipText("Your health");
		hangmanTextArea.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
		hangmanTextArea.setForeground(Color.BLUE);

		JLabel lblHangmanTheme = new JLabel("4-LETTER CAR BRANDS");
		lblHangmanTheme.setFont(new Font("Segoe UI Semibold", PLAIN, 16));
		lblHangmanTheme.setForeground(WHITE);
		lblHangmanTheme.setBounds(270, 44, 183, 29);
		frame.getContentPane().add(lblHangmanTheme);

		JLabel lblGuessesLeft = new JLabel("Guesses left:");
		lblGuessesLeft.setFont(new Font("Century Schoolbook", PLAIN, 16));
		lblGuessesLeft.setForeground(WHITE);
		lblGuessesLeft.setBounds(280, 44, 183, 100);
		frame.getContentPane().add(lblGuessesLeft);

		Font customFont = new Font("MV Boli", BOLD, 15);

		textFieldGuessesLeft = new JTextField();
		textFieldGuessesLeft.setEditable(false);
		textFieldGuessesLeft.setFont(customFont);
		textFieldGuessesLeft.setBounds(375, 85, 40, 20);
		frame.getContentPane().add(textFieldGuessesLeft);
		textFieldGuessesLeft.setFocusable(false);

		JLabel lblWordText = new JLabel("WORD:");
		lblWordText.setFont(new Font("Century Schoolbook", PLAIN, 14));
		lblWordText.setForeground(WHITE);
		lblWordText.setBounds(310, 102, 126, 72);
		frame.getContentPane().add(lblWordText);

		textFieldHangmanWord = new JTextField("****");
		textFieldHangmanWord.setEditable(false);
		textFieldHangmanWord.setFont(customFont);
		textFieldHangmanWord.setBounds(368, 125, 50, 27);
		frame.getContentPane().add(textFieldHangmanWord);
		textFieldHangmanWord.setFocusable(false);
		textFieldHangmanWord.setToolTipText("Secret Word");

		JSeparator separatorLine = new JSeparator();
		separatorLine.setBounds(272, 168, 171, 7);
		frame.getContentPane().add(separatorLine);

		// text fields (holders) for letter guesses
		letterTextFields = new JFormattedTextField[4];

		for (int x = 0; x < letterTextFields.length; x++) {
			letterTextFields[x] = new JFormattedTextField(createFormatter("U"));
			letterTextFields[x].setFont(new Font("Papyrus", Font.ITALIC, 11));
			frame.getContentPane().add(letterTextFields[x]);
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
		frame.getContentPane().add(lblGameScore);

		textFieldGameScore = new JTextField("0");
		textFieldGameScore.setEditable(false);
		textFieldGameScore.setFont(customFont);
		textFieldGameScore.setBounds(465, 250, 40, 20);
		frame.getContentPane().add(textFieldGameScore);
		textFieldGameScore.setFocusable(false);
		textFieldGameScore.setBackground(Color.GRAY);

		boolean loadSuccessful = obtainRandomWords();

		if(loadSuccessful) {
			makeSecretWord();
		}
	}

	private void makeSecretWord() {
		if(wordList.isEmpty()) {
			logger_.severe("Error: File of secret words to load is empty");
		}
		else {
			secretWord = getSecretWord();

			// log the hangman word
			logger_.log(Level.INFO, "The secret word is \"{0}\"", secretWord);

			frame.setVisible(true);
			frame.setLocationRelativeTo(null);
		}
	}

	/**
	 * Loads file and obtains a list of hangman words
	 */
	private boolean obtainRandomWords() {
		try (BufferedReader reader = new BufferedReader(new FileReader(new File("hangman.txt")))) {
			String line;

			// read file of random hangman words
			while ((line = reader.readLine()) != null) {
				// store every line (secret word) in an arraylist
				// to attach a line number to each element
				String word = validateWord(line);

				if(!word.isBlank()) {
					wordList.add(word);
				}
			}

			Collections.shuffle(wordList); // shuffle the hangman words list, adding an extra layer of randomness to when selecting a secret word

			return true;

		} catch (FileNotFoundException e) {
			logger_.severe("Error: File not found");
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return false;
	}

	private String validateWord(String word) {
		// if duplicate word is found in data file, if word accessed is not length of 4, if word is not all chars
		// also allow txt file to contain comments by ignoring anything that starts with '#'
		if(wordList.contains(word.toUpperCase()) || word.length() != 4 || !word.matches("[a-zA-Z]+")
				|| word.startsWith("#")) {
			return "";
		}

		// in case player loads a file with words that include lower case letters
		for(int x = 0; x < word.length(); x++) {
			// if a letter in the word is found to be lower case, make the whole word upper case
			if(Character.isLowerCase(word.charAt(x))) {
				logger_.warning("Word loaded from the file had lowercase letters. Loading in uppercase format");
				return word.toUpperCase();
			}
		}

		return word;
	}

	/**
	 * MaskFormatter is used to specify/create/enforce a custom format for a text field's input
	 * @param argString is the text field's user input area (the box)
	 * @return the formatted text field
	 */
	private MaskFormatter createFormatter(String argString) {
		try {
			return new MaskFormatter(argString);
		} catch (java.text.ParseException exc) {
			logger_.severe("formatter is bad: " + exc.getMessage());
			exc.printStackTrace();
			System.exit(-1);
			return null;
		}
	}

	/**
	 * Read and randomly select a word from a loaded file with a list of hangman words
	 */
	private String getSecretWord() {
		guessesLeft = HANGMAN_DRAWING.size(); // initializes the tracker for the number of guesses you have at start
		textFieldGuessesLeft.setText(String.valueOf(guessesLeft));

		maskingAsterisk = new String(new char[letterTextFields.length]).replace("\0", "*");

		// choose random word from txt file, update the .nextInt() parameter value (line.size()) as you add words to hangman.txt in the future
		return wordList.get(randomGenerator.nextInt(wordList.size()));
	}

	/**
	 * Resets the game, occurs when user wins or looses
	 */
	private void playAgain() {
		hangmanTextArea.setText("");
		textFieldHangmanWord.setText("****");

		for (JFormattedTextField textField : letterTextFields) {
			// Needed to use setText() twice for each letter to fix extra space being entered
			textField.setText("");
			textField.setText("");
		}

		letterTextFields[0].requestFocus();

		for (int i = 0; i < letter.length; i++) {
			letter[i] = false;
		}

		avoidDuplicateWord();

		textFieldGuessesLeft.setBorder(new LineBorder(Color.BLACK, 0, true));

		// log the hangman word
		logger_.info(secretWord);
		wongWordCount = 0;
	}

	private void avoidDuplicateWord() {
		// enforces the program from choosing the same word as previously chosen from the hangman word list file
		String previousSecretWord = secretWord;

		// use counter var to tell if loop is being used again, if it is then that means we had to choose another word
		int counter = 0;

		do {

			if(++counter > 1) {
				logger_.info("Chosen word was same as previously chosen word. Choosing a different word");
			}

			secretWord = getSecretWord();

		} while((wordList.size() > 1) && secretWord.equals(previousSecretWord));
	}

	/**
	 * Performs actions to display the correct placements of '*' in the hangman words box, while user is guessing
	 * @return returns letters guessed to reveal along with masking '*'
	 */
	private String maskHangmanWord(char argUsersGuess) {
		StringBuilder newasterisk = new StringBuilder();

		for (int i = 0; i < secretWord.length(); i++) {
			if (secretWord.charAt(i) == argUsersGuess) {
				newasterisk.append(argUsersGuess);
			}
			else if (maskingAsterisk.charAt(i) != '*') {
				newasterisk.append(secretWord.charAt(i));
			}
			else {
				newasterisk.append("*");
			}
		}

		maskingAsterisk = newasterisk.toString();
		return maskingAsterisk;
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		if(Integer.parseInt(textFieldGuessesLeft.getText()) <= 3) {
			textFieldGuessesLeft.setBorder(new LineBorder(Color.RED, 2, true));
		}

		// accept only letters from user
		if (KeyEvent.getKeyText(ke.getKeyCode()).matches("[a-zA-Z]")) {
			analyzeGuess(Character.toUpperCase(ke.getKeyChar()));

			// if all letters are found
			if (letter[0] && letter[1] && letter[2] && letter[3]) {
				JOptionPane.showMessageDialog(frame.getComponent(0), "CORRECT! YOU WIN! The secret word is: " + secretWord);
				textFieldGameScore.setText(Integer.toString(++gameScore));
				playAgain();
			}
		}
	}

	/**
	 * Examines the letter the user placed in a text field
	 * @param guess users guess
	 */
	private void analyzeGuess(char guess) {
		// text field 1 chosen + letter entered matches first char of
		// hangman word + the letter that hasn't been guessed yet
		for (int i = 0; i <= 3; i++) {
			if(isGuessCorrect(guess, i)) {
				return;
			}
		}

		if (letterTextFields[0].getText().length() <= 1 && (guess != secretWord.charAt(0))
				&& (guess != secretWord.charAt(1)) && (guess != secretWord.charAt(2))) {
			handleWrongGuess(guess);
		}
	}

	private boolean isGuessCorrect(char guess, int x) {
		if (textFieldHasFocus[x] && (guess == secretWord.charAt(x)) && !letter[x]) {
			textFieldHangmanWord.setText(maskHangmanWord(guess)); // x000, 0x00, 00x0, or 000x

			letter[x] = true;
			return true;
		}

		return false;
	}

	private void handleWrongGuess(char guess) {
		// prevents player from loosing health if the same wrong letter was pressed more than once
		if (guess != previousGuess) {
			hangmanTextArea.append(HANGMAN_DRAWING.get(wongWordCount++));
			textFieldGuessesLeft.setText(String.valueOf(--guessesLeft));

			// handles game over checking and response
			if (wongWordCount == HANGMAN_DRAWING.size()) {
				JOptionPane.showMessageDialog(frame.getComponent(0), "GAME OVER! The secret word is: " + secretWord);

				if(!("0".equals(textFieldGameScore.getText()))) {
					textFieldGameScore.setText(Integer.toString(--gameScore));
				}

				playAgain();
			}
		}

		previousGuess = guess;
	}

	/**
	 * Indicates which text field is currently holding insertion pointer (at current
	 * moment)
	 */
	@Override
	public void focusGained(FocusEvent e) {
		Arrays.stream(letterTextFields).forEach(textField -> textField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)));

		for (int i = 0; i < letterTextFields.length; i++) {
			if (letterTextFields[i].hasFocus()) {
				letterTextFields[i].setBorder(BorderFactory.createLineBorder(LIGHT_GREEN, 2));
				textFieldHasFocus[i] = true;
				break;
			}
		}
	}

	/**
	 * Indicates if specific text field doesn't have insertion pointer (at current
	 * moment)
	 */
	@Override
	public void focusLost(FocusEvent e) {
		for (int x = 0; x < textFieldHasFocus.length; x++) {
			if (!letterTextFields[x].hasFocus()) {
				textFieldHasFocus[x] = false;
			}
		}
	}
}
