package model;

import graph.model.MyEdgeType;
import graph.model.MyNode;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.pssif.consistencyDataStructures.NodeAndType;
import org.pssif.exception.ConsistencyException;
import org.pssif.mainProcesses.Methods;
import org.pssif.mergedDataStructures.MergedNodePair;

import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.common.PSSIFValue;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.AttributeGroup;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.JunctionNodeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.NodeTypeBase;
import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;

/**
 * Very basic Model Merger. Can merge two models into one model. Does only copy
 * everything from one model to the other. No matching at all!
 * 
 * @author Luc
 * 
 */
public class ModelMerger {

	private Model modelOrigin;
	private Model modelNew;
	private Metamodel metaModelOrigin, metaModelNew;

	private HashMap<Node, Node> oldToNewNodes;

	/**
	 * this variable maps the unmatched nodes of the old model to the respective
	 * node in the new model
	 */
	private Map<NodeAndType, Node> nodeTransferUnmatchedOldToNewModel = new HashMap<NodeAndType, Node>();
	/**
	 * this variable maps the traced nodes of the old model to the respective
	 * node in the new model
	 */
	private Map<NodeAndType, Node> nodeTransferTracedOldToNewModel = new HashMap<NodeAndType, Node>();

	private List<MergedNodePair> mergedNodes;
	private List<MergedNodePair> tracedNodes;

	private List<NodeAndType> unmatchedNodesOrigin;

	/**
	 * the first imported model
	 */
	MyModelContainer activeModel;

	/**
	 * the recently imported model
	 */
	MyModelContainer newModel;

	/**
	 * @return the oldToNewNodes
	 */
	public HashMap<Node, Node> getOldToNewNodes() {
		return oldToNewNodes;
	}

	/**
	 * merge two models into one model in respect of the given metamodel
	 * 
	 * @param model1
	 *            first model
	 * @param modelNew
	 *            second model
	 * @param metaModelOrigin
	 *            metamodel
	 * @return the merged model
	 */
	public Model mergeModels(Model model1, Model model2, Metamodel meta) {
		this.modelOrigin = model1;
		this.modelNew = model2;
		this.metaModelOrigin = meta;
		this.oldToNewNodes = new HashMap<Node, Node>();

		// printNbEdges(model1);
		// printNbNodes(model1);
		// start transformation operations
		addAllNodes();
		addAllJunctionNodes();
		addAllEdges();

		// printNbEdges(model1);
		// printNbNodes(model1);

		return this.modelOrigin;
	}

	/**
	 * This method starts the real merge between two given models.
	 * 
	 * @param modelOrigin
	 *            the first imported model
	 * @param modelNew
	 *            the recent imported model
	 * @param activeModel
	 *            the first imported model as a container
	 * @param metaModelOrigin
	 *            the meta model of the first model
	 * @return the recently imported model with the old nodes merged into it
	 * @author Andreas
	 * @param unmatchedNodesOrigin
	 *            the nodes of the first model which have no trace/merge partner
	 *            in the recent model
	 * @param tracedNodes
	 *            the nodes of the first model which have a trace partner in the
	 *            recent model
	 */
	public Model mergeModels(Model modelOrigin, Model modelNew,
			Metamodel metaModelOriginal, Metamodel metaModelNew,
			List<MergedNodePair> mergedNodes,
			List<NodeAndType> unmatchedNodesOrigin,
			List<MergedNodePair> tracedNodes, MyModelContainer activeModel) {
		this.modelOrigin = modelOrigin;
		this.modelNew = modelNew;
		this.metaModelOrigin = metaModelOriginal;
		this.metaModelNew = metaModelNew;
		this.mergedNodes = mergedNodes;
		this.unmatchedNodesOrigin = unmatchedNodesOrigin;
		this.activeModel = activeModel;
		this.tracedNodes = tracedNodes;

		return mergeNodes();
	}

