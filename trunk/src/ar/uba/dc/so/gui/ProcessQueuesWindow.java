package ar.uba.dc.so.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JSplitPane;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import javax.swing.JScrollPane;

public class ProcessQueuesWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JSplitPane jSplitPane = null;
	private JSplitPane jSplitPane1 = null;
	private JSplitPane jSplitPane2 = null;
	private JScrollPane jScrollPane = null;

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
		this.setSize(300, 200);
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
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
			jSplitPane1.setTopComponent(getJScrollPane());
			
		}
		return jSplitPane1;
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
			mxGraphComponent gComp = new mxGraphComponent(graph);
			jScrollPane = new JScrollPane(gComp);
			
			Object parent = graph.getDefaultParent();
			
			graph.getModel().beginUpdate();
			Object v1 = graph.insertVertex(parent, "A", "A", 0, 5, 20, 20);
			Object v2 = graph.insertVertex(parent, "B", "B", 40, 5, 20, 20);
			Object v3 = graph.insertVertex(parent, "C", "C", 80, 5, 20, 20);
			
			graph.insertEdge(parent, "Edge", "Edge", v1, v2);
			graph.insertEdge(parent, "Edge2", "Edge2", v2, v3);
			
			graph.getModel().endUpdate();
			
			gComp.getViewport().setOpaque(false);
			gComp.setBackground(Color.GREEN);
			gComp.clearCellOverlays();
		}
		return jScrollPane;
	}

}
