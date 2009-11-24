package ar.uba.dc.so.domain;

public class Partition {
	public final int sizeInKb;
	private Integer processId;
	
	public Partition(int sizeInKb) {
		this.sizeInKb = sizeInKb;
	}

	public boolean isEmpty() {
		return (processId == null);
	}
	
	public void clear() {
		processId = null;
	}
	
	public boolean setProcessId(Integer processId) {
		if (!isEmpty())
			return false;
		this.processId = processId;
		return true;
	}
	
	public Integer getProcessId() {
		return processId;
	}
}
