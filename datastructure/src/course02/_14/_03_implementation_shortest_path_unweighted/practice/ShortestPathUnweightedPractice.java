package course02._14._03_implementation_shortest_path_unweighted.practice;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;


public class ShortestPathUnweightedPractice {
	
	public static void main(String[] args) {
		
		GraphPractice graph = new AdjacencyMatrixGraphPractice(8, GraphPractice.GraphPracticeType.DIRECTED);
		
		graph.addEdge(2, 7);
		graph.addEdge(3, 0);
		graph.addEdge(0, 4);
		graph.addEdge(0, 1);
		graph.addEdge(2, 1);
		graph.addEdge(1, 3);
		graph.addEdge(3, 5);
		graph.addEdge(6, 3);
		graph.addEdge(4, 7);
        
		//System.out.println(buildDistanceTable(graph, 1));
		
		shortestPath(graph, 1, 7);
	}
	
	private static Map<Integer, DistanceInfoPractice> buildDistanceTable(GraphPractice graph, int source) 
	{
		
		/*Map<Integer, DistanceInfoPractice> distanceTable = new HashMap<Integer, DistanceInfoPractice>();
		
		//Initialize the Distance table for each vertex
		for (int i = 0 ; i < graph.getNumVertices(); i++)
		{
			distanceTable.put(i, new DistanceInfoPractice());
		}
		
		//set source vertex and distance 
		distanceTable.get(source).setDistance(0);
		distanceTable.get(source).setLastVertex(source);
		
		//Initialize the queue for traversing in a breadth first manner
		LinkedList<Integer> queue = new LinkedList<>();
		queue.add(source);
		
		while (!queue.isEmpty())
		{
			int currentVertex = queue.pollFirst();
			//Get Adjacents
			List<Integer> list = graph.getAdjacentVertices(currentVertex);
			
			for (int v: list)
			{
				//get the current distrance 
				int currentDistance = distanceTable.get(v).getDistance();
				
				//if current distance is -1 i.e never visited
				if(currentDistance == -1)
				{
					//get distance 
					 currentDistance= 1 + distanceTable.get(currentVertex).getDistance();
					 //set distance and vertex
					 distanceTable.get(v).setDistance(currentDistance);
					 distanceTable.get(v).setLastVertex(currentVertex);
					 
					 //check if the vertex has adjacents or not -if it has adjacent then add to queue for 
					 //further explore
					 if(!graph.getAdjacentVertices(v).isEmpty())
					 {
						 queue.add(v);
					 }
				}
			}
		}*/
		Map<Integer, DistanceInfoPractice> distanceTable = new HashMap<Integer, DistanceInfoPractice>();
		
		for (int i =0; i < graph.getNumVertices(); i++)
		{
			distanceTable.put(i, new DistanceInfoPractice());
		}
		
		distanceTable.get(source).setDistance(0);
		distanceTable.get(source).setLastVertex(source);
		
		//Initialize the queue for traversing in a breadth first manner
		LinkedList<Integer> queue = new LinkedList<>();
		
		queue.add(source);
		
		while(!queue.isEmpty())
		{
			int currentVertex = queue.pollFirst();
			//Get Adjacents
			List<Integer> list = graph.getAdjacentVertices(currentVertex);
			
			for (int v: list)
			{
				int currentDistance = distanceTable.get(v).getDistance();
				//if current distance is -1 i.e never visited
				if(currentDistance == -1)
				{
					//get distance 
					 currentDistance= 1 + distanceTable.get(currentVertex).getDistance();
					 //set distance and vertex
					 distanceTable.get(v).setDistance(currentDistance);
					 distanceTable.get(v).setLastVertex(currentVertex);
					 
					//check if the vertex has adjacents or not -if it has adjacent then add to queue for 
					 //further explore
					 if(!graph.getAdjacentVertices(v).isEmpty())
					 {
						 queue.add(v);
					 }
				}
			}
		}
				
		return distanceTable;
	}
	
	public static void shortestPath(GraphPractice graph, int source, int destination) {
		
		Map<Integer, DistanceInfoPractice> distanceTable = buildDistanceTable(graph, source);
		
		Stack<Integer> stack = new Stack<Integer>();
        stack.push(destination);
        
        int previousVertex = distanceTable.get(destination).getLastVertex();
        while (previousVertex != -1 && previousVertex != source)
        {
        	stack.push(previousVertex);
        	previousVertex = distanceTable.get(previousVertex).getLastVertex();
        }
        
        if (previousVertex == -1) {
            System.out.println("There is no path from node: " + source
                    + " to node: " + destination);
        }
        else {
            System.out.print("Smallest path is " + source);
            while (!stack.isEmpty()) {
                System.out.print(" -> " +stack.pop());
            }
            System.out.println(" Shortest Path Unweighted DONE!");
        }
		
	}

}
