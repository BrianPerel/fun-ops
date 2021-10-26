package stopwatch;

import java.awt.BorderLayout;
import java.awt.Color;
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

/**
 * A Java application that works as a stop-watch in the format 00:00:00
 * (hh:mm:ss) Classes are nested to support multiple inheritance
 */
@SuppressWarnings("serial")
public class StopWatch extends JFrame {
	
	public static void main(String[] args) {
		// default requested window measurements (width x height)
		new StopWatch(300, 150);
	}

	/**
	 * Creates the GUI frame (box)
	 */	
	public StopWatch(int x, int y) {
		super("Brian Perel - Stopwatch");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
			
			final int timebase = 60, centsecbase = 99, showbase = 10;

			/**
			 * Updates the watch label when button is pushed.
			 * 
			 * @param event Indicates a button is pushed
			 */
			public void actionPerformed(ActionEvent event) {

				if (hour == timebase && minute == timebase && second == timebase) {
					hour = minute = second = 0;
				}
				centisec++;
				if (minute == timebase) {
					hour++;
					minute = 0;
				}
				if (second == timebase) {
					minute++;
					second = 0;
				}
				if (centisec == centsecbase) {
					second++;
					centisec = 0;
				}
				if (event.getSource() == btnStart) {
					timer.start();
				} else if (event.getSource() == btnStop) {
					centisec--;
					timer.stop();
				} else if (event.getSource() == btnReset) { 
					timer.stop();
					hour = minute = second = 0;
					watch.setText("00:00:00");
				}
				watch.setText(((hour < showbase) ? "0" : "") + hour + ":" + ((minute < showbase) ? "0" : "") + minute
						+ ":" + ((second < showbase) ? "0" : "") + second);
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (hour == timebase && minute == timebase && second == timebase) {
					hour = minute = second = 0;
				}
				centisec++;
				if (minute == timebase) {
					hour++;
					minute = 0;
				}
				if (second == timebase) {
					minute++;
					second = 0;
				}
				if (centisec == centsecbase) {
					second++;
					centisec = 0;
				}
				if (e.getKeyChar() == KeyEvent.VK_ENTER && e.getSource() == btnStart) {
						timer.start();
				} else if (e.getKeyChar() == KeyEvent.VK_ENTER && e.getSource() == btnStop) {
					centisec--;
					timer.stop();
				} else if (e.getKeyChar() == KeyEvent.VK_ENTER && e.getSource() == btnReset) { 
					timer.stop();
					hour = minute = second = 0;
					watch.setText("00:00:00");
				}
				watch.setText(((hour < showbase) ? "0" : "") + hour + ":" + ((minute < showbase) ? "0" : "") + minute
						+ ":" + ((second < showbase) ? "0" : "") + second);
			}
		}
	}
}