package com.tictactoe;

import static java.awt.Color.GREEN;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

/**
 * Implementation for 3x3 tic-tac-toe game board. Initiates the game. Player 1 will go first.<br>
 * 
 * @author Brian Perel
 * 
 */
public class PvPGameBoard implements ActionListener {

	protected static final JFrame f = new JFrame("Tic Tac Toe");
	private static final Color LIGHT_ORANGE = new Color(222, 126, 0);
	private static final Color ULTRA_LIGHT_ORANGE = new Color(244, 164, 96);
	protected static final Color LIGHT_RED = new Color(232, 46, 6);
	public static final String PLAYER_ONE_LETTER = "X";
	public static final String PLAYER_TWO_LETTER = "O";
	protected static boolean isGameOver = false;
	private static String playerOnesName;
	private static String playerTwosName;

	protected String[] tile;
	protected boolean isCvPGame;
	protected JLabel lblPlayersTurn;
	private boolean isGameFinished;
	protected boolean isPlayerOnesTurn;
	protected boolean isPlayerTwosTurn;
	protected JButton[] gameBoardTiles;
	private JSeparator[] gameBoardSeparators; // game board divider lines (separators)
	private final Logger logger_ = Logger.getLogger(this.getClass().getName());

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
			logger_.severe("Error: " + e.toString());
			e.printStackTrace();
		}

		initializeGame(argIsStart, argIsPlayerOnesTurn, argIsPlayerTwosTurn);

		isGameOver = false; // need to set variable to false here to avoid name label not changing bug
		
		tile = new String[9];
		gameBoardTiles = new JButton[9];
		gameBoardSeparators = new JSeparator[4];
		
		// assigning a background image to the app
		f.setContentPane(new JLabel(new ImageIcon("res/graphics/bg-image-tac.jpg")));

		// creates and sets up the board tiles
		for (int i = 0; i < gameBoardTiles.length; i++) {
			gameBoardTiles[i] = new JButton();
			f.getContentPane().add(gameBoardTiles[i]);
			gameBoardTiles[i].addActionListener(this);
			gameBoardTiles[i].setSize(80, 70);
			gameBoardTiles[i].setFont(new Font("Magneto", Font.PLAIN, 35));
			gameBoardTiles[i].setBackground(ULTRA_LIGHT_ORANGE);
			gameBoardTiles[i].setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.GRAY));
			
			final int x = i;
			gameBoardTiles[i].addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent evt) {
					gameBoardTiles[x].setBackground(LIGHT_ORANGE);
				}

				@Override
				public void mouseExited(MouseEvent evt) {
					gameBoardTiles[x].setBackground(ULTRA_LIGHT_ORANGE);
				}
			});
		}
		
		gameBoardTiles[0].setLocation(63, 64);
		gameBoardTiles[1].setLocation(63, 145);
		gameBoardTiles[2].setLocation(63, 226);
		gameBoardTiles[3].setLocation(153, 64);
		gameBoardTiles[4].setLocation(153, 145);
		gameBoardTiles[5].setLocation(153, 226);
		gameBoardTiles[6].setLocation(243, 64);
		gameBoardTiles[7].setLocation(243, 145);
		gameBoardTiles[8].setLocation(243, 226);
		
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
				gameBoardSeparators[x].setSize(7, 232);
			}
			else {
				gameBoardSeparators[x].setSize(260, 11);
			}
		}

		gameBoardSeparators[0].setLocation(63, 138);
		gameBoardSeparators[1].setLocation(63, 220);
		gameBoardSeparators[2].setLocation(148, 64);
		gameBoardSeparators[3].setLocation(237, 64);
		
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
		// enforces player1 to always start first: Sets p1 and p2's turns for first round
		if (argIsStart) {
			isPlayerOnesTurn = !argIsPlayerOnesTurn;
			isPlayerTwosTurn = !argIsPlayerTwosTurn;
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		
		// scans through the game board and performs all actions needed to complete a player's turn
		for (JButton button : gameBoardTiles) {
			if (ae.getSource() == button && button.getText().isEmpty()) {				
				completePlayersTurn(isCvPGame, button, isPlayerOnesTurn ? LIGHT_RED : Color.BLUE, 
					isPlayerOnesTurn ? PLAYER_TWO_LETTER : PLAYER_ONE_LETTER, 
					isPlayerOnesTurn ? getPlayerOnesName() : getPlayerTwosName());
				
				isPlayerOnesTurn = !isPlayerOnesTurn;
				isPlayerTwosTurn = !isPlayerTwosTurn;
				
				break;
			} 
			else if (ae.getSource() == button && !button.getText().isEmpty()) {
				logger_.warning("Invalid Move!");
				Toolkit.getDefaultToolkit().beep();
			}
		}
		
	}
	
	public void checkForPattern(String playerLetter) {
		
		// if buttons 0, 1, 2 are triggered - 3 in a row vertically - down the left side column
		if (tile[0].equals(playerLetter) && tile[1].equals(playerLetter) && tile[2].equals(playerLetter)) {
			logWinner(playerLetter);			
			displayWinnersPattern(gameBoardTiles[0], gameBoardTiles[1], gameBoardTiles[2]);
			new GameWinner(playerLetter.equals(PLAYER_ONE_LETTER) ? getPlayerOnesName() : getPlayerTwosName());
		} 
		
		// if buttons 3, 4, 5 are triggered - 3 in a row vertically - down the middle column
		else if (tile[3].equals(playerLetter) && tile[4].equals(playerLetter) && tile[5].equals(playerLetter)) {
			logWinner(playerLetter);			
			displayWinnersPattern(gameBoardTiles[3], gameBoardTiles[4], gameBoardTiles[5]);
			new GameWinner(playerLetter.equals(PLAYER_ONE_LETTER) ? getPlayerOnesName() : getPlayerTwosName());
		} 
		
		// if buttons 6, 7, 8 are triggered - 3 in a row vertically - down the right side column
		else if (tile[6].equals(playerLetter) && tile[7].equals(playerLetter) && tile[8].equals(playerLetter)) {
			logWinner(playerLetter);			
			displayWinnersPattern(gameBoardTiles[6], gameBoardTiles[7], gameBoardTiles[8]);
			new GameWinner(playerLetter.equals(PLAYER_ONE_LETTER) ? getPlayerOnesName() : getPlayerTwosName());
		} 
		
		// if buttons 0, 3, 6 are triggered - 3 in a row horizontally - across the top row
		else if (tile[0].equals(playerLetter) && tile[3].equals(playerLetter) && tile[6].equals(playerLetter)) {
			logWinner(playerLetter);			
			displayWinnersPattern(gameBoardTiles[0], gameBoardTiles[3], gameBoardTiles[6]);
			new GameWinner(playerLetter.equals(PLAYER_ONE_LETTER) ? getPlayerOnesName() : getPlayerTwosName());
		} 
		
		// if buttons 1, 4, 7 are triggered - 3 in a row horizontally - across the middle row
		else if (tile[1].equals(playerLetter) && tile[4].equals(playerLetter) && tile[7].equals(playerLetter)) {
			logWinner(playerLetter);			
			displayWinnersPattern(gameBoardTiles[1], gameBoardTiles[4], gameBoardTiles[7]);
			new GameWinner(playerLetter.equals(PLAYER_ONE_LETTER) ? getPlayerOnesName() : getPlayerTwosName());
		} 
		
		checkFurtherForPattern(playerLetter);
	}

	public void checkFurtherForPattern(String playerLetter) {
		
		// if buttons 2, 5, 8 are triggered - 3 in a row horizontally - across the bottom row
		if (tile[2].equals(playerLetter) && tile[5].equals(playerLetter) && tile[8].equals(playerLetter)) {
			logWinner(playerLetter);			
			displayWinnersPattern(gameBoardTiles[2], gameBoardTiles[5], gameBoardTiles[8]);
			new GameWinner(playerLetter.equals(PLAYER_ONE_LETTER) ? getPlayerOnesName() : getPlayerTwosName());
		} 
		
		// if buttons 0, 4, 8 are triggered - 3 in a row diagonally
		else if (tile[0].equals(playerLetter) && tile[4].equals(playerLetter) && tile[8].equals(playerLetter)) {
			logWinner(playerLetter);			
			displayWinnersPattern(gameBoardTiles[0], gameBoardTiles[4], gameBoardTiles[8]);
			new GameWinner(playerLetter.equals(PLAYER_ONE_LETTER) ? getPlayerOnesName() : getPlayerTwosName());
		} 
		
		// if buttons 2, 4, 6 are triggered - 3 in a row diagonally
		else if (tile[2].equals(playerLetter) && tile[4].equals(playerLetter) && tile[6].equals(playerLetter)) {
			logWinner(playerLetter);			
			displayWinnersPattern(gameBoardTiles[2], gameBoardTiles[4], gameBoardTiles[6]);
			new GameWinner(playerLetter.equals(PLAYER_ONE_LETTER) ? getPlayerOnesName() : getPlayerTwosName());
		} 
	}

	public void logWinner(String playerLetter) {
		String logMessage = playerLetter.equals(PLAYER_ONE_LETTER) ? getPlayerOnesName()
				: getPlayerTwosName();
		
		logger_.log(Level.INFO, "{0} wins!", logMessage);
	}
	
	/**
	 * Scans board after every move to see if a pattern of 3 has been made in a row, column, or diagonally or if all tiles have been clicked
	 */
	public void checkForWinner(boolean isCvPGame) {
		
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
	
		checkForPattern(PLAYER_ONE_LETTER);
		checkForPattern(PLAYER_TWO_LETTER);
		
		// isGameFinished boolean variable enforces the isBoardFull() from running, unless above method calls 
		// don't get any matches. This will prevent a full board with a player getting 3 in a row 
		// from displaying player won and draw at the same time error
		if(!isGameFinished && isBoardFull()) {
			new GameWinner(isCvPGame ? "Game Over! It's a draw!!" : "Game Over! It's a draw!");
			logger_.info("Game Over! It's a draw!");
		}
	}

	/**
	 * Checks if game board is full, displays game over - tie (draw)
	 */
	public boolean isBoardFull() {
		// return false if even just 1 of the tiles is empty, since then there's no way of finding the board to be filled out at that point
		return Arrays.stream(gameBoardTiles).noneMatch(x -> x.getText().isEmpty());
	}

	/**
	 * Highlights the winning player's selected tiles and disables the tiles
	 * @param tileOne   first tile clicked
	 * @param tileTwo   second tile clicked
	 * @param tileThree third tile clicked
	 */
	public void displayWinnersPattern(JButton tileOne, JButton tileTwo, JButton tileThree) {
		isGameFinished = true; 
		
		JButton[] highlightWinnersTiles = {tileOne, tileTwo, tileThree};
		
		for (JButton button : highlightWinnersTiles) {
			button.setBackground(GREEN);
			button.setBorder(new LineBorder(Color.BLUE, 3, true));
			
			// this will prevent the program from changing colors when you hover after winning
			button.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent evt) {
					button.setBackground(GREEN);
				}

				@Override
				public void mouseExited(MouseEvent evt) {
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
	 * Performs actions after player turn
	 * @param buttonPressed button that was just pressed by player 
	 */
	public void completePlayersTurn(boolean isCvPGame, JButton buttonPressed, Color color, String playersLetter, String playersName) {
		buttonPressed.setForeground(color);
		buttonPressed.setText(playersLetter);
		
		checkForWinner(isCvPGame);

		if(!isGameOver) {
			lblPlayersTurn.setText(playersName + "'s turn:");
		}
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
	 * @param argPlayerOnesName
	 */
	public static void setPlayerOnesName(String argPlayerOnesName) {
		PvPGameBoard.playerOnesName = argPlayerOnesName;
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
	 * @param argPlayerTwosName
	 */
	public static void setPlayerTwosName(String argPlayerTwosName) {
		PvPGameBoard.playerTwosName = argPlayerTwosName;
	}
}
