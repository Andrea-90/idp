package reqtool;

import graph.model.MyEdge;
import graph.model.MyNode;

import java.util.LinkedList;

import model.ModelBuilder;
import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;

public class IssueResolver {
	private MyNode selectedNode;
	
	public IssueResolver (MyNode myNode){
		this.selectedNode = myNode;
	}
	
	public void resolveIssue (){
		
		System.out.println("Resolving Issue for Issue: "+selectedNode.getName());
		
		TestCaseVerifier test = new TestCaseVerifier(this.getTestCase());
		LinkedList<MyNode> solArts = test.getVerifiedSolArtifacts();
		System.out.println("Resolving Issue for  TestCase: "+getTestCase().getName());
		
		
		
		String condition = getTestCase().getNodeType().getType().getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_COMMENT).getOne().get(getTestCase().getNode()).getOne().asString();
				//getNodeType().getType().getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_COMMENT).getOne().get(getTestCase().getNode()).;
		
		System.out.println("TestCase Condition Leading to Issue: "+condition);
		
		
		System.out.println("Resolving Issue for  Requirement: "+getRequirement().getName());
		
		
		if (solArts.size()==0){
			System.out.println("No Solution Artifact related to this Issue!");
		}
		else if (solArts.size()==1){
			MyNode solArt = solArts.get(0);
			System.out.println("Resolving Issue for  Solution Artifact: "+solArt.getName());
			MyNode changeProposal = ModelBuilder.addNewNodeFromGUI("ChangeEvent for Issue"+selectedNode.getName() , ModelBuilder.getNodeTypes().getValue(PSSIFCanonicMetamodelCreator.N_CHANGE_EVENT));
			
			ModelBuilder.addNewEdgeGUI(selectedNode, changeProposal, ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_CHRONOLOGICAL_LEADS_TO), true);
			
			MyNode decision = ModelBuilder.addNewNodeFromGUI("Decision for ChangeProposal"+selectedNode.getName() , ModelBuilder.getNodeTypes().getValue(PSSIFCanonicMetamodelCreator.N_DECISION));
			
			ModelBuilder.addNewEdgeGUI(changeProposal, decision, ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_CHRONOLOGICAL_LEADS_TO), true);
			
			MyNode changeEvent = ModelBuilder.addNewNodeFromGUI("ChangeEvent"+selectedNode.getName(),  ModelBuilder.getNodeTypes().getValue(PSSIFCanonicMetamodelCreator.N_CHANGE_EVENT));
			
			ModelBuilder.addNewEdgeGUI(selectedNode, changeEvent, ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_REFERENTIAL), true);
			ModelBuilder.addNewEdgeGUI(changeProposal, changeEvent, ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_REFERENTIAL), true);
			ModelBuilder.addNewEdgeGUI(decision, changeEvent, ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_REFERENTIAL), true);
		}
		else {
			System.out.println("More than 1 Solution Artifact - TODO");
		}
			
		
		
	}
	
	private MyNode getRequirement() {
		MyNode node = null;
		for (MyEdge e: ModelBuilder.getAllEdges()){
			if (e.getSourceNode().equals(this.getTestCase()) && ((MyNode) e.getDestinationNode()).getNodeType().equals(ModelBuilder.getNodeTypes().getValue(PSSIFCanonicMetamodelCreator.N_REQUIREMENT))){
				node =  (MyNode) e.getDestinationNode();
			}
		}
		return node;
	}

	public MyNode getTestCase(){
		MyNode node = null;
		for (MyEdge e: ModelBuilder.getAllEdges()){
			if (e.getDestinationNode().equals(selectedNode) && e.getEdgeType().equals(ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_CHRONOLOGICAL_LEADS_TO))){
				node =  (MyNode) e.getSourceNode();
			}
		}
		return node;
	}

}
