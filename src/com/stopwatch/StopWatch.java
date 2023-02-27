package com.stopwatch;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serial;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
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
		} catch (Exception e) {
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
		setResizable(false);
		setSize(x, y);
		setVisible(true);
		setLocation(390, 345); // position the GUI on the screen at custom location (x, y)
	}

	/**
	 * Represents the stop-watch panel for this program. Will update the time here.
	 * This is an inner class of StopWatch
	 */
	public class StopWatchPanel extends JPanel {

		@Serial
		private static final long serialVersionUID = -6217657906467075510L;

		private static final String START_TIME = "00:00:00";
		public static final JLabel watch = new JLabel(START_TIME, SwingConstants.CENTER); // show the timer
		public static final JButton btnStart = new JButton("START");
		public static final JButton btnStop = new JButton("STOP");
		public static final JButton btnReset = new JButton("RESET");
		public static boolean isRedFontEnabled;

		/** represent the hours, minutes, and seconds in the watch */
		private int hour;
		private int minute;
		private int second;
		private int centisecond;

		private final Timer timer; // watch timer

		/**
		 * Constructor: Sets up this panel to listen for mouse events
		 */
		public StopWatchPanel() {
			setLayout(new BorderLayout());
			JPanel watchPanel = new JPanel();
			watch.setFont(new Font("Helvetica", Font.PLAIN, 36));
			watchPanel.add(watch);
			final Color lightGray = new Color(225, 225, 225);
			watchPanel.setBackground(lightGray);
			add(watchPanel, BorderLayout.NORTH);
			JPanel buttonPanel = new JPanel();
			buttonPanel.setBackground(lightGray);
		    final Color lightGreen = new Color(0, 255, 128);
			btnStart.setBackground(lightGreen);
			final Color lightRed = new Color(255, 98, 98);
			btnStop.setBackground(lightRed);
			final Color lightBlue = new Color(146, 205, 255);
			btnReset.setBackground(lightBlue);
			add(buttonPanel, BorderLayout.CENTER);
			ButtonListener buttonListener = new ButtonListener();
			timer = new Timer(0, buttonListener);
			JButton[] buttons = {btnStart, btnStop, btnReset};

			// when running the guessing games, if user closes the timer window stop counting the time
			if(isRedFontEnabled) {
				addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						btnStop.doClick();
					}
				});
			}

			for(JButton button : buttons) {
				button.addKeyListener(buttonListener);
				button.setFont(new Font("Georgia", Font.PLAIN, 15));
				button.addActionListener(buttonListener);
				button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				buttonPanel.add(button);
			}
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
					if (source.equals(btnStart)) {
						btnStart.setEnabled(false);
						btnStop.setEnabled(true);
						timer.start();
					}
					else if (source.equals(btnStop)) {
						btnStart.setEnabled(true);
						btnStop.setEnabled(false);
						timer.stop();
					}
					else if (source.equals(btnReset)) {
						btnStart.setEnabled(true);
						btnStop.setEnabled(true);
						btnStart.requestFocus();
						timer.stop();
						hour = minute = second = 0;
						watch.setText(START_TIME);

						if(isRedFontEnabled) {
							watch.setForeground(Color.BLACK);
						}
					}
				}

				setWatchText();
			}

			/**
			 * Sets the watch's time
			 */
			private void setWatchText() {
				if(watch.getText().compareTo("00:00:10") >= 0 && isRedFontEnabled) {
					watch.setForeground(Color.RED);
				}

				watch.setText(((hour < SHOWBASE) ? "0" : "") + hour + ":" + ((minute < SHOWBASE) ? "0" : "") + minute
						+ ":" + ((second < SHOWBASE) ? "0" : "") + second);
			}
		}
	}
}