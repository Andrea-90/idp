package de.tum.pssif.transform.graphml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.junit.Test;


public class GraphMLReadTest {
  @Test
  public void testReadGraph() throws IOException {
    InputStream in = getClass().getResourceAsStream("/flow.graphml");

    GraphMLGraph graph = GraphMLGraph.read(in);

    for (GraphMLNode node : graph.getNodes()) {
      System.out.println("node: " + node.getId());

      System.out.println("attributes:");
      Map<String, String> attributes = node.getValues();
      for (String key : attributes.keySet()) {
        System.out.println("\t" + key + ": " + attributes.get(key));
      }
    }

    for (GraphMLEdge edge : graph.getEdges()) {
      System.out.println("edge: " + edge.getId() + "(" + edge.getSourceId() + "," + edge.getTargetId() + ")");

      System.out.println("attributes:");
      Map<String, String> attributes = edge.getValues();
      for (String key : attributes.keySet()) {
        System.out.println("\t" + key + ": " + attributes.get(key));
      }
    }

    //    while (vertices.hasNext()) {
    //      Vertex vertex = vertices.next();
    //      System.out.println("vertex: " + vertex.getId());
    //
    //      Iterator<Edge> outEdges = vertex.getEdges(Direction.OUT).iterator();
    //      Iterator<Edge> inEdges = vertex.getEdges(Direction.IN).iterator();
    //
    //      System.out.println("properties:");
    //      for (String key : vertex.getPropertyKeys()) {
    //        System.out.println("\t" + key + ": " + vertex.getProperty(key));
    //      }
    //
    //      System.out.println("out edges:");
    //      while (outEdges.hasNext()) {
    //        Edge edge = outEdges.next();
    //        System.out.println("\t" + edge.getId());
    //      }
    //
    //      System.out.println("in edges:");
    //      while (inEdges.hasNext()) {
    //        Edge edge = inEdges.next();
    //        System.out.println("\t" + edge.getId());
    //      }
    //
    //      System.out.println();
    //    }
  }
}
