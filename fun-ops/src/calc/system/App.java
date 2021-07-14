package calc.system;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
 * type String class <br>
 * 
 * @author Brian Perel
 * @created Dec 11, 2020
 */
public class App implements ActionListener, KeyListener {

	private JFrame frame;
	private JFormattedTextField inputTextField;
	DecimalFormat df = new DecimalFormat("#0"); // for whole number rounding
	static boolean operatorFlags[] = new boolean[4]; // array to hold flags to be raised if a calculator operator is
														// clicked

	char spaces[] = new char[29];
	static String cursor_right_position_with_zero;
	static String cursor_right_position;

	MyCalculator system = new MyCalculator();

	boolean zeroEnteredByUser;

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
		cursor_right_position = String.valueOf(spaces);
		cursor_right_position_with_zero = String.valueOf(spaces) + "0";

		frame = new JFrame();
		frame.setBounds(100, 100, 400, 436);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JButton btnTurnToFraction = new JButton("1/x");
		btnTurnToFraction.setBounds(31, 141, 80, 40);
		frame.getContentPane().add(btnTurnToFraction);
		btnTurnToFraction.addActionListener(this);
		btnTurnToFraction.addKeyListener(this);

		JButton btnClearCE = new JButton("CE");
		btnClearCE.setBounds(110, 100, 80, 40);
		frame.getContentPane().add(btnClearCE);
		btnClearCE.addActionListener(this);
		btnClearCE.addKeyListener(this);

		JButton btnClearC = new JButton("C");
		btnClearC.setBounds(189, 100, 80, 40);
		frame.getContentPane().add(btnClearC);
		btnClearC.addActionListener(this);
		btnClearC.addKeyListener(this);

		// backspace symbol
		JButton btnBackspace = new JButton("\u232B");
		btnBackspace.setBounds(268, 100, 80, 40);
		frame.getContentPane().add(btnBackspace);
		btnBackspace.addActionListener(this);
		btnBackspace.addKeyListener(this);

		JButton btnPercent = new JButton("%");
		btnPercent.setBounds(31, 100, 80, 40);
		frame.getContentPane().add(btnPercent);
		btnPercent.addActionListener(this);
		btnPercent.addKeyListener(this);

		inputTextField = new JFormattedTextField(cursor_right_position_with_zero);
		inputTextField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		inputTextField.setBounds(178, 27, 170, 40);
		frame.getContentPane().add(inputTextField);
		inputTextField.setColumns(10);
		inputTextField.setEditable(false);
		inputTextField.addKeyListener(this);

		// Unicode for X^2
		JButton btnSquare = new JButton("x\u00B2");
		btnSquare.setBounds(110, 141, 80, 40);
		frame.getContentPane().add(btnSquare);
		btnSquare.addActionListener(this);
		btnSquare.addKeyListener(this);

		// 2 square root x symbol
		JButton btnSquareRoot = new JButton("2\u221Ax");
		btnSquareRoot.setBounds(189, 141, 80, 40);
		frame.getContentPane().add(btnSquareRoot);
		btnSquareRoot.addActionListener(this);
		btnSquareRoot.addKeyListener(this);

		// division symbol
		JButton btnDivision = new JButton("\u00F7");
		btnDivision.setBounds(268, 141, 80, 40);
		frame.getContentPane().add(btnDivision);
		btnDivision.addActionListener(this);
		btnDivision.addKeyListener(this);

		JButton btnMultiply = new JButton("*");
		btnMultiply.setBounds(268, 182, 80, 40);
		frame.getContentPane().add(btnMultiply);
		btnMultiply.addActionListener(this);
		btnMultiply.addKeyListener(this);

		JButton btnNumberZero = new JButton("0");
		btnNumberZero.setBounds(110, 300, 80, 40);
		frame.getContentPane().add(btnNumberZero);
		btnNumberZero.addActionListener(this);
		btnNumberZero.addKeyListener(this);

		JButton btnNumberOne = new JButton("1");
		btnNumberOne.setBounds(31, 263, 80, 40);
		frame.getContentPane().add(btnNumberOne);
		btnNumberOne.addActionListener(this);
		btnNumberOne.addKeyListener(this);

