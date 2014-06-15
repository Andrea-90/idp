package de.tum.pssif.core.metamodel;

import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.metamodel.impl.MetamodelImpl;
import de.tum.pssif.core.metamodel.mutable.MutableEdgeType;
import de.tum.pssif.core.metamodel.mutable.MutableEnumeration;
import de.tum.pssif.core.metamodel.mutable.MutableJunctionNodeType;
import de.tum.pssif.core.metamodel.mutable.MutableNodeType;


public final class PSSIFCanonicMetamodelCreator {
  public static final String ENUM_CONJUNCTION                          = "Conjunction";
  public static final String ENUM_CONJUNCTION_AND                      = "AND";
  public static final String ENUM_CONJUNCTION_OR                       = "OR";
  public static final String ENUM_CONJUNCTION_XOR                      = "XOR";

  //  public static final String ENUM_FLOW_DIRECTION                       = "FlowDirection";
  //  public static final String ENUM_FLOW_DIRECTION_IN                    = "In";
  //  public static final String ENUM_FLOW_DIRECTION_OUT                   = "Out";
  //  public static final String ENUM_FLOW_DIRECTION_IN_OUT                = "InOut";
  //
  //  public static final String ENUM_FEATURE_DIRECTION                    = "FlowDirection";
  //  public static final String ENUM_FEATUREDIRECTION_PROVIDED            = "Provided";
  //  public static final String ENUM_FEATURE_DIRECTION_REQUIRED           = "Required";
  //  public static final String ENUM_FEATURE_DIRECTION_PROVIDED_REQUIRED  = "RequiredProvided";

  public static final String N_DEV_ARTIFACT                            = "Development Artifact";

  public static final String N_FUNCTIONALITY                           = "Functionality";
  public static final String N_REQUIREMENT                             = "Requirement";
  public static final String N_USE_CASE                                = "Use Case";
  public static final String N_TEST_CASE                               = "Test Case";
  public static final String N_VIEW                                    = "View";
  public static final String N_EVENT                                   = "Event";
  public static final String N_ISSUE                                   = "Issue";
  public static final String N_DECISION                                = "Decision";
  public static final String N_CHANGE_PROPOSAL                         = "Change Proposal";
  public static final String N_CHANGE_EVENT                            = "Change Event";
  public static final String N_ABSTRACTION_LEVEL                       = "Abstraction Level";
  
  public static final String N_SPEC_ARTIFACT						   = "Specification Artifact";
  
  public static final String N_SCENARIO                            	   = "Scenario";
  public static final String N_EPK_D                                   = "EPK-Diagramm";
  public static final String N_ER_D                         	       = "ER-Diagramm";
  public static final String N_ACTIVITY_D                              = "Activity Diagramm";
  public static final String N_SEQ_D                                   = "Sequence Diagramm";
  public static final String N_STATE_D                                 = "State Diagramm";
  public static final String N_CLASS_D                                 = "Class Diagramm";
  public static final String N_TEXT                         	       = "Text";
  public static final String N_USE_CASE_D                       	   = "Use Case Diagramm";
  
  

  public static final String N_SOL_ARTIFACT                            = "Solution Artifact";

  public static final String N_BLOCK                                   = "Block";
  public static final String N_FUNCTION                                = "Function";
  public static final String N_ACTIVITY                                = "Activity";
  public static final String N_STATE                                   = "State";
  public static final String N_ACTOR                                   = "Actor";
  
  public static final String N_AUTHOR                                  = "Author";
  public static final String N_PRINCIPAL                               = "Principal";
  public static final String N_SERVICE                                 = "Service";
  public static final String N_SOFTWARE                                = "Software";
  public static final String N_HARDWARE                                = "Hardware";
  public static final String N_MECHANIC                                = "Mechanic";
  public static final String N_ELECTRONIC                              = "Electronic";
  public static final String N_MODULE                                  = "Module";
  public static final String N_CONJUNCTION                             = ENUM_CONJUNCTION;

