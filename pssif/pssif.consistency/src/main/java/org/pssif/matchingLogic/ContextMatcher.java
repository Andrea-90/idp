package org.pssif.matchingLogic;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.pssif.comparedDataStructures.ComparedNodePair;
import org.pssif.consistencyDataStructures.ConsistencyData;
import org.pssif.consistencyDataStructures.NodeAndType;
import org.pssif.consistencyDataStructures.Token;
import org.pssif.mainProcesses.Methods;
import org.pssif.textNormalization.Normalizer;

import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.NodeTypeBase;
import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.core.metamodel.impl.ReadFromNodesOperation;
import de.tum.pssif.core.metamodel.impl.ReadToNodesOperation;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;

/**
 * This class represents the implementation of the context matching metric. It
 * compares the nodes sorrounding the two given ones with the chosen metrics.
 * 
 * @author Andreas
 * 
 */
public class ContextMatcher extends MatchMethod {

	/**
	 * These are the subclasses of PSIFFDevArtifacts that are checked for
	 * consistency
	 */
	private final String[] pssifDevArtifactSubClasses = {
			PSSIFCanonicMetamodelCreator.N_FUNCTIONALITY,
			PSSIFCanonicMetamodelCreator.N_REQUIREMENT,
			PSSIFCanonicMetamodelCreator.N_USE_CASE,
			PSSIFCanonicMetamodelCreator.N_TEST_CASE,
			PSSIFCanonicMetamodelCreator.N_VIEW,
			PSSIFCanonicMetamodelCreator.N_EVENT,
			PSSIFCanonicMetamodelCreator.N_ISSUE,
			PSSIFCanonicMetamodelCreator.N_DECISION,
			PSSIFCanonicMetamodelCreator.N_CHANGE_EVENT };

	/**
	 * These are the subclasses of PSIFFSolArtifacts that are checked for
	 * consistency
	 */
	private final String[] pssifSolArtifactSubClasses = {
			PSSIFCanonicMetamodelCreator.N_BLOCK,
			PSSIFCanonicMetamodelCreator.N_FUNCTION,
			PSSIFCanonicMetamodelCreator.N_ACTIVITY,
			PSSIFCanonicMetamodelCreator.N_STATE,
			PSSIFCanonicMetamodelCreator.N_ACTOR,
			PSSIFCanonicMetamodelCreator.N_SERVICE,
			PSSIFCanonicMetamodelCreator.N_SOFTWARE,
			PSSIFCanonicMetamodelCreator.N_HARDWARE,
			PSSIFCanonicMetamodelCreator.N_MECHANIC,
			PSSIFCanonicMetamodelCreator.N_ELECTRONIC,
			PSSIFCanonicMetamodelCreator.N_MODULE };

	private static final int depth = 1;

	/**
	 * these two variables store the weight with which the syntactic and
	 * semantic results of the sorrounding nodes add to the contextual
	 * similarity result
	 */
	private static final double snytacticWeight = 0.5;
	private static final double semanticWeight = 0.5;

	private List<MatchMethod> matchMethods;
	private ConsistencyData consistencyData;
	private Normalizer normalizer;

	private Model originalModel;
	private Model newModel;
	private Metamodel metaModelOriginal;
	private Metamodel metaModelNew;

	private ComparedNodePair tempNodePair;

	private Set<NodeAndType> sorroundingNodesOrigin;

	private Set<NodeAndType> sorroundingNodesNew;

	private NodeTypeBase conjunctionNodeType;

	public ContextMatcher(MatchingMethods matchMethod, boolean isActive,
			double weigth) {
		super(matchMethod, isActive, weigth);
	}

	/**
	 * @param matchMethods
	 *            the matchMethods to set
	 */
	public void setMatchMethods(List<MatchMethod> matchMethods) {
		this.matchMethods = matchMethods;
	}

	/**
	 * @param consistencyData
	 *            the consistencyData to set
	 */
	public void setConsistencyData(ConsistencyData consistencyData) {
		this.consistencyData = consistencyData;
	}

	/**
	 * @param normalizer
	 *            the normalizer to set
	 */
	public void setNormalizer(Normalizer normalizer) {
		this.normalizer = normalizer;
	}

