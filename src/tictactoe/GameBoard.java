package tictactoe;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

/**
 * Implementation for tic tac toe game board. Initiates the game. <br>
 * 
 * @author Brian Perel
 *
 */
public class GameBoard implements ActionListener {

	static JFrame f = new JFrame("Tic Tac Toe");
	static String playerOnesName, playerTwosName;
	private static JLabel lblPlayersTurn;
	private static boolean isPlayerOnesTurn, isPlayerTwosTurn, start;
	private static final Logger logger = Logger.getLogger(GameBoard.class);
	private static String playerOneWinsMessage, playerTwoWinsMessage;
	private JButton[] gameBoardTiles = new JButton[9];
	private JButton[] highlightTiles = new JButton[3];
	private JSeparator[] gameBoardSeparators = new JSeparator[5];

	// needed to invert these to fix a window2 symbol problem
	private static final String PLAYER_ONE_SHAPE = "O", PLAYER_TWO_SHAPE = "X";

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

		playerOneWinsMessage = "Player 1 (" + playerOnesName + ") wins!";
		playerTwoWinsMessage = "Player 2 (" + playerTwosName + ") wins!";
	}

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
	public GameBoard(boolean argIsStart, boolean argIsPlayerOnesTurn, boolean argIsPlayerTwosTurn) {

		initializeGame(argIsStart, argIsPlayerOnesTurn, argIsPlayerTwosTurn);

		lblPlayersTurn = new JLabel(playerOnesName + "'s turn:");

		f.setResizable(false);
		f.setBounds(100, 100, 399, 358);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().setLayout(null);
		f.setLocationRelativeTo(null);
		f.setVisible(true);

		// assigning a background image to the app
		f.setContentPane(new JLabel(new ImageIcon("res/graphics/bgImageToe.jpg")));
		
		for(int i = 0; i < gameBoardTiles.length; i++) {
			gameBoardTiles[i] = new JButton();
			f.getContentPane().add(gameBoardTiles[i]);
			gameBoardTiles[i].addActionListener(this);
			gameBoardTiles[i].setFont(new Font("Magneto", Font.PLAIN, 20));
			gameBoardTiles[i].setBackground(new Color(244, 164, 96));
			
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
		
		for(int x = 0; x < gameBoardSeparators.length; x++) {
			gameBoardSeparators[x] = new JSeparator();
			gameBoardSeparators[x].setBackground(Color.blue);
			f.getContentPane().add(gameBoardSeparators[x]);
		}

		gameBoardSeparators[0].setBounds(63, 138, 260, 11);
		gameBoardSeparators[1].setBounds(63, 221, 260, 11);
		gameBoardSeparators[2].setBounds(148, 64, 7, 232);
		gameBoardSeparators[3].setBounds(237, 64, 7, 232);

		gameBoardSeparators[2].setOrientation(SwingConstants.VERTICAL);
		gameBoardSeparators[3].setOrientation(SwingConstants.VERTICAL);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		// enforces player1 to always start first: Sets p1 and p2's turns for first round
		if (start) {
			isPlayerOnesTurn = !isPlayerOnesTurn;
			isPlayerTwosTurn = !isPlayerTwosTurn;
			start = false;
		}

		for(int x = 0; x < gameBoardTiles.length; x++) {
			if (ae.getSource() == gameBoardTiles[x] && gameBoardTiles[x].getText().isEmpty()) {
				if (isPlayerOnesTurn) {
					playerOnesTurnComplete(gameBoardTiles[x]);
				} else if (isPlayerTwosTurn) {
					playerTwosTurnComplete(gameBoardTiles[x]);
				} 
			} else if (ae.getSource() == gameBoardTiles[x] && !gameBoardTiles[x].getText().isEmpty()) {
				logger.warn("Invalid Move!");
			}
		}

		// game rules: Need 3 in a row in any direction
		
		/*
		 * Game board reference (button #'s):
		 * 	1  4  7
		 *  2  5  8
		 *  3  6  9
		 */
		
		// if all buttons are pressed default to game over, tie. However if succeeding 'if' condition is triggered this will be ignored
		if (!gameBoardTiles[0].getText().isEmpty() && !gameBoardTiles[1].getText().isEmpty() && !gameBoardTiles[2].getText().isEmpty()
				&& !gameBoardTiles[3].getText().isEmpty() && !gameBoardTiles[4].getText().isEmpty() && !gameBoardTiles[5].getText().isEmpty()
				&& !gameBoardTiles[6].getText().isEmpty() && !gameBoardTiles[7].getText().isEmpty() && !gameBoardTiles[8].getText().isEmpty()) {
			new Winner("Game Over! Tie");
		}

		// if buttons 1, 2, 3 are triggered
		if (gameBoardTiles[0].getText().equals("X") && gameBoardTiles[1].getText().equals("X") && gameBoardTiles[2].getText().equals("X")) {
			logger.info(playerOneWinsMessage);
			winnersPattern(gameBoardTiles[0], gameBoardTiles[1], gameBoardTiles[2]);
			new Winner(playerOnesName);
		} else if (gameBoardTiles[0].getText().equals("O") && gameBoardTiles[1].getText().equals("O")
				&& gameBoardTiles[2].getText().equals("O")) {
			logger.info(playerTwoWinsMessage);
			winnersPattern(gameBoardTiles[0], gameBoardTiles[1], gameBoardTiles[2]);
			new Winner(playerTwosName);
		}

		// if buttons 4, 5, 6 are triggered
		if (gameBoardTiles[3].getText().equals("X") && gameBoardTiles[4].getText().equals("X") && gameBoardTiles[5].getText().equals("X")) {
			logger.info(playerOneWinsMessage);
			winnersPattern(gameBoardTiles[3], gameBoardTiles[4], gameBoardTiles[5]);
			new Winner(playerOnesName);
		} else if (gameBoardTiles[3].getText().equals("O") && gameBoardTiles[4].getText().equals("O") && gameBoardTiles[5].getText().equals("O")) {
			logger.info(playerTwoWinsMessage);
			winnersPattern(gameBoardTiles[3], gameBoardTiles[4], gameBoardTiles[5]);
			new Winner(playerTwosName);
		}

		// if buttons 7, 8, 9 are triggered
		if (gameBoardTiles[6].getText().equals("X") && gameBoardTiles[7].getText().equals("X") && gameBoardTiles[8].getText().equals("X")) {
			logger.info(playerOneWinsMessage);
			winnersPattern(gameBoardTiles[6], gameBoardTiles[7], gameBoardTiles[8]);
			new Winner(playerOnesName);
		} else if (gameBoardTiles[6].getText().equals("O") && gameBoardTiles[7].getText().equals("O") && gameBoardTiles[8].getText().equals("O")) {
			logger.info(playerTwoWinsMessage);
			winnersPattern(gameBoardTiles[6], gameBoardTiles[7], gameBoardTiles[8]);
			new Winner(playerTwosName);
		}

		// if buttons 1, 4, 7 are triggered
		if (gameBoardTiles[0].getText().equals("X") && gameBoardTiles[3].getText().equals("X") && gameBoardTiles[6].getText().equals("X")) {
			logger.info(playerOneWinsMessage);
			winnersPattern(gameBoardTiles[0], gameBoardTiles[3], gameBoardTiles[6]);
			new Winner(playerOnesName);
		} else if (gameBoardTiles[0].getText().equals("O") && gameBoardTiles[3].getText().equals("O")
				&& gameBoardTiles[6].getText().equals("O")) {
			logger.info(playerTwoWinsMessage);
			winnersPattern(gameBoardTiles[0], gameBoardTiles[3], gameBoardTiles[6]);
			new Winner(playerTwosName);
		}

		// if buttons 2, 5, 8 are triggered
		if (gameBoardTiles[1].getText().equals("X") && gameBoardTiles[4].getText().equals("X") && gameBoardTiles[7].getText().equals("X")) {
			logger.info(playerOneWinsMessage);
			winnersPattern(gameBoardTiles[1], gameBoardTiles[4], gameBoardTiles[7]);
			new Winner(playerOnesName);
		} else if (gameBoardTiles[1].getText().equals("O") && gameBoardTiles[4].getText().equals("O") && gameBoardTiles[7].getText().equals("O")) {
			logger.info(playerTwoWinsMessage);
			winnersPattern(gameBoardTiles[1], gameBoardTiles[4], gameBoardTiles[7]);
			new Winner(playerTwosName);
		}

		// if buttons 3, 6, 9 are triggered
		if (gameBoardTiles[2].getText().equals("X") && gameBoardTiles[5].getText().equals("X") && gameBoardTiles[8].getText().equals("X")) {
			logger.info(playerOneWinsMessage);
			winnersPattern(gameBoardTiles[2], gameBoardTiles[5], gameBoardTiles[8]);
			new Winner(playerOnesName);
		} else if (gameBoardTiles[2].getText().equals("O") && gameBoardTiles[5].getText().equals("O") && gameBoardTiles[8].getText().equals("O")) {
			logger.info(playerTwoWinsMessage);
			winnersPattern(gameBoardTiles[2], gameBoardTiles[5], gameBoardTiles[8]);
			new Winner(playerTwosName);
		}

		// if buttons 1, 5, 9 are triggered
		if (gameBoardTiles[0].getText().equals("X") && gameBoardTiles[4].getText().equals("X") && gameBoardTiles[8].getText().equals("X")) {
			logger.info(playerOneWinsMessage);
			winnersPattern(gameBoardTiles[0], gameBoardTiles[4], gameBoardTiles[8]);
			new Winner(playerOnesName);
		} else if (gameBoardTiles[0].getText().equals("O") && gameBoardTiles[4].getText().equals("O") && gameBoardTiles[8].getText().equals("O")) {
			logger.info(playerTwoWinsMessage);
			winnersPattern(gameBoardTiles[0], gameBoardTiles[4], gameBoardTiles[8]);
			new Winner(playerTwosName);
		}

		// if buttons 3, 5, 7 are triggered
		if (gameBoardTiles[2].getText().equals("X") && gameBoardTiles[4].getText().equals("X") && gameBoardTiles[6].getText().equals("X")) {
			logger.info(playerOneWinsMessage);
			winnersPattern(gameBoardTiles[2], gameBoardTiles[4], gameBoardTiles[6]);
			new Winner(playerOnesName);
		} else if (gameBoardTiles[2].getText().equals("O") && gameBoardTiles[4].getText().equals("O") && gameBoardTiles[6].getText().equals("O")) {
			logger.info(playerTwoWinsMessage);
			winnersPattern(gameBoardTiles[2], gameBoardTiles[4], gameBoardTiles[6]);
			new Winner(playerTwosName);
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
		
		for(int i = 0; i < highlightTiles.length; i++) {
			highlightTiles[i].setBackground(Color.GREEN);
			
			// this will prevent the program from changing colors when you hover after winning
			final int x = i;
			highlightTiles[i].addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					highlightTiles[x].setBackground(Color.GREEN);
				}

				@Override
				public void mouseExited(java.awt.event.MouseEvent evt) {
					highlightTiles[x].setBackground(Color.GREEN);
				}
			});
		}
		
		// disables all 9 buttons on board after game is over
		for(int x = 0; x < gameBoardTiles.length; x++) {
			gameBoardTiles[x].setEnabled(false);
		}
	}

	/**
	 * Performs actions after player one's turn
	 * 
	 * @param buttonPressed button that was just pressed by player one
	 */
	public static void playerOnesTurnComplete(JButton buttonPressed) {
		buttonPressed.setForeground(new Color(232, 46, 6));
		buttonPressed.setText(PLAYER_ONE_SHAPE);
		lblPlayersTurn.setText(playerOnesName + "'s turn:");
		isPlayerOnesTurn = !isPlayerOnesTurn;
		isPlayerTwosTurn = !isPlayerTwosTurn;
	}

	/**
	 * Performs actions after player two's turn
	 * 
	 * @param buttonPressed button that was just pressed by player two
	 */
	public static void playerTwosTurnComplete(JButton buttonPressed) {
		buttonPressed.setForeground(new Color(0, 0, 255));
		buttonPressed.setText(PLAYER_TWO_SHAPE);
		lblPlayersTurn.setText(playerTwosName + "'s turn:");
		isPlayerOnesTurn = !isPlayerOnesTurn;
		isPlayerTwosTurn = !isPlayerTwosTurn;
	}
}
