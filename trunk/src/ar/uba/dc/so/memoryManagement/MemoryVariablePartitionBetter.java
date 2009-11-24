package ar.uba.dc.so.memoryManagement;

import ar.uba.dc.so.domain.Partition;
import ar.uba.dc.so.domain.Process;

public class MemoryVariablePartitionBetter extends MemoryVariablePartition {
	
	public MemoryVariablePartitionBetter(int sizeInKb) {
		super(sizeInKb);
	}

	@Override
	public boolean alloc(Process process) {
		Integer partitionId = null;
		Integer partitionSizeInKb = null;
		for (int i = 0; i < partitions.size(); i++) {
			Partition partition = partitions.get(i);
			if (partition.isEmpty() && partition.sizeInKb >= process.sizeInKb) {
				if (partitionSizeInKb == null || partitionSizeInKb > partition.sizeInKb) {
					partitionSizeInKb = partition.sizeInKb;
					partitionId = i;
				}
			}
		}
		if (partitionId == null)
			return false;
		else {
			_alloc(partitionId, process);
			return true;
		}
	}
}
