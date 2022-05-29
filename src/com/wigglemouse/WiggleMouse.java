package com.wigglemouse;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;

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

	private int xCoordinate;
	private int yCoordinate;
	private long waitTime = 30L;
	private final String[] WAIT_TIME_CHOICES = {"1/2 minute", "1 minute", "3 minutes", "5 minutes"};	
	private final JComboBox<String> WAIT_TIME_OPTIONS_COMBO_BOX = new JComboBox<>(new DefaultComboBoxModel<>(WAIT_TIME_CHOICES));

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
		JFrame frame = new JFrame("Wiggle mouse by B. Perel");
		frame.setResizable(false);
		frame.setSize(550, 182);
		frame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 14));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblDisplayMessage = new JLabel("Time to wait before wiggling your mouse (in minutes):");
		lblDisplayMessage.setFont(new Font("Narkisim", Font.PLAIN, 15));
		lblDisplayMessage.setBounds(35, 23, 370, 23);
		frame.getContentPane().add(lblDisplayMessage);

		WAIT_TIME_OPTIONS_COMBO_BOX.setBounds(395, 23, 100, 22);
		frame.getContentPane().add(WAIT_TIME_OPTIONS_COMBO_BOX);
		WAIT_TIME_OPTIONS_COMBO_BOX.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		JLabel lblArrowIcon = new JLabel(new ImageIcon("res/graphics/image-mouse-shake.jpg"));
		lblArrowIcon.setBounds(35, 64, 80, 55);
		frame.getContentPane().add(lblArrowIcon);	
		
		JButton btnStart = new JButton("SET TIME");
		btnStart.setFont(new Font("Book Antiqua", Font.ITALIC, 12));
		btnStart.setBounds(215, 78, 99, 23);
		frame.getContentPane().add(btnStart);
	    btnStart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnStart.addActionListener(actionEvent -> {
			if (actionEvent.getSource() == btnStart) {
				waitTime = updateIdleTime();
			}
		});
		btnStart.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER && e.getSource() == btnStart) {
					waitTime = updateIdleTime();
				}
			}
		});
		
	    frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	
		moveMouse(new Robot());
	}
	
	/**
	 * Updates the time to wait before wiggling the mouse
	 */
	public long updateIdleTime() {
		switch ((String) WAIT_TIME_OPTIONS_COMBO_BOX.getSelectedItem()) {
		case "1/2 minute":
			return 30;

		case "1 minute":
			return 60;

		case "3 minutes":
			return 180;

		case "5 minutes":
			return 300;
		
		default:
			return waitTime;
		}
	}

	/**
	 * Performs mouse movement actions
	 * @throws InterruptedException thrown if thread is interrupted while performing thread sleep operation
	 * @throws AWTException 
	 */
	public void moveMouse(Robot robot) throws InterruptedException {
		while (true) {
			// get current mouse pointer coordinates and set them. This needs to be done
			// twice in loop so that if user moves the mouse, the pointer doesn't jump back to the original point
			setMouseLocation(); 
			TimeUnit.SECONDS.sleep(waitTime); // time to wait before next mouse move
			setMouseLocation();

			// move the mouse to specified x,y coordinates with a shift value -- Wiggle Mouse action
			robot.mouseMove(xCoordinate, yCoordinate++);
			TimeUnit.MILLISECONDS.sleep(50L);
			robot.mouseMove(xCoordinate, yCoordinate--);
			TimeUnit.MILLISECONDS.sleep(50L);
			robot.mouseMove(xCoordinate, yCoordinate);
		}
	}

	/**
	 * Sets the current mouse's position into variables (x, y coordinates)
	 */
	public void setMouseLocation() {
		xCoordinate = (int) MouseInfo.getPointerInfo().getLocation().getX();
		yCoordinate = (int) MouseInfo.getPointerInfo().getLocation().getY();
	}
}