	/**
	 * This method adds every node from the old model which is not marked as to
	 * be merged to the new model. The nodes which are marked as to be merged
	 * are deleted and not transfered to the new model. But their edges are
	 * transfered to the new model.
	 * 
	 * @return the new active model.
	 * @author Andreas
	 */
	private Model mergeNodes() {

		newModel = new MyModelContainer(modelNew, metaModelNew);

		// adding unmatched nodes to the new model
		for (NodeAndType unmergedNode : unmatchedNodesOrigin) {

			Node newNode = addNodeToNewModel(unmergedNode.getNode(),
					unmergedNode.getType());

			if (newNode == null) {
				throw new ConsistencyException("The old, unmatched node: "
						+ Methods.findName(unmergedNode.getType(),
								unmergedNode.getNode())
						+ " couln't be transferred/created in the new model.");
			} else {
				nodeTransferUnmatchedOldToNewModel.put(unmergedNode, newNode);
			}
		}

		// transferring the traced nodes to the new model
		for (MergedNodePair tracedPair : tracedNodes) {

			Node newNode = addNodeToNewModel(tracedPair.getNodeOriginalModel(),
					tracedPair.getTypeOriginModel());

			if (newNode == null) {
				throw new ConsistencyException("The old (to be traced) node: "
						+ Methods.findName(tracedPair.getTypeOriginModel(),
								tracedPair.getNodeOriginalModel())
						+ " couln't be transferred/created in the new model.");
			} else {
				nodeTransferTracedOldToNewModel.put(
						new NodeAndType(tracedPair.getNodeOriginalModel(),
								tracedPair.getTypeOriginModel()), newNode);
			}
		}

		// transferring the egdes of the unmatched nodes to the new model
		transferOldEdges();

		// creating tracelinks
		setTracedLinks();

		return newModel.getModel();
	}

	/**
	 * This method creates the tracelink edges between the nodepairs given in
	 * the traceNode list.
	 * 
	 * @author Andreas
	 */
	private void setTracedLinks() {
		MyEdgeType edgeType = new MyEdgeType(
				metaModelNew
						.getEdgeType(
								PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_CHRONOLOGICAL_EVOLVES_TO)
						.getOne(), 6);

		Iterator<Entry<NodeAndType, Node>> it = nodeTransferTracedOldToNewModel
				.entrySet().iterator();

		NodeAndType tempNodeOrigin;
		Node tempNodeTransferred;

		MyNode searchedFromNodeNew = null, searchedToNodeNew = null;

		// Iterate over all old nodes (which have a trace partner) transferred
		// to the new model
		while (it.hasNext()) {
			Map.Entry<NodeAndType, Node> pairs = (Entry<NodeAndType, Node>) it
					.next();
			tempNodeOrigin = pairs.getKey();
			tempNodeTransferred = pairs.getValue();

			for (MergedNodePair tracedPair : tracedNodes) {
				// here the tracepartner of the current node is searched
				if (Methods.findGlobalID(tempNodeOrigin.getNode(),
						tempNodeOrigin.getType()).equals(
						Methods.findGlobalID(tracedPair.getNodeOriginalModel(),
								tracedPair.getTypeOriginModel()))) {

					// here the two nodes which shall be connected by a
					// tracelink are retrieved in the new model
					for (MyNode tempNode : newModel.getAllNodes()) {
						if (Methods.findGlobalID(tempNode.getNode(),
								tempNode.getNodeType().getType()).equals(
								Methods.findGlobalID(pairs.getValue(),
										tempNodeOrigin.getType()))) {
							searchedFromNodeNew = tempNode;
						}
						if (Methods.findGlobalID(tempNode.getNode(),
								tempNode.getNodeType().getType()).equals(
								Methods.findGlobalID(
										tracedPair.getNodeNewModel(),
										tracedPair.getTypeNewModel()))) {
							searchedToNodeNew = tempNode;
						}
					}

					if (searchedToNodeNew == null) {
						throw new NullPointerException(
								"Error at tracelink generation: The To node: "
										+ Methods.findName(tracedPair
												.getTypeOriginModel(),
												tracedPair
														.getNodeOriginalModel())
										+ " should have been transferred to the new model but it couldn't be found in the new model.");
					}

					if (searchedFromNodeNew == null) {
						throw new NullPointerException(
								"Error at tracelink generation: The From node "
										+ Methods.findName(
												tempNodeOrigin.getType(),
												tempNodeOrigin.getNode())
										+ " should have been transferred to the new model. But it couldn't be found in the new model.");
					}

					// here the tracelink is generated
					newModel.addNewEdgeGUI(searchedFromNodeNew,
							searchedToNodeNew, edgeType, true);
				}
			}
		}
	}

