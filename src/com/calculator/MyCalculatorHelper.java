package com.calculator;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Helper class to provide calculator with the extra required parts
 * @author Brian Perel
 */
public class MyCalculatorHelper {

	/**
	 * Resets all calculator's arrays (memory).
	 */
	public void resetValues() {
		MyCalculatorGui.hasUserEnteredZero = false;
		Arrays.fill(MyCalculatorGui.operatorFlags, false);
		MyCalculator.stringNumbers.clear();
		MyCalculator.bigDecimalNumbers.clear();
	}

	public void setNumberText() {
		MyCalculator.setNumber(MyCalculatorGui.textFieldUserInput.getText());
		MyCalculatorGui.textFieldUserInput.setText(MyCalculatorGui.CURSOR_RIGHT_POSITION);
	}

	/**
	 * Remove the auto display '0' value from the main number entry textField box so that
	 * this '0' is not included in calculations. Since it's only needed for display purposes.
	 */
	public void removeAutoDisplayZero(boolean hasUserEnteredZero) {
		if (MyCalculatorGui.textFieldUserInput.getText().equals(MyCalculatorGui.CURSOR_RIGHT_POSITION_W_ZERO) && !hasUserEnteredZero) {
			MyCalculatorGui.textFieldUserInput.setText(MyCalculatorGui.CURSOR_RIGHT_POSITION);
		}
	}

	/**
	 * Prevents user from adding a leading zero to an input number (prevents ex. 04)
	 */
	public void removeIllegalZero() {
		if(MyCalculatorGui.textFieldUserInput.getText().trim().length() > 1 && MyCalculatorGui.textFieldUserInput.getText().trim().charAt(0) == '0') {
			MyCalculatorGui.textFieldUserInput.setText(MyCalculatorGui.textFieldUserInput.getText().trim().substring(1));
		}
	}

	/**
	 * Calculator '%' operation
	 * @param argNumber the value positioned before the % sign in user input
	 * @return the percent result
	 */
	public BigDecimal calculatePercentage(double argNumber) {
		return BigDecimal.valueOf(argNumber).divide(new BigDecimal(100));
	}

	/**
	 * Performs actions responsible for when the calculator's equals button is hit
	 */
	public void performEnterOrEquals() {
		// if textField label is blank, then no action has been done by user.
		// Hence, in that scenario equal operation isn't performed
		if (!MyCalculatorGui.textFieldUserInput.getText().equals(MyCalculatorGui.CURSOR_RIGHT_POSITION)) {
			MyCalculator.setNumber(MyCalculatorGui.textFieldUserInput.getText());

			// perform computation to make and get the value
			String value = MyCalculator.compute();

			// if value is whole then don't display 0's after decimal; ex. instead of 25.00
			// display 25
			final double v = Double.parseDouble(value);
			if ((v * 10) % 10 == 0) { // if value calculated is whole number
				value = MyCalculatorGui.df.format(v); // removes zero's after decimal point
			}

			// check for division by zero. Avoids exception being flagged
			MyCalculatorGui.textFieldUserInput.setText(
					(MyCalculator.divideByZeroflag) ? "Cannot divide by zero" : MyCalculatorGui.CURSOR_RIGHT_POSITION.concat(value));

			resetValues(); // reset all array/memory values
		}
		else if (MyCalculatorGui.textFieldUserInput.getText().equals(MyCalculatorGui.CURSOR_RIGHT_POSITION)) {
			MyCalculatorGui.textFieldUserInput.setText(MyCalculatorGui.CURSOR_RIGHT_POSITION_W_ZERO);
		}
	}

}
