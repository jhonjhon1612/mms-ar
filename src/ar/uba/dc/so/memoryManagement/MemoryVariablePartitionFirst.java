package ar.uba.dc.so.memoryManagement;

import ar.uba.dc.so.domain.Partition;
import ar.uba.dc.so.domain.Process;

public class MemoryVariablePartitionFirst extends MemoryVariablePartition {
	
	public MemoryVariablePartitionFirst(int sizeInKb) {
		super(sizeInKb);
	}

	@Override
	public boolean alloc(Process process) {
		for (int i = 0; i < partitions.size(); i++) {
			Partition partition = partitions.get(i);
			if (partition.isEmpty() && partition.sizeInKb >= process.sizeInKb) {
				_alloc(i, process);
				return true;
			}
		}
		return false;
	}
}
