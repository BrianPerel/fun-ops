package pingpong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.security.SecureRandom;

public class Ball extends Rectangle {

	private static final long serialVersionUID = 1L;
	private double xVelocityOfBall, yVelocityOfBall, initialBallSpeed = 2;

	/**
	 * Creates the ball
	 * @param x the x-coordinate of the ball
	 * @param y the y-coordinate of the ball
	 * @param width the width of the ball
	 * @param height the height of the ball
	 */
	public Ball(int x, int y, int width, int height) {
		super(x, y, width, height);
		SecureRandom random = new SecureRandom();
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
	public void setXDirection(double randomXDirection) {
		setxVelocityOfBall(randomXDirection);
	}

	/**
	 * sets the y direction of the ball
	 * @param randomYDirection indicates a random y direction when the game starts
	 */
	public void setYDirection(double randomYDirection) {
		setyVelocityOfBall(randomYDirection);
	}

	/**
	 * Move the pong ball
	 */
	public void move() {
		x += getxVelocityOfBall();
		y += getyVelocityOfBall();
	}

	/**
	 * Draws the ball animation with given configurations
	 * @param g Graphics
	 */
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.fillOval(x, y, height, width);	
	}

	/**
	 * Gets the x velocity of the game ball
	 * @return xVelocityOfBall
	 */
	public double getxVelocityOfBall() {
		return xVelocityOfBall;
	}

	/**
	 * Sets the x velocity of the game ball
	 * @param xVelocityOfBall
	 */
	public void setxVelocityOfBall(double xVelocityOfBall) {
		this.xVelocityOfBall = xVelocityOfBall;
	}

	/**
	 * Gets the y velocity of the game ball
	 * @return
	 */
	public double getyVelocityOfBall() {
		return yVelocityOfBall;
	}

	/**
	 * Sets the y velocity of the game ball
	 * @param yVelocityOfBall
	 */
	public void setyVelocityOfBall(double yVelocityOfBall) {
		this.yVelocityOfBall = yVelocityOfBall;
	}
}