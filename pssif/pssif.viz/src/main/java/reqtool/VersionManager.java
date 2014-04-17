package reqtool;

import graph.model.MyEdge;
import graph.model.MyEdgeType;
import graph.model.MyNode;

import java.util.LinkedList;

import model.ModelBuilder;
import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.common.PSSIFValue;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;

public class VersionManager {
	private static final String SEPARATOR = "_";

	private MyNode selectedNode;
	private PSSIFOption<PSSIFValue> oldVersion;
	private MyNode newVersionNode;
	private MyEdgeType evolvesTo;
	private NodeType selectedNodeType;

	public VersionManager(MyNode myNode) {
		this.evolvesTo = ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_CHRONOLOGICAL_EVOLVES_TO);
		initSelectedNode(myNode);
	}

	private void initSelectedNode(MyNode node) {
		this.selectedNode = node;
		this.selectedNodeType = ModelBuilder.getMetamodel().getNodeType(selectedNode.getNodeType().getName()).getOne();
		this.oldVersion = selectedNodeType.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION).getOne().get(selectedNode.getNode());
		if (oldVersion.isNone() || oldVersion.getOne().asString().length() == 0) {
			setNodeVersion(selectedNode, "0.1");
		}
	}

	public boolean createNewVersion(String newVersion) {
		try {
			initSelectedNode(getMaxVersion());
			if (Double.parseDouble(oldVersion.getOne().asString()) < (Double.parseDouble(newVersion))) {
				createNewVersionNode(newVersion);
				moveEdges(selectedNode, newVersionNode);
				return true;
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("NEW VERSION: FALSE!!!!!!");
		return false;
	}

	private void moveEdges(MyNode selNode, MyNode newVersNode) {
		LinkedList<MyEdge> allEdges = new LinkedList<MyEdge>();
		allEdges.addAll(ModelBuilder.getAllEdges());

		for (MyEdge e : allEdges) {
			if (!e.getEdgeType().equals(evolvesTo)) {
				if (e.getDestinationNode().equals(selNode)
						&& !((MyNode) e.getSourceNode()).getNodeType().toString().equals(PSSIFCanonicMetamodelCreator.N_TEST_CASE)) {
					ModelBuilder.addNewEdgeGUI((MyNode) e.getSourceNode(), newVersNode, e.getEdgeType(), e.isDirected());
					ModelBuilder.getAllEdges().remove(e);
				} else if (e.getSourceNode().equals(selNode)) {
					ModelBuilder.addNewEdgeGUI(newVersNode, (MyNode) e.getDestinationNode(), e.getEdgeType(), e.isDirected());
					ModelBuilder.getAllEdges().remove(e);
				}
			}
		}

	}

	public boolean hasPreviousVersions(MyNode myNode) {
		for (MyEdge e : ModelBuilder.getAllEdges()) {
			if (e.getEdgeType().equals(evolvesTo)
					&& e.getDestinationNode().getNode().equals(myNode.getNode())) {
				return true;
			}
		}
		return false;
	}

	public boolean hasNextVersions(MyNode myNode) {
		for (MyEdge e : ModelBuilder.getAllEdges()) {
			if (e.getEdgeType().equals(evolvesTo)
					&& e.getSourceNode().getNode().equals(myNode.getNode())) {
				return true;
			}
		}
		return false;
	}

	public void createNewVersionNode(String newVersion) {
		this.newVersionNode = ModelBuilder.addNewNodeFromGUI(selectedNode.getNode().getId()
				+ SEPARATOR, selectedNode.getNodeType());
		ModelBuilder.addNewEdgeGUI(selectedNode, newVersionNode, evolvesTo, true);
		setNodeVersion(newVersionNode, newVersion);
		newVersionNode.getNodeType().getType().getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID).getOne().set(newVersionNode.getNode(), PSSIFOption.one(PSSIFValue.create(selectedNode.getNode().getId())));
		newVersionNode.getNodeType().getType().getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME).getOne().set(newVersionNode.getNode(), PSSIFOption.one(PSSIFValue.create(selectedNode.getName())));
		selectedNode.getNodeType().getType().getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID).getOne().set(selectedNode.getNode(), PSSIFOption.one(PSSIFValue.create(newVersionNode.getNode().getId()
				+ "_" + oldVersion.getOne().asString())));
		selectedNode.getNodeType().getType().getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION).getOne().set(selectedNode.getNode(), PSSIFOption.one(PSSIFValue.create(String.valueOf(oldVersion.getOne().asString()))));
	}

	public MyNode getMaxVersion() {
		MyNode maxVersionNode = null;
		double maxVersion = Integer.MIN_VALUE;
		String[] idNV = selectedNode.getNode().getId().toString().split(SEPARATOR);
		for (MyNode newNode : ModelBuilder.getAllNodes()) {
			String[] idNVNew = newNode.getNode().getId().toString().split(SEPARATOR);
			if (idNVNew.length > 0 && idNVNew[0].equals(idNV[0])) {
				PSSIFOption<PSSIFValue> version = selectedNodeType.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION).getOne().get(newNode.getNode());
				if (!version.isNone()) {
					double versionDbl = Double.parseDouble(version.getOne().asString());
					if (versionDbl > maxVersion) {
						maxVersion = versionDbl;
						maxVersionNode = newNode;
					}
				}
			}
		}
		return maxVersionNode;
	}

	private Attribute getNodeVersion(MyNode node) {
		return node.getNodeType().getType().getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION).getOne();
	}

	private void setNodeVersion(MyNode node, String version) {
		getNodeVersion(node).set(node.getNode(), PSSIFOption.one(PSSIFValue.create(String.valueOf(version))));
	}
}
