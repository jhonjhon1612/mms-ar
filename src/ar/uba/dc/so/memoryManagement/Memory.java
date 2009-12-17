package ar.uba.dc.so.memoryManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import ar.uba.dc.so.domain.Partition;
import ar.uba.dc.so.domain.Process;
import ar.uba.dc.so.domain.Scheduler;

public abstract class Memory {
	public final int sizeInKb;
	protected int usedSizeInKb = 0;
	protected int reallyUsedInKb = 0;
	
	protected List<Partition> partitions = new ArrayList<Partition>();
	
	public Memory(int sizeInKb) {
		this.sizeInKb = sizeInKb;
	}
	
	public void free(Integer processId) {
		for (int i = 0; i < partitions.size(); i++) {
			Partition partition = partitions.get(i);
			if (!partition.isEmpty() && partition.getProcessId() == processId) {
				partition.clear();
				
				// Le resto el tamaÃ±o de la particiÃ³n usada
				// acoto por abajo para cuando la particiÃ³n es una sola
				usedSizeInKb -= partition.sizeInKb;
				usedSizeInKb = (usedSizeInKb < 0)?0:usedSizeInKb;
				
				// TODO Esto no está del todo bien...
				Process process = Scheduler.processes.get(processId);
				if(process != null) // Dónde están los procesos que terminan?
					reallyUsedInKb -= process.sizeInKb;
				
				break;
			}
		}
	}
	
	public int getFreeSize() {
		return sizeInKb - getAllocedSize();
	}
	
	public int getAllocedSize() {
		return usedSizeInKb;
	}
	
	public abstract boolean alloc(Process process);
	
	public abstract void initPartitions();
	
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
				
				if(partition.sizeInKb - procSizeInKb > 0)
					System.out.println(partition.sizeInKb - procSizeInKb + " Kb are not being used in the partition.");
			}
		}
		System.out.println(sizeInKb - usedSizeInKb + " Kb in the memory are not being used.");
	}

	public final List<Partition> getPartitions() {
		return partitions;
	}
	
	public int getReallyUsedMemory() {
		return reallyUsedInKb;
	}
	
	public int getWastedMemory() {
		return usedSizeInKb-reallyUsedInKb; 
	}
}
