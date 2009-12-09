package ar.uba.dc.so.gui;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Dimension;
import javax.swing.JScrollPane;

import ar.uba.dc.so.domain.Partition;
import ar.uba.dc.so.memoryManagement.Memory;

import com.mxgraph.layout.mxStackLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import java.awt.Rectangle;
import javax.swing.WindowConstants;

public class MemoryVisualizationWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JScrollPane jScrollPane = null;
	private mxGraphComponent gComp = null;
	private mxStackLayout graphLayout = null;
	
	/**
	 * This is the default constructor
	 */
	public MemoryVisualizationWindow() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setContentPane(getJContentPane());
		this.setTitle("Memory Simulator (Memory visualization)");
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setBounds(new Rectangle(800, 0, 300, 534));
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
			jContentPane.add(getJScrollPane(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			mxGraph graph = new mxGraph();
			gComp = new mxGraphComponent(graph);
			graphLayout = new mxStackLayout(graph, false, 20);
			
			jScrollPane = new JScrollPane(gComp);
		}
		return jScrollPane;
	}
	
	public void draw(Memory m) {
		mxGraph g = gComp.getGraph();
		g.setModel(new mxGraphModel());
		g.getModel().beginUpdate();
		
		Object prev = null;
		for(Partition p : m.getPartitions()) {
			if(prev == null)
				prev = g.insertVertex(g.getDefaultParent(), null, p.sizeInKb + "KB", 0, 5, 40, p.sizeInKb * 2);
			else {
				Object now = g.insertVertex(g.getDefaultParent(), null, p.sizeInKb + "KB", 0, 5, 40, p.sizeInKb * 2);
				g.insertEdge(g.getDefaultParent(), "", "", prev, now);
				prev = now;
			}
			mxCell cell = (mxCell) prev;
			if(p.isEmpty()) {
				cell.setStyle("shape="+mxConstants.SHAPE_RECTANGLE+";strokeColor=red;fillColor=green");
			}
			else {
				cell.setStyle("strokeColor=green;fillColor=red");
			}
		}
		
		g.getModel().endUpdate();
		graphLayout.execute(g.getDefaultParent());
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
