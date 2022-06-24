package com.clock;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

public class Clock implements ActionListener {

	private JLabel lblClockTime;
	private String alarmTime;
	private JMenuItem menuOption;
	private boolean hasAlarmRung;
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			System.out.println("Error ClassNotFoundException" + e.getMessage());
		}
		
		new Clock();
	}

	/**
	 * Create the clock application. Places all the buttons on the app's board and initializes the contents of the frame, building the gui.
	 */
	public Clock() {
		JFrame frame = new JFrame("Clock");
		frame.setSize(450, 280);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(BLACK);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);
		
		Font custFont = new Font("Bookman Old Style", Font.PLAIN, 14);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Alarm");
		menu.setFont(custFont);
		menu.setMnemonic('a'); // alt+a = alarm keyboard shortcut/keyboard mnemonic
		menu.setDisplayedMnemonicIndex(-1); // force program to not decorate (don't underline) mnemonic 
		menu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		menu.setToolTipText("Sets an alarm time");
		
		menuOption = new JMenuItem("Set Alarm Time");
		menuOption.setFont(custFont);
		menuOption.addActionListener(this);
		menuOption.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	
		menu.add(menuOption);
		menuBar.add(menu);
		frame.setJMenuBar(menuBar);
		
		lblClockTime = new JLabel();
		lblClockTime.setForeground(WHITE);
		lblClockTime.setHorizontalAlignment(SwingConstants.CENTER);

		Font font;

		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("res/fonts/clock-font.ttf")).deriveFont(Font.BOLD, 110);
		} catch (FontFormatException | IOException e) {
			font = custFont;
			e.printStackTrace();
		}

		lblClockTime.setFont(font);
		lblClockTime.setForeground(Color.GREEN);
		lblClockTime.setBounds(32, 23, 365, 123);
		frame.getContentPane().add(lblClockTime);
		
		JCheckBox militaryTimeFormatCheckBox = new JCheckBox("24 hour time");
		militaryTimeFormatCheckBox.setBounds(310, 172, 97, 25);
		militaryTimeFormatCheckBox.setBackground(BLACK);
		militaryTimeFormatCheckBox.setForeground(WHITE);
		frame.getContentPane().add(militaryTimeFormatCheckBox);		
		
		// adding this code in case frame.getContentPane().setLayout(null); removed
		militaryTimeFormatCheckBox.setVerticalAlignment(SwingConstants.BOTTOM);
		militaryTimeFormatCheckBox.setHorizontalAlignment(SwingConstants.RIGHT);
		militaryTimeFormatCheckBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				
		getTime(militaryTimeFormatCheckBox);
	}

	/**
	 * Obtains the current time, applies formatting if needed, listens for if user enters an alarm time, and repeats these actions every second
	 * @param militaryTimeFormatCheckBox the display military time JCheckBox 
	 */
	public void getTime(JCheckBox militaryTimeFormatCheckBox) {
		
		String time = "";

		while (true) {			
			time = obtainTime(militaryTimeFormatCheckBox);

			if(alarmTime != null && !hasAlarmRung && (time.equalsIgnoreCase(alarmTime))) {
				ringAlarm(time);
			}

			// updates the time every second
			try {
				TimeUnit.SECONDS.sleep(1L);
			} catch (final InterruptedException ie) {
				ie.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}

	public String obtainTime(JCheckBox militaryTimeFormatCheckBox) {
		String time = java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern((militaryTimeFormatCheckBox.isSelected()) 
				? "HH:mm" : "hh:mm a"));
		
		// current time (not 11 or 12 o'clock) will show up with a 0 in front of the time by default, this prevents that
		if (!militaryTimeFormatCheckBox.isSelected() && time.startsWith("0")) {
			time = time.substring(1);
		}	
		
		lblClockTime.setText(time);
		return time;
	}

	/**
	 * Rings the alarm if conditions are met
	 * @param time the set alarm time
	 */
	public void ringAlarm(String time) {
		// play alarm wav file
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File("res/audio/clock-alarm.wav")));
			clip.start();
			TimeUnit.SECONDS.sleep(3L);
			clip.stop();
			hasAlarmRung = true;
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e1) {
			Thread.currentThread().interrupt();
			e1.printStackTrace();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == menuOption)  {  
			alarmTime = JOptionPane.showInputDialog("Alarm time (in AM/PM format): ");
						
			// index 0, 1, 3, and 4 of alarmTime string should be numbers only
			if(alarmTime != null && !alarmTime.isBlank() && alarmTime.length() == 8) {
				alarmTime = alarmTime.trim();

				if(alarmTime.substring(0, 1).matches("[0-9]+") && alarmTime.substring(3, 4).matches("[0-9]+")
						&& (alarmTime.toUpperCase().endsWith("AM") || alarmTime.toUpperCase().endsWith("PM"))) {
					hasAlarmRung = false;
					JOptionPane.showMessageDialog(null, "Alarm time has been set", "Alarm time set", JOptionPane.INFORMATION_MESSAGE);
				} 	
				else {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(null, "Alarm time could not be set. Please enter time of the appropriate format (x:xx AM or PM)", "Alarm time set", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			else {
				Toolkit.getDefaultToolkit().beep();
			}
		}		
	}
}
