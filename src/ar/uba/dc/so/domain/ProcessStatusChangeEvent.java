package ar.uba.dc.so.domain;

import java.awt.AWTEvent;

@SuppressWarnings("serial")
public class ProcessStatusChangeEvent extends AWTEvent {
	private Process process;
	private ProcessState previousState;
	private ProcessState nextState;
	
	public ProcessStatusChangeEvent(Scheduler s, int id, Process p, ProcessState prevState, ProcessState nState) {
		super(s, id);
		
		process = p;
		previousState = prevState;
		nextState = nState;
	}
	
	public ProcessState getPreviousState() {
		return previousState;
	}
	
	public ProcessState getNextState() {
		return nextState;
	}
	
	public Process getProcess() {
		return process;
	}
}
