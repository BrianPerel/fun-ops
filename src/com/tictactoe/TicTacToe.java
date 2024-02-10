package com.tictactoe;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class TicTacToe {

	private static final Logger LOG = Logger.getLogger(TicTacToe.class.getName());
	protected static final String PLAYER = "Player";
	protected static final String COMPUTER = "Computer";
	protected static final String PLAYER_ONE_SHAPE = "X";
	protected static final String PLAYER_TWO_SHAPE = "O";

	private String playerOnesName;
	private String playerTwosName;
	protected boolean isGameOver;

	public static void main(String[] args) {
		customizeLogger(LOG);

		LOG.info("Starting tic-tac-toe log");
		new StartMenu();
	}

	/**
	 * Customizes the used logger by adding a custom color and having the output display thread name and line numbers
	 */
	protected static void customizeLogger(final Logger LOG) {
		// set the console handler to use the custom formatter
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new Formatter() {
        	@Override
        	 public String format(LogRecord logRecord) {
                 String threadName = "Thread: [" + Thread.currentThread().getName() + "], ";
 				 // ANSI color code for white (\u001B[37m)
                 return "\u001B[37m" + threadName + getLineNumber(logRecord);
             }
        });
        LOG.addHandler(consoleHandler);
	}

	/**
	 * Adds a custom logging component to output the line numbers of log statements.
	 * Extracts the line number where the log statement is declared by analyzing the
	 * stack trace information of the provided argument LogRecord
	 *
	 * @param logRecord object holding metadata about the current logging event
	 * @return the line number where log statement is declared
	 */
	private static String getLineNumber(LogRecord logRecord) {
		try {
			StackTraceElement[] stackTrace = new Throwable().getStackTrace();

			for (StackTraceElement element : stackTrace) {
				if (element.getClassName().equals(logRecord.getSourceClassName())
						&& element.getMethodName().equals(logRecord.getSourceMethodName())) {

					return "Line: [" + element.getLineNumber() + "] - ";
				}
			}
		}
		catch (Exception e) {
			LOG.severe("An exception occurred while getting the line number: " + e.getMessage());
		}

		return "Line: [Line unknown] - ";
	}

	/**
	 * Sets the player names
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
