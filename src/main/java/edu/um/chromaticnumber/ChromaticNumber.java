package edu.um.chromaticnumber;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ChromaticNumber {

    public enum Type {
        UPPER,
        LOWER,
        EXACT
    }

    public static int compute(Type type, Graph graph) {
        graph.reset();

        switch (type) {

            case LOWER: return -1;
            case UPPER: return upperBoundIterative(graph);
            case EXACT: return exactTest(graph);

        }
        throw new IllegalStateException();
    }

    //---
    public static int exactTest(Graph graph) {
        int upper = greedy(graph);
        System.out.println("Greedy " + upper);
        graph.reset();
        upper--;
        while (exact(graph, upper)) {
            graph.reset();
            upper--;
        }
        return upper+1;
    }

    public static int simpleUpperBound(Graph graph) {
        return graph.getEdges().values().stream().mapToInt(List::size).max().getAsInt() + 1;
    }

    public static int upperBoundIterative(Graph graph) {
        //--- Build unvisited map ordered by degree of nodes descending
        Stack<Node> unvisited = graph.getNodes().values().stream()
                .sorted(Comparator.comparingInt(o -> graph.getEdges(o.getId()).size()))
                .collect(Collectors.toCollection(Stack::new));
        int max = 0;
        while (!unvisited.isEmpty()){
            Node node = unvisited.pop();

            //--- What colours does its neighbours have?
            List<Node.Edge> edges = graph.getEdges(node.getId());
            List<Integer> colours = edges.stream()
                    .filter(edge -> edge.getTo().getValue() != -1)
                    .map(edge -> edge.getTo().getValue())
                    .collect(Collectors.toList());

            //--- No colours -> first node being visited in the graph
            if (colours.isEmpty()) {
                node.setValue(0);
            }
            //--- At least one colour -> not the first node anymore
            else {

                //--- "Highest"  value/colour adjacent to the node
                final int maxColour = colours.stream().max(Comparator.naturalOrder()).get();

                int colour = 0; // Lowest value we can chose for a valid colour

                //--- try to ideally find an existing colour that we can reuse
                while (colour <= maxColour) {
                    if (!colours.contains(colour)) {
                        break;
                    }
                    colour++;
                }

                node.setValue(colour);
                max = Math.max(max, colour);

            }

        }

        return max + 1;

    }
/*
    public static int lowerBound(Graph graph) {
        return bronKerbosch(graph.getEdgeList(), new ArrayList<>(), new ArrayList<>());
    }
*/
   /* private static int bronKerbosch(List<Node.Edge> p, List<Node.Edge> r, List<Node.Edge> x) {
        if(p.stream().filter(x::contains).count() == 0) {
            return r.size();
        }
        p.forEach(edge -> {
            bronKerbosch((p.contains(edge) ? new ArrayList<Node.Edge>() {{this.add(edge)}} : new ArrayList<>())), r.stream().filter())
        });
    }*/

    public static int greedy(Graph graph) {
        HashMap<Integer, Node> unvisited = new LinkedHashMap<>();
        Map.Entry<Integer, Node> entry = graph.getNodes().entrySet().stream().findFirst().get();
        unvisited.put(entry.getKey(), entry.getValue());

        int max = 0;
        while (!unvisited.isEmpty()){
            // is this (too) slow?
            Node node = unvisited.values().stream().findFirst().get();
            unvisited.remove(node.getId());

            //--- What colours does its neighbours have?
            List<Node.Edge> edges = graph.getEdges(node.getId());
            List<Integer> colours = edges.stream()
                    .filter(edge -> edge.getTo().getValue() != -1)
                    .map(edge -> edge.getTo().getValue())
                    .collect(Collectors.toList());

            //--- No colours -> first node being visited in the graph
            if (colours.isEmpty()) {
                node.setValue(0);
            }
            //--- At least one colour -> not the first node anymore
            else {

                //--- "Highest"  value/colour adjacent to the node
                final int maxColour = colours.stream().max(Comparator.naturalOrder()).get();

                int colour = 0; // Lowest value we can chose for a valid colour

                //--- try to ideally find an existing colour that we can reuse
                while (colour <= maxColour) {
                    if (!colours.contains(colour)) {
                        break;
                    }
                    colour++;
                }

                node.setValue(colour);
                max = Math.max(max, colour);

            }

            //--- call for neighbour nodes & figure out the "highest" value/colour used
            for (Node.Edge edge : edges) {
                if (edge.getTo().getValue() == -1) {
                    Node e = edge.getTo();
                    unvisited.put(e.getId(), e);
                }
            }

        }

        return max + 1;

    }

    public static boolean exact(Graph graph, int colours) {
        return exactIterative(graph, colours, graph.getNode(graph.getMinNodeId()));
    }

    private static boolean exactIterative(Graph graph, int color_nb, Node node) {

        if(graph.getNodes().values().stream().noneMatch(e -> e.getValue() == -1)) {
            return true;
        }

        for(int c = 1; c < color_nb + 1; c++) {
            if(isSafeColour(graph, node, c)) {
                node.setValue(c);

                Node next = graph.getNode(node.getId() + 1);

                if(next == null || exactIterative(graph, color_nb, next)) {
                    return true;
                }

                node.setValue(-1);
            }
        }

        return false;
    }

    private static boolean isSafeColour(Graph graph, Node node, int colour) {
        return graph.getEdges(node.getId()).stream().noneMatch(e -> e.getTo().getValue() == colour);
    }


    private static boolean isGraphFullyConnected(Graph graph) {
        //--- Check if the amount edges is at every node equals to the amount of total nodes minus one.
        Map<Integer, Node> nodes = graph.getNodes();
        return nodes.entrySet().stream().noneMatch(node -> graph.getEdges(node.getKey()).size() != nodes.size() - 1);
    }

    private static boolean isGraphTree(Graph graph, int nodeId, Set<Integer> visited, int currentParent) {

        visited.add(nodeId);

        for(Node.Edge edge : graph.getEdges(nodeId)) {
            if(!(visited.contains(edge.getTo().getId()))) {
                return isGraphTree(graph, edge.getTo().getId(), visited, nodeId);
            } else if (edge.getTo().getId() != currentParent) {
                return false;
            }
        }

        return true;
    }

}
