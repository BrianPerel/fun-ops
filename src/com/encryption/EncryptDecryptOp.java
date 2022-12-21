package com.encryption;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Class performs encryption and decryption process.
 * @author Brian Perel
 */
public class EncryptDecryptOp {

	private StringBuilder data; // field to hold data from file provided by user
	private boolean isEncrypted; // flag to tell if encryption has already occurred or not

	/**
	 * Constructor sets data being passed in and assumes encryption process has not occurred
	 * @param argData the user data to be encrypted/decrypted
	 */
	public EncryptDecryptOp(StringBuilder argData) {
		data = argData;
	}

	/**
	 * Encrypt data given through constructor set into data field variable. Validate
	 * that encryption process hasn't already occurred. Create blank variable to
	 * store line of random characters. Traverse loop taking character at current
	 * index + a random number, casting to char type, and appending to blank
	 * variable. Write new line of data to file
	 *
	 * @throws IOException signals that an I/O exception has occurred while attempting to write to a file
	 */
	protected boolean encrypt() throws IOException {

		// checks if encryption process has already occurred. Since you can't encrypt encrypted data
		if (!isEncrypted) {
			StringBuilder maskedData = new StringBuilder(); // create variable that will store a line of random characters
															// of same length as original sentence in the file

			// loop to traverse data String provided by user, in order to replace every
			// character with a random integer number added and casted to char type
			for (int index = 0; index < data.length(); index++) {
				// my cipher algorithm = obtain letter at current index of data loaded from user file,
				// add a value which automatically returns the ASCII value of given character (then adds the value), and cast this int number into a char

				// example: 'a' is replaced with ('a' + 5) then cast a5 (which is type int) into a char which is 'o'
				maskedData.append((char) (data.charAt(index) + 5));
			}

			data = maskedData.reverse(); // reverse the contents of the string builder for further encryption

			try(FileWriter myWriter = new FileWriter(EncryptDecryptFileUtils.getFileName())) {
				myWriter.write(data.toString()); // write newly created string of random characters into file
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}

			EncryptDecryptFileUtils.openFile();

			isEncrypted = true;
			return isEncrypted;
		}

		return false;
	}

	/**
	 * Validate that encryption process has occurred. Create a new blank variable to
	 * store original text. Traverse loop taking character at current index location
	 * - random number, cast to char type, and append to blank variable. Write new
	 * random character filled txt line to file
	 *
	 * @return returns the unmasked/decrypted data string
	 * @throws IOException signals that an I/O exception has occurred while attempting to write to a file
	 */
	protected boolean decrypt() throws IOException {

		// checks if encryption process has already occurred. Since you can't decrypt un-encrypted data
		if (isEncrypted) {
			data = data.reverse(); // reverse back the encrypted contents of the string builder for decryption
			data = decryptAndFormat(0); // perform decryption process and format the data, the 0 value is used to count the words

			try(FileWriter myWriter = new FileWriter(EncryptDecryptFileUtils.getFileName())) {
				myWriter.write(data.toString()); // override with decrypted data
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}

			EncryptDecryptFileUtils.openFile();

			isEncrypted = false;
			return !isEncrypted;
		}

		return false;
	}

	/**
	 * Enforces every sentence to start with a single space, during decryption process
	 * @return unmaskedData the decrypted data
	 */
	private StringBuilder decryptAndFormat(int argWordCount) {
		StringBuilder unmaskedData = new StringBuilder();

		// loop to traverse encrypted data, fill in a blank StringBuilder variable with
		// values from data variable
		// with a random integer number subtracted and casted to char type
		for (int index = 0; index < data.length(); index++) {
			// example: 'o' is replaced with ('o' - 14) then cast 97 (which is type int)
			// into a char which is 'a'
			unmaskedData.append((char) (data.charAt(index) - 5));

			if (Character.isWhitespace((char) (data.charAt(index) - 5)) && (++argWordCount % 25 == 0)) {
				// if the number in wordCount is divisible by 25 (ex. 25, 50, 75, 100, etc.) then add a new line to the file being decrypted
				unmaskedData.append('\n');
			}
		}

		return formatData(unmaskedData);
	}

	/**
	 * Formats the decrypted data
	 * @param unmaskedData data in it's original (decrypted) form
	 */
	private StringBuilder formatData(StringBuilder unmaskedData) {
		// checks that every sentence starts with a single space
		for (int index = 0; index < unmaskedData.length() - 1; index++) {
			// if current character detected is a '.' and the next character is not a space then...
			if (unmaskedData.charAt(index) == '.' && !Character.isWhitespace(unmaskedData.charAt(index + 1))) {
				// insert a single space into StringBuilder unmaskedData here, at index+1
				unmaskedData.insert(index + 1, ' ');

				// if 2 spaces at the beginning of a sentence are encountered, format it to start with just 1 space
				if (Character.isWhitespace(unmaskedData.charAt(index + 2))) {
					unmaskedData.replace(index + 2, index + 3, "");
				}
			}
		}

		return unmaskedData;
	}
}
