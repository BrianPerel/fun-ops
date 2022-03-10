package pingpong;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;

public class PingPongGui extends JFrame implements ActionListener {

	private static final long serialVersionUID = 8513294011065865486L;
	private static final Color DARK_GREEN = new Color(0, 78, 3);
	private JMenuItem menuOption;

	public static void main(String[] args) {
		new PingPongGui();
	}

	/**
	 * Setups and creates the GUI
	 */
	public PingPongGui() {
		super("Pong Game");
		this.add(new GameBoard());
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		menuOption = new JMenuItem("Restart Game");
		
		menu.add(menuOption);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
		menuOption.addActionListener(this);
		
		this.setResizable(false);
		this.setBackground(DARK_GREEN);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);	
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == menuOption)  {  
			this.dispose();
			new PingPongGui();
		}
	}
}
