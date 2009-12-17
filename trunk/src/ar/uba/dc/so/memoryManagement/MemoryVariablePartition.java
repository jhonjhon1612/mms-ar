package ar.uba.dc.so.memoryManagement;

import ar.uba.dc.so.domain.Partition;
import ar.uba.dc.so.domain.Process;

public abstract class MemoryVariablePartition extends Memory {
	public MemoryVariablePartition(int sizeInKb) {
		super(sizeInKb);
		initPartitions();
	}
	
	public void initPartitions() {
		partitions.add(new Partition(this.sizeInKb));
	}
	
	protected void _alloc(Integer partitionId, Process process) {
		for (int i = partitions.size(); i > (partitionId+1); i--) {
			if (i < partitions.size())
				partitions.set(i, partitions.get(i-1));
			else
				partitions.add(partitions.get(i-1));
		}
		Partition aux = new Partition(process.sizeInKb);
		
		usedSizeInKb += aux.sizeInKb;
		reallyUsedInKb += process.sizeInKb;
		
		int freeMemory = partitions.get(partitionId).sizeInKb - process.sizeInKb;
		
		aux.setProcessId(process.id);
		partitions.set(partitionId, aux);
		if (freeMemory > 0) {
			aux = new Partition(freeMemory);
			if (partitionId+1 < partitions.size())
				partitions.set(partitionId+1, aux);
			else
				partitions.add(aux);
		} else {
			if (partitionId+1 < partitions.size())
				partitions.remove(partitionId+1);
		}
	}
}
