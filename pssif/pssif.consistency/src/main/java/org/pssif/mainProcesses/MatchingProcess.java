package org.pssif.mainProcesses;

import java.util.Iterator;
import java.util.List;

import org.pssif.comparedDataStructures.ComparedLabelPair;
import org.pssif.comparedDataStructures.ComparedNodePair;
import org.pssif.comparedDataStructures.ComparedNormalizedTokensPair;
import org.pssif.consistencyDataStructures.ConsistencyData;
import org.pssif.consistencyDataStructures.Token;
import org.pssif.matchingLogic.MatchMethod;
import org.pssif.textMining.Tokenizer;

import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.common.PSSIFValue;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;

/**
 * @author Andreas This class is responsible for conducting the whole matching
 *         process. It takes two nodes and applies all active matching methods
 *         to them. Afterwards it saves the idpair of the two nodes so they
 *         won't be compared again.
 */
public class MatchingProcess {

	/**
	 * @param originalModel
	 *            the model which was first imported
	 * @param newModel
	 *            the model which was recently imported
	 * @param metaModel
	 *            the metamodel according to the two models
	 * @param consistencyData
	 *            the data of the matching process will be stored here and can
	 *            be accesed both by the Comparsion and the matching process
	 * @param matchMethods
	 *            these are the matching metrics for the current matching
	 *            operation
	 */
	public MatchingProcess(Model originalModel, Model newModel,
			Metamodel metaModel, ConsistencyData consistencyData,
			List<MatchMethod> matchMethods) {

		this.originalModel = originalModel;
		this.newModel = newModel;
		this.metaModel = metaModel;
		this.consistencyData = consistencyData;
		this.matchMethods = matchMethods;

		checkMatchMethods();
	}

	private Model originalModel, newModel;
	private Metamodel metaModel;

	private ConsistencyData consistencyData;

	private List<MatchMethod> matchMethods;

	/**
	 * this bool says whether we need to remove the whitespace from the labels
	 * of the nodes for exact matching
	 */
	private boolean whiteSpaceRemovalRequired;

	/**
	 * this bool says whether we have to tokenize the labels from the nodes for
	 * some metrics
	 */
	private boolean tokenizationRequired;

	private ComparedLabelPair comparedLabelPair = null;
	private ComparedNormalizedTokensPair comparedNormalizedTokensPair = null;
	private ComparedNodePair comparedNodePair = null;

	/**
	 * This method checks which matching Methods are active and then saves if we
	 * have to remove the whitespace from the labels and/or if we have to
	 * tokenize the labels
	 */
	private void checkMatchMethods() {
		Iterator<MatchMethod> currentMatchMethod = matchMethods.iterator();

		while (currentMatchMethod.hasNext()) {
			MatchMethod currentMethod = currentMatchMethod.next();
			if (currentMethod.isActive()) {

				switch (currentMethod.getMatchMethod()) {
				case EXACT_STRING_MATCHING:
					whiteSpaceRemovalRequired = true;
				case DEPTH_MATCHING:
					tokenizationRequired = true;
				case STRING_EDIT_DISTANCE_MATCHING:
					tokenizationRequired = true;
				case HYPHEN_MATCHING:
					tokenizationRequired = true;
				case LINGUISTIC_MATCHING:
					tokenizationRequired = true;
				case VECTOR_SPACE_MODEL_MATCHING:
					tokenizationRequired = true;
				case LATENT_SEMANTIC_INDEXING_MATCHING:
					tokenizationRequired = true;
				default:
					;
				}
			}
		}
	}

