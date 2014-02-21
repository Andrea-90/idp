package graph.operations;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import model.ModelBuilder;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import graph.model2.MyEdge2;
import graph.model2.MyEdgeTypes;
import graph.model2.MyNode2;

public class MyCollapser {
	

	private MyNode2 recStartNode;
	private LinkedList<MyEdge2> newInEdges;
	private LinkedList<MyEdge2> newOutEdges;
	private LinkedList<MyNode2> touchedNodes;
	
	private HashMap<MyNode2,CollapseContainer> history;
	private CollapseContainer container;
	
	public MyCollapser()
	{
		this.history = new HashMap<MyNode2, CollapseContainer>();
	}
	
	/*public Graph<MyNode2,MyEdge2> collapseGraph(Graph<MyNode2,MyEdge2> g, MyNode2 startNode) 
	{
		// save copy
		container = new CollapseContainer();
		
		recStartNode=startNode;
		newInEdges = new LinkedList<InfoContainer>();
		newOutEdges = new LinkedList<InfoContainer>();
		touchedNodes = new LinkedList<MyNode2>();
		
		recCollapseGraph(g,startNode,true,new LinkedList<MyNode2>());
		
		for (InfoContainer ic : newInEdges)
		{
			if (!touchedNodes.contains(ic.getEdgeSource()))
			{
				g.addEdge(ic.getEdge(), ic.getEdgeSource(), ic.getEdgeDestintation(),EdgeType.DIRECTED);
				container.addNewEdges(ic);
			}
		}
		
		for (InfoContainer ic : newOutEdges)
		{
			if (!touchedNodes.contains(ic.getEdgeDestintation()))
			{
				g.addEdge(ic.getEdge(), ic.getEdgeSource(), ic.getEdgeDestintation(),EdgeType.DIRECTED);
				container.addNewEdges(ic);
			}
			
		}
		
		history.put(startNode, container);
		
		return g;
	}*/
	
	public void collapseGraph( MyNode2 startNode) 
	{
		// save copy
		container = new CollapseContainer();
		
		recStartNode=startNode;
		newInEdges = new LinkedList<MyEdge2>();
		newOutEdges = new LinkedList<MyEdge2>();
		touchedNodes = new LinkedList<MyNode2>();
		
		recCollapseGraph(startNode,true,new LinkedList<MyNode2>());
		
		for (MyEdge2 ic : newInEdges)
		{
			if (!touchedNodes.contains(ic.getSourceNode()))
			{
				//g.addEdge(ic.getEdge(), ic.getEdgeSource(), ic.getEdgeDestintation(),EdgeType.DIRECTED);
				ModelBuilder.addCollapserEdge(ic);
				container.addNewEdges(ic);
			}
		}
		
		for (MyEdge2 ic : newOutEdges)
		{
			if (!touchedNodes.contains(ic.getDestinationNode()))
			{
				//g.addEdge(ic.getEdge(), ic.getEdgeSource(), ic.getEdgeDestintation(),EdgeType.DIRECTED);
				ModelBuilder.addCollapserEdge(ic);
				container.addNewEdges(ic);
			}
			
		}
		
		history.put(startNode, container);
		
		//return g;
	}
	
	
	private void recCollapseGraph ( MyNode2 startNode, boolean start, LinkedList<MyNode2> work)
	{
		System.out.println("Start: "+start);
		if (start)
		{
			LinkedList<MyEdge2> out = findOutgoingEdges(startNode);//g.getOutEdges(startNode);
			
			LinkedList<MyNode2> tmp = new LinkedList<MyNode2>();
			LinkedList<MyEdge2> del = new LinkedList<MyEdge2>();
			for (MyEdge2 e : out)
			{
				if (e.getEdgeType().getName().equals(MyEdgeTypes.CONTAINMENT))
				{
					MyNode2 next = e.getDestinationNode();//g.getDest(e);
					del.add(e);
					
					tmp.add(next);
					//System.out.println("Nodes to process "+next.getName());
				}
			}
			work.addAll(tmp);
			touchedNodes.addAll(tmp);
			for (int i = 0;i<del.size();i++)
			{
				System.out.println("delete first start Edge: "+del.get(i).toString());
				
				MyEdge2 e = del.get(i);
				
				container.addOldEdge(e);//g.getSource(e), g.getDest(e)));
				//g.removeEdge(e);
				e.setVisible(false);
			}
		}
		
		
		
		if (work.size()>0)
		{
			System.out.println("loop Work "+work.size());
			MyNode2 workNode = work.getFirst();
			
			/*Collection<MyEdge2> fout = g.getOutEdges(workNode);
			Collection<MyEdge2> fin =  g.getInEdges(workNode);*/
			
			List<MyEdge2> out = findOutgoingEdges(workNode);//new LinkedList<MyEdge2>();
			List<MyEdge2> in = findIncomingEdges(workNode);//new LinkedList<MyEdge2>();
			
			if (in!=null)
			{
				/*Iterator<MyEdge2> it = fin.iterator();
				while(it.hasNext())
				{
					in.add(it.next());
				}*/
				
				//redirect edges to "collapsed" Node
			
				//Iterator<MyEdge> it = in.iterator();
				for (int i = 0; i<in.size();i++)
				{
					System.out.println("loop in "+i);
					MyEdge2 e= in.get(i);
					
					MyNode2 source = e.getSourceNode();//g.getSource(e);
					
					container.addOldEdge(e);//g.getSource(e), g.getDest(e)));
					//g.removeEdge(e);
					e.setVisible(false);
					newInEdges.add(new MyEdge2(e.getEdge(), e.getEdgeType(), source, recStartNode));// InfoContainer(e, source, recStartNode));
					//g.addEdge(e, source, recStartNode);
				}
			}
			if (out!=null)
			{				
				/*Iterator<MyEdge2> it = fout.iterator();
				while(it.hasNext())
				{
					out.add(it.next());
				}*/
				
				for (int i = 0; i<out.size();i++)
				{
					System.out.println("loop out "+out.size());
					MyEdge2 e= out.get(i);
					MyNode2 dest = e.getDestinationNode();//g.getDest(e);
					System.out.println("Remove Edge  "+e.getEdgeType().getName());
					container.addOldEdge(e);//g.getSource(e), g.getDest(e)));
					//g.removeEdge(e);
					e.setVisible(false);
					System.out.println("Add to work  "+dest.getName());
					newOutEdges.add(new MyEdge2(e.getEdge(),e.getEdgeType(),recStartNode, e.getDestinationNode()));
					if (e.getEdgeType().getName().equals(MyEdgeTypes.CONTAINMENT) && !work.contains(dest) )
					{
						work.add(dest);
						touchedNodes.add(dest);
						
					}
				}
			}
			//System.out.println("workNode " + (workNode==null)+" name "+ workNode.getName());
			container.addOldNode(workNode);
			//g.removeVertex(workNode);
			workNode.setVisible(false);
			work.remove(workNode);
			
			System.out.println("Work to do "+work.size());
			for (MyNode2 tmp :work)
			{
				System.out.println("Node : " + tmp.getRealName());
			}
			System.out.println("		Tried to remove Node : " + workNode.getRealName());
			//if (work.size()>0)
			System.out.println("----------------------");
			recCollapseGraph( null, false, work);
		}
		
	
	}
	
