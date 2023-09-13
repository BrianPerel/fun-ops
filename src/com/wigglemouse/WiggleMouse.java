package com.wigglemouse;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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

	// using DefaultComboBoxModel to prevent problem with WindowBuilder
	private static final JComboBox<String> WAIT_TIME_OPTIONS_COMBO_BOX = new JComboBox<>(
		new DefaultComboBoxModel<>(new String[] {"1/2 minute", "1 minute", "3 minutes", "5 minutes"}));

	private int mouseXCoordinate;
	private int mouseYCoordinate;
	private long waitTime = 30L;

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
	    moveMouse(new Robot());
	}

	private void createGui() {
	    JFrame window = new JFrame("Wiggle mouse by B. Perel");
	    window.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 14));
	    window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    window.setSize(550, 182);

		UIManager.put("Button.select", new Color(200, 203, 232));

	    // changes the program's taskbar icon
	    window.setIconImage(new ImageIcon("res/graphics/taskbar_icons/mouse.png").getImage());

	    JPanel panel = new JPanel(new GridBagLayout());
	    window.add(panel);

	    // set minimum GUI size
	    window.setMinimumSize(new Dimension(window.getWidth(), window.getHeight()));

	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(5, 5, 5, 5);

	    JLabel lblDisplayMessage = new JLabel("Time to wait before wiggling your mouse (in minutes):");
	    lblDisplayMessage.setFont(new Font("Narkisim", Font.PLAIN, 15));
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.gridwidth = 2;
	    panel.add(lblDisplayMessage, gbc);

	    WAIT_TIME_OPTIONS_COMBO_BOX.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    gbc.gridx = 2;
	    gbc.gridy = 0;
	    gbc.gridwidth = 1;
	    panel.add(WAIT_TIME_OPTIONS_COMBO_BOX, gbc);

	    JLabel lblArrowIcon = new JLabel(new ImageIcon("res/graphics/image-mouse-shake.jpg"));
	    gbc.gridx = 0;
	    gbc.gridy = 1;
	    gbc.gridwidth = 1;
	    panel.add(lblArrowIcon, gbc);

	    JButton btnSetTime = new JButton("SET TIME");
	    btnSetTime.setFont(new Font("Book Antiqua", Font.ITALIC, 12));
	    btnSetTime.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    btnSetTime.addActionListener(actionEvent -> {
	        if (actionEvent.getSource().equals(btnSetTime)) {
	            waitTime = updateIdleTime();
	        }
	    });
	    btnSetTime.addKeyListener(new KeyAdapter() {
	        @Override
	        public void keyPressed(KeyEvent e) {
	            if (e.getKeyChar() == KeyEvent.VK_ENTER && e.getSource().equals(btnSetTime)) {
	                waitTime = updateIdleTime();
	            }
	        }
	    });
	    btnSetTime.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseEntered(MouseEvent e) {
	            btnSetTime.setBackground(new Color(200, 203, 232));
	        }

	        @Override
	        public void mouseExited(MouseEvent e) {
	            btnSetTime.setBackground(UIManager.getColor(btnSetTime));
	        }
	    });
	    gbc.gridx = 1;
	    gbc.gridy = 1;
	    gbc.gridwidth = 1;
	    panel.add(btnSetTime, gbc);

	    window.setLocationRelativeTo(null);
	    window.setVisible(true);
	    btnSetTime.doClick();
	    window.setExtendedState(Frame.ICONIFIED); // makes the GUI window be minimized when launched
	}

	/**
	 * Updates the time to wait in minutes before wiggling the mouse
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
	    	setMouseLocation();
		    int previousX = mouseXCoordinate;
		    int previousY = mouseYCoordinate;

	        TimeUnit.SECONDS.sleep(waitTime); // time to wait before next mouse move
	        setMouseLocation(); // set the mouse coordinates again to see if the mouse was moved during the waiting period

	        // check if the mouse coordinates have changed
	        if (mouseXCoordinate != previousX || mouseYCoordinate != previousY) {
	            // if user is moving the mouse, execute continue
	            continue;
	        }

	        // Move the mouse to specified (x, y) coordinates with a shift value -- creates the wiggle mouse action
	        for (int x = 0; x < 2; x++) {
                int shiftAmount = (x == 0) ? 1 : -1;
	            robot.mouseMove(mouseXCoordinate, mouseYCoordinate + shiftAmount);
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
