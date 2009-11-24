package ar.uba.dc.so.memoryManagement;

import ar.uba.dc.so.domain.Process;

public class MemoryVariablePartitionWorstCompact extends MemoryVariablePartitionWorst {
	
	public MemoryVariablePartitionWorstCompact(int sizeInKb) {
		super(sizeInKb);
	}

	@Override
	public boolean alloc(Process process) {
		if (super.alloc(process))
			return true;
		else
			return MemoryVariablePartitionCompact.compactAlloc(process, partitions);
	}
}
