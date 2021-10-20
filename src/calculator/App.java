package calculator;

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
	static String cursorRightPositionedWithZero, cursorRightPositioned;
	private JFormattedTextField userInputTextField;
	DecimalFormat df = new DecimalFormat("#0"); // for whole number rounding
	static boolean[] operatorFlags = new boolean[4]; // array to hold flags to be raised if a calculator operator is
														// clicked
	char[] spaces = new char[29];
	static boolean numberZeroEnteredByUser;

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
	 * Constructor: Initializes the contents of the frame, building the gui.
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
		btnTurnToFraction.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnTurnToFraction.setBackground(new Color(225, 225, 225));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnTurnToFraction.setBackground(Color.WHITE);
			}
		});

		JButton btnClearCE = new JButton("CE");
		btnClearCE.setBackground(Color.WHITE);
		btnClearCE.setBounds(110, 100, 80, 40);
		frame.getContentPane().add(btnClearCE);
		btnClearCE.addActionListener(this);
		btnClearCE.addKeyListener(this);
		btnClearCE.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnClearCE.setBackground(new Color(225, 225, 225));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnClearCE.setBackground(Color.WHITE);
			}
		});

		JButton btnClearC = new JButton("C");
		btnClearC.setBackground(Color.WHITE);
		btnClearC.setBounds(189, 100, 80, 40);
		frame.getContentPane().add(btnClearC);
		btnClearC.addActionListener(this);
		btnClearC.addKeyListener(this);
		btnClearC.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnClearC.setBackground(new Color(225, 225, 225));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnClearC.setBackground(Color.WHITE);
			}
		});

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

		JButton btnPercent = new JButton("%");
		btnPercent.setBackground(Color.WHITE);
		btnPercent.setBounds(31, 100, 80, 40);
		frame.getContentPane().add(btnPercent);
		btnPercent.addActionListener(this);
		btnPercent.addKeyListener(this);
		btnPercent.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnPercent.setBackground(new Color(225, 225, 225));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnPercent.setBackground(Color.WHITE);
			}
		});

		userInputTextField = new JFormattedTextField(cursorRightPositionedWithZero);
		userInputTextField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		userInputTextField.setBounds(178, 27, 170, 40);
		frame.getContentPane().add(userInputTextField);
		userInputTextField.setColumns(10);
		userInputTextField.setEditable(false);
		userInputTextField.addKeyListener(this);

		// unicode for X^2 (x squared)
		JButton btnSquare = new JButton("x\u00B2");
		btnSquare.setBackground(Color.WHITE);
		btnSquare.setBounds(110, 141, 80, 40);
		frame.getContentPane().add(btnSquare);
		btnSquare.addActionListener(this);
		btnSquare.addKeyListener(this);
		btnSquare.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnSquare.setBackground(new Color(225, 225, 225));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnSquare.setBackground(Color.WHITE);
			}
		});

		// unicode for 2 square root x symbol
		JButton btnSquareRoot = new JButton("2\u221Ax");
		btnSquareRoot.setBackground(Color.WHITE);
		btnSquareRoot.setBounds(189, 141, 80, 40);
		frame.getContentPane().add(btnSquareRoot);
		btnSquareRoot.addActionListener(this);
		btnSquareRoot.addKeyListener(this);
		btnSquareRoot.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnSquareRoot.setBackground(new Color(225, 225, 225));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnSquareRoot.setBackground(Color.WHITE);
			}
		});

		// unicode for division symbol
		JButton btnDivision = new JButton("\u00F7");
		btnDivision.setBackground(Color.WHITE);
		btnDivision.setBounds(268, 141, 80, 40);
		frame.getContentPane().add(btnDivision);
		btnDivision.addActionListener(this);
		btnDivision.addKeyListener(this);
		btnDivision.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnDivision.setBackground(new Color(225, 225, 225));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnDivision.setBackground(Color.WHITE);
			}
		});

		JButton btnMultiply = new JButton("*");
		btnMultiply.setBackground(Color.WHITE);
		btnMultiply.setBounds(268, 182, 80, 40);
		frame.getContentPane().add(btnMultiply);
		btnMultiply.addActionListener(this);
		btnMultiply.addKeyListener(this);
		btnMultiply.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnMultiply.setBackground(new Color(225, 225, 225));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnMultiply.setBackground(Color.WHITE);
			}
		});

		JButton btnNumberZero = new JButton("0");
		btnNumberZero.setBackground(Color.WHITE);
		btnNumberZero.setBounds(110, 304, 80, 40);
		frame.getContentPane().add(btnNumberZero);
		btnNumberZero.addActionListener(this);
		btnNumberZero.addKeyListener(this);
		btnNumberZero.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnNumberZero.setBackground(new Color(225, 225, 225));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnNumberZero.setBackground(Color.WHITE);
			}
		});

		JButton btnNumberOne = new JButton("1");
		btnNumberOne.setBackground(Color.WHITE);
		btnNumberOne.setBounds(31, 263, 80, 40);
		frame.getContentPane().add(btnNumberOne);
		btnNumberOne.addActionListener(this);
		btnNumberOne.addKeyListener(this);
		btnNumberOne.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnNumberOne.setBackground(new Color(225, 225, 225));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnNumberOne.setBackground(Color.WHITE);
			}
		});

		JButton btnNumberTwo = new JButton("2");
		btnNumberTwo.setBackground(Color.WHITE);
		btnNumberTwo.setBounds(110, 263, 80, 40);
		frame.getContentPane().add(btnNumberTwo);
		btnNumberTwo.addActionListener(this);
		btnNumberTwo.addKeyListener(this);
		btnNumberTwo.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnNumberTwo.setBackground(new Color(225, 225, 225));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnNumberTwo.setBackground(Color.WHITE);
			}
		});

		JButton btnNumberThree = new JButton("3");
		btnNumberThree.setBackground(Color.WHITE);
		btnNumberThree.setBounds(189, 263, 80, 40);
		frame.getContentPane().add(btnNumberThree);
		btnNumberThree.addActionListener(this);
		btnNumberThree.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnNumberThree.setBackground(new Color(225, 225, 225));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnNumberThree.setBackground(Color.WHITE);
			}
		});

		JButton btnNumberFour = new JButton("4");
		btnNumberFour.setBackground(Color.WHITE);
		btnNumberFour.setBounds(31, 222, 80, 40);
		frame.getContentPane().add(btnNumberFour);
		btnNumberFour.addActionListener(this);
		btnNumberFour.addKeyListener(this);
		btnNumberFour.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnNumberFour.setBackground(new Color(225, 225, 225));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnNumberFour.setBackground(Color.WHITE);
			}
		});

		JButton btnNumberFive = new JButton("5");
		btnNumberFive.setBackground(Color.WHITE);
		btnNumberFive.setBounds(110, 222, 80, 40);
		frame.getContentPane().add(btnNumberFive);
		btnNumberFive.addActionListener(this);
		btnNumberFive.addKeyListener(this);
		btnNumberFive.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnNumberFive.setBackground(new Color(225, 225, 225));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnNumberFive.setBackground(Color.WHITE);
			}
		});

		JButton btnNumberSix = new JButton("6");
		btnNumberSix.setBackground(Color.WHITE);
		btnNumberSix.setBounds(189, 222, 80, 40);
		frame.getContentPane().add(btnNumberSix);
		btnNumberSix.addActionListener(this);
		btnNumberSix.addKeyListener(this);
		btnNumberSix.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnNumberSix.setBackground(new Color(225, 225, 225));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnNumberSix.setBackground(Color.WHITE);
			}
		});

		JButton btnNumberSeven = new JButton("7");
		btnNumberSeven.setBackground(Color.WHITE);
		btnNumberSeven.setBounds(31, 182, 80, 40);
		frame.getContentPane().add(btnNumberSeven);
		btnNumberSeven.addActionListener(this);
		btnNumberSeven.addKeyListener(this);
		btnNumberSeven.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnNumberSeven.setBackground(new Color(225, 225, 225));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnNumberSeven.setBackground(Color.WHITE);
			}
		});

		JButton btnNumberEight = new JButton("8");
		btnNumberEight.setBackground(Color.WHITE);
		btnNumberEight.setBounds(110, 182, 80, 40);
		frame.getContentPane().add(btnNumberEight);
		btnNumberEight.addActionListener(this);
		btnNumberEight.addKeyListener(this);
		btnNumberEight.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnNumberEight.setBackground(new Color(225, 225, 225));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnNumberEight.setBackground(Color.WHITE);
			}
		});

		JButton btnNumberNine = new JButton("9");
		btnNumberNine.setBackground(Color.WHITE);
		btnNumberNine.setBounds(189, 182, 80, 40);
		frame.getContentPane().add(btnNumberNine);
		btnNumberNine.addActionListener(this);
		btnNumberNine.addKeyListener(this);
		btnNumberNine.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnNumberNine.setBackground(new Color(225, 225, 225));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnNumberNine.setBackground(Color.WHITE);
			}
		});

		JButton btnSubtract = new JButton("-");
		btnSubtract.setBackground(Color.WHITE);
		btnSubtract.setBounds(268, 222, 80, 40);
		frame.getContentPane().add(btnSubtract);
		btnSubtract.addActionListener(this);
		btnSubtract.addKeyListener(this);
		btnSubtract.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnSubtract.setBackground(new Color(225, 225, 225));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnSubtract.setBackground(Color.WHITE);
			}
		});

		JButton btnAdd = new JButton("+");
		btnAdd.setBackground(Color.WHITE);
		btnAdd.setBounds(268, 263, 80, 40);
		frame.getContentPane().add(btnAdd);
		btnAdd.addActionListener(this);
		btnAdd.addKeyListener(this);
		btnAdd.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnAdd.setBackground(new Color(225, 225, 225));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnAdd.setBackground(Color.WHITE);
			}
		});

		JButton btnPlusMinus = new JButton("+/-");
		btnPlusMinus.setBackground(Color.WHITE);
		btnPlusMinus.setBounds(31, 304, 80, 40);
		frame.getContentPane().add(btnPlusMinus);
		btnPlusMinus.addActionListener(this);
		btnPlusMinus.addKeyListener(this);
		btnPlusMinus.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnPlusMinus.setBackground(new Color(225, 225, 225));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnPlusMinus.setBackground(Color.WHITE);
			}
		});

		JButton btnDecimalPoint = new JButton(".");
		btnDecimalPoint.setBackground(Color.WHITE);
		btnDecimalPoint.setBounds(189, 304, 80, 40);
		frame.getContentPane().add(btnDecimalPoint);
		btnDecimalPoint.addActionListener(this);
		btnDecimalPoint.addKeyListener(this);
		btnDecimalPoint.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnDecimalPoint.setBackground(new Color(225, 225, 225));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnDecimalPoint.setBackground(Color.WHITE);
			}
		});

		JButton btnEquals = new JButton("=");
		btnEquals.setBackground(Color.WHITE);
		btnEquals.setBounds(268, 304, 80, 40);
		frame.getContentPane().add(btnEquals);
		btnEquals.addActionListener(this);
		btnEquals.addKeyListener(this);
		btnEquals.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnEquals.setBackground(new Color(225, 225, 225));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnEquals.setBackground(Color.WHITE);
			}
		});
	}

	/**
	 * This is responsible for listening to when buttons are clicked
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		// remove auto display '0' value from main number entry textField box so that
		// '0' is not included in calculation
		// Since it's only needed for display purposes
		if (userInputTextField.getText().equals(cursorRightPositionedWithZero) && !numberZeroEnteredByUser) {
			userInputTextField.setText(cursorRightPositioned);
		}

		// actions for numbers 0-9 buttons and all calculator operands
		switch (ae.getActionCommand()) {
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
			userInputTextField
					.setText(userInputTextField.getText().substring(0, userInputTextField.getText().length() - 1));
			break;

		case "1/x":
			// validate that the current text in textField isn't blank
			if (!userInputTextField.getText().equals(cursorRightPositioned)) {
				// need to cast below multiple times in order to perform 1/x operation
				userInputTextField.setText(
						cursorRightPositioned + Double.toString(1 / Double.valueOf(userInputTextField.getText())));
			}
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
						Calculator.divideByZeroflag ? " Cannot divide by zero" : cursorRightPositioned + value);

				// reset all array values to 0
				Collections.fill(Calculator.nums, "");
				numberZeroEnteredByUser = false;
				resetValues();
			}
			break; // break statement for case equals button

		case "CE", "C":
			userInputTextField.setText(cursorRightPositionedWithZero);
			Collections.fill(Calculator.nums, null);
			Calculator.divideByZeroflag = false;
			resetValues();
			break;

		case ".":
			if (!userInputTextField.getText().contains(".")) {
				userInputTextField.setText(userInputTextField.getText() + ".");
			}
			break;

		case "%":
			if (!userInputTextField.getText().equals(cursorRightPositioned)) {
				Calculator.setNumber(String.valueOf(Calculator.percent(Double.parseDouble(userInputTextField.getText()))));
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
				userInputTextField
						.setText(cursorRightPositioned + Math.sqrt(Double.valueOf(userInputTextField.getText())));
			}
			break;

		case "+/-":
			if (!userInputTextField.getText().equals(cursorRightPositioned)) {
				// if current number is positive, number becomes negative (minus is
				// prepended) else if number is negative, number becomes positive (minues is
				// removed)
				userInputTextField.setText(userInputTextField.getText().trim().substring(0, 1).equals("-")
						? cursorRightPositioned + userInputTextField.getText().replace("-", "")
						: cursorRightPositioned + ("-" + userInputTextField.getText().trim()));
			}
			break;

		default:
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
				Collections.fill(Calculator.nums, "");
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
		 * KeyEvent.VK_ADD, and KeyEvent.C don't work
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
	
	// method will reset all arrays 
	public static void resetValues() {
		Arrays.fill(operatorFlags, Boolean.FALSE);
		numberZeroEnteredByUser = false;
		Collections.fill(Calculator.doubleNums, 0.0);
		Calculator.nums.clear();
		Calculator.doubleNums.clear();
		Calculator.arrayNumsFilled = Calculator.arrayPositionNumber = 0;
	}
}