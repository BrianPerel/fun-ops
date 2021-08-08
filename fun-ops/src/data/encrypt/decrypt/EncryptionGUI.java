package data.encrypt.decrypt;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;

/**
 * Encryption-decryption application. The idea is that the user can load a file,
 * encrypt it's contents, and hold an encrypted file. Then at any point can open
 * this modified file and decrypt it.
 * 
 * @author Brian Perel
 *
 */
public class EncryptionGUI implements ActionListener {

	private JFrame frame;
	private JTextField loadingTextField;
	JButton btnLoadFile = new JButton("Load file");
	JButton btnEncrypt = new JButton("Encrypt");
	JButton btnDecrypt = new JButton("Decrypt");
	JButton btnBrowse;
	EncryptDecrypt dataSet1;
	String data = "";
	static String fileName;
	boolean fileLoaded;
	static EncryptionGUI window;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					window = new EncryptionGUI();
					window.frame.setVisible(true);
					window.frame.setTitle("Encrypt-decrypt App by: Brian Perel");
					window.frame.setResizable(false);
					window.frame.setAlwaysOnTop(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @throws FileNotFoundException
	 */
	public EncryptionGUI() throws FileNotFoundException {

		frame = new JFrame();
		frame.setBounds(100, 100, 414, 264);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		ImageIcon image = new ImageIcon(getClass().getResource("mainImage.jpg"));
		JLabel backgroundLabel = new JLabel(image);
		frame.setContentPane(backgroundLabel);

		btnLoadFile.setBounds(41, 44, 89, 23);
		frame.getContentPane().add(btnLoadFile);
		btnLoadFile.addActionListener(this);
		btnLoadFile.setFocusable(false);
		btnLoadFile.setBackground(new Color(135, 206, 250));

		loadingTextField = new JTextField();
		loadingTextField.setBounds(157, 45, 86, 20);
		frame.getContentPane().add(loadingTextField);
		loadingTextField.setColumns(10);

		btnEncrypt.setBounds(80, 148, 89, 23);
		frame.getContentPane().add(btnEncrypt);
		btnEncrypt.addActionListener(this);
		btnEncrypt.setBackground(new Color(135, 206, 250));

		btnDecrypt.setBounds(231, 147, 89, 23);
		frame.getContentPane().add(btnDecrypt);

		JSeparator separator = new JSeparator();
		separator.setBounds(41, 105, 307, 2);
		frame.getContentPane().add(separator);
		btnDecrypt.addActionListener(this);
		btnDecrypt.setBackground(new Color(135, 206, 250));

		btnBrowse = new JButton("Browse");
		btnBrowse.setBounds(269, 44, 86, 23);
		frame.getContentPane().add(btnBrowse);
		btnBrowse.addActionListener(this);
		btnBrowse.setBackground(new Color(135, 206, 250));
	}

	/**
	 * JFileChooser browse menu
	 */
	public void fileBrowse() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(fileChooser);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			// Path to secret.txt file C:\Users\_____\git\fun-ops\fun-ops\secret.txt
			loadingTextField.setText(selectedFile.getName());
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		// if filename isn't empty or file hasn't yet been loaded
		if (ae.getSource() == btnLoadFile && !loadingTextField.getText().isEmpty() && !fileLoaded) {

			Scanner read = null;

			String fileToLoad = loadingTextField.getText();

			if (!loadingTextField.getText().contains(".txt")) {
				fileToLoad = loadingTextField.getText() + ".txt";
			}

			try {
				File f1 = new File(fileToLoad);
				
				read = new Scanner(f1);

				while (read.hasNextLine()) {
					data += read.nextLine();
				}

				JOptionPane.showMessageDialog(frame.getComponent(0), "File succesfully loaded");
				fileName = f1.toString();
				dataSet1 = new EncryptDecrypt(data);
				fileLoaded = true;

				// check if Desktop is supported by Platform or not
				if (!Desktop.isDesktopSupported()) {
					JOptionPane.showMessageDialog(frame.getComponent(0), "Desktop is not supported by this application",
							"Error", JOptionPane.ERROR_MESSAGE);
					read.close();
					return;
				}

				// check if this file exists
				if (f1.exists()) {
					Desktop.getDesktop().open(f1);
				}

			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(frame.getComponent(0), "File not found");
				read = new Scanner("");
				loadingTextField.setText("");
			} catch (IOException e) {
				e.printStackTrace();
			}

			read.close();

		}

		// if file load textfield is empty while load file btn is pushed
		else if (ae.getSource() == btnLoadFile && loadingTextField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "No file name entered", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		// if loaded file isn't blank, allow encryption op
		else if (ae.getSource() == btnEncrypt && !data.isBlank()) {
			window.frame.setAlwaysOnTop(false);

			try {
				dataSet1.encrypt();
			} catch (IOException e) {
				e.printStackTrace();
			}
			JOptionPane.showMessageDialog(frame.getComponent(0), "File succesfully encrypted");
		}

		// if loaded file isn't blank, allow decryption op
		else if (ae.getSource() == btnDecrypt && !data.isBlank()) {
			try {
				dataSet1.decrypt();
			} catch (IOException e) {
				e.printStackTrace();
			}
			JOptionPane.showMessageDialog(frame.getComponent(0), "File succesfully decrypted");
		}

		// if loaded file is blank or decrypt btn pushed
		else if (data.isBlank() && ae.getSource() == btnEncrypt && ae.getSource() == btnDecrypt) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "No file provided yet", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		// if file has been already been loaded and load file btn pushed
		else if (ae.getSource() == btnLoadFile && fileLoaded) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "A file has already been loaded", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		else if (ae.getSource() == btnEncrypt || ae.getSource() == btnDecrypt && !fileLoaded) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "No file loaded yet", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		else if (ae.getSource() == btnBrowse) {
			fileBrowse();
		}
	}
}