	/**
	 * This method iterates over all nodes from the original model which have
	 * been transferred to the new model but have no merge nor tracepartner
	 * 
	 * @author Andreas
	 */
	private void transferOldEdges() {
		Iterator<Entry<NodeAndType, Node>> it = nodeTransferUnmatchedOldToNewModel
				.entrySet().iterator();
		NodeAndType tempNodeOrigin;
		Node tempNodeTransferred;

		// Iterate over all old nodes transferred to the new model
		while (it.hasNext()) {
			Map.Entry<NodeAndType, Node> pairs = (Entry<NodeAndType, Node>) it
					.next();
			tempNodeOrigin = pairs.getKey();

			/**
			 * the old node in the new model
			 */
			tempNodeTransferred = pairs.getValue();

			checkForEdgesToTransfer(tempNodeTransferred,
					tempNodeOrigin.getType(), tempNodeOrigin.getNode(),
					tempNodeOrigin.getType());
		}
	}

	/**
	 * This method calls methods to check the incoming and outgoing edges of the
	 * node which is merged into the new model. So the sorrounding edges can be
	 * created in the new model.
	 * 
	 * @param nodeNew
	 *            the node in the new model to which the old edge shall link
	 * @param nodeOrigin
	 *            the node of the origin model from which the old edge shall
	 *            link
	 * @author Andreas
	 */
	private void checkForEdgesToTransfer(Node nodeNew,
			NodeTypeBase typeNodeNew, Node nodeOrigin,
			NodeTypeBase typeNodeOrigin) {

		checkIncomingEdges(nodeNew, typeNodeNew, nodeOrigin, typeNodeOrigin);
		checkOutgoingEdges(typeNodeOrigin, nodeOrigin, nodeNew, typeNodeNew);
	}

