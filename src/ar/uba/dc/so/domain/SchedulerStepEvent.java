package ar.uba.dc.so.domain;

import java.awt.AWTEvent;

@SuppressWarnings("serial")
public class SchedulerStepEvent extends AWTEvent {
	
	public SchedulerStepEvent(Scheduler s, int id) {
		super(s, id);
	}
}
