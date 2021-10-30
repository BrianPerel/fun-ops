package pingpong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class Paddle extends Rectangle {

	String id;
	int yVelocity, speed = 10;

	public Paddle(int x, int y, int PADDLE_WIDTH, int PADDLE_HEIGHT, String id) {
		super(x, y, PADDLE_WIDTH, PADDLE_HEIGHT);
		this.id = id;
	}

	/**
	 * Controls the movement of the paddles when a key is pressed
	 * @param e
	 */
	public void keyPressed(KeyEvent e) {
		switch (id) {
			case "Paddle1": {
				if (e.getKeyCode() == KeyEvent.VK_W) {
					setYDirection(-speed);
				}
	
				if (e.getKeyCode() == KeyEvent.VK_S) {
					setYDirection(speed);
				}
				break;
			}
			case "Paddle2": {
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					setYDirection(-speed);
				}
	
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					setYDirection(speed);
				}
				break;
			}
		}
	}

	/**
	 * Controls the movement of the paddles when a key is released
	 * @param e
	 */
	public void keyReleased(KeyEvent e) {
		switch (id) {
			case "Paddle1": {
				if (e.getKeyCode() == KeyEvent.VK_W) {
					setYDirection(0);
				}
				if (e.getKeyCode() == KeyEvent.VK_S) {
					setYDirection(0);
				}
				break;
			}
	
			case "Paddle2": {
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					setYDirection(0);
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					setYDirection(0);
				}
				break;
			}
		}
	}

	/**
	 * Sets the y-coordinate direction of specified paddle
	 * @param yDirection
	 */
	public void setYDirection(int yDirection) {
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
	 * @param g
	 */
	public void draw(Graphics g) {		
		if (id.equals("Paddle1")) {
			g.setColor(Color.blue);
		} else {
			g.setColor(Color.red);
		}
				
		g.fillRect(x, y, width, height);
	}
}
