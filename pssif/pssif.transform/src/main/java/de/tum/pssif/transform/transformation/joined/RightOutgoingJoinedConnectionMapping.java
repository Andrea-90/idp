package de.tum.pssif.transform.transformation.joined;

import java.util.Collection;

import com.google.common.collect.Sets;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.exception.PSSIFStructuralIntegrityException;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.transform.transformation.viewed.ViewedConnectionMapping;


public class RightOutgoingJoinedConnectionMapping extends ViewedConnectionMapping {
  private final ConnectionMapping joinedMapping;

  public RightOutgoingJoinedConnectionMapping(ConnectionMapping baseMapping, EdgeType type, NodeType from, NodeType via, NodeType to,
      ConnectionMapping joinedMapping) {
    super(baseMapping, type, from, via);
    this.joinedMapping = joinedMapping;
  }

  @Override
  public Edge create(Model model, Node from, Node to) {
    //we expect exactly one node being connected to from via the joined mapping, otherwise we need to return PSSIFOption<Edge> with possibly none or many
    Node actualToNode = joinedMapping.applyTo(joinedMapping.applyOutgoing(to, true).getOne());
    return getBaseMapping().create(model, from, actualToNode);
  }

  @Override
  public PSSIFOption<Edge> apply(Model model, boolean includeSubtypes) {
    Collection<Edge> result = Sets.newHashSet();
    for (Edge e : getBaseMapping().apply(model, includeSubtypes).getMany()) {
      Node from = getBaseMapping().applyFrom(e);
      Node to = getBaseMapping().applyTo(e);
      PSSIFOption<Edge> joined = joinedMapping.applyIncoming(to, includeSubtypes);
      if (joined.isOne()) {
        Node inner = joinedMapping.applyFrom(joined.getOne());
        if (getTo().apply(model, true).getMany().contains(inner)) {
          result.add(new UnjoinedEdge(e, from, inner));
        }
      }
      else {
        throw new PSSIFStructuralIntegrityException("ambiguous edges");
      }
    }
    return PSSIFOption.many(result);
  }

  @Override
  public Node applyTo(Edge edge) {
    for (Edge candidate : getBaseMapping().apply(edge.getModel(), true).getMany()) {
      if (candidate.getId().equals(edge.getId())) {
        Node to = getBaseMapping().applyTo(candidate);
        PSSIFOption<Edge> joined = joinedMapping.applyIncoming(to, true);
        if (joined.isOne()) {
          return joinedMapping.applyFrom(joined.getOne());
        }
        else {
          throw new PSSIFStructuralIntegrityException("ambiguous edges");
        }
      }
    }
    throw new PSSIFStructuralIntegrityException("edge not found");
  }

  @Override
  public PSSIFOption<Edge> applyIncoming(Node node, boolean includeSubtypes) {
    PSSIFOption<Edge> joined = joinedMapping.applyOutgoing(node, includeSubtypes);
    if (joined.isOne()) {
      return getBaseMapping().applyIncoming(joinedMapping.applyTo(joined.getOne()), includeSubtypes);
    }
    else {
      throw new PSSIFStructuralIntegrityException("ambiguous edges");
    }
  }
}
