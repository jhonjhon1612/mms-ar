package ar.uba.dc.so.memoryManagement;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class MemoryTableCellRenderer extends DefaultTableCellRenderer {
	
	@ Override
	public Component getTableCellRendererComponent(  JTable table, Object value, boolean isSelected, boolean hasFocus,int row,int col) {
		Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
		String s = table.getModel().getValueAt(row, 2).toString();
		if(s.equalsIgnoreCase("false"))
			comp.setBackground(Color.RED);
		else
			comp.setBackground(Color.GREEN);
		
		return comp;
	}
}
