package kruskal;

import graph.Edge;
/***************************************************************************
 * File: KruskalUsage.java
 * Author: Murazzano, Zendri
 *
 * Implementation of the usage of Kruskal algorithm.
 */
import graph.Graph;

public class KruskalTest{
	
	public static void main(String []args){
		// Build the graph from filepath
		  Graph<String> graph = new Graph<>(false);

		  graph.addVertex("a");
		  graph.addVertex("b");
		  graph.addVertex("c");
		  graph.addVertex("d");
		  graph.addVertex("e");
		  graph.addVertex("f");
		  graph.addVertex("g");
		  graph.addVertex("h");
		  graph.addVertex("i");
		  
		  graph.addEdge(new Edge<>("a", "b", Double.parseDouble("4000")));
		  graph.addEdge(new Edge<>("b", "c", Double.parseDouble("8000")));
		  graph.addEdge(new Edge<>("c", "d", Double.parseDouble("7000")));
		  graph.addEdge(new Edge<>("d", "e", Double.parseDouble("9000")));
		  graph.addEdge(new Edge<>("e", "f", Double.parseDouble("10000")));
		  graph.addEdge(new Edge<>("f", "g", Double.parseDouble("2000")));
		  graph.addEdge(new Edge<>("g", "h", Double.parseDouble("1000")));
		  graph.addEdge(new Edge<>("h", "a", Double.parseDouble("8000")));
		  graph.addEdge(new Edge<>("b", "h", Double.parseDouble("11000")));
		  graph.addEdge(new Edge<>("h", "i", Double.parseDouble("7000")));
		  graph.addEdge(new Edge<>("i", "c", Double.parseDouble("2000")));
		  graph.addEdge(new Edge<>("i", "g", Double.parseDouble("6000")));
		  graph.addEdge(new Edge<>("c", "f", Double.parseDouble("4000")));
		  graph.addEdge(new Edge<>("d", "f", Double.parseDouble("14000")));
		  
		  System.out.println("Dati grafo in ingresso:");
	      System.out.println("Vertices: " + graph.getNumVertex());
	      System.out.println("Edges: " + graph.getNumEdges());
	      System.out.println("Total weight: " + String.format("%,.3f", graph.getWeight(graph) / 1000) + " km");
		  
	      // Kruskal algo
	      Graph<String> path = Kruskal.mst(graph);
	      
	      // Display the result of Kruskal algorithm
	      System.out.println("\nKruskal algorithm - Minimum forest covering the graph:");
	      System.out.println("Vertices: " + path.getNumVertex());
	      System.out.println("Edges: " + path.getNumEdges());
	      System.out.println("Total weight: " + String.format("%,.3f", path.getWeight(path) / 1000) + " km");
	      
	}

}