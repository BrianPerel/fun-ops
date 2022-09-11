package com.tictactoe;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

/**
 * Implementation for winner window. When a player wins, this window is displayed. <br>
 * 
 * @author Brian Perel
 */
public class GameWinner extends KeyAdapter implements ActionListener {

	private static final String GAME_OVER_MSG = "Game Over! It's a draw!";
	private static final Color LIGHT_GREEN = new Color(144, 238, 144);
	
	private JFrame frame2;
	private JButton btnQuit;
	private String gameResult;
	private JButton btnPlayAgain;

	/**
	 * Builds GUI window to be displayed when a player wins
	 * @param argGameResult holds the result of the game - winner's name or game over message
	 */
	public GameWinner(String argGameResult) {
		gameResult = argGameResult;
		
		PvPGameBoard.isGameOver = true; // prevents code that switches gameboard name label from running
		
		// if exit button is clicked, dispose of this frame 
		// and create a new GameBoard frame
		frame2 = new JFrame("Tic Tac Toe");
		frame2.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame2.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent e) {
				PvPGameBoard.f.dispose();
				new PvPGameBoard(false, false, true);
		    }
		});
		
		frame2.setResizable(false);
		frame2.setSize(315, 167);
		frame2.getContentPane().setLayout(null);

		frame2.setContentPane(new JLabel(new ImageIcon("res/graphics/bg-image-tac.jpg")));
		
		// 2 '!' at the end of the string indicates the result comes from tic-tac-toe v2 (player vs. computer), 1 '!' at the end of the string indicates result is from player vs. player
		JLabel lblGameResult = new JLabel(argGameResult.equals(GAME_OVER_MSG)
				|| argGameResult.equals(GAME_OVER_MSG + "!") ? argGameResult : (argGameResult + " wins!"));
		
		lblGameResult.setFont(new Font("Bookman Old Style", Font.PLAIN, 15));
		lblGameResult.setHorizontalAlignment(SwingConstants.CENTER);
		lblGameResult.setBounds(0, 0, 310, 57);
		frame2.getContentPane().add(lblGameResult);
		
		Font customFont = new Font("Lucida Fax", Font.BOLD, 12);
		
		btnPlayAgain = new JButton("Play again");
		btnPlayAgain.setFont(customFont);
		btnPlayAgain.setBounds(39, 68, 100, 34);
		btnPlayAgain.setBackground(LIGHT_GREEN);
		frame2.getContentPane().add(btnPlayAgain);
		btnPlayAgain.addActionListener(this);
		btnPlayAgain.addKeyListener(this);
		btnPlayAgain.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		btnQuit = new JButton("Quit");
		btnQuit.setFont(customFont);
		btnQuit.setBackground(LIGHT_GREEN);
		frame2.getContentPane().add(btnQuit);
		btnQuit.setBounds(169, 68, 100, 34);
		btnQuit.addActionListener(this);
		btnQuit.addKeyListener(this);
		btnQuit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		frame2.setLocation(StartMenu.frame.getX() + StartMenu.frame.getWidth(), StartMenu.frame.getY());
		frame2.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {	
		determineEndGameAction(e.getSource());
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_ENTER) {
			determineEndGameAction(e.getSource());
		}
	}

	public void determineEndGameAction(Object source) {
		frame2.dispose();
		
		if (source == btnPlayAgain) {
			if (gameResult.equals(StartMenu.PLAYER) || gameResult.equals(StartMenu.COMPUTER) || gameResult.equals(GAME_OVER_MSG + "!")) {
				new CvPGameBoard(false, false, true);				
			}
			else {
				PvPGameBoard.f.dispose();
				new PvPGameBoard(false, false, true);
			}
		} 
		else if (source == btnQuit || (gameResult.equals(StartMenu.PLAYER)
				|| gameResult.equals(StartMenu.COMPUTER) || gameResult.equals(GAME_OVER_MSG + "!"))) {
			
			System.exit(0);
		}
	}
}
