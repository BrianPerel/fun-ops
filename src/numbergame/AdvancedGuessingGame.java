package numbergame;

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
		frame.setTitle("Advanced Number Guessing Game by: Brian Perel");
		randomNumber = randomGenerator.nextInt(899) + 100; // range is set to between 100-999
		textFieldRandomNumber.setText(Integer.toString(randomNumber));
		lblGuess.setText("Enter a number b/w 100-999 to make 1000");
		lblGuess.setBounds(230, 140, 275, 37);
	}
	
	@Override
	public void performGuiButtonAction(Object source, boolean isOutOfTime) {
		
		super.performGuiButtonAction(source, isOutOfTime);
		
		if (source == btnPlayAgain) {
			randomNumber = randomGenerator.nextInt(899) + 100;
			textFieldRandomNumber.setText(Integer.toString(randomNumber));
		}
	}
	
	@Override
	public void evaluateGuess(boolean isOutOfTime, final int MAX_LIMIT) {	
		super.evaluateGuess(isOutOfTime, 1000);
	}
}