  public static final String N_PORT                                    = "Port";
  public static final String N_PORT_SOFTWARE                           = "SoftwarePort";
  public static final String N_PORT_MECHANIC                           = "MechanicPort";
  public static final String N_PORT_ELECTRONIC                         = "ElectronicPort";

  public static final String A_DURATION                                = "duration";
  public static final String A_REQUIREMENT_PRIORITY                    = "priority";
  public static final String A_REQUIREMENT_TYPE                        = "type";
  public static final String A_BLOCK_COST                              = "cost";
  public static final String A_HARDWARE_WEIGHT                         = "weight";
  public static final String A_TEST_CASE_STATUS 					   = "status";
  public static final String A_CONJUNCTION                             = ENUM_CONJUNCTION;
  public static final String A_CONJUGATED                              = "conjugated";
  public static final String A_DIRECTION                               = "direction";
  public static final String A_DATA_TYPE                               = "dataType";

  public static final String E_FLOW                                    = "Flow";
  public static final String E_FLOW_ENERGY                             = "Energy Flow";
  public static final String E_FLOW_MATERIAL                           = "Material Flow";
  public static final String E_FLOW_INFORMATION                        = "Information Flow";
  public static final String E_FLOW_CONTROL                            = "Control Flow";
  public static final String E_FLOW_VALUE                              = "Value Flow";

  public static final String E_RELATIONSHIP                            = "Relationship";

  public static final String E_RELATIONSHIP_ATTRIBUTIONAL              = "Attributional Relation";
  public static final String E_RELATIONSHIP_ATTRIBUTIONAL_PERFORMS     = "Performs";
  public static final String E_RELATIONSHIP_ATTRIBUTIONAL_AVOIDS       = "Avoids";
  public static final String E_RELATIONSHIP_ATTRIBUTIONAL_ACCOUNTS_FOR = "Accounts For";

  public static final String E_RELATIONSHIP_CHRONOLOGICAL              = "Chronological Relation";
  public static final String E_RELATIONSHIP_CHRONOLOGICAL_EVOLVES_TO   = "Evolves To";
  public static final String E_RELATIONSHIP_CHRONOLOGICAL_REPLACES     = "Replaces";
  public static final String E_RELATIONSHIP_CHRONOLOGICAL_BASED_ON     = "Based On";
  public static final String E_RELATIONSHIP_CHRONOLOGICAL_REFINES      = "Refines";
  public static final String E_RELATIONSHIP_CHRONOLOGICAL_PRECONDITION = "Precondition";
  public static final String E_RELATIONSHIP_CHRONOLOGICAL_LEADS_TO 	   = "Leads To";

  public static final String E_RELATIONSHIP_REFERENTIAL                = "Referential Relation";
  public static final String E_RELATIONSHIP_REFERENTIAL_DESCRIBES      = "Describes";
  public static final String E_RELATIONSHIP_REFERENTIAL_DEFINES        = "Defines";
  public static final String E_RELATIONSHIP_REFERENTIAL_TRACE          = "Trace";
  public static final String E_RELATIONSHIP_REFERENTIAL_USES           = "Uses";

  public static final String E_RELATIONSHIP_INCLUSION                  = "Inclusion Relation";
  public static final String E_RELATIONSHIP_INCLUSION_CONTAINS         = "Contains";
  public static final String E_RELATIONSHIP_INCLUSION_INCLUDES         = "Includes";
  public static final String E_RELATIONSHIP_INCLUSION_GENERALIZES      = "Generalizes";
  public static final String E_RELATIONSHIP_INCLUSION_BELONGS_TO 	   = "Belongs To";

