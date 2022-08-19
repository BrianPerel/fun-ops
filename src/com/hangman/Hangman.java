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
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
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
 * App simulating a traditional hangman game with a 4-letter car theme. <br>
 * @author Brian Perel
 */
public class Hangman extends KeyAdapter implements FocusListener {
	
	private static final Color LIGHT_GREEN = new Color(34, 139, 34);
	private static final SecureRandom randomGenerator = new SecureRandom(
			LocalDateTime.now().toString().getBytes(StandardCharsets.US_ASCII));

	private final Logger logger_ = Logger.getLogger(this.getClass().getName());
	private JFormattedTextField[] letterTextFields;
	private JTextArea hangmanTextArea;
	private JTextField textFieldGuessesLeft;
	private JTextField textFieldGameScore;
	private JTextField textFieldHangmanWord;
	private String maskingAsterisk;
	private JLabel lblGameScore;
	private String secretWord;
	private int guessesLeft;
	private int gameScore;
	private JFrame frame;

	// store contents of random words in arraylist to give ability to extract txt at
	// specific line. Using an arraylist because this will allow us to modify our hangman 
	// word file list on the fly. On the contrary a regular array requires us to specify the size when we declare it,
	// requiring us to change array size if we modify the txt file
	private final List<String> wordList = new ArrayList<>();

	// placeholder for a counter which tells which part of the hangman figure to display in the game from the hangmanDrawing
	private int count;

	// placeholder for defect fix - prevent loosing health when same button is continuously pressed
	private char previousGuess;
	
	// textFieldHasFocus: flags to indicate which of the 4 user guessing text fields have the insertion
	// pointer. w-d: flags to indicate this particular letter has been discovered by user and
	// printed
	private boolean w, o, r, d;
	private boolean[] textFieldHasFocus = new boolean[4]; 
	
	// store hangman drawing in an array, each part is to be displayed is in a separate
	// space of the array
	private final String[] HANGMAN_DRAWING = { "  ____________",
											   "\n |  /      |",
											   "\n | /       O",
											   "\n |/        | ",
											   "\n |        /|\\",
											   "\n |       / | \\",
											   "\n |         |",
											   "\n |        / \\",
											   "\n |       /   \\",
											   "\n |____________" };
	
	public static void main(String[] args) {
		new Hangman();
		UIManager.put("ToolTip.background", Color.ORANGE);
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
		
		JSeparator separator = new JSeparator();
		separator.setBounds(272, 168, 171, 7);
		frame.getContentPane().add(separator);
		
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
		
		lblGameScore = new JLabel("Total Score:");
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
		
		obtainRandomWords();
		
		if(wordList.isEmpty()) {
			logger_.severe("Error: File of secret words to load is empty");
		}
		else {
			secretWord = getSecretWord();
			
			// log the hangman word 
			logger_.log(Level.INFO, "Secret word \"{0}\"", secretWord);
			
			frame.setVisible(true);
			frame.setLocationRelativeTo(null);
		}
	}
	
