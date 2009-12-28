package ar.uba.dc.so.gui;

import ar.uba.dc.so.domain.ProcessStatusChangeEvent;
import ar.uba.dc.so.domain.ProcessStatusChangeListener;

public class CustomProcessStatusChangeListener implements ProcessStatusChangeListener {
	private ProcessQueuesWindow pw = null;
	
	public CustomProcessStatusChangeListener(ProcessQueuesWindow pw) {
		this.pw = pw;
	}
	
	@Override
	public void statusChanged(ProcessStatusChangeEvent e) {
		if(e.getPreviousState() != null)
			System.out.println("Process " + e.getProcess().id + " moved from " + e.getPreviousState() + " to " + e.getNextState());
		else
			System.out.println("Process " + e.getProcess().id + " is now " + e.getNextState());
		
		if(e.getPreviousState() != null) {
			switch(e.getPreviousState()) {
			case WAITING:
				pw.removeProcessWaiting(e.getProcess());
				break;
			case RUNNING:
				pw.removeProcessRunning(e.getProcess());
				break;
			case INTERRUPTED:
				pw.removeProcessInterrupted(e.getProcess());
				break;
			case FINISHED:
				pw.removeProcessFinished(e.getProcess());
				break;
			}
		}
		
		switch(e.getNextState()) {
		case WAITING:
			pw.addProcessWaiting(e.getProcess());
			break;
		case RUNNING:
			pw.addProcessRunning(e.getProcess());
			break;
		case INTERRUPTED:
			pw.addProcessInterrupted(e.getProcess());
			break;
		case FINISHED:
			pw.addProcessFinished(e.getProcess());
			break;
		}
	}

}
