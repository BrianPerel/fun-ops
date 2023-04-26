package com.numbergame;

/**
 * An advanced (harder difficulty version) guessing number game in which the user receives a randomly generated number
 * between 10-999 and he/she must guess what the remainder is. Every correct guess
 * equates to 10 points, every incorrect guess equates to -10 points. Score is
 * kept for every session. <br>
 *
 * @author Brian Perel
 *
 */
public class AdvancedGuessingGame extends GuessingGame {

	public static void main(String[] args) {
		turnOn = false; // workaround to avoid GUI launch issue
		new AdvancedGuessingGame();
	}

	public AdvancedGuessingGame() {
		window.setTitle("Advanced ".concat(window.getTitle()));
		randomNumber = randomGenerator.nextInt(100, 999);
		textFieldRandomNumber.setText(Integer.toString(randomNumber)); // range is set to between 100-999
		lblGuessInstructions.setText(lblGuessInstructions.getText().substring(0, lblGuessInstructions.getText().indexOf("1-99")).concat("100-999 to make 1000"));
		lblGuessInstructions.setLocation(230, 140);
		maxCharsLimit = 3;
		window.setVisible(true); // allow the GUI to be visible only after applying the above changes because otherwise the GUI launches with the base class GUI values and then changes
	}

	@Override
	public void performGuiButtonAction(Object source, boolean isTimeout) {
		super.performGuiButtonAction(source, isTimeout);

		if (source.equals(btnPlayAgain)) {
			randomNumber = randomGenerator.nextInt(100, 999);
			textFieldRandomNumber.setText(Integer.toString(randomNumber));
		}
	}

	@Override
	public void evaluateGuess(boolean isTimeout, final int MAX_LIMIT) {
		super.evaluateGuess(isTimeout, 1000);
	}
}
