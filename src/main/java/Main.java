import edu.um.chromaticnumber.ChromaticNumber;
import edu.um.chromaticnumber.Graph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        //
        int type = 4;

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
        } else if(type == 3) {
            System.out.println("new");
            graph.addNode(1, -1);
            graph.addNode(2, -1);
            graph.addNode(3, -1);
            graph.addNode(4, -1);
            graph.addNode(5, -1);
            graph.addNode(6, -1);
            graph.addNode(7, -1);
            graph.addNode(8, -1);



        } else if(type == 4) {
            final int nodes = 100;
            IntStream.range(0, nodes).forEach(i -> graph.addNode(i, -1));
            Random random = new Random();

            for(int from = 0; from < nodes; from++) {
                for(int to = 0; to < nodes; to++) {
                    if (from != to && random.nextDouble() < .1) {
                        graph.addEdge(from, to, true);
                    }
                }
            }

            /*
            long totalTime = 0;
            List<Node> empty = new ArrayList<>();
            int result = -1;
            for(int from = 0; from < nodes; from++) {
                long time = System.nanoTime();
                result = Math.max(result, ChromaticNumber.exact(graph, graph.getNode(from), empty));
                for(int to = 0; to < nodes; to++) {
                    if(from != to) {
                        graph.addEdge(from, to, true);
                        graph.getNode(from).setValue(-1);
                        graph.getNode(to).setValue(-1);
                    }
                }
                totalTime += (System.nanoTime() - time);
            }
            System.out.println(totalTime + "ns -> " + TimeUnit.NANOSECONDS.toMillis(totalTime) + " ms => " + result);
            */

        } else if(type == 5) {
            for(int i = 0; i < 12; i++) graph.addNode(i, -1);

            graph.addEdge(0, 1, true);
            graph.addEdge(0, 2, true);
            graph.addEdge(2, 3, true);
            graph.addEdge(1, 3, true);
            graph.addEdge(1, 4, true);
            graph.addEdge(1, 7, true);
            graph.addEdge(3, 7, true);
            graph.addEdge(4, 5, true);
            graph.addEdge(5, 6, true);
            graph.addEdge(6, 7, true);
            graph.addEdge(7, 8, true);
            graph.addEdge(8, 9, true);
            graph.addEdge(3, 9, true);
            graph.addEdge(3, 10, true);
            graph.addEdge(10, 11, true);
            graph.addEdge(11, 8, true);
        } else if(type == 6) {
            graph.addNode(0, -1);
            graph.addNode(1, -1);
            graph.addNode(2, -1);
            graph.addEdge(0, 1, true);
            graph.addEdge(1, 2, true);
            graph.addEdge(2, 0, true);
        } else if(type == 7) {

            for(int i = 0; i < 8; i++) graph.addNode(i, -1);

            graph.addEdge(0, 5, true);
            graph.addEdge(0,6, true);
            graph.addEdge(0, 7, true);
            graph.addEdge(1, 4, true);
            graph.addEdge(1, 6, true);
            graph.addEdge(1, 7, true);
            graph.addEdge(2, 4, true);
            graph.addEdge(2, 5,true );
            graph.addEdge(2, 7, true);
            graph.addEdge(3, 4, true);
            graph.addEdge(3, 5, true);
            graph.addEdge(3, 6, true);

        }

        System.out.println(ChromaticNumber.compute(ChromaticNumber.Type.EXACT, graph, true));
    }

}
