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
import java.util.logging.Logger;

/**
 * Implementation for tic-tac-toe game board. Initiates the game and has functionality to support player vs computer game mode<br>
 * 
 * @author Brian Perel
 *
 */
public class CvPGameBoard extends PvPGameBoard implements ActionListener {

	private static final SecureRandom randomGenerator = new SecureRandom(
			LocalDateTime.now().toString().getBytes(StandardCharsets.US_ASCII));
	
	private int randomCell;
	private int[] freeEmptyTiles;
	private boolean invalidMoveSelected; // enforces the computer to not click if user clicked on an invalid tile
	private boolean shouldRun; // enforces the computer to only do 1 click inside the new thread
	private final Logger logger_ = Logger.getLogger(this.getClass().getName());

	/**
	 * Builds the game's GUI board
	 * 
	 * @param argIsStart         boolean flag indicating whether or not the game has just
	 *                  begun
	 * @param argIsPlayerOnesTurn boolean flag indicating if it's player one's turn in the
	 *                  game
	 * @param argIsPlayerTwosTurn boolean flag indicating if it's player two's turn in the
	 *                  game
	 */
	public CvPGameBoard(boolean argIsStart, boolean argIsPlayerOnesTurn, boolean argIsPlayerTwosTurn) {
		super(argIsStart, argIsPlayerOnesTurn, argIsPlayerTwosTurn);
		setPlayerOnesName(StartMenu.PLAYER);
		setPlayerTwosName(StartMenu.COMPUTER);
		this.initializeGame(argIsStart, argIsPlayerOnesTurn, argIsPlayerTwosTurn);
		lblPlayersTurn.setText(getPlayerOnesName() + "'s turn:");
	}

	@Override
	public void initializeGame(boolean argIsStart, boolean argIsPlayerOnesTurn, boolean argIsPlayerTwosTurn) {
		super.initializeGame(argIsStart, argIsPlayerOnesTurn, argIsPlayerTwosTurn);
		
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
			
			if ((ae.getSource() == gameBoardTiles[x]) && gameBoardTiles[x].getText().isEmpty()) {
				super.completePlayersTurn(isCvPGame, gameBoardTiles[x], isPlayerOnesTurn ? LIGHT_RED : Color.BLUE, 
					isPlayerOnesTurn ? PLAYER_TWO_LETTER : PLAYER_ONE_LETTER, 
					isPlayerOnesTurn ? getPlayerOnesName() : getPlayerTwosName());
				
				shouldRun = !isPlayerOnesTurn;
				
				freeEmptyTiles[x] = -99; // prevents computer from choosing a tile that player has chosen
				isPlayerOnesTurn = !isPlayerOnesTurn;
				isPlayerTwosTurn = !isPlayerTwosTurn;	
				
				break;
			} 
			// if you try to select a tile that is not empty
			else if (ae.getSource() == gameBoardTiles[x] && !gameBoardTiles[x].getText().isEmpty()) {
				invalidMoveSelected = true;
				logger_.warning("Invalid Move.");
				Toolkit.getDefaultToolkit().beep();
			}
		}
		
