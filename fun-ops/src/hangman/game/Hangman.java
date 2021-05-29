package hangman.game;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

public class Hangman implements KeyListener, FocusListener {

	private JFrame frame;
	private JFormattedTextField letter1TextField;
	private JFormattedTextField letter2TextField;
	private JFormattedTextField letter3TextField;
	private JFormattedTextField letter4TextField;
	private JTextField hangmanTextField;
	private JTextField hangmanWordTextField;
	private Random rand = new Random();
	String wrongLetterResult = "YOU LOOSE";
	// chosen hangman word
	String word = "";
		
	// store contents of random words in arraylist to give ability to extract txt at specific line
	ArrayList<String> line = new ArrayList<>();
	
	// placeholder for a counter
	int count = 0;
	
	// placeholder to store letter entered
	char letterGuessed;
	
	// flags to indicate which of the 4 user guessing text fields have the insertion pointer
	boolean t1, t2, t3, t4 = false;
	
	// flags to indicate this particular letter has been discovered by user and printed
	boolean w, o, r, d = false;
	
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
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application and initialize the contents of the frame
	 */
	public Hangman() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(223, 158, 171, 7);
		frame.getContentPane().add(separator);
		
		hangmanTextField = new JTextField();
		hangmanTextField.setFont(new Font("Tahoma", Font.PLAIN, 18));
		hangmanTextField.setBounds(29, 31, 151, 172);
		frame.getContentPane().add(hangmanTextField);
		hangmanTextField.setColumns(10);
		hangmanTextField.setEditable(false);
		hangmanTextField.setFocusable(false);
		hangmanTextField.setHorizontalAlignment(JTextField.CENTER);
		
		hangmanWordTextField = new JTextField();
		hangmanWordTextField.setBounds(289, 72, 86, 20);
		frame.getContentPane().add(hangmanWordTextField);
		hangmanWordTextField.setColumns(10);
		hangmanWordTextField.setEditable(false);
		hangmanWordTextField.setFocusable(false);
		
		letter1TextField = new JFormattedTextField(createFormatter("U"));
		letter1TextField.setBounds(257, 176, 17, 20);
		frame.getContentPane().add(letter1TextField);
		letter1TextField.setColumns(10);
		letter1TextField.addKeyListener(this);
		letter1TextField.addFocusListener(this);
		
		letter2TextField = new JFormattedTextField(createFormatter("U"));
		letter2TextField.setColumns(10);
		letter2TextField.setBounds(284, 176, 17, 20);
		frame.getContentPane().add(letter2TextField);
		letter2TextField.addKeyListener(this);
		letter2TextField.addFocusListener(this);
		
		letter3TextField = new JFormattedTextField(createFormatter("U"));
		letter3TextField.setColumns(10);
		letter3TextField.setBounds(311, 176, 17, 20);
		frame.getContentPane().add(letter3TextField);
		letter3TextField.addKeyListener(this);
		letter3TextField.addFocusListener(this);
		
		letter4TextField = new JFormattedTextField(createFormatter("U"));
		letter4TextField.setColumns(10);
		letter4TextField.setBounds(338, 176, 17, 20);
		frame.getContentPane().add(letter4TextField);
		letter4TextField.addKeyListener(this);
		letter4TextField.addFocusListener(this);
				
		JLabel lblNewLabel = new JLabel("WORD:");
		lblNewLabel.setBounds(239, 75, 46, 14);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("4-LETTER CAR BRANDS");
		lblNewLabel_1.setBounds(243, 31, 151, 14);
		frame.getContentPane().add(lblNewLabel_1);
		
