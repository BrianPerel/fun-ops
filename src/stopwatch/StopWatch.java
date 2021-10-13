package stopwatch;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		/** represent the minute in the watch. */
		private int hour, minute, second, centisec;
		/** btns - start, stop, reset */
		public static JButton btnStart, btnStop, btnReset;
		/** Show the timer. */
		private JLabel watch;
		/** Timer to be used for watch. */
		private Timer timer;
		/** Timer delay. */
		private int delay = 10;
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
			add(watchPanel, BorderLayout.NORTH);
			JPanel buttonPanel = new JPanel();
			btnStart = new JButton("start");
			btnStop = new JButton("stop");
			btnReset = new JButton("reset");
			btnStart.addActionListener(b);
			btnStop.addActionListener(b);
			btnReset.addActionListener(b);
			buttonPanel.add(btnStart);
			buttonPanel.add(btnStop);
			buttonPanel.add(btnReset);
			add(buttonPanel, BorderLayout.CENTER);
			timer = new Timer(delay, b);
		}

		/**
		 * Represents a listener for button push (action) events.
		 */
		public class ButtonListener implements ActionListener {
			/**
			 * Updates the watch label when button is pushed.
			 * 
			 * @param event Indicates a button is pushed
			 */
			public void actionPerformed(ActionEvent event) {
				final int timebase = 60;
				final int centsecbase = 99;
				final int showbase = 10;
				if (hour == timebase && minute == timebase && second == timebase && centisec == centsecbase) {
					hour = minute = second = centisec = 0;
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
				} else { // if event.getSource() == btnReset
					if (!timer.isRunning()) {
						timer.stop();
						hour = minute = second = centisec = 0;
						watch.setText("00:00:00");
					}
				}
				watch.setText(((hour < showbase) ? "0" : "") + hour + ":" + ((minute < showbase) ? "0" : "") + minute
						+ ":" + ((second < showbase) ? "0" : "") + second);
			}
		}
	}

	public static void main(String[] args) {
		new StopWatch(300, 160);
	}
}