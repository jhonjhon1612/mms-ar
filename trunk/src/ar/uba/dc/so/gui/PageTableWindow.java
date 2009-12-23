package ar.uba.dc.so.gui;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JDesktopPane;
import javax.swing.JRootPane;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import ar.uba.dc.so.memoryManagement.MemoryPaging;
import ar.uba.dc.so.memoryManagement.MemoryPagingByDemand;
import ar.uba.dc.so.memoryManagement.MemoryPagingByDemandTableModel;
import ar.uba.dc.so.memoryManagement.MemoryPagingTableModel;
import ar.uba.dc.so.memoryManagement.MemoryPagingByDemandTableCellRenderer;

public class PageTableWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JDesktopPane jDesktopPane = null;
	private JTabbedPane jTabbedPane = null;
	private JScrollPane jScrollPane = null;
	private JTable jTable = null;

	/**
	 * This is the default constructor
	 */
	public PageTableWindow() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(726, 279);
		this.setLocation(5, 568);
		
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
		
		this.setUndecorated(true);
		this.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJDesktopPane(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jDesktopPane	
	 * 	
	 * @return javax.swing.JDesktopPane	
	 */
	private JDesktopPane getJDesktopPane() {
		if (jDesktopPane == null) {
			jDesktopPane = new JDesktopPane();
			jDesktopPane.setLayout(new BorderLayout());
			jDesktopPane.add(getJTabbedPane(), BorderLayout.CENTER);
		}
		return jDesktopPane;
	}

	/**
	 * This method initializes jTabbedPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.addTab("Pages X Process", null, getJScrollPane(), null);
			//jTabbedPane.addTab(null, null, getJScrollPane1(), null);
		}
		return jTabbedPane;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJTable());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getJTable() {
		if (jTable == null) {
			jTable = new JTable();
		}
		return jTable;
	}
	
	public void draw(MemoryPaging m) {
		if(m instanceof MemoryPagingByDemand) {
			getJTable().setModel(new MemoryPagingByDemandTableModel((MemoryPagingByDemand) m));
			
			MemoryPagingByDemandTableCellRenderer mtCellRenderer = new MemoryPagingByDemandTableCellRenderer();
			TableColumnModel tColModel = getJTable().getColumnModel();
			for(int i = 0; i < tColModel.getColumnCount(); i++) {
				tColModel.getColumn(i).setCellRenderer(mtCellRenderer);
			}
		}
		else
			getJTable().setModel(new MemoryPagingTableModel(m));
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
