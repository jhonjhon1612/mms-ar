package ar.uba.dc.so.domain;

import java.util.EventListener;

public interface SchedulerStepListener extends EventListener {
	public abstract void schedullerStep(SchedulerStepEvent e);
}
