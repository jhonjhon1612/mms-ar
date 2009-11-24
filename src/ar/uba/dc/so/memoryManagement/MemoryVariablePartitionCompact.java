package ar.uba.dc.so.memoryManagement;

import java.util.ArrayList;
import java.util.List;

import ar.uba.dc.so.domain.Partition;
import ar.uba.dc.so.domain.Process;

public class MemoryVariablePartitionCompact {
	public static boolean compactAlloc(Process process, List<Partition> partitions) {
		int sizeInKb = 0;
		List<Integer> emptyPartitions = new ArrayList<Integer>();
		
		for (Partition partition : partitions) {
			if (partition.isEmpty()) {
				sizeInKb += partition.sizeInKb;
				if (sizeInKb >= process.sizeInKb) {
					partitions.set(emptyPartitions.get(0), new Partition(process.sizeInKb));
					emptyPartitions.remove(0);
					if (sizeInKb - process.sizeInKb > 0) {
						partitions.set(emptyPartitions.get(0), new Partition(sizeInKb - process.sizeInKb));
						emptyPartitions.remove(0);
					}
					for (Integer i : emptyPartitions) {
						partitions.remove(i);
					}
				}
			} else {
				sizeInKb = 0;
				emptyPartitions.clear();
			}
		}
		return false;
	}
}
