package ar.uba.dc.so.simulator;

import java.io.FileNotFoundException;

import javax.swing.JFrame;

import ar.uba.dc.so.gui.ControlWindow;
import ar.uba.dc.so.gui.MemoryVisualizationWindow;
import ar.uba.dc.so.gui.OutputConsole;
import ar.uba.dc.so.gui.ProcessQueuesWindow;
import ar.uba.dc.so.io.TextAreaOutputStream;
import ar.uba.dc.so.io.TextAreaPrintStream;

public class GraphicMode {
	public static void run() throws FileNotFoundException {
		ProcessQueuesWindow pw = new ProcessQueuesWindow();
		MemoryVisualizationWindow mw = new MemoryVisualizationWindow();
		OutputConsole oc = new OutputConsole();
		
		ControlWindow cw = new ControlWindow(pw, mw, oc);
		
		cw.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		TextAreaOutputStream tostream = new TextAreaOutputStream(oc.getJTextArea());
		System.setOut(new TextAreaPrintStream(tostream));
		
		cw.setVisible(true);
		pw.setVisible(true);
		mw.setVisible(true);
		oc.setVisible(true);
	}
}
