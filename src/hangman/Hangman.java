package hangman;

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
import javax.swing.SwingConstants;
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
	private static String previousHangmanWord;
	private JTextArea hangmanTextField;
	private static JTextField guessesLeftTextField;
	private static JTextField hangmanWordTextField;
	private static String hangmanWord, maskingAsterisk;
	private static int guessesLeft;

	// store contents of random words in arraylist to give ability to extract txt at
	// specific line
	private static ArrayList<String> line = new ArrayList<>();

	// store hangman drawing in arraylist, each part to be displayed is in separate
	// space of arraylist
	private static ArrayList<String> hangmanDrawing = new ArrayList<>();

	// placeholder for a counter which tells which part of the hangman figure to display in the game from the hangmanDrawing
	private int count;

	// placeholder for defect fix - prevent loosing health when same button is continuously pressed
	private char tmp;

	// t1-t4: flags to indicate which of the 4 user guessing text fields have the insertion
	// pointer. w-d: flags to indicate this particular letter has been discovered by user and
	// printed
	private boolean w, o, r, d;
	private static int numberOfHangmanWords; // tracks number of hangman words in file to choose from
	private boolean[] t = new boolean[4]; 
	private static SecureRandom randomGenerator = new SecureRandom();
	
	public static void main(String[] args) {
		new Hangman();
	}

	/**
	 * Create the application and initialize the contents of the frame
	 */
	public Hangman() {
		// add drawing components to list
		hangmanDrawing.add("  ___________");
		hangmanDrawing.add("\n |         |");
		hangmanDrawing.add("\n |         O");
		hangmanDrawing.add("\n |         | ");
		hangmanDrawing.add("\n |        /|\\");
		hangmanDrawing.add("\n |       / | \\");
		hangmanDrawing.add("\n |         |");
		hangmanDrawing.add("\n |        / \\");
		hangmanDrawing.add("\n |       /   \\ \n |");

		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 529, 326);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Hangman App by: Brian Perel");
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		frame.setContentPane(new JLabel(new ImageIcon("res/graphics/bg-image-hangman.jpg")));

		JSeparator separator = new JSeparator();
		separator.setBounds(272, 168, 171, 7);
		frame.getContentPane().add(separator);

		hangmanTextField = new JTextArea();
		hangmanTextField.setBackground(Color.LIGHT_GRAY);
		hangmanTextField.setFont(new Font("Tahoma", Font.PLAIN, 18));
		hangmanTextField.setBounds(59, 21, 142, 239);
		frame.getContentPane().add(hangmanTextField);
		hangmanTextField.setColumns(10);
		hangmanTextField.setEditable(false);
		hangmanTextField.setFocusable(false);
		hangmanTextField.setToolTipText("Your health");
		hangmanTextField.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));

		hangmanWordTextField = new JTextField();
		hangmanWordTextField.setEditable(false);
		hangmanWordTextField.setHorizontalAlignment(SwingConstants.LEFT);
		hangmanWordTextField.setFont(new Font("MV Boli", Font.BOLD, 15));
		hangmanWordTextField.setBounds(368, 125, 50, 27);
		frame.getContentPane().add(hangmanWordTextField);
		hangmanWordTextField.setColumns(10);
		hangmanWordTextField.setFocusable(false);
		hangmanWordTextField.setToolTipText("Secret Word");
		
		for(int x = 0; x < letterTextFields.length; x++) {
			letterTextFields[x] = new JFormattedTextField(createFormatter("U"));
			letterTextFields[x].setFont(new Font("Papyrus", Font.ITALIC, 11));
			frame.getContentPane().add(letterTextFields[x]);
			letterTextFields[x].setColumns(10);
			letterTextFields[x].addKeyListener(this);
			letterTextFields[x].setHorizontalAlignment(SwingConstants.LEFT);
			letterTextFields[x].addFocusListener(this);
		}

		letterTextFields[0].setBounds(306, 186, 17, 20);
		letterTextFields[1].setBounds(333, 186, 17, 20);
		letterTextFields[2].setBounds(360, 186, 17, 20);
		letterTextFields[3].setBounds(387, 186, 17, 20);

		JLabel lblWordText = new JLabel("WORD:");
		lblWordText.setFont(new Font("Century Schoolbook", Font.PLAIN, 14));
		lblWordText.setForeground(Color.WHITE);
		lblWordText.setBounds(310, 102, 126, 72);
		frame.getContentPane().add(lblWordText);

		JLabel lblHangmanTheme = new JLabel("4-LETTER CAR BRANDS");
		lblHangmanTheme.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		lblHangmanTheme.setForeground(Color.WHITE);
		lblHangmanTheme.setBounds(270, 44, 183, 29);
		frame.getContentPane().add(lblHangmanTheme);
		
		JLabel lblGuessesLeft = new JLabel("Guesses left:");
		lblGuessesLeft.setFont(new Font("Century Schoolbook", Font.PLAIN, 16));
		lblGuessesLeft.setForeground(Color.WHITE);
		lblGuessesLeft.setBounds(280, 44, 183, 100);
		frame.getContentPane().add(lblGuessesLeft);
		
		guessesLeftTextField = new JTextField();
		guessesLeftTextField.setEditable(false);
		guessesLeftTextField.setHorizontalAlignment(SwingConstants.LEFT);
		guessesLeftTextField.setFont(new Font("MV Boli", Font.BOLD, 15));
		guessesLeftTextField.setBounds(375, 85, 40, 20);
		frame.getContentPane().add(guessesLeftTextField);
		guessesLeftTextField.setColumns(10);
		guessesLeftTextField.setFocusable(false);

		try {
			// read file of random hangman words
			Scanner myReader = new Scanner(new File("Hangman.txt"));

			while (myReader.hasNext()) {
				// store every line in arraylist
				// to attach a line number to each element
				line.add(myReader.nextLine());
				numberOfHangmanWords++;
			}

			myReader.close();

		} catch (FileNotFoundException e) {
			// logger.error("Error: File not found. " + e.toString());
			e.printStackTrace();
		}
		
		createHangmanWord();
		
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

	/**
	 * MaskFormatter is used to specify/create the format of a text field
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
	 * Read from list file and randomly select a hangman word
	 */
	public static void createHangmanWord() {
		guessesLeft = hangmanDrawing.size();
		guessesLeftTextField.setText(String.valueOf(guessesLeft));
		hangmanWordTextField.setText("****");
		
		// choose random word from txt file, update the .nextInt() parameter value as you add words to hangman.txt
		hangmanWord = line.get(randomGenerator.nextInt(numberOfHangmanWords));
		
		// enforces the program from choosing the same previously chosen word from file 
		if(hangmanWord.equals(previousHangmanWord)) {
			hangmanWord = line.get(randomGenerator.nextInt(numberOfHangmanWords));
		}
		
		maskingAsterisk = new String(new char[hangmanWord.length()]).replace("\0", "*");
		
		// reveal the hangman word to the console 
		System.out.println(hangmanWord);		
	}

	/**
	 * resets the game, occurs when user wins or looses
	 */
	public void resetGame() {
		hangmanTextField.setText("");
		hangmanWordTextField.setText("");
		
		for (JFormattedTextField textField : letterTextFields) {
			// Needed to use setText() twice for each letter to fix extra space being entered
			textField.setText("");
			textField.setText("");
		}
		
		letterTextFields[0].requestFocus();
		w = o = r = d = false;
		previousHangmanWord = hangmanWord;
		createHangmanWord();
		count = 0;
	}
	
	/**
	 * Performs actions to display the correct placements of '*' while user is guessing
	 */
	public static String maskRemainingHangmanWord(char argUsersGuess) {
		StringBuilder newasterisk = new StringBuilder();
		
		for (int i = 0; i < hangmanWord.length(); i++) {
			if (hangmanWord.charAt(i) == argUsersGuess) {
				newasterisk.append(argUsersGuess);
			} 
			else if (maskingAsterisk.charAt(i) != '*') {
				newasterisk.append(hangmanWord.charAt(i));
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
			char keyChar = e.getKeyChar();
			char charGuessed = Character.toUpperCase(keyChar);
			
			// text field 1 chosen + letter entered matches first char of
			// hangman word + the letter hasn't been guessed yet
			if (t[0] && (charGuessed == hangmanWord.charAt(0)) && !w) {
				hangmanWordTextField.setText(maskRemainingHangmanWord(charGuessed)); // x000
				w = true;
			} else if (t[1] && (charGuessed == hangmanWord.charAt(1)) && !o) {
				hangmanWordTextField.setText(maskRemainingHangmanWord(charGuessed)); // 0x00
				o = true;
			} else if (t[2] && (charGuessed == hangmanWord.charAt(2)) && !r) {
				hangmanWordTextField.setText(maskRemainingHangmanWord(charGuessed)); // 00x0
				r = true;
			} else if (t[3] && (charGuessed == hangmanWord.charAt(3)) && !d) {
				hangmanWordTextField.setText(maskRemainingHangmanWord(charGuessed)); // 000x
				// below line is a code fix: will force set/display final letter if correct before
				// trigger of winner message appears
				letterTextFields[3].setText(letterTextFields[3].getText());
				d = true;
			} else if (letterTextFields[0].getText().length() <= 1 && (charGuessed != hangmanWord.charAt(0))
					&& (charGuessed != hangmanWord.charAt(1))
					&& (charGuessed != hangmanWord.charAt(2))) {
				
				// defect fix - prevent character from loosing health if same wrong letter was
				// pressed more than once
				if (keyChar != tmp) {
					hangmanTextField.append(hangmanDrawing.get(count++));
					guessesLeftTextField.setText(String.valueOf(--guessesLeft));
				} 

				if (count == hangmanDrawing.size()) {
					JOptionPane.showMessageDialog(frame.getComponent(0), "GAME OVER! The word is: " + hangmanWord);
					resetGame();
				}

				tmp = keyChar;
			}

			if (w && o && r && d) {
				JOptionPane.showMessageDialog(frame.getComponent(0), "CORRECT! YOU WIN! The word is: " + hangmanWord);
				resetGame();	
			}
		}
	}

	/**
	 * Indicates which text field is currently holding insertion pointer (at current
	 * moment)
	 */
	@Override
	public void focusGained(FocusEvent e) {
		for (JFormattedTextField textField : letterTextFields) {
			textField.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
		}
		
		for (int i = 0; i < letterTextFields.length; i++) {
			if (letterTextFields[i].hasFocus()) {
				letterTextFields[i].setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 2));
				t[i] = true;
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
		for (int x = 0; x < t.length; x++) {
			if (!letterTextFields[x].hasFocus()) {
				t[x] = false;
			}
		}
	}
}
