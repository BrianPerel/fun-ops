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
 * Wiggle mouse program useful for when you need to be away from your computer while appearing available.
 * Allows for users to set the time interval for mouse wiggling in order to continuously maintain the appearance of activity.<br>
 *
 * @author Brian Perel
 *
 */
public class WiggleMouse {

	// using DefaultComboBoxModel type to prevent problem from arising with WindowBuilder eclipse plug-in
	private static final JComboBox<String> WAIT_TIME_OPTIONS_COMBO_BOX = new JComboBox<>(
		new DefaultComboBoxModel<>(new String[] {"1/2 minute", "1 minute", "3 minutes", "5 minutes"}));

	private int mouseXCoordinate;
	private int mouseYCoordinate;
	private long waitTime = 30L;

	/**
	 * Launches the application by creating an instance of the class
	 *
	 * @throws InterruptedException	thrown if thread is interrupted while building the app's window frame
	 * @throws AWTException thrown if there is an issue with the Abstract Window Toolkit (AWT) during application initialization.
	 */
	public static void main(String[] args) throws InterruptedException, AWTException {
		new WiggleMouse();
	}

	/**
	 * Initializes the contents of the frame and then enters mouse monitoring movement loop
	 *
	 * @throws InterruptedException	thrown if thread is interrupted while building the app's window frame
	 * @throws AWTException thrown if there is an issue with the Abstract Window Toolkit (AWT) during application GUI creation/start.
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

	    // ultra light grey color
		UIManager.put("Button.select", new Color(200, 203, 232));

		// applies a custom taskbar icon to the app
	    window.setIconImage(new ImageIcon("res/graphics/taskbar_icons/mouse.png").getImage());

	    JPanel panel = new JPanel(new GridBagLayout());
	    window.getContentPane().add(panel);

	    // set the minimum GUI size
	    window.setMinimumSize(new Dimension(window.getWidth(), window.getHeight()));

	    // using grid bag layout for GUI layout manager
	    GridBagConstraints gbcLabel = new GridBagConstraints();
	    gbcLabel.insets = new Insets(5, 5, 5, 5); // insets used for margin spacing
	    JLabel lblDisplayMessage = new JLabel("Time to wait before wiggling your mouse (in minutes):");
	    lblDisplayMessage.setFont(new Font("Narkisim", Font.PLAIN, 15));
	    gbcLabel.gridx = 0;
	    gbcLabel.gridy = 0;
	    gbcLabel.gridwidth = 2;
	    panel.add(lblDisplayMessage, gbcLabel);

	    GridBagConstraints gbcComboBox = new GridBagConstraints();
	    WAIT_TIME_OPTIONS_COMBO_BOX.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    gbcComboBox.gridx = 2;
	    gbcComboBox.gridy = 0;
	    gbcComboBox.gridwidth = 1;
	    panel.add(WAIT_TIME_OPTIONS_COMBO_BOX, gbcComboBox);

	    GridBagConstraints gbcArrowIcon = new GridBagConstraints();
	    JLabel lblArrowIcon = new JLabel(new ImageIcon("res/graphics/image-mouse-shake.jpg"));
	    gbcArrowIcon.gridx = 0;
	    gbcArrowIcon.gridy = 1;
	    gbcArrowIcon.gridwidth = 1;
	    panel.add(lblArrowIcon, gbcArrowIcon);

	    GridBagConstraints gbcBtnSetTime = new GridBagConstraints();
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
	    	    // ultra light grey color
	            btnSetTime.setBackground(new Color(200, 203, 232));
	        }

	        @Override
	        public void mouseExited(MouseEvent e) {
	            btnSetTime.setBackground(UIManager.getColor(btnSetTime));
	        }
	    });
	    gbcBtnSetTime.gridx = 1;
	    gbcBtnSetTime.gridy = 1;
	    gbcBtnSetTime.gridwidth = 1;
	    panel.add(btnSetTime, gbcBtnSetTime);

	    window.setLocationRelativeTo(null);
	    window.setVisible(true);
	    btnSetTime.doClick();
	    window.setExtendedState(Frame.ICONIFIED); // makes the GUI window be minimized (iconified) when launched
	}

	/**
	 * Retrieves the selected item from the wait time drop down box
	 *
	 * @return the time to wait in minutes before programmatically wiggling the mouse or the current waitTime value if no match is found
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
	 * Continuously monitors and performs mouse movement actions by retrieving the current mouse coordinates,
	 * waiting the allotted time, and then moving the mouse to the new coordinates.
	 * Wiggle effect is only done if the mouse coordinates remain unchanged during the waiting period.
	 *
	 * @param robot The Robot instance used for controlling mouse actions.
	 * @throws InterruptedException thrown if the thread is interrupted during the sleep operation.
	 * @throws AWTException thrown if there is an issue with the Abstract Window Toolkit (AWT) during mouse movement.
	 */
	private void moveMouse(Robot robot) throws InterruptedException {
	    while (true) {
	    	setMouseLocation();
		    int previousX = mouseXCoordinate;
		    int previousY = mouseYCoordinate;

	        TimeUnit.SECONDS.sleep(waitTime); // time to wait before next mouse move
	        setMouseLocation(); // sets the current mouse coordinates after the wait time to see if the mouse was moved during the waiting period

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
	 * Obtains and sets the current mouse's position into x, y coordinate variables
	 */
	private void setMouseLocation() {
		mouseXCoordinate = (int) MouseInfo.getPointerInfo().getLocation().getX();
		mouseYCoordinate = (int) MouseInfo.getPointerInfo().getLocation().getY();
	}
}