		JButton btnNumberTwo = new JButton("2");
		btnNumberTwo.setBounds(110, 263, 80, 40);
		frame.getContentPane().add(btnNumberTwo);
		btnNumberTwo.addActionListener(this);
		btnNumberTwo.addKeyListener(this);

		JButton btnNumberThree = new JButton("3");
		btnNumberThree.setBounds(189, 263, 80, 40);
		frame.getContentPane().add(btnNumberThree);
		btnNumberThree.addActionListener(this);

		JButton btnNumberFour = new JButton("4");
		btnNumberFour.setBounds(31, 222, 80, 40);
		frame.getContentPane().add(btnNumberFour);
		btnNumberFour.addActionListener(this);
		btnNumberFour.addKeyListener(this);

		JButton btnNumberFive = new JButton("5");
		btnNumberFive.setBounds(110, 222, 80, 40);
		frame.getContentPane().add(btnNumberFive);
		btnNumberFive.addActionListener(this);
		btnNumberFive.addKeyListener(this);

		JButton btnNumberSix = new JButton("6");
		btnNumberSix.setBounds(189, 222, 80, 40);
		frame.getContentPane().add(btnNumberSix);
		btnNumberSix.addActionListener(this);
		btnNumberSix.addKeyListener(this);

		JButton btnNumberSeven = new JButton("7");
		btnNumberSeven.setBounds(31, 182, 80, 40);
		frame.getContentPane().add(btnNumberSeven);
		btnNumberSeven.addActionListener(this);
		btnNumberSeven.addKeyListener(this);

		JButton btnNumberEight = new JButton("8");
		btnNumberEight.setBounds(110, 182, 80, 40);
		frame.getContentPane().add(btnNumberEight);
		btnNumberEight.addActionListener(this);
		btnNumberEight.addKeyListener(this);

		JButton btnNumberNine = new JButton("9");
		btnNumberNine.setBounds(189, 182, 80, 40);
		frame.getContentPane().add(btnNumberNine);
		btnNumberNine.addActionListener(this);
		btnNumberNine.addKeyListener(this);

		JButton btnSubtract = new JButton("-");
		btnSubtract.setBounds(268, 222, 80, 40);
		frame.getContentPane().add(btnSubtract);
		btnSubtract.addActionListener(this);
		btnSubtract.addKeyListener(this);

		JButton btnAdd = new JButton("+");
		btnAdd.setBounds(268, 263, 80, 40);
		frame.getContentPane().add(btnAdd);
		btnAdd.addActionListener(this);
		btnAdd.addKeyListener(this);

		JButton btnPlusMinus = new JButton("+/-");
		btnPlusMinus.setBounds(31, 300, 80, 40);
		frame.getContentPane().add(btnPlusMinus);
		btnPlusMinus.addActionListener(this);
		btnPlusMinus.addKeyListener(this);

		JButton btnDecimalPoint = new JButton(".");
		btnDecimalPoint.setBounds(189, 300, 80, 40);
		frame.getContentPane().add(btnDecimalPoint);
		btnDecimalPoint.addActionListener(this);
		btnDecimalPoint.addKeyListener(this);

		JButton btnEquals = new JButton("=");
		btnEquals.setBounds(268, 300, 80, 40);
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
		if (inputTextField.getText().equals(cursor_right_position_with_zero) && !zeroEnteredByUser) {
			inputTextField.setText(cursor_right_position);
		}

