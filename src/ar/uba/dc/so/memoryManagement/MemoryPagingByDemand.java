package ar.uba.dc.so.memoryManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ar.uba.dc.so.domain.Process;
import ar.uba.dc.so.domain.Scheduler;

public abstract class MemoryPagingByDemand extends MemoryPaging {
	protected Map<Integer,Map<Integer,Map<String, Object>>> processesPages = new HashMap<Integer,Map<Integer,Map<String, Object>>>();
	
	public MemoryPagingByDemand(int sizeInKb, int pageSizeInKb) throws Exception {
		super(sizeInKb, pageSizeInKb);
	}
	
	@Override
	public void free(Integer processId) {
		if (processesPages.containsKey(processId)) {
			Map<Integer,Map<String, Object>> pages = processesPages.get(processId);
			for (Integer page : pages.keySet()) {
				Map<String, Object> pageInfo = pages.get(page);
				if ((Boolean) pageInfo.get("inMemory"))
					freePartition((Integer) pageInfo.get("partitionPos"));
			}
			processesPages.remove(processId);
		}
	}
	
	@Override
	public boolean alloc(Process process) {
		if (!isAllocable(process))
			return false;
		boolean ret = true;
		if (!processesPages.containsKey(process.id))
			createProcessPages(process);
		ArrayList<Integer> positions = process.memoryPositionsNeeded();
		for (int i = 0; i < positions.size(); i++)
			positions.set(i, getPageNumber(positions.get(i)));
		Set<Integer> pages = new HashSet<Integer>(positions);
		for(Integer page : pages) {
			if (isPageLoaded(process.id, page))
				touch(process.id, page);
			else	
				ret = ret && loadPage(process.id, page);
		}
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	private boolean isAllocable(Process process) {
		int neddedPartitions = getNumberOfPages(process);
		if (neddedPartitions <= getNumberOfPages()) {
			List<Integer> neededPositions = (List<Integer>) process.memoryPositionsNeeded().clone();
			for (int i = 0; i < neededPositions.size(); i++)
				neededPositions.set(i, getPageNumber(neededPositions.get(i)));
			neddedPartitions = (new HashSet(neededPositions)).size();
			
			for (int i = 0; i < partitions.size(); i++) {
				if (partitions.get(i).isEmpty())
					neddedPartitions--;
				if (neddedPartitions == 0)
					return true;
			}
			
			for (Integer processId : processesPages.keySet()) {
				for (Integer page : processesPages.get(processId).keySet()) {
					if ((Boolean) processesPages.get(processId).get(page).get("inMemory")) {
						Integer aux = (Integer) processesPages.get(processId).get(page).get("lastUseInMemory");
						if (aux < Scheduler.getTimeInSeconds()) {
							neddedPartitions--;
							if (neddedPartitions == 0)
								return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	private void createProcessPages(Process process) {
		int neddedPartitions = getNumberOfPages(process);
		Map<Integer,Map<String, Object>> processPages = new HashMap<Integer, Map<String, Object>>();
		for (int page = 0; page < neddedPartitions; page++) {
			Map<String, Object> pageInfo = new HashMap<String, Object>();
			pageInfo.put("partitionPos", null);
			pageInfo.put("inMemory", Boolean.FALSE);
			pageInfo.put("allocatedInMemory", null);
			pageInfo.put("lastUse", null);
			pageInfo.put("usesCount", null);
			pageInfo.put("lastUseInMemory", null);
			pageInfo.put("usesCountInMemory", null);
			processPages.put(page, pageInfo);
		}
		processesPages.put(process.id, processPages);
	}
	
	private boolean loadPage(Integer processId, Integer page) {
		if (!isEmptyPartitionOnMemory())
			freePartition();
		Integer pos = getEmptyPartitionIndex();
		Map<String, Object> pageInfo = processesPages.get(processId).get(page);
		pageInfo.put("partitionPos", pos);
		pageInfo.put("inMemory", Boolean.TRUE);
		pageInfo.put("allocatedInMemory", Scheduler.getTimeInSeconds());
		createPartition(processId, pos);
		return touch(processId, page);
	}
	
	private boolean isEmptyPartitionOnMemory() {
		return (getEmptyPartitionIndex() != null);
	}
	
	private boolean touch(Integer processId, Integer page) {
		if (isPageLoaded(processId, page)) {
			Map<String, Object> pageInfo = processesPages.get(processId).get(page);
			pageInfo.put("lastUse", Scheduler.getTimeInSeconds());
			Integer usesCount = (Integer) pageInfo.get("usesCount");
			pageInfo.put("usesCount", ((usesCount == null) ? 1 : usesCount + 1));
			pageInfo.put("lastUseInMemory", Scheduler.getTimeInSeconds());
			Integer usesCountInMemory = (Integer) pageInfo.get("usesCountInMemory");
			pageInfo.put("usesCountInMemory", ((usesCountInMemory == null) ? 1 : usesCountInMemory + 1));
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isPageLoaded(Integer processId, Integer page) {
		return (Boolean) processesPages.get(processId).get(page).get("inMemory");
	}
	
	@Override
	protected Integer getPartitionProcSizeInKb(Integer partition, Integer processId) {
		for (Integer page : processesPages.get(processId).keySet()) {
			if (processesPages.get(processId).get(page).get("partitionPos") == partition) {
				Process process = Scheduler.processes.get(processId);
				if (page < getNumberOfPages(process))
					return pageSizeInKb;
				else
					return (process.sizeInKb % pageSizeInKb);
			}
		}
		return null;
	}
	
	@Override
	protected void writePageLog() {
		for (Integer processId : processesPages.keySet()) {
			System.out.println("\nPages Table for the process " + processId + " have " + processesPages.get(processId).size() + " positions.");
			for (Integer page : processesPages.get(processId).keySet()) {
				Map<String, Object> pageInfo = processesPages.get(processId).get(page);
				Integer partitionPos = (Integer) pageInfo.get("partitionPos");
				Boolean inMemory = (Boolean) pageInfo.get("inMemory");
				Integer lastUse = (Integer) pageInfo.get("lastUse");
				Integer usesCount = (Integer) pageInfo.get("usesCount");
				Integer allocatedInMemory = (Integer) pageInfo.get("allocatedInMemory");
				Integer lastUseInMemory = (Integer) pageInfo.get("lastUseInMemory");
				Integer usesCountInMemory = (Integer) pageInfo.get("usesCountInMemory");
					
				if (inMemory) {
				System.out.println("Page " + page + "\tis allocated in memory: position " + partitionPos.toString());	
					System.out.println("\tAllocated in memory at second: " + allocatedInMemory.toString());
					System.out.println("\tLast use in memory at second: " + lastUseInMemory.toString());
					System.out.println("\tUses count in memory: " + usesCountInMemory.toString());
				} else
					System.out.println("Page " + page + "\tis not allocated in memory.");	
				if (lastUse != null)
					System.out.println("\tLast use at second: " + lastUse.toString());
				if (usesCount != null)
					System.out.println("\tUses count: " + usesCount.toString());
			}
			System.out.println("");
		}
	}

	protected abstract void freePartition();
}