  public static final String E_RELATIONSHIP_CAUSAL                     = "Causal Relation";
  public static final String E_RELATIONSHIP_CAUSAL_CREATES             = "Creates";
  public static final String E_RELATIONSHIP_CAUSAL_REQUESTS            = "Requests";
  public static final String E_RELATIONSHIP_CAUSAL_REQUIRES            = "Requires";
  public static final String E_RELATIONSHIP_CAUSAL_IMPLEMENTS          = "Implements";
  public static final String E_RELATIONSHIP_CAUSAL_REALIZES            = "Realizes";

  public static final String E_RELATIONSHIP_LOGICAL                    = "Logical Relation";
  public static final String E_RELATIONSHIP_LOGICAL_CONFLICTS          = "Conflicts";
  public static final String E_RELATIONSHIP_LOGICAL_EXTENDS            = "Extends";
  public static final String E_RELATIONSHIP_LOGICAL_SATISFIES          = "Satisfies";
  public static final String E_RELATIONSHIP_LOGICAL_VERIFIES           = "Verifies";
  public static final String E_RELATIONSHIP_LOGICAL_OVERLAPS           = "Overlaps";
  public static final String E_RELATIONSHIP_LOGICAL_IS_ALTERNATIVE     = "Is Alternative";
  


  public static final String E_FULFILLS                                = "fulfills";
  public static final String E_IS_MANDATORY_FOR                        = "isMandatoryFor";
  public static final String E_IS_CONNECTED_TO                         = "isConnectedTo";

  private PSSIFCanonicMetamodelCreator() {
    //Nop
  }

  public static Metamodel create() {
    MetamodelImpl metamodel = new MetamodelImpl();

    createEnumerations(metamodel);
    createJunctionNodeTypes(metamodel);
    createDevArtifacts(metamodel);
    createSolArtifacts(metamodel);
    createRelationships(metamodel);
    createFlows(metamodel);

    return metamodel;
  }

  private static void createEnumerations(MetamodelImpl metamodel) {
    MutableEnumeration conjunction = metamodel.createEnumeration(ENUM_CONJUNCTION);
    conjunction.createLiteral(ENUM_CONJUNCTION_AND);
    conjunction.createLiteral(ENUM_CONJUNCTION_OR);
    conjunction.createLiteral(ENUM_CONJUNCTION_XOR);
  }

  private static void createJunctionNodeTypes(MetamodelImpl metamodel) {
    MutableJunctionNodeType junction = metamodel.createJunctionNodeType(N_CONJUNCTION);
    junction.createAttribute(junction.getAttributeGroup(PSSIFConstants.DEFAULT_ATTRIBUTE_GROUP_NAME).getOne(), A_CONJUNCTION,
        PrimitiveDataType.STRING, true, AttributeCategory.METADATA);
  }

