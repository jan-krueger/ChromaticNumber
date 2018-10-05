import edu.um.chromaticnumber.ChromaticNumber;
import edu.um.chromaticnumber.Graph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        //
        int type = 0;

        Graph graph = new Graph();
        if(type == 0) {
            String fileName = "data/graph.txt";
            System.out.println("########### READ FROM FILE ###########");
            try {
                List<String> lines = Files.readAllLines(Paths.get(fileName));
                Set<Integer> nodes = new HashSet<>();
                Set<int[]> edges = new HashSet<>();

                int lineNumber = 1;
                for (final String line : lines) {
                    if (!line.startsWith("VERTICES") && !line.startsWith("EDGES") && !line.startsWith("//")) {
                        String[] split = line.split(" ");

                        //--- Error
                        if (split.length != 2) {
                            System.out.println(String.format("Debug %s:%d >> %s", fileName, lineNumber, String.format("Malformed edge line: %s", line)));
                        }

                        int from = Integer.parseInt(split[0]);
                        int to = Integer.parseInt(split[1]);
                        nodes.add(from);
                        nodes.add(to);
                        edges.add(new int[]{from, to});
                    }

                    lineNumber++;
                }

                nodes.forEach(id -> graph.addNode(id, -1));
                edges.forEach(edge -> graph.addEdge(edge[0], edge[1], true));

            } catch (IOException e) {
                System.out.println(String.format("Debug %s:-1 >> %s", fileName, String.format("The file could not (!) be read. (%s)", e.getCause().getMessage())));
                System.exit(0);
            }

        } else if(type == 1) {
            System.out.println("########### TREE ###########");

            //--- Tree
            graph.addNode(0, -1);
            graph.addNode(1, -1);
            graph.addNode(2, -1);
            graph.addNode(3, -1);
            graph.addNode(4, -1);
            graph.addNode(5, -1);
            graph.addNode(6, -1);

            graph.addEdge(0, 1, true);
            graph.addEdge(0, 2, true);
            graph.addEdge(1, 3, true);
            graph.addEdge(1, 4, true);
            graph.addEdge(2, 5, true);
            graph.addEdge(2, 6, true);

        } else if(type == 2) {
            System.out.println("########### WHEEL ###########");

            //--- Wheel Graph
            graph.addNode(1, -1);
            graph.addNode(2, -1);
            graph.addNode(3, -1);
            graph.addNode(4, -1);

            graph.addEdge(1, 2, true);
            graph.addEdge(1, 4, true);
            graph.addEdge(1, 3, true);
            graph.addEdge(2, 3, true);
            graph.addEdge(4, 2, true);
            graph.addEdge(4, 3, true);
        }

        long time = System.nanoTime();
        int output = ChromaticNumber.exact(graph);
        long timeDelta = (System.nanoTime() - time);
        System.out.println("Upper Bound: " + ChromaticNumber.upperBound(graph) + " <-> " + ChromaticNumber.lowerBOund(graph));
        System.out.println(String.format("Chromatic Number: %d -> Time: %dns (%dms)", output, timeDelta, TimeUnit.NANOSECONDS.toMillis(timeDelta)));
    }

}
