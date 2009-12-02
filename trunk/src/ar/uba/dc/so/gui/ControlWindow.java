package ar.uba.dc.so.gui;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JComboBox;
import java.awt.Rectangle;

public class ControlWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JComboBox jComboBox = null;

	/**
	 * This is the default constructor
	 */
	public ControlWindow() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
		this.setBounds(new Rectangle(300, 0, 300, 200));
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJComboBox(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJComboBox() {
		if (jComboBox == null) {
			jComboBox = new JComboBox();
			jComboBox.setBounds(new Rectangle(6, 17, 196, 28));
			
			jComboBox.addItem("Fixed Partition");
			jComboBox.addItem("Simple Contiguous");
			jComboBox.addItem("Variable Partition");
			jComboBox.addItem("Swapping");
		}
		return jComboBox;
	}

}
