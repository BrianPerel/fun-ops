package pingpong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Ball extends Rectangle {

	Random random;
	int xVelocityOfBall, yVelocityOfBall, initialBallSpeed = 2;

	/**
	 * Creates the ball
	 * @param x-coordinate of the ball
	 * @param y-coordinate of the ball
	 * @param width of the ball
	 * @param height of the ball
	 */
	public Ball(int x, int y, int width, int height) {
		super(x, y, width, height);
		random = new Random();
		int randomXDirection = random.nextInt(2);
		if (randomXDirection == 0) {
			randomXDirection--;
		}
		setXDirection(randomXDirection * initialBallSpeed);
		int randomYDirection = random.nextInt(2);
		if (randomYDirection == 0) {
			randomYDirection--;
		}
		setYDirection(randomYDirection * initialBallSpeed);
	}

	/**
	 * sets the x direction of the ball
	 * @param randomXDirection indicates a random x direction when the game starts
	 */
	public void setXDirection(int randomXDirection) {
		xVelocityOfBall = randomXDirection;
	}

	/**
	 * sets the y direction of the ball
	 * @param randomYDirection indicates a random y direction when the game starts
	 */
	public void setYDirection(int randomYDirection) {
		yVelocityOfBall = randomYDirection;
	}

	/**
	 * Move the pong ball
	 */
	public void move() {
		x += xVelocityOfBall;
		y += yVelocityOfBall;
	}

	/**
	 * Draws the ball animation with given configurations
	 * @param g Graphics
	 */
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.fillOval(x, y, height, width);	
	}
}