  private static void createDevArtifacts(MetamodelImpl metamodel) {
    NodeType devArtifact = metamodel.createNodeType(N_DEV_ARTIFACT);

    NodeType functionality = metamodel.createNodeType(N_FUNCTIONALITY);
    functionality.inherit(devArtifact);

    MutableNodeType requirement = metamodel.createNodeType(N_REQUIREMENT);
    requirement.inherit(devArtifact);
    requirement.createAttribute(requirement.getDefaultAttributeGroup(), A_REQUIREMENT_PRIORITY, PrimitiveDataType.STRING, true,
        AttributeCategory.METADATA);
    requirement.createAttribute(requirement.getDefaultAttributeGroup(), A_REQUIREMENT_TYPE, PrimitiveDataType.STRING, true,
        AttributeCategory.METADATA);

    NodeType useCase = metamodel.createNodeType(N_USE_CASE);
    useCase.inherit(devArtifact);

    MutableNodeType testCase = metamodel.createNodeType(N_TEST_CASE);
    testCase.inherit(devArtifact);
    testCase.createAttribute(testCase.getDefaultAttributeGroup(), A_TEST_CASE_STATUS, PrimitiveDataType.STRING, true, AttributeCategory.METADATA);
    testCase.createAttribute(testCase.getDefaultAttributeGroup(), PSSIFConstants.A_TEST_CASE_CONDITION_ATTRIBUTE, PrimitiveDataType.STRING, true, AttributeCategory.METADATA);
    testCase.createAttribute(testCase.getDefaultAttributeGroup(), PSSIFConstants.A_TEST_CASE_CONDITION_OP, PrimitiveDataType.STRING, true, AttributeCategory.METADATA);
    testCase.createAttribute(testCase.getDefaultAttributeGroup(), PSSIFConstants.A_TEST_CASE_CONDITION_VALUE, PrimitiveDataType.STRING, true, AttributeCategory.METADATA);
    

    NodeType view = metamodel.createNodeType(N_VIEW);
    view.inherit(devArtifact);

    NodeType event = metamodel.createNodeType(N_EVENT);
    event.inherit(devArtifact);

    NodeType issue = metamodel.createNodeType(N_ISSUE);
    issue.inherit(event);

    NodeType decision = metamodel.createNodeType(N_DECISION);
    decision.inherit(event);
    
    NodeType changeProposal = metamodel.createNodeType(N_CHANGE_PROPOSAL);
    changeProposal.inherit(event);

    NodeType changeEvent = metamodel.createNodeType(N_CHANGE_EVENT);
    changeEvent.inherit(event);
    
    NodeType abstractionLevel = metamodel.createNodeType(N_ABSTRACTION_LEVEL);
    abstractionLevel.inherit(event);
    
    NodeType specArtifact = metamodel.createNodeType(N_SPEC_ARTIFACT);
    specArtifact.inherit(devArtifact);
    
    NodeType useCaseDiag = metamodel.createNodeType(N_USE_CASE_D);
    useCaseDiag.inherit(specArtifact);
    
    NodeType scenario = metamodel.createNodeType(N_SCENARIO);
    scenario.inherit(specArtifact);
    
    NodeType epkDiag = metamodel.createNodeType(N_EPK_D);
    epkDiag.inherit(specArtifact);
    
    NodeType erDiag = metamodel.createNodeType(N_ER_D);
    erDiag.inherit(specArtifact);
    
    NodeType activityDiag = metamodel.createNodeType(N_ACTIVITY_D);
    activityDiag.inherit(specArtifact);
    
    NodeType sequenceDiag = metamodel.createNodeType(N_SEQ_D);
    sequenceDiag.inherit(specArtifact);
    
    NodeType stateDiag = metamodel.createNodeType(N_STATE_D);
    stateDiag.inherit(specArtifact);
    
    NodeType classDiag = metamodel.createNodeType(N_CLASS_D);
    classDiag.inherit(specArtifact);
    
    NodeType text = metamodel.createNodeType(N_TEXT);
    text.inherit(specArtifact);
    
    
  }
  
  
  private static void createSolArtifacts(MetamodelImpl metamodel) {
    NodeType solutionArtifact = metamodel.createNodeType(N_SOL_ARTIFACT);

    MutableNodeType block = metamodel.createNodeType(N_BLOCK);
    block.inherit(solutionArtifact);
    block.createAttribute(block.getDefaultAttributeGroup(), A_BLOCK_COST, PrimitiveDataType.DECIMAL, true, AttributeCategory.MONETARY);

    NodeType function = metamodel.createNodeType(N_FUNCTION);
    function.inherit(solutionArtifact);

    MutableNodeType activity = metamodel.createNodeType(N_ACTIVITY);
    activity.inherit(function);
    activity.createAttribute(activity.getDefaultAttributeGroup(), A_DURATION, PrimitiveDataType.INTEGER, Units.SECOND, true, AttributeCategory.TIME);

    NodeType state = metamodel.createNodeType(N_STATE);
    state.inherit(function);

    NodeType actor = metamodel.createNodeType(N_ACTOR);
    actor.inherit(block);
    
    NodeType author = metamodel.createNodeType(N_AUTHOR);
    author.inherit(actor);
    
    NodeType principal = metamodel.createNodeType(N_PRINCIPAL);
    principal.inherit(actor);

    NodeType service = metamodel.createNodeType(N_SERVICE);
    service.inherit(block);

    NodeType software = metamodel.createNodeType(N_SOFTWARE);
    software.inherit(block);

    MutableNodeType hardware = metamodel.createNodeType(N_HARDWARE);
    hardware.inherit(block);
    hardware.createAttribute(hardware.getDefaultAttributeGroup(), A_HARDWARE_WEIGHT, PrimitiveDataType.DECIMAL, true, AttributeCategory.WEIGHT);

    NodeType mechanic = metamodel.createNodeType(N_MECHANIC);
    mechanic.inherit(hardware);

    NodeType electronic = metamodel.createNodeType(N_ELECTRONIC);
    electronic.inherit(hardware);

    NodeType module = metamodel.createNodeType(N_MODULE);
    module.inherit(block);

    MutableNodeType port = metamodel.createNodeType(N_PORT);
    port.createAttribute(port.getDefaultAttributeGroup(), A_CONJUGATED, PrimitiveDataType.BOOLEAN, true, AttributeCategory.METADATA);
    port.createAttribute(port.getDefaultAttributeGroup(), A_DIRECTION, PrimitiveDataType.STRING, true, AttributeCategory.METADATA);
    port.createAttribute(port.getDefaultAttributeGroup(), A_DATA_TYPE, PrimitiveDataType.STRING, true, AttributeCategory.METADATA);

    MutableNodeType electronicPort = metamodel.createNodeType(N_PORT_ELECTRONIC);
    electronicPort.inherit(port);
    MutableNodeType mechanicPort = metamodel.createNodeType(N_PORT_MECHANIC);
    mechanicPort.inherit(port);
    MutableNodeType softwarePort = metamodel.createNodeType(N_PORT_SOFTWARE);
    softwarePort.inherit(port);
  }

