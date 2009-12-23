package ar.uba.dc.so.memoryManagement;

import java.util.HashMap;
import java.util.Map;

import javax.swing.table.DefaultTableModel;


@SuppressWarnings("serial")
public class MemoryPagingTableModel extends DefaultTableModel {

	private Map<Integer, Map<String, Object>> pagesInfo = null;
	
	public MemoryPagingTableModel(MemoryPaging m) {
		super();
		
		Object[] colIds = {"Process", "Page", "Partition Position"};
		this.setColumnIdentifiers(colIds);
		
		pagesInfo = new HashMap<Integer, Map<String, Object>>();
		int count = 0;
		for(Integer pId : m.processesPages.keySet()) {
			Map<Integer, Integer> map = m.processesPages.get(pId); 
			for(Integer page : map.keySet()) {
				HashMap<String, Object> pInfoToStore = new HashMap<String, Object>();
				
				pInfoToStore.put("processId", pId);
				pInfoToStore.put("page", page);
				pInfoToStore.put("partitionPos", map.get(page));
				
				pagesInfo.put(count++, pInfoToStore);
			}
		}
	}
	
	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public int getRowCount() {
		if(pagesInfo == null)
			return 0;
		return pagesInfo.keySet().size();
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		Map<String, Object> map = pagesInfo.get(arg0);
		switch(arg1) {
		case 0:
			return map.get("processId");
		case 1:
			return map.get("page");
		case 2:
			return map.get("partitionPos");
		}
		
		return null;
	}

}
