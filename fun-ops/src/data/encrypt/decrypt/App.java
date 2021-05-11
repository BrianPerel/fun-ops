package data.encrypt.decrypt;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;

/**
 * Encryption-decryption application
 * @author Brian Perel
 *
 */
public class App implements ActionListener {

	private JFrame frame;
	private JTextField loadingTextField;
	JButton btnLoadFile = new JButton("Load file");
	JButton btnEncrypt = new JButton("Encrypt");
	JButton btnDecrypt = new JButton("Decrypt");
	EncryptDecrypt dataSet1;
	String data = "";	
	static String fileName;
	boolean fileLoaded;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					App window = new App();
					window.frame.setVisible(true);
					window.frame.setTitle("Encrypt-decrypt App by: Brian Perel");
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
	 * 
	 * @throws FileNotFoundException
	 */
	public App() throws FileNotFoundException {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 412, 272);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		btnLoadFile.setBounds(80, 57, 89, 23);
		frame.getContentPane().add(btnLoadFile);
		btnLoadFile.addActionListener(this);
		btnLoadFile.setFocusable(false);
		
		loadingTextField = new JTextField();
		loadingTextField.setBounds(234, 57, 86, 20);
		frame.getContentPane().add(loadingTextField);
		loadingTextField.setColumns(10);
		
		btnEncrypt.setBounds(80, 132, 89, 23);
		frame.getContentPane().add(btnEncrypt);
		btnEncrypt.addActionListener(this);
		
		btnDecrypt.setBounds(231, 131, 89, 23);
		frame.getContentPane().add(btnDecrypt);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(41, 105, 307, 2);
		frame.getContentPane().add(separator);
		btnDecrypt.addActionListener(this);	
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {		
				
		// if filename isn't empty or file hasn't yet been loaded
		if(ae.getSource() == btnLoadFile && !loadingTextField.getText().isEmpty() && !fileLoaded) {
			
			Scanner read = null;
			
			try {
				File f1 = new File(loadingTextField.getText());
				read = new Scanner(f1);

				while (read.hasNextLine()) {
					data = read.nextLine();
				}
				
				JOptionPane.showMessageDialog(frame.getComponent(0), "File succesfully loaded");
				fileName = f1.toString();
				dataSet1 = new EncryptDecrypt(data);
				fileLoaded = true;
				
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(frame.getComponent(0), "File not found");
				loadingTextField.setText("");
			}
			
			read.close();

		} 
		
		// if file load textfield is empty while load file btn is pushed
		else if(ae.getSource() == btnLoadFile && loadingTextField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "No file name entered");
		}
		
		// if loaded file isn't blank, allow encryption op  
		else if(ae.getSource() == btnEncrypt && !data.isBlank()) {
			try {
				dataSet1.encrypt();
			} catch (IOException e) {
				e.printStackTrace();
			}
			JOptionPane.showMessageDialog(frame.getComponent(0), "File succesfully encrypted");
		}
		
		// if loaded file isn't blank, allow decryption op 
		else if(ae.getSource() == btnDecrypt && !data.isBlank()) {
			try {
				dataSet1.decrypt();
			} catch (IOException e) {
				e.printStackTrace();
			}
			JOptionPane.showMessageDialog(frame.getComponent(0), "File succesfully decrypted");
		}
		
		// if loaded file is blank or decrypt btn pushed 
		else if(data.isBlank() && ae.getSource() == btnEncrypt && ae.getSource() == btnDecrypt) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "No file provided yet");
		}

		// if file has been already been loaded and load file btn pushed 
		else if(ae.getSource() == btnLoadFile && fileLoaded) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "A file has already been loaded");
		}
	}
}
