package calc.system;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.text.MaskFormatter;

/**
 * Application implementation for the calculator's UI. Builds and displays all
 * GUI components for the calculator. Note: the reason for all this back and
 * forth casting is because textField and all other Swing GUI components are
 * type String class. I extended KeyAdapter abstract class instead of
 * KeyListener for the purpose of not having to hold override methods that I
 * won't be using <br>
 * 
 * @author Brian Perel
 * @created Dec 11, 2020
 */
public class App extends KeyAdapter implements ActionListener {

	private JFrame frame;
	private JFormattedTextField userInputTextField;
	DecimalFormat df = new DecimalFormat("#0"); // for whole number rounding
	static boolean operatorFlags[] = new boolean[4]; // array to hold flags to be raised if a calculator operator is
														// clicked
	char spaces[] = new char[29];
	static String cursorRightPositionedWithZero;
	static String cursorRightPositioned;
	MyCalculator system = new MyCalculator();
	boolean numberZeroEnteredByUser;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
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
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public App() {

		Arrays.fill(spaces, ' ');
		cursorRightPositioned = String.valueOf(spaces);
		cursorRightPositionedWithZero = String.valueOf(spaces) + "0";

		frame = new JFrame();
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		frame.setBounds(100, 100, 400, 436);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JButton btnTurnToFraction = new JButton("1/x");
		btnTurnToFraction.setBackground(Color.WHITE);
		btnTurnToFraction.setBounds(31, 141, 80, 40);
		frame.getContentPane().add(btnTurnToFraction);
		btnTurnToFraction.addActionListener(this);
		btnTurnToFraction.addKeyListener(this);

		JButton btnClearCE = new JButton("CE");
		btnClearCE.setBackground(Color.WHITE);
		btnClearCE.setBounds(110, 100, 80, 40);
		frame.getContentPane().add(btnClearCE);
		btnClearCE.addActionListener(this);
		btnClearCE.addKeyListener(this);

		JButton btnClearC = new JButton("C");
		btnClearC.setBackground(Color.WHITE);
		btnClearC.setBounds(189, 100, 80, 40);
		frame.getContentPane().add(btnClearC);
		btnClearC.addActionListener(this);
		btnClearC.addKeyListener(this);

		// backspace symbol
		JButton btnBackspace = new JButton("\u232B");
		btnBackspace.setBackground(Color.WHITE);
		btnBackspace.setBounds(268, 100, 80, 40);
		frame.getContentPane().add(btnBackspace);
		btnBackspace.addActionListener(this);
		btnBackspace.addKeyListener(this);

		JButton btnPercent = new JButton("%");
		btnPercent.setBackground(Color.WHITE);
		btnPercent.setBounds(31, 100, 80, 40);
		frame.getContentPane().add(btnPercent);
		btnPercent.addActionListener(this);
		btnPercent.addKeyListener(this);

		userInputTextField = new JFormattedTextField(cursorRightPositionedWithZero);
		userInputTextField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		userInputTextField.setBounds(178, 27, 170, 40);
		frame.getContentPane().add(userInputTextField);
		userInputTextField.setColumns(10);
		userInputTextField.setEditable(false);
		userInputTextField.addKeyListener(this);

		// Unicode for X^2
		JButton btnSquare = new JButton("x\u00B2");
		btnSquare.setBackground(Color.WHITE);
		btnSquare.setBounds(110, 141, 80, 40);
		frame.getContentPane().add(btnSquare);
		btnSquare.addActionListener(this);
		btnSquare.addKeyListener(this);

		// 2 square root x symbol
		JButton btnSquareRoot = new JButton("2\u221Ax");
		btnSquareRoot.setBackground(Color.WHITE);
		btnSquareRoot.setBounds(189, 141, 80, 40);
		frame.getContentPane().add(btnSquareRoot);
		btnSquareRoot.addActionListener(this);
		btnSquareRoot.addKeyListener(this);

		// division symbol
		JButton btnDivision = new JButton("\u00F7");
		btnDivision.setBackground(Color.WHITE);
		btnDivision.setBounds(268, 141, 80, 40);
		frame.getContentPane().add(btnDivision);
		btnDivision.addActionListener(this);
		btnDivision.addKeyListener(this);

		JButton btnMultiply = new JButton("*");
		btnMultiply.setBackground(Color.WHITE);
		btnMultiply.setBounds(268, 182, 80, 40);
		frame.getContentPane().add(btnMultiply);
		btnMultiply.addActionListener(this);
		btnMultiply.addKeyListener(this);

		JButton btnNumberZero = new JButton("0");
		btnNumberZero.setBackground(Color.WHITE);
		btnNumberZero.setBounds(110, 304, 80, 40);
		frame.getContentPane().add(btnNumberZero);
		btnNumberZero.addActionListener(this);
		btnNumberZero.addKeyListener(this);

		JButton btnNumberOne = new JButton("1");
		btnNumberOne.setBackground(Color.WHITE);
		btnNumberOne.setBounds(31, 263, 80, 40);
		frame.getContentPane().add(btnNumberOne);
		btnNumberOne.addActionListener(this);
		btnNumberOne.addKeyListener(this);

		JButton btnNumberTwo = new JButton("2");
		btnNumberTwo.setBackground(Color.WHITE);
		btnNumberTwo.setBounds(110, 263, 80, 40);
		frame.getContentPane().add(btnNumberTwo);
		btnNumberTwo.addActionListener(this);
		btnNumberTwo.addKeyListener(this);

		JButton btnNumberThree = new JButton("3");
		btnNumberThree.setBackground(Color.WHITE);
		btnNumberThree.setBounds(189, 263, 80, 40);
		frame.getContentPane().add(btnNumberThree);
		btnNumberThree.addActionListener(this);

		JButton btnNumberFour = new JButton("4");
		btnNumberFour.setBackground(Color.WHITE);
		btnNumberFour.setBounds(31, 222, 80, 40);
		frame.getContentPane().add(btnNumberFour);
		btnNumberFour.addActionListener(this);
		btnNumberFour.addKeyListener(this);

		JButton btnNumberFive = new JButton("5");
		btnNumberFive.setBackground(Color.WHITE);
		btnNumberFive.setBounds(110, 222, 80, 40);
		frame.getContentPane().add(btnNumberFive);
		btnNumberFive.addActionListener(this);
		btnNumberFive.addKeyListener(this);

		JButton btnNumberSix = new JButton("6");
		btnNumberSix.setBackground(Color.WHITE);
		btnNumberSix.setBounds(189, 222, 80, 40);
		frame.getContentPane().add(btnNumberSix);
		btnNumberSix.addActionListener(this);
		btnNumberSix.addKeyListener(this);

		JButton btnNumberSeven = new JButton("7");
		btnNumberSeven.setBackground(Color.WHITE);
		btnNumberSeven.setBounds(31, 182, 80, 40);
		frame.getContentPane().add(btnNumberSeven);
		btnNumberSeven.addActionListener(this);
		btnNumberSeven.addKeyListener(this);

		JButton btnNumberEight = new JButton("8");
		btnNumberEight.setBackground(Color.WHITE);
		btnNumberEight.setBounds(110, 182, 80, 40);
		frame.getContentPane().add(btnNumberEight);
		btnNumberEight.addActionListener(this);
		btnNumberEight.addKeyListener(this);

		JButton btnNumberNine = new JButton("9");
		btnNumberNine.setBackground(Color.WHITE);
		btnNumberNine.setBounds(189, 182, 80, 40);
		frame.getContentPane().add(btnNumberNine);
		btnNumberNine.addActionListener(this);
		btnNumberNine.addKeyListener(this);

		JButton btnSubtract = new JButton("-");
		btnSubtract.setBackground(Color.WHITE);
		btnSubtract.setBounds(268, 222, 80, 40);
		frame.getContentPane().add(btnSubtract);
		btnSubtract.addActionListener(this);
		btnSubtract.addKeyListener(this);

		JButton btnAdd = new JButton("+");
		btnAdd.setBackground(Color.WHITE);
		btnAdd.setBounds(268, 263, 80, 40);
		frame.getContentPane().add(btnAdd);
		btnAdd.addActionListener(this);
		btnAdd.addKeyListener(this);

		JButton btnPlusMinus = new JButton("+/-");
		btnPlusMinus.setBackground(Color.WHITE);
		btnPlusMinus.setBounds(31, 304, 80, 40);
		frame.getContentPane().add(btnPlusMinus);
		btnPlusMinus.addActionListener(this);
		btnPlusMinus.addKeyListener(this);

		JButton btnDecimalPoint = new JButton(".");
		btnDecimalPoint.setBackground(Color.WHITE);
		btnDecimalPoint.setBounds(189, 304, 80, 40);
		frame.getContentPane().add(btnDecimalPoint);
		btnDecimalPoint.addActionListener(this);
		btnDecimalPoint.addKeyListener(this);

		JButton btnEquals = new JButton("=");
		btnEquals.setBackground(Color.WHITE);
		btnEquals.setBounds(268, 304, 80, 40);
		frame.getContentPane().add(btnEquals);
		btnEquals.addActionListener(this);
		btnEquals.addKeyListener(this);
	}

