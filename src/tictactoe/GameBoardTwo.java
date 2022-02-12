package tictactoe;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.SecureRandom;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

// import org.apache.log4j.Logger;

/**
 * Implementation for tic tac toe game board. Initiates the game. Has functionality to support player vs computer game mode<br>
 * 
 * @author Brian Perel
 *
 */
public class GameBoardTwo implements ActionListener {

	private int randomCell;
	private static JLabel lblPlayersTurn;
	public static boolean toRun = true; // toRun = enforces the computer to only do 1 click inside new thread
	private static int[] availableEmptyCells = new int[9];
	private static boolean isPlayerOnesTurn, isPlayerTwosTurn, start; 
	// private static final Logger logger = Logger.getLogger(GameBoardTwo.class);
	private JButton[] gameBoardTiles = new JButton[9], highlightTiles = new JButton[3];
	private JSeparator[] gameBoardSeparators = new JSeparator[5];
	private static final String PLAYER_ONE_SHAPE = "O", PLAYER_TWO_SHAPE = "X"; // needed to invert these to fix a window2 symbol problem
	private static String playerOneWinsMessage, playerTwoWinsMessage, playerOnesName = "Player", playerTwosName = "Computer";
	public static final JFrame f = new JFrame("Tic Tac Toe");
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
	public GameBoardTwo(boolean argIsStart, boolean argIsPlayerOnesTurn, boolean argIsPlayerTwosTurn) {

		initializeGame(argIsStart, argIsPlayerOnesTurn, argIsPlayerTwosTurn);

		lblPlayersTurn = new JLabel(playerOnesName + "'s turn:");

		f.setResizable(false);
		f.setBounds(100, 100, 399, 358);
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.getContentPane().setLayout(null);
		f.setLocationRelativeTo(null);
		f.setVisible(true);

		// assigning a background image to the app
		f.setContentPane(new JLabel(new ImageIcon("res/graphics/bg-image-tac.jpg")));
		
		// creates and sets up the board tiles
		for(int i = 0; i < gameBoardTiles.length; i++) {
			gameBoardTiles[i] = new JButton();
			f.getContentPane().add(gameBoardTiles[i]);
			gameBoardTiles[i].addActionListener(this);
			gameBoardTiles[i].setFont(new Font("Magneto", Font.PLAIN, 35));
			gameBoardTiles[i].setBackground(new Color(244, 164, 96));
			gameBoardTiles[i].setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.gray));
			
