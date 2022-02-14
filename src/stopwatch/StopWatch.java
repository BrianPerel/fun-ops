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
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

/**
 * A Java application that works as a stop-watch in the format 00:00:00
 * (hh:mm:ss) Classes are nested to support multiple inheritance
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
		super("Brian Perel - Stopwatch");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setContentPane(new StopWatchPanel());
		setResizable(false);
		setSize(x, y);
		setVisible(true);
		setLocation(390, 345); // position the gui on the screen at custom location (x, y)
	}

	/**
	 * Represents the stop-watch panel for this program. Will update the time here
	 */
	public class StopWatchPanel extends JPanel {
		/** represent the hours, minutes, and seconds in the watch. */
		private int hour, minute, second, centisec;
		/** btns - start, stop, reset */
		public static JButton btnStart, btnStop, btnReset;
		/** Show the timer. */
		public static JLabel watch;
		/** Timer to be used for watch. */
		private Timer timer;
		/** Button listener. */
		private ActionListener b = new ButtonListener();

		/**
		 * Constructor: Sets up this panel to listen for mouse events.
		 */
		public StopWatchPanel() {
			setLayout(new BorderLayout());
			JPanel watchPanel = new JPanel();
			watch = new JLabel("00:00:00", JLabel.CENTER);
			watch.setFont(new Font("Helvetica", Font.PLAIN, 36));
			watchPanel.add(watch);
			watchPanel.setBackground(new Color(225, 225, 225));
			add(watchPanel, BorderLayout.NORTH);
			JPanel buttonPanel = new JPanel();
			buttonPanel.setBackground(new Color(225, 225, 225));
			btnStart = new JButton("START");
			btnStop = new JButton("STOP");
			btnReset = new JButton("RESET");
			btnStart.setFont(new Font("Georgia", Font.PLAIN, 15));
			btnStop.setFont(new Font("Georgia", Font.PLAIN, 15));
			btnReset.setFont(new Font("Georgia", Font.PLAIN, 15));
		    btnStart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		    btnStop.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		    btnReset.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			btnStart.setBackground(new Color(0, 255, 128));
			btnStop.setBackground(new Color(255, 98, 98));
			btnReset.setBackground(new Color(146, 205, 255));
			btnStart.addActionListener(b);
			btnStop.addActionListener(b);
			btnReset.addActionListener(b);
			btnStart.addKeyListener((KeyListener) b);
			btnStop.addKeyListener((KeyListener) b);
			btnReset.addKeyListener((KeyListener) b);
			buttonPanel.add(btnStart);
			buttonPanel.add(btnStop);
			buttonPanel.add(btnReset);
			add(buttonPanel, BorderLayout.CENTER);
			timer = new Timer(10, b);
		}

		/**
		 * Represents a listener for button push (action) events.
		 */
		public class ButtonListener extends KeyAdapter implements ActionListener {
			
			private static final int TIMEBASE = 60, CENTSECBASE = 99, SHOWBASE = 10;

			@Override
			public void actionPerformed(ActionEvent event) {				
				if (hour == TIMEBASE && minute == TIMEBASE && second == TIMEBASE) {
					hour = minute = second = 0;
				}
				
				centisec++;
				
				if (minute == TIMEBASE) {
					hour++;
					minute = 0;
				}
				if (second == TIMEBASE) {
					minute++;
					second = 0;
				}
				if (centisec == CENTSECBASE) {
					second++;
					centisec = 0;
				}
				if (event.getSource() == btnStart) {
					btnStart.setEnabled(false);
					btnStop.setEnabled(true);
					timer.start();
				} else if (event.getSource() == btnStop) {
					btnStart.setEnabled(true);
					btnStop.setEnabled(false);
					timer.stop();
				} else if (event.getSource() == btnReset) { 
					btnStart.setEnabled(true);
					btnStop.setEnabled(true);
					btnStart.requestFocus();
					timer.stop();
					hour = minute = second = 0;
					watch.setText("00:00:00");
				}
				
				watchSetText();
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (hour == TIMEBASE && minute == TIMEBASE && second == TIMEBASE) {
					hour = minute = second = 0;
				}
				
				centisec++;
				
				if (minute == TIMEBASE) {
					hour++;
					minute = 0;
				}
				if (second == TIMEBASE) {
					minute++;
					second = 0;
				}
				if (centisec == CENTSECBASE) {
					second++;
					centisec = 0;
				}
				if (e.getKeyChar() == KeyEvent.VK_ENTER && e.getSource() == btnStart) {
					btnStart.setEnabled(false);
					btnStop.setEnabled(true);
					timer.start();
				} else if (e.getKeyChar() == KeyEvent.VK_ENTER && e.getSource() == btnStop) {
					btnStart.setEnabled(true);
					btnStop.setEnabled(false);
					timer.stop();
				} else if (e.getKeyChar() == KeyEvent.VK_ENTER && e.getSource() == btnReset) { 
					btnStart.setEnabled(true);
					btnStop.setEnabled(true);
					btnStart.requestFocus();
					timer.stop();
					hour = minute = second = 0;
					watch.setText("00:00:00");
				}
				watchSetText();
			}

			/**
			 * Sets the watch's time
			 */
			public void watchSetText() {
				watch.setText(((hour < SHOWBASE) ? "0" : "") + hour + ":" + ((minute < SHOWBASE) ? "0" : "") + minute
						+ ":" + ((second < SHOWBASE) ? "0" : "") + second);
			}
		}
	}
}