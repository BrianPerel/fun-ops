package com.stopwatch;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serial;
import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

/**
 * Stopwatch app in the format 00:00:00 (hh:mm:ss).
 * Classes are nested to support multiple inheritance
 */
public class StopWatch extends JFrame {

	@Serial
	private static final long serialVersionUID = -75355816260383730L;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			System.out.println("Failed to set LookAndFeel\n" + e.getMessage());
			e.printStackTrace();
		}

		new StopWatch(300, 150); // 300 x 150 = default requested window measurements (width x height)
	}

	/**
	 * Creates the GUI frame (box)
	 */
	public StopWatch(int x, int y) {
		super("Stopwatch");
		createGui(x, y);
	}

	private void createGui(int x, int y) {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setContentPane(new StopWatchPanel());
		setSize(x, y);
		setVisible(true);
		setLocation(390, 345); // position the GUI on the screen at custom location (x, y)

		// set minimum GUI size
	    setMinimumSize(new Dimension(x, y));

	    // changes the program's taskbar icon
	    setIconImage(new ImageIcon("res/graphics/taskbar_icons/stopwatch.png").getImage());
	}

	/**
	 * Represents the stop-watch panel for this program. Will update the time here.
	 * This is an inner class of StopWatch
	 */
	public class StopWatchPanel extends JPanel {

		@Serial
		private static final long serialVersionUID = -6217657906467075510L;

		private static final String START_TIME = "00:00:00";
		public static final JLabel WATCH = new JLabel(START_TIME, SwingConstants.CENTER); // show the timer
		public static final JButton BTN_START = new JButton("START");
		public static final JButton BTN_STOP = new JButton("STOP");
		public static final JButton BTN_RESET = new JButton("RESET");
		public static boolean isRedFontEnabled;

		/** represent the hours, minutes, and seconds in the watch */
		private int hour;
		private int minute;
		private int second;
		private int centisecond;
	    final Color lightGreenColor = new Color(0, 255, 128);
	    final Color lightRedColor = new Color(255, 98, 98);
	    final Color lightBlueColor = new Color(146, 205, 255);

		private final Timer timer; // watch timer

		/**
		 * Constructor: Sets up this panel to listen for mouse events
		 */
		public StopWatchPanel() {
		    setLayout(new BorderLayout());
		    JPanel watchPanel = new JPanel();
		    WATCH.setFont(new Font("Helvetica", Font.PLAIN, 36));
		    watchPanel.add(WATCH);
		    final Color lightGrayColor = new Color(225, 225, 225);
		    watchPanel.setBackground(lightGrayColor);
		    add(watchPanel, BorderLayout.NORTH);

		    JPanel buttonPanel = new JPanel(new GridBagLayout());
		    buttonPanel.setBackground(lightGrayColor);
		    BTN_START.setBackground(lightGreenColor);
		    setHoverColor(BTN_START, lightGreenColor, new Color(0, 168, 49));
		    BTN_STOP.setBackground(lightRedColor);
		    setHoverColor(BTN_STOP, lightRedColor, new Color(192, 37, 51));
		    BTN_RESET.setBackground(lightBlueColor);
		    setHoverColor(BTN_RESET, lightBlueColor, new Color(60, 125, 171));

		    GridBagConstraints gbc = new GridBagConstraints();
		    gbc.insets = new Insets(10, 10, 10, 10);
		    gbc.anchor = GridBagConstraints.CENTER;
		    gbc.fill = GridBagConstraints.HORIZONTAL;

		    gbc.weightx = 1;
		    gbc.weighty = 1;
		    buttonPanel.add(BTN_START, gbc);

		    gbc.gridx = 1;
		    gbc.gridy = 0;
		    buttonPanel.add(BTN_STOP, gbc);

		    gbc.gridx = 2;
		    gbc.gridy = 0;
		    buttonPanel.add(BTN_RESET, gbc);

		    add(buttonPanel, BorderLayout.CENTER);

		    ButtonListener buttonListener = new ButtonListener();
		    timer = new Timer(0, buttonListener);

		    // when running the guessing games, if user closes the timer window stop counting the time
		    if (isRedFontEnabled) {
		        addWindowListener(new WindowAdapter() {
		            @Override
		            public void windowClosing(WindowEvent e) {
		                BTN_STOP.doClick();
		            }
		        });
		    }

		    for (JButton button : new JButton[] {BTN_START, BTN_STOP, BTN_RESET}) {
		        button.addKeyListener(buttonListener);
		        button.setFont(new Font("Georgia", Font.PLAIN, 15));
		        button.addActionListener(buttonListener);
		        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		    }
		}

		/**
		 * Sets a custom color to appear when you hover over a specific button
		 *
		 * @param argBtn button on which you want a different color to appear when hovering
		 * @param orgColor the original color the button had attached
		 * @param hoverColor the color you want to appear when hovering over the specific button
		 */
		private void setHoverColor(JButton argBtn, Color orgColor, Color hoverColor) {
			argBtn.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					argBtn.setBackground(hoverColor);
				}

				@Override
				public void mouseExited(MouseEvent e) {
					argBtn.setBackground(orgColor);
				}
			});
		}

		/**
		 * Represents a listener for button push (action) events.
		 * This is an inner class of StopWatchPanel
		 */
		private class ButtonListener extends KeyAdapter implements ActionListener, Serializable {

			@Serial
			private static final long serialVersionUID = 1905122041950251207L;

			private static final int TIMEBASE = 60;
			private static final int CENTSECBASE = 99;
			private static final int SHOWBASE = 10;

			@Override
			public void keyPressed(KeyEvent ke) {
				eventHandler(ke.getSource(), ke.getKeyChar());
			}

			@Override
			public void actionPerformed(ActionEvent ae) {
				eventHandler(ae.getSource(), (char) KeyEvent.VK_ENTER);
			}

			private void eventHandler(Object source, char keyChar) {
				if (hour == TIMEBASE && minute == TIMEBASE && second == TIMEBASE) {
					hour = minute = second = 0;
				}

				centisecond++;

				if (minute == TIMEBASE) {
					hour++;
					minute = 0;
				}
				if (second == TIMEBASE) {
					minute++;
					second = 0;
				}
				if (centisecond == CENTSECBASE) {
					second++;
					centisecond = 0;
				}
				if(keyChar == KeyEvent.VK_ENTER) {
					if (BTN_START.equals(source)) {
						BTN_START.setEnabled(false);
						BTN_STOP.setEnabled(true);
						timer.start();
					}
					else if (BTN_STOP.equals(source)) {
						BTN_START.setEnabled(true);
						BTN_STOP.setEnabled(false);
						timer.stop();
					}
					else if (BTN_RESET.equals(source)) {
						BTN_START.setEnabled(true);
						BTN_STOP.setEnabled(true);
						BTN_START.requestFocus();
						timer.stop();
						hour = minute = second = 0;
						WATCH.setText(START_TIME);

						if(isRedFontEnabled) {
							WATCH.setForeground(Color.BLACK);
						}
					}
				}

				setWatchText();
			}

			/**
			 * Sets the watch's time
			 */
			private void setWatchText() {
				if(WATCH.getText().compareTo("00:00:10") >= 0 && isRedFontEnabled) {
					WATCH.setForeground(Color.RED);
				}

				WATCH.setText(((hour < SHOWBASE) ? "0" : "") + hour + ":" + ((minute < SHOWBASE) ? "0" : "") + minute
						+ ":" + ((second < SHOWBASE) ? "0" : "") + second);
			}
		}
	}
}