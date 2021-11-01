package pingpong;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JPanel;

public class GameBoard extends JPanel implements Runnable {

	static final int GAME_WIDTH = 1000, GAME_HEIGHT = 556, BALL_DIAMETER = 20;
	static final int PADDLE_WIDTH = 25, PADDLE_HEIGHT = 100;
	Paddle paddleOne, paddleTwo;
	Image image;
	Ball pongBall;
	Score gameScore;
	Thread gameThread;
	boolean isGamePaused;

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
		pongBall = new Ball((GAME_WIDTH / 2) - (BALL_DIAMETER / 2),  new Random().nextInt(GAME_HEIGHT - BALL_DIAMETER),
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
		image = createImage(getWidth(), getHeight());
		draw(image.getGraphics());
		g.drawImage(image, 0, 0, this);
		g.setColor(Color.white);
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
			pongBall.setYDirection(-pongBall.yVelocityOfBall);
		}

		if (pongBall.y >= GAME_HEIGHT - BALL_DIAMETER) {
			pongBall.setYDirection(-pongBall.yVelocityOfBall);
		}

		// bounce ball off paddles
		if (pongBall.intersects(paddleOne)) {
			pongBall.xVelocityOfBall = Math.abs(pongBall.xVelocityOfBall);
			pongBall.xVelocityOfBall++; // optional for more difficulty - increases the balls speed
			
			if (pongBall.yVelocityOfBall > 0) {
				pongBall.yVelocityOfBall++; // optional for more difficulty
			} else {
				pongBall.yVelocityOfBall--;
			}

			pongBall.setXDirection(pongBall.xVelocityOfBall);
			pongBall.setYDirection(pongBall.yVelocityOfBall);
		}

		if (pongBall.intersects(paddleTwo)) {
			pongBall.xVelocityOfBall = Math.abs(pongBall.xVelocityOfBall);
			pongBall.xVelocityOfBall++; // optional for more difficulty

			if (pongBall.yVelocityOfBall > 0) {
				pongBall.yVelocityOfBall++; // optional for more difficulty
			} else {
				pongBall.yVelocityOfBall--;
			}
			
			pongBall.setXDirection(-pongBall.xVelocityOfBall);
			pongBall.setYDirection(pongBall.yVelocityOfBall);
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
		if (pongBall.x <= 0) {
			gameScore.playerTwoScore++;
			createPongBall();
		}
		if (pongBall.x >= GAME_WIDTH - BALL_DIAMETER) {
			gameScore.playerOneScore++;
			createPongBall();
		}
	}

	public void run() {
		// launch the gui but wait 1 second
		try {
			Thread.sleep(800);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// game loop
		long lastTime = System.nanoTime();
		double delta = 0;
		// loop keeps the game running
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

	public class ActionListener extends KeyAdapter {
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