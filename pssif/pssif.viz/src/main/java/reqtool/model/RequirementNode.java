package reqtool.model;

import graph.model.MyEdge;
import graph.model.MyNode;

import java.util.LinkedList;

import model.ModelBuilder;
import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.common.PSSIFValue;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.NodeType;

public class RequirementNode {
	
	private MyNode node;
	
	public RequirementNode(MyNode node) {
		this.node = node;
	}
	
	public static PSSIFOption<PSSIFValue> getAttributeValue(MyNode node, String attrName) throws NullPointerException{
		NodeType currentType = node.getNodeType().getType();
		for (Attribute a : currentType.getAttributes()) {
			if (a.getName().equalsIgnoreCase(attrName)) {
				return a.get(node.getNode());
			
			}
		}
		return null;
	}
	
	public PSSIFOption<PSSIFValue> getAttributeValue(String attrName) throws NullPointerException{
		NodeType currentType = node.getNodeType().getType();
		for (Attribute a : currentType.getAttributes()) {
			if (a.getName().equalsIgnoreCase(attrName)) {
				return a.get(node.getNode());
			}
		}
		return null;
	}
	
	public static LinkedList<MyNode> getRequirementSourceNodes(MyNode requirementNode, String edgeTypeName) {
		LinkedList<MyNode> nodes = new LinkedList<MyNode>();
		for (MyEdge myEdge : ModelBuilder.getAllEdges()) {
			if (myEdge.getDestinationNode().equals(requirementNode)	&& myEdge.getEdgeType().getName().equals(edgeTypeName)) {
				nodes.add((MyNode) myEdge.getSourceNode());
			}
		}
		return nodes;
	}
	
	public static LinkedList<MyNode> getRequirementSourceNodes(MyNode requirementNode, String edgeTypeName, String nodeTypeName) {
		LinkedList<MyNode> nodes = new LinkedList<MyNode>();
		for (MyEdge myEdge : ModelBuilder.getAllEdges()) {
			if (myEdge.getDestinationNode().equals(requirementNode)	&& 
				myEdge.getEdgeType().getName().equals(edgeTypeName) && 
				( ( (MyNode) myEdge.getSourceNode()).getNodeType().getType().getName().equals(nodeTypeName)) )
				{
				nodes.add((MyNode) myEdge.getSourceNode());
			}
		}
		return nodes;
	}
	
	public static LinkedList<MyNode> getRequirementDestNodes(MyNode requirementNode, String edgeTypeName) {
		LinkedList<MyNode> nodes = new LinkedList<MyNode>();
		for (MyEdge myEdge : ModelBuilder.getAllEdges()) {
			if (myEdge.getSourceNode().equals(requirementNode)	&& myEdge.getEdgeType().getName().equals(edgeTypeName)) {
				nodes.add((MyNode) myEdge.getDestinationNode());
			}
		}
		return nodes;
	}
	
	public static LinkedList<MyNode> getRequirementDestNodes(MyNode requirementNode, String edgeTypeName, String nodeTypeName) {
		LinkedList<MyNode> nodes = new LinkedList<MyNode>();
		for (MyEdge myEdge : ModelBuilder.getAllEdges()) {
			if (myEdge.getSourceNode().equals(requirementNode)	&& 
				myEdge.getEdgeType().getName().equals(edgeTypeName) && 
				( (MyNode) myEdge.getDestinationNode()).getNodeType().getName().equals(nodeTypeName)) {
				nodes.add((MyNode) myEdge.getDestinationNode());
			}
		}
		return nodes;
	}
	
	public static LinkedList<MyNode> getRequirementRelNodes(MyNode requirementNode, String edgeTypeName, String nodeTypeName) {
		LinkedList<MyNode> nodes = new LinkedList<MyNode>();
		for (MyEdge myEdge : ModelBuilder.getAllEdges()) {
			if ((myEdge.getDestinationNode().equals(requirementNode)	&& 
				myEdge.getEdgeType().getName().equals(edgeTypeName) && 
				( (MyNode) myEdge.getSourceNode()).getNodeType().getName().equals(nodeTypeName)) ||
				
				(myEdge.getSourceNode().equals(requirementNode)	&& 
						myEdge.getEdgeType().getName().equals(edgeTypeName) && 
						( (MyNode) myEdge.getDestinationNode()).getNodeType().getName().equals(nodeTypeName))
				) {
				nodes.add((MyNode) myEdge.getSourceNode());
			}
		}
		return nodes;
	}
	
}
