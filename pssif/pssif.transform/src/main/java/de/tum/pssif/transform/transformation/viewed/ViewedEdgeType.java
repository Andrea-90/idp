package de.tum.pssif.transform.transformation.viewed;

import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.AttributeCategory;
import de.tum.pssif.core.metamodel.AttributeGroup;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.DataType;
import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Multiplicity;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.Unit;
import de.tum.pssif.core.metamodel.impl.base.AbstractEdgeType;


public class ViewedEdgeType extends AbstractEdgeType {
  private final EdgeType baseType;

  public ViewedEdgeType(EdgeType baseType) {
    this(baseType, baseType.getName());
  }

  public ViewedEdgeType(EdgeType baseType, String name) {
    super(name);
    this.baseType = baseType;

    for (AttributeGroup g : baseType.getAttributeGroups()) {
      addAttributeGroup(new ViewedAttributeGroup(g, g.getName(), this));
    }
  }

  public void addMapping(ConnectionMapping mapping) {
    addMappingInternal(mapping);
  }

  @Override
  public ConnectionMapping createMapping(String inName, NodeType in, Multiplicity inMultiplicity, String outName, NodeType out,
                                         Multiplicity outMultiplicity) {
    //FIXME separate mutable edgetype interface from interface edgetype
    return null;
  }

  @Override
  public EdgeEnd createAuxiliary(String name, Multiplicity multiplicity, NodeType to) {
    //FIXME separate mutable edgetype interface from interface edgetype
    return null;
  }

  @Override
  public AttributeGroup createAttributeGroup(String name) {
    //FIXME separate mutable edgetype interface from interface edgetype
    return null;
  }

  @Override
  public void removeAttributeGroup(AttributeGroup attributeGroup) {
    //FIXME separate mutable edgetype interface from interface edgetype

  }

  @Override
  public Attribute createAttribute(AttributeGroup group, String name, DataType dataType, boolean visible, AttributeCategory category) {
    //FIXME separate mutable edgetype interface from interface edgetype
    return null;
  }

  @Override
  public Attribute createAttribute(AttributeGroup group, String name, DataType dataType, Unit unit, boolean visible, AttributeCategory category) {
    //FIXME separate mutable edgetype interface from interface edgetype
    return null;
  }

}
