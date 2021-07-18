package tic.tac.toe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * 
 * @author Brian Perel
 *
 *         Implementation for tic tac toe game board. Initiates the game.
 */
public class GameBoard implements ActionListener {

	private JFrame f = new JFrame("Tic Tac Toe");
	JButton button_1 = new JButton("");
	JButton button_2 = new JButton("");
	JButton button_3 = new JButton("");
	JButton button_4 = new JButton("");
	JButton button_5 = new JButton("");
	JButton button_6 = new JButton("");
	JButton button_7 = new JButton("");
	JButton button_8 = new JButton("");
	JButton button_9 = new JButton("");
	static String playerOne;
	static String playerTwo;
	boolean playerOnesTurn = true;
	boolean playerTwosTurn;
	private final JLabel LABEL_PLAYER_TURN = new JLabel(playerOne + "'s turn:");
//	private static final Logger logger_ = Logger.getLogger(TicTacToeBoard.class);
	final String P1WINS = "Player 1 wins!";
	final String P2WINS = "Player 2 wins!";

	// needed to invert these to fix a window2 symbol problem
	static final String P_ONE_SHAPE = "O";
	static final String P_TWO_SHAPE = "X";
	static boolean start = true;

	public GameBoard() {

		f.setResizable(false);
		f.setBounds(100, 100, 399, 358);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().setLayout(null);
		f.setVisible(true);
		f.setLocationRelativeTo(null);

		button_1.setBounds(63, 64, 80, 70);
		f.getContentPane().add(button_1);
		button_1.addActionListener(this);

		button_2.setBounds(63, 145, 80, 70);
		f.getContentPane().add(button_2);
		button_2.addActionListener(this);

		button_3.setBounds(63, 226, 80, 70);
		f.getContentPane().add(button_3);
		button_3.addActionListener(this);

		button_4.setBounds(153, 64, 80, 70);
		f.getContentPane().add(button_4);
		button_4.addActionListener(this);

		button_5.setBounds(153, 145, 80, 70);
		f.getContentPane().add(button_5);
		button_5.addActionListener(this);

		button_6.setBounds(153, 226, 80, 70);
		f.getContentPane().add(button_6);
		button_6.addActionListener(this);

		button_7.setBounds(243, 64, 80, 70);
		f.getContentPane().add(button_7);
		button_7.addActionListener(this);

		button_8.setBounds(243, 145, 80, 70);
		f.getContentPane().add(button_8);
		button_8.addActionListener(this);

		button_9.setBounds(243, 226, 80, 70);
		f.getContentPane().add(button_9);
		button_9.addActionListener(this);

		LABEL_PLAYER_TURN.setBounds(63, 15, 260, 38);
		f.getContentPane().add(LABEL_PLAYER_TURN);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		if (start) {
			playerOnesTurn = !playerOnesTurn;
			playerTwosTurn = !playerTwosTurn;
			start = false;
		}

		if (ae.getSource() == button_1 && button_1.getText().isEmpty()) {
			if (playerOnesTurn) {
				playerOnesTurn(button_1);
			} else if (playerTwosTurn) {
				playerTwosTurn(button_1);
			}
		} else if (ae.getSource() == button_2 && button_2.getText().isEmpty()) {
			if (playerOnesTurn) {
				playerOnesTurn(button_2);
			} else if (playerTwosTurn) {
				playerTwosTurn(button_2);
			}
		} else if (ae.getSource() == button_3 && button_3.getText().isEmpty()) {
			if (playerOnesTurn) {
				playerOnesTurn(button_3);
			} else if (playerTwosTurn) {
				playerTwosTurn(button_3);
			}
		} else if (ae.getSource() == button_4 && button_4.getText().isEmpty()) {
			if (playerOnesTurn) {
				playerOnesTurn(button_4);
			} else if (playerTwosTurn) {
				playerTwosTurn(button_4);
			}
		} else if (ae.getSource() == button_5 && button_5.getText().isEmpty()) {
			if (playerOnesTurn) {
				playerOnesTurn(button_5);
			} else if (playerTwosTurn) {
				playerTwosTurn(button_5);
			}
		} else if (ae.getSource() == button_6 && button_6.getText().isEmpty()) {
			if (playerOnesTurn) {
				playerOnesTurn(button_6);
			} else if (playerTwosTurn) {
				playerTwosTurn(button_6);
			}
		} else if (ae.getSource() == button_7 && button_7.getText().isEmpty()) {
			if (playerOnesTurn) {
				playerOnesTurn(button_7);
			} else if (playerTwosTurn) {
				playerTwosTurn(button_7);
			}
		} else if (ae.getSource() == button_8 && button_8.getText().isEmpty()) {
			if (playerOnesTurn) {
				playerOnesTurn(button_8);
			} else if (playerTwosTurn) {
				playerTwosTurn(button_8);
			}
		} else if (ae.getSource() == button_9 && button_9.getText().isEmpty()) {
			if (playerOnesTurn) {
				playerOnesTurn(button_9);
			} else if (playerTwosTurn) {
				playerTwosTurn(button_9);
			}
		} else {
			// logger_.warn("Invalid Move!");
		}

		// game rules: Need 3 in a row in any direction
		if (!button_1.getText().isEmpty() && !button_2.getText().isEmpty() && !button_3.getText().isEmpty()
				&& !button_4.getText().isEmpty() && !button_5.getText().isEmpty() && !button_6.getText().isEmpty()
				&& !button_7.getText().isEmpty() && !button_8.getText().isEmpty() && !button_9.getText().isEmpty()) {
			f.dispose();
			new Winner("Game Over! Tie, no one wins");
		}
		if (!button_1.getText().isEmpty() && !button_2.getText().isEmpty() && !button_3.getText().isEmpty()) {
			if (button_1.getText().equals("X") && button_2.getText().equals("X") && button_3.getText().equals("X")) {
				// logger_.info(P1WINS);
				f.dispose();
				new Winner(playerOne);
			} else if (button_1.getText().equals("O") && button_2.getText().equals("O")
					&& button_3.getText().equals("O")) {
				// logger_.info(P2WINS);
				f.dispose();
				new Winner(playerTwo);
			}
		}
		if (!button_4.getText().isEmpty() && !button_5.getText().isEmpty() && !button_6.getText().isEmpty()) {
			if (button_4.getText().equals("X") && button_5.getText().equals("X") && button_6.getText().equals("X")) {
				// logger_.info(P1WINS);
				f.dispose();
				new Winner(playerOne);
			} else if (button_4.getText().equals("O") && button_5.getText().equals("O")
					&& button_6.getText().equals("O")) {
				// logger_.info(P2WINS);
				f.dispose();
				new Winner(playerTwo);
			}
		}
		if (!button_7.getText().isEmpty() && !button_8.getText().isEmpty() && !button_9.getText().isEmpty()) {
			if (button_7.getText().equals("X") && button_8.getText().equals("X") && button_9.getText().equals("X")) {
				// logger_.info(P1WINS);
				f.dispose();
				new Winner(playerOne);
			} else if (button_7.getText().equals("O") && button_8.getText().equals("O")
					&& button_9.getText().equals("O")) {
				// logger_.info(P2WINS);
				f.dispose();
				new Winner(playerTwo);
			}
		}
		if (!button_1.getText().isEmpty() && !button_4.getText().isEmpty() && !button_7.getText().isEmpty()) {
			if (button_1.getText().equals("X") && button_4.getText().equals("X") && button_7.getText().equals("X")) {
				// logger_.info(P1WINS);
				f.dispose();
				new Winner(playerOne);
			} else if (button_1.getText().equals("O") && button_4.getText().equals("O")
					&& button_7.getText().equals("O")) {
				// logger_.info(P2WINS);
				f.dispose();
				new Winner(playerTwo);
			}
		}
		if (!button_2.getText().isEmpty() && !button_5.getText().isEmpty() && !button_8.getText().isEmpty()) {
			if (button_2.getText().equals("X") && button_5.getText().equals("X") && button_8.getText().equals("X")) {
				// logger_.info(P1WINS);
				f.dispose();
				new Winner(playerOne);
			} else if (button_2.getText().equals("O") && button_5.getText().equals("O")
					&& button_8.getText().equals("O")) {
				// logger_.info(P2WINS);
				f.dispose();
				new Winner(playerTwo);
			}
		}
		if (!button_3.getText().isEmpty() && !button_6.getText().isEmpty() && !button_9.getText().isEmpty()) {
			if (button_3.getText().equals("X") && button_6.getText().equals("X") && button_9.getText().equals("X")) {
				// logger_.info(P1WINS);
				f.dispose();
				new Winner(playerOne);
			} else if (button_3.getText().equals("O") && button_6.getText().equals("O")
					&& button_9.getText().equals("O")) {
				// logger_.info(P2WINS);
				f.dispose();
				new Winner(playerTwo);
			}
		}
		if (!button_1.getText().isEmpty() && !button_5.getText().isEmpty() && !button_9.getText().isEmpty()) {
			if (button_1.getText().equals("X") && button_5.getText().equals("X") && button_9.getText().equals("X")) {
				// logger_.info(P1WINS);
				f.dispose();
				new Winner(playerOne);
			} else if (button_1.getText().equals("O") && button_5.getText().equals("O")
					&& button_9.getText().equals("O")) {
				// logger_.info(P2WINS);
				f.dispose();
				new Winner(playerTwo);
			}
		}
		if (!button_3.getText().isEmpty() && !button_5.getText().isEmpty() && !button_7.getText().isEmpty()) {
			if (button_3.getText().equals("X") && button_5.getText().equals("X") && button_7.getText().equals("X")) {
				// logger_.info(P1WINS);
				f.dispose();
				new Winner(playerOne);
			} else if (button_3.getText().equals("O") && button_5.getText().equals("O")
					&& button_7.getText().equals("O")) {
				// logger_.info(P2WINS);
				f.dispose();
				new Winner(playerTwo);
			}
		}
	}

	public void playerOnesTurn(JButton button_) {
		button_.setText(P_ONE_SHAPE);
		LABEL_PLAYER_TURN.setText(playerOne + "'s turn:");
		playerOnesTurn = !playerOnesTurn;
		playerTwosTurn = !playerTwosTurn;
	}

	public void playerTwosTurn(JButton button_) {
		button_.setText(P_TWO_SHAPE);
		LABEL_PLAYER_TURN.setText(playerTwo + "'s turn:");
		playerOnesTurn = !playerOnesTurn;
		playerTwosTurn = !playerTwosTurn;
	}
}
