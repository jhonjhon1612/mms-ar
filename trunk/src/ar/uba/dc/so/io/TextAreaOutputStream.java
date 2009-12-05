package ar.uba.dc.so.io;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

public class TextAreaOutputStream extends OutputStream {
	private JTextArea textArea = null;
	
	public TextAreaOutputStream(JTextArea tArea) {
		textArea = tArea;
	}
	
	@Override
	public void write(int value) throws IOException {
		byte b = new Integer(value & 0xFF).byteValue();
		char c = (char) (b&0xFF);
		textArea.append(Character.toString(c));
	}

}
