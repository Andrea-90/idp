package org.pssif.matchingLogic;

import java.util.List;

import org.pssif.consistencyDataStructures.Token;

import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;

public class VsmMatcher extends MatchMethod {

	public VsmMatcher(MatchingMethods matchMethod, boolean isActive,
			double weigth) {
		super(matchMethod, isActive, weigth);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double executeMatching(Node tempNodeOrigin, Node tempNodeNew,
			Model originalModel, Model newModel, Metamodel metaModel,
			NodeType actTypeOriginModel, NodeType actTypeNewModel,
			String labelOrigin, String labelNew, List<Token> tokensOrigin,
			List<Token> tokensNew) {
		// TODO Auto-generated method stub
		return 0;
	}

}