  private static void createRelationships(MetamodelImpl metamodel) {
    MutableEdgeType relationship = metamodel.createEdgeType(E_RELATIONSHIP);
    relationship.createMapping(node(N_REQUIREMENT, metamodel), node(N_REQUIREMENT, metamodel));
    relationship.createMapping(node(N_FUNCTION, metamodel), node(PSSIFConstants.ROOT_NODE_TYPE_NAME, metamodel));
    relationship.createMapping(node(PSSIFConstants.ROOT_NODE_TYPE_NAME, metamodel), node(N_FUNCTION, metamodel));

    MutableEdgeType fulfils = metamodel.createEdgeType(E_FULFILLS);
    fulfils.createMapping(node(N_BLOCK, metamodel), node(N_FUNCTIONALITY, metamodel));

    MutableEdgeType isMandatoryFor = metamodel.createEdgeType(E_IS_MANDATORY_FOR);
    isMandatoryFor.createMapping(node(N_PORT, metamodel), node(N_FUNCTIONALITY, metamodel));

    MutableEdgeType isConnectedTo = metamodel.createEdgeType(E_IS_CONNECTED_TO);
    isConnectedTo.createMapping(node(N_PORT_ELECTRONIC, metamodel), node(N_PORT_ELECTRONIC, metamodel));
    isConnectedTo.createMapping(node(N_PORT_MECHANIC, metamodel), node(N_PORT_MECHANIC, metamodel));
    isConnectedTo.createMapping(node(N_PORT_SOFTWARE, metamodel), node(N_PORT_SOFTWARE, metamodel));

    createAttributionalRelationships(metamodel, relationship);
    createChronologicalRelationships(metamodel, relationship);
    createReferentialRelationships(metamodel, relationship);
    createInclusionRelationships(metamodel, relationship);
    createCausalRelationships(metamodel, relationship);
    createLogicalRelationships(metamodel, relationship);

  }