	@Override
	public double executeMatching(Node tempNodeOrigin, Node tempNodeNew,
			Model originalModel, Model newModel, Metamodel metaModelOriginal,
			Metamodel metaModelNew, NodeType actTypeOriginModel,
			NodeType actTypeNewModel, String labelOrigin, String labelNew,
			List<Token> tokensOrigin, List<Token> tokensNew) {

		this.originalModel = originalModel;
		this.newModel = newModel;
		this.metaModelOriginal = metaModelOriginal;
		this.metaModelNew = metaModelNew;

		this.conjunctionNodeType = metaModelOriginal.getBaseNodeType(
				PSSIFCanonicMetamodelCreator.ENUM_CONJUNCTION).getOne();

		tempNodePair = null;

		sorroundingNodesOrigin = new HashSet<NodeAndType>();
		sorroundingNodesNew = new HashSet<NodeAndType>();

		/**
		 * retrieving all from/to nodes connected with the two nodes
		 */
		typeIteration(tempNodeOrigin, actTypeOriginModel,
				sorroundingNodesOrigin, true, true, true);
		typeIteration(tempNodeNew, actTypeNewModel, sorroundingNodesNew, true,
				true, false);

		return compareSorroundingNodes(tempNodeOrigin, actTypeOriginModel,
				tempNodeNew, actTypeNewModel, labelOrigin, labelNew);
	}

	/**
	 * this method iterates over every node type of the pssif metamodel and
	 * calls the method findSorroundingNodes() with the current type. This
	 * ensures that all nodes are compared with all from/to nodes of the edges.
	 * 
	 * It starts with the children of the Type DevelopmentArtifact and then
	 * continues with the children of the Type Solution Artifact. After that the
	 * Development and Solution Artifact Type and finally the Root Node Type are
	 * iterated. At the end the type of conjunctions is given to the method
	 * findSorroundingNodes(). Then the neighbours of the conjunction are added
	 * to the node list if the conjunction is a sorrounding node.
	 * 
	 * @param tempNode
	 *            the node from which sorrounding nodes shall be found
	 * @param sorroundingNodes
	 *            the list with the sorrounding nodes of the given node
	 * @param incoming
	 *            TODO
	 * @param outgoing
	 *            TODO
	 * @param isOriginalNode
	 *            TODO
	 */
	public void typeIteration(Node tempNode, NodeTypeBase typeTempNode,
			Set<NodeAndType> sorroundingNodes, boolean incoming,
			boolean outgoing, boolean isOriginalNode) {

		findSorroundingNodes(typeTempNode, tempNode, sorroundingNodes,
				incoming, outgoing, isOriginalNode);
	}

	private void findSorroundingNodes(NodeTypeBase type, Node tempNode,
			Set<NodeAndType> sorroundingNodes, boolean incoming,
			boolean outgoing, boolean isOriginalNode) {

		if (incoming) {
			if (isOriginalNode) {
				addIncomingNeighourhood(metaModelOriginal, type, tempNode,
						sorroundingNodes, isOriginalNode);
			} else {
				addIncomingNeighourhood(metaModelNew, type, tempNode,
						sorroundingNodes, isOriginalNode);
			}
		}
		if (outgoing) {
			if (isOriginalNode) {
				addOutgoingNeighourhood(metaModelOriginal, type, tempNode,
						sorroundingNodes, isOriginalNode);
			} else {
				addOutgoingNeighourhood(metaModelNew, type, tempNode,
						sorroundingNodes, isOriginalNode);
			}
		}
	}

	private List<NodeAndType> convertSetToLinkedList(Set<Node> setToConvert,
			NodeTypeBase actNodeType) {
		List<NodeAndType> result = new LinkedList<NodeAndType>();

		for (Node tempNode : setToConvert) {
			result.add(new NodeAndType(tempNode, actNodeType));
		}

		return result;
	}

