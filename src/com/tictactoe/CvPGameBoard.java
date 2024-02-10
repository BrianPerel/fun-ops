package com.tictactoe;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Implementation for a 3x3 tic-tac-toe game board. Initiates the game and functionality to support the human player
 * vs computer game mode where the human player will go first<br>
 *
 * @author Brian Perel
 *
 */
public class CvPGameBoard extends PvPGameBoard implements ActionListener {

	// using a seed value derived from the current date and time, encoded as a byte array using the ASCII character set.
	// By using the current date and time as the seed, the random number generator is initialized with a value that is likely to be unique to this
	// instance and time, making it more difficult to predict or reproduce the sequence of random numbers it generates
	private static final SecureRandom randomGenerator = new SecureRandom(
			LocalDateTime.now().toString().getBytes(StandardCharsets.US_ASCII));
	private static final Logger LOG = Logger.getLogger(CvPGameBoard.class.getName());
	private static final String PLAYER_SHAPE = TicTacToe.PLAYER_ONE_SHAPE;
	private static final String COMPUTER_SHAPE = TicTacToe.PLAYER_TWO_SHAPE;

	private int randomCell;
	private int[] freeEmptyTiles; // array of empty tiles to indicate to AI what buttons are available to click
	private boolean invalidMoveSelected; // enforces the computer to not click if user clicked on an invalid tile
	private boolean shouldRun; // enforces the computer to only do 1 click inside the new thread

	/**
	 * Builds the game's GUI board
	 *
	 * @param argIsStart         boolean flag indicating whether the game has just
	 *                  begun
	 * @param argIsPlayerOnesTurn boolean flag indicating if it's player one's turn in the
	 *                  game
	 */
	public CvPGameBoard(boolean argIsStart, boolean argIsPlayerOnesTurn, TicTacToe argTicTacToeGame) {
		super(argIsStart, argIsPlayerOnesTurn, argTicTacToeGame);

		if(LOG.getHandlers().length == 0 ||
		        !LOG.getHandlers()[0].getFormatter().format(new LogRecord(Level.ALL, "")).contains("Thread")) {
			TicTacToe.customizeLogger(LOG);
		}

		ticTacToeGame.setPlayerNames(TicTacToe.PLAYER, TicTacToe.COMPUTER);
		this.initializeGame(argIsStart, argIsPlayerOnesTurn);

		lblPlayersTurn.setText(String.format("%s's turn (%s):", ticTacToeGame.getPlayerOnesName(), PLAYER_SHAPE));
	}

	/**
	 * Constructs a CvPGameBoard object with the specified parameters, allowing customization of the initial game state and window location.
	 *
	 * @param argIsStart             Boolean flag indicating whether the game has just begun
	 * @param argIsPlayerOnesTurn    Boolean flag indicating if it's Player One's turn in the game
	 * @param setLocationToHere      String representing the desired window location in the format "x,y"
	 * @param argTicTacToeGame       Instance of the TicTacToe class managing the game logic
	 */
	public CvPGameBoard(boolean argIsStart, boolean argIsPlayerOnesTurn, String setLocationToHere, TicTacToe argTicTacToeGame) {
		this(argIsStart, argIsPlayerOnesTurn, argTicTacToeGame);
		window.setLocation(Integer.parseInt(setLocationToHere.split(",")[0]), Integer.parseInt(setLocationToHere.split(",")[1]));
	}

