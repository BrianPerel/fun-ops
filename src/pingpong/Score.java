package pingpong;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Score {

	int playerOneScore, playerTwoScore;

	/**
	 * Draws the white middle dividing game line, draws the score labels for player1 and player2, sets font 
	 * @param g Graphics
	 */
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("Magneto", Font.PLAIN, 40));
		g.drawLine(500, 0, 500, 556);	
		g.drawLine(0, 278, 1000, 278);
		g.drawString("P1:".concat(String.valueOf(playerOneScore / 10)).concat(String.valueOf(playerOneScore % 10)), 170, 50);
		g.drawString("P2:".concat(String.valueOf(playerTwoScore / 10)).concat(String.valueOf(playerTwoScore % 10)), 690, 50);
	}
}
