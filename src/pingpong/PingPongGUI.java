package pingpong;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class PingPongGUI extends JFrame {
	
	public static void main(String[] args) {
		new PingPongGUI();
	}

	/**
	 * Setups and creates the GUI
	 */
	public PingPongGUI() {
		this.add(new GameBoard());
		this.setTitle("Pong Game");
		this.setResizable(false);
		this.setBackground(new Color(0, 91, 33));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
}
