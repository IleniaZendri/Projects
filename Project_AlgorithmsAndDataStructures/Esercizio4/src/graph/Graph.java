package graph;

import java.util.*;

/**
 * @author:     Murazzano Luca
 *  * 			Zendri Ilenia
 *
 * Graph class
 *
 * @param <T> Generic type
 */
public class Graph<T> {
    private final Map<T, LinkedList<Edge<T>>> adjList;
    private final boolean isOriented;

    /**
     * Graph constructor
     *
     * @param isOriented Graph type
     */
    public Graph(boolean isOriented) {
        adjList = new HashMap<>();
        this.isOriented = isOriented;
    }

    /**
     * Checks the graph's oriented property
     *
     * @return true if the graph is oriented, false otherwise
     */
    public boolean isOriented() {
        return isOriented;
    }

    /**
     * Checks if the Vertex v is contained in the Graph
     *
     * @param v Vertex
     * @return true if it is contained, false otherwise
     */
    public boolean contains(T vertex) {
        return adjList.containsKey(vertex);
    }

    /**
     * Adds the Vertex v in the Graph.
     *
     * @param v Vertex
     */
    public void addVertex(T vertex) {
        if (!adjList.containsKey(vertex))
            adjList.put(vertex, new LinkedList<>());
    }

    /**
     * Removes the Vertex v from Set<Vertex> vertices
     *
     * @param v Vertex
     */
    public void removeVertex(T vertex) {
        adjList.remove(vertex);
    }

    /**
     * Adds Edge<T> e in the Graph.
     *
     * @param e the Edge to be added in the Graph.
     */
    public void addEdge(Edge<T> e) {
        if (isOriented) {
        	Edge<T> eReverse = new Edge<>(e.getVertex2(), e.getVertex1(), e.getWeight());
            addEdgeOriented(e);
            addEdgeOriented(eReverse);
        }
        else {
            addEdgeOriented(e);
        }
    }

    private boolean edgeIsAlreadyPresent(Edge<T> e) {
        if (adjList.containsKey(e.getVertex1())) {
        	for (Edge<T> edge : adjList.get(e.getVertex1())) {
        		if (edge.getVertex2().equals(e.getVertex2())) {
        			return true;
        		}
        	} 
        }
        return false;
     }
    
    /**
     * Support method used in addEdge()
     *
     * @param e the Edge<T> to be added to the Graph.
     * @see #addEdge(Edge)
     */
    private void addEdgeOriented(Edge<T> e) {
        // first we check if both the vertices of the edge
        // are already inside the adjList, if don't, then we add them.
    	adjList.putIfAbsent(e.getVertex1(), new LinkedList<>());
        adjList.putIfAbsent(e.getVertex2(), new LinkedList<>());

         // finally we put the edge in the adjList
        adjList.get(e.getVertex1()).add(e);
    }

    /**
     * Removes an edge from the Graph.
     *
     * @param e the Edge<T> to be removed
     * @return true if it's removed, false otherwise
     */
    public boolean removeEdge(Edge<T> e) {
        if (!this.isOriented) {
            LinkedList<Edge<T>> edges = adjList.get(e.getVertex1());
            return edges.remove(e);
        } else {
            LinkedList<Edge<T>> edgesV1 = adjList.get(e.getVertex1());
            LinkedList<Edge<T>> edgesV2 = adjList.get(e.getVertex2());

            Edge<T> eReverse = new Edge<>(e.getVertex2(), e.getVertex1(), e.getWeight());

            return edgesV1.remove(e) && edgesV2.remove(eReverse);
        }
    }

    /**
     * It returns a LinkedList<T> which contains all the vertex of the Graph.
     *
     * @return a LinkedList<T> which contains all the vertex of the Graph.
     * @see GraphTests
     * @see PrimTest
     */
    public LinkedList<T> getVertex() {
        return new LinkedList<>(adjList.keySet());
    }

    
    public int getNumVertex() {
    	return adjList.size();
    }
    
    
    /**
     * Gets adjacent vertices from the Vertex<T> v.
     *
     * @param v Vertex from which start the computation.
     * @return a LinkedList<Vertex<T> containing all the adjacent vertices of the Vertex<T> v.
     */
    public LinkedList<T> getAdjVertex(T vertex) {
        LinkedList<T> res = new LinkedList<>();
        for (Edge<T> e: adjList.get(vertex)) {
            res.add(e.getVertex2());
        }
        return res;
    }

    /**
     * It returns a LinkedList<T> which contains all the edges of the Graph.
     *
     * @return a LinkedList<T> which contains all the edges of the Graph.
     * @see GraphTests
     * @see PrimTest
     */
    public LinkedList<Edge<T>> getEdges() {
        LinkedList<Edge<T>> res = new LinkedList<>();
        for (T vertex : adjList.keySet()) {
            res.addAll(adjList.get(vertex));
        }
        return res;
    }
    
    public int getNumEdges() {
    	return getEdges().size();
    }

    /**
     * Gets edge's weight
     *
     * @param v1 start vertex of the Edge to compute the weight
     * @param v2 final vertex of the Edge to compute the weight
     * @return the weight of edge from the first vertex v1 to the final vertex v2
     */
    public double getEdgeWeight(T v1, T v2) {
        LinkedList<Edge<T>> res = adjList.get(v1);
        for (Edge<T> e : res)
            if (e.getVertex1().equals(v1) && e.getVertex2().equals(v2))
                return e.getWeight();
        return 0;
    }

    public double getWeight(Graph<T> graph) {
		double cost = 0;
		LinkedList<Edge<T>> list = graph.getEdges();
		for (int i = 0; i < list.size(); i++) {
			T v1=list.get(i).getVertex1();
			T v2=list.get(i).getVertex2();
			cost = cost + getEdgeWeight(v1, v2);
		}
		return cost;
	}
    
    /**
     * Gets graph's size.
     *
     * @return the number of the vertex in the graph.
     */
    public int getGraphSize() {
        return adjList.size();
    }
    


    /**
     * Overrides the behavior of the method to create a string to represent the
     * Graph object
     *
     * @return Graph object printed into String
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (T vertex : adjList.keySet()) {
            for (Edge<T> e: adjList.get(vertex)) {
                s.append("from vertex ").
                        append(e.getVertex1()).append(" to vertex ").
                        append(e.getVertex2()).append(" weight ").
                        append(e.getWeight()).append("\n");
            }
        }
        return s.toString();
    }
}
