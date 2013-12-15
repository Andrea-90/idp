package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.DataType;
import de.tum.pssif.core.metamodel.Unit;


public class AttributeImpl extends NamedImpl implements Attribute {

  private final DataType type;
  private final Unit     unit;
  private final boolean  visible;

  public AttributeImpl(String name, DataType type, Unit unit, boolean visible) {
    super(name);
    this.type = type;
    this.unit = unit;
    this.visible = visible;
  }

  @Override
  public DataType getType() {
    return type;
  }

  @Override
  public Unit getUnit() {
    return unit;
  }

  @Override
  public boolean isVisible() {
    return visible;
  }

  @Override
  public final boolean equals(Object obj) {
    if (!(obj instanceof Attribute)) {
      return false;
    }
    return super.equals(obj) && getType().equals(((Attribute) obj).getType());
  }

  @Override
  public final int hashCode() {
    return getMetaType().hashCode() ^ (type.getName() + getName()).hashCode();
  }

  @Override
  public Class<?> getMetaType() {
    return Attribute.class;
  }

}
