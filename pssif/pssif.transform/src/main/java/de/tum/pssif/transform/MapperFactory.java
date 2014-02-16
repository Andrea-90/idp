package de.tum.pssif.transform;

import de.tum.pssif.core.exception.PSSIFException;
import de.tum.pssif.core.util.PSSIFUtil;
import de.tum.pssif.transform.mapper.BpmnMapper;
import de.tum.pssif.transform.mapper.EpkMapper;
import de.tum.pssif.transform.mapper.SysMlMapper;
import de.tum.pssif.transform.mapper.graphml.GraphMLMapper;


public final class MapperFactory {

  /**
   * Umsatzorientierte Funktionsplanung.
   */
  public static final String UOFP  = "uofp";

  /**
   * SysML.
   */
  public static final String SYSML = "sysml";

  /**
   * EPK.
   */
  public static final String EPK   = "epk";

  /**
   * BPMN.
   */
  public static final String BPMN  = "bpmn";

  public static Mapper getMapper(String name) {
    if (PSSIFUtil.areSame(UOFP, name)) {
      return new GraphMLMapper();
    }
    else if (PSSIFUtil.areSame(SYSML, name)) {
      return new SysMlMapper();
    }
    else if (PSSIFUtil.areSame(EPK, name)) {
      return new EpkMapper();
    }
    else if (PSSIFUtil.areSame(BPMN, name)) {
      return new BpmnMapper();
    }
    throw new PSSIFException("No mapper found for name: " + name);
  }

  private MapperFactory() {
    //Nothing here
  }

}