	/**
	 * @param tempNodeOrigin
	 *            the node from the original model
	 * @param tempNodeNew
	 *            the node from the new model
	 * @param actTypeOriginModel
	 *            the type of the tempNodeOrigin
	 * @param actTypeNewModel
	 *            the type of the tempNodeNew
	 * @throws RuntimeException
	 *             If something at the saving goes wrong an exception is thrown.
	 *             Else nothing besides the saving happens.
	 * 
	 *             This method guides the whole matching process. It initializes
	 *             the variables where the consistencyData will be stored later.
	 *             Then it normalizes and/or tokenizes the labels if necessary.
	 *             Then it applies the active matching methods to the nodes and
	 *             saves the results
	 */
	public void startMatchingProcess(Node tempNodeOrigin, Node tempNodeNew,
			NodeType actTypeOriginModel, NodeType actTypeNewModel) {

		double currentMetricResult;

		/**
		 * initializing the consistency Data variables here
		 */
		comparedLabelPair = null;
		comparedNormalizedTokensPair = null;
		comparedNodePair = new ComparedNodePair();

		/**
		 * here the strings of the old and the new node are read from the model.
		 */
		String labelOrigin = findName(actTypeOriginModel, tempNodeOrigin);
		String labelNew = findName(actTypeNewModel, tempNodeNew);

		Token[] tokensOrigin = null;
		Token[] tokensNew = null;

		createComparedNormalizedTokensPair(tokensOrigin, tokensNew);

		if (tokenizationRequired) {
			tokensOrigin = Tokenizer.tokenize(labelOrigin);
			tokensNew = Tokenizer.tokenize(labelNew);

			createComparedNormalizedTokensPair(tokensOrigin, tokensNew);
		}

		/**
		 * Here the whitespace of the two labels is removed if necessary and
		 * they are converted to lowercase
		 * 
		 */
		if (whiteSpaceRemovalRequired) {
			labelOrigin = labelOrigin.replaceAll("\\s+", "").toLowerCase();
			labelNew = labelNew.replaceAll("\\s+", "").toLowerCase();
		}

		Iterator<MatchMethod> currentMatchMethod = matchMethods.iterator();

		/**
		 * Applying every match Method to the two nodes here
		 */
		while (currentMatchMethod.hasNext()) {
			MatchMethod currentMethod = currentMatchMethod.next();

			if (currentMethod.isActive()) {
				currentMetricResult = currentMethod.executeMatching(
						tempNodeOrigin, tempNodeNew, originalModel, newModel,
						metaModel, actTypeOriginModel, actTypeNewModel,
						labelOrigin, labelNew, tokensOrigin, tokensNew);

				saveMatchMethodResult(currentMethod, currentMetricResult,
						labelOrigin, labelNew);
			}
		}

		comparedNodePair.setLabelComparison(comparedLabelPair);
		comparedNodePair.setTokensComparison(comparedNormalizedTokensPair);

		/**
		 * Saves the two compared nodes, the ids and the matching results in the
		 * consistencyData object. If something goes wrong an exception is
		 * thrown. Else nothing besides the saving happens.
		 */
		if (!(consistencyData.putComparedEntry(tempNodeOrigin, tempNodeNew,
				comparedNodePair))) {
			throw new RuntimeException(
					"Something went wrong with the saving of the recently matched elements.");
		}

		System.out.println("The node(origin): " + labelOrigin
				+ " and the node(new) " + labelNew
				+ " have the following similarieties:");
		System.out.println("Syntactic similarity: "
				+ getWeightedSyntacticSimilarity());
		System.out.println("Semantic similarity: "
				+ getWeightedSemanticSimilarity());
		System.out.println("Contextual Similarity: "
				+ getWeightedContextSimilarity());

	}

	/**
	 * @param currentMethod
	 *            the matchMethod which was currently applied
	 * @param currentMetricResult
	 *            the result of the currently applied matchMethod
	 * @param labelOrigin
	 * @param labelNew
	 * 
	 *            This method is supposed to save the results of the last match
	 *            operation. Depending on the matchMethod type it's saved in a
	 *            different matchData container.
	 */
	public void saveMatchMethodResult(MatchMethod currentMethod,
			double currentMetricResult, String labelOrigin, String labelNew) {

		switch (currentMethod.getMatchMethod()) {
		case EXACT_STRING_MATCHING:
			if (comparedLabelPair == null) {
				comparedLabelPair = new ComparedLabelPair(labelOrigin,
						labelNew, currentMetricResult);
			}
		case DEPTH_MATCHING:
			comparedNodePair.setDepthMatchResult(currentMetricResult);
		case STRING_EDIT_DISTANCE_MATCHING:
			comparedNormalizedTokensPair
					.setStringEditDistanceResult(currentMetricResult);
		case HYPHEN_MATCHING:
			comparedNormalizedTokensPair
					.setHyphenMatchResult(currentMetricResult);
		case LINGUISTIC_MATCHING:
			comparedNormalizedTokensPair
					.setLinguisticMatchResult(currentMetricResult);
		case VECTOR_SPACE_MODEL_MATCHING:
			comparedNormalizedTokensPair.setVsmMatchResult(currentMetricResult);
		case LATENT_SEMANTIC_INDEXING_MATCHING:
			comparedNormalizedTokensPair.setLsiMatchResult(currentMetricResult);
		case CONTEXT_MATCHING:
			comparedNodePair.setContextMatchResult(currentMetricResult);
		default:
			;
		}
	}

