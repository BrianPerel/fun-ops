package calculator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Performs all calculator operations. <br>
 * 
 * @author Brian Perel
 */
public class Calculator {

	static int arrayPositionNumber; // used to place every user calculator operand value in a separate location
	static int arrayNumsFilled; // container to tell what array subscripts are free or taken
	static DecimalFormat df = new DecimalFormat("#0.00"); // for 2 precision point rounding
	static boolean divideByZeroflag; // if user divides by 0, raise flag

	// values are stored as string values at start to input into textField
	// component, then for computation we cast values entered to double
	static List<String> nums = new ArrayList<>(); // hold values input into calculator, max width should be 10 point
													// values
	static List<Double> doubleNums = new ArrayList<>(); // container for when values are converted

	/**
	 * sets the number (contained as the argument) in the numbers array. This will
	 * allow the user to perform operations with more than just 2 numbers.
	 * 
	 * @param argNumber number to be set
	 */
	public static void setNumber(String argNumber) {

		// do not set number in memory if % is still attached to number (enforces fact
		// that code must remove % before this step) or if string
		// includes a character
		if (!argNumber.contains("%")) {
			nums.add(arrayPositionNumber, argNumber.trim());
			arrayPositionNumber++;
			arrayNumsFilled++;
		}
	}

	/**
	 * Calculator division operation
	 * 
	 * @return the quotient
	 */
	public static double division() {
		double ans = doubleNums.get(0);

		for (int i = 1; i < arrayNumsFilled; i++) {
			if (doubleNums.get(i) != 0 || doubleNums.get(0) == 0 || doubleNums.get(1) == 0) {
				if (doubleNums.get(1) == 0) {
					divideByZeroflag = true;
					ans = 0;
				} else {
					ans /= doubleNums.get(i);
				}
			}
		}

		return ans;
	}

	/**
	 * Calculator multiplication operation
	 * 
	 * @return the product
	 */
	public static double multiply() {
		double ans = doubleNums.get(0);

		/*
		 * array starts at 1 because the first number is already taken into account
		 * above during initialization. Loop traverses until only array filled subscript
		 * locations are taken in, avoiding any null values, since the Array by default
		 * is initialized to null values
		 */
		for (int i = 1; i < arrayNumsFilled; i++) {
			// first part of below condition is because: by default double array values have
			// been initialized to 0,
			// since we're multiplying we don't want to multiply any random 0's cause we'll
			// be getting 0
			// second and third part of statement are to allow you to do 0 * x or x * 0
			if (doubleNums.get(i) != 0 || doubleNums.get(0) == 0 || doubleNums.get(1) == 0) {
				ans *= doubleNums.get(i);
			}
		}

		return ans;
	}

	/**
	 * Calculator subtraction operation
	 * 
	 * @return the difference
	 */
	public static double subtract() {
		double ans = doubleNums.get(0);

		for (int i = 1; i < doubleNums.size(); i++) {
			ans -= doubleNums.get(i);
		}

		return ans;
	}

	/**
	 * Calculator addition operation
	 * 
	 * @return the sum
	 */
	public static double add() {
		double ans = doubleNums.get(0);

		for (int i = 1; i < doubleNums.size(); i++) {
			ans += doubleNums.get(i);
		}

		return ans;
	}

	/**
	 * Calculator % operation
	 * 
	 * @param argNumber the value positioned before the % sign in user input
	 * @return the percent result
	 */
	public static double percent(double argNumber) {
		return argNumber / 100;
	}

	/**
	 * Fills up the double array with String array values after being casted to
	 * double. This is performed when equals operator is hit
	 */
	public static void applyStringToDoubleConversion() {
		for (int i = 0; i < nums.size(); i++) {
			doubleNums.add(i, Double.parseDouble(nums.get(i)));
		}
	}

	/**
	 * calls appropriate requested operation after converting String to Double and
	 * then formatting return value
	 * 
	 * @return formatted result value after performing operation
	 */
	public static String compute() {
		applyStringToDoubleConversion();

		if (App.operatorFlags[0]) {
			return df.format(division());
		} else if (App.operatorFlags[1]) {
			return df.format(multiply());
		} else if (App.operatorFlags[2]) {
			return df.format(subtract());
		} else if (App.operatorFlags[3]) {
			return df.format(add());
		}

		return "0";
	}
}
