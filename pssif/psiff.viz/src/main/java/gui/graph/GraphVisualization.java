package gui.graph;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractModalGraphMouse;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import graph.listener.MiddleMousePlugin;
import graph.listener.MyPopupGraphMousePlugin;
import graph.model.ConnectionType;
import graph.model.GraphBuilder;
import graph.model.MyEdge;
import graph.model.MyNode;
import graph.model.NodeType;
import graph.operations.VertexStrokeHighlight;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ChainedTransformer;

public class GraphVisualization
{
  private Graph<MyNode, MyEdge> g;
  static int Nodecount;
  private AbstractModalGraphMouse gm;
  private VisualizationViewer<MyNode, MyEdge> vv;
  private Layout<MyNode, MyEdge> layout;
  private VertexStrokeHighlight<MyNode, MyEdge> vsh;
  private GraphBuilder gb;
  private boolean detailedNodes;
  
  public void reset()
  {
    this.g = this.gb.createGraph(this.detailedNodes);
    
    this.vv.repaint();
  }
  
  public GraphVisualization(Dimension d, boolean details)
  {
    int i = 1000;
    this.detailedNodes = details;
    this.gb = new GraphBuilder();
    
    this.g = this.gb.createGraph(this.detailedNodes);
    

    this.layout = new FRLayout<MyNode, MyEdge>(this.g);
    if (d != null)
    {
      int x = (int)d.getWidth() - 50;
      int y = (int)d.getHeight() - 50;
      this.layout.setSize(new Dimension(x, y));
    }
    else
    {
      this.layout.setSize(new Dimension(i, i));
    }
    this.vv = new VisualizationViewer<MyNode, MyEdge>(this.layout);
    if (d != null) {
      this.vv.setPreferredSize(d);
    } else {
      this.vv.setPreferredSize(new Dimension(i, i));
    }
    Transformer<MyNode, Paint> vertexColor = new Transformer<MyNode, Paint>()
    {
      public Paint transform(MyNode i)
      {
        if (i.getNodeType() == NodeType.FUNCTION) {
          return Color.GREEN;
        }
        return Color.RED;
      }
    };
    Transformer<MyNode, Shape> vertexSize = new Transformer<MyNode, Shape>()
    {
      public Shape transform(MyNode node)
      {
        if (node.isDetailedOutput())
        {
          Rectangle2D rec = new Rectangle2D.Double(-75.0D, -75.0D, 150.0D, 150.0D);
          if (node.getSize() != 0) {
            return 
              AffineTransform.getScaleInstance(node.getSize() + 1, node.getSize() + 1).createTransformedShape(rec);
          }
          return rec;
        }
        Rectangle2D rec = new Rectangle2D.Double(-75.0D, -25.0D, 150.0D, 50.0D);
        return rec;
      }
    };
    Transformer<MyNode, String> vertexLabelTransformer = new ChainedTransformer<MyNode, String>(new Transformer[] {
      new ToStringLabeller<String>(), 
      new Transformer<String,String>()
      {
        public String transform(String input)
        {
          return "<html>" + input;
        }
      } });
	  // Set up a new stroke Transformer for the edges
    Transformer<MyEdge, Stroke> edgeTransformer = new Transformer<MyEdge, Stroke>()
    {
      public Stroke transform(MyEdge edge)
      {
        int res = edge.getConnectionType().getLineType() % 4;
        BasicStroke b = null;
        
        switch (res)
        {
        case 0: 
          return RenderContext.DASHED;
        case 1: 
          return RenderContext.DOTTED;
        case 2: 
          b = new BasicStroke(1.0F, 0, 2, 0.0F, new float[] { 20.0F, 12.0F }, 0.0F);
          return b;
        case 3: 
          b = new BasicStroke(5.0F, 0, 2, 0.0F, new float[] { 1.0F, 1.0F }, 0.0F);
          return b;
        }
        b = new BasicStroke(1.0F, 0, 2, 0.0F, new float[] { 1.0F, 1.0F }, 0.0F);
        return b;
      }
    };
    Transformer<MyEdge, String> edgeLabelTransformer = new ChainedTransformer<MyEdge, String>(new Transformer[] {
      new ToStringLabeller<String>(), 
      new Transformer<String,String>()
      {
        public String transform(String input)
        {
          return "<html>" + input;
        }
      } });
    Transformer<MyEdge, Paint> edgePaint = new Transformer<MyEdge, Paint>()
    {
      public Paint transform(MyEdge edge)
      {
        int res = edge.getConnectionType().getLineType() % 13;
        switch (res)
        {
        case 0: 
          return Color.BLACK;
        case 1: 
          return Color.YELLOW;
        case 2: 
          return Color.BLUE;
        case 3: 
          return Color.CYAN;
        case 4: 
          return Color.DARK_GRAY;
        case 5: 
          return Color.GRAY;
        case 6: 
          return Color.GREEN;
        case 7: 
          return Color.LIGHT_GRAY;
        case 8: 
          return Color.MAGENTA;
        case 9: 
          return Color.ORANGE;
        case 10: 
          return Color.PINK;
        case 11: 
          return Color.RED;
        case 12: 
          return Color.WHITE;
        }
        return Color.BLACK;
      }
    };
    this.vsh = new VertexStrokeHighlight<MyNode,MyEdge>(this.g, this.vv.getPickedVertexState());
    
    this.vsh.setHighlight(true, 1, new LinkedList<ConnectionType>());
    
	//Vertex
    this.vv.getRenderContext().setVertexFillPaintTransformer(vertexColor);
    this.vv.getRenderContext().setVertexShapeTransformer(vertexSize);
    this.vv.getRenderContext().setVertexLabelTransformer(vertexLabelTransformer);
    this.vv.getRenderContext().setVertexStrokeTransformer(this.vsh);
    this.vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
    
	//Edge
    this.vv.getRenderContext().setEdgeStrokeTransformer(edgeTransformer);
    //vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<MyEdge>());
    this.vv.getRenderContext().setEdgeLabelTransformer(edgeLabelTransformer);
    // vv.getRenderContext().setEdgeLabelClosenessTransformer(new MutableDirectionalEdgeValue(.3, .3));
    this.vv.getRenderContext().setEdgeDrawPaintTransformer(edgePaint);
    //vv.getRenderContext().setE
	//vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.CubicCurve<MyNode,MyEdge>());



    this.gm = new DefaultModalGraphMouse<MyNode, MyEdge>();
    this.vv.setGraphMouse(this.gm);
    this.gm.add(new MyPopupGraphMousePlugin());
    this.gm.add(new MiddleMousePlugin());
  }
  
