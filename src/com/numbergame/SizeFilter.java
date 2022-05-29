package com.numbergame;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Creates a custom document filter to handle/enforce user action, 
 * allowing you to limit the number of available spaces of input a user can use in an entry box component
 * @author Brian
 *
 */
public class SizeFilter extends DocumentFilter {

	private int maxChars;

	public SizeFilter(int argMaxChars) {
		this.maxChars = argMaxChars;
	}

	@Override
	public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
			throws BadLocationException {
		if ((fb.getDocument().getLength() + text.length() - length) <= maxChars && !text.matches("[a-zA-Z]+")
				&& !text.matches("[^a-zA-Z0-9]+")) {
			super.replace(fb, offset, length, text, attrs);
		}
	}
}
