package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;
import java.util.Collections;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;

public class NodeTypeImpl extends NamedImpl implements NodeType {
	private NodeType general;
	private Collection<NodeType> specials = Sets.newHashSet();

	private Collection<EdgeType> incomings = Sets.newHashSet();
	private Collection<EdgeType> outgoings = Sets.newHashSet();
	private Collection<EdgeType> auxiliaries = Sets.newHashSet();

	public NodeTypeImpl(String name) {
		super(name);
	}

	@Override
	public Collection<EdgeType> getEdgeTypes() {
		Collection<EdgeType> result = Sets.newHashSet();

		result.addAll(getIncomings());
		result.addAll(getOutgoings());
		result.addAll(getAuxiliaries());

		return Collections.unmodifiableCollection(result);
	}

	@Override
	public Collection<EdgeType> getIncomings() {
		return Collections.unmodifiableCollection(incomings);
	}

	@Override
	public Collection<EdgeType> getOutgoings() {
		return Collections.unmodifiableCollection(outgoings);
	}

	@Override
	public Collection<EdgeType> getAuxiliaries() {
		return Collections.unmodifiableCollection(auxiliaries);
	}

	@Override
	public EdgeType findEdgeType(String name) {
		for (EdgeType element : getEdgeTypes()) {
			if (element.getName().equals(name)) {
				return element;
			}
		}
		return null;
	}

	@Override
	public Node create(Model model) {
		return model.createNode(this);
	}

	@Override
	public NodeType getGeneral() {
		return general;
	}

	@Override
	public Collection<NodeType> getSpecials() {
		return Collections.unmodifiableCollection(specials);
	}

	@Override
	public void inherit(NodeType general) {
		general.registerSpecialization(this);
	}

	@Override
	public void registerSpecialization(NodeTypeImpl special) {
		specials.add(special);
		special.registerGeneralization(this);
	}

	@Override
	public void registerGeneralization(NodeTypeImpl general) {
		this.general = general;
	}

	@Override
	public void registerIncoming(EdgeType edge) {
		incomings.add(edge);
	}

	@Override
	public void registerOutgoing(EdgeType edge) {
		outgoings.add(edge);
	}

	@Override
	public void registerAuxiliary(EdgeType edge) {
		auxiliaries.add(edge);
	}

	@Override
	public Collection<Node> apply(Model model) {
		Collection<Node> result = model.findAll(this);

		for (NodeType special : getSpecials()) {
			result.addAll(special.apply(model));
		}

		return Collections.unmodifiableCollection(result);
	}

}
