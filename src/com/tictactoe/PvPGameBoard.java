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

	protected static final JFrame window = new JFrame("Tic Tac Toe");
	private static final Color LIGHT_ORANGE_COLOR = new Color(222, 126, 0);
	private static final Color ULTRA_LIGHT_ORANGE_COLOR = new Color(244, 164, 96);
	private static final Logger logger_ = Logger.getLogger(PvPGameBoard.class.getName());
	protected static final Color LIGHT_RED_COLOR = new Color(232, 46, 6);
	protected static final String PLAYER_ONE_LETTER = "X";
	protected static final String PLAYER_TWO_LETTER = "O";
	protected static JButton[] gameBoardTiles; // the clickable board buttons
	protected static boolean isGameOver;
	private static String playerOnesName;
	private static String playerTwosName;

	protected String[] tile; // the String version of the clickable board buttons
	protected boolean isCvPGame; // determines current game mode, used to prevent bug
	protected JLabel lblPlayersTurn; // label that displays whose turn it is
	protected boolean isPlayerOnesTurn;
	protected boolean isPlayerTwosTurn;
	private JSeparator[] gameBoardSeparators; // game board divider lines (separators)

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
			logger_.severe("Failed to set LookAndFeel\n" + e.toString());
			e.printStackTrace();
		}

		initializeGame(argIsStart, argIsPlayerOnesTurn, argIsPlayerTwosTurn);

		isGameOver = false; // need to set variable to false here to avoid name label not changing bug

		tile = new String[9];
		gameBoardTiles = new JButton[9];
		gameBoardSeparators = new JSeparator[4];

		// assigning a background image to the app
		window.setContentPane(new JLabel(new ImageIcon("res/graphics/bg-image-tac.jpg")));

		// creates and sets up the board tiles
		for (int i = 0; i < gameBoardTiles.length; i++) {
			gameBoardTiles[i] = new JButton();
			window.getContentPane().add(gameBoardTiles[i]);
			gameBoardTiles[i].addActionListener(this);
			gameBoardTiles[i].setSize(80, 70);
			gameBoardTiles[i].setFont(new Font("Magneto", Font.PLAIN, 35));
			gameBoardTiles[i].setBackground(ULTRA_LIGHT_ORANGE_COLOR);
			gameBoardTiles[i].setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.GRAY));

			final int x = i;
			gameBoardTiles[i].addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent evt) {
					gameBoardTiles[x].setBackground(LIGHT_ORANGE_COLOR);
				}

				@Override
				public void mouseExited(MouseEvent evt) {
					gameBoardTiles[x].setBackground(ULTRA_LIGHT_ORANGE_COLOR);
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

		lblPlayersTurn = new JLabel(String.format("%s's turn (%s):", getPlayerOnesName(), PLAYER_ONE_LETTER));
		lblPlayersTurn.setBounds(63, 15, 260, 38);
		lblPlayersTurn.setOpaque(false);
		window.getContentPane().add(lblPlayersTurn);

		// creates and sets up the board line dividers
		for (int x = 0; x < gameBoardSeparators.length; x++) {
			gameBoardSeparators[x] = new JSeparator();
			gameBoardSeparators[x].setBackground(Color.BLUE);
			window.getContentPane().add(gameBoardSeparators[x]);

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

		window.setResizable(false);
		window.setSize(399, 358);
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setLocation(StartMenu.window.getX(), StartMenu.window.getY());
		window.setVisible(true);
	}

	public PvPGameBoard(boolean argIsStart, boolean argIsPlayerOnesTurn, boolean argIsPlayerTwosTurn, String setLocationToHere) {
		this(argIsStart, argIsPlayerOnesTurn, argIsPlayerTwosTurn);
		window.setLocation(Integer.parseInt(setLocationToHere.split(",")[0]), Integer.parseInt(setLocationToHere.split(",")[1]));
	}


	/**
	 * Setups the current game: makes decision on who's turn it is and assigns
	 * player entered names. Enforces player1 to always start first: Sets p1 and p2's turns for first round
	 *
	 * @param argIsStart         boolean flag indicating whether or not the game has just
	 *                  begun
	 * @param pOnesTurn boolean flag indicating if it's player one's turn in the
	 *                  game
	 * @param pTwosTurn boolean flag indicating if it's player two's turn in the
	 *                  game
	 */
	protected void initializeGame(boolean argIsStart, boolean argIsPlayerOnesTurn, boolean argIsPlayerTwosTurn) {
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
				completePlayersTurn(isCvPGame, button, isPlayerOnesTurn ? LIGHT_RED_COLOR : Color.BLUE,
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

	/**
	 * Scans board for any 3 in a row combos made by either a player
	 * @param playerLetter player's board letter type
	 */
	private void checkForPattern(String playerLetter) {

		// 3 in a row gameboard cells that trigger game win:
		// if buttons 3, 4, 5 are triggered - 3 in a row vertically - down the middle column
		// if buttons 6, 7, 8 are triggered - 3 in a row vertically - down the right side column
		// if buttons 0, 3, 6 are triggered - 3 in a row horizontally - across the top row
		// if buttons 1, 4, 7 are triggered - 3 in a row horizontally - across the middle row
		// if buttons 2, 5, 8 are triggered - 3 in a row horizontally - across the bottom row
		// if buttons 0, 4, 8 are triggered - 3 in a row diagonally
		// if buttons 2, 4, 6 are triggered - 3 in a row diagonally
		final String[] PAIR_ARR = {"0|1|2", "3|4|5", "6|7|8", "0|3|6", "1|4|7", "2|5|8", "0|4|8", "2|4|6"};

		for (String pair : PAIR_ARR) {
			checkPair(playerLetter, pair);
		}
	}

	private void checkPair(String playerLetter, String pairArr) {
		int button1 = Integer.parseInt(pairArr.split("|")[0]);
		int button2 = Integer.parseInt(pairArr.split("|")[2]);
		int button3 = Integer.parseInt(pairArr.split("|")[4]);

		// if buttons 0, 1, 2 are triggered (pressed) - 3 in a row vertically - down the left side column
		if (tile[button1].equals(playerLetter) && tile[button2].equals(playerLetter) && tile[button3].equals(playerLetter)) {
			displayWinnersPattern(gameBoardTiles[button1], gameBoardTiles[button2], gameBoardTiles[button3]);
			new GameWinner(PLAYER_ONE_LETTER.equals(playerLetter) ? getPlayerOnesName() : getPlayerTwosName());
			logWinner(playerLetter);
		}
	}

	/**
	 * Logs the current game's winner
	 * @param playerLetter player's letter type - used to get the winner's name
	 */
	private void logWinner(String playerLetter) {
		logger_.log(Level.INFO, "{0} wins!", PLAYER_ONE_LETTER.equals(playerLetter) ? getPlayerOnesName()
				: getPlayerTwosName());
	}

	/**
	 * Scans board after every move to see if a pattern of 3 has been made in a row, column, or diagonally or if all tiles have been clicked
	 */
	private void checkForWinner(boolean isCvPGame) {

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
		if(!isGameOver && isBoardFull()) {
			new GameWinner(isCvPGame ? "Game Over! It's a draw!!" : "Game Over! It's a draw!");
			logger_.info("Game Over! It's a draw!");
		}
	}

	/**
	 * Checks if game board is full, displays game over - tie (draw)
	 */
	private boolean isBoardFull() {
		// return false if even just 1 of the tiles is empty, since then there's no way of finding the board to be filled out at that point
		return Arrays.stream(gameBoardTiles).noneMatch(x -> x.getText().isEmpty());
	}

	/**
	 * Highlights the winning player's selected tiles and disables the tiles
	 * @param tileOne   first tile clicked
	 * @param tileTwo   second tile clicked
	 * @param tileThree third tile clicked
	 */
	private static void displayWinnersPattern(JButton tileOne, JButton tileTwo, JButton tileThree) {
		isGameOver = true;

		JButton[] highlightWinnersTiles = {tileOne, tileTwo, tileThree};

		// this will prevent game board tiles from changing color when a game is won
		for (int i = 0; i < gameBoardTiles.length; i++) {
			final int x = i;
			gameBoardTiles[i].addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent evt) {
					gameBoardTiles[x].setBackground(ULTRA_LIGHT_ORANGE_COLOR);
				}
			});
		}

		// makes game board tiles that player won with green when a game is won
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
	 * Performs actions after player's turn
	 * @param buttonPressed button that was just pressed by player
	 */
	protected void completePlayersTurn(boolean isCvPGame, JButton buttonPressed, Color color, String playersLetter, String playersName) {
		buttonPressed.setForeground(color);
		buttonPressed.setText(playersLetter);

		checkForWinner(isCvPGame);

		playersLetter = (playersLetter.equals(PLAYER_ONE_LETTER)) ? PLAYER_TWO_LETTER : PLAYER_ONE_LETTER;

		if(!isGameOver) {
			lblPlayersTurn.setText(String.format("%s's turn (%s):", playersName, playersLetter));
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
