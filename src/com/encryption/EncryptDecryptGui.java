package com.encryption;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.Serial;

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
import javax.swing.filechooser.FileNameExtensionFilter;

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

	protected static JFrame window;
	protected static JTextField textFieldLoading;
	protected static final String ERROR = "Error";
	private static final Color LIGHT_BLUE_COLOR = new Color(135, 206, 250); // regular color of GUI buttons
	private static final Color DARK_LIGHT_BLUE_COLOR = new Color(102, 178, 255); // color of GUI buttons when hovering
	protected static final String DEFAULT_FILENAME_ENTRY_TEXT = "Enter file name...";

	protected static StringBuilder data;
	private JButton btnBrowse;
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
			System.out.println("Failed to set LookAndFeel\n" + e.getMessage());
			e.printStackTrace();
		}

		textFieldLoading = new JTextField(DEFAULT_FILENAME_ENTRY_TEXT);
		window = new JFrame("File Caesar Cipher App by: Brian Perel");
		new EncryptDecryptGui();
	}

	/**
	 * Create the application. Places all the buttons on the app's board and initializes the contents of the frame, building the GUI.
	 */
	public EncryptDecryptGui() {
		createGui();
	}

	private void createGui() {
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setContentPane(new JLabel(new ImageIcon("res/graphics/bg-image-encryption.jpg")));
		window.getContentPane().setLayout(null);
		window.setResizable(false);
		window.setSize(421, 264);

		data = new StringBuilder();

		JButton[] buttons = new JButton[4];

		btnLoadFile = buttons[0] = new JButton("Load file");

		textFieldLoading.setBounds(148, 44, 112, 26);
		textFieldLoading.setForeground(Color.GRAY);
		window.getContentPane().add(textFieldLoading);
		textFieldLoading.setColumns(10);
		textFieldLoading.setToolTipText("Enter file name to load");
		allowFileDragNDrop();
		textFieldLoading.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent ke) {
				if(DEFAULT_FILENAME_ENTRY_TEXT.equals(textFieldLoading.getText())) {
					textFieldLoading.setForeground(Color.BLACK);
					textFieldLoading.setText("");
				}

				if((ke.getKeyChar() == KeyEvent.VK_BACK_SPACE && textFieldLoading.getText().length() <= 1)
						|| (ke.isControlDown() && ke.getKeyChar() == KeyEvent.VK_BACK_SPACE)) {
					textFieldLoading.setText(DEFAULT_FILENAME_ENTRY_TEXT);
					textFieldLoading.setForeground(Color.GRAY);
					textFieldLoading.setCaretPosition(0);
				}
			}
		});

		btnBrowse = buttons[1] = new JButton("Browse");
		btnEncrypt = buttons[2] = new JButton("Encrypt");
		btnDecrypt = buttons[3] = new JButton("Decrypt");

		for(JButton button : buttons) {
			window.getContentPane().add(button);
			button.addActionListener(this);
			button.setFocusable(false);
			button.setSize(89, 28);
			button.setBackground(LIGHT_BLUE_COLOR);
			button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			button.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
			    public void mouseEntered(MouseEvent evt) {
					button.setBackground(DARK_LIGHT_BLUE_COLOR);
			    }

				@Override
			    public void mouseExited(MouseEvent evt) {
					button.setBackground(LIGHT_BLUE_COLOR);
			    }
			});
		}

		buttons[0].setLocation(49, 43);
		buttons[1].setLocation(270, 43);
		buttons[2].setLocation(84, 140);
		buttons[3].setLocation(235, 139);

		window.setVisible(true);
		window.setLocationRelativeTo(null);
	}

	/**
	 * Allows the user to drag and drop a file onto the text field to load
	 */
	private void allowFileDragNDrop() {

		textFieldLoading.setDropTarget(new DropTarget() {

			@Serial
			private static final long serialVersionUID = 7192052102451674891L;

			@Override
			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					evt.acceptDrop(DnDConstants.ACTION_COPY);

					String fileName = new File(evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor).toString()).getName();

					if(fileName.contains("]")) {
						fileName = fileName.replace("]", "");
					}

					textFieldLoading.setText(fileName);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	/**
	 * JFileChooser file browse menu for GUI
	 */
	private void fileBrowse() {
		JFileChooser fileChooser = new JFileChooser();
		// browse menu's default look in location is set to the user's home (C:\Users\example)
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setDialogTitle("Browse for file to encrypt");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text", "txt"));

        // creates the JFileChooser browse menu window that pops up when browse is hit
		if (fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
			textFieldLoading.setForeground(Color.BLACK);
			textFieldLoading.setText(fileChooser.getSelectedFile().getName());
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		Object source = ae.getSource();
		String fileToLoad = textFieldLoading.getText().trim();

		if (source.equals(btnLoadFile)) {
			// if filename isn't empty or file hasn't yet been loaded
			if(!(fileToLoad.isEmpty() || isFileLoaded || fileToLoad.contains(DEFAULT_FILENAME_ENTRY_TEXT))) {
				EncryptDecryptFileUtils.loadFileData(fileToLoad);
				return; // prevent below statements from executing if we fall into here
			}

			// if file has been already been loaded and load file btn pushed
			// or if file load textfield is empty while load file btn is pushed
			JOptionPane.showMessageDialog(window.getComponent(0), isFileLoaded ? "A file has already been loaded"
					: "No file name entered", ERROR, JOptionPane.ERROR_MESSAGE);
			Toolkit.getDefaultToolkit().beep();
		}
		// if encrypt/decrypt btn pushed and file has not been loaded
		else if ((source.equals(btnEncrypt) || source.equals(btnDecrypt)) && !isFileLoaded) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(window.getComponent(0), "No file loaded yet", ERROR,
					JOptionPane.ERROR_MESSAGE);
		}
		else if (source.equals(btnBrowse)) {
			if(isFileLoaded) {
				// if file has been already been loaded and load file btn pushed
				JOptionPane.showMessageDialog(window.getComponent(0), "A file has already been loaded", ERROR, JOptionPane.ERROR_MESSAGE);
				Toolkit.getDefaultToolkit().beep();
			}
			else {
				fileBrowse();
			}
		}
		else {
			checkOtherMenuActions(source, fileToLoad);
		}
	}

	/**
	 * Listens for the other program buttons to be pushed
	 * @param source the object on which the Event initially occurred
	 * @param fileToLoad the file we're going to encrypt/decrypt
	 */
	private void checkOtherMenuActions(Object source, String fileToLoad) {
		// if loaded file isn't blank, allow encryption op
		if (source.equals(btnEncrypt) && !data.isEmpty()) {
			try {
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(window.getComponent(0),
					dataSet.encrypt() ? "File successfully encrypted" : "File already encrypted. Could not encrypt");
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		// if loaded file isn't blank, allow decryption operation
		else if (source.equals(btnDecrypt) && !data.isEmpty()) {
			try {
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(window.getComponent(0),
					dataSet.decrypt() ? "File successfully decrypted" : "File already decrypted. Could not decrypt");
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		// if loaded file is blank and encrypt/decrypt btn pushed
		else if (!fileToLoad.isEmpty() && data.isEmpty() && (source.equals(btnEncrypt) || source.equals(btnDecrypt))) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(window.getComponent(0), "File provided is empty", ERROR,
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
