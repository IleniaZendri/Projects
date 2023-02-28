package unionfind;
/************************************************************************
 * File: UnionFind.java
 * Author: Murazzano, Zendri
 *
 * An implementation of a union-find (disjoint set) data structure using
 * a disjoint-set forest.  This implementation implements the path
 * compression and union by rank optimizations. *
 * Disjoint set structures can be used to implement relational
 * unification, Kruskal's MST algorithm, or Hindley-Milner type
 * inference.  They are also good at finding connected components of
 * an undirected graph.
 */

import java.util.*; // For Map, HashMap


//A class representing the union-find abstraction.
public final class UnionFind<T> {
    //A utility struct holding an an object's parent and rank.
    private static final class Link<T> {
        public T parent;
        public int rank = 0;
        //Creates a new Link object with the specified parent.
        Link(T elem) {
            this.parent = elem;
        }
    }

    // A map from objects in the UnionFind structure to their associated
    private final Map<T, Link<T>> elems = new HashMap<T, Link<T>>();

    //Creates a new UnionFind structure that is initially empty.
    public UnionFind() {
        // Handled implicitly
    }

    /*Creates a new UnionFind structure that initially contains all of
     * the elements from the specified container.  Duplicate elements
     * will appear with multiplicity one.
     */
    public UnionFind(Collection<? extends T> elems) {
        // Iterate across the collection, adding each to this structure.
        for (T elem: elems)
            add(elem);
    }

    /*Inserts a new element into the UnionFind structure that initially
     * is in its own partition.  If the element already exists, this
     * function returns false.  Otherwise, it returns true.
     */
    public boolean add(T elem) {
        // Check for null.
        if (elem == null)
            throw new NullPointerException("UnionFind does not support null.");
        // Check whether this entry exists; fail if it does.
        if (elems.containsKey(elem))
            return false;
        // Otherwise add the element as its own parent.
        elems.put((T) elem, new Link<T>(elem));
        return true;
    }

    /*Given an element, returns the representative element of the set
     * containing that element.  If the element is not contained in the
     * structure, this method throws a NoSuchElementException.
     */
    public T find(T vertex) {
        // Check whether the element exists; fail if it doesn't.
        if (!elems.containsKey(vertex))
            throw new NoSuchElementException(vertex + " is not an element.");
        // Recursively search the structure and return the result.
        return recFind(vertex);
    }

    /*Given an element which is known to be in the structure, searches
     * for its representative element and returns it, applying path
     * compression at each step.
     */
    private T recFind(T parent) {
        // Get the info on this object.
        Link<T> info = elems.get(parent);
        /* If the element is its own parent, it's the representative of its
         * class and we should say so.
         */
        if (info.parent.equals(parent))
            return parent;
        /* Otherwise, look up the parent of this element, then compress the
         * path.
         */
        info.parent = recFind(info.parent);
        return info.parent;
    }
    
    /*Given two elements, unions together the sets containing those
     * elements.  If either element is not contained in the set,
     * throws a NoSuchElementException.
     */
    public void union(T vertex1, T vertex2) {       
        /* Get the link info for the parents.  This also handles the exception
         * guarantee.
         */
        Link<T> oneLink = elems.get(find(vertex1));
        Link<T> twoLink = elems.get(find(vertex2));
        // If these are the same object, we're done.
        if (oneLink == twoLink) return;
        /* Otherwise, link the two.  We'll do a union-by-rank, where the parent
         * with the lower rank will merge with the parent with higher rank.
         */
        if (oneLink.rank > twoLink.rank) {
            /* Since each parent must link to itself, the value of oneLink.parent
             * is the representative of one.
             */
            twoLink.parent = oneLink.parent;
        } else if (oneLink.rank < twoLink.rank) {
            // Same logic as above.
            oneLink.parent = twoLink.parent;
        } else {
            // Arbitrarily wire one to be the parent of two.
            twoLink.parent = oneLink.parent;   
            // Bump up the representative of one to the next rank.
            oneLink.rank++;
        }
    }
}