	/**
	 * This method gets every incoming edges of the given node. Then the edge is
	 * created in the new model.
	 * 
	 * @param nodeNew
	 *            the transferred node object
	 * @param nodeNewType
	 *            the type of the transferred node
	 * @param nodeOrigin
	 *            the node whichs edges are retrieved in the original mode
	 * @param nodeTypeOrigin
	 *            the type of the original node
	 * 
	 * @author Andreas
	 */
	private void checkIncomingEdges(Node nodeNew, NodeTypeBase nodeNewType,
			Node nodeOrigin, NodeTypeBase nodeTypeOrigin) {

		Node tempFromEdgeNode;
		NodeTypeBase tempFromEdgeNodeType;

		MyNode searchedFromNodeNew = null, searchedToNodeNew = null;

		NodeAndType tempNodeOrigin;

		// get all incoming edges here
		for (EdgeType edgeType : metaModelNew.getEdgeTypes()) {
			for (ConnectionMapping incomingMapping : edgeType
					.getIncomingMappings(nodeTypeOrigin)) {
				for (Edge incomingEdge : incomingMapping
						.applyIncoming(nodeOrigin)) {

					// undirected edges have to be handled separateley. They are
					// only transferred in the method checkOutgoingEdges because
					// if these edges are transfered in both methods,
					// checkIncoming and checkOutgoingEdges, the undirected edge
					// will appear twice in the new model
					if (!isEdgeDirected(edgeType, incomingEdge)) {
						continue;
					}

					tempFromEdgeNode = incomingMapping.applyFrom(incomingEdge);
					tempFromEdgeNodeType = incomingMapping.getFrom();

					// here a bug is fixed, that directed edges between
					// unmatched nodes double each time when a new model is
					// merged into the pssif fw. The problem is if a directed
					// edge is present between two unmatched nodes the edge
					// would appear twice in the merged model because the edge
					// is created once through the incoming and once through the
					// outgoing mapping
					// This problem is solved by ignoring the problematic edge
					// in the iteration of the incoming mappings and only
					// handling it in the iteration of the outgoing mappings.
					boolean outGoingMappingThere = false;

					for (ConnectionMapping outgoingMapping : edgeType
							.getOutgoingMappings(tempFromEdgeNodeType)) {
						for (Edge outgoingEdge : outgoingMapping
								.applyOutgoing(tempFromEdgeNode)) {
							if (outgoingMapping.applyTo(outgoingEdge).equals(
									nodeOrigin)) {
								for (NodeAndType unmatchedNode : unmatchedNodesOrigin) {
									if (unmatchedNode.getNode().equals(
											tempFromEdgeNode)) {
										outGoingMappingThere = true;
									}
								}
							}
						}
					}

					if (outGoingMappingThere) {
						continue;
					}

//					// TODO handle conjunctions separately
//					// don't match conjunctions
//					if (tempFromEdgeNodeType.getName().equals(
//							PSSIFCanonicMetamodelCreator.N_CONJUNCTION)) {
//						continue;
//					}

					Iterator<Entry<NodeAndType, Node>> it = nodeTransferUnmatchedOldToNewModel
							.entrySet().iterator();

					// look up whether the from node of the old edge has also
					// been transfered to the new model. If this is the case the
					// edge is created between the two nodes in the new model
					while (it.hasNext()) {
						Map.Entry<NodeAndType, Node> pairs = (Entry<NodeAndType, Node>) it
								.next();
						tempNodeOrigin = pairs.getKey();

						if (Methods.findGlobalID(tempFromEdgeNode,
								tempFromEdgeNodeType).equals(
								Methods.findGlobalID(tempNodeOrigin.getNode(),
										tempNodeOrigin.getType()))) {

							// here the two nodes which shall be connected by a
							// edge are retrieved in the new model
							for (MyNode tempNode : newModel.getAllNodes()) {
								if (Methods.findGlobalID(tempNode.getNode(),
										tempNode.getNodeType().getType())
										.equals(Methods.findGlobalID(
												pairs.getValue(),
												tempNodeOrigin.getType()))) {
									searchedFromNodeNew = tempNode;
								}
								if (Methods.findGlobalID(tempNode.getNode(),
										tempNode.getNodeType().getType())
										.equals(Methods.findGlobalID(nodeNew,
												nodeNewType))) {
									searchedToNodeNew = tempNode;
								}
							}

							if (searchedFromNodeNew == null) {
								throw new NullPointerException(
										"Error at incoming edge transfer: The from node: "
												+ Methods.findName(
														tempFromEdgeNodeType,
														tempFromEdgeNode)
												+ " should have been transferred to the new model. But it couldn't be found in the new model.");
							}

							if (searchedToNodeNew == null) {
								throw new NullPointerException(
										"Error at incoming edge transfer: The to node "
												+ Methods.findName(
														nodeTypeOrigin,
														nodeOrigin)
												+ " should have been transferred to the new model. But it couldn't be found in the new model.");
							}

							// create the new edge in the new model
							Edge newEdge = incomingMapping.create(modelNew,
									searchedFromNodeNew.getNode(),
									searchedToNodeNew.getNode());

							// transfer the attributes of the old to the new
							// edge
							transferEdgeAttributes(incomingEdge, newEdge,
									edgeType);

							break;
						}
					}

					// if the from node has been deleted because it appears in
					// the recently imported model, too. Then the newer version
					// becomes the from node of the edge
					for (MergedNodePair actMerged : mergedNodes) {
						if (Methods.findGlobalID(tempFromEdgeNode,
								tempFromEdgeNodeType).equals(
								Methods.findGlobalID(
										actMerged.getNodeOriginalModel(),
										actMerged.getTypeOriginModel()))) {

							// here the two nodes which shall be connected by a
							// edge are retrieved in the new model
							for (MyNode tempNode : newModel.getAllNodes()) {
								if (Methods.findGlobalID(tempNode.getNode(),
										tempNode.getNodeType().getType())
										.equals(Methods.findGlobalID(
												actMerged.getNodeNewModel(),
												actMerged.getTypeNewModel()))) {
									searchedFromNodeNew = tempNode;
								}
								if (Methods.findGlobalID(tempNode.getNode(),
										tempNode.getNodeType().getType())
										.equals(Methods.findGlobalID(nodeNew,
												nodeNewType))) {
									searchedToNodeNew = tempNode;
								}
							}

							if (searchedFromNodeNew == null) {
								throw new NullPointerException(
										"Error at incoming edge transfer: The from node: "
												+ Methods.findName(
														actMerged
																.getTypeOriginModel(),
														actMerged
																.getNodeOriginalModel())
												+ " should be available as a new version in the new model but it couldn't be found in the new model.");
							}

							if (searchedToNodeNew == null) {
								throw new NullPointerException(
										"Error at incoming edge transfer: The to node "
												+ Methods.findName(
														nodeTypeOrigin,
														nodeOrigin)
												+ " should have been transferred to the new model. But it couldn't be found in the new model.");
							}

							// create the new edge in the new model
							Edge newEdge = incomingMapping.create(modelNew,
									searchedFromNodeNew.getNode(),
									searchedToNodeNew.getNode());

							// transfer the attributes of the old to the new
							// edge
							transferEdgeAttributes(incomingEdge, newEdge,
									edgeType);

							break;
						}
					}

				}
			}
		}
	}

