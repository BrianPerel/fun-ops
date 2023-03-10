package com.wigglemouse;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Frame;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

/**
 * Wiggle mouse program. Useful for when you want to be away from your computer
 * however appear to be available. <br>
 *
 * @author Brian Perel
 *
 */
public class WiggleMouse {

	private int mouseXCoordinate;
	private int mouseYCoordinate;
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
	 * @throws AWTException
	 * @throws InterruptedException
	 */
	public WiggleMouse() throws InterruptedException, AWTException  {
		createGui();
	}

	private void createGui() throws InterruptedException, AWTException {
		JFrame window = new JFrame("Wiggle mouse by B. Perel");
		window.setResizable(false);
		window.setSize(550, 182);
		window.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 14));
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.getContentPane().setLayout(null);

		JLabel lblDisplayMessage = new JLabel("Time to wait before wiggling your mouse (in minutes):");
		lblDisplayMessage.setFont(new Font("Narkisim", Font.PLAIN, 15));
		lblDisplayMessage.setBounds(35, 23, 370, 23);
		window.getContentPane().add(lblDisplayMessage);

		WAIT_TIME_OPTIONS_COMBO_BOX.setBounds(395, 23, 100, 22);
		window.getContentPane().add(WAIT_TIME_OPTIONS_COMBO_BOX);
		WAIT_TIME_OPTIONS_COMBO_BOX.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		JLabel lblArrowIcon = new JLabel(new ImageIcon("res/graphics/image-mouse-shake.jpg"));
		lblArrowIcon.setBounds(35, 64, 80, 55);
		window.getContentPane().add(lblArrowIcon);

		JButton btnSetTime = new JButton("SET TIME");
		btnSetTime.setFont(new Font("Book Antiqua", Font.ITALIC, 12));
		btnSetTime.setBounds(215, 78, 99, 23);
		window.getContentPane().add(btnSetTime);
	    btnSetTime.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnSetTime.addActionListener(actionEvent -> {
			if (actionEvent.getSource() == btnSetTime) {
				waitTime = updateIdleTime();
			}
		});
		btnSetTime.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER && e.getSource() == btnSetTime) {
					waitTime = updateIdleTime();
				}
			}
		});
		btnSetTime.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnSetTime.setBackground(new Color(200, 203, 232));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btnSetTime.setBackground(UIManager.getColor(btnSetTime));
			}
		});

	    window.setVisible(true);
		window.setLocationRelativeTo(null);

		btnSetTime.doClick();
		window.setExtendedState(Frame.ICONIFIED); // makes the GUI window be minimized when launched
		moveMouse(new Robot());
	}

	/**
	 * Updates the time to wait before wiggling the mouse
	 */
	private long updateIdleTime() {
		return switch ((String) WAIT_TIME_OPTIONS_COMBO_BOX.getSelectedItem()) {
			case "1/2 minute" -> 30L;
			case "1 minute" -> 60L;
			case "3 minutes" -> 180L;
			case "5 minutes" -> 300L;
			default -> waitTime;
		};
	}

	/**
	 * Performs mouse movement actions by getting current mouse coordinates, waiting the allotted time, then moving the mouse to the new coordinates
	 * @throws InterruptedException thrown if thread is interrupted while performing thread sleep operation
	 * @throws AWTException
	 */
	private void moveMouse(Robot robot) throws InterruptedException {
		while (true) {
			// get current mouse pointer coordinates and set them. This needs to be done
			// twice in loop so that if user moves the mouse, the pointer doesn't jump back to the original point
			setMouseLocation();
			TimeUnit.SECONDS.sleep(waitTime); // time to wait before next mouse move
			setMouseLocation();

			// move the mouse to specified (x, y) coordinates with a shift value -- creates the Wiggle Mouse action
			for(int x = 0; x < 2; x++) {
				robot.mouseMove(mouseXCoordinate, (x == 0) ? mouseYCoordinate++ : mouseYCoordinate--);
				TimeUnit.MILLISECONDS.sleep(50L);
			}

			robot.mouseMove(mouseXCoordinate, mouseYCoordinate);
		}
	}

	/**
	 * Obtains and sets the current mouse's position into variables (x, y coordinates)
	 */
	private void setMouseLocation() {
		mouseXCoordinate = (int) MouseInfo.getPointerInfo().getLocation().getX();
		mouseYCoordinate = (int) MouseInfo.getPointerInfo().getLocation().getY();
	}
}
