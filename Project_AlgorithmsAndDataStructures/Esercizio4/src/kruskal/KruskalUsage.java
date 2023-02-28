package kruskal;

/***************************************************************************
 * File: KruskalUsage.java
 * Author: Murazzano, Zendri
 *
 * Implementation of the usage of Kruskal algorithm.
 */
import graph.Graph;

public class KruskalUsage{
	
	public static void mstKruskal(String filePath){
		// Build the graph from filepath
		  Graph<String> graph = new Graph<>(false);
	      FileUtils.GraphCSV(graph, filePath);

	      // Kruskal algo
	      Graph<String> path = Kruskal.mst(graph);
	      
	      // Display the result of Kruskal algorithm
	      System.out.println("\nKruskal algorithm - Minimum forest covering the graph:");
	      System.out.println("Vertices: " + path.getNumVertex());
	      System.out.println("Edges: " + path.getNumEdges());
	      System.out.println("Total weight: " + String.format("%,.3f", path.getWeight(path) / 1000) + " km");
	}
	
	public static void main(String[] args){
		mstKruskal("italian_dist_graph.csv");
	}
}