	/**
	 * This method gets every outgoing edges of the given node. Then the edge is
	 * created in the new model.
	 * 
	 * 
	 * @param nodeNew
	 *            the transferred node object
	 * @param nodeNewType
	 *            the type of the transferred node
	 * @param nodeOrigin
	 *            the node whichs edges are retrieved in the original mode
	 * @param nodeTypeOrigin
	 *            the type of the original node
	 * @author Andreas
	 */
	private void checkOutgoingEdges(NodeTypeBase nodeTypeOrigin,
			Node nodeOrigin, Node nodeNew, NodeTypeBase nodeNewType) {

		Node tempToEdgeNode;
		NodeTypeBase tempToEdgeNodeType;

		MyNode searchedFromNodeNew = null, searchedToNodeNew = null;

		NodeAndType tempNodeOrigin;

		// retrieving all outgoing edges here
		for (EdgeType edgeType : metaModelNew.getEdgeTypes()) {
			for (ConnectionMapping outgoingMapping : edgeType
					.getOutgoingMappings(nodeTypeOrigin)) {
				for (Edge outgoingEdge : outgoingMapping
						.applyOutgoing(nodeOrigin)) {

					tempToEdgeNode = outgoingMapping.applyTo(outgoingEdge);
					tempToEdgeNodeType = outgoingMapping.getTo();

					// TODO handle conjunctions separately
					// don't match conjunctions
					if (tempToEdgeNodeType.getName().equals(
							PSSIFCanonicMetamodelCreator.N_CONJUNCTION)) {
						continue;
					}

					Iterator<Entry<NodeAndType, Node>> it = nodeTransferUnmatchedOldToNewModel
							.entrySet().iterator();

					// look up whether the to node of the old edge has also
					// been transfered to the new model. If this is the case the
					// edge is created between the two nodes in the new model
					while (it.hasNext()) {
						Map.Entry<NodeAndType, Node> pairs = (Entry<NodeAndType, Node>) it
								.next();
						tempNodeOrigin = pairs.getKey();

						if (Methods.findGlobalID(tempToEdgeNode,
								tempToEdgeNodeType).equals(
								Methods.findGlobalID(tempNodeOrigin.getNode(),
										tempNodeOrigin.getType()))) {

							// here the two nodes which shall be connected by a
							// edge are retrieved in the new model
							for (MyNode tempNode : newModel.getAllNodes()) {
								if (Methods.findGlobalID(tempNode.getNode(),
										tempNode.getNodeType().getType())
										.equals(Methods.findGlobalID(
												pairs.getValue(),
												tempNodeOrigin.getType()))) {
									searchedToNodeNew = tempNode;
								}
								if (Methods.findGlobalID(tempNode.getNode(),
										tempNode.getNodeType().getType())
										.equals(Methods.findGlobalID(nodeNew,
												nodeNewType))) {
									searchedFromNodeNew = tempNode;
								}
							}

							if (searchedToNodeNew == null) {
								throw new NullPointerException(
										"Error at outgoing edge transfer: The To node: "
												+ Methods.findName(
														tempToEdgeNodeType,
														tempToEdgeNode)
												+ " should have been transferred to the new model but it couldn't be found in the new model.");
							}

							if (searchedFromNodeNew == null) {
								throw new NullPointerException(
										"Error at outgoing edge transfer: The From node "
												+ Methods.findName(
														nodeTypeOrigin,
														nodeOrigin)
												+ " should have been transferred to the new model. But it couldn't be found in the new model.");
							}

							// create the new edge in the new model
							Edge newEdge = outgoingMapping.create(modelNew,
									searchedFromNodeNew.getNode(),
									searchedToNodeNew.getNode());

							// transfer the attributes of the old to the new
							// edge
							transferEdgeAttributes(outgoingEdge, newEdge,
									edgeType);

							break;
						}
					}

					// if the to node has been deleted because it appears in
					// the recently imported model, too. Then the newer version
					// becomes the to node of the edge
					for (MergedNodePair actMerged : mergedNodes) {
						if (Methods.findGlobalID(tempToEdgeNode,
								tempToEdgeNodeType).equals(
								Methods.findGlobalID(
										actMerged.getNodeOriginalModel(),
										actMerged.getTypeOriginModel()))) {

							// here the two nodes which shall be connected by a
							// edge are retrieved in the new model
							for (MyNode tempNode : newModel.getAllNodes()) {
								if (Methods.findGlobalID(tempNode.getNode(),
										tempNode.getNodeType().getType())
										.equals(Methods.findGlobalID(
												actMerged.getNodeNewModel(),
												actMerged.getTypeNewModel()))) {
									searchedToNodeNew = tempNode;
								}
								if (Methods.findGlobalID(tempNode.getNode(),
										tempNode.getNodeType().getType())
										.equals(Methods.findGlobalID(nodeNew,
												nodeNewType))) {
									searchedFromNodeNew = tempNode;
								}
							}

							if (searchedToNodeNew == null) {
								throw new NullPointerException(
										"Error at outgoing edge transfer: The To node: "
												+ Methods.findName(
														actMerged
																.getTypeOriginModel(),
														actMerged
																.getNodeOriginalModel())
												+ " should be available as a new version in the new model but it couldn't be found in the new model.");
							}

							if (searchedFromNodeNew == null) {
								throw new NullPointerException(
										"Error at outgoing edge transfer: The From node "
												+ Methods.findName(
														nodeTypeOrigin,
														nodeOrigin)
												+ " should have been transferred to the new model. But it couldn't be found in the new model.");
							}

							// create the new edge in the new model
							Edge newEdge = outgoingMapping.create(modelNew,
									searchedFromNodeNew.getNode(),
									searchedToNodeNew.getNode());

							// transfer the attributes of the old to the new
							// edge
							transferEdgeAttributes(outgoingEdge, newEdge,
									edgeType);

							break;
						}
					}

				}
			}
		}
	}

