package org.pssif.matchingLogic;

import java.util.List;

import org.pssif.consistencyDataStructures.Token;

import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;

/**
 * @author Andreas
 * 
 *         This class represents a simple matching method for two labels. If the
 *         two labels are equal we get 1 as a result. Else 0.
 * 
 */
public class ExactMatcher extends MatchMethod {

	public ExactMatcher(MatchingMethods matchMethod, boolean isActive, double weigth) {
		super(matchMethod, isActive, weigth);
	}

	@Override
	public double executeMatching(Node tempNodeOrigin, Node tempNodeNew,
			Model originalModel, Model newModel, Metamodel metaModelOriginal,
			Metamodel metaModelNew, NodeType actTypeOriginModel,
			NodeType actTypeNewModel, String labelOrigin, String labelNew,
			List<Token> tokensOrigin, List<Token> tokensNew) {
		double result = 0;

		if (labelOrigin.equals(labelNew)) {
			result = 1;
		}

		return result;
	}

}
