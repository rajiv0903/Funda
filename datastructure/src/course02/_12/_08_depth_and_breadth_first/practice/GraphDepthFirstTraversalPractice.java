package course02._12._08_depth_and_breadth_first.practice;

import java.util.List;

import course02._12._08_depth_and_breadth_first.practice.GraphPractice.GraphPracticeType;

public class GraphDepthFirstTraversalPractice {

	private static int N = 8;

	public static void main(String[] args) {

		GraphPractice graph = new AdjacencyMatrixGraphPractice(N, GraphPracticeType.DIRECTED);

		graph.addEdge(1, 0);
		graph.addEdge(1, 2);
		graph.addEdge(2, 7);
		graph.addEdge(2, 4);
		graph.addEdge(2, 3);
		graph.addEdge(1, 5);
		graph.addEdge(5, 6);
		graph.addEdge(6, 3);
		graph.addEdge(3, 4);
		
		//Initialize visited vertex 
		int[] visited = new int[N];
		for (int i =0; i <N; i++)
		{
			visited[i] = 0;
		}
		
		// This for-loop ensures that all nodes are covered even for an unconnected
        // graph.
		for (int i =0; i < N; i++)
		{
			depthFirstTraversal(graph, visited, i);
		}
	}
	
	public static void depthFirstTraversal(GraphPractice graph, int[] visited, int currentVertex) {
		
		if(visited[currentVertex] ==1)
			return;
		
		visited[currentVertex]=1;
		
		List<Integer> list = graph.getAdjacentVertices(currentVertex);
		for (int vertex: list)
		{
			depthFirstTraversal(graph, visited, vertex);
		}
		
		System.out.print(currentVertex+"->");
	}

}
