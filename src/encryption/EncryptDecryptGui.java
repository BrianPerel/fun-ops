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
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

/**
 * Encryption-decryption application. The idea is that the user can load a file,
 * encrypt it's contents, and hold an encrypted file. Then at any point can open
 * this modified file and decrypt it.
 * 
 * The science of encrypting and decrypting information is called cryptography. 
 * Unencrypted data is also known as plaintext, and encrypted data is called ciphertext
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
	private static final String ERROR = "ERROR";
	private static final Color LIGHT_BLUE = new Color(135, 206, 250); // regular color of gui buttons
	private static final Color DARK_LIGHT_BLUE = new Color(102, 178, 255); // color of gui buttons when hovering 

	public static void main(String[] args) {	
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		new EncryptDecryptGui();
	}

	/**
	 * Create the application. Places all the buttons on the app's board and initializes the contents of the frame, building the gui.
	 */
	public EncryptDecryptGui() {
		frame = new JFrame("Encrypt-decrypt App by: Brian Perel");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		frame.setSize(421, 264);
		
		data = new StringBuilder();
		btnEncrypt = new JButton("Encrypt");
		btnDecrypt = new JButton("Decrypt");
		btnLoadFile = new JButton("Load file");

		frame.setContentPane(new JLabel(new ImageIcon("res/graphics/bg-image-encryption.jpg")));

		btnLoadFile.setBounds(49, 43, 89, 28);
		frame.getContentPane().add(btnLoadFile);
		btnLoadFile.addActionListener(this);
		btnLoadFile.setFocusable(false);
		btnLoadFile.setBackground(LIGHT_BLUE);
		btnLoadFile.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnLoadFile.setBackground(DARK_LIGHT_BLUE);
		    }
			
			@Override
		    public void mouseExited(java.awt.event.MouseEvent evt) {
				btnLoadFile.setBackground(LIGHT_BLUE);
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
		btnEncrypt.setBackground(LIGHT_BLUE);
		btnEncrypt.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnEncrypt.setBackground(DARK_LIGHT_BLUE);
		    }
			
			@Override
		    public void mouseExited(java.awt.event.MouseEvent evt) {
				btnEncrypt.setBackground(LIGHT_BLUE);
		    }
		});
		
		btnEncrypt.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		btnDecrypt.setBounds(235, 139, 89, 28);
		frame.getContentPane().add(btnDecrypt);

		JSeparator separator = new JSeparator();
		separator.setBounds(49, 104, 307, 2);
		frame.getContentPane().add(separator);
		btnDecrypt.addActionListener(this);
		btnDecrypt.setBackground(LIGHT_BLUE);
		btnDecrypt.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnDecrypt.setBackground(DARK_LIGHT_BLUE);
		    }
			
			@Override
		    public void mouseExited(java.awt.event.MouseEvent evt) {
				btnDecrypt.setBackground(LIGHT_BLUE);
		    }
		});
		
		btnDecrypt.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		btnBrowse = new JButton("Browse");
		btnBrowse.setBounds(270, 43, 86, 28);
		frame.getContentPane().add(btnBrowse);
		btnBrowse.addActionListener(this);
		btnBrowse.setBackground(LIGHT_BLUE);
		btnBrowse.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnBrowse.setBackground(DARK_LIGHT_BLUE);
		    }
			
			@Override
		    public void mouseExited(java.awt.event.MouseEvent evt) {
				btnBrowse.setBackground(LIGHT_BLUE);
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
		// browse menu's default look in location is set to the user's home (C:\Users\example)
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home"))); 
		
		// creates the JFileChooser browse menu window that pops up when browse is hit
		if (fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
			loadingTextField.setText(fileChooser.getSelectedFile().getName());
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		Object source = ae.getSource();
		String fileToLoad = loadingTextField.getText();
		
		if (source == btnLoadFile) {
			// if filename isn't empty or file hasn't yet been loaded
			if(!fileToLoad.isEmpty() && !isFileLoaded) {
				obtainFileData(fileToLoad);
				return;
			}
			
			// if file has been already been loaded and load file btn pushed
			else if (isFileLoaded) {
				JOptionPane.showMessageDialog(frame.getComponent(0), "A file has already been loaded", ERROR,
						JOptionPane.ERROR_MESSAGE);
				return;
			}
	
			// if file load textfield is empty while load file btn is pushed
			JOptionPane.showMessageDialog(frame.getComponent(0), "No file name entered", ERROR,
					JOptionPane.ERROR_MESSAGE);
		}		

		// if loaded file isn't blank, allow encryption op
		else if (source == btnEncrypt && !data.isEmpty()) {
			try {
				dataSet.encrypt();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			
			JOptionPane.showMessageDialog(frame.getComponent(0), "File succesfully encrypted");
		}

		// if loaded file isn't blank, allow decryption operation
		else if (source == btnDecrypt && !data.isEmpty()) {
			try {
				dataSet.decrypt();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			
			JOptionPane.showMessageDialog(frame.getComponent(0), "File succesfully decrypted");
		}

		// if loaded file is BLANK and encrypt/decrypt btn pushed
		else if (!fileToLoad.isEmpty() && data.isEmpty() && (source == btnEncrypt || source == btnDecrypt)) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "File provided is empty", ERROR,
					JOptionPane.ERROR_MESSAGE);
		}

		// if encrypt/decrypt is pushed and file has not been loaded
		else if ((source == btnEncrypt || source == btnDecrypt) && !isFileLoaded) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "No file loaded yet", ERROR,
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

		// append .txt to the filename entered if entered without .txt 
		if (!fileToLoad.endsWith(".txt")) {
			fileToLoad += ".txt";
		}
		
		File file = new File(fileToLoad);

		// use try with resources here, which will auto close resources
		try (
			  Scanner read = new Scanner(file);
			) {

			// place every line of the file into a data StringBuilder, to use 'data' for encryption/decryption
			while (read.hasNextLine()) {
				data.append(read.nextLine());
			}

			JOptionPane.showMessageDialog(frame.getComponent(0), "File succesfully loaded");
			setFileName(file.toString());
			dataSet = new EncryptDecrypt(data);
			isFileLoaded = true;

			// check if Desktop is supported by this Platform or not
			if (!Desktop.isDesktopSupported()) {
				JOptionPane.showMessageDialog(frame.getComponent(0), "Desktop is not supported by this application",
						ERROR, JOptionPane.ERROR_MESSAGE);
				return;
			}

			// check if this file exists
			if (file.exists()) {
				Desktop.getDesktop().open(file);
			}

		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "File not found");
			loadingTextField.setText("");
		} catch (IOException ioe) {
			ioe.printStackTrace();
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