	private void addIncomingNeighourhood(Metamodel metamodel,
			NodeTypeBase nodeType, Node nodeOfInterest,
			Set<NodeAndType> sorroundingNodes, boolean isOriginalNode) {

		for (EdgeType edgeType : metamodel.getEdgeTypes()) {
			for (ConnectionMapping incomingMapping : edgeType
					.getIncomingMappings(nodeType)) {
				for (Edge incomingEdge : incomingMapping
						.applyIncoming(nodeOfInterest)) {
					if (incomingEdge.apply(new ReadFromNodesOperation()) != null) {
						if (incomingMapping.getFrom() == conjunctionNodeType) {
							typeIteration(
									incomingEdge
											.apply(new ReadFromNodesOperation()),
									incomingMapping.getFrom(),
									sorroundingNodes, true, false,
									isOriginalNode);
						} else {
							sorroundingNodes.add(new NodeAndType(incomingEdge
									.apply(new ReadFromNodesOperation()),
									incomingMapping.getFrom()));
						}
					}
				}
			}
		}
	}

	private void addOutgoingNeighourhood(Metamodel metamodel,
			NodeTypeBase nodeType, Node nodeOfInterest,
			Set<NodeAndType> sorroundingNodes, boolean isOriginalNode) {

		for (EdgeType edgeType : metamodel.getEdgeTypes()) {
			for (ConnectionMapping outgoingMapping : edgeType
					.getOutgoingMappings(nodeType)) {
				for (Edge outgoingEdge : outgoingMapping
						.applyOutgoing(nodeOfInterest)) {

					if (outgoingMapping.getTo() == conjunctionNodeType) {
						typeIteration(
								outgoingEdge.apply(new ReadToNodesOperation()),
								outgoingMapping.getTo(), sorroundingNodes,
								false, true, isOriginalNode);
					} else {
						sorroundingNodes.add(new NodeAndType(outgoingEdge
								.apply(new ReadToNodesOperation()),
								outgoingMapping.getFrom()));
					}

				}
			}
		}
	}

	/**
	 * This method iterates over all found nodes of the two models and compares
	 * them based on the set matching methods. After every iteration the
	 * similarity value is added to the result.
	 * 
	 * @param tempNodeOrigin
	 *            the node from the original model
	 * @param actTypeOriginModel
	 * @param tempNodeNew
	 *            the node from the new model
	 * @param actTypeNewModel
	 * @param labelOrigin
	 * @param labelNew
	 * @return
	 */
	private double compareSorroundingNodes(Node tempNodeOrigin,
			NodeType actTypeOriginModel, Node tempNodeNew,
			NodeType actTypeNewModel, String labelOrigin, String labelNew) {
		double similaritySum = 0;
		double result = 0;

		System.out.println("Vergleich zwischen (original): "
				+ Methods.findName((NodeType) actTypeOriginModel,
						tempNodeOrigin) + " dem neuen Knoten: "
				+ Methods.findName((NodeType) actTypeNewModel, tempNodeNew));

		for (NodeAndType tempNodeSorroundingOrigin : sorroundingNodesOrigin) {
			for (NodeAndType tempNodeSorroundingNew : sorroundingNodesNew) {

				System.out.println("-------Kontext zwischen (original): "
						+ Methods.findName(
								(NodeType) tempNodeSorroundingOrigin.getType(),
								tempNodeSorroundingOrigin.getNode())
						+ " dem neuen Kontext: "
						+ Methods.findName(
								(NodeType) tempNodeSorroundingNew.getType(),
								tempNodeSorroundingNew.getNode()));

				if (nodesAlreadyCompared(tempNodeSorroundingOrigin.getNode(),
						tempNodeSorroundingNew.getNode())) {
					similaritySum += calculateWeightedSimilarities();
				} else {
					similaritySum += computeSimilarity(
							tempNodeSorroundingOrigin.getNode(),
							tempNodeSorroundingOrigin.getType(),
							tempNodeSorroundingNew.getNode(),
							tempNodeSorroundingNew.getType());
				}
			}
		}
		double denominator = Math.max(sorroundingNodesOrigin.size(),
				sorroundingNodesNew.size());

		if (denominator != 0) {
			result = (similaritySum / denominator);
		} else {
			return 0;
		}

		return result;
	}

