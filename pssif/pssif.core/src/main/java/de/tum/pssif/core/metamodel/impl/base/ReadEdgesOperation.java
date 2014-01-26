package de.tum.pssif.core.metamodel.impl.base;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.util.PSSIFOption;


public final class ReadEdgesOperation {
  private final ConnectionMapping mapping;

  /*package*/ReadEdgesOperation(ConnectionMapping mapping) {
    this.mapping = mapping;
  }

  public ConnectionMapping getMapping() {
    return mapping;
  }

  public PSSIFOption<Edge> apply(Model model) {
    return model.apply(this);
  }
}
