package com.encryption;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
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
public class EncryptDecryptGui extends KeyAdapter implements ActionListener {
	
	protected static JFrame frame;
	protected static JTextField textFieldLoading;
	protected static final String ERROR = "ERROR";
	private static final Color LIGHT_BLUE = new Color(135, 206, 250); // regular color of GUI buttons
	private static final Color DARK_LIGHT_BLUE = new Color(102, 178, 255); // color of GUI buttons when hovering 

	private JButton btnBrowse;
	static StringBuilder data;
	private JButton btnEncrypt;
	private JButton btnDecrypt;
	private JButton btnLoadFile;
	protected static boolean isFileLoaded;
	protected static EncryptDecryptOp dataSet;

	public static void main(String[] args) {	
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		new EncryptDecryptGui();
	}

	/**
	 * Create the application. Places all the buttons on the app's board and initializes the contents of the frame, building the GUI.
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
		
		btnLoadFile = buttons[0] = new JButton("Load file");
		
		textFieldLoading = new JTextField("Enter file name...");
		textFieldLoading.setBounds(148, 44, 112, 26);
		textFieldLoading.setForeground(Color.GRAY);
		frame.getContentPane().add(textFieldLoading);
		textFieldLoading.setColumns(10);
		textFieldLoading.setToolTipText("Enter file name to load");
		textFieldLoading.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(textFieldLoading.getText().equals("Enter file name...")) {
					textFieldLoading.setForeground(Color.BLACK);
					textFieldLoading.setText("");
				}
			}
		});

		btnBrowse = buttons[1] = new JButton("Browse");
		btnEncrypt = buttons[2] = new JButton("Encrypt");
		btnDecrypt = buttons[3] = new JButton("Decrypt");

		for(JButton button : buttons) {
			frame.getContentPane().add(button);
			button.addActionListener(this);
			button.setFocusable(false);
			button.setSize(89, 28);
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
		
		buttons[0].setLocation(49, 43);
		buttons[1].setLocation(270, 43);
		buttons[2].setLocation(84, 140);
		buttons[3].setLocation(235, 139);
		
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
			textFieldLoading.setText(fileChooser.getSelectedFile().getName());
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		Object source = ae.getSource();
		String fileToLoad = textFieldLoading.getText().trim();
		
		if (source == btnLoadFile) {
			// if filename isn't empty or file hasn't yet been loaded
			if(!(fileToLoad.isEmpty() || isFileLoaded)) {
				EncryptDecryptFileUtils.loadFileData(fileToLoad);
				return; // prevent below statements from executing if we fall into here
			}
			
			// if file has been already been loaded and load file btn pushed
			// or if file load textfield is empty while load file btn is pushed
			JOptionPane.showMessageDialog(frame.getComponent(0), isFileLoaded ? "A file has already been loaded" 
					: "No file name entered", ERROR, JOptionPane.ERROR_MESSAGE);
			Toolkit.getDefaultToolkit().beep();
		}		
		// if encrypt/decrypt btn pushed and file has not been loaded
		else if ((source == btnEncrypt || source == btnDecrypt) && !isFileLoaded) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(frame.getComponent(0), "No file loaded yet", ERROR,
					JOptionPane.ERROR_MESSAGE);
		}
		else if (source == btnBrowse) {
			if(isFileLoaded) {
				// if file has been already been loaded and load file btn pushed
				JOptionPane.showMessageDialog(frame.getComponent(0), "A file has already been loaded", ERROR, JOptionPane.ERROR_MESSAGE);
				Toolkit.getDefaultToolkit().beep();
			}
			else {
				fileBrowse();
			}
		}
		else {
			checkOtherActions(source, fileToLoad);
		}
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
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(frame.getComponent(0), 
					dataSet.encrypt() ? "File succesfully encrypted" : "File already encrypted. Could not encrypt");
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		// if loaded file isn't blank, allow decryption operation
		else if (source == btnDecrypt && !data.isEmpty()) {
			try {
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(frame.getComponent(0), 
					dataSet.decrypt() ? "File succesfully decrypted" : "File already decrypted. Could not decrypt");
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		// if loaded file is blank and encrypt/decrypt btn pushed
		else if (!fileToLoad.isEmpty() && data.isEmpty() && (source == btnEncrypt || source == btnDecrypt)) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(frame.getComponent(0), "File provided is empty", ERROR,
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
