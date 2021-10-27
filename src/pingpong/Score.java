package pingpong;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Score {

	static int GAME_WIDTH = 1000, GAME_HEIGHT = 556;
	int playerOneScore, playerTwoScore;

	/**
	 * Draws the white middle dividing game line, draws the score labels for player1 and player2, sets font 
	 * @param g Graphics
	 */
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("Magneto", Font.PLAIN, 40));
		g.drawLine(GAME_WIDTH / 2, 0, GAME_WIDTH / 2, GAME_HEIGHT);	
		g.drawLine(0, GAME_HEIGHT / 2, GAME_WIDTH, GAME_HEIGHT / 2);
		g.drawString("P1:".concat(String.valueOf(playerOneScore / 10)).concat(String.valueOf(playerOneScore % 10)).substring(0, 3).concat(
				"P1:".concat(String.valueOf(playerOneScore / 10)).concat(String.valueOf(playerOneScore % 10)).substring(4, 5)), (GAME_WIDTH / 2) - 330, 50);
		g.drawString("P2:".concat(String.valueOf(playerTwoScore / 10)).concat(String.valueOf(playerTwoScore % 10)).substring(0, 3).concat(
				"P2:".concat(String.valueOf(playerTwoScore / 10)).concat(String.valueOf(playerTwoScore % 10)).substring(4, 5)), (GAME_WIDTH / 2) + 190, 50);
	}
}
