package course02._14._07_Dijkstras_algorithm_implementation.practice;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

public class DijkstraPractice {

	public static void main(String[] args) {

		GraphPractice graph = new AdjacencyMatrixGraphPractice(8, GraphPractice.GraphPracticeType.DIRECTED);
		
		graph.addEdge(2, 7, 4);
		graph.addEdge(0, 3, 2);
		graph.addEdge(0, 4, 2);
		graph.addEdge(0, 1, 1);
		graph.addEdge(2, 1, 3);
		graph.addEdge(1, 3, 2);
		graph.addEdge(3, 5, 1);
		graph.addEdge(3, 6, 3);
		graph.addEdge(4, 7, 2);
		graph.addEdge(7, 5, 4);
		
		shortestPath(graph, 0, 5);
	}
	
	/**
	 * @param graph
	 * @param source
	 * @return
	 */
	public static Map<Integer, DistanceInfoPractice> buildDistanceTable(GraphPractice graph,int source) 
	{
		Map<Integer, DistanceInfoPractice> distanceTable = new HashMap<Integer, DistanceInfoPractice>();
		//Create temporary Map for Vertex to track 
		Map<Integer, VertexInfoPractice> vertexInfoMap = new HashMap<>();
				
		//Initialize distance table
		for (int i=0; i < graph.getNumVertices(); i++){
			distanceTable.put(i, new DistanceInfoPractice());
		}
		
		//set source distance and vertex 
		distanceTable.get(source).setDistance(0);
		distanceTable.get(source).setLastVertex(source);
		
		PriorityQueue<VertexInfoPractice> queue = new PriorityQueue<>(11, 
				new Comparator<VertexInfoPractice>() 
				{
					@Override
					public int compare(VertexInfoPractice v1, VertexInfoPractice v2)
					{
						 return ((Integer) v1.getDistance()).compareTo(v2.getDistance());
					}
				});
		
		//Create Source VertexInfoPractice
		VertexInfoPractice sourceVertexInfo = new VertexInfoPractice(source, 0);
				
		vertexInfoMap.put(source, sourceVertexInfo);
		queue.add(sourceVertexInfo);
		
		while(!queue.isEmpty())
		{
			VertexInfoPractice vertexInfo = queue.poll();
			int currentVertex = vertexInfo.getVertexId();
			
			for(Integer neighbour: graph.getAdjacentVertices(currentVertex))
			{
				//get the distance of neighbour from current vertex 
				int distance = distanceTable.get(currentVertex).getDistance() + graph.getWeightedEdge(currentVertex, neighbour);
				
				// If we find a new shortest path to the neighbour update
                // the distance and the last vertex.
				if(distanceTable.get(neighbour).getDistance() > distance)
				{
					distanceTable.get(neighbour).setDistance(distance);
					distanceTable.get(neighbour).setLastVertex(currentVertex);
					
					// We've found a new short path to the neighbour so remove
                    // the old node from the priority queue.
					VertexInfoPractice neighbourVertexInfo = vertexInfoMap.get(neighbour);
					if (neighbourVertexInfo != null) {
						queue.remove(neighbourVertexInfo);
					}
					
					// Add the neighbour back with a new updated distance.
					neighbourVertexInfo = new VertexInfoPractice(neighbour, distance);
					queue.add(neighbourVertexInfo);
					vertexInfoMap.put(neighbour, neighbourVertexInfo);
				}
			}
		}
		
		return distanceTable;
		
		/*Map<Integer, DistanceInfoPractice> distanceTable = new HashMap<>();
		//Create temporary Map for Vertex to track 
		Map<Integer, VertexInfoPractice> vertexInfoMap = new HashMap<>();
		
		//Initialize Distance Table
		for (int i = 0; i <graph.getNumVertices(); i++)
		{
			distanceTable.put(i, new DistanceInfoPractice());
		}
		//Set the initial data for source 
		distanceTable.get(source).setLastVertex(source);
		distanceTable.get(source).setDistance(0);
		
		PriorityQueue<VertexInfoPractice> queue = new PriorityQueue<VertexInfoPractice>(11, 
			new Comparator<VertexInfoPractice>() 
			{
					@Override
					public int compare(VertexInfoPractice v1, VertexInfoPractice v2)
					{
						 return ((Integer) v1.getDistance()).compareTo(v2.getDistance());
					}
			});
		
		//Create Source VertexInfoPractice
		VertexInfoPractice sourceVertexInfo = new VertexInfoPractice(source, 0);
		
		//add to queue
		queue.add(sourceVertexInfo);
		vertexInfoMap.put(source, sourceVertexInfo);
		
		while (!queue.isEmpty())
		{
			VertexInfoPractice vertexInfo = queue.poll();
			int currentVertex = vertexInfo.getVertexId();
			
			for (Integer neighbour: graph.getAdjacentVertices(currentVertex))
			{
				// Get the new distance, account for the weighted edge.
				int distance = distanceTable.get(currentVertex).getDistance() + graph.getWeightedEdge(currentVertex, neighbour);
				
				// If we find a new shortest path to the neighbour update
                // the distance and the last vertex.
				if(distanceTable.get(neighbour).getDistance() > distance)
				{
					distanceTable.get(neighbour).setDistance(distance);
					distanceTable.get(neighbour).setLastVertex(currentVertex);
					
					// We've found a new short path to the neighbour so remove
                    // the old node from the priority queue.
					VertexInfoPractice neighbourVertexInfo = vertexInfoMap.get(neighbour);
					if (neighbourVertexInfo != null) {
						queue.remove(neighbourVertexInfo);
					}
					// Add the neighbour back with a new updated distance.
					neighbourVertexInfo = new VertexInfoPractice(neighbour, distance);
					queue.add(neighbourVertexInfo);
					vertexInfoMap.put(neighbour, neighbourVertexInfo);
				}
			}
		}
		return distanceTable;*/
	}
	
	public static void shortestPath(GraphPractice graph, Integer source, Integer destination) {
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
            System.out.println(" Dijkstra  DONE!");
        }
    }

}
