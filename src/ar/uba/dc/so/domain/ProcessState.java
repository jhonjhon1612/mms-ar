package ar.uba.dc.so.domain;

public enum ProcessState {
	INTERRUPTED, FINISHED, RUNNING, WAITING;
	
	public String toString() {
		switch(this) {
		case INTERRUPTED:
			return "INTERRUPTED";
		case FINISHED:
			return "FINISHED";
		case RUNNING:
			return "RUNNING";
		case WAITING:
			return "WAITING";
			default:
				return "";
		}
	}
}
