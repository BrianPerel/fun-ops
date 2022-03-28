package clock;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
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
	private boolean alarmShouldRing = true;
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		new Clock();
	}

	/**
	 * Create the clock application. Places all the buttons on the app's board and initializes the contents of the frame, building the gui.
	 */
	public Clock() {
		JFrame frame = new JFrame("Clock");
		frame.setBounds(100, 100, 450, 280);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(BLACK);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);
		
		Font custFont = new Font("Bookman Old Style", Font.PLAIN, 13);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Alarm");
		menu.setFont(custFont);
		menu.setMnemonic('a'); // alt+a = alarm keyboard shortcut/ keyboard mnemonic
	
		menuOption = new JMenuItem("Set Alarm Time");
		menuOption.setFont(custFont);
	
		menu.add(menuOption);
		menuBar.add(menu);
		frame.setJMenuBar(menuBar);
		
		menuOption.addActionListener(this);

		lblClockTime = new JLabel();
		lblClockTime.setForeground(WHITE);
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
		lblClockTime.setForeground(Color.GREEN);
		lblClockTime.setBounds(37, 0, 365, 197);
		frame.getContentPane().add(lblClockTime);
		
		JCheckBox militaryTimeFormatCheckBox = new JCheckBox("24 hour time");
		militaryTimeFormatCheckBox.setBounds(310, 172, 97, 25);
		militaryTimeFormatCheckBox.setBackground(BLACK);
		militaryTimeFormatCheckBox.setForeground(WHITE);
		frame.getContentPane().add(militaryTimeFormatCheckBox);		
				
		getTime(militaryTimeFormatCheckBox);
	}

	/**
	 * Obtains the current time, applies formatting if needed, listens for if user enters an alarm time, and repeats these actions every second
	 */
	public void getTime(JCheckBox militaryTimeFormatCheckBox) {
		
		String time;

		while (true) {			
			time = java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern((militaryTimeFormatCheckBox.isSelected()) 
					? "HH:mm" : "hh:mm a"));
			
			// current time (not 11 or 12 o'clock) will show up with a 0 in front of the time by default, this prevents that
			if (!militaryTimeFormatCheckBox.isSelected() && time.startsWith("0")) {
				time = time.substring(1, time.length());
			}	
							
			// if alarm time set is triggered, play wav file
			if (alarmShouldRing && time.equalsIgnoreCase(alarmTime)) {
				try {
					Clip clip = AudioSystem.getClip();
					clip.open(AudioSystem.getAudioInputStream(new File("res/audio/clock-alarm.wav")));
					clip.start();
					TimeUnit.SECONDS.sleep(3);
					clip.stop();
					alarmShouldRing = false;
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e1) {
					Thread.currentThread().interrupt();
					e1.printStackTrace();
				}
			}

			lblClockTime.setText(time);
			
			// updates the time every second
			try {
				Thread.sleep(1000); 
			} catch (InterruptedException ie) {
				ie.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == menuOption)  {  
				alarmTime = JOptionPane.showInputDialog("Alarm time: ");
				// first 2 and 4 index of alarmTime string should be numbers only
				if(alarmTime != null) {
					if(alarmTime.substring(0, 1).matches("[0-9]+") && alarmTime.substring(3, 4).matches("[0-9]+")
							&& alarmTime.substring(2, 3).equals(":") && (alarmTime.toUpperCase().contains("AM") || alarmTime.toUpperCase().contains("PM"))) {
						alarmShouldRing = true;
						alarmTime = alarmTime.trim();
						JOptionPane.showMessageDialog(null, "Alarm time has been set", "Alarm time set", JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "Alarm time could not be set. Please enter time of the appropriate format (x:xx AM or PM)", "Alarm time set", JOptionPane.INFORMATION_MESSAGE);
					}
				}
		}		
	}
}