  public Graph<MyNode, MyEdge> getGraph()
  {
    return this.g;
  }
  
  public VisualizationViewer<MyNode, MyEdge> getVisualisationViewer()
  {
    return this.vv;
  }
  
  public AbstractModalGraphMouse getAbstractModalGraphMouse()
  {
    return this.gm;
  }
  
  public void setNodeVisualisation(boolean details)
  {
    if (details != this.detailedNodes)
    {
      this.detailedNodes = details;
      this.g = this.gb.createGraph(details);
      
      this.vv.repaint();
    }
  }
  
  public void setHighlightNodes(LinkedList<ConnectionType> types)
  {
	 // if (types.size()==0)
	//	  this.vsh.setHighlight(true, 1, types);
	 // else
	 this.vsh = new VertexStrokeHighlight<MyNode, MyEdge>(g, this.vv.getPickedVertexState());
	 this.vsh.setHighlight(true, 1, types);
	 this.vv.getRenderContext().setVertexStrokeTransformer(this.vsh);
	 this.vv.repaint();
  }
  
  public LinkedList<ConnectionType> getHighlightNodes()
  {
    return this.vsh.getFollowEdges();
  }
  
  /*	  private class MutableDirectionalEdgeValue extends ConstantDirectionalEdgeValueTransformer<MyNode,MyEdge> {
	        BoundedRangeModel undirectedModel = new DefaultBoundedRangeModel(5,0,0,10);
	        BoundedRangeModel directedModel = new DefaultBoundedRangeModel(7,0,0,10);
	        
	        public MutableDirectionalEdgeValue(double undirected, double directed) {
	            super(undirected, directed);
	            undirectedModel.setValue((int)(undirected*10));
	            directedModel.setValue((int)(directed*10));
	            
	            undirectedModel.addChangeListener(new ChangeListener(){
	                public void stateChanged(ChangeEvent e) {
	                    setUndirectedValue(new Double(undirectedModel.getValue()/10f));
	                    vv.repaint();
	                }
	            });
	            directedModel.addChangeListener(new ChangeListener(){
	                public void stateChanged(ChangeEvent e) {
	                    setDirectedValue(new Double(directedModel.getValue()/10f));
	                    vv.repaint();
	                }
	            });
	        }
	        /**
	         * @return Returns the directedModel.
	         */
	/*        public BoundedRangeModel getDirectedModel() {
	            return directedModel;
	        }

	        /**
	         * @return Returns the undirectedModel.
	         */
/*	        public BoundedRangeModel getUndirectedModel() {
	            return undirectedModel;
	        }
	    }*/
}
