package ar.uba.dc.so.gui;

import java.awt.BorderLayout;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JComboBox;
import java.awt.Rectangle;
import java.io.File;

import javax.swing.JTextField;
import javax.swing.JLabel;

import ar.uba.dc.so.domain.ProcessStatusChangeEvent;
import ar.uba.dc.so.domain.ProcessStatusChangeListener;
import ar.uba.dc.so.domain.Scheduler;
import ar.uba.dc.so.domain.SchedulerStepEvent;
import ar.uba.dc.so.domain.SchedulerStepListener;
import ar.uba.dc.so.gui.component.ComboBoxOption;
import ar.uba.dc.so.gui.component.IntegerTextField;
import ar.uba.dc.so.simulator.CmdLineMode;

import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import javax.swing.JSplitPane;
import javax.swing.JProgressBar;
import java.awt.Color;
import javax.swing.JSlider;
import java.awt.Font;

public class ControlWindow extends JFrame {
	private Thread tSimulator;
	
	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JComboBox jMemoryTypeComboBox = null;
	private JTextField jMemorySizeTextField = null;
	private JLabel jLabel = null;
	private JLabel jMemoryTypeLabel = null;
	private IntegerTextField jTimeToSimulateTextField = null;
	private JLabel jTimeToSimulateLabel = null;
	private JComboBox jAlgorithmComboBox = null;
	private JLabel jAlgorithmLabel = null;
	private JTextField jProcessFileTextField = null;
	private JButton jFileChooseButton = null;
	private JLabel jProcessFileLabel = null;
	private JPanel jProgressPanel = null;
	private JSplitPane jSplitPane = null;
	private JButton jStartSimulationButton = null;
	private JButton jStopSimulationButton = null;
	private JProgressBar jProgressBar = null;
	private JLabel jInfoLabel = null;

	private ProcessQueuesWindow pw;
	private JSlider jSpeedFactorSlider = null;
	private JLabel jSpeedLabel = null;
	