	/**
	 * @author Andreas
	 * @param type
	 * @param edge
	 * @return
	 */
	private boolean isEdgeDirected(EdgeType type, Edge edge) {
		Collection<AttributeGroup> attrgroups = type.getAttributeGroups();

		boolean isDirectedEdge = false;

		if (attrgroups != null) {
			for (AttributeGroup ag : attrgroups) {

				Collection<Attribute> attr = ag.getAttributes();

				for (Attribute a : attr) {
					if (!a.getName().equals(
							PSSIFConstants.BUILTIN_ATTRIBUTE_DIRECTED)) {
						continue;
					}

					PSSIFOption<PSSIFValue> attrvalue = a.get(edge);

					if (attrvalue != null) {
						isDirectedEdge = attrvalue.getOne().asBoolean()
								.booleanValue();
					}
				}
			}
		}

		return isDirectedEdge;
	}

	private void printNbEdges(Model model) {
		int counter = 0;
		for (EdgeType t : metaModelOrigin.getEdgeTypes()) {
			PSSIFOption<ConnectionMapping> tmp = t.getMappings();

			if (tmp != null && (tmp.isMany() || tmp.isOne())) {
				Set<ConnectionMapping> mappings;

				if (tmp.isMany())
					mappings = tmp.getMany();
				else {
					mappings = new HashSet<ConnectionMapping>();
					mappings.add(tmp.getOne());
				}

				for (ConnectionMapping mapping : mappings) {
					PSSIFOption<Edge> edges = mapping.apply(model);

					if (edges.isMany()) {
						for (Edge e : edges.getMany()) {
							counter++;
						}
					} else {
						if (edges.isOne()) {
							counter++;
						}
					}
				}
			}
		}
		System.out.println("Nb edges :" + counter);
	}

	private void printNbNodes(Model model) {
		int counter = 0;
		for (NodeType t : metaModelOrigin.getNodeTypes()) {
			// get all the Nodes of this type
			PSSIFOption<Node> tempNodes = t.apply(model, true);

			if (tempNodes.isMany()) {
				Set<Node> many = tempNodes.getMany();
				for (Node n : many) {
					counter++;
				}
			} else if (tempNodes.isOne()) {
				counter++;
			}

		}
		System.out.println("Nb nodes :" + counter);
	}

	/**
	 * add all the Nodes from modelNew to model1
	 */
	private void addAllNodes() {
		// loop over all Node types
		for (NodeType t : metaModelOrigin.getNodeTypes()) {
			// get all the Nodes of this type
			PSSIFOption<Node> tempNodes = t.apply(modelNew, false);

			if (tempNodes.isMany()) {
				Set<Node> many = tempNodes.getMany();
				for (Node n : many) {
					// copy it to model1
					addNode(n, t);
				}
			} else {
				if (tempNodes.isOne()) {
					Node current = tempNodes.getOne();
					// copy it to model1
					addNode(current, t);
				}
			}
		}
	}

	/**
	 * add all the JunctionNodes from modelNew to model1
	 */
	private void addAllJunctionNodes() {
		// loop over all JunctionNode types
		for (JunctionNodeType t : metaModelOrigin.getJunctionNodeTypes()) {
			// get all the Nodes of this type
			PSSIFOption<Node> tempNodes = t.apply(modelNew, false);

			if (tempNodes.isMany()) {
				Set<Node> many = tempNodes.getMany();
				for (Node n : many) {
					// copy it to model1
					addJunctionNode(n, t);
				}
			} else {
				if (tempNodes.isOne()) {
					Node current = tempNodes.getOne();
					// copy it to model1
					addJunctionNode(current, t);
				}
			}
		}
	}

