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
	private boolean isEncrypted;
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
		frame.setContentPane(new JLabel(new ImageIcon("res/graphics/bg-image-encryption.jpg")));
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		frame.setSize(421, 264);
		
		data = new StringBuilder();
		
		JButton[] buttons = new JButton[4];
		
		buttons[0] = new JButton("Load file");
		btnLoadFile = buttons[0];
		
		loadingTextField = new JTextField();
		loadingTextField.setBounds(148, 44, 112, 26);
		frame.getContentPane().add(loadingTextField);
		loadingTextField.setColumns(10);
		
		buttons[1] = new JButton("Browse");
		btnBrowse = buttons[1];
		buttons[2] = new JButton("Encrypt");
		btnEncrypt = buttons[2];
		buttons[3] = new JButton("Decrypt");
		btnDecrypt = buttons[3];

		for(JButton button : buttons) {
			frame.getContentPane().add(button);
			button.addActionListener(this);
			button.setFocusable(false);
			button.setBackground(LIGHT_BLUE);
			button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			button.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
			    public void mouseEntered(java.awt.event.MouseEvent evt) {
					button.setBackground(DARK_LIGHT_BLUE);
			    }
				
				@Override
			    public void mouseExited(java.awt.event.MouseEvent evt) {
					button.setBackground(LIGHT_BLUE);
			    }
			});
		}
		
		buttons[0].setBounds(49, 43, 89, 28);
		buttons[1].setBounds(270, 43, 86, 28);
		buttons[2].setBounds(84, 140, 89, 28);
		buttons[3].setBounds(235, 139, 89, 28);

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
				loadFileData(fileToLoad);
				return;
			}
			
			// if file has been already been loaded and load file btn pushed
			// or if file load textfield is empty while load file btn is pushed
			JOptionPane.showMessageDialog(frame.getComponent(0), isFileLoaded ? "A file has already been loaded" 
					: "No file name entered", ERROR, JOptionPane.ERROR_MESSAGE);
		}		
		
		// if encrypt/decrypt btn pushed and file has not been loaded
		else if ((source == btnEncrypt || source == btnDecrypt) && !isFileLoaded) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "No file loaded yet", ERROR,
					JOptionPane.ERROR_MESSAGE);
		}

		else if (source == btnBrowse) {
			fileBrowse();
		}

		checkOtherActions(source, fileToLoad);
	}
	
	/**
	 * Listens for the other program buttons to be pushed
	 * @param source the object on which the Event initially occurred
	 * @param fileToLoad the we're going to encrypt/decrypt
	 */
	public void checkOtherActions(Object source, String fileToLoad) {
		
		// if loaded file isn't blank, allow encryption op
		if (source == btnEncrypt && !data.isEmpty()) {
			try {
				JOptionPane.showMessageDialog(frame.getComponent(0), 
					dataSet.encrypt() ? "File succesfully encrypted" : "This action has already occurred. Could not complete request");
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

		// if loaded file isn't blank, allow decryption operation
		else if (source == btnDecrypt && !data.isEmpty()) {
			try {
				JOptionPane.showMessageDialog(frame.getComponent(0), 
					dataSet.decrypt() ? "File succesfully decrypted" : "File already decrypted. Could not decrypt");
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		
		// if loaded file is blank and encrypt/decrypt btn pushed
		else if (!fileToLoad.isEmpty() && data.isEmpty() && (source == btnEncrypt || source == btnDecrypt)) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "File provided is empty", ERROR,
					JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * Loads the desired file and obtains the contents within
	 * @param fileToLoad the file we're gonna encrypt/decrypt
	 */
	public void loadFileData(String fileToLoad) {

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
