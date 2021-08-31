package encryption;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Class performs encryption and decryption process. Note: file to encrypt or
 * decrypt must be kept in same directory as this .jar file
 * 
 * @author Brian Perel
 *
 */
public class EncryptDecrypt {

	private String data; // field to hold data from file provided by user
	private boolean encrypted; // flag to tell if encryption has already occurred or not

	public EncryptDecrypt(String data) {
		this.data = data;
		encrypted = false;
	}

	/**
	 * Encrypt data given through constructor set into data field variable. Validate
	 * that encryption process hasn't already occurred. Create blank variable to
	 * store line of random characters. Traverse loop taking character at current
	 * index + a random number, casting to char type, and appending to blank
	 * variable. Write new line of data to file
	 * 
	 * @throws IOException
	 */
	public void encrypt() throws IOException {
		if (!encrypted) { // checks if encryption process has already occurred. Since you can't encrypt
							// encrypted data
			StringBuilder masked = new StringBuilder(""); // create variable that will store a line of random characters
															// of same length as original sentence in the file

			// loop to traverse data String provided by user, in order to replace every
			// character
			// with a random integer number added and casted to char type
			for (int index = 0; index < data.length(); index++) {
				// example: 'a' is replaced with ('a' + 14) then cast a14 (which is type int)
				// into a char which is 'o'
				masked.append((char) (data.charAt(index) + 5));
			}

			// cast the StringBuilder into a String
			data = masked.toString();

			FileWriter myWriter = new FileWriter(EncryptionGUI.fileName); // access the existing txt file

			try {
				myWriter.write(data); // write newly created string of random characters into file
			} catch (IOException e) {
				e.printStackTrace();
			}

			myWriter.close();
			encrypted = true;
		}
	}

	/**
	 * Validate that encryption process has occurred. Create a new blank variable to
	 * store original text. Traverse loop taking character at current index location
	 * - random number, cast to char type, and append to blank variable. Write new
	 * random character filled txt line to file
	 * 
	 * @return
	 * @throws IOException
	 */
	public String decrypt() throws IOException {

		StringBuilder unmasked = new StringBuilder();

		// loop to traverse encrypted data, fill in a blank StringBuilder variable with
		// values from data variable
		// with a random integer number subtracted and casted to char type
		for (int index = 0; index < data.length(); index++) {
			// example: 'o' is replaced with ('o' - 14) then cast 97 (which is type int)
			// into a char which is 'a'
			unmasked.append((char) (data.charAt(index) - 5));
		}

		// cast the StringBuilder into a String
		data = unmasked.toString();

		FileWriter myWriter = new FileWriter(EncryptionGUI.fileName);

		try {
			// override with decrypted data
			myWriter.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

		myWriter.close();

		encrypted = false;

		return data;
	}

	@Override
	public String toString() {
		return data;
	}
}