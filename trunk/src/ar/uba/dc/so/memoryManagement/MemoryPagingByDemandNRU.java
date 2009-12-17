package ar.uba.dc.so.memoryManagement;

public class MemoryPagingByDemandNRU extends MemoryPagingByDemand {
	public MemoryPagingByDemandNRU(int sizeInKb, int pageSizeInKb) throws Exception {
		super(sizeInKb, pageSizeInKb);
	}

	@Override
	public void freePartition() {
		Integer slectedProcessId = null;
		Integer selectedPageIndex = null;
		Integer lastUseInMemory = null;
		
		for (Integer processId : processesPages.keySet()) {
			for (Integer page : processesPages.get(processId).keySet()) {
				Integer aux = (Integer) processesPages.get(processId).get(page).get("lastUseInMemory");
				if ((Boolean) processesPages.get(processId).get(page).get("inMemory") && (lastUseInMemory == null || (aux != null && aux <= lastUseInMemory))) {
					lastUseInMemory = aux;
					slectedProcessId = processId;
					selectedPageIndex = page;
				}
			}
		}
		processesPages.get(slectedProcessId).get(selectedPageIndex).put("inMemory", Boolean.FALSE);
		freePartition((Integer) processesPages.get(slectedProcessId).get(selectedPageIndex).get("partitionPos"));
	}
}
