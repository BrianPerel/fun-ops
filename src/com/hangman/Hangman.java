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
import javax.swing.text.MaskFormatter;

/**
 * Hangman game app with a 4-letter car brand theme. <br>
 * @author Brian Perel
 */
public class Hangman extends KeyAdapter implements FocusListener {

	private static final Color LIGHT_GREEN_COLOR = new Color(34, 139, 34);
	private static final SecureRandom randomGenerator = new SecureRandom(
			LocalDateTime.now().toString().getBytes(StandardCharsets.US_ASCII));
	private static final Logger LOG = Logger.getLogger(Hangman.class.getName());

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

	// store contents of random words in arraylist to give ability to extract txt at
	// specific line. Using an arraylist because this will allow us to modify our hangman
	// word file list on the fly. On the contrary a regular array requires us to specify the size when we declare it,
	// requiring us to change array size if we modify the txt file
	private final List<String> secretWordList = new ArrayList<>();

	// counter that tells which part of the hangman part to display in the game from the hangman drawing
	private int wrongWordCount;

	// prevents player from loosing health when same button is continuously pressed
	private char previousGuess;

	// textFieldHasFocus: flags to indicate which of the 4 user guessing text fields have the insertion
	// pointer. letter[0-3]: flags to indicate this particular letter has been discovered by user and
	// printed
	private final boolean[] letters = new boolean[4];
	private final boolean[] textFieldHasFocus = new boolean[4];

	// custom font for GUI components
	private Font customFont;

	// store hangman drawing in an unmodifiable collection list, each part is to be displayed is in a separate
	// space of the array
	private static final List<String> HANGMAN_DRAWING = List.of(
			   "  ____________",
			   "\n |  /      |",
			   "\n | /       O",
			   "\n |/        | ",
			   "\n |        /|\\",
			   "\n |       / | \\",
			   "\n |         |",
			   "\n |        / \\",
			   "\n |       /   \\",
			   "\n |____________" );

