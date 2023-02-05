package com.encryption;

import static com.encryption.EncryptDecryptGui.DEFAULT_FILENAME_ENTRY_TEXT;
import static com.encryption.EncryptDecryptGui.ERROR;
import static com.encryption.EncryptDecryptGui.data;
import static com.encryption.EncryptDecryptGui.dataSet;
import static com.encryption.EncryptDecryptGui.window;
import static com.encryption.EncryptDecryptGui.isFileLoaded;
import static com.encryption.EncryptDecryptGui.textFieldLoading;

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
		// private constructor will hide the implicit public one, b/c utility classes like this should not have public constructors
	}

	/**
	 * Loads the desired file and obtains the contents within
	 * @param fileToLoad the file we're gonna encrypt/decrypt
	 */
	protected static void loadFileData(String fileToLoad) {
		// append .txt to the filename entered if entered without .txt
		loadFile(fileToLoad);

		// use try with resources here, which will auto close resources
		try (Scanner read = new Scanner(file)) {

			// place every line of the file into a data StringBuilder, to use 'data' for encryption/decryption
			while (read.hasNextLine()) {
				data.append(read.nextLine());
			}

			if(data.isEmpty()) {
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(EncryptDecryptGui.window.getComponent(0), "File is empty");
				return;
			}

			JOptionPane.showMessageDialog(window.getComponent(0), "File succesfully loaded");
			textFieldLoading.setEditable(false);
			textFieldLoading.setBackground(Color.LIGHT_GRAY);
			setFileName(file.toString());
			dataSet = new EncryptDecryptOp(EncryptDecryptGui.data);
			isFileLoaded = true;

			// check if Desktop is supported by this Platform or not
			if (!Desktop.isDesktopSupported()) {
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(window.getComponent(0), "Desktop is not supported by this application",
						ERROR, JOptionPane.ERROR_MESSAGE);
				return;
			}

			openFile();

		} catch (FileNotFoundException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(EncryptDecryptGui.window.getComponent(0), "File not found", ERROR, JOptionPane.INFORMATION_MESSAGE);
			textFieldLoading.setText(DEFAULT_FILENAME_ENTRY_TEXT);
			textFieldLoading.setForeground(Color.GRAY);
			textFieldLoading.setCaretPosition(0);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Loads the file to be encrypted
	 * @param fileToLoad file to load
	 */
	private static void loadFile(String fileToLoad) {
		if (!fileToLoad.endsWith(".txt")) {
			fileToLoad += ".txt";
			textFieldLoading.setText(fileToLoad);
		}

		file = new File(fileToLoad);
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
