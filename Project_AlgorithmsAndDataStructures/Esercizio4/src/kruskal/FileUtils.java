package kruskal;

import graph.Edge;
import graph.Graph;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author:     Murazzano, Zendri
 *
 * Utils Class to read a file and setUp a data structure
 */
public class FileUtils {

    public static void GraphCSV(Graph graph, String path) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line = br.readLine();
            while (line != null) {
                String[] fields = line.split(",");
                graph.addEdge(new Edge<>(fields[0], fields[1] , Double.parseDouble(fields[2])));
                line = br.readLine();
            }
            br.close();
        }
        catch (IOException e) {
            System.out.println("Error, file not found ...");
        }
    }
}
