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
import javax.swing.WindowConstants;

/**
 * Implementation for winner window. When a player wins, this window is displayed. <br>
 * 
 * @author Brian Perel
 */
public class Winner extends KeyAdapter implements ActionListener {

	private String gameResult;
	private JFrame f2 = new JFrame("Tic Tac Toe");
	protected static final String PLAYER = "Player";
	protected static final String COMPUTER = "Computer";
	private static final String GAME_OVER = "Game Over! It's a draw!";
	private JButton btnPlayAgain = new JButton("Play again");
	private JButton btnQuit = new JButton("Quit");
	private static final Color LIGHT_GREEN = new Color(144, 238, 144);

	/**
	 * Builds GUI window to be displayed when a player wins
	 * @param argGameResult holds the result of the game - winner's name or game over message
	 */
	public Winner(String argGameResult) {
		gameResult = argGameResult;
		
		JLabel lblGameResult = new JLabel();
		
		// if exit button is clicked, dispose of this frame 
		// and create a new GameBoard frame
		f2.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f2.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent e) {
				PvPGameBoard.f.dispose();
				new PvPGameBoard(false, false, true);
		    }
		});
		
		lblGameResult.setFont(new Font("Bookman Old Style", Font.PLAIN, 15));
		
		f2.setResizable(false);
		f2.setBounds(100, 100, 315, 167);
		f2.getContentPane().setLayout(null);
		f2.setLocationRelativeTo(null);
		f2.setVisible(true);

		f2.setContentPane(new JLabel(new ImageIcon("res/graphics/bg-image-tac.jpg")));
		
		// 2 '!' at the end of the string indicates the result comes from tictactoe v2 (player vs. computer), 1 '!' at the end of the string indicates result is from player vs. player
		String message = argGameResult.equals(GAME_OVER)
				|| argGameResult.equals(GAME_OVER + "!") ? argGameResult : (argGameResult + " wins!");
		lblGameResult.setText(message);
		lblGameResult.setHorizontalAlignment(SwingConstants.CENTER);
		lblGameResult.setBounds(0, 0, 310, 57);
		f2.getContentPane().add(lblGameResult);
		
		Font customFont = new Font("Lucida Fax", Font.BOLD, 12);
		
		btnPlayAgain.setFont(customFont);
		btnPlayAgain.setBounds(39, 68, 100, 34);
		btnPlayAgain.setBackground(LIGHT_GREEN);
		f2.getContentPane().add(btnPlayAgain);
		btnPlayAgain.addActionListener(this);
		btnPlayAgain.addKeyListener(this);
		
		btnQuit.setFont(customFont);
		btnQuit.setBackground(LIGHT_GREEN);
		f2.getContentPane().add(btnQuit);
		btnQuit.setBounds(169, 68, 100, 34);
		btnQuit.addActionListener(this);
		btnQuit.addKeyListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {	
		f2.dispose();
		PvPGameBoard.f.dispose();
		
		if (e.getSource() == btnPlayAgain) {
			if (gameResult.equals(PLAYER) || gameResult.equals(COMPUTER) || gameResult.equals(GAME_OVER + "!")) {
				new PvEGameBoard(false, false, true).toRun = true;
			}
			else {
				new PvPGameBoard(false, false, true);
			}
		} 
		else if (e.getSource() == btnQuit && gameResult.equals(PLAYER) || gameResult.equals(COMPUTER) 
				|| gameResult.equals("Game Over! It's a draw!!")) {
			
			System.exit(0);		
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		f2.dispose();
		PvPGameBoard.f.dispose();

		if (e.getKeyChar() == KeyEvent.VK_ENTER) {
			if (e.getSource() == btnPlayAgain) {
				if (gameResult.equals(PLAYER) || gameResult.equals(COMPUTER) || gameResult.equals(GAME_OVER + "!")) {
					new PvEGameBoard(false, false, true).toRun = true;				
				}
				else {
					new PvPGameBoard(false, false, true);
				}
			} 
			else if (e.getSource() == btnQuit && gameResult.equals(PLAYER) 
					|| gameResult.equals(COMPUTER) || gameResult.equals(GAME_OVER + "!")) {
				
				System.exit(0);
			}
		}
	}
}
