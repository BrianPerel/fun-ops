package com.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Performs all calculator operations. <br>
 * @author Brian Perel
 */
public class MyCalculator {

	// values are stored as string values at start to input into textField
	// component, then for computation we cast values entered to BigDecimal
	protected static List<String> stringNumbers = new ArrayList<>(); // hold values input into calculator, max width should be 10 point values
	protected static List<BigDecimal> bigDecimalNumbers = new ArrayList<>(); // container for when values are converted	private static final DecimalFormat df = new DecimalFormat("#,###.##"); // for 2 decimal places precision rounding with commas
	protected static boolean divideByZeroflag; // if user divides by 0, raise flag
	private static BigDecimal answer;

	private MyCalculator() {
		// private constructor will hide the implicit public one, b/c utility classes like this should not have public constructors
	}

	/**
	 * Sets the number (contained as the argument) in the numbers array. This will
	 * allow the user to perform operations with more than just 2 numbers.
	 * @param argNumber number to be set
	 */
	protected static void setNumber(String argNumber) {
		// do not set number in memory if % is still attached to number (enforces fact
		// that code must remove % before this step) or if string
		// includes a character
		argNumber = argNumber.replace(",", "");

		if (!argNumber.endsWith("%")) {
			if(argNumber.isEmpty()) {
				argNumber = "0";
			}

			stringNumbers.add(argNumber.trim());
		}
	}

	/**
	 * Calculator division operation
	 * @return the quotient
	 */
	private static BigDecimal divide() {
		for (int i = 1; i < bigDecimalNumbers.size(); i++) {
			if (BigDecimal.ZERO.equals(bigDecimalNumbers.get(1))) {
				divideByZeroflag = true;
				return BigDecimal.ZERO;
			}

			// specifying a rounding mode of ROUND_HALF_UP for the divide() method,
			// which rounds the result to the nearest neighbor, with ties being rounded up
			// preventing a ArithmeticException: Non-terminating decimal expansion; no exact representable decimal result
			answer = answer.divide(bigDecimalNumbers.get(i), RoundingMode.HALF_UP);
		}

		return answer;
	}

	/**
	 * Calculator multiplication operation
	 * @return the product
	 */
	private static BigDecimal multiply() {
		/*
		 * array starts at 1 because the first number is already taken into account
		 * above during initialization. Loop traverses until only array filled subscript
		 * locations are taken in, avoiding any null values, since the Array by default
		 * is initialized to null values
		 */
		for (int i = 1; i < bigDecimalNumbers.size(); i++) {
			// first part of below condition is because: by default double array values have
			// been initialized to 0,
			// since we're multiplying we don't want to multiply any random 0's because we'll
			// be getting 0
			// second and third part of statement are to allow you to do 0 * x or x * 0
			if (bigDecimalNumbers.get(i) != BigDecimal.ZERO || bigDecimalNumbers.get(0) == BigDecimal.ZERO || bigDecimalNumbers.get(1) == BigDecimal.ZERO) {
				answer = answer.multiply(bigDecimalNumbers.get(i));
			}
		}

		return answer;
	}

	/**
	 * Calculator subtraction operation
	 * @return the difference
	 */
	private static BigDecimal subtract() {
		for (int i = 1; i < bigDecimalNumbers.size(); i++) {
			answer = answer.subtract(bigDecimalNumbers.get(i));
		}

		return answer;
	}

	/**
	 * Calculator addition operation
	 * @return the sum
	 */
	private static BigDecimal add() {
		for (int i = 1; i < bigDecimalNumbers.size(); i++) {
			answer = answer.add(bigDecimalNumbers.get(i));
		}

		return answer;
	}

	/**
	 * Fills up the BigDecimal array with String array values after being cast to
	 * BigDecimal. This is performed when equals operator is hit
	 */
	private static void toBigDecimalConversion() {
		for (String i : stringNumbers) { // get String value
			bigDecimalNumbers.add(new BigDecimal(stringNumbers.get(stringNumbers.indexOf(i))));
		}

		answer = bigDecimalNumbers.get(0);
	}

	/**
	 * Converts String to BigDecimal values, calls appropriate requested operation,
	 * and formats the return value.
	 * @return formatted result value after performing operation
	 */
	protected static String compute() {
		toBigDecimalConversion();

		if (MyCalculatorGui.operatorFlags[0]) {
			return df.format(divide());
		}
		else if (MyCalculatorGui.operatorFlags[1]) {
			return df.format(multiply());
		}
		else if (MyCalculatorGui.operatorFlags[2]) {
			return df.format(subtract());
		}
		else if (MyCalculatorGui.operatorFlags[3]) {
			return df.format(add());
		}

		return "0";
	}
}
