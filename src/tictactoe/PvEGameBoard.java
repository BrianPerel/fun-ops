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
		
		makeMoveComputer();
		
		availableEmptyCells[randomCell] = -99; // prevents tic-tac-toe AI from choosing the same button it just clicked on it's next turn

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
			// if you try to select a tile that is not empty
			else if (ae.getSource() == gameBoardTiles[x] && !gameBoardTiles[x].getText().isEmpty()) {
				// logger.warn("Invalid Move!");
			}
		}
		
		isWinner();
	}
	
	/**
	 * Creates a new thread when it's the tic-tac-toe AI's turn to have it select a free/empty tile on the gameboard
	 */
	public void makeMoveComputer() { 
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
            			int tmp = canPlayerWinInOneMove();
            			
            			if(tmp != -99) {
	            			for(int x = 0; x < availableEmptyCells.length; x++) {
	            				if(tmp == availableEmptyCells[x]) {
	            					randomCell = tmp;
	            					break;
	            				}
	            			}
	            			
	            			break;
            			} 
            			
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
	
	/**
	 * Checks whether the player can win in there next move (1 move)
	 * @return recommended button for AI to select
	 */
	public int canPlayerWinInOneMove() {
		
		/*
		 * Game board's tile/button index numbers
		 * 
		 * 0 3 6
		 * 1 4 7
		 * 2 5 8
		 */
				
		for(int x = 0; x < 9; x++) {
			tile[x] = gameBoardTiles[x].getText();
		}
		
		// if player has pressed buttons 0, 2 and computer hasn't pressed button 1
		if (tile[0].equals("X") && tile[2].equals("X") && !tile[1].equals("O")) {
			return 1;
		} 
		

		// if player has pressed buttons 3, 5 and computer hasn't pressed button 4
		else if (tile[3].equals("X") && tile[5].equals("X") && !tile[4].equals("O")) {
			return 6;
		} 
		

		// if player has pressed buttons 6, 8 and computer hasn't pressed button 7
		else if (tile[6].equals("X") && tile[8].equals("X") && !tile[7].equals("O")) {
			return 7;
		} 
		
		// if player has pressed buttons 0, 6 and computer hasn't pressed button 3
		else if (tile[0].equals("X") && tile[6].equals("X") && !tile[3].equals("O")) {
			return 3;
		} 
		
		// if player has pressed buttons 1, 7 and computer hasn't pressed button 4
		// or if player has pressed buttons 0, 8 and computer hasn't pressed button 4
		else if (tile[1].equals("X") && tile[7].equals("X") && !tile[4].equals("O")
				|| (tile[0].equals("X") && tile[8].equals("X") && !tile[4].equals("O"))) {
			return 4;
		} 
		
		// if player has pressed buttons 2, 8 and computer hasn't pressed button 5 
		// or if player has pressed buttons 2, 8 and computer hasn't pressed button 4
		else if (tile[2].equals("X") && tile[8].equals("X") && !tile[5].equals("O")
				|| (tile[2].equals("X") && tile[8].equals("X") && !tile[4].equals("O"))) {
			return 5;
		} 
		
		return -99; // this is an indication that the player hasn't chosen 2 spaces in a row yet
	}
}
