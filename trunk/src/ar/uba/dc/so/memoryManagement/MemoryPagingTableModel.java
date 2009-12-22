package ar.uba.dc.so.memoryManagement;

import javax.swing.table.AbstractTableModel;

import ar.uba.dc.so.domain.Partition;


@SuppressWarnings("serial")
public class MemoryPagingTableModel extends AbstractTableModel {

	private MemoryPaging m;
	
	public MemoryPagingTableModel(MemoryPaging m) {
		this.m = m;
	}
	
	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public int getRowCount() {
		return m.getNumberOfPages();
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		Partition p = m.partitions.get(arg0);
		switch(arg1) {
		case 0:
			if(p.isEmpty())
				return "Free";
			return "Process " + p.getProcessId();
		case 1:
			return arg0;
		case 2:
			return p.sizeInKb + "Kb";
		case 3:
			return "0x" + Integer.toHexString(p.sizeInKb * arg0).toUpperCase();
		}
		
		return null;
	}

}
