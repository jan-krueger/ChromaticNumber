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

    public Map<Integer, List<Edge>> getEdges() {
        return this.edges;
    }

    public int[][] toAdjacentMatrix() {

        int max = this.nodes.keySet().stream().max(Integer::compare).get();
        int[][] matrix = new int[max][max];

        for(Map.Entry<Integer, Node> node : this.nodes.entrySet()) {
            for(Node.Edge edge : this.getEdges(node.getKey())) {
                matrix[node.getKey()][edge.getTo().getId()] = 1;
            }
        }

        return matrix;

    }

}
