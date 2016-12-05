package main;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;

import org.apache.jena.graph.Node;
import org.apache.jena.rdf.model.Statement;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.BasicRenderer;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;





public class jungBasicExample extends JPanel {

	private Layout mVisualizer;
	private Renderer mRenderer;
	private VisualizationViewer<Node, Double> mVizViewer;
	private	DefaultModalGraphMouse m_graphmouse;
	
	@SuppressWarnings("unchecked")
	public jungBasicExample(Graph g){
	
		mRenderer = new BasicRenderer<Object, Object>();
		/* how nodes will be places....change this */
        mVisualizer = new KKLayout(g);//

		/* single DS to help you work with layouts... */
        mVizViewer = new VisualizationViewer(mVisualizer, new Dimension(700,
				700));
        
        mVizViewer.getRenderContext().setVertexLabelRenderer(new DefaultVertexLabelRenderer(Color.BLACK));
        

        // label sur les noeuds
        mVizViewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Node>());
        
        mVizViewer.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
        
        // label sur les arcs
        // On veut uniquement le noms des predicats, osef du reste.
        mVizViewer.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller(){
            public String transform(Object v) {
            	String predicate = ((Statement) v).getPredicate().toString();
                return predicate;
            }});
        
        
        // On deplace un peu les labels pour qu'ils ne soit pas pile sur les fleches
        mVizViewer.getRenderContext().setLabelOffset(20);
        
        
        
		mVizViewer.setBackground(Color.WHITE);
		
		m_graphmouse = new DefaultModalGraphMouse();
	    mVizViewer.setGraphMouse(m_graphmouse);
		
		add(mVizViewer);
		
		//show it off
		mVizViewer.revalidate();
		mVizViewer.repaint();
	}
	
}
