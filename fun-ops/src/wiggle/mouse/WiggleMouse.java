package wiggle.mouse;

import java.awt.AWTException;
import java.awt.EventQueue;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.ImageIcon;
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
public class WiggleMouse implements ActionListener {

	private JFrame frame;
	JButton btnStart, btnStop;
	// performAction will be used to indicate whether or not to perform mouse move. 
	// Only if start btn is pushed will this be set to true
	static boolean performAction = false;
	static Robot robot;
	static Point point;
	static int x, y, timeToWait;
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
					WiggleMouse window = new WiggleMouse();
					window.frame.setVisible(true);
					window.frame.setTitle("Mouse move by B. Perel");
					window.frame.setResizable(false);
					window.frame.setLocationRelativeTo(null);
					robot = new Robot();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		while (true) {
			moveMouse();
			Thread.sleep(500);
		}
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws InterruptedException
	 * @throws AWTException
	 */
	private WiggleMouse() {
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

		ImageIcon icon = new ImageIcon(getClass().getResource("icon.jpg"));
		JLabel lblWithIcon = new JLabel(icon);
		lblWithIcon.setBounds(336, 69, 55, 49);
		frame.getContentPane().add(lblWithIcon);

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
				timeToWait = 2000;
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

	public static void moveMouse() throws InterruptedException {
		while (performAction) {
			// get current mouse pointer coordinates and set them. This needs to be done twice in loop so that if user moves mouse the pointer doesn't jump back to original point
			setMouseLocation();
			// time to wait before next mouse move
			Thread.sleep(timeToWait);
			setMouseLocation();
			
			// defect fix: after clicking stop btn wiggle mouse still occurs once, however this prevents that action from happening
			if(!performAction) {
				break;
			}
			
			// move the mouse to specified x,y coordinates with a shift value -- Wiggle Mouse action
			robot.mouseMove(x, y + 5);
			Thread.sleep(50);
			robot.mouseMove(x, y - 5);
			Thread.sleep(50);
			robot.mouseMove(x, y);
		}
	}
	
	public static void setMouseLocation() {
		point = MouseInfo.getPointerInfo().getLocation(); 
		x = (int) point.getX();
		y = (int) point.getY();
	}
}