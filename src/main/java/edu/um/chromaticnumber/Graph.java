package edu.um.chromaticnumber;

import edu.um.chromaticnumber.Node.Edge;

import java.util.*;

public class Graph implements Cloneable {

    private Map<Integer, Node> nodes = new HashMap<>();
    private Map<Integer, List<Edge>> edges = new HashMap<>();

    private int minNodeId = Integer.MAX_VALUE;
    private int maxNodeId = Integer.MIN_VALUE;

    public Graph() {}

    public void reset() {
        this.nodes.values().forEach(e -> e.setValue(-1));
    }

    public void addNode(int id, int value) {
        minNodeId = Math.min(id, minNodeId);
        maxNodeId = Math.max(id, maxNodeId);
        if(!(this.nodes.containsKey(id))) {
            this.nodes.put(id, new Node(id, value));
        }
    }

    public void addEdge(int from, int to, boolean bidirectional) {
        if(!(this.edges.containsKey(from))) {
            this.edges.put(from, new ArrayList<>());
        }
        this.edges.get(from).add(new Edge(this.getNode(from), this.getNode(to)));

        if(bidirectional) {
            addEdge(to, from, false);
        }
    }

    public Node getNode(int i) {
        if(this.nodes.containsKey(i)) {
            return this.nodes.get(i);
        }
        throw new IllegalArgumentException();
    }

    public Node getNextAvailableNode(Node start) {
        for(int i = start.getId() + 1; i <= maxNodeId; i++) {
            if(this.nodes.containsKey(i)) {
                return this.nodes.get(i);
            }
        }
        return null;
    }

    public List<Edge> getEdges(int node) {
        return this.edges.getOrDefault(node, new ArrayList<>());
    }

    public Map<Integer, Node> getNodes() {
        return this.nodes;
    }

    public Map<Integer, List<Edge>> getEdges() {
        return this.edges;
    }


    public int getMaxNodeId() {
        return maxNodeId;
    }

    public int getMinNodeId() {
        return minNodeId;
    }

    @Override
    public Graph clone() {
        Graph clone = new Graph();
        this.nodes.forEach((k, v) -> clone.addNode(k, v.getValue()));
        this.edges.forEach((k, v) -> v.forEach(edge -> clone.addEdge(edge.getFrom().getId(), edge.getTo().getId(), true)));
        return clone;
    }
}
