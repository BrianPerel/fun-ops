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

	private JFrame f2;
	private JButton btnQuit;
	private String gameResult;
	private JButton btnPlayAgain;
	private static final String GAME_OVER = "Game Over! It's a draw!";
	private static final Color LIGHT_GREEN = new Color(144, 238, 144);

	/**
	 * Builds GUI window to be displayed when a player wins
	 * @param argGameResult holds the result of the game - winner's name or game over message
	 */
	public GameWinner(String argGameResult) {
		gameResult = argGameResult;
		
		// if exit button is clicked, dispose of this frame 
		// and create a new GameBoard frame
		f2 = new JFrame("Tic Tac Toe");
		f2.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f2.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent e) {
				PvPGameBoard.f.dispose();
				new PvPGameBoard(false, false, true);
		    }
		});
		
		f2.setResizable(false);
		f2.setSize(315, 167);
		f2.getContentPane().setLayout(null);

		f2.setContentPane(new JLabel(new ImageIcon("res/graphics/bg-image-tac.jpg")));
		
		// 2 '!' at the end of the string indicates the result comes from tictactoe v2 (player vs. computer), 1 '!' at the end of the string indicates result is from player vs. player
		JLabel lblGameResult = new JLabel(argGameResult.equals(GAME_OVER)
				|| argGameResult.equals(GAME_OVER + "!") ? argGameResult : (argGameResult + " wins!"));
		lblGameResult.setFont(new Font("Bookman Old Style", Font.PLAIN, 15));
		lblGameResult.setHorizontalAlignment(SwingConstants.CENTER);
		lblGameResult.setBounds(0, 0, 310, 57);
		f2.getContentPane().add(lblGameResult);
		
		Font customFont = new Font("Lucida Fax", Font.BOLD, 12);
		
		btnPlayAgain = new JButton("Play again");
		btnPlayAgain.setFont(customFont);
		btnPlayAgain.setBounds(39, 68, 100, 34);
		btnPlayAgain.setBackground(LIGHT_GREEN);
		f2.getContentPane().add(btnPlayAgain);
		btnPlayAgain.addActionListener(this);
		btnPlayAgain.addKeyListener(this);
		btnPlayAgain.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		btnQuit = new JButton("Quit");
		btnQuit.setFont(customFont);
		btnQuit.setBackground(LIGHT_GREEN);
		f2.getContentPane().add(btnQuit);
		btnQuit.setBounds(169, 68, 100, 34);
		btnQuit.addActionListener(this);
		btnQuit.addKeyListener(this);
		btnQuit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		f2.setLocationRelativeTo(null);
		f2.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {	
		f2.dispose();
		determineEndGameAction(e.getSource());
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_ENTER) {
			f2.dispose();
			determineEndGameAction(e.getSource());
		}
	}

	public void determineEndGameAction(Object source) {
		if (source == btnPlayAgain) {
			if (gameResult.equals(StartMenu.PLAYER) || gameResult.equals(StartMenu.COMPUTER) || gameResult.equals(GAME_OVER + "!")) {
				new CvPGameBoard(false, false, true).shouldRun = true;				
			}
			else {
				PvPGameBoard.f.dispose();
				new PvPGameBoard(false, false, true);
			}
		} 
		else if (source == btnQuit || (gameResult.equals(StartMenu.PLAYER)
				|| gameResult.equals(StartMenu.COMPUTER) || gameResult.equals(GAME_OVER + "!"))) {
			
			System.exit(0);
		}
	}
}
