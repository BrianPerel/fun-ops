package data.encrypt.decrypt;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class EncryptDecrypt implements IEncryptDecrypt {
	
	private String data; // field to hold data from file provided by user 
	private boolean encrypted; // flag to tell if encryption has already occured or not 
	private int shift; // shift field used to add a random number value during data String traversal 
	private Random generator;
	
	public EncryptDecrypt(String data) {
		this.data = data;
		encrypted = false;
		generator = new Random();
		shift = generator.nextInt(10) + 5;
	}

	/**
	 * encrypt data given through constructor set to data field.
	 * Traverse data field String 
	 */
	@Override
	public void encrypt() {
		if(!encrypted) { // checks if encryption process has already occurred. Since you can't encrypt encrypted data  
			StringBuilder masked = new StringBuilder("");
			
			// loop to traverse data String provided by user, in order to replace every character
			// into a random integer number casted to char type 
			for(int index = 0; index < data.length(); index++) {
				masked.append((char) (data.charAt(index) + shift));
			}
			
			// cast the StringBuilder into a String 
			data = masked.toString();
			
			try {
				FileWriter myWriter = new FileWriter("secret.txt");
				myWriter.write(data);
				myWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			 
			encrypted = true;
		}
	}

	@Override
	public String decrypt() {
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
			
			try {
				FileWriter myWriter = new FileWriter("secret.txt");
				myWriter.write(data);
				myWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			encrypted = false;
		}
		
		return data;		
	}
	
	@Override
	public String toString() {
		return data;
	}
}
