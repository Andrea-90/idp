package de.tum.pssif.transform.transformation;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.JunctionNodeType;
import de.tum.pssif.core.metamodel.NodeTypeBase;
import de.tum.pssif.core.metamodel.mutable.MutableEdgeType;
import de.tum.pssif.core.metamodel.mutable.MutableJunctionNodeType;
import de.tum.pssif.transform.transformation.viewed.ViewedConnectionMapping;
import de.tum.pssif.transform.transformation.viewed.ViewedJunctionNodeType;


public class AliasJunctionNodeTypeTransformation extends RenameTransformation<JunctionNodeType> {

  public AliasJunctionNodeTypeTransformation(JunctionNodeType target, String name) {
    super(target, name);
  }

  @Override
  public void apply(View view) {
    MutableJunctionNodeType actualTarget = view.getMutableJunctionNodeType(getTarget().getName()).getOne();
    ViewedJunctionNodeType aliased = new ViewedJunctionNodeType(actualTarget, getName());
    view.add(aliased);

    for (MutableEdgeType met : view.getMutableEdgeTypes()) {
      for (ConnectionMapping cm : met.getMappings().getMany()) {
        NodeTypeBase from = cm.getFrom();
        NodeTypeBase to = cm.getTo();
        boolean create = false;
        if (from.equals(actualTarget)) {
          from = aliased;
          create = true;
        }
        if (to.equals(actualTarget)) {
          to = aliased;
          create = true;
        }
        if (create) {
          met.addMapping(new ViewedConnectionMapping(cm, met, from, to));
        }
      }
    }
  }

}
