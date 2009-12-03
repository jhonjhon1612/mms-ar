package ar.uba.dc.so.domain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.event.EventListenerList;

import org.ho.yaml.Yaml;

import ar.uba.dc.so.memoryManagement.Memory;

public class Scheduler {
	public static String DEFAULT_PPROCESSES_FILE_NAME = "/Users/Ignacio/workspace/OSMMS/resources/processes.yml";
	private final Memory memory;
	public static Map<Integer, Process> processes = new HashMap<Integer, Process>();

	// Listeners
	private EventListenerList processStatusChange = new EventListenerList();
	private EventListenerList schedulerStep = new EventListenerList();
	
	private List<Process> processesWaiting = new ArrayList<Process>();
	private List<Process> processesRunning = new ArrayList<Process>();
	private Map<Process, Integer> processesInterrupted = new HashMap<Process, Integer>();
	private List<Process> processesFinished = new ArrayList<Process>();
	
	private Set<Integer> processesWereInterrupted = new HashSet<Integer>();
	
	private int timeInSeconds = 0;
	
	/*public Scheduler(Memory memory) throws IOException {
		this(DEFAULT_PPROCESSES_FILE_NAME, memory);
	}*/
	
	public Scheduler(Memory memory) throws IOException {
		this.memory = memory;
	}
	
	public void initialize(String processesFileName) throws IOException {
		readProcessesFile(processesFileName);
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
			
			fireProcessStatusChangeEvent(new ProcessStatusChangeEvent(this, 1, aux, null, ProcessState.WAITING));
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
					//System.out.println("Process " + process.id + " moved from running to finished.");
					memory.free(process.id);
					processesFinished.add(process);
					
					// Fire events
					fireProcessStatusChangeEvent(new ProcessStatusChangeEvent(this, 4, process, ProcessState.RUNNING, ProcessState.FINISHED));
				} else if (state == ProcessState.INTERRUPTED) {
					processesInterrupted.put(process, 2);
					processesWereInterrupted.add(process.id);
					if (memory.getClass().getName() == "MemorySwapping")
						memory.free(process.id);
					
					// Fire events
					fireProcessStatusChangeEvent(new ProcessStatusChangeEvent(this, 5, process, ProcessState.RUNNING, ProcessState.INTERRUPTED));
				}
			}
		}
		for (int i = 0; i < processesWaiting.size(); i++) {
			Process process = processesWaiting.get(i);
			System.out.println("Try to alloc process " + process.id + ".");
			if (!processesWereInterrupted.contains(process.id) || memory.getClass().getName() == "MemorySwapping") {
				if (memory.alloc(process)) {
					processesWaiting.remove(process); i--;
					processesRunning.add(process);
					
					// Fire events
					fireProcessStatusChangeEvent(new ProcessStatusChangeEvent(this, 2, process, ProcessState.WAITING, ProcessState.RUNNING));
				}
			} else {
				processesWaiting.remove(process); i--;
				processesRunning.add(process);
				
				// Fire events
				fireProcessStatusChangeEvent(new ProcessStatusChangeEvent(this, 2, process, ProcessState.WAITING, ProcessState.RUNNING));
			}
		}
		for (Process process : processesInterrupted.keySet()) {
			Integer remainTime = processesInterrupted.get(process) - 1;			
			if (remainTime == 0) {
				processesInterrupted.remove(process);
				processesWaiting.add(process);
				
				// Fire events
				fireProcessStatusChangeEvent(new ProcessStatusChangeEvent(this, 3, process, ProcessState.INTERRUPTED, ProcessState.WAITING));
			} else {
				System.out.println("Process " + process.id + " interrupted. " + remainTime + " seconds remain.");
				processesInterrupted.put(process, remainTime);
			}
		}
		if (processesRunning.size() == 0)
			System.out.println("Idle.");
		memory.writeLog();
		System.out.println("\n\n");
		
		// Fire event
		fireSchedulerStepEvent(new SchedulerStepEvent(this, 1));
	}
	
	
	// Events methods
	public void addProcessStatusChangeListener(ProcessStatusChangeListener listener) {
		processStatusChange.add(ProcessStatusChangeListener.class, listener);
	}
	
	protected void fireProcessStatusChangeEvent(ProcessStatusChangeEvent e) {
		Object[] listeners =  processStatusChange.getListenerList();
	     // loop through each listener and pass on the event if needed
	     Integer numListeners = listeners.length;
	     for (int i = 0; i<numListeners; i+=2) 
	     {
	          if (listeners[i]==ProcessStatusChangeListener.class) 
	          {
	               // pass the event to the listeners event dispatch method
	                ((ProcessStatusChangeListener)listeners[i+1]).statusChanged(e);
	          }            
	     }
	}
	
	public void addSchedulerStepListener(SchedulerStepListener listener) {
		schedulerStep.add(SchedulerStepListener.class, listener);
	}
	
	protected void fireSchedulerStepEvent(SchedulerStepEvent e) {
		Object[] listeners =  schedulerStep.getListenerList();
	     // loop through each listener and pass on the event if needed
	     Integer numListeners = listeners.length;
	     for (int i = 0; i<numListeners; i+=2) 
	     {
	          if (listeners[i]==SchedulerStepListener.class) 
	          {
	               // pass the event to the listeners event dispatch method
	                ((SchedulerStepListener)listeners[i+1]).schedullerStep(e);
	          }            
	     }
	}
}
