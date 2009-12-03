package ar.uba.dc.so.simulator;

import javax.swing.JFrame;

import ar.uba.dc.so.gui.ControlWindow;
import ar.uba.dc.so.gui.MainWindow;
import ar.uba.dc.so.gui.ProcessQueuesWindow;

public class GraphicMode {
	public static void run() {
		ProcessQueuesWindow pw = new ProcessQueuesWindow();
		ControlWindow cw = new ControlWindow(pw);
		
		cw.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		cw.setVisible(true);
		pw.setVisible(true);
	}
}