  private static void createAttributionalRelationships(MetamodelImpl metamodel, EdgeType relationship) {
    EdgeType attributionalRelationship = metamodel.createEdgeType(E_RELATIONSHIP_ATTRIBUTIONAL);
    attributionalRelationship.inherit(relationship);

    MutableEdgeType performsRelationship = metamodel.createEdgeType(E_RELATIONSHIP_ATTRIBUTIONAL_PERFORMS);
    performsRelationship.createMapping(node("Node", metamodel), node("Node", metamodel));
    performsRelationship.inherit(attributionalRelationship);

    EdgeType avoidsRelationship = metamodel.createEdgeType(E_RELATIONSHIP_ATTRIBUTIONAL_AVOIDS);
    avoidsRelationship.inherit(attributionalRelationship);

    EdgeType accountsForRelationship = metamodel.createEdgeType(E_RELATIONSHIP_ATTRIBUTIONAL_ACCOUNTS_FOR);
    accountsForRelationship.inherit(attributionalRelationship);
  }

  private static void createChronologicalRelationships(MetamodelImpl metamodel, EdgeType relationship) {
    EdgeType chronologicalRelationship = metamodel.createEdgeType(E_RELATIONSHIP_CHRONOLOGICAL);
    chronologicalRelationship.inherit(relationship);

    MutableEdgeType evolvesToRelationship = metamodel.createEdgeType(E_RELATIONSHIP_CHRONOLOGICAL_EVOLVES_TO);
    evolvesToRelationship.createMapping(node(N_REQUIREMENT, metamodel), node(N_REQUIREMENT, metamodel));
    evolvesToRelationship.createMapping(node(N_SOL_ARTIFACT, metamodel), node(N_SOL_ARTIFACT, metamodel));
    evolvesToRelationship.inherit(chronologicalRelationship);

    EdgeType replacesRelationship = metamodel.createEdgeType(E_RELATIONSHIP_CHRONOLOGICAL_REPLACES);
    replacesRelationship.inherit(chronologicalRelationship);

    MutableEdgeType basedOnRelationship = metamodel.createEdgeType(E_RELATIONSHIP_CHRONOLOGICAL_BASED_ON);
    basedOnRelationship.createMapping(node(N_REQUIREMENT, metamodel), node(N_REQUIREMENT, metamodel));
    basedOnRelationship.createMapping(node(N_TEST_CASE, metamodel), node(N_REQUIREMENT, metamodel));
    basedOnRelationship.createMapping(node(N_REQUIREMENT, metamodel), node(N_CHANGE_EVENT, metamodel));
    basedOnRelationship.createMapping(node(N_SOL_ARTIFACT, metamodel), node(N_CHANGE_EVENT, metamodel));
    basedOnRelationship.inherit(chronologicalRelationship);
    
    MutableEdgeType leadsToRelationship = metamodel.createEdgeType(E_RELATIONSHIP_CHRONOLOGICAL_LEADS_TO);
    leadsToRelationship.createMapping(node(N_TEST_CASE, metamodel), node(N_EVENT, metamodel));
    leadsToRelationship.createMapping(node(N_EVENT, metamodel), node(N_EVENT, metamodel));
    leadsToRelationship.inherit(chronologicalRelationship);

    EdgeType refinesRelationship = metamodel.createEdgeType(E_RELATIONSHIP_CHRONOLOGICAL_REFINES);
    refinesRelationship.inherit(chronologicalRelationship);

    EdgeType preconditionRelationship = metamodel.createEdgeType(E_RELATIONSHIP_CHRONOLOGICAL_PRECONDITION);
    preconditionRelationship.inherit(chronologicalRelationship);
  }

