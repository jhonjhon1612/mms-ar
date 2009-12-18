package ar.uba.dc.so.io;

import java.io.PrintStream;

public class TextAreaPrintStream extends PrintStream {
	public TextAreaPrintStream(TextAreaOutputStream tostream) {
		super(tostream, true);
	}
}
