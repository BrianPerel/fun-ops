package calculator;

import static java.awt.Color.WHITE;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
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

	private JFormattedTextField textFieldUserInput;
	private static final String CURSOR_RIGHT_POSITION = String.format("%31s", "");
	private static final String CURSOR_RIGHT_POSITION_W_ZERO = CURSOR_RIGHT_POSITION + "0";
	private static final DecimalFormat df = new DecimalFormat("#0"); // for whole number rounding
	protected static boolean[] operatorFlags = new boolean[4]; // array to hold flags to be raised if a calculator operator is
														// clicked
	private boolean hasUserEnteredZero;
	private static final Color SUPER_LIGHT_GRAY = new Color(225, 225, 225);
	
	public static void main(String[] args) {			
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		new MyCalculatorGui();
	}

	/**
	 * Places all the buttons on the app's board and initializes the contents of the frame, building the gui. 
	 */
	public MyCalculatorGui() {
		JFrame frame = new JFrame("Calculator App by: Brian Perel");
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		frame.setSize(400, 436);
		
		textFieldUserInput = new JFormattedTextField(CURSOR_RIGHT_POSITION_W_ZERO);
		textFieldUserInput.setHorizontalAlignment(SwingConstants.RIGHT);
		textFieldUserInput.setFont(new Font("Bookman Old Style", Font.PLAIN, 16));
		textFieldUserInput.setBounds(33, 27, 315, 40);
		textFieldUserInput.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3));
		frame.getContentPane().add(textFieldUserInput);
		textFieldUserInput.setColumns(10);
		textFieldUserInput.setEditable(false);
		textFieldUserInput.addKeyListener(this);

		JButton[] buttons = new JButton[25];
		buttons[0] = new JButton("1/x");
		buttons[1] = new JButton("CE");
		buttons[2] = new JButton("C");
		buttons[3] = new JButton("\u232B"); // unicode for backspace symbol = \u232B
		buttons[4] = new JButton("%");
		buttons[5] = new JButton("x\u00B2"); // unicode for X^2 (x squared) = x\u00B2
		buttons[6] = new JButton("2\u221Ax"); // unicode for 2 square root x symbol = 2\u221Ax
		buttons[7] = new JButton("\u00F7"); // unicode for division symbol = \u00F7
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
		buttons[24] = new JButton(); // fixes an issue caused when commenting out 		frame.getContentPane().setLayout(null);
		
		for (JButton button : buttons) {
			button.setFont(new Font("Bookman Old Style", Font.BOLD + Font.ITALIC, 13));
			button.setBackground(WHITE);
			frame.getContentPane().add(button);
			button.addActionListener(this);
			button.addKeyListener(this);
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
		}
		
		buttons[3].setFont(new Font("SansSerif", Font.BOLD, 12)); // need to custom add default font details for the backspace button, as the font details applied from above prevent the backspace symbol from being displayed

		buttons[0].setBounds(31, 141, 80, 40);
		buttons[1].setBounds(110, 100, 80, 40);
		buttons[2].setBounds(189, 100, 80, 40);
		buttons[3].setBounds(268, 100, 80, 40);
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
		
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

	/**
	 * This is responsible for listening to when buttons are clicked via the mouse (which represent actions).
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {		
		// remove the auto display '0' value from the main number entry textField box so that
		// this '0' is not included in calculations. Since it's only needed for display purposes
		if (textFieldUserInput.getText().equals(CURSOR_RIGHT_POSITION_W_ZERO) && !hasUserEnteredZero) {
			textFieldUserInput.setText(CURSOR_RIGHT_POSITION);
		}

		// actions for numbers 0-9 buttons and all calculator operators/symbol buttons
		switch (ae.getActionCommand()) {
		case "0":
			// if 0 button is clicked and the main number entry textField box doesn't have the auto display 0
			if (!textFieldUserInput.getText().equals(CURSOR_RIGHT_POSITION_W_ZERO)) {
				textFieldUserInput.setText(textFieldUserInput.getText().concat("0"));
				hasUserEnteredZero = true;
			}
			break;

		case "1":
			textFieldUserInput.setText(textFieldUserInput.getText().concat("1"));
			break;

		case "2":
			textFieldUserInput.setText(textFieldUserInput.getText().concat("2"));
			break;

		case "3":
			textFieldUserInput.setText(textFieldUserInput.getText().concat("3"));
			break;

		case "4":
			textFieldUserInput.setText(textFieldUserInput.getText().concat("4"));
			break;

		case "5":
			textFieldUserInput.setText(textFieldUserInput.getText().concat("5"));
			break;

		case "6":
			textFieldUserInput.setText(textFieldUserInput.getText().concat("6"));
			break;

		case "7":
			textFieldUserInput.setText(textFieldUserInput.getText().concat("7"));
			break;

		case "8":
			textFieldUserInput.setText(textFieldUserInput.getText().concat("8"));
			break;

		case "9":
			textFieldUserInput.setText(textFieldUserInput.getText().concat("9"));
			break;

		// actions for symbol buttons
		case "*":
			MyCalculator.setNumber(textFieldUserInput.getText());
			textFieldUserInput.setText(CURSOR_RIGHT_POSITION);
			operatorFlags[1] = true;
			break;

		// division symbol
		case "\u00F7":
			MyCalculator.setNumber(textFieldUserInput.getText());
			textFieldUserInput.setText(CURSOR_RIGHT_POSITION);
			operatorFlags[0] = true;
			break;

		case "+":
			MyCalculator.setNumber(textFieldUserInput.getText());
			textFieldUserInput.setText(CURSOR_RIGHT_POSITION);
			operatorFlags[3] = true;
			break;

		case "-":
			MyCalculator.setNumber(textFieldUserInput.getText());
			textFieldUserInput.setText(CURSOR_RIGHT_POSITION);
			operatorFlags[2] = true;
			break;

		// backspace symbol
		case "\u232B":
			// if you backspace with only 1 digit in the input box, instead of displaying blank display '0'
			if (textFieldUserInput.getText().trim().length() == 1) {
				textFieldUserInput.setText(CURSOR_RIGHT_POSITION_W_ZERO);
				break;
			}
			
			// otherwise remove the last digit of the current string in text field
			textFieldUserInput.setText(!textFieldUserInput.getText().equals(CURSOR_RIGHT_POSITION)
					? textFieldUserInput.getText().substring(0, textFieldUserInput.getText().length() - 1)
					: CURSOR_RIGHT_POSITION_W_ZERO);
			break;

		case "1/x":		
			if(textFieldUserInput.getText().trim().equals("0")) {
				double value = 1 / Double.valueOf(textFieldUserInput.getText());
				
				// validate that the current text in textField isn't blank
				textFieldUserInput.setText(!textFieldUserInput.getText().equals(CURSOR_RIGHT_POSITION)
						// need to cast below multiple times in order to perform 1/x operation
						? CURSOR_RIGHT_POSITION.concat(Double.toString(value))
						: CURSOR_RIGHT_POSITION_W_ZERO);
				
				// if value is whole then don't display 0's after decimal; ex. instead of 25.00 display 25
				if ((value * 10) % 10 == 0) { // if value calculated is whole number
					textFieldUserInput.setText(df.format(value)); // removes zero's after decimal point
				} 
			}
			else {
				textFieldUserInput.setText(CURSOR_RIGHT_POSITION_W_ZERO);
			}
			
			break;

		case "=":
			performEnterOrEquals();
			break;

		case "CE", "C":
			textFieldUserInput.setText(CURSOR_RIGHT_POSITION_W_ZERO);
			MyCalculator.divideByZeroflag = false;
			resetValues();
			break;

		case ".":
			if (!textFieldUserInput.getText().contains(".")) {
				textFieldUserInput.setText(textFieldUserInput.getText().concat("."));
			}
			break;

		case "%":
			if (!textFieldUserInput.getText().equals(CURSOR_RIGHT_POSITION)) {
				MyCalculator.setNumber(String.valueOf(MyCalculator.calculatePercentage(Double.parseDouble(textFieldUserInput.getText()))));
				textFieldUserInput.setText(textFieldUserInput.getText().concat("%"));
					
				break;
			}
				
			textFieldUserInput.setText(CURSOR_RIGHT_POSITION_W_ZERO);
			break;

		// x\u00B2 -> X^2 symbol
		case "x\u00B2":		
			if(textFieldUserInput.getText().trim().equals("0")) {
				double valueSquared = Math.pow(Double.valueOf(textFieldUserInput.getText()), 2);
				
				textFieldUserInput.setText(!textFieldUserInput.getText().equals(CURSOR_RIGHT_POSITION)
						? CURSOR_RIGHT_POSITION.concat(Double.toString(valueSquared))
						: CURSOR_RIGHT_POSITION_W_ZERO);	
				
				// if value is whole then don't display 0's after decimal; ex. instead of 25.00 display 25
				if ((valueSquared * 10) % 10 == 0) { // if value calculated is whole number
					textFieldUserInput.setText(df.format(valueSquared)); // removes zero's after decimal point
				}
			}
			else {
				textFieldUserInput.setText(CURSOR_RIGHT_POSITION_W_ZERO);
			}
			
			break;

		// 2\u221Ax -> 2 square root x symbol
		case "2\u221Ax":	
			if(textFieldUserInput.getText().trim().equals("0")) {
				double valueSquareRooted = Math.sqrt(Double.valueOf(textFieldUserInput.getText()));
				
				textFieldUserInput.setText(!textFieldUserInput.getText().equals(CURSOR_RIGHT_POSITION)
						? CURSOR_RIGHT_POSITION.concat(String.valueOf(valueSquareRooted))
						: CURSOR_RIGHT_POSITION_W_ZERO);
				
				// if value is whole then don't display 0's after decimal; ex. instead of 25.00 display 25
				if ((valueSquareRooted * 10) % 10 == 0) { // if value calculated is whole number
					textFieldUserInput.setText(df.format(valueSquareRooted)); // removes zero's after decimal point
				}
			}
			else {
				textFieldUserInput.setText(CURSOR_RIGHT_POSITION_W_ZERO);
			}
			

			break;

		case "+/-":
			if (!textFieldUserInput.getText().equals(CURSOR_RIGHT_POSITION)) {
				// if current number is positive, number becomes negative (minus is
				// prepended) else if number is negative, number becomes positive (minues is
				// removed)
				textFieldUserInput.setText(textFieldUserInput.getText().trim().startsWith("-")
						? CURSOR_RIGHT_POSITION.concat(textFieldUserInput.getText().replace("-", ""))
						: CURSOR_RIGHT_POSITION.concat("-".concat(textFieldUserInput.getText().trim())));
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
		char keyChar = e.getKeyChar();

		// remove auto display '0' value from main number entry textField box so that
		// '0' is not included in calculation
		// Since it's only needed for display purposes
		if (textFieldUserInput.getText().equals(CURSOR_RIGHT_POSITION_W_ZERO) && !hasUserEnteredZero) {
			textFieldUserInput.setText(CURSOR_RIGHT_POSITION);
		}

		// actions for numbers 0-9 buttons. No need for default case since all buttons
		// are utilized as a case
		switch (keyChar) {
			case KeyEvent.VK_0: 
				textFieldUserInput.setText(textFieldUserInput.getText().concat("0"));
				hasUserEnteredZero = true;
				break;
	
			case KeyEvent.VK_1:
				textFieldUserInput.setText(textFieldUserInput.getText().concat("1"));
				break;
	
			case KeyEvent.VK_2:
				textFieldUserInput.setText(textFieldUserInput.getText().concat("2"));
				break;
	
			case KeyEvent.VK_3:
				textFieldUserInput.setText(textFieldUserInput.getText().concat("3"));
				break;
	
			case KeyEvent.VK_4:
				textFieldUserInput.setText(textFieldUserInput.getText().concat("4"));
				break;
	
			case KeyEvent.VK_5:
				textFieldUserInput.setText(textFieldUserInput.getText().concat("5"));
				break;
	
			case KeyEvent.VK_6:
				textFieldUserInput.setText(textFieldUserInput.getText().concat("6"));
				break;
	
			case KeyEvent.VK_7:
				textFieldUserInput.setText(textFieldUserInput.getText().concat("7"));
				break;
	
			case KeyEvent.VK_8:
				textFieldUserInput.setText(textFieldUserInput.getText().concat("8"));
				break;
	
			case KeyEvent.VK_9:
				textFieldUserInput.setText(textFieldUserInput.getText().concat("9"));
				break;
	
			// actions for symbol buttons
				
			case KeyEvent.VK_SLASH: // VK_SLASH indicates '/' or division
				MyCalculator.setNumber(textFieldUserInput.getText());
				textFieldUserInput.setText(CURSOR_RIGHT_POSITION);
				operatorFlags[0] = true;
				break;
	
			// KeyEvent.VK_PLUS DOESN'T WORK - it was left for reference to see what it
			// would look like in
			// KeyEvent code
			case KeyEvent.VK_PLUS:
				MyCalculator.setNumber(textFieldUserInput.getText());
				textFieldUserInput.setText(CURSOR_RIGHT_POSITION);
				operatorFlags[3] = true;
				break;
	
			case KeyEvent.VK_MINUS:
				MyCalculator.setNumber(textFieldUserInput.getText());
				textFieldUserInput.setText(CURSOR_RIGHT_POSITION);
				operatorFlags[2] = true;
				break;
	
			case KeyEvent.VK_ENTER, KeyEvent.VK_EQUALS:
				performEnterOrEquals();
				break; // break statement for case enter key button
	
			case KeyEvent.VK_BACK_SPACE:				
				// if you backspace with only 1 digit in the input box, instead of displaying blank display '0'
				if (textFieldUserInput.getText().trim().length() == 1) {
					textFieldUserInput.setText(CURSOR_RIGHT_POSITION_W_ZERO);
					break;
				}
				
				// otherwise remove the last digit of the current string in text field
				textFieldUserInput.setText(!textFieldUserInput.getText().equals(CURSOR_RIGHT_POSITION)
						? textFieldUserInput.getText().substring(0, textFieldUserInput.getText().length() - 1)
						: CURSOR_RIGHT_POSITION_W_ZERO);
				break;
	
			case KeyEvent.VK_PERIOD:
				// adds a decimal point
				if (!textFieldUserInput.getText().contains(".")) {
					textFieldUserInput.setText(textFieldUserInput.getText() + ".");
				}
				break;
	
			default:
				textFieldUserInput.setText(CURSOR_RIGHT_POSITION_W_ZERO);
				break;
		}

		/**
		 * use e.getKeyChar() here for "*", "+", "C" because KeyEvent.VK_MULTIPLY,
		 * KeyEvent.VK_ADD, and KeyEvent.C don't work.
		 */

		if (Character.toUpperCase(keyChar) == 'C') {
			textFieldUserInput.setText(CURSOR_RIGHT_POSITION_W_ZERO);
			resetValues();
		}

		if (keyChar == '*' || keyChar == '+') {
			MyCalculator.setNumber(textFieldUserInput.getText());
			textFieldUserInput.setText(CURSOR_RIGHT_POSITION);

			if (keyChar == '*') {
				operatorFlags[1] = true;
			} 
			else {
				operatorFlags[3] = true;
			}
		}
	}

	/**
	 * Resets all calculator's arrays/memory.
	 */
	public void resetValues() {
		Arrays.fill(operatorFlags, false);
		hasUserEnteredZero = false;
		MyCalculator.stringNumbers.clear();
		MyCalculator.bigDecimalNumbers.clear();
	}
	
	/**
	 * Performs actions responsible for when the calculator's equals button is hit
	 */
	public void performEnterOrEquals() {
		// if textField label is blank, then no action has been done by user.
		// Hence in that scenario equal operation isn't performed
		if (!textFieldUserInput.getText().equals(CURSOR_RIGHT_POSITION)) {
			MyCalculator.setNumber(textFieldUserInput.getText());

			// perform computation to make and get the value
			String value = MyCalculator.compute();

			// if value is whole then don't display 0's after decimal; ex. instead of 25.00
			// display 25
			double v = Double.parseDouble(value);
			if ((v * 10) % 10 == 0) { // if value calculated is whole number
				value = df.format(v); // removes zero's after decimal point
			}

			// check for division by zero. Avoids exception being flagged
			textFieldUserInput.setText(
					(MyCalculator.divideByZeroflag) ? "Cannot divide by zero" : CURSOR_RIGHT_POSITION.concat(value));

			resetValues(); // reset all array/memory values
			
		} 
		else if (textFieldUserInput.getText().equals(CURSOR_RIGHT_POSITION)) {
			textFieldUserInput.setText(CURSOR_RIGHT_POSITION_W_ZERO);
		} 
	}
}
