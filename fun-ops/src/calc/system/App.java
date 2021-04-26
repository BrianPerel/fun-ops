package calc.system;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 * Application implementation for the UI. Builds and displays all GUI components
 * for the calculator. Note: the reason for all this back and forth casting is
 * because textField and all other Swing GUI components are type String class
 * <br>
 * 
 * @author Brian Perel
 * @created Dec 11, 2020
 */
public class App implements ActionListener {

	private JFrame frame;
	private JTextField textField;
	DecimalFormat df = new DecimalFormat("#0"); // for whole number rounding
	static boolean operatorFlags[] = new boolean[4]; // array to hold flags to be raised if a calculator operator is
														// clicked
	static String cursor_right_position_with_zero;
	static String cursor_right_position;

	MyCalculator system = new MyCalculator();

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

		char spaces[] = new char[29];
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

		JButton btnClearCE = new JButton("CE");
		btnClearCE.setBounds(110, 100, 80, 40);
		frame.getContentPane().add(btnClearCE);
		btnClearCE.addActionListener(this);

		JButton btnClearC = new JButton("C");
		btnClearC.setBounds(189, 100, 80, 40);
		frame.getContentPane().add(btnClearC);
		btnClearC.addActionListener(this);

		// backspace symbol
		JButton btnBackspace = new JButton("\u232B");
		btnBackspace.setBounds(268, 100, 80, 40);
		frame.getContentPane().add(btnBackspace);
		btnBackspace.addActionListener(this);

		JButton btnPercent = new JButton("%");
		btnPercent.setBounds(31, 100, 80, 40);
		frame.getContentPane().add(btnPercent);
		btnPercent.addActionListener(this);

		textField = new JTextField(cursor_right_position_with_zero);
		textField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textField.setBounds(178, 27, 170, 40);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		textField.setEditable(false);

		// Unicode for X^2
		JButton btnSquare = new JButton("x\u00B2");
		btnSquare.setBounds(110, 141, 80, 40);
		frame.getContentPane().add(btnSquare);
		btnSquare.addActionListener(this);

		// 2 square root x symbol
		JButton btnSquareRoot = new JButton("2\u221Ax");
		btnSquareRoot.setBounds(189, 141, 80, 40);
		frame.getContentPane().add(btnSquareRoot);
		btnSquareRoot.addActionListener(this);

		// division symbol
		JButton btnDivision = new JButton("\u00F7");
		btnDivision.setBounds(268, 141, 80, 40);
		frame.getContentPane().add(btnDivision);
		btnDivision.addActionListener(this);

		JButton btnNumberSeven = new JButton("7");
		btnNumberSeven.setBounds(31, 182, 80, 40);
		frame.getContentPane().add(btnNumberSeven);
		btnNumberSeven.addActionListener(this);

		JButton btnNumberEight = new JButton("8");
		btnNumberEight.setBounds(110, 182, 80, 40);
		frame.getContentPane().add(btnNumberEight);
		btnNumberEight.addActionListener(this);

		JButton btnNumberNine = new JButton("9");
		btnNumberNine.setBounds(189, 182, 80, 40);
		frame.getContentPane().add(btnNumberNine);
		btnNumberNine.addActionListener(this);

		JButton btnMultiply = new JButton("*");
		btnMultiply.setBounds(268, 182, 80, 40);
		frame.getContentPane().add(btnMultiply);
		btnMultiply.addActionListener(this);

		JButton btnNumberFour = new JButton("4");
		btnNumberFour.setBounds(31, 222, 80, 40);
		frame.getContentPane().add(btnNumberFour);
		btnNumberFour.addActionListener(this);

		JButton btnNumberFive = new JButton("5");
		btnNumberFive.setBounds(110, 222, 80, 40);
		frame.getContentPane().add(btnNumberFive);
		btnNumberFive.addActionListener(this);

		JButton btnNumberSix = new JButton("6");
		btnNumberSix.setBounds(189, 222, 80, 40);
		frame.getContentPane().add(btnNumberSix);
		btnNumberSix.addActionListener(this);

		JButton btnSubtract = new JButton("-");
		btnSubtract.setBounds(268, 222, 80, 40);
		frame.getContentPane().add(btnSubtract);
		btnSubtract.addActionListener(this);

		JButton btnNumberOne = new JButton("1");
		btnNumberOne.setBounds(31, 263, 80, 40);
		frame.getContentPane().add(btnNumberOne);
		btnNumberOne.addActionListener(this);

		JButton btnNumberTwo = new JButton("2");
		btnNumberTwo.setBounds(110, 263, 80, 40);
		frame.getContentPane().add(btnNumberTwo);
		btnNumberTwo.addActionListener(this);

		JButton btnNumberThree = new JButton("3");
		btnNumberThree.setBounds(189, 263, 80, 40);
		frame.getContentPane().add(btnNumberThree);
		btnNumberThree.addActionListener(this);

		JButton btnAdd = new JButton("+");
		btnAdd.setBounds(268, 263, 80, 40);
		frame.getContentPane().add(btnAdd);
		btnAdd.addActionListener(this);

		JButton btnNewButton_20 = new JButton("+/-");
		btnNewButton_20.setBounds(31, 300, 80, 40);
		frame.getContentPane().add(btnNewButton_20);
		btnNewButton_20.addActionListener(this);

		JButton btnNumberZero = new JButton("0");
		btnNumberZero.setBounds(110, 300, 80, 40);
		frame.getContentPane().add(btnNumberZero);
		btnNumberZero.addActionListener(this);

		JButton btnDecimalPoint = new JButton(".");
		btnDecimalPoint.setBounds(189, 300, 80, 40);
		frame.getContentPane().add(btnDecimalPoint);
		btnDecimalPoint.addActionListener(this);

