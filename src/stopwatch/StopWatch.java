package stopwatch;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

/**
 * Stop-watch app in the format 00:00:00 (hh:mm:ss).
 * Classes are nested to support multiple inheritance
 */
@SuppressWarnings("serial")
public class StopWatch extends JFrame {
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		new StopWatch(300, 150); // 300 x 150 = default requested window measurements (width x height)
	}

	/**
	 * Creates the GUI frame (box)
	 */	
	public StopWatch(int x, int y) {
		super("Stopwatch");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setContentPane(new StopWatchPanel());
		setResizable(false);
		setSize(x, y);
		setVisible(true);
		setLocation(390, 345); // position the gui on the screen at custom location (x, y)
	}

	/**
	 * Represents the stop-watch panel for this program. Will update the time here.
	 * This is an inner class of StopWatch
	 */
	public class StopWatchPanel extends JPanel {
		/** represent the hours, minutes, and seconds in the watch */
		private int hour;
		private int minute;
		private int second;
		private int centisecond;
		public static boolean isRedFontEnabled;
		private static final String START_TIME = "00:00:00";
		/** Show the timer */
		public static final JLabel watch = new JLabel(START_TIME, SwingConstants.CENTER);
		/** btns - start, stop, reset */
		public static final JButton btnStart = new JButton("START");
		public static final JButton btnStop = new JButton("STOP");
		public static final JButton btnReset = new JButton("RESET");
		/** Timer to be used for watch */
		private Timer timer;
		/** Button listener */
		private ActionListener buttonListener = new ButtonListener();

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
			timer = new Timer(0, buttonListener);
			JButton[] buttons = {btnStart, btnStop, btnReset};
			
			for(JButton button : buttons) {
				button.addKeyListener((KeyListener) buttonListener);
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
		class ButtonListener extends KeyAdapter implements ActionListener {
			
			private static final int TIMEBASE = 60;
			private static final int CENTSECBASE = 99;
			private static final int SHOWBASE = 10;

			@Override
			public void actionPerformed(ActionEvent event) {	
				Object source = event.getSource();
				
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
				if (source == btnStart) {
					btnStart.setEnabled(false);
					btnStop.setEnabled(true);
					timer.start();
				} 
				else if (source == btnStop) {
					btnStart.setEnabled(true);
					btnStop.setEnabled(false);
					timer.stop();
				} 
				else if (source == btnReset) { 
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
				
				setTextForWatch();
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				Object source = e.getSource();
				char keyChar = e.getKeyChar();
				
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
				if (keyChar == KeyEvent.VK_ENTER && source == btnStart) {
					btnStart.setEnabled(false);
					btnStop.setEnabled(true);
					timer.start();
				} 
				else if (keyChar == KeyEvent.VK_ENTER && source == btnStop) {
					btnStart.setEnabled(true);
					btnStop.setEnabled(false);
					timer.stop();
				} 
				else if (keyChar == KeyEvent.VK_ENTER && source == btnReset) { 
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
				
				setTextForWatch();
			}

			/**
			 * Sets the watch's time
			 */
			public void setTextForWatch() {
				if(watch.getText().compareTo("00:00:10") >= 0 && isRedFontEnabled) {
					watch.setForeground(Color.RED);
				}
				
				watch.setText(((hour < SHOWBASE) ? "0" : "") + hour + ":" + ((minute < SHOWBASE) ? "0" : "") + minute
						+ ":" + ((second < SHOWBASE) ? "0" : "") + second);
			}
		}
	}
}