package wiggle.mouse;

import java.awt.AWTException;
import java.awt.EventQueue;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * Mouse move program. Moves the mouse to random coordinate points on the screen every 4 seconds until STOP button is pressed.
 * @author Brian Perel
 *
 */
public class MouseMove implements ActionListener {

	private JFrame frame;
	JButton btnStart;
	JButton btnStop;
	static boolean performAction;
	private static final long FOUR_SECONDS = 4000;
	private static final int SIX_HUNDRED = 600;
	static Random random = new Random();

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
		frame.setBounds(100, 100, 176, 148);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		btnStart = new JButton("START");
		btnStart.setBounds(45, 43, 91, 23);
		frame.getContentPane().add(btnStart);
		btnStart.addActionListener(this);

		btnStop = new JButton("STOP");
		btnStop.setBounds(177, 43, 91, 23);
		frame.getContentPane().add(btnStop);
		btnStop.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == btnStart) {
			performAction = true;
		} else if (ae.getSource() == btnStop) {
			performAction = false;
		}
	}

	public static void moveMouse() throws AWTException, InterruptedException {
		Robot rb = new Robot();

		while (performAction) {
			rb.mouseMove(random.nextInt(SIX_HUNDRED), random.nextInt(SIX_HUNDRED));
			Thread.sleep(FOUR_SECONDS);
		}
	}
}