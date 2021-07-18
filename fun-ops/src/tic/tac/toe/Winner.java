package tic.tac.toe;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * 
 * @author Brian Perel
 *
 *         Implementation for winner window. When a player wins, this window is
 *         displayed.
 */
public class Winner implements ActionListener {

	private JFrame f2 = new JFrame("Tic Tac Toe");
	JButton btnPlayAgain = new JButton("Play again");
	JLabel lblNewLabel = new JLabel();

	public Winner(String winner) {

		lblNewLabel.setText("Label");
		f2.setResizable(false);
		f2.setBounds(100, 100, 315, 167);
		f2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f2.getContentPane().setLayout(null);
		f2.setLocationRelativeTo(null);

		if (!winner.equals("Game Over!")) {
			lblNewLabel.setText(winner + " wins!");
		} else if (winner.equals("Game Over! Tie, no one wins")) {
			lblNewLabel.setText(winner);
		} else {
			lblNewLabel.setText("Game Over!");
		}

		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);

		lblNewLabel.setBounds(0, 0, 310, 57);
		f2.getContentPane().add(lblNewLabel);

		btnPlayAgain.setBounds(104, 68, 100, 34);
		btnPlayAgain.setBackground(new Color(144, 238, 144));
		f2.getContentPane().add(btnPlayAgain);
		f2.setVisible(true);
		btnPlayAgain.addActionListener(this);
		btnPlayAgain.setFocusable(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnPlayAgain) {
			f2.dispose();
			new GameBoard(false, false, true);
		}
	}
}
