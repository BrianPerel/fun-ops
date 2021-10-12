package tic.tac.toe;

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

/**
 * Implementation for winner window. When a player wins, this window is displayed. <br>
 * 
 * @author Brian Perel
 *       
 */
public class Winner extends KeyAdapter implements ActionListener {

	private JFrame f2 = new JFrame("Tic Tac Toe");
	JButton btnPlayAgain = new JButton("Play again");
	JButton btnQuit = new JButton("Quit");
	JLabel lblNewLabel = new JLabel();

	/**
	 * Builds GUI window to be displayed when a player wins
	 * @param gameResult holds the result of the game - winner's name or game over message
	 */
	public Winner(String gameResult) {
		
		// if exit button is clicked, dispose of this frame 
		// and create a new GameBoard frame
		f2.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		f2.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent e) {
				GameBoard.f.dispose();
				new GameBoard(false, false, true);
		    }
		});
		lblNewLabel.setFont(new Font("Bookman Old Style", Font.PLAIN, 15));
		
		lblNewLabel.setText("Label");
		f2.setResizable(false);
		f2.setBounds(100, 100, 315, 167);
		f2.getContentPane().setLayout(null);
		f2.setLocationRelativeTo(null);
		f2.setVisible(true);

		f2.setContentPane(new JLabel(new ImageIcon("res/graphics/bgImageToe.jpg")));
		
		if (gameResult.equals("Game Over! Tie")) {
			lblNewLabel.setText(gameResult);
		} else {
			lblNewLabel.setText(gameResult + " wins!");
		}

		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(0, 0, 310, 57);
		f2.getContentPane().add(lblNewLabel);
		btnPlayAgain.setFont(new Font("Lucida Fax", Font.BOLD, 12));

		btnPlayAgain.setBounds(39, 68, 100, 34);
		btnPlayAgain.setBackground(new Color(144, 238, 144));
		f2.getContentPane().add(btnPlayAgain);
		btnPlayAgain.addActionListener(this);
		btnPlayAgain.addKeyListener(this);
		btnPlayAgain.setSelected(true);
		btnQuit.setFont(new Font("Lucida Fax", Font.BOLD, 12));
		
		btnQuit.setSelected(true);
		btnQuit.setBackground(new Color(144, 238, 144));
		f2.getContentPane().add(btnQuit);
		btnQuit.setBounds(169, 68, 100, 34);
		btnQuit.addActionListener(this);
		btnQuit.addKeyListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {		
		if (e.getSource() == btnPlayAgain) {
			GameBoard.f.dispose();
			f2.dispose();
			new GameBoard(false, false, true);
		} else if (e.getSource() == btnQuit) {
			GameBoard.f.dispose();
			f2.dispose();
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (btnPlayAgain.isSelected() && e.getKeyChar() == KeyEvent.VK_ENTER) {
			GameBoard.f.dispose();
			f2.dispose();
			new GameBoard(false, false, true);
		}
	}
}
