package com.calculator;

import static java.awt.Color.WHITE;
import static java.awt.event.KeyEvent.VK_0;
import static java.awt.event.KeyEvent.VK_1;
import static java.awt.event.KeyEvent.VK_2;
import static java.awt.event.KeyEvent.VK_3;
import static java.awt.event.KeyEvent.VK_4;
import static java.awt.event.KeyEvent.VK_5;
import static java.awt.event.KeyEvent.VK_6;
import static java.awt.event.KeyEvent.VK_7;
import static java.awt.event.KeyEvent.VK_8;
import static java.awt.event.KeyEvent.VK_9;
import static java.awt.event.KeyEvent.VK_BACK_SPACE;
import static java.awt.event.KeyEvent.VK_DIVIDE;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_EQUALS;
import static java.awt.event.KeyEvent.VK_MINUS;
import static java.awt.event.KeyEvent.VK_MULTIPLY;
import static java.awt.event.KeyEvent.VK_PERIOD;
import static java.awt.event.KeyEvent.VK_PLUS;
import static java.awt.event.KeyEvent.VK_SLASH;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

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
public class MyCalculatorGui extends KeyAdapter implements ActionListener {

	protected static final String CURSOR_RIGHT_POSITION = String.format("%31s", "");
	protected static final String CURSOR_RIGHT_POSITION_W_ZERO = CURSOR_RIGHT_POSITION + "0";
	private static final Color SUPER_LIGHT_GRAY = new Color(225, 225, 225);
	protected static boolean[] operatorFlags = new boolean[4]; // array to hold flags to be raised if a calculator operator is clicked or pressed
	protected static JFormattedTextField textFieldUserInput;
	protected static boolean hasUserEnteredZero;

