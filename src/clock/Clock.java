package clock;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
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
		frame.setBounds(100, 100, 450, 280);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(Color.BLACK);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);

		lblClockTime = new JLabel();
		lblClockTime.setForeground(Color.WHITE);
		lblClockTime.setHorizontalAlignment(SwingConstants.CENTER);
		
		Font font = null;
		
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("res/clock-font.ttf"));
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(font != null) {
			font = font.deriveFont(Font.BOLD, 110);
		}
		
		lblClockTime.setFont(font); 
		lblClockTime.setForeground(Color.green);
		lblClockTime.setBounds(37, 0, 365, 197);
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
				Thread.sleep(1000); // updates the time every second
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println(e.toString());
			}
		}
	}
}
