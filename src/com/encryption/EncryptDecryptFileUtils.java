package com.encryption;

import static com.encryption.EncryptDecryptGui.DEFAULT_FILENAME_ENTRY_TEXT;
import static com.encryption.EncryptDecryptGui.ERROR;
import static com.encryption.EncryptDecryptGui.data;
import static com.encryption.EncryptDecryptGui.dataSet;
import static com.encryption.EncryptDecryptGui.isFileLoaded;
import static com.encryption.EncryptDecryptGui.textFieldEnterFileName;
import static com.encryption.EncryptDecryptGui.window;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

public class EncryptDecryptFileUtils {

	private static File file;
	private static String fileName;

	private EncryptDecryptFileUtils() {
		// private constructor will hide the implicit (default) public one, b/c utility classes like this should not have public constructors
	}

	/**
	 * Loads the desired file and obtains it's contents
	 * @param fileToLoad the file we're going to encrypt/decrypt
	 */
	protected static void loadFileData(String fileToLoad) {
		if (loadFile(fileToLoad)) {

			try (Scanner read = new Scanner(file)) {
				// place every line of the file into a data StringBuilder, to use 'data' for encryption/decryption
				while (read.hasNextLine()) {
					data.append(read.nextLine());
				}

				if (data.isEmpty()) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(EncryptDecryptGui.window.getComponent(0), "File is empty");
					return;
				}

				handleSuccessfulFileLoadingStatus();
				checkIfDesktopIsSupportedAndOpenFile();

			} catch (FileNotFoundException e) {
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(EncryptDecryptGui.window.getComponent(0), "File not found", ERROR, JOptionPane.INFORMATION_MESSAGE);
				textFieldEnterFileName.setText(DEFAULT_FILENAME_ENTRY_TEXT);
				textFieldEnterFileName.setForeground(Color.GRAY);
				textFieldEnterFileName.setCaretPosition(0);
			}
		}
	}

	private static void checkIfDesktopIsSupportedAndOpenFile() {
		// check if Desktop is supported by this Platform or not
	    if (!Desktop.isDesktopSupported()) {
	        Toolkit.getDefaultToolkit().beep();
	        JOptionPane.showMessageDialog(window.getComponent(0), "Desktop is not supported by this application", ERROR, JOptionPane.ERROR_MESSAGE);
	        return;
	    }

	    try {
			openFile();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void handleSuccessfulFileLoadingStatus() {
	    JOptionPane.showMessageDialog(window.getComponent(0), "File successfully loaded");
	    textFieldEnterFileName.setEditable(false);
	    textFieldEnterFileName.setBackground(Color.LIGHT_GRAY);
	    setFileName(file.toString());
	    dataSet = new EncryptDecryptOp(EncryptDecryptGui.data);
	    isFileLoaded = true;
	}

	/**
	 * Loads the file to be encrypted
	 * @param fileToLoad file to load
	 * @return loading result
	 */
	private static boolean loadFile(String fileToLoad) {

		// append .txt to the filename entered if entered without .txt extension
		if (!(fileToLoad.toLowerCase().endsWith(".txt"))) {
			fileToLoad += ".txt";
			textFieldEnterFileName.setText(fileToLoad);
		}

		if (new File(fileToLoad).canRead()) {
			file = new File(fileToLoad);
			return true;
		}
		else {
			 JOptionPane.showMessageDialog(null, "File couldn't be found or is not accessible", "Error", JOptionPane.ERROR_MESSAGE);
			 Toolkit.getDefaultToolkit().beep();
			 textFieldEnterFileName.selectAll();
		}

		return false;
	}

	/**
	 * Opens the loaded data file and brings back the GUI to the front
	 * @throws IOException file io exception
	 */
	protected static void openFile() throws IOException {
		// check if this file exists
		if (file.exists()) {
			Desktop.getDesktop().open(file);

			try {
				TimeUnit.MILLISECONDS.sleep(300L);
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}

			window.toFront();
		}
	}

	/**
	 * Returns file name to be encrypted
	 * @return filename to be encrypted
	 */
	public static String getFileName() {
		return fileName;
	}

	/**
	 * Sets the file name to be encrypted
	 * @param argFileName that will be encrypted
	 */
	public static void setFileName(String argFileName) {
		fileName = argFileName;
	}
}
