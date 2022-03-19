package hangman;

import static java.awt.Color.WHITE;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.text.MaskFormatter;

// import org.apache.log4j.Logger;

/**
 * App simulating a traditional hangman game with a 4-letter car theme. <br>
 * @author Brian Perel
 */
public class Hangman extends KeyAdapter implements FocusListener {

	private JFrame frame;
	// private static final Logger logger = Logger.getLogger(Hangman.class);
	private JFormattedTextField[] letterTextFields = new JFormattedTextField[4];
	private JTextArea hangmanTextArea;
	private JTextField guessesLeftTextField;
	private JTextField hangmanWordTextField;
	private String secretWord;
	private String maskingAsterisk;
	private int guessesLeft;
	private static final Color LIGHT_GREEN = new Color(34, 139, 34);

	// store contents of random words in arraylist to give ability to extract txt at
	// specific line
	private static final ArrayList<String> WORD_LIST = new ArrayList<>();

	// store hangman drawing in an array, each part is to be displayed is in a separate
	// space of the array
	private static final String[] HANGMAN_DRAWING = new String[9];

	// placeholder for a counter which tells which part of the hangman figure to display in the game from the hangmanDrawing
	private int count;

	// placeholder for defect fix - prevent loosing health when same button is continuously pressed
	private char tmp;

	// textFieldHasFocus: flags to indicate which of the 4 user guessing text fields have the insertion
	// pointer. w-d: flags to indicate this particular letter has been discovered by user and
	// printed
	private boolean w, o, r, d;
	private boolean[] textFieldHasFocus = new boolean[4]; 
	private static SecureRandom randomGenerator = new SecureRandom();
	
	public static void main(String[] args) {
		new Hangman();
	}

