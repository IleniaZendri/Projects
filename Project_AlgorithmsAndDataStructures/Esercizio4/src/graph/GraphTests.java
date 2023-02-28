package graph;

import java.util.LinkedList;
import org.junit.*;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author:     Murazzano, Zendri
 */
public class GraphTests {
    private Graph<String> graph;

    @Before
    public void setUp() {
        graph = new Graph<>(false);

        String a = "A";
        String b = "B";
        String c = "C";
        String d = "D";
        String e = "E";

        Edge<String> ab5 = new Edge<String>(a, b, 5);
        Edge<String> bc10 = new Edge<String>(b, c, 10);
        Edge<String> ac6 = new Edge<String>(a, c, 6);
        Edge<String> de6 = new Edge<String>(d, e, 6);

        graph.addEdge(ab5);
        graph.addEdge(bc10);
        graph.addEdge(ac6);
        graph.addEdge(de6);
    }

    @Test
    public void testIsOriented(){
        assertFalse(graph.isOriented());
    }

    @Test
    public void testContains(){
        assertTrue(graph.contains("A"));
    }

    @Test
    public void testAddVertex() {
        String toAdd = "F";
        graph.addVertex(toAdd);
        assertTrue(graph.contains(toAdd));
    }

    @Test
    public void testRemoveVertex() {
        String toRemove = "E";
        graph.removeVertex(toRemove);
        assertFalse(graph.contains(toRemove));
    }

    @Test
    public void testAddEdge() {
        graph.addEdge(new Edge<String>("A", "D", 1));
        assertEquals(1.0, graph.getEdgeWeight("A", "D"),0);
    }

    @Test
    public void testRemoveEdge() {
        boolean test = graph.removeEdge(new Edge<String>("D", "E", 6));
        assertTrue(test);
    }

    @Test
    public void testGetVertex() {
        LinkedList<String> expected = new LinkedList<>();
        expected.add("A");
        expected.add("B");
        expected.add("C");
        expected.add("D");
        expected.add("E");

        LinkedList<String> result = graph.getVertex();
        assertEquals(expected, result);
    }

    @Test
    public void testGetAdjVertices() {
    	LinkedList<String> expeted = new LinkedList<>();
    	expeted= graph.getAdjVertex("B");
        assertEquals("C", expeted.getLast());
    }

    @Test
    public void testGetEdges() {
        Graph<String> graphClone = graph;
        LinkedList<Edge<String>> expected = graphClone.getEdges();
        LinkedList<Edge<String>> result = graph.getEdges();
        assertEquals(expected, result);
    }

    @Test
    public void testGetEdgeWeight() {
        assertEquals(5.0, graph.getEdgeWeight("A", "B"),0);
    }
    
    @Test
    public void testGetWeight() {
    	assertEquals(27.0, graph.getWeight(graph),0);
    }


    @Test
    public void testToString() {
        assertEquals("", new Graph(false).toString());
    }

    // For automaticly run all the tests
    /*public static void main(String[] args) {
        Result result = JUnitCore.runClasses(GraphTests.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println("\nGraphTests result: " + result.wasSuccessful());
    }*/
}
