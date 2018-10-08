package edu.um.chromaticnumber;

import java.util.*;
import java.util.stream.Collectors;

public class ChromaticNumber {

    public enum Type {
        UPPER,
        LOWER,
        EXACT
    }

    public static int compute(Type type, Graph graph) {
        if(graph.getNodes().isEmpty() || graph.getEdges().isEmpty()) {
            return 1;
        }

        switch (type) {

            case UPPER:
                return upperBound(graph);

            case LOWER:
                return lowerBound(graph);

            case EXACT:
                List<Node> unvisited = new ArrayList<>(graph.getNodes().values());
                int max = -1;
                while (!(unvisited.isEmpty())) {
                    max = Math.max(exact(graph, unvisited.get(0), unvisited), max);
                }
                return max;

        }
        throw new IllegalStateException();
    }

    //--- Upper Bound
    private static int upperBound(Graph graph) {
        return graph.getNodes().keySet().stream().map(graph::getEdges).mapToInt(List::size).max().getAsInt();
    }

    private static int lowerBound(Graph graph) {
        return graph.getNodes().keySet().stream().map(graph::getEdges).mapToInt(List::size).min().getAsInt();
    }


    //---
    public static int exact(Graph graph, final Node node, List<Node> unvisited) {
        unvisited.remove(node);

        /*if(isGraphFullyConnected(graph)) {
            return graph.getNodes().size() - 1;
        } else if(isGraphTree(graph, node.getId(), new HashSet<>(), -1)) {
            return 2;
        } /*else if(isCircle(graph, )) {
            return graph.getNodes().size() % 2 == 0 ? 2 : 3;
        }*/

        //--- Termination Condition: Already checked this node!
        if(node.getValue() > -1) {
            return node.getValue();
        }

        //--- What colours does its neighbours have?
        List<Node.Edge> edges = graph.getEdges(node.getId());
        List<Integer> colours = edges.stream()
                .filter(edge -> edge.getTo().getValue() != -1)
                .map(edge -> edge.getTo().getValue())
                .collect(Collectors.toList());

        //--- No colours -> first node being visited in the graph
        if(colours.isEmpty()) {
            node.setValue(0);
        }
        //--- At least one colour -> not the first node anymore
        else {

            //--- "Highest"  value/colour adjacent to the node
            final int max = colours.stream().max(Comparator.naturalOrder()).get();

            int colour = 0; // Lowest value we can chose for a valid colour

            //--- try to ideally find an existing colour that we can reuse
            while (colour <= max) {
                if(!colours.contains(colour)) {
                    break;
                }
                colour++;
            }

            node.setValue(colour);

        }

        //--- call for neighbour nodes & figure out the "highest" value/colour used
        int max = node.getValue();
        for (Node.Edge edge : edges) {
            max = Math.max(max, exact(graph, edge.getTo(), unvisited));
        }

        return max;

    }

    public static int exactIterative(Graph graph) {
        HashMap<Integer, Node> unvisited = new LinkedHashMap<>();
        Map.Entry<Integer, Node> entry = graph.getNodes().entrySet().stream().findFirst().get();
        unvisited.put(entry.getKey(), entry.getValue());

        int max = 0;
        while (!unvisited.isEmpty()){
             // is this (too) slow?
            Node node = unvisited.values().stream().findFirst().get();
            System.out.println(node.getId());
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
