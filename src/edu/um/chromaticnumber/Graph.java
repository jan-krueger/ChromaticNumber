package edu.um.chromaticnumber;

import edu.um.chromaticnumber.Node.Edge;

import java.util.*;

public class Graph {

    private Map<Integer, Node> nodes = new HashMap<>();
    private Map<Integer, List<Edge>> edges = new HashMap<>();

    public Graph() {}

    public void addNode(int id, int value) {
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

    public List<Edge> getEdges(int node) {
        return this.edges.getOrDefault(node, new ArrayList<>());
    }


    public Map<Integer, Node> getNodes() {
        return this.nodes;
    }

}