	/**
	 * Creates the application. Places all the buttons on the app's board and initializes the contents of the frame, building the gui.
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
		hangmanTextArea.setFont(new Font("Tahoma", Font.PLAIN, 18));
		hangmanTextArea.setBounds(59, 21, 142, 239);
		frame.getContentPane().add(hangmanTextArea);
		hangmanTextArea.setEditable(false);
		hangmanTextArea.setFocusable(false);
		hangmanTextArea.setToolTipText("Your health");
		hangmanTextArea.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
		
		JLabel lblHangmanTheme = new JLabel("4-LETTER CAR BRANDS");
		lblHangmanTheme.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		lblHangmanTheme.setForeground(WHITE);
		lblHangmanTheme.setBounds(270, 44, 183, 29);
		frame.getContentPane().add(lblHangmanTheme);
		
		JLabel lblGuessesLeft = new JLabel("Guesses left:");
		lblGuessesLeft.setFont(new Font("Century Schoolbook", Font.PLAIN, 16));
		lblGuessesLeft.setForeground(WHITE);
		lblGuessesLeft.setBounds(280, 44, 183, 100);
		frame.getContentPane().add(lblGuessesLeft);
		
		guessesLeftTextField = new JTextField();
		guessesLeftTextField.setEditable(false);
		guessesLeftTextField.setFont(new Font("MV Boli", Font.BOLD, 15));
		guessesLeftTextField.setBounds(375, 85, 40, 20);
		frame.getContentPane().add(guessesLeftTextField);
		guessesLeftTextField.setFocusable(false);
		
		JLabel lblWordText = new JLabel("WORD:");
		lblWordText.setFont(new Font("Century Schoolbook", Font.PLAIN, 14));
		lblWordText.setForeground(WHITE);
		lblWordText.setBounds(310, 102, 126, 72);
		frame.getContentPane().add(lblWordText);
		
		hangmanWordTextField = new JTextField("****");
		hangmanWordTextField.setEditable(false);
		hangmanWordTextField.setFont(new Font("MV Boli", Font.BOLD, 15));
		hangmanWordTextField.setBounds(368, 125, 50, 27);
		frame.getContentPane().add(hangmanWordTextField);
		hangmanWordTextField.setFocusable(false);
		hangmanWordTextField.setToolTipText("Secret Word");
		
		JSeparator separator = new JSeparator();
		separator.setBounds(272, 168, 171, 7);
		frame.getContentPane().add(separator);
		
		for (int x = 0; x < letterTextFields.length; x++) {
			letterTextFields[x] = new JFormattedTextField(createFormatter("U"));
			letterTextFields[x].setFont(new Font("Papyrus", Font.ITALIC, 11));
			frame.getContentPane().add(letterTextFields[x]);
			letterTextFields[x].addKeyListener(this);
			letterTextFields[x].addFocusListener(this);
		}

		letterTextFields[0].setBounds(306, 186, 17, 20);
		letterTextFields[1].setBounds(333, 186, 17, 20);
		letterTextFields[2].setBounds(360, 186, 17, 20);
		letterTextFields[3].setBounds(387, 186, 17, 20);
		
		buildHangmanDrawing();		
		getRandomWords();
		secretWord = getHangmanWord();
		
		// reveal the hangman word to the console 
		System.out.println(secretWord);
		
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}
	
	/**
	 * Loads file and obtains a list of hangman words
	 */
	public static void getRandomWords() {
		try {
			// read file of random hangman words
			Scanner myReader = new Scanner(new File("Hangman.txt"));

			while (myReader.hasNext()) {
				// store every line in arraylist
				// to attach a line number to each element
				String word = myReader.next();
				
				// in case player loads a file with words that include lower case letters
				for(int x = 0; x < word.length(); x++) {
					// if a letter in the word is found to be lower case, make the whole word upper case 
					if(Character.isLowerCase(word.charAt(x))) {
						word = word.toUpperCase();
						break;
					}
				}
				
				WORD_LIST.add(word);				
			}			

			myReader.close();

		} catch (FileNotFoundException e) {
			// logger.error("Error: File not found. " + e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * Puts the hangman drawing's pieces into a list, to be displayed for every incorrect guess
	 */
	public static void buildHangmanDrawing() {
		// add drawing components to list
		HANGMAN_DRAWING[0] = "  ___________";
		HANGMAN_DRAWING[1] = "\n |         |";
		HANGMAN_DRAWING[2] = "\n |         O";
		HANGMAN_DRAWING[3] = "\n |         | ";
		HANGMAN_DRAWING[4] = "\n |        /|\\";
		HANGMAN_DRAWING[5] = "\n |       / | \\";
		HANGMAN_DRAWING[6] = "\n |         |";
		HANGMAN_DRAWING[7] = "\n |        / \\";
		HANGMAN_DRAWING[8] = "\n |       /   \\ \n |";		
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
			// logger.error("formatter is bad: " + exc.getMessage());
			exc.printStackTrace();
			System.exit(-1);
			return null;
		}
	}

	/**
	 * Read and randomly select a word from a loaded file with a list of hangman words
	 */
	public String getHangmanWord() {
		guessesLeft = HANGMAN_DRAWING.length; // initializes the tracker for the number of guesses you have at start
		guessesLeftTextField.setText(String.valueOf(guessesLeft)); 
		
		maskingAsterisk = new String(new char[letterTextFields.length]).replace("\0", "*");
		
		// choose random word from txt file, update the .nextInt() parameter value (line.size()) as you add words to hangman.txt in the future
		return WORD_LIST.get(randomGenerator.nextInt(WORD_LIST.size()));
	}

	/**
	 * Resets the game, occurs when user wins or looses
	 */
	public void playAgain() {
		hangmanTextArea.setText("");
		hangmanWordTextField.setText("****");
		
		for (JFormattedTextField textField : letterTextFields) {
			// Needed to use setText() twice for each letter to fix extra space being entered
			textField.setText("");
			textField.setText("");
		}
		
		letterTextFields[0].requestFocus();
		w = o = r = d = false;
				
		// enforces the program from choosing the same word as previously chosen from the hangman word list file 
		String previousHangmanWord = secretWord;
		
		secretWord = getHangmanWord();
		
		if (secretWord.equals(previousHangmanWord)) {
			secretWord = getHangmanWord();
		}
		
		// reveal the hangman word to the console 
		System.out.println(secretWord);
		
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

	/**
	 * Performs appropriate actions when a key is pressed 
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		// accept only letters from user
		if (KeyEvent.getKeyText(e.getKeyCode()).matches("[a-zA-Z]")) {
			
			analyzeGuess(Character.toUpperCase(e.getKeyChar()));

			if (w && o && r && d) {
				JOptionPane.showMessageDialog(frame.getComponent(0), "CORRECT! YOU WIN! The secret word is: " + secretWord);
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
			hangmanWordTextField.setText(maskHangmanWord(guess)); // x000
			w = true;
		} else if (textFieldHasFocus[1] && (guess == secretWord.charAt(1)) && !o) {
			hangmanWordTextField.setText(maskHangmanWord(guess)); // 0x00
			o = true;
		} else if (textFieldHasFocus[2] && (guess == secretWord.charAt(2)) && !r) {
			hangmanWordTextField.setText(maskHangmanWord(guess)); // 00x0
			r = true;
		} else if (textFieldHasFocus[3] && (guess == secretWord.charAt(3)) && !d) {
			hangmanWordTextField.setText(maskHangmanWord(guess)); // 000x
			// below line is a code fix: will force set/display final letter if correct before
			// trigger of winner message appears
			letterTextFields[3].setText(letterTextFields[3].getText());
			d = true;
		} else if (letterTextFields[0].getText().length() <= 1 && (guess != secretWord.charAt(0))
				&& (guess != secretWord.charAt(1)) && (guess != secretWord.charAt(2))) {
			
			// prevents player from loosing health if same wrong letter was pressed more than once
			if (guess != tmp) {
				hangmanTextArea.append(HANGMAN_DRAWING[count++]);
				guessesLeftTextField.setText(String.valueOf(--guessesLeft));
			} 

			if (count == HANGMAN_DRAWING.length) {
				JOptionPane.showMessageDialog(frame.getComponent(0), "GAME OVER! The word is: " + secretWord);
				playAgain();
			}

			tmp = guess;
		}
	}

	/**
	 * Indicates which text field is currently holding insertion pointer (at current
	 * moment)
	 */
	@Override
	public void focusGained(FocusEvent e) {
		for (JFormattedTextField textField : letterTextFields) {
			textField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		}
		
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
