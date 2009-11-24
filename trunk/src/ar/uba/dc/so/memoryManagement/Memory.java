package ar.uba.dc.so.memoryManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import ar.uba.dc.so.domain.Partition;
import ar.uba.dc.so.domain.Process;
import ar.uba.dc.so.domain.Scheduler;

public abstract class Memory {
	public final int sizeInKb;
	protected List<Partition> partitions = new ArrayList<Partition>();
	
	public Memory(int sizeInKb) {
		this.sizeInKb = sizeInKb;
	}
	
	public void free(Integer processId) {
		for (int i = 0; i < partitions.size(); i++) {
			Partition partition = partitions.get(i);
			if (!partition.isEmpty() && partition.getProcessId() == processId) {
				partition.clear();
				break;
			}
		}
	}
	
	public abstract boolean alloc(Process process);
	
	public void render() {
		
	}
	
	public void writeLog() {
		System.out.println("The memory (" + sizeInKb + " Kb) have " + partitions.size() + " positions.");
		Integer usedSizeInKb = 0;
		for (int i = 0; i < partitions.size(); i++) {
			Partition partition = partitions.get(i);
			if (partition.isEmpty())
				System.out.println("The position " + i + " (" + partition.sizeInKb + " Kb) is empty.");
			else {
				Integer procSizeInKb = Scheduler.processes.get(partition.getProcessId()).sizeInKb;
				usedSizeInKb += procSizeInKb;
				System.out.println("The position " + i + " (" + partition.sizeInKb + " Kb) has process " + partition.getProcessId() + " (" + procSizeInKb + " Kb).");
				System.out.println(partition.sizeInKb - procSizeInKb + " Kb are not being used in the partition.");
			}
		}
		System.out.println(sizeInKb - usedSizeInKb + " Kb in the memory are not being used.");
	}
}
