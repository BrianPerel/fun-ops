package com.tictactoe;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

/**
 * Implementation for winner window. When a player wins, this window is displayed. <br>
 *
 * @author Brian Perel
 */
public class MatchOver extends KeyAdapter implements ActionListener {

	private static final String GAME_OVER_DRAW_MSG = "Game Over! It's a draw!";
	private static final Color LIGHT_GREEN_COLOR = new Color(144, 238, 144);

	private final JFrame window = new JFrame("Tic Tac Toe");
	private final JButton btnQuit;
	private final String gameResult;
	private final JButton btnPlayAgain;
	private TicTacToe ticTacToeGame;

	/**
	 * Builds GUI window to be displayed when a player wins
	 * @param argMatchResult holds the result of the game - winner's name or game over message
	 * @param argTicTacToeGame holds the games properties like player names and shapes
	 */
	public MatchOver(String argMatchResult, TicTacToe argTicTacToeGame) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

		ticTacToeGame = argTicTacToeGame;

		gameResult = argMatchResult;

		argTicTacToeGame.isGameOver = true; // prevents code that switches gameboard name label from running

		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		// changes the program's taskbar icon
	    window.setIconImage(new ImageIcon("res/graphics/taskbar_icons/tic-tac-toe.png").getImage());

		// if we close the game's winner window frame then close the other main game frame too
		window.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent e) {
				new PvPGameBoard(false, false, ticTacToeGame);
		    }
		});

		window.setResizable(false);
		window.setSize(315, 167);
		window.getContentPane().setLayout(null);

		window.setContentPane(new JLabel(new ImageIcon("res/graphics/bg-image-tac.jpg")));

		// 2 '!' at the end of the string indicates the result comes from tic-tac-toe v2 (player vs. computer)
		// 1 '!' at the end of the string indicates result is from player vs. player
		JLabel lblGameResult = new JLabel(GAME_OVER_DRAW_MSG.equals(argMatchResult)
				|| (GAME_OVER_DRAW_MSG + "!").equals(argMatchResult) ? argMatchResult : (argMatchResult + " wins!"));

		lblGameResult.setFont(new Font("Bookman Old Style", Font.PLAIN, 15));
		lblGameResult.setHorizontalAlignment(SwingConstants.CENTER);
		lblGameResult.setBounds(0, 0, 310, 57);
		window.getContentPane().add(lblGameResult);

		Font customFont = new Font("Lucida Fax", Font.BOLD, 12);

		btnPlayAgain = new JButton("Play again");
		btnPlayAgain.setFont(customFont);
		btnPlayAgain.setBounds(39, 68, 100, 34);
		btnPlayAgain.setBackground(LIGHT_GREEN_COLOR);
		window.getContentPane().add(btnPlayAgain);
		btnPlayAgain.addActionListener(this);
		btnPlayAgain.addKeyListener(this);
		btnPlayAgain.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		StartMenu.setBtnHoverColor(btnPlayAgain);

		btnQuit = new JButton("Quit");
		btnQuit.setFont(customFont);
		btnQuit.setBackground(LIGHT_GREEN_COLOR);
		window.getContentPane().add(btnQuit);
		btnQuit.setBounds(169, 68, 100, 34);
		btnQuit.addActionListener(this);
		btnQuit.addKeyListener(this);
		btnQuit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		StartMenu.setBtnHoverColor(btnQuit);

		window.setLocation(PvPGameBoard.window.getX() + PvPGameBoard.window.getWidth(), PvPGameBoard.window.getY());
		window.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		matchOverOptions(e.getSource());
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_ENTER) {
			matchOverOptions(e.getSource());
		}
	}

	/**
	 * Determines and makes the necessary action associated to the button that was pressed when the game ends (play again or quit)
	 * @param source the object on which the Event initially occurred
	 */
	private void matchOverOptions(Object source) {
		window.dispose();

		if (source.equals(btnPlayAgain)) {
			playAgain();
		}
		else {
			quit(source);
		}
	}

	private void playAgain() {
		// launches appropriate game mode when play again btn is pressed
		if (ticTacToeGame.PLAYER.equals(gameResult) || ticTacToeGame.COMPUTER.equals(gameResult) || (GAME_OVER_DRAW_MSG + "!").equals(gameResult)) {
			new CvPGameBoard(false, false, PvPGameBoard.window.getX() + "," + PvPGameBoard.window.getY(), ticTacToeGame);
		}
		else {
			new PvPGameBoard(false, false, PvPGameBoard.window.getX() + "," + PvPGameBoard.window.getY(), ticTacToeGame);
		}
	}

	private void quit(Object source) {
		if (source.equals(btnQuit) || (ticTacToeGame.PLAYER.equals(gameResult)
				|| ticTacToeGame.COMPUTER.equals(gameResult) || (GAME_OVER_DRAW_MSG + "!").equals(gameResult))) {

			System.exit(0); // perform program termination with a normal exit status
		}
	}

}
