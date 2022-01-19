package tictactoe;

import java.awt.Color;
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

import tictactoe.playercomputer.GameBoardTwo;

/**
 * Implementation for winner window. When a player wins, this window is displayed. <br>
 * 
 * @author Brian Perel
 */
public class Winner extends KeyAdapter implements ActionListener {

	private String gameResult;
	private JFrame f2 = new JFrame("Tic Tac Toe");
	private JButton btnPlayAgain = new JButton("Play again"), btnQuit = new JButton("Quit");

	/**
	 * Builds GUI window to be displayed when a player wins
	 * @param argGameResult holds the result of the game - winner's name or game over message
	 */
	public Winner(String argGameResult) {
		gameResult = argGameResult;
		
		JLabel lblGameResult = new JLabel();
		
		// if exit button is clicked, dispose of this frame 
		// and create a new GameBoard frame
		f2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f2.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent e) {
				GameBoard.f.dispose();
				new GameBoard(false, false, true);
		    }
		});
		
		lblGameResult.setFont(new Font("Bookman Old Style", Font.PLAIN, 15));
		
		f2.setResizable(false);
		f2.setBounds(100, 100, 315, 167);
		f2.getContentPane().setLayout(null);
		f2.setLocationRelativeTo(null);
		f2.setVisible(true);

		f2.setContentPane(new JLabel(new ImageIcon("res/graphics/bg-image-tac.jpg")));
		
		// 2 '!' indicates the result comes from tictactoe v2 (player vs. computer), 1 '!' indicates result is from player vs. player
		String message = argGameResult.equals("Game Over! It's a draw!")
				|| argGameResult.equals("Game Over! It's a draw!!") ? argGameResult : (argGameResult + " wins!");
		lblGameResult.setText(message);
		lblGameResult.setHorizontalAlignment(SwingConstants.CENTER);
		lblGameResult.setBounds(0, 0, 310, 57);
		f2.getContentPane().add(lblGameResult);
		
		btnPlayAgain.setFont(new Font("Lucida Fax", Font.BOLD, 12));
		btnPlayAgain.setBounds(39, 68, 100, 34);
		btnPlayAgain.setBackground(new Color(144, 238, 144));
		f2.getContentPane().add(btnPlayAgain);
		btnPlayAgain.addActionListener(this);
		btnPlayAgain.addKeyListener(this);
		
		btnQuit.setFont(new Font("Lucida Fax", Font.BOLD, 12));
		btnQuit.setBackground(new Color(144, 238, 144));
		f2.getContentPane().add(btnQuit);
		btnQuit.setBounds(169, 68, 100, 34);
		btnQuit.addActionListener(this);
		btnQuit.addKeyListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {	
		f2.dispose();
		
		if (e.getSource() == btnPlayAgain) {
			if(gameResult.equals("player") || gameResult.equals("computer") || gameResult.equals("Game Over! It's a draw!!")) {
				GameBoardTwo.toRun = true;
				GameBoardTwo.f.dispose();
				new GameBoardTwo(false, false, true);
			}
			else {
				GameBoard.f.dispose();
				new GameBoard(false, false, true);
			}
		} 
		else if (e.getSource() == btnQuit) {
			if(gameResult.equals("player") || gameResult.equals("computer") || gameResult.equals("Game Over! It's a draw!!")) {
				GameBoardTwo.f.dispose();
			} 
			else {
				GameBoard.f.dispose();
			}			
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		f2.dispose();

		if (e.getSource() == btnPlayAgain && e.getKeyChar() == KeyEvent.VK_ENTER) {
			if(gameResult.equals("player") || gameResult.equals("computer") || gameResult.equals("Game Over! It's a draw!!")) {
				GameBoardTwo.f.dispose();
				new GameBoardTwo(false, false, true);
			}
			else {
				GameBoard.f.dispose();
				new GameBoard(false, false, true);
			}
		} 
		else if(e.getSource() == btnQuit && e.getKeyChar() == KeyEvent.VK_ENTER) {
			if(gameResult.equals("player") || gameResult.equals("computer") || gameResult.equals("Game Over! It's a draw!!")) {
				GameBoardTwo.f.dispose();
			}
			else {
				GameBoard.f.dispose();
			}
		}
	}
}
