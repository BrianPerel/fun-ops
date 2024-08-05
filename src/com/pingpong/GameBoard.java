package com.pingpong;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serial;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GameBoard extends JPanel implements Runnable, Serializable {

	@Serial
	private static final long serialVersionUID = 1871004171456570750L;

	private static final int GAME_WIDTH = 1000, GAME_HEIGHT = 556, BALL_DIAMETER = 20;
	private static final int PADDLE_WIDTH = 25, PADDLE_HEIGHT = 100;
    private static final List<Color> ballColors =
    		List.copyOf(Set.of(Color.CYAN, Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.ORANGE, Color.RED));
    private static final SecureRandom randomGenerator = new SecureRandom(
			LocalDateTime.now().toString().getBytes(StandardCharsets.US_ASCII));

	private Paddle paddleOne, paddleTwo;
	private Ball pongBall;
	private Color color;
	private final Score gameScore;

	/**
	 * Sets up the game and the GUI
	 */
	public GameBoard() {
		Thread gameThread = new Thread(this);
		gameThread.start();
		gameScore = new Score();
		createPongPaddles();
		createPongBall();
		this.setFocusable(true);
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				paddleOne.keyWasPressed(e);
				paddleTwo.keyWasPressed(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				paddleOne.keyWasReleased(e);
				paddleTwo.keyWasReleased(e);
			}
		});

		this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
	}

	/**
	 * Creates the pong ball
	 */
	private void createPongBall() {
		// if game just started, the pong ball will be white
		if(gameScore.getPlayerOneScore() == 0 && gameScore.getPlayerTwoScore() == 0) {
			color = Color.WHITE;
		}
		// if score of either player hits a multiple of 5 then make special color
		else if((gameScore.getPlayerOneScore() != 0 && gameScore.getPlayerTwoScore() != 0) &&
				(gameScore.getPlayerOneScore() % 5 == 0 || gameScore.getPlayerTwoScore() % 5 == 0)) {
			color = ballColors.get(randomGenerator.nextInt(ballColors.size()));
		}

		pongBall = new Ball((GAME_WIDTH / 2) - (BALL_DIAMETER / 2), new SecureRandom(
				LocalDateTime.now().toString().getBytes(StandardCharsets.US_ASCII)).nextInt(GAME_HEIGHT - BALL_DIAMETER),
				BALL_DIAMETER, BALL_DIAMETER, color);
	}

	/**
	 * Creates the pong game paddles
	 */
	private void createPongPaddles() {
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
		drawGamePieces(image.getGraphics());
		g.drawImage(image, 0, 0, this);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Magneto", Font.PLAIN, 40));
	}

	/**
	 * Draws the paddles, ball, and game score
	 * @param g Graphics
	 */
	private void drawGamePieces(Graphics g) {
		gameScore.draw(g);
		paddleOne.drawPaddles(g);
		paddleTwo.drawPaddles(g);
		pongBall.drawBall(g);
	}

	/**
	 * Controls the movement for both paddles and the ball
	 */
	private void moveGamePieces() {
		paddleOne.movePaddles();
		paddleTwo.movePaddles();
		pongBall.moveBall();
	}

	/**
	 * Detects edges and manages collision
	 */
	private void checkForCollision() {
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

		checkForEdges(paddleOne);
		checkForEdges(paddleTwo);

		// give player 1 a point and create new paddles & ball
		if (pongBall.x <= -22) {
			gameScore.setPlayerTwoScore(gameScore.getPlayerTwoScore() + 1);

			try {
				TimeUnit.MILLISECONDS.sleep(300L);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
				Thread.currentThread().interrupt();
			}

			createPongBall();
		}
		if (pongBall.x >= (GAME_WIDTH - BALL_DIAMETER) + 22) {
			gameScore.setPlayerOneScore(gameScore.getPlayerOneScore() + 1);

			try {
				TimeUnit.MILLISECONDS.sleep(300L);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
				Thread.currentThread().interrupt();
			}

			createPongBall();
		}

		// player scores should not exceed 10000 as if it does then displaying of the scores will cause offset in GUI layout
		if(gameScore.getPlayerOneScore() >= 10000 || gameScore.getPlayerTwoScore() >= 10000) {
			gameOver();
		}
	}

	private void gameOver() {
		String message;

		if(gameScore.getPlayerOneScore() > gameScore.getPlayerTwoScore()) {
			message = "Player 1 wins!";
		}
		else if(gameScore.getPlayerTwoScore() > gameScore.getPlayerOneScore()) {
			message = "Player 2 wins!";
		}
		else {
			message = "It's a draw!";
		}

		JOptionPane.showMessageDialog(this, message, "Game Over",
				JOptionPane.INFORMATION_MESSAGE);

		System.exit(0);
	}

	private void checkForEdges(Paddle paddleNumber) {
		// stops paddles at window edges
		if (paddleNumber.y <= 0) {
			paddleNumber.y = 0;
		}

		if (paddleNumber.y >= (GAME_HEIGHT - PADDLE_HEIGHT)) {
			paddleNumber.y = GAME_HEIGHT - PADDLE_HEIGHT;
		}
	}

	/**
	 * Wait 0.8 of a second, launches a loop that moves everything and checks for collision
	 */
	@Override
	public void run() {
		try {
			TimeUnit.MILLISECONDS.sleep(300L);
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
				moveGamePieces();
				checkForCollision();
				repaint();
				delta--;
			}
		}
	}
}