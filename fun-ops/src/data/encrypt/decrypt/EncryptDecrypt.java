package data.encrypt.decrypt;

import java.util.Random;

public class EncryptDecrypt implements IEncryptDecrypt {
	
	private String data;
	private boolean encrypted;
	private int shift;
	private Random generator;
	
	public EncryptDecrypt(String data) {
		this.data = data;
		encrypted = false;
		generator = new Random();
		shift = generator.nextInt(10) + 5;
	}

	@Override
	public void encrypt() {
		if(!encrypted) {
			StringBuilder masked = new StringBuilder();
			
			for(int index = 0; index < data.length(); index++) {
				masked.append((char) (data.charAt(index) + shift));
			}
			
			data = masked.toString();
			encrypted = true;
		}
	}

	@Override
	public String decrypt() {
		if(encrypted) {
			StringBuilder unmasked = new StringBuilder();
			
			for (int index = 0; index < data.length(); index++) {
				unmasked.append((char) (data.charAt(index) - shift));
			}
			
			data = unmasked.toString();
			encrypted = false;
		}
		
		return data;		
	}
	
	@Override
	public String toString() {
		return data;
	}
}