	/**
	 * Add all the Edges from modelNew to model1
	 */
	private void addAllEdges() {
		for (EdgeType t : metaModelOrigin.getEdgeTypes()) {
			PSSIFOption<ConnectionMapping> tmp = t.getMappings();

			if (tmp != null && (tmp.isMany() || tmp.isOne())) {
				Set<ConnectionMapping> mappings;

				if (tmp.isMany())
					mappings = tmp.getMany();
				else {
					mappings = new HashSet<ConnectionMapping>();
					mappings.add(tmp.getOne());
				}

				for (ConnectionMapping mapping : mappings) {
					PSSIFOption<Edge> edges = mapping.apply(modelNew);
					if (edges.isMany()) {
						for (Edge e : edges.getMany()) {
							Node source = mapping.applyFrom(e);
							Node target = mapping.applyTo(e);

							Edge newEdge = mapping.create(modelOrigin,
									oldToNewNodes.get(source),
									oldToNewNodes.get(target));
							transferEdgeAttributes(e, newEdge, t);
						}
					} else if (edges.isOne()) {
						Edge e = edges.getOne();
						Node source = mapping.applyFrom(e);
						Node target = mapping.applyTo(e);

						Edge newEdge = mapping.create(modelOrigin,
								oldToNewNodes.get(source),
								oldToNewNodes.get(target));
						transferEdgeAttributes(e, newEdge, t);
					}
				}
			}
		}
	}

	/**
	 * Add a given Node to Model1
	 * 
	 * @param dataNode
	 *            the model which should be transfered to model1
	 * @param currentType
	 *            the type of the dataNode
	 */
	private void addNode(Node dataNode, NodeType currentType) {
		// create Node
		Node newNode = currentType.create(modelOrigin);

		oldToNewNodes.put(dataNode, newNode);

		// transfer attribute groups
		Collection<AttributeGroup> attrgroups = currentType
				.getAttributeGroups();

		if (attrgroups != null) {
			for (AttributeGroup ag : attrgroups) {
				// transfer attribute values
				Collection<Attribute> attr = ag.getAttributes();

				for (Attribute a : attr) {
					PSSIFOption<PSSIFValue> attrvalue = a.get(dataNode);

					if (attrvalue != null) {
						currentType.getAttribute(a.getName()).getOne()
								.set(newNode, attrvalue);
					}
				}
			}
		}

		// transfer annotations

		PSSIFOption<Entry<String, String>> tmp = dataNode.getAnnotations();

		Set<Entry<String, String>> annotations = null;

		if (tmp != null && (tmp.isMany() || tmp.isOne())) {
			if (tmp.isMany())
				annotations = tmp.getMany();
			else {
				annotations = new HashSet<Entry<String, String>>();
				annotations.add(tmp.getOne());
			}
		}

		if (annotations != null) {
			for (Entry<String, String> a : annotations) {
				newNode.annotate(a.getKey(), a.getValue());
			}
		}

		/*
		 * Collection<Annotation> annotations =
		 * currentType.getAnnotations(dataNode);
		 * 
		 * if (annotations!=null) { for (Annotation a : annotations) {
		 * PSSIFOption<String> value = a.getValue(); if (value!=null &&
		 * value.isOne()) { currentType.setAnnotation(newNode,
		 * a.getKey(),value.getOne()); }
		 * 
		 * if (value!=null && value.isMany()) { Set<String> concreteValues =
		 * value.getMany(); for (String s : concreteValues) {
		 * currentType.setAnnotation(newNode, a.getKey(),s); } } } }
		 */
	}

