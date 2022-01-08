package pingpong;

import java.awt.Color;

import javax.swing.JFrame;

public class PingPongGui extends JFrame {
	
	private static final long serialVersionUID = 8513294011065865486L;

	public static void main(String[] args) {
		new PingPongGui();
	}

	/**
	 * Setups and creates the GUI
	 */
	public PingPongGui() {
		this.add(new GameBoard());
		this.setTitle("Pong Game");
		this.setResizable(false);
		this.setBackground(new Color(0, 78, 3));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
}
