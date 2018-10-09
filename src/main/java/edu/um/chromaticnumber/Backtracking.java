package edu.um.chromaticnumber;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Backtracking {


    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("data/graph.txt"));

            int lineNumber = 1;

            int edges = -1;
            int[][] adjacentMatrix = null;
            for (final String line : lines) {
                if(line.startsWith("\\\\")) {
                    continue;
                } else if(line.startsWith("VERTICES")) {
                    int vertices = Integer.parseInt(line.replaceAll(" ", "").split("=")[1]);
                    adjacentMatrix = new int[vertices][vertices];
                } else if(line.startsWith("EDGES")) {
                    edges = Integer.parseInt(line.replaceAll(" ", "").split("=")[1]);
                } else if(adjacentMatrix != null) {
                    String[] split = line.split(" ");

                    //--- Error
                    //if (split.length != 2) {
                    //    System.out.println(String.format("Debug %s:%d >> %s", fileName, lineNumber, String.format("Malformed edge line: %s", line)));
                    //}

                    int from = Integer.parseInt(split[0]) - 1;
                    int to = Integer.parseInt(split[1]) - 1;

                    adjacentMatrix[from][to] = 1;
                } else {
                    throw new IllegalStateException();
                }

                lineNumber++;
            }


            Backtracking backtracking = new Backtracking(adjacentMatrix);

        } catch (IOException e) {
            e.printStackTrace();
            //System.out.println(String.format("Debug %s:-1 >> %s", fileName, String.format("The file could not (!) be read. (%s)", e.getCause().getMessage())));
            System.exit(0);
        }

    }

    //---------------------
    private int[] color;
    private int[][] graph;

    public Backtracking(int[][] adjacentMatrix) {
        int m = 9;
        this.graph = adjacentMatrix;
        if(graphColoring(graph, m)) {
            System.out.println(Arrays.toString(color));
        } else {
            System.out.println("No Solution!");
        }
    }


    /* A utility function to check if the current
       color assignment is safe for vertex v */
    private boolean isSafe(int v, int[][] graph, int[] color,  int c)
    {
        for (int i = 0; i < graph.length; i++)
            if (graph[v][i] == 1 && c == color[i])
                return false;
        return true;
    }

    /* A recursive utility function to solve m
       coloring  problem */
    private boolean graphColoringUtil(int[][] graph, int m, int[] color, int v)
    {
        /* base case: If all vertices are assigned
           a color then return true */
        if (v == graph.length)
            return true;

        /* Consider this vertex v and try different
           colors */
        for (int c = 1; c <= m; c++)
        {
            /* Check if assignment of color c to v
               is fine*/
            if (isSafe(v, graph, color, c))
            {
                color[v] = c;

                /* recur to assign colors to rest
                   of the vertices */
                if (graphColoringUtil(graph, m,
                        color, v + 1))
                    return true;

                /* If assigning color c doesn't lead
                   to a solution then remove it */
                color[v] = 0;
            }
        }

        /* If no color can be assigned to this vertex
           then return false */
        return false;
    }

    /* This function solves the m Coloring problem using
       Backtracking. It mainly uses graphColoringUtil()
       to solve the problem. It returns false if the m
       colors cannot be assigned, otherwise return true
       and  prints assignments of colors to all vertices.
       Please note that there  may be more than one
       solutions, this function prints one of the
       feasible solutions.*/
    private boolean graphColoring(int[][] graph, int m)
    {
        // Initialize all color values as 0. This
        // initialization is needed correct functioning
        // of isSafe()
        color = new int[graph.length];
        for (int i = 0; i < graph.length; i++)
            color[i] = 0;

        // Call graphColoringUtil() for vertex 0
        if (!graphColoringUtil(graph, m, color, 0))
        {
            System.out.println("Solution does not exist");
            return false;
        }

        // Print the solution
        printSolution(color);
        return true;
    }

    /* A utility function to print solution */
    private void printSolution(int[] color)
    {
        System.out.println("Solution Exists: Following" +
                " are the assigned colors");
        for (int i = 0; i < graph.length; i++)
            System.out.print(" " + color[i] + " ");
        System.out.println();
    }

    private int upperBound(int[][] graph) {
        int result[] = new int[graph.length];

        // Initialize all vertices as unassigned
        Arrays.fill(result, -1);

        // Assign the first color to first vertex
        result[0]  = 0;

        // A temporary array to store the available colors. False
        // value of available[cr] would mean that the color cr is
        // assigned to one of its adjacent vertices
        boolean available[] = new boolean[graph.length];

        // Initially, all colors are available
        Arrays.fill(available, true);

        // Assign colors to remaining V-1 vertices
        for (int u = 1; u < graph.length; u++)
        {
            // Process all adjacent vertices and flag their colors
            // as unavailable
            Iterator<Integer> it = adj[u].iterator() ;
            while (it.hasNext())
            {
                int i = it.next();
                if (result[i] != -1)
                    available[result[i]] = false;
            }

            // Find the first available color
            int cr;
            for (cr = 0; cr < graph.length; cr++){
                if (available[cr])
                    break;
            }

            result[u] = cr; // Assign the found color

            // Reset the values back to true for the next iteration
            Arrays.fill(available, true);
        }


        return IntStream.of(result).max().getAsInt();
    }
}
