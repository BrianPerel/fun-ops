package numbergame;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;

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
	public void performGuiButtonAction(Object source, boolean outOfTimeFlag) {
		
		super.performGuiButtonAction(source, outOfTimeFlag);
		
		if (source == btnPlayAgain) {
			randomNumber = randomGenerator.nextInt(899) + 100;
			textFieldRandomNumber.setText(Integer.toString(randomNumber));
		}
	}
	
	@Override
	public void evaluateGuess(boolean outOfTimeFlag) {		
		
		totalGuessesMade++;
				
		int textFieldGuessTheNumberInt = Integer.parseInt(textFieldGuessTheNumber.getText());

		// if input remainder entered is outside of range 100-999
		if (textFieldGuessTheNumberInt >= 999 || textFieldGuessTheNumberInt <= 100) {
			
			playSound(FAIL_SOUND);

			textFieldGuessTheNumber.setBorder(BorderFactory.createLineBorder(Color.red, 2));
			JOptionPane.showMessageDialog(frame.getComponent(0), "Please enter a valid number (100-999)");

		} else if (textFieldGuessTheNumberInt + randomNumber == 1000) {
			
			playSound("res/audio/win.wav");
			Color lightGreen = new Color(50, 205, 50);
			textFieldGuessTheNumber.setBorder(BorderFactory.createLineBorder(lightGreen, 2));
			JOptionPane.showMessageDialog(frame.getComponent(0), "Correct! You made 1000");
			randomNumber = randomGenerator.nextInt(899) + 100;
			textFieldRandomNumber.setText(Integer.toString(randomNumber));
			
			if (!outOfTimeFlag) {
				totalGameScore += 10;
			}

		} else if (textFieldGuessTheNumberInt + randomNumber != 1000) {
			
			playSound(FAIL_SOUND);

			textFieldGuessTheNumber.setBorder(BorderFactory.createLineBorder(Color.red, 2));
			JOptionPane.showMessageDialog(frame.getComponent(0), "Incorrect! That doesn't sum to 1000");
			
			if (totalGameScore != 0) {
				totalGameScore -= 10;
			}
		}
		
		textFieldGuessTheNumber.setBorder(BorderFactory.createLineBorder(Color.black, 1));

		// set score after action is completed
		textFieldScore.setText(Integer.toString(totalGameScore));
		guessesTextField.setText(Integer.toString(totalGuessesMade));
	}
}
