package edu.um.chromaticnumber;

import java.util.*;
import java.util.stream.Collectors;

public class ChromaticNumber {

    public enum Type {
        UPPER,
        LOWER,
        EXACT
    }

    public static int compute(Type type, Graph graph, boolean printAdditionalInformation) {
        graph.reset();

        switch (type) {

            case LOWER: return lowerBound(graph);
            case UPPER: return upperBoundIterative(graph);
            case EXACT: return exactTest(graph, printAdditionalInformation);

        }
        throw new IllegalStateException();
    }

    //---
    private static int exactTest(Graph graph, boolean printAdditionalInformation) {
        int upper = simpleGreedyUpperBound(graph);
        System.out.println("<Exact Test> Upper Bound: " + upper);
        upper--;
        graph.reset();
        while (exact(graph, upper)) {
            System.out.println("<Exact Test> Testing #Colours: " + upper);
            graph.reset();
            upper++;
        }
        return upper + 1;
    }

    private static int simpleUpperBound(Graph graph) {
        return graph.getEdges().values().stream().mapToInt(List::size).max().getAsInt() + 1;
    }

    private static int upperBoundIterative(Graph graph) {
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

    // --- SIMPLE GREEDY UPPER BOUND ---
    private static int simpleGreedyUpperBound(Graph graph) {
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

    //--- EXACT CHROMATIC NUMBER ---
    private static boolean exact(Graph graph, int colours) {
        return exact(graph, colours, graph.getNode(graph.getMinNodeId()));
    }

    private static boolean exact(Graph graph, int color_nb, Node node) {

        //--- Are all nodes coloured? If so, we are done.
        if(graph.getNodes().values().stream().noneMatch(e -> e.getValue() == -1)) {
            return true;
        }

        //--- Check this note for all colours
        for(int c = 1; c <= color_nb; c++) {
            if(exactIsColourAvailable(graph, node, c)) {
                node.setValue(c);

                Node next = graph.getNextAvailableNode(node);

                if(next == null || exact(graph, color_nb, next)) {
                    return true;
                }

                node.setValue(0);
            }
        }

        return false;
    }

    private static boolean exactIsColourAvailable(Graph graph, Node node, int colour) {
        return graph.getEdges(node.getId()).stream().noneMatch(e -> e.getTo().getValue() == colour);
    }

    //--- LOWER BOUND --

    //http://www.dcs.gla.ac.uk/~pat/jchoco/clique/enumeration/report.pdf
    public static int lowerBound(Graph graph) {
        return bronKerbosch(graph, new ArrayList<>(), new ArrayList<>(graph.getNodes().values()), new ArrayList<>(), Integer.MAX_VALUE);
    }

    private static void tomita(Graph graph, List<Node> subg, List<Node> cand) {
        if(subg.isEmpty()) {
            System.out.println("clique,");
        } else {

        }
    }

    private static int bronKerbosch(Graph graph, List<Node> potential_clique, List<Node> candidates, List<Node> excluded_set, int min) {
        if(candidates.isEmpty() && excluded_set.isEmpty()) {
            min = Math.min(min, potential_clique.size());
        }

        Iterator<Node> nodeIterator = candidates.iterator();
        while (nodeIterator.hasNext()) {
            //---
            Node node = nodeIterator.next();
            List<Node> neighbours = graph.getEdges(node.getId())
                    .stream()
                    .map(Node.Edge::getTo)
                    .collect(Collectors.toList());

            //---
            /*List<Node> R_new = new ArrayList<Node>(_R) {{
                this.add(node);
            }};*/
            potential_clique.add(node);
            candidates = candidates.stream()
                    .filter(neighbours::contains)
                    .collect(Collectors.toList());
            excluded_set = excluded_set.stream()
                    .filter(neighbours::contains)
                    .collect(Collectors.toList());

            min = Math.min(bronKerbosch(graph, potential_clique, candidates, excluded_set, min), min);

            nodeIterator.remove();
            excluded_set.add(node);
        }

        return min;
    }

}
