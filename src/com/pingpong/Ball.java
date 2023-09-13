package com.pingpong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.Serial;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Objects;

public class Ball extends Rectangle {

	@Serial
	private static final long serialVersionUID = 1L;
	private static final double INITIAL_BALL_SPEED = 2;

	private double xVelocityOfBall;
	private double yVelocityOfBall;
	private final Color ballColor;

	/**
	 * Creates the ball
	 * @param x the x-coordinate of the ball
	 * @param y the y-coordinate of the ball
	 * @param width the width of the ball
	 * @param height the height of the ball
	 * @param argColor the color of the ball
	 */
	public Ball(int x, int y, int width, int height, Color argColor) {
		super(x, y, width, height);
		SecureRandom random = new SecureRandom(LocalDateTime.now().toString().getBytes(StandardCharsets.US_ASCII));
		int randomXDirection = random.nextInt(2);
		ballColor = argColor;

		if (randomXDirection == 0) {
			randomXDirection--;
		}

		setXDirection(randomXDirection * INITIAL_BALL_SPEED);
		int randomYDirection = random.nextInt(2);

		if (randomYDirection == 0) {
			randomYDirection--;
		}

		setYDirection(randomYDirection * INITIAL_BALL_SPEED);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		return prime * super.hashCode() + Objects.hash(INITIAL_BALL_SPEED, xVelocityOfBall, yVelocityOfBall);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		Ball other = (Ball) obj;
		return java.lang.Double.doubleToLongBits(INITIAL_BALL_SPEED) == java.lang.Double
				.doubleToLongBits(Ball.INITIAL_BALL_SPEED)
				&& java.lang.Double.doubleToLongBits(xVelocityOfBall) == java.lang.Double
						.doubleToLongBits(other.xVelocityOfBall)
				&& java.lang.Double.doubleToLongBits(yVelocityOfBall) == java.lang.Double
						.doubleToLongBits(other.yVelocityOfBall);
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
		g.setColor(ballColor);
		g.fillOval(x, y, width, height);
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
	 * @return y velocity
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