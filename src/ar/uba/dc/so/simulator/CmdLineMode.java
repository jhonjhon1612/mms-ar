package ar.uba.dc.so.simulator;

import java.io.File;

import ar.uba.dc.so.domain.ProcessStatusChangeEvent;
import ar.uba.dc.so.domain.ProcessStatusChangeListener;
import ar.uba.dc.so.domain.Scheduler;
import ar.uba.dc.so.memoryManagement.Memory;
import ar.uba.dc.so.memoryManagement.MemoryFixedPartition;
import ar.uba.dc.so.memoryManagement.MemorySimpleContiguous;
import ar.uba.dc.so.memoryManagement.MemorySwapping;
import ar.uba.dc.so.memoryManagement.MemoryVariablePartitionBetter;
import ar.uba.dc.so.memoryManagement.MemoryVariablePartitionBetterCompact;
import ar.uba.dc.so.memoryManagement.MemoryVariablePartitionFirst;
import ar.uba.dc.so.memoryManagement.MemoryVariablePartitionFirstCompact;
import ar.uba.dc.so.memoryManagement.MemoryVariablePartitionWorst;
import ar.uba.dc.so.memoryManagement.MemoryVariablePartitionWorstCompact;

public class CmdLineMode {
	public static void printHelp() {
		System.out.println("\nNAME");
		System.out.println("\tOSMMS -- Operating System Memory Manager Simulator\n");
		System.out.println("DESCRIPTION");
		System.out.println("\tSimulates different type of OS memory managers, in some cases with different algorithms.");
		System.out.println("\tSimulation can be graphic or by console.\n");
		System.out.println("USAGE");
		System.out.println("\tjava -jar OSMMS -g");
		System.out.println("\tjava -jar OSMMS -m 6 -s 64 -t 2 -p \"/Path/To/Processes/File/processes.yml\"");
		System.out.println("\tjava -jar OSMMS -m 3 - f 16 -s 64 -t 2 -p \"/Path/To/Processes/File/processes.yml\"\n");
		System.out.println("OPTIONS");
		System.out.println("\t-m, -memoryManager\tMemory manager to simulate. Avaiable memory managers are:");
		System.out.println("\t\t1) Simple contiguous.");
		System.out.println("\t\t2) Swapping.");
		System.out.println("\t\t3) Fixed partition.");
		System.out.println("\t\t4) Variable partition 'First Free Zone'.");
		System.out.println("\t\t5) Variable partition 'Best Zone'.");
		System.out.println("\t\t6) Variable partition 'Worst Zone'.");
		System.out.println("\t\t7) Variable partition 'First Free Zone' (with compactation).");
		System.out.println("\t\t8) Variable partition 'Best Zone' (with compactation).");
		System.out.println("\t\t9) Variable partition 'Worst Zone' (with compactation).\n");
		System.out.println("\t-s, -memorySize\t\tThe memory size in Kb. Must be a possitive number, bigger than zero.\n");
		System.out.println("\t-t, -time\t\tTime to simulate in seconds. Must be a possitive number, bigger than zero.\n");
		System.out.println("\t-p, -processesFile\tYaml file with with the processes to simulate. Sample processes definition are:");
		System.out.println("\t- !java.util.HashMap\n\tid: 2\n\tsizeInKb: 9\n\ttimeInSeconds: 7\n\tinterruptions: [2, 5]\n");
		System.out.println("\t- !java.util.HashMap\n\tid: 3\n\tsizeInKb: 23\n\ttimeInSeconds: 17\n\tinterruptions: []\n");
		System.out.println("\t-f, -fixedPartitionSize\t[ONLY FOR FIXED PARTITION MODE] The memory partition size in Kb. Must be a possitive number, bigger than zero.");	
		System.out.println("\t-g, -graphicMode\tIts FALSE by default (if its not setted).\n");	
		System.exit(0);
	}
	
	public static void run(Integer memoryType, Integer memorySizeInKb, Integer fixedPartitionSizeInKb, Integer runForInSeconds, String processesFile) throws Exception {
		run(null, memoryType, memorySizeInKb, fixedPartitionSizeInKb, runForInSeconds, processesFile);
	}	
	
	public static void run(ProcessStatusChangeListener pscl, Integer memoryType, Integer memorySizeInKb, Integer fixedPartitionSizeInKb, Integer runForInSeconds, String processesFile) throws Exception {
		if (
		(memoryType == null || memorySizeInKb == null || runForInSeconds == null || processesFile == null) || (memoryType == 3 && fixedPartitionSizeInKb == null) || 
		!(new File(processesFile)).exists() || 
		(memoryType < 1) || (memoryType > 9) ||
		memorySizeInKb < 1 ||
		runForInSeconds < 1 ||
		(fixedPartitionSizeInKb != null && fixedPartitionSizeInKb < 1)
		) {
			printHelp();
			System.exit(0);
		}
		Memory memory;
		memory = new MemorySimpleContiguous(memorySizeInKb);
		switch (memoryType) {
			case 1:
				memory = new MemorySimpleContiguous(memorySizeInKb);
				break;
			case 2:
				memory = new MemorySwapping(memorySizeInKb);
				break;
			case 3:
				memory = new MemoryFixedPartition(memorySizeInKb, fixedPartitionSizeInKb);
				break;
			case 4:
				memory = new MemoryVariablePartitionFirst(memorySizeInKb);
				break;
			case 5:
				memory = new MemoryVariablePartitionBetter(memorySizeInKb);
				break;
			case 6:
				memory = new MemoryVariablePartitionWorst(memorySizeInKb);
				break;
			case 7:
				memory = new MemoryVariablePartitionFirstCompact(memorySizeInKb);
				break;
			case 8:
				memory = new MemoryVariablePartitionBetterCompact(memorySizeInKb);
				break;
			case 9:
				memory = new MemoryVariablePartitionWorstCompact(memorySizeInKb);
				break;
		}
		Scheduler scheduler = new Scheduler(memory);
		if(pscl != null)
			scheduler.addProcessStatusChangeListener(pscl);
		scheduler.initialize(processesFile);
		
		try {
			for (int i = 0; i < runForInSeconds; i++) {
				scheduler.incrementTime();
				Thread.sleep(1000); // 1000 ms = 1s
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
