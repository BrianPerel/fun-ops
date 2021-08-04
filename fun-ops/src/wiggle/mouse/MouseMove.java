package wiggle.mouse;

import java.awt.AWTException;
import java.awt.EventQueue;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Mouse move program. Moves the mouse to random coordinate points on the screen
 * every 4 seconds until STOP button is pressed. Useful for when you want to be
 * away from your computer however appear to be available
 * 
 * @author Brian Perel
 *
 */
public class MouseMove implements ActionListener {

	private JFrame frame;
	JButton btnStart;
	JButton btnStop;
	static boolean performAction = false;
	static final int SIX_HUNDRED = 600;
	static Random random = new Random();
	static int timeToWait;
	static Robot rb;
	final JComboBox<String> comboBox;
	String[] choices = { "1/2 minute", "1 minute", "3 minutes", "5 minutes", "10 minutes" };

	/**
	 * Launch the application.
	 * 
	 * @throws InterruptedException
	 * @throws AWTException
	 */
	public static void main(String[] args) throws AWTException, InterruptedException {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MouseMove window = new MouseMove();
					window.frame.setVisible(true);
					window.frame.setTitle("Mouse move by B. Perel");
					window.frame.setResizable(false);
					window.frame.setLocationRelativeTo(null);
					rb = new Robot();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		while (true) {
			moveMouse();
			Thread.sleep(1000);
		}
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws InterruptedException
	 * @throws AWTException
	 */
	private MouseMove() {
		frame = new JFrame();
		frame.setBounds(100, 100, 430, 182);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		btnStart = new JButton("START");
		btnStart.setBounds(88, 82, 91, 23);
		frame.getContentPane().add(btnStart);
		btnStart.addActionListener(this);

		btnStop = new JButton("STOP");
		btnStop.setBounds(220, 82, 91, 23);
		frame.getContentPane().add(btnStop);

		JLabel lblNewLabel = new JLabel("Time to wait before mouse move (in minutes)");
		lblNewLabel.setBounds(23, 23, 264, 23);
		frame.getContentPane().add(lblNewLabel);

		// JComboBox comboBox = new JComboBox(); // use this when switching to
		// WindowBuilder editor's design tab since "new JComboBox<>() is not valid
		// choice for component creation"
		comboBox = new JComboBox<>(choices);
		comboBox.setBounds(297, 23, 83, 22);
		frame.getContentPane().add(comboBox);
		btnStop.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == btnStart) {
			performAction = true;

			switch ((String) comboBox.getSelectedItem()) {
			case "1/2 minute":
				timeToWait = 30000;
				break;

			case "1 minute":
				timeToWait = 60000;
				break;

			case "3 minutes":
				timeToWait = 180000;
				break;

			case "5 minutes":
				timeToWait = 300000;
				break;

			case "10 minutes":
				timeToWait = 600000;
				break;
			}

		} else if (ae.getSource() == btnStop) {
			performAction = false;
		}
	}

	public static void moveMouse() throws AWTException, InterruptedException {
		while (performAction) {
			Thread.sleep(timeToWait);
			rb.mouseMove(random.nextInt(SIX_HUNDRED), random.nextInt(SIX_HUNDRED));
		}
	}
}