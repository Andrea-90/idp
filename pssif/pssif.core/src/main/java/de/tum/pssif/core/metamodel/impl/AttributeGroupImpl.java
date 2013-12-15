package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.AttributeGroup;
import de.tum.pssif.core.util.PSSIFUtil;


public class AttributeGroupImpl extends NamedImpl implements AttributeGroup {

  private final Map<String, Attribute> attributes = Maps.newHashMap();
  private final ElementTypeImpl<?>     owner;

  public AttributeGroupImpl(String name, ElementTypeImpl<?> owner) {
    super(name);
    this.owner = owner;
  }

  void addAttribute(AttributeImpl attribute) {
    this.attributes.put(PSSIFUtil.normalize(attribute.getName()), attribute);
  }

  @Override
  public Attribute findAttribute(String name) {
    Attribute result = this.attributes.get(PSSIFUtil.normalize(name));
    if (result != null) {
      return result;
    }
    AttributeGroup generalGroup = owner.getGeneral() != null ? owner.getGeneral().findAttributeGroup(getName()) : null;
    if (generalGroup != null) {
      return generalGroup.findAttribute(name);
    }
    return null;
  }

  @Override
  public Collection<Attribute> getAttributes() {
    Set<Attribute> result = Sets.newHashSet(this.attributes.values());
    if (owner.getGeneral() != null && owner.getGeneral().findAttributeGroup(getName()) != null) {
      result.addAll(owner.getGeneral().findAttributeGroup(getName()).getAttributes());
    }
    return Collections.unmodifiableCollection(result);

  }

  @Override
  public void removeAttribute(Attribute attribute) {
    attributes.remove(PSSIFUtil.normalize(attribute.getName()));
    //TODO fail if attribute not found?
  }

  @Override
  public Class<?> getMetaType() {
    return AttributeGroup.class;
  }

}
