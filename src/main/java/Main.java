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

            graph.addEdge(1, 4, true);
            graph.addEdge(1, 6, true);
            graph.addEdge(1, 8, true);

            graph.addEdge(3, 2, true);
            graph.addEdge(3, 6, true);
            graph.addEdge(3, 8, true);

            graph.addEdge(5, 2, true);
            graph.addEdge(5, 4, true);
            graph.addEdge(5, 8, true);


            graph.addEdge(7, 2, true);
            graph.addEdge(7, 4, true);
            graph.addEdge(7, 6, true);
        } else if(type == 4) {
            final int nodes = 5000;
            IntStream.range(0, nodes).forEach(i -> graph.addNode(i, -1));
            Random random = new Random();

            for(int from = 0; from < nodes; from++) {
                for(int to = 0; to < nodes; to++) {
                    if (from != to && random.nextDouble() < .33333333333333) {
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

        }

        boolean iterative = true;

        long time = System.nanoTime();
        int output = iterative ? ChromaticNumber.exactIterative(graph) : ChromaticNumber.compute(ChromaticNumber.Type.EXACT, graph);
        long timeDelta = (System.nanoTime() - time);
        System.out.println("Upper Bound: " + ChromaticNumber.compute(ChromaticNumber.Type.UPPER, graph) + " <-> " + ChromaticNumber.compute(ChromaticNumber.Type.LOWER, graph));
        System.out.println(String.format("Chromatic Number: %d -> Time: %dns (%dms)", output, timeDelta, TimeUnit.NANOSECONDS.toMillis(timeDelta)));

    }

}