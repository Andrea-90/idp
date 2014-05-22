package org.pssif.comparedDataStructures;

import de.tum.pssif.core.model.Node;

/**
 * @author Andreas
 * 
 *         this class stores two nodes. one from the first imported model and
 *         the other one from the recent imported model. In addition to this
 *         this class stores the match result of the label compairson of the two
 *         nodes, the tokenization of the labels (plus the according metric
 *         results) and the result of two node metrics (depth matching and
 *         contextual similarity)
 * 
 */
public class ComparedNodePair extends Compared {

	private Node nodeOriginalModel, nodeNewModel;

	private ComparedLabelPair labelComparison;
	private ComparedNormalizedTokensPair tokensComparison;

	/**
	 * the result of the matching between two elements based on their the
	 * surrounding elements
	 */
	private double contextMatchResult;

	/**
	 * the result of the matching between two elements based on their depth in
	 * the modelgraph
	 */
	private double depthMatchResult;

	/**
	 * @return the contextMatchResult
	 */
	public double getContextMatchResult() {
		return contextMatchResult;
	}

	/**
	 * @return the depthMatchResult
	 */
	public double getDepthMatchResult() {
		return depthMatchResult;
	}

	/**
	 * @return the labelComparison
	 */
	public ComparedLabelPair getLabelComparison() {
		return labelComparison;
	}

	/**
	 * @return the tokensComparison
	 */
	public ComparedNormalizedTokensPair getTokensComparison() {
		return tokensComparison;
	}

	/**
	 * @param nodeOriginalModel the nodeOriginalModel to set
	 */
	public void setNodeOriginalModel(Node nodeOriginalModel) {
		this.nodeOriginalModel = nodeOriginalModel;
	}

	/**
	 * @param nodeNewModel the nodeNewModel to set
	 */
	public void setNodeNewModel(Node nodeNewModel) {
		this.nodeNewModel = nodeNewModel;
	}

	/**
	 * @param labelComparison the labelComparison to set
	 */
	public void setLabelComparison(ComparedLabelPair labelComparison) {
		this.labelComparison = labelComparison;
	}

	/**
	 * @param tokensComparison the tokensComparison to set
	 */
	public void setTokensComparison(ComparedNormalizedTokensPair tokensComparison) {
		this.tokensComparison = tokensComparison;
	}

	/**
	 * @param contextMatchResult the contextMatchResult to set
	 */
	public void setContextMatchResult(double contextMatchResult) {
		this.contextMatchResult = contextMatchResult;
	}

	/**
	 * @param depthMatchResult the depthMatchResult to set
	 */
	public void setDepthMatchResult(double depthMatchResult) {
		this.depthMatchResult = depthMatchResult;
	}
}