		JButton btnEquals = new JButton("=");
		btnEquals.setBounds(268, 300, 80, 40);
		frame.getContentPane().add(btnEquals);
		btnEquals.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		String action = ae.getActionCommand();

		// remove auto display '0' value from main number entry textField box so that
		// '0' is not included in calculation
		// Since it's only needed for display purposes
		if (textField.getText().equals(cursor_right_position_with_zero)) {
			textField.setText(cursor_right_position);
		}

		// actions for numbers 0-9 buttons. No need for default case since all buttons
		// are utilized as a case
		switch (action) {
		case "0":
			textField.setText(textField.getText() + "0");
			break;

		case "1":
			textField.setText(textField.getText() + "1");
			break;

		case "2":
			textField.setText(textField.getText() + "2");
			break;

		case "3":
			textField.setText(textField.getText() + "3");
			break;

		case "4":
			textField.setText(textField.getText() + "4");
			break;

		case "5":
			textField.setText(textField.getText() + "5");
			break;

		case "6":
			textField.setText(textField.getText() + "6");
			break;

		case "7":
			textField.setText(textField.getText() + "7");
			break;

		case "8":
			textField.setText(textField.getText() + "8");
			break;

		case "9":
			textField.setText(textField.getText() + "9");
			break;

		// actions for symbol buttons
		case "CE":
			textField.setText(cursor_right_position_with_zero);
			Collections.fill(MyCalculator.nums, null);
			Collections.fill(MyCalculator.doubleNums, 0.0);
			Arrays.fill(operatorFlags, Boolean.FALSE);
			MyCalculator.nums.clear();
			MyCalculator.doubleNums.clear();
			MyCalculator.divideByZeroflag = false;
			MyCalculator.arrayNumsFilled = MyCalculator.arrayPosNum = 0;
			break;

		case "C":
			textField.setText(cursor_right_position_with_zero);
			Arrays.fill(operatorFlags, Boolean.FALSE);
			Collections.fill(MyCalculator.nums, null);
			Collections.fill(MyCalculator.doubleNums, 0.0);
			MyCalculator.nums.clear();
			MyCalculator.doubleNums.clear();
			MyCalculator.divideByZeroflag = false;
			MyCalculator.arrayNumsFilled = MyCalculator.arrayPosNum = 0;
			break;

		case "*":
			MyCalculator.setNumber(textField.getText());
			textField.setText(cursor_right_position);
			operatorFlags[1] = true;
			break;

		// division symbol
		case "\u00F7":
			MyCalculator.setNumber(textField.getText());
			textField.setText(cursor_right_position);
			operatorFlags[0] = true;
			break;

		case "+":
			MyCalculator.setNumber(textField.getText());
			textField.setText(cursor_right_position);
			operatorFlags[3] = true;
			break;

		case "-":
			MyCalculator.setNumber(textField.getText());
			textField.setText(cursor_right_position);
			operatorFlags[2] = true;
			break;

		// backspace symbol
		case "\u232B":
			textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
			break;

		case "1/x":
			// validate that the current text in textField isn't blank
			if (!textField.getText().equals(cursor_right_position)) {
				// need to cast below multiple times in order to perform 1/x operation
				textField.setText(cursor_right_position + Double.toString(1 / Double.valueOf(textField.getText())));
			}
			break;

		case "=":
			// if textField label is blank, then no action has been done.
			// Hence equal operation isn't performed
			if (!textField.getText().equals(cursor_right_position)) {
				MyCalculator.setNumber(textField.getText());

				// perform computation to make the value
				String value = system.compute();

				// if value is whole then don't display 0's after decimal; ex. instead of 25.00
				// display 25
				double v = Double.parseDouble(value);
				if ((v * 10) % 10 == 0) {
					value = df.format(v);
				}

				// check for division by zero. Avoid exception being flagged
				if (MyCalculator.divideByZeroflag) {
					textField.setText(" Cannot divide by zero");
				} else {
					textField.setText(cursor_right_position + value);
				}

				// re set all array values to 0
				Arrays.fill(operatorFlags, Boolean.FALSE);
				Collections.fill(MyCalculator.nums, "");
				Collections.fill(MyCalculator.doubleNums, 0.0);
				MyCalculator.nums.clear();
				MyCalculator.doubleNums.clear();
				MyCalculator.arrayNumsFilled = MyCalculator.arrayPosNum = 0;
			}
		}

		if (action.equals("%") && !textField.getText().equals(cursor_right_position)) {
			MyCalculator.setNumber(String.valueOf(system.percent(Double.parseDouble(textField.getText()))));
			textField.setText(textField.getText() + "%");
		} else if (action.equals("x\u00B2") && !textField.getText().equals(cursor_right_position)) {
			textField.setText(
					cursor_right_position + Double.toString(Math.pow((Double.valueOf(textField.getText())), 2)));
		}
		// 2 square root x symbol
		else if (action.equals("2\u221Ax") && !textField.getText().equals(cursor_right_position)) {
			textField.setText(cursor_right_position + Math.sqrt(Double.valueOf(textField.getText())));
		} else if (action.equals("+/-") && !textField.getText().equals(cursor_right_position)) {
			// if current number is a positive number, number becomes negative (minus is
			// prepended)
			if (!textField.getText().trim().substring(0, 1).equals("-")) {
				textField.setText(cursor_right_position + ("-" + textField.getText().trim()));
			}
			// if current number is a negative number, number becomes positive (minus is
			// removed)
			else if (textField.getText().trim().substring(0, 1).equals("-")) {
				textField.setText(cursor_right_position + textField.getText().replace("-", ""));
			}			
		} else if (action.equals(".") && !textField.getText().contains(".")) {
			textField.setText(textField.getText() + ".");
		}
	}
}
