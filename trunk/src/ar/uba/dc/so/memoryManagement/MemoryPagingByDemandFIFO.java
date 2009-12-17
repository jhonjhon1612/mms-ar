package ar.uba.dc.so.memoryManagement;

public class MemoryPagingByDemandFIFO extends MemoryPagingByDemand {
	public MemoryPagingByDemandFIFO(int sizeInKb, int pageSizeInKb) throws Exception {
		super(sizeInKb, pageSizeInKb);
	}

	@Override
	public void freePartition() {
		Integer slectedProcessId = null;
		Integer selectedPageIndex = null;
		Integer allocatedInMemory = null;
		
		for (Integer processId : processesPages.keySet()) {
			for (Integer page : processesPages.get(processId).keySet()) {
				Integer aux = (Integer) processesPages.get(processId).get(page).get("allocatedInMemory");
				if ((Boolean) processesPages.get(processId).get(page).get("inMemory") && (allocatedInMemory == null || (aux != null && aux <= allocatedInMemory))) {
					allocatedInMemory = aux;
					slectedProcessId = processId;
					selectedPageIndex = page;
				}
			}
		}
		processesPages.get(slectedProcessId).get(selectedPageIndex).put("inMemory", Boolean.FALSE);
		freePartition((Integer) processesPages.get(slectedProcessId).get(selectedPageIndex).get("partitionPos"));
	}
}
