package pingpong;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.security.SecureRandom;

import javax.swing.JPanel;

public class GameBoard extends JPanel implements Runnable {

	private static final long serialVersionUID = 1871004171456570750L;
	private static final int GAME_WIDTH = 1000, GAME_HEIGHT = 556, BALL_DIAMETER = 20;
	private static final int PADDLE_WIDTH = 25, PADDLE_HEIGHT = 100;
	private Paddle paddleOne, paddleTwo;
	private Ball pongBall;
	private Score gameScore;
	private Thread gameThread;

	/**
	 * Sets up the game and the GUI
	 */
	public GameBoard() {
		gameThread = new Thread(this);
		gameThread.start();
		gameScore = new Score();
		createPongPaddles();
		createPongBall();
		this.setFocusable(true);
		this.addKeyListener(new ActionListener());
		this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
	}

	/**
	 * Creates the pong ball
	 */
	public void createPongBall() {
		pongBall = new Ball((GAME_WIDTH / 2) - (BALL_DIAMETER / 2),  new SecureRandom().nextInt(GAME_HEIGHT - BALL_DIAMETER),
				BALL_DIAMETER, BALL_DIAMETER);
	}

	/**
	 * Creates the pong game paddles
	 */
	public void createPongPaddles() {
		paddleOne = new Paddle(0, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, "Paddle1");
		paddleTwo = new Paddle(GAME_WIDTH - PADDLE_WIDTH, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH,
				PADDLE_HEIGHT, "Paddle2");
	}

	/**
	 * Paints the panel/board
	 */
	@Override
	public void paint(Graphics g) {
		Image image = createImage(getWidth(), getHeight());
		draw(image.getGraphics());
		g.drawImage(image, 0, 0, this);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Magneto", Font.PLAIN, 40));
	}

	/**
	 * Draws the paddles, ball, and game score
	 * @param g Graphics
	 */
	public void draw(Graphics g) {
		gameScore.draw(g);
		paddleOne.draw(g);
		paddleTwo.draw(g);
		pongBall.draw(g);
	}

	/**
	 * Controls the movement for both paddles and the ball
	 */
	public void move() {
		paddleOne.move();
		paddleTwo.move();
		pongBall.move();
	}

	/**
	 * Detects edges and manages collision
	 */
	public void checkCollision() {

		// bounce ball off top & bottom window edges
		if (pongBall.y <= 0) {
			pongBall.setYDirection(-pongBall.getyVelocityOfBall());
		}

		if (pongBall.y >= GAME_HEIGHT - BALL_DIAMETER) {
			pongBall.setYDirection(-pongBall.getyVelocityOfBall());
		}

		// bounce ball off paddles
		if (pongBall.intersects(paddleOne)) {
			pongBall.setxVelocityOfBall(Math.abs(pongBall.getxVelocityOfBall()));
			pongBall.setxVelocityOfBall(pongBall.getxVelocityOfBall() + 0.2); // optional for more difficulty - increases the balls speed
			
			pongBall.setyVelocityOfBall((pongBall.getyVelocityOfBall() > 0) ? pongBall.getyVelocityOfBall() + 0.2 : pongBall.getyVelocityOfBall() - 1); // optional for more difficulty

			pongBall.setXDirection(pongBall.getxVelocityOfBall());
			pongBall.setYDirection(pongBall.getyVelocityOfBall());
		}

		if (pongBall.intersects(paddleTwo)) {
			pongBall.setxVelocityOfBall(Math.abs(pongBall.getxVelocityOfBall()));
			pongBall.setxVelocityOfBall(pongBall.getxVelocityOfBall() + 1); // optional for more difficulty

			pongBall.setyVelocityOfBall((pongBall.getyVelocityOfBall() > 0) ? pongBall.getyVelocityOfBall() + 1 : pongBall.getyVelocityOfBall() - 1); // optional for more difficulty
			
			pongBall.setXDirection(-pongBall.getxVelocityOfBall());
			pongBall.setYDirection(pongBall.getyVelocityOfBall());
		}

		// stops paddles at window edges
		if (paddleOne.y <= 0) {
			paddleOne.y = 0;
		}
		if (paddleOne.y >= (GAME_HEIGHT - PADDLE_HEIGHT)) {
			paddleOne.y = GAME_HEIGHT - PADDLE_HEIGHT;
		}
		if (paddleTwo.y <= 0) {
			paddleTwo.y = 0;
		}
		if (paddleTwo.y >= (GAME_HEIGHT - PADDLE_HEIGHT)) {
			paddleTwo.y = GAME_HEIGHT - PADDLE_HEIGHT;
		}
		
		// give player 1 a point and create new paddles & ball
		if (pongBall.x <= -22) {
			gameScore.setPlayerTwoScore(gameScore.getPlayerTwoScore() + 1);
			
			try {
				Thread.sleep(300);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
				Thread.currentThread().interrupt();
			}
			
			createPongBall();
		}
		if (pongBall.x >= (GAME_WIDTH - BALL_DIAMETER) + 22) {
			gameScore.setPlayerOneScore(gameScore.getPlayerOneScore() + 1);
			
			try {
				Thread.sleep(300);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
				Thread.currentThread().interrupt();
			}
			
			createPongBall();
		}
	}

	/**
	 * Wait 0.8 of a second, launches a loop that moves everything and checks for collision
	 */
	public void run() {
		try {
			Thread.sleep(800);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
			Thread.currentThread().interrupt();
		}
		
		long lastTime = System.nanoTime();
		double delta = 0;
		
		// game loop keeps the game running
		while (true) {
			long now = System.nanoTime();
			delta += (now - lastTime) / 16666666.66;
			lastTime = now;
			
			if (delta >= 1) {
				move();
				checkCollision();
				repaint();
				delta--;
			}
		}
	}

	/**
	 * A action listener class that listens to keys pressed and released on the pong table
	 */
	private class ActionListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			paddleOne.keyPressed(e);
			paddleTwo.keyPressed(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			paddleOne.keyReleased(e);
			paddleTwo.keyReleased(e);
		}
	}	
}