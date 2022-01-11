package encryption;

import java.awt.Color;
import java.awt.Desktop;
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
public class EncryptionGui implements ActionListener {

	private String data = "";
	private JButton btnBrowse;
	private boolean isFileLoaded;
	private JFrame frame;
	private static String fileName;
	private EncryptDecrypt dataSet;
	private static EncryptionGui window;
	private JTextField loadingTextField;
	private JButton btnEncrypt = new JButton("Encrypt");
	private JButton btnDecrypt = new JButton("Decrypt");
	private JButton btnLoadFile = new JButton("Load file");

	public static void main(String[] args) {	
		try {
			window = new EncryptionGui();
			window.frame.setVisible(true);
			window.frame.setTitle("Encrypt-decrypt App by: Brian Perel");
			window.frame.setResizable(false);
			window.frame.setLocationRelativeTo(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the application.
	 * @throws FileNotFoundException thrown if file not found
	 */
	public EncryptionGui() throws FileNotFoundException {
		frame = new JFrame();
		frame.setBounds(100, 100, 421, 264);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		frame.setContentPane(new JLabel(new ImageIcon("res/graphics/bg-image-encryption.jpg")));

		btnLoadFile.setBounds(49, 43, 89, 23);
		frame.getContentPane().add(btnLoadFile);
		btnLoadFile.addActionListener(this);
		btnLoadFile.setFocusable(false);
		btnLoadFile.setBackground(new Color(135, 206, 250));
		btnLoadFile.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnLoadFile.setBackground(new Color(102, 178, 255));
		    }
			
			@Override
		    public void mouseExited(java.awt.event.MouseEvent evt) {
				btnLoadFile.setBackground(new Color(135, 206, 250));
		    }
		});

		loadingTextField = new JTextField();
		loadingTextField.setBounds(148, 44, 112, 22);
		frame.getContentPane().add(loadingTextField);
		loadingTextField.setColumns(10);

		btnEncrypt.setBounds(84, 140, 89, 23);
		frame.getContentPane().add(btnEncrypt);
		btnEncrypt.addActionListener(this);
		btnEncrypt.setBackground(new Color(135, 206, 250));
		btnEncrypt.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnEncrypt.setBackground(new Color(102, 178, 255));
		    }
			
			@Override
		    public void mouseExited(java.awt.event.MouseEvent evt) {
				btnEncrypt.setBackground(new Color(135, 206, 250));
		    }
		});

		btnDecrypt.setBounds(235, 139, 89, 23);
		frame.getContentPane().add(btnDecrypt);

		JSeparator separator = new JSeparator();
		separator.setBounds(49, 104, 307, 2);
		frame.getContentPane().add(separator);
		btnDecrypt.addActionListener(this);
		btnDecrypt.setBackground(new Color(135, 206, 250));
		btnDecrypt.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnDecrypt.setBackground(new Color(102, 178, 255));
		    }
			
			@Override
		    public void mouseExited(java.awt.event.MouseEvent evt) {
				btnDecrypt.setBackground(new Color(135, 206, 250));
		    }
		});

		btnBrowse = new JButton("Browse");
		btnBrowse.setBounds(270, 43, 86, 23);
		frame.getContentPane().add(btnBrowse);
		btnBrowse.addActionListener(this);
		btnBrowse.setBackground(new Color(135, 206, 250));
		btnBrowse.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnBrowse.setBackground(new Color(102, 178, 255));
		    }
			
			@Override
		    public void mouseExited(java.awt.event.MouseEvent evt) {
				btnBrowse.setBackground(new Color(135, 206, 250));
		    }
		});
	}

	/**
	 * JFileChooser file browse menu for GUI
	 */
	public void fileBrowse() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

		if (fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
			loadingTextField.setText(fileChooser.getSelectedFile().getName());
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		// if filename isn't empty or file hasn't yet been loaded
		if (ae.getSource() == btnLoadFile && !loadingTextField.getText().isEmpty() && !isFileLoaded) {
			obtainFileData();
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
				dataSet.encrypt();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			JOptionPane.showMessageDialog(frame.getComponent(0), "File succesfully encrypted");
		}

		// if loaded file isn't blank, allow decryption operation
		else if (ae.getSource() == btnDecrypt && !data.isBlank()) {
			try {
				dataSet.decrypt();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			JOptionPane.showMessageDialog(frame.getComponent(0), "File succesfully decrypted");
		}
		

		// if loaded file is BLANK or encrypt/decrypt btn pushed
		else if (data.isBlank() && (ae.getSource() == btnEncrypt || ae.getSource() == btnDecrypt)) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "File provided is empty", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		// if file has been already been loaded and load file btn pushed
		else if (ae.getSource() == btnLoadFile && isFileLoaded) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "A file has already been loaded", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		// if encrypt/decrypt is pushed and file has not been loaded
		else if ((ae.getSource() == btnEncrypt || ae.getSource() == btnDecrypt) && !isFileLoaded) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "No file loaded yet", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		else if (ae.getSource() == btnBrowse) {
			fileBrowse();
		}
	}

	/**
	 * Loads the desired file and obtains the contents within
	 */
	public void obtainFileData() {
		Scanner read = null;

		String fileToLoad = loadingTextField.getText();

		try {
			File f1 = new File(fileToLoad);
			
			read = new Scanner(f1);

			while (read.hasNextLine()) {
				data += read.nextLine();
			}

			JOptionPane.showMessageDialog(frame.getComponent(0), "File succesfully loaded");
			setFileName(f1.toString());
			dataSet = new EncryptDecrypt(data);
			isFileLoaded = true;

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

		if(read != null) {
			read.close();
		}
	}

	/**
	 * Returns name of file to be encrypted
	 * @return filename to be encrypted
	 */
	public static String getFileName() {
		return fileName;
	}

	/**
	 * Sets the name of the file to be encrypted
	 * @param argFileName that will be encrypted
	 */
	public static void setFileName(String argFileName) {
		EncryptionGui.fileName = argFileName;
	}
}
