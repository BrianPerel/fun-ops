package com.numbergame;

/**
 * An advanced guessing number game in which the user receives a randomly generated number
 * between 10-999 and he/she must guess what the remainder is. Every correct guess
 * equates to 10 points, every incorrect guess equates to -10 points. Score is
 * kept for every session. <br>
 * @author Brian
 *
 */
public class AdvancedGuessingGame extends GuessingGame {

	public static void main(String[] args) {
		new AdvancedGuessingGame();
	}
	
	public AdvancedGuessingGame() {
		frame.setTitle("Advanced " + frame.getTitle());
		randomNumber = randomGenerator.nextInt(899) + 100;
		textFieldRandomNumber.setText(Integer.toString(randomNumber)); // range is set to between 100-999
		lblGuess.setText("Enter a number b/w 100-999 to make 1000");
		lblGuess.setLocation(230, 140);
		maxChars = 3;
	}
	
	@Override
	public void performGuiButtonAction(Object source, boolean isTimeout) {
		
		super.performGuiButtonAction(source, isTimeout);
		
		if (source == btnPlayAgain) {
			randomNumber = randomGenerator.nextInt(899) + 100;
			textFieldRandomNumber.setText(Integer.toString(randomNumber));
		}
	}
	
	@Override
	public void evaluateGuess(boolean isTimeout, final int MAX_LIMIT) {	
		super.evaluateGuess(isTimeout, 1000);
	}
}
