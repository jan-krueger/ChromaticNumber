import edu.um.chromaticnumber.ChromaticNumber;
import edu.um.chromaticnumber.Graph;
import edu.um.chromaticnumber.Node;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        //
        int type = 0;

        Graph graph = new Graph();
        if(type == 0) {

            if(args.length == 0) {
                System.out.println("Debug: No file path provided!");
                return;
            }

            String fileName = args[0];
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

                System.out.printf("Debug: Graph (%s) parsed with %d vertices and %d edges.%n", fileName, nodes.size(), edges.size());

            } catch (IOException e) {
                System.out.println(String.format("Debug %s:-1 >> %s", fileName, String.format("The file could not (!) be read. (%s)", e.getMessage())));
                System.exit(0);
                e.printStackTrace();
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

            final int nodes = 70;
            IntStream.range(0, nodes).forEach(i -> graph.addNode(i, -1));
            Random random = new Random();

            for(int from = 0; from < nodes; from++) {
                for(int to = 0; to < nodes; to++) {
                    if (from != to && random.nextDouble() < .3) {
                        graph.addEdge(from, to, true);
                    }
                }
            }

            //--- Create file
            StringBuilder builder = new StringBuilder();
            graph.getEdges().forEach((k, v) -> v.forEach(edge -> builder.append(String.format("%s %s%n", k, edge.getTo().getId()))));


            try {
                File file = new File("data/" + System.currentTimeMillis() + ".txt");
                if(file.createNewFile()) {
                    FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsolutePath());
                    fileOutputStream.write(builder.toString().getBytes(Charset.forName("UTF-8")));
                    fileOutputStream.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

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

        } else if(type == 8) {
            graph.addNode(0, -1);
            graph.addNode(1, -1);
            graph.addNode(2, -1);
            graph.addNode(3, -1);
            graph.addNode(4, -1);
            graph.addNode(5, -1);
            graph.addNode(6, -1);
            graph.addNode(7, -1);

            graph.addEdge(0, 1, true);
            graph.addEdge(0, 2, true);
            graph.addEdge(0, 3, true);
            graph.addEdge(1, 2, true);

            graph.addEdge(3, 4, true);
            graph.addEdge(3, 5, true);
            graph.addEdge(3, 6, true);
            graph.addEdge(3, 7, true);
            graph.addEdge(4, 5, true);
            graph.addEdge(4, 6, true);
            graph.addEdge(4, 7, true);

            graph.addEdge(5, 6, true);
            graph.addEdge(5, 7, true);

            graph.addEdge(6, 7, true);
        }

        long now = System.currentTimeMillis();
        //ChromaticNumber.exactTestAync(graph);
        final int result = ChromaticNumber.compute(ChromaticNumber.Type.EXACT, graph, false);
        System.out.printf("Time to execute: %dms%n", (System.currentTimeMillis() - now));

        //--- Gephi
        try {

            List<Color> colors = new LinkedList<Color>() {{
                this.add(new Color(0xBA8B02));
                this.add(new Color(0x3d72b4));
                this.add(new Color(0x135058));
                this.add(new Color(0xdc2430));
                this.add(new Color(0x7b4397));
                this.add( new Color(0x2a0845));
                this.add(new Color(0xFFA17F));
            }};

            StringBuilder builder = new StringBuilder();
            builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><gexf xmlns=\"http://www.gexf.net/1.2draft\" " +
                    "xmlns:viz=\"http://www.gexf.net/1.1draft/viz\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                    "xsi:schemaLocation=\"http://www.gexf.net/1.2draft http://www.gexf.net/1.2draft/gexf.xsd\" version=\"1.2\">" +
                    "<graph><nodes>");
            ChromaticNumber.graph.getNodes().forEach((k, v) -> {
                if(colors.size() <= v.getValue()) {
                    return;
                }
                Color color = colors.get(v.getValue());
                builder.append(String.format("<node id=\"%d\" label=\"glossy\"><viz:color r=\"%d\" g=\"%d\" b=\"%d\" /><viz:group value=\"%d\"/></node>",
                        v.getId(),
                        color.getRed(),
                        color.getGreen(),
                        color.getBlue(),
                        color.getRGB()
                ));
            });
            builder.append("</nodes><edges>");

            int edgeId = 0;
            for (Map.Entry<Integer, List<Node.Edge>> entry : ChromaticNumber.graph.getEdges().entrySet()) {
                for (Node.Edge edge : entry.getValue()) {
                    builder.append(String.format("<edge id=\"%d\" source=\"%d\" target=\"%d\" />", edgeId, edge.getFrom().getId(), edge.getTo().getId()));
                    edgeId++;
                }
            }

            builder.append("</edges></graph></gexf>");
            File file = new File("data/gephi.gexf");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(builder.toString().getBytes(Charset.forName("UTF-8")));
            fileOutputStream.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
