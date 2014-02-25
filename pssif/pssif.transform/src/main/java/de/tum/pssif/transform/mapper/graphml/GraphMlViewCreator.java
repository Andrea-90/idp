package de.tum.pssif.transform.mapper.graphml;

import de.tum.pssif.core.PSSIFConstants;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.transform.transformation.CreateNodeTransformation;
import de.tum.pssif.transform.transformation.HideEdgeTypeAttributeTransformation;
import de.tum.pssif.transform.transformation.HideNodeTypeAttributeTransformation;
import de.tum.pssif.transform.transformation.MoveAttributeTransformation;
import de.tum.pssif.transform.transformation.RenameEdgeTypeTransformation;
import de.tum.pssif.transform.transformation.RenameNodeTypeTransformation;


public class GraphMlViewCreator {
  public static Metamodel createGraphMlView(Metamodel metamodel) {
    Metamodel view = new RenameEdgeTypeTransformation(metamodel.findEdgeType("Information Flow"), "InformationFlow").apply(metamodel);
    view = new RenameEdgeTypeTransformation(view.findEdgeType("Energy Flow"), "EnergyFlow").apply(view);
    view = new RenameEdgeTypeTransformation(view.findEdgeType("Material Flow"), "MaterialFlow").apply(view);
    view = new RenameNodeTypeTransformation(view.findNodeType("Function"), "AbstractFunction").apply(view);
    view = new RenameNodeTypeTransformation(view.findNodeType("Activity"), "Function").apply(view);
    NodeType rootNode = view.findNodeType(PSSIFConstants.ROOT_NODE_TYPE_NAME);
    EdgeType rootEdge = view.findEdgeType(PSSIFConstants.ROOT_EDGE_TYPE_NAME);
    view = new HideNodeTypeAttributeTransformation(rootNode, rootNode.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_COMMENT)).apply(view);
    view = new HideNodeTypeAttributeTransformation(rootNode, rootNode.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VALIDITY_END)).apply(view);
    view = new HideNodeTypeAttributeTransformation(rootNode, rootNode.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VALIDITY_START)).apply(view);
    view = new HideNodeTypeAttributeTransformation(rootNode, rootNode.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION)).apply(view);
    view = new HideEdgeTypeAttributeTransformation(rootEdge, rootEdge.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_COMMENT)).apply(view);
    view = new HideEdgeTypeAttributeTransformation(rootEdge, rootEdge.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VALIDITY_END)).apply(view);
    view = new HideEdgeTypeAttributeTransformation(rootEdge, rootEdge.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VALIDITY_START)).apply(view);
    view = new HideEdgeTypeAttributeTransformation(rootEdge, rootEdge.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION)).apply(view);

    view = new HideNodeTypeAttributeTransformation(view.findNodeType("Hardware"), view.findNodeType("Hardware").findAttribute("weight")).apply(view);
    view = new HideNodeTypeAttributeTransformation(view.findNodeType("Requirement"), view.findNodeType("Requirement").findAttribute("type"))
        .apply(view);
    view = new HideNodeTypeAttributeTransformation(view.findNodeType("Requirement"), view.findNodeType("Requirement").findAttribute("priority"))
        .apply(view);
    view = new HideNodeTypeAttributeTransformation(view.findNodeType("Function"), view.findNodeType("Function").findAttribute("duration"))
        .apply(view);
    view = new HideNodeTypeAttributeTransformation(view.findNodeType("Block"), view.findNodeType("Block").findAttribute("cost")).apply(view);

    view = new CreateNodeTransformation(view.findNodeType("Function"), view.findNodeType("Block"), view.findEdgeType("Relationship")).apply(view);
    view = new CreateNodeTransformation(view.findNodeType("State"), view.findNodeType("Block"), view.findEdgeType("Relationship")).apply(view);

    NodeType block = view.findNodeType("Block");
    Attribute id = block.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID);
    view = new MoveAttributeTransformation(view.findNodeType("Function"), "functionary", block, id, view.findEdgeType("Relationship")).apply(view);
    block = view.findNodeType("Block");
    Attribute name = block.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME);
    view = new MoveAttributeTransformation(view.findNodeType("Function"), "functionary", block, name, view.findEdgeType("Relationship")).apply(view);

    block = view.findNodeType("Block");
    id = block.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID);
    view = new MoveAttributeTransformation(view.findNodeType("State"), "functionary", block, id, view.findEdgeType("Relationship")).apply(view);
    block = view.findNodeType("Block");
    name = block.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME);
    view = new MoveAttributeTransformation(view.findNodeType("State"), "functionary", block, name, view.findEdgeType("Relationship")).apply(view);

    /*

    // Transformation starts here
    EdgeType informationFlow = view.findEdgeType("InformationFlow");
    //    EdgeType energyFlow = view.findEdgeType("EnergyFlow");
    EdgeType controlFlow = view.findEdgeType("Control Flow");
    EdgeType relationship = view.findEdgeType("Relationship");
    NodeType function = view.findNodeType("Function");
    NodeType state = view.findNodeType("State");
    NodeType block = view.findNodeType("Block");
    ConnectionMapping ifOrigMapping = informationFlow.getMapping(block, block);
    //    ConnectionMapping efOrigMapping = energyFlow.getMapping(block, block);
    ConnectionMapping relMappingF2B = relationship.getMapping(function, block);
    //    ConnectionMapping cfMapping = controlFlow.getMapping(function.getGeneral(), function.getGeneral());

    ConnectionMapping iff2s = informationFlow.createMapping("f2if", function,
        MultiplicityContainer.of(1, UnlimitedNatural.UNLIMITED, 0, UnlimitedNatural.UNLIMITED), "if2s", state,
        MultiplicityContainer.of(1, UnlimitedNatural.UNLIMITED, 0, UnlimitedNatural.UNLIMITED));
    controlFlow.createMapping("f2cf", function, MultiplicityContainer.of(1, UnlimitedNatural.UNLIMITED, 0, UnlimitedNatural.UNLIMITED), "cf2s",
        state, MultiplicityContainer.of(1, UnlimitedNatural.UNLIMITED, 0, UnlimitedNatural.UNLIMITED));
    //    ConnectionMapping eff2s = energyFlow.createMapping("f2ef", function,
    //        MultiplicityContainer.of(1, UnlimitedNatural.UNLIMITED, 0, UnlimitedNatural.UNLIMITED), "if2s", state,
    //        MultiplicityContainer.of(1, UnlimitedNatural.UNLIMITED, 0, UnlimitedNatural.UNLIMITED));
    //    ConnectionMapping efs2f = energyFlow.createMapping("s2ef", state,
    //        MultiplicityContainer.of(1, UnlimitedNatural.UNLIMITED, 0, UnlimitedNatural.UNLIMITED), "if2f", function,
    //        MultiplicityContainer.of(1, UnlimitedNatural.UNLIMITED, 0, UnlimitedNatural.UNLIMITED));

    view = new RetypeConnectionMappingTransformation(informationFlow, iff2s, controlFlow, new LeftJoinedConnectionMapping(iff2s,
        relMappingF2B.getFrom(), relMappingF2B.getTo(), ifOrigMapping)).apply(view);

    informationFlow = view.findEdgeType("InformationFlow");
    //    energyFlow = view.findEdgeType("EnergyFlow");
    controlFlow = view.findEdgeType("Control Flow");
    relationship = view.findEdgeType("Relationship");
    function = view.findNodeType("Function");
    state = view.findNodeType("State");
    block = view.findNodeType("Block");
    ifOrigMapping = informationFlow.getMapping(block, block);
    //    efOrigMapping = energyFlow.getMapping(block, block);
    relMappingF2B = relationship.getMapping(function, block);
    //    cfMapping = controlFlow.getMapping(function.getGeneral(), function.getGeneral());
    ConnectionMapping ifs2f = informationFlow.createMapping("s2if", state,
        MultiplicityContainer.of(1, UnlimitedNatural.UNLIMITED, 0, UnlimitedNatural.UNLIMITED), "if2f", function,
        MultiplicityContainer.of(1, UnlimitedNatural.UNLIMITED, 0, UnlimitedNatural.UNLIMITED));
    controlFlow.createMapping("s2cf", state, MultiplicityContainer.of(1, UnlimitedNatural.UNLIMITED, 0, UnlimitedNatural.UNLIMITED), "cf2f",
        function, MultiplicityContainer.of(1, UnlimitedNatural.UNLIMITED, 0, UnlimitedNatural.UNLIMITED));
    view = new RetypeConnectionMappingTransformation(informationFlow, ifs2f, controlFlow, new RightJoinedConnectionMapping(ifs2f,
        relMappingF2B.getFrom(), relMappingF2B.getTo(), ifOrigMapping)).apply(view);

    //    informationFlow = view.findEdgeType("InformationFlow");
    //    controlFlow = view.findEdgeType("Control Flow");
    //    function = view.findNodeType("Function");
    //    relationship = view.findEdgeType("Relationship");
    //    state = view.findNodeType("State");
    //    block = view.findNodeType("Block");
    //    relMappingF2B = relationship.getMapping(function, block);
    //    ConnectionMapping ifs2f = informationFlow.createMapping("s2if", state,
    //        MultiplicityContainer.of(1, UnlimitedNatural.UNLIMITED, 0, UnlimitedNatural.UNLIMITED), "if2f", function,
    //        MultiplicityContainer.of(1, UnlimitedNatural.UNLIMITED, 0, UnlimitedNatural.UNLIMITED));
    //
    //    view = new RetypeConnectionMappingTransformation(informationFlow, ifs2f, controlFlow, new RightJoinedConnectionMapping(ifs2f,
    //        relMappingF2B.getFrom(), relMappingF2B.getTo(), ifOrigMapping)).apply(view);
    //    //    view = new RetypeConnectionMappingTransformation(energyFlow, eff2s, controlFlow, new LeftJoinedConnectionMapping(eff2s, relMappingF2B.getFrom(),
    //    //        relMappingF2B.getTo(), efOrigMapping)).apply(view);
    //    //    view = new RetypeConnectionMappingTransformation(energyFlow, efs2f, controlFlow, new LeftJoinedConnectionMapping(efs2f, cfMapping.getTo(),
    //    //        cfMapping.getFrom(), efOrigMapping)).apply(view);
    //    //Transformation ends here

    //    view = new HideEdgeTypeTransformation(view.findEdgeType("Control Flow")).apply(view);

    view = new NodifyTransformation(view.findNodeType("Function"), view.findNodeType("Block"), view.findEdgeType("Relationship"), "functionary")
        .apply(view);
        
        
        */

    return view;
  }
}
