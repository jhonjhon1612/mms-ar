package ar.uba.dc.so.domain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.ho.yaml.Yaml;

import ar.uba.dc.so.memoryManagement.Memory;

public class Scheduler {
	private static final String DEFAULT_PPROCESSES_FILE_NAME = "/Users/damianburszytn/Code/SOMS/resources/processes.yml";
	private final Memory memory;
	public static Map<Integer, Process> processes = new HashMap<Integer, Process>();

	private List<Process> processesWaiting = new ArrayList<Process>();
	private List<Process> processesRunning = new ArrayList<Process>();
	private Map<Process, Integer> processesInterrupted = new HashMap<Process, Integer>();
	private List<Process> processesFinished = new ArrayList<Process>();
	
	private Set<Integer> processesWereInterrupted = new HashSet<Integer>();
	
	private int timeInSeconds = 0;
	
	public Scheduler(Memory memory) throws IOException {
		this(DEFAULT_PPROCESSES_FILE_NAME, memory);
	}
	
	public Scheduler(String processesFileName, Memory memory) throws IOException {
		readProcessesFile(processesFileName);
		this.memory = memory;
	}
	
	@SuppressWarnings("unchecked")
	private final void readProcessesFile(String processesFileName) throws IOException {
		ArrayList<HashMap<String, Object>> processesInput = Yaml.loadType(new File(processesFileName), ArrayList.class);
		for (HashMap<String, Object> m : processesInput) {
			int id = ((Integer) m.get("id")).intValue();
			int sizeInKb = ((Integer) m.get("sizeInKb")).intValue();
			int timeInSeconds = ((Integer) m.get("timeInSeconds")).intValue();
			ArrayList<Integer> interruptions = (ArrayList<Integer>) m.get("interruptions");
			Process aux = new Process(id, sizeInKb, timeInSeconds, interruptions);
			processesWaiting.add(aux);
			processes.put(id, aux);
		}
	}
	
	public final int getTimeInSeconds() {
		return timeInSeconds;
	}

	public final void incrementTime() throws Exception {
		timeInSeconds++;
		System.out.println("Second: " + timeInSeconds);
		for (int i = 0; i < processesRunning.size(); i++) {
			Process process = processesRunning.get(i);
			ProcessState state = process.decrementRemainTime();
			if (state != ProcessState.RUNNING) {
				processesRunning.remove(process); i--;
				if (state == ProcessState.FINISHED) {
					System.out.println("Process " + process.id + " moved from running to finished.");
					memory.free(process.id);
					processesFinished.add(process);
				} else if (state == ProcessState.INTERRUPTED) {
					System.out.println("Process " + process.id + " moved from running to interrupted.");
					processesInterrupted.put(process, 2);
					processesWereInterrupted.add(process.id);
					if (memory.getClass().getName() == "MemorySwapping")
						memory.free(process.id);
				}
			}
		}
		for (int i = 0; i < processesWaiting.size(); i++) {
			Process process = processesWaiting.get(i);
			System.out.println("Try to alloc process " + process.id + ".");
			if (!processesWereInterrupted.contains(process.id) || memory.getClass().getName() == "MemorySwapping") {
				if (memory.alloc(process)) {
					System.out.println("Process " + process.id + " moved from waiting to running.");
					processesWaiting.remove(process); i--;
					processesRunning.add(process);
				}
			} else {
				System.out.println("Process " + process.id + " moved from waiting to running.");
				processesWaiting.remove(process); i--;
				processesRunning.add(process);
			}
		}
		for (Process process : processesInterrupted.keySet()) {
			Integer remainTime = processesInterrupted.get(process) - 1;			
			if (remainTime == 0) {
				System.out.println("Process " + process.id + " moved from interrupted to wating.");
				processesInterrupted.remove(process);
				processesWaiting.add(process);
			} else {
				System.out.println("Process " + process.id + " interrupted. " + remainTime + " seconds remain.");
				processesInterrupted.put(process, remainTime);
			}
		}
		if (processesRunning.size() == 0)
			System.out.println("Idle.");
		memory.writeLog();
		System.out.println("\n\n");
		Thread.sleep(1000); // 1000 ms = 1 s
	}
}
