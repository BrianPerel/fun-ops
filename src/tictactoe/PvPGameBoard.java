package tictactoe;

import static java.awt.Color.GREEN;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

// import org.apache.log4j.Logger;

/**
 * Implementation for 3x3 tic tac toe game board. Initiates the game. Player 1 will go first.<br>
 * 
 * @author Brian Perel
 * 
 */
public class PvPGameBoard implements ActionListener {

	protected boolean start;
	protected JLabel lblPlayersTurn;
	protected boolean isPlayerOnesTurn;
	protected boolean isPlayerTwosTurn;
	protected String[] tile = new String[9];
	protected JButton[] gameBoardTiles = new JButton[9];
	protected JButton[] highlightWinnersTiles = new JButton[3];
	private JSeparator[] gameBoardSeparators = new JSeparator[5]; // game board divider lines (separators)
	protected static String playerOneWinsMessage;
	protected static String playerTwoWinsMessage;
	private static String playerOnesName;
	private static String playerTwosName;
	// private static final Logger logger = Logger.getLogger(GameBoard.class);	
	protected static final JFrame f = new JFrame("Tic Tac Toe");
	private static final Color LIGHT_ORANGE = new Color(222, 126, 0);
	private static final Color ULTRA_LIGHT_ORANGE = new Color(244, 164, 96);
	private static final Color LIGHT_RED = new Color(232, 46, 6);
	public static final String PLAYER_ONE_LETTER = "X";
	public static final String PLAYER_TWO_LETTER = "O";

	/**
	 * Builds the game's GUI board
	 * 
	 * @param argIsStart         boolean flag indicating whether or not the game has just
	 *                  begun
	 * @param argIsPlayerOnesTurn boolean flag for program indicating if it's player one's turn in the
	 *                  game
	 * @param argIsPlayerTwosTurn boolean flag for program indicating if it's player two's turn in the
	 *                  game
	 */
	public PvPGameBoard(boolean argIsStart, boolean argIsPlayerOnesTurn, boolean argIsPlayerTwosTurn) {
		
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		initializeGame(argIsStart, argIsPlayerOnesTurn, argIsPlayerTwosTurn);
		
		// assigning a background image to the app
		f.setContentPane(new JLabel(new ImageIcon("res/graphics/bg-image-tac.jpg")));

		// creates and sets up the board tiles
		for (int i = 0; i < gameBoardTiles.length; i++) {
			gameBoardTiles[i] = new JButton();
			f.getContentPane().add(gameBoardTiles[i]);
			gameBoardTiles[i].addActionListener(this);
			gameBoardTiles[i].setFont(new Font("Magneto", Font.PLAIN, 35));
			gameBoardTiles[i].setBackground(ULTRA_LIGHT_ORANGE);
			gameBoardTiles[i].setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.GRAY));
			
