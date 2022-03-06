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

	private StringBuilder data; // field to hold data from file provided by user
	private boolean isEncrypted; // flag to tell if encryption has already occurred or not

	/**
	 * Constructor sets data being passed in and assumes encryption process has not occurred
	 * @param argData the user data to be encrypted/decrypted
	 */
	public EncryptDecrypt(StringBuilder argData) {
		this.data = argData;
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
	public void encrypt() throws IOException {
		
		// checks if encryption process has already occurred. Since you can't encrypt encrypted data
		if (!isEncrypted) { 
			StringBuilder maskedData = new StringBuilder(); // create variable that will store a line of random characters
															// of same length as original sentence in the file

			// loop to traverse data String provided by user, in order to replace every
			// character
			// with a random integer number added and casted to char type
			for (int index = 0; index < data.length(); index++) {
				// example: 'a' is replaced with ('a' + 14) then cast a14 (which is type int)
				// into a char which is 'o'
				maskedData.append((char) (data.charAt(index) + 5));
			}
			
			// cast the StringBuilder into a String
			data = maskedData;
			FileWriter myWriter = new FileWriter(EncryptDecryptGui.getFileName()); // access the existing txt file

			try {
				myWriter.write(data.toString()); // write newly created string of random characters into file
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}

			myWriter.close();
			isEncrypted = true;
		}
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
	public StringBuilder decrypt() throws IOException {
		
		// checks if encryption process has already occurred. Since you can't decrypt un-encrypted data
		if (isEncrypted) {				
			// cast the StringBuilder into a String
			data = checkSentenceFormat();
			FileWriter myWriter = new FileWriter(EncryptDecryptGui.getFileName());
	
			try {
				// override with decrypted data
				myWriter.write(data.toString());
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
	
			myWriter.close();
			isEncrypted = false;
		}
		
		return data;
	}
	
	/**
	 * Enforces every sentence to start with a single space, during decryption process
	 * @return unmaskedData the decrypted data
	 */
	public StringBuilder checkSentenceFormat() {
				
		int wordCount = 0;

		StringBuilder unmaskedData = new StringBuilder();
				
		// loop to traverse encrypted data, fill in a blank StringBuilder variable with
		// values from data variable
		// with a random integer number subtracted and casted to char type
		for (int index = 0; index < data.length(); index++) {
			// example: 'o' is replaced with ('o' - 14) then cast 97 (which is type int)
			// into a char which is 'a'
			unmaskedData.append((char) (data.charAt(index) - 5));
			
			if (Character.isWhitespace((char) (data.charAt(index) - 5))) {
				wordCount++;
				
				// if the number in wordCount is divisible by 25 (ex. 25, 50, 75, 100, etc.) then add a new line to the file being decrypted
				if (wordCount % 25 == 0) {
					unmaskedData.append('\n');
				} 
			}
		}
					
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

	@Override
	public String toString() {
		return data.toString();
	}
}
