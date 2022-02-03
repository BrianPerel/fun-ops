package clock;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Clock {

	private JLabel lblClockTime;

	public static void main(String[] args) {
		new Clock();
	}

	/**
	 * Create the clock application
	 */
	public Clock() {
		JFrame frame = new JFrame();
		frame.setTitle("Clock");
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
			font = Font.createFont(Font.TRUETYPE_FONT, new File("res/fonts/clock-font.ttf"));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}

		if (font != null) {
			font = font.deriveFont(Font.BOLD, 110);
		}

		lblClockTime.setFont(font);
		lblClockTime.setForeground(Color.green);
		lblClockTime.setBounds(37, 0, 365, 197);
		frame.getContentPane().add(lblClockTime);
		
		JCheckBox militaryTimeFormatCheckBox = new JCheckBox("24 hour time");
		militaryTimeFormatCheckBox.setBounds(310, 178, 97, 23);
		militaryTimeFormatCheckBox.setBackground(Color.BLACK);
		militaryTimeFormatCheckBox.setForeground(Color.WHITE);
		frame.getContentPane().add(militaryTimeFormatCheckBox);
		
		getTime(militaryTimeFormatCheckBox);
	}

	/**
	 * Obtains the current time, applies formatting if needed, and repeats action every second
	 */
	public void getTime(JCheckBox militaryTimeFormatCheckBox) {
		
		String time;

		while (true) {			
			if(militaryTimeFormatCheckBox.isSelected()) {
				time = java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
			} 
			else {				
				time = java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
				
				// current time (not 11 or 12 o'clock) will show up with a 0 in front of the time by default, this prevents that
				if (time.substring(0, 1).equals("0")) {
					time = time.substring(1, time.length());
				}	
			}
			
			lblClockTime.setText(time);
			
			// updates the time every second
			try {
				Thread.sleep(1000); 
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}
}