			final int x = i;
			gameBoardTiles[i].addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					gameBoardTiles[x].setBackground(LIGHT_ORANGE);
				}

				@Override
				public void mouseExited(java.awt.event.MouseEvent evt) {
					gameBoardTiles[x].setBackground(ULTRA_LIGHT_ORANGE);
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
		
		lblPlayersTurn = new JLabel(getPlayerOnesName() + "'s turn:");
		lblPlayersTurn.setBounds(63, 15, 260, 38);
		lblPlayersTurn.setOpaque(false);
		f.getContentPane().add(lblPlayersTurn);
		
		// creates and sets up the board line dividers
		for (int x = 0; x < gameBoardSeparators.length; x++) {
			gameBoardSeparators[x] = new JSeparator();
			gameBoardSeparators[x].setBackground(Color.BLUE);
			f.getContentPane().add(gameBoardSeparators[x]);
			
			if (x == 2 || x == 3) {
				gameBoardSeparators[x].setOrientation(SwingConstants.VERTICAL);
			}
		}

		gameBoardSeparators[0].setBounds(63, 138, 260, 11);
		gameBoardSeparators[1].setBounds(63, 220, 260, 11);
		gameBoardSeparators[2].setBounds(148, 64, 7, 232);
		gameBoardSeparators[3].setBounds(237, 64, 7, 232);
		
		f.setResizable(false);
		f.setSize(399, 358);
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
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
	public void initializeGame(boolean argIsStart, boolean argIsPlayerOnesTurn, boolean argIsPlayerTwosTurn) {
		start = argIsStart;
		isPlayerOnesTurn = argIsPlayerOnesTurn;
		isPlayerTwosTurn = argIsPlayerTwosTurn;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		// enforces player1 to always start first: Sets p1 and p2's turns for first round
		if (start) {
			isPlayerOnesTurn = !isPlayerOnesTurn;
			isPlayerTwosTurn = !isPlayerTwosTurn;
			start = false;
		}

		// scans through the game board and performs all actions needed to complete a player's turn
		for (JButton button : gameBoardTiles) {
			if (ae.getSource() == button && button.getText().isEmpty()) {				
				if (isPlayerOnesTurn) {
					playerOnesTurnComplete(button);
				} 
				else if (isPlayerTwosTurn) {
					playerTwosTurnComplete(button);
				} 	
				
				isPlayerOnesTurn = !isPlayerOnesTurn;
				isPlayerTwosTurn = !isPlayerTwosTurn;
				
				break;
			} 
			else if (ae.getSource() == button && !button.getText().isEmpty()) {
				// logger.warn("Invalid Move!");
			}
		}
		
		isWinner();
	}
	
	/**
	 * Scans board after every move to see if a pattern of 3 has been made in a row, column, or diagonally or if all tiles have been clicked
	 */
	public void checkForWinner() {
		
		/*
		 * Game board's tile/button index numbers
		 * 
		 * 0 3 6
		 * 1 4 7
		 * 2 5 8
		 */
				
		for(int x = 0; x < tile.length; x++) {
			tile[x] = gameBoardTiles[x].getText();
		}
		
		// if buttons 0, 1, 2 are triggered - 3 in a row down the left side column
		if (tile[0].equals(PLAYER_ONE_LETTER) && tile[1].equals(PLAYER_ONE_LETTER) && tile[2].equals(PLAYER_ONE_LETTER)) {
			// logger.info(playerOneWinsMessage);
			winnersPattern(gameBoardTiles[0], gameBoardTiles[1], gameBoardTiles[2]);
			new Winner(getPlayerOnesName());
		} 
		else if (tile[0].equals(PLAYER_TWO_LETTER) && tile[1].equals(PLAYER_TWO_LETTER) && tile[2].equals(PLAYER_TWO_LETTER)) {
			// logger.info(playerTwoWinsMessage);
			winnersPattern(gameBoardTiles[0], gameBoardTiles[1], gameBoardTiles[2]);
			new Winner(getPlayerTwosName());
		}

		// if buttons 3, 4, 5 are triggered - 3 in a row down the middle column
		else if (tile[3].equals(PLAYER_ONE_LETTER) && tile[4].equals(PLAYER_ONE_LETTER) && tile[5].equals(PLAYER_ONE_LETTER)) {
			// logger.info(playerOneWinsMessage);
			winnersPattern(gameBoardTiles[3], gameBoardTiles[4], gameBoardTiles[5]);
			new Winner(getPlayerOnesName());
		} 
		else if (tile[3].equals(PLAYER_TWO_LETTER) && tile[4].equals(PLAYER_TWO_LETTER) && tile[5].equals(PLAYER_TWO_LETTER)) {
			// logger.info(playerTwoWinsMessage);
			winnersPattern(gameBoardTiles[3], gameBoardTiles[4], gameBoardTiles[5]);
			new Winner(getPlayerTwosName());
		}

		// if buttons 6, 7, 8 are triggered - 3 in a row down the right side column
		else if (tile[6].equals(PLAYER_ONE_LETTER) && tile[7].equals(PLAYER_ONE_LETTER) && tile[8].equals(PLAYER_ONE_LETTER)) {
			// logger.info(playerOneWinsMessage);
			winnersPattern(gameBoardTiles[6], gameBoardTiles[7], gameBoardTiles[8]);
			new Winner(getPlayerOnesName());
		} 
		else if (tile[6].equals(PLAYER_TWO_LETTER) && tile[7].equals(PLAYER_TWO_LETTER) && tile[8].equals(PLAYER_TWO_LETTER)) {
			// logger.info(playerTwoWinsMessage);
			winnersPattern(gameBoardTiles[6], gameBoardTiles[7], gameBoardTiles[8]);
			new Winner(getPlayerTwosName());
		}

		// if buttons 0, 3, 6 are triggered - 3 in a row across the top row
		else if (tile[0].equals(PLAYER_ONE_LETTER) && tile[3].equals(PLAYER_ONE_LETTER) && tile[6].equals(PLAYER_ONE_LETTER)) {
			// logger.info(playerOneWinsMessage);
			winnersPattern(gameBoardTiles[0], gameBoardTiles[3], gameBoardTiles[6]);
			new Winner(getPlayerOnesName());
		} 
		else if (tile[0].equals(PLAYER_TWO_LETTER) && tile[3].equals(PLAYER_TWO_LETTER) && tile[6].equals(PLAYER_TWO_LETTER)) {
			// logger.info(playerTwoWinsMessage);
			winnersPattern(gameBoardTiles[0], gameBoardTiles[3], gameBoardTiles[6]);
			new Winner(getPlayerTwosName());
		}

		// if buttons 1, 4, 7 are triggered - 3 in a row across the middle row 
		else if (tile[1].equals(PLAYER_ONE_LETTER) && tile[4].equals(PLAYER_ONE_LETTER) && tile[7].equals(PLAYER_ONE_LETTER)) {
			// logger.info(playerOneWinsMessage);
			winnersPattern(gameBoardTiles[1], gameBoardTiles[4], gameBoardTiles[7]);
			new Winner(getPlayerOnesName());
		} 
		else if (tile[1].equals(PLAYER_TWO_LETTER) && tile[4].equals(PLAYER_TWO_LETTER) && tile[7].equals(PLAYER_TWO_LETTER)) {
			// logger.info(playerTwoWinsMessage);
			winnersPattern(gameBoardTiles[1], gameBoardTiles[4], gameBoardTiles[7]);
			new Winner(getPlayerTwosName());
		}

		// if buttons 2, 5, 8 are triggered - 3 in a row across the bottom row
		else if (tile[2].equals(PLAYER_ONE_LETTER) && tile[5].equals(PLAYER_ONE_LETTER) && tile[8].equals(PLAYER_ONE_LETTER)) {
			// logger.info(playerOneWinsMessage);
			winnersPattern(gameBoardTiles[2], gameBoardTiles[5], gameBoardTiles[8]);
			new Winner(getPlayerOnesName());
		} 
		else if (tile[2].equals(PLAYER_TWO_LETTER) && tile[5].equals(PLAYER_TWO_LETTER) && tile[8].equals(PLAYER_TWO_LETTER)) {
			// logger.info(playerTwoWinsMessage);
			winnersPattern(gameBoardTiles[2], gameBoardTiles[5], gameBoardTiles[8]);
			new Winner(getPlayerTwosName());
		}

		// if buttons 0, 4, 8 are triggered - 3 in a row diagonally
		else if (tile[0].equals(PLAYER_ONE_LETTER) && tile[4].equals(PLAYER_ONE_LETTER) && tile[8].equals(PLAYER_ONE_LETTER)) {
			// logger.info(playerOneWinsMessage);
			winnersPattern(gameBoardTiles[0], gameBoardTiles[4], gameBoardTiles[8]);
			new Winner(getPlayerOnesName());
		} 
		else if (tile[0].equals(PLAYER_TWO_LETTER) && tile[4].equals(PLAYER_TWO_LETTER) && tile[8].equals(PLAYER_TWO_LETTER)) {
			// logger.info(playerTwoWinsMessage);
			winnersPattern(gameBoardTiles[0], gameBoardTiles[4], gameBoardTiles[8]);
			new Winner(getPlayerTwosName());
		}

		// if buttons 2, 4, 6 are triggered - 3 in a row diagonally
		else if (tile[2].equals(PLAYER_ONE_LETTER) && tile[4].equals(PLAYER_ONE_LETTER) && tile[6].equals(PLAYER_ONE_LETTER)) {
			// logger.info(playerOneWinsMessage);
			winnersPattern(gameBoardTiles[2], gameBoardTiles[4], gameBoardTiles[6]);
			new Winner(getPlayerOnesName());
		} 
		else if (tile[2].equals(PLAYER_TWO_LETTER) && tile[4].equals(PLAYER_TWO_LETTER) && tile[6].equals(PLAYER_TWO_LETTER)) {
			// logger.info(playerTwoWinsMessage);
			winnersPattern(gameBoardTiles[2], gameBoardTiles[4], gameBoardTiles[6]);
			new Winner(getPlayerTwosName());
		}
		
		else {
			if(isBoardFull()) {
				new Winner("Game Over! It's a draw!");
			}
		}
	}

	/**
	 * Checks if game board is full, displays game over - tie (draw)
	 */
	public boolean isBoardFull() {
		for(JButton x : gameBoardTiles) {
			// return false if even just 1 of the tiles is empty, since then there's no way of finding the board to be filled out at that point
			if (x.getText().isEmpty()) {
				return false;
			}
		}
		
		return true;
	}

	/**
	 * Highlights the winning player's selected tiles and disables the tiles
	 * @param one   first tile clicked
	 * @param two   second tile clicked
	 * @param three third tile clicked
	 */
	public void winnersPattern(JButton one, JButton two, JButton three) {
		highlightWinnersTiles[0] = one;
		highlightWinnersTiles[1] = two;
		highlightWinnersTiles[2] = three;
		
		for (JButton button : highlightWinnersTiles) {
			button.setBackground(GREEN);
			
			// this will prevent the program from changing colors when you hover after winning
			button.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					button.setBackground(GREEN);
				}

				@Override
				public void mouseExited(java.awt.event.MouseEvent evt) {
					button.setBackground(GREEN);
				}
			});
		}
		
		// disables all 9 buttons on board after game is over
		for (JButton button : gameBoardTiles) {
			button.setEnabled(false);
		}
	}

	/**
	 * Performs actions after player one's turn
	 * @param buttonPressed button that was just pressed by player one
	 */
	public void playerOnesTurnComplete(JButton buttonPressed) {
		buttonPressed.setForeground(LIGHT_RED);
		buttonPressed.setText(PLAYER_TWO_LETTER);
		lblPlayersTurn.setText(getPlayerOnesName() + "'s turn:");
	}

	/**
	 * Performs actions after player two's turn
	 * @param buttonPressed button that was just pressed by player two
	 */
	public void playerTwosTurnComplete(JButton buttonPressed) {
		buttonPressed.setForeground(Color.BLUE);
		buttonPressed.setText(PLAYER_ONE_LETTER);
		lblPlayersTurn.setText(getPlayerTwosName() + "'s turn:");
	}

	/**
	 * Gets player one's name
	 * @return player one's name
	 */
	public static String getPlayerOnesName() {
		return playerOnesName;
	}

	/**
	 * Sets player one's name
	 * @param playerOnesName
	 */
	public static void setPlayerOnesName(String playerOnesName) {
		PvPGameBoard.playerOnesName = playerOnesName;
	}

	/**
	 * Gets player two's name
	 * @return player two's name
	 */
	public static String getPlayerTwosName() {
		return playerTwosName;
	}

	/**
	 * Sets player two's name
	 * @param playerTwosName
	 */
	public static void setPlayerTwosName(String playerTwosName) {
		PvPGameBoard.playerTwosName = playerTwosName;
	}
}
