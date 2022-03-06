package wigglemouse;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

/**
 * Mouse wiggle program. Useful for when you want to be away from your computer
 * however appear to be available. <br>
 * 
 * @author Brian Perel
 *
 */
public class WiggleMouse {

	private JButton btnStart;
	private static int y;
	private static int x;
	private static int timeToWait;
	private static String[] waitTimeChoices = {"1/2 minute", "1 minute", "3 minutes", "5 minutes"};	
	private static final JComboBox<String> waitTimeOptionsComboBox = new JComboBox<>(new DefaultComboBoxModel<>(waitTimeChoices));

	/**
	 * Launch the application.
	 * @throws InterruptedException	thrown if thread is interrupted while building the apps window and frame
	 * @throws AWTException 
	 */
	public static void main(String[] args) throws InterruptedException, AWTException {		
		new WiggleMouse();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public WiggleMouse() throws InterruptedException, AWTException {
		JFrame frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 550, 182);
		frame.setTitle("Wiggle mouse by B. Perel");
		frame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 14));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		btnStart = new JButton("SET TIME");
		btnStart.setFont(new Font("Book Antiqua", Font.ITALIC, 12));
		btnStart.setBounds(215, 78, 99, 23);
		frame.getContentPane().add(btnStart);

		JLabel lblDisplayMessage = new JLabel("Time to wait before wiggling your mouse (in minutes):");
		lblDisplayMessage.setFont(new Font("Narkisim", Font.PLAIN, 15));
		lblDisplayMessage.setBounds(35, 23, 370, 23);
		frame.getContentPane().add(lblDisplayMessage);

		waitTimeOptionsComboBox.setBounds(395, 23, 100, 22);
		frame.getContentPane().add(waitTimeOptionsComboBox);
		
		JLabel lblNewLabel = new JLabel(new ImageIcon("res/graphics/image-mouse-shake.jpg"));
		lblNewLabel.setBounds(35, 64, 80, 55);
		frame.getContentPane().add(lblNewLabel);	
		
	    btnStart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    
	    frame.setVisible(true);
		frame.setLocationRelativeTo(null);

		btnStart.addActionListener((ActionEvent ae) -> {
			if (ae.getSource() == btnStart) {
				updateWaitingTime();
			}
		});
		
		btnStart.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent ae) {
				if (ae.getKeyChar() == KeyEvent.VK_ENTER && ae.getSource() == btnStart) {
					updateWaitingTime();
				}
			}
		});
	
		moveMouse();
	}
	
	/**
	 * Updates the time to wait before moving the mouse
	 */
	public static void updateWaitingTime() {
		switch ((String) waitTimeOptionsComboBox.getSelectedItem()) {
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
		
		default:
			break;
		}
	}

	/**
	 * Performs mouse movement action
	 * @throws InterruptedException thrown if thread is interrupted while performing thread sleep operation
	 * @throws AWTException 
	 */
	public static void moveMouse() throws InterruptedException, AWTException {
		
		timeToWait = 30000; // default wait time before wiggling your mouse
		Robot robot = new Robot();
		
		while (true) {
			// get current mouse pointer coordinates and set them. This needs to be done
			// twice in loop so that if user moves mouse the pointer doesn't jump back to original point
			setMouseLocation();
			Thread.sleep(timeToWait); // time to wait before next mouse move
			setMouseLocation();

			// move the mouse to specified x,y coordinates with a shift value -- Wiggle
			// Mouse action
			robot.mouseMove(x, y++);
			Thread.sleep(50);
			robot.mouseMove(x, y--);
			Thread.sleep(50);
			robot.mouseMove(x, y);
		}
	}

	/**
	 * Sets the current mouse's position into variables (x, y coordinates)
	 */
	public static void setMouseLocation() {
		x = (int) MouseInfo.getPointerInfo().getLocation().getX();
		y = (int) MouseInfo.getPointerInfo().getLocation().getY();
	}
}
