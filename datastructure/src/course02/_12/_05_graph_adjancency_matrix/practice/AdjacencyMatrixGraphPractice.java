package course02._12._05_graph_adjancency_matrix.practice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdjacencyMatrixGraphPractice implements GraphPractice {

	private int[][] adjacencyMatrix;
	private GraphPracticeType graphType = GraphPracticeType.DIRECTED;
	private int numVertices = 0;

	public AdjacencyMatrixGraphPractice(int numVertices,
			GraphPracticeType graphType) 
	{				
		this.numVertices = numVertices;
		this.graphType = graphType;
		this.adjacencyMatrix = new int[numVertices][numVertices];
		
		for (int i=0; i < numVertices; i ++)
		{
			for (int j =0 ; j < numVertices; j++)
			{
				this.adjacencyMatrix[i][j] = 0;
			}
		}
	}

	public void addEdge(int v1, int v2) 
	{		
		if(v1 <0 || v1 >= numVertices || v2<0 || v2 >=numVertices)
		{
			throw new IllegalArgumentException("Vertex number is not valid");
		}
		this.adjacencyMatrix[v1][v2]=1;
		if(this.graphType == GraphPracticeType.UNDIRECTED)
		{
			this.adjacencyMatrix[v2][v1] = 1;
		}
	}

	public List<Integer> getAdjacentVertices(int v) 
	{				
		if(v >= numVertices || v <0)
		{
			throw new IllegalArgumentException("Vertex number is not valid");
		}
		
		List<Integer> adjacentVerticesList = new ArrayList<>();
		
		for (int i=0; i < numVertices; i++){
			if(this.adjacencyMatrix[v][i] ==1){
				adjacentVerticesList.add(i);
			}
		}
		Collections.sort(adjacentVerticesList);
		return adjacentVerticesList;
	}

}
