package ar.uba.dc.so.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.JSplitPane;

import ar.uba.dc.so.domain.Process;
import ar.uba.dc.so.domain.ProcessState;

import com.mxgraph.layout.mxStackLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import java.awt.Dimension;

public class ProcessQueuesWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JSplitPane jSplitPane = null;
	private JSplitPane jSplitPane1 = null;
	private JSplitPane jSplitPane2 = null;
	private JScrollPane jScrollPane = null;
	private JScrollPane jScrollPane1 = null;
	private JScrollPane jScrollPane2 = null;
	private JScrollPane jScrollPane3 = null;
	
	private Object lastWaitingAdded = null;  //  @jve:decl-index=0:
	private mxGraphComponent gCompWaiting = null;
	private mxStackLayout waitingGraphLayout = null;
	
	private Object lastRunningAdded = null;
	private mxGraphComponent gCompRunning = null;
	private mxStackLayout runningGraphLayout = null;
	
	private Object lastInterruptedAdded = null;
	private mxGraphComponent gCompInterrupted = null;
	private mxStackLayout interruptedGraphLayout = null;
	
	private Object lastFinishedAdded = null;
	private mxGraphComponent gCompFinished = null;
	private mxStackLayout finishedGraphLayout = null;
	private JPanel jWaitingProcessesPanel = null;
	private JPanel jRunningProcessesPanel = null;
	private JPanel jInterruptedProcessesPanel = null;
	private JPanel jFinishedProcessesPanel = null;
	/**
	 * This is the default constructor
	 */
	public ProcessQueuesWindow() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 467);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setContentPane(getJContentPane());
		this.setTitle("Memory Simulator (Processes)");
		
		this.setUndecorated(true);
		this.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJSplitPane(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			jSplitPane.setDividerLocation(210);
			jSplitPane.setBottomComponent(getJSplitPane2());
			jSplitPane.setTopComponent(getJSplitPane1());
		}
		return jSplitPane;
	}

	/**
	 * This method initializes jSplitPane1	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane1() {
		if (jSplitPane1 == null) {
			jSplitPane1 = new JSplitPane();
			jSplitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
			jSplitPane1.setDividerLocation(100);
			jSplitPane1.setBottomComponent(getJRunningProcessesPanel());
			jSplitPane1.setTopComponent(getJWaitingProcessesPanel());
			
		}
		return jSplitPane1;
	}
	
	private JPanel getJWaitingProcessesPanel() {
		if(jWaitingProcessesPanel == null) {
			jWaitingProcessesPanel = new JPanel();
			jWaitingProcessesPanel.setLayout(new BorderLayout());
			jWaitingProcessesPanel.add(getJScrollPane(), BorderLayout.CENTER);
			jWaitingProcessesPanel.add(new JLabel("Waiting"), BorderLayout.NORTH);
		}
		return jWaitingProcessesPanel;
	}
	
	private JPanel getJRunningProcessesPanel() {
		if(jRunningProcessesPanel == null) {
			jRunningProcessesPanel = new JPanel();
			jRunningProcessesPanel.setLayout(new BorderLayout());
			jRunningProcessesPanel.add(getJScrollPane1(), BorderLayout.CENTER);
			jRunningProcessesPanel.add(new JLabel("Running"), BorderLayout.NORTH);
		}
		return jRunningProcessesPanel;
	}
	
	private JPanel getJInterruptedProcessesPanel() {
		if(jInterruptedProcessesPanel == null) {
			jInterruptedProcessesPanel = new JPanel();
			jInterruptedProcessesPanel.setLayout(new BorderLayout());
			jInterruptedProcessesPanel.add(getJScrollPane2(), BorderLayout.CENTER);
			jInterruptedProcessesPanel.add(new JLabel("Interrupted"), BorderLayout.NORTH);
		}
		return jInterruptedProcessesPanel;
	}
	
	private JPanel getJFinishedProcessesPanel() {
		if(jFinishedProcessesPanel == null) {
			jFinishedProcessesPanel = new JPanel();
			jFinishedProcessesPanel.setLayout(new BorderLayout());
			jFinishedProcessesPanel.add(getJScrollPane3(), BorderLayout.CENTER);
			jFinishedProcessesPanel.add(new JLabel("Finished"), BorderLayout.NORTH);
		}
		return jFinishedProcessesPanel;
	}
	/**
	 * This method initializes jSplitPane2	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane2() {
		if (jSplitPane2 == null) {
			jSplitPane2 = new JSplitPane();
			jSplitPane2.setOrientation(JSplitPane.VERTICAL_SPLIT);
			jSplitPane2.setTopComponent(getJInterruptedProcessesPanel());
			jSplitPane2.setBottomComponent(getJFinishedProcessesPanel());
			jSplitPane2.setDividerLocation(100);
		}
		return jSplitPane2;
	}
	
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			mxGraph graph = new mxGraph();
			gCompWaiting = new mxGraphComponent(graph);
			jScrollPane = new JScrollPane(gCompWaiting);
			
			gCompWaiting.getViewport().setOpaque(false);
			gCompWaiting.setBackground(Color.CYAN);
			
			waitingGraphLayout = new mxStackLayout(graph, true, 40);
		}
		return jScrollPane;
	}
	
	
	public void addProcessWaiting(Process p) {
		addProcess(p, ProcessState.WAITING);
	}

	public void removeProcessRunning(Process p) {
		removeProcess(p, ProcessState.RUNNING);
	}
	
	public void addProcessRunning(Process p) {
		addProcess(p, ProcessState.RUNNING);
	}

	public void removeProcessInterrupted(Process p) {
		removeProcess(p, ProcessState.INTERRUPTED);
	}
	
	public void addProcessInterrupted(Process p) {
		addProcess(p, ProcessState.INTERRUPTED);
	}

	public void removeProcessFinished(Process p) {
		removeProcess(p, ProcessState.FINISHED);
	}
	
	public void addProcessFinished(Process p) {
		addProcess(p, ProcessState.FINISHED);
	}

	public void removeProcessWaiting(Process p) {
		removeProcess(p, ProcessState.WAITING);
	}
	
	private void addProcess(Process p, ProcessState state) {
		String idString = "";
		Object lastAdded = null; // Se queda por si queremos volver a poner las flechitas
		mxGraphComponent gComp = null;
		mxStackLayout layout = null;
		
		switch(state) {
		case WAITING:
			idString = "WaitingVertex";
			lastAdded = lastWaitingAdded;
			gComp = gCompWaiting;
			layout = waitingGraphLayout;
			break;
		case RUNNING:
			idString = "RunningVertex";
			lastAdded = lastRunningAdded;
			gComp = gCompRunning;
			layout = runningGraphLayout;
			break;
		case INTERRUPTED:
			idString = "InterruptedVertex";
			lastAdded = lastInterruptedAdded;
			gComp = gCompInterrupted;
			layout = interruptedGraphLayout;
			break;
		case FINISHED:
			idString = "FinishedVertex";
			lastAdded = lastFinishedAdded;
			gComp = gCompFinished;
			layout = finishedGraphLayout;
			break;
		}
		
		
		mxGraph graph = gComp.getGraph();
		
		Object parent = graph.getDefaultParent();
		
		graph.getModel().beginUpdate();
		Object v1 = graph.insertVertex(parent, idString+Integer.toString(p.id), new Integer(p.id), 0, 5, 20, 20);
		
		/*if(lastAdded != null)
			graph.insertEdge(parent, "Edge", "", lastAdded, v1);*/
		
		switch(state) {
		case WAITING:
			lastWaitingAdded = v1;
			break;
		case RUNNING:
			lastRunningAdded = v1;
			break;
		case INTERRUPTED:
			lastInterruptedAdded = v1;
			break;
		case FINISHED:
			lastFinishedAdded = v1;
			break;
		}
		
		graph.getModel().endUpdate();
		
		layout.execute(parent);
		gComp.refresh();
	}
	
	private void removeProcess(Process p, ProcessState state) {
		String idString = "";
		Object lastAdded = null;
		mxGraphComponent gComp = null;
		mxStackLayout layout = null;
		
		switch(state) {
		case WAITING:
			idString = "WaitingVertex";
			lastAdded = lastWaitingAdded;
			gComp = gCompWaiting;
			layout = waitingGraphLayout;
			break;
		case RUNNING:
			idString = "RunningVertex";
			lastAdded = lastRunningAdded;
			gComp = gCompRunning;
			layout = runningGraphLayout;
			break;
		case INTERRUPTED:
			idString = "InterruptedVertex";
			lastAdded = lastInterruptedAdded;
			gComp = gCompInterrupted;
			layout = interruptedGraphLayout;
			break;
		case FINISHED:
			idString = "FinishedVertex";
			lastAdded = lastFinishedAdded;
			gComp = gCompFinished;
			layout = finishedGraphLayout;
			break;
		}
		
		mxGraph graph = gComp.getGraph();
		Object parent = graph.getDefaultParent();
		
		Object cell = ((mxGraphModel) graph.getModel()).getCell(idString+Integer.toString(p.id));
		Object[] edges = graph.getEdges(cell);
		
		graph.getModel().beginUpdate();
		// Como son listas los ejes son a lo sumo 2.
		
		if(edges.length == 0) {
			switch(state) {
			case WAITING:
				lastWaitingAdded = null;
				break;
			case RUNNING:
				lastRunningAdded = null;
				break;
			case INTERRUPTED:
				lastInterruptedAdded = null;
				break;
			case FINISHED:
				lastFinishedAdded = null;
				break;
			}
		}
		else if(edges.length == 1) {
			mxCell edge = (mxCell) edges[0];
			
			if(lastAdded == cell) {
				switch(state) {
				case WAITING:
					lastWaitingAdded = edge.getSource();
					break;
				case RUNNING:
					lastRunningAdded = edge.getSource();
					break;
				case INTERRUPTED:
					lastInterruptedAdded = edge.getSource();
					break;
				case FINISHED:
					lastFinishedAdded = edge.getSource();
					break;
				}
			}
		}
		else if(edges.length == 2) {
			mxCell edge0 = (mxCell) edges[0];
			mxCell edge1 = (mxCell) edges[1];
			mxCell source = null;
			mxCell target = null;
			
			if(edge0.getTarget() == cell) {
				source = (mxCell) edge0.getSource();
				target = (mxCell) edge1.getTarget();
			}
			else {
				source = (mxCell) edge1.getSource();
				target = (mxCell) edge0.getTarget();
			}
			
			graph.insertEdge(parent, "Edge", "", source, target);
		}
		graph.removeCells(edges);
		graph.getModel().remove(cell);
		graph.getModel().endUpdate();
		
		layout.execute(parent);
		gComp.refresh();
	}

	/**
	 * This method initializes jScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			mxGraph graph = new mxGraph();
			gCompRunning = new mxGraphComponent(graph);
			jScrollPane1 = new JScrollPane(gCompRunning);
			
			gCompRunning.getViewport().setOpaque(false);
			gCompRunning.setBackground(Color.GREEN);
			
			runningGraphLayout = new mxStackLayout(graph, true, 40);
		}
		return jScrollPane1;
	}

	/**
	 * This method initializes jScrollPane2	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane2() {
		if (jScrollPane2 == null) {
			mxGraph graph = new mxGraph();
			gCompInterrupted = new mxGraphComponent(graph);
			jScrollPane2 = new JScrollPane(gCompInterrupted);
			
			gCompInterrupted.getViewport().setOpaque(false);
			gCompInterrupted.setBackground(Color.RED);
			
			interruptedGraphLayout = new mxStackLayout(graph, true, 40);
		}
		return jScrollPane2;
	}

	/**
	 * This method initializes jScrollPane3	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane3() {
		if (jScrollPane3 == null) {
			mxGraph graph = new mxGraph();
			gCompFinished = new mxGraphComponent(graph);
			jScrollPane3 = new JScrollPane(gCompFinished);
			
			gCompFinished.getViewport().setOpaque(false);
			gCompFinished.setBackground(Color.GRAY);
			
			finishedGraphLayout = new mxStackLayout(graph, true, 40);
		}
		return jScrollPane3;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
