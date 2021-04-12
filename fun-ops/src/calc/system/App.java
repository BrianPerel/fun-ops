package calc.system;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	static boolean operatorFlags[] = new boolean[4]; // array to hold flags to be raised if a calculator operator is
														// clicked
	static final String CURSOR_RIGHT_POSITION_WITH_ZERO = "                             0";
	static final String CURSOR_RIGHT_POSITION = "                             ";

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
	 * Create the application.
	 */
	public App() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 400, 436);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JButton btnNewButton = new JButton("1/x");
		btnNewButton.setBounds(31, 141, 80, 40);
		frame.getContentPane().add(btnNewButton);
		btnNewButton.addActionListener(this);

		JButton btnNewButton_1 = new JButton("CE");
		btnNewButton_1.setBounds(110, 100, 80, 40);
		frame.getContentPane().add(btnNewButton_1);
		btnNewButton_1.addActionListener(this);

		JButton btnNewButton_2 = new JButton("C");
		btnNewButton_2.setBounds(189, 100, 80, 40);
		frame.getContentPane().add(btnNewButton_2);
		btnNewButton_2.addActionListener(this);

		// backspace symbol
		JButton btnNewButton_3 = new JButton("\u232B");
		btnNewButton_3.setBounds(268, 100, 80, 40);
		frame.getContentPane().add(btnNewButton_3);
		btnNewButton_3.addActionListener(this);

		JButton btnNewButton_4 = new JButton("%");
		btnNewButton_4.setBounds(31, 100, 80, 40);
		frame.getContentPane().add(btnNewButton_4);
		btnNewButton_4.addActionListener(this);

		textField = new JTextField(CURSOR_RIGHT_POSITION_WITH_ZERO);
		textField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textField.setBounds(178, 27, 170, 40);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		textField.setEditable(false);

		// Unicode for X^2
		JButton btnNewButton_5 = new JButton("x\u00B2");
		btnNewButton_5.setBounds(110, 141, 80, 40);
		frame.getContentPane().add(btnNewButton_5);
		btnNewButton_5.addActionListener(this);

		// 2 square root x symbol
		JButton btnNewButton_6 = new JButton("2\u221Ax");
		btnNewButton_6.setBounds(189, 141, 80, 40);
		frame.getContentPane().add(btnNewButton_6);
		btnNewButton_6.addActionListener(this);

		JButton btnNewButton_7 = new JButton("\u00F7");
		btnNewButton_7.setBounds(268, 141, 80, 40);
		frame.getContentPane().add(btnNewButton_7);
		btnNewButton_7.addActionListener(this);

		JButton btnNewButton_8 = new JButton("7");
		btnNewButton_8.setBounds(31, 182, 80, 40);
		frame.getContentPane().add(btnNewButton_8);
		btnNewButton_8.addActionListener(this);

		JButton btnNewButton_9 = new JButton("8");
		btnNewButton_9.setBounds(110, 182, 80, 40);
		frame.getContentPane().add(btnNewButton_9);
		btnNewButton_9.addActionListener(this);

		JButton btnNewButton_10 = new JButton("9");
		btnNewButton_10.setBounds(189, 182, 80, 40);
		frame.getContentPane().add(btnNewButton_10);
		btnNewButton_10.addActionListener(this);

		JButton btnNewButton_11 = new JButton("*");
		btnNewButton_11.setBounds(268, 182, 80, 40);
		frame.getContentPane().add(btnNewButton_11);
		btnNewButton_11.addActionListener(this);

		JButton btnNewButton_12 = new JButton("4");
		btnNewButton_12.setBounds(31, 222, 80, 40);
		frame.getContentPane().add(btnNewButton_12);
		btnNewButton_12.addActionListener(this);

		JButton btnNewButton_13 = new JButton("5");
		btnNewButton_13.setBounds(110, 222, 80, 40);
		frame.getContentPane().add(btnNewButton_13);
		btnNewButton_13.addActionListener(this);

		JButton btnNewButton_14 = new JButton("6");
		btnNewButton_14.setBounds(189, 222, 80, 40);
		frame.getContentPane().add(btnNewButton_14);
		btnNewButton_14.addActionListener(this);

		JButton btnNewButton_15 = new JButton("-");
		btnNewButton_15.setBounds(268, 222, 80, 40);
		frame.getContentPane().add(btnNewButton_15);
		btnNewButton_15.addActionListener(this);

		JButton btnNewButton_16 = new JButton("1");
		btnNewButton_16.setBounds(31, 263, 80, 40);
		frame.getContentPane().add(btnNewButton_16);
		btnNewButton_16.addActionListener(this);

		JButton btnNewButton_17 = new JButton("2");
		btnNewButton_17.setBounds(110, 263, 80, 40);
		frame.getContentPane().add(btnNewButton_17);
		btnNewButton_17.addActionListener(this);

		JButton btnNewButton_18 = new JButton("3");
		btnNewButton_18.setBounds(189, 263, 80, 40);
		frame.getContentPane().add(btnNewButton_18);
		btnNewButton_18.addActionListener(this);

		JButton btnNewButton_19 = new JButton("+");
		btnNewButton_19.setBounds(268, 263, 80, 40);
		frame.getContentPane().add(btnNewButton_19);
		btnNewButton_19.addActionListener(this);

		JButton btnNewButton_20 = new JButton("+/-");
		btnNewButton_20.setBounds(31, 300, 80, 40);
		frame.getContentPane().add(btnNewButton_20);
		btnNewButton_20.addActionListener(this);

		JButton btnNewButton_21 = new JButton("0");
		btnNewButton_21.setBounds(110, 300, 80, 40);
		frame.getContentPane().add(btnNewButton_21);
		btnNewButton_21.addActionListener(this);

		JButton btnNewButton_22 = new JButton(".");
		btnNewButton_22.setBounds(189, 300, 80, 40);
		frame.getContentPane().add(btnNewButton_22);
		btnNewButton_22.addActionListener(this);

		JButton btnNewButton_23 = new JButton("=");
		btnNewButton_23.setBounds(268, 300, 80, 40);
		frame.getContentPane().add(btnNewButton_23);
		btnNewButton_23.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		String action = ae.getActionCommand();
		
		if(textField.getText().equals(CURSOR_RIGHT_POSITION_WITH_ZERO)) { 
			textField.setText(CURSOR_RIGHT_POSITION); 
		}
		
		// actions for numbers 0-9
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

		// actions for symbols
		case "CE":
			textField.setText(CURSOR_RIGHT_POSITION_WITH_ZERO);
			Collections.fill(MyCalculator.nums, null);
			Collections.fill(MyCalculator.doubleNums, 0.0);
			Arrays.fill(operatorFlags, Boolean.FALSE);
			MyCalculator.nums.clear();
			MyCalculator.doubleNums.clear();
			MyCalculator.divideByZeroflag = false;
			MyCalculator.arrayNumsFilled = MyCalculator.arrayPosNum = 0;
			break;

		case "C":
			textField.setText(CURSOR_RIGHT_POSITION_WITH_ZERO);
			Arrays.fill(operatorFlags, Boolean.FALSE);
			Collections.fill(MyCalculator.nums, null);
			Collections.fill(MyCalculator.doubleNums, 0.0);
			MyCalculator.nums.clear();
			MyCalculator.doubleNums.clear();
			MyCalculator.divideByZeroflag = false;
			MyCalculator.arrayNumsFilled = MyCalculator.arrayPosNum = 0;
			break;

		case "\u246B":
			MyCalculator.setNumber(textField.getText());
			textField.setText(CURSOR_RIGHT_POSITION);
			operatorFlags[0] = true;
			break;

		case "*":
			MyCalculator.setNumber(textField.getText());
			textField.setText(CURSOR_RIGHT_POSITION);
			operatorFlags[1] = true;
			break;

		case "\u00F7":
			MyCalculator.setNumber(textField.getText());
			textField.setText(CURSOR_RIGHT_POSITION);
			operatorFlags[2] = true;
			break;

		case "+":
			MyCalculator.setNumber(textField.getText());
			textField.setText(CURSOR_RIGHT_POSITION);
			operatorFlags[3] = true;
			break;
		}

		if (action.equals("%") && !textField.getText().equals(CURSOR_RIGHT_POSITION)) {
			MyCalculator.setNumber(String.valueOf(system.percent(Double.parseDouble(textField.getText()))));
			textField.setText(textField.getText() + "%");
		} else if (action.equals("1/x")) {
			// validate that the current text in textField isn't blank
			if (!textField.getText().equals(CURSOR_RIGHT_POSITION)) {
				// need to cast below multiple times in order to perform 1/x operation
				textField.setText(CURSOR_RIGHT_POSITION + Double.toString(1 / Double.valueOf(textField.getText())));
			}
		} else if (action.equals("x\u00B2") && !textField.getText().equals(CURSOR_RIGHT_POSITION)) {
			textField.setText(
					CURSOR_RIGHT_POSITION + Double.toString(Math.pow((Double.valueOf(textField.getText())), 2)));
		}
		// 2 square root x symbol
		else if (action.equals("2\u221Ax") && !textField.getText().equals(CURSOR_RIGHT_POSITION)) {
			textField.setText(CURSOR_RIGHT_POSITION + Math.sqrt(Double.valueOf(textField.getText())));
		}
		// backspace symbol
		else if (action.equals("\u232B")) {
			textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
		} else if (action.equals("+/-") && !textField.getText().equals(CURSOR_RIGHT_POSITION)) {
			// if current number is a positive number, number becomes negative (minus is
			// prepended)
			if (!textField.getText().trim().substring(0, 1).equals("-")) {
				textField.setText(CURSOR_RIGHT_POSITION + ("-" + textField.getText().trim()));
			}
			// if current number is a negative number, number becomes positive (minus is
			// removed)
			else if (textField.getText().trim().substring(0, 1).equals("-")) {
				textField.setText(CURSOR_RIGHT_POSITION + textField.getText().replace("-", ""));
			}
		}

		// actions for calculator operators
		if (action.equals("=")) {
			// if textField label is blank, then no action has been done.
			// Hence equal operation isn't performed
			if (!textField.getText().equals(CURSOR_RIGHT_POSITION)) {
				MyCalculator.setNumber(textField.getText());

				String value = system.compute();

				if (MyCalculator.divideByZeroflag) {
					textField.setText(" Cannot divide by zero");
				} else {
					textField.setText(CURSOR_RIGHT_POSITION + value);
				}

				Arrays.fill(operatorFlags, Boolean.FALSE);
				Collections.fill(MyCalculator.nums, null);
				Collections.fill(MyCalculator.doubleNums, 0.0);
				MyCalculator.nums.clear();
				MyCalculator.doubleNums.clear();
				MyCalculator.arrayNumsFilled = MyCalculator.arrayPosNum = 0;
			}
		} else if (action.equals(".") && !textField.getText().contains(".")) {
			textField.setText(textField.getText() + ".");
		}
	}
}