  private static void createReferentialRelationships(MetamodelImpl metamodel, EdgeType relationship) {
    MutableEdgeType referentialRelationship = metamodel.createEdgeType(E_RELATIONSHIP_REFERENTIAL);
    referentialRelationship.createMapping(node(N_EVENT, metamodel), node(N_EVENT, metamodel));
    referentialRelationship.inherit(relationship);

    EdgeType describesRelationship = metamodel.createEdgeType(E_RELATIONSHIP_REFERENTIAL_DESCRIBES);
    describesRelationship.inherit(referentialRelationship);

    MutableEdgeType definesRelationship = metamodel.createEdgeType(E_RELATIONSHIP_REFERENTIAL_DEFINES);
    definesRelationship.createMapping(node(N_SPEC_ARTIFACT, metamodel), node(N_REQUIREMENT, metamodel));
    definesRelationship.inherit(referentialRelationship);

    EdgeType traceRelationship = metamodel.createEdgeType(E_RELATIONSHIP_REFERENTIAL_TRACE);
    traceRelationship.inherit(referentialRelationship);

    EdgeType usesRelationship = metamodel.createEdgeType(E_RELATIONSHIP_REFERENTIAL_USES);
    usesRelationship.inherit(referentialRelationship);
  }

  private static void createInclusionRelationships(MetamodelImpl metamodel, EdgeType relationship) {
    EdgeType inclusionRelationship = metamodel.createEdgeType(E_RELATIONSHIP_INCLUSION);
    inclusionRelationship.inherit(relationship);

    MutableEdgeType containsRelationship = metamodel.createEdgeType(E_RELATIONSHIP_INCLUSION_CONTAINS);
    containsRelationship.createMapping(node(N_SPEC_ARTIFACT, metamodel), node("Node", metamodel));
    containsRelationship.inherit(inclusionRelationship);

    containsRelationship.createMapping(node(N_MODULE, metamodel), node(N_PORT, metamodel));
    containsRelationship.createMapping(node(N_MECHANIC, metamodel), node(N_PORT_MECHANIC, metamodel));
    containsRelationship.createMapping(node(N_ELECTRONIC, metamodel), node(N_PORT, metamodel));
    containsRelationship.createMapping(node(N_SOFTWARE, metamodel), node(N_PORT_SOFTWARE, metamodel));
    containsRelationship.createMapping(node(N_BLOCK, metamodel), node(N_BLOCK, metamodel));

    EdgeType includesRelationship = metamodel.createEdgeType(E_RELATIONSHIP_INCLUSION_INCLUDES);
    includesRelationship.inherit(inclusionRelationship);

    EdgeType generalizesRelationship = metamodel.createEdgeType(E_RELATIONSHIP_INCLUSION_GENERALIZES);
    generalizesRelationship.inherit(inclusionRelationship);
    
    MutableEdgeType belongsToRelationship = metamodel.createEdgeType(E_RELATIONSHIP_INCLUSION_BELONGS_TO);
    belongsToRelationship.createMapping(node(N_REQUIREMENT, metamodel), node(N_ABSTRACTION_LEVEL, metamodel));
    belongsToRelationship.inherit(inclusionRelationship);
  }

  private static void createCausalRelationships(MetamodelImpl metamodel, EdgeType relationship) {
    EdgeType causalRelationship = metamodel.createEdgeType(E_RELATIONSHIP_CAUSAL);
    causalRelationship.inherit(relationship);

    MutableEdgeType createsRelationship = metamodel.createEdgeType(E_RELATIONSHIP_CAUSAL_CREATES);
    createsRelationship.createMapping(node(N_AUTHOR, metamodel), node(N_REQUIREMENT, metamodel));
    createsRelationship.inherit(causalRelationship);

    MutableEdgeType requestsRelationship = metamodel.createEdgeType(E_RELATIONSHIP_CAUSAL_REQUESTS);
    requestsRelationship.createMapping(node(N_PRINCIPAL, metamodel), node(N_REQUIREMENT, metamodel));
    requestsRelationship.inherit(causalRelationship);

    EdgeType requiresRelationship = metamodel.createEdgeType(E_RELATIONSHIP_CAUSAL_REQUIRES);
    requiresRelationship.inherit(causalRelationship);

    EdgeType implementsRelationship = metamodel.createEdgeType(E_RELATIONSHIP_CAUSAL_IMPLEMENTS);
    implementsRelationship.inherit(causalRelationship);

    EdgeType realizesRelationship = metamodel.createEdgeType(E_RELATIONSHIP_CAUSAL_REALIZES);
    realizesRelationship.inherit(causalRelationship);
  }

