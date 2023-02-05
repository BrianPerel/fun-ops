package com.pingpong;

import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_S;
import static java.awt.event.KeyEvent.VK_UP;
import static java.awt.event.KeyEvent.VK_W;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Objects;

public class Paddle extends Rectangle {

	private static final long serialVersionUID = -7099975578602496893L;

	private int yVelocity, speed = 10;
	private String id;

	public Paddle(int xCoordinate, int yCoordinate, final int PADDLE_WIDTH, final int PADDLE_HEIGHT, String id) {
		super(xCoordinate, yCoordinate, PADDLE_WIDTH, PADDLE_HEIGHT);
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(id, speed, yVelocity);
		return result;
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

		Paddle other = (Paddle) obj;
		return Objects.equals(id, other.id) && speed == other.speed && yVelocity == other.yVelocity;
	}

	/**
	 * Controls the movement of the paddles when a key is pressed
	 *
	 * @param e the key event
	 */
	public void keyWasPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();

		if ("Paddle1".equals(id)) {
			if (keyCode == VK_W) {
				setYDirection(-speed);
			}

			if (keyCode == VK_S) {
				setYDirection(speed);
			}
		}
		else if ("Paddle2".equals(id)) {
			if (keyCode == VK_UP) {
				setYDirection(-speed);
			}

			if (keyCode == VK_DOWN) {
				setYDirection(speed);
			}
		}
	}

	/**
	 * Controls the movement of the paddles when a key is released
	 *
	 * @param e the key event
	 */
	public void keyWasReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();

		if ("Paddle1".equals(id)) {
			if (keyCode == VK_W) {
				setYDirection(0);
			}
			if (keyCode == VK_S) {
				setYDirection(0);
			}
		}
		else if ("Paddle2".equals(id)) {
			if (keyCode == VK_UP) {
				setYDirection(0);
			}
			if (keyCode == VK_DOWN) {
				setYDirection(0);
			}
		}
	}

	/**
	 * Sets the y-coordinate direction of specified paddle
	 *
	 * @param yDirection
	 */
	private void setYDirection(int yDirection) {
		yVelocity = yDirection;
	}

	/**
	 * Moves the specified paddle
	 */
	public void move() {
		y += yVelocity;
	}

	/**
	 * Draws the game paddles
	 * @param g Graphics
	 */
	public void draw(Graphics g) {
		g.setColor("Paddle1".equals(id) ? Color.RED : Color.BLUE);
		g.fillOval(x, y, width, height);
	}
}