		makeMoveComputer();
		freeEmptyTiles[randomCell] = -99; // prevents tic-tac-toe AI from choosing the same button it just clicked on it's next turn
	}
	
	/**
	 * Creates a new thread when it's the tic-tac-toe AI's turn to have it select a free/empty tile on the gameboard
	 */
	public void makeMoveComputer() { 
		// toRun enforces the computer to only do 1 click inside the new thread
		if (shouldRun && !invalidMoveSelected) {		
			/*
			 * create another thread and have doClick() called from within that new thread. This is needed because
			 * doClick method's timeout gets checked inside the event thread, so it won't get released 
			 * until the parent calling method (actionPerformed) exits (and so the event thread can continue its event processing)
			 */
			new Thread(() -> {
        		makeBestMove();
        		
        		try {
					TimeUnit.MILLISECONDS.sleep(300L);
				} catch (InterruptedException ie) {
					logger_.severe("Error: " + ie.toString());
					ie.printStackTrace();
					Thread.currentThread().interrupt();
				}
        	
        		// solution to doClick() not releasing button until the parent calling method
        		// (actionPerformed) finishes: doClick was moved to another thread here
				gameBoardTiles[randomCell].doClick(); 
			}).start();					
		}
		
		invalidMoveSelected = false;
	}
	
	public void makeBestMove() {
		for(int x = 0; x < tile.length; x++) {
			tile[x] = gameBoardTiles[x].getText();
		}
		
		do {	
			int tmp = canPlayerWinInOneMove(tile);
			
			if(tmp != -99 && Arrays.stream(freeEmptyTiles).anyMatch(x -> x == tmp)) {
				// loop through int array if tmp variable is in the array (meaning it's a free empty cell tile number, then randomCell gets tmp's value
    			randomCell = tmp;
    			break;
			}		
			
			// use random generator to choose an empty cell from the above array and click it
			randomCell = freeEmptyTiles[randomGenerator.nextInt(freeEmptyTiles.length)];
			
		} while(randomCell == -99); // -99 is our special value indicating that the array index number can't be picked in next turn. Can't use 0 because 0 is a possible array index number
	}
	
	/**
	 * Checks whether the player can win in there next move (1 move)
	 * @return recommended button for AI to select to prevent player from winning in their next move
	 */
	public int canPlayerWinInOneMove(String[] tile) {
		
		/*
		 * Game board's tile/button index numbers
		 * 
		 * 0 3 6
		 * 1 4 7
		 * 2 5 8
		 */
		
		// if player has pressed buttons 0, 2 and computer hasn't pressed button 1
		if (tile[0].equals(PLAYER_ONE_LETTER) && tile[2].equals(PLAYER_ONE_LETTER) && !tile[1].equals(PLAYER_TWO_LETTER)) {
			return 1;
		} 
		// if player has pressed buttons 3, 5 and computer hasn't pressed button 4
		else if (tile[3].equals(PLAYER_ONE_LETTER) && tile[5].equals(PLAYER_ONE_LETTER) && !tile[4].equals(PLAYER_TWO_LETTER)) {
			return 6;
		} 
		// if player has pressed buttons 6, 8 and computer hasn't pressed button 7
		else if (tile[6].equals(PLAYER_ONE_LETTER) && tile[8].equals(PLAYER_ONE_LETTER) && !tile[7].equals(PLAYER_TWO_LETTER)) {
			return 7;
		} 
		// if player has pressed buttons 0, 6 and computer hasn't pressed button 3
		else if (tile[0].equals(PLAYER_ONE_LETTER) && tile[6].equals(PLAYER_ONE_LETTER) && !tile[3].equals(PLAYER_TWO_LETTER)) {
			return 3;
		} 
		// if player has pressed buttons 1, 7 and computer hasn't pressed button 4
		// or if player has pressed buttons 0, 8 and computer hasn't pressed button 4
		else if ((tile[1].equals(PLAYER_ONE_LETTER) && tile[7].equals(PLAYER_ONE_LETTER)) || (tile[0].equals(PLAYER_ONE_LETTER)
				&& tile[8].equals(PLAYER_ONE_LETTER)) && !tile[4].equals(PLAYER_TWO_LETTER)) {
			return 4;
		} 
		// if player has pressed buttons 2, 8 and computer hasn't pressed button 5 
		// or if player has pressed buttons 2, 8 and computer hasn't pressed button 4
		else if (tile[2].equals(PLAYER_ONE_LETTER) && tile[8].equals(PLAYER_ONE_LETTER) && (!tile[5].equals(PLAYER_TWO_LETTER)
				|| !tile[4].equals(PLAYER_TWO_LETTER))) {
			return 5;
		} 
		
		return -99; // this is an indication that the player hasn't chosen 2 spaces in a row yet
	}
}
