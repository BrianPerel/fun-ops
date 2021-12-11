package pingpong;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Score {

	private int playerOneScore, playerTwoScore;

	/**
	 * Draws the white middle dividing game line, draws the score labels for player1 and player2, sets font 
	 * @param g Graphics
	 */
	public void draw(Graphics g) {
		String p1, p2;
		g.setColor(Color.white);
		g.setFont(new Font("Magneto", Font.PLAIN, 40));
		g.drawLine(0, 278, 1000, 278); // main horizontal line
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(4));
		g.drawLine(500, 0, 500, 556); // main vertical line	
        g2.setStroke(new BasicStroke(7));
		g.drawLine(2, 0, 2, 556); // vertical line	
		g.drawLine(997, 0, 997, 556); // vertical line	
		g.drawLine(0, 552, 1000, 552); // horizontal line
		g.drawLine(0, 2, 1000, 2); // horizontal line

		p1 = "P1: ".concat(String.valueOf(getPlayerOneScore() / 10)).concat(String.valueOf(getPlayerOneScore() % 10));
		p2 = "P2: ".concat(String.valueOf(getPlayerTwoScore() / 10)).concat(String.valueOf(getPlayerTwoScore() % 10));
		
		// if score is below 10 show only single digit for player scores
		if(getPlayerOneScore() < 10 || getPlayerTwoScore() < 10) {
			g.drawString(p1.substring(0, 4).concat(p1.substring(5, 6)), 170, 50);
			g.drawString(p2.substring(0, 4).concat(p2.substring(5, 6)), 690, 50);
		} else {
			g.drawString(p1, 170, 50);
			g.drawString(p2, 690, 50);
		}
	}

	/**
	 * Gets the current score for player one
	 * @return player one's current score
	 */
	public int getPlayerOneScore() {
		return playerOneScore;
	}

	/**
	 * Sets the current score for player one 
	 * @param playerOneScore Player one's current score
	 */
	public void setPlayerOneScore(int playerOneScore) {
		this.playerOneScore = playerOneScore;
	}

	/**
	 * Gets the current score for player two
	 * @return player two's current score
	 */
	public int getPlayerTwoScore() {
		return playerTwoScore;
	}

	/**
	 * Sets the current score for player two
	 * @param playerTwoScore Player two's current score
	 */
	public void setPlayerTwoScore(int playerTwoScore) {
		this.playerTwoScore = playerTwoScore;
	}
}
