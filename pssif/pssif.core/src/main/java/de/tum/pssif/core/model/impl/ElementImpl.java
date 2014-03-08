package de.tum.pssif.core.model.impl;

import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.common.PSSIFValue;
import de.tum.pssif.core.metamodel.impl.GetValueOperation;
import de.tum.pssif.core.metamodel.impl.SetValueOperation;
import de.tum.pssif.core.model.Element;


public abstract class ElementImpl implements Element {
  private Map<String, PSSIFOption<PSSIFValue>> values      = Maps.newHashMap();
  private Map<String, String>                  annotations = Maps.newHashMap();

  @Override
  public void setId(String id) {
    values.put(PSSIFConstants.BUILTIN_ATTRIBUTE_ID, PSSIFOption.one(PSSIFValue.create(id)));
  }

  @Override
  public final String getId() {
    return values.get(PSSIFConstants.BUILTIN_ATTRIBUTE_ID).getOne().asString();
  }

  @Override
  public PSSIFOption<PSSIFValue> apply(GetValueOperation op) {
    return values.get(op.getAttribute().getName());
  }

  @Override
  public void apply(SetValueOperation op) {
    values.put(op.getAttribute().getName(), op.getValue());
  }

  @Override
  public void annotate(String key, String value) {
    annotate(key, value, false);
  }

  @Override
  public void annotate(String key, String value, boolean overwrite) {
    if (!overwrite) {
      Preconditions.checkState(!annotations.containsKey(key) || annotations.get(key).equals(value));
    }
    annotations.put(key, value);
  }

  @Override
  public PSSIFOption<String> getAnnotation(String key) {
    return PSSIFOption.one(annotations.get(key));
  }
}
