package ar.uba.dc.so.memoryManagement;

import java.util.ArrayList;
import java.util.List;

import ar.uba.dc.so.domain.Partition;
import ar.uba.dc.so.domain.Process;

public class MemoryVariablePartitionCompact {
	public static boolean compactAlloc(Process process, List<Partition> partitions) {
		int sizeInKb = 0;
		List<Integer> indexOfEmptyPartitions = new ArrayList<Integer>();
		List<Partition> emptyPartitions = new ArrayList<Partition>();
		
		for (Integer i = 0; i < partitions.size(); i++) {
			Partition partition = partitions.get(i);
			if (partition.isEmpty()) {
				sizeInKb += partition.sizeInKb;
				
				indexOfEmptyPartitions.add(i);
				emptyPartitions.add(partition);
				
				if (sizeInKb >= process.sizeInKb) {
					Partition toRemove = partitions.get(indexOfEmptyPartitions.get(0)); 
						
					Partition aux = new Partition(process.sizeInKb);
					aux.setProcessId(process.id);
					partitions.set(indexOfEmptyPartitions.get(0), aux);
					
					indexOfEmptyPartitions.remove(0);
					emptyPartitions.remove(toRemove);
					
					if (sizeInKb - process.sizeInKb > 0) {
						toRemove = partitions.get(indexOfEmptyPartitions.get(0));
						partitions.set(indexOfEmptyPartitions.get(0), new Partition(sizeInKb - process.sizeInKb));
						
						indexOfEmptyPartitions.remove(0);
						emptyPartitions.remove(toRemove);
					}
					for (Partition p : emptyPartitions)
						partitions.remove(p);
					return true;
				}
			} else {
				sizeInKb = 0;
				indexOfEmptyPartitions.clear();
			}
		}
		return false;
	}
}