			final int x = i;
			gameBoardTiles[i].addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					gameBoardTiles[x].setBackground(new Color(222, 126, 0));
				}

				@Override
				public void mouseExited(java.awt.event.MouseEvent evt) {
					gameBoardTiles[x].setBackground(new Color(244, 164, 96));
				}
			});
		}
		
		gameBoardTiles[0].setBounds(63, 64, 80, 70);
		gameBoardTiles[1].setBounds(63, 145, 80, 70);
		gameBoardTiles[2].setBounds(63, 226, 80, 70);
		gameBoardTiles[3].setBounds(153, 64, 80, 70);
		gameBoardTiles[4].setBounds(153, 145, 80, 70);
		gameBoardTiles[5].setBounds(153, 226, 80, 70);
		gameBoardTiles[6].setBounds(243, 64, 80, 70);
		gameBoardTiles[7].setBounds(243, 145, 80, 70);
		gameBoardTiles[8].setBounds(243, 226, 80, 70);

		lblPlayersTurn.setBounds(63, 15, 260, 38);
		f.getContentPane().add(lblPlayersTurn);
		
		// creates and sets up the board line dividers
		for(int x = 0; x < gameBoardSeparators.length; x++) {
			gameBoardSeparators[x] = new JSeparator();
			gameBoardSeparators[x].setBackground(Color.blue);
			f.getContentPane().add(gameBoardSeparators[x]);
			
			if(x == 2 || x == 3) {
				gameBoardSeparators[x].setOrientation(SwingConstants.VERTICAL);
			}
		}

		gameBoardSeparators[0].setBounds(63, 138, 260, 11);
		gameBoardSeparators[1].setBounds(63, 221, 260, 11);
		gameBoardSeparators[2].setBounds(148, 64, 7, 232);
		gameBoardSeparators[3].setBounds(237, 64, 7, 232);
	}
	
	/**
	 * Setups the current game: makes decision on who's turn it is and assigns
	 * player entered names
	 * 
	 * @param argIsStart         boolean flag indicating whether or not the game has just
	 *                  begun
	 * @param pOnesTurn boolean flag indicating if it's player one's turn in the
	 *                  game
	 * @param pTwosTurn boolean flag indicating if it's player two's turn in the
	 *                  game
	 */
	public static void initializeGame(boolean argIsStart, boolean argIsPlayerOnesTurn, boolean argIsPlayerTwosTurn) {
		start = argIsStart;
		isPlayerOnesTurn = argIsPlayerOnesTurn;
		isPlayerTwosTurn = argIsPlayerTwosTurn;

		playerOneWinsMessage = playerOnesName + " wins!";
		playerTwoWinsMessage = playerTwosName + " wins!";
		
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
	
	/**
	 * Scans board after every move to see if a pattern of 3 has been made or if all tiles have been clicked
	 */
	public void patternCheck() {
		
		/*
		 * Game board cell reference (button #'s):
		 * 	1  4  7
		 *  2  5  8
		 *  3  6  9
		 */
		
		/*
		 * 0 3 6
		 * 1 4 7
		 * 2 5 8
		 */
		
		// prevents bug - avoids simultaneous winning and tie messages
		if(!toRun) {
			// if buttons 1, 2, 3 are triggered
			if (gameBoardTiles[0].getText().equals("X") && gameBoardTiles[1].getText().equals("X") && gameBoardTiles[2].getText().equals("X")) {
				// logger.info(playerOneWinsMessage);
				winnersPattern(gameBoardTiles[0], gameBoardTiles[1], gameBoardTiles[2]);
				new Winner(playerOnesName);
			} 
			else if (gameBoardTiles[0].getText().equals("O") && gameBoardTiles[1].getText().equals("O")
					&& gameBoardTiles[2].getText().equals("O")) {
				// logger.info(playerTwoWinsMessage);
				winnersPattern(gameBoardTiles[0], gameBoardTiles[1], gameBoardTiles[2]);
				new Winner(playerTwosName);
			}
	
			// if buttons 4, 5, 6 are triggered
			else if (gameBoardTiles[3].getText().equals("X") && gameBoardTiles[4].getText().equals("X") && gameBoardTiles[5].getText().equals("X")) {
				// logger.info(playerOneWinsMessage);
				winnersPattern(gameBoardTiles[3], gameBoardTiles[4], gameBoardTiles[5]);
				new Winner(playerOnesName);
			} 
			else if (gameBoardTiles[3].getText().equals("O") && gameBoardTiles[4].getText().equals("O") && gameBoardTiles[5].getText().equals("O")) {
				// logger.info(playerTwoWinsMessage);
				winnersPattern(gameBoardTiles[3], gameBoardTiles[4], gameBoardTiles[5]);
				new Winner(playerTwosName);
			}
	
			// if buttons 7, 8, 9 are triggered
			else if (gameBoardTiles[6].getText().equals("X") && gameBoardTiles[7].getText().equals("X") && gameBoardTiles[8].getText().equals("X")) {
				// logger.info(playerOneWinsMessage);
				winnersPattern(gameBoardTiles[6], gameBoardTiles[7], gameBoardTiles[8]);
				new Winner(playerOnesName);
			} 
			else if (gameBoardTiles[6].getText().equals("O") && gameBoardTiles[7].getText().equals("O") && gameBoardTiles[8].getText().equals("O")) {
				// logger.info(playerTwoWinsMessage);
				winnersPattern(gameBoardTiles[6], gameBoardTiles[7], gameBoardTiles[8]);
				new Winner(playerTwosName);
			}
	
			// if buttons 1, 4, 7 are triggered
			else if (gameBoardTiles[0].getText().equals("X") && gameBoardTiles[3].getText().equals("X") && gameBoardTiles[6].getText().equals("X")) {
				// logger.info(playerOneWinsMessage);
				winnersPattern(gameBoardTiles[0], gameBoardTiles[3], gameBoardTiles[6]);
				new Winner(playerOnesName);
			} 
			else if (gameBoardTiles[0].getText().equals("O") && gameBoardTiles[3].getText().equals("O")
					&& gameBoardTiles[6].getText().equals("O")) {
				// logger.info(playerTwoWinsMessage);
				winnersPattern(gameBoardTiles[0], gameBoardTiles[3], gameBoardTiles[6]);
				new Winner(playerTwosName);
			}
	
			// if buttons 2, 5, 8 are triggered
			else if (gameBoardTiles[1].getText().equals("X") && gameBoardTiles[4].getText().equals("X") && gameBoardTiles[7].getText().equals("X")) {
				// logger.info(playerOneWinsMessage);
				winnersPattern(gameBoardTiles[1], gameBoardTiles[4], gameBoardTiles[7]);
				new Winner(playerOnesName);
			} 
			else if (gameBoardTiles[1].getText().equals("O") && gameBoardTiles[4].getText().equals("O") && gameBoardTiles[7].getText().equals("O")) {
				// logger.info(playerTwoWinsMessage);
				winnersPattern(gameBoardTiles[1], gameBoardTiles[4], gameBoardTiles[7]);
				new Winner(playerTwosName);
			}
	
			// if buttons 3, 6, 9 are triggered
			else if (gameBoardTiles[2].getText().equals("X") && gameBoardTiles[5].getText().equals("X") && gameBoardTiles[8].getText().equals("X")) {
				// logger.info(playerOneWinsMessage);
				winnersPattern(gameBoardTiles[2], gameBoardTiles[5], gameBoardTiles[8]);
				new Winner(playerOnesName);
			} 
			else if (gameBoardTiles[2].getText().equals("O") && gameBoardTiles[5].getText().equals("O") && gameBoardTiles[8].getText().equals("O")) {
				// logger.info(playerTwoWinsMessage);
				winnersPattern(gameBoardTiles[2], gameBoardTiles[5], gameBoardTiles[8]);
				new Winner(playerTwosName);
			}
	
			// if buttons 1, 5, 9 are triggered
			else if (gameBoardTiles[0].getText().equals("X") && gameBoardTiles[4].getText().equals("X") && gameBoardTiles[8].getText().equals("X")) {
				// logger.info(playerOneWinsMessage);
				winnersPattern(gameBoardTiles[0], gameBoardTiles[4], gameBoardTiles[8]);
				new Winner(playerOnesName);
			} 
			else if (gameBoardTiles[0].getText().equals("O") && gameBoardTiles[4].getText().equals("O") && gameBoardTiles[8].getText().equals("O")) {
				// logger.info(playerTwoWinsMessage);
				winnersPattern(gameBoardTiles[0], gameBoardTiles[4], gameBoardTiles[8]);
				new Winner(playerTwosName);
			}
	
			// if buttons 3, 5, 7 are triggered
			else if (gameBoardTiles[2].getText().equals("X") && gameBoardTiles[4].getText().equals("X") && gameBoardTiles[6].getText().equals("X")) {
				// logger.info(playerOneWinsMessage);
				winnersPattern(gameBoardTiles[2], gameBoardTiles[4], gameBoardTiles[6]);
				new Winner(playerOnesName);
			} 
			else if (gameBoardTiles[2].getText().equals("O") && gameBoardTiles[4].getText().equals("O") && gameBoardTiles[6].getText().equals("O")) {
				// logger.info(playerTwoWinsMessage);
				winnersPattern(gameBoardTiles[2], gameBoardTiles[4], gameBoardTiles[6]);
				new Winner(playerTwosName);
			}
			
			else {
				// if all buttons are pressed default to game over, tie (draw)
				for(int x = 0; x < gameBoardTiles.length; x++) {
					if(gameBoardTiles[x].getText().isEmpty()) {
						break;
					}
					else if(x == (gameBoardTiles.length - 1)) {
						// need gameFinished variable to prevent bug where this code runs twice 
						new Winner("Game Over! It's a draw!!");
					}
				}
			}
		}
	}

	/**
	 * Highlights the winning player's selected tiles and disables the tiles
	 * 
	 * @param one   first tile clicked
	 * @param two   second tile clicked
	 * @param three third tile clicked
	 */
	public void winnersPattern(JButton one, JButton two, JButton three) {
		highlightTiles[0] = one;
		highlightTiles[1] = two;
		highlightTiles[2] = three;
		
		for(JButton button : highlightTiles) {
			button.setBackground(Color.GREEN);
			
			// this will prevent the program from changing colors when you hover after winning
			button.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					button.setBackground(Color.GREEN);
				}

				@Override
				public void mouseExited(java.awt.event.MouseEvent evt) {
					button.setBackground(Color.GREEN);
				}
			});
		}
		
		// disables all 9 buttons on board after game is over. Otherwise when game ends, user can still click on other cells causing another win
		for(JButton button : gameBoardTiles) {
			button.setEnabled(false);
		}
	}

	/**
	 * Performs actions after player one's turn
	 * @param buttonPressed button that was just pressed by player one
	 */
	public static void playerOnesTurnComplete(JButton buttonPressed) {
		buttonPressed.setForeground(new Color(232, 46, 6));
		buttonPressed.setText(PLAYER_ONE_SHAPE);
		lblPlayersTurn.setText(playerOnesName + "'s turn:");
		toRun = true;
	}

	/**
	 * Performs actions after player two's turn
	 * @param buttonPressed button that was just pressed by player two
	 */
	public static void playerTwosTurnComplete(JButton buttonPressed) {
		buttonPressed.setForeground(new Color(0, 0, 255));
		buttonPressed.setText(PLAYER_TWO_SHAPE);
		lblPlayersTurn.setText(playerTwosName + "'s turn:");
		toRun = false;
	}
}
