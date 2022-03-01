package encryption;

import java.awt.Color;
import java.awt.Cursor;
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
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

/**
 * Encryption-decryption application. The idea is that the user can load a file,
 * encrypt it's contents, and hold an encrypted file. Then at any point can open
 * this modified file and decrypt it.
 * 
 * @author Brian Perel
 *
 */
public class EncryptDecryptGui implements ActionListener {

	private JFrame frame;
	private JButton btnBrowse;
	private boolean isFileLoaded;
	private static String fileName;
	private EncryptDecrypt dataSet;
	private JTextField loadingTextField;
	private StringBuilder data;
	private JButton btnEncrypt;
	private JButton btnDecrypt;
	private JButton btnLoadFile;

	public static void main(String[] args) {	
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		new EncryptDecryptGui();
	}

	/**
	 * Create the application.
	 */
	public EncryptDecryptGui() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 421, 264);
		frame.setTitle("Encrypt-decrypt App by: Brian Perel");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		data = new StringBuilder();
		btnEncrypt = new JButton("Encrypt");
		btnDecrypt = new JButton("Decrypt");
		btnLoadFile = new JButton("Load file");

		frame.setContentPane(new JLabel(new ImageIcon("res/graphics/bg-image-encryption.jpg")));

		btnLoadFile.setBounds(49, 43, 89, 28);
		frame.getContentPane().add(btnLoadFile);
		btnLoadFile.addActionListener(this);
		btnLoadFile.setFocusable(false);
		Color lightBlue = new Color(135, 206, 250); // regular color of gui buttons
		Color darkerLightBlue = new Color(102, 178, 255); // color of gui buttons when hovering 
		btnLoadFile.setBackground(lightBlue);
		btnLoadFile.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnLoadFile.setBackground(darkerLightBlue);
		    }
			
			@Override
		    public void mouseExited(java.awt.event.MouseEvent evt) {
				btnLoadFile.setBackground(lightBlue);
		    }
		});
		
		btnLoadFile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		loadingTextField = new JTextField();
		loadingTextField.setBounds(148, 44, 112, 26);
		frame.getContentPane().add(loadingTextField);
		loadingTextField.setColumns(10);

		btnEncrypt.setBounds(84, 140, 89, 28);
		frame.getContentPane().add(btnEncrypt);
		btnEncrypt.addActionListener(this);
		btnEncrypt.setBackground(lightBlue);
		btnEncrypt.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnEncrypt.setBackground(darkerLightBlue);
		    }
			
			@Override
		    public void mouseExited(java.awt.event.MouseEvent evt) {
				btnEncrypt.setBackground(lightBlue);
		    }
		});
		
		btnEncrypt.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		btnDecrypt.setBounds(235, 139, 89, 28);
		frame.getContentPane().add(btnDecrypt);

		JSeparator separator = new JSeparator();
		separator.setBounds(49, 104, 307, 2);
		frame.getContentPane().add(separator);
		btnDecrypt.addActionListener(this);
		btnDecrypt.setBackground(lightBlue);
		btnDecrypt.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnDecrypt.setBackground(darkerLightBlue);
		    }
			
			@Override
		    public void mouseExited(java.awt.event.MouseEvent evt) {
				btnDecrypt.setBackground(lightBlue);
		    }
		});
		
		btnDecrypt.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		btnBrowse = new JButton("Browse");
		btnBrowse.setBounds(270, 43, 86, 28);
		frame.getContentPane().add(btnBrowse);
		btnBrowse.addActionListener(this);
		btnBrowse.setBackground(lightBlue);
		btnBrowse.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnBrowse.setBackground(darkerLightBlue);
		    }
			
			@Override
		    public void mouseExited(java.awt.event.MouseEvent evt) {
				btnBrowse.setBackground(lightBlue);
		    }
		});
		
		btnBrowse.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
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
		Object source = ae.getSource();
		String fileToLoad = loadingTextField.getText();
		
		// if filename isn't empty or file hasn't yet been loaded
		if (source == btnLoadFile && !fileToLoad.isEmpty() && !isFileLoaded) {
			obtainFileData(fileToLoad);
		}

		// if file load textfield is empty while load file btn is pushed
		else if (source == btnLoadFile && fileToLoad.isEmpty()) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "No file name entered", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		// if loaded file isn't blank, allow encryption op
		else if (source == btnEncrypt && !data.isEmpty()) {
			try {
				frame.getContentPane().add(new JProgressBar());
				dataSet.encrypt();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			JOptionPane.showMessageDialog(frame.getComponent(0), "File succesfully encrypted");
		}

		// if loaded file isn't blank, allow decryption operation
		else if (source == btnDecrypt && !data.isEmpty()) {
			try {
				dataSet.decrypt();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			JOptionPane.showMessageDialog(frame.getComponent(0), "File succesfully decrypted");
		}

		// if loaded file is BLANK and encrypt/decrypt btn pushed
		else if (!fileToLoad.isEmpty() && data.isEmpty() && (source == btnEncrypt || source == btnDecrypt)) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "File provided is empty", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		// if file has been already been loaded and load file btn pushed
		else if (source == btnLoadFile && isFileLoaded) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "A file has already been loaded", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		// if encrypt/decrypt is pushed and file has not been loaded
		else if ((source == btnEncrypt || source == btnDecrypt) && !isFileLoaded) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "No file loaded yet", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		else if (source == btnBrowse) {
			fileBrowse();
		}
	}

	/**
	 * Loads the desired file and obtains the contents within
	 */
	public void obtainFileData(String fileToLoad) {
		Scanner read = null;
		
		if(!fileToLoad.endsWith(".txt")) {
			fileToLoad += ".txt";
		}

		try {
			File f1 = new File(fileToLoad);
			read = new Scanner(f1);

			while (read.hasNextLine()) {
				data.append(read.nextLine());
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
		} finally {
			if(read != null) {
				read.close();
			}
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
		EncryptDecryptGui.fileName = argFileName;
	}
}
