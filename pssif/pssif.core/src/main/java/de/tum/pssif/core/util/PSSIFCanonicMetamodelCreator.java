package de.tum.pssif.core.util;

import de.tum.pssif.core.PSSIFConstants;
import de.tum.pssif.core.metamodel.AttributeCategory;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.Multiplicity;
import de.tum.pssif.core.metamodel.Multiplicity.MultiplicityContainer;
import de.tum.pssif.core.metamodel.Multiplicity.UnlimitedNatural;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.PrimitiveDataType;
import de.tum.pssif.core.metamodel.Units;
import de.tum.pssif.core.metamodel.impl.MetamodelImpl;


public final class PSSIFCanonicMetamodelCreator {

  public static final String N_DEV_ARTIFACT                            = "Development Artifact";

  public static final String N_REQUIREMENT                             = "Requirement";
  public static final String N_USE_CASE                                = "Use Case";
  public static final String N_TEST_CASE                               = "Test Case";
  public static final String N_VIEW                                    = "View";
  public static final String N_EVENT                                   = "Event";
  public static final String N_ISSUE                                   = "Issue";
  public static final String N_DECISION                                = "Decision";
  public static final String N_CHANGE_EVENT                            = "Change Event";

  public static final String N_SOL_ARTIFACT                            = "Solution Artifact";

  public static final String N_BLOCK                                   = "Block";
  public static final String N_FUNCTION                                = "Function";
  public static final String N_ACTIVITY                                = "Activity";
  public static final String N_STATE                                   = "State";
  public static final String N_ACTOR                                   = "Actor";
  public static final String N_SERVICE                                 = "Service";
  public static final String N_SOFTWARE                                = "Software";
  public static final String N_HARDWARE                                = "Hardware";
  public static final String N_MECHANIC                                = "Mechanic";
  public static final String N_ELECTRONIC                              = "Electronic";

  public static final String A_DURATION                                = "duration";
  public static final String A_REQUIREMENT_PRIORITY                    = "priority";
  public static final String A_REQUIREMENT_TYPE                        = "type";
  public static final String A_BLOCK_COST                              = "cost";
  public static final String A_HARDWARE_WEIGHT                         = "weight";

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
  public static final String E_RELATIONSHIP_CHRONOLOGICAL_PRECONDITION = "Precondigion";

  public static final String E_RELATIONSHIP_REFERENTIAL                = "Referential Relation";
  public static final String E_RELATIONSHIP_REFERENTIAL_DESCRIBES      = "Describes";
  public static final String E_RELATIONSHIP_REFERENTIAL_DEFINES        = "Defines";
  public static final String E_RELATIONSHIP_REFERENTIAL_TRACE          = "Trace";
  public static final String E_RELATIONSHIP_REFERENTIAL_USES           = "Uses";

  public static final String E_RELATIONSHIP_INCLUSION                  = "Inclusion Relation";
  public static final String E_RELATIONSHIP_INCLUSION_CONTAINS         = "Contains";
  public static final String E_RELATIONSHIP_INCLUSION_INCLUDES         = "Includes";
  public static final String E_RELATIONSHIP_INCLUSION_GENERALIZES      = "Generalizes";

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

  private PSSIFCanonicMetamodelCreator() {
    //Nop
  }

  public static Metamodel create() {
    MetamodelImpl metamodel = new MetamodelImpl();

    createDevArtifacts(metamodel);
    createSolArtifacts(metamodel);
    createRelationships(metamodel);
    createFlows(metamodel);

    return metamodel;
  }

  private static void createDevArtifacts(MetamodelImpl metamodel) {
    NodeType devArtifact = metamodel.createNodeType(N_DEV_ARTIFACT);

    NodeType requirement = metamodel.createNodeType(N_REQUIREMENT);
    requirement.inherit(devArtifact);
    requirement.createAttribute(requirement.getDefaultAttributeGroup(), A_REQUIREMENT_PRIORITY, PrimitiveDataType.STRING, true,
        AttributeCategory.METADATA);
    requirement.createAttribute(requirement.getDefaultAttributeGroup(), A_REQUIREMENT_TYPE, PrimitiveDataType.STRING, true,
        AttributeCategory.METADATA);

    NodeType useCase = metamodel.createNodeType(N_USE_CASE);
    useCase.inherit(devArtifact);

    NodeType testCase = metamodel.createNodeType(N_TEST_CASE);
    testCase.inherit(devArtifact);

    NodeType view = metamodel.createNodeType(N_VIEW);
    view.inherit(devArtifact);

    NodeType event = metamodel.createNodeType(N_EVENT);
    event.inherit(devArtifact);

    NodeType issue = metamodel.createNodeType(N_ISSUE);
    issue.inherit(event);

    NodeType decision = metamodel.createNodeType(N_DECISION);
    decision.inherit(event);

    NodeType changeEvent = metamodel.createNodeType(N_CHANGE_EVENT);
    changeEvent.inherit(event);
  }

