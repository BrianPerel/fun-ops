package tictactoe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.SecureRandom;

// import org.apache.log4j.Logger;

/**
 * Implementation for tic tac toe game board. Initiates the game. Has functionality to support player vs computer game mode<br>
 * 
 * @author Brian Perel
 *
 */
public class PvEGameBoard extends PvPGameBoard implements ActionListener {

	private int randomCell;
	protected boolean toRun = true; // toRun = enforces the computer to only do 1 click inside the new thread
	private int[] availableEmptyCells;
	// private static final Logger logger = Logger.getLogger(GameBoardTwo.class);
	private SecureRandom randomGenerator = new SecureRandom();

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
	public PvEGameBoard(boolean argIsStart, boolean argIsPlayerOnesTurn, boolean argIsPlayerTwosTurn) {
		super(argIsStart, argIsPlayerOnesTurn, argIsPlayerTwosTurn);
		this.initializeGame(argIsStart, argIsPlayerOnesTurn, argIsPlayerTwosTurn);
		setPlayerOnesName(Winner.PLAYER);
		setPlayerTwosName(Winner.COMPUTER);
		lblPlayersTurn.setText(getPlayerOnesName() + "'s turn");
	}

	@Override
	public void initializeGame(boolean argIsStart, boolean argIsPlayerOnesTurn, boolean argIsPlayerTwosTurn) {
		super.initializeGame(argIsStart, argIsPlayerOnesTurn, argIsPlayerTwosTurn);
		
		availableEmptyCells = new int[9];
		
		for (int x = 0; x < availableEmptyCells.length; x++) {
			availableEmptyCells[x] = x;
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		// enforces player1 to always start first: Sets p1's and p2's turns for first round
		if (start) {
			isPlayerOnesTurn = !isPlayerOnesTurn;
			isPlayerTwosTurn = !isPlayerTwosTurn;
			start = false;
		}
		
		createStartThread();
		
		availableEmptyCells[randomCell] = -99; // prevents computer from choosing same button it's clicked on it's next turn

		// scans through the game board and performs all actions needed to complete a player's turn
		for (int x = 0; x < gameBoardTiles.length; x++) {
			if (ae.getSource() == gameBoardTiles[x] && gameBoardTiles[x].getText().isEmpty()) {
				if (isPlayerOnesTurn) {
					super.playerOnesTurnComplete(gameBoardTiles[x]);
					toRun = true;
				} 
				else if (isPlayerTwosTurn) {
					super.playerTwosTurnComplete(gameBoardTiles[x]);
					toRun = false;
				} 		
				
				availableEmptyCells[x] = -99; // prevents computer from choosing a tile that player has chosen
				isPlayerOnesTurn = !isPlayerOnesTurn;
				isPlayerTwosTurn = !isPlayerTwosTurn;	
				
				break;
			} 
			else if (ae.getSource() == gameBoardTiles[x] && !gameBoardTiles[x].getText().isEmpty()) {
				// logger.warn("Invalid Move!");
			}
		}
		
		patternCheck();
	}
	
	/**
	 * Creates a new thread when it's the computer's turn and selects a free/empty tile on the gameboard
	 */
	public void createStartThread() { 
		// toRun enforces the computer to only do 1 click inside the new thread
		if (toRun) {		
			/*
			 * create another thread and have doClick() called from within that new thread. This is needed because
			 * doClick's timeout gets checked inside the event thread, so it won't get released 
			 * until the actionPerformed method exits (and so the event thread can continue its event processing)
			 */
			new Thread(() -> {
            	// check that the current turn is the computer's turn
            	if (lblPlayersTurn.getText().equals("Computer's turn:")) {  
            		do {
        				// use random generator to choose an empty cell from the above array and click it
        	            randomCell = availableEmptyCells[randomGenerator.nextInt(availableEmptyCells.length)];
        			} while(randomCell == -99); // -99 is our special value indicating that the array index number can't be picked in next turn. Can't use 0 because 0 is a possible array index number
            		
            		try {
						Thread.sleep(300);
					} catch (InterruptedException ie) {
						ie.printStackTrace();
						Thread.currentThread().interrupt();
					}
            		
 					gameBoardTiles[randomCell].doClick();  	
 				}
			}).start();					
		}
	}
}
