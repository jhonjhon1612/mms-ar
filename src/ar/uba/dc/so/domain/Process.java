package ar.uba.dc.so.domain;

import java.util.ArrayList;
import java.util.logging.Logger;
/**
 * This class models a light version of process.
 * Interruptions are simplified. All the interruptions have the same length.
 */
public class Process {
	public final int id;
	public final int sizeInKb;
	public final int timeInSeconds;
	public final ArrayList<Integer> interruptions;
	
	private int remainTimeInSeconds;
	
	public Process(int id, int sizeInKb, int timeInSeconds,
			ArrayList<Integer> interruptions) {
		this.id = id;
		this.sizeInKb = sizeInKb;
		this.timeInSeconds = timeInSeconds;
		this.interruptions = interruptions;
		
		remainTimeInSeconds = this.timeInSeconds;
	}

	public final int getRemainTimeInSeconds() {
		return remainTimeInSeconds;
	}

	public final ProcessState decrementRemainTime() {
		remainTimeInSeconds--;
		System.out.println("Process " + id + ", " + remainTimeInSeconds + " seconds remain.");
		if (remainTimeInSeconds == 0)
			return ProcessState.FINISHED;
		else if (interruptions.contains(remainTimeInSeconds))
			return ProcessState.INTERRUPTED;
		else
			return ProcessState.RUNNING;
	}
}