		// actions for numbers 0-9 buttons. No need for default case since all buttons
		// are utilized as a case
		switch (action) {
		case "0":
			if (!inputTextField.getText().equals(cursor_right_position_with_zero)) {
				inputTextField.setText(inputTextField.getText() + "0");
				zeroEnteredByUser = true;
			}
			break;

		case "1":
			inputTextField.setText(inputTextField.getText() + "1");
			break;

		case "2":
			inputTextField.setText(inputTextField.getText() + "2");
			break;

		case "3":
			inputTextField.setText(inputTextField.getText() + "3");
			break;

		case "4":
			inputTextField.setText(inputTextField.getText() + "4");
			break;

		case "5":
			inputTextField.setText(inputTextField.getText() + "5");
			break;

		case "6":
			inputTextField.setText(inputTextField.getText() + "6");
			break;

		case "7":
			inputTextField.setText(inputTextField.getText() + "7");
			break;

		case "8":
			inputTextField.setText(inputTextField.getText() + "8");
			break;

		case "9":
			inputTextField.setText(inputTextField.getText() + "9");
			break;

		// actions for symbol buttons
		case "*":
			MyCalculator.setNumber(inputTextField.getText());
			inputTextField.setText(cursor_right_position);
			operatorFlags[1] = true;
			break;

		// division symbol
		case "\u00F7":
			MyCalculator.setNumber(inputTextField.getText());
			inputTextField.setText(cursor_right_position);
			operatorFlags[0] = true;
			break;

		case "+":
			MyCalculator.setNumber(inputTextField.getText());
			inputTextField.setText(cursor_right_position);
			operatorFlags[3] = true;
			break;

		case "-":
			MyCalculator.setNumber(inputTextField.getText());
			inputTextField.setText(cursor_right_position);
			operatorFlags[2] = true;
			break;

		// backspace symbol
		case "\u232B":
			inputTextField.setText(inputTextField.getText().substring(0, inputTextField.getText().length() - 1));
			break;

		case "1/x":
			// validate that the current text in textField isn't blank
			if (!inputTextField.getText().equals(cursor_right_position)) {
				// need to cast below multiple times in order to perform 1/x operation
				inputTextField
						.setText(cursor_right_position + Double.toString(1 / Double.valueOf(inputTextField.getText())));
			}
			break;

		case "=":
			// if textField label is blank, then no action has been done by user.
			// Hence in that scenario equal operation isn't performed
			if (!inputTextField.getText().equals(cursor_right_position)) {
				MyCalculator.setNumber(inputTextField.getText());

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
					inputTextField.setText(" Cannot divide by zero");
				} else {
					inputTextField.setText(cursor_right_position + value);
				}

				// re set all array values to 0
				Arrays.fill(operatorFlags, Boolean.FALSE);
				Collections.fill(MyCalculator.nums, "");
				Collections.fill(MyCalculator.doubleNums, 0.0);
				MyCalculator.nums.clear();
				MyCalculator.doubleNums.clear();
				MyCalculator.arrayNumsFilled = MyCalculator.arrayPosNum = 0;
				zeroEnteredByUser = false;
			}
			break; // break statement for case equals button
		} // end switch

		if (action.equals("CE") || action.equals("C")) {
			inputTextField.setText(cursor_right_position_with_zero);
			Arrays.fill(operatorFlags, Boolean.FALSE);
			zeroEnteredByUser = false;
			Collections.fill(MyCalculator.nums, null);
			Collections.fill(MyCalculator.doubleNums, 0.0);
			MyCalculator.nums.clear();
			MyCalculator.doubleNums.clear();
			MyCalculator.divideByZeroflag = false;
			MyCalculator.arrayNumsFilled = MyCalculator.arrayPosNum = 0;
		}

		if (action.equals("%") && !inputTextField.getText().equals(cursor_right_position)) {
			MyCalculator.setNumber(String.valueOf(system.percent(Double.parseDouble(inputTextField.getText()))));
			inputTextField.setText(inputTextField.getText() + "%");
			// x\u00B2 -> X^2 symbol
		} else if (action.equals("x\u00B2") && !inputTextField.getText().equals(cursor_right_position)) {
			inputTextField.setText(
					cursor_right_position + Double.toString(Math.pow((Double.valueOf(inputTextField.getText())), 2)));
		}
		// 2\u221Ax -> 2 square root x symbol
		else if (action.equals("2\u221Ax") && !inputTextField.getText().equals(cursor_right_position)) {
			inputTextField.setText(cursor_right_position + Math.sqrt(Double.valueOf(inputTextField.getText())));
		} else if (action.equals("+/-") && !inputTextField.getText().equals(cursor_right_position)) {
			// if current number is a positive number, number becomes negative (minus is
			// prepended)
			if (!inputTextField.getText().trim().substring(0, 1).equals("-")) {
				inputTextField.setText(cursor_right_position + ("-" + inputTextField.getText().trim()));
			}
			// if current number is a negative number, number becomes positive (minus is
			// removed)
			else if (inputTextField.getText().trim().substring(0, 1).equals("-")) {
				inputTextField.setText(cursor_right_position + inputTextField.getText().replace("-", ""));
			}
		} else if (action.equals(".") && !inputTextField.getText().contains(".")) {
			inputTextField.setText(inputTextField.getText() + ".");
		}
	}

	/**
	 * This is responsible for listening to all keyboard keys input
	 */
	@Override
	public void keyPressed(KeyEvent e) {

		// remove auto display '0' value from main number entry textField box so that
		// '0' is not included in calculation
		// Since it's only needed for display purposes
		if (inputTextField.getText().equals(cursor_right_position_with_zero) && !zeroEnteredByUser) {
			inputTextField.setText(cursor_right_position);
		}

		// actions for numbers 0-9 buttons. No need for default case since all buttons
		// are utilized as a case
		switch (e.getKeyChar()) {
		case KeyEvent.VK_0:
			inputTextField.setText(inputTextField.getText() + "0");
			zeroEnteredByUser = true;
			break;

		case KeyEvent.VK_1:
			inputTextField.setText(inputTextField.getText() + "1");
			break;

		case KeyEvent.VK_2:
			inputTextField.setText(inputTextField.getText() + "2");
			break;

		case KeyEvent.VK_3:
			inputTextField.setText(inputTextField.getText() + "3");
			break;

		case KeyEvent.VK_4:
			inputTextField.setText(inputTextField.getText() + "4");
			break;

		case KeyEvent.VK_5:
			inputTextField.setText(inputTextField.getText() + "5");
			break;

		case KeyEvent.VK_6:
			inputTextField.setText(inputTextField.getText() + "6");
			break;

		case KeyEvent.VK_7:
			inputTextField.setText(inputTextField.getText() + "7");
			break;

		case KeyEvent.VK_8:
			inputTextField.setText(inputTextField.getText() + "8");
			break;

		case KeyEvent.VK_9:
			inputTextField.setText(inputTextField.getText() + "9");
			break;

		// actions for symbol buttons
		case KeyEvent.VK_SLASH:
			MyCalculator.setNumber(inputTextField.getText());
			inputTextField.setText(cursor_right_position);
			operatorFlags[0] = true;
			break;

		// DOESN'T WORK
		case KeyEvent.VK_PLUS:
			MyCalculator.setNumber(inputTextField.getText());
			inputTextField.setText(cursor_right_position);
			operatorFlags[3] = true;
			break;

		case KeyEvent.VK_MINUS:
			MyCalculator.setNumber(inputTextField.getText());
			inputTextField.setText(cursor_right_position);
			operatorFlags[2] = true;
			break;

		case KeyEvent.VK_ENTER:
		case KeyEvent.VK_EQUALS:
			// if textField label is blank, then no action has been done by user.
			// Hence in that scenario equal operation isn't performed
			if (!inputTextField.getText().equals(cursor_right_position)) {
				MyCalculator.setNumber(inputTextField.getText());

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
					inputTextField.setText(" Cannot divide by zero");
				} else {
					inputTextField.setText(cursor_right_position + value);
				}

				// re set all array values to 0
				Arrays.fill(operatorFlags, Boolean.FALSE);
				Collections.fill(MyCalculator.nums, "");
				Collections.fill(MyCalculator.doubleNums, 0.0);
				MyCalculator.nums.clear();
				MyCalculator.doubleNums.clear();
				MyCalculator.arrayNumsFilled = MyCalculator.arrayPosNum = 0;
				zeroEnteredByUser = false;
			}
			break; // break statement for case enter key button

		case KeyEvent.VK_BACK_SPACE:
			inputTextField.setText(inputTextField.getText().substring(0, inputTextField.getText().length() - 1));
			break;
		}

		if (e.getKeyChar() == KeyEvent.VK_PERIOD && !inputTextField.getText().contains(".")) {
			inputTextField.setText(inputTextField.getText() + ".");
		}

		if (e.getKeyChar() == '*') {
			MyCalculator.setNumber(inputTextField.getText());
			inputTextField.setText(cursor_right_position);
			operatorFlags[1] = true;
		}

		if (e.getKeyChar() == '+') {
			MyCalculator.setNumber(inputTextField.getText());
			inputTextField.setText(cursor_right_position);
			operatorFlags[3] = true;
		}
	}

	/**
	 * Do nothing because method isn't needed
	 */
	@Override
	public void keyReleased(KeyEvent e) {
	}

	/**
	 * Do nothing because method isn't needed
	 */
	@Override
	public void keyTyped(KeyEvent e) {
	}
}
