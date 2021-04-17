package calc.system;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Performs all calculator operations. <br>
 * 
 * @author Brian Perel
 * @created Dec 11, 2020
 */
public class MyCalculator {

	static int arrayPosNum; // used to place every user calculator operand value in a separate location
	DecimalFormat df = new DecimalFormat("#0.00"); // for 2 precision point rounding
	static int arrayNumsFilled; // container to tell what array subscripts are free or taken
	static boolean divideByZeroflag; // if user divides by 0, raise flag

	// values are stored as string values at start to input into textField
	// component, then for computation we cast values entered to double
	static List<String> nums = new ArrayList<>(); // hold values input into calculator, max width should be 10 point values
	static List<Double> doubleNums = new ArrayList<>(); // container for when values are converted

	/**
	 * sets the number (contained as the argument) in the numbers array. This will
	 * allow the user to perform operations with more than just 2 numbers.
	 * 
	 * @param num number to be set
	 */
	public static void setNumber(String num) {
		num = num.trim();

		// do not set number in memory if % is still attached to number (enforces fact that code must remove % before this step) or if string
		// includes a character
		if (!num.contains("%")) {
			nums.add(arrayPosNum, num);
			arrayPosNum++;
			arrayNumsFilled++;
		} 
	}

	/**
	 * Calculator division operation
	 * 
	 * @return the quotient
	 */
	public static double div() {
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
	public double mult() {
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
	public double sub() {
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
	public double add() {
		double ans = doubleNums.get(0);
		for (int i = 1; i < doubleNums.size(); i++) {
			ans += doubleNums.get(i);
		}

		return ans;
	}

	/**
	 * Calculator % operation
	 * 
	 * @param n the value positioned before the % sign in user input
	 * @return the percent result
	 */
	public double percent(double n) {
		return n / 100;
	}

	/**
	 * Fills up the double array with String array values after being casted to
	 * double. This is performed when equals operator is hit
	 */
	public void stringToDoubleConv() {
		for (int i = 0; i < nums.size(); i++) {
			doubleNums.add(i, Double.parseDouble(nums.get(i)));
		}
	}

	/**
	 * calls appropriate requested operation
	 * 
	 * @return result value after performing operation
	 */
	public String compute() {
		stringToDoubleConv();

		if (App.operatorFlags[0]) {
			return df.format(div());
		} else if (App.operatorFlags[1]) {
			return df.format(mult());
		} else if (App.operatorFlags[2]) {
			return df.format(sub());
		} else if (App.operatorFlags[3]) {
			return df.format(add());
		}

		return "0";
	}

}
