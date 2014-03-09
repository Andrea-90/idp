package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.exception.PSSIFStructuralIntegrityException;
import de.tum.pssif.core.metamodel.AttributeGroup;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.ElementType;
import de.tum.pssif.core.metamodel.JunctionNodeType;
import de.tum.pssif.core.metamodel.NodeTypeBase;
import de.tum.pssif.core.metamodel.mutable.MutableConnectionMapping;
import de.tum.pssif.core.metamodel.mutable.MutableEdgeType;


public class EdgeTypeImpl extends ElementTypeImpl implements MutableEdgeType {
  private Collection<MutableConnectionMapping> mappings        = Sets.newHashSet();
  private EdgeType                             general         = null;
  private final Set<EdgeType>                  specializations = Sets.newHashSet();

  public EdgeTypeImpl(String name) {
    super(name);
    addAttributeGroup(new InheritingAttributeGroup<EdgeType>(PSSIFConstants.DEFAULT_ATTRIBUTE_GROUP_NAME, this));
  }

  @Override
  public AttributeGroup createAttributeGroup(String name) {
    if (!getAttributeGroup(name).isNone()) {
      throw new PSSIFStructuralIntegrityException("group with name " + name + " already exists in type " + getName());
    }
    MutableAttributeGroup result = new InheritingAttributeGroup<EdgeType>(name, this);
    addAttributeGroup(result);
    return result;
  }

  @Override
  public ConnectionMapping createMapping(NodeTypeBase from, NodeTypeBase to, JunctionNodeType... junctions) {
    return createMapping(from, to, PSSIFOption.many(junctions));
  }

  @Override
  public ConnectionMapping createMapping(NodeTypeBase from, NodeTypeBase to, PSSIFOption<JunctionNodeType> junctions) {
    for (JunctionNodeType junction : junctions.getMany()) {
      mappings.add(new ConnectionMappingImpl(this, junction, junction));
      for (JunctionNodeType other : junctions.getMany()) {
        if (other != junction) {
          mappings.add(new ConnectionMappingImpl(this, junction, other));
        }
      }
      mappings.add(new ConnectionMappingImpl(this, from, junction));
      mappings.add(new ConnectionMappingImpl(this, junction, to));
    }

    MutableConnectionMapping result = new ConnectionMappingImpl(this, from, to);
    addMapping(result);
    return result;
  }

  @Override
  public final void addMapping(MutableConnectionMapping result) {
    mappings.add(result);
  }

  @Override
  public PSSIFOption<ConnectionMapping> getMappings() {
    return PSSIFOption.<ConnectionMapping> many(mappings);
  }

  @Override
  public PSSIFOption<ConnectionMapping> getMapping(NodeTypeBase from, NodeTypeBase to) {
    return PSSIFOption.<ConnectionMapping> many(getMutableMapping(from, to).getMany());
  }

  @Override
  public PSSIFOption<ConnectionMapping> getOutgoingMappings(final NodeTypeBase from) {
    return PSSIFOption.<ConnectionMapping> many(getOutgoingMutableMappings(from).getMany());
  }

  @Override
  public PSSIFOption<ConnectionMapping> getIncomingMappings(final NodeTypeBase to) {
    return PSSIFOption.<ConnectionMapping> many(getIncomingMutableMappings(to).getMany());
  }

  @Override
  public boolean isAssignableFrom(ElementType type) {
    if (this.equals(type)) {
      return true;
    }
    else {
      for (EdgeType special : getSpecials()) {
        if (special.isAssignableFrom(type)) {
          return true;
        }
      }
    }

    return false;
  }

  @Override
  public void inherit(EdgeType general) {
    if (general.isAssignableFrom(this)) {
      throw new PSSIFStructuralIntegrityException("inheritance cycle detected");
    }
    if (this.general != null) {
      this.general.unregisterSpecialization(this);
    }
    this.general = general;
    this.general.registerSpecialization(this);
  }

  @Override
  public PSSIFOption<EdgeType> getGeneral() {
    return PSSIFOption.one(general);
  }

  @Override
  public Collection<EdgeType> getSpecials() {
    return ImmutableSet.copyOf(specializations);
  }

  @Override
  public void registerSpecialization(EdgeType special) {
    specializations.add(special);
  }

  @Override
  public void unregisterSpecialization(EdgeType special) {
    specializations.remove(special);
  }

  @Override
  public Class<?> getMetaType() {
    return EdgeType.class;
  }

  @Override
  public PSSIFOption<MutableConnectionMapping> getMutableMapping(NodeTypeBase from, NodeTypeBase to) {
    MutableConnectionMapping result = null;

    for (MutableConnectionMapping candidate : mappings) {
      if (candidate.getFrom().isAssignableFrom(from) && candidate.getTo().isAssignableFrom(to)) {
        if (result != null) {
          if (result.getFrom().isAssignableFrom(candidate.getFrom()) && result.getTo().isAssignableFrom(candidate.getTo())) {
            result = candidate;
          }
        }
        else {
          result = candidate;
        }
      }
    }

    return PSSIFOption.one(result);
  }

  @Override
  public PSSIFOption<MutableConnectionMapping> getMutableMappings() {
    return PSSIFOption.many(mappings);
  }

  @Override
  public PSSIFOption<MutableConnectionMapping> getOutgoingMutableMappings(final NodeTypeBase from) {
    return PSSIFOption.many(Collections2.filter(mappings, new Predicate<ConnectionMapping>() {
      @Override
      public boolean apply(ConnectionMapping input) {
        return input.getFrom().isAssignableFrom(from);
      }
    }));
  }

  @Override
  public PSSIFOption<MutableConnectionMapping> getIncomingMutableMappings(final NodeTypeBase to) {
    return PSSIFOption.many(Collections2.filter(mappings, new Predicate<ConnectionMapping>() {
      @Override
      public boolean apply(ConnectionMapping input) {
        return input.getTo().isAssignableFrom(to);
      }
    }));
  }
}
