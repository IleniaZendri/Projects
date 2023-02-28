package kruskal;
/***************************************************************************
 * File: Kruskal.java
 * Author: Murazzano, Zendri
 *
 * An implementation of Kruskal's algorithm for minimum spanning trees.
 */
import java.util.*; // For Set, List, Collections
import graph.Graph;
import graph.Edge;
import unionfind.UnionFind;

public final class Kruskal {
    /* Given an undirected graph with real-valued edge costs, returns a
     * spanning tree of that graph with minimum weight.
     */
    public static <T> Graph<T> mst(Graph<T> graph) {
        // Build up the graph that will hold the result.
        Graph<T> result = new Graph<T>(false);
        // Edge case - if the input graph has zero or one nodes, we're done.
        if (graph.getNumVertex() <= 1)
            return result;
        /* Begin by building up a collection of all the edges of the graph.
         * Because we are given the edges via bidirectional adjacency lists,
         * we need to do some processing for this step.
         */
        List<Edge<T>> edges = graph.getEdges();
        // Sort the edges in ascending order of size;
        Collections.sort(edges);
        // Set up the partition of nodes in a union-find structure.
        UnionFind<T> unionFind = new UnionFind<T>();
        
        for (T node : graph.getVertex())
            unionFind.add(node);
        /* Now, sweep over the edges, adding each edge if its endpoints aren't
         * in the same partition.
         */
        
        //System.out.println("Numero archi grafo partenza: "+graph.getNumEdges());
        //System.out.println(edges.size());
        //System.out.println("Numero vertici grafo partenza: "+graph.getNumVertex());
        
        int counter=0;
        int i=0;
        int n=graph.getNumVertex();
        int m=edges.size();
        
        /*for (Edge<T> edge: edges) {
            // If the endpoints are connected, skip this edge.
            if (unionFind.find(edge.getVertex1()) != unionFind.find(edge.getVertex2())) {
            	// Otherwise, add the edge.
                result.addEdge(edge);
                // Link the endpoints together.
                unionFind.union(unionFind.find(edge.getVertex1()), unionFind.find(edge.getVertex2()));
                numEdges=numEdges+1;
            }
            if(numEdges== graph.getNumVertex()-2) {
            	break;
            }
        }*/
        
        while(counter<n-1 && i<m) {
        	Edge<T> edge=edges.get(i);
        	if (unionFind.find(edge.getVertex1()) != unionFind.find(edge.getVertex2())) {
            	// Otherwise, add the edge.
                result.addEdge(edge);
                // Link the endpoints together.
                unionFind.union(unionFind.find(edge.getVertex1()), unionFind.find(edge.getVertex2()));
                counter++;
            }
        	i++;
        }
        
        //System.out.println("NumEdges: " + counter);
        /* Hand back the generated graph. */
        return result;
    }
} 