package tic.tac.toe;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

/**
 * 
 * @author Brian Perel
 *
 *         Implementation for tic tac toe game board. Initiates the game.
 */
public class GameBoard implements ActionListener {

	private JFrame f = new JFrame("Tic Tac Toe");
	JButton btnOne = new JButton("");
	JButton btnTwo = new JButton("");
	JButton btnThree = new JButton("");
	JButton btnFour = new JButton("");
	JButton btnFive = new JButton("");
	JButton btnSix = new JButton("");
	JButton btnSeven = new JButton("");
	JButton btnEight = new JButton("");
	JButton btnNine = new JButton("");
	static String playerOne;
	static String playerTwo;
	boolean playerOnesTurn;
	boolean playerTwosTurn;
	private final JLabel LABEL_PLAYER_TURN = new JLabel(playerOne + "'s turn:");
	// private static final Logger logger_ = Logger.getLogger(TicTacToeBoard.class);
	final String P1WINS = "Player 1 wins!";
	final String P2WINS = "Player 2 wins!";

	// needed to invert these to fix a window2 symbol problem
	static final String PLAYER_ONE_SHAPE = "O";
	static final String PLAYER_TWO_SHAPE = "X";
	static boolean start;

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
		
		ImageIcon image = new ImageIcon(getClass().getResource("bg.jpg"));
		JLabel backgroundLabel = new JLabel(image);
		f.setContentPane(backgroundLabel);

		btnOne.setBounds(63, 64, 80, 70);
		f.getContentPane().add(btnOne);
		btnOne.addActionListener(this);
		btnOne.setBackground(new Color(244, 164, 96));

		btnTwo.setBounds(63, 145, 80, 70);
		f.getContentPane().add(btnTwo);
		btnTwo.addActionListener(this);
		btnTwo.setBackground(new Color(244, 164, 96));

		btnThree.setBounds(63, 226, 80, 70);
		f.getContentPane().add(btnThree);
		btnThree.addActionListener(this);
		btnThree.setBackground(new Color(244, 164, 96));

		btnFour.setBounds(153, 64, 80, 70);
		f.getContentPane().add(btnFour);
		btnFour.addActionListener(this);
		btnFour.setBackground(new Color(244, 164, 96));

		btnFive.setBounds(153, 145, 80, 70);
		f.getContentPane().add(btnFive);
		btnFive.addActionListener(this);
		btnFive.setBackground(new Color(244, 164, 96));

		btnSix.setBounds(153, 226, 80, 70);
		f.getContentPane().add(btnSix);
		btnSix.addActionListener(this);
		btnSix.setBackground(new Color(244, 164, 96));

		btnSeven.setBounds(243, 64, 80, 70);
		f.getContentPane().add(btnSeven);
		btnSeven.addActionListener(this);
		btnSeven.setBackground(new Color(244, 164, 96));

		btnEight.setBounds(243, 145, 80, 70);
		f.getContentPane().add(btnEight);
		btnEight.addActionListener(this);
		btnEight.setBackground(new Color(244, 164, 96));

		btnNine.setBounds(243, 226, 80, 70);
		f.getContentPane().add(btnNine);
		btnNine.addActionListener(this);
		btnNine.setBackground(new Color(244, 164, 96));

		LABEL_PLAYER_TURN.setBounds(63, 15, 260, 38);
		f.getContentPane().add(LABEL_PLAYER_TURN);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(63, 138, 260, 11);
		separator_1.setBackground(Color.blue);
		f.getContentPane().add(separator_1);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(63, 221, 260, 11);
		separator_2.setBackground(Color.blue);
		f.getContentPane().add(separator_2);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setOrientation(SwingConstants.VERTICAL);
		separator_3.setBounds(148, 64, 7, 232);
		separator_3.setBackground(Color.blue);
		f.getContentPane().add(separator_3);
		
