package currency.converter;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class CurrencyConverter {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					CurrencyConverter window = new CurrencyConverter();
					window.frame.setVisible(true);
					window.frame.setTitle("Currency Converter");
					window.frame.setResizable(false);
					window.frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CurrencyConverter() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 555, 323);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		ImageIcon image = new ImageIcon(getClass().getResource("money.jpg"));
		JLabel backgroundLabel = new JLabel(image);
		frame.setContentPane(backgroundLabel);

		JLabel lblNewLabel = new JLabel(" Convert this amount:");
		lblNewLabel.setBounds(32, 53, 131, 34);
		frame.getContentPane().add(lblNewLabel);
		lblNewLabel.setOpaque(true);

		JLabel lblFromThisCurrency = new JLabel(" From this currency:");
		lblFromThisCurrency.setBounds(182, 52, 131, 35);
		frame.getContentPane().add(lblFromThisCurrency);
		lblFromThisCurrency.setOpaque(true);

		JLabel lblToThisCurrency = new JLabel(" To this currency:");
		lblToThisCurrency.setBounds(363, 52, 131, 34);
		frame.getContentPane().add(lblToThisCurrency);
		lblToThisCurrency.setOpaque(true);

		textField = new JTextField();
		textField.setText("$");
		textField.setBounds(32, 94, 117, 25);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		String[] choices = { "US Dollar (USD)", "", "", "", "" };
		JComboBox comboBox = new JComboBox(choices);
		comboBox.setBounds(182, 92, 159, 27);
		frame.getContentPane().add(comboBox);

		String[] choices2 = { "Australian Dollar (AUS)", "", "", "", "" };
		JComboBox comboBox_1 = new JComboBox(choices2);
		comboBox_1.setBounds(363, 92, 159, 27);
		frame.getContentPane().add(comboBox_1);

		JButton btnNewButton = new JButton("Convert");
		btnNewButton.setBounds(270, 219, 106, 45);
		frame.getContentPane().add(btnNewButton);

		JButton btnClose = new JButton("Close");
		btnClose.setBounds(386, 219, 106, 45);
		frame.getContentPane().add(btnClose);

		JLabel lblNewLabel_1 = new JLabel(" Result:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1.setBounds(206, 154, 60, 29);
		frame.getContentPane().add(lblNewLabel_1);
		lblNewLabel_1.setOpaque(true);

		textField_1 = new JTextField();
		textField_1.setBounds(270, 154, 106, 29);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Currency Converter");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_2.setBounds(25, 11, 196, 34);
		frame.getContentPane().add(lblNewLabel_2);
		lblNewLabel_2.setForeground(Color.blue);

	}
}
