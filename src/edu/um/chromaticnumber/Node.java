package edu.um.chromaticnumber;

public class Node {

    private final int id;
    private int value;

    public Node(int id, int value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static class Edge {

        private Node a;
        private Node b;

        public Edge(Node a, Node b) {
            this.a = a;
            this.b = b;
        }

        public Node getA() {
            return this.a;
        }

        public Node getB() {
            return this.b;
        }

    }

}
