package calculator;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.SwingConstants;

/**
 * Application implementation for the calculator's UI. Builds and displays all
 * GUI components for the calculator. Note: the reason for all this back and
 * forth casting is because textField and all other Swing GUI components are
 * type String class. I extended KeyAdapter abstract class instead of
 * KeyListener for the purpose of not having to hold override methods that I
 * won't be using <br>
 * 
 * @author Brian Perel
 */
public class App extends KeyAdapter implements ActionListener {

	private JFrame frame;
	private static String cursorRightPositionedWithZero, cursorRightPositioned;
	private JFormattedTextField userInputTextField;
	private DecimalFormat df = new DecimalFormat("#0"); // for whole number rounding
	protected static boolean[] operatorFlags = new boolean[4]; // array to hold flags to be raised if a calculator operator is
														// clicked
	private char[] spacesForMainTextField = new char[31];
	private static boolean hasNumberZeroBeenEnteredByUser;
	private JButton[] buttons = new JButton[24];

	public static void main(String[] args) {		
		try {
			App window = new App();
			window.frame.setVisible(true);
			window.frame.setTitle("Calculator App by: Brian Perel");
			window.frame.setResizable(false);
			window.frame.setLocationRelativeTo(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initializes the contents of the frame, building the gui.
	 */
	public App() {
		Arrays.fill(spacesForMainTextField, ' ');
		cursorRightPositioned = String.valueOf(spacesForMainTextField);
		cursorRightPositionedWithZero = cursorRightPositioned.concat("0");

		frame = new JFrame();
		frame.setBounds(100, 100, 400, 436);
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		buttons[0] = new JButton("1/x");
		buttons[1] = new JButton("CE");
		buttons[2] = new JButton("C");
		buttons[3] = new JButton("\u232B"); // unicode for backspace symbol = \u232B
		buttons[4] = new JButton("%");
		buttons[5] = new JButton("x\u00B2"); // unicode for X^2 (x squared) = x\u00B2
		buttons[6] = new JButton("2\u221Ax"); // unicode for 2 square root x symbol = 2\u221Ax
		buttons[7] = new JButton("\u00F7"); // unicode for division symbol = \u00F7
		buttons[8] = new JButton("*");
		buttons[9] = new JButton("0");
		buttons[10] = new JButton("1");
		buttons[11] = new JButton("2");
		buttons[12] = new JButton("3");
		buttons[13] = new JButton("4");
		buttons[14] = new JButton("5");
		buttons[15] = new JButton("6");
		buttons[16] = new JButton("7");
		buttons[17] = new JButton("8");
		buttons[18] = new JButton("9");
		buttons[19] = new JButton("-");
		buttons[20] = new JButton("+");
		buttons[21] = new JButton("+/-");
		buttons[22] = new JButton(".");
		buttons[23] = new JButton("=");

		for (JButton button : buttons) {
			button.setFont(new Font("Bookman Old Style", Font.BOLD, 13));
			button.setBackground(Color.WHITE);
			frame.getContentPane().add(button);
			button.addActionListener(this);
			button.addKeyListener(this);
			button.setHorizontalAlignment(SwingConstants.CENTER);

			button.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					button.setBackground(new Color(225, 225, 225));
				}

				@Override
				public void mouseExited(java.awt.event.MouseEvent evt) {
					button.setBackground(Color.WHITE);
				}
			});
		}

		buttons[0].setBounds(31, 141, 80, 40);
		buttons[1].setBounds(110, 100, 80, 40);
		buttons[2].setBounds(189, 100, 80, 40);
		buttons[4].setBounds(31, 100, 80, 40);
		buttons[5].setBounds(110, 141, 80, 40);
		buttons[6].setBounds(189, 141, 80, 40);
		buttons[7].setBounds(268, 141, 80, 40);
		buttons[8].setBounds(268, 182, 80, 40);
		buttons[9].setBounds(110, 304, 80, 40);
		buttons[10].setBounds(31, 263, 80, 40);
		buttons[11].setBounds(110, 263, 80, 40);
		buttons[12].setBounds(189, 263, 80, 40);
		buttons[13].setBounds(31, 222, 80, 40);
		buttons[14].setBounds(110, 222, 80, 40);
		buttons[15].setBounds(189, 222, 80, 40);
		buttons[16].setBounds(31, 182, 80, 40);
		buttons[17].setBounds(110, 182, 80, 40);
		buttons[18].setBounds(189, 182, 80, 40);
		buttons[19].setBounds(268, 222, 80, 40);
		buttons[20].setBounds(268, 263, 80, 40);
		buttons[21].setBounds(31, 304, 80, 40);
		buttons[22].setBounds(189, 304, 80, 40);
		buttons[23].setBounds(268, 304, 80, 40);

		// unicode for backspace symbol
		JButton btnBackspace = new JButton("\u232B");
		btnBackspace.setBackground(Color.WHITE);
		btnBackspace.setBounds(268, 100, 80, 40);
		frame.getContentPane().add(btnBackspace);
		btnBackspace.addActionListener(this);
		btnBackspace.addKeyListener(this);
		btnBackspace.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnBackspace.setBackground(new Color(225, 225, 225));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnBackspace.setBackground(Color.WHITE);
			}
		});

		userInputTextField = new JFormattedTextField(cursorRightPositionedWithZero);
		userInputTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		userInputTextField.setFont(new Font("Bookman Old Style", Font.PLAIN, 16));
		userInputTextField.setBounds(33, 27, 315, 40);
		userInputTextField.setBorder(BorderFactory.createLineBorder(Color.gray, 3));

		frame.getContentPane().add(userInputTextField);
		userInputTextField.setColumns(10);
		userInputTextField.setEditable(false);
		userInputTextField.addKeyListener(this);
	}

	/**
	 * This is responsible for listening to when buttons are clicked via mouse (actions).
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		// remove the auto display '0' value from the main number entry textField box so that
		// this '0' is not included in calculations
		// Since it's only needed for display purposes
		if (userInputTextField.getText().equals(cursorRightPositionedWithZero) && !hasNumberZeroBeenEnteredByUser) {
			userInputTextField.setText(cursorRightPositioned);
		}

		// actions for numbers 0-9 buttons and all calculator operands
		switch (ae.getActionCommand()) {
		case "0":
			if (!userInputTextField.getText().equals(cursorRightPositionedWithZero)) {
				userInputTextField.setText(userInputTextField.getText().concat("0"));
				hasNumberZeroBeenEnteredByUser = true;
			}
			break;

		case "1":
			userInputTextField.setText(userInputTextField.getText().concat("1"));
			break;

		case "2":
			userInputTextField.setText(userInputTextField.getText().concat("2"));
			break;

		case "3":
			userInputTextField.setText(userInputTextField.getText().concat("3"));
			break;

		case "4":
			userInputTextField.setText(userInputTextField.getText().concat("4"));
			break;

		case "5":
			userInputTextField.setText(userInputTextField.getText().concat("5"));
			break;

		case "6":
			userInputTextField.setText(userInputTextField.getText().concat("6"));
			break;

		case "7":
			userInputTextField.setText(userInputTextField.getText().concat("7"));
			break;

		case "8":
			userInputTextField.setText(userInputTextField.getText().concat("8"));
			break;

		case "9":
			userInputTextField.setText(userInputTextField.getText().concat("9"));
			break;

		// actions for symbol buttons
		case "*":
			Calculator.setNumber(userInputTextField.getText());
			userInputTextField.setText(cursorRightPositioned);
			operatorFlags[1] = true;
			break;

		// division symbol
		case "\u00F7":
			Calculator.setNumber(userInputTextField.getText());
			userInputTextField.setText(cursorRightPositioned);
			operatorFlags[0] = true;
			break;

		case "+":
			Calculator.setNumber(userInputTextField.getText());
			userInputTextField.setText(cursorRightPositioned);
			operatorFlags[3] = true;
			break;

		case "-":
			Calculator.setNumber(userInputTextField.getText());
			userInputTextField.setText(cursorRightPositioned);
			operatorFlags[2] = true;
			break;

		// backspace symbol
		case "\u232B":
			userInputTextField.setText(!userInputTextField.getText().equals(cursorRightPositioned)
					? userInputTextField.getText().substring(0, userInputTextField.getText().length() - 1)
					: cursorRightPositionedWithZero);
			break;

		case "1/x":			
			// validate that the current text in textField isn't blank
			userInputTextField.setText(!userInputTextField.getText().equals(cursorRightPositioned)
					// need to cast below multiple times in order to perform 1/x operation
					? cursorRightPositioned.concat(Double.toString(1 / Double.valueOf(userInputTextField.getText())))
					: cursorRightPositionedWithZero);
			break;

		case "=":
			// if textField label is blank, then no action has been done by user.
			// Hence in that scenario equal operation isn't performed
			if (!userInputTextField.getText().equals(cursorRightPositioned)) {
				Calculator.setNumber(userInputTextField.getText());

				// perform computation to make the value
				String value = Calculator.compute();

				// if value is whole then don't display 0's after decimal; ex. instead of 25.00
				// display 25
				double v = Double.parseDouble(value);
				if ((v * 10) % 10 == 0) { // if value calculated is whole number
					value = df.format(v); // removes zero's after decimal point
				}

				// check for division by zero. Avoids exception being flagged
				userInputTextField.setText(
						Calculator.divideByZeroflag ? " Cannot divide by zero" : cursorRightPositioned.concat(value));

				// reset all array values to 0
				Collections.fill(Calculator.stringNumbers, "");
				hasNumberZeroBeenEnteredByUser = false;
				resetValues();
			} else if (userInputTextField.getText().equals(cursorRightPositioned)) {
				userInputTextField.setText(cursorRightPositionedWithZero);
			} 
			break; // break statement for case equals button

		case "CE", "C":
			userInputTextField.setText(cursorRightPositionedWithZero);
			Collections.fill(Calculator.stringNumbers, null);
			Calculator.divideByZeroflag = false;
			resetValues();
			break;

		case ".":
			if (!userInputTextField.getText().contains(".")) {
				userInputTextField.setText(userInputTextField.getText().concat("."));
			}
			break;

		case "%":
			if (!userInputTextField.getText().equals(cursorRightPositioned)) {
				Calculator.setNumber(
						String.valueOf(Calculator.percentage(Double.parseDouble(userInputTextField.getText()))));
				userInputTextField.setText(userInputTextField.getText().concat("%"));
			} else {
				userInputTextField.setText(cursorRightPositionedWithZero);
			}			
			break;

		// x\u00B2 -> X^2 symbol
		case "x\u00B2":			
			userInputTextField.setText(!userInputTextField.getText().equals(cursorRightPositioned)
					? cursorRightPositioned
							.concat(Double.toString(Math.pow((Double.valueOf(userInputTextField.getText())), 2)))
					: cursorRightPositionedWithZero);			
			break;

		// 2\u221Ax -> 2 square root x symbol
		case "2\u221Ax":			
			userInputTextField.setText(!userInputTextField.getText().equals(cursorRightPositioned)
					? cursorRightPositioned.concat(String.valueOf(Math.sqrt(Double.valueOf(userInputTextField.getText()))))
					: cursorRightPositionedWithZero);
			break;

		case "+/-":
			if (!userInputTextField.getText().equals(cursorRightPositioned)) {
				// if current number is positive, number becomes negative (minus is
				// prepended) else if number is negative, number becomes positive (minues is
				// removed)
				userInputTextField.setText(userInputTextField.getText().trim().substring(0, 1).equals("-")
						? cursorRightPositioned.concat(userInputTextField.getText().replace("-", ""))
						: cursorRightPositioned.concat(("-".concat(userInputTextField.getText().trim()))));
			}
			break;

		default:
			break;
		} // end switch
	}

	/**
	 * This is responsible for listening to all keyboard input.
	 */
	@Override
	public void keyPressed(KeyEvent e) {

		// remove auto display '0' value from main number entry textField box so that
		// '0' is not included in calculation
		// Since it's only needed for display purposes
		if (userInputTextField.getText().equals(cursorRightPositionedWithZero) && !hasNumberZeroBeenEnteredByUser) {
			userInputTextField.setText(cursorRightPositioned);
		}

		// actions for numbers 0-9 buttons. No need for default case since all buttons
		// are utilized as a case
		switch (e.getKeyChar()) {
			case KeyEvent.VK_0:
				userInputTextField.setText(userInputTextField.getText().concat("0"));
				hasNumberZeroBeenEnteredByUser = true;
				break;
	
			case KeyEvent.VK_1:
				userInputTextField.setText(userInputTextField.getText().concat("1"));
				break;
	
			case KeyEvent.VK_2:
				userInputTextField.setText(userInputTextField.getText().concat("2"));
				break;
	
			case KeyEvent.VK_3:
				userInputTextField.setText(userInputTextField.getText().concat("3"));
				break;
	
			case KeyEvent.VK_4:
				userInputTextField.setText(userInputTextField.getText().concat("4"));
				break;
	
			case KeyEvent.VK_5:
				userInputTextField.setText(userInputTextField.getText().concat("5"));
				break;
	
			case KeyEvent.VK_6:
				userInputTextField.setText(userInputTextField.getText().concat("6"));
				break;
	
			case KeyEvent.VK_7:
				userInputTextField.setText(userInputTextField.getText().concat("7"));
				break;
	
			case KeyEvent.VK_8:
				userInputTextField.setText(userInputTextField.getText().concat("8"));
				break;
	
			case KeyEvent.VK_9:
				userInputTextField.setText(userInputTextField.getText().concat("9"));
				break;
	
			// actions for symbol buttons
			case KeyEvent.VK_SLASH:
				Calculator.setNumber(userInputTextField.getText());
				userInputTextField.setText(cursorRightPositioned);
				operatorFlags[0] = true;
				break;
	
			// KeyEvent.VK_PLUS DOESN'T WORK - it was left for reference to see what it
			// would look like in
			// KeyEvent code
			case KeyEvent.VK_PLUS:
				Calculator.setNumber(userInputTextField.getText());
				userInputTextField.setText(cursorRightPositioned);
				operatorFlags[3] = true;
				break;
	
			case KeyEvent.VK_MINUS:
				Calculator.setNumber(userInputTextField.getText());
				userInputTextField.setText(cursorRightPositioned);
				operatorFlags[2] = true;
				break;
	
			case KeyEvent.VK_ENTER, KeyEvent.VK_EQUALS:
				// if textField label is blank, then no action has been done by user.
				// Hence in that scenario equal operation isn't performed
				if (!userInputTextField.getText().equals(cursorRightPositioned)) {
					Calculator.setNumber(userInputTextField.getText());
	
					// perform computation to make the value
					String value = Calculator.compute();
	
					// if value is whole then don't display 0's after decimal; ex. instead of 25.00
					// display 25
					double v = Double.parseDouble(value);
					if ((v * 10) % 10 == 0) { // if value calculated is whole number
						value = df.format(v); // removes zero's after decimal point
					}
	
					// check for division by zero. Avoids exception being flagged
					userInputTextField.setText(
							Calculator.divideByZeroflag ? " Cannot divide by zero" : cursorRightPositioned + value);
	
					// reset all array values to 0
					Collections.fill(Calculator.stringNumbers, "");
					resetValues();
				}
				break; // break statement for case enter key button
	
			case KeyEvent.VK_BACK_SPACE:
				userInputTextField
						.setText(userInputTextField.getText().substring(0, userInputTextField.getText().length() - 1));
				break;
	
			case KeyEvent.VK_PERIOD:
				if (!userInputTextField.getText().contains(".")) {
					userInputTextField.setText(userInputTextField.getText() + ".");
				}
				break;
	
			default:
				break;
		}

		/**
		 * use e.getKeyChar() here for "*", "+", "C" because KeyEvent.VK_MULTIPLY,
		 * KeyEvent.VK_ADD, and KeyEvent.C don't work.
		 */

		if (Character.toUpperCase(e.getKeyChar()) == 'C') {
			userInputTextField.setText(cursorRightPositionedWithZero);
			resetValues();
		}

		if (e.getKeyChar() == '*' || e.getKeyChar() == '+') {
			Calculator.setNumber(userInputTextField.getText());
			userInputTextField.setText(cursorRightPositioned);

			if (e.getKeyChar() == '*') {
				operatorFlags[1] = true;
			} else {
				operatorFlags[3] = true;
			}
		}
	}

	/**
	 * Resets all calculator array's values.
	 */
	public static void resetValues() {
		Arrays.fill(operatorFlags, Boolean.FALSE);
		hasNumberZeroBeenEnteredByUser = false;
		Collections.fill(Calculator.doubleNumbers, 0.0);
		Calculator.stringNumbers.clear();
		Calculator.doubleNumbers.clear();
		Calculator.arrayNumsFilled = Calculator.arrayPositionNumber = 0;
	}
}