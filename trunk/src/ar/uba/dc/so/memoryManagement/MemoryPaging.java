package ar.uba.dc.so.memoryManagement;

import java.util.HashMap;
import java.util.Map;

import ar.uba.dc.so.domain.Partition;
import ar.uba.dc.so.domain.Process;
import ar.uba.dc.so.domain.Scheduler;

public class MemoryPaging extends Memory {
	public final int pageSizeInKb;
	protected Map<Integer,Map<Integer,Integer>> processesPages = new HashMap<Integer,Map<Integer,Integer>>();
	
	public MemoryPaging(int sizeInKb, int pageSizeInKb) throws Exception {
		super(sizeInKb);	
		if (sizeInKb % pageSizeInKb != 0)
			throw new Exception("The page size must be divider of the memory size.");
		this.pageSizeInKb = pageSizeInKb;
		initPartitions();
	}

	@Override
	public void initPartitions() {
		int numberOfPartitions = getNumberOfPages();
		for (Integer p = 1; p <= numberOfPartitions; p++)
			partitions.add(new Partition(this.pageSizeInKb));	
	}
	
	@Override
	public void free(Integer processId) {
		if (processesPages.containsKey(processId)) {
			Map<Integer,Integer> pages = processesPages.get(processId);
			for (Integer page : pages.keySet())
				freePartition(pages.get(page));
			processesPages.remove(processId);
		}
	}
	
	@Override
	public boolean alloc(Process process) {
		if (isAllocable(process)) {
			int neddedPartitions = getNumberOfPages(process);
			processesPages.put(process.id, new HashMap<Integer, Integer>());
			for (int page = 0; page < neddedPartitions; page++)
				loadPage(process.id, page);
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isAllocable(Process process) {
		int neddedPartitions = getNumberOfPages(process);
		if (neddedPartitions > getNumberOfPages())
			return false;
		for (int i = 0; i < partitions.size(); i++) {
			if (partitions.get(i).isEmpty())
				neddedPartitions--;
			if (neddedPartitions == 0)
				return true;
		}
		return false;
	}
	
	private boolean loadPage(Integer processId, Integer page) {
		Integer pos = getEmptyPartitionIndex();
		processesPages.get(processId).put(page, pos);
		createPartition(processId, pos);
		return true;
	}

	protected void createPartition(Integer processId, Integer pos) {
		Partition partition = new Partition(pageSizeInKb);
		partition.setProcessId(processId);
		partitions.set(pos, partition);
		
		usedSizeInKb += partition.sizeInKb;
		reallyUsedInKb += partition.sizeInKb;
	}
	
	protected Integer getEmptyPartitionIndex() {
		for (int i = 0; i < partitions.size(); i++) {
			if (partitions.get(i).isEmpty())
				return i;
		}
		return null;
	}
	
	protected void freePartition(Integer partitionPos) {
		partitions.get(partitionPos).clear();
		
		usedSizeInKb -= partitions.get(partitionPos).sizeInKb;
		reallyUsedInKb -= partitions.get(partitionPos).sizeInKb;
	}

	protected int getNumberOfPages() {
		return sizeInKb / pageSizeInKb;
	}

	protected int getNumberOfPages(Process process) {
		return (process.sizeInKb + pageSizeInKb - 1) / pageSizeInKb;
	}
	
	protected int getPageNumber(int positionKb) {
		return (int) java.lang.Math.floor(positionKb / pageSizeInKb);
	}
	
	protected Integer getPartitionProcSizeInKb(Integer partition, Integer processId) {
		for (Integer page : processesPages.get(processId).keySet()) {
			if (processesPages.get(processId).get(page) == partition) {
				Process process = Scheduler.processes.get(processId);
				if (page == getNumberOfPages(process) - 1 && (process.sizeInKb % pageSizeInKb) != 0) {
					return (process.sizeInKb % pageSizeInKb);
				} else
					return pageSizeInKb;
			}
		}
		return null;
	}

	public Integer getProcessPageNumber(Integer partition, Integer processId) {
		for (Integer page : processesPages.get(processId).keySet())
			if (processesPages.get(processId).get(page) == partition)
				return page;
		return null;
	}
	
	@Override
	public void writeLog() {
		writePageLog();
		writeMemoryLog();
	}

	protected void writePageLog() {
		for (Integer processId : processesPages.keySet()) {
			System.out.println("\nPages Table for the process " + processId + " have " + processesPages.get(processId).size() + " positions.");
			for (Integer page : processesPages.get(processId).keySet())
				System.out.println("Page " + page + " is in the " + processesPages.get(processId).get(page) + " position of the memory.");	
		}
		System.out.println("");
	}

	protected void writeMemoryLog() {
		System.out.println("The memory (" + sizeInKb + " Kb) have " + partitions.size() + " positions.");
		Integer usedSizeInKb = 0;
		for (int i = 0; i < partitions.size(); i++) {
			Partition partition = partitions.get(i);
			if (partition.isEmpty())
				System.out.println("The position " + i + " (" + partition.sizeInKb + " Kb) is empty.");
			else {
				Integer procSizeInKb = getPartitionProcSizeInKb(i, partition.getProcessId());
				usedSizeInKb += procSizeInKb;
				System.out.println("The position " + i + " (" + partition.sizeInKb + " Kb) has process " + partition.getProcessId() + " (" + procSizeInKb + " Kb).");
				if (partition.sizeInKb - procSizeInKb > 0)
					System.out.println(partition.sizeInKb - procSizeInKb + " Kb are not being used in the partition.");
			}
		}
		System.out.println(sizeInKb - usedSizeInKb + " Kb in the memory are not being used.\n");
	}
}
