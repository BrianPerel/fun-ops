package pingpong;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class PingPongGUI extends JFrame {
	
	public static void main(String[] args) {
		new PingPongGUI();
	}

	public PingPongGUI() {
		GamePanel panel = new GamePanel();
		this.add(panel);
		this.setTitle("Pong Game");
		this.setResizable(false);
		this.setBackground(new Color(0, 91, 33));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
}
