package clock;

import java.awt.Color;
import java.awt.Font;
import java.time.format.DateTimeFormatter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Clock {

	JLabel lblClockTime;
	String time;

	public static void main(String[] args) {
		new Clock();
	}

	/**
	 * Create the application.
	 */
	public Clock() {
		JFrame frame = new JFrame();
		frame.setTitle("My Clock");
		frame.setBounds(100, 100, 450, 300);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(Color.BLACK);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);

		lblClockTime = new JLabel();
		lblClockTime.setForeground(Color.WHITE);
		lblClockTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblClockTime.setFont(new Font("Euphemia", Font.PLAIN, 80));
		lblClockTime.setBounds(37, 32, 365, 197);
		frame.add(lblClockTime);

		getTime();
	}

	public void getTime() {

		while (true) {
			time = java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
			if(time.substring(0, 1).equals("0")) {
				time = time.substring(1, time.length());
			}
			
			lblClockTime.setText(time);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println(e.toString());
			}
		}
	}
}
