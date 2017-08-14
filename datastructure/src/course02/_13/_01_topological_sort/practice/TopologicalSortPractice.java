package course02._13._01_topological_sort.practice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TopologicalSortPractice {

	public static void main(String[] args) 
	{
		GraphPractice graph = new AdjacencyMatrixGraphPractice(8,
				GraphPractice.GraphPracticeType.DIRECTED);
		graph.addEdge(2, 7);
		graph.addEdge(0, 3);
		graph.addEdge(0, 4);
		graph.addEdge(0, 1);
		graph.addEdge(2, 1);
		graph.addEdge(1, 3);
		graph.addEdge(3, 5);
		graph.addEdge(3, 6);
		graph.addEdge(4, 7);

		printList(sort(graph));
	}

	public static void printList(List<Integer> list) 
	{
		for (int v : list) {
			System.out.print("-> " + v  );
		}
	}

	public static List<Integer> sort(GraphPractice graph)
    {
		//Create a list to store the each visited vertex 
		List<Integer> sortedList = new ArrayList<>();
		//Create a queue to process each vertex 
		LinkedList<Integer> queue = new LinkedList<Integer>();
		//Store and Update indegree map 
		Map<Integer, Integer> indegreeMap = new HashMap<Integer, Integer>();
		//Let's build the indegree map and also queued the vertex having 0
		for (int i=0; i < graph.getNumVertices(); i++){
			int indegree = graph.getIndegree(i);
			indegreeMap.put(i, indegree);
			if(indegree == 0){
				queue.add(i);
			}
		}
		while(!queue.isEmpty())
		{
			int vertex = queue.pollFirst();
			//add visited vertex
			sortedList.add(vertex);
			//get adjacent vertices
			List<Integer> adjacents = graph.getAdjacentVertices(vertex);
			//iterate through adjacent and update the indegree and if indegree is 0 
			//then add to queue for processing
			for (int v: adjacents)
			{
				int indegree = indegreeMap.get(v);
				//update indegree 
				int updatedIndegree= indegree -1;
				indegreeMap.remove(v);
				indegreeMap.put(v, updatedIndegree);
				if(updatedIndegree ==0){
					queue.add(v);
				}
			}
		}
		
		//check whether graph has cycle or not 
		if(sortedList.size() != graph.getNumVertices()){
			 throw new RuntimeException("The Graph had a cycle!");
		}
		
		return sortedList;
		
    }
}
