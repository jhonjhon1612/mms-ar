package ar.uba.dc.so.gui;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JDesktopPane;
import javax.swing.JRootPane;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import ar.uba.dc.so.memoryManagement.MemoryPaging;
import ar.uba.dc.so.memoryManagement.MemoryPagingByDemand;
import ar.uba.dc.so.memoryManagement.MemoryPagingByDemandTableModel;
import ar.uba.dc.so.memoryManagement.MemoryPagingTableModel;
import ar.uba.dc.so.memoryManagement.MemoryTableCellRenderer;
import java.awt.Dimension;

public class PageTableWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JDesktopPane jDesktopPane = null;
	private JTabbedPane jTabbedPane = null;
	private JScrollPane jScrollPane = null;
	private JTable jTable = null;
	private JScrollPane jScrollPane1 = null;
	private JTable jTable1 = null;

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
			
			MemoryTableCellRenderer mtCellRenderer = new MemoryTableCellRenderer();
			TableColumnModel tColModel = getJTable().getColumnModel();
			for(int i = 0; i < tColModel.getColumnCount(); i++) {
				tColModel.getColumn(i).setCellRenderer(mtCellRenderer);
			}
		}
		else
			getJTable().setModel(new MemoryPagingTableModel(m));
	}

	/**
	 * This method initializes jScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setViewportView(getJTable1());
		}
		return jScrollPane1;
	}

	/**
	 * This method initializes jTable1	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getJTable1() {
		if (jTable1 == null) {
			jTable1 = new JTable();
		}
		return jTable1;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