	private Node addNodeToNewModel(Node dataNode, NodeTypeBase nodeTypeBase) {
		Model modelNew = newModel.getModel();

		// create Node
		Node newNode = nodeTypeBase.create(modelNew);

		// transfer attribute groups
		Collection<AttributeGroup> attrgroups = nodeTypeBase
				.getAttributeGroups();

		if (attrgroups != null) {
			for (AttributeGroup ag : attrgroups) {
				// transfer attribute values
				Collection<Attribute> attr = ag.getAttributes();

				for (Attribute a : attr) {
					PSSIFOption<PSSIFValue> attrvalue = a.get(dataNode);

					if (attrvalue != null) {
						nodeTypeBase.getAttribute(a.getName()).getOne()
								.set(newNode, attrvalue);
					}
				}
			}
		}

		// transfer annotations

		PSSIFOption<Entry<String, String>> tmp = dataNode.getAnnotations();

		Set<Entry<String, String>> annotations = null;

		if (tmp != null && (tmp.isMany() || tmp.isOne())) {
			if (tmp.isMany())
				annotations = tmp.getMany();
			else {
				annotations = new HashSet<Entry<String, String>>();
				annotations.add(tmp.getOne());
			}
		}

		if (annotations != null) {
			for (Entry<String, String> a : annotations) {
				newNode.annotate(a.getKey(), a.getValue());
			}
		}
		// TODO Performance improvement: don't create a new mymodel container
		// each time you copy a node from an old to a new model
		this.newModel = new MyModelContainer(modelNew, newModel.getMetamodel());

		return newNode;
	}

	/**
	 * Add a given JunctionNode to Model1
	 * 
	 * @param dataNode
	 *            the model which should be transfered to model1
	 * @param currentType
	 *            the type of the dataNode
	 */
	private void addJunctionNode(Node dataNode, JunctionNodeType currentType) {
		// create Node
		Node newNode = currentType.create(modelOrigin);

		oldToNewNodes.put(dataNode, newNode);

		// transfer attribute groups
		Collection<AttributeGroup> attrgroups = currentType
				.getAttributeGroups();

		if (attrgroups != null) {
			for (AttributeGroup ag : attrgroups) {
				// transfer attribute values
				Collection<Attribute> attr = ag.getAttributes();

				for (Attribute a : attr) {
					PSSIFOption<PSSIFValue> attrvalue = a.get(dataNode);

					if (attrvalue != null) {
						currentType.getAttribute(a.getName()).getOne()
								.set(newNode, attrvalue);
					}
				}
			}
		}

		// transfer annotations

		PSSIFOption<Entry<String, String>> tmp = dataNode.getAnnotations();

		Set<Entry<String, String>> annotations = null;

		if (tmp != null && (tmp.isMany() || tmp.isOne())) {
			if (tmp.isMany())
				annotations = tmp.getMany();
			else {
				annotations = new HashSet<Entry<String, String>>();
				annotations.add(tmp.getOne());
			}
		}

		if (annotations != null) {
			for (Entry<String, String> a : annotations) {
				newNode.annotate(a.getKey(), a.getValue());
			}
		}
	}

	/**
	 * transfer all the attributes and annotations from one Edge to the other
	 * 
	 * @param oldEdge
	 *            contains all the information which should be transfered
	 * @param newEdge
	 *            the edge which should get all the information
	 * @param type
	 *            the type of both edges
	 */
	private void transferEdgeAttributes(Edge oldEdge, Edge newEdge,
			EdgeType type) {
		// transfer attribute groups
		Collection<AttributeGroup> attrgroups = type.getAttributeGroups();

		if (attrgroups != null) {
			for (AttributeGroup ag : attrgroups) {
				// transfer attribute values
				Collection<Attribute> attr = ag.getAttributes();

				for (Attribute a : attr) {
					PSSIFOption<PSSIFValue> attrvalue = a.get(oldEdge);

					if (attrvalue != null) {
						type.getAttribute(a.getName()).getOne()
								.set(newEdge, attrvalue);
					}
				}
			}
		}

		// transfer annotations

		PSSIFOption<Entry<String, String>> tmp = oldEdge.getAnnotations();

		Set<Entry<String, String>> annotations = null;

		if (tmp != null && (tmp.isMany() || tmp.isOne())) {
			if (tmp.isMany())
				annotations = tmp.getMany();
			else {
				annotations = new HashSet<Entry<String, String>>();
				annotations.add(tmp.getOne());
			}
		}

		if (annotations != null) {
			for (Entry<String, String> a : annotations) {
				newEdge.annotate(a.getKey(), a.getValue());
			}
		}

		/*
		 * Collection<Annotation> annotations = type.getAnnotations(oldEdge);
		 * 
		 * if (annotations!=null) { for (Annotation a : annotations) {
		 * PSSIFOption<String> value = a.getValue(); if (value!=null &&
		 * value.isOne()) { type.setAnnotation(newEdge,
		 * a.getKey(),value.getOne()); }
		 * 
		 * if (value!=null && value.isMany()) { Set<String> concreteValues =
		 * value.getMany(); for (String s : concreteValues) {
		 * type.setAnnotation(newEdge, a.getKey(),s); } } } }
		 */
	}
}
