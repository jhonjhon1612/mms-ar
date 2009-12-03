package ar.uba.dc.so.domain;

import java.util.EventListener;

public interface ProcessStatusChangeListener extends EventListener {
	public abstract void statusChanged(ProcessStatusChangeEvent e);
}
