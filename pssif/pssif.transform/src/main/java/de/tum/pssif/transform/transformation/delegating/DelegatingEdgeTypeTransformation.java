package de.tum.pssif.transform.transformation.delegating;

import java.util.Collection;

import com.google.common.collect.Sets;

import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeTypeBase;
import de.tum.pssif.transform.transformation.AbstractTransformation;
import de.tum.pssif.transform.transformation.View;


public class DelegatingEdgeTypeTransformation extends AbstractTransformation {
  private final String                                    name;
  private Collection<ConnectionMappingDelegateDescriptor> descriptors = Sets.newHashSet();

  public DelegatingEdgeTypeTransformation(String name) {
    this.name = name;
  }

  @Override
  public void apply(View view) {
    DelegatingEdgeType delegating = new DelegatingEdgeType(name);
    for (ConnectionMappingDelegateDescriptor descriptor : descriptors) {
      EdgeType baseType = view.getEdgeType(descriptor.getBaseMapping().getType().getName()).getOne();
      NodeTypeBase from = view.getBaseNodeType(descriptor.getFrom().getName()).getOne();
      NodeTypeBase to = view.getBaseNodeType(descriptor.getTo().getName()).getOne();
      ConnectionMapping baseMapping = baseType.getMapping(from, to).getOne();
      delegating.delegate(baseMapping, from, to);
    }
    //FIXME how to handle attributes of the delegate types??
    delegating.inherit(view.getEdgeType(PSSIFConstants.ROOT_EDGE_TYPE_NAME).getOne());
    view.add(delegating);
  }

  public void delegate(NodeTypeBase from, NodeTypeBase to, ConnectionMapping baseMapping) {
    descriptors.add(new ConnectionMappingDelegateDescriptor(from, to, baseMapping));
  }
}
