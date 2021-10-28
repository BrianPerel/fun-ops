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
	static List<String> stringNumbers = new ArrayList<>(); // hold values input into calculator, max width should be 10 point
													// values
	static List<Double> doubleNumbers = new ArrayList<>(); // container for when values are converted
	
	static double answer;

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
			stringNumbers.add(arrayPositionNumber, argNumber.trim());
			arrayPositionNumber++;
			arrayNumsFilled++;
		}
	}

	/**
	 * Calculator division operation
	 * 
	 * @return the quotient
	 */
	public static double divide() {
		for (int i = 1; i < arrayNumsFilled; i++) {
			if (doubleNumbers.get(i) != 0 || doubleNumbers.get(0) == 0 || doubleNumbers.get(1) == 0) {
				if (doubleNumbers.get(1) == 0) {
					divideByZeroflag = true;
					answer = 0;
				} else {
					answer /= doubleNumbers.get(i);
				}
			}
		}

		return answer;
	}

	/**
	 * Calculator multiplication operation
	 * 
	 * @return the product
	 */
	public static double multiply() {
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
			if (doubleNumbers.get(i) != 0 || doubleNumbers.get(0) == 0 || doubleNumbers.get(1) == 0) {
				answer *= doubleNumbers.get(i);
			}
		}

		return answer;
	}

	/**
	 * Calculator subtraction operation
	 * 
	 * @return the difference
	 */
	public static double subtract() {
		for (int i = 1; i < doubleNumbers.size(); i++) {
			answer -= doubleNumbers.get(i);
		}

		return answer;
	}

	/**
	 * Calculator addition operation
	 * 
	 * @return the sum
	 */
	public static double add() {
		for (int i = 1; i < doubleNumbers.size(); i++) {
			answer += doubleNumbers.get(i);
		}

		return answer;
	}

	/**
	 * Calculator % operation
	 * 
	 * @param argNumber the value positioned before the % sign in user input
	 * @return the percent result
	 */
	public static double percentage(double argNumber) {
		return argNumber / 100;
	}

	/**
	 * Fills up the double array with String array values after being casted to
	 * double. This is performed when equals operator is hit
	 */
	public static void applyStringToDoubleConversion() {
		for (int i = 0; i < stringNumbers.size(); i++) {
			doubleNumbers.add(i, Double.parseDouble(stringNumbers.get(i)));
		}
		
		answer = doubleNumbers.get(0);
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
			return df.format(divide());
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
