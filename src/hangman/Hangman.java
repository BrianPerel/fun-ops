package hangman;

import java.awt.Color;
import java.awt.EventQueue;
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
import javax.swing.border.Border;
import javax.swing.text.MaskFormatter;

import org.apache.log4j.Logger;

/**
 * App simulating a traditional hangman game with a 4-letter car theme. <br>
 * 
 * @author Brian Perel
 */
public class Hangman extends KeyAdapter implements FocusListener {

	private JFrame frame;
	private static final Logger logger = Logger.getLogger(Hangman.class);
	private JFormattedTextField letter1TextField, letter2TextField, letter3TextField, letter4TextField;
	private JTextArea hangmanTextField;
	private JTextField hangmanWordTextField;
	
	// chosen hangman word
	static String hangmanWord = "";
	private static String maskingAsterisk;

	// store contents of random words in arraylist to give ability to extract txt at
	// specific line
	ArrayList<String> line = new ArrayList<>();

	// store hangman drawing in arraylist, each part to be displayed is in separate
	// space of arraylist
	ArrayList<String> hangmanDrawing = new ArrayList<>();

	// placeholder for a counter
	int count = 0;

	// placeholder to store letter entered
	char letterGuessed;

	// placeholder for defect fix - prevent loosing health when same button is
	// continuously pressed
	char tmp = '!';

	// flags to indicate which of the 4 user guessing text fields have the insertion
	// pointer
	boolean t1, t2, t3, t4 = false;

	// flags to indicate this particular letter has been discovered by user and
	// printed
	boolean w, o, r, d = false;
	