	public static void main(String[] args) {
		// set the console handler to use the custom formatter
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord logRecord) {
            	// ANSI color code for white - \u001B[37m
                return "\u001B[37m";
            }
        });
        LOG.addHandler(consoleHandler);

		UIManager.put("ToolTip.background", Color.ORANGE); // sets the tooltip's background color to given custom color
		new Hangman();
	}

	/**
	 * Creates the application. Places all the buttons on the app's board (initializes the contents of the frame), building the GUI.
	 */
	public Hangman() {
		createGui();
	}

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
		hangmanTextArea.setFont(new Font("Tahoma", PLAIN, 18));
		hangmanTextArea.setBounds(59, 21, 150, 239);
		window.getContentPane().add(hangmanTextArea);
		hangmanTextArea.setEditable(false);
		hangmanTextArea.setFocusable(false);
		hangmanTextArea.setToolTipText("Your health");
		hangmanTextArea.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
		hangmanTextArea.setForeground(Color.BLUE);

		JLabel lblHangmanTheme = new JLabel("4-LETTER CAR BRANDS");
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
		letterTextFields = new JFormattedTextField[4];

		for (int x = 0; x < letterTextFields.length; x++) {
			letterTextFields[x] = new JFormattedTextField(createFormatter("U"));
			letterTextFields[x].setFont(new Font("Papyrus", Font.ITALIC, 11));
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
		textFieldGameScore.setEditable(false);
		textFieldGameScore.setFont(customFont);
		textFieldGameScore.setBounds(465, 250, 40, 20);
		window.getContentPane().add(textFieldGameScore);
		textFieldGameScore.setFocusable(false);
		textFieldGameScore.setBackground(Color.GRAY);

		boolean isloadSuccessful = obtainSecretWords();

		if(isloadSuccessful) {
			makeSecretWord();
		}
	}

	private void makeSecretWord() {
		if(secretWordList.isEmpty()) {
			LOG.severe("Error: File of secret words to load is empty");
			JOptionPane.showMessageDialog(window, "File of secret words to load is empty", "Error", JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			secretWord = getSecretWord();

			// log the hangman word
			LOG.log(Level.INFO, "The secret word is \"{0}\"", secretWord);

			window.setVisible(true);
			window.setLocationRelativeTo(null);
		}
	}

	/**
	 * Loads file and obtains a list of hangman secret words
	 */
	private boolean obtainSecretWords() {

		File hangmanFile = new File("hangman.txt");

		// ensure that the file is not a directory and that we have at least read access
		if(hangmanFile.isFile() && hangmanFile.canRead()) {

			try (BufferedReader reader = new BufferedReader(new FileReader("hangman.txt"))) {
				String line;

				// read file of random hangman words
				while ((line = reader.readLine()) != null) {

					// store every line (secret word) in an arraylist
					// to attach a line number to each element
					String validWord = validateWord(line.trim());

					if(!validWord.isBlank()) {
						secretWordList.add(validWord);
					}
				}

				Collections.shuffle(secretWordList); // shuffle the hangman words list, adding an extra layer of randomness to when selecting a secret word

				return true;

			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(window, "File of hangman words not found", "Error", JOptionPane.INFORMATION_MESSAGE);
				LOG.severe("Error: File not found" + e);
				e.printStackTrace();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(window, "Error reading file", "Error", JOptionPane.INFORMATION_MESSAGE);
		        LOG.severe("Error reading file" + e1);
				e1.printStackTrace();
			}

		}

		JOptionPane.showMessageDialog(window, "Error reading file", "Error", JOptionPane.INFORMATION_MESSAGE);
        LOG.severe("Error reading file");

		return false;
	}

	/**
	 * Validates every word being loaded from our file of words
	 *
	 * @param word a secret hangman word
	 * @return empty string indicating to not use a secret work or the validated secret word w/o formatting
	 */
	private String validateWord(String word) {
		// if duplicate word is found in data file, if word accessed is not length of 4, if word is not all chars return empty string to avoid adding that String
		// also allow txt file to contain comments by ignoring anything that starts with '#'
		if(word.startsWith("#") || secretWordList.contains(word.toUpperCase()) || word.length() != 4 || !word.matches("[a-zA-Z]+")) {
			return "";
		}

		// in case player loads a file with words that include lower case letters
		for(char c : word.toCharArray()) {
			// if a letter in the word is found to be lower case, make the whole word upper case
			if(Character.isLowerCase(c)) {
				LOG.warning("Word loaded from the file had lowercase letters. Loading in uppercase format");
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
			LOG.severe("formatter is bad: " + exc.getMessage());
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
		return secretWordList.get(randomGenerator.nextInt(secretWordList.size()));
	}

	/**
	 * Resets the game, occurs when user wins or looses
	 */
	private void playAgain() {
		hangmanTextArea.setText("");
		textFieldHangmanWord.setText("****");

		for (JFormattedTextField textField : letterTextFields) {
			// Needed to use setText() twice for each letter to fix extra space being entered after you finish 1 game
			textField.setText("");
			textField.setText("");
		}

		letterTextFields[0].requestFocus();

		Arrays.fill(letters, false);

		avoidDuplicateWord();

		textFieldGuessesLeft.setBorder(new LineBorder(Color.BLACK, 0, true));

		// log the hangman word
		LOG.info(secretWord);
		wrongWordCount = 0;
	}

	private void avoidDuplicateWord() {
		// enforces the program from choosing the same word as previously chosen from the hangman word list file
		String previousSecretWord = secretWord;

		// use counter var to tell if loop is being used again, if it is then that means we had to choose another word
		int counter = 0;

		do {
			if(++counter > 1) {
				LOG.info("Chosen word was same as previously chosen word. Choosing a different word");
			}

			secretWord = getSecretWord();

		} while((secretWordList.size() > 1) && secretWord.equals(previousSecretWord));
	}

	/**
	 * Performs actions to display the correct placements of '*' in the hangman words box, while user is guessing
	 * @return returns letters guessed to reveal along with the masking asterisks
	 */
	private String maskHangmanWord(char argUserGuess) {
		StringBuilder maskedWord = new StringBuilder(secretWord.length());

		// iterate through every letter of the secret word, append to maskedWord as needed depending on the conditions that we're met
		for (char currentChar : secretWord.toCharArray()) {
		    maskedWord.append(currentChar == argUserGuess || maskingAsterisk.charAt(maskedWord.length()) != '*' ? currentChar : '*');
		}

		maskingAsterisk = maskedWord.toString();
		return maskingAsterisk;
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		final int MAX_GUESSES = 3;

		if(Integer.parseInt(textFieldGuessesLeft.getText()) <= MAX_GUESSES) {
			textFieldGuessesLeft.setBorder(new LineBorder(Color.RED, 2, true));
		}

		// accept only letters from user
		if (KeyEvent.getKeyText(ke.getKeyCode()).matches("[a-zA-Z]")) {
			analyzeGuess(Character.toUpperCase(ke.getKeyChar()));

			// if all letters are found
			if (letters[0] && letters[1] && letters[2] && letters[3]) {
				// applies strikethrough text decoration to secret work as it's displayed when user wins
				textFieldHangmanWord.setFont(customFont.deriveFont(Map.of(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON)));

				JOptionPane.showMessageDialog(window.getComponent(0), "Correct, you win. The secret word is: " + secretWord, "Winner", JOptionPane.INFORMATION_MESSAGE);

				// turns off strikethrough when next game begins
				textFieldHangmanWord.setFont(customFont);

				// stop counting the score at 100
				if(!"100".equals(textFieldGameScore.getText())) {
					textFieldGameScore.setText(Integer.toString(++gameScore));
				}

				playAgain();
			}
		}
	}

	/**
	 * Examines the letter the user placed in a text field
	 * @param letterGuess users guess
	 */
	private void analyzeGuess(char letterGuess) {
		// text field 1 chosen + letter entered matches first char of
		// hangman word + the letter that hasn't been guessed yet
		for (int i = 0; i <= 3; i++) {
			if(isGuessCorrect(letterGuess, i)) {
				return;
			}
		}

		if (letterTextFields[0].getText().length() == 1 && (letterGuess != secretWord.charAt(0))
				&& (letterGuess != secretWord.charAt(1)) && (letterGuess != secretWord.charAt(2))) {
			handleWrongGuess(letterGuess);
		}
	}

	private boolean isGuessCorrect(char guess, int x) {
		if (textFieldHasFocus[x] && (guess == secretWord.charAt(x)) && !letters[x]) {
			textFieldHangmanWord.setText(maskHangmanWord(guess)); // x000, 0x00, 00x0, or 000x

			letters[x] = true;
			return true;
		}

		return false;
	}

	private void handleWrongGuess(char guess) {
		// prevents player from loosing health if the same wrong letter was pressed more than once
		if (guess != previousGuess) {
			hangmanTextArea.append(HANGMAN_DRAWING.get(wrongWordCount++));
			textFieldGuessesLeft.setText(String.valueOf(--guessesLeft));

			// handles game over checking and response
			if (wrongWordCount == HANGMAN_DRAWING.size()) {
				JOptionPane.showMessageDialog(window.getComponent(0), "Game over. The secret word is: " + secretWord);

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
				letterTextFields[i].setBorder(BorderFactory.createLineBorder(LIGHT_GREEN_COLOR, 2));
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
		for (JTextField textField : letterTextFields) {
		    if (!textField.hasFocus()) {
		        Arrays.fill(textFieldHasFocus, false);
		        break;
		    }
		}
	}
}