	/**
	 * Loads file and obtains a list of hangman words
	 */
	public void obtainRandomWords() {		
		try (Scanner myReader = new Scanner(new File("Hangman.txt"))) { 
			// read file of random hangman words
			while (myReader.hasNext()) {
				// store every line (secret word) in an arraylist
				// to attach a line number to each element
				String word = myReader.next();
				
				if(wordList.contains(word.toUpperCase())) {
					continue;
				}

				// in case player loads a file with words that include lower case letters
				for(int x = 0; x < word.length(); x++) {
					// if a letter in the word is found to be lower case, make the whole word upper case 
					if(Character.isLowerCase(word.charAt(x))) {
						logger_.warning("Word loaded from file was lowercase. Loading in uppercase form");
						word = word.toUpperCase();
						break;
					}
				}
				
				wordList.add(word);
			}		
			
			Collections.shuffle(wordList); // shuffle the hangman words list, adding an extra layer of randomness 
			
		} catch (FileNotFoundException e) {
			logger_.severe("Error: File not found. " + e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * MaskFormatter is used to specify/create/enforce a custom format for a text field's input
	 * @param argString is the text field's user input area (the box)
	 * @return the formatted text field
	 */
	protected MaskFormatter createFormatter(String argString) {
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
	public String getSecretWord() {
		guessesLeft = HANGMAN_DRAWING.length; // initializes the tracker for the number of guesses you have at start
		textFieldGuessesLeft.setText(String.valueOf(guessesLeft)); 
		
		maskingAsterisk = new String(new char[letterTextFields.length]).replace("\0", "*");
		
		// choose random word from txt file, update the .nextInt() parameter value (line.size()) as you add words to hangman.txt in the future
		return wordList.get(randomGenerator.nextInt(wordList.size()));
	}

	/**
	 * Resets the game, occurs when user wins or looses
	 */
	public void playAgain() {
		hangmanTextArea.setText("");
		textFieldHangmanWord.setText("****");
		
		for (JFormattedTextField textField : letterTextFields) {
			// Needed to use setText() twice for each letter to fix extra space being entered
			textField.setText("");
			textField.setText("");
		}
		
		letterTextFields[0].requestFocus();
		w = o = r = d = false;
				
		// enforces the program from choosing the same word as previously chosen from the hangman word list file 
		String previousSecretWord = secretWord;
		
		secretWord = getSecretWord();
		
		if (secretWord.equals(previousSecretWord)) {
			logger_.warning("Chosen word was same as previously chosen word. Choosing a different word");
			secretWord = getSecretWord();
		}
		
		textFieldGuessesLeft.setBorder(new LineBorder(Color.BLACK, 0, true));
		
		// log the hangman word
		logger_.info(secretWord);
		count = 0;
	}
	
	/**
	 * Performs actions to display the correct placements of '*' in the hangman words box, while user is guessing
	 * @return returns letters guessed to reveal along with masking '*'
	 */
	public String maskHangmanWord(char argUsersGuess) {
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
	public void keyPressed(KeyEvent e) {
		if(Integer.parseInt(textFieldGuessesLeft.getText()) <= 3) {
			textFieldGuessesLeft.setBorder(new LineBorder(Color.RED, 2, true));
		}
		
		// accept only letters from user
		if (KeyEvent.getKeyText(e.getKeyCode()).matches("[a-zA-Z]")) {
			analyzeGuess(Character.toUpperCase(e.getKeyChar()));

			// if all letters are found
			if (w && o && r && d) {
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
	public void analyzeGuess(char guess) {
		// text field 1 chosen + letter entered matches first char of
		// hangman word + the letter hasn't been guessed yet
		if (textFieldHasFocus[0] && (guess == secretWord.charAt(0)) && !w) {
			textFieldHangmanWord.setText(maskHangmanWord(guess)); // x000
			w = true;
		} 
		else if (textFieldHasFocus[1] && (guess == secretWord.charAt(1)) && !o) {
			textFieldHangmanWord.setText(maskHangmanWord(guess)); // 0x00
			o = true;
		} 
		else if (textFieldHasFocus[2] && (guess == secretWord.charAt(2)) && !r) {
			textFieldHangmanWord.setText(maskHangmanWord(guess)); // 00x0
			r = true;
		} 
		else if (textFieldHasFocus[3] && (guess == secretWord.charAt(3)) && !d) {
			textFieldHangmanWord.setText(maskHangmanWord(guess)); // 000x
			// below line is a code fix: will force set/display final letter if correct before
			// trigger of winner message appears
			letterTextFields[3].setText(letterTextFields[3].getText());
			d = true;
		} 
		else if (letterTextFields[0].getText().length() <= 1 && (guess != secretWord.charAt(0))
				&& (guess != secretWord.charAt(1)) && (guess != secretWord.charAt(2))) {
			handleIncorrectGuess(guess);
		}
	}

	private void handleIncorrectGuess(char guess) {
		// prevents player from loosing health if the same wrong letter was pressed more than once
		if (guess != previousGuess) {
			hangmanTextArea.append(HANGMAN_DRAWING[count++]);
			textFieldGuessesLeft.setText(String.valueOf(--guessesLeft));
			
			// handles game over checking and response
			if (count == HANGMAN_DRAWING.length) {
				JOptionPane.showMessageDialog(frame.getComponent(0), "GAME OVER! The secret word is: " + secretWord);
				
				if(!textFieldGameScore.getText().equals("0")) {
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