	/**
	 * This method calculates the similarity of two nodes based on the given
	 * match methods. It work's similar as to the method in
	 * MatchingProcess.java.
	 * 
	 * @param tempNodeOrigin
	 *            the node from the original model
	 * @param actTypeOriginModel
	 *            the node type of the original node
	 * @param tempNodeNew
	 *            the node from the new model
	 * @param actTypeNewModel
	 *            the node type of the new node
	 * @return the similarity of two given nodes based on the chosen matching
	 *         methods
	 */
	private double computeSimilarity(Node tempNodeOrigin,
			NodeTypeBase actTypeOriginModelBase, Node tempNodeNew,
			NodeTypeBase actTypeNewModelBase) {

		double result = 0;
		double currentMetricResult = 0;

		NodeType actTypeOriginModel = (NodeType) actTypeOriginModelBase;
		NodeType actTypeNewModel = (NodeType) actTypeNewModelBase;

		/**
		 * here the strings of the old and the new node are read from the model
		 */
		String labelOriginNode = Methods.findName(actTypeOriginModel,
				tempNodeOrigin);
		String labelNewNode = Methods.findName(actTypeNewModel, tempNodeNew);

		String labelOriginNodeNormalized, labelNewNodeNormalized;

		/**
		 * variables to store the different normalized tokens
		 */
		List<Token> tokensOriginNodeNormalized = normalizer
				.createNormalizedTokensFromLabel(labelOriginNode, true, true,
						true, true);
		List<Token> tokensNewNodeNormalized = normalizer
				.createNormalizedTokensFromLabel(labelNewNode, true, true,
						true, true);
		List<Token> tokensOriginNodeNormalizedCompundedUnstemmed = normalizer
				.createNormalizedTokensFromLabel(labelOriginNode, true, true,
						false, false);
		List<Token> tokensNewNodeNormalizedCompundedUnstemmed = normalizer
				.createNormalizedTokensFromLabel(labelNewNode, true, true,
						false, false);

		labelOriginNodeNormalized = normalizer.normalizeLabel(labelOriginNode);

		labelNewNodeNormalized = normalizer.normalizeLabel(labelNewNode);

		Iterator<MatchMethod> currentMatchMethod = matchMethods.iterator();

		/**
		 * Here the tokenized forms are created if necessary. Thereby it's
		 * important to note that the syntactic metrics require a normalization
		 * without compund splitting and without stemming. Whereas the semantic
		 * metrics require full normalization.
		 * 
		 * Then every active match Method is applied to the two nodes here.
		 */
		while (currentMatchMethod.hasNext()) {
			MatchMethod currentMethod = currentMatchMethod.next();
			currentMetricResult = 0;

			/**
			 * Depending on the current match method different token sets of the
			 * labels have to be given to the match method. If the method is
			 * StringEditDistance or Hyphen we don't use token normalization
			 * with compoundsplitting or stemming. Thatfore we have to separate
			 * these two cases.
			 */
			if (currentMethod.isActive()) {
				if (currentMethod.getMatchMethod() == MatchingMethods.CONTEXT_MATCHING) {
					continue;
				}
				if ((currentMethod.getMatchMethod() == MatchingMethods.STRING_EDIT_DISTANCE_MATCHING)
						|| (currentMethod.getMatchMethod() == MatchingMethods.HYPHEN_MATCHING)) {

					currentMetricResult = currentMethod.executeMatching(
							tempNodeOrigin, tempNodeNew, originalModel,
							newModel, metaModelOriginal, metaModelNew,
							actTypeOriginModel, actTypeNewModel,
							labelOriginNodeNormalized, labelNewNodeNormalized,
							tokensOriginNodeNormalizedCompundedUnstemmed,
							tokensNewNodeNormalizedCompundedUnstemmed);
				} else {

					currentMetricResult = currentMethod
							.executeMatching(tempNodeOrigin, tempNodeNew,
									originalModel, newModel, metaModelOriginal,
									metaModelNew, actTypeOriginModel,
									actTypeNewModel, labelOriginNodeNormalized,
									labelNewNodeNormalized,
									tokensOriginNodeNormalized,
									tokensNewNodeNormalized);
				}

				switch (currentMethod.getMatchMethod()) {
				case EXACT_STRING_MATCHING:
				case DEPTH_MATCHING:
				case STRING_EDIT_DISTANCE_MATCHING:
				case HYPHEN_MATCHING:
					result += ((currentMetricResult * currentMethod.getWeigth()) * snytacticWeight);
					break;
				case LINGUISTIC_MATCHING:
				case VECTOR_SPACE_MODEL_MATCHING:
				case LATENT_SEMANTIC_INDEXING_MATCHING:
				case ATTRIBUTE_MATCHING:
					result += ((currentMetricResult * currentMethod.getWeigth()) * semanticWeight);
					break;
					default:
						break;
				}
			}
		}

		return result;
	}

