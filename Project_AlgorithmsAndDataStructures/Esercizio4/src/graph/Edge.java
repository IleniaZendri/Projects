package graph;

import java.util.Objects;

/**
 * @author:     Murazzano, Zendri
 * 
 * Edge class
 *
 * @param <T> Generic Type
 */
public class Edge<T> implements Comparable<Edge<T>>{
    private T vertex1, vertex2;
    private double weight;
    public final int tiebreaker;
    public static int nextTiebreaker = 0;
    /**
     * Costructor of Edge Object.
     *
     * @param v1 the first vertex
     * @param v2 the second vertex
     * @param weight the weight of the edge
     */
    public Edge(T v1, T v2, double weight){
        vertex1 = v1;
        vertex2 = v2;
        this.weight = weight;
        tiebreaker = nextTiebreaker++;
    }

    /**
     * Gets vertex1
     *
     * @return vertex1
     */
    public T getVertex1() {
        return vertex1;
    }

    /**
     * Gets vertex2
     *
     * @return vertex2
     */
    public T getVertex2() {
        return vertex2;
    }

    /**
     * Gets the weight of the edge
     *
     * @return weight
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Overrides the behavior of the method equals to return true if equals,
     * false otherwise
     *
     * @return true if they are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Edge))
            return false;

        Edge<T> _obj = (Edge<T>) obj;
        return _obj.vertex1.equals(vertex1) && _obj.vertex2.equals(vertex2)
                && _obj.weight == weight;
    }

    /**
     * Overrides the behavior of the method hashCode (it needs for the equals method)
     *
     * @return int hashcode
     */
    @Override
    public int hashCode() {
        int hash = 28;
        hash = 5 * hash + Objects.hashCode(vertex1);
        hash = 5 * hash + Objects.hashCode(vertex2);
        hash = 5 * hash + Objects.hashCode(this.weight);
        return hash;
    }
    

    /* Compares two edges first by their cost, then by their tiebreaker.
     * Because this class is only used internally, we don't need to worry
     * about the other fields.  They aren't relevant for the comparison.
     */
    public int compareTo(Edge<T> other) {
        // Check how the costs compare.
        if (weight < other.weight) return -1;
        if (weight > other.weight) return +1;
        /* If they have equal costs, use the tiebreaker to make the
         * decision.
         * */
        return tiebreaker - other.tiebreaker;
    }
}