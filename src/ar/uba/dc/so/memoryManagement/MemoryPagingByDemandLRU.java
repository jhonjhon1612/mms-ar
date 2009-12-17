package ar.uba.dc.so.memoryManagement;

public class MemoryPagingByDemandLRU extends MemoryPagingByDemand {
	public MemoryPagingByDemandLRU(int sizeInKb, int pageSizeInKb) throws Exception {
		super(sizeInKb, pageSizeInKb);
	}

	@Override
	public void freePartition() {
		Integer slectedProcessId = null;
		Integer selectedPageIndex = null;
		Integer usesCountInMemory = null;
		
		for (Integer processId : processesPages.keySet()) {
			for (Integer page : processesPages.get(processId).keySet()) {
				Integer aux = (Integer) processesPages.get(processId).get(page).get("usesCountInMemory");
				if ((Boolean) processesPages.get(processId).get(page).get("inMemory") && (usesCountInMemory == null || (aux!= null && aux <= usesCountInMemory))) {
					usesCountInMemory = aux;
					slectedProcessId = processId;
					selectedPageIndex = page;
				}
			}
		}
		processesPages.get(slectedProcessId).get(selectedPageIndex).put("inMemory", Boolean.FALSE);
		freePartition((Integer) processesPages.get(slectedProcessId).get(selectedPageIndex).get("partitionPos"));
	}
}