	/*public Graph<MyNode2,MyEdge2> expandNode(Graph<MyNode2,MyEdge2> g, MyNode2 startNode, boolean nodeDetails)
	{
		CollapseContainer container = this.history.get(startNode);
		
		if (container==null)
			return g;
		else
		{
			LinkedList<InfoContainer> workEdges = container.getNewEdges();
			
			// remove added Edges
			for (InfoContainer ic :workEdges)
			{
				g.removeEdge(ic.getEdge());
			}
			
			// add old Nodes
			LinkedList<MyNode2> nodes = container.getOldNodes();
			
			for (MyNode2 n : nodes)
			{
				if (n!=null)
				{
					n.setDetailedOutput(nodeDetails);
					g.addVertex(n);
				}
			}
			
			// add old Edges
			workEdges = container.getOldEdges();
			for (InfoContainer ic :workEdges)
			{
				g.addEdge(ic.getEdge(), ic.getEdgeSource(), ic.getEdgeDestintation(), EdgeType.DIRECTED);
			}
			
			this.history.remove(startNode);
			return g;
		}
	}*/
	
	public void expandNode( MyNode2 startNode, boolean nodeDetails)
	{
		CollapseContainer container = this.history.get(startNode);
		
		if (container!=null)
		{
			LinkedList<MyEdge2> workEdges = container.getNewEdges();
			
			// remove added Edges
			for (MyEdge2 ic :workEdges)
			{
				//g.removeEdge(ic.getEdge());
				ModelBuilder.removeCollapserEdge(ic);
			}
			
			// add old Nodes
			LinkedList<MyNode2> nodes = container.getOldNodes();
			
			for (MyNode2 n : nodes)
			{
				if (n!=null)
				{
					MyNode2 tmp = ModelBuilder.findNode(n.getNode());
					
					tmp.setDetailedOutput(nodeDetails);
					tmp.setVisible(true);
					
				}
			}
			
			// add old Edges
			workEdges = container.getOldEdges();
			for (MyEdge2 ic :workEdges)
			{
				//g.addEdge(ic.getEdge(), ic.getEdgeSource(), ic.getEdgeDestintation(), EdgeType.DIRECTED);
				MyEdge2 tmp = ModelBuilder.findEdge(ic.getEdge());
				
				tmp.setVisible(true);
			}
			
			this.history.remove(startNode);
			//return g;
		}
	}
	
	
	public boolean isExpandable (MyNode2 startNode)
	{
		CollapseContainer container = this.history.get(startNode);
		
		if (container==null)
			return false;
		else
			return true;
	}
	
	public boolean isCollapsable (MyNode2 startNode)
	{
		LinkedList<MyEdge2> out = findOutgoingEdges(startNode);//g.getOutEdges(startNode);
		for (MyEdge2 e : out)
		{
			if (e.getEdgeType().getName().equals(MyEdgeTypes.CONTAINMENT))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean CollapserActive()
	{
		if (this.history.size()>0)
			return true;
		else
			return false;
	}
	
	public void reset()
	{
		this.history.clear();
	}
	
	private LinkedList<MyEdge2> findOutgoingEdges (MyNode2 node)
	{
		LinkedList<MyEdge2> res = new LinkedList<MyEdge2>();
		
		for (MyEdge2 e :ModelBuilder.getAllEdges())
		{
			if (e.isVisible() && e.getSourceNode().equals(node))
				res.add(e);
		}
		
		return res;
	}
	
	private LinkedList<MyEdge2> findIncomingEdges (MyNode2 node)
	{
		LinkedList<MyEdge2> res = new LinkedList<MyEdge2>();
		
		for (MyEdge2 e :ModelBuilder.getAllEdges())
		{
			if (e.isVisible() && e.getDestinationNode().equals(node))
				res.add(e);
		}
		
		return res;
	}
	
}
