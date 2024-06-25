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
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

/**
 * Clock application with an alarm feature. By default the time is displayed in a 12-hour time format, but the user
 * has the option to switch to and from a 24-hour time format<br>
 *
 * @author Brian Perel
 *
 */
public class Clock implements ActionListener {

	private JFrame window;
	private JLabel lblClockTime;
	private String alarmTimeString;
	private boolean hasAlarmRung;
	private JCheckBoxMenuItem menuOption;
	private JCheckBox militaryTimeCheckBox;
	private Optional<String> alarmTime = Optional.ofNullable(null);

	public static void main(String[] args) throws UnsupportedLookAndFeelException {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			throw new UnsupportedLookAndFeelException("Failed to set LookAndFeel\n" + e.getMessage());
		}

		new Clock();
	}

	/**
	 * Creates the clock application
	 */
	public Clock() {
		createGui();
		window.setVisible(true);

		getTime(militaryTimeCheckBox);
	}

	private void createGui() {
		window = new JFrame("Clock");
		window.setSize(450, 280);
		window.setResizable(false);
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setContentPane(new JLabel(new ImageIcon("res/graphics/night-sky-stars-animation.gif"))); // sets app background

	    // changes the program's taskbar icon
	    window.setIconImage(new ImageIcon("res/graphics/taskbar_icons/clock.png").getImage());

		window.getContentPane().setLayout(null);
		window.setAlwaysOnTop(true);

		Font custFont = new Font("Bookman Old Style", Font.PLAIN, 13);

		JMenu menu = new JMenu("Alarm ▼");
		menu.setFont(custFont);
		menu.setMnemonic('a'); // alt+a = alarm keyboard shortcut/keyboard mnemonic
		menu.setDisplayedMnemonicIndex(-1); // force program to not decorate (don't underline) mnemonic
		menu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		menu.setToolTipText("Sets an alarm time");

		// default menu blue color
		menu.setBackground(new Color(0, 126, 210));
		menu.setForeground(Color.WHITE);
		menu.setOpaque(true);
		menu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				// dark blue menu hover color
				menu.setBackground(new Color(31, 83, 162));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// default menu button color
				menu.setBackground(new Color(0, 126, 210));
			}
		});

		menuOption = new JCheckBoxMenuItem("Set Alarm Time");
		menuOption.setFont(custFont);
		menuOption.setIcon(new ImageIcon("res/graphics/alarm-clock-sign.png"));
		menuOption.addActionListener(this);
		menuOption.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		menuOption.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.ALT_DOWN_MASK));

		menu.add(menuOption);

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(menu);
		window.setJMenuBar(menuBar);

		lblClockTime = new JLabel();
		lblClockTime.setForeground(WHITE);
		lblClockTime.setHorizontalAlignment(SwingConstants.CENTER);

		Font font;

		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("res/fonts/clock-font.ttf")).deriveFont(Font.BOLD, 110);
			lblClockTime.setForeground(Color.GREEN);
		} catch (FontFormatException | IOException e) {
			lblClockTime.setForeground(Color.BLACK);
			font = new Font("Bookman Old Style", Font.PLAIN, 70);
			e.printStackTrace();
		}

		lblClockTime.setFont(font);
		lblClockTime.setBounds(32, 23, 365, 123);
		window.getContentPane().add(lblClockTime);

		militaryTimeCheckBox = new JCheckBox("24-hour clock");
		militaryTimeCheckBox.setBounds(310, 172, 97, 25);
		militaryTimeCheckBox.setBackground(BLACK);
		militaryTimeCheckBox.setForeground(WHITE);
		window.getContentPane().add(militaryTimeCheckBox);

		// adding this code in case frame.getContentPane().setLayout(null); is removed
		militaryTimeCheckBox.setVerticalAlignment(SwingConstants.BOTTOM);
		militaryTimeCheckBox.setHorizontalAlignment(SwingConstants.RIGHT);
		militaryTimeCheckBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		// sets clock alarm time
		if (ae.getSource().equals(menuOption)) {
			alarmTime = Optional.ofNullable(JOptionPane.showInputDialog(window, "Alarm time (AM/PM format): ", "Set alarm", JOptionPane.PLAIN_MESSAGE));

			// (?i) makes the replaceAll() check for the text while being case-insensitive
			// removes optional, [, ] text/symbols
			alarmTimeString = alarmTime.toString().replaceAll("(?i)optional|\\[|\\]", "").trim().toUpperCase();

			if (alarmTime.isPresent() && !alarmTimeString.isBlank()
					&& (alarmTimeString.length() == 7 || alarmTimeString.length() == 8)) {
				alarmTime = Optional.ofNullable(alarmTimeString);

				// indices 0, 1, 3, and 4 of alarmTime string should be only numbers followed by a single space, and the string must end in 'am' or 'pm' (case insensitive)
				if (alarmTimeString.matches("^\\d.*\\d [AP]M$")) {
					hasAlarmRung = false;
					JOptionPane.showMessageDialog(window, "Alarm time has been set", "Alarm time set",
							JOptionPane.INFORMATION_MESSAGE);

					return;
				}
			}
			else if (!".EMPTY".equals(alarmTimeString)) {
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(window, "Please enter time of the appropriate format (X:XX AM or PM)",
						"Error setting alarm", JOptionPane.INFORMATION_MESSAGE);
			}

			menuOption.setSelected(false); // removes check mark from the app's menu option
		}
	}

	/**
	 * Continuously obtains the current time every second, applies formatting if needed, and listens for user
	 * alarm time input
	 *
	 * @param argMilitaryTimeFormatCheckBox The checkbox indicating whether to display time in the military (24-hour) format.
	 *                                      If selected, time will be displayed in 24-hour format; otherwise in 12-hour format.
	 */
	private void getTime(JCheckBox argMilitaryTimeFormatCheckBox) {
		while (true) {
			createFormattedTime(argMilitaryTimeFormatCheckBox);

			String currentUnformattedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));

			// since the user set alarm time should be only in am/pm time, perform comparison of the time using unformatted (am/pm) time
			if (alarmTime.isPresent() && !hasAlarmRung && currentUnformattedTime.equalsIgnoreCase(alarmTimeString)) {
				ringAlarm();
			}

			// updates the time every second
			try {
				TimeUnit.SECONDS.sleep(1L);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}

	/**
	 * Formats the clock's current time being displayed to avoid having a zero appear before single digit hour.
	 * Changes the time to be displayed to 24-hour format (if user selects the option)
	 *
	 * @param argMilitaryTimeFormatCheckBox check box to request 24-hour format time
	 */
	private void createFormattedTime(JCheckBox argMilitaryTimeFormatCheckBox) {
		String time = LocalDateTime.now()
				.format(DateTimeFormatter.ofPattern((argMilitaryTimeFormatCheckBox.isSelected()) ? "HH:mm" : "hh:mm a"));

		// current time (not 11 or 12 o'clock) will show up with a 0 in front of the
		// time by default, this prevents that
		if (!argMilitaryTimeFormatCheckBox.isSelected() && time.startsWith("0")) {
			time = time.substring(1); // removes leading zero
		}

		lblClockTime.setText(time);
	}

	/**
	 * Rings the alarm if conditions are met and plays the alarm wav file for a fixed duration of 3 seconds,
	 * using the Java Sound API (AudioSystem)
	 *
	 * @throws UnsupportedAudioFileException If the audio file format is not supported
	 * @throws IOException If an I/O error occurs while reading the audio file
	 * @throws LineUnavailableException If a line cannot be opened because it is unavailable
	 * @throws InterruptedException If the thread is interrupted during the alarm sound duration
	 */
	private void ringAlarm() {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File("res/audio/clock-alarm.wav")));
			menuOption.setSelected(false); // deselects the corresponding menu option to remove the check mark
			clip.start();
			TimeUnit.SECONDS.sleep(3L);
			clip.stop();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
			e1.printStackTrace();
		} catch (InterruptedException ie) {
			Thread.currentThread().interrupt();
			ie.printStackTrace();
		}

		hasAlarmRung = true;
	}

}