  private static void createSolArtifacts(MetamodelImpl metamodel) {
    NodeType solutionArtifact = metamodel.createNodeType(N_SOL_ARTIFACT);

    NodeType function = metamodel.createNodeType(N_FUNCTION);
    function.inherit(solutionArtifact);

    NodeType activity = metamodel.createNodeType(N_ACTIVITY);
    activity.inherit(function);
    activity.createAttribute(activity.getDefaultAttributeGroup(), A_DURATION, PrimitiveDataType.INTEGER, Units.SECOND, true, AttributeCategory.TIME);

    NodeType state = metamodel.createNodeType(N_STATE);
    state.inherit(function);

    NodeType block = metamodel.createNodeType(N_BLOCK);
    block.inherit(solutionArtifact);
    block.createAttribute(block.getDefaultAttributeGroup(), A_BLOCK_COST, PrimitiveDataType.DECIMAL, true, AttributeCategory.MONETARY);

    NodeType actor = metamodel.createNodeType(N_ACTOR);
    actor.inherit(block);

    NodeType service = metamodel.createNodeType(N_SERVICE);
    service.inherit(block);

    NodeType software = metamodel.createNodeType(N_SOFTWARE);
    software.inherit(block);

    NodeType hardware = metamodel.createNodeType(N_HARDWARE);
    hardware.inherit(block);
    hardware.createAttribute(hardware.getDefaultAttributeGroup(), A_HARDWARE_WEIGHT, PrimitiveDataType.DECIMAL, true, AttributeCategory.WEIGHT);

    NodeType mechanic = metamodel.createNodeType(N_MECHANIC);
    mechanic.inherit(hardware);

    NodeType electronic = metamodel.createNodeType(N_ELECTRONIC);
    electronic.inherit(hardware);
  }

  private static void createRelationships(MetamodelImpl metamodel) {
    EdgeType relationship = metamodel.createEdgeType(E_RELATIONSHIP);
    relationship.createMapping("from", node(N_FUNCTION, metamodel), defaultNoneToManyMultiplicity(), "to",
        node(PSSIFConstants.ROOT_NODE_TYPE_NAME, metamodel), defaultNoneToManyMultiplicity());
    relationship.createMapping("from", node(PSSIFConstants.ROOT_NODE_TYPE_NAME, metamodel), defaultNoneToManyMultiplicity(), "to",
        node(N_FUNCTION, metamodel), defaultNoneToManyMultiplicity());

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

    EdgeType performsRelationship = metamodel.createEdgeType(E_RELATIONSHIP_ATTRIBUTIONAL_PERFORMS);
    performsRelationship.inherit(attributionalRelationship);

    EdgeType avoidsRelationship = metamodel.createEdgeType(E_RELATIONSHIP_ATTRIBUTIONAL_AVOIDS);
    avoidsRelationship.inherit(attributionalRelationship);

    EdgeType accountsForRelationship = metamodel.createEdgeType(E_RELATIONSHIP_ATTRIBUTIONAL_ACCOUNTS_FOR);
    accountsForRelationship.inherit(attributionalRelationship);
  }

  private static void createChronologicalRelationships(MetamodelImpl metamodel, EdgeType relationship) {
    EdgeType chronologicalRelationship = metamodel.createEdgeType(E_RELATIONSHIP_CHRONOLOGICAL);
    chronologicalRelationship.inherit(relationship);

    EdgeType evolvesToRelationship = metamodel.createEdgeType(E_RELATIONSHIP_CHRONOLOGICAL_EVOLVES_TO);
    evolvesToRelationship.inherit(chronologicalRelationship);

    EdgeType replacesRelationship = metamodel.createEdgeType(E_RELATIONSHIP_CHRONOLOGICAL_REPLACES);
    replacesRelationship.inherit(chronologicalRelationship);

    EdgeType basedOnRelationship = metamodel.createEdgeType(E_RELATIONSHIP_CHRONOLOGICAL_BASED_ON);
    basedOnRelationship.inherit(chronologicalRelationship);

    EdgeType refinesRelationship = metamodel.createEdgeType(E_RELATIONSHIP_CHRONOLOGICAL_REFINES);
    refinesRelationship.inherit(chronologicalRelationship);

    EdgeType preconditionRelationship = metamodel.createEdgeType(E_RELATIONSHIP_CHRONOLOGICAL_PRECONDITION);
    preconditionRelationship.inherit(chronologicalRelationship);
  }

