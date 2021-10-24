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
	static boolean isPlayerOnesTurn, isPlayerTwosTurn, start;
	private static JLabel lblPlayersTurn;
	private static final Logger logger = Logger.getLogger(GameBoard.class);
	static String playerOneWinningMessage, playerTwoWinningMessage;
	JButton[] buttons = new JButton[9];
	JButton[] highlightWinButtons = new JButton[3];

	// needed to invert these to fix a window2 symbol problem
	static final String PLAYER_ONE_SHAPE = "O", PLAYER_TWO_SHAPE = "X";

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

		playerOneWinningMessage = "Player 1 (" + playerOnesName + ") wins!";
		playerTwoWinningMessage = "Player 2 (" + playerTwosName + ") wins!";
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
		
		for(int i = 0; i < buttons.length; i++) {
			buttons[i] = new JButton();
			f.getContentPane().add(buttons[i]);
			buttons[i].addActionListener(this);
			buttons[i].setFont(new Font("Magneto", Font.PLAIN, 20));
			buttons[i].setBackground(new Color(244, 164, 96));
			
			final int x = i;
			buttons[i].addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					buttons[x].setBackground(new Color(222, 126, 0));
				}

				@Override
				public void mouseExited(java.awt.event.MouseEvent evt) {
					buttons[x].setBackground(new Color(244, 164, 96));
				}
			});
		}
		buttons[0].setBounds(63, 64, 80, 70);
		buttons[1].setBounds(63, 145, 80, 70);
		buttons[2].setBounds(63, 226, 80, 70);
		buttons[3].setBounds(153, 64, 80, 70);
		buttons[4].setBounds(153, 145, 80, 70);
		buttons[5].setBounds(153, 226, 80, 70);
		buttons[6].setBounds(243, 64, 80, 70);
		buttons[7].setBounds(243, 145, 80, 70);
		buttons[8].setBounds(243, 226, 80, 70);

		lblPlayersTurn.setBounds(63, 15, 260, 38);
		f.getContentPane().add(lblPlayersTurn);

		JSeparator separatorOne = new JSeparator();
		separatorOne.setBounds(63, 138, 260, 11);
		separatorOne.setBackground(Color.blue);
		f.getContentPane().add(separatorOne);

		JSeparator separatorTwo = new JSeparator();
		separatorTwo.setBounds(63, 221, 260, 11);
		separatorTwo.setBackground(Color.blue);
		f.getContentPane().add(separatorTwo);

		JSeparator separatorThree = new JSeparator();
		separatorThree.setOrientation(SwingConstants.VERTICAL);
		separatorThree.setBounds(148, 64, 7, 232);
		separatorThree.setBackground(Color.blue);
		f.getContentPane().add(separatorThree);

		JSeparator separatorFour = new JSeparator();
		separatorFour.setOrientation(SwingConstants.VERTICAL);
		separatorFour.setBounds(237, 64, 7, 232);
		separatorFour.setBackground(Color.blue);
		f.getContentPane().add(separatorFour);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		// enforces player1 to always start first
		if (start) {
			isPlayerOnesTurn = !isPlayerOnesTurn;
			isPlayerTwosTurn = !isPlayerTwosTurn;
			start = false;
		}

		if (ae.getSource() == buttons[0] && buttons[0].getText().isEmpty()) {
			if (isPlayerOnesTurn) {
				playerOnesTurnComplete(buttons[0]);
			} else if (isPlayerTwosTurn) {
				playerTwosTurnComplete(buttons[0]);
			}
		} else if (ae.getSource() == buttons[1] && buttons[1].getText().isEmpty()) {
			if (isPlayerOnesTurn) {
				playerOnesTurnComplete(buttons[1]);
			} else if (isPlayerTwosTurn) {
				playerTwosTurnComplete(buttons[1]);
			}
		} else if (ae.getSource() == buttons[2] && buttons[2].getText().isEmpty()) {
			if (isPlayerOnesTurn) {
				playerOnesTurnComplete(buttons[2]);
			} else if (isPlayerTwosTurn) {
				playerTwosTurnComplete(buttons[2]);
			}
		} else if (ae.getSource() == buttons[3] && buttons[3].getText().isEmpty()) {
			if (isPlayerOnesTurn) {
				playerOnesTurnComplete(buttons[3]);
			} else if (isPlayerTwosTurn) {
				playerTwosTurnComplete(buttons[3]);
			}
		} else if (ae.getSource() == buttons[4] && buttons[4].getText().isEmpty()) {
			if (isPlayerOnesTurn) {
				playerOnesTurnComplete(buttons[4]);
			} else if (isPlayerTwosTurn) {
				playerTwosTurnComplete(buttons[4]);
			}
		} else if (ae.getSource() == buttons[5] && buttons[5].getText().isEmpty()) {
			if (isPlayerOnesTurn) {
				playerOnesTurnComplete(buttons[5]);
			} else if (isPlayerTwosTurn) {
				playerTwosTurnComplete(buttons[5]);
			}
		} else if (ae.getSource() == buttons[6] && buttons[6].getText().isEmpty()) {
			if (isPlayerOnesTurn) {
				playerOnesTurnComplete(buttons[6]);
			} else if (isPlayerTwosTurn) {
				playerTwosTurnComplete(buttons[6]);
			}
		} else if (ae.getSource() == buttons[7] && buttons[7].getText().isEmpty()) {
			if (isPlayerOnesTurn) {
				playerOnesTurnComplete(buttons[7]);
			} else if (isPlayerTwosTurn) {
				playerTwosTurnComplete(buttons[7]);
			}
		} else if (ae.getSource() == buttons[8] && buttons[8].getText().isEmpty()) {
			if (isPlayerOnesTurn) {
				playerOnesTurnComplete(buttons[8]);
			} else if (isPlayerTwosTurn) {
				playerTwosTurnComplete(buttons[8]);
			}
		} else {
			logger.warn("Invalid Move!");
		}

		// game rules: Need 3 in a row in any direction
		
		/*
		 * Game board reference (button #'s):
		 * 	1  4  7
		 *  2  5  8
		 *  3  6  9
		 */
		
		// if all buttons are pressed, default to game over. However if succeeding 'if' condition is triggered this will be ignored
		if (!buttons[0].getText().isEmpty() && !buttons[1].getText().isEmpty() && !buttons[2].getText().isEmpty()
				&& !buttons[3].getText().isEmpty() && !buttons[4].getText().isEmpty() && !buttons[5].getText().isEmpty()
				&& !buttons[6].getText().isEmpty() && !buttons[7].getText().isEmpty() && !buttons[8].getText().isEmpty()) {
			new Winner("Game Over! Tie");
		}

		// if buttons 1, 2, 3 are triggered
		if (buttons[0].getText().equals("X") && buttons[1].getText().equals("X") && buttons[2].getText().equals("X")) {
			logger.info(playerOneWinningMessage);
			winnersPattern(buttons[0], buttons[1], buttons[2]);
			new Winner(playerOnesName);
		} else if (buttons[0].getText().equals("O") && buttons[1].getText().equals("O")
				&& buttons[2].getText().equals("O")) {
			logger.info(playerTwoWinningMessage);
			winnersPattern(buttons[0], buttons[1], buttons[2]);
			new Winner(playerTwosName);
		}

		// if buttons 4, 5, 6 are triggered
		if (buttons[3].getText().equals("X") && buttons[4].getText().equals("X") && buttons[5].getText().equals("X")) {
			logger.info(playerOneWinningMessage);
			winnersPattern(buttons[3], buttons[4], buttons[5]);
			new Winner(playerOnesName);
		} else if (buttons[3].getText().equals("O") && buttons[4].getText().equals("O") && buttons[5].getText().equals("O")) {
			logger.info(playerTwoWinningMessage);
			winnersPattern(buttons[3], buttons[4], buttons[5]);
			new Winner(playerTwosName);
		}

		// if buttons 7, 8, 9 are triggered
		if (buttons[6].getText().equals("X") && buttons[7].getText().equals("X") && buttons[8].getText().equals("X")) {
			logger.info(playerOneWinningMessage);
			winnersPattern(buttons[6], buttons[7], buttons[8]);
			new Winner(playerOnesName);
		} else if (buttons[6].getText().equals("O") && buttons[7].getText().equals("O") && buttons[8].getText().equals("O")) {
			logger.info(playerTwoWinningMessage);
			winnersPattern(buttons[6], buttons[7], buttons[8]);
			new Winner(playerTwosName);
		}

		// if buttons 1, 4, 7 are triggered
		if (buttons[0].getText().equals("X") && buttons[3].getText().equals("X") && buttons[6].getText().equals("X")) {
			logger.info(playerOneWinningMessage);
			winnersPattern(buttons[0], buttons[3], buttons[6]);
			new Winner(playerOnesName);
		} else if (buttons[0].getText().equals("O") && buttons[3].getText().equals("O")
				&& buttons[6].getText().equals("O")) {
			logger.info(playerTwoWinningMessage);
			winnersPattern(buttons[0], buttons[3], buttons[6]);
			new Winner(playerTwosName);
		}

		// if buttons 2, 5, 8 are triggered
		if (buttons[1].getText().equals("X") && buttons[4].getText().equals("X") && buttons[7].getText().equals("X")) {
			logger.info(playerOneWinningMessage);
			winnersPattern(buttons[1], buttons[4], buttons[7]);
			new Winner(playerOnesName);
		} else if (buttons[1].getText().equals("O") && buttons[4].getText().equals("O") && buttons[7].getText().equals("O")) {
			logger.info(playerTwoWinningMessage);
			winnersPattern(buttons[1], buttons[4], buttons[7]);
			new Winner(playerTwosName);
		}

		// if buttons 3, 6, 9 are triggered
		if (buttons[2].getText().equals("X") && buttons[5].getText().equals("X") && buttons[8].getText().equals("X")) {
			logger.info(playerOneWinningMessage);
			winnersPattern(buttons[2], buttons[5], buttons[8]);
			new Winner(playerOnesName);
		} else if (buttons[2].getText().equals("O") && buttons[5].getText().equals("O") && buttons[8].getText().equals("O")) {
			logger.info(playerTwoWinningMessage);
			winnersPattern(buttons[2], buttons[5], buttons[8]);
			new Winner(playerTwosName);
		}

		// if buttons 1, 5, 9 are triggered
		if (buttons[0].getText().equals("X") && buttons[4].getText().equals("X") && buttons[8].getText().equals("X")) {
			logger.info(playerOneWinningMessage);
			winnersPattern(buttons[0], buttons[4], buttons[8]);
			new Winner(playerOnesName);
		} else if (buttons[0].getText().equals("O") && buttons[4].getText().equals("O") && buttons[8].getText().equals("O")) {
			logger.info(playerTwoWinningMessage);
			winnersPattern(buttons[0], buttons[4], buttons[8]);
			new Winner(playerTwosName);
		}

		// if buttons 3, 5, 7 are triggered
		if (buttons[2].getText().equals("X") && buttons[4].getText().equals("X") && buttons[6].getText().equals("X")) {
			logger.info(playerOneWinningMessage);
			winnersPattern(buttons[2], buttons[4], buttons[6]);
			new Winner(playerOnesName);
		} else if (buttons[2].getText().equals("O") && buttons[4].getText().equals("O") && buttons[6].getText().equals("O")) {
			logger.info(playerTwoWinningMessage);
			winnersPattern(buttons[2], buttons[4], buttons[6]);
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
		highlightWinButtons[0] = one;
		highlightWinButtons[1] = two;
		highlightWinButtons[2] = three;
		
		for(int i = 0; i < highlightWinButtons.length; i++) {
			highlightWinButtons[i].setBackground(Color.GREEN);
			
			// this will prevent the program from changing colors when you hover after winning
			final int x = i;
			highlightWinButtons[i].addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					highlightWinButtons[x].setBackground(Color.GREEN);
				}

				@Override
				public void mouseExited(java.awt.event.MouseEvent evt) {
					highlightWinButtons[x].setBackground(Color.GREEN);
				}
			});
		}
		
		// disables all 9 buttons on board after game is over
		for(int x = 0; x < buttons.length; x++) {
			buttons[x].setEnabled(false);
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
