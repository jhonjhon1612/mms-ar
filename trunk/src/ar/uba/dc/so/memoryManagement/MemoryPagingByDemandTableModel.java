package ar.uba.dc.so.memoryManagement;

import java.util.HashMap;
import java.util.Map;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import ar.uba.dc.so.domain.Partition;


@SuppressWarnings("serial")
public class MemoryPagingByDemandTableModel extends DefaultTableModel {

	private MemoryPagingByDemand m;
	private Map<Integer, Map<String,Object>> pagesInfo = null;
	
	public MemoryPagingByDemandTableModel(MemoryPagingByDemand m) {
		super();
		
		this.m = m;
		Object[] colIds = {"Process", "Partition Position", "In memory?", "Allocated in memory?", "Last use", "Uses count", "Last use in memory", "Uses count in memory"};
		this.setColumnIdentifiers(colIds);
		
		pagesInfo = new HashMap<Integer, Map<String,Object>>();
		int count = 0;
		for(Integer pId : m.processesPages.keySet()) {
			Map<Integer, Map<String, Object>> map = m.processesPages.get(pId); 
			for(Integer page : map.keySet()) {
				Map<String, Object> pInfo = map.get(page);
				HashMap<String, Object> pInfoToStore = new HashMap<String, Object>(pInfo);
				
				pInfoToStore.put("processId", pId);
				pInfoToStore.put("page", page);
				
				pagesInfo.put(count++, pInfoToStore);
			}
		}
	}
	
	@Override
	public int getColumnCount() {
		return 8;
	}

	@Override
	public int getRowCount() {
		if(pagesInfo == null)
			return 0;
		return pagesInfo.keySet().size();
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		Map<String, Object> pageInfo = pagesInfo.get(arg0);
		
		switch(arg1) {
		case 0:
			return pageInfo.get("processId");
		case 1:
			return pageInfo.get("partitionPos");
		case 2:
			return pageInfo.get("inMemory");
		case 3:
			return pageInfo.get("allocatedInMemory");
		case 4:
			return pageInfo.get("lastUse");
		case 5:
			return pageInfo.get("usesCount");
		case 6:
			return pageInfo.get("lastUseInMemory");
		case 7:
			return pageInfo.get("usesCountInMemory");
		}
		
		return null;
	}

}
