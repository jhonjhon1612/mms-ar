package ar.uba.dc.so.memoryManagement;

import ar.uba.dc.so.domain.Partition;
import ar.uba.dc.so.domain.Process;

public class MemorySimpleContiguous extends Memory {
	public MemorySimpleContiguous(int sizeInKb) {
		super(sizeInKb);
		partitions.add(new Partition(this.sizeInKb));
	}

	@Override
	public boolean alloc(Process process) {
		Partition partition = partitions.get(0);
		if (partition.isEmpty() && partition.sizeInKb >= process.sizeInKb) {
			partition.setProcessId(process.id);
			return true;
		} else
			return false;
	}
}
