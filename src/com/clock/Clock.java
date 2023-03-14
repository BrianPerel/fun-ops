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
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
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

public class Clock implements ActionListener {

	private JFrame window;
	private String alarmTime;
	private JLabel lblClockTime;
	private JCheckBoxMenuItem menuOption;
	private boolean hasAlarmRung;

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
	 * Create the clock application. Places all the buttons on the app's board
	 * (initializes the contents of the frame), building the GUI.
	 */
	public Clock() {
		createGui();
	}

	private void createGui() {
		window = new JFrame("Clock");
		window.setSize(450, 280);
		window.setResizable(false);
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setContentPane(new JLabel(new ImageIcon("res/graphics/night-sky-stars-animation.gif"))); // sets app background

		window.getContentPane().setLayout(null);
		window.setAlwaysOnTop(true);

		Font custFont = new Font("Bookman Old Style", Font.PLAIN, 13);

		JMenu menu = new JMenu("Alarm ▼");
		menu.setFont(custFont);
		menu.setMnemonic('a'); // alt+a = alarm keyboard shortcut/keyboard mnemonic
		menu.setDisplayedMnemonicIndex(-1); // force program to not decorate (don't underline) mnemonic
		menu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		menu.setToolTipText("Sets an alarm time");
		menu.setBackground(new Color(0, 126, 210));
		menu.setForeground(Color.WHITE);
		menu.setOpaque(true);
		menu.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				menu.setBackground(new Color(31, 83, 162));
			}

			@Override
			public void mouseExited(MouseEvent e) {
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
		} catch (FontFormatException | IOException e) {
			font = custFont;
			e.printStackTrace();
		}

		lblClockTime.setFont(font);
		lblClockTime.setForeground(Color.GREEN);
		lblClockTime.setBounds(32, 23, 365, 123);
		window.getContentPane().add(lblClockTime);

		JCheckBox militaryTimeCheckBox = new JCheckBox("24 hour clock");
		militaryTimeCheckBox.setBounds(310, 172, 97, 25);
		militaryTimeCheckBox.setBackground(BLACK);
		militaryTimeCheckBox.setForeground(WHITE);
		window.getContentPane().add(militaryTimeCheckBox);

		// adding this code in case frame.getContentPane().setLayout(null); is removed
		militaryTimeCheckBox.setVerticalAlignment(SwingConstants.BOTTOM);
		militaryTimeCheckBox.setHorizontalAlignment(SwingConstants.RIGHT);
		militaryTimeCheckBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		window.setVisible(true);

		getTime(militaryTimeCheckBox);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		// sets clock alarm time
		if (ae.getSource().equals(menuOption)) {
			alarmTime = JOptionPane.showInputDialog(window, "Alarm time (AM/PM format): ");

			if (alarmTime != null && !alarmTime.isBlank()
					&& (alarmTime.trim().length() == 7 || alarmTime.trim().length() == 8)) {
				alarmTime = alarmTime.trim().toUpperCase();

				// index 0, 1, 3, and 4 of alarmTime string should be numbers only
				if (alarmTime.substring(0, 1).matches("\\d") && alarmTime.substring(3, 4).matches("\\d")
						&& (alarmTime.endsWith("AM") || alarmTime.endsWith("PM"))) {
					hasAlarmRung = false;
					JOptionPane.showMessageDialog(window, "Alarm time has been set", "Alarm time set",
							JOptionPane.INFORMATION_MESSAGE);

					return;
				}
			} else if (alarmTime != null) {
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(window, "Please enter time of the appropriate format (x:xx AM or PM)",
						"Error setting alarm", JOptionPane.INFORMATION_MESSAGE);
			}

			menuOption.setSelected(false); // removes check mark from the app's menu option
		}
	}

	/**
	 * Obtains the current time, applies formatting if needed, listens for if user
	 * enters an alarm time, and repeats these actions every second
	 *
	 * @param militaryTimeFormatCheckBox the display military time JCheckBox
	 */
	private void getTime(JCheckBox militaryTimeFormatCheckBox) {

		String time = "";

		while (true) {
			time = obtainFormattedTime(militaryTimeFormatCheckBox);

			if (alarmTime != null && !hasAlarmRung && (time.equalsIgnoreCase(alarmTime))) {
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
	 * Changes the time to be displayed to 24 hr format if user selects the option
	 *
	 * @param militaryTimeFormatCheckBox check box to request 24 hr format time
	 * @return the formatted current time
	 */
	private String obtainFormattedTime(JCheckBox militaryTimeFormatCheckBox) {
		String time = java.time.LocalDateTime.now()
				.format(DateTimeFormatter.ofPattern((militaryTimeFormatCheckBox.isSelected()) ? "HH:mm" : "hh:mm a"));

		// current time (not 11 or 12 o'clock) will show up with a 0 in front of the
		// time by default, this prevents that
		if (!militaryTimeFormatCheckBox.isSelected() && time.startsWith("0")) {
			time = time.substring(1);
		}

		lblClockTime.setText(time);
		return time;
	}

	/**
	 * Rings the alarm if conditions are met. Plays alarm wav file
	 */
	private void ringAlarm() {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File("res/audio/clock-alarm.wav")));
			menuOption.setSelected(false); // removes check mark from the app menu option
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
