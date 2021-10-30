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
		g.drawLine(500, 0, 500, 556); // vertical line	
		g.drawLine(0, 278, 1000, 278); // horizontal line

		String p1 = "P1:".concat(String.valueOf(playerOneScore / 10)).concat(String.valueOf(playerOneScore % 10)),
				p2 = "P2:".concat(String.valueOf(playerTwoScore / 10)).concat(String.valueOf(playerTwoScore % 10));
		
		// if score is below 10 show only single digit for player scores
		if(playerOneScore < 10 || playerTwoScore < 10) {
			g.drawString(p1.substring(0, 3).concat(p1.substring(4, 5)), 170, 50);
			g.drawString(p2.substring(0, 3)
					.concat(p2.substring(4, 5)), 690, 50);
		} else {
			g.drawString(p1, 170, 50);
			g.drawString(p2, 690, 50);
		}
	}
}
