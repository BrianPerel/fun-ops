package tictactoe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.SecureRandom;

import javax.swing.JButton;

// import org.apache.log4j.Logger;

/**
 * Implementation for tic tac toe game board. Initiates the game. Has functionality to support player vs computer game mode<br>
 * 
 * @author Brian Perel
 *
 */
public class PvEGameBoard extends PvPGameBoard implements ActionListener {

	private int randomCell;
	public static boolean toRun = true; // toRun = enforces the computer to only do 1 click inside new thread
	private static int[] availableEmptyCells = new int[9];
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
		setPlayerOnesName("Player");
		setPlayerTwosName("Computer");
		lblPlayersTurn.setText(getPlayerOnesName() + "'s turn");
	}

	@Override
	public void initializeGame(boolean argIsStart, boolean argIsPlayerOnesTurn, boolean argIsPlayerTwosTurn) {
		super.initializeGame(argIsStart, argIsPlayerOnesTurn, argIsPlayerTwosTurn);
		
		for(int x = 0; x < availableEmptyCells.length; x++) {
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

		if(toRun) {		
			/*
			 * create another thread and have doClick() called from within that new thread. This is needed because
			 * doClick's timeout gets checked inside the event thread, so it won't get released 
			 * until the actionPerformed method exits (and so the event thread can continue its event processing)
			 */
			new Thread(() -> {
	            	// check that the current turn is the computer's turn
	            	if(lblPlayersTurn.getText().equals("Computer's turn:")) {  
	            		do {
	        				// use random generator to choose an empty cell from the above array and click it
	        	            randomCell = availableEmptyCells[randomGenerator.nextInt(availableEmptyCells.length)];
	        			} while(randomCell == -99); // -99 is our special value indicating that the array index number can't be picked in next turn. Can't use 0 because 0 is a possible array index number
	            		
	            		try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							e.printStackTrace();
							Thread.currentThread().interrupt();
						}
	            		
	 					gameBoardTiles[randomCell].doClick();  	
	 				}
				}
	         ).start();					
		}
		
		availableEmptyCells[randomCell] = -99; // prevents computer from choosing same button it's clicked on it's next turn

		// scans through the game board and performs all actions needed to complete a player's turn
		for(int x = 0; x < gameBoardTiles.length; x++) {
			if (ae.getSource() == gameBoardTiles[x] && gameBoardTiles[x].getText().isEmpty()) {
				if (isPlayerOnesTurn) {
					playerOnesTurnComplete(gameBoardTiles[x]);
				} 
				else if (isPlayerTwosTurn) {
					playerTwosTurnComplete(gameBoardTiles[x]);
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
	
	@Override
	public void playerOnesTurnComplete(JButton buttonPressed) {
		super.playerOnesTurnComplete(buttonPressed);
		toRun = true;
	}

	@Override
	public void playerTwosTurnComplete(JButton buttonPressed) {
		super.playerTwosTurnComplete(buttonPressed);
		toRun = false;
	}
}
