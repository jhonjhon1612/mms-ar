package ar.uba.dc.so.io;

import java.io.PrintStream;

import javax.swing.JTextArea;

public class TextAreaPrintStream extends PrintStream {
	public TextAreaPrintStream(TextAreaOutputStream tostream) {
		super(tostream, true);
	}
}