		getHangmanWord();
	}
	
	protected MaskFormatter createFormatter(String s) {
	    MaskFormatter formatter = null;
	    try {
	        formatter = new MaskFormatter(s);
	    } catch (java.text.ParseException exc) {
	        System.err.println("formatter is bad: " + exc.getMessage());
	        System.exit(-1);
	    }
	    return formatter;
	}
	
	/**
	 * read from list file and randomly select a hangman word
	 */
	public void getHangmanWord() {
		try {
			// read file of random hangman words
			File myObj = new File("hangmanWords.txt");
			Scanner myReader = new Scanner(myObj);
						
			while(myReader.hasNext()) {
				// store every line in arraylist 
				// to attach a line number to each element
				line.add(myReader.nextLine());
			}
						
			// choose random word from txt file
			word = line.get(rand.nextInt(6));
			
			myReader.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
		}
	}
	
	
	/**
	 * resets the game, occurs when user wins or looses
	 */
	public void resetGame() {
		hangmanTextField.setText(""); hangmanWordTextField.setText(""); letter1TextField.setText("");
		letter2TextField.setText(""); letter3TextField.setText(""); letter4TextField.setText("");
		letter1TextField.requestFocus();
		w = o = r = d = false;
		getHangmanWord();
		count = 0;
	}
	
	@Override
	public void keyTyped(KeyEvent e) { 
		// Do nothing here because method isn't needed but must be overridden due to interface rule
	}

	/**
	 * Performs appropriate actions when key pressed
	 */
	@Override
	public void keyPressed(KeyEvent e) {	
		
		// accept only letters from user
		if(KeyEvent.getKeyText(e.getKeyCode()).matches("[a-zA-Z]")) {
						
			// text field 1 chosen + letter entered matches first char of 
			// hangman word + the letter hasn't been guessed yet 
			if(t1 && Character.toUpperCase(e.getKeyChar()) == word.charAt(0) && !w) {
				hangmanWordTextField.setText(String.valueOf(word.charAt(0)));
				w = true;
			}
					
			else if(t2 && Character.toUpperCase(e.getKeyChar()) == word.charAt(1) && !o) {
				hangmanWordTextField.setText(hangmanWordTextField.getText() + String.valueOf(word.charAt(1)));
				o = true;
			}
			
			else if(t3 && Character.toUpperCase(e.getKeyChar()) == word.charAt(2) && !r) {
				hangmanWordTextField.setText(hangmanWordTextField.getText() + String.valueOf(word.charAt(2)));
				r = true;
			}
			
			else if(t4 && Character.toUpperCase(e.getKeyChar()) == word.charAt(3) && !d) {
				hangmanWordTextField.setText(hangmanWordTextField.getText() + String.valueOf(word.charAt(3)));
				d = true;
			}
			
			else if(Character.toUpperCase(e.getKeyChar()) != word.charAt(0) && Character.toUpperCase(e.getKeyChar()) != word.charAt(1)
					&& Character.toUpperCase(e.getKeyChar()) != word.charAt(2) && e.getKeyChar() != KeyEvent.VK_BACK_SPACE) {
											
				// in large text box add a character for display from 'YOU LOOSE'
				hangmanTextField.setText(hangmanTextField.getText() + wrongLetterResult.charAt(count));
	
				if(count == wrongLetterResult.length()-1) {
					JOptionPane.showMessageDialog(frame.getComponent(0), "YOU LOOSE.");
					resetGame();
				}
				
				count++;
			}
			
			if(w && o && r && d) {
				JOptionPane.showMessageDialog(frame.getComponent(0), "YOU WIN! The random word was: " + word);
				resetGame();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {		
		// Do nothing here because method isn't needed but must be overridden due to interface rule
	}

	/**
	 * Indicates which text field is currently holding insertion pointer
	 */
	@Override
	public void focusGained(FocusEvent e) {		
		
		if(letter1TextField.hasFocus()) {		
			t1 = true;
		}
		
		else if(letter2TextField.hasFocus()) {
			t2 = true;
		}
		
		else if(letter3TextField.hasFocus()) {
			t3 = true;
		}
		
		else if(letter4TextField.hasFocus()) {
			t4 = true;
		}
	}

	/**
	 * Indicates if specific text field doesn't not have insertion pointer
	 */
	@Override
	public void focusLost(FocusEvent e) {
		
		if(!letter1TextField.hasFocus()) {
			t1 = false;
		}
		
		else if(!letter2TextField.hasFocus()) {
			t2 = false;
		}
		
		else if(!letter3TextField.hasFocus()) {
			t3 = false;
		}
		
		else if(!letter4TextField.hasFocus()) {
			t4 = false;
		}
	}
}