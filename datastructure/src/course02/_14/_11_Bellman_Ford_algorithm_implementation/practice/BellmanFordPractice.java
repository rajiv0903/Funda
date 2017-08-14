package course02._14._11_Bellman_Ford_algorithm_implementation.practice;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Stack;



public class BellmanFordPractice {

	public static void main(String[] args) {

		GraphPractice graph1 = new AdjacencyMatrixGraphPractice(8, GraphPractice.GraphPracticeType.DIRECTED);
		graph1.addEdge(2, 7, 4);
		graph1.addEdge(0, 3, 23);
		graph1.addEdge(0, 4, 23);
		graph1.addEdge(0, 1, 1);
		graph1.addEdge(2, 1, 3);
		graph1.addEdge(1, 3, 2);
		graph1.addEdge(3, 5, 1);
		graph1.addEdge(3, 6, 3);
		graph1.addEdge(4, 7, 2);
		graph1.addEdge(7, 5, 4);

		// shortestPath(graph1, 0, 5);

		GraphPractice graph2 = new AdjacencyMatrixGraphPractice(5, GraphPractice.GraphPracticeType.DIRECTED);
		graph2.addEdge(0, 1, 2);
		graph2.addEdge(0, 2, 1);
		graph2.addEdge(1, 3, 3);
		graph2.addEdge(1, 4, -2);
		graph2.addEdge(2, 4, 2);
		graph2.addEdge(4, 3, 1);
		graph2.addEdge(2, 1, -5);

		// shortestPath(graph2, 0, 3);

		GraphPractice graph3 = new AdjacencyMatrixGraphPractice(5, GraphPractice.GraphPracticeType.DIRECTED);
		graph3.addEdge(0, 1, 2);
		graph3.addEdge(0, 2, 3);
		graph3.addEdge(3, 1, 2);
		graph3.addEdge(1, 4, -5);
		graph3.addEdge(2, 4, 6);
		graph3.addEdge(4, 3, -4);

		// shortestPath(graph3, 0, 3);

		GraphPractice graph4 = new AdjacencyMatrixGraphPractice(6, GraphPractice.GraphPracticeType.DIRECTED);
		graph4.addEdge(0, 1, 2);
		graph4.addEdge(1, 2, 3);
		graph4.addEdge(2, 3, 2);
		graph4.addEdge(3, 4, -5);
		graph4.addEdge(3, 5, 1);
		graph4.addEdge(4, 5, -3);
		graph4.addEdge(5, 4, -3);

		shortestPath(graph4, 0, 5);
	}
	
	
	
	public static Map<Integer, DistanceInfoPractice> buildDistanceTable(GraphPractice graph, int source) 
	{
		Map<Integer, DistanceInfoPractice> distanceTable = new HashMap<>();
		
		//first initialize the distance for each vertex 
		for (int i = 0; i < graph.getNumVertices(); i ++){
			distanceTable.put(i, new DistanceInfoPractice());
		}
		
		// Set up the distance of the specified source.
        distanceTable.get(source).setDistance(0);
        distanceTable.get(source).setLastVertex(source);
        
        LinkedList<Integer> queue = new LinkedList<>();
        // Relaxing (processing) all the edges numVertices - 1 times
        for (int numIterations = 0; numIterations < graph.getNumVertices() - 1; numIterations++) 
        {
            // Add every vertex to the queue so we're sure to access all the edges
            // in the graph.
            for (int v = 0; v < graph.getNumVertices(); v++)
            {
                queue.add(v);
            }

            // Keep track of the edges visited so we visit each edge just once
            // in every iteration.
            Set<String> visitedEdges = new HashSet<>();
            while (!queue.isEmpty()) 
            {
                int currentVertex = queue.pollFirst();

                for (int neighbor : graph.getAdjacentVertices(currentVertex)) {
                    String edge = String.valueOf(currentVertex) + String.valueOf(neighbor);

                    // Do not visit edges more than once in each iteration.
                    if (visitedEdges.contains(edge)) {
                        continue;
                    }
                    visitedEdges.add(edge);

                    int distance = distanceTable.get(currentVertex).getDistance()
                            + graph.getWeightedEdge(currentVertex, neighbor);
                    // If we find a new shortest path to the neighbour update
                    // the distance and the last vertex.
                    if (distanceTable.get(neighbor).getDistance() > distance) {
                        distanceTable.get(neighbor).setDistance(distance);
                        distanceTable.get(neighbor).setLastVertex(currentVertex);
                    }
                }
            }
        }
        
        // Add all the vertices to the queue one last time to check for
        // a negative cycle.
        for (int v = 0; v < graph.getNumVertices(); v++) {
            queue.add(v);
        }
        
        // Relaxing (processing) all the edges one last time to check if
        // there exists a negative cycle
        while (!queue.isEmpty()) 
        {
            int currentVertex = queue.pollFirst();
            for (int neighbor : graph.getAdjacentVertices(currentVertex)) {
                int distance = distanceTable.get(currentVertex).getDistance()
                        + graph.getWeightedEdge(currentVertex, neighbor);
                if (distanceTable.get(neighbor).getDistance() > distance) {
                    throw new IllegalArgumentException("The Graph has a -ve cycle");
                }
            }
        }
		
		return distanceTable;
	}
	
	 public static void shortestPath(GraphPractice graph, Integer source, Integer destination) 
	 {
	        Map<Integer, DistanceInfoPractice> distanceTable = buildDistanceTable(graph, source);

	        Stack<Integer> stack = new Stack<>();
	        stack.push(destination);

	        int previousVertex = distanceTable.get(destination).getLastVertex();
	        while (previousVertex != -1 && previousVertex != source) {
	            stack.push(previousVertex);
	            previousVertex = distanceTable.get(previousVertex).getLastVertex();

	        }

	        if (previousVertex == -1) {
	            System.out.println("There is no path from node: " + source
	                    + " to node: " + destination);
	        }
	        else {
	            System.out.print("Smallest Path is " + source);
	            while (!stack.isEmpty()) {
	                System.out.print(" -> " +stack.pop());
	            }
	            System.out.println(" BellmanFord DONE!");
	        }

	    }

}
