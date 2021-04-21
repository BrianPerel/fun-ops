package data.encrypt.decrypt;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JFrame;

public class App {

	private JFrame frame;

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
		String data = "";
		
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		try {
			File f1 = new File("secret.txt");
			Scanner read = new Scanner(f1);

			while (read.hasNextLine()) {
				data = read.nextLine();
			}

			read.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
		}

		EncryptDecrypt dataSet1 = new EncryptDecrypt(data);
		System.out.println("Data in original form: " + dataSet1);
		dataSet1.encrypt();
		System.out.println("Encrypted data: " + dataSet1);
		dataSet1.decrypt();
		System.out.println("Decrypted data: " + dataSet1);
	}

}
