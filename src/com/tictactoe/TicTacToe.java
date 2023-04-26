package com.tictactoe;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class TicTacToe {

	private static final Logger LOG = Logger.getLogger(TicTacToe.class.getName());

	private String playerOnesName;
	private String playerTwosName;
	protected boolean isGameOver;
	protected final String PLAYER = "Player";
	protected final String COMPUTER = "Computer";
	protected final String PLAYER_ONE_SHAPE = "X";
	protected final String PLAYER_TWO_SHAPE = "O";

	public static void main(String[] args) {
		// set the console handler to use the custom formatter that uses a white text foreground color
		ConsoleHandler consoleHandler = new ConsoleHandler();

		consoleHandler.setFormatter(new Formatter() {
			@Override
			public String format(LogRecord logRecord) {
				// ANSI color code for white (\u001B[37m)
				return "\u001B[37m";
			}
		});

		LOG.addHandler(consoleHandler);
		LOG.info("Starting tic-tac-toe log");

		new StartMenu();
	}

	/**
	 * Sets players names
	 * @param argPlayerOnesName player one's name
	 * @param argPlayerTwosName player two's name
	 */
	public void setPlayerNames(String argPlayerOnesName, String argPlayerTwosName) {
		playerOnesName = argPlayerOnesName;
		playerTwosName = argPlayerTwosName;
	}

	/**
	 * Gets player one's name
	 * @return player one's name
	 */
	public String getPlayerOnesName() {
		return playerOnesName;
	}

	/**
	 * Gets player two's name
	 * @return player two's name
	 */
	public String getPlayerTwosName() {
		return playerTwosName;
	}
}