	/**
	 * MaskFormatter is used to specify/create the format of a text field
	 * 
	 * @param s is the text field's user input area (the box)
	 * @return the formatted text field
	 */
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
	 * This is responsible for listening to when buttons are clicked
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		String action = ae.getActionCommand();

		// remove auto display '0' value from main number entry textField box so that
		// '0' is not included in calculation
		// Since it's only needed for display purposes
		if (userInputTextField.getText().equals(cursorRightPositionedWithZero) && !numberZeroEnteredByUser) {
			userInputTextField.setText(cursorRightPositioned);
		}

		// actions for numbers 0-9 buttons. No need for default case since all buttons
		// are utilized as a case
		switch (action) {
		case "0":
			if (!userInputTextField.getText().equals(cursorRightPositionedWithZero)) {
				userInputTextField.setText(userInputTextField.getText() + "0");
				numberZeroEnteredByUser = true;
			}
			break;

		case "1":
			userInputTextField.setText(userInputTextField.getText() + "1");
			break;

		case "2":
			userInputTextField.setText(userInputTextField.getText() + "2");
			break;

		case "3":
			userInputTextField.setText(userInputTextField.getText() + "3");
			break;

		case "4":
			userInputTextField.setText(userInputTextField.getText() + "4");
			break;

		case "5":
			userInputTextField.setText(userInputTextField.getText() + "5");
			break;

		case "6":
			userInputTextField.setText(userInputTextField.getText() + "6");
			break;

		case "7":
			userInputTextField.setText(userInputTextField.getText() + "7");
			break;

		case "8":
			userInputTextField.setText(userInputTextField.getText() + "8");
			break;

		case "9":
			userInputTextField.setText(userInputTextField.getText() + "9");
			break;

		// actions for symbol buttons
		case "*":
			MyCalculator.setNumber(userInputTextField.getText());
			userInputTextField.setText(cursorRightPositioned);
			operatorFlags[1] = true;
			break;

		// division symbol
		case "\u00F7":
			MyCalculator.setNumber(userInputTextField.getText());
			userInputTextField.setText(cursorRightPositioned);
			operatorFlags[0] = true;
			break;

		case "+":
			MyCalculator.setNumber(userInputTextField.getText());
			userInputTextField.setText(cursorRightPositioned);
			operatorFlags[3] = true;
			break;

		case "-":
			MyCalculator.setNumber(userInputTextField.getText());
			userInputTextField.setText(cursorRightPositioned);
			operatorFlags[2] = true;
			break;

		// backspace symbol
		case "\u232B":
			userInputTextField.setText(userInputTextField.getText().substring(0, userInputTextField.getText().length() - 1));
			break;

		case "1/x":
			// validate that the current text in textField isn't blank
			if (!userInputTextField.getText().equals(cursorRightPositioned)) {
				// need to cast below multiple times in order to perform 1/x operation
				userInputTextField
						.setText(cursorRightPositioned + Double.toString(1 / Double.valueOf(userInputTextField.getText())));
			}
			break;

		case "=":
			// if textField label is blank, then no action has been done by user.
			// Hence in that scenario equal operation isn't performed
			if (!userInputTextField.getText().equals(cursorRightPositioned)) {
				MyCalculator.setNumber(userInputTextField.getText());

				// perform computation to make the value
				String value = system.compute();

				// if value is whole then don't display 0's after decimal; ex. instead of 25.00
				// display 25
				double v = Double.parseDouble(value);
				if ((v * 10) % 10 == 0) { // if value calculated is whole number
					value = df.format(v); // removes zero's after decimal point
				}

				// check for division by zero. Avoid exception being flagged
				if (MyCalculator.divideByZeroflag) {
					userInputTextField.setText(" Cannot divide by zero");
				} else {
					userInputTextField.setText(cursorRightPositioned + value);
				}

				// re set all array values to 0
				Arrays.fill(operatorFlags, Boolean.FALSE);
				Collections.fill(MyCalculator.nums, "");
				Collections.fill(MyCalculator.doubleNums, 0.0);
				MyCalculator.nums.clear();
				MyCalculator.doubleNums.clear();
				MyCalculator.arrayNumsFilled = MyCalculator.arrayPositionNumber = 0;
				numberZeroEnteredByUser = false;
			}
			break; // break statement for case equals button

		case "CE":
		case "C":
			userInputTextField.setText(cursorRightPositionedWithZero);
			Arrays.fill(operatorFlags, Boolean.FALSE);
			numberZeroEnteredByUser = false;
			Collections.fill(MyCalculator.nums, null);
			Collections.fill(MyCalculator.doubleNums, 0.0);
			MyCalculator.nums.clear();
			MyCalculator.doubleNums.clear();
			MyCalculator.divideByZeroflag = false;
			MyCalculator.arrayNumsFilled = MyCalculator.arrayPositionNumber = 0;
			break;

		case ".":
			if (!userInputTextField.getText().contains(".")) {
				userInputTextField.setText(userInputTextField.getText() + ".");
			}
			break;

		case "%":
			if (!userInputTextField.getText().equals(cursorRightPositioned)) {
				MyCalculator.setNumber(String.valueOf(system.percent(Double.parseDouble(userInputTextField.getText()))));
				userInputTextField.setText(userInputTextField.getText() + "%");
				// x\u00B2 -> X^2 symbol
			}
			break;

		case "x\u00B2":
			if (!userInputTextField.getText().equals(cursorRightPositioned)) {
				userInputTextField.setText(cursorRightPositioned
						+ Double.toString(Math.pow((Double.valueOf(userInputTextField.getText())), 2)));
			}
			break;

		// 2\u221Ax -> 2 square root x symbol
		case "2\u221Ax":
			if (!userInputTextField.getText().equals(cursorRightPositioned)) {
				userInputTextField.setText(cursorRightPositioned + Math.sqrt(Double.valueOf(userInputTextField.getText())));
			}
			break;

		case "+/-":
			if (!userInputTextField.getText().equals(cursorRightPositioned)) {
				// if current number is a positive number, number becomes negative (minus is
				// prepended)
				if (!userInputTextField.getText().trim().substring(0, 1).equals("-")) {
					userInputTextField.setText(cursorRightPositioned + ("-" + userInputTextField.getText().trim()));
				}
				// if current number is a negative number, number becomes positive (minus is
				// removed)
				else if (userInputTextField.getText().trim().substring(0, 1).equals("-")) {
					userInputTextField.setText(cursorRightPositioned + userInputTextField.getText().replace("-", ""));
				}
			}
			break;
		} // end switch
	}

	/**
	 * This is responsible for listening to all keyboard keys input
	 */
	@Override
	public void keyPressed(KeyEvent e) {

		// remove auto display '0' value from main number entry textField box so that
		// '0' is not included in calculation
		// Since it's only needed for display purposes
		if (userInputTextField.getText().equals(cursorRightPositionedWithZero) && !numberZeroEnteredByUser) {
			userInputTextField.setText(cursorRightPositioned);
		}

		// actions for numbers 0-9 buttons. No need for default case since all buttons
		// are utilized as a case
		switch (e.getKeyChar()) {
		case KeyEvent.VK_0:
			userInputTextField.setText(userInputTextField.getText() + "0");
			numberZeroEnteredByUser = true;
			break;

		case KeyEvent.VK_1:
			userInputTextField.setText(userInputTextField.getText() + "1");
			break;

		case KeyEvent.VK_2:
			userInputTextField.setText(userInputTextField.getText() + "2");
			break;

		case KeyEvent.VK_3:
			userInputTextField.setText(userInputTextField.getText() + "3");
			break;

		case KeyEvent.VK_4:
			userInputTextField.setText(userInputTextField.getText() + "4");
			break;

		case KeyEvent.VK_5:
			userInputTextField.setText(userInputTextField.getText() + "5");
			break;

		case KeyEvent.VK_6:
			userInputTextField.setText(userInputTextField.getText() + "6");
			break;

		case KeyEvent.VK_7:
			userInputTextField.setText(userInputTextField.getText() + "7");
			break;

		case KeyEvent.VK_8:
			userInputTextField.setText(userInputTextField.getText() + "8");
			break;

		case KeyEvent.VK_9:
			userInputTextField.setText(userInputTextField.getText() + "9");
			break;

		// actions for symbol buttons
		case KeyEvent.VK_SLASH:
			MyCalculator.setNumber(userInputTextField.getText());
			userInputTextField.setText(cursorRightPositioned);
			operatorFlags[0] = true;
			break;

		// DOESN'T WORK - left just for reference to see what it would look like in
		// KeyEvent code
		case KeyEvent.VK_PLUS:
			MyCalculator.setNumber(userInputTextField.getText());
			userInputTextField.setText(cursorRightPositioned);
			operatorFlags[3] = true;
			break;

		case KeyEvent.VK_MINUS:
			MyCalculator.setNumber(userInputTextField.getText());
			userInputTextField.setText(cursorRightPositioned);
			operatorFlags[2] = true;
			break;

		case KeyEvent.VK_ENTER:
		case KeyEvent.VK_EQUALS:
			// if textField label is blank, then no action has been done by user.
			// Hence in that scenario equal operation isn't performed
			if (!userInputTextField.getText().equals(cursorRightPositioned)) {
				MyCalculator.setNumber(userInputTextField.getText());

				// perform computation to make the value
				String value = system.compute();

				// if value is whole then don't display 0's after decimal; ex. instead of 25.00
				// display 25
				double v = Double.parseDouble(value);
				if ((v * 10) % 10 == 0) { // if value calculated is whole number
					value = df.format(v); // removes zero's after decimal point
				}

				// check for division by zero. Avoid exception being flagged
				if (MyCalculator.divideByZeroflag) {
					userInputTextField.setText(" Cannot divide by zero");
				} else {
					userInputTextField.setText(cursorRightPositioned + value);
				}

				// reset all array values to 0
				Arrays.fill(operatorFlags, Boolean.FALSE);
				Collections.fill(MyCalculator.nums, "");
				Collections.fill(MyCalculator.doubleNums, 0.0);
				MyCalculator.nums.clear();
				MyCalculator.doubleNums.clear();
				MyCalculator.arrayNumsFilled = MyCalculator.arrayPositionNumber = 0;
				numberZeroEnteredByUser = false;
			}
			break; // break statement for case enter key button

		case KeyEvent.VK_BACK_SPACE:
			userInputTextField.setText(userInputTextField.getText().substring(0, userInputTextField.getText().length() - 1));
			break;

		case KeyEvent.VK_PERIOD:
			if (!userInputTextField.getText().contains(".")) {
				userInputTextField.setText(userInputTextField.getText() + ".");
			}
			break;
		}

		/**
		 * use e.getKeyChar() here for "*", "+", "C" because KeyEvent.VK_MULTIPLY,
		 * KeyEvent.VK_ADD, and KeyEvent.C don't work
		 */
		
		if(Character.toUpperCase(e.getKeyChar()) == 'C') { 
			userInputTextField.setText(cursorRightPositionedWithZero);
			Arrays.fill(operatorFlags, Boolean.FALSE);
			numberZeroEnteredByUser = false;
			Collections.fill(MyCalculator.nums, null);
			Collections.fill(MyCalculator.doubleNums, 0.0);
			MyCalculator.nums.clear();
			MyCalculator.doubleNums.clear();
			MyCalculator.divideByZeroflag = false;
			MyCalculator.arrayNumsFilled = MyCalculator.arrayPositionNumber = 0;
		}

		if (e.getKeyChar() == '*' || e.getKeyChar() == '+') {
			MyCalculator.setNumber(userInputTextField.getText());
			userInputTextField.setText(cursorRightPositioned);

			if(e.getKeyChar() == '*') {
				operatorFlags[1] = true;
			} else {
				operatorFlags[3] = true;
			}
		}
	}
}
