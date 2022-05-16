package calculator;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Performs all calculator operations. <br>
 * @author Brian Perel
 */
public class MyCalculator {
	
	private static final DecimalFormat df = new DecimalFormat("#0.00"); // for 2 precision point rounding
	protected static boolean divideByZeroflag; // if user divides by 0, raise flag

	// values are stored as string values at start to input into textField
	// component, then for computation we cast values entered to BigDecimal
	protected static ArrayList<String> stringNumbers = new ArrayList<>(); // hold values input into calculator, max width should be 10 point
													// values
	protected static ArrayList<BigDecimal> bigDecimalNumbers = new ArrayList<>(); // container for when values are converted
	
	private static BigDecimal answer;
	
	private MyCalculator() {
		// private constructor will hide the implicit public one, b/c utility classes like this should not have public constructors
	}

	/**
	 * Sets the number (contained as the argument) in the numbers array. This will
	 * allow the user to perform operations with more than just 2 numbers.
	 * @param argNumber number to be set
	 */
	public static void setNumber(String argNumber) {
		// do not set number in memory if % is still attached to number (enforces fact
		// that code must remove % before this step) or if string
		// includes a character
		if (!argNumber.endsWith("%")) {			
			stringNumbers.add(argNumber.trim());
		}
	}

	/**
	 * Calculator division operation
	 * @return the quotient
	 */
	public static BigDecimal divide() {
		for (int i = 1; i < bigDecimalNumbers.size(); i++) {
			if (BigDecimal.ZERO.equals(bigDecimalNumbers.get(1))) {
				divideByZeroflag = true;
				return BigDecimal.ZERO;
			} 
				
			answer = answer.divide(bigDecimalNumbers.get(i));
		}

		return answer;
	}

	/**
	 * Calculator multiplication operation
	 * @return the product
	 */
	public static BigDecimal multiply() {
		/*
		 * array starts at 1 because the first number is already taken into account
		 * above during initialization. Loop traverses until only array filled subscript
		 * locations are taken in, avoiding any null values, since the Array by default
		 * is initialized to null values
		 */
		for (int i = 1; i < bigDecimalNumbers.size(); i++) {
			// first part of below condition is because: by default double array values have
			// been initialized to 0,
			// since we're multiplying we don't want to multiply any random 0's cause we'll
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
	public static BigDecimal subtract() {		
		for (int i = 1; i < bigDecimalNumbers.size(); i++) {
			answer = answer.subtract(bigDecimalNumbers.get(i));
		}

		return answer;
	}

	/**
	 * Calculator addition operation
	 * @return the sum
	 */
	public static BigDecimal add() {
		for (int i = 1; i < bigDecimalNumbers.size(); i++) {
			answer = answer.add(bigDecimalNumbers.get(i));
		}

		return answer;
	}

	/**
	 * Calculator '%' operation
	 * @param argNumber the value positioned before the % sign in user input
	 * @return the percent result
	 */
	public static BigDecimal calculatePercentage(double argNumber) {
		return BigDecimal.valueOf(argNumber).divide(new BigDecimal(100));
	}

	/**
	 * Fills up the BigDecimal array with String array values after being casted to
	 * BigDecimal. This is performed when equals operator is hit
	 */
	public static void applyStringToBigDecimalConversion() {
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
	public static String compute() {
		applyStringToBigDecimalConversion();

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