	/**
	 * @return the result of similarity analysis from past matches if two nodes
	 *         have already been compared.
	 */
	private double calculateWeightedSimilarities() {
		double result = 0;

		if (normalizer.isSyntacticMetricActive()
				&& normalizer.isSemanticMetricActive()) {
			result += (getWeightedSyntacticSimilarity() * snytacticWeight);
			result += (getWeightedSemanticSimilarity() * semanticWeight);
		} else {
			if (normalizer.isSyntacticMetricActive()) {
				result += getWeightedSyntacticSimilarity();
			} else {
				result += getWeightedSemanticSimilarity();
			}
		}

		return result;
	}

	/**
	 * This method looks up whether two nodes have already been compared. If yes
	 * the result is stored in the variable tempNodePair. It can then be used by
	 * the context matcher to look up results from past compairsons.
	 * 
	 * @param tempNodeOrigin
	 * @param tempNodeNew
	 * @return
	 */
	private boolean nodesAlreadyCompared(Node tempNodeOrigin, Node tempNodeNew) {

		this.tempNodePair = consistencyData.getNodeMatch(tempNodeOrigin,
				tempNodeNew);

		return (this.tempNodePair != null);
	}

	/**
	 * @return The weighted combination of all results of the syntactic match
	 *         methods.
	 */
	public double getWeightedSyntacticSimilarity() {
		double result = 0;

		double exactMatch = tempNodePair.getLabelComparison()
				.getExactMatchResult();
		double depthMatch = tempNodePair.getDepthMatchResult();
		double stringEditDistanceMatch = tempNodePair.getTokensComparison()
				.getStringEditDistanceResult();
		double hyphenMatch = tempNodePair.getTokensComparison()
				.getHyphenMatchResult();

		Iterator<MatchMethod> currentMatchMethod = matchMethods.iterator();

		while (currentMatchMethod.hasNext()) {
			MatchMethod currentMethod = currentMatchMethod.next();

			switch (currentMethod.getMatchMethod()) {
			case EXACT_STRING_MATCHING:
				result += currentMethod.getWeigth() * exactMatch;
				break;
			case DEPTH_MATCHING:
				result += currentMethod.getWeigth() * depthMatch;
				break;
			case STRING_EDIT_DISTANCE_MATCHING:
				result += currentMethod.getWeigth() * stringEditDistanceMatch;
				break;
			case HYPHEN_MATCHING:
				result += currentMethod.getWeigth() * hyphenMatch;
				break;
			default:
				;
			}
		}
		return result;
	}

	/**
	 * @return The weighted combination of all results of the semantic match
	 *         methods.
	 */
	public double getWeightedSemanticSimilarity() {
		double result = 0;

		double linguisticMatch = tempNodePair.getTokensComparison()
				.getLinguisticMatchResult();
		double vsmMatch = tempNodePair.getTokensComparison()
				.getVsmMatchResult();
		double lsiMatch = tempNodePair.getTokensComparison()
				.getLsiMatchResult();
		double attributeMatch = tempNodePair.getAttributeMatchResult();

		Iterator<MatchMethod> currentMatchMethod = matchMethods.iterator();

		while (currentMatchMethod.hasNext()) {
			MatchMethod currentMethod = currentMatchMethod.next();

			switch (currentMethod.getMatchMethod()) {
			case LINGUISTIC_MATCHING:
				result += currentMethod.getWeigth() * linguisticMatch;
				break;
			case VECTOR_SPACE_MODEL_MATCHING:
				result += currentMethod.getWeigth() * vsmMatch;
				break;
			case LATENT_SEMANTIC_INDEXING_MATCHING:
				result += currentMethod.getWeigth() * lsiMatch;
				break;
			case ATTRIBUTE_MATCHING:
				result += currentMethod.getWeigth() * attributeMatch;
				break;
			default:
				;
			}
		}

		return result;
	}
}