  private static void createReferentialRelationships(MetamodelImpl metamodel, EdgeType relationship) {
    EdgeType referentialRelationship = metamodel.createEdgeType(E_RELATIONSHIP_REFERENTIAL);
    referentialRelationship.inherit(relationship);

    EdgeType describesRelationship = metamodel.createEdgeType(E_RELATIONSHIP_REFERENTIAL_DESCRIBES);
    describesRelationship.inherit(referentialRelationship);

    EdgeType definesRelationship = metamodel.createEdgeType(E_RELATIONSHIP_REFERENTIAL_DEFINES);
    definesRelationship.inherit(referentialRelationship);

    EdgeType traceRelationship = metamodel.createEdgeType(E_RELATIONSHIP_REFERENTIAL_TRACE);
    traceRelationship.inherit(referentialRelationship);

    EdgeType usesRelationship = metamodel.createEdgeType(E_RELATIONSHIP_REFERENTIAL_USES);
    usesRelationship.inherit(referentialRelationship);
  }

  private static void createInclusionRelationships(MetamodelImpl metamodel, EdgeType relationship) {
    EdgeType inclusionRelationship = metamodel.createEdgeType(E_RELATIONSHIP_INCLUSION);
    inclusionRelationship.inherit(relationship);

    EdgeType containsRelationship = metamodel.createEdgeType(E_RELATIONSHIP_INCLUSION_CONTAINS);
    containsRelationship.inherit(inclusionRelationship);

    EdgeType includesRelationship = metamodel.createEdgeType(E_RELATIONSHIP_INCLUSION_INCLUDES);
    includesRelationship.inherit(inclusionRelationship);

    EdgeType generalizesRelationship = metamodel.createEdgeType(E_RELATIONSHIP_INCLUSION_GENERALIZES);
    generalizesRelationship.inherit(inclusionRelationship);
  }

  private static void createCausalRelationships(MetamodelImpl metamodel, EdgeType relationship) {
    EdgeType causalRelationship = metamodel.createEdgeType(E_RELATIONSHIP_CAUSAL);
    causalRelationship.inherit(relationship);

    EdgeType createsRelationship = metamodel.createEdgeType(E_RELATIONSHIP_CAUSAL_CREATES);
    createsRelationship.inherit(causalRelationship);

    EdgeType requestsRelationship = metamodel.createEdgeType(E_RELATIONSHIP_CAUSAL_REQUESTS);
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

    EdgeType satisfiesRelationship = metamodel.createEdgeType(E_RELATIONSHIP_LOGICAL_SATISFIES);
    satisfiesRelationship.inherit(logicalRelationship);

    EdgeType verifiesRelationship = metamodel.createEdgeType(E_RELATIONSHIP_LOGICAL_VERIFIES);
    verifiesRelationship.inherit(logicalRelationship);

    EdgeType overlapsRelationship = metamodel.createEdgeType(E_RELATIONSHIP_LOGICAL_OVERLAPS);
    overlapsRelationship.inherit(logicalRelationship);

    EdgeType isAlternativeRelationship = metamodel.createEdgeType(E_RELATIONSHIP_LOGICAL_IS_ALTERNATIVE);
    isAlternativeRelationship.inherit(logicalRelationship);
  }

  private static void createFlows(MetamodelImpl metamodel) {
    EdgeType flow = metamodel.createEdgeType(E_FLOW);
    flow.createMapping("from", node(N_SOL_ARTIFACT, metamodel), defaultNoneToManyMultiplicity(), "to", node(N_SOL_ARTIFACT, metamodel),
        defaultNoneToManyMultiplicity());

    NodeType block = node(N_BLOCK, metamodel);

    EdgeType energyFlow = metamodel.createEdgeType(E_FLOW_ENERGY);
    energyFlow.inherit(flow);
    energyFlow.createMapping("from", block, defaultNoneToManyMultiplicity(), "to", block, defaultNoneToManyMultiplicity());
    metamodel.addAlias(energyFlow, "EnergyFlow");

    EdgeType materialFlow = metamodel.createEdgeType(E_FLOW_MATERIAL);
    materialFlow.inherit(flow);
    materialFlow.createMapping("from", block, defaultNoneToManyMultiplicity(), "to", block, defaultNoneToManyMultiplicity());

    EdgeType informationFlow = metamodel.createEdgeType(E_FLOW_INFORMATION);
    informationFlow.inherit(flow);
    informationFlow.createMapping("from", block, defaultNoneToManyMultiplicity(), "to", block, defaultNoneToManyMultiplicity());
    metamodel.addAlias(informationFlow, "InformationFlow");

    EdgeType controlFlow = metamodel.createEdgeType(E_FLOW_CONTROL);
    controlFlow.inherit(flow);
    controlFlow.createMapping("from", node(N_FUNCTION, metamodel), defaultNoneToManyMultiplicity(), "to", node(N_FUNCTION, metamodel),
        defaultNoneToManyMultiplicity());

    EdgeType valueFlow = metamodel.createEdgeType(E_FLOW_VALUE);
    valueFlow.inherit(flow);
  }

  private static NodeType node(String name, Metamodel metamodel) {
    return metamodel.findNodeType(name);
  }

  private static Multiplicity defaultNoneToManyMultiplicity() {
    return MultiplicityContainer.of(1, 1, 0, UnlimitedNatural.UNLIMITED);
  }

}
