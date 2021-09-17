package tic.tac.toe;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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
	static String playerOne, playerTwo;
	boolean playerOnesTurn, playerTwosTurn;
	private final JLabel LABEL_PLAYER_TURN = new JLabel(playerOne + "'s turn:");
	private static final Logger logger = Logger.getLogger(GameBoard.class);
	final String P1WINS = "Player 1 wins!";
	final String P2WINS = "Player 2 wins!";

	// needed to invert these to fix a window2 symbol problem
	static final String PLAYER_ONE_SHAPE = "O";
	static final String PLAYER_TWO_SHAPE = "X";
	static boolean start;

	/**
	 * Builds the game's GUI board
	 * @param s boolean flag indicating whether or not the game has just begun
	 * @param pOnesTurn boolean flag indicating if it's player one's turn in the game
	 * @param pTwosTurn boolean flag indicating if it's player two's turn in the game
	 */
	public GameBoard(boolean s, boolean pOnesTurn, boolean pTwosTurn) {
		start = s;
		playerOnesTurn = pOnesTurn;
		playerTwosTurn = pTwosTurn;

		f.setResizable(false);
		f.setBounds(100, 100, 399, 358);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().setLayout(null);
		f.setVisible(true);
		f.setLocationRelativeTo(null);

		// assigning a background image to the app
		File bgImageFile = new File("res/graphics/bgImageToe.jpg");
		ImageIcon image = new ImageIcon(bgImageFile.toString());
		JLabel backgroundLabel = new JLabel(image);
		f.setContentPane(backgroundLabel);

		btnOne = new JButton("");
		btnOne.setBounds(63, 64, 80, 70);
		f.getContentPane().add(btnOne);
		btnOne.addActionListener(this);
		btnOne.setBackground(new Color(244, 164, 96));

		btnTwo = new JButton("");
		btnTwo.setBounds(63, 145, 80, 70);
		f.getContentPane().add(btnTwo);
		btnTwo.addActionListener(this);
		btnTwo.setBackground(new Color(244, 164, 96));

		btnThree = new JButton("");
		btnThree.setBounds(63, 226, 80, 70);
		f.getContentPane().add(btnThree);
		btnThree.addActionListener(this);
		btnThree.setBackground(new Color(244, 164, 96));

		btnFour = new JButton();
		btnFour.setBounds(153, 64, 80, 70);
		f.getContentPane().add(btnFour);
		btnFour.addActionListener(this);
		btnFour.setBackground(new Color(244, 164, 96));

		btnFive = new JButton();
		btnFive.setBounds(153, 145, 80, 70);
		f.getContentPane().add(btnFive);
		btnFive.addActionListener(this);
		btnFive.setBackground(new Color(244, 164, 96));

		btnSix = new JButton();
		btnSix.setBounds(153, 226, 80, 70);
		f.getContentPane().add(btnSix);
		btnSix.addActionListener(this);
		btnSix.setBackground(new Color(244, 164, 96));

		btnSeven = new JButton();
		btnSeven.setBounds(243, 64, 80, 70);
		f.getContentPane().add(btnSeven);
		btnSeven.addActionListener(this);
		btnSeven.setBackground(new Color(244, 164, 96));

		btnEight = new JButton();
		btnEight.setBounds(243, 145, 80, 70);
		f.getContentPane().add(btnEight);
		btnEight.addActionListener(this);
		btnEight.setBackground(new Color(244, 164, 96));

		btnNine = new JButton();
		btnNine.setBounds(243, 226, 80, 70);
		f.getContentPane().add(btnNine);
		btnNine.addActionListener(this);
		btnNine.setBackground(new Color(244, 164, 96));

		LABEL_PLAYER_TURN.setBounds(63, 15, 260, 38);
		f.getContentPane().add(LABEL_PLAYER_TURN);

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

		if (start) {
			playerOnesTurn = !playerOnesTurn;
			playerTwosTurn = !playerTwosTurn;
			start = false;
		}
		
		if (ae.getSource() == btnOne && btnOne.getText().isEmpty()) {
			if (playerOnesTurn) {
				playerOnesTurn(btnOne);
			} else if (playerTwosTurn) {
				playerTwosTurn(btnOne);
			}
		} else if (ae.getSource() == btnTwo && btnTwo.getText().isEmpty()) {
			if (playerOnesTurn) {
				playerOnesTurn(btnTwo);
			} else if (playerTwosTurn) {
				playerTwosTurn(btnTwo);
			}
		} else if (ae.getSource() == btnThree && btnThree.getText().isEmpty()) {
			if (playerOnesTurn) {
				playerOnesTurn(btnThree);
			} else if (playerTwosTurn) {
				playerTwosTurn(btnThree);
			}
		} else if (ae.getSource() == btnFour && btnFour.getText().isEmpty()) {
			if (playerOnesTurn) {
				playerOnesTurn(btnFour);
			} else if (playerTwosTurn) {
				playerTwosTurn(btnFour);
			}
		} else if (ae.getSource() == btnFive && btnFive.getText().isEmpty()) {
			if (playerOnesTurn) {
				playerOnesTurn(btnFive);
			} else if (playerTwosTurn) {
				playerTwosTurn(btnFive);
			}
		} else if (ae.getSource() == btnSix && btnSix.getText().isEmpty()) {
			if (playerOnesTurn) {
				playerOnesTurn(btnSix);
			} else if (playerTwosTurn) {
				playerTwosTurn(btnSix);
			}
		} else if (ae.getSource() == btnSeven && btnSeven.getText().isEmpty()) {
			if (playerOnesTurn) {
				playerOnesTurn(btnSeven);
			} else if (playerTwosTurn) {
				playerTwosTurn(btnSeven);
			}
		} else if (ae.getSource() == btnEight && btnEight.getText().isEmpty()) {
			if (playerOnesTurn) {
				playerOnesTurn(btnEight);
			} else if (playerTwosTurn) {
				playerTwosTurn(btnEight);
			}
		} else if (ae.getSource() == btnNine && btnNine.getText().isEmpty()) {
			if (playerOnesTurn) {
				playerOnesTurn(btnNine);
			} else if (playerTwosTurn) {
				playerTwosTurn(btnNine);
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
				logger.info(P1WINS);
				new Winner(playerOne);
			} else if (btnOne.getText().equals("O") && btnTwo.getText().equals("O") && btnThree.getText().equals("O")) {
				logger.info(P2WINS);
				new Winner(playerTwo);
			}
		}
		if (!btnFour.getText().isEmpty() && !btnFive.getText().isEmpty() && !btnSix.getText().isEmpty()) {
			if (btnFour.getText().equals("X") && btnFive.getText().equals("X") && btnSix.getText().equals("X")) {
				logger.info(P1WINS);
				new Winner(playerOne);
			} else if (btnFour.getText().equals("O") && btnFive.getText().equals("O") && btnSix.getText().equals("O")) {
				logger.info(P2WINS);
				new Winner(playerTwo);
			}
		}
		if (!btnSeven.getText().isEmpty() && !btnEight.getText().isEmpty() && !btnNine.getText().isEmpty()) {
			if (btnSeven.getText().equals("X") && btnEight.getText().equals("X") && btnNine.getText().equals("X")) {
				logger.info(P1WINS);
				new Winner(playerOne);
			} else if (btnSeven.getText().equals("O") && btnEight.getText().equals("O")
					&& btnNine.getText().equals("O")) {
				logger.info(P2WINS);
				new Winner(playerTwo);
			}
		}
		if (!btnOne.getText().isEmpty() && !btnFour.getText().isEmpty() && !btnSeven.getText().isEmpty()) {
			if (btnOne.getText().equals("X") && btnFour.getText().equals("X") && btnSeven.getText().equals("X")) {
				logger.info(P1WINS);
				new Winner(playerOne);
			} else if (btnOne.getText().equals("O") && btnFour.getText().equals("O")
					&& btnSeven.getText().equals("O")) {
				logger.info(P2WINS);
				new Winner(playerTwo);
			}
		}
		if (!btnTwo.getText().isEmpty() && !btnFive.getText().isEmpty() && !btnEight.getText().isEmpty()) {
			if (btnTwo.getText().equals("X") && btnFive.getText().equals("X") && btnEight.getText().equals("X")) {
				logger.info(P1WINS);
				new Winner(playerOne);
			} else if (btnTwo.getText().equals("O") && btnFive.getText().equals("O")
					&& btnEight.getText().equals("O")) {
				logger.info(P2WINS);
				new Winner(playerTwo);
			}
		}
		if (!btnThree.getText().isEmpty() && !btnSix.getText().isEmpty() && !btnNine.getText().isEmpty()) {
			if (btnThree.getText().equals("X") && btnSix.getText().equals("X") && btnNine.getText().equals("X")) {
				logger.info(P1WINS);
				new Winner(playerOne);
			} else if (btnThree.getText().equals("O") && btnSix.getText().equals("O")
					&& btnNine.getText().equals("O")) {
				logger.info(P2WINS);
				new Winner(playerTwo);
			}
		}
		if (!btnOne.getText().isEmpty() && !btnFive.getText().isEmpty() && !btnNine.getText().isEmpty()) {
			if (btnOne.getText().equals("X") && btnFive.getText().equals("X") && btnNine.getText().equals("X")) {
				logger.info(P1WINS);
				new Winner(playerOne);
			} else if (btnOne.getText().equals("O") && btnFive.getText().equals("O") && btnNine.getText().equals("O")) {
				logger.info(P2WINS);
				new Winner(playerTwo);
			}
		}
		if (!btnThree.getText().isEmpty() && !btnFive.getText().isEmpty() && !btnSeven.getText().isEmpty()) {
			if (btnThree.getText().equals("X") && btnFive.getText().equals("X") && btnSeven.getText().equals("X")) {
				logger.info(P1WINS);
				new Winner(playerOne);
			} else if (btnThree.getText().equals("O") && btnFive.getText().equals("O")
					&& btnSeven.getText().equals("O")) {
				logger.info(P2WINS);
				new Winner(playerTwo);
			}
		}
	}

	/**
	 * Performs actions after player one's turn
	 * @param button button that was just pressed by player one
	 */
	public void playerOnesTurn(JButton button) {
		button.setText(PLAYER_ONE_SHAPE);
		LABEL_PLAYER_TURN.setText(playerOne + "'s turn:");
		playerOnesTurn = !playerOnesTurn;
		playerTwosTurn = !playerTwosTurn;
	}

	/**
	 * Performs actions after player two's turn
	 * @param button button that was just pressed by player two
	 */
	public void playerTwosTurn(JButton button) {
		button.setText(PLAYER_TWO_SHAPE);
		LABEL_PLAYER_TURN.setText(playerTwo + "'s turn:");
		playerOnesTurn = !playerOnesTurn;
		playerTwosTurn = !playerTwosTurn;
	}
}
