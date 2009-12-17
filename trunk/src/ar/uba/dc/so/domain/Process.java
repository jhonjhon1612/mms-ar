package ar.uba.dc.so.domain;

import java.util.ArrayList;

/**
 * This class models a light version of process.
 * Interruptions are simplified. All the interruptions have the same length.
 */
public class Process {
	public final int id;
	public final int sizeInKb;
	public final int timeInSeconds;
	public final ArrayList<Integer> interruptions;
	public final ArrayList<ArrayList<Integer>> positions;
	
	public int remainTimeInSeconds;
	
	public Process(int id, int sizeInKb, int timeInSeconds, ArrayList<Integer> interruptions, ArrayList<ArrayList<Integer>> positions) throws Exception {
		if(positions.size() != timeInSeconds)
			throw new Exception("There is a problem with process: " + id + " input data. The size of the positions list must be equal of the process in seconds");
		
		this.id = id;
		this.sizeInKb = sizeInKb;
		this.timeInSeconds = timeInSeconds;
		this.interruptions = interruptions;
		this.positions = positions;
		
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
		else if (interruptions.contains(timeInSeconds - remainTimeInSeconds))
			return ProcessState.INTERRUPTED;
		else
			return ProcessState.RUNNING;
	}
	
	public final ArrayList<Integer> memoryPositionsNeeded() {
		return positions.get(timeInSeconds - remainTimeInSeconds);
	}
}