	/**
	 * This is the default constructor
	 */
	public ControlWindow(ProcessQueuesWindow pw) {
		super();
		
		this.pw = pw;
		
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setContentPane(getJContentPane());
		this.setTitle("Memory Simulator (Control Window)");
		this.setResizable(false);
		this.setBounds(new Rectangle(300, 0, 370, 315));
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jSpeedLabel = new JLabel();
			jSpeedLabel.setBounds(new Rectangle(7, 142, 356, 16));
			jSpeedLabel.setText("Simulation Speed (ranging from 2s per step to 100ms per step)");
			jProcessFileLabel = new JLabel();
			jProcessFileLabel.setBounds(new Rectangle(7, 102, 155, 16));
			jProcessFileLabel.setText("Processes File");
			jAlgorithmLabel = new JLabel();
			jAlgorithmLabel.setBounds(new Rectangle(223, 4, 76, 16));
			jAlgorithmLabel.setText("Algorithm");
			jTimeToSimulateLabel = new JLabel();
			jTimeToSimulateLabel.setBounds(new Rectangle(7, 78, 165, 16));
			jTimeToSimulateLabel.setText("Time to Simulate (seconds)");
			jMemoryTypeLabel = new JLabel();
			jMemoryTypeLabel.setBounds(new Rectangle(7, 3, 87, 16));
			jMemoryTypeLabel.setText("Memory Type");
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(6, 52, 107, 16));
			jLabel.setText("Memory size (KB)");
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJMemoryTypeComboBox(), null);
			jContentPane.add(getJMemorySizeTextField(), null);
			jContentPane.add(jLabel, null);
			jContentPane.add(jMemoryTypeLabel, null);
			jContentPane.add(getJTimeToSimulateTextField(), null);
			jContentPane.add(jTimeToSimulateLabel, null);
			jContentPane.add(getJAlgorithmComboBox(), null);
			jContentPane.add(jAlgorithmLabel, null);
			jContentPane.add(getJProcessFileTextField(), null);
			jContentPane.add(getJFileChooseButton(), null);
			jContentPane.add(jProcessFileLabel, null);
			jContentPane.add(getJProgressPanel(), null);
			jContentPane.add(getJSpeedFactorSlider(), null);
			jContentPane.add(jSpeedLabel, null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jMemoryTypeComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJMemoryTypeComboBox() {
		if (jMemoryTypeComboBox == null) {
			jMemoryTypeComboBox = new JComboBox();
			jMemoryTypeComboBox.setBounds(new Rectangle(6, 23, 196, 21));
			
			jMemoryTypeComboBox.addItem(new ComboBoxOption<Integer>("", 0));
			jMemoryTypeComboBox.addItem(new ComboBoxOption<Integer>("Simple Contiguous", 1));
			jMemoryTypeComboBox.addItem(new ComboBoxOption<Integer>("Swapping", 2));
			jMemoryTypeComboBox.addItem(new ComboBoxOption<Integer>("Fixed Partition", 3));
			jMemoryTypeComboBox.addItem(new ComboBoxOption<Integer>("Variable Partition", -1));
			
			jMemoryTypeComboBox.setSelectedIndex(1);
		}
		return jMemoryTypeComboBox;
	}

	/**
	 * This method initializes jMemorySizeTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJMemorySizeTextField() {
		if (jMemorySizeTextField == null) {
			jMemorySizeTextField = new IntegerTextField();
			jMemorySizeTextField.setBounds(new Rectangle(115, 50, 21, 20));
			jMemorySizeTextField.setText("300");
		}
		return jMemorySizeTextField;
	}

	/**
	 * This method initializes jTimeToSimulateTextField	
	 * 	
	 * @return ar.uba.dc.so.gui.component.IntegerTextField	
	 */
	private IntegerTextField getJTimeToSimulateTextField() {
		if (jTimeToSimulateTextField == null) {
			jTimeToSimulateTextField = new IntegerTextField();
			jTimeToSimulateTextField.setBounds(new Rectangle(171, 77, 22, 20));
			jTimeToSimulateTextField.setText("30");
		}
		return jTimeToSimulateTextField;
	}

	/**
	 * This method initializes jAlgorithmComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJAlgorithmComboBox() {
		if (jAlgorithmComboBox == null) {
			jAlgorithmComboBox = new JComboBox();
			jAlgorithmComboBox.setBounds(new Rectangle(220, 22, 131, 21));
			
			jAlgorithmComboBox.addItem(new ComboBoxOption<Integer>("First Free Zone", 4));
			jAlgorithmComboBox.addItem(new ComboBoxOption<Integer>("Best Zone", 5));
			jAlgorithmComboBox.addItem(new ComboBoxOption<Integer>("Worst Zone", 6));
			jAlgorithmComboBox.addItem(new ComboBoxOption<Integer>("First Free Zone (compactation)", 7));
			jAlgorithmComboBox.addItem(new ComboBoxOption<Integer>("Best Zone (compactation)", 8));
			jAlgorithmComboBox.addItem(new ComboBoxOption<Integer>("Worst Zone (compactation)", 9));
		}
		return jAlgorithmComboBox;
	}

	/**
	 * This method initializes jProcessFileTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJProcessFileTextField() {
		if (jProcessFileTextField == null) {
			jProcessFileTextField = new JTextField();
			jProcessFileTextField.setBounds(new Rectangle(7, 119, 207, 20));
			jProcessFileTextField.setEnabled(false);
		}
		return jProcessFileTextField;
	}

	/**
	 * This method initializes jFileChooseButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJFileChooseButton() {
		if (jFileChooseButton == null) {
			jFileChooseButton = new JButton();
			jFileChooseButton.setBounds(new Rectangle(214, 119, 82, 20));
			jFileChooseButton.setText("Choose");
			final JFileChooser fc = new JFileChooser(new File(Scheduler.DEFAULT_PPROCESSES_FILE_NAME));
			jFileChooseButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int retVal = fc.showOpenDialog(ControlWindow.this);
					
					if(retVal == JFileChooser.APPROVE_OPTION) {
						File processFile = fc.getSelectedFile();
						jProcessFileTextField.setText(processFile.getAbsolutePath());
					}
				}
			});
		}
		return jFileChooseButton;
	}

	/**
	 * This method initializes jProgressPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJProgressPanel() {
		if (jProgressPanel == null) {
			jInfoLabel = new JLabel();
			jInfoLabel.setText("Status: simulating; Fragmentation index: 2%");
			jProgressPanel = new JPanel();
			jProgressPanel.setLayout(new BorderLayout());
			jProgressPanel.setBounds(new Rectangle(3, 208, 355, 79));
			jProgressPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			jProgressPanel.add(getJSplitPane(), BorderLayout.NORTH);
			jProgressPanel.add(getJProgressBar(), BorderLayout.CENTER);
			jProgressPanel.add(jInfoLabel, BorderLayout.SOUTH);
		}
		return jProgressPanel;
	}

	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setLeftComponent(getJStartSimulationButton());
			jSplitPane.setRightComponent(getJStopSimulationButton());
			jSplitPane.setDividerLocation(170);
		}
		return jSplitPane;
	}

	/**
	 * This method initializes jStartSimulationButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJStartSimulationButton() {
		if (jStartSimulationButton == null) {
			jStartSimulationButton = new JButton();
			jStartSimulationButton.setText("Start");
			final ControlWindow cw = this;
			jStartSimulationButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Integer memType = ((ComboBoxOption<Integer>) jMemoryTypeComboBox.getSelectedItem()).getValue();
					if(memType == 0)
						return;
					else if (memType == -1) {
						memType = ((ComboBoxOption<Integer>) jAlgorithmComboBox.getSelectedItem()).getValue();
					}
					
					final Integer memorySizeInKb = Integer.parseInt(jMemorySizeTextField.getText());
					final Integer runForInSeconds = Integer.parseInt(jTimeToSimulateTextField.getText());
					final String processesFile = jProcessFileTextField.getText();
					final Integer fixedPartitionSizeInKb = 1;
					
					cw.getJProgressBar().setMaximum(runForInSeconds);
					
					try {
						final ProcessStatusChangeListener pscl = new ProcessStatusChangeListener() {
							
							@Override
							public void statusChanged(ProcessStatusChangeEvent e) {
								System.out.println("Process " + e.getProcess().id + " moved from " + e.getPreviousState() + " to " + e.getNextState());
								
								if(e.getPreviousState() != null) {
									switch(e.getPreviousState()) {
									case WAITING:
										pw.removeProcessWaiting(e.getProcess());
										break;
									case RUNNING:
										pw.removeProcessRunning(e.getProcess());
										break;
									case INTERRUPTED:
										pw.removeProcessInterrupted(e.getProcess());
										break;
									case FINISHED:
										pw.removeProcessFinished(e.getProcess());
										break;
									}
								}
								
								switch(e.getNextState()) {
								case WAITING:
									pw.addProcessWaiting(e.getProcess());
									break;
								case RUNNING:
									pw.addProcessRunning(e.getProcess());
									break;
								case INTERRUPTED:
									pw.addProcessInterrupted(e.getProcess());
									break;
								case FINISHED:
									pw.addProcessFinished(e.getProcess());
									break;
								}
							}
						};
						
						final SchedulerStepListener ssl = new SchedulerStepListener() {
							
							@Override
							public void schedullerStep(SchedulerStepEvent e) {
								cw.getJProgressBar().setValue(cw.getJProgressBar().getValue()+1);
								cw.getJProgressBar().setString("Elapsed simulation time: " + cw.getJProgressBar().getValue() + "s");
							}
						};
						
						// Launch a new thread so GUI is not affected
						final Integer memoryType = memType;
						if(tSimulator == null) {
							tSimulator = new Thread() {
								public void run() {
									try {
										CmdLineMode.run(cw.getJSpeedFactorSlider().getValue(), ssl, pscl, memoryType, memorySizeInKb, fixedPartitionSizeInKb, runForInSeconds, processesFile);
									}
									catch(Exception e) {
										System.err.println(e);
									}
								}
							};
							tSimulator.start();
						}
						else {
							tSimulator.resume();
						}
						
						getJStartSimulationButton().setEnabled(false);
						getJStopSimulationButton().setEnabled(true);
					}
					catch (Exception ex) {
						System.err.println(ex);
					}
				}
			});
		}
		return jStartSimulationButton;
	}

	/**
	 * This method initializes jStopSimulationButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJStopSimulationButton() {
		if (jStopSimulationButton == null) {
			jStopSimulationButton = new JButton();
			jStopSimulationButton.setText("Stop");
			jStopSimulationButton.setEnabled(false);
			jStopSimulationButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					tSimulator.suspend();
					
					getJStartSimulationButton().setEnabled(true);
					getJStopSimulationButton().setEnabled(false);
				}
			});
		}
		return jStopSimulationButton;
	}

	/**
	 * This method initializes jProgressBar	
	 * 	
	 * @return javax.swing.JProgressBar	
	 */
	private JProgressBar getJProgressBar() {
		if (jProgressBar == null) {
			jProgressBar = new JProgressBar();
			jProgressBar.setValue(0);
			jProgressBar.setStringPainted(true);
			jProgressBar.setString("0s");
			jProgressBar.setBackground(new Color(51, 255, 102));
			jProgressBar.setFont(new Font("Dialog", Font.BOLD, 12));
			jProgressBar.setForeground(new Color(51, 102, 255));
		}
		return jProgressBar;
	}

	/**
	 * This method initializes jSpeedFactorSlider	
	 * 	
	 * @return javax.swing.JSlider	
	 */
	private JSlider getJSpeedFactorSlider() {
		if (jSpeedFactorSlider == null) {
			jSpeedFactorSlider = new JSlider();
			jSpeedFactorSlider.setBounds(new Rectangle(8, 159, 352, 45));
			jSpeedFactorSlider.setMaximum(20);
			jSpeedFactorSlider.setValue(1);
			jSpeedFactorSlider.setMinorTickSpacing(1);
			jSpeedFactorSlider.setPaintTicks(true);
			jSpeedFactorSlider.setMinimum(1);
		}
		return jSpeedFactorSlider;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
