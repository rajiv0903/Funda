package course02._12._05_graph_adjancency_matrix.practice;

import course02._12._05_graph_adjancency_matrix.practice.GraphPractice.GraphPracticeType;

public class AdjacencyMatrixGraphPracticeDemo {
	
	public static void main(String[] args) 
	{
		
		AdjacencyMatrixGraphPractice undirectedGraph =
				new AdjacencyMatrixGraphPractice(10, GraphPracticeType.UNDIRECTED);
		
		undirectedGraph.addEdge(1, 0);
		undirectedGraph.addEdge(1, 2);
		undirectedGraph.addEdge(2, 7);
		undirectedGraph.addEdge(2, 4);
		undirectedGraph.addEdge(2, 3);
		undirectedGraph.addEdge(1, 5);
		undirectedGraph.addEdge(5, 6);
		undirectedGraph.addEdge(6, 3);
		undirectedGraph.addEdge(3, 4);
		
		System.out.println(undirectedGraph.getAdjacentVertices(1));
		
		AdjacencyMatrixGraphPractice directedGraph =
				new AdjacencyMatrixGraphPractice(10, GraphPracticeType.UNDIRECTED);
		
		directedGraph.addEdge(1, 0);
		directedGraph.addEdge(1, 2);
		directedGraph.addEdge(2, 7);
		directedGraph.addEdge(2, 4);
		directedGraph.addEdge(2, 3);
		directedGraph.addEdge(1, 5);
		directedGraph.addEdge(5, 6);
		directedGraph.addEdge(6, 3);
		directedGraph.addEdge(3, 4);
		
		System.out.println(directedGraph.getAdjacentVertices(1));
	}

}
