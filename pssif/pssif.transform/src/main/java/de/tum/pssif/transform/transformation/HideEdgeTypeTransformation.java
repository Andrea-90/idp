package de.tum.pssif.transform.transformation;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.metamodel.EdgeType;


public class HideEdgeTypeTransformation extends HideTypeTransformation<EdgeType> {
  public HideEdgeTypeTransformation(EdgeType type) {
    super(type);
  }

  @Override
  public void apply(View view) {
    if (getType() != null) {
      removeType(view, getType());
    }
  }

  private void removeType(View view, EdgeType type) {
    PSSIFOption<EdgeType> actualType = view.getEdgeType(type.getName());

    for (EdgeType met : actualType.getMany()) {
      for (EdgeType special : met.getSpecials()) {
        removeType(view, special);
      }
    }

    for (EdgeType met : actualType.getMany()) {
      view.removeEdgeType(met);
    }
  }
}