	SecureRandom randomGenerator = new SecureRandom();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {

				try {
					Hangman window = new Hangman();
					window.frame.setVisible(true);
					window.frame.setTitle("Hangman App by: Brian Perel");
					window.frame.setResizable(false);
					window.frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					logger.error("Error: " + e.toString());
				}
			}
		});		
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
		frame.setBounds(100, 100, 529, 326);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setContentPane(new JLabel(new ImageIcon("res/graphics/bgImageHangman.jpg")));

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
		Border border = BorderFactory.createLineBorder(Color.BLUE, 2);
		hangmanTextField.setBorder(border);

		hangmanWordTextField = new JTextField();
		hangmanWordTextField.setFont(new Font("MV Boli", Font.BOLD, 15));
		hangmanWordTextField.setBounds(356, 99, 70, 27);
		frame.getContentPane().add(hangmanWordTextField);
		hangmanWordTextField.setColumns(10);
		hangmanWordTextField.setEditable(false);
		hangmanWordTextField.setFocusable(false);
		hangmanWordTextField.setToolTipText("Secret Word");

		letter1TextField = new JFormattedTextField(createFormatter("U"));
		letter1TextField.setFont(new Font("Papyrus", Font.ITALIC, 11));
		letter1TextField.setBounds(306, 186, 17, 20);
		frame.getContentPane().add(letter1TextField);
		letter1TextField.setColumns(10);
		letter1TextField.addKeyListener(this);
		letter1TextField.addFocusListener(this);

		letter2TextField = new JFormattedTextField(createFormatter("U"));
		letter2TextField.setFont(new Font("Papyrus", Font.ITALIC, 11));
		letter2TextField.setColumns(10);
		letter2TextField.setBounds(333, 186, 17, 20);
		frame.getContentPane().add(letter2TextField);
		letter2TextField.addKeyListener(this);
		letter2TextField.addFocusListener(this);

		letter3TextField = new JFormattedTextField(createFormatter("U"));
		letter3TextField.setFont(new Font("Papyrus", Font.ITALIC, 11));
		letter3TextField.setColumns(10);
		letter3TextField.setBounds(360, 186, 17, 20);
		frame.getContentPane().add(letter3TextField);
		letter3TextField.addKeyListener(this);
		letter3TextField.addFocusListener(this);

		letter4TextField = new JFormattedTextField(createFormatter("U"));
		letter4TextField.setFont(new Font("Papyrus", Font.ITALIC, 11));
		letter4TextField.setColumns(10);
		letter4TextField.setBounds(387, 186, 17, 20);
		frame.getContentPane().add(letter4TextField);
		letter4TextField.addKeyListener(this);
		letter4TextField.addFocusListener(this);

		JLabel lblWordText = new JLabel("WORD:");
		lblWordText.setFont(new Font("Century Schoolbook", Font.PLAIN, 13));
		lblWordText.setForeground(Color.WHITE);
		lblWordText.setBounds(300, 102, 126, 24);
		frame.getContentPane().add(lblWordText);

		JLabel lblHangmanTheme = new JLabel("4-LETTER CAR BRANDS");
		lblHangmanTheme.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		lblHangmanTheme.setForeground(Color.WHITE);
		lblHangmanTheme.setBounds(273, 44, 183, 29);
		frame.getContentPane().add(lblHangmanTheme);

		try {
			// read file of random hangman words
			Scanner myReader = new Scanner(new File("Hangman.txt"));

			while (myReader.hasNext()) {
				// store every line in arraylist
				// to attach a line number to each element
				line.add(myReader.nextLine());
			}

			myReader.close();

		} catch (FileNotFoundException e) {
			logger.error("Error: File not found. " + e.toString());
		}

		getHangmanWord();
	}

	/**
	 * MaskFormatter is used to specify/create the format of a text field
	 * 
	 * @param argString is the text field's user input area (the box)
	 * @return the formatted text field
	 */
	protected MaskFormatter createFormatter(String argString) {

		try {
			return new MaskFormatter(argString);
		} catch (java.text.ParseException exc) {
			logger.error("formatter is bad: " + exc.getMessage());
			System.exit(-1);
		}
		
		return null;
	}

	/**
	 * read from list file and randomly select a hangman word
	 */
	public void getHangmanWord() {
		// choose random word from txt file
		hangmanWord = line.get(randomGenerator.nextInt(6));
		maskingAsterisk = new String(new char[hangmanWord.length()]).replace("\0", "*");
		
		// this line is revealing the word to the console 
		// be sure to remove before doing a build
		System.out.println(hangmanWord);		
	}

	/**
	 * resets the game, occurs when user wins or looses
	 */
	public void resetGame() {
		hangmanTextField.setText("");
		hangmanWordTextField.setText("");
		// Needed to use setText() twice for each letter to fix extra space being
		// entered
		letter1TextField.setText("");
		letter1TextField.setText("");
		letter2TextField.setText("");
		letter2TextField.setText("");
		letter3TextField.setText("");
		letter3TextField.setText("");
		letter4TextField.setText("");
		letter4TextField.setText("");
		letter1TextField.requestFocus();
		w = o = r = d = false;
		getHangmanWord();
		count = 0;
	}
	
	// performs actions to display correct placements of * while user is guessing
	public static String hang(char argUsersGuess) {
		String newasterisk = "";
		for (int i = 0; i < hangmanWord.length(); i++) {
			if (hangmanWord.charAt(i) == argUsersGuess) {
				newasterisk += argUsersGuess;
			} else if (maskingAsterisk.charAt(i) != '*') {
				newasterisk += hangmanWord.charAt(i);
			} else {
				newasterisk += "*";
			}
		}			

		maskingAsterisk = newasterisk;
		return newasterisk;
	}

	/**
	 * Performs appropriate actions when key pressed
	 */
	@Override
	public void keyPressed(KeyEvent e) {

		// accept only letters from user
		if (KeyEvent.getKeyText(e.getKeyCode()).matches("[a-zA-Z]")) {
			
			char charGuessed = Character.toUpperCase(e.getKeyChar());
			
			// text field 1 chosen + letter entered matches first char of
			// hangman word + the letter hasn't been guessed yet
			if (t1 && (charGuessed == hangmanWord.charAt(0)) && !w) {
				hangmanWordTextField.setText(hang(charGuessed)); // x000
				w = true;
			}

			else if (t2 && (charGuessed == hangmanWord.charAt(1)) && !o) {
				hangmanWordTextField.setText(hang(charGuessed)); // 0x00
				o = true;
			}

			else if (t3 && (charGuessed == hangmanWord.charAt(2)) && !r) {
				hangmanWordTextField.setText(hang(charGuessed)); // 00x0
				r = true;
			}

			else if (t4 && (charGuessed == hangmanWord.charAt(3)) && !d) {
				hangmanWordTextField.setText(hang(charGuessed)); // 000x
				// below line is a code fix: will force set/display final letter if correct before
				// trigger of winner message appears
				letter4TextField.setText(letter4TextField.getText());
				d = true;
			}

			else if (letter1TextField.getText().length() <= 1 && (charGuessed != hangmanWord.charAt(0))
					&& (charGuessed != hangmanWord.charAt(1))
					&& (charGuessed != hangmanWord.charAt(2))) {

				// defect fix - prevent character from loosing health if same wrong letter was
				// pressed more than once
				if (e.getKeyChar() != tmp) {
					hangmanTextField.append(hangmanDrawing.get(count));
					count++;
				} 

				if (count == hangmanDrawing.size()) {
					JOptionPane.showMessageDialog(frame.getComponent(0), "GAME OVER! The word was: " + hangmanWord);
					resetGame();
				}

				tmp = e.getKeyChar();
			}

			if (w && o && r && d) {
				JOptionPane.showMessageDialog(frame.getComponent(0), "CORRECT! YOU WIN! The word was: " + hangmanWord);
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
		
		letter1TextField.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
		letter2TextField.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
		letter3TextField.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
		letter4TextField.setBorder(BorderFactory.createLineBorder(Color.gray, 1));

		if (letter1TextField.hasFocus()) {
			letter1TextField.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 2));
			t1 = true;
		}

		else if (letter2TextField.hasFocus()) {
			letter2TextField.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 2));
			t2 = true;
		}

		else if (letter3TextField.hasFocus()) {
			letter3TextField.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 2));
			t3 = true;
		}

		else if (letter4TextField.hasFocus()) {
			letter4TextField.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 2));
			t4 = true;
		}
	}

	/**
	 * Indicates if specific text field doesn't have insertion pointer (at current
	 * moment)
	 */
	@Override
	public void focusLost(FocusEvent e) {

		if (!letter1TextField.hasFocus()) {
			t1 = false;
		}

		else if (!letter2TextField.hasFocus()) {
			t2 = false;
		}

		else if (!letter3TextField.hasFocus()) {
			t3 = false;
		}

		else if (!letter4TextField.hasFocus()) {
			t4 = false;
		}
	}
}
