package data.encrypt.decrypt;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Class performs encryption and decryption process
 * @author Brian Perel
 *
 */
public class EncryptDecrypt {
	
	private String data; // field to hold data from file provided by user 
	private boolean encrypted; // flag to tell if encryption has already occured or not 
	private int shift; // shift field used to add a random number value during data String traversal 
	private Random generator; // random class generator
	
	public EncryptDecrypt(String data) {
		this.data = data;
		encrypted = false;
		generator = new Random();
		shift = generator.nextInt(10) + 5;
	}

	/**
	 * Encrypt data given through constructor set into data field variable. Validate that encryption process hasn't already occurred.
	 * Create blank variable to store line of random characters. Traverse loop taking character at current index + a random number, 
	 * casting to char type, and appending to blank variable. Write new line of data to file
	 * @throws IOException 
	 */
	public void encrypt() throws IOException {
		if(!encrypted) { // checks if encryption process has already occurred. Since you can't encrypt encrypted data  
			StringBuilder masked = new StringBuilder(""); // create blank variable that will store a line of random characters same length as original sentence in txt file 
			
			// loop to traverse data String provided by user, in order to replace every character
			// into a random integer number casted to char type 
			for(int index = 0; index < data.length(); index++) {
				masked.append((char) (data.charAt(index) + shift));
			}
			
			// cast the StringBuilder into a String 
			data = masked.toString();
			
			FileWriter myWriter = new FileWriter(App.fileName); // access the existing txt file 

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
	 * Validate that encryption process has occurred. Create a new blank variable to store original text. 
	 * Traverse loop taking character at current index location - random number, cast to char type, and append to blank variable.
	 * Write new random character filled txt line to file   
	 * @return
	 * @throws IOException
	 */
	public String decrypt() throws IOException {
		// checks to make sure encryption has already occured. Otherwise it won't be possible to decrypt un-encrypted data 
		if(encrypted) {
			StringBuilder unmasked = new StringBuilder();
			
			// loop to traverse encrypted data, fill in a blank StringBuilder variable with values from data variable 
			// into a random integer number casted to char type 
			for (int index = 0; index < data.length(); index++) {
				unmasked.append((char) (data.charAt(index) - shift));
			}
			
			// cast the StringBuilder into a String 
			data = unmasked.toString();
			
			FileWriter myWriter = new FileWriter(App.fileName);

			try {
				myWriter.write(data);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			myWriter.close();
			
			encrypted = false;
		}
		
		return data;		
	}
	
	@Override
	public String toString() {
		return data;
	}
}
