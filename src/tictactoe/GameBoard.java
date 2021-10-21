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
	JButton btnOne, btnTwo, btnThree, btnFour, btnFive;
	JButton btnSix, btnSeven, btnEight, btnNine;
	static String playerOnesName, playerTwosName;
	static boolean isPlayerOnesTurn, isPlayerTwosTurn;
	private static JLabel PLAYERS_TURN_LABEL;
	private static final Logger logger = Logger.getLogger(GameBoard.class);
	static String pOneWins, pTwoWins;

	// needed to invert these to fix a window2 symbol problem
	static final String PLAYER_ONE_SHAPE = "O";
	static final String PLAYER_TWO_SHAPE = "X";
	static boolean start;

	/**
	 * Setups the current game: makes decision on who's turn it is and assigns
	 * player entered names
	 * 
	 * @param s         boolean flag indicating whether or not the game has just
	 *                  begun
	 * @param pOnesTurn boolean flag indicating if it's player one's turn in the
	 *                  game
	 * @param pTwosTurn boolean flag indicating if it's player two's turn in the
	 *                  game
	 */
	public static void initializeGame(boolean s, boolean pOnesTurn, boolean pTwosTurn) {
		start = s;
		isPlayerOnesTurn = pOnesTurn;
		isPlayerTwosTurn = pTwosTurn;

		pOneWins = "Player 1 (" + playerOnesName + ") wins!";
		pTwoWins = "Player 2 (" + playerTwosName + ") wins!";
	}

	/**
	 * Builds the game's GUI board
	 * 
	 * @param s         boolean flag indicating whether or not the game has just
	 *                  begun
	 * @param pOnesTurn boolean flag indicating if it's player one's turn in the
	 *                  game
	 * @param pTwosTurn boolean flag indicating if it's player two's turn in the
	 *                  game
	 */
	public GameBoard(boolean s, boolean pOnesTurn, boolean pTwosTurn) {

		initializeGame(s, pOnesTurn, pTwosTurn);

		PLAYERS_TURN_LABEL = new JLabel(playerOnesName + "'s turn:");

		f.setResizable(false);
		f.setBounds(100, 100, 399, 358);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().setLayout(null);
		f.setLocationRelativeTo(null);
		f.setVisible(true);

		// assigning a background image to the app
		f.setContentPane(new JLabel(new ImageIcon("res/graphics/bgImageToe.jpg")));

		btnOne = new JButton("");
		btnOne.setBounds(63, 64, 80, 70);
		f.getContentPane().add(btnOne);
		btnOne.addActionListener(this);
		btnOne.setBackground(new Color(244, 164, 96));
		btnOne.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnOne.setBackground(new Color(222, 126, 0));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnOne.setBackground(new Color(244, 164, 96));
			}
		});

		btnTwo = new JButton("");
		btnTwo.setBounds(63, 145, 80, 70);
		f.getContentPane().add(btnTwo);
		btnTwo.addActionListener(this);
		btnTwo.setBackground(new Color(244, 164, 96));
		btnTwo.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnTwo.setBackground(new Color(222, 126, 0));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnTwo.setBackground(new Color(244, 164, 96));
			}
		});

		btnThree = new JButton("");
		btnThree.setBounds(63, 226, 80, 70);
		f.getContentPane().add(btnThree);
		btnThree.addActionListener(this);
		btnThree.setBackground(new Color(244, 164, 96));
		btnThree.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnThree.setBackground(new Color(222, 126, 0));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnThree.setBackground(new Color(244, 164, 96));
			}
		});

		btnFour = new JButton();
		btnFour.setBounds(153, 64, 80, 70);
		f.getContentPane().add(btnFour);
		btnFour.addActionListener(this);
		btnFour.setBackground(new Color(244, 164, 96));
		btnFour.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnFour.setBackground(new Color(222, 126, 0));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnFour.setBackground(new Color(244, 164, 96));
			}
		});

		btnFive = new JButton();
		btnFive.setBounds(153, 145, 80, 70);
		f.getContentPane().add(btnFive);
		btnFive.addActionListener(this);
		btnFive.setBackground(new Color(244, 164, 96));
		btnFive.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnFive.setBackground(new Color(222, 126, 0));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnFive.setBackground(new Color(244, 164, 96));
			}
		});

		btnSix = new JButton();
		btnSix.setBounds(153, 226, 80, 70);
		f.getContentPane().add(btnSix);
		btnSix.addActionListener(this);
		btnSix.setBackground(new Color(244, 164, 96));
		btnSix.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnSix.setBackground(new Color(222, 126, 0));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnSix.setBackground(new Color(244, 164, 96));
			}
		});

		btnSeven = new JButton();
		btnSeven.setBounds(243, 64, 80, 70);
		f.getContentPane().add(btnSeven);
		btnSeven.addActionListener(this);
		btnSeven.setBackground(new Color(244, 164, 96));
		btnSeven.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnSeven.setBackground(new Color(222, 126, 0));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnSeven.setBackground(new Color(244, 164, 96));
			}
		});

		btnEight = new JButton();
		btnEight.setBounds(243, 145, 80, 70);
		f.getContentPane().add(btnEight);
		btnEight.addActionListener(this);
		btnEight.setBackground(new Color(244, 164, 96));
		btnEight.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnEight.setBackground(new Color(222, 126, 0));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnEight.setBackground(new Color(244, 164, 96));
			}
		});

		btnNine = new JButton();
		btnNine.setBounds(243, 226, 80, 70);
		f.getContentPane().add(btnNine);
		btnNine.addActionListener(this);
		btnNine.setBackground(new Color(244, 164, 96));
		PLAYERS_TURN_LABEL.setFont(new Font("Bookman Old Style", Font.PLAIN, 13));
		btnNine.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnNine.setBackground(new Color(222, 126, 0));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnNine.setBackground(new Color(244, 164, 96));
			}
		});

		PLAYERS_TURN_LABEL.setBounds(63, 15, 260, 38);
		f.getContentPane().add(PLAYERS_TURN_LABEL);

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

		// enforces player1 to always start first and then swap roles every match
		if (start) {
			isPlayerOnesTurn = !isPlayerOnesTurn;
			isPlayerTwosTurn = !isPlayerTwosTurn;
			start = false;
		}

		if (ae.getSource() == btnOne && btnOne.getText().isEmpty()) {
			if (isPlayerOnesTurn) {
				playerOnesTurnComplete(btnOne);
			} else if (isPlayerTwosTurn) {
				playerTwosTurnComplete(btnOne);
			}
		} else if (ae.getSource() == btnTwo && btnTwo.getText().isEmpty()) {
			if (isPlayerOnesTurn) {
				playerOnesTurnComplete(btnTwo);
			} else if (isPlayerTwosTurn) {
				playerTwosTurnComplete(btnTwo);
			}
		} else if (ae.getSource() == btnThree && btnThree.getText().isEmpty()) {
			if (isPlayerOnesTurn) {
				playerOnesTurnComplete(btnThree);
			} else if (isPlayerTwosTurn) {
				playerTwosTurnComplete(btnThree);
			}
		} else if (ae.getSource() == btnFour && btnFour.getText().isEmpty()) {
			if (isPlayerOnesTurn) {
				playerOnesTurnComplete(btnFour);
			} else if (isPlayerTwosTurn) {
				playerTwosTurnComplete(btnFour);
			}
		} else if (ae.getSource() == btnFive && btnFive.getText().isEmpty()) {
			if (isPlayerOnesTurn) {
				playerOnesTurnComplete(btnFive);
			} else if (isPlayerTwosTurn) {
				playerTwosTurnComplete(btnFive);
			}
		} else if (ae.getSource() == btnSix && btnSix.getText().isEmpty()) {
			if (isPlayerOnesTurn) {
				playerOnesTurnComplete(btnSix);
			} else if (isPlayerTwosTurn) {
				playerTwosTurnComplete(btnSix);
			}
		} else if (ae.getSource() == btnSeven && btnSeven.getText().isEmpty()) {
			if (isPlayerOnesTurn) {
				playerOnesTurnComplete(btnSeven);
			} else if (isPlayerTwosTurn) {
				playerTwosTurnComplete(btnSeven);
			}
		} else if (ae.getSource() == btnEight && btnEight.getText().isEmpty()) {
			if (isPlayerOnesTurn) {
				playerOnesTurnComplete(btnEight);
			} else if (isPlayerTwosTurn) {
				playerTwosTurnComplete(btnEight);
			}
		} else if (ae.getSource() == btnNine && btnNine.getText().isEmpty()) {
			if (isPlayerOnesTurn) {
				playerOnesTurnComplete(btnNine);
			} else if (isPlayerTwosTurn) {
				playerTwosTurnComplete(btnNine);
			}
		} else {
			logger.warn("Invalid Move!");
		}

		// game rules: Need 3 in a row in any direction
		if (!btnOne.getText().isEmpty() && !btnTwo.getText().isEmpty() && !btnThree.getText().isEmpty()
				&& !btnFour.getText().isEmpty() && !btnFive.getText().isEmpty() && !btnSix.getText().isEmpty()
				&& !btnSeven.getText().isEmpty() && !btnEight.getText().isEmpty() && !btnNine.getText().isEmpty()) {
			new Winner("Game Over! Tie");
		}
		if (!btnOne.getText().isEmpty() && !btnTwo.getText().isEmpty() && !btnThree.getText().isEmpty()) {
			if (btnOne.getText().equals("X") && btnTwo.getText().equals("X") && btnThree.getText().equals("X")) {
				logger.info(pOneWins);
				new Winner(playerOnesName);
			} else if (btnOne.getText().equals("O") && btnTwo.getText().equals("O") && btnThree.getText().equals("O")) {
				logger.info(pTwoWins);
				new Winner(playerTwosName);
			}
		}
		if (!btnFour.getText().isEmpty() && !btnFive.getText().isEmpty() && !btnSix.getText().isEmpty()) {
			if (btnFour.getText().equals("X") && btnFive.getText().equals("X") && btnSix.getText().equals("X")) {
				logger.info(pOneWins);
				new Winner(playerOnesName);
			} else if (btnFour.getText().equals("O") && btnFive.getText().equals("O") && btnSix.getText().equals("O")) {
				logger.info(pTwoWins);
				new Winner(playerTwosName);
			}
		}
		if (!btnSeven.getText().isEmpty() && !btnEight.getText().isEmpty() && !btnNine.getText().isEmpty()) {
			if (btnSeven.getText().equals("X") && btnEight.getText().equals("X") && btnNine.getText().equals("X")) {
				logger.info(pOneWins);
				new Winner(playerOnesName);
			} else if (btnSeven.getText().equals("O") && btnEight.getText().equals("O")
					&& btnNine.getText().equals("O")) {
				logger.info(pTwoWins);
				new Winner(playerTwosName);
			}
		}
		if (!btnOne.getText().isEmpty() && !btnFour.getText().isEmpty() && !btnSeven.getText().isEmpty()) {
			if (btnOne.getText().equals("X") && btnFour.getText().equals("X") && btnSeven.getText().equals("X")) {
				logger.info(pOneWins);
				new Winner(playerOnesName);
			} else if (btnOne.getText().equals("O") && btnFour.getText().equals("O")
					&& btnSeven.getText().equals("O")) {
				logger.info(pTwoWins);
				new Winner(playerTwosName);
			}
		}
		if (!btnTwo.getText().isEmpty() && !btnFive.getText().isEmpty() && !btnEight.getText().isEmpty()) {
			if (btnTwo.getText().equals("X") && btnFive.getText().equals("X") && btnEight.getText().equals("X")) {
				logger.info(pOneWins);
				new Winner(playerOnesName);
			} else if (btnTwo.getText().equals("O") && btnFive.getText().equals("O")
					&& btnEight.getText().equals("O")) {
				logger.info(pTwoWins);
				new Winner(playerTwosName);
			}
		}
		if (!btnThree.getText().isEmpty() && !btnSix.getText().isEmpty() && !btnNine.getText().isEmpty()) {
			if (btnThree.getText().equals("X") && btnSix.getText().equals("X") && btnNine.getText().equals("X")) {
				logger.info(pOneWins);
				new Winner(playerOnesName);
			} else if (btnThree.getText().equals("O") && btnSix.getText().equals("O")
					&& btnNine.getText().equals("O")) {
				logger.info(pTwoWins);
				new Winner(playerTwosName);
			}
		}
		if (!btnOne.getText().isEmpty() && !btnFive.getText().isEmpty() && !btnNine.getText().isEmpty()) {
			if (btnOne.getText().equals("X") && btnFive.getText().equals("X") && btnNine.getText().equals("X")) {
				logger.info(pOneWins);
				new Winner(playerOnesName);
			} else if (btnOne.getText().equals("O") && btnFive.getText().equals("O") && btnNine.getText().equals("O")) {
				logger.info(pTwoWins);
				new Winner(playerTwosName);
			}
		}
		if (!btnThree.getText().isEmpty() && !btnFive.getText().isEmpty() && !btnSeven.getText().isEmpty()) {
			if (btnThree.getText().equals("X") && btnFive.getText().equals("X") && btnSeven.getText().equals("X")) {
				logger.info(pOneWins);
				new Winner(playerOnesName);
			} else if (btnThree.getText().equals("O") && btnFive.getText().equals("O")
					&& btnSeven.getText().equals("O")) {
				logger.info(pTwoWins);
				new Winner(playerTwosName);
			}
		}
	}

	/**
	 * Performs actions after player one's turn
	 * 
	 * @param buttonPressed button that was just pressed by player one
	 */
	public static void playerOnesTurnComplete(JButton buttonPressed) {
		buttonPressed.setText(PLAYER_ONE_SHAPE);
		PLAYERS_TURN_LABEL.setText(playerOnesName + "'s turn:");
		isPlayerOnesTurn = !isPlayerOnesTurn;
		isPlayerTwosTurn = !isPlayerTwosTurn;
	}

	/**
	 * Performs actions after player two's turn
	 * 
	 * @param buttonPressed button that was just pressed by player two
	 */
	public static void playerTwosTurnComplete(JButton buttonPressed) {
		buttonPressed.setText(PLAYER_TWO_SHAPE);
		PLAYERS_TURN_LABEL.setText(playerTwosName + "'s turn:");
		isPlayerOnesTurn = !isPlayerOnesTurn;
		isPlayerTwosTurn = !isPlayerTwosTurn;
	}
}