	/**
	 * @param tokensOrigin
	 * @param tokensNew
	 * 
	 *            This method creates an object for the field
	 *            "comparedNormalizedTokensPair" if it's not yet created. This
	 *            ensures that every different token dependent metric is saved
	 *            into the same matchData container.
	 */
	private void createComparedNormalizedTokensPair(Token[] tokensOrigin,
			Token[] tokensNew) {
		if (comparedNormalizedTokensPair == null) {
			comparedNormalizedTokensPair = new ComparedNormalizedTokensPair(
					tokensOrigin, tokensNew);
		}
	}

	/**
	 * @return The weighted combination of all results of the syntactic match
	 *         methods.
	 */
	public double getWeightedSyntacticSimilarity() {
		double result = 0;

		double exactMatch = comparedLabelPair.getExactMatchResult();
		double depthMatch = comparedNodePair.getDepthMatchResult();
		double stringEditDistanceMatch = comparedNormalizedTokensPair
				.getStringEditDistanceResult();
		double hyphenMatch = comparedNormalizedTokensPair
				.getHyphenMatchResult();

		Iterator<MatchMethod> currentMatchMethod = matchMethods.iterator();

		while (currentMatchMethod.hasNext()) {
			MatchMethod currentMethod = currentMatchMethod.next();

			switch (currentMethod.getMatchMethod()) {
			case EXACT_STRING_MATCHING:
				result += currentMethod.getWeigth() * exactMatch;
			case DEPTH_MATCHING:
				result += currentMethod.getWeigth() * depthMatch;
			case STRING_EDIT_DISTANCE_MATCHING:
				result += currentMethod.getWeigth() * stringEditDistanceMatch;
			case HYPHEN_MATCHING:
				result += currentMethod.getWeigth() * hyphenMatch;
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

		double linguisticMatch = comparedNormalizedTokensPair
				.getLinguisticMatchResult();
		double vsmMatch = comparedNormalizedTokensPair.getVsmMatchResult();
		double lsiMatch = comparedNormalizedTokensPair.getLsiMatchResult();

		Iterator<MatchMethod> currentMatchMethod = matchMethods.iterator();

		while (currentMatchMethod.hasNext()) {
			MatchMethod currentMethod = currentMatchMethod.next();

			switch (currentMethod.getMatchMethod()) {
			case LINGUISTIC_MATCHING:
				result += currentMethod.getWeigth() * linguisticMatch;
			case VECTOR_SPACE_MODEL_MATCHING:
				result += currentMethod.getWeigth() * vsmMatch;
			case LATENT_SEMANTIC_INDEXING_MATCHING:
				result += currentMethod.getWeigth() * lsiMatch;
			default:
				;
			}
		}

		return result;
	}

	/**
	 * @return The weighted combination of all results of the context match
	 *         methods.
	 */
	public double getWeightedContextSimilarity() {
		double result = 0;

		Iterator<MatchMethod> currentMatchMethod = matchMethods.iterator();

		while (currentMatchMethod.hasNext()) {
			MatchMethod currentMethod = currentMatchMethod.next();

			switch (currentMethod.getMatchMethod()) {
			case CONTEXT_MATCHING:
				result += currentMethod.getWeigth()
						* comparedNodePair.getContextMatchResult();
			default:
				;
			}
		}

		return result;
	}

	/**
	 * Get the name from the Node object
	 * 
	 * @return the actual name or "Name not available" if the name was not
	 *         defined
	 * @author Luc
	 */
	private String findName(NodeType actType, Node actNode) {
		String name = "Name not available";
		// find the name of the Node
		PSSIFOption<Attribute> tmp = actType
				.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME);
		if (tmp.isOne()) {
			Attribute nodeName = tmp.getOne();

			if (nodeName.get(actNode) != null) {
				PSSIFValue value = null;
				if (nodeName.get(actNode).isOne()) {
					value = nodeName.get(actNode).getOne();
					name = value.asString();
				}
				if (nodeName.get(actNode).isNone()) {
					name = "Name not available";
				}
			} else
				name = "Name not available";
		}

		return name;
	}
}