	@Override
	protected void initializeGame(boolean argIsStart, boolean argIsPlayerOnesTurn) {
		super.initializeGame(argIsStart, argIsPlayerOnesTurn);

		freeEmptyTiles = new int[9];

		for (int x = 0; x < freeEmptyTiles.length; x++) {
			freeEmptyTiles[x] = x;
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		isCvPGame = true;

		// scans through the game board and performs all actions needed to complete a player's turn
		for (int x = 0; x < gameBoardTiles.length; x++) {

			if ((ae.getSource().equals(gameBoardTiles[x])) && gameBoardTiles[x].getText().isEmpty()) {
				super.makeMove(isCvPGame, gameBoardTiles[x],
					(isPlayerOnesTurn) ? LIGHT_RED_COLOR : Color.BLUE,
					(isPlayerOnesTurn) ? COMPUTER_SHAPE : PLAYER_SHAPE,
					(isPlayerOnesTurn) ? ticTacToeGame.getPlayerOnesName() : ticTacToeGame.getPlayerTwosName());

				shouldRun = !isPlayerOnesTurn;

				freeEmptyTiles[x] = -99; // prevents computer from choosing a tile that player has chosen
				isPlayerOnesTurn = !isPlayerOnesTurn;

				break;
			}
			// if you try to select a tile that is not empty
			else if (ae.getSource().equals(gameBoardTiles[x]) && !gameBoardTiles[x].getText().isEmpty()) {
				invalidMoveSelected = true;
				LOG.warning("Invalid Move.");
				Toolkit.getDefaultToolkit().beep();
			}
		}

		makeMoveComputer();
		freeEmptyTiles[randomCell] = -99; // prevents tic-tac-toe AI from choosing the same button it just clicked on it's next turn
	}

	/**
	 * Creates a new thread when it's the tic-tac-toe AI's turn to have it select a free/empty tile on the gameboard
	 */
	private void makeMoveComputer() {
		// toRun enforces the computer to only do 1 click inside the new thread
		if (shouldRun && !invalidMoveSelected) {
			/*
			 * create another thread and have doClick() called from within that new thread. This is needed because
			 * doClick method's timeout gets checked inside the event thread, so it won't get released
			 * until the parent calling method (actionPerformed) exits (and so the event thread can continue its event processing)
			 *
			 * relates to: <a href="https://stackoverflow.com/questions/9866456/doclick-not-releasing-keys-until-loop-ends">Stack overflow post</a>
			 */
			new Thread(this::doClickThread).start();
		}

		invalidMoveSelected = false;
	}

	/**
	 * Assists the AI in choosing the best move to make in it's current turn by calling makeBestMoveComputer().
	 * This secondary thread calls doClick on the randomly picked game board tile
	 */
	private synchronized void doClickThread() {
		Thread.currentThread().setName("Computer-AI");
		makeBestMoveComputer();

		try {
			TimeUnit.MILLISECONDS.sleep(300L);
		} catch (InterruptedException ie) {
			LOG.severe("Error: " + ie);
			ie.printStackTrace();
			Thread.currentThread().interrupt();
		}

		// solution to doClick() not releasing button until the parent calling method
		// (actionPerformed) finishes: doClick was moved to another thread (to this thread)
		gameBoardTiles[randomCell].doClick();
	}

	/**
	 * Assists the AI in choosing the best move to make in it's current turn. This is done by scanning the board to see if other
	 * player can win in their next move. If so code will block that combo from completing by placing AI's game shape there
	 */
	private void makeBestMoveComputer() {
		for(int x = 0; x < tile.length; x++) {
			tile[x] = gameBoardTiles[x].getText();
		}

		do {
			int tmp = canPlayerWinInNextMove(tile);

			if (tmp != -99 && Arrays.stream(freeEmptyTiles).anyMatch(tileNumber -> tileNumber == tmp)) {
				// loop through int array if tmp variable is in the array (meaning it's a free empty cell tile number, then randomCell gets tmp's value
    			randomCell = tmp;
    			break;
			}

			// use random generator to choose an empty cell from the above array and click it
			randomCell = freeEmptyTiles[randomGenerator.nextInt(freeEmptyTiles.length)];

		} while(randomCell == -99); // -99 is our special value indicating that the array index number can't be picked in next turn. Can't use 0 because 0 is a possible array index number
	}

	/**
	 * Checks whether the player can win in their next move (in 1 move); if they can complete the 3 in a row combo
	 *
	 * @return recommended blocking move for AI to make to prevent player from winning in their next move
	 * (returns the button that hasn't been pressed yet to prevent the player from creating a 3 in a row winning pattern)
	 */
	private int canPlayerWinInNextMove(String[] tile) {

		/*
		 * Game board's tile/button index numbers
		 *
		 * 0 3 6
		 * 1 4 7
		 * 2 5 8
		 */

		// if player has pressed buttons 0, 2 and computer hasn't pressed button 1
		if (PLAYER_SHAPE.equals(tile[0]) && PLAYER_SHAPE.equals(tile[2]) && !COMPUTER_SHAPE.equals(tile[1])) {
			return 1;
		}
		// if player has pressed buttons 3, 5 and computer hasn't pressed button 4
		else if (PLAYER_SHAPE.equals(tile[3]) && PLAYER_SHAPE.equals(tile[5]) && !COMPUTER_SHAPE.equals(tile[4])) {
			return 6;
		}
		// if player has pressed buttons 6, 8 and computer hasn't pressed button 7
		else if (PLAYER_SHAPE.equals(tile[6]) && PLAYER_SHAPE.equals(tile[8]) && !COMPUTER_SHAPE.equals(tile[7])) {
			return 7;
		}
		// if player has pressed buttons 0, 6 and computer hasn't pressed button 3
		else if (PLAYER_SHAPE.equals(tile[0]) && PLAYER_SHAPE.equals(tile[6]) && !COMPUTER_SHAPE.equals(tile[3])) {
			return 3;
		}
		// if player has pressed buttons 1, 7 and computer hasn't pressed button 4
		// or if player has pressed buttons 0, 8 and computer hasn't pressed button 4
		else if ((PLAYER_SHAPE.equals(tile[1]) && PLAYER_SHAPE.equals(tile[7])) || (PLAYER_SHAPE.equals(tile[0])
				&& PLAYER_SHAPE.equals(tile[8])) && !COMPUTER_SHAPE.equals(tile[4])) {
			return 4;
		}
		// if player has pressed buttons 2, 8 and computer hasn't pressed button 5
		// or if player has pressed buttons 2, 8 and computer hasn't pressed button 4
		else if (PLAYER_SHAPE.equals(tile[2]) && PLAYER_SHAPE.equals(tile[8]) && (!COMPUTER_SHAPE.equals(tile[5])
				|| !COMPUTER_SHAPE.equals(tile[4]))) {
			return 5;
		}

		return -99; // this is an indication that the player hasn't chosen 2 spaces in a row yet
	}
}
