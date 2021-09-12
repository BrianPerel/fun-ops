package tic.tac.toe;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

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
		
		lblNewLabel.setText("Label");
		f2.setResizable(false);
		f2.setBounds(100, 100, 315, 167);
		f2.getContentPane().setLayout(null);
		f2.setLocationRelativeTo(null);

		File bgImageFile = new File("res/graphics/bgImageToe.jpg");
		ImageIcon image = new ImageIcon(bgImageFile.toString());
		JLabel backgroundLabel = new JLabel(image);
		f2.setContentPane(backgroundLabel);

		if (!gameResult.equals("Game Over! Tie")) {
			lblNewLabel.setText(gameResult + " wins!");
		} else if (gameResult.equals("Game Over! Tie")) {
			lblNewLabel.setText(gameResult);
		} 

		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(0, 0, 310, 57);
		f2.getContentPane().add(lblNewLabel);

		btnPlayAgain.setBounds(104, 68, 100, 34);
		btnPlayAgain.setBackground(new Color(144, 238, 144));
		f2.getContentPane().add(btnPlayAgain);
		f2.setVisible(true);
		btnPlayAgain.setSelected(true);
		btnPlayAgain.addActionListener(this);
		btnPlayAgain.addKeyListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {		
		if (e.getSource() == btnPlayAgain) {
			GameBoard.f.dispose();
			f2.dispose();
			new GameBoard(false, false, true);
		} 
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(btnPlayAgain.isSelected() && e.getKeyChar() == KeyEvent.VK_ENTER) {
			GameBoard.f.dispose();
			f2.dispose();
			new GameBoard(false, false, true);
		}
	}
}
