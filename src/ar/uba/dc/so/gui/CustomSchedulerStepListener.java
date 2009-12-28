package ar.uba.dc.so.gui;

import ar.uba.dc.so.domain.Scheduler;
import ar.uba.dc.so.domain.SchedulerStepEvent;
import ar.uba.dc.so.domain.SchedulerStepListener;
import ar.uba.dc.so.memoryManagement.Memory;
import ar.uba.dc.so.memoryManagement.MemoryPaging;

public class CustomSchedulerStepListener implements SchedulerStepListener {
	ControlWindow cw = null;
	MemoryVisualizationWindow mw = null;
	PageTableWindow ptw = null;
	
	public CustomSchedulerStepListener(ControlWindow cw, MemoryVisualizationWindow mw, PageTableWindow ptw) {
		this.cw = cw;
		this.mw = mw;
		this.ptw = ptw;
	}
	
	public void schedullerStep(SchedulerStepEvent e) {
		Scheduler s = (Scheduler) e.getSource();
		
		// Actualizo progreso en la simulación
		cw.getJProgressBar().setValue(Scheduler.getTimeInSeconds());
		cw.getJProgressBar().setString("Elapsed simulation time: " + Scheduler.getTimeInSeconds() + "s");
		
		// Actualizo el uso de memoria
		Memory m = s.getMemory();
		cw.getJMemoryUsageProgressBar().setValue(m.getAllocedSize());
		cw.getJMemoryUsageProgressBar().setString(m.getAllocedSize() + "KB / " + m.sizeInKb + "KB");
		
		//cw.getJInfoLabel().setText("Status: simulating; Wasted memory: " + m.getWastedMemory() + "KB");
		
		mw.draw(m);
		if(m instanceof MemoryPaging) {
			MemoryPaging mPrima = (MemoryPaging) m;
			ptw.draw(mPrima);
		}
		
		if(cw.getJStepByStepCheckBox().isSelected())
			cw.stopSimulationButtonAction();
	}
}