		JSeparator separator_4 = new JSeparator();
		separator_4.setOrientation(SwingConstants.VERTICAL);
		separator_4.setBounds(237, 64, 7, 232);
		separator_4.setBackground(Color.blue);
		f.getContentPane().add(separator_4);
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
			// logger_.warn("Invalid Move!");
		}

		// game rules: Need 3 in a row in any direction
		if (!btnOne.getText().isEmpty() && !btnTwo.getText().isEmpty() && !btnThree.getText().isEmpty()
				&& !btnFour.getText().isEmpty() && !btnFive.getText().isEmpty() && !btnSix.getText().isEmpty()
				&& !btnSeven.getText().isEmpty() && !btnEight.getText().isEmpty() && !btnNine.getText().isEmpty()) {
			f.dispose();
			new Winner("Game Over! Tie");
		}
		if (!btnOne.getText().isEmpty() && !btnTwo.getText().isEmpty() && !btnThree.getText().isEmpty()) {
			if (btnOne.getText().equals("X") && btnTwo.getText().equals("X") && btnThree.getText().equals("X")) {
				// logger_.info(P1WINS);
				f.dispose();
				new Winner(playerOne);
			} else if (btnOne.getText().equals("O") && btnTwo.getText().equals("O") && btnThree.getText().equals("O")) {
				// logger_.info(P2WINS);
				f.dispose();
				new Winner(playerTwo);
			}
		}
		if (!btnFour.getText().isEmpty() && !btnFive.getText().isEmpty() && !btnSix.getText().isEmpty()) {
			if (btnFour.getText().equals("X") && btnFive.getText().equals("X") && btnSix.getText().equals("X")) {
				// logger_.info(P1WINS);
				f.dispose();
				new Winner(playerOne);
			} else if (btnFour.getText().equals("O") && btnFive.getText().equals("O") && btnSix.getText().equals("O")) {
				// logger_.info(P2WINS);
				f.dispose();
				new Winner(playerTwo);
			}
		}
		if (!btnSeven.getText().isEmpty() && !btnEight.getText().isEmpty() && !btnNine.getText().isEmpty()) {
			if (btnSeven.getText().equals("X") && btnEight.getText().equals("X") && btnNine.getText().equals("X")) {
				// logger_.info(P1WINS);
				f.dispose();
				new Winner(playerOne);
			} else if (btnSeven.getText().equals("O") && btnEight.getText().equals("O")
					&& btnNine.getText().equals("O")) {
				// logger_.info(P2WINS);
				f.dispose();
				new Winner(playerTwo);
			}
		}
		if (!btnOne.getText().isEmpty() && !btnFour.getText().isEmpty() && !btnSeven.getText().isEmpty()) {
			if (btnOne.getText().equals("X") && btnFour.getText().equals("X") && btnSeven.getText().equals("X")) {
				// logger_.info(P1WINS);
				f.dispose();
				new Winner(playerOne);
			} else if (btnOne.getText().equals("O") && btnFour.getText().equals("O")
					&& btnSeven.getText().equals("O")) {
				// logger_.info(P2WINS);
				f.dispose();
				new Winner(playerTwo);
			}
		}
		if (!btnTwo.getText().isEmpty() && !btnFive.getText().isEmpty() && !btnEight.getText().isEmpty()) {
			if (btnTwo.getText().equals("X") && btnFive.getText().equals("X") && btnEight.getText().equals("X")) {
				// logger_.info(P1WINS);
				f.dispose();
				new Winner(playerOne);
			} else if (btnTwo.getText().equals("O") && btnFive.getText().equals("O")
					&& btnEight.getText().equals("O")) {
				// logger_.info(P2WINS);
				f.dispose();
				new Winner(playerTwo);
			}
		}
		if (!btnThree.getText().isEmpty() && !btnSix.getText().isEmpty() && !btnNine.getText().isEmpty()) {
			if (btnThree.getText().equals("X") && btnSix.getText().equals("X") && btnNine.getText().equals("X")) {
				// logger_.info(P1WINS);
				f.dispose();
				new Winner(playerOne);
			} else if (btnThree.getText().equals("O") && btnSix.getText().equals("O")
					&& btnNine.getText().equals("O")) {
				// logger_.info(P2WINS);
				f.dispose();
				new Winner(playerTwo);
			}
		}
		if (!btnOne.getText().isEmpty() && !btnFive.getText().isEmpty() && !btnNine.getText().isEmpty()) {
			if (btnOne.getText().equals("X") && btnFive.getText().equals("X") && btnNine.getText().equals("X")) {
				// logger_.info(P1WINS);
				f.dispose();
				new Winner(playerOne);
			} else if (btnOne.getText().equals("O") && btnFive.getText().equals("O") && btnNine.getText().equals("O")) {
				// logger_.info(P2WINS);
				f.dispose();
				new Winner(playerTwo);
			}
		}
		if (!btnThree.getText().isEmpty() && !btnFive.getText().isEmpty() && !btnSeven.getText().isEmpty()) {
			if (btnThree.getText().equals("X") && btnFive.getText().equals("X") && btnSeven.getText().equals("X")) {
				// logger_.info(P1WINS);
				f.dispose();
				new Winner(playerOne);
			} else if (btnThree.getText().equals("O") && btnFive.getText().equals("O")
					&& btnSeven.getText().equals("O")) {
				// logger_.info(P2WINS);
				f.dispose();
				new Winner(playerTwo);
			}
		}
	}

	public void playerOnesTurn(JButton button_) {
		button_.setText(PLAYER_ONE_SHAPE);
		LABEL_PLAYER_TURN.setText(playerOne + "'s turn:");
		playerOnesTurn = !playerOnesTurn;
		playerTwosTurn = !playerTwosTurn;
	}

	public void playerTwosTurn(JButton button_) {
		button_.setText(PLAYER_TWO_SHAPE);
		LABEL_PLAYER_TURN.setText(playerTwo + "'s turn:");
		playerOnesTurn = !playerOnesTurn;
		playerTwosTurn = !playerTwosTurn;
	}
}