  private static void createLogicalRelationships(MetamodelImpl metamodel, EdgeType relationship) {
    EdgeType logicalRelationship = metamodel.createEdgeType(E_RELATIONSHIP_LOGICAL);
    logicalRelationship.inherit(relationship);

    EdgeType conflictsRelationship = metamodel.createEdgeType(E_RELATIONSHIP_LOGICAL_CONFLICTS);
    conflictsRelationship.inherit(logicalRelationship);

    EdgeType extendsRelationship = metamodel.createEdgeType(E_RELATIONSHIP_LOGICAL_EXTENDS);
    extendsRelationship.inherit(logicalRelationship);

    MutableEdgeType satisfiesRelationship = metamodel.createEdgeType(E_RELATIONSHIP_LOGICAL_SATISFIES);
    satisfiesRelationship.createMapping(node(N_SOL_ARTIFACT, metamodel), node(N_REQUIREMENT, metamodel));
    satisfiesRelationship.inherit(logicalRelationship);

    MutableEdgeType verifiesRelationship = metamodel.createEdgeType(E_RELATIONSHIP_LOGICAL_VERIFIES);
    verifiesRelationship.createMapping(node(N_TEST_CASE, metamodel), node(N_SOL_ARTIFACT, metamodel));
    verifiesRelationship.inherit(logicalRelationship);

    EdgeType overlapsRelationship = metamodel.createEdgeType(E_RELATIONSHIP_LOGICAL_OVERLAPS);
    overlapsRelationship.inherit(logicalRelationship);

    EdgeType isAlternativeRelationship = metamodel.createEdgeType(E_RELATIONSHIP_LOGICAL_IS_ALTERNATIVE);
    isAlternativeRelationship.inherit(logicalRelationship);
  }

  private static void createFlows(MetamodelImpl metamodel) {
    MutableEdgeType flow = metamodel.createEdgeType(E_FLOW);
    flow.createMapping(node(N_SOL_ARTIFACT, metamodel), node(N_SOL_ARTIFACT, metamodel));

    NodeType solutionArtifact = node(N_SOL_ARTIFACT, metamodel);

    MutableEdgeType energyFlow = metamodel.createEdgeType(E_FLOW_ENERGY);
    energyFlow.inherit(flow);
    energyFlow.createMapping(solutionArtifact, solutionArtifact);

    MutableEdgeType materialFlow = metamodel.createEdgeType(E_FLOW_MATERIAL);
    materialFlow.inherit(flow);
    materialFlow.createMapping(solutionArtifact, solutionArtifact);

    MutableEdgeType informationFlow = metamodel.createEdgeType(E_FLOW_INFORMATION);
    informationFlow.inherit(flow);
    informationFlow.createMapping(solutionArtifact, solutionArtifact);

    MutableEdgeType controlFlow = metamodel.createEdgeType(E_FLOW_CONTROL);
    PSSIFOption<JunctionNodeType> junction = metamodel.getJunctionNodeType(N_CONJUNCTION);
    controlFlow.inherit(flow);
    controlFlow.createMapping(node(N_FUNCTION, metamodel), node(N_FUNCTION, metamodel), junction);

    EdgeType valueFlow = metamodel.createEdgeType(E_FLOW_VALUE);
    valueFlow.inherit(flow);
  }

  private static NodeType node(String name, Metamodel metamodel) {
    return metamodel.getNodeType(name).getOne();
  }
}
