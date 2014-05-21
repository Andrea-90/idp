package org.pssif.comparedDataStructures;

import de.tum.pssif.core.model.Node;


/**
 * @author Andreas
 *
 */
public class ComparedNodePair extends Compared {

	private Node nodeOriginalModel, nodeNewModel;
	
	private ComparedLabelPair labelComparison;
	private ComparedNormalizedTokensPair tokensComparison;
	
	/**
	 * the result of the matching between the surrounding elements between two elements
	 */
	private double contextMatchResult;
	
	/**
	 * the result of the matching between two elements based on their depth in the modelgraph
	 */
	private double depthMatchResult;
}