	private final MyCalculatorHelper helper = new MyCalculatorHelper();
	private JFrame window = new JFrame("Calculator App by: Brian Perel");

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			System.out.println("Failed to set LookAndFeel\n" + e.getMessage());
			e.printStackTrace();
		}

		MyCalculatorGui calculator = new MyCalculatorGui();
		calculator.window.setVisible(true);
	}

	/**
	 * Places all the buttons on the app's board and initializes the contents of the frame, building the GUI.
	 */
	public MyCalculatorGui() {
		createGui();
	}

	private void createGui() {
		window.getContentPane().setBackground(Color.DARK_GRAY);
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.getContentPane().setLayout(null);
		window.setResizable(false);
		window.setSize(400, 436);

    	 // changes the program's taskbar icon
	    window.setIconImage(new ImageIcon("res/graphics/taskbar_icons/calculator.png").getImage());

		textFieldUserInput = new JFormattedTextField(CURSOR_RIGHT_POSITION_W_ZERO);
		textFieldUserInput.requestFocus();
		textFieldUserInput.setHorizontalAlignment(SwingConstants.RIGHT);
		textFieldUserInput.setFont(new Font("Bookman Old Style", Font.PLAIN, 16));
		textFieldUserInput.setBounds(33, 27, 315, 40);
		textFieldUserInput.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3));
		window.getContentPane().add(textFieldUserInput);
		textFieldUserInput.setColumns(10);
		textFieldUserInput.setEditable(false);
		textFieldUserInput.addKeyListener(this);

		JButton[] buttons = new JButton[24]; // change this to 25 if removing frame.getContentPane().setLayout(null)
		buttons[0] = new JButton("1/x");
		buttons[1] = new JButton("CE");
		buttons[2] = new JButton("C");
		buttons[3] = new JButton("⌫");
		buttons[4] = new JButton("%");
		buttons[5] = new JButton("x²");
		buttons[6] = new JButton("2√x");
		buttons[7] = new JButton("÷");
		buttons[8] = new JButton("*");

		// assign numeric keypad button values for buttons 9-18
		for (int x = 0; x <= 9; x++) {
			buttons[x+9] = new JButton(Integer.toString(x));
		}

		buttons[19] = new JButton("-");
		buttons[20] = new JButton("+");
		buttons[21] = new JButton("+/-");
		buttons[22] = new JButton(".");
		buttons[23] = new JButton("=");
		// buttons[24] = new JButton(); // fixes an issue caused when removing frame.getContentPane().setLayout(null)

		for (JButton button : buttons) {
			button.setFont(new Font("Bookman Old Style", Font.BOLD + Font.ITALIC, 13));
			button.setBackground(WHITE);
			window.getContentPane().add(button);
			button.addActionListener(this);
			button.addKeyListener(this);
			button.setSize(80, 40);
			button.setHorizontalAlignment(SwingConstants.CENTER);
			// below code is used to create the button hover effect
			button.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent evt) {
					button.setBackground(SUPER_LIGHT_GRAY);
				}

				@Override
				public void mouseExited(MouseEvent evt) {
					button.setBackground(WHITE);
				}
			});

			setHoverColor(button, WHITE, new Color(160, 160, 160));
		}

		// need to custom add default font details for the backspace button, as the font details applied from above prevent the backspace symbol from being displayed
		buttons[3].setFont(new Font("SansSerif", Font.BOLD, 12));

		buttons[0].setLocation(31, 141);
		buttons[1].setLocation(110, 100);
		buttons[2].setLocation(189, 100);
		buttons[3].setLocation(268, 100);
		buttons[4].setLocation(31, 100);
		buttons[5].setLocation(110, 141);
		buttons[6].setLocation(189, 141);
		buttons[7].setLocation(268, 141);
		buttons[8].setLocation(268, 182);
		buttons[9].setLocation(110, 304);
		buttons[10].setLocation(31, 263);
		buttons[11].setLocation(110, 263);
		buttons[12].setLocation(189, 263);
		buttons[13].setLocation(31, 222);
		buttons[14].setLocation(110, 222);
		buttons[15].setLocation(189, 222);
		buttons[16].setLocation(31, 182);
		buttons[17].setLocation(110, 182);
		buttons[18].setLocation(189, 182);
		buttons[19].setLocation(268, 222);
		buttons[20].setLocation(268, 263);
		buttons[21].setLocation(31, 304);
		buttons[22].setLocation(189, 304);
		buttons[23].setLocation(268, 304);

		window.setLocationRelativeTo(null);
	}

	/**
	 * Sets a custom color to appear when you hover over a specific button
	 *
	 * @param argBtn button on which you want a different color to appear when hovering
	 * @param orgColor the original color the button had attached
	 * @param hoverColor the color you want to appear when hovering over the specific button
	 */
	private void setHoverColor(JButton argBtn, Color orgColor, Color hoverColor) {
		argBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				argBtn.setBackground(hoverColor);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				argBtn.setBackground(orgColor);
			}
		});
	}

	/**
	 * This is responsible for listening to when buttons are clicked via the mouse (which represent actions).
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		final String userInput = textFieldUserInput.getText().replace(",", "");

		helper.removeAutoDisplayZero(hasUserEnteredZero);

		if (userInput.trim().length() <= 29 && !userInput.contains("%")) {
			String numberClicked = numberActionButtons(ae);

			if(!numberClicked.isEmpty()) {
				textFieldUserInput.setText(MyCalculator.df.format(Double.valueOf(textFieldUserInput.getText().replace(",", "").concat(numberClicked))));
			}
		}

		operatorActionButtons(ae);

		// actions for all calculator operators/symbol buttons
		switch (ae.getActionCommand()) {
		// actions for symbol buttons
		case "CE", "C":
			textFieldUserInput.setText(CURSOR_RIGHT_POSITION_W_ZERO);
			MyCalculator.divideByZeroflag = false;
			helper.resetValues();
			break;

		case ".":
			if (!userInput.contains(".")) {
				textFieldUserInput.setText(MyCalculator.df.format(Double.valueOf(userInput)).concat("."));
			}
			break;

		case "%":
			if (!CURSOR_RIGHT_POSITION.equals(userInput)) {
				// save num in calculator's memory then display the % sign on calc display to avoid exception
				MyCalculator.setNumber(String.valueOf(helper.calculatePercentage(Double.parseDouble(userInput))));
				textFieldUserInput.setText(MyCalculator.df.format(Double.valueOf(userInput)).concat("%"));
				break;
			}

			textFieldUserInput.setText(CURSOR_RIGHT_POSITION_W_ZERO);
			break;

		case "+/-":
			if (!(CURSOR_RIGHT_POSITION.equals(userInput) || CURSOR_RIGHT_POSITION_W_ZERO.equals(userInput))) {
				// if current number is positive, number becomes negative (minus is
				// prepended) else if number is negative, number becomes positive (minus is
				// removed)
				textFieldUserInput.setText(userInput.trim().startsWith("-")
						? CURSOR_RIGHT_POSITION.concat(MyCalculator.df.format(Double.valueOf(userInput.replace("-", ""))))
						: CURSOR_RIGHT_POSITION.concat(MyCalculator.df.format(Double.valueOf("-".concat(userInput.trim())))));
			}
			break;

		case "1/x":
			if (!(userInput.isBlank() || "0".equals(userInput))) {
				final double value = 1 / Double.valueOf(userInput);

				// validate that the current text in textField isn't blank
				textFieldUserInput.setText(!CURSOR_RIGHT_POSITION.equals(userInput)
						// need to cast below multiple times in order to perform 1/x operation
						? CURSOR_RIGHT_POSITION.concat(Double.toString(value))
						: CURSOR_RIGHT_POSITION_W_ZERO);

				// if value is whole then don't display 0's after decimal; ex. instead of 25.00 display 25
				if ((value * 10) % 10 == 0) { // if value calculated is whole number
					textFieldUserInput.setText(MyCalculator.df.format(value)); // removes zero's after decimal point
				}

				break;
			}

			textFieldUserInput.setText(CURSOR_RIGHT_POSITION_W_ZERO);
			break;

		default:
			break;
		} // end switch

		symbolActionButtons(ae, userInput.replace(",", ""));
	}

	private void operatorActionButtons(ActionEvent ae) {
		switch(ae.getActionCommand()) {
			case "*":
				helper.setNumberText();
				operatorFlags[1] = true;
				break;

			// division symbol '÷'
			case "\u00F7":
				helper.setNumberText();
				operatorFlags[0] = true;
				break;

			case "+":
				helper.setNumberText();
				operatorFlags[3] = true;
				break;

			case "-":
				helper.setNumberText();
				operatorFlags[2] = true;
				break;

			case "=":
				helper.performEnterOrEquals();
				break;

			default:
				break;
		}
	}

	private void symbolActionButtons(ActionEvent ae, String argUserInput) {

		switch(ae.getActionCommand()) {
		// backspace symbol
		case "\u232B":
			// if you backspace with only 1 digit in the input box, instead of displaying blank we will display '0'
			// or if you backspace with a negative single digit number (ex. -1) to make just '-' we want to prevent this and instead display '0'
			if (argUserInput.trim().length() == 1 || (argUserInput.trim().startsWith("-") && argUserInput.trim().length() == 2)) {
				textFieldUserInput.setText(CURSOR_RIGHT_POSITION_W_ZERO);
				break;
			}

			// otherwise remove the last digit of the current string in text field
			textFieldUserInput.setText(!CURSOR_RIGHT_POSITION.equals(argUserInput)
					? MyCalculator.df.format(Double.valueOf(argUserInput.substring(0, argUserInput.length() - 1)))
					: CURSOR_RIGHT_POSITION_W_ZERO);
			break;

		// x\u00B2 = X^2 symbol
		case "x\u00B2":
			if (!(argUserInput.isBlank() || "0".equals(argUserInput))) {
				final double valueSquared = Math.pow(Double.valueOf(argUserInput), 2);

				textFieldUserInput.setText(!CURSOR_RIGHT_POSITION.equals(argUserInput)
						? CURSOR_RIGHT_POSITION.concat(Double.toString(valueSquared))
						: CURSOR_RIGHT_POSITION_W_ZERO);

				// if value is whole then don't display 0's after decimal; ex. instead of 25.00 display 25
				if ((valueSquared * 10) % 10 == 0) { // if value calculated is whole number
					textFieldUserInput.setText(MyCalculator.df.format(valueSquared)); // removes zero's after decimal point
				}

				break;
			}

			textFieldUserInput.setText(CURSOR_RIGHT_POSITION_W_ZERO);
			break;

		// 2\u221Ax = 2 square root x symbol
		case "2\u221Ax":
			if (!(argUserInput.isBlank() || "0".equals(argUserInput))) {
				final double valueSquareRooted = Math.sqrt(Double.valueOf(argUserInput));

				textFieldUserInput.setText(!CURSOR_RIGHT_POSITION.equals(argUserInput)
						? CURSOR_RIGHT_POSITION.concat(String.valueOf(valueSquareRooted))
						: CURSOR_RIGHT_POSITION_W_ZERO);

				// if value is whole then don't display 0's after decimal; ex. instead of 25.00 display 25
				if ((valueSquareRooted * 10) % 10 == 0) { // if value calculated is whole number
					textFieldUserInput.setText(MyCalculator.df.format(valueSquareRooted)); // removes zero's after decimal point
				}

				break;
			}

			textFieldUserInput.setText(CURSOR_RIGHT_POSITION_W_ZERO);
			break;

		default:
			break;
		}
	}

	private String numberActionButtons(ActionEvent ae) {
		return switch(ae.getActionCommand()) {
			case "0" -> {
		        if (!CURSOR_RIGHT_POSITION_W_ZERO.equals(textFieldUserInput.getText())) {
		            hasUserEnteredZero = true;
		        }

		        yield "0";
		    }

			case "1" -> "1";
			case "2" -> "2";
			case "3" -> "3";
			case "4" -> "4";
			case "5" -> "5";
			case "6" -> "6";
			case "7" -> "7";
			case "8" -> "8";
			case "9" -> "9";
			default -> "";
		};
	}

	/**
	 * Validates if the key pressed on the keyboard is valid or not
	 *
	 * @param argChar the keyboard character pressed
	 * @return the validation result
	 */
	private boolean isValidKeyPressed(char argChar) {
		// checks if the entered character is a special symbol
		// need to guard against 'o' because 'o' is equivalent to VK_DIVIDE
		// need to guard against 'j' because 'j' is equivalent to VK_MULTIPLY
	    if (argChar != 'o' && argChar != 'j' && (argChar == VK_PLUS || argChar == VK_MINUS
	        || argChar == VK_EQUALS || argChar == VK_MULTIPLY
	        || argChar == VK_DIVIDE || argChar == VK_PERIOD
	        || argChar == VK_BACK_SPACE || argChar == VK_ENTER
	        || argChar == VK_SLASH || argChar == '*'
	        || argChar == '+' || argChar == '-'
	        || Character.toUpperCase(argChar) == 'C')) {

	        return true;
	    }

	    // checks if the entered character is a number
	    return Character.isDigit(argChar);
	}

	/**
	 * This is responsible for listening to all keyboard input.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		final char keyChar = e.getKeyChar();

		// prevent user from attempting to enter a non-numeric value
		if (!isValidKeyPressed(keyChar)) {
			return;
		}

		helper.removeAutoDisplayZero(hasUserEnteredZero);

		// actions for numbers 0-9 buttons. No need for default case since all buttons are utilized as a case
		numberKeyButtons(keyChar);

		operatorKeyButtons(keyChar);

		// use e.getKeyChar() here for "*", "+", "C" because KeyEvent.VK_MULTIPLY, KeyEvent.VK_ADD, and KeyEvent.C don't work.

		if (Character.toUpperCase(keyChar) == 'C') {
			textFieldUserInput.setText(CURSOR_RIGHT_POSITION_W_ZERO);
			helper.resetValues();
		}
		else if (keyChar == '*' || keyChar == '+') {
			helper.setNumberText();
			operatorFlags[(keyChar == '*') ? 1 : 3] = true;
		}
	}

	private void operatorKeyButtons(char keyChar) {

		switch (keyChar) {
		// actions for symbol buttons

		case VK_SLASH: // VK_SLASH indicates '/' or division
			helper.setNumberText();
			operatorFlags[0] = true;
			break;

		// KeyEvent.VK_PLUS DOESN'T WORK - it was left for reference to see what it
		// would look like in
		// KeyEvent code
		case VK_PLUS:
			helper.setNumberText();
			operatorFlags[3] = true;
			break;

		case VK_MINUS:
			helper.setNumberText();
			operatorFlags[2] = true;
			break;

		case VK_ENTER, VK_EQUALS:
			helper.performEnterOrEquals();
			break; // break statement for case enter key button

		case VK_BACK_SPACE:
			// if you backspace with only 1 digit in the input box, instead of displaying blank display '0'
			// or if you backspace with a negative single digit number (ex. -1) to make just '-' we want to prevent this and instead display '0'
			if (textFieldUserInput.getText().trim().length() == 1 || (textFieldUserInput.getText().trim().startsWith("-") && textFieldUserInput.getText().trim().length() == 2)) {
				textFieldUserInput.setText(CURSOR_RIGHT_POSITION_W_ZERO);
				break;
			}

			if(textFieldUserInput.getText().isBlank()) {
				textFieldUserInput.setText(CURSOR_RIGHT_POSITION_W_ZERO);
				break;
			}

			// otherwise remove the last digit of the current string in text field
			textFieldUserInput.setText(!CURSOR_RIGHT_POSITION.equals(textFieldUserInput.getText())
					? MyCalculator.df.format(Double.valueOf(textFieldUserInput.getText().substring(0, textFieldUserInput.getText().length() - 1).replace(",", "")))
					: CURSOR_RIGHT_POSITION_W_ZERO);
			break;

		case VK_PERIOD:
			// adds a decimal point
			if (!textFieldUserInput.getText().contains(".")) {
				textFieldUserInput.setText(textFieldUserInput.getText()
					+ ((!textFieldUserInput.getText().trim().isEmpty()) ? "." : "0."));
			}
			break;

		default:
			break;
		}
	}

	private void numberKeyButtons(char keyChar) {
		// bug fix: prevents user from prepending a zero before a number
		if ("0".equals(textFieldUserInput.getText().trim())) {
			textFieldUserInput.setText("");
		}

		if (textFieldUserInput.getText().trim().length() <= 29) {
			switch(keyChar) {
			case VK_0:
				textFieldUserInput.setText(MyCalculator.df.format(Double.valueOf(textFieldUserInput.getText().replace(",", "").concat("0"))));
				hasUserEnteredZero = true;
				break;

			case VK_1:
				textFieldUserInput.setText(MyCalculator.df.format(Double.valueOf(textFieldUserInput.getText().replace(",", "").concat("1"))));
				break;

			case VK_2:
				textFieldUserInput.setText(MyCalculator.df.format(Double.valueOf(textFieldUserInput.getText().replace(",", "").concat("2"))));
				break;

			case VK_3:
				textFieldUserInput.setText(MyCalculator.df.format(Double.valueOf(textFieldUserInput.getText().replace(",", "").concat("3"))));
				break;

			case VK_4:
				textFieldUserInput.setText(MyCalculator.df.format(Double.valueOf(textFieldUserInput.getText().replace(",", "").concat("4"))));
				break;

			case VK_5:
				textFieldUserInput.setText(MyCalculator.df.format(Double.valueOf(textFieldUserInput.getText().replace(",", "").concat("5"))));
				break;

			case VK_6:
				textFieldUserInput.setText(MyCalculator.df.format(Double.valueOf(textFieldUserInput.getText().replace(",", "").concat("6"))));
				break;

			case VK_7:
				textFieldUserInput.setText(MyCalculator.df.format(Double.valueOf(textFieldUserInput.getText().replace(",", "").concat("7"))));
				break;

			case VK_8:
				textFieldUserInput.setText(MyCalculator.df.format(Double.valueOf(textFieldUserInput.getText().replace(",", "").concat("8"))));
				break;

			case VK_9:
				textFieldUserInput.setText(MyCalculator.df.format(Double.valueOf(textFieldUserInput.getText().replace(",", "").concat("9"))));
				break;

			default:
				break;
			}
		}
